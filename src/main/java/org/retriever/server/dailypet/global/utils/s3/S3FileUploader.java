package org.retriever.server.dailypet.global.utils.s3;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.retriever.server.dailypet.domain.s3.dto.MultipartUrlResponse;
import org.retriever.server.dailypet.domain.s3.dto.PresignedUrlResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3FileUploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public PresignedUrlResponse getPreSignedUrl(S3Path s3Path, String fileName) {
        fileName = s3Path.getFilePath() + fileName;
        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePreSignedUrlRequest(fileName);
        URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
        return PresignedUrlResponse.from(url.toString());
    }

    private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String fileName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, fileName)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(getPreSignedUrlExpiration());
        generatePresignedUrlRequest.addRequestParameter(
                Headers.S3_CANNED_ACL,
                CannedAccessControlList.PublicRead.toString());
        return generatePresignedUrlRequest;
    }

    private Date getPreSignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 2;
        expiration.setTime(expTimeMillis);
        log.info(expiration.toString());
        return expiration;
    }

    public MultipartUrlResponse uploadMultipartFileToS3(MultipartFile multipartFile, S3Path s3Path) throws IOException {
        String fileName = s3Path.getFilePath() + multipartFile.getOriginalFilename();
        byte[] fileData = multipartFile.getBytes();
        ObjectMetadata metadata = new ObjectMetadata();

        metadata.setContentType(multipartFile.getContentType());
        metadata.setContentLength(fileData.length);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileData);

        return MultipartUrlResponse.from(putS3(fileName, byteArrayInputStream, metadata));
    }

    private String putS3(String fileName, ByteArrayInputStream fileData, ObjectMetadata metadata) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, fileData, metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }
}
