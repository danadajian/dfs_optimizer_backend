package util;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class AWSClient {
    private BasicAWSCredentials awsCredentials;

    public AWSClient() {
        this.awsCredentials = new BasicAWSCredentials(
                getEnvironmentVariable("aws_key"),
                getEnvironmentVariable("aws_secret")
        );
    }

    public void uploadToS3(String fileName, Object objectToUpload) {
        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion("us-east-2")
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .build();
            String jsonString = new ObjectMapper().writeValueAsString(objectToUpload);
            s3Client.putObject("dfs-pipeline", fileName, jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void sendTextMessage(List<String> optimalPlayerNames) {
        AmazonSNS snsClient = AmazonSNSClientBuilder.standard()
                .withRegion("us-east-1")
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
        String stringToSendAsText = "Good evening, Tony. Here is your optimal lineup for tonight:" +
                optimalPlayerNames.toString().substring(1, optimalPlayerNames.toString().length() - 1);
        snsClient.publish(new PublishRequest()
                .withMessage(stringToSendAsText)
                .withPhoneNumber(getEnvironmentVariable("phone_number"))
        );
    }

    public String getEnvironmentVariable(String varName) {
        String environmentVariable = "";
        try (InputStream input = new FileInputStream("src/main/resources/config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            environmentVariable = prop.getProperty(varName);
        } catch (IOException e) {
            if (System.getenv(varName) != null)
                environmentVariable = System.getenv(varName);
        }
        return environmentVariable;
    }
}
