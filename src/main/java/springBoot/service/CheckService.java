package springBoot.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import springBoot.entity.Check;
import springBoot.entity.Product;
import springBoot.entity.TemporaryCheck;
import springBoot.entity.User;
import springBoot.exception.CheckCantBeDeleted;
import springBoot.exception.NotEnoughProductsException;
import springBoot.repository.CheckRepository;
import springBoot.repository.TemporaryCheckRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Service
public class CheckService {

    private final CheckRepository checkRepository;
    private final TemporaryCheckRepository temporaryCheckRepository;
    private final ProductService productService;
    private final UserService userService;


    public CheckService(CheckRepository checkRepository, TemporaryCheckRepository temporaryCheckRepository, ProductService productService, UserService userService) {
        this.checkRepository = checkRepository;
        this.temporaryCheckRepository = temporaryCheckRepository;
        this.productService = productService;
        this.userService = userService;

    }

    public Page<Check> getAllChecks(Pageable pageable) {

        return checkRepository.findAll(pageable);
    }

    List<Check> getAllChecks() {

        return checkRepository.findAll();
    }

    public TemporaryCheck getTemporaryCheckById(long id) {

        return temporaryCheckRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("There are no check with such id: " + id));
    }

    private Check getCheckById(long id) {

        return checkRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("There are no check with such id: " + id));
    }

    void deleteUncompletedChecks() {
        temporaryCheckRepository.findAll()
                .forEach(temporaryCheckRepository::delete);
    }

    public TemporaryCheck createCheck(User user) {

        String username = user.getUsername();
        TemporaryCheck check = TemporaryCheck
                .builder()
                .userName(username)
                .productAmount(new HashMap<>())
                .time(LocalDateTime.now())
                .total(new BigDecimal(0.0)
                        .setScale(2, RoundingMode.HALF_UP))
                .build();
        temporaryCheckRepository.save(check);
        return check;
    }

    public void addProductToCheckByName(Long checkId, String name, Long amount) throws NotEnoughProductsException {
        TemporaryCheck check = getTemporaryCheckById(checkId);
        Map<Product, Long> products = check.getProductAmount();

        if (products.containsKey(productService.getProductByName(name))) {
            Long newAmount = products.get(productService.getProductByName(name)) + amount;
            products.replace(productService.getProductByName(name), newAmount);
        } else {
            products.put(productService.getProductByName(name), amount);
        }
        if (productService.checkProductAvailabilityByName(name,
                products.get(productService.getProductByName(name)))) {
            check.setTotal(new BigDecimal(calcTotal(products)));
            temporaryCheckRepository.save(check);
            log.info("product was added to check");
        } else {
            log.warn("Not enough products in storage!");
            throw new NotEnoughProductsException("Not enough products in storage");
        }
    }

    public void addProductToCheckById(Long checkId, Long id, Long amount) throws NotEnoughProductsException {
        TemporaryCheck check = getTemporaryCheckById(checkId);
        Map<Product, Long> products = check.getProductAmount();

        if (products.containsKey(productService.getProductById(id))) {
            Long newAmount = products.get(productService.getProductById(id)) + amount;
            products.replace(productService.getProductById(id), newAmount);
        } else {
            products.put(productService.getProductById(id), amount);
        }
        if (productService.checkProductAvailabilityById(id,
                products.get(productService.getProductById(id)))) {
            check.setTotal(new BigDecimal(calcTotal(products)));
            temporaryCheckRepository.save(check);
            log.info("product was added to check");
        } else {
            log.warn("Not enough products in storage!");
            throw new NotEnoughProductsException("Not enough products in storage");
        }
    }

    private double calcHelper(Product product, Long amount) {
        return product.getPrice().doubleValue() * amount;
    }

    private double calcTotal(Map<Product, Long> products) {
        List<Double> sums = products.keySet().stream()
                .map(key -> calcHelper(key, products.get(key)))
                .collect(Collectors.toList());
        return sums.stream()
                .reduce(0.0, (x, y) -> x + y);
    }
    public void closeCheck(Long checkId) throws NotEnoughProductsException{
        log.info("at close check method");
        TemporaryCheck temporaryCheck = getTemporaryCheckById(checkId);
        log.info("temp check: " + temporaryCheck);
        Map<Product, Long> products = getTemporaryCheckById(temporaryCheck.getId()).getProductAmount();
        temporaryCheckRepository.delete(temporaryCheck);
        products.forEach(productService::takeAway);
        Check check = Check.builder()
                .user(userService.findUserByUsername(temporaryCheck.getUserName()))
                .productAmount(products)
                .time(temporaryCheck.getTime())
                .total(temporaryCheck.getTotal())
                .toDelete(true)
                .build();
        log.info("check to save: " + check);
        if (check.getTotal().compareTo(new BigDecimal(0)) == 0) {
            return;
        }
        checkRepository.save(check);
        log.info("Check was saved. Check id: " + check.getId());

    }

    void saveCheck(Check check) {
        checkRepository.save(check);
    }


    public void deleteCheck(Long id) throws CheckCantBeDeleted {
        log.info(getCheckById(id).isToDelete());
        if (!getCheckById(id).isToDelete()) {
            throw new CheckCantBeDeleted("This check already in report so it cant be deleted!");
        } else {
            log.info("products to delete: " + getCheckById(id).getProductAmount());
            getCheckById(id).getProductAmount().forEach(productService::takeBack);
            checkRepository.delete(getCheckById(id));
            log.info("Check was deleted.");
        }
    }


    public void deleteProductFromCheck(Long checkId, String name) throws CheckCantBeDeleted {
        Check check = getCheckById(checkId);
        if (!check.isToDelete()) {
            throw new CheckCantBeDeleted("This check already in report so product cant be deleted!");
        } else {
            Product productToDelete = productService.getProductByName(name);
            Long amountToDelete = check.getProductAmount().get(productToDelete);
            check.getProductAmount().remove(productToDelete);
            if (check.getProductAmount().isEmpty()) {
                productService.takeBack(productToDelete,amountToDelete);
                deleteCheck(checkId);
            } else {
                check.setTotal(new BigDecimal(calcTotal(check.getProductAmount())));
                checkRepository.save(check);
                productService.takeBack(productToDelete, amountToDelete);
            }
        }

    }

}
