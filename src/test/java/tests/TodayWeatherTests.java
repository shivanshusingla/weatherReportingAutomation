package tests;

import apicommon.ApiConfigs;
import common.Constants;
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

  @Test(description = "Verify Current Weather", enabled = true)
  public void getNVerifyCurrentWeather() {
    todayWeatherPages.enterCityNameInSearchField(Constants.CITY_NAME);
    String currentTemperatureValueFromUI = todayWeatherPages.getCurrentTemperatureValue();
    String currentTemperatureValueFromApi = ApiConfigs
        .getResponse("http://api.openweathermap.org/data/2.5/weather",
            "q=Delhi&appid=5b91820a0537b1dbf1853e6ce440afba&units=metric", "main.temp");
    Assert.assertEquals(currentTemperatureValueFromUI, currentTemperatureValueFromApi,
        "Temperatures from api and ui are not equals");
  }

}
