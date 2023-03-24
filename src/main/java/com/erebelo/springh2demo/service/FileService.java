package com.erebelo.springh2demo.service;

import com.erebelo.springh2demo.domain.response.FileResponse;
import com.erebelo.springh2demo.domain.response.FileResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    List<FileResponseDTO> getFiles();

    FileResponse getFileById(Long id);

    FileResponseDTO insertFile(MultipartFile file);

}
