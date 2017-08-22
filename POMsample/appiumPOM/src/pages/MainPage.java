package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import io.appium.java_client.android.AndroidDriver;

public class MainPage extends TestAppPages {
	By backBtn = By.xpath("//android.widget.Button[@text = 'Back']");
	By firstBtn = By.xpath("//android.widget.Button[starts-with(@text, 'First')]");
	By secondBtn = By.xpath("//android.widget.Button[starts-with(@text, 'Second')]");
	By thirdBtn = By.xpath("//android.widget.Button[starts-with(@text, 'Third')]");

	public MainPage(AndroidDriver<WebElement> driver) {
		super(driver);
		// validate that we are on the Main page by looking for the "First Screen" button.
		try {
			driver.findElement(firstBtn);
		} catch (NoSuchElementException e) {
			throw e;
		}
	}

	public StartPage goStart() {
		try {
			driver.findElement(backBtn).click();
			return new StartPage(driver);
		} catch (Exception e) {
			throw e;
		}
	}

	public ThirdPage goThird() {
		try {
			driver.findElement(thirdBtn).click();
			return new ThirdPage(driver);
		} catch (Exception e) {
			throw e;
		}
	}

	public SecondPage goSecond() {
		try {
			driver.findElement(secondBtn).click();
			return new SecondPage(driver);
		} catch (Exception e) {
			throw e;
		}
	}
	
}
