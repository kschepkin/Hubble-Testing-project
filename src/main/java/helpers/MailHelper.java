package helpers;

import com.sun.mail.imap.protocol.FLAGS;
import controllers.AllureLogger;
import controllers.PropertyLoader;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.AndTerm;
import javax.mail.search.FlagTerm;
import javax.mail.search.SearchTerm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codeborne.selenide.Selenide.sleep;
import static controllers.PropertyLoader.loadProperty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class MailHelper {
    public static final Logger log = LogManager.getLogger(PropertyLoader.class);
    String header;
    String body;
    String html;
    Integer var;


    // Регулярки для поиска текста в письме
    private static List<Pattern> getPattern() {
        List<Pattern> emailPattern = new ArrayList<>();
        emailPattern.add(0, Pattern.compile("Это сообщение для ([^\"]*) от", Pattern.CASE_INSENSITIVE | Pattern.DOTALL));
        emailPattern.add(1, Pattern.compile("Это сообщение для ([^\"]*) от", Pattern.CASE_INSENSITIVE | Pattern.DOTALL));
        emailPattern.add(2, Pattern.compile(".+href=\"([^\"]*)\" class=\"es-button msohide", Pattern.CASE_INSENSITIVE | Pattern.DOTALL));
        return emailPattern;
    }

    @Step("Удаление всех писем")
    public static void clearMails(String userCode) throws Exception {
        AllureLogger.info("Удаляем все письма");
        String login = loadProperty(userCode + ".maillogin");
        String mailpassword = loadProperty(userCode + ".mailpassword");
        String mailserver = loadProperty(userCode + ".mailserver");

        // Соединение
        Properties props = new Properties();
        props.setProperty("mail.imap.ssl.enable", "true");

        // set any other needed mail.imap.* properties here
        Session session = Session.getInstance(props);
        Store store = session.getStore("imap");
        store.connect(mailserver, login, mailpassword);
        AllureLogger.debug("Соединение установлено");
        // переходим во входящие
        Folder folder = store.getFolder("INBOX");
        folder.open(Folder.READ_WRITE);

        Message[] msg = folder.getMessages();

        for (int i = 0; i < msg.length; i++) {
            msg[i].setFlag(FLAGS.Flag.DELETED, true);
        }

        folder.close(true);
        store.close();
        AllureLogger.info("Все письма удалены");
    }

    // Загружает параметры поиска и выполняет свитч по типу письма
    @Step("Получение входящих писем")
    public static helpers.MailHelper getMessages(String userCode, String type, int timeout) throws Exception {
        AllureLogger.info("Получаем список входящих писем");
        String login = loadProperty(userCode + ".maillogin");
        String mailpassword = loadProperty(userCode + ".mailpassword");
        String mailserver = loadProperty(userCode + ".mailserver");
        String forgotPass = loadProperty("MailTheme.forgotPassword");
        String register = loadProperty("MailTheme.register");
        String checkoutRegisterMail = loadProperty("MailTheme.checkoutRegister");
        helpers.MailHelper message = new helpers.MailHelper();
        message.var = 0;

        switch (type) {
            case "Восстановление пароля":
                message = getMail(login, mailpassword, mailserver, forgotPass, timeout);
                message.var = 2;
                return message;

            case "Регистрация":
                message = getMail(login, mailpassword, mailserver, userCode + register, timeout);
                message.var = 1;
                return message;

            case "Регистрация на чекауте":
                message = getMail(login, mailpassword, mailserver, checkoutRegisterMail, timeout);
                message.var = 0;
                return message;

            default:
                log.error("Тип письма определён неверно.");
                message.body = "Тип письма определён неверно.";
                message.header = "Тип письма определён неверно.";
                message.html = "Тип письма определён неверно.";
                return message;
        }
    }

    @Step("Поиск письма")
    private static helpers.MailHelper getMail(String username, String password, String mailserver, String search, int timeout) throws Exception {
        int time = 0;
        Folder folder;
        Flags seen = new Flags(Flags.Flag.SEEN);
        FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
        SearchTerm term;
        Message content = null;

        // Соединение
        Properties props = new Properties();
        props.setProperty("mail.imap.ssl.enable", "true");

        // set any other needed mail.imap.* properties here
        Session session = Session.getInstance(props);
        Store store = session.getStore("imap");
        store.connect(mailserver, username, password);
        AllureLogger.debug("Соединение установлено");
        // переходим во входящие

        while (time < timeout) {
            try {
                sleep(5000);
                folder = store.getFolder("INBOX");
                folder.open(Folder.READ_WRITE);

                // Поиск сообщения в папке входящие
                term = new SearchTerm() {
                    public boolean match(Message message) {
                        try {
                            if (message.getSubject().contains(search)) {
                                return true;
                            }
                        } catch (Exception MessagingException) {
                            return false;
                        }
                        return false;
                    }
                };
                AllureLogger.debug("Поиск сообщения: " + search);
                SearchTerm searchTerm = new AndTerm(unseenFlagTerm, term);
                Message[] foundMessages = folder.search(searchTerm);

                // Получаем 1 сообщение
                content = foundMessages[0];
                break;
            } catch (Exception e) {
                time = time + 5;
            }
        }
        // Создаем сущность в виде гномика
        helpers.MailHelper msg = new helpers.MailHelper();
        assert content != null;
        msg.header = content.getSubject();
        AllureLogger.info("Найдено письмо: " + msg.header);
        msg.body = getTextFromMessage(content);
        AllureLogger.debug("Текст письма: " + msg.body);
        msg.html = getHtmlSourceFromMessage(content);
        attachMailToReport("Код письма '" + msg.header + "'", msg.html);

        /* Еще раз получаем фолдер входящие, так как к этому моменту он теряется */
        folder = store.getFolder("INBOX");
        folder.open(Folder.READ_WRITE);
        /* Делаем все письма во входящих прочитанными и проставляем флаг "Deleted" */
        Message[] messages = folder.getMessages();    // Получение всех сообщений в папке "Входящие"
        folder.setFlags(messages, new Flags(Flags.Flag.SEEN), true);
        folder.setFlags(messages, new Flags(Flags.Flag.DELETED), true);
        AllureLogger.debug("Письма удалены");

        // Закрыть соединение
        folder.close(false);
        store.close();
        return msg;
    }

    @Step("Получение текста письма")
    private static String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        AllureLogger.debug("Тип контента письма: " + message.getContentType());
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private static String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
        String result = "";
        int count = mimeMultipart.getCount();

        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
            }
        }
        return result;
    }

    //  Возвращает данные из письма, ищет по регулярке
    private static ArrayList<String> getDataFromMessage(String message, int var) {
        ArrayList<String> finds = new ArrayList<>();
        Pattern searchPattern = getPattern().get(var);
        Matcher pageMatcher = searchPattern.matcher(message);
        while (pageMatcher.find()) {
            finds.add(pageMatcher.group(1));
        }
        return finds;
    }

    //    Возвращает html код письма
    @Step("Получение html исходника из письма")
    private static String getHtmlSourceFromMessage(Message message) throws MessagingException, IOException {
        AllureLogger.debug("Получаем исходный код письма с темой: " + message.getSubject());
//        MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
        String result = message.getContent().toString();
//        int count = mimeMultipart.getCount();
//        AllureLogger.debug("count mime: " + count);
//        for (int i = 0; i < count; i++) {
//            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
//            result = result + "\n" + bodyPart.getContent();
//            AllureLogger.debug("result "+i+": " + result);
//        }
        return result;
    }

    //        Проверка почтового адреса в тексте письма
    @Step("Проверка адреса эл.почты в письме")
    public static void checkEmailFromMessage(String mail, helpers.MailHelper msg) {
        String mailFromMessage = getDataFromMessage(msg.html, msg.var).get(0);
        assertThat(String.format("В тексте письма найден адрес [%s], который не совпадает с заданным [%s]", mailFromMessage,
                mail), mailFromMessage, containsString(mail));
    }

    @Step("Получение ссылки на восстановление пароля из письма")
    public static String getForgotUrlFromMessage(helpers.MailHelper msg) {
        String forgotUrl = getDataFromMessage(msg.html, msg.var).get(0);
        AllureLogger.info("Ссылка на восстановление пароля: " + forgotUrl);
        return forgotUrl;
    }


    /**
     * @param key         name for attachment
     * @param strToAttach attachment
     * @return attachment as byte array
     */
    @Attachment("{0}")
    public static byte[] attachMailToReport(String key, String strToAttach) {
        return strToAttach.getBytes();
    }
}
