package springBoot.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springBoot.entity.Check;


@Repository
public interface CheckRepository extends JpaRepository<Check, Long> {

}


