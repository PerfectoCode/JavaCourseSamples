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

    public static void main(String[] args) throws MalformedURLException, IOException {
        System.out.println("Run started");

        String browserName = "mobileOS";
        DesiredCapabilities capabilities = new DesiredCapabilities(browserName, "", Platform.ANY);
        String host = "myHost.perfectomobile.com";
        capabilities.setCapability("user", "myUser");
        capabilities.setCapability("password", "myPassword");

        //TODO: Change your device ID
        capabilities.setCapability("deviceName", "12345");

        // Use the automationName capability to define the required framework - Appium (this is the default) or PerfectoMobile.
        capabilities.setCapability("automationName", "Appium");

        // Call this method if you want the script to share the devices with the Perfecto Lab plugin.
        setExecutionIdCapability(capabilities, host);

        // Application settings examples.
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("appPackage", "com.google.android.keep");
		capabilities.setCapability("appActivity", ".activities.BrowseActivity");

        // Add a persona to your script (see https://community.perfectomobile.com/posts/1048047-available-personas)
        //capabilities.setCapability(WindTunnelUtils.WIND_TUNNEL_PERSONA_CAPABILITY, WindTunnelUtils.GEORGIA);

        // Name your script
        capabilities.setCapability("scriptName", "Android_Keep");

        AndroidDriver driver = new AndroidDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
        // IOSDriver driver = new IOSDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

        try {
            // write your code here
			By getStarted = By.xpath("//android.widget.TextView[@text='GET STARTED']");
			By newList = By.xpath("//*[@resource-id=\"com.google.android.keep:id/new_list_button\"]");
			By titleF = By.xpath("//*[@resource-id=\"com.google.android.keep:id/editable_title\"]");
			By addItem = By.xpath("//android.widget.ImageView[parent::android.widget.LinearLayout[@content-desc='Add list item']]");
			By newItem = By.xpath("//android.widget.EditText[@resource-id='com.google.android.keep:id/description' and @text ='']");
			WebElement field;
			String title = "Test Scripting";
			
            try
            {
                field = driver.findElement(getStarted);
                field.click();
                System.out.println("Pressed the Get Started button to get into the application");
            }
            catch (Exception n)
            {
            	System.out.println("No Welcome screen - just plow ahead");
            }	
            
			field = driver.findElement(newList);
			field.click();
			// give the list a title - that can be checked on the main Note-board display
			field = driver.findElement(titleF);
			//field.click();
			field.sendKeys(title);
			// app is now ready to accept text for the first list item
			//field = driver.findElement(newItem);
			field = driver.findElement(newItem);
			//field.click();
			field.sendKeys("Select app to test");
			// click the add field
			field = driver.findElement(addItem);
			field.click();
			// Enter the second line in the list
			field = driver.findElement(newItem);
			//field.click();
			field.sendKeys("Write the script");
			
			// click the add field
			field = driver.findElement(addItem);
			field.click();
			// return to the Note-board
			field = driver.findElementByClassName("android.widget.ImageButton");
			field.click();
			
			try {
				//Verify that new note appears on the Note-board
				field = driver.findElementByXPath("//*[@resource-id=\"com.google.android.keep:id/title\" and @text=\"" + title + "\"]");
			} catch (NoSuchElementException f) {
				System.out.println("Did not find the note on the NoteBoard");
				//f.printStackTrace();
			}
			// cleanup the application before closing the connection
			String cmnd = "mobile:application:clean";
			Map<String, Object> params = new HashMap<>();
			params.put("name", "keep");
			driver.executeScript(cmnd, params);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Retrieve the URL of the Wind Tunnel Report, can be saved to your execution summary and used to download the report at a later point
                String reportURL = (String)(driver.getCapabilities().getCapability(WindTunnelUtils.WIND_TUNNEL_REPORT_URL_CAPABILITY));

                driver.close();

                // In case you want to download the report or the report attachments, do it here.
                PerfectoLabUtils.downloadReport(driver, "pdf", "C:\\test\\report-KeepApp");
                // PerfectoLabUtils.downloadAttachment(driver, "video", "C:\\test\\report\\video", "flv");
                // PerfectoLabUtils.downloadAttachment(driver, "image", "C:\\test\\report\\images", "jpg");

            } catch (Exception e) {
                e.printStackTrace();
            }

            driver.quit();
        }

        System.out.println("Run ended");
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
