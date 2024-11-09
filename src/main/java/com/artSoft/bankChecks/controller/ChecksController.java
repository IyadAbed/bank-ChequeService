package com.artSoft.bankChecks.controller;

import com.artSoft.bankChecks.model.checks.dto.request.ChecksRequest;
import com.artSoft.bankChecks.model.checks.dto.request.ChecksSearch;
import com.artSoft.bankChecks.model.checks.dto.response.ChecksResponse;
import com.artSoft.bankChecks.model.checks.enums.SortDirection;
import com.artSoft.bankChecks.model.user.dto.response.MessageResponse;
import com.artSoft.bankChecks.service.checks.ChecksService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/checks")
public class ChecksController {

    @Autowired
    private ChecksService checksService;

    @PostMapping
    public ResponseEntity<MessageResponse> create(@Valid @RequestBody ChecksRequest checksRequest) {
        return ResponseEntity.ok(checksService.create(checksRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChecksResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(checksService.getById(id));
    }

    @GetMapping("/date-of-pay")
    public ResponseEntity<Page<ChecksResponse>> dateOfPay(@RequestParam LocalDate startDate,
                                                          @RequestParam LocalDate endDate,
                                                          @RequestParam SortDirection sortDirection,
                                                          @RequestParam(defaultValue = "1") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(checksService.dateOfPay(startDate, endDate, sortDirection, page, size));
    }

    @GetMapping("/latency-date")
    public ResponseEntity<Page<ChecksResponse>> latencyDate(@RequestParam LocalDate startDate,
                                                            @RequestParam LocalDate endDate,
                                                            @RequestParam SortDirection sortDirection,
                                                            @RequestParam(defaultValue = "1") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(checksService.latencyDate(startDate, endDate, sortDirection, page, size));
    }

    @PostMapping("/search")
    public ResponseEntity<Page<ChecksResponse>> search(@Valid @RequestBody ChecksSearch request,
                                                       @RequestParam(defaultValue = "1") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(checksService.search(request, page, size));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> update(@PathVariable String id, @Valid @RequestBody ChecksRequest checksRequest) {
        return ResponseEntity.ok(checksService.update(id, checksRequest));
    }

    @PatchMapping("/{id}/pay")
    public ResponseEntity<MessageResponse> pay(@PathVariable String id) {
        return ResponseEntity.ok(checksService.pay(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> delete(@PathVariable String id) {
        return ResponseEntity.ok(checksService.delete(id));
    }
}
