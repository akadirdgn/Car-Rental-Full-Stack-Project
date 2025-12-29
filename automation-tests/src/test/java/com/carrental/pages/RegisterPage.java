package com.carrental.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RegisterPage extends BasePage {

    private By firstNameInput = By.name("firstName");
    private By lastNameInput = By.name("lastName");
    private By emailInput = By.name("emailId");
    private By passwordInput = By.name("password");
    private By phoneInput = By.name("phoneNo");
    private By streetInput = By.name("street");
    private By cityInput = By.name("city");
    private By pincodeInput = By.name("pincode");
    private By registerButton = By.xpath("//input[@type='submit']");

    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    public void register(String firstName, String lastName, String email, String password, String phone, String street,
            String city, String pincode) {
        sendKeys(firstNameInput, firstName);
        sendKeys(lastNameInput, lastName);
        sendKeys(emailInput, email);
        sendKeys(passwordInput, password);
        sendKeys(phoneInput, phone);
        sendKeys(streetInput, street);
        sendKeys(cityInput, city);
        sendKeys(pincodeInput, pincode);

        scrollIntoView(waitForElementVisible(registerButton));
        // Small delay if needed via explicit wait, but let's trust visibility
        jsClick(registerButton);
    }

    public void goToRegister(String baseUrl) {
        driver.get(baseUrl + "/user/customer/register");
    }
}
