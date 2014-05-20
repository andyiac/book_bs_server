package com.androidbook.utils;

public class Utils {
	
	public static boolean IsEmpty(String friends) {
		if(friends==null || friends.equals("")){
			return true;
		}
		return false;
	}
	public static String pad(String field){
		StringBuffer sb = new StringBuffer();
		sb.append("'").append(field).append("'");
		return sb.toString();
		
	}
}
