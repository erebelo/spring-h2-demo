package com.erebelo.springh2demo.service.impl;

import com.erebelo.springh2demo.domain.response.FileResponse;
import com.erebelo.springh2demo.domain.response.FileResponseDTO;
import com.erebelo.springh2demo.exception.model.ConflictException;
import com.erebelo.springh2demo.exception.model.NotFoundException;
import com.erebelo.springh2demo.exception.model.UnprocessableEntityException;
import com.erebelo.springh2demo.mapper.FileMapper;
import com.erebelo.springh2demo.repository.FileDataRepository;
import com.erebelo.springh2demo.repository.FileRepository;
import com.erebelo.springh2demo.service.FileService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileMapper mapper;
    private final FileRepository fileRepository;
    private final FileDataRepository fileDataRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);

    private static final String RESPONSE_BODY_LOGGER = "Response body: {}";

    @Override
    public List<FileResponseDTO> getFiles() {
        LOGGER.info("Getting files");
        var fileEntityList = fileRepository.findAll();

        if (fileEntityList.isEmpty()) {
            throw new NotFoundException("File not found");
        }

        LOGGER.info(RESPONSE_BODY_LOGGER, fileEntityList);
        return mapper.entityListToResponseDTOList(fileEntityList);
    }

    @Override
    public FileResponse getFileById(Long id) {
        LOGGER.info("Getting file by id: {}", id);
        var fileDataEntity = fileDataRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("File not found by id: %s", id)));

        LOGGER.info(RESPONSE_BODY_LOGGER, fileDataEntity.getFile().getName());
        return mapper.fileDataEntityToResponse(fileDataEntity);
    }

    @Override
    @Transactional
    public FileResponseDTO insertFile(MultipartFile file) {
        var dataBytes = extractFileBytes(file);
        var filename = file.getOriginalFilename();

        if (dataBytes.length == 0 || !isValidFilename(filename)) {
            throw new UnprocessableEntityException("Invalid filename and/or content");
        }

        LOGGER.info("Checking whether file object exists by name: {}", filename);
        fileRepository.findByName(filename).ifPresent(o -> {
            throw new ConflictException(String.format("File already exists by name: %s", filename));
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

    private boolean isValidFilename(String filename) {
        try {
            if (!ObjectUtils.isEmpty(filename)) {
                String[] breakdown = filename.split("\\.");
                return breakdown.length == 2 && !ObjectUtils.isEmpty(breakdown[0]);
            }
            return true;
        } catch (Exception e) {
            LOGGER.warn(String.format("Exception not thrown when validating the filename: %s", filename), e);
            return false;
        }
    }
}
