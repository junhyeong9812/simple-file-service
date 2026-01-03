package com.simple.fileservice;

import io.github.junhyeong9812.streamix.starter.annotation.EnableStreamix;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Simple File Service - Streamix 레퍼런스 구현체.
 *
 * <p>Streamix v2.0.0의 모든 기능을 사용하는 예제 애플리케이션입니다.</p>
 *
 * <h2>지원 파일 타입 (v2.0.0)</h2>
 * <ul>
 *   <li>IMAGE - 이미지 파일 (jpg, png, gif, webp)</li>
 *   <li>VIDEO - 비디오 파일 (mp4, webm, avi)</li>
 *   <li>AUDIO - 오디오 파일 (mp3, wav, flac)</li>
 *   <li>DOCUMENT - 문서 파일 (pdf, doc, xlsx)</li>
 *   <li>ARCHIVE - 압축 파일 (zip, rar, 7z)</li>
 *   <li>OTHER - 기타 파일</li>
 * </ul>
 *
 * @author junhyeong9812
 * @since 1.0.0
 */
@SpringBootApplication
@EnableStreamix
public class FileserviceApplication {

  public static void main(String[] args) {
    SpringApplication.run(FileserviceApplication.class, args);
  }
}