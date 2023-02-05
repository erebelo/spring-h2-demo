package com.erebelo.springh2demo.domain.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "product_orders")
public class ProductOrderEntity implements Serializable {

    @EmbeddedId
    @EqualsAndHashCode.Include()
    private ProductOrderPK id;

    @NotNull(message = "total is mandatory")
    @DecimalMin(value = "0", message = "total must be greater than or equal to 0")
    private BigDecimal total;

    @NotNull(message = "amount is mandatory")
    @Min(value = 1, message = "amount must be greater than or equal to 1")
    private Integer amount;

    @NotNull(message = "discount is mandatory")
    @DecimalMin(value = "0", message = "discount must be greater than or equal to 0")
    private BigDecimal discount;

    public ProductOrderEntity(ProductEntity product, OrderEntity order, BigDecimal total, Integer amount, BigDecimal discount) {
        id.setProduct(product);
        id.setOrder(order);
        this.total = total;
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
