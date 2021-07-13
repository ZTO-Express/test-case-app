package com.zto.testcase.service;

import com.zto.testcase.dto.TcFileDto;
import com.zto.testcase.model.TcFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public interface FileUploadService {
    TcFile upload(MultipartFile file, String baseDir, TcFileDto tcFileDto) throws Exception;

    List<TcFile> uploadFiles(MultipartFile[] files, String baseDir, TcFileDto tcFileDto) throws Exception;

    String download(HttpServletResponse response,Integer id) throws IOException;

    TcFile download(Integer id) throws IOException;

    String delete(Integer id);

    String uploadTemplate(MultipartFile file,Integer type) throws Exception;

    String downloadTemplate(HttpServletResponse response,Integer type) throws Exception;

    String importExcel(MultipartFile file,String user) throws Exception;

    String importXMind(MultipartFile file,String user) throws Exception;

}
