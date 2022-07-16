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
import java.io.IOException;
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

            while (LocalDateTime.now().isBefore(finalTime)) {
                short holdPeriod = 50;
                BufferedImage incomingCallCheck = takePartialScreenshot("incomingCallCheck", lineXcoordinate + 10, (int) (screenHeight / 2), 50, 50);
                BufferedImage stopperCheck = takePartialScreenshot("stopperCheck", 10, 50, 100, 70);
                BufferedImage deleteHistoryCheck = takePartialScreenshot("deleteHistoryTriggerCheck",1550, 160, 80,20);
                BufferedImage statusCheck = takePartialScreenshot("statusCheck", (int) (screenWidth - 12), (int) (screenHeight - 70), 5, 22);
                enableTheStatus(statusCheck);
                if (isSimilarSensitive(incomingCallCheck, incomingCallImage)) {
                    Thread.sleep(200);
                    LocalTime incomingCallTime =LocalTime.now();
                    boolean flag = true;
                    while (flag) {

                        Thread.sleep(200);
                        acceptCall(acceptButtonXcoordinate, acceptButtonYcoordinate);
                        Thread.sleep(300);
                        BufferedImage afterCallImageCheck = takePartialScreenshot("afterCallImageCheck", lineXcoordinate + 10, (int) (screenHeight / 2), 50, 50);
                        BufferedImage ongoingCallImageCheck = takePartialScreenshot("ongoingCallCheck", lineXcoordinate + 50, 82, 50, 20);
                        LocalTime holdPeriodStart = incomingCallTime.plus(holdPeriod, ChronoUnit.SECONDS).minus(3, ChronoUnit.SECONDS);
                        LocalTime holdPeriodEnd = incomingCallTime.plus(holdPeriod, ChronoUnit.SECONDS);
                        if(LocalTime.now().isAfter(holdPeriodStart) && LocalTime.now().isBefore(holdPeriodEnd)){
                            clickOnCoordinate(holdButtonXcoordinate, holdButtonYcoordinate);
                            Thread.sleep(1000);
                            clickOnCoordinate(holdButtonXcoordinate, holdButtonYcoordinate);
                            holdPeriod = (short) (holdPeriod + 50);
                        }
                        if (isSimilarSensitive(ongoingCallImage, ongoingCallImageCheck)) {
                            Thread.sleep(500);
                        }
                        if(isSimilarSensitive(beforeCallImage,afterCallImageCheck)) {
                            flag = false;
                        }
                    }
                }
                else if (isSimilarSensitive(stopperCheck, stopperImage)) {
                    Thread.sleep(1000);
                    break;
                }
                else if(isSimilarSensitive(deleteHistoryCheck,deleteHistoryImage)){
                    deleteCallHistory();
                }
                else {
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
    public void setUp() throws IOException, InterruptedException {
        startMicollab();
        selectMicollabFromTaskbar();
        Thread.sleep(1000);
        BufferedImage startScreen = ImageIO.read(new File(System.getProperty("user.dir") + "\\src\\test\\resources\\images\\startScreen.png"));
        BufferedImage startScreenCheck = takePartialScreenshot("startScreenCheck", 100, 30, 30, 30);

        if(isSimilarSensitive(startScreen, startScreenCheck)){
            Thread.sleep(24000);
        }
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
