package com.diezel.cryptofeed;

/**
 * Created by zalet on 2/11/2018.
 */
public class BCryptPasswordEncoder {

    private static final int PASSWORDS_TO_GENERATE = 10;
    private static final int PASSWORD_ENCODER_STRENGTH = 10;

    public static void main(String[] args) {

        String password = "admin";

        org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder passwordEncoder = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder(PASSWORD_ENCODER_STRENGTH);

        System.out.println("Genering "+PASSWORDS_TO_GENERATE+" hashes for password: "+password);
        for(int n = 1; n <= PASSWORDS_TO_GENERATE; n++) {
            System.out.println("#"+n+". "+passwordEncoder.encode(password));
        }
    }

}
