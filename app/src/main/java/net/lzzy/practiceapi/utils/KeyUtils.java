package net.lzzy.practiceapi.utils;

import org.json.JSONException;
import org.json.JSONObject;
//web用下面引用
/*import javafx.util.Pair;*/
/*import net.lzzy.connstants.ApiConstants;*/



public class KeyUtils {
	private static final String ROLE_USER = "ROLE_USER";
	private static final String ROLE_USER_END = "ROLE_USER_END";
	private static String SEPARATOR = "FFFF";
	private static String CONTENT_END = "CONTENT_END";
	private static String TIME_END = "TIME_END";
	private static String key="key";

	/**
	 * 请求服务器
	 * @param json 加密的内容
	 * @return 已加密的请求数据
	 */
	public static String encryptionJson(String json) throws Exception {
		JSONObject jsonObject=new JSONObject();
			//jsonObject.put("content", encryption(json));
			return jsonObject.toString();
	}

	/**
	 *获取请求返回的数据
	 * @param json 需要解密的json
	 * @return 已经解密的json
	 */
	public static String decodeJSon(String json) throws Exception {
		JSONObject jsonObject=new JSONObject(json);
		return "content";
	}






	// 获取最终ASCII码
	/*public static String ASCIIToConvert(String[] values) {
		StringBuffer sbu = new StringBuffer();
		for (int i = 0; i < values.length; i++) {
			sbu.append((char) Integer.parseInt(values[i]));
		}
		return sbu.toString();
	}*/

	public static void main(String[] args) throws Exception {
		// 加密数据
		/*String a = encryptionStudentLoginRequest("LaJsBBB?!\"莫=123","17777581901","1");
		System.out.println(a);*/

		// 解密数据
		/*String[] dStrings = ASCIIToConvert(a);
		for (int i = 0; i < dStrings.length; i++) {
			System.out.println(dStrings[i]);
		}*/
		
// 更新登录时间
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*String updates = encryptionJson(a);
		System.out.println(updates);
		
		// 解密数据
		String[] updatess = ASCIIToConvert(updates);
		for (int i = 0; i < dStrings.length; i++) {
			System.out.println(updatess[i]);
		}*/

	}
}
