package com.carrental.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    protected WebElement waitForElementVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void click(By locator) {
        waitForElementVisible(locator).click();
    }

    protected void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    protected void jsClick(By locator) {
        WebElement element = waitForElementVisible(locator);
        jsClick(element);
    }

    protected void sendKeys(By locator, String text) {
        WebElement element = waitForElementVisible(locator);
        // element.clear(); // Removing clear() as it causes
        // InvalidElementStateException for some inputs
        element.sendKeys(text);
    }

    protected void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getPageSource() {
        return driver.getPageSource();
    }

    protected void selectByValue(By locator, String value) {
        org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(
                waitForElementVisible(locator));
        select.selectByValue(value);
    }

    protected void selectByVisibleText(By locator, String text) {
        org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(
                waitForElementVisible(locator));
        select.selectByVisibleText(text);
    }
}
