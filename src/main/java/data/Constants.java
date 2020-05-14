package data;

public final class Constants {

    public static final int DEFAULT_TIMEOUT = 15000;

    public static final String TITTLE_HUBBLE = "\n" +
            "      .///      #%%#         \n" +
            "    ////////   %%%%%%%                 " + "                   ______   ______   _        _______ \n" +
            "  ,///////////   %%%%#,/               " + "|\\     /||\\     /|(  ___ \\ (  ___ \\ ( \\      (  ____ \\\n" +
            "  (////////////,,, /,/////             " + "| )   ( || )   ( || (   ) )| (   ) )| (      | (    \\/\n" +
            "   /////////*,,*///. ######(           " + "| (___) || |   | || (__/ / | (__/ / | |      | (__    \n" +
            "     /////*,,////////* ####            " + "|  ___  || |   | ||  __ (  |  __ (  | |      |  __)   \n" +
            "       //,,////////////.               " + "| (   ) || |   | || (  \\ \\ | (  \\ \\ | |      | (      \n" +
            "  #%%%, ,,////////////***              " + "| )   ( || (___) || )___) )| )___) )| (____/\\| (____/\\\n" +
            " #%%%%%%,,//////////****               " + "|/     \\|(_______)|/ \\___/ |/ \\___/ (_______/(_______/ \n" +
            "   %%%%/,/.#//////*****      \n" +
            "     /,/////. //*****        \n" +
            "       #######.#**           \n" +
            "         ####   ";

    public static final String TRANSLATE_URL = "/api/v1.5/tr.json/translate";
    public static final String ERROR404_URL = "/404";

    private Constants() {
        throw new IllegalStateException("Utility class");
    }
}
