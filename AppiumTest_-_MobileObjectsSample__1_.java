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

import io.appium.java_client.*;
import io.appium.java_client.android.*;
import io.appium.java_client.ios.*;

public class AppiumTest {
	
	public static void main(String[] args) throws MalformedURLException, IOException {

		System.out.println("Run started");
		
		String cloudUser = "myUser";
		String cloudPw = "myPassword";
		String resultString;
		
//		String browserName = "mobileOS";
		DesiredCapabilities capabilities = new DesiredCapabilities("", "", Platform.ANY);
		String host = "myCloud.perfectomobile.com";		
		capabilities.setCapability("user", cloudUser);
		capabilities.setCapability("password", cloudPw);

        //TODO: Change your device ID
		capabilities.setCapability("automationName", "Appium");
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("platformVersion", "5.*");

		capabilities.setCapability("autoLaunch", true);
		capabilities.setCapability("fullReset", true);
		capabilities.setCapability("app", "PUBLIC:Samples/TestApp.apk");
		capabilities.setCapability("appPackage", "com.example.testapp");

        // Call this method if you want the script to share the devices with the Perfecto Lab plugin.
        setExecutionIdCapability(capabilities, host);

        // Add a persona to your script (see https://community.perfectomobile.com/posts/1048047-available-personas)
        //capabilities.setCapability(WindTunnelUtils.WIND_TUNNEL_PERSONA_CAPABILITY, WindTunnelUtils.GEORGIA);

        // Name your script
        // capabilities.setCapability("scriptName", "AppiumTest");

        AndroidDriver driver = new AndroidDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
        // IOSDriver driver = new IOSDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        try {
            // write your code here
			/* PART 1:
			 * Login to demo application - set user and password and click Login using objects
			 */

			//Set the username		
			WebElement nameElement = driver.findElementByXPath("//*[@resource-id='com.example.testapp:id/etName']");
			nameElement.sendKeys("John");
			
			//Set the password			
			WebElement passwordElement = driver.findElement(By.xpath("//*[@resource-id='com.example.testapp:id/etPass']"));
			passwordElement.sendKeys("Perfecto1");
			
			//Click on the Login button			
			WebElement loginElement = driver.findElementByXPath("//*[@text='Login']");
            loginElement.click();

			
			//Text checkpoint on "Welcome back John"            
         	Map<String, Object> params = new HashMap<>();
         	params.put("content", "Welcome back John");
         	params.put("timeout", "20");
         	resultString = (String) driver.executeScript("mobile:checkpoint:text", params);
         	if (!resultString.equalsIgnoreCase("true")) {
         		System.out.println("Did not login successfully - 'Welcome back John' text not found");
         		throw new NoSuchFieldException();
         	}
				
			
			/* PART 2:
			 * Navigate within application
			 */
         	
         	
         	//Click on Continue button         	
         	WebElement continueElement = driver.findElementByXPath("//android.widget.Button [@text='Continue']");
         	continueElement.click();
            
            //Cick on First Screen button
            WebElement firstScreenElement = driver.findElementByXPath("//android.widget.Button[starts-with(@text,'First')]");
            firstScreenElement.click();
			
            //Radio button example for Android devices
           	List<WebElement> radioButtons = driver.findElementsByXPath("//*[@checkable='true']");
            for (WebElement radioButton:radioButtons){
                	radioButton.click();
            } 

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Retrieve the URL of the Wind Tunnel Report, can be saved to your execution summary and used to download the report at a later point
                String reportURL = (String)(driver.getCapabilities().getCapability(WindTunnelUtils.WIND_TUNNEL_REPORT_URL_CAPABILITY));

                driver.close();

                // In case you want to download the report or the report attachments, do it here.
                // PerfectoLabUtils.downloadReport(driver, "pdf", "C:\\test\\report");
                // PerfectoLabUtils.downloadAttachment(driver, "video", "C:\\test\\report\\video", "flv");
                // PerfectoLabUtils.downloadAttachment(driver, "image", "C:\\test\\report\\images", "jpg");

            } catch (Exception e) {
                e.printStackTrace();
            }

            driver.quit();
        }

        System.out.println("Run ended");
    }

	// Recommended:
	// Remove the comment and use this method if you want the script to share the devices with the recording plugin.\
	// Requires import com.perfectomobile.selenium.util.EclipseConnector;

 	private static void setExecutionIdCapability(DesiredCapabilities capabilities) throws IOException {
		EclipseConnector connector = new EclipseConnector();
		String executionId = connector.getExecutionId();
		capabilities.setCapability(EclipseConnector.ECLIPSE_EXECUTION_ID, executionId);
	}

	
}