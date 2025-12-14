package com.simple.fileservice;

import io.github.junhyeong9812.streamix.starter.annotation.EnableStreamix;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

@SpringBootApplication
@EnableStreamix
public class FileserviceApplication {

  public static void main(String[] args) {
    SpringApplication.run(FileserviceApplication.class, args);
  }
}