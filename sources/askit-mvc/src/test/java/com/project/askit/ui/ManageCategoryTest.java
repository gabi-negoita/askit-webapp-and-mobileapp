package com.project.askit.ui;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ManageCategoryTest {
    JavascriptExecutor js;
    private WebDriver driver;
    private Map<String, Object> vars;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/java/com/project/askit/ui/chrome/driver/chromedriver.exe");

        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        js = (JavascriptExecutor) driver;
        vars = new HashMap<>();
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    public String waitForWindow(int timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Set<String> whNow = driver.getWindowHandles();
        Set<String> whThen = (Set<String>) vars.get("window_handles");
        if (whNow.size() > whThen.size()) {
            whNow.removeAll(whThen);
        }
        return whNow.iterator().next();
    }

    @Test
    public void run() {
        driver.get("http://localhost:9091/login");
        driver.manage().window().setSize(new Dimension(904, 831));
        driver.findElement(By.name("email")).click();
        driver.findElement(By.name("email")).sendKeys("moderator@email.com");
        driver.findElement(By.name("password")).click();
        driver.findElement(By.name("password")).sendKeys("abcdef123");
        driver.findElement(By.name("submit")).click();
        driver.findElement(By.id("sidebar")).click();
        driver.findElement(By.linkText("Create category")).click();
        driver.findElement(By.id("title")).click();
        driver.findElement(By.id("title")).sendKeys("selenium test");
        driver.findElement(By.id("create-category")).click();
        driver.findElement(By.cssSelector(".green:nth-child(2)")).click();
        driver.findElement(By.id("sidebar")).click();
        driver.findElement(By.linkText("Categories")).click();
        driver.findElement(By.cssSelector(".title")).click();
        driver.findElement(By.name("search")).click();
        driver.findElement(By.name("search")).sendKeys("selenium test");
        driver.findElement(By.cssSelector(".eight > .ui")).click();
        vars.put("window_handles", driver.getWindowHandles());
        driver.findElement(By.linkText("Manage")).click();
        vars.put("win3260", waitForWindow(2000));
        vars.put("root", driver.getWindowHandle());
        driver.switchTo().window(vars.get("win3260").toString());
        driver.findElement(By.id("title")).click();
        driver.findElement(By.id("title")).sendKeys("selenium test modified");
        driver.findElement(By.id("save-changes")).click();
        driver.findElement(By.cssSelector(".actions > .ui > .green")).click();
        driver.findElement(By.id("delete")).click();
        driver.findElement(By.cssSelector("form .green")).click();
        driver.findElement(By.id("sidebar")).click();
        driver.findElement(By.cssSelector(".logout .header")).click();
        driver.findElement(By.cssSelector(".ok")).click();
        driver.close();
        driver.switchTo().window(vars.get("root").toString());
        driver.close();
    }
}