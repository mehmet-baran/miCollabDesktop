package miCollab;

import io.appium.java_client.windows.WindowsDriver;
import miCollab.utilities.Tools;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class MiCollabDesktopApp extends Tools {

    public static WindowsDriver driver = null;

    @BeforeMethod
    public void setUp() throws MalformedURLException, InterruptedException {
        startMicollab();
        selectMicollabFromTaskbar();
    }

    @AfterMethod
    public void cleanUp() throws MalformedURLException, InterruptedException {
        quitMicollab();
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void incomingCallTest() throws InterruptedException, IOException, AWTException {

        LocalDateTime finalTime = LocalDateTime.now().plus(Duration.ofDays(testDurationInDays));
        Thread.sleep(25000);
        Actions action = new Actions(driver);
        Robot robot = new Robot();
        //Clicking on OK button
        robot.mouseMove((int) (screenWidth / 2), 544);
        Thread.sleep(1000);
        action.click().build().perform();
        //Taking screenshot of before calls
        File screenshotBeforeCall = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotBeforeCall, new File(System.getProperty("user.dir") + "\\Screenshot1.png"));
        BufferedImage beforeCallImage = ImageIO.read(screenshotBeforeCall);
        //Clicking on Settings button
        robot.mouseMove(90, 345);
        action.click().build().perform();
        Thread.sleep(1000);
        //Taking screenshot of show stopper
        File showStopper = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(showStopper, new File(System.getProperty("user.dir") + "\\showStopper.png"));
        BufferedImage showStopperImage = ImageIO.read(showStopper);
        //Clicking on Home button
        robot.mouseMove(90, 116);
        action.click().build().perform();
        //Taking screenshot of the status
        File statusScreenshot =((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        BufferedImage statusImage=ImageIO.read(statusScreenshot);
        BufferedImage statusImagePartial = statusImage.getSubimage(statusXcoordinate, statusYcoordinate, 200,35);
        ImageIO.write(statusImagePartial, "png",statusScreenshot);
        FileUtils.copyFile(statusScreenshot,new File(System.getProperty("user.dir")+"\\status.png"));


        while (LocalDateTime.now().isBefore(finalTime)) {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshot, new File(System.getProperty("user.dir") + "\\Screenshot1.png"));
            BufferedImage screenshotImage = ImageIO.read(screenshot);
            //Checking the status
            File statusCheckScreenshot =((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            BufferedImage statusCheckImage=ImageIO.read(statusCheckScreenshot);
            BufferedImage statusCheckImagePartial = statusCheckImage.getSubimage(statusXcoordinate, statusYcoordinate, 200,35);
            ImageIO.write(statusCheckImagePartial, "png",statusCheckScreenshot);
            FileUtils.copyFile(statusCheckScreenshot,new File(System.getProperty("user.dir")+"\\statusCheck.png"));

            if(!isSimilarSensitive(statusImagePartial, statusCheckImagePartial)){
                robot.mouseMove((int) (screenWidth-70), (int) (screenHeight-60));
                action.click().build().perform();
                action.release().build().perform();
                Thread.sleep(1000);
                robot.mouseMove((int)(screenWidth-50), (int)(screenHeight-215));
                action.click().build().perform();
                action.release().build().perform();
                Thread.sleep(1000);
                robot.mouseMove((int)(screenWidth/2), (int)(screenHeight/2));
                action.click().build().perform();
                action.release().build().perform();
            }

            if (isSimilarSensitive(screenshotImage, beforeCallImage)) {
                Thread.sleep(500);
            } else if (isSimilarSensitive(screenshotImage, showStopperImage)) {
                break;
            } else {
                LocalTime incomingCallTime = LocalTime.now();
                boolean flag = true;
                while (flag) {
                    robot.mouseMove(acceptButtonXcoordinate, acceptButtonYcoordinate);
                    action.click().perform();
                    Thread.sleep(300);
                    File screenshotAfterCall = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                    FileUtils.copyFile(screenshotAfterCall, new File(System.getProperty("user.dir") + "\\Screenshot2.png"));
                    BufferedImage afterCallImage = ImageIO.read(screenshotAfterCall);
                    if (LocalTime.now().isAfter(incomingCallTime.plus(holdPeriod, ChronoUnit.MINUTES).minus(5, ChronoUnit.SECONDS)) && LocalTime.now().isBefore(incomingCallTime.plus(holdPeriod, ChronoUnit.MINUTES))) {
                        Robot newRobot = new Robot();
                        newRobot.mouseMove(holdButtonXcoordinate, holdButtonYcoordinate);
                        action.click().build().perform();
                        Thread.sleep(3000);
                        action.click().build().perform();
                        holdPeriod = (short) (holdPeriod + 2);
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

        robot.mouseMove(90, 270);
        action.click().build().perform();
        Thread.sleep(1000);
        File callHistory = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(callHistory, new File(System.getProperty("user.dir") + "\\callHistory.png"));
        Thread.sleep(1000);
    }




}
