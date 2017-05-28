package me.nickadam;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Scanner;

public class intial {
    /*
    Variables Declaration for Username etc for SMSBOX.gr.
    Also the decleration of the private key
    Hashing Technique:
        hash = md5(md5(key) + md5(time))
     */

    private final String username = ""; //SMSBOX Username
    private final String password = ""; //SMSBOX Password
    private final String recipient = ""; //Recipient
    private final String sender = "OneTimePass"; //Sender
    private final String privkey = ""; //PrivatekKey
    private int year = Calendar.getInstance().get(Calendar.YEAR);
    private int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
    private int day = Calendar.getInstance().get(Calendar.DATE);
    private int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    private int minute =  Calendar.getInstance().get(Calendar.MINUTE);
    private final int time = year + hour + month + day + minute;
    public static String hash;

    public static void main(String[] args) throws IOException  {
        while(true){
        System.out.println("OneTimePass by Nick Adam" + "\nInput 1 in order to create a new OTP or input 2 to verify an OTP");
        Scanner keyboard = new Scanner(System.in);
        int input = keyboard.nextInt();
        switch (input) {
            case 1:
               new intial().encode();
               break;
            case 2:
                new intial().decode();
                break;
            default:
               System.out.println("Invalid Input");
                break;
                }
        }
        }

    public void encode() throws IOException {
        String almosthashed = DigestUtils.md5Hex(privkey) + DigestUtils.md5Hex(Integer.toString(time));
        intial.hash = DigestUtils.md5Hex(almosthashed);
        new intial().send();

    }

    public void decode() {
        String almosthashed = DigestUtils.md5Hex(privkey) + DigestUtils.md5Hex(Integer.toString(time));
        intial.hash = DigestUtils.md5Hex(almosthashed);
        final String hashed = intial.hash.substring(intial.hash.length() - 6);
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Enter the OneTimePass: ");
        String input = keyboard.next();
        if(hashed.equals(input)) {
            System.out.println("Your pass is Valid!");
        } else {
            System.out.println("Your pass is Invalid!");
        }
    }

    public void send() throws IOException {
        final String hashed = intial.hash.substring(intial.hash.length() - 6);
         URL sms = new URL("http://www.smsbox.gr/httpapi/sendsms.php?username=" + username + "&password=" + password + "&from=" + sender + "&to=" + recipient + "&text=" + "OTP:%20" + hashed);
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