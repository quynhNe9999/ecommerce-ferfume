package com.quynhtd.ecommerce.controller;

import com.quynhtd.ecommerce.dto.GraphQLRequest;
import com.quynhtd.ecommerce.dto.perfume.PerfumeResponse;
import com.quynhtd.ecommerce.dto.user.UpdateUserRequest;
import com.quynhtd.ecommerce.dto.user.UserResponse;
import com.quynhtd.ecommerce.mapper.UserMapper;
import com.quynhtd.ecommerce.security.UserPrincipal;
import com.quynhtd.ecommerce.service.graphql.GraphQLProvider;
import com.quynhtd.ecommerce.constants.PathConstants;
import graphql.ExecutionResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(PathConstants.API_V1_USERS)
public class UserController {

    private final UserMapper userMapper;
    private final GraphQLProvider graphQLProvider;

    @GetMapping
    public ResponseEntity<UserResponse> getUserInfo(@AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(userMapper.getUserInfo(user.getEmail()));
    }

    @PutMapping
    public ResponseEntity<UserResponse> updateUserInfo(@AuthenticationPrincipal UserPrincipal user,
                                                       @Valid @RequestBody UpdateUserRequest request,
                                                       BindingResult bindingResult) {
        return ResponseEntity.ok(userMapper.updateUserInfo(user.getEmail(), request, bindingResult));
    }

    @PostMapping(PathConstants.CART)
    public ResponseEntity<List<PerfumeResponse>> getCart(@RequestBody List<Long> perfumesIds) {
        return ResponseEntity.ok(userMapper.getCart(perfumesIds));
    }

    @PostMapping(PathConstants.GRAPHQL)
    public ResponseEntity<ExecutionResult> getUserInfoByQuery(@RequestBody GraphQLRequest request) {
        return ResponseEntity.ok(graphQLProvider.getGraphQL().execute(request.getQuery()));
    }
}
