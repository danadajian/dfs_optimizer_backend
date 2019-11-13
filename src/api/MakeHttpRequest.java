package api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MakeHttpRequest {
    String HttpRequest(String url) {
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
            throw new ApiException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    int extractErrorCodeFromString(String errorMessage) {
        if (errorMessage.startsWith("java.io.FileNotFoundException:")) {
            return 404;
        }
        String errorCodeText = "java.io.IOException: Server returned HTTP response code: ";
        int errorCodeIndex = errorMessage.indexOf(errorCodeText) + errorCodeText.length();
        String messageSubString = errorMessage.substring(errorCodeIndex);
        return Integer.parseInt(messageSubString.split(" ")[0]);
    }

    public class ApiException extends RuntimeException {
        String errorMessage;

        ApiException(Throwable e) {
            this.errorMessage = e.toString();
        }

        public int getErrorCode() {
            return extractErrorCodeFromString(errorMessage);
        }

        public String getErrorMessage(int errorCode) {
            String errorMessage;
            switch(errorCode) {
                case 403:
                    errorMessage = "Access denied.";
                    break;
                case 404:
                    errorMessage = "Data not found.";
                    break;
                case 500:
                    errorMessage = "Internal server error.";
                    break;
                default:
                    errorMessage = "An unknown error occurred.";
            }
            return errorMessage;
        }

    }
}
