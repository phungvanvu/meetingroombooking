package org.training.meetingroombooking.service.impl;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.training.meetingroombooking.service.S3Service;

@Service
public class S3ServiceImpl implements S3Service {
  private final AmazonS3 amazonS3;
  private final String bucketName;

  public S3ServiceImpl(
      @Value("${aws.s3.endpoint-url}") String endpointUrl,
      @Value("${aws.s3.region}") String region,
      @Value("${aws.access-key-id}") String accessKeyId,
      @Value("${aws.secret-access-key}") String secretAccessKey,
      @Value("${aws.s3.bucket-name}") String bucketName) {
    this.bucketName = bucketName;
    // Cấu hình client AWS S3
    BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
    this.amazonS3 =
        AmazonS3ClientBuilder.standard()
            .withEndpointConfiguration(
                new AwsClientBuilder.EndpointConfiguration(endpointUrl, region))
            .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
            .enablePathStyleAccess()
            .build();
  }

  public String uploadFile(String keyName, MultipartFile file) throws IOException {
    File convertedFile = convertMultiPartToFile(file);
    amazonS3.putObject(new PutObjectRequest(bucketName, keyName, convertedFile));
    return amazonS3.getUrl(bucketName, keyName).toString();
  }

  private File convertMultiPartToFile(MultipartFile file) throws IOException {
    File convertedFile =
        new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
    file.transferTo(convertedFile);
    return convertedFile;
  }
}
