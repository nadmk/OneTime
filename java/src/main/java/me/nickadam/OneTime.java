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

public class OneTime {
    private static final String SERVICE = "http://www.smsbox.gr/httpapi/sendsms.php";

    private final String username;
    private final String password;
    private final String recipient;
    private final String sender;
    private final String key;

    public OneTime(String[] args) throws Throwable {
        if (args.length < 3) {

            System.out.println("*** ERROR *** Please, try to insert the requirement arguments.");
            System.out.println("Example: <recipient> <sender> <key>");

            throw new Throwable();
        }

        Path path = Paths.get("./config.yml");
        Yaml yaml = new Yaml();

        Map<String, Object> values = null; //WHY NOT

        if (Files.notExists(path)) {
            Files.copy(getClass().getClassLoader().getResourceAsStream("config.yml"), path);
        } else {
            values = (Map<String, Object>) yaml.load(Files.newInputStream(path));
        }


        if (!(isString("username", values) || isString("password", values))) {

            System.out.println("*** ERROR *** Please, check the configuration file. Is incorrect.");

            throw new Throwable();
        }

        this.username = (String) values.get("username");
        this.password = (String) values.get("password");
        this.recipient = args[0];
        this.sender = args[1];
        this.key = args[2];
    }

    private static boolean isString(String key, Map<String, Object> values) {
        if (values == null) return false;
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
