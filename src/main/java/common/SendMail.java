package common;

import java.io.File;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class SendMail {

  private static FileDataReader fileDataReader;
  // After complete execution send test report by email

  /**
   * Send email using javax.mail
   *
   * @param from
   * @param password
   * @param to
   * @param cc
   * @param subject
   * @param mailBody
   */
  public static void sendTestReportByGMail(String from, String password, String to, String cc,
      String subject,
      String mailBody) {
    Properties properties = System.getProperties();
    properties.put(Constants.MailConstants.MAIL_SMTP_STARTTLS_ENABLE, "true");
    properties.put(Constants.MailConstants.MAIL_SMTP_HOST, Constants.SMTP_HOST);
    properties.put(Constants.MailConstants.MAIL_SMTP_USER, from);
    properties.put(Constants.MailConstants.MAIL_SMTP_PASSWORD, password);
    properties.put(Constants.MailConstants.MAIL_SMTP_PORT, "587");
    properties.put(Constants.MailConstants.MAIL_SMTP_AUTH, "true");

    Session session = Session.getDefaultInstance(properties);
    MimeMessage message = new MimeMessage(session);

    try {
      // Set from address TO
      message.setFrom(new InternetAddress(from));
      InternetAddress[] parse = InternetAddress.parse(to, true);
      message.setRecipients(javax.mail.Message.RecipientType.TO, parse);
      // Set from address CC
      InternetAddress[] parse1 = InternetAddress.parse(cc, true);
      message.setRecipients(javax.mail.Message.RecipientType.CC, parse1);
      // Set subject
      message.setSubject(subject);
      message.setContent("text/html", "charset=utf-8");
      BodyPart objMessageBodyPart = new MimeBodyPart();
      objMessageBodyPart.setContent(mailBody, "text/html; charset=utf-8");
      Multipart multipart = new MimeMultipart();
      multipart.addBodyPart(objMessageBodyPart);
      objMessageBodyPart = new MimeBodyPart();
      // Set path to the test report file
      String extentReport = System.getProperty(Constants.USER_DIR) + File.separator
          + FileDataReader.getPropertyValue(Constants.MailConstants.MAIL_EXTENT_REPORT_PATH);
      String emailableReport = System.getProperty(Constants.USER_DIR) + File.separator
          + FileDataReader.getPropertyValue(Constants.MailConstants.MAIL_REPORT_ATTACHMENT_PATH);
      File logFile = FileDataReader
          .getLastModifiedFile(new File(System.getProperty(Constants.USER_DIR)
              + File.separator + FileDataReader.getPropertyValue(Constants.LOGS_PATH)));
      addAttachment(multipart, extentReport);
      addAttachment(multipart, emailableReport);
      addAttachment(multipart, logFile.getPath());
      message.setContent(multipart);
      Transport transport = session.getTransport("smtp");
      transport.connect(Constants.SMTP_HOST, from, password);
      transport.sendMessage(message, message.getAllRecipients());
      TestLogs.info("Sent mail successfully.......... \nTo - {} & CC - {}", to, cc);
      transport.close();
    } catch (AddressException ae) {
      TestLogs.fail(ExceptionUtils.getStackTrace(ae));
    } catch (MessagingException me) {
      TestLogs.fail(ExceptionUtils.getStackTrace(me));
    }
  }

  private static void addAttachment(Multipart multipart, String filename) {
    try {
      DataSource source = new FileDataSource(filename);
      BodyPart messageBodyPart = new MimeBodyPart();
      messageBodyPart.setDataHandler(new DataHandler(source));
      messageBodyPart.setFileName(BaseUtils.getCurrentDateTime() + "-" + source.getName());
      multipart.addBodyPart(messageBodyPart);
    } catch (MessagingException me) {
      TestLogs.fail(ExceptionUtils.getStackTrace(me));
    } catch (Exception e) {
    }
  }
}