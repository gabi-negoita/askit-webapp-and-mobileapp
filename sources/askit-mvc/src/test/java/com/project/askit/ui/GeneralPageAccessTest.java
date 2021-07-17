package com.project.askit.ui;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

public class GeneralPageAccessTest {
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
        driver.findElement(By.name("email")).sendKeys("superuser@email.com");
        driver.findElement(By.name("password")).sendKeys("abcdef123");
        driver.findElement(By.name("submit")).click();
        driver.findElement(By.id("sidebar")).click();
        driver.findElement(By.cssSelector(".item:nth-child(3) > .middle:nth-child(1) > .header")).click();
        driver.findElement(By.id("sidebar")).click();
        driver.findElement(By.linkText("About")).click();
        driver.findElement(By.id("sidebar")).click();
        driver.findElement(By.cssSelector(".item:nth-child(7) .header")).click();
        driver.findElement(By.id("sidebar")).click();
        driver.findElement(By.cssSelector(".item:nth-child(8) b")).click();
        driver.findElement(By.id("sidebar")).click();
        driver.findElement(By.cssSelector(".item:nth-child(9) .header")).click();
        driver.findElement(By.id("sidebar")).click();
        driver.findElement(By.cssSelector(".item:nth-child(12) b")).click();
        driver.findElement(By.id("sidebar")).click();
        driver.findElement(By.cssSelector(".item:nth-child(13) b")).click();
        driver.findElement(By.id("sidebar")).click();
        driver.findElement(By.cssSelector(".item:nth-child(14) b")).click();
        driver.findElement(By.id("sidebar")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".item:nth-child(17) b"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        }
        driver.findElement(By.linkText("Create category")).click();
        driver.findElement(By.id("sidebar")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".item:nth-child(17) b"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        }
        driver.findElement(By.linkText("Categories")).click();
        driver.findElement(By.id("sidebar")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".item:nth-child(17) b"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        }
        driver.findElement(By.cssSelector(".item:nth-child(21) .header")).click();
        driver.findElement(By.id("sidebar")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".item:nth-child(17) b"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        }
        driver.findElement(By.cssSelector(".item:nth-child(22) .header")).click();
        driver.findElement(By.id("sidebar")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".item:nth-child(17) b"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        }
        driver.findElement(By.cssSelector(".item:nth-child(23) .header")).click();
        driver.findElement(By.id("sidebar")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".item:nth-child(17) b"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        }
        driver.findElement(By.cssSelector(".logout .header")).click();
        driver.findElement(By.cssSelector(".ok")).click();
        driver.close();
    }
}
