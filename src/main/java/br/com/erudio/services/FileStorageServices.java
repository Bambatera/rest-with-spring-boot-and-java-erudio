package br.com.erudio.services;

import br.com.erudio.config.FileStorageConfig;
import br.com.erudio.exceptions.FileNotFoundException;
import br.com.erudio.exceptions.FileStorageException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageServices {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageServices(FileStorageConfig fileStorageConfig) {
        this.fileStorageLocation = Paths.get(fileStorageConfig.getUploadDir()).toAbsolutePath().normalize();

        try {
            Files.createDirectories(fileStorageLocation);
        } catch (Exception e) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be storage!", e);
        }
    }

    public String storeFile(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (filename.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + filename);
            }
            Path targetLocation = this.fileStorageLocation.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return filename;
        } catch (Exception e) {
            throw new FileStorageException("Could not store file " + filename + ". Please try again!", e);
        }
    }

    public Resource loadFileAsResource(String filename) {
        try {
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found!");
            }
        } catch (Exception e) {
            throw new FileNotFoundException("File not found!", e);
        }
    }
}
