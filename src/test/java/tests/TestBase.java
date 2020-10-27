package tests;

import common.BrowserActions;
import common.BrowserFactory;
import common.Constants;
import common.FileDataReader;
import common.SendMail;
import common.TestLogs;
import java.io.File;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

/**
 * <h1>test.TestBase Class</h1> <B>test.TestBase Class is working as a Base Class for all
 * classes in Test Package. Every test class in com.reporting.test package should extends
 * test.TestBase Class. BaseClass is inherited in this class to get object references of
 * Browser,TestCaseLogs,TestMethods,FileDataReader class.</B>
 *
 * @author Shivanshu Singla
 * @version 1.0
 * @since 26-10-2020
 */

public class TestBase {

  BrowserFactory browserFactory = new BrowserFactory();
  BrowserActions browserActions = new BrowserActions();
  FileDataReader fileDataReader = new FileDataReader();
  WebDriver driver;

  /**
   * <p>
   * This method is used to delete logs folder files which will run before suite.As logs file is
   * generated every time when suite runs which will create lot of memory consumption after months.
   * So, this method is used to delete logs .html files which was not modified from more than 15
   * days.
   * </p>
   */

  @BeforeSuite(description = "Delete 15 days old logs files", enabled = true) // In logs folder
  public void deleteLogs() {
    File projectDirectory = new File(System.getProperty(Constants.USER_DIR));
    File logsDirectory = new File(
        projectDirectory + File.separator + FileDataReader.getPropertyValue(Constants.LOGS_PATH));
    File screenshotDirectory = new File(
        projectDirectory + File.separator + FileDataReader
            .getPropertyValue(Constants.SCREENSHOT_PATH));
    // Giving the permissions to file
    File[] listOfFiles = projectDirectory.listFiles();
    for (int i = 0; i < listOfFiles.length; i++) {
      listOfFiles[i].setWritable(true);
      listOfFiles[i].setExecutable(true);
      listOfFiles[i].setReadable(true);
    }
    // printing the permissions associated with the file
    TestLogs.info("Logs Folder Permissions - Executable: {} , Readable: {}, Writable: {}",
        logsDirectory.canExecute(), logsDirectory.canRead(), logsDirectory.canWrite());
    TestLogs.info("Screenshots Folder Permissions - Executable: {} ,Readable: {} , Writable: {}",
        screenshotDirectory.canExecute(), screenshotDirectory.canRead(),
        screenshotDirectory.canWrite());
    if (logsDirectory.canWrite()) {
      TestLogs.info("Log file is created with filename - {}",
          FileDataReader.getLastModifiedFile(logsDirectory).getName());
    } else {
      TestLogs.warn(
          "Directory does not have permissions write on files. Please change the permission access!");
    }
    FileDataReader.deleteFiles(logsDirectory, 15);
    FileDataReader.deleteFiles(screenshotDirectory, 15);
  }

  /**
   * <p>
   * This method is used to Initialize WebDriver and Launch URL which will run before every Test. In
   * this method we can run our test cases on multiple browser by enter Browser name in
   * Properties/config.properties and it will always launch before every test not class and Launch
   * URL is provided in BeforeTest because in every test case same URL is required to be launched.
   * It is provided in BeforeTest
   * </p>
   */

  @BeforeTest(description = "Initialize WebDriver and Launching the Browser")
  @Parameters({"browserName"})
  public void setBrowser(String browserName) {
//    if (browserName.isEmpty() || browserName.equalsIgnoreCase(null)) {
//      driver = browserFactory
//          .getDriver(FileDataReader.getPropertyValue(Constants.BROWSER_NAME).toLowerCase(),
//              "Starting WebDriver, ");
//    } else {
    driver = browserFactory
        .getDriver(browserName, "Starting WebDriver, ");
  }

  /**
   * <p>
   * This method is used to launch the URL-<a href="https://weather.com/en-IN/">Weather URL</a> URL
   * value can be change in Key-URL in Properties/config.properties
   * </p>
   */

  @BeforeClass(description = "Launching the Weather Url")
  public void launchWeatherURL() {
    browserActions
        .openWebsiteURL(FileDataReader.getPropertyValue(Constants.WEATHER_URL),
            "Opening Url - ");
    BrowserActions.wait(20);
  }

  /**
   * <p>
   * This method is used to close the WebBrowser every Test
   * </p>
   */

  @AfterTest(description = "Closing the Browser", alwaysRun = true)
  public void closeBrowser() {
    driver.quit();
    TestLogs.info("Browser Closed");
  }

  /**
   * <p>
   * This method is used to send Test Reports in email after every suite to users It will send email
   * in both passed and failed test cases except test cases skipped
   * </p>
   */

  @AfterSuite(description = "Sending Mail", enabled = true)
  public void sendEMail() {
    sendMail(FileDataReader.getExcelData(Constants.SHEET_CREDENTIALS_PATH, "MailIds", 1, 1),
        FileDataReader.getExcelData(Constants.SHEET_CREDENTIALS_PATH, "MailIds", 1, 2));
  }

  public void sendMail(String to, String cc) {
    String subject = "Weather Reporting Test Report";
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        SendMail.sendTestReportByGMail(
            FileDataReader.getExcelData(Constants.SHEET_CREDENTIALS_PATH, "GmailID", 1, 0),
            FileDataReader.getExcelData(Constants.SHEET_CREDENTIALS_PATH, "GmailID", 1, 1), to, cc,
            subject,
            new StringBuilder()
                .append("</br></br><body align=\"center\"><h2>Test results Details</h2>")
                .append(FileDataReader
                    .readFile(new File(System.getProperty(Constants.USER_DIR) + File.separator
                        + FileDataReader
                        .getPropertyValue(Constants.MailConstants.MAIL_REPORT_PATH))))
                .append("</body>").toString()

        );
      }
    });
  }
}