package util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DriverUtil {

    static public WebDriver getBrowserInstance(String browserName){

        if (browserName.equals("chrome")){
            return new ChromeDriver();
        }
        if (browserName.equals("edge")){
            return new EdgeDriver();
        }
        if (browserName.equals("firefox")){
            return new FirefoxDriver();
        }
        else
        {
            return null;
        }

    }
}
