import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.*;
import org.openqa.selenium.html5.*;
import org.openqa.selenium.logging.*;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.Cookie.Builder;

import io.appium.java_client.*;
import io.appium.java_client.android.*;
import io.appium.java_client.ios.*;

import com.perfectomobile.selenium.util.EclipseConnector;

public class AppiumTest {
	
	private static Dimension winSize;
	
	public static void main(String[] args) throws MalformedURLException, IOException {

		System.out.println("Run started");
		int startX, endX, startY, endY; 
		String browserName = "mobileOS";
		DesiredCapabilities capabilities = new DesiredCapabilities(browserName, "", Platform.ANY);
		String host = <Your Cloud>;		
		capabilities.setCapability("user", <username>);
		capabilities.setCapability("password", <password>);
		capabilities.setCapability("deviceName", <Device ID>); 
 		capabilities.setCapability("automationName", "Appium");

		// Call this method if you want the script to share the devices with the Perfecto Lab plugin.
		setExecutionIdCapability(capabilities, host);

		// Application settings examples.
		// For iOS:
		capabilities.setCapability("bundleId", "com.example.apple-samplecode.Recipes");

		//AndroidDriver driver = new AndroidDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
		IOSDriver driver = new IOSDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		winSize = driver.manage().window().getSize();
		
		try {
			// write your code here
			// 1. Validate that we are on the application home page
			WebElement navBar = driver.findElementByClassName("UIANavigationBar");

			try {
				navBar.findElement(By.xpath("//UIAStaticText[@name = 'Recipes']"));
			} catch (Exception nf) {
				// Not on main page, press the Recipes Button to go back
				driver.findElementByXPath("//UIAButton[@name = 'Recipes']").click();;
			}
			
			// 2. click on Chocolate Cake recipe
			driver.findElementByXPath("//UIAStaticText[@name='Chocolate Cake']").click();
			
			// 2a. Validate on correct recipe
			navBar = driver.findElementByClassName("UIANavigationBar");
			navBar.findElement(By.xpath("//UIAStaticText[@name = 'Chocolate Cake']"));

			// 3. scroll down to the Self-raising flour
			IOSElement tbl = (IOSElement) driver.findElementByClassName("UIATableView");
			try {
				tbl.scrollTo("Self-raising");
			}  catch (Exception fs) {
				// failed to find the element to scroll to
				System.out.println("Failed to scrollTo the Flour, following is exception");
				fs.printStackTrace();
			}

			try {
				tbl.scrollToExact("Dessert");
			}  catch (Exception fs) {
				// failed to find the element to scroll to
				System.out.println("Failed to scrollTo the top, following is exception");
				fs.printStackTrace();
			}
			
			try {
				tbl.scrollToExact("butter");
			}  catch (Exception fs) {
				// failed to find the element to scroll to
				System.out.println("Failed to scrollToExact bad spelling, following is exception");
				System.out.println(fs.getClass().toString());
				//fs.printStackTrace();
			}

			driver.findElementByXPath("//UIAButton[@name = 'Recipes']").click();;
			// 2. click on Waffle recipe
			driver.findElementByXPath("//UIAStaticText[@name='Gaufres de Liège']").click();
			
			// 2a. Validate on correct recipe
			navBar = driver.findElementByClassName("UIANavigationBar");
			navBar.findElement(By.xpath("//UIAStaticText[@name = 'Gaufres de Liège']"));

			startX = getX(20); endX = getX(22);
			startY = getY(82); endY = getY(25);
			TouchAction touchAction4 = new TouchAction(driver);
			touchAction4.press(startX, startY).moveTo(endX, endY).release();
			driver.performTouchAction(touchAction4);

            touchAction4.press(517, 370).moveTo(531, 1156).release();
            driver.performTouchAction(touchAction4);
            
			touchAction4.press(300,512).moveTo(367,1070).release();
			driver.performTouchAction(touchAction4);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				driver.close();
				
				// In case you want to down the report or the report attachments, do it here.
				RemoteWebDriverUtils.downloadReport(driver, "pdf", "C:\\test\\report-Scroll");
				// RemoteWebDriverUtils.downloadAttachment(driver, "video", "C:\\test\\report\\video", "flv");
				// RemoteWebDriverUtils.downloadAttachment(driver, "image", "C:\\test\\report\\images", "jpg");
			} catch (Exception e) {
				e.printStackTrace();
			}
			driver.quit();
		}
		
		System.out.println("Run ended");
	}
	
	private static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}
	
	private static int getX (int x) {
		return (int) ((winSize.width * x) / 100);
	}

	private static int getY (int y) {
		return (int) ((winSize.height * y) / 100);
	}

 	private static void setExecutionIdCapability(DesiredCapabilities capabilities, String host) throws IOException {
		EclipseConnector connector = new EclipseConnector();
		String eclipseHost = connector.getHost();
		if ((eclipseHost == null) || (eclipseHost.equalsIgnoreCase(host))) {
			String executionId = connector.getExecutionId();
			capabilities.setCapability(EclipseConnector.ECLIPSE_EXECUTION_ID, executionId);
		}
	}
}
