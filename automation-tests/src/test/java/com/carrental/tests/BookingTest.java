package com.carrental.tests;

import static org.testng.Assert.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.Select;

import com.carrental.pages.LoginPage;
import com.carrental.pages.RegisterPage;

public class BookingTest extends BaseTest {

    @Test
    public void testCustomerCanBookVehicle() throws InterruptedException {
        // 1. Register a new Customer to ensure clean state
        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.goToRegister(baseUrl);

        String uniqueEmail = "booking_test_" + System.currentTimeMillis() + "@example.com";
        String uniquePassword = "password123";
        // register(firstname, lastname, email, password, phone, street, city, zip)
        registerPage.register("Booking", "Tester", uniqueEmail, uniquePassword, "1234567890", "Test St", "Istanbul",
                "34000");

        // Wait for redirect/toast
        Thread.sleep(2000);

        // 2. Login with the new Customer (Manually to ensure Role selection works)
        driver.get(baseUrl + "/user/login");

        WebElement roleDropdown = waitForElementVisible(By.name("role"));
        Select roleSelect = new Select(roleDropdown);
        roleSelect.selectByValue("Customer"); // Select by value is robust

        WebElement emailInput = driver.findElement(By.name("emailId"));
        emailInput.sendKeys(uniqueEmail);

        WebElement passwordInput = driver.findElement(By.name("password"));
        passwordInput.sendKeys(uniquePassword);

        WebElement loginBtn = driver.findElement(By.xpath("//button[@type='submit']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", loginBtn);

        // Wait for login to complete and redirect
        Thread.sleep(2000);
        waitForElementVisible(By.cssSelector(".navbar"));

        // 3. Go to Home Page
        driver.get(baseUrl + "/home");

        // 4. Select a Vehicle (Click on the first car card found)
        WebElement firstCarCard = waitForElementVisible(By.cssSelector(".car-card"));
        // Use JS Click to avoid ElementClickInterceptedException
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstCarCard);

        // 5. Wait for Detail Page to load
        waitForElementVisible(By.id("startDate"));

        // 6. Calculate Dates (Tomorrow and Day After Tomorrow)
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate dayAfterTomorrow = LocalDate.now().plusDays(2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String startDateStr = tomorrow.format(formatter);
        String endDateStr = dayAfterTomorrow.format(formatter);

        System.out.println("Setting Start Date: " + startDateStr);
        System.out.println("Setting End Date: " + endDateStr);

        // 7. Fill Booking Form (Use simple JS dispatchEvent)
        // Note: ViewVariantDetail.jsx has a bug where state updates are not functional,
        // leading to race conditions if inputs are updated too fast. Adding sleep to
        // workaround.
        WebElement startDateInput = driver.findElement(By.id("startDate"));
        setDateUsingJS(startDateInput, startDateStr);

        Thread.sleep(1000); // Wait for React state to process first input

        WebElement endDateInput = driver.findElement(By.id("endDate"));
        setDateUsingJS(endDateInput, endDateStr);

        // Verify values before submit
        System.out.println("Input Start Date Value: " + startDateInput.getAttribute("value"));
        System.out.println("Input End Date Value: " + endDateInput.getAttribute("value"));

        // 8. Click Book Button (Araç Kirala)
        WebElement bookButton = driver.findElement(By.cssSelector("input[type='submit']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", bookButton);
        Thread.sleep(500);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", bookButton);

        // 9. Verify Success Toast Message (Turkish)
        // Wait longer for Toast. Catch ANY toast.
        WebElement toastMessage = waitForElementVisible(By.className("Toastify__toast"));
        String messageText = toastMessage.getText();

        System.out.println("Toast Message: " + messageText);

        assertTrue(messageText.contains("Rezervasyon başarıyla oluşturuldu"),
                "Toast message should contain success text. Actual: " + messageText);
    }

    private void setDateUsingJS(WebElement element, String dateValue) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
                "let input = arguments[0];" +
                        "let lastValue = input.value;" +
                        "input.value = arguments[1];" +
                        "let event = new Event('input', { bubbles: true });" +
                        "let tracker = input._valueTracker;" +
                        "if (tracker) {" +
                        "   tracker.setValue(lastValue);" +
                        "}" +
                        "input.dispatchEvent(event);",
                element, dateValue);
    }
}
