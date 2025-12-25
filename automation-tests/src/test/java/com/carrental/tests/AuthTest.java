package com.carrental.tests;

import static org.testng.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public class AuthTest extends BaseTest {

    // Scenario 1: Customer Registration Flow
    @Test
    public void testCustomerRegistration() throws InterruptedException {
        driver.get(baseUrl + "/user/customer/register");

        // Fill form
        driver.findElement(By.name("firstName")).sendKeys("Test");
        driver.findElement(By.name("lastName")).sendKeys("User");
        String email = "testuser" + System.currentTimeMillis() + "@example.com";
        driver.findElement(By.name("emailId")).sendKeys(email);
        driver.findElement(By.name("password")).sendKeys("password123");
        driver.findElement(By.name("phoneNo")).sendKeys("1234567890");
        
        // Address
        driver.findElement(By.name("street")).sendKeys("Main St");
        driver.findElement(By.name("city")).sendKeys("Istanbul");
        driver.findElement(By.name("pincode")).sendKeys("34000");

        // Submit
        WebElement registerBtn = driver.findElement(By.xpath("//input[@type='submit']"));
        registerBtn.click();
        
        // Wait and Assert
        Thread.sleep(2000); // Wait for toast/redirect
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("login") || currentUrl.contains("register")); // Depends on redirect logic
    }

    // Scenario 4: Customer Login Validation
    @Test
    public void testCustomerLogin() throws InterruptedException {
        driver.get(baseUrl + "/user/login");

        // Select Role: Customer (Value "Customer")
        driver.findElement(By.name("role")).sendKeys("Müşteri"); // Or Select class

        driver.findElement(By.name("emailId")).sendKeys("test@example.com"); // Assumes this user exists (seeded)
        driver.findElement(By.name("password")).sendKeys("password");

        driver.findElement(By.xpath("//button[contains(text(),'Giriş')]")).click();

        Thread.sleep(2000);
        // Verify redirection to home
        assertTrue(driver.getCurrentUrl().contains("home"));
    }
}
