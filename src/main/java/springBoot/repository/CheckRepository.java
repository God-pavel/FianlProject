package springBoot.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springBoot.entity.Check;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Repository
public interface CheckRepository extends JpaRepository<Check, Long> {
    Page<Check> findAll(Pageable pageable);
}


