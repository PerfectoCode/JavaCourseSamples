package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import io.appium.java_client.android.AndroidDriver;

public abstract class TestAppPages {
	protected final AndroidDriver<WebElement> driver;
	By appHeader = By.xpath("//android.widget.TextView[@text='Perfecto Mobile OSE']");
	By backBtn = By.xpath("//android.widget.Button[@text = 'Back']");

	// constructor verifies that the page objects have pointer to the driver instance
	public TestAppPages(AndroidDriver<WebElement> driver) {
		this.driver = driver;
	}

	public Boolean isLoggedIn() {
		try {
			driver.findElement(appHeader);
			return false;
		} catch (Exception e) {
			return true;
		}
	}
	
	public LoginPage getLogin() {
    	if (isLoggedIn()) {
    		// if already logged in - close and reopen application
    		// only way (I know of) to log out
    		driver.closeApp();
    		driver.launchApp();
    	}
		return new LoginPage(driver);
	}
	
	public MainPage goBack() {
		try {
			driver.findElement(backBtn).click();
			return new MainPage(driver);
		} catch (Exception e) {
			throw e;
		}
	}
	

}
