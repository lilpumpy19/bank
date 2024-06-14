package ru.shchegol.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CreditDto {
    private BigDecimal amount;
    private Integer term;
    private BigDecimal monthlyPayment;
    private BigDecimal baseRate;
    private BigDecimal rate;
    private BigDecimal psk;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
    private List<PaymentScheduleElementDto> paymentSchedule;

    public CreditDto(BigDecimal baseRate) {
        this.rate = baseRate;
        this.baseRate = baseRate;
        this.paymentSchedule = new ArrayList<PaymentScheduleElementDto>();
    }

    public void changeRate(BigDecimal rate) {
        this.rate = this.rate.add(rate);
    }

    public void addPaymentScheduleElement(PaymentScheduleElementDto element) {
        this.paymentSchedule.add(element);
    }
}

