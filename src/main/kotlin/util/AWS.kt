package util

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.InputStream
import java.util.*

class AWS {
    fun uploadToS3(fileName: String, objectToUpload: Any) {
        try {
            val jsonString = ObjectMapper().writeValueAsString(objectToUpload)
            val awsCredentials = BasicAWSCredentials(System.getenv("AWS_KEY"), System.getenv("AWS_SECRET"))
            val s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion("us-east-2")
                    .withCredentials(AWSStaticCredentialsProvider(awsCredentials))
                    .build()
            s3Client.putObject("dfs-pipeline", fileName, jsonString)
        } catch (e: JsonProcessingException) {
            e.printStackTrace()
        }
    }

    fun downloadFromS3(fileName: String?): List<Map<String?, Any?>?>? {
        val awsCredentials = BasicAWSCredentials(System.getenv("AWS_KEY"), System.getenv("AWS_SECRET"))
        val s3Client = AmazonS3ClientBuilder.standard()
                .withRegion("us-east-2")
                .withCredentials(AWSStaticCredentialsProvider(awsCredentials))
                .build()
        val s3Object: InputStream = s3Client.getObject("dfs-pipeline", fileName).objectContent
        val s: Scanner = Scanner(s3Object).useDelimiter("\\A")
        val s3ObjectString = if (s.hasNext()) s.next() else ""
        var result: List<Map<String?, Any?>?>? = emptyList<Map<String?, Any?>>()
        try {
            result = ObjectMapper().readValue(s3ObjectString, object : TypeReference<List<Map<String?, Any?>?>?>() {})
        } catch (e: JsonProcessingException) {
            e.printStackTrace()
        }
        return result
    }
}