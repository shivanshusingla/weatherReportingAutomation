package common;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;

/**
 * <h1>Log Class</h1> <B>Main purpose of Log Class is to create logger Methods
 * of log4j like pass, fail, warn, info etc. if in any class I have to verify wither the element is
 * displayed or not I just write log.pass in passed case and log.fail in failed case.</B>
 *
 * @author Shivanshu Singla
 * @version 1.0
 * @since 27-10-2020
 */

public class TestLogs {

  private static final Logger log = LoggerFactory.getLogger(TestLogs.class);

  public TestLogs() {
    System.setProperty(Constants.CURRENT_DATE,
        new SimpleDateFormat("dd-MM-yyyy hh a").format(new Date()));
    PropertyConfigurator
        .configure(System.getProperty(Constants.USER_DIR) + File.separator + "src" + File.separator
            + "main" + File.separator + "resources" + File.separator + "log4j.properties");
  }

  public static String info(String description, Object... args) {
    log.info(description, args);
    Reporter.log(description);
    return description;
  }

  public static String warn(String description, Object... args) {
    log.warn(description, args);
    Reporter.log(new StringBuilder().append("<span style=\"color:yellow\")>").append(description)
        .append("</span>")
        .toString());
    return description;
  }

  public static String pass(String description) {
    log.info(description);
    Reporter.log(new StringBuilder().append("<span style=\"color:green\")>").append(description)
        .append("</span>")
        .toString());
    return description;
  }

  public static String fail(String description) {
    log.error(description);
    Reporter.log(new StringBuilder().append("<span style=\"color:red\")>").append(description)
        .append("</span>")
        .toString());
    Assert.fail(description);
    return description;
  }

}
