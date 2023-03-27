package com.erebelo.springh2demo.service.impl;

import com.erebelo.springh2demo.domain.response.FileResponse;
import com.erebelo.springh2demo.domain.response.FileResponseDTO;
import com.erebelo.springh2demo.exception.StandardException;
import com.erebelo.springh2demo.mapper.FileMapper;
import com.erebelo.springh2demo.repository.FileDataRepository;
import com.erebelo.springh2demo.repository.FileRepository;
import com.erebelo.springh2demo.service.FileService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.erebelo.springh2demo.exception.ErrorEnum.ERROR_404_001;
import static com.erebelo.springh2demo.exception.ErrorEnum.ERROR_404_004;
import static com.erebelo.springh2demo.exception.ErrorEnum.ERROR_409_002;
import static com.erebelo.springh2demo.exception.ErrorEnum.ERROR_422_001;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileMapper mapper;
    private final FileRepository fileRepository;
    private final FileDataRepository fileDataRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);
    private static final String RESPONSE_BODY_LOGGER = "Response body: {}";
    private static final String FILE_MSG = "File";

    @Override
    public List<FileResponseDTO> getFiles() {
        LOGGER.info("Getting files");
        var fileEntityList = fileRepository.findAll();

        if (fileEntityList.isEmpty()) {
            throw new StandardException(ERROR_404_004, FILE_MSG);
        }

        LOGGER.info(RESPONSE_BODY_LOGGER, fileEntityList);
        return mapper.entityListToResponseDTOList(fileEntityList);
    }

    @Override
    public FileResponse getFileById(Long id) {
        LOGGER.info("Getting file by id: {}", id);
        var fileDataEntity = fileDataRepository.findById(id).orElseThrow(() ->
                new StandardException(ERROR_404_001, FILE_MSG, id));

        LOGGER.info(RESPONSE_BODY_LOGGER, fileDataEntity.getFile().getName());
        return mapper.fileDataEntityToResponse(fileDataEntity);
    }

    @Override
    @Transactional
    public FileResponseDTO insertFile(MultipartFile file) {
        var dataBytes = extractFileBytes(file);
        var filename = file.getOriginalFilename();

        if (dataBytes.length == 0 || !validFilename(filename)) {
            throw new StandardException(ERROR_422_001);
        }

        LOGGER.info("Checking whether file object exists by name: {}", filename);
        fileRepository.findByName(filename).ifPresent(o -> {
            throw new StandardException(ERROR_409_002, FILE_MSG, filename);
        });

        LOGGER.info("Inserting file information: {}", filename);
        var fileEntity = mapper.fileToEntity(filename);
        fileEntity = fileRepository.save(fileEntity);

        LOGGER.info("Inserting file data");
        var fileDataEntity = mapper.bytesToFileDataEntity(fileEntity, dataBytes);
        fileDataRepository.save(fileDataEntity);

        LOGGER.info(RESPONSE_BODY_LOGGER, fileEntity.getName());
        return mapper.entityToResponseDTO(fileEntity);
    }

    private byte[] extractFileBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("Error handling file: %s", file.getName()), e);
        }
    }

    private boolean validFilename(String filename) {
        try {
            if (filename != null && !filename.trim().equals("")) {
                String[] breakdown = filename.split("\\.");
                return breakdown.length == 2 && !breakdown[0].trim().equals("");
            }
            return true;
        } catch (Exception e) {
            LOGGER.error(String.format("Exception not thrown when validating the filename: %s", filename), e);
            return false;
        }
    }
}
