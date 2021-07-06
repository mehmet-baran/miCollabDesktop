package miCollab;

import io.appium.java_client.windows.WindowsDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import miCollab.pages.PageInitializer;
import miCollab.utilities.ConfigurationReader;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.Point;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import javax.tools.Tool;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static miCollab.pages.PageInitializer.*;
import static miCollab.utilities.CommonSteps.waitFor;

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
    public void incomingCallTest() throws InterruptedException, IOException, AWTException {
        int testDurationInDays = 30;
        double screenHeight = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        double screenWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int lineXcoordinate = (int) (0.5515 * screenWidth);
        int holdButtonXcoordinate = (int) (lineXcoordinate + (screenWidth - lineXcoordinate) / 2 + 90);
        int holdButtonYcoordinate = (int) (screenHeight - 100);
        int acceptButtonXcoordinate=(int)(lineXcoordinate + (screenWidth-lineXcoordinate)*0.685);
        int acceptButtonYcoordinate = (int) ((screenHeight-40)*0.774);

        LocalDateTime finalTime = LocalDateTime.now().plus(Duration.ofDays(testDurationInDays));
        Thread.sleep(23000);
        Actions action = new Actions(driver);
        driver.findElement(By.name("System")).click();
        driver.findElement(By.name("Maximize")).click();
        Thread.sleep(3000);
        Robot robot = new Robot();
        //Clicking on OK button
        robot.mouseMove((int) (screenWidth / 2), 544);
        Thread.sleep(1000);
        action.doubleClick().perform();
        //Taking screenshot of before calls
        File screenshotBeforeCall = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotBeforeCall, new File(System.getProperty("user.dir") + "\\Screenshot1.png"));
        BufferedImage beforeCallImage = ImageIO.read(screenshotBeforeCall);
        //Clicking on Settings button
        robot.mouseMove(90, 345);
        action.click().perform();
        Thread.sleep(1000);
        //Taking screenshot of show stopper
        File showStopper = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(showStopper, new File(System.getProperty("user.dir") + "\\showStopper.png"));
        BufferedImage showStopperImage = ImageIO.read(showStopper);
        //Clicking on Home button
        robot.mouseMove(90, 116);
        action.click().perform();

        int numberOfIncomingCalls = 0;

        while (LocalDateTime.now().isBefore(finalTime)) {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshot, new File(System.getProperty("user.dir") + "\\Screenshot1.png"));
            BufferedImage screenshotImage = ImageIO.read(screenshot);
            if (isSimilarSensitive(screenshotImage, beforeCallImage)) {
            } else if (isSimilarSensitive(screenshotImage, showStopperImage)) {
                break;
            } else {
                LocalTime incomingCallTime = LocalTime.now();
                int holdPeriod = 2;
                numberOfIncomingCalls++;
                boolean flag = true;
                while (flag) {
                    robot.mouseMove(acceptButtonXcoordinate, acceptButtonYcoordinate);
                    action.click().perform();
                    action.keyDown(Keys.CONTROL).sendKeys(Keys.NUMPAD1).perform();
                    action.release().sendKeys(Keys.CONTROL).perform();
                    Thread.sleep(300);
                    File screenshotAfterCall = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                    FileUtils.copyFile(screenshotAfterCall, new File(System.getProperty("user.dir") + "\\Screenshot2.png"));
                    BufferedImage afterCallImage = ImageIO.read(screenshotAfterCall);
                    if (LocalTime.now().isAfter(incomingCallTime.plus(holdPeriod, ChronoUnit.MINUTES).minus(5, ChronoUnit.SECONDS)) && LocalTime.now().isBefore(incomingCallTime.plus(holdPeriod, ChronoUnit.MINUTES))) {
                        Robot newRobot = new Robot();
                        newRobot.mouseMove(holdButtonXcoordinate, holdButtonYcoordinate);
                        action.click().perform();
                        Thread.sleep(3000);
                        action.click().perform();
                        holdPeriod = holdPeriod + 2;
                    }

                    if (!isSimilar(afterCallImage, beforeCallImage)) {
                        Thread.sleep(300);
                    } else if (isSimilar(afterCallImage, showStopperImage)) {
                        break;
                    } else {
                        flag = false;
                    }
                }
            }
        }

        System.out.println("Number of incoming calls during the test = " + numberOfIncomingCalls);
        robot.mouseMove(90, 270);
        action.click().perform();
        Thread.sleep(1000);
        File callHistory = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(callHistory, new File(System.getProperty("user.dir") + "\\callHistory.png"));
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

    public boolean isSimilarSensitive(BufferedImage actual, BufferedImage expectedImage) {
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
        if (percentage > 0.10) {
            return false;
        } else return true;
    }
}
