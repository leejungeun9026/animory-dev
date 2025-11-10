package com.four.animory.controller.sitter;

import com.four.animory.dto.sitter.SitterFileDTO;
import com.four.animory.dto.sitter.file.SitterUploadFileDTO;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.nio.file.Files;

@Log4j2
@Controller
@RequestMapping("/sitter")
public class SitterFileController {
  @Value("${com.four.animory.upload.path}")
  private String uploadRootPath;

  @GetMapping("/view/{filename}")
  public ResponseEntity<Resource> viewFileGet(@PathVariable("filename") String filename){
    String uploadPath = uploadRootPath + "\\sitter";
    Resource resource = new FileSystemResource(uploadPath + File.separator + filename);
    String resourceName = resource.getFilename();
    log.info("resourceName...... : " + resourceName);
    HttpHeaders headers = new HttpHeaders();
    try {
      headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.internalServerError().build();
    }
    log.info(ResponseEntity.ok().headers(headers).body(resource));
    return ResponseEntity.ok().headers(headers).body(resource);
  }


}
