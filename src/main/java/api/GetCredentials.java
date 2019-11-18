package api;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GetCredentials {
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
}
