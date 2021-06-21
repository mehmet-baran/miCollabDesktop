package miCollab;

import io.appium.java_client.windows.WindowsDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

public class MiCollabDesktopApp {

    public static WindowsDriver driver = null;

    @BeforeMethod
    public void setUp() throws IOException {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("app", "C:\\Program Files (x86)\\Mitel\\MiCollab\\MiCollab.exe");
        desiredCapabilities.setCapability("platformName", "Windows");
        desiredCapabilities.setCapability("deviceName", "WindowsPC");
        try {
            driver = new WindowsDriver(new URL("http://127.0.0.1:4723"), desiredCapabilities);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @AfterMethod
    public void cleanUp() throws MalformedURLException, InterruptedException {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("app", "Root");
        driver = new WindowsDriver(new URL("http://127.0.0.1:4723"), desiredCapabilities);
        Actions actions = new Actions(driver);
        actions.contextClick(driver.findElement(By.name("MiCollab - 1 running window"))).perform();
        Thread.sleep(1000);
        driver.findElement(By.name("Quit MiCollab")).click();
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void incomingCallTest() throws InterruptedException, IOException {
        int testDurationInDays = 30;
        LocalDateTime finalTime = LocalDateTime.now().plus(Duration.ofDays(testDurationInDays));
        Thread.sleep(25000);

        driver.findElement(By.name("System")).click();
        driver.findElement(By.name("Maximize")).click();
        int numberOfCalls = 0;
        while (LocalDateTime.now().isBefore(finalTime)) {
            Actions action = new Actions(driver);
            action.sendKeys(Keys.ENTER).perform();
            Thread.sleep(1000);
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshot, new File(System.getProperty("user.dir") + "\\Screenshot1.png"));
            BufferedImage beforeCallImage = ImageIO.read(new File("src/test/resources/beforecall.png"));
            BufferedImage showStopperImage = ImageIO.read(new File("src/test/resources/showStopper.png"));
            BufferedImage actual = ImageIO.read(screenshot);

            if (isSimilar(actual, beforeCallImage)) {
                Thread.sleep(1000);

            } else if(isSimilar(actual,showStopperImage)){
                break;
            }
            else {
                numberOfCalls++;
                boolean flag=true;
                while (flag){
                    Actions actions2 = new Actions(driver);
                    actions2.sendKeys(Keys.ENTER).perform();
                    actions2.keyDown(Keys.CONTROL).sendKeys(Keys.NUMPAD1).perform();
                    actions2.release().sendKeys(Keys.CONTROL).perform();
                    Thread.sleep(1000);
                    File screenshotAfterCall = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                    FileUtils.copyFile(screenshotAfterCall, new File(System.getProperty("user.dir") + "\\Screenshot2.png"));
                    BufferedImage afterCallImage = ImageIO.read(screenshotAfterCall);
                    if(!isSimilar(afterCallImage,beforeCallImage)){
                        Thread.sleep(1000);
                    }
                    else {
                        flag=false;
                    }
                }
            }
        }

        System.out.println("numberOfCalls = " + numberOfCalls);
        Thread.sleep(1000);
    }

    public boolean isSimilar(BufferedImage actual, BufferedImage expectedImage) {
        double percentage = 1000;
        int w1 = actual.getWidth();
        int w2 = expectedImage.getWidth();
        int h1 = actual.getHeight();
        int h2 = expectedImage.getHeight();
        if ((w1 != w2) || (h1 != h2)) {
            System.out.println("Both images should have same dimensions");
        } else {
            long diff = 0;
            for (int j = 0; j < h1; j++) {
                for (int i = 0; i < w1; i++) {
                    //Getting the RGB values of a pixel
                    int pixel1 = actual.getRGB(i, j);
                    Color color1 = new Color(pixel1, true);
                    int r1 = color1.getRed();
                    int g1 = color1.getGreen();
                    int b1 = color1.getBlue();
                    int pixel2 = expectedImage.getRGB(i, j);
                    Color color2 = new Color(pixel2, true);
                    int r2 = color2.getRed();
                    int g2 = color2.getGreen();
                    int b2 = color2.getBlue();
                    //sum of differences of RGB values of the two images
                    long data = Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2);
                    diff = diff + data;
                }
            }
            double avg = diff / (w1 * h1 * 3);
            percentage = (avg / 255) * 100;
            //System.out.println("Difference: " + percentage);
        }
        if (percentage > 2) {
            return false;
        } else return true;
    }
}
