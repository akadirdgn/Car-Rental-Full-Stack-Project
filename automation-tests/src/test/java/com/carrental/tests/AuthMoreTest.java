package com.carrental.tests;

import static org.testng.Assert.assertTrue;

import org.openqa.selenium.By;
import org.testng.annotations.Test;

import com.carrental.pages.LoginPage;
import com.carrental.pages.RegisterPage;

public class AuthMoreTest extends BaseTest {

    // Scenario 6: Empty Login Attempt
    @Test
    public void testEmptyLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.goToLogin(baseUrl);
        loginPage.login("Müşteri", "", ""); // Empty creds

        // Should still be on login page or show error
        assertTrue(driver.getCurrentUrl().contains("login"));
    }

    // Scenario 7: Invalid Email Login
    @Test
    public void testInvalidEmailLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.goToLogin(baseUrl);
        loginPage.login("Müşteri", "invalidemail", "password");

        // HTML5 validation might catch this, strictly we just want to ensure we don't
        // crash
        assertTrue(driver.getCurrentUrl().contains("login"));
    }

    // Scenario 8: Invalid Password Login
    @Test
    public void testInvalidPasswordLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.goToLogin(baseUrl);
        loginPage.login("Müşteri", "valid@email.com", "wrongpassword");

        assertTrue(driver.getCurrentUrl().contains("login"));
    }

    // Scenario 9: Register Button Presence
    @Test
    public void testRegisterButtonPresence() {
        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.goToRegister(baseUrl);
        // We know from RegisterPage it uses xpath input[@type='submit']
        // We just verify it finds something
        boolean btnExists = driver.findElements(By.xpath("//input[@type='submit']")).size() > 0;
        if (!btnExists) {
            btnExists = driver.findElements(By.tagName("button")).size() > 0;
        }
        assertTrue(btnExists);
    }

    // Scenario 10: Login Button Presence
    @Test
    public void testLoginButtonPresence() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.goToLogin(baseUrl);
        // From LoginPage: //button[@type='submit']
        boolean btnExists = driver.findElements(By.xpath("//button[@type='submit']")).size() > 0;
        assertTrue(btnExists);
    }

}
