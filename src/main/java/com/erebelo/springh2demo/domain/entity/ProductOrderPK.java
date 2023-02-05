package com.erebelo.springh2demo.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Getter
@Setter
@ToString(exclude = {"product", "order"})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Embeddable
public class ProductOrderPK implements Serializable {

    @ManyToOne
    @JoinColumn(name = "product_id")
    @EqualsAndHashCode.Include()
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @EqualsAndHashCode.Include()
    private OrderEntity order;

}
