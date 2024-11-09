package com.artSoft.bankChecks.service.user.impl;

import com.artSoft.bankChecks.handelException.exception.ConflictException;
import com.artSoft.bankChecks.handelException.exception.NotFoundException;
import com.artSoft.bankChecks.mapper.UserMapper;
import com.artSoft.bankChecks.mapper.assistant.Helper;
import com.artSoft.bankChecks.model.user.documents.User;
import com.artSoft.bankChecks.model.user.dto.request.SearchRequest;
import com.artSoft.bankChecks.model.user.dto.request.UserRequest;
import com.artSoft.bankChecks.model.user.dto.response.MessageResponse;
import com.artSoft.bankChecks.model.user.dto.response.UserResponse;
import com.artSoft.bankChecks.model.user.enums.UserStatus;
import com.artSoft.bankChecks.repository.user.UserRepo;
import com.artSoft.bankChecks.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private Helper helper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public MessageResponse create(UserRequest request) {
      User user = userMapper.toEntity(request);
        validateEmailAndPhoneUniqueness(user.getEmail(), user.getPhone(), null);
        userRepo.save(user);
        return helper.toMessageResponse("User Created Successfully");
    }

    @Override
    public List<UserResponse> findAll() {
        List<User> users = userRepo.findByStatusNot(UserStatus.DELETED);

        return users.stream().map(userMapper::toResponse).toList();
    }

    @Override
    public Page<UserResponse> findAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<User> users = userRepo.findByStatusNot(UserStatus.DELETED, pageRequest);

        return users.map(userMapper::toResponse);
    }

    @Override
    public UserResponse findById(String id) {
        User user = getUserById(id);
        return userMapper.toResponse(user);
    }

    @Override
    public Page<UserResponse> search(SearchRequest request, int page, int size) {
        Query query = build(request);

        PageRequest pageRequest = PageRequest.of(page, size);

       List<User> users = mongoTemplate.find(query.with(pageRequest), User.class);

        long total = mongoTemplate.count(query, User.class);

        List<UserResponse> userResponses = users.stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(userResponses, pageRequest, total);

    }

    private Query build(SearchRequest request){
        Query query = new Query();
        if(request.getName() != null){
            query.addCriteria(Criteria.where("name").regex(request.getName(), "i"));
        }
        if(request.getEmail() != null){
            query.addCriteria(Criteria.where("email").regex(request.getEmail(), "i"));
        }
        if(request.getPhone() != null){
            query.addCriteria(Criteria.where("phone").regex(request.getPhone(), "i"));
        }
        if(request.getStatus() != null){
            query.addCriteria(Criteria.where("status").is(request.getStatus()));
        }
        else{
            query.addCriteria(Criteria.where("status").ne(UserStatus.DELETED));
        }
        if(request.getRole() != null){
            query.addCriteria(Criteria.where("role").is(request.getRole()));

        }
        return query;
    }

    @Override
    public MessageResponse update(String id, UserRequest request) {
      User user = getUserById(id);
        validateEmailAndPhoneUniqueness(request.getEmail(), request.getPhone(), user);
        user.setName(helper.trimString(request.getName()));
        user.setEmail(helper.trimString(request.getEmail()));
        user.setPhone(helper.trimString(request.getPhone()));
        user.setUpdatedAt(helper.getCurrentDate());
        userRepo.save(user);
        return helper.toMessageResponse("User Updated Successfully");
    }

    @Override
    public MessageResponse delete(String id) {
        User user = getUserById(id);
            user.setStatus(UserStatus.DELETED);
            user.setDeletedAt(helper.getCurrentDate());
            userRepo.save(user);
            return helper.toMessageResponse("User Deleted Successfully");
    }

    @Override
    public MessageResponse block(String id) {
        User user = getUserById(id);
        user.setStatus(UserStatus.BLOCKED);
        user.setUpdatedAt(helper.getCurrentDate());
        userRepo.save(user);
        return helper.toMessageResponse("User Blocked Successfully");
    }

    private User getUserById(String id) {
        return userRepo.findByIdAndStatus(id, UserStatus.ACTIVE)
                .orElseThrow(() -> new NotFoundException("User Not Found"));
    }

    public void validateEmailAndPhoneUniqueness(String email, String phone, User currentUser) {

        // Check email uniqueness
        userRepo.findByEmailAndStatusNot(email, UserStatus.DELETED).ifPresent(existingUser -> {
            if (currentUser == null || !existingUser.getId().equals(currentUser.getId())) {
                throw new ConflictException("The Email is Used For Another User");
            }
        });

        // Check phone uniqueness
        userRepo.findByPhoneAndStatusNot(phone, UserStatus.DELETED).ifPresent(existingUser -> {
            if (currentUser == null || !existingUser.getId().equals(currentUser.getId())) {
                throw new ConflictException("The Phone is Used For Another User");
            }
        });
    }
}
