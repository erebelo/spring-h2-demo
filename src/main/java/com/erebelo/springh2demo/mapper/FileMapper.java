package com.erebelo.springh2demo.mapper;

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

    FileResponse entityToResponse(FileEntity entity);

    @Mapping(target = "name", source = "fileName")
    @Mapping(target = "data", source = "bytes")
    FileEntity fileToEntity(String fileName, byte[] bytes);

}
