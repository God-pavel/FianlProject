package springBoot.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springBoot.entity.TemporaryCheck;

import java.math.BigDecimal;


@Repository
public interface TemporaryCheckRepository extends JpaRepository<TemporaryCheck, Long> {
    TemporaryCheck findByTotal(BigDecimal total);
}


