package tests;

import apicommon.ApiConfigs;
import common.Constants;
import common.FileDataReader;
import common.Listeners;
import java.util.List;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.TodayWeatherPages;

public class TodayWeatherTests extends TestBase {


  private TodayWeatherPages todayWeatherPages;

  @BeforeClass(description = "WebDriver Initialization")
  public void driverInitialization() {
    todayWeatherPages = PageFactory.initElements(driver, TodayWeatherPages.class);
  }

  @Test(description = "Verify Current Weather", enabled = true, retryAnalyzer = Listeners.class)
  public void getNVerifyCurrentWeather() {
    todayWeatherPages.enterCityNameInSearchField(Constants.CITY_NAME);
    double currentTemperatureValueFromUI = Double
        .parseDouble(todayWeatherPages.getCurrentTemperatureValue());
    List<String> listOfCities = (List<String>) FileDataReader.getJSONValue("City");
    long variance = (long) FileDataReader.getJSONValue("Variance");
    double currentTemperatureValueFromApi = Double.parseDouble(ApiConfigs
        .getResponse("http://api.openweathermap.org/data/2.5/weather",
            "q=" + listOfCities.get(0).trim()
                + "&appid=5b91820a0537b1dbf1853e6ce440afba&units=metric",
            "main.temp"));
    double currentTemperatureValueFromApiPercentage =
        currentTemperatureValueFromApi * ((double) variance / 100);
    double currentTemperatureValueDifference =
        currentTemperatureValueFromApi - currentTemperatureValueFromUI;
    double currentTemperatureValue = currentTemperatureValueFromApi > currentTemperatureValueFromUI
        ? currentTemperatureValueDifference : -1 * currentTemperatureValueDifference;
    Assert.assertTrue(currentTemperatureValue <= variance,
        "Temperatures from api and ui are not nearby variance value");
  }
}
