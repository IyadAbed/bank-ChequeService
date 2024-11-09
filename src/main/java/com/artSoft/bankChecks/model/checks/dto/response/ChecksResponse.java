package com.artSoft.bankChecks.model.checks.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChecksResponse {

    private String id;
    private Double chequeAmount;
    private String chequePayTo;
    private Integer chequeNumber;
    private LocalDate dateOfPay;
    private Boolean isPayed;
    private LocalDate latencyDate;


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

}
