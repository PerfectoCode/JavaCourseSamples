package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import io.appium.java_client.android.AndroidDriver;

public class SecondPage extends TestAppPages {

	By backBtn = By.xpath("//android.widget.Button[@text = 'Back']");
	By txtField = By.id("com.example.testapp:id/editText1");
	By txtView = By.id("com.example.testapp:id/textView1");
	By echoBtn = By.id("com.example.testapp:id/btn");
	By myBtn = By.id("com.example.testapp:id/myButton");

	public SecondPage(AndroidDriver<WebElement> driver) {
		super(driver);
		try {
			driver.findElement(myBtn);
		} catch (NoSuchElementException e) {
			throw e;
		}
	}

	// Method that sends the automation script text to the EditText field
	public void enterText(String phrase) {
		driver.findElement(txtField).sendKeys(phrase);
	}

	// Method that clicks the upper button and retrieves the text that it
	// sends to the TextView field
	public String echoText() {
		driver.findElement(echoBtn).click();
		return driver.findElement(txtView).getText();
	}
	
	// Method that clicks the lower button and retrieves the text that it
	// sends to the TextView field
	public String myBtnStr() {
		driver.findElement(myBtn).click();
		return driver.findElement(txtView).getText();
	}
}
