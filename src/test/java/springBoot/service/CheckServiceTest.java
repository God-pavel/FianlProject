package springBoot.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import springBoot.entity.Product;
import springBoot.entity.TemporaryCheck;
import springBoot.entity.User;
import springBoot.entity.enums.ProductType;
import springBoot.repository.TemporaryCheckRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CheckServiceTest {
    @Autowired
    private CheckService checkService;
    @MockBean
    private TemporaryCheckRepository temporaryCheckRepository;
    @MockBean
    private ProductService productService;

    @Test
    public void createCheck(){
        User user = new User();
        user.setUsername("Ivan");

        TemporaryCheck check = checkService.createCheck(user);
        TemporaryCheck correctCheck = TemporaryCheck
                .builder()
                .userName("Ivan")
                .productAmount(new HashMap<>())
                .time(check.getTime())
                .total(new BigDecimal(0.0)
                        .setScale(2, RoundingMode.HALF_UP))
                .build();

        Assert.assertEquals(correctCheck,check);
        Mockito.verify(temporaryCheckRepository, Mockito.times(1)).save(check);

    }

    @Test
    public void addProductToCheckByName() {
        TemporaryCheck correctCheck = TemporaryCheck
                .builder()
                .id(1L)
                .userName("Ivan")
                .productAmount(new HashMap<>())
                .time(LocalDateTime.now())
                .total(new BigDecimal(0.0)
                        .setScale(2, RoundingMode.HALF_UP))
                .build();
        Product product = Product.builder().id(10L).amount(100L).name("Lemon").price(new BigDecimal(10)).productType(ProductType.QUANTITY).build();

        Mockito.doReturn(Optional.of(correctCheck))
                .when(temporaryCheckRepository)
                .findById(1L);

        Mockito.doReturn(true)
                .when(productService)
                .checkProductAvailabilityByName("Lemon",10L);

        Mockito.doReturn(product)
                .when(productService)
                .getProductByName("Lemon");

        checkService.addProductToCheckByName(1L,"Lemon",10L);

        Assert.assertTrue(!correctCheck.getProductAmount().isEmpty());
        Assert.assertEquals((Long)10L,correctCheck.getProductAmount().get(product));
        Mockito.verify(temporaryCheckRepository, Mockito.times(1)).save(correctCheck);
    }

    @Test
    public void addProductToCheckById() {
        TemporaryCheck correctCheck = TemporaryCheck
                .builder()
                .id(1L)
                .userName("Ivan")
                .productAmount(new HashMap<>())
                .time(LocalDateTime.now())
                .total(new BigDecimal(0.0)
                        .setScale(2, RoundingMode.HALF_UP))
                .build();
        Product product = Product.builder().id(10L).amount(100L).name("Lemon").price(new BigDecimal(10)).productType(ProductType.QUANTITY).build();

        Mockito.doReturn(Optional.of(correctCheck))
                .when(temporaryCheckRepository)
                .findById(1L);

        Mockito.doReturn(true)
                .when(productService)
                .checkProductAvailabilityById(1L,10L);

        Mockito.doReturn(product)
                .when(productService)
                .getProductById(1L);

        checkService.addProductToCheckById(1L,1L,10L);

        Assert.assertTrue(!correctCheck.getProductAmount().isEmpty());
        Assert.assertEquals((Long)10L,correctCheck.getProductAmount().get(product));
        Mockito.verify(temporaryCheckRepository, Mockito.times(1)).save(correctCheck);
    }

}