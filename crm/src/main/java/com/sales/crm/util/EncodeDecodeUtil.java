package com.sales.crm.util;

import java.util.Base64;

public class EncodeDecodeUtil {
	
	public static String encodeNumberToString(int number) {
		return Base64.getEncoder().withoutPadding().encodeToString(String.valueOf(number*2).getBytes());
	}
	
	public static int decodeStringToNumber(String encodedString) {
		byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
		String decodedString = new String(decodedBytes);
		return Integer.valueOf(decodedString)/2;
	}
	
	public static void main(String a[]) {
		String encodedString = encodeNumberToString(1000);
		System.out.println(encodedString);
		System.out.println(decodeStringToNumber(encodedString));
	}

}
