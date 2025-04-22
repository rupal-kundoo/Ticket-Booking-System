package org.example.util;

import org.mindrot.jbcrypt.BCrypt;

public class userUtilService {

    public static String hashedpassword(String plainpassword)
    {
       return BCrypt.hashpw(plainpassword,BCrypt.gensalt());
    }

    public static boolean checkpassword(String plainpassword, String hashpassword)
    {
       return BCrypt.checkpw(plainpassword,hashpassword);
    }
}
