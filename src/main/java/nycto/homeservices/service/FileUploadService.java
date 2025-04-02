package nycto.homeservices.service;

import nycto.homeservices.exceptions.EmptyFileException;
import nycto.homeservices.exceptions.FileSizeLimitException;
import nycto.homeservices.exceptions.InvalidFileFormatException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUploadService {

    private static final String UPLOAD_DIR = "uploads/";

    public String uploadFile(MultipartFile file) throws IOException {

        if (file.isEmpty())
            throw new EmptyFileException("Profile picture is required");



        if (!file.getContentType().equals("image/jpeg"))
            throw new InvalidFileFormatException("Only JPG files are allowed");


        if (file.getSize() > 300 * 1024)
            throw new FileSizeLimitException("File size exceeds 300 KB");


        String fileName = UUID.randomUUID().toString() + ".jpg";
        Path filePath = Paths.get(UPLOAD_DIR + fileName);

        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());

        return filePath.toString();
    }
}