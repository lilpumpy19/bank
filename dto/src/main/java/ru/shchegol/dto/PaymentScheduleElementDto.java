package ru.shchegol.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentScheduleElementDto {
    @Schema(description = "Payment number", example = "1")
    private Integer number;

    @Schema(description = "Payment date", example = "2024-07-14")
    private LocalDate date;

    @Schema(description = "payment in this number", example = "5181.33")
    private BigDecimal totalPayment;

    @Schema(description = "interest payment", example = "348.0000")
    private BigDecimal interestPayment;

    @Schema(description = "debt payment", example = "4833.3300")
    private BigDecimal debtPayment;

    @Schema(description = "remaining debt", example = "55166.6700")
    private BigDecimal remainingDebt;
}
