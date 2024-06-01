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
        String extension = fileName.substring(fileName.lastIndexOf("."));
         
        try (var inputStream = multipartFile.getInputStream()) {
            var filePath = uploadPath.resolve(fileCode + extension);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {       
            throw new IOException("Could not save file: " + fileName, ioe);
        }
         
        return fileCode + extension;
    }

    public static void deleteFile(String path, String name) throws IOException{
        var filePath = Paths.get(path,name);
        try {
            Files.delete(filePath);  
        } catch (IOException e) {
            throw new IOException("Could not delete file: " + filePath.toString(), e);
        }
    }
}
