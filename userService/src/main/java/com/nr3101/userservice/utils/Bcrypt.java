package com.nr3101.userservice.utils;

import static org.mindrot.jbcrypt.BCrypt.*;

public class Bcrypt {

    public static String hashPassword(String password) {
        return hashpw(password, gensalt());
    }

    public static boolean checkPassword(String password, String hashed) {
        return checkpw(password, hashed);
    }
}
