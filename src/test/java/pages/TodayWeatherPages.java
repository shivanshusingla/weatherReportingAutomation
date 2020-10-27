package pages;

import common.BrowserActions;
import common.TestMethods;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class TodayWeatherPages {


  TestMethods testMethods;
  BrowserActions browserActions;

  @FindBy(id = "LocationSearch_input")
  private WebElement locationSearchInput;

  @FindBy(xpath = "//div[contains(@class,\"CurrentConditions--primary\")]/span[@data-testid=\"TemperatureValue\"]")
  private WebElement currentTemperatureValue;

  public void enterCityNameInSearchField(String cityName) {
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    testMethods.enterData(locationSearchInput, cityName, "Enter city name :- " + cityName);
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    locationSearchInput.sendKeys(Keys.ENTER);
  }

  public String getCurrentTemperatureValue() {
    return testMethods.getText(currentTemperatureValue, "Current Temperature Value")
        .replaceAll("[^0-9]", "").trim();
  }

}
