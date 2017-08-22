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
import pages.*;

import com.perfectomobile.selenium.util.EclipseConnector;

public class AppiumTest {

	static LoginPage appPage;
	static AndroidDriver driver;
	
    public static void main(String[] args) throws MalformedURLException, IOException {
        System.out.println("Run started");

        String browserName = "mobileOS";
        DesiredCapabilities capabilities = new DesiredCapabilities(browserName, "", Platform.ANY);
        String host = "<YOUR CLOUD>.perfectomobile.com";
        capabilities.setCapability("user", <USERNAME>);
        capabilities.setCapability("password", <PASSWORD>);

        //TODO: Change your device ID
        capabilities.setCapability("deviceName", <DEVICE ID>);

        // Use the automationName capability to define the required framework - Appium (this is the default) or PerfectoMobile.
        capabilities.setCapability("automationName", "Appium");

        // Call this method if you want the script to share the devices with the Perfecto Lab plugin.
        setExecutionIdCapability(capabilities, host);

        // Application settings examples.
        // capabilities.setCapability("app", "PRIVATE:applications/Errands.ipa");
        // For Android:
        capabilities.setCapability("appPackage", "com.example.testapp");
        // capabilities.setCapability("appActivity", ".activities.BrowseActivity");
        // For iOS:
        // capabilities.setCapability("bundleId", "com.yoctoville.errands");

        // Add a persona to your script (see https://community.perfectomobile.com/posts/1048047-available-personas)
        //capabilities.setCapability(WindTunnelUtils.WIND_TUNNEL_PERSONA_CAPABILITY, WindTunnelUtils.GEORGIA);

        // Name your script
        // capabilities.setCapability("scriptName", "AppiumTest");

        driver = new AndroidDriver<WebElement>(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
        // IOSDriver driver = new IOSDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

        try {
            // write your code here
        	appPage = new LoginPage(driver);
        	
        	MainPage main = loginTest();
        	page2Test(main);
 /*       	
        	main.goThird();
        	WebElement layout = driver.findElementByXPath("//android.widget.TextView[@text='Net']/..");
        	layout.findElement(By.xpath("./android.widget.Button[@text='Click']")).click();
 */       	
        	page3Test(main);
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

    private static void setExecutionIdCapability(DesiredCapabilities capabilities, String host) throws IOException {
        EclipseConnector connector = new EclipseConnector();
        String eclipseHost = connector.getHost();
        if ((eclipseHost == null) || (eclipseHost.equalsIgnoreCase(host))) {
            String executionId = connector.getExecutionId();
            capabilities.setCapability(EclipseConnector.ECLIPSE_EXECUTION_ID, executionId);
        }
    }
    
    private static MainPage loginTest() throws Exception {
    	LoginPage logPg = appPage.getLogin();
    	System.out.println("Starting login test");
    	
    	// login the user - gain access to start page
    	StartPage stPage = logPg.login("john", "Perfecto1");
    	String welMsg = stPage.getWelcomeText();
    	if (welMsg.contains("john")) {
    		System.out.println("Successful login");
    		return stPage.clickContinue();
    	} else {
    		throw new Exception("failed to login to john account");
    	} 	
    }
    
    private static void page2Test(MainPage mainPg) {
    	System.out.println("Starting to test Second Page");
    	String phrase = "The quick gray fox jumped";
    	
    	SecondPage page2 = mainPg.goSecond();
    	page2.enterText(phrase);
    	String result = page2.echoText();
    	// verify that resulting string is same as original
    	if (result.equalsIgnoreCase(phrase)) {
    		System.out.println("Successful echo");
    	} else {
    		System.out.println("Something is wrong on Second Screen");
    	}
    	// go back to the main page for any continuation
    	page2.goBack();
    }

    private static void page3Test(MainPage mainPg) {
    	String app1 = "LinkedIn";
    	String app2 = "Net";
    	
    	System.out.println("Starting to test Third Page");
    	ThirdPage page3 = mainPg.goThird();
    	String result = page3.clickApp(app1);
    	System.out.println(result);
    	
    	// going to second app
    	result = page3.clickApp(app2);
    	System.out.println(result);
    	
    	page3.goBack();
    }
}
