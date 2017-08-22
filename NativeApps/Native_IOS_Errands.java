import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.*;
import org.openqa.selenium.html5.*;
import org.openqa.selenium.logging.*;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.Cookie.Builder;
import org.openqa.selenium.NoSuchElementException;

import io.appium.java_client.*;
import io.appium.java_client.android.*;
import io.appium.java_client.ios.*;

import com.perfectomobile.selenium.util.EclipseConnector;

public class AppiumTest {

    public static void main(String[] args) throws MalformedURLException, IOException {
        System.out.println("Run started");

        String browserName = "mobileOS";
        DesiredCapabilities capabilities = new DesiredCapabilities(browserName, "", Platform.ANY);
        String host = <your cloud>;
        String user = <username>;
        String pw = <password>;
        try {
            PerfectoLabUtils.uploadMedia(host, user, pw, 
                    "C:\\test\\applications\\Errands.ipa", 
                    "PRIVATE:applications/Errands.ipa");
        } catch (IOException upl) {
            System.out.println("Unable to upload application");
        } 
        capabilities.setCapability("user", user);
        capabilities.setCapability("password", pw);
        //TODO: Change your device ID
        capabilities.setCapability("deviceName", <device ID>);  

        // Use the automationName capability to define the required framework - Appium (this is the default) or PerfectoMobile.
        capabilities.setCapability("automationName", "Appium");

        // Call this method if you want the script to share the devices with the Perfecto Lab plugin.
        setExecutionIdCapability(capabilities, host);

        // Application settings examples.
        capabilities.setCapability("autoLaunch", false);
        capabilities.setCapability("fullReset", true);
        capabilities.setCapability("app", "PRIVATE:applications/Errands.ipa");
        capabilities.setCapability("bundleId", "com.yoctoville.errands");

        // Add a persona to your script (see https://community.perfectomobile.com/posts/1048047-available-personas)
        //capabilities.setCapability(WindTunnelUtils.WIND_TUNNEL_PERSONA_CAPABILITY, WindTunnelUtils.GEORGIA);

        // Name your script
        // capabilities.setCapability("scriptName", "AppiumTest");

        //AndroidDriver driver = new AndroidDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
        IOSDriver driver = new IOSDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        try {
            // write your code here
        	WebElement alert;
        	driver.launchApp();
        	// Check for Alert requesting Apple ID
        	try {
        		alert = driver.findElementByXPath("//UIAAlert[contains(@name, 'Sign In')]");
        		alert.findElement(By.className("UIATextField")).sendKeys("perfectoyw@gmail.com");
        		alert.findElement(By.className("UIASecureTextField")).sendKeys("Appium#8");
        		alert.findElement(By.name("OK")).click();
        	} catch (NoSuchElementException n1) {
                System.out.println("No username alert, just plow ahead!");
            }
        	
            // press Don't Allow button on Notifications screen if it appears
            try {
                alert = driver.findElementByXPath("//UIAAlert[contains(@name, \"Send You Notifications\")]");
                alert.findElement(By.xpath("//*[starts-with(@name, 'Don')]")).click();
            } catch (NoSuchElementException n1) {
                System.out.println("No notifications popup, just plow ahead!");
            }
            
            // press OK button on Welcome popup if it appears
            try {
                WebElement header = driver.findElementByName("Welcome");
                WebElement okBtn = driver.findElementByName("OK");
                okBtn.click();
            } catch (NoSuchElementException n1) {
                System.out.println("No welcome popup, just plow ahead!");
            }
            
            // Press "New Note" button (on right end of header)
            driver.findElementByName("add os7").click();


            // Verify that arrive at the New Task screen
            try {
                WebElement nTask = driver.findElementByName("New Task");
            } catch (NoSuchElementException n) {
                System.out.println("Did not reach New Task window!");
                throw n;
            }
            
            // Enter a title for the new task
            WebElement title = driver.findElementByIosUIAutomation("UIATarget.localTarget().frontMostApp().mainWindow().textFields()[0]");
            //WebElement title = driver.findElementByClassName("UIATextField[1]");
            title.sendKeys("Prepare the script for Native Application");
            
            // Enter text into the Details field
            WebElement detail = driver.findElementByIosUIAutomation(".textFields()[1]");
            //WebElement detail = driver.findElementByClassName("UIATextField[2]");
            detail.sendKeys("Select application, upload, and use Object Spy");
            
            // Click "Done" to go to next stage
            driver.findElementByName("Done").click();
            
            // Click on the Due Date field to select the due date
            try {
                driver.findElementByXPath("//UIAStaticText[starts-with(@name, 'Due Date')]").click();
            } catch (NoSuchElementException n) {
                System.out.println("Not showing second stage display!");
                throw n;
            }
            // select that task is due next week
            driver.findElementByName("+1 Week").click();
            
            // Click "Done" to complete the task definition
            driver.findElementByName("Done").click();
            
            // Verify that task is listed
            try {
                WebElement nTask = driver.findElementByName("Prepare the script for Native Application");
            } catch (NoSuchElementException n) {
                System.out.println("The task is not listed!");
                throw n;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Retrieve the URL of the Wind Tunnel Report, can be saved to your execution summary and used to download the report at a later point
                String reportURL = (String)(driver.getCapabilities().getCapability(WindTunnelUtils.WIND_TUNNEL_REPORT_URL_CAPABILITY));

                driver.close();

                // In case you want to download the report or the report attachments, do it here.
                PerfectoLabUtils.downloadReport(driver, "pdf", "C:\\test\\report-errands");
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
