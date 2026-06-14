package com.bsmaa.alumni_connect.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService {

    @Value("${app.upload.dir}")
    private String uploadRoot;

    public String uploadFile(MultipartFile file, String subDir) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // Create directory
        String targetDir = uploadRoot + subDir + "/";
        File dir = new File(targetDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Clean file name to prevent directory traversal
        String originalName = file.getOriginalFilename();
        String extension = "";
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf("."));
        }
        String fileName = UUID.randomUUID().toString() + extension;

        Path path = Paths.get(targetDir, fileName);
        Files.write(path, file.getBytes());

        return "/images/uploads/" + subDir + "/" + fileName;
    }
}
