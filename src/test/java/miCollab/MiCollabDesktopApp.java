package miCollab;

import io.appium.java_client.windows.WindowsDriver;
import miCollab.utilities.Tools;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class MiCollabDesktopApp extends Tools {

    public static WindowsDriver driver = null;
    @Test
    public void incomingCallTest() {
        try {
            LocalDateTime finalTime = LocalDateTime.now().plus(Duration.ofDays(testDurationInDays));
            //Clicking on OK button
            clickOnCoordinate((int) (screenWidth / 2), 544);
            BufferedImage stopperImage = ImageIO.read(new File(System.getProperty("user.dir") + "\\src\\test\\resources\\images\\stopper.png"));
            BufferedImage beforeCallImage = ImageIO.read(new File(System.getProperty("user.dir") + "\\src\\test\\resources\\images\\beforeCall.png"));
            BufferedImage incomingCallImage = ImageIO.read(new File(System.getProperty("user.dir") + "\\src\\test\\resources\\images\\incomingCall.png"));
            while (LocalDateTime.now().isBefore(finalTime)) {
                BufferedImage incomingCallCheck = takePartialScreenshot("incomingCallCheck", lineXcoordinate + 10, (int) (screenHeight / 2), 50, 50);
                BufferedImage stopperCheck = takePartialScreenshot("stopperCheck", 10, 50, 100, 70);
                BufferedImage statusCheck = takePartialScreenshot("statusCheck", (int) (screenWidth - 12), (int) (screenHeight - 70), 5, 22);
                enableTheStatus(statusCheck);
                if (isSimilarSensitive(incomingCallCheck, incomingCallImage)) {
                    Thread.sleep(200);
                    LocalTime incomingCallTime = LocalTime.now();
                    boolean flag = true;
                    while (flag) {
                        Thread.sleep(200);
                        acceptCall(acceptButtonXcoordinate, acceptButtonYcoordinate);
                        Thread.sleep(300);
                        BufferedImage afterCallImageCheck = takePartialScreenshot("afterCallImageCheck", lineXcoordinate + 10, (int) (screenHeight / 2), 50, 50);

                        if (LocalTime.now().isAfter(incomingCallTime.plus(holdPeriod, ChronoUnit.MINUTES).minus(5, ChronoUnit.SECONDS)) && LocalTime.now().isBefore(incomingCallTime.plus(holdPeriod, ChronoUnit.MINUTES))) {
                            clickOnCoordinate(holdButtonXcoordinate, holdButtonYcoordinate);
                            Thread.sleep(3000);
                            clickOnCoordinate(holdButtonXcoordinate, holdButtonYcoordinate);
                            holdPeriod = (short) (holdPeriod + 2);
                        }
                        if (!isSimilarSensitive(afterCallImageCheck, beforeCallImage)) {
                            Thread.sleep(300);
                        } else {
                            flag = false;
                        }
                    }
                } else if (isSimilarSensitive(stopperCheck, stopperImage)) {
                    break;
                } else {
                    Thread.sleep(200);
                }
            }
            clickOnCoordinate(90, 270);
            Thread.sleep(1000);
            takeFullScreenshot("callHistory");
            Thread.sleep(1000);
        } catch (Exception e) {
            incomingCallTest();
        }
    }

    @BeforeMethod
    public void setUp() throws MalformedURLException, InterruptedException {
        startMicollab();
        selectMicollabFromTaskbar();
        Thread.sleep(25000);
    }

    @AfterMethod
    public void cleanUp() throws MalformedURLException, InterruptedException {
        quitMicollab();
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

}
