package com.erebelo.springh2demo.service.impl;

import com.erebelo.springh2demo.domain.response.FileResponse;
import com.erebelo.springh2demo.domain.response.FileResponseDTO;
import com.erebelo.springh2demo.exception.StandardException;
import com.erebelo.springh2demo.mapper.FileMapper;
import com.erebelo.springh2demo.repository.FileRepository;
import com.erebelo.springh2demo.service.FileService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.erebelo.springh2demo.exception.ErrorEnum.ERROR_404_001;
import static com.erebelo.springh2demo.exception.ErrorEnum.ERROR_404_004;
import static com.erebelo.springh2demo.exception.ErrorEnum.ERROR_409_002;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileMapper mapper;
    private final FileRepository repository;

    private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);
    private static final String RESPONSE_BODY_LOGGER = "Response body: {}";
    private static final String FILE_MSG = "File";

    @Override
    public List<FileResponseDTO> getFiles() {
        LOGGER.info("Getting files");
        var fileEntityList = repository.findAll();

        if (fileEntityList.isEmpty()) {
            throw new StandardException(ERROR_404_004, FILE_MSG);
        }

        LOGGER.info(RESPONSE_BODY_LOGGER, fileEntityList);
        return mapper.entityListToResponseDTOList(fileEntityList);
    }

    @Override
    public FileResponse getFileById(Long id) {
        LOGGER.info("Getting file by id: {}", id);
        var fileEntity = repository.findById(id).orElseThrow(() ->
                new StandardException(ERROR_404_001, FILE_MSG, id));

        LOGGER.info(RESPONSE_BODY_LOGGER, fileEntity.getName());
        return mapper.entityToResponse(fileEntity);
    }

    @Override
    @Transactional
    public FileResponseDTO insertFile(MultipartFile file) {
        try {
            // TODO throw separated excetions, check lazy method
            var fileName = file.getOriginalFilename();
            LOGGER.info("Checking whether file object exists by name: {}", fileName);
            repository.findByName(fileName).ifPresent(o -> {
                throw new StandardException(ERROR_409_002, FILE_MSG, fileName);
            });

            var fileEntity = mapper.fileToEntity(fileName, file.getBytes());
            LOGGER.info("Inserting file: {}", fileEntity.getName());
            fileEntity = repository.save(fileEntity);

            LOGGER.info(RESPONSE_BODY_LOGGER, fileEntity.getName());
            return mapper.entityToResponseDTO(fileEntity);
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("Error handling file: %s", file.getName()), e);
        }
    }
}
