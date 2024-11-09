package com.artSoft.bankChecks.repository.checks.impl;

import com.artSoft.bankChecks.model.checks.documents.Checks;
import com.artSoft.bankChecks.model.checks.enums.Status;
import com.artSoft.bankChecks.repository.checks.ChecksRepo;
import com.artSoft.bankChecks.repository.checks.mongo.ChecksMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class ChecksRepoImpl implements ChecksRepo {

    @Autowired
    private ChecksMongo checksMongo;

    @Override
    public void save(Checks checks) {
        checksMongo.save(checks);
    }

    @Override
    public Optional<Checks> findByIdAndStatus(String id, Status status) {
        return checksMongo.findByIdAndStatus(id, status);
    }

    @Override
    public List<Checks> findByStatus(Status status) {
        return checksMongo.findByStatus(status);
    }

    @Override
    public List<Checks> findByStatusAndIsPayed(Status status, Boolean isPayed) {
        return checksMongo.findByStatusAndIsPayed(status, isPayed);
    }

    @Override
    public Page<Checks> findByStatusAndDateOfPayBetween(Status status, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return checksMongo.findByStatusAndDateOfPayBetween(status, startDate, endDate, pageable);
    }

    @Override
    public Page<Checks> findByStatusAndLatencyDateBetween(Status status, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return checksMongo.findByStatusAndLatencyDateBetween(status, startDate, endDate, pageable);
    }
}
