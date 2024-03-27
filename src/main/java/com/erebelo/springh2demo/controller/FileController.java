package com.erebelo.springh2demo.controller;

import com.erebelo.springh2demo.domain.response.FileResponseDTO;
import com.erebelo.springh2demo.service.FileService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

import static com.erebelo.springh2demo.constant.BusinessConstant.FILE;

@Validated
@RestController
@RequestMapping(FILE)
@RequiredArgsConstructor
public class FileController {

    private final FileService service;

    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);
    private static final String FILE_RESPONSE = "File response: {}";

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FileResponseDTO>> getFiles() {
        LOGGER.info("Getting files");
        var response = service.getFiles();

        LOGGER.info(FILE_RESPONSE, response);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> getFileById(@PathVariable Long id) {
        LOGGER.info("Getting file by id: {}", id);
        var response = service.getFileById(id);

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", response.getName());
        headers.setContentLength(response.getData().length);

        LOGGER.info(FILE_RESPONSE, response.getName());
        return ResponseEntity.ok().headers(headers).body(response.getData());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> insertFile(@RequestParam("file") MultipartFile file) {
        LOGGER.info("Inserting file - Request body: {}", file.getOriginalFilename());
        var response = service.insertFile(file);

        LOGGER.info(FILE_RESPONSE, response.getName());
        var uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}
