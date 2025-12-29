package com.carrental.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private By roleSelect = By.name("role");
    private By emailInput = By.name("emailId");
    private By passwordInput = By.name("password");
    private By loginButton = By.xpath("//button[@type='submit']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void login(String role, String email, String password) {
        // Handle role using Select if it's a select element, or sendKeys if it's an
        // input
        // Based on original test code:
        // waitForElementVisible(By.name("role")).sendKeys("Müşteri");
        // So it seems to be an input or a custom select. Original code sent keys.
        // Assuming it is a Select for robustness, but if sendKeys worked I'll stick to
        // that or check details.
        // Wait, original test code used sendKeys. But usually role is a dropdown.
        // Let's look at `AuthTest.java`:
        // `waitForElementVisible(By.name("role")).sendKeys("Müşteri");`
        // Let's look at `AdminTest.java`:
        // `waitForElementVisible(By.name("role")).sendKeys("Admin");`
        // It seems consistent. I will just send keys for now as per original working
        // (mostly) tests.

        sendKeys(roleSelect, role);
        sendKeys(emailInput, email);
        sendKeys(passwordInput, password);
        jsClick(loginButton);
    }

    public void goToLogin(String baseUrl) {
        driver.get(baseUrl + "/user/login");
    }
}
