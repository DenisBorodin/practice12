package ru.Litecart;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CheckNewWindow {
    WebDriver driver;
    WebDriverWait wait;

    @Test
    public void checkLog() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);

        driver.manage().window().maximize();

        driver.get("http://localhost/litecart/admin/");
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("remember_me")).click();
        driver.findElement(By.name("login")).click();
        driver.get("http://localhost/litecart/admin/?app=countries&doc=countries");

        driver.findElement(By.cssSelector("[href=\"http://localhost/litecart/admin/?app=countries&doc=edit_country&country_code=AF\"]")).click();
        List<WebElement> faLink = driver.findElements(By.cssSelector("#content i.fa-external-link"));

        String originalWindow = driver.getWindowHandle();
        Set<String> oldWindow = driver.getWindowHandles();
        for (WebElement element : faLink) {
            element.click();

            String newWindow = wait.until(new ExpectedCondition<String>() {
                @Override
                public String apply(WebDriver input) {

                    Set<String> newWindows = input.getWindowHandles();
                    newWindows.removeAll(oldWindow);
                    return newWindows.size() > 0 ? newWindows.iterator().next() : null;

                }
            });

            driver.switchTo().window(newWindow);
            wait.until(F -> {
                return driver.getTitle().length() > 0;
            });
            driver.close();
            driver.switchTo().window(originalWindow);
        }
        driver.quit();
        driver = null;
    }
}
