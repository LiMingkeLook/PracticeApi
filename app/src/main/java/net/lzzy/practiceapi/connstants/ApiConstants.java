package net.lzzy.practiceapi.connstants;

public class ApiConstants {

    public static final String GET_QUESTION = "/Practice/api/get_question";

    public static String getPracticeUrl(){
        return HTTP+IP_DELIMITER_PORT+ GET_PRACTICE;
    }
    private static String IP_DELIMITER_PORT="luoxiutong.com:8080";
    private static final String HTTP="http://";
    private static final String GET_PRACTICE ="/Practice/api/get_practice";
    private static final String GET_REGISTER="/Practice/api/post_register";

    public static String getIpDelimiterPort() {
        return IP_DELIMITER_PORT;
    }

    public static void setIpDelimiterPort(String ipDelimiterPort) {
        IP_DELIMITER_PORT = ipDelimiterPort;
    }


    public static String getQuestionUrl() {
        return  HTTP+IP_DELIMITER_PORT+ GET_QUESTION;
    }

    public static String getRegisterUrl() {
        return  HTTP+IP_DELIMITER_PORT+ GET_REGISTER;
    }
}

