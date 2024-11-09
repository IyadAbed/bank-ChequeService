package com.artSoft.bankChecks.repository.user.mongo;

import com.artSoft.bankChecks.model.user.documents.User;
import com.artSoft.bankChecks.model.user.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserMongo extends MongoRepository<User, String> {

    Optional<User> findByIdAndStatus(String id, UserStatus status);

    Optional<User> findByEmailAndStatus(String email, UserStatus status);

    Optional<User> findByEmailAndStatusNot(String email, UserStatus status);

    Optional<User> findByPhoneAndStatusNot(String phone, UserStatus status);

    List<User> findByStatusNot(UserStatus status);

    Page<User> findByStatusNot(UserStatus status, Pageable pageable);


}
