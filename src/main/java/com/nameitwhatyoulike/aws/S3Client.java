package com.nameitwhatyoulike.aws;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URL;

/**
 * Created by adam on 6/3/14.
 */
@Component
public class S3Client {

    //S3Client is threadsafe, except of setRegion method
    AmazonS3 s3client;

    @PostConstruct
    public void setUp(){
         /*
         * @link{DefaultAWSCredentialsProviderChain #DefaultAWSCredentialsProviderChain}
         * AWS credentials provider chain that looks for credentials in this order:
         *   Environment Variables - AWS_ACCESS_KEY_ID and AWS_SECRET_KEY
         *   Java System Properties - aws.accessKeyId and aws.secretKey
         *   Credential profiles file at the default location (~/.aws/credentials) shared by all AWS SDKs and the AWS CLI
         *   Instance profile credentials delivered through the Amazon EC2 metadata service
         *   Instance profile credentials delivered through the Amazon EC2 metadata service
         */
        s3client = new AmazonS3Client(new DefaultAWSCredentialsProviderChain());
    }


    public URL generateUploadURL(String userId, String fileName){
        return getPresignedUrl(userId, fileName, HttpMethod.PUT);
    }

    public URL generateDownloadURL(String userId, String fileName){
        return getPresignedUrl(userId, fileName, HttpMethod.GET);
    }

    private URL getPresignedUrl(String userId, String fileName,HttpMethod method) {
        java.util.Date expiration = new java.util.Date();
        long msec = expiration.getTime();
        msec += 1000 * 60 * 60;
        expiration.setTime(msec);

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(getBucketNameFor(userId), generateKeyFor(userId,fileName));
        generatePresignedUrlRequest.setMethod(method);
        generatePresignedUrlRequest.setExpiration(expiration);

        return s3client.generatePresignedUrl(generatePresignedUrlRequest);
    }

    /*
    If keys overlap newest file will be stored on S3
     */
    private String generateKeyFor(String userId, String fileName) {
        return userId + "_" + fileName;
    }


    /*
    User files should probably be stored in different buckets based on geolocation, for now its only one.
     */
    private static final String BUCKET_NAME = "cohesivatest";
    private String getBucketNameFor(String userId){
        return BUCKET_NAME;
    }



}
