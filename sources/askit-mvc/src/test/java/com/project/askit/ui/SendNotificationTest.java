package com.project.askit.ui;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class SendNotificationTest {
    JavascriptExecutor js;
    private WebDriver driver;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/java/com/project/askit/ui/chrome/driver/chromedriver.exe");

        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        js = (JavascriptExecutor) driver;
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void run() {
        driver.get("http://localhost:9091/logout");
        driver.manage().window().setSize(new Dimension(904, 831));
        driver.findElement(By.name("email")).click();
        driver.findElement(By.name("email")).sendKeys("admin@email.com");
        driver.findElement(By.name("password")).click();
        driver.findElement(By.name("password")).sendKeys("abcdef123");
        driver.findElement(By.name("submit")).click();
        driver.findElement(By.id("sidebar")).click();
        driver.findElement(By.cssSelector(".item:nth-child(12) b")).click();
        driver.findElement(By.name("title")).click();
        driver.findElement(By.name("title")).sendKeys("selenium notification");
        driver.findElement(By.cssSelector("label:nth-child(2)")).click();
        driver.findElement(By.name("content")).click();
        driver.findElement(By.name("content")).sendKeys("selenium notification");
        driver.findElement(By.id("submit-button")).click();
        driver.findElement(By.id("sidebar")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".item:nth-child(17) b"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        }
        driver.findElement(By.cssSelector(".logout")).click();
        driver.findElement(By.cssSelector(".ok")).click();
        driver.close();
    }
}
