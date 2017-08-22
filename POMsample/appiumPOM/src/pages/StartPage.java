package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import io.appium.java_client.android.AndroidDriver;

public class StartPage extends TestAppPages {
	By welcome = By.xpath("//android.widget.TextView[contains(@text, 'Welcome back')]");
	By gotoNext = By.xpath("//android.widget.Button[@text='Continue']");

	public StartPage(AndroidDriver<WebElement> driver) {
		super(driver);
		try {
			driver.findElement(gotoNext);
		} catch (NoSuchElementException e) {
			throw e;
		}
	}

	// method that searches for the Welcome back string on start page and returns the full string
	public String getWelcomeText() {
		try {
			return driver.findElement(welcome).getText();
		} catch (Exception e) {
			return null;
		}
		
	}
	
	// method to click on the continue button to get to main page
	public MainPage clickContinue() {
		try {
			driver.findElement(gotoNext).click();
			return new MainPage(driver);
		} catch (Exception e) {
			throw e;
		}
	}
}
