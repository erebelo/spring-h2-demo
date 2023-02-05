package com.erebelo.springh2demo.domain.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductOrderDTO {

    @NotNull(message = "amount is mandatory")
    @Min(value = 1, message = "amount must be greater than or equal to 1")
    private Integer amount;

    @NotNull(message = "discount is mandatory")
    @DecimalMin(value = "0", message = "discount must be greater than or equal to 0")
    private BigDecimal discount;

    @Valid
    @NotNull(message = "product is mandatory")
    private ProductDTO product;

}
