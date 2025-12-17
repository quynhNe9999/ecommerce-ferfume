package com.quynhtd.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quynhtd.ecommerce.dto.GraphQLRequest;
import com.quynhtd.ecommerce.dto.order.OrderRequest;
import com.quynhtd.ecommerce.constants.ErrorMessage;
import com.quynhtd.ecommerce.constants.PathConstants;
import com.quynhtd.ecommerce.util.TestConstants;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/sql/create-user-before.sql", "/sql/create-perfumes-before.sql", "/sql/create-orders-before.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql/create-orders-after.sql", "/sql/create-perfumes-after.sql", "/sql/create-user-after.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void getOrderById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PathConstants.API_V1_ORDER + PathConstants.ORDER_ID, 111)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(111))
                .andExpect(jsonPath("$.totalPrice").value(TestConstants.TOTAL_PRICE))
                .andExpect(jsonPath("$.date").value("2021-02-06"))
                .andExpect(jsonPath("$.firstName").value(TestConstants.FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(TestConstants.LAST_NAME))
                .andExpect(jsonPath("$.city").value(TestConstants.CITY))
                .andExpect(jsonPath("$.address").value(TestConstants.ADDRESS))
                .andExpect(jsonPath("$.email").value(TestConstants.USER_EMAIL))
                .andExpect(jsonPath("$.phoneNumber").value(TestConstants.PHONE_NUMBER))
                .andExpect(jsonPath("$.postIndex").value(TestConstants.POST_INDEX));
    }

    @Test
    public void getOrderById_ShouldNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PathConstants.API_V1_ORDER + PathConstants.ORDER_ID, 1111)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value(ErrorMessage.ORDER_NOT_FOUND));
    }

    @Test
    public void getOrderItemsByOrderId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PathConstants.API_V1_ORDER + PathConstants.ORDER_ID_ITEMS, 111)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(2)))
                .andExpect(jsonPath("$[*].id").isNotEmpty())
                .andExpect(jsonPath("$[*].amount").isNotEmpty())
                .andExpect(jsonPath("$[*].quantity").isNotEmpty());
    }

    @Test
    @WithUserDetails(TestConstants.USER_EMAIL)
    public void getUserOrders() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PathConstants.API_V1_ORDER)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id").isNotEmpty())
                .andExpect(jsonPath("$[*].totalPrice", Matchers.hasItem(TestConstants.TOTAL_PRICE)))
                .andExpect(jsonPath("$[*].date").isNotEmpty())
                .andExpect(jsonPath("$[*].firstName", Matchers.hasItem(TestConstants.FIRST_NAME)))
                .andExpect(jsonPath("$[*].lastName", Matchers.hasItem(TestConstants.LAST_NAME)))
                .andExpect(jsonPath("$[*].city", Matchers.hasItem(TestConstants.CITY)))
                .andExpect(jsonPath("$[*].address", Matchers.hasItem(TestConstants.ADDRESS)))
                .andExpect(jsonPath("$[*].email", Matchers.hasItem(TestConstants.USER_EMAIL)))
                .andExpect(jsonPath("$[*].phoneNumber", Matchers.hasItem(TestConstants.PHONE_NUMBER)))
                .andExpect(jsonPath("$[*].postIndex", Matchers.hasItem(TestConstants.POST_INDEX)));
    }

    @Test
    public void postOrder() throws Exception {
        Map<Long, Long> perfumesId = new HashMap<>();
        perfumesId.put(2L, 1L);
        perfumesId.put(4L, 1L);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setFirstName(TestConstants.FIRST_NAME);
        orderRequest.setLastName(TestConstants.LAST_NAME);
        orderRequest.setCity(TestConstants.CITY);
        orderRequest.setAddress(TestConstants.ADDRESS);
        orderRequest.setEmail(TestConstants.ORDER_EMAIL);
        orderRequest.setPostIndex(TestConstants.POST_INDEX);
        orderRequest.setPhoneNumber(TestConstants.PHONE_NUMBER);
        orderRequest.setTotalPrice(TestConstants.TOTAL_PRICE);
        orderRequest.setPerfumesId(perfumesId);

        mockMvc.perform(MockMvcRequestBuilders.post(PathConstants.API_V1_ORDER)
                        .content(mapper.writeValueAsString(orderRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(TestConstants.FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(TestConstants.LAST_NAME))
                .andExpect(jsonPath("$.city").value(TestConstants.CITY))
                .andExpect(jsonPath("$.address").value(TestConstants.ADDRESS))
                .andExpect(jsonPath("$.email").value(TestConstants.ORDER_EMAIL))
                .andExpect(jsonPath("$.phoneNumber").value(TestConstants.PHONE_NUMBER))
                .andExpect(jsonPath("$.postIndex").value(TestConstants.POST_INDEX))
                .andExpect(jsonPath("$.totalPrice").value(TestConstants.TOTAL_PRICE));
    }

    @Test
    public void postOrder_ShouldInputFieldsAreEmpty() throws Exception {
        OrderRequest OrderRequest = new OrderRequest();

        mockMvc.perform(MockMvcRequestBuilders.post(PathConstants.API_V1_ORDER)
                        .content(mapper.writeValueAsString(OrderRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.firstNameError", Matchers.is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(jsonPath("$.lastNameError", Matchers.is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(jsonPath("$.cityError", Matchers.is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(jsonPath("$.addressError", Matchers.is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(jsonPath("$.emailError", Matchers.is(ErrorMessage.EMAIL_CANNOT_BE_EMPTY)))
                .andExpect(jsonPath("$.phoneNumberError", Matchers.is(ErrorMessage.EMPTY_PHONE_NUMBER)))
                .andExpect(jsonPath("$.postIndexError", Matchers.is(ErrorMessage.EMPTY_POST_INDEX)));
    }

    @Test
    @WithUserDetails(TestConstants.USER_EMAIL)
    public void getUserOrdersByQuery() throws Exception {
        GraphQLRequest graphQLRequest = new GraphQLRequest();
        graphQLRequest.setQuery(TestConstants.GRAPHQL_QUERY_ORDERS);

        mockMvc.perform(MockMvcRequestBuilders.post(PathConstants.API_V1_ORDER + PathConstants.GRAPHQL)
                        .content(mapper.writeValueAsString(graphQLRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.orders[*].id").isNotEmpty())
                .andExpect(jsonPath("$.data.orders[*].totalPrice", Matchers.hasItem(TestConstants.TOTAL_PRICE)))
                .andExpect(jsonPath("$.data.orders[*].date").isNotEmpty())
                .andExpect(jsonPath("$.data.orders[*].firstName", Matchers.hasItem(TestConstants.FIRST_NAME)))
                .andExpect(jsonPath("$.data.orders[*].lastName", Matchers.hasItem(TestConstants.LAST_NAME)))
                .andExpect(jsonPath("$.data.orders[*].city", Matchers.hasItem(TestConstants.CITY)))
                .andExpect(jsonPath("$.data.orders[*].address", Matchers.hasItem(TestConstants.ADDRESS)))
                .andExpect(jsonPath("$.data.orders[*].email", Matchers.hasItem(TestConstants.USER_EMAIL)))
                .andExpect(jsonPath("$.data.orders[*].phoneNumber", Matchers.hasItem(TestConstants.PHONE_NUMBER)))
                .andExpect(jsonPath("$.data.orders[*].postIndex", Matchers.hasItem(TestConstants.POST_INDEX)))
                .andExpect(jsonPath("$.data.orders[*].orderItems").isNotEmpty());
    }
}
