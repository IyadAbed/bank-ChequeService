package com.artSoft.bankChecks.service.checks.impl;

import com.artSoft.bankChecks.mapper.assistant.Helper;
import com.artSoft.bankChecks.model.checks.documents.Checks;
import com.artSoft.bankChecks.model.checks.enums.Status;
import com.artSoft.bankChecks.repository.checks.ChecksRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ScheduledTask {

    @Autowired
    private ChecksRepo checksRepo;
    @Autowired
    private Helper helper;
    // Scheduled to run at 00:01 (every day)
    @Scheduled(cron = "0 1 0 * * *")
    public void runTask() {

        LocalDate today = LocalDate.now();

        List<Checks> checks = checksRepo.findByStatusAndIsPayed(Status.ACTIVE, false);
        checks.forEach(check -> {
            // Check if the dateOfPay is before today
            if (check.getDateOfPay() != null && check.getDateOfPay().isBefore(today)) {
                // Update the latencyDate (custom logic for latencyDate update)
                check.setLatencyDate(today);
                check.setUpdatedAt(helper.getCurrentDate());
                checksRepo.save(check);
            }
        });
        }
}
