package com.carrentalsystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

import com.carrentalsystem.dto.AddDrivingLicenseRequest;
import com.carrentalsystem.dto.CommonApiResponse;
import com.carrentalsystem.dto.RegisterUserRequestDto;
import com.carrentalsystem.dto.UserLoginRequest;
import com.carrentalsystem.dto.UserLoginResponse;
import com.carrentalsystem.dto.UserResponseDto;
import com.carrentalsystem.dto.UserStatusUpdateRequestDto;
import com.carrentalsystem.resource.UserResource;
import org.springframework.mock.web.MockMultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters for simplicity in this unit test
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserResource userResource;

    @MockBean
    private com.carrentalsystem.service.UserService userService;

    @MockBean
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @MockBean
    private com.carrentalsystem.utility.JwtUtils jwtUtils;

    @MockBean
    private com.carrentalsystem.config.CustomUserDetailsService customUserDetailsService;

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

    @Test
    void testRegisterAdmin_Success() throws Exception {
        RegisterUserRequestDto request = new RegisterUserRequestDto();
        request.setEmailId("admin@example.com");
        request.setPassword("admin123");

        CommonApiResponse response = new CommonApiResponse();
        response.setResponseMessage("Admin Registered Successfully");
        response.setSuccess(true);

        when(userResource.registerAdmin(any(RegisterUserRequestDto.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        mockMvc.perform(post("/api/user/admin/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseMessage").value("Admin Registered Successfully"))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testFetchAllUsersByRole_Success() throws Exception {
        UserResponseDto response = new UserResponseDto();
        response.setResponseMessage("Users Fetched Successfully");
        response.setSuccess(true);

        when(userResource.getUsersByRole("Customer"))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        mockMvc.perform(get("/api/user/fetch/role-wise")
                .param("role", "Customer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseMessage").value("Users Fetched Successfully"));
    }

    @Test
    void testUpdateUserStatus_Success() throws Exception {
        UserStatusUpdateRequestDto request = new UserStatusUpdateRequestDto();
        request.setUserId(1);
        request.setStatus("Active");

        CommonApiResponse response = new CommonApiResponse();
        response.setResponseMessage("User Status Updated Successfully");
        response.setSuccess(true);

        when(userResource.updateUserStatus(any(UserStatusUpdateRequestDto.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        mockMvc.perform(put("/api/user/update/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseMessage").value("User Status Updated Successfully"));
    }

    @Test
    void testFetchUserById_Success() throws Exception {
        UserResponseDto response = new UserResponseDto();
        response.setResponseMessage("User Fetched Successfully");
        response.setSuccess(true);

        when(userResource.getUserById(1))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        mockMvc.perform(get("/api/user/fetch/user-id")
                .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseMessage").value("User Fetched Successfully"));
    }

    @Test
    void testDeleteUserById_Success() throws Exception {
        CommonApiResponse response = new CommonApiResponse();
        response.setResponseMessage("User Deleted Successfully");
        response.setSuccess(true);

        when(userResource.deleteUserById(1))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        mockMvc.perform(delete("/api/user/delete/user-id")
                .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseMessage").value("User Deleted Successfully"));
    }

    @Test
    void testAddCustomerDrivingLicense_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("licensePic", "license.jpg", "image/jpeg",
                "some image data".getBytes());

        CommonApiResponse response = new CommonApiResponse();
        response.setResponseMessage("Driving License Added Successfully");
        response.setSuccess(true);

        when(userResource.addCustomerDrivingLicense(any(AddDrivingLicenseRequest.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        mockMvc.perform(multipart("/api/user/add/driving-licence")
                .file(file)
                .param("customerId", "1")
                .param("licenseNumber", "DL12345")
                .param("expirationDate", "2030-01-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseMessage").value("Driving License Added Successfully"));
    }
}
