package mju.scholarship.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mju.scholarship.result.exception.FileConvertException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class S3UploadService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucket;

    public String upload(MultipartFile multipartFile, String dirName) throws IOException {

        File uploadFile = convert(multipartFile)
                .orElseThrow(FileConvertException::new);

        return upload(uploadFile, dirName);
    }

    public String upload(File uploadFile, String dirName){
        final String fileName = dirName + "/" + uploadFile.getName();
        final String uploadImageUrl = putS3(uploadFile, fileName);

        removeNewFile(uploadFile);
        log.info("uploadImageUrl={}", uploadImageUrl);
        return uploadImageUrl;
    }

    private void removeNewFile(File targetFile) {

        if(targetFile.delete()){
            log.info("파일 삭제");
        }else{
            log.info("파일 삭제 못함");
        }
    }

    private String putS3(File uploadFile, String fileName){
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );

        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private Optional<File> convert(MultipartFile multipartFile) throws IOException {
        // 원본 파일 이름을 가져옵니다.
        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            originalFilename = "default_file";
        }

        // 임시 디렉터리에서 고유한 파일 생성
        File convertFile = new File(System.getProperty("java.io.tmpdir"), System.currentTimeMillis() + "_" + originalFilename);

        log.info("convertFile path: {}", convertFile.getAbsolutePath());

        try {
            // 파일 생성 및 데이터 쓰기
            if (convertFile.createNewFile()) {
                try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                    fos.write(multipartFile.getBytes());
                }
                log.info("File created and written successfully: {}", convertFile.getAbsolutePath());
                return Optional.of(convertFile);
            } else {
                log.error("Failed to create new file: {}", convertFile.getAbsolutePath());
            }
        } catch (IOException e) {
            log.error("Error while converting MultipartFile to File: {}", e.getMessage());
            throw e;
        }

        return Optional.empty();
    }


}
