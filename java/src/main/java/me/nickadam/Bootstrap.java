package me.nickadam;

import java.io.IOException;

/**
 * This file is part of Oglofus Protection project.
 * Created by Nikolaos Grammatikos <nikosgram@protonmail.com> on 19/05/2017.
 */
public class Bootstrap {
    public static void main(String[] args) throws Throwable {

        if (Float.parseFloat(System.getProperty("java.class.version")) < 52.0) {
            System.err.println("*** ERROR *** OneTime requires Java 8 or above to function! Please download and " +
                    "install it!");
            System.out.println("You can check your Java version with the command: java -version");
            return;
        }

        OneTime.main(args);
    }
}
