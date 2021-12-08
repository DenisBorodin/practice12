package ru.Litecart;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class CheckLog {
    WebDriver driver;
    WebDriverWait wait;
    @Test
    public void checkLog() {
        enter();
        searchAndClick(By.cssSelector("ul#box-apps-menu li#app-"), "Catalog");
        driver.findElement(By.xpath(".//table[@class='dataTable']//td[3]/a")).click();
        workWithPage(By.xpath(".//table[@class='dataTable']//td[3]/img"));
        driver.quit();
        driver = null;
    }

    private  void workWithPage(By locator) {
        int countItem = driver.findElements(locator).size();
        WebElement firstImg = driver.findElement(locator);

        String first = firstImg.findElement(By.xpath("../..")).getAttribute("rowIndex");
        int firstItem = Integer.parseInt(first) + 1;
        clearBrowserLog();
        consistentClickItem(firstItem, countItem);
    }

    private void consistentClickItem(int firstItem, int countItem) {
        WebElement currentItem;
        for (int i=firstItem; i<countItem + firstItem; i++) {
            currentItem = driver.findElement(By.xpath(".//table[@class='dataTable']//tr[" + i +"]/td[3]/a"));
            System.out.println("Item " + currentItem.getAttribute("text"));
            currentItem.click();
            implicitlyWaitOff();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1")));
            implicitlyWaitOn();
            driver.findElement(By.name("cancel")).click();
            printBrowserLog();
        }
    }

    private void printBrowserLog () {
        List<LogEntry> logList = driver.manage().logs().get("browser").getAll();
        if (logList.size()!= 0) {
            for (LogEntry l : logList)
                System.out.println(l);
        }
    }

    private void clearBrowserLog () {
        driver.manage().logs().get("browser").getAll();
    }

    public void enter() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);

        driver.manage().window().maximize();
        driver.get("http://localhost/litecart/admin/");
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("remember_me")).click();
        driver.findElement(By.name("login")).click();
    }

    protected void searchAndClick(By locator, String text) {
        List<WebElement> list = driver.findElements(locator);
        String name;
        for (WebElement we : list) {
            name = we.getText();
            if (name.equals(text)) {
                we.click();
                break;
            }
        }
    }

    protected void implicitlyWaitOn() {
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
    }

    protected void implicitlyWaitOff() {
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
    }
}
