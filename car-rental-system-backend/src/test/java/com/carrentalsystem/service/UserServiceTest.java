package com.carrentalsystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.carrentalsystem.dao.UserDao;
import com.carrentalsystem.entity.User;
// UserServiceImpl is in the same package, no import needed or import from correct package

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setId(1);
        sampleUser.setFirstName("Test");
        sampleUser.setLastName("User");
        sampleUser.setEmailId("test@example.com");
        sampleUser.setPassword("password");
        sampleUser.setRole("Customer");
    }

    @Test
    void testAddUser() {
        when(userDao.save(any(User.class))).thenReturn(sampleUser);

        User savedUser = userService.addUser(sampleUser);

        assertNotNull(savedUser);
        assertEquals("test@example.com", savedUser.getEmailId());
        assertEquals("Customer", savedUser.getRole());
    }

    @Test
    void testGetUserById_Found() {
        when(userDao.findById(1)).thenReturn(java.util.Optional.of(sampleUser));

        User foundUser = userService.getUserById(1);

        assertNotNull(foundUser);
        assertEquals(1, foundUser.getId());
    }

    @Test
    void testGetUserById_NotFound() {
        when(userDao.findById(99)).thenReturn(java.util.Optional.empty());

        User foundUser = userService.getUserById(99);

        org.junit.jupiter.api.Assertions.assertNull(foundUser);
    }

    @Test
    void testGetUserByEmailAndStatus() {
        when(userDao.findByEmailIdAndStatus("test@example.com", "Active")).thenReturn(sampleUser);

        User foundUser = userService.getUserByEmailAndStatus("test@example.com", "Active");

        assertNotNull(foundUser);
        assertEquals("test@example.com", foundUser.getEmailId());
    }
}
