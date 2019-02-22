package com.bwin.docx4jdemo.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadService {

    void upload(MultipartFile multipartFile) throws IOException;

}
