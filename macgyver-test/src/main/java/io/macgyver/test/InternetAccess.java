package io.macgyver.test;

import java.net.*;
public class InternetAccess {

	public static boolean isPublicDNSAvailable() {
		try {
			InetAddress.getByName("www.google.com");
			return true;
		}
		catch (Exception e) {
		
		}
		return false;
	}
}
