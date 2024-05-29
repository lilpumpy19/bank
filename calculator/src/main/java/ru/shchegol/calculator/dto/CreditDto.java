package ru.shchegol.calculator.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Data
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
    }

    public void setRate(BigDecimal rate) {
        this.rate =this.rate.add(rate);
        System.out.println("setRate: " + this.rate+" "+rate);
    }
}

