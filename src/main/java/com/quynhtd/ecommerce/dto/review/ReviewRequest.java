package com.quynhtd.ecommerce.dto.review;

import com.quynhtd.ecommerce.constants.ErrorMessage;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ReviewRequest {

    private Long perfumeId;

    @NotBlank(message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
    private String author;

    @NotBlank(message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
    private String message;

    @NotNull(message = "Choose perfume rating")
    private Integer rating;
}
