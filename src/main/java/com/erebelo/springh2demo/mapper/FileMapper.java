package com.erebelo.springh2demo.mapper;

import com.erebelo.springh2demo.domain.entity.FileDataEntity;
import com.erebelo.springh2demo.domain.entity.FileEntity;
import com.erebelo.springh2demo.domain.response.FileResponse;
import com.erebelo.springh2demo.domain.response.FileResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

import static org.mapstruct.ReportingPolicy.WARN;

@Mapper(componentModel = "spring", unmappedTargetPolicy = WARN)
public interface FileMapper {

    List<FileResponseDTO> entityListToResponseDTOList(List<FileEntity> entity);

    FileResponseDTO entityToResponseDTO(FileEntity entity);

    @Mapping(target = "id", source = "fileDataEntity.file.id")
    @Mapping(target = "name", source = "fileDataEntity.file.name")
    FileResponse fileDataEntityToResponse(FileDataEntity fileDataEntity);

    @Mapping(target = "name", source = "fileName")
    FileEntity fileToEntity(String fileName);

    @Mapping(target = "file", source = "entity")
    @Mapping(target = "data", source = "bytes")
    FileDataEntity bytesToFileDataEntity(FileEntity entity, byte[] bytes);

}
