package ru.shchegol.deal.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import ru.shchegol.deal.dto.PaymentScheduleElementDto;
import ru.shchegol.dto.enums.CreditStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "credit")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Credit {
    @Id
    @Column(name = "credit_id")
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID creditId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "term")
    private Integer term;

    @Column(name = "monthly_payment")
    private BigDecimal monthlyPayment;

    @Column(name = "rate")
    private BigDecimal rate;

    @Column(name = "psk")
    private BigDecimal psk;

    @Column(columnDefinition = "jsonb",name = "payment_schedule")
    @ElementCollection
    private List<PaymentScheduleElementDto> paymentSchedule;

    @Column(name = "insurance_enabled")
    private boolean insuranceEnabled;

    @Column(name = "salary_client")
    private boolean salaryClient;

    @Column(name = "credit_status")
    @Enumerated(EnumType.STRING)
    private CreditStatus creditStatus;

}
