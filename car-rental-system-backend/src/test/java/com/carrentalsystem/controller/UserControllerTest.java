package com.carrentalsystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.carrentalsystem.dto.CommonApiResponse;
import com.carrentalsystem.dto.RegisterUserRequestDto;
import com.carrentalsystem.dto.UserLoginRequest;
import com.carrentalsystem.dto.UserLoginResponse;
import com.carrentalsystem.resource.UserResource;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters for simplicity in this unit test
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserResource userResource;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        RegisterUserRequestDto request = new RegisterUserRequestDto();
        request.setFirstName("Test");
        request.setLastName("User");
        request.setEmailId("test@example.com");
        request.setPassword("password");
        request.setRole("Customer");
        request.setCity("Test City");
        request.setPincode(12345);
        request.setStreet("Test Street");

        CommonApiResponse response = new CommonApiResponse();
        response.setResponseMessage("User Registered Successfully");
        response.setSuccess(true);

        when(userResource.registerUser(any(RegisterUserRequestDto.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseMessage").value("User Registered Successfully"))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testLogin_Success() throws Exception {
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setEmailId("test@example.com");
        loginRequest.setPassword("password");
        loginRequest.setRole("Customer");

        UserLoginResponse response = new UserLoginResponse();
        response.setResponseMessage("Login Successful");
        response.setSuccess(true);
        response.setJwtToken("dummy-token");

        when(userResource.login(any(UserLoginRequest.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        mockMvc.perform(post("/api/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseMessage").value("Login Successful"))
                .andExpect(jsonPath("$.jwtToken").value("dummy-token"));
    }
}
