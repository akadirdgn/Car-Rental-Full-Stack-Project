package com.carrental.tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest {

    protected WebDriver driver;
    protected String baseUrl = "http://localhost:3000"; // Frontend URL

    @BeforeMethod
    public void setUp() {
        // Use WebDriverManager to setup Chrome Driver
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--start-maximized");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    protected WebElement waitForElementVisible(By locator) {
        return new org.openqa.selenium.support.ui.WebDriverWait(driver, Duration.ofSeconds(20))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(locator));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
