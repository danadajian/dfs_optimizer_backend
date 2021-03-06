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

    public void uploadToS3(String fileName, Object objectToUpload) {
        try {
            String jsonString = new ObjectMapper().writeValueAsString(objectToUpload);
            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(
                    getEnvironmentVariable("AWS_KEY"),
                    getEnvironmentVariable("AWS_SECRET")
            );
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion("us-east-2")
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .build();
            s3Client.putObject("dfs-pipeline", fileName, jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public List<Map<String, Object>> downloadFromS3(String fileName) {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(
                getEnvironmentVariable("AWS_KEY"),
                getEnvironmentVariable("AWS_SECRET")
        );
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion("us-east-2")
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
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
