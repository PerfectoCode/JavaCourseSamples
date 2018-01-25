import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.*;
import org.openqa.selenium.html5.*;
import org.openqa.selenium.logging.*;
import org.openqa.selenium.remote.*;

import com.perfectomobile.selenium.util.EclipseConnector;

public class MobileRemoteTest {
	
	public static void main(String[] args) throws MalformedURLException, IOException {

		String resultString;
		String browserName = "mobileOS";
		String cloudUser = "myUser";
		String cloudPw = "myPw";
		DesiredCapabilities capabilities = new DesiredCapabilities(browserName, "", Platform.ANY);
		String host = "myCloud.perfectomobile.com";	
		// define credentials for the CQ Lab connection
		capabilities.setCapability("user", cloudUser);
		capabilities.setCapability("password", cloudPw);
		// define the device to use for the testing
		capabilities.setCapability("deviceName", "deviceID");

//		setExecutionIdCapability(capabilities);
		
        RemoteWebDriver driver = new RemoteWebDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
		
		try {
			// write your code here
			// reset the device and browse to the website on the default browser
			driver.get("http://nxc.co.il/demoaut/index.php");
			//Set the implicit wait period before throwing a NoSuchElementException
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			// Try looking for header under the implicit wait period
			try {
				WebElement valid = driver.findElementByLinkText("Perfecto Virtual Tours");
			} catch (NoSuchElementException n) {
				System.out.println("Not displaying the opening web page");
				throw n;
			}
			
			// search for the username field
			WebElement nameField = driver.findElement(By.name("username"));
			nameField.sendKeys("John");
			
			// search for password field and enter the pw
			WebElement pwField = driver.findElement(By.name("password"));
			pwField.sendKeys("Perfecto1");
			
			// find the Sign in button and click on it
			WebElement signInBtn = driver.findElementByLinkText("Sign in");
			signInBtn.click();
			
			Map<String, Object> params = new HashMap<>();
			params.put("content", "Welcome back John");
			params.put("timeout", "50");
			resultString = (String) driver.executeScript("mobile:checkpoint:text", params3);
			if (!resultString.equalsIgnoreCase("true")) {
				System.out.println("'Welcome back John' text not found");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			driver.close();
			// In case you want to down the report or the report attachments, do it here.
			RemoteWebDriverUtils.downloadReport(driver, "pdf", "C:\\test\\report");
			RemoteWebDriverUtils.downloadAttachment(driver, "video", "C:\\test\\report\\video", "flv");
			// RemoteWebDriverUtils.downloadAttachment(driver, "image", "C:\\test\\report\\images", "jpg");
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
	
	private static void switchToContext(RemoteWebDriver driver, String context) {
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		Map<String,String> params = new HashMap<String,String>();
		params.put("name", context);
		executeMethod.execute(DriverCommand.SWITCH_TO_CONTEXT, params);
	}

	private static String getCurrentContextHandle(RemoteWebDriver driver) {		  
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		String context =  (String) executeMethod.execute(DriverCommand.GET_CURRENT_CONTEXT_HANDLE, null);
		return context;
	}

	private static List<String> getContextHandles(RemoteWebDriver driver) {		  
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		List<String> contexts =  (List<String>) executeMethod.execute(DriverCommand.GET_CONTEXT_HANDLES, null);
		return contexts;
	}
	
	// Recommended:
	// Remove the comment and use this method if you want the script to share the devices with the recording plugin.
	
 	private static void setExecutionIdCapability(DesiredCapabilities capabilities) throws IOException {
		EclipseConnector connector = new EclipseConnector();
		String executionId = connector.getExecutionId();
		capabilities.setCapability(EclipseConnector.ECLIPSE_EXECUTION_ID, executionId);
	}
	
}
