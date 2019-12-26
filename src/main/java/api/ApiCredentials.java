package api;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Properties;

public class ApiCredentials {

    public String getSignature(String key, String secret) {
        final String combo = key + secret + (new Date().getTime()/1000);
        String signature = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(combo.getBytes(StandardCharsets.UTF_8));
            signature = convertBytesToHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return signature;
    }

    public String[] getPropValues(String filePath) {
        String[] props = {"", ""};
        try (InputStream input = new FileInputStream(filePath)) {
            Properties prop = new Properties();
            prop.load(input);
            props[0] = prop.getProperty("key");
            props[1] = prop.getProperty("secret");
        } catch (IOException e) {
            if (System.getenv("key") != null && System.getenv("secret") != null) {
                props[0] = System.getenv("key");
                props[1] = System.getenv("secret");
            }
        }
        return props;
    }

    private static String convertBytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
