package org.training.meetingroombooking.controller;

import org.training.meetingroombooking.service.S3Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/files")
public class FileController {

  private final S3Service s3Service;

  public FileController(S3Service s3Service) {
    this.s3Service = s3Service;
  }

  @PostMapping("/upload")
  public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
    try {
      String fileUrl = s3Service.uploadFile("rooms/" + file.getOriginalFilename(), file);
      Map<String, String> response = new HashMap<>();
      response.put("url", fileUrl);
      return ResponseEntity.ok(response);
    } catch (IOException e) {
      return ResponseEntity.internalServerError().body(Map.of("error", "File upload failed"));
    }
  }
}
