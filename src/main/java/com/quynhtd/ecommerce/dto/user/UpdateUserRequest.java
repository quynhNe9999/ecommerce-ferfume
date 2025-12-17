package com.quynhtd.ecommerce.dto.user;

import com.quynhtd.ecommerce.constants.ErrorMessage;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateUserRequest {
    private Long id;

    @NotBlank(message = ErrorMessage.EMPTY_FIRST_NAME)
    private String firstName;

    @NotBlank(message = ErrorMessage.EMPTY_LAST_NAME)
    private String lastName;

    private String city;
    private String address;
    private String phoneNumber;
    private String postIndex;
}
