package net.lzzy.practiceapi.connstants;

public class ApiConstants {

    public static final String GET_QUESTION = "/Practice/api/get_question";


    public static String getPracticeUrl(){
        return HTTP+IP_DELIMITER_PORT+ GET_PRACTICE;
    }
    private static String IP_DELIMITER_PORT="192.168.123.197:8080";
    private static final String HTTP="http://";
    private static final String GET_PRACTICE ="/Practice/api/get_practice";
    private static final String GET_REGISTER="/Practice/api/post_register";
    private static final String GET_ALL_TEACHER= "/Practice/api/get_all_teacher";
    private static final String GET_COURSE= "/Practice/api/get_course";
    private static final String COURSE_APPLIED= "/Practice/api/course_applied";
    private static final String DELETE_COURSE= "/Practice/api/post_delete_course";
    private static final String ADD_COURSE = "/Practice/api/post_add_course";
    private static final String POST_UPDATE_COURSE = "/Practice/api/post_update_course";
    private static final String ALL_STUDENT = "/Practice/api/get_all_student";
    private static final String SEARCH_STUDENT = "/Practice/api/search_student";
    private static final String GET_STUDENT_BY_COURSEID = "/Practice/api/get_student_by_courseId";
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
    public static String getTeacherUrl() {
        return  HTTP+IP_DELIMITER_PORT+ GET_ALL_TEACHER;
    }
    public static String getCourseUrl() {
        return  HTTP+IP_DELIMITER_PORT+ GET_COURSE;
    }
    public static String getCourseAppliedUrl() {
        return  HTTP+IP_DELIMITER_PORT+ COURSE_APPLIED;
    }
    public static String getDeleteCourseUrl() {
        return  HTTP+IP_DELIMITER_PORT+DELETE_COURSE ;
    }
    public static String addCourseUrl() {
        return  HTTP+IP_DELIMITER_PORT+ ADD_COURSE;
    }

    public static String getUpdateCourseUrl() {
        return HTTP+IP_DELIMITER_PORT+ POST_UPDATE_COURSE;
    }
    public static String getAllStudentUrl() {
        return HTTP+IP_DELIMITER_PORT+ ALL_STUDENT;
    }
    public static String getSearchStudentUrl() {
        return HTTP+IP_DELIMITER_PORT+ SEARCH_STUDENT;
    }
    public static String getStudentByCourseId() {
        return HTTP+IP_DELIMITER_PORT+ GET_STUDENT_BY_COURSEID;
    }


}

