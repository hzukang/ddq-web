package ddq.common;

import java.io.UnsupportedEncodingException;

public class StringUtil {

	/**
	 * 繁体中文转简体中文
	 */
	public static String big5ToChinese(String s){
		try{
			if(s==null||s.equals("")) return "";
			String newstring=null;
			newstring=new String(s.getBytes("big5"),"gb2312");
			return newstring;
			
		}
		catch(UnsupportedEncodingException e)
		{
			return s;
		}
	}

	/**
	 * 简体中文转繁体中文
	 */
	public static String ChineseTobig5(String s){
		try{
			if(s==null||s.equals("")) return "";
			String newstring=null;
			newstring=new String(s.getBytes("gb2312"),"big5");
			return newstring;
		}
		catch(UnsupportedEncodingException e)
		{
			return s;
		}
	}

	/**
	 * 半角转全角
	 * @param input String.
	 * @return 全角字符串.
	 */
	public static String ToSBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000';
			} else if (c[i] < '\177') {
				c[i] = (char) (c[i] + 65248);

			}
		}
		return new String(c);
	}

	/**
	 * 全角转半角
	 * @param input String.
	 * @return 半角字符串
	 */
	public static String ToDBC(String input) {


		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);

			}
		}
		String returnString = new String(c);

		return returnString;
	}

}
