package com.quynhtd.ecommerce.mapper;

import com.quynhtd.ecommerce.domain.Review;
import com.quynhtd.ecommerce.dto.review.ReviewRequest;
import com.quynhtd.ecommerce.dto.review.ReviewResponse;
import com.quynhtd.ecommerce.exception.InputFieldException;
import com.quynhtd.ecommerce.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewMapper {

    private final CommonMapper commonMapper;
    private final ReviewService reviewService;

    public List<ReviewResponse> getReviewsByPerfumeId(Long perfumeId) {
        return commonMapper.convertToResponseList(reviewService.getReviewsByPerfumeId(perfumeId), ReviewResponse.class);
    }

    public ReviewResponse addReviewToPerfume(ReviewRequest reviewRequest, Long perfumeId, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InputFieldException(bindingResult);
        }
        Review review = commonMapper.convertToEntity(reviewRequest, Review.class);
        return commonMapper.convertToResponse(reviewService.addReviewToPerfume(review, perfumeId), ReviewResponse.class);
    }
}
