package com.example.developjeans.global.config.aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.springframework.web.servlet.function.RequestPredicates.contentType;


@Slf4j
@Service
@Transactional
@Tag(name = "배경화면")
@RequiredArgsConstructor
public class S3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;

    public String uploadImage(MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        UUID fileNameUUID = UUID.randomUUID();
        //파일 형식 구하기
        String ext = Objects.requireNonNull(fileName).split("\\.")[1];
        fileName = fileNameUUID+"."+ext;
        String contentType = "";

        //content type 지정
        switch (ext) {
            case "jpeg":
                contentType = "image/jpeg";
                break;
            case "jpg":
                contentType = "image/jpg";
                break;
            case "png":
                contentType = "image/png";
                break;
        }

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            //S3 upload
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, multipartFile.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        }
        //이미지 주소 리턴
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    public String getImage(String bucket, String fileName){
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    public void deleteImage(File file){
        if (file.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 않았습니다.");
        }
    }

    public List<String> getImageList(String categoryName){
        ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucket).withPrefix(categoryName);
        ListObjectsV2Result result;
        List<String> imageUriList = new ArrayList<>();

        do {
            result = amazonS3.listObjectsV2(req);

            for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
                String fileName = objectSummary.getKey();
                if (!fileName.endsWith(".DS_Store")) {
                    imageUriList.add(getFile(fileName));
                }
            }
            String token = result.getNextContinuationToken();
            req.setContinuationToken(token);
        } while(result.isTruncated());

        return imageUriList;
    }

    public String getFile(String fileName) {
        try{
            return amazonS3.getUrl(bucket, fileName).toString();
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "파일이 없습니다");
        }
    }

    public ResponseEntity<byte[]> download(String fileUrl) throws IOException { // 객체 다운  fileUrl : 폴더명/파일네임.파일확장자

        // 파일의 경로와 파일명을 추출
        String[] urlParts = fileUrl.split("/");
        String key = String.join("/", Arrays.copyOfRange(urlParts, 3, urlParts.length)); // 버킷명 다음의 경로부터가 키
        log.info("key = " + key);

        S3Object s3Object = amazonS3.getObject(new GetObjectRequest(bucket, key));
        S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
        byte[] bytes = IOUtils.toByteArray(objectInputStream);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(contentType(fileUrl));
        httpHeaders.setContentLength(bytes.length);

        // 파일명 추출 및 인코딩
        String fileName = urlParts[urlParts.length - 1];
        fileName = URLEncoder.encode(fileName, "UTF-8");
        log.info("fileName = " + fileName);
        httpHeaders.setContentDispositionFormData("attachment", fileName); // 파일이름 지정

        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }

    private MediaType contentType(String keyname) {
        String[] arr = keyname.split("\\.");
        String type = arr[arr.length - 1];
        log.info("type = " + type);
        switch (type) {
            case "txt":
                return MediaType.TEXT_PLAIN;
            case "png":
                return MediaType.IMAGE_PNG;
            case "jpg":
                return MediaType.IMAGE_JPEG;
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

}
/*
@Service
@Transactional
@RequiredArgsConstructor // final 변수가 있으면 생성자에 포함시킴
@Slf4j
public class S3Service {
    @Value("${cloud.aws.s3.bucket")
    private String bucket;
    private final AmazonS3 amazonS3;

    public String uploadImage(MultipartFile multipartFile, String name) throws IOException{
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));

        return upload(uploadFile, name);

    }

    private String upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" + UUID.randomUUID() + "." + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        File convertFile = new File(file.getOriginalFilename());
        System.out.println("convertFile = " + convertFile);
        if(convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }
}
*/
