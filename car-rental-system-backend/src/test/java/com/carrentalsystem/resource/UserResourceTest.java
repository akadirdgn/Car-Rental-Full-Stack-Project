package com.carrentalsystem.resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.carrentalsystem.dto.CommonApiResponse;
import com.carrentalsystem.dto.RegisterUserRequestDto;
import com.carrentalsystem.entity.Address;
import com.carrentalsystem.entity.User;
import com.carrentalsystem.service.AddressService;
import com.carrentalsystem.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserResourceTest {

    @InjectMocks
    private UserResource userResource;

    @Mock
    private UserService userService;

    @Mock
    private AddressService addressService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private RegisterUserRequestDto registerRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterUserRequestDto();
        registerRequest.setFirstName("Test");
        registerRequest.setLastName("User");
        registerRequest.setEmailId("test@example.com");
        registerRequest.setPassword("password");
        registerRequest.setRole("Customer");
        registerRequest.setCity("City");
        registerRequest.setPincode(12345);
        registerRequest.setStreet("Street");
    }

    @Test
    void testRegisterUser_Success() {
        when(userService.getUserByEmailAndStatus(anyString(), any())).thenReturn(null);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPwd");
        when(addressService.addAddress(any(Address.class))).thenReturn(new Address());
        when(userService.addUser(any(User.class))).thenReturn(new User());

        ResponseEntity<CommonApiResponse> response = userResource.registerUser(registerRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Kullanıcı Başarıyla Kaydedildi", response.getBody().getResponseMessage());
    }

    @Test
    void testRegisterUser_DuplicateEmail() {
        when(userService.getUserByEmailAndStatus(anyString(), any())).thenReturn(new User());

        ResponseEntity<CommonApiResponse> response = userResource.registerUser(registerRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Bu E-posta adresi ile kayıtlı kullanıcı zaten var!!!", response.getBody().getResponseMessage());
    }
}
