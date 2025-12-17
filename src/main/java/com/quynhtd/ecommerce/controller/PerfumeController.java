package com.quynhtd.ecommerce.controller;

import java.util.List;

import com.quynhtd.ecommerce.constants.PathConstants;
import com.quynhtd.ecommerce.dto.GraphQLRequest;
import com.quynhtd.ecommerce.dto.HeaderResponse;
import com.quynhtd.ecommerce.dto.perfume.FullPerfumeResponse;
import com.quynhtd.ecommerce.dto.perfume.PerfumeResponse;
import com.quynhtd.ecommerce.dto.perfume.PerfumeSearchRequest;
import com.quynhtd.ecommerce.dto.perfume.SearchTypeRequest;
import com.quynhtd.ecommerce.mapper.PerfumeMapper;
import com.quynhtd.ecommerce.service.graphql.GraphQLProvider;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.quynhtd.ecommerce.dto.perfume.*;

import graphql.ExecutionResult;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(PathConstants.API_V1_PERFUMES)
public class PerfumeController {

    private final PerfumeMapper perfumeMapper;
    private final GraphQLProvider graphQLProvider;

    @GetMapping
    public ResponseEntity<List<PerfumeResponse>> getAllPerfumes(@PageableDefault(size = 15) Pageable pageable) {
        HeaderResponse<PerfumeResponse> response = perfumeMapper.getAllPerfumes(pageable);
        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getItems());
    }

    @GetMapping(PathConstants.PERFUME_ID)
    public ResponseEntity<FullPerfumeResponse> getPerfumeById(@PathVariable Long perfumeId) {
        return ResponseEntity.ok(perfumeMapper.getPerfumeById(perfumeId));
    }

    @PostMapping(PathConstants.IDS)
    public ResponseEntity<List<PerfumeResponse>> getPerfumesByIds(@RequestBody List<Long> perfumesIds) {
        return ResponseEntity.ok(perfumeMapper.getPerfumesByIds(perfumesIds));
    }

    @PostMapping(PathConstants.SEARCH)
    public ResponseEntity<List<PerfumeResponse>> findPerfumesByFilterParams(@RequestBody PerfumeSearchRequest filter,
                                                                            @PageableDefault(size = 15) Pageable pageable) {
        HeaderResponse<PerfumeResponse> response = perfumeMapper.findPerfumesByFilterParams(filter, pageable);
        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getItems());
    }

    @PostMapping(PathConstants.SEARCH_GENDER)
    public ResponseEntity<List<PerfumeResponse>> findByPerfumeGender(@RequestBody PerfumeSearchRequest filter) {
        return ResponseEntity.ok(perfumeMapper.findByPerfumeGender(filter.getPerfumeGender()));
    }

    @PostMapping(PathConstants.SEARCH_PERFUMER)
    public ResponseEntity<List<PerfumeResponse>> findByPerfumer(@RequestBody PerfumeSearchRequest filter) {
        return ResponseEntity.ok(perfumeMapper.findByPerfumer(filter.getPerfumer()));
    }

    @PostMapping(PathConstants.SEARCH_TEXT)
    public ResponseEntity<List<PerfumeResponse>> findByInputText(@RequestBody SearchTypeRequest searchType,
                                                                 @PageableDefault(size = 15) Pageable pageable) {
        HeaderResponse<PerfumeResponse> response = perfumeMapper.findByInputText(searchType.getSearchType(), searchType.getText(), pageable);
        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getItems());
    }

    @PostMapping(PathConstants.GRAPHQL_IDS)
    public ResponseEntity<ExecutionResult> getPerfumesByIdsQuery(@RequestBody GraphQLRequest request) {
        return ResponseEntity.ok(graphQLProvider.getGraphQL().execute(request.getQuery()));
    }

    @PostMapping(PathConstants.GRAPHQL_PERFUMES)
    public ResponseEntity<ExecutionResult> getAllPerfumesByQuery(@RequestBody GraphQLRequest request) {
        return ResponseEntity.ok(graphQLProvider.getGraphQL().execute(request.getQuery()));
    }

    @PostMapping(PathConstants.GRAPHQL_PERFUME)
    public ResponseEntity<ExecutionResult> getPerfumeByQuery(@RequestBody GraphQLRequest request) {
        return ResponseEntity.ok(graphQLProvider.getGraphQL().execute(request.getQuery()));
    }
}
