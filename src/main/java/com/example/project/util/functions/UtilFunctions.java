package com.example.project.util.functions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public class UtilFunctions {
    public static String saveFile(String path, String fileName, MultipartFile multipartFile)
            throws IOException {
        var uploadPath = Paths.get(path);
 
        String fileCode = UUID.randomUUID().toString();
         
        try (var inputStream = multipartFile.getInputStream()) {
            var filePath = uploadPath.resolve(fileCode + "-" + fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {       
            throw new IOException("Could not save file: " + fileName, ioe);
        }
         
        return fileCode;
    }
}
