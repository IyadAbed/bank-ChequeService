package com.artSoft.bankChecks.repository.checks;

import com.artSoft.bankChecks.model.checks.documents.Checks;
import com.artSoft.bankChecks.model.checks.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ChecksRepo {

    void save(Checks checks);

    Optional<Checks> findByIdAndStatus(String id, Status status);

    List<Checks> findByStatus(Status status);

    List<Checks> findByStatusAndIsPayed(Status status, Boolean isPayed);

    Page<Checks> findByStatusAndDateOfPayBetween(Status status, LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<Checks> findByStatusAndLatencyDateBetween(Status status, LocalDate startDate, LocalDate endDate, Pageable pageable);

}
