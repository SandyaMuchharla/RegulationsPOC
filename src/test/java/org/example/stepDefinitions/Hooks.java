package org.example.stepDefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.example.utils.DriverBase;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class Hooks extends DriverBase{
    private DriverBase driverBase;

    @Before()
    public void driverSetup()
    {
        DriverBase driverBase = new DriverBase();
        String browserName = System.getProperty("browser");
        System.out.println(browserName);
        if (browserName == null) {
            browserName = "chrome";
        }
        driverBase.initialize(browserName);
    }

    @After(order = 2)
    public void addDataAndClose(io.cucumber.java.Scenario scenario) {
        if (scenario.isFailed() && driver instanceof TakesScreenshot) {
            addScreenshot(scenario);
        }
        addPageLink(scenario);
    }

    @After(order = 1)
    public void tearDown() {
        getDriver().quit();
        tdriver.remove();
    }

    private void addPageLink(io.cucumber.java.Scenario scenario) {
        scenario.log(String.format("Test page: %s", getDriver().getCurrentUrl()));
        scenario.log(String.format("Test Browser: %s", System.getProperty("browser")));
    }

    private void addScreenshot(io.cucumber.java.Scenario scenario) {
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        scenario.attach(screenshot, "image/png", "Screenshot");
    }
}



