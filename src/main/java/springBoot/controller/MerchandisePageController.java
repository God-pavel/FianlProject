package springBoot.controller;


import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springBoot.dto.ProductDTO;
import springBoot.entity.enums.ProductType;
import springBoot.service.ProductService;

import java.util.Map;

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
    public String allProducts(Map<String, Object> model) {
        model.put("products", productService.getAllProducts());
        model.put("types", ProductType.values());
        return "merchandise";
    }

    @PostMapping
    public String addProduct(ProductDTO productdto, Map<String, Object> model) {
        if (!productService.addProduct(productdto)) {
            model.put("products", productService.getAllProducts());
            model.put("types", ProductType.values());
            model.put("message", messageSource.getMessage("message.exist.product", null, LocaleContextHolder.getLocale()));
            return "merchandise";
        }


        return "redirect:/storage";
    }

}
