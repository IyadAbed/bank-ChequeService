package com.artSoft.bankChecks.model.checks.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ChecksRequest {

    @NotNull(message = "chequePayTo is required")
    private Double chequeAmount;
    @NotBlank(message = "chequePayTo is required")
    private String chequePayTo;
    @NotNull(message = "chequeNumber is required")
    private Integer chequeNumber;
    @NotNull(message = "dateOfPay is required")
    private LocalDate dateOfPay;

}
