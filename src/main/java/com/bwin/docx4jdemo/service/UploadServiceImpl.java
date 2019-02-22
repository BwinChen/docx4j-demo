package com.bwin.docx4jdemo.service;

import com.bwin.docx4jdemo.util.Docx4jUtil;
import com.bwin.docx4jdemo.util.PoiUtil;
import com.bwin.docx4jdemo.util.RarUtil;
import com.bwin.docx4jdemo.util.ZipUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

@Slf4j
@Service
public class UploadServiceImpl implements UploadService{

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void upload(MultipartFile multipartFile) throws IOException {
        File target = new File("D:\\Test\\upload\\" + multipartFile.getOriginalFilename());
        multipartFile.transferTo(target);
        String path = target.getAbsolutePath();
        String extension = FilenameUtils.getExtension(path);
        // 压缩文件
        if (extension.equalsIgnoreCase("rar")) {
            RarUtil.unRar(path, target.getParent() + File.separator);
        }
        if (extension.equalsIgnoreCase("zip")) {
            ZipUtil.unZip(path, target.getParent() + File.separator);
        }
        // microsoft office文档
        if (extension.equalsIgnoreCase("docx")) {
            Docx4jUtil.saveDocxImage(path, target.getParent() + File.separator);
        }
        if (extension.equalsIgnoreCase("pptx")) {
            Docx4jUtil.savePptxImage(path, target.getParent() + File.separator);
        }
        if (extension.equalsIgnoreCase("xlsx")) {
            Docx4jUtil.saveXlsxImage(path, target.getParent() + File.separator);
        }
        if (extension.equalsIgnoreCase("doc")) {
            PoiUtil.saveDocImage(path, target.getParent() + File.separator);
        }
        if (extension.equalsIgnoreCase("ppt")) {
            PoiUtil.savePptImage(path, target.getParent() + File.separator);
        }
        if (extension.equalsIgnoreCase("xls")) {
            PoiUtil.saveXlsImage(path, target.getParent() + File.separator);
        }
        String[] extensions = new String[]{"png", "jpg"};
        Collection<File> files = FileUtils.listFiles(target.getParentFile(), extensions, true);
        for (File file : files) {
            log.info(file.getName());
        }

        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("orderType", 5);
        parameters.add("userId", 1);
        parameters.add("jobId", 1);
        parameters.add("picCount", 10);
        parameters.add("copyrightType", 2);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(parameters, headers);
        ResponseEntity<String> response = restTemplate.exchange(String.format("http://%s:%d/%s", "192.168.2.31", 8080, "yuanyin_order/order/torder/add"), HttpMethod.POST, requestEntity, String.class);
        response.getStatusCodeValue();
    }

}
