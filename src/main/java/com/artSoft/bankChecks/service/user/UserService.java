package com.artSoft.bankChecks.service.user;

import com.artSoft.bankChecks.model.user.documents.User;
import com.artSoft.bankChecks.model.user.dto.request.SearchRequest;
import com.artSoft.bankChecks.model.user.dto.request.UserRequest;
import com.artSoft.bankChecks.model.user.dto.response.MessageResponse;
import com.artSoft.bankChecks.model.user.dto.response.UserResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    MessageResponse create(UserRequest request);

    List<UserResponse> findAll();

    Page<UserResponse> findAll(int page, int size);

    UserResponse findById(String id);

    Page<UserResponse> search(SearchRequest request, int page, int size);

    MessageResponse update(String id, UserRequest request);

    MessageResponse delete(String id);

    MessageResponse block(String id);

    void validateEmailAndPhoneUniqueness(String email, String phone, User currentUser);

}
