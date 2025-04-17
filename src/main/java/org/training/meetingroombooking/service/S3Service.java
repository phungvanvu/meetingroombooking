package org.training.meetingroombooking.service;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
  String uploadFile(String keyName, MultipartFile file) throws IOException;
}
