package com.siemens.sidama.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.siemens.sidama.entity.File;
import com.siemens.sidama.entity.User;
import com.siemens.sidama.exception.FileStorageException;
import com.siemens.sidama.exception.MyFileNotFoundException;
import com.siemens.sidama.property.FileStorageProperties;
import com.siemens.sidama.repository.FileRepository;
import com.siemens.sidama.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    
    @Autowired
    FileRepository fileRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
            log.info("createDirectories: " + this.fileStorageLocation.toString());
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(Long userId, MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        
        Optional<User> optUser = userRepository.findById(userId);
        if (optUser == null) {
            log.error("Unable to find user with id: " + Long.toString(userId));
        }

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path tempLocation = fileStorageLocation.resolve(Long.toString(userId));
            log.info(tempLocation.toString());
            Files.createDirectories(tempLocation);
            Path targetLocation = tempLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            // Do not forget to add entry into the database
            File ff = new File(fileName, file.getContentType(), targetLocation.toFile().length(), optUser.get());
            fileRepository.save(ff);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(Long userId, String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(Long.toString(userId)).resolve(fileName).normalize();
            log.info(filePath.toString());
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }

    // Added by FBG
    public void deleteFile(Long userId, String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(Long.toString(userId)).resolve(fileName).normalize();
            log.info(filePath.toString());
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                filePath.toFile().delete();
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }

    public List<File> listByUser(Long userId) {
        List<File> files = new ArrayList<>();
        fileRepository.findByUserId(userId).forEach(files::add);
        return files;
    }

    public List<File> listAll() {
        List<File> files = new ArrayList<>();
        fileRepository.findAll().forEach(files::add);
        return files;
    }
}
