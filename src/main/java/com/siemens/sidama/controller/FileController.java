package com.siemens.sidama.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.siemens.sidama.entity.File;
import com.siemens.sidama.payload.UploadFileResponse;
import com.siemens.sidama.service.FileStorageService;
import com.siemens.sidama.util.CustomErrorType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/user/{userId}/uploadFile")
    public UploadFileResponse uploadFile(@PathVariable (value = "userId") Long userId, @RequestParam("file") MultipartFile file) {
        log.info(file.toString());

        String fileName = fileStorageService.storeFile(userId, file);
        log.info(fileName);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/user/" + userId +"/downloadFile/")
                .path(fileName)
                .toUriString();
        log.info(fileDownloadUri);
        
        String contentType = file.getContentType();
        log.info(contentType);
        
        long fileSize = file.getSize();
        log.info(Long.toString(fileSize));

        return new UploadFileResponse(fileName, fileDownloadUri, contentType, fileSize);
    }

    @PostMapping("/user/{userId}/uploadMultipleFiles")
    public List<UploadFileResponse> uploadMultipleFiles(@PathVariable (value = "userId") Long userId, @RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(userId, file))
                .collect(Collectors.toList());
    }

    @GetMapping("/user/{userId}/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable (value = "userId") Long userId, @PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(userId, fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/user/{userId}/files")
    public ResponseEntity<?> getAllUserFiles(@PathVariable (value = "userId") Long userId) {
        List<File> allFiles = fileStorageService.listByUser(userId);
        if (allFiles == null) {
            log.error("WEIRD: empty file list");
            return new ResponseEntity<CustomErrorType>(
                    new CustomErrorType("WEIRD: empty file list"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<List<File>>(allFiles, HttpStatus.OK);
    }

    @GetMapping("/files")
    public ResponseEntity<?> getAllFiles() {
        List<File> allFiles = fileStorageService.listAll();
        if (allFiles == null) {
            log.error("WEIRD: empty file list");
            return new ResponseEntity<CustomErrorType>(
                    new CustomErrorType("WEIRD: empty file list"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<List<File>>(allFiles, HttpStatus.OK);
    }
}
