package ru.shchegol.deal.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import ru.shchegol.deal.dto.LoanOfferDto;
import ru.shchegol.deal.entity.enums.ApplicationStatus;
import ru.shchegol.deal.entity.jsonb.StatusHistory;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "statement")
@Getter
@Setter
@AllArgsConstructor
public class Statement {
    @Id
    @Column(name = "statement_id")
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID statementId;

    @OneToOne
    @JoinColumn(name = "client_id", referencedColumnName = "client_id")
    private Client clientId;

    @OneToOne
    @JoinColumn(name = "credit_id", referencedColumnName = "credit_id")
    private Credit creditId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @Column(name = "creation_date")
    private Timestamp creationDate;

    @Column(columnDefinition = "jsonb",name = "applied_offer")
    @Embedded
    private LoanOfferDto appliedOffer;

    @Column(name = "sign_date")
    private Timestamp signDate;

    @Column(name = "ses_code")
    private Integer sesCode;

    @Column(columnDefinition = "jsonb",name = "status_history")
    @ElementCollection
    private List<StatusHistory> statusHistory;

    public Statement() {
        statusHistory = new ArrayList<>();
    }

    public void addStatusHistory(StatusHistory statusHistory){
        this.statusHistory.add(statusHistory);
    }
}
