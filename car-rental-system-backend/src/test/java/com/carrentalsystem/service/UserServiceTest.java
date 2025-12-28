package com.carrentalsystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.carrentalsystem.dao.UserDao;
import com.carrentalsystem.entity.User;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setId(1);
        sampleUser.setFirstName("John");
        sampleUser.setLastName("Doe");
        sampleUser.setEmailId("john.doe@example.com");
        sampleUser.setPassword("password123");
        sampleUser.setRole("Customer");
        sampleUser.setStatus("Active");
    }

    // --- addUser Tests ---

    @Test
    @DisplayName("addUser: Should return saved user when valid user is provided")
    void addUser_ShouldReturnSavedUser_WhenDetailAreValid() {
        when(userDao.save(any(User.class))).thenReturn(sampleUser);

        User savedUser = userService.addUser(sampleUser);

        assertNotNull(savedUser);
        assertEquals(1, savedUser.getId());
        assertEquals("john.doe@example.com", savedUser.getEmailId());
        verify(userDao, times(1)).save(sampleUser);
    }

    // --- updateUser Tests ---

    @Test
    @DisplayName("updateUser: Should return updated user when valid user is provided")
    void updateUser_ShouldReturnUpdatedUser_WhenDetailAreValid() {
        when(userDao.save(any(User.class))).thenReturn(sampleUser);

        User updatedUser = userService.updateUser(sampleUser);

        assertNotNull(updatedUser);
        assertEquals("John", updatedUser.getFirstName());
        verify(userDao, times(1)).save(sampleUser);
    }

    // --- getUserByEmailAndStatus Tests ---

    @Test
    @DisplayName("getUserByEmailAndStatus: Should return user when found")
    void getUserByEmailAndStatus_ShouldReturnUser_WhenFound() {
        String email = "john.doe@example.com";
        String status = "Active";
        when(userDao.findByEmailIdAndStatus(email, status)).thenReturn(sampleUser);

        User foundUser = userService.getUserByEmailAndStatus(email, status);

        assertNotNull(foundUser);
        assertEquals(email, foundUser.getEmailId());
        assertEquals(status, foundUser.getStatus());
    }

    @Test
    @DisplayName("getUserByEmailAndStatus: Should return null when not found")
    void getUserByEmailAndStatus_ShouldReturnNull_WhenNotFound() {
        String email = "unknown@example.com";
        String status = "Active";
        when(userDao.findByEmailIdAndStatus(email, status)).thenReturn(null);

        User foundUser = userService.getUserByEmailAndStatus(email, status);

        assertNull(foundUser);
    }

    // --- getUserByEmailid Tests ---

    @Test
    @DisplayName("getUserByEmailid: Should return user when found")
    void getUserByEmailid_ShouldReturnUser_WhenFound() {
        String email = "john.doe@example.com";
        when(userDao.findByEmailId(email)).thenReturn(sampleUser);

        User foundUser = userService.getUserByEmailid(email);

        assertNotNull(foundUser);
        assertEquals(email, foundUser.getEmailId());
    }

    @Test
    @DisplayName("getUserByEmailid: Should return null when not found")
    void getUserByEmailid_ShouldReturnNull_WhenNotFound() {
        String email = "unknown@example.com";
        when(userDao.findByEmailId(email)).thenReturn(null);

        User foundUser = userService.getUserByEmailid(email);

        assertNull(foundUser);
    }

    // --- getUserByRole Tests ---

    @Test
    @DisplayName("getUserByRole: Should return list of users when found")
    void getUserByRole_ShouldReturnUserList_WhenFound() {
        String role = "Customer";
        List<User> userList = Arrays.asList(sampleUser);
        when(userDao.findByRole(role)).thenReturn(userList);

        List<User> result = userService.getUserByRole(role);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(role, result.get(0).getRole());
    }

    @Test
    @DisplayName("getUserByRole: Should return empty list when no users found")
    void getUserByRole_ShouldReturnEmptyList_WhenNoUsersFound() {
        String role = "Admin";
        when(userDao.findByRole(role)).thenReturn(Collections.emptyList());

        List<User> result = userService.getUserByRole(role);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // --- getUserById Tests ---

    @Test
    @DisplayName("getUserById: Should return user when optional is present")
    void getUserById_ShouldReturnUser_WhenUserExists() {
        int userId = 1;
        when(userDao.findById(userId)).thenReturn(Optional.of(sampleUser));

        User result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
    }

    @Test
    @DisplayName("getUserById: Should return null when optional is empty")
    void getUserById_ShouldReturnNull_WhenUserDoesNotExist() {
        int userId = 99;
        when(userDao.findById(userId)).thenReturn(Optional.empty());

        User result = userService.getUserById(userId);

        assertNull(result);
    }

    // --- getUserByEmailIdAndRoleAndStatus Tests ---

    @Test
    @DisplayName("getUserByEmailIdAndRoleAndStatus: Should return user when found")
    void getUserByEmailIdAndRoleAndStatus_ShouldReturnUser_WhenFound() {
        String email = "john.doe@example.com";
        String role = "Customer";
        String status = "Active";
        when(userDao.findByEmailIdAndRoleAndStatus(email, role, status)).thenReturn(sampleUser);

        User result = userService.getUserByEmailIdAndRoleAndStatus(email, role, status);

        assertNotNull(result);
        assertEquals(email, result.getEmailId());
        assertEquals(role, result.getRole());
    }

    @Test
    @DisplayName("getUserByEmailIdAndRoleAndStatus: Should return null when not found")
    void getUserByEmailIdAndRoleAndStatus_ShouldReturnNull_WhenNotFound() {
        String email = "john.doe@example.com";
        String role = "Admin"; // mismatched role
        String status = "Active";
        when(userDao.findByEmailIdAndRoleAndStatus(email, role, status)).thenReturn(null);

        User result = userService.getUserByEmailIdAndRoleAndStatus(email, role, status);

        assertNull(result);
    }

    // --- updateAllUser Tests ---

    @Test
    @DisplayName("updateAllUser: Should return updated list of users")
    void updateAllUser_ShouldReturnUpdatedUsersList() {
        List<User> users = Arrays.asList(sampleUser);
        when(userDao.saveAll(users)).thenReturn(users);

        List<User> result = userService.updateAllUser(users);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userDao, times(1)).saveAll(users);
    }

    // --- getUserByRoleAndStatus Tests ---

    @Test
    @DisplayName("getUserByRoleAndStatus: Should return list of users when found")
    void getUserByRoleAndStatus_ShouldReturnUserList_WhenFound() {
        String role = "Customer";
        String status = "Active";
        List<User> userList = Arrays.asList(sampleUser);
        when(userDao.findByRoleAndStatus(role, status)).thenReturn(userList);

        List<User> result = userService.getUserByRoleAndStatus(role, status);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(status, result.get(0).getStatus());
    }

    @Test
    @DisplayName("getUserByRoleAndStatus: Should return empty list when not found")
    void getUserByRoleAndStatus_ShouldReturnEmptyList_WhenNotFound() {
        String role = "Customer";
        String status = "Inactive";
        when(userDao.findByRoleAndStatus(role, status)).thenReturn(Collections.emptyList());

        List<User> result = userService.getUserByRoleAndStatus(role, status);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
