package common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Properties;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * <h1>DataReader Class</h1> <B>Main purpose of DataReader Class is to Read data
 * from external data files like excel sheets, text files etc.</B>
 *
 * @author Shivanshu Singla
 * @version 1.0
 * @since 27-10-2020
 */

public class FileDataReader {

  public static File configFileDirectory = new File(
      System.getProperty(Constants.USER_DIR) + File.separator + "target" + File.separator
          + "classes");
  public static String configFile;

  public static String getPropertyValue(String key) {
    FileReader reader;
    Properties p = null;
    try {
      configFile = configFileDirectory + File.separator + "config.properties";
      reader = new FileReader(configFile);
      p = new Properties();
      p.load(reader);
      p.load(reader);
    } catch (FileNotFoundException fnfe) {
      TestLogs.fail(ExceptionUtils.getStackTrace(fnfe));
    } catch (Exception e) {
      TestLogs.fail(ExceptionUtils.getStackTrace(e));
    }
    return p.getProperty(key);
  }

  public static String getExcelData(String dataSheetPropertyPath, String sheetName, int row,
      int col) {
    File excelSheetDirectory = new File(
        System.getProperty(Constants.USER_DIR) + File.separator
            + "src/main/resources/testdatasheets"
            + File.separator + getPropertyValue(dataSheetPropertyPath));
    FileInputStream inputStream = null;
    try {
      inputStream = new FileInputStream(excelSheetDirectory);
    } catch (FileNotFoundException e) {
      TestLogs.fail(ExceptionUtils.getStackTrace(e));
    }
    String fileName = getPropertyValue(Constants.SHEET_CREDENTIALS_PATH);
    Workbook workbook = null;
    String fileExtensionName = fileName.substring(fileName.indexOf('.'));
    // Check condition if the file is xlsx file
    if (fileExtensionName.equals(".xlsx")) {
      TestLogs.info("Getting Data from {}", fileName);
      try {
        workbook = new XSSFWorkbook(inputStream);
      } catch (IOException e) {
        TestLogs.fail(ExceptionUtils.getStackTrace(e));
      }
    }

    /*
     * Check condition if the file is xls file and xls file is not supported in MS
     * Xcel above 2007
     */
    else if (fileExtensionName.equals(".xls")) {
      TestLogs.info("Getting Data from {}", fileName);
      try {
        workbook = new HSSFWorkbook(inputStream);
      } catch (IOException e) {
        TestLogs.fail(ExceptionUtils.getStackTrace(e));
      }
    } else { // for .ods file
      TestLogs.fail("Getting Data from " + fileName + " But .ods file is not implemented");
      /*
       * ODSReader objODSReader = new ODSReader(); objODSReader.readODS(file);
       * Spreadsheet document = new Spreadsheet(inputStream);
       */
    }

    // Read sheet inside the workbook by its name

    Sheet sheet = workbook.getSheet(sheetName);

    DataFormatter formatter = new DataFormatter();
    String val = formatter.formatCellValue(sheet.getRow(row).getCell(col));
    ArrayList list = new ArrayList();
    list.add(val);

    return val;
  }

  public static String readFile(File file) {
    StringBuilder bldr = new StringBuilder();
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(file));
    } catch (FileNotFoundException e) {
      TestLogs.fail(ExceptionUtils.getStackTrace(e));
    }
    String strLine;
    try {
      while ((strLine = br.readLine()) != null) {
        bldr.append(strLine + "\n");
      }
    } catch (IOException e) {
      TestLogs.fail(ExceptionUtils.getStackTrace(e));
    }
    String content = bldr.toString();
    return content;
  }

  /**
   * This method is used to get last modified file from a folder. Main purpose of this method is to
   * get file which is created recently. Also to send the mail attachment of latest report file in
   * directory of many files with same name.
   *
   * @param directory - Path of folder where last modified file is there.
   * @return files[0]
   */

  public static File getLastModifiedFile(File directory) {
    File[] files = directory.listFiles();
    if (files.length == 0) {
      return null;
    }
    Arrays.sort(files, new Comparator<File>() {
      public int compare(File file1, File file2) {
        return new Long(file2.lastModified()).compareTo(file1.lastModified());
      }
    });
    return files[0];
  }

  /**
   * This method is used to delete a all files from provided folder for days from which that
   * particular file is not modified.
   * <p>
   * Main purpose of using this method to screenshots and logs which are older than 20 days.
   * </p>
   *
   * @param folder - Path of folder.
   * @param days   - No. of days on which you want to delete files which not modified since days
   *               provided.
   */

  public static void deleteFiles(File folder, long days) {
    if (folder.exists() && folder.canExecute()) {
      File[] listOfFiles = folder.listFiles();
      for (int i = 0; i < listOfFiles.length; i++) {
        if (listOfFiles[i].isFile()) {
          long now = Calendar.getInstance().getTimeInMillis();
          long oneDay = 24L * 60L * 60L * 1000L;
          long noOfDays = days * oneDay;
          File file = listOfFiles[i];
          long diff = now - file.lastModified();
          if (diff > noOfDays) {
            file.delete();
            TestLogs.info("File : {} is Deleted", listOfFiles[i].getName());
          }
        } else if (listOfFiles[i].isDirectory()) {
          TestLogs.info("Directory {}", listOfFiles[i].getName());
        }
      }
    } else {
      TestLogs
          .warn("Files are not DELETED as folder is not found! and folder path is - {}", folder);
    }
  }
}
