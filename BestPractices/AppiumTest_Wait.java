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

		WebElement interval;
        String browserName = "mobileOS";
        DesiredCapabilities capabilities = new DesiredCapabilities(browserName, "", Platform.ANY);
        String host = "myHost.perfectomobile.com";
        capabilities.setCapability("user", "myUser");
        capabilities.setCapability("password", "myPassword");

		//	Select device running iOS8.3 or iOS8.4
		capabilities.setCapability("platformName", "ios");
		capabilities.setCapability("platformVersion", "8.[34].*");
		// Exclude tablets by selecting only an iPhone
		capabilities.setCapability("model", "iPhone.*");

        // Use the automationName capability to define the required framework - Appium (this is the default) or PerfectoMobile.
        capabilities.setCapability("automationName", "Appium");

        // Call this method if you want the script to share the devices with the Perfecto Lab plugin.
        setExecutionIdCapability(capabilities, host);

        // Application settings examples.
		capabilities.setCapability("bundleId", "perfecto-mobile.timersTest");

        // Add a persona to your script (see https://community.perfectomobile.com/posts/1048047-available-personas)
        //capabilities.setCapability(WindTunnelUtils.WIND_TUNNEL_PERSONA_CAPABILITY, WindTunnelUtils.GEORGIA);

        // Name your script
        // capabilities.setCapability("scriptName", "AppiumTest");

        AndroidDriver driver = new AndroidDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
        // IOSDriver driver = new IOSDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

        try {
            // write your code here
			WebDriverWait wait = new WebDriverWait(driver, 15);
			// trying with a very short implicit wait
			try {
				interval = driver.findElementByName("Timer Interval");
			} catch (NoSuchElementException n) {
				System.out.println("Did not find the Timer header within implicit wait time");
			}

				// Set the timer of the application to display the Label field after 30 seconds
			interval = driver.findElementByName("Timer Interval");
			WebElement time = interval.findElement(By.xpath("following-sibling::*"));
			time.clear();
			time.sendKeys("30");
			
			driver.findElementByName("Click").click();

			// Reset the implicit timeout to zero to avoid conflicts
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
				// demonstrate use of explicit wait
			try {
				wait.until(ExpectedConditions.visibilityOf(driver.findElementByName("Label")));
			} catch (TimeoutException t) {
				System.out.println("Did not find the Label within explicit wait time");
			}

				// Reset the application timer to display Label after 20 seconds
			time.clear();
			time.sendKeys("20");
			
			driver.findElementByName("Click").click();

				// using FluentWait with a function that finds the Label and then clicks the Reset button
			FluentWait<WebDriver> pwait = new FluentWait<WebDriver>(driver)
					.withTimeout(28, TimeUnit.SECONDS)
					.pollingEvery(7, TimeUnit.SECONDS)
					.ignoring(NoSuchElementException.class);
		
			try {
				interval = pwait.until(new Function<WebDriver, WebElement>() {
					public WebElement apply(WebDriver d) {
						WebElement lbl = d.findElement(By.name("Label"));
						WebElement rst = d.findElement(By.name("Reset"));
						rst.click();
						return lbl;
					}
				});
			} catch (TimeoutException t) {
				System.out.println("Did not find the Label within fluent wait time");
			}
			
			time.clear();
			time.sendKeys("30");
			
			driver.findElementByName("Click").click();
		
			// use FluentWait with ExpectedConditions
			FluentWait<WebDriver> fwait = new FluentWait<WebDriver>(driver)
					.withTimeout(28, TimeUnit.SECONDS)
					.pollingEvery(7, TimeUnit.SECONDS)
					.ignoring(NoSuchElementException.class);
		
			try {
				fwait.until(ExpectedConditions.visibilityOf(interval = driver.findElementByName("Label")));
			} catch (TimeoutException t) {
				System.out.println("Did not find the Label within fluent wait time");
			}

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Retrieve the URL of the Wind Tunnel Report, can be saved to your execution summary and used to download the report at a later point
                String reportURL = (String)(driver.getCapabilities().getCapability(WindTunnelUtils.WIND_TUNNEL_REPORT_URL_CAPABILITY));

                driver.close();

                // In case you want to download the report or the report attachments, do it here.
                PerfectoLabUtils.downloadReport(driver, "pdf", "C:\\test\\report-waiting");
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
