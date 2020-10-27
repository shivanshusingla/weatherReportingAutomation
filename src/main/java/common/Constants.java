package common;

/**
 * <h1>Constants Interface</h1> <B>The purpose of Constants Interface is to
 * declare constant values in the project whose values remains constant or unchanged over whole code
 * life cycle. Constants is interface because all constants remains public static final by default.
 * Interface provide this functionality by default.</B>
 *
 * @author Shivanshu Singla
 * @version 1.0
 * @since 21-09-2020
 */
public interface Constants {

  String OS_NAME = "os.name";
  String DATE_TIME_FORMAT = "dd-MM-yyyy HH:mm:ss";
  String USER_DIR = "user.dir";
  String SHEET_CREDENTIALS_PATH = "mail.credentials.path";
  String JAVA_VERSION = "java.version";
  String LOGS_PATH = "logs.path";
  String CURRENT_DATE = "current.date";
  String SCREENSHOT_PATH = "screenshot.path";
  String BROWSER_NAME = "browser.name";
  String WEATHER_URL = "weather.url";
  String SMTP_HOST = "smtp.gmail.com";
  String CITY_NAME = "Delhi";

  /**
   * <h1>MailConstants Nested Interface</h1> <B>The purpose of MailConstants
   * nested Interface is to declare mail constants values and mail properties.</B>
   */

  interface MailConstants {

    String MAIL_EXTENT_REPORT_PATH = "extent.report.path";
    String MAIL_REPORT_PATH = "emailable.report.path";
    String MAIL_REPORT_ATTACHMENT_PATH = "email.attachment.report.path";
    String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
    String MAIL_SMTP_HOST = "mail.smtp.host";
    String MAIL_SMTP_USER = "mail.smtp.user";
    String MAIL_SMTP_PASSWORD = "mail.smtp.password";
    String MAIL_SMTP_PORT = "mail.smtp.port";
    String MAIL_SMTP_AUTH = "mail.smtp.auth";
  }
}
