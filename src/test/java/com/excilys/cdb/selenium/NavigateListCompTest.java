package com.excilys.cdb.selenium;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

public class NavigateListCompTest {
	private WebDriver driver;
	private Actions actions;

	public NavigateListCompTest() {
		System.setProperty("webdriver.chrome.driver", "/opt/WebDriver/bin/chromedriver");
	}

	@Before
	public void setUp() {
		driver = new ChromeDriver();
		actions = new Actions(driver);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.get("http://localhost:8080/web-cdb/");
	}

	@After
	public void tearDown() {
		driver.quit();
	}

	@Test
	public void navigateList() throws InterruptedException {
		actions.moveToElement(driver.findElement(By.name("button50"))).perform();
		actions.pause(1000).click().perform();
		for (int i = 0; i < 10; i++) {
			actions.moveToElement(driver.findElement(By.id("nextPage"))).perform();
			actions.pause(1000).click().perform();
		}
		actions.moveToElement(driver.findElement(By.name("button100"))).perform();
		actions.pause(1000).click().perform();
		for (int i = 0; i < 4; i++) {
			actions.moveToElement(driver.findElement(By.id("previousPage"))).perform();
			actions.pause(1000).click().perform();
		}
		actions.moveToElement(driver.findElement(By.name("button10"))).perform();
		actions.pause(1000).click().perform();
		actions.pause(1000);
	}
}
