package com.carrentalsystem.resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.carrentalsystem.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class UserResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    // A simple test to verify context loads and a basic endpoint (if accessible)
    // Since we have Security enabled, accessing a protected resource might return
    // 401/403 which is also a valid test result
    // But specific endpoints like /api/user/login are permitted.
    // However, login requires POST.
    // Let's try to hit a public endpoint or just verify context loading which
    // covers "Integration" of components.

    @Test
    public void contextLoads() throws Exception {
        // This test ensures that the Spring Application Context loads successfully,
        // effectively testing the integration of all beans, configuration, and DB
        // connections.
    }

    // We can add a simple GET test if there's a simpler public URL, typically
    // /api/user/login is POST.
    // Checking a secured endpoint to see 403 is also a good integration test for
    // Security layer.
    @Test
    public void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/api/user/fetch/all"))
                .andExpect(status().isForbidden()); // or isUnauthorized() depending on config
    }
}
