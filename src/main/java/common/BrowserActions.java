package common;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * <h1>Browser Class</h1> <B>Browser Class is used to do all Browser operations
 * like maximize,minimize,wait,close wait,waitUntil,refresh,back and forward,
 * getCurrentUrl,verifyCurrentUrl, switchToNewWindow</B>
 * <p>
 * It should inherits BrowserFactory class to get driver and browser
 * </p>
 *
 * @author Shivanshu Singla
 * @version 1.0
 * @since 27-10-2020
 */

public class BrowserActions {

  public static void maximize(String description) {
    BrowserFactory.driver.manage().window().maximize();
    TestLogs.info(description);
  }

  public static void wait(int time) {
    TestLogs.info("Waiting for {} seconds", time);
    BrowserFactory.driver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
  }

  public static void explicitWait(int time, WebElement element) {
    TestLogs.info("Waiting for {} seconds", time);
    WebDriverWait wait = new WebDriverWait(BrowserFactory.driver, time);
    wait.until(ExpectedConditions.elementToBeClickable(element));
  }

  public String openWebsiteURL(String url, String description) {
    BrowserFactory.driver.get(url);
    TestLogs.info(new StringBuilder().append(description).append(url).toString());
    return url;
  }

  public String getTitle() {
    String pageTitle = BrowserFactory.driver.getTitle();
    TestLogs.info("Page title - {}", pageTitle);
    return pageTitle;
  }
}
