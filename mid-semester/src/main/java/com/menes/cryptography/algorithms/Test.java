package com.menes.cryptography.algorithms;

import javax.crypto.Cipher;
import java.security.Provider;
import java.security.Security;

public class Test {
    public static void main(String[] args) {
        String data = "I very like this song. I like your stage. It’s so professional. A great voice. I feel like I am being in a different world. I like this space. I like instruments on your stage. It looks like a concert. I like best. That is the piano. And I see. There are some ballads. Everything is mixing. And You give me a large vision of the music. That is modern. So good";
//        String key  = "";
        for (Provider provider: Security.getProviders()) {
            System.out.println(provider.getName());
            for (String key: provider.stringPropertyNames())
                System.out.println("\t" + key + "\t" + provider.getProperty(key));
        }

    }
}
