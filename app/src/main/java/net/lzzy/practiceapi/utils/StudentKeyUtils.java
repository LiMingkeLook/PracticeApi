package net.lzzy.practiceapi.utils;

import androidx.core.util.Pair;

import net.lzzy.practiceapi.network.ApiService;

import org.json.JSONException;
import org.json.JSONObject;

public class StudentKeyUtils {
    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_USER_END = "ROLE_USER_END";
    public static final String CONTENT = "content";
    private static String SEPARATOR = "FFFF";
    private static String CONTENT_END = "CONTENT_END";
    private static String TIME_END = "TIME_END";

    /**
     * 加密
     * @param content 登录加密的json
     * @return 已加密的json请求数据
     */
    public static String encryptionRequest(String content) throws JSONException {
        /*StringBuilder sb = new StringBuilder();
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
                    contents[i] = ch[i] + timeMillis[i] ;
                } else if (i == ch.length - 1) {
                    contents[i] = ch[i] + CONTENT_END + timeMillis[i] ;
                } else {
                    contents[i] = timeMillis[i] ;
                }
            }
        } else if (timeMillis.length < ch.length) {
            contents = new String[ch.length];
            for (int i = 0; i < ch.length; i++) {
                if (i < timeMillis.length - 1) {
                    contents[i] = ch[i] + timeMillis[i] ;
                } else if (i == timeMillis.length - 1) {
                    contents[i] = ch[i] + TIME_END + timeMillis[i] ;
                } else {
                    contents[i] = ch[i];
                }
            }
        } else {
            contents = new String[ch.length];
            for (int i = 0; i < ch.length; i++) {
                contents[i] = ch[i] + timeMillis[i] ;
            }
        }
        for (int i = 0; i < contents.length; i++) {
            if (i != contents.length - 1) {
                sb.append(contents[i] + SEPARATOR);
            } else {
                sb.append(contents[i]);
            }
        }
        JSONObject object=new JSONObject();
        object.put(CONTENT,sb.toString());
        return object.toString();*/
        return content;
    }

    /**
     * 解密
     * @param bodyRequest 用户请求体
     * @return Pair String[0]=返回的参数。String[1]=返回的时间；
     * @throws Exception
     */
    public static Pair<String, Long> decodeResponse(String bodyRequest) {
        /*JSONObject object= null;
        try {
            object = new JSONObject(bodyRequest);
            String convertContent=object.getString(CONTENT);
            String[] chars = convertContent.split(SEPARATOR);
            StringBuilder sb = new StringBuilder();
            StringBuilder loginTimeSB = new StringBuilder();
            boolean contentEnd = false;
            boolean timeEnd = false;
            for (int i = 0; i < chars.length; i++) {
                String vi =chars[i] ;
                if (contentEnd) {
                    loginTimeSB.append(vi);
                } else if (timeEnd) {
                    sb.append((char) Integer.parseInt(vi));
                } else if (vi.contains(CONTENT_END)) {
                    contentEnd = true;
                    String[] strings = vi.split(CONTENT_END);
                    sb.append((char) Integer.parseInt(strings[0]));
                    loginTimeSB.append(strings[1]);
                } else if (vi.contains(TIME_END)) {
                    timeEnd = true;
                    String[] strings = vi.split(TIME_END);
                    sb.append((char) Integer.parseInt(strings[0]));
                    loginTimeSB.append(strings[1]);
                } else {
                    String content = vi.substring(0, vi.length() - 1);
                    String loginTime = vi.replace(content,"");
                    sb.append((char) Integer.parseInt(content));
                    loginTimeSB.append(loginTime);
                }
            }
            String content=sb.toString();
            Long time=Long.parseLong(loginTimeSB.toString());
            return new Pair<String, Long>(content,time) ;
        } catch (JSONException e) {
            e.printStackTrace();
            return new Pair<String, Long>(bodyRequest,System.currentTimeMillis()) ;
        }*/
        return new Pair<String, Long>(bodyRequest,System.currentTimeMillis()) ;


    }

    public static void main(String[] args){
       /* JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("iphone","17777581901");
            jsonObject.put("password","1");
            jsonObject.put("user", "student");
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        String body= null;
        try {
            body = encryptionRequest("{\"iphone\":\"17777581901\"}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(body);

        /*String[] requests= new String[0];
        try {
            requests = convertBodyReqest(body);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < requests.length; i++) {
            System.out.println(requests[i]);
        }*/
    }
}
