package springBoot.controller;


import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springBoot.dto.ProductDTO;
import springBoot.entity.enums.ProductType;
import springBoot.service.ProductService;
import springBoot.validator.*;

import java.util.Map;

@Log4j2
@Controller
@RequestMapping("/merchandise")
public class MerchandisePageController {

    private final ProductService productService;
    private final MessageSource messageSource;

    public MerchandisePageController(ProductService productService, MessageSource messageSource) {
        this.productService = productService;
        this.messageSource = messageSource;
    }

    @GetMapping
    public String allTypes(Map<String, Object> model) {
        model.put("types", ProductType.values());
        return "merchandise";
    }

    @PostMapping
    public String addProduct(ProductDTO productdto, Map<String, Object> model) {
        log.info("productdto: " + productdto);

        Result checkDTO = new ProductDtoValidator(new PriceValidator(), new AmountValidator(), new ProductNameValidator()).validate(productdto);

        if (!checkDTO.isValid()) {
            model.put("message", checkDTO.getMessage());
            model.put("types", ProductType.values());
            log.warn(checkDTO.getMessage());
            return "merchandise";
        }



        if (!productService.addProduct(productdto)) {
            model.put("types", ProductType.values());
            model.put("message", messageSource.getMessage("message.exist.product", null, LocaleContextHolder.getLocale()));
            return "merchandise";
        }


        return "redirect:/storage";
    }

}
