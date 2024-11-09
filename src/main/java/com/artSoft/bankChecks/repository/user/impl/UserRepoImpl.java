package com.artSoft.bankChecks.repository.user.impl;

import com.artSoft.bankChecks.model.user.documents.User;
import com.artSoft.bankChecks.model.user.enums.UserStatus;
import com.artSoft.bankChecks.repository.user.UserRepo;
import com.artSoft.bankChecks.repository.user.mongo.UserMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepoImpl implements UserRepo {

    @Autowired
    private UserMongo userMongo;

    @Override
    public void save(User user) {
        userMongo.save(user);
    }

    @Override
    public Optional<User> findByIdAndStatus(String id, UserStatus status) {
        return userMongo.findByIdAndStatus(id, status);
    }

    @Override
    public Optional<User> findByEmailAndStatus(String email, UserStatus status) {
        return userMongo.findByEmailAndStatus(email, status);
    }

    @Override
    public Optional<User> findByEmailAndStatusNot(String email, UserStatus status) {
        return userMongo.findByEmailAndStatusNot(email, status);
    }

    @Override
    public Optional<User> findByPhoneAndStatusNot(String phone, UserStatus status) {
        return userMongo.findByPhoneAndStatusNot(phone, status);
    }

    @Override
    public List<User> findByStatusNot(UserStatus status) {
        return userMongo.findByStatusNot(status);
    }

    @Override
    public Page<User> findByStatusNot(UserStatus status, Pageable pageable) {
        return userMongo.findByStatusNot(status, pageable);
    }
}
