package com.artSoft.bankChecks.service.checks.impl;

import com.artSoft.bankChecks.mapper.ChecksMapper;
import com.artSoft.bankChecks.mapper.assistant.Helper;
import com.artSoft.bankChecks.model.checks.documents.Checks;
import com.artSoft.bankChecks.model.checks.dto.request.ChecksRequest;
import com.artSoft.bankChecks.model.checks.dto.request.ChecksSearch;
import com.artSoft.bankChecks.model.checks.dto.response.ChecksResponse;
import com.artSoft.bankChecks.model.checks.enums.SortDirection;
import com.artSoft.bankChecks.model.checks.enums.Status;
import com.artSoft.bankChecks.model.user.documents.User;
import com.artSoft.bankChecks.model.user.dto.request.SearchRequest;
import com.artSoft.bankChecks.model.user.dto.response.MessageResponse;
import com.artSoft.bankChecks.model.user.dto.response.UserResponse;
import com.artSoft.bankChecks.model.user.enums.UserStatus;
import com.artSoft.bankChecks.repository.checks.ChecksRepo;
import com.artSoft.bankChecks.service.checks.ChecksService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ChecksServiceImpl implements ChecksService {

    @Autowired
    private ChecksRepo checksRepo;

    @Autowired
    private ChecksMapper checksMapper;

    @Autowired
    private Helper helper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public MessageResponse create(ChecksRequest request) {
        Checks checks=checksMapper.toEntity(request);
        checksRepo.save(checks);
        return helper.toMessageResponse("Checks created successfully");
    }

    @Override
    public MessageResponse update(String id, ChecksRequest request) {
        Checks checks=getChecks(id);
        checks.setChequeAmount(request.getChequeAmount());
        checks.setChequePayTo(request.getChequePayTo());
        checks.setChequeNumber(request.getChequeNumber());
        checks.setDateOfPay(request.getDateOfPay());
        checksRepo.save(checks);
        return helper.toMessageResponse("Checks updated successfully");
    }

    @Override
    public ChecksResponse getById(String id) {
        Checks checks = getChecks(id);
        return checksMapper.toResponse(checks);
    }

    @Override
    public Page<ChecksResponse> dateOfPay(LocalDate startDate, LocalDate endDate, SortDirection sortDirection, int page, int size) {
        Sort sort;

        if (sortDirection == SortDirection.DESC) {
            sort = Sort.by(Sort.Order.desc("dateOfPay"));
        } else {
            sort = Sort.by(Sort.Order.asc("dateOfPay"));
        }

        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Page<Checks> checks = checksRepo.findByStatusAndDateOfPayBetween(Status.ACTIVE, startDate, endDate, pageRequest);

        return checks.map(checksMapper::toResponse);
    }

    @Override
    public Page<ChecksResponse> latencyDate(LocalDate startDate, LocalDate endDate, SortDirection sortDirection, int page, int size) {
        Sort sort;

        if (sortDirection == SortDirection.DESC) {
            sort = Sort.by(Sort.Order.desc("latencyDate"));
        } else {
            sort = Sort.by(Sort.Order.asc("latencyDate"));
        }

        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Page<Checks> checks = checksRepo.findByStatusAndLatencyDateBetween(Status.ACTIVE, startDate, endDate, pageRequest);

        return checks.map(checksMapper::toResponse);
    }

    @Override
    public Page<ChecksResponse> search(ChecksSearch search, int page, int size) {
        Query query = build(search);

        PageRequest pageRequest = PageRequest.of(page, size);

        List<Checks> checks = mongoTemplate.find(query.with(pageRequest), Checks.class);

        long total = mongoTemplate.count(query, User.class);

        List<ChecksResponse> checksResponses = checks.stream()
                .map(checksMapper::toResponse)
                .toList();

        return new PageImpl<>(checksResponses, pageRequest, total);
    }

    private Query build(ChecksSearch request){
        Query query = new Query();
        query.addCriteria(Criteria.where("status").ne(UserStatus.DELETED));

        if(request.getStartChequeAmount() != null && request.getEndChequeAmount() != null){
            query.addCriteria(Criteria.where("chequeAmount").gte(request.getStartChequeAmount()).lte(request.getEndChequeAmount()));
        }
        // Handle latencyDate range (only if startLatencyDate is provided)
        if (request.getStartLatencyDate() != null) {
            if (request.getEndLatencyDate() == null) {
                // Only startLatencyDate is provided, set range to start date to the end of that day
                query.addCriteria(Criteria.where("latencyDate").gte(request.getStartLatencyDate()).lte(request.getStartLatencyDate()));
            } else {
                // Both startLatencyDate and endLatencyDate are provided
                query.addCriteria(Criteria.where("latencyDate").gte(request.getStartLatencyDate()).lte(request.getEndLatencyDate()));
            }
        }

        // Handle dateOfPay range (only if startDateOfPay is provided)
        if (request.getStartDateOfPay() != null) {
            if (request.getEndDateOfPay() == null) {
                // Only startDateOfPay is provided, set range to start date to the end of that day
                query.addCriteria(Criteria.where("dateOfPay").gte(request.getStartDateOfPay()).lte(request.getStartDateOfPay()));
            } else {
                // Both startDateOfPay and endDateOfPay are provided
                query.addCriteria(Criteria.where("dateOfPay").gte(request.getStartDateOfPay()).lte(request.getEndDateOfPay()));
            }
        }
        if(request.getIsPayed() != null){
            query.addCriteria(Criteria.where("isPayed").is(request.getIsPayed()));
        }
        if(request.getChequePayTo() != null){
            query.addCriteria(Criteria.where("chequePayTo").is(request.getChequePayTo()));
        }
        if(request.getChequeNumber() != null){
            query.addCriteria(Criteria.where("chequeNumber").is(request.getChequeNumber()));
        }
        return query;
    }

    @Override
    public MessageResponse delete(String id) {
        Checks checks=getChecks(id);
        checks.setStatus(Status.DELETED);
        checks.setDeletedAt(helper.getCurrentDate());
        checksRepo.save(checks);
        return helper.toMessageResponse("Checks deleted successfully");
    }

    @Override
    public MessageResponse pay(String id) {
        Checks checks=getChecks(id);
        checks.setIsPayed(true);
        checksRepo.save(checks);
        return helper.toMessageResponse("Checks payed successfully");
    }

    private Checks getChecks(String id){
        return checksRepo.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(()->new RuntimeException("Checks not found"));
    }
}
