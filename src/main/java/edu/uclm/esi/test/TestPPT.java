package edu.uclm.esi.test;

import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestPPT {
    private WebDriver driverPepa, driverBss1;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    public TestPPT() {
        //System.setProperty("webdriver.gecko.driver", "C:\\Users\\bss1\\Desktop\\tw\\geckodriver.exe");
        //System.setProperty("webdriver.chrome.driver", "C:\\Users\\bss1\\Desktop\\tw\\chromedriver.exe");
    	System.setProperty("webdriver.chrome.driver", "C:\\Users\\Angel\\Documents\\UNIVERSIDAD\\WEB\\chromedriver.exe");
    }

  @Before
  public void setUp() throws Exception {
    driverPepa= new ChromeDriver();
    driverBss1 = new ChromeDriver();
    baseUrl = "http://localhost:8080/";
    driverPepa.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    driverBss1.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testPPT() throws Exception {
    //Apertura del navegador  
    driverPepa.get("http://localhost:8080/");
    driverBss1.get("http://localhost:8080/");
    
    //Login
    driverPepa.findElement(By.id("email")).click();
    driverBss1.findElement(By.id("email")).click();
    driverPepa.findElement(By.id("email")).clear();
    driverBss1.findElement(By.id("email")).clear();
    driverPepa.findElement(By.id("email")).sendKeys("pepa");
    driverBss1.findElement(By.id("email")).sendKeys("bss1");
    driverPepa.findElement(By.id("pwd1")).click();
    driverBss1.findElement(By.id("pwd1")).click();
    driverPepa.findElement(By.id("pwd1")).clear();
    driverBss1.findElement(By.id("pwd1")).clear();
    driverPepa.findElement(By.id("pwd1")).sendKeys("pepa");
    driverBss1.findElement(By.id("pwd1")).sendKeys("bss1");
    driverPepa.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='¿Olvidaste la contraseña?'])[1]/following::button[1]")).click();
    driverBss1.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='¿Olvidaste la contraseña?'])[1]/following::button[1]")).click();
    
    //Juego PPT
    driverPepa.findElement(By.xpath("//button[@onclick=\"joinGame('Piedra, papel, tijera')\"]")).click();
    driverBss1.findElement(By.xpath("//button[@onclick=\"joinGame('Piedra, papel, tijera')\"]")).click();
    
    //Eleccion de jugada
    driverPepa.findElement(By.id("pulsarPiedra")).click();
    driverBss1.findElement(By.id("pulsarTijera")).click();
    Thread.sleep(5000);
    
  }

  @After
  public void tearDown() throws Exception {
    driverPepa.quit();
    driverBss1.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driverPepa.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driverPepa.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driverPepa.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}