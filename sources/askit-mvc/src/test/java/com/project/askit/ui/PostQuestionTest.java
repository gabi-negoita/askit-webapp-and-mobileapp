package com.project.askit.ui;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

public class PostQuestionTest {
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
        driver.findElement(By.name("email")).sendKeys("basic@email.com");
        driver.findElement(By.name("password")).sendKeys("abcdef123");
        driver.findElement(By.name("submit")).click();
        driver.findElement(By.id("sidebar")).click();
        driver.findElement(By.linkText("Ask question")).click();
        driver.findElement(By.id("subject")).click();
        driver.findElement(By.id("subject")).sendKeys("selenium test");
        driver.findElement(By.cssSelector(".dropdown")).click();
        driver.findElement(By.cssSelector(".transition > .item:nth-child(1)")).click();
        driver.switchTo().frame(0);
        driver.findElement(By.cssSelector("html")).click();
        {
            WebElement element = driver.findElement(By.id("tinymce"));
            js.executeScript("if(arguments[0].contentEditable === 'true') {arguments[0].innerText = '<p>selenium test</p>'}", element);
        }
        driver.switchTo().defaultContent();
        driver.findElement(By.id("submit-button")).click();
        driver.findElement(By.id("subject")).click();
        driver.findElement(By.id("subject")).sendKeys("selenium test #123456789");
        driver.switchTo().frame(0);
        driver.findElement(By.cssSelector("p")).click();
        driver.switchTo().defaultContent();
        driver.findElement(By.id("subject")).click();
        driver.switchTo().frame(0);
        driver.findElement(By.cssSelector("p")).click();
        {
            WebElement element = driver.findElement(By.id("tinymce"));
            js.executeScript("if(arguments[0].contentEditable === 'true') {arguments[0].innerText = '<p>selenium test #123456789</p><p>selenium test #123456789<br data-mce-bogus=\"1\"></p><p>selenium test #123456789<br data-mce-bogus=\"1\"></p>'}", element);
        }
        driver.switchTo().defaultContent();
        driver.findElement(By.id("submit-button")).click();
        driver.findElement(By.id("sidebar")).click();
        driver.findElement(By.cssSelector(".logout .header")).click();
        driver.findElement(By.cssSelector(".ok")).click();
        driver.close();
    }
}
