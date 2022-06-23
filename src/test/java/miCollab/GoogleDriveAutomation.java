package miCollab;

import io.github.bonigarcia.wdm.WebDriverManager;
import miCollab.utilities.CommonSteps;
import miCollab.utilities.ConfigurationReader;
import org.jboss.aerogear.security.otp.Totp;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

import java.awt.*;
import java.time.LocalTime;

public class GoogleDriveAutomation extends CommonSteps {

    public static WebDriver driver;

    @Test
    public void googleDrive() throws InterruptedException, AWTException {
        https://drive.google.com/drive/u/0/my-drive

//        waitForVisibility((WebElement) driver.findElement(By.xpath("//a[.='Sign in']")), 10);
        Thread.sleep(2000);
//        driver.findElement(By.xpath("//a[.='Sign in']")).click();
        driver.findElement(By.id("identifierId")).sendKeys(ConfigurationReader.get("gmailAddress"));
        driver.findElement(By.xpath("//span[.='Next']")).click();
//        waitForVisibility((WebElement) driver.findElement(By.name("password")), 10);
        Thread.sleep(2000);
        driver.findElement(By.name("password")).sendKeys(ConfigurationReader.get("gmailPassword"));
        waitFor(2);
        driver.findElement(By.xpath("//span[.='Next']")).click();
        Totp totp = new Totp(ConfigurationReader.get("otpKey"));
        String twoFactorCode = totp.now();
        waitFor(2);
        driver.findElement(By.name("totpPin")).sendKeys(twoFactorCode);
        driver.findElement(By.xpath("(//span[@jsname='V67aGc'])[1]")).click();
        waitFor(3);
        driver.get("https://docs.google.com/spreadsheets/d/1Fuf_dI_Y-cCMdaXJSkbak_qxqihKnNsC");
        Actions actions = new Actions(driver);
//        actions.sendKeys(Keys.ARROW_DOWN).build().perform();
//        actions.sendKeys(Keys.ARROW_RIGHT).build().perform();
//        actions.sendKeys(Keys.ARROW_RIGHT).build().perform();
//        actions.sendKeys(Keys.ARROW_RIGHT).build().perform();
//        actions.sendKeys("DONE").build().perform();
        LocalTime beforeChangingData = LocalTime.now();
        System.out.println("beforeChangingData = " + beforeChangingData);
        driver.findElement(By.id("t-name-box")).clear();
        driver.findElement(By.id("t-name-box")).sendKeys("D2"+Keys.ENTER);
        waitFor(5000);
        LocalTime beforePrinting = LocalTime.now();
        System.out.println("beforePrinting = " + beforePrinting);
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("document.getElementById(\"t-formula-bar-input\").innerText;");
        String registrationStatus = driver.findElement(By.xpath("(//*[.='DONE'])[4]")).getText();
        System.out.println("registrationStatus = " + registrationStatus);
        LocalTime afterPrinting = LocalTime.now();
        System.out.println("afterPrinting = " + afterPrinting);
//        Robot robot = new Robot();
//        robot.mouseMove(365,400);

//        actions.doubleClick().build().perform();



    }

}
