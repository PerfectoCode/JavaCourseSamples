package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import io.appium.java_client.android.AndroidDriver;

public class LoginPage extends TestAppPages {
	By unameEntry = By.xpath("//android.widget.EditText[@resource-id='com.example.testapp:id/etName']");
	By pwEntry = By.xpath("//android.widget.EditText[@resource-id='com.example.testapp:id/etPass']");
	By loginBtn = By.xpath("//android.widget.Button[@resource-id='com.example.testapp:id/login']");
	

	public LoginPage(AndroidDriver<WebElement> driver) {
		super(driver);
		boolean again = false;
		// validate that we are on the Login page by looking for Login button
		do {
			try {
				driver.findElement(loginBtn);
				again = false;
			} catch (NoSuchElementException e) {
				if (again) throw e;
				getLogin();
				again = true;
			}
		} while (again);
	}
	
	public void enterUser(String uname) {
		driver.findElement(unameEntry).sendKeys(uname);
	}

	public void enterPW(String password) {
		driver.findElement(pwEntry).sendKeys(password);
	}

	public StartPage login(String uname, String password){
		enterUser(uname);
		enterPW(password);
		
		driver.findElement(loginBtn).click();
		return new StartPage(driver);
	}
}
