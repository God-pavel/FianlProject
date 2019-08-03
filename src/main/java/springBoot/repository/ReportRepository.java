package springBoot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springBoot.entity.Report;

import java.time.LocalDate;
import java.util.Set;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Set<Report> findByDate(LocalDate date);
}
