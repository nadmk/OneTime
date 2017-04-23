package me.nickadam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

public class Generator {
	
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
		String privatekey = "test"; //PrivateKey
		 String phone = ""; //The recipient 
		 String username = ""; //user of your smsbox.gr account
		 String password = ""; //pass of your smsbox.gr account
		 String from = "me"; //sender
		 int year = Calendar.getInstance().get(Calendar.YEAR); 
		 int month = Calendar.getInstance().get(Calendar.MONTH); 
		 int day = Calendar.getInstance().get(Calendar.DATE);
		 int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY); 
		 int minute =  Calendar.getInstance().get(Calendar.MINUTE);
		 month++;
		
			 int fina = year + hour + month + day + minute;
			 String unhashedfina = Integer.toString(fina);
			 String unhashed = unhashedfina += privatekey;
			 byte[] bytesOfMessage = unhashed.getBytes("UTF-8");

			 MessageDigest md = MessageDigest.getInstance("MD5");
			 byte[] thedigest = md.digest(bytesOfMessage);
			 BigInteger number = new BigInteger(1, thedigest);
		        String hashtext = number.toString(16);
		        String substr = hashtext.substring(hashtext.length() - 6);
		        URL sms = new URL("http://www.smsbox.gr/httpapi/sendsms.php?username=" + username + "&password=" + password + "&from=" + from + "&to=" + phone + "&text=" + "OneTimePass:" + substr);
		        URLConnection yc = sms.openConnection();
		        BufferedReader in = new BufferedReader(
		                                new InputStreamReader(
		                                yc.getInputStream()));
		        String inputLine;
		        while ((inputLine = in.readLine()) != null) 
		            System.out.println(inputLine);
		        in.close();
		        
		        
		 
	}
	

}