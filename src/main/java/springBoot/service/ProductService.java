package springBoot.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import springBoot.dto.ProductDTO;
import springBoot.entity.Product;
import springBoot.repository.ProductRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class ProductService {

    private final ProductRepository productRepository;


    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    private ProductDTO productConvert(Product product) {
        return new ProductDTO(product.getId(), product.getName(), product.getPrice().doubleValue(), product.getProductType()
                , product.getAmount());
    }


    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::productConvert)
                .collect(Collectors.toList());
    }

    Product getProductByName(String name) {
        return productRepository.findByName(name).orElseThrow(() ->
                new IllegalArgumentException("There are no product with such name: " + name));
    }


    Product getProductById(long id) {
        return productRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("There are no product with such id: " + id));
    }


    public boolean addProduct(ProductDTO productdto) {
        try {
            Product productFromDb = getProductByName(productdto.getName());
            log.warn("Product already exist at storage!");
            return false;
        } catch (IllegalArgumentException e) {
            Product product = Product
                    .builder()
                    .name(productdto.getName())
                    .price(new BigDecimal(productdto.getPrice())
                            .setScale(2, RoundingMode.HALF_UP))
                    //TODO improve types
                    .productType(productdto.getProductType())
                    .amount(productdto.getAmount())
                    .build();
            productRepository.save(product);
            log.info("Product was saved. Product name: " + product.getName());
            return true;
        }
    }

    public void editProduct(Product product, BigDecimal price, Long amount) {
        product.setPrice(price);
        product.setAmount(amount);
        productRepository.save(product);
        log.info(product.getName() + " was edited. New price: " + product.getPrice());
    }

    boolean checkProductAvailabilityByName(String name, Long amount) throws IllegalArgumentException {

        return getProductByName(name).getAmount() >= amount;
    }

    boolean checkProductAvailabilityById(Long id, Long amount) throws IllegalArgumentException {

        return getProductById(id).getAmount() >= amount;
    }

    void takeAway(Product product, Long amount) throws IllegalArgumentException {
        product.setAmount(product.getAmount() - amount);
        productRepository.save(product);
        log.info("Was taked" + product.getName() + " " + amount
                + ".\n Left " + product.getAmount() + product.getName());
    }

    void takeBack(Product product, Long amount) throws IllegalArgumentException {
        product.setAmount(product.getAmount() + amount);
        productRepository.save(product);
        log.info("Was returned" + product.getName() + " " + amount
                + ".\n Left " + product.getAmount() + product.getName());
    }


}
