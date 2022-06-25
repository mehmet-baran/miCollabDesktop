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
    public void incomingCallTest() throws Exception {
        try{
            LocalDateTime finalTime = LocalDateTime.now().plus(Duration.ofDays(testDurationInDays));
            Thread.sleep(25000);
            Actions action = new Actions(driver);
            Robot robot = new Robot();
            //Clicking on OK button
            clickOnCoordinate((int) (screenWidth / 2), 544);
            //Taking screenshot of before calls
            BufferedImage beforeCallImage = takeFullScreenshot("beforeCall");
            //Clicking on Settings button
            clickOnCoordinate(90, 345);
            Thread.sleep(1000);
            //Taking screenshot of show stopper
            BufferedImage showStopperImage = takeFullScreenshot("showStopper");
            //Clicking on Home button
            clickOnCoordinate(90, 116);
            while (LocalDateTime.now().isBefore(finalTime)) {
                BufferedImage screenshotImage = takeFullScreenshot("beforeCall");
                BufferedImage statusCheckImagePartial = takePartialScreenshot("statusCheck", statusXcoordinate, statusYcoordinate, 80, 20);
                enableTheStatus(statusCheckImagePartial);
                if (isSimilarSensitive(screenshotImage, beforeCallImage)) {
                    Thread.sleep(500);
                } else if (isSimilarSensitive(screenshotImage, showStopperImage)) {
                    break;
                } else {
                    LocalTime incomingCallTime = LocalTime.now();
                    boolean flag = true;
                    while (flag) {
                        acceptCall(acceptButtonXcoordinate, acceptButtonYcoordinate);
                        Thread.sleep(300);
                        BufferedImage afterCallImage = takeFullScreenshot("afterCall");
                        if (LocalTime.now().isAfter(incomingCallTime.plus(holdPeriod, ChronoUnit.MINUTES).minus(5, ChronoUnit.SECONDS)) && LocalTime.now().isBefore(incomingCallTime.plus(holdPeriod, ChronoUnit.MINUTES))) {
                            clickOnCoordinate(holdButtonXcoordinate, holdButtonYcoordinate);
                            Thread.sleep(3000);
                            clickOnCoordinate(holdButtonXcoordinate, holdButtonYcoordinate);
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
            clickOnCoordinate(90, 270);
            Thread.sleep(1000);
            takeFullScreenshot("callHistory");
            Thread.sleep(1000);
        }catch (Exception e){
            //quitMicollab();
            driver.quit();
            startMicollab();
            selectMicollabFromTaskbar();
            incomingCallTest();
        }
    }
}
