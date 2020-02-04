package util;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class S3Upload {

    public void uploadToS3(String fileName, Object objectToUpload) {
        String[] credentials = getAwsCredentials("src/main/resources/config.properties");
        String aws_key = credentials[0];
        String aws_secret = credentials[1];
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(aws_key, aws_secret);
        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion("us-east-2")
                    .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                    .build();
            String jsonString = new ObjectMapper().writeValueAsString(objectToUpload);
            s3Client.putObject("dfs-pipeline", fileName, jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public String[] getAwsCredentials(String filePath) {
        String[] props = {"", ""};
        try (InputStream input = new FileInputStream(filePath)) {
            Properties prop = new Properties();
            prop.load(input);
            props[0] = prop.getProperty("aws_key");
            props[1] = prop.getProperty("aws_secret");
        } catch (IOException e) {
            if (System.getenv("aws_key") != null && System.getenv("aws_secret") != null) {
                props[0] = System.getenv("aws_key");
                props[1] = System.getenv("aws_secret");
            }
        }
        return props;
    }
}
