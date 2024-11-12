package security;
import oop.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.ArrayList;

public class PasswordHashing
{
    static ArrayList<String> passW = new ArrayList<>();
    static String[] passwords;
    static ArrayList<String> useW = new ArrayList<>();
    static String[] usernames;
    static int counter = 0;

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void init(User[] pass, User[] use, int count)
    {
        counter = count;
        for(int i = 0; i < counter; i++)
        {
            passW.add(pass[i].getPassword());
            useW.add(use[i].getUsername());
        }
        passwords = passW.toArray(new String[0]);
        usernames = useW.toArray(new String[0]);

        String[] hashedPasswords = new String[passwords.length];
        for (int i = 0; i < passwords.length; i++)
            hashedPasswords[i] = hashPassword(passwords[i]);

        System.out.println("Usernames: " + Arrays.toString(usernames));
        System.out.println("Original Passwords: " + Arrays.toString(passwords));
        System.out.println("Hashed Passwords (SHA-256): " + Arrays.toString(hashedPasswords));
    }
}


