package controllers;

import com.galenframework.api.Galen;
import com.galenframework.reports.model.LayoutReport;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codeborne.selenide.Screenshots.getLastScreenshot;
import static com.codeborne.selenide.Selenide.screenshot;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.junit.jupiter.api.Assertions.fail;

public class GalenController {
    protected static final String SPECS_DIR_PATH = "src/test/resources/specs/";
    protected static final String IMG_DIFF_PATH = "build/results-img/";

    /**
     * compare current page with spec file
     *
     * @param spec spec file name
     * @param tag  mobile/desktop/tablet
     */
    @Step("Проверка на соответствие спецификации {0}")
    public void compareCurrentPageWithSpec(String spec, String tag) {
        List<String> tags = new ArrayList<>();
        tags.add(tag);
        checkLayoutAccordingToSpec(spec, tags);
        AllureLogger.info(String.format("Страница/блок соответствует спецификации %s для экрана %s", spec, tag));
    }

    public void checkLayoutAccordingToSpec(String spec, List<String> tags) {
        try {
            LayoutReport report = Galen.checkLayout(getWebDriver(), SPECS_DIR_PATH + spec, tags);
            report.getFileStorage().copyAllFilesTo(new File(IMG_DIFF_PATH));
            if (report.errors() > 0) {
                embedScreenshotAndFail(report);
            }
        } catch (IOException e) {
            AllureLogger.getStackTrace(e);
            AllureLogger.error("Не удалось открыть файл");
        }
    }

    /**
     * Adds screenshots, sets the status to failed
     *
     * @param report
     */
    private void embedScreenshotAndFail(LayoutReport report) {
        Map<String, File> screenshots = report.getFileStorage().getFiles();
        screenshots.forEach((key, value) -> {
            if (key.contains("map") || key.contains("expected") || key.contains("actual")) {
                embedFileToReport(key, value);
            }
        });

        screenshot("Page-screenshot");
        embedFileToReport("Screenshot", getLastScreenshot());

        fail(onError(report.getValidationErrorResults().toString()));
    }

    /**
     * @param key          name for attachment
     * @param fileToAttach attachment
     * @return attachment as byte array
     */
    @Attachment("{0}")
    public byte[] embedFileToReport(String key, File fileToAttach) {
        try {
            return FileUtils.readFileToByteArray(fileToAttach);
        } catch (IOException e) {
            AllureLogger.error("Скриншот не найден");
            return new byte[0];
        }
    }

    public String onError(String message){
        String newLine = System.getProperty("line.separator");
        String regex = "Error\\{\\[(.*?)\\]";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(message);
// compile errors to multiline string
        StringBuilder fullResult = new StringBuilder();
        while (matcher.find()) {
            fullResult.append(matcher.group(1));
            fullResult.append(newLine);
        }
        Allure.addAttachment("Full Errors list", fullResult.toString());
// remove duplicates
        Set<String> tokens = new HashSet<String>(Arrays.asList(fullResult.toString().split("\n")));
        StringBuilder resultBuilder = new StringBuilder();
        boolean first = true;
        for(String token : tokens) {
            if(first) first = false;
            else resultBuilder.append("\n");
            resultBuilder.append(token);
        }
        return resultBuilder.toString();
    }
}