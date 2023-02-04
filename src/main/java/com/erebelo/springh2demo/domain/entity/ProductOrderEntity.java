package com.erebelo.springh2demo.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "product_orders")
public class ProductOrderEntity implements Serializable {

    @EmbeddedId
    private ProductOrderPK id; // TODO =  new ProductOrderPK();

    private BigDecimal price;
    private Integer amount;
    private BigDecimal discount;

    public ProductOrderEntity(ProductEntity product, OrderEntity order, BigDecimal price, Integer amount, BigDecimal discount) {
        id.setProduct(product);
        id.setOrder(order);
        this.price = price;
        this.amount = amount;
        this.discount = discount;
    }

    public ProductEntity getProduct() {
        return id.getProduct();
    }

    public OrderEntity getOrder() {
        return id.getOrder();
    }
}
