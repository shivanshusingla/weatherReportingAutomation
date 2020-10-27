package common;

import java.io.File;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * <h1>common.BrowserFactory Class</h1> <B>common.BrowserFactory Class is used to provide
 * generic code for all browser. So, that the test cases can be run every single device and on
 * multiple browser.</B>
 *
 * @author Shivanshu Singla
 * @version 1.0
 * @since 27-10-2020
 */

public class BrowserFactory {

  public static WebDriver driver = null;

  public WebDriver getDriver(String browserName, String message) {
    String browserNames;
    browserNames = browserName.split("-")[0];
    if (browserNames.contains("firefox")) {
      browserNames = "gecko";
    }
    String browserDriver = browserNames + "driver";
    String driverFileDirectory = System.getProperty("user.dir") + File.separator
        + FileDataReader.getPropertyValue("driver.binary.file.path") + File.separator
        + browserDriver;
    if (BaseUtils.getOSName().contains("Windows")) {
      System.setProperty("webdriver." + browserNames + ".driver", driverFileDirectory + ".exe");
    } else if (BaseUtils.getOSName().contains("Linux")) {
      System.setProperty("webdriver." + browserNames + ".driver", driverFileDirectory);
    } else if (BaseUtils.getOSName().contains("Mac")) {
      System.setProperty("webdriver." + browserNames + ".driver", driverFileDirectory + "mac");
    } else {
      TestLogs.fail("Binary files are not available on the running OS-" + BaseUtils.getOSName());
    }
    TestLogs.info("{} in {} OS", message, BaseUtils.getOSName());
    TestLogs.info("Opening {} browser in {} OS", browserNames, BaseUtils.getOSName());
    switch (browserName) {
      case "chrome":
        driver = new ChromeDriver();
        break;
      case "firefox": // Firefox Browser
        driver = new FirefoxDriver();
        break;
      case "chrome-headless":
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--window-size=1920,1080");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--disable-extensions");
        chromeOptions.setExperimentalOption("useAutomationExtension", false);
        chromeOptions.addArguments("--proxy-server='direct://'");
        chromeOptions.addArguments("--proxy-bypass-list=*");
        chromeOptions.addArguments("--start-maximized");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--headless");
        driver = new ChromeDriver(chromeOptions);
        break;
      case "firefox-headless":
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.addArguments("--window-size=1920,1080");
        firefoxOptions.addArguments("--disable-gpu");
        firefoxOptions.addArguments("--disable-extensions");
        firefoxOptions.addArguments("--proxy-server='direct://'");
        firefoxOptions.addArguments("--proxy-bypass-list=*");
        firefoxOptions.addArguments("--start-maximized");
        firefoxOptions.addArguments("--no-sandbox");
        firefoxOptions.addArguments("--headless");
        driver = new FirefoxDriver(firefoxOptions);
        break;
      default:
        TestLogs.fail(new StringBuilder().append("Wrong Browser name - '").append(browserNames)
            .append("' entered!, Please enter correct Browser name in file - '")
            .append(FileDataReader.configFile).append("'").toString());
    }
    if (!driver.toString().contains("null")) {
      TestLogs.info("{} Browser is opened at {}", browserNames, BaseUtils.getCurrentDateTime());
      BrowserActions.maximize("Browser is Maximized");
    } else {
      TestLogs.fail("Browser is not opened!");
    }
    return driver;
  }
}
