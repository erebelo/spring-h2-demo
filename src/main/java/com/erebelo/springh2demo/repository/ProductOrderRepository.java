package com.erebelo.springh2demo.repository;

import com.erebelo.springh2demo.domain.entity.ProductOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrderEntity, Long> {

}
