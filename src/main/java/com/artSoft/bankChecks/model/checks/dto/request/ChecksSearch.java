package com.artSoft.bankChecks.model.checks.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChecksSearch {

    private Double startChequeAmount;
    private Double endChequeAmount;
    private String chequePayTo;
    private Integer chequeNumber;
    private Boolean isPayed;
    private LocalDate startDateOfPay;
    private LocalDate endDateOfPay;
    private LocalDate StartLatencyDate;
    private LocalDate endLatencyDate;
}
