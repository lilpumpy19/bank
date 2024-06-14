package ru.shchegol.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CreditDto {
    @Schema(description = "Requested amount", example = "62175.96")
    private BigDecimal amount;

    @Schema(description = "Term in months", example = "12")
    private Integer term;

    @Schema(description = "Monthly payment", example = "5181.33")
    private BigDecimal monthlyPayment;

    @Schema(description = "Base rate", example = "15")
    private BigDecimal baseRate;

    @Schema(description = "Rate", example = "17")
    private BigDecimal rate;

    @Schema(description = "PSK", example = "24.3500")
    private BigDecimal psk;

    @Schema(description = "Is insurance enabled", example = "true")
    private Boolean isInsuranceEnabled;

    @Schema(description = "Is salary client", example = "true")
    private Boolean isSalaryClient;

    @Schema(description = "Payment schedule")
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

