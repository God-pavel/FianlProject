package springBoot.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "temporary_checks")

public class TemporaryCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "temporary_id", nullable = false)
    private Long id;

    @Column(name = "temporary_user")
    private String userName;

    @Column(name = "temporary_time", nullable = false)
    private LocalDateTime time;

    @Column(name = "temporary_price", nullable = false)
    private BigDecimal total;

    @ElementCollection
    @CollectionTable(name = "temporaryCheck_products_mapping",
            joinColumns = {@JoinColumn(name = "temporaryCheck_id", referencedColumnName = "temporary_id")})
    @Column(name = "amount")
    private Map<Product, Long> productAmount;

}
