package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.*;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.android.AndroidDriver;

public class ThirdPage extends TestAppPages {
	By alertMsg = By.xpath("//*[contains(@text, 'was clicked')]");
	By appClick = By.xpath("./android.widget.Button[@text='Click']");
	By okBtn = By.xpath("//android.widget.Button[@text='OK']");
	By clickBtn = By.xpath("//android.widget.Button[@text='Click']");
	
	public ThirdPage(AndroidDriver<WebElement> driver) {
		super(driver);
		// validate that we are on the Third Screen by looking for the Click button 
		try {
			driver.findElement(clickBtn);
		} catch (NoSuchElementException e) {
			throw e;
		}
	}
	

	public String clickApp(String appName) {
		String message;
		WebElement layout;
		try {
			driver.scrollTo(appName);
		} catch (Exception e) {
			return null;
		}
		String parent = String.format("//android.widget.TextView[@text= '%s" +"']/..", appName);
		layout = driver.findElement(By.xpath(parent));

		layout.findElement(appClick).click();
		
		// check for the popup
		try {
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			message = driver.findElement(alertMsg).getText();
			driver.findElement(okBtn).click();
		} catch (Exception e) {
			throw e;
		}
		
    	if (message.startsWith(appName)) {
    		message = "Clicked on " + appName;
    	} else {
    		message = "Missed " + appName;
    	}

		return message;
	}
	
	public MainPage goBack() {
		Map<String, Object> parms = new HashMap<>();
		parms.put("keySequence", "BACK");
		driver.executeScript("mobile:presskey", parms);
		
		return new MainPage(driver);
	}
}
