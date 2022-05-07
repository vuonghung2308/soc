package com.mh.soc.controller;

import com.mh.soc.interceptor.CustomException;
import com.mh.soc.model.File;
import com.mh.soc.repository.FileRepository;
import com.mh.soc.vo.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("api/file")
public class FileController {
    @Autowired
    private FileRepository repository;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(
            @RequestParam("file") MultipartFile file
    ) {
        try {
            String name = StringUtils.cleanPath(
                    Objects.requireNonNull(file.getOriginalFilename()));
            SimpleDateFormat f = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSSSSS");
            String ext = name.substring(name.lastIndexOf("."));
            String newName = "file_" + f.format(new Date()) + ext;
            File image = new File(newName, file.getContentType(), file.getBytes());
            repository.save(image);
            ResponseMessage message = new ResponseMessage(
                    "UPLOAD_SUCCESSFULLY",
                    "upload file successfully, file name: " + newName
            );
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(
                    "CANT_NOT_UPLOAD_FILE",
                    "can't upload file name: " + file.getOriginalFilename()
            );
        }
    }

    @GetMapping("/{name}")
    public ResponseEntity<byte[]> getFile(@PathVariable String name) {
        Optional<File> optionalFile = repository.findByName(name);
        if (optionalFile.isPresent()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
                            + optionalFile.get().getName() + "\"")
                    .body(optionalFile.get().getData());
        } else {
            throw new CustomException(
                    "FILE_NOT_FOUND",
                    "can't find file with id: " + name
            );
        }
    }
}
