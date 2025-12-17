package com.quynhtd.ecommerce.controller;

import com.quynhtd.ecommerce.dto.GraphQLRequest;
import com.quynhtd.ecommerce.dto.HeaderResponse;
import com.quynhtd.ecommerce.dto.order.OrderResponse;
import com.quynhtd.ecommerce.dto.perfume.PerfumeRequest;
import com.quynhtd.ecommerce.dto.perfume.FullPerfumeResponse;
import com.quynhtd.ecommerce.dto.user.BaseUserResponse;
import com.quynhtd.ecommerce.dto.user.UserResponse;
import com.quynhtd.ecommerce.mapper.OrderMapper;
import com.quynhtd.ecommerce.mapper.PerfumeMapper;
import com.quynhtd.ecommerce.mapper.UserMapper;
import com.quynhtd.ecommerce.service.graphql.GraphQLProvider;
import com.quynhtd.ecommerce.constants.PathConstants;
import graphql.ExecutionResult;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping(PathConstants.API_V1_ADMIN)
public class AdminController {

    private final UserMapper userMapper;
    private final PerfumeMapper perfumeMapper;
    private final OrderMapper orderMapper;
    private final GraphQLProvider graphQLProvider;

    @PostMapping(PathConstants.ADD)
    public ResponseEntity<FullPerfumeResponse> addPerfume(@RequestPart(name = "file", required = false) MultipartFile file,
                                                          @RequestPart("perfume") @Valid PerfumeRequest perfume,
                                                          BindingResult bindingResult) {
        return ResponseEntity.ok(perfumeMapper.savePerfume(perfume, file, bindingResult));
    }

    @PostMapping(PathConstants.EDIT)
    public ResponseEntity<FullPerfumeResponse> updatePerfume(@RequestPart(name = "file", required = false) MultipartFile file,
                                                             @RequestPart("perfume") @Valid PerfumeRequest perfume,
                                                             BindingResult bindingResult) {
        return ResponseEntity.ok(perfumeMapper.savePerfume(perfume, file, bindingResult));
    }

    @DeleteMapping(PathConstants.DELETE_BY_PERFUME_ID)
    public ResponseEntity<String> deletePerfume(@PathVariable Long perfumeId) {
        return ResponseEntity.ok(perfumeMapper.deletePerfume(perfumeId));
    }

    @GetMapping(PathConstants.ORDERS)
    public ResponseEntity<List<OrderResponse>> getAllOrders(@PageableDefault(size = 10) Pageable pageable) {
        HeaderResponse<OrderResponse> response = orderMapper.getAllOrders(pageable);
        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getItems());
    }

    @GetMapping(PathConstants.ORDER_BY_EMAIL)
    public ResponseEntity<List<OrderResponse>> getUserOrdersByEmail(@PathVariable String userEmail, 
                                                                    @PageableDefault(size = 10) Pageable pageable) {
        HeaderResponse<OrderResponse> response = orderMapper.getUserOrders(userEmail, pageable);
        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getItems());
    }

    @DeleteMapping(PathConstants.ORDER_DELETE)
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderMapper.deleteOrder(orderId));
    }

    @GetMapping(PathConstants.USER_BY_ID)
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userMapper.getUserById(userId));
    }

    @GetMapping(PathConstants.USER_ALL)
    public ResponseEntity<List<BaseUserResponse>> getAllUsers(@PageableDefault(size = 10) Pageable pageable) {
        HeaderResponse<BaseUserResponse> response = userMapper.getAllUsers(pageable);
        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getItems());
    }

    @PostMapping(PathConstants.GRAPHQL_USER)
    public ResponseEntity<ExecutionResult> getUserByQuery(@RequestBody GraphQLRequest request) {
        return ResponseEntity.ok(graphQLProvider.getGraphQL().execute(request.getQuery()));
    }

    @PostMapping(PathConstants.GRAPHQL_USER_ALL)
    public ResponseEntity<ExecutionResult> getAllUsersByQuery(@RequestBody GraphQLRequest request) {
        return ResponseEntity.ok(graphQLProvider.getGraphQL().execute(request.getQuery()));
    }

    @PostMapping(PathConstants.GRAPHQL_ORDERS)
    public ResponseEntity<ExecutionResult> getAllOrdersQuery(@RequestBody GraphQLRequest request) {
        return ResponseEntity.ok(graphQLProvider.getGraphQL().execute(request.getQuery()));
    }

    @PostMapping(PathConstants.GRAPHQL_ORDER)
    public ResponseEntity<ExecutionResult> getUserOrdersByEmailQuery(@RequestBody GraphQLRequest request) {
        return ResponseEntity.ok(graphQLProvider.getGraphQL().execute(request.getQuery()));
    }
}
