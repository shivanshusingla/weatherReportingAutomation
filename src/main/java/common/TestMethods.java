package common;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.testng.Assert;

/**
 * <h1>TestMethods Class</h1> <B>Main purpose of TestMethods Class is to create
 * Wrapper Methods of getText, click, doubleClick etc. so that before browser will click on any
 * element, It will verify wither the element is enabled or not and before getting text of any
 * element it will verify wither element is present or not. and it will throw assertion failure if
 * element is not present or displayed before click any element. Reporter.log() methods are use to
 * print strings in mail </B>
 *
 * @author Shivanshu Singla
 * @version 1.0
 * @since 27-10-2020
 */

public class TestMethods {

  public static void clear(WebElement element, String description) {
    element.clear();
    TestLogs.info(new StringBuilder().append("Data cleared of ").append(description).toString());
  }

  public static void click(WebElement element, String description) {
    try {
      BrowserActions.explicitWait(15, element);

      element.click();
      TestLogs.info("Click on '" + description + "'");
    } catch (StaleElementReferenceException ser) {
      TestLogs.fail(ser.toString());
    } catch (UnreachableBrowserException ube) {
      TestLogs.fail(ExceptionUtils.getStackTrace(ube));
    } catch (org.openqa.selenium.TimeoutException tm) {
      TestLogs.fail(tm.toString());
    } catch (Exception e) {
      TestLogs.fail(e.toString());
    }
  }

  public static void actionClick(WebElement element, String description) {
    try {
      BrowserActions.explicitWait(15, element);

      TestLogs.info("Right Click on - {}", element.getText());
      Actions actions = new Actions(BrowserFactory.driver);
      actions.moveToElement(element).click().perform();
    } catch (Exception e) {
      TestLogs.fail(e.toString());
    }
  }

  public static String getText(WebElement element, String description) {
    BrowserActions.explicitWait(10, element);
    TestLogs.info("Getting text of '" + description + "'");
    String actualText = null;
    if (isElementDisplayed(element, description)) {
      actualText = element.getText();
      TestLogs.pass("Text is - '" + actualText + "' and Element is displayed");
    } else if (isElementDisplayed(element, description) && actualText.contains("")) {
      TestLogs.warn("Element is displayed but Empty text is present");
    } else {
      TestLogs.fail(
          "Text is not found & Element not displayed! for the element - " + element.toString());
    }
    return actualText;
  }

  public static boolean isElementDisplayed(WebElement element, String description) {
    if (element == null) {
      Assert.assertNotNull(element, "WebElement have a null value");
      TestLogs.info("Null element. Trying again...");
    }
    try {
      BrowserActions.explicitWait(15, element);
      return element.isDisplayed();
    } catch (StaleElementReferenceException sre) {
      TestLogs.warn(sre.toString());
    } catch (NoSuchElementException e) {
      TestLogs.warn(e.toString());
    } catch (ElementNotVisibleException ev) {
      TestLogs.warn(ev.toString());
    }
    return false;
  }

  public static boolean verifyElementPresent(WebElement element, String description) {
    if (element.isDisplayed()) {
      TestLogs.pass("Verified the presence of element '" + description + "' on the page");
      return true;
    } else {
      TestLogs.warn("Element '" + description + "' is not present on the page");
    }
    return false;
  }

  /**
   * <b>getAttribute(WebElement element, String attributeName, String description)
   * Method</b>
   * <p>
   * This method is used to get attribute value of attribute of WebElement like href, title etc. in
   * <a> tag
   * </p>
   *
   * @return value
   */

  public static String getAttribute(WebElement element, String attributeName, String description) {
    TestLogs.info("Getting value of attribute '" + attributeName + "' for :" + description);
    String value = "";
    try {
      value = element.getAttribute(attributeName);
    } catch (Exception wde) {
      TestLogs
          .fail("Exception occurred in fetching value of attribute '" + attributeName + "' for :"
              + description + " : " + wde.getMessage());
    }

    return value;
  }

  public static List<String> getTextOfAllElements(List<WebElement> elements) {
    List<String> textOfAllElements = new ArrayList<String>();
    if (!elements.isEmpty()) {
      for (WebElement e : elements) {
        textOfAllElements.add(e.getText());
      }
    } else if (textOfAllElements.contains("")) {
      TestLogs.warn("Element is displayed but Empty text is present");
    } else {
      TestLogs.fail("Text is not found & Elements are not displayed!");
    }
    return textOfAllElements;
  }

  public static boolean scrollDownToView(WebElement element) {
    JavascriptExecutor jse = (JavascriptExecutor) BrowserFactory.driver;
    jse.executeScript("arguments[0].scrollIntoView(true)", element);
    return element.isDisplayed();
  }

  public static void scrollDownToBottom() {
    JavascriptExecutor jse = (JavascriptExecutor) BrowserFactory.driver;
    jse.executeScript("window.scrollTo(0, document.body.scrollHeight)");
  }

  public static void enterData(WebElement element, String value, String description) {
    BrowserActions.explicitWait(10, element);
    if (!value.equalsIgnoreCase("{skip}")) {

      // encode the html characters so that they get printed correctly
      String message = StringUtils.replaceEach(value, new String[]{"&", "\"", "<", ">"},
          new String[]{"&amp;", "&quot;", "&lt;", "&gt;"});
      TestLogs.info("Enter the " + description + " as '" + message + "'");
      element.clear();
      element.sendKeys(value);
    } else {
      TestLogs.fail("Skipped data entry for " + description);
    }
  }

}
