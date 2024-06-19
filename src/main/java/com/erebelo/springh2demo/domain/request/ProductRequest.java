package com.erebelo.springh2demo.domain.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductRequest {

    @NotBlank(message = "name is mandatory")
    private String name;

    @NotNull(message = "price is mandatory")
    @DecimalMin(value = "0", message = "price must be greater than or equal to 0")
    @DecimalMax(value = "9999999.99", message = "price must be less than or equal to 9,999,999.99")
    private BigDecimal price;

}
