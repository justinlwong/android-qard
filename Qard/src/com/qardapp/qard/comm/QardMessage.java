package com.qardapp.qard.comm;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QardMessage {
	public static int ID = 1;
	public static int FIRST_NAME = 2;
	public static int LAST_NAME = 3;
	public static int PHONE = 4;
	public static String pattern = "Q/(.*)/(.*)/(.*)/(.*)";
	
	public static String encodeMessage (String id, String first, String last, String phone) {
		return "Q/" + id +"/" + first + "/" + last + "/"+ phone;
	}
	
	public static ArrayList<String> decodeMessage (String msg) {
		Pattern pat = Pattern.compile(pattern);
		Matcher m = pat.matcher(msg);
		if (m.matches()) {
			ArrayList<String> values = new ArrayList<String>();
			values.add(0, msg);
			values.add(ID, m.group(ID));
			values.add(FIRST_NAME, m.group(FIRST_NAME));
			values.add(LAST_NAME, m.group(LAST_NAME));
			values.add(PHONE, m.group(PHONE));
			return values;
		}
		return null;
	}
}
