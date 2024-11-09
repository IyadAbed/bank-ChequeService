package com.artSoft.bankChecks.service.checks;

import com.artSoft.bankChecks.model.checks.dto.request.ChecksRequest;
import com.artSoft.bankChecks.model.checks.dto.request.ChecksSearch;
import com.artSoft.bankChecks.model.checks.dto.response.ChecksResponse;
import com.artSoft.bankChecks.model.checks.enums.SortDirection;
import com.artSoft.bankChecks.model.user.dto.response.MessageResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface ChecksService {

    MessageResponse create(ChecksRequest request);

    MessageResponse update(String id, ChecksRequest request);

    ChecksResponse getById(String id);

    Page<ChecksResponse> dateOfPay(LocalDate startDate, LocalDate endDate, SortDirection sortDirection, int page, int size);

    Page<ChecksResponse> latencyDate(LocalDate startDate, LocalDate endDate, SortDirection sortDirection, int page, int size);

    Page<ChecksResponse> search(ChecksSearch search, int page, int size);

    MessageResponse delete(String id);

    MessageResponse pay(String id);


}
