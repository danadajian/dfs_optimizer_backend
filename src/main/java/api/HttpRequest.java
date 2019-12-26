package api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequest {
    private int statusCode;

    public HttpRequest() {
        this.statusCode = 200;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String makeHttpRequest(String url) {
        StringBuilder response = new StringBuilder();
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            statusCode = extractErrorCodeFromString(e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    public int extractErrorCodeFromString(String errorMessage) {
        if (errorMessage.startsWith("java.io.FileNotFoundException:")) {
            return 404;
        }
        String errorCodeText = "java.io.IOException: Server returned HTTP response code: ";
        int errorCodeIndex = errorMessage.indexOf(errorCodeText) + errorCodeText.length();
        String messageSubString = errorMessage.substring(errorCodeIndex);
        return Integer.parseInt(messageSubString.split(" ")[0]);
    }
}
