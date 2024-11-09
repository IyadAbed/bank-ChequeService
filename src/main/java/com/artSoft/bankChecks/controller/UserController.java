package com.artSoft.bankChecks.controller;

import com.artSoft.bankChecks.model.user.dto.request.SearchRequest;
import com.artSoft.bankChecks.model.user.dto.request.UserRequest;
import com.artSoft.bankChecks.model.user.dto.response.MessageResponse;
import com.artSoft.bankChecks.model.user.dto.response.UserResponse;
import com.artSoft.bankChecks.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<MessageResponse> create(@Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.create(request));
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> findAll(){
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<UserResponse>> findAll(@RequestParam(defaultValue = "1") int page,
                                                      @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(userService.findAll(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable String id){
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<UserResponse>> search(@RequestParam(defaultValue = "1") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @Valid @RequestBody SearchRequest request){
        return ResponseEntity.ok(userService.search(request, page, size));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> delete(@PathVariable String id){
        return ResponseEntity.ok(userService.delete(id));
    }

    @PatchMapping("/block/{id}")
    public ResponseEntity<MessageResponse> block(@PathVariable String id){
        return ResponseEntity.ok(userService.block(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> update(@PathVariable String id, @Valid @RequestBody UserRequest request){
        return ResponseEntity.ok(userService.update(id, request));
    }

}
