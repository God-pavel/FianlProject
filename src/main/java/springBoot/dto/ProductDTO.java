package springBoot.dto;

import lombok.*;
import springBoot.entity.enums.ProductType;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductDTO {
    private Long id;
    private String name;
    private Double price;
    private ProductType productType;
    private Long amount;
}