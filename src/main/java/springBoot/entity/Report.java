package springBoot.entity;

import lombok.*;
import springBoot.entity.enums.ReportType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "date", nullable = false)
    private LocalDate date;
    @Column(name = "total", nullable = false)
    private BigDecimal total;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "checks_in_reports",
            joinColumns = @JoinColumn(name = "reports_id"),
            inverseJoinColumns = @JoinColumn(name = "checks_id"))
    private Set<Check> checks;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportType reportType;
}