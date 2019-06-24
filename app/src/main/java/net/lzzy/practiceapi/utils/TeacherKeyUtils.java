package net.lzzy.practiceapi.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class TeacherKeyUtils {
    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_USER_END = "ROLE_USER_END";
    private static String SEPARATOR = "FFFF";
    private static String CONTENT_END = "CONTENT_END";
    private static String TIME_END = "TIME_END";
    private static String key = "key";

    /**
     * 教师登录请求专用
     * @param content 登录加密的json
     * @return 已加密的请求数据
     */
    public static String encryptionTeacherLoginRequest(String content, String iphone, String pwa) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        StringBuilder sb = new StringBuilder();
        String[] contents;
        char[] timeMillis1 = String.valueOf(System.currentTimeMillis()).toCharArray();
        String[] timeMillis = new String[timeMillis1.length];
        for (int i = 0; i < timeMillis1.length; i++) {
            timeMillis[i] = String.valueOf(timeMillis1[i]);
        }
        char[] ch1 = content.toCharArray();
        String[] ch = new String[ch1.length];
        for (int i = 0; i < ch1.length; i++) {
            ch[i] = String.valueOf(Integer.valueOf(ch1[i]).intValue());
        }
        if (timeMillis.length > ch.length) {
            contents = new String[timeMillis.length];
            for (int i = 0; i < timeMillis.length; i++) {
                if (i < ch.length - 1) {
                    contents[i] = ch[i] + timeMillis[i] + timeMillis[i];
                } else if (i == ch.length - 1) {
                    contents[i] = ch[i] + CONTENT_END + timeMillis[i] + timeMillis[i];
                } else {
                    contents[i] = timeMillis[i] + timeMillis[i];
                }
            }
        } else if (timeMillis.length < ch.length) {
            contents = new String[ch.length];
            for (int i = 0; i < ch.length; i++) {
                if (i < timeMillis.length - 1) {
                    contents[i] = ch[i] + timeMillis[i] + timeMillis[i];
                } else if (i == timeMillis.length - 1) {
                    contents[i] = ch[i] + TIME_END + timeMillis[i] + timeMillis[i];
                } else {
                    contents[i] = ch[i];
                }
            }
        } else {
            contents = new String[ch.length];
            for (int i = 0; i < ch.length; i++) {
                contents[i] = ch[i] + timeMillis[i] + timeMillis[i];
            }
        }
        // 添加分隔符
        String role = "{\"role\":\"teacher\",\"iphone\":\"" + iphone + "\",\"paw\":\"" + pwa + "\"}";
        char[] roles = role.toCharArray();
        StringBuilder roleSB = new StringBuilder();
        for (int i = 0; i < roles.length; i++) {
            roleSB.append(Integer.valueOf(ch1[i]).intValue());
            if (i == roles.length - 1) {
                roleSB.append(ROLE_USER_END);
            } else {
                roleSB.append(ROLE_USER);
            }
        }
        for (int i = 0; i < contents.length; i++) {
            if (i != contents.length - 1) {
                sb.append(contents[i] + SEPARATOR);
            } else {
                sb.append(contents[i]);
            }
        }
        jsonObject.put("content", roleSB.toString() + sb.toString());
        return jsonObject.toString();
    }
}
