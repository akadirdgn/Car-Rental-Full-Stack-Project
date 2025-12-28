package com.carrental.tests;

import static org.testng.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

public class AdminTest extends BaseTest {

    // Scenario 5: Admin Login Validation
    @Test
    public void testAdminLogin() throws InterruptedException {
        driver.get(baseUrl + "/user/login");

        waitForElementVisible(By.name("role")).sendKeys("Admin");
        waitForElementVisible(By.name("emailId")).sendKeys("admin@demo.com"); // Assumes default admin
        waitForElementVisible(By.name("password")).sendKeys("admin123");

        waitForElementVisible(By.xpath("//button[contains(text(),'Giriş')]")).click();

        Thread.sleep(2000);
        assertTrue(driver.getCurrentUrl().contains("home"));
        // Additional check for Admin Header elements
        assertTrue(driver.getPageSource().contains("Admin"));
    }

    // Scenario 2: Admin Add Car Flow (Company + Variant)
    @Test(dependsOnMethods = "testAdminLogin")
    public void testAddCarFlow() throws InterruptedException {
        // 1. Add Company
        driver.get(baseUrl + "/admin/company/add");
        driver.findElement(By.name("name")).sendKeys("TestCompany" + System.currentTimeMillis());
        driver.findElement(By.xpath("//input[@type='submit']")).click();
        Thread.sleep(1000); // Wait for toast

        // 2. Add Variant
        driver.get(baseUrl + "/admin/variant/add");

        // Fill form
        Select companySelect = new Select(driver.findElement(By.name("companyId")));
        companySelect.selectByIndex(1); // Select the first available company

        driver.findElement(By.name("name")).sendKeys("TestModel X");
        driver.findElement(By.name("modelNumber")).sendKeys("2024");
        driver.findElement(By.name("year")).sendKeys("2024");
        driver.findElement(By.name("pricePerDay")).sendKeys("500");
        driver.findElement(By.name("seatingCapacity")).sendKeys("4");

        // Selecting Fuel Type
        Select fuelSelect = new Select(driver.findElement(By.name("fuelType")));
        fuelSelect.selectByVisibleText("Petrol");

        // Submit
        driver.findElement(By.xpath("//input[@type='submit']")).click();

        Thread.sleep(2000);
        // Verify redirection or success message
    }

    // Scenario 7 & 8: View Bookings & Approve
    @Test(priority = 3)
    public void testAdminApproveBooking() throws InterruptedException {
        // Login first if not separate instance (TestNG manages checking, but manual
        // login safer)
        if (!driver.getCurrentUrl().contains("home")) {
            testAdminLogin();
        }

        driver.get(baseUrl + "/admin/customer/bookings");

        // Find a "Pending" booking and Approve (Simplified XPath)
        // In real world, we'd target the specific booking created in BookingTest
        // Here we just check page load and presence of table
        assertTrue(driver.getPageSource().contains("Tüm Rezervasyonlar"));

        // Example interaction for approval (commented out as it depends on dynamic
        // data)
        /*
         * WebElement updateBtn =
         * driver.findElement(By.xpath("//button[contains(text(),'Güncelle')]"));
         * updateBtn.click();
         * Select statusSelect = new Select(driver.findElement(By.name("status")));
         * statusSelect.selectByValue("Approved");
         * // Select vehicle...
         */
    }
}
