package com.example.BookingTravelBackend.controllers;
import com.example.BookingTravelBackend.Configuration.WebConfig;
import com.example.BookingTravelBackend.payload.respone.HttpRespone;
import com.example.BookingTravelBackend.payload.respone.fileResponse;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
@RestController
@RequestMapping("/file")
public class FileUploadController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping("/upload")
    public ResponseEntity<HttpRespone> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>(new HttpRespone(HttpStatus.BAD_REQUEST.value(), "File không được để trống", null), HttpStatus.BAD_REQUEST);
        }

        try {
            BufferedImage originalImage = ImageIO.read(file.getInputStream());

            // Resize ảnh
            BufferedImage resizedImage = Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, 800, 600);

            // Tạo file nén
            String uniqueFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path compressedFilePath = Paths.get(uploadDir + uniqueFilename);

            if (Files.notExists(compressedFilePath.getParent())) {
                Files.createDirectories(compressedFilePath.getParent());
            }

            // Lưu file nén
            ImageIO.write(resizedImage, "jpg", compressedFilePath.toFile());

            // Trả về đường dẫn
            String fileDownloadUri = WebConfig.urlBackend + "/api/file/uploads/" + uniqueFilename;
            return new ResponseEntity<>(new HttpRespone(HttpStatus.OK.value(), "Upload thành công", new fileResponse(fileDownloadUri)), HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new HttpRespone(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Không thể upload file", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<byte[]> getFile(@PathVariable String filename) {
        try {
            Path path = Paths.get(uploadDir + filename);
            byte[] fileContent = Files.readAllBytes(path);

            // Xác định loại MIME của file ảnh
            String mimeType = Files.probeContentType(path);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(mimeType));
            headers.setContentDispositionFormData("filename", filename);

            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
