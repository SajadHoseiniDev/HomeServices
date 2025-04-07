package nycto.homeservices.service;

import nycto.homeservices.exceptions.EmptyFileException;
import nycto.homeservices.exceptions.FileSizeLimitException;
import nycto.homeservices.exceptions.InvalidFileFormatException;
import nycto.homeservices.service.serviceInterface.FileUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadServiceImpl.class);


    private static final String UPLOAD_DIR = "uploads/";

    @Override
    public String uploadFile(MultipartFile file) throws IOException {

        if (file.isEmpty()) {

            logger.error("File is empty: {}", file.getOriginalFilename());

            throw new EmptyFileException("Profile picture is required");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.equals("image/jpeg")) {
            logger.error("Invalid file format: {}. Only JPG allowed", contentType);
            throw new InvalidFileFormatException("Only JPG files are allowed");
        }

        if (file.getSize() > 300 * 1024) { // 300 KB
            logger.error("File size exceeds limit: {} bytes", file.getSize());
            throw new FileSizeLimitException("File size exceeds 300 KB");
        }

        String fileName = UUID.randomUUID().toString() + ".jpg";
        Path filePath = Paths.get(UPLOAD_DIR + fileName);

        logger.info("Saving file to: {}", filePath);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());

        logger.info("File uploaded successfully: {}", filePath);
        return filePath.toString();
    }
}