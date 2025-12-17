package com.quynhtd.ecommerce.dto.order;

import com.quynhtd.ecommerce.constants.ErrorMessage;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
public class OrderRequest {

    private Double totalPrice;
    private Map<Long, Long> perfumesId;

    @NotBlank(message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
    private String firstName;

    @NotBlank(message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
    private String lastName;

    @NotBlank(message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
    private String city;

    @NotBlank(message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
    private String address;

    @Email(message = ErrorMessage.INCORRECT_EMAIL)
    @NotBlank(message = ErrorMessage.EMAIL_CANNOT_BE_EMPTY)
    private String email;

    @NotBlank(message = ErrorMessage.EMPTY_PHONE_NUMBER)
    private String phoneNumber;

    @NotNull(message = ErrorMessage.EMPTY_POST_INDEX)
    @Min(value = 5, message = "Post index must contain 5 digits")
    private Integer postIndex;
}
