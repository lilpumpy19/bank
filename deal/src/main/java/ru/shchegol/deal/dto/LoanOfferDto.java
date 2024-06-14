package ru.shchegol.deal.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class LoanOfferDto {

    @Column(name = "statement_id",updatable = false,insertable = false)
    @Schema(description = "Id of the loan statement", example = "0107605e-f116-4eb9-bd3d-a0e8bd90fdb6")
    private UUID statementId;

    @Schema(description = "Requested loan amount", example = "50000")
    @DecimalMin(value = "30000")
    private BigDecimal requestedAmount;

    @Schema(description = "Total loan amount", example = "63727.44")
    @DecimalMin(value = "30000")
    private BigDecimal totalAmount;

    @Schema(description = "Loan term in months", example = "12")
    @Min(0)
    private Integer term;

    @Schema(description = "Monthly payment", example = "5310.62")
    @Min(0)
    private BigDecimal monthlyPayment;

    @Schema(description = "Rate", example = "11.0")
    @Min(0)
    private BigDecimal rate;

    @Schema(description = "Insurance enabled", example = "true")
    @NotNull
    private Boolean isInsuranceEnabled;

    @Schema(description = "is Salary client", example = "true")
    @NotNull
    private Boolean isSalaryClient;

    public void setInsuranceEnabled(boolean isInsuranceEnabled) {
        this.isInsuranceEnabled = isInsuranceEnabled;
    }

    public void setSalaryClient(boolean isSalaryClient) {
        this.isSalaryClient = isSalaryClient;
    }
}
