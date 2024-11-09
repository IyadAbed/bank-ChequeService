package com.artSoft.bankChecks.mapper;

import com.artSoft.bankChecks.mapper.assistant.Helper;
import com.artSoft.bankChecks.model.checks.documents.Checks;
import com.artSoft.bankChecks.model.checks.dto.request.ChecksRequest;
import com.artSoft.bankChecks.model.checks.dto.response.ChecksResponse;
import com.artSoft.bankChecks.model.checks.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChecksMapper {

    @Autowired
    private Helper helper;

    public Checks toEntity(ChecksRequest request){
        return Checks.builder()
                .chequeAmount(request.getChequeAmount())
                .chequePayTo(request.getChequePayTo())
                .chequeNumber(request.getChequeNumber())
                .dateOfPay(request.getDateOfPay())
                .isPayed(false)
                .status(Status.ACTIVE)
                .createdAt(helper.getCurrentDate())
                .updatedAt(helper.getCurrentDate())
                .build();

    }

    public ChecksResponse toResponse(Checks checks){
        return ChecksResponse.builder()
                .id(checks.getId())
                .chequeAmount(checks.getChequeAmount())
                .chequePayTo(checks.getChequePayTo())
                .chequeNumber(checks.getChequeNumber())
                .dateOfPay(checks.getDateOfPay())
                .isPayed(checks.getIsPayed())
                .latencyDate(checks.getLatencyDate())
                .createdAt(checks.getCreatedAt())
                .updatedAt(checks.getUpdatedAt())
                .deletedAt(checks.getDeletedAt())
                .build();
    }
}
