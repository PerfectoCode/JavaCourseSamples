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
        String host = <Your Cloud>;
        String user = <username>;
        String pw = <password>;
        
        // upload the application file to the Repository
		PerfectoLabUtils.uploadMedia(host, user, pw,
                "C:\\test\\applications\\com.voyagesoftech.myexpensemanager.apk",
                "PRIVATE:applications/com.voyagesoftech.myexpensemanager.apk");
        
        capabilities.setCapability("user", user);
        capabilities.setCapability("password", pw);

        //TODO: Change your device ID
        capabilities.setCapability("deviceName", <Device ID>);

        // Use the automationName capability to define the required framework - Appium (this is the default) or PerfectoMobile.
        capabilities.setCapability("automationName", "Appium");

        // Call this method if you want the script to share the devices with the Perfecto Lab plugin.
        setExecutionIdCapability(capabilities, host);

        // Application installation capabilities.
        capabilities.setCapability("app", "PRIVATE:applications/com.voyagesoftech.myexpensemanager.apk");
        capabilities.setCapability("autoInstrument", true);
        capabilities.setCapability("fullReset", true);
        // For Android:
        capabilities.setCapability("appPackage", "com.voyagesoftech.myexpensemanager");
        // Add persona to your script
        //capabilities.setCapability(WindTunnelUtils.WIND_TUNNEL_PERSONA_CAPABILITY, "Georgia");

        // Name your script
        // capabilities.setCapability("scriptName", "AppiumTest");

        AndroidDriver driver = new AndroidDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
        // IOSDriver driver = new IOSDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        try {
            // write your code here
        	Map<String, Object> params = new HashMap<>();
			// prepare the data input source
			ExpenseData xpnses = new ExpenseData("c:\\test\\data\\expenses.txt");
			String nxtCat, nxtSubCat, nxtExp, nxtAmnt;

			while (xpnses.nextRecord()) {
				// collect the data from the file
				nxtCat = xpnses.get_category();
				nxtSubCat = xpnses.get_subCat();
				nxtExp = xpnses.get_expDesc();
				nxtAmnt = xpnses.get_expAmount();
	        	Boolean ExpClick = true;
	    		driver.context("WEBVIEW");       	
	        	// if the Expense button does not appear, need to skip over the ad
	    		while (ExpClick) {
		        	try {
		        		// click the Expense button to enter a new expense
		        		driver.findElementByXPath("//*[text()='Expense']").click();
		        		ExpClick = false;
		        	} catch (Exception ne) {
		        		// did not find the Expense button - likely cause is ad display
		        		// go BACK and try again
		        		params.clear();
		        		params.put("keySequence", "BACK");
		        		driver.executeScript("mobile:presskey", params);
		        		System.out.println("Press the back button to get out of ad");
		        	}
	        	}

	        	// open the Expense category menu
	        	driver.findElementByXPath("(//select[@id='CategoryExpense'])").click();
	
	        	// select the expense category
	        	driver.context("NATIVE_APP");
	        	driver.scrollTo(nxtCat).click();
	        	//driver.findElementByXPath("//android.widget.CheckedTextView[@text='" + nxtCat + "']").click();
	
	        	driver.context("WEBVIEW");
	        	driver.findElementByXPath("(//select[@id='SubCategoryExpense'])").click();
	
	        	driver.context("NATIVE_APP");
	        	driver.findElementByXPath("//android.widget.CheckedTextView[@text='" + nxtSubCat + "']").click();
	
	        	driver.context("WEBVIEW");
	        	driver.findElementByXPath("(//input[@id='EDescription'])").sendKeys(nxtExp);
	
	        	driver.findElementByXPath("(//input[@id=\"Amountforex\"])").sendKeys(nxtAmnt);
	
	
	        	driver.findElementByXPath("//*[text()='Save']").click();
	
	        	// verify that Success Notification appears
	        	driver.context("NATIVE_APP");
	        	List<WebElement> elems = driver.findElementsByXPath("//android.widget.TextView[@text='Expense added succesfully']");
	        	if (elems.isEmpty()) {
	        		System.out.println("Expense not updated!");
	        	} else {
	        		driver.findElementByXPath("//android.widget.Button[@text='OK']").click();
	        		System.out.println("Expense updated to ledger");
	        	}
	
	        	// use the image identification to click the home page button
	        	params.clear();
	        	params.put("content", "PRIVATE:Home-cropped.png");
                params.put("threshold", 80);
                params.put("match", "bounded");
                params.put("imageBounds.needleBound", 50);
                params.put("screen.top", "0");
                params.put("screen.height", "20%");
				driver.executeScript("mobile:image:select", params);
			}

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // If you added a persona to your script, you can download the report:
                // String reportURL = (String)(driver.getCapabilities().getCapability(WindTunnelUtils.WIND_TUNNEL_REPORT_URL_CAPABILITY));

                driver.close();

                // In case you want to down the report or the report attachments, do it here.
                PerfectoLabUtils.downloadReport(driver, "pdf", "C:\\test\\reportExpData");
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
