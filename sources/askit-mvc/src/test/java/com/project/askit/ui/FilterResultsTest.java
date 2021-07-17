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

public class FilterResultsTest {
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
        driver.findElement(By.id("sidebar")).click();
        driver.findElement(By.cssSelector(".item:nth-child(7) b")).click();
        driver.findElement(By.cssSelector(".title")).click();
        driver.findElement(By.cssSelector(".clearable")).click();
        driver.findElement(By.cssSelector(".transition > .item:nth-child(3)")).click();
        driver.findElement(By.cssSelector(".eight > .ui")).click();
        driver.findElement(By.cssSelector(".title")).click();
        driver.findElement(By.cssSelector(".text:nth-child(3)")).click();
        driver.findElement(By.cssSelector(".transition > .item:nth-child(3)")).click();
        driver.findElement(By.cssSelector(".eight > .ui")).click();
        driver.findElement(By.cssSelector(".title")).click();
        driver.findElement(By.name("search")).click();
        driver.findElement(By.name("search")).sendKeys("what");
        driver.findElement(By.cssSelector(".eight > .ui")).click();
        driver.close();
    }
}
