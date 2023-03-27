package com.erebelo.springh2demo.repository;

import com.erebelo.springh2demo.domain.entity.FileDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDataRepository extends JpaRepository<FileDataEntity, Long> {

}
