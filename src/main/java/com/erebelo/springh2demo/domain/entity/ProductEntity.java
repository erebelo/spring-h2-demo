package com.erebelo.springh2demo.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "product")
public class ProductEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include()
    private Long id;

    @Column(unique = true)
    @NotBlank(message = "name is mandatory")
    private String name;

    @NotNull(message = "price is mandatory")
    @DecimalMin(value = "0", message = "price must be greater than or equal to 0")
    @DecimalMax(value = "9999999.99", message = "price must be less than or equal to 9,999,999.99")
    private BigDecimal price;

    @OneToMany(mappedBy = "id.product")
    private Set<ProductOrderEntity> productOrders;

    public List<OrderEntity> getOrders() {
        List<OrderEntity> orders = new ArrayList<>();
        for (ProductOrderEntity x : productOrders) {
            orders.add(x.getOrder());
        }
        return orders;
    }
}
