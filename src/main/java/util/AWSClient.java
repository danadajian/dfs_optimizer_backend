package util;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import optimize.Player;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class AWSClient {
    AmazonS3 s3Client;
    AmazonSNS snsClient;

    public AWSClient() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(
                getEnvironmentVariable("AWS_KEY"),
                getEnvironmentVariable("AWS_SECRET")
        );
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion("us-east-2")
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
        this.snsClient = AmazonSNSClientBuilder.standard()
                .withRegion("us-east-1")
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    public void uploadToS3(String fileName, Object objectToUpload) {
        try {
            String jsonString = new ObjectMapper().writeValueAsString(objectToUpload);
            s3Client.putObject("dfs-pipeline", fileName, jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public List<Map<String, Object>> downloadFromS3(String fileName) {
        InputStream s3Object = s3Client.getObject("dfs-pipeline", fileName).getObjectContent();
        Scanner s = new Scanner(s3Object).useDelimiter("\\A");
        String s3ObjectString = s.hasNext() ? s.next() : "";
        List<Map<String, Object>> result = Collections.emptyList();
        try {
            result = new ObjectMapper().readValue(s3ObjectString, new TypeReference<List<Map<String, Object>>>(){});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void sendTextMessages(String sport, List<Player> optimalPlayersWithNames) {
        String textOutput = convertToTextOutput(optimalPlayersWithNames);
        String stringToSendAsText =
                "Good evening, Tony. Here is the optimal " + sport.toUpperCase() + " lineup for tonight:" + textOutput;
        snsClient.publish(new PublishRequest()
                .withMessage(stringToSendAsText)
                .withPhoneNumber(getEnvironmentVariable("DAN_PHONE_NUMBER")));
        snsClient.publish(new PublishRequest()
                .withMessage(stringToSendAsText)
                .withPhoneNumber(getEnvironmentVariable("TONY_PHONE_NUMBER")));
    }

    public String convertToTextOutput(List<Player> optimalPlayerNames) {
        List<String> outputForText = optimalPlayerNames.stream()
                .map(player -> "\n" + player.name + " " + player.team + " " + player.position)
                .collect(Collectors.toList());
        outputForText.add("\nTotal projected points: " +
                optimalPlayerNames.stream()
                        .mapToDouble(player -> player.projection)
                        .sum());
        outputForText.add("\nTotal salary: $" +
                optimalPlayerNames.stream()
                        .mapToInt(player -> player.salary)
                        .sum());
        return outputForText.toString().substring(1, outputForText.toString().length() - 1);

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
