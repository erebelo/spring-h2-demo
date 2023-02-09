package com.erebelo.springh2demo.repository;

import com.erebelo.springh2demo.domain.entity.CustomerEntity;
import com.erebelo.springh2demo.domain.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

    List<OrderEntity> findByCustomer(CustomerEntity customer);

    @Query(value = "SELECT O.ID FROM PRODUCT P" +
            " INNER JOIN PRODUCT_ORDERS PO ON PO.PRODUCT_ID = P.ID" +
            " INNER JOIN ORDERS O ON O.ID = PO.ORDER_ID" +
            " WHERE PRODUCT_ID = ?1", nativeQuery = true)
    List<Integer> nativeFindByProductId(Integer productId);

}
