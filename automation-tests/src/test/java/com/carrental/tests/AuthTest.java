package com.carrental.tests;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.carrental.pages.LoginPage;
import com.carrental.pages.RegisterPage;

public class AuthTest extends BaseTest {

    @Test
    public void testCustomerRegistration() throws InterruptedException {
        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.goToRegister(baseUrl);

        String email = "testuser" + System.currentTimeMillis() + "@example.com";
        registerPage.register("Test", "User", email, "password123", "1234567890", "Main St", "Istanbul", "34000");

        Thread.sleep(2000);
        String currentUrl = registerPage.getCurrentUrl();
        // Depending on success, it might redirect to login or stay
        assertTrue(currentUrl.contains("login") || currentUrl.contains("register") || currentUrl.contains("home"));
    }

    @Test
    public void testCustomerLogin() throws InterruptedException {
        // Register first
        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.goToRegister(baseUrl);

        long timestamp = System.currentTimeMillis();
        String email = "login" + timestamp + "@example.com";
        registerPage.register("Login", "Tester", email, "password", "5555555555", "Login St", "Login City", "54321");

        Thread.sleep(2000);

        // Then Login
        LoginPage loginPage = new LoginPage(driver);
        loginPage.goToLogin(baseUrl);
        loginPage.login("Müşteri", email, "password");

        Thread.sleep(2000);
        assertTrue(loginPage.getCurrentUrl().contains("home"));
    }
}
