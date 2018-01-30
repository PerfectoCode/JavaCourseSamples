import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.*;
import org.openqa.selenium.remote.*;

import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.Job;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;

/**
 * This template is for users that use DigitalZoom Reporting (ReportiumClient).
 * For any other use cases please see the basic template at https://github.com/PerfectoCode/Templates.
 * For more programming samples and updated templates refer to the Perfecto Documentation at: http://developers.perfectomobile.com/
 */
public class RemoteWebDriverTest {

    public static void main(String[] args) throws IOException {
        System.out.println("Run started");

        String browserName = "mobileOS";
        DesiredCapabilities capabilities = new DesiredCapabilities(browserName, "", Platform.ANY);
        //TODO: change <host> to match your host
        String host = "<host>.perfectomobile.com";
        //TODO: change <username> to match your perfecto username
        capabilities.setCapability("user", "<username>");
        //TODO: change <password> to match your perfecto password
        capabilities.setCapability("password", "<password>");

        //TODO: Change your device ID
        capabilities.setCapability("deviceName", "1234");

        // Use the automationName capability to define the required framework - Appium (this is the default) or PerfectoMobile.
        // capabilities.setCapability("automationName", "PerfectoMobile");

        // Call this method if you want the script to share the devices with the Perfecto Lab plugin.
        PerfectoLabUtils.setExecutionIdCapability(capabilities, host);

        // Name your script
        // capabilities.setCapability("scriptName", "RemoteWebDriverTest");

        RemoteWebDriver driver = new RemoteWebDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

        // Reporting client. For more details, see http://developers.perfectomobile.com/display/PD/Reporting
        PerfectoExecutionContext perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
                .withProject(new Project("Assignment1", "1.0"))
                .withJob(new Job("WikipediaJob", 45))
                .withContextTags("ScreenshotWiki")
                .withWebDriver(driver)
                .build();
        ReportiumClient reportiumClient = new ReportiumClientFactory().createPerfectoReportiumClient(perfectoExecutionContext);

        try {
        	//TODO: you may need to change this directory.
        	String filePath = "c://test/images/";
        	
        	
            reportiumClient.testStart("Screenshot Wikipedia", new TestContext("screenshot", "wikipedia"));

            reportiumClient.stepStart("Navigate to Wikipedia");
            //Navigate to wikipedia
            driver.get("www.wikipedia.com");
            
            //Find the wikipedia logo
            switchToContext(driver, "WEBVIEW");
            driver.findElementsByXPath("//*[@class=\"central-textlogo__image sprite svg-Wikipedia_wordmark\"]");
            reportiumClient.stepEnd();
            //Call getScreenshotAs method to create image file
            reportiumClient.stepStart("Take Screnshot");
            TakesScreenshot scrShot =((TakesScreenshot)driver);
            reportiumClient.stepEnd();
            reportiumClient.stepStart("Save File");
            //Move image file to new destination
            File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);
            File DestFile=new File(filePath+"image.jpg");
            //Copy file at destination
            org.apache.commons.io.FileUtils.copyFile(SrcFile, DestFile);
            reportiumClient.stepEnd();
            reportiumClient.testStop(TestResultFactory.createSuccess());
        } catch (Exception e) {
            reportiumClient.testStop(TestResultFactory.createFailure(e.getMessage(), e));
            e.printStackTrace();
        } finally {
            try {
                driver.quit();

                // Retrieve the URL to the DigitalZoom Report (= Reportium Application) for an aggregated view over the execution
                String reportURL = reportiumClient.getReportUrl();

                // Retrieve the URL to the Execution Summary PDF Report
                String reportPdfUrl = (String)(driver.getCapabilities().getCapability("reportPdfUrl"));
                // For detailed documentation on how to export the Execution Summary PDF Report, the Single Test report and other attachments such as
                // video, images, device logs, vitals and network files - see http://developers.perfectomobile.com/display/PD/Exporting+the+Reports

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("Run ended");
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
}
