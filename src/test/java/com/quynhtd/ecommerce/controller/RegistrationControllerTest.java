package com.quynhtd.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quynhtd.ecommerce.dto.RegistrationRequest;
import com.quynhtd.ecommerce.constants.ErrorMessage;
import com.quynhtd.ecommerce.constants.PathConstants;
import com.quynhtd.ecommerce.util.TestConstants;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/sql/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private RegistrationRequest registrationRequest;

    @Before
    public void init() {
        registrationRequest = new RegistrationRequest();
        registrationRequest.setEmail("testtest@test.com");
        registrationRequest.setPassword(TestConstants.USER_PASSWORD);
        registrationRequest.setPassword2(TestConstants.USER_PASSWORD);
        registrationRequest.setFirstName(TestConstants.FIRST_NAME);
        registrationRequest.setLastName(TestConstants.LAST_NAME);
        registrationRequest.setCaptcha("12345");
    }

    @Test
    public void registration_ShouldPassword2BeEmpty() throws Exception {
        registrationRequest.setPassword("");
        registrationRequest.setPassword2("");

        mockMvc.perform(MockMvcRequestBuilders.post(PathConstants.API_V1_REGISTRATION)
                        .content(mapper.writeValueAsString(registrationRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password2Error", Matchers.is(ErrorMessage.PASSWORD2_CHARACTER_LENGTH)));
    }

    @Test
    public void registration_ShouldPasswordsNotMatch() throws Exception {
        registrationRequest.setPassword2("12345678");

        mockMvc.perform(MockMvcRequestBuilders.post(PathConstants.API_V1_REGISTRATION)
                        .content(mapper.writeValueAsString(registrationRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.passwordError", Matchers.is(ErrorMessage.PASSWORDS_DO_NOT_MATCH)));
    }

    @Test
    public void registration_ShouldUserEmailIsExist() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setEmail(TestConstants.USER_EMAIL);
        registrationRequest.setPassword(TestConstants.USER_PASSWORD);
        registrationRequest.setPassword2(TestConstants.USER_PASSWORD);
        registrationRequest.setFirstName(TestConstants.FIRST_NAME);
        registrationRequest.setLastName(TestConstants.LAST_NAME);
        registrationRequest.setCaptcha("12345");

        mockMvc.perform(MockMvcRequestBuilders.post(PathConstants.API_V1_REGISTRATION)
                        .content(mapper.writeValueAsString(registrationRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.emailError").value(ErrorMessage.EMAIL_IN_USE));
    }

    @Test
    public void registration_ShouldCaptchaNotFilled() throws Exception {
        registrationRequest.setCaptcha(null);

        mockMvc.perform(MockMvcRequestBuilders.post(PathConstants.API_V1_REGISTRATION)
                        .content(mapper.writeValueAsString(registrationRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.captchaError").value("Fill captcha."));
    }

    @Test
    public void registration_ShouldInputFieldsAreEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PathConstants.API_V1_REGISTRATION)
                        .param("password2", "")
                        .param("g-recaptcha-response", "")
                        .content(mapper.writeValueAsString(new RegistrationRequest()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void activateEmailCode() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PathConstants.API_V1_REGISTRATION + PathConstants.ACTIVATE_CODE, TestConstants.USER_ACTIVATION_CODE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("User successfully activated.")));
    }

    @Test
    public void activateEmailCode_ShouldNotFoundActivationCode() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PathConstants.API_V1_REGISTRATION + PathConstants.ACTIVATE_CODE, "123")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", Matchers.is(ErrorMessage.ACTIVATION_CODE_NOT_FOUND)));
    }
}
