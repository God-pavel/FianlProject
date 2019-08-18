package springBoot.validator;

import springBoot.dto.ProductDTO;

import java.math.BigDecimal;

public class ProductDtoValidator implements Validator<ProductDTO> {

    private final PriceValidator priceValidator;
    private final AmountValidator amountValidator;
    private final ProductNameValidator productNameValidator;

    public ProductDtoValidator(PriceValidator priceValidator, AmountValidator amountValidator, ProductNameValidator productNameValidator) {
        this.priceValidator = priceValidator;
        this.amountValidator = amountValidator;
        this.productNameValidator = productNameValidator;
    }

    @Override
    public Result validate(ProductDTO dto) {
        if(!amountValidator.validate(dto.getAmount()).isValid()) return new Result(false,amountValidator.validate(dto.getAmount()).getMessage());
        if(!productNameValidator.validate(dto.getName()).isValid()) return new Result(false,productNameValidator.validate(dto.getName()).getMessage());
        if(!priceValidator.validate(new BigDecimal(dto.getPrice())).isValid()) return new Result(false,priceValidator.validate(new BigDecimal(dto.getPrice())).getMessage());

        return new Result(true);

    }
}
