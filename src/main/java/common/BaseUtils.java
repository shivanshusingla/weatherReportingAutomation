package common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * <h1>Base Utils Class</h1> <B>BaseUtils Class is working as a Base utils Class
 * for all classes in all common classes. to get different OS name, takeSnapShot on failure test
 * case.</B>
 *
 * @author Shivanshu Singla
 * @version 1.0
 * @since 27-10-2020
 */

public class BaseUtils {

  /**
   * <p>
   * This method is used to get OS Name on which test cases are running like - Windows 98,Windows
   * 2000,Windows ME,Windows XP Linux,Solaris/sparc,MacOSX
   * </p>
   *
   * @return System.getProperty(" os.name ")
   */

  public static String getOSName() {
    return System.getProperty(Constants.OS_NAME);
  }

  public static void takeSnapShot(WebDriver webdriver, String fileWithPath) {
    TakesScreenshot scrShot = ((TakesScreenshot) webdriver);
    File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
    File DestFile = new File(fileWithPath);
    try {
      FileUtils.copyFile(SrcFile, DestFile);
    } catch (IOException e) {
      TestLogs.fail(ExceptionUtils.getStackTrace(e));
    }
  }

  public static Date getCurrentDateTime() {
    DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);
    Date date = new Date();
    String currentDate = dateFormat.format(date);
    Date dateFromString = null;
    try {
      dateFromString = dateFormat.parse(currentDate);
    } catch (ParseException e) {
    }
    return dateFromString;
  }

  public static Date getCurrentDateTime(String pattern) { // for eg.-pattern="dd-MM-yyyy HH:mm:ss"
    DateFormat dateFormat = new SimpleDateFormat(pattern);
    Date date = new Date();
    String currentDate = dateFormat.format(date);
    Date dateFromString = null;
    try {
      dateFromString = dateFormat.parse(currentDate);
    } catch (ParseException e) {
    }
    return dateFromString;
  }

  protected static Date getTime(long millis) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(millis);
    return calendar.getTime();
  }

  public static boolean netIsAvailable() {
    try {
      final URL url = new URL("http://www.google.com");
      final URLConnection conn = url.openConnection();
      conn.connect();
      return true;
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      return false;
    }
  }

  public static String encodeFileToBase64Binary(File file) {
    String encodedfile = null;
    try {
      FileInputStream fileInputStreamReader = new FileInputStream(file);
      byte[] bytes = new byte[(int) file.length()];
      fileInputStreamReader.read(bytes);
      encodedfile = new String(Base64.encodeBase64(bytes), StandardCharsets.UTF_8);
    } catch (FileNotFoundException fnfe) {
      TestLogs.fail(fnfe.getMessage());
    } catch (IOException ioe) {
      TestLogs.fail(ioe.getMessage());
    }
    return encodedfile;
  }
}
