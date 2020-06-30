package com.excilys.cdb.selenium;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class NavigateListCompTest {
	private WebDriver driver = new ChromeDriver();

	public NavigateListCompTest() {
		System.setProperty("webdriver.chrome.driver", "/opt/WebDriver/bin/");
	}

	@Before
	public void setUp() {
		driver.get("http://localhost:8080/web-cdb/");
	}

	@AfterClass
	public void tearDown() {
		driver.quit();
	}

	@Test
	public void navigateList() {
		driver.findElement(By.name("button50")).click();
		for (int i = 0; i < 10; i++) {
			driver.findElement(By.id("nextPage")).click();
		}
		driver.findElement(By.name("button100")).click();
		for (int i = 0; i < 4; i++) {
			driver.findElement(By.id("previousPage")).click();
		}
		driver.findElement(By.name("button10")).click();
	}
}
