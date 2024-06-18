package ru.shchegol.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class LoanOfferDto {
    @Schema(description = "Loan statement id", example = "c6408a51-f046-447b-915f-3028b00bf4a5")
    private UUID statementId;

    @Schema(description = "Requested amount", example = "50000")
    private BigDecimal requestedAmount;

    @Schema(description = "Total amount", example = "63727.44")
    private BigDecimal totalAmount;

    @Schema(description = "Term in months", example = "12")
    private Integer term;

    @Schema(description = "Monthly payment", example = "5310.62")
    private BigDecimal monthlyPayment;

    @Schema(description = "Rate", example = "11.0")
    private BigDecimal rate;

    @Schema(description = "Is insurance enabled", example = "true")
    private Boolean isInsuranceEnabled;

    @Schema(description = "Is salary client", example = "true")
    private Boolean isSalaryClient;

    public void setInsuranceEnabled(boolean isInsuranceEnabled) {
        this.isInsuranceEnabled = isInsuranceEnabled;
    }

    public void setSalaryClient(boolean isSalaryClient) {
        this.isSalaryClient = isSalaryClient;
    }
}
