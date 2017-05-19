package me.nickadam;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Date;
import java.util.Map;

/**
 * This file is part of Oglofus Protection project.
 * Created by Nikolaos Grammatikos <nikosgram@protonmail.com> on 19/05/2017.
 */
public class OneTime {
    private static final String SERVICE = "http://www.smsbox.gr/httpapi/sendsms.php";

    private final String username;
    private final String password;
    private final String recipient;
    private final String sender;
    private final String key;

    public OneTime(String username, String password, String recipient, String sender, String key) {
        this.username = username;
        this.password = password;
        this.recipient = recipient;
        this.sender = sender;
        this.key = key;
    }

    public static void main(String[] args) throws Throwable {
        if (args.length < 5) {

            System.out.println("*** ERROR *** Please, try to insert the requirement arguments.");
            System.out.println("Example: <recipient> <sender> <key>");

            return;
        }

        //LOAD CONFIGURATION FILE.. YML

        Path path = Paths.get("./config.yml");

        if (Files.notExists(path)) {

        }

        Yaml yaml = new Yaml();

        Map<String, Object> values = (Map<String, Object>) yaml
                .load(Files.newInputStream(path));

        if (!(isString("username", values) || isString("password", values))) {

            System.out.println("*** ERROR *** Please, check the configuration file. Is incorrect.");

            return;
        }

        new OneTime(
                (String) values.get("username"),
                (String) values.get("password"),
                args[2],
                args[3],
                args[5]
        ).send();
    }

    private static boolean isString(String key, Map<String, Object> values) {
        if (!values.containsKey(key)) return false;

        Object value = values.get(key);

        return value instanceof String;
    }

    public static String encode(String key) throws Throwable {
        String message = new BigInteger(
                1,
                MessageDigest
                        .getInstance("MD5")
                        .digest(
                                (String.valueOf(new Date().hashCode()) + key)
                                        .getBytes("UTF-8")
                        )
        ).toString();

        return message.substring(message.length() - 6);
    }

    public void send() throws Throwable {
        String code = encode(key);

        String url = String.format(
                SERVICE + "?username=%s&password=%s&from=%s&to=%s&text=OneTimePass:%s",
                URLEncoder.encode(username, "UTF-8"),
                URLEncoder.encode(password, "UTF-8"),
                URLEncoder.encode(sender, "UTF-8"),
                URLEncoder.encode(recipient, "UTF-8"),
                URLEncoder.encode(code, "UTF-8")
        );

        System.out.println(url);

        URL           request    = new URL(url);
        URLConnection connection = request.openConnection();

        InputStream stream = connection.getInputStream();

        int bit;
        while ((bit = stream.read()) != -1) {
            System.out.println((byte) bit);
        }

        stream.close();
    }
}
