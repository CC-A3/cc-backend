package com.cloudcomputing.upload;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.apache.http.entity.ContentType.*;

@Service
@RequiredArgsConstructor
public class UploadService {
    private final DynamoDBMapper mapper;
    private final FileStore fileStore;

    public String uploadUserProfileImage(MultipartFile file) {

        isFileEmpty(file);

        isImage(file);

        Map<String, String> metadata = extractMetadata(file);

        String path = "cc-vehicle-img";
        String filename = UUID.randomUUID() + "." + file.getOriginalFilename().split("\\.")[1];

        try {
            fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return "cc-vehicle-img.s3.ap-southeast-2.amazonaws.com/" + filename;

    }

    private Map<String, String> extractMetadata(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        return metadata;
    }

    private void isImage(MultipartFile file) {
        if (!Arrays.asList(
                IMAGE_JPEG.getMimeType(),
                IMAGE_PNG.getMimeType(),
                IMAGE_GIF.getMimeType()).contains(file.getContentType())) {
            throw new IllegalStateException("File must be an image [" + file.getContentType() + "]");
        }
    }

    private void isFileEmpty(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file [ " + file.getSize() + "]");
        }
    }

}