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
            BufferedImage stopperImage = ImageIO.read(new File(System.getProperty("user.dir") + "\\src\\test\\resources\\images\\stopper.png"));
            BufferedImage beforeCallImage = ImageIO.read(new File(System.getProperty("user.dir") + "\\src\\test\\resources\\images\\beforeCall.png"));
            BufferedImage incomingCallImage = ImageIO.read(new File(System.getProperty("user.dir") + "\\src\\test\\resources\\images\\incomingCall.png"));
            while (LocalDateTime.now().isBefore(finalTime)) {
                short holdPeriod = 2;
                BufferedImage incomingCallCheck = takePartialScreenshot("incomingCallCheck", lineXcoordinate + 10, (int) (screenHeight / 2), 50, 50);
                BufferedImage stopperCheck = takePartialScreenshot("stopperCheck", 10, 50, 100, 70);
                BufferedImage statusCheck = takePartialScreenshot("statusCheck", (int) (screenWidth - 12), (int) (screenHeight - 70), 5, 22);
                enableTheStatus(statusCheck);
//                System.out.println("WAITING FOR THE CALL");
                if (isSimilarSensitive(incomingCallCheck, incomingCallImage)) {
                    Thread.sleep(200);
                    LocalTime incomingCallTime = LocalTime.now();
//                    System.out.println("CALL DETECTED");
                    boolean flag = true;
                    while (flag) {
                        Thread.sleep(200);
                        acceptCall(acceptButtonXcoordinate, acceptButtonYcoordinate);
//                        System.out.println("CALL ACCEPTED");
//                        System.out.println(LocalTime.now());
                        Thread.sleep(300);
                        BufferedImage afterCallImageCheck = takePartialScreenshot("afterCallImageCheck", lineXcoordinate + 10, (int) (screenHeight / 2), 50, 50);
//                        System.out.println("AFTERCALL IMAGE SCREENSHOT HAS BEEN TAKEN");
//                        System.out.println("holdPeriod in while loop= " + holdPeriod);
                        LocalTime holdPeriodStart = incomingCallTime.plus(holdPeriod, ChronoUnit.MINUTES).minus(5, ChronoUnit.SECONDS);
                        LocalTime holdPeriodEnd = incomingCallTime.plus(holdPeriod, ChronoUnit.MINUTES);
//                        System.out.println("holdPeriodStart = " + holdPeriodStart);
//                        System.out.println("holdPeriodEnd = " + holdPeriodEnd);
                        //if (LocalTime.now().isAfter(incomingCallTime.plus(holdPeriod, ChronoUnit.MINUTES).minus(5, ChronoUnit.SECONDS)) && LocalTime.now().isBefore(incomingCallTime.plus(holdPeriod, ChronoUnit.MINUTES))) {
                        if(LocalTime.now().isAfter(holdPeriodStart) && LocalTime.now().isBefore(holdPeriodEnd)){
                            clickOnCoordinate(holdButtonXcoordinate, holdButtonYcoordinate);
//                            System.out.println("HOLD BUTTON CLICKED");
                            Thread.sleep(3000);
                            clickOnCoordinate(holdButtonXcoordinate, holdButtonYcoordinate);
                            holdPeriod = (short) (holdPeriod + 2);
//                            System.out.println("holdPeriod in first if= " + holdPeriod);
                        }
                        if (!isSimilarSensitive(afterCallImageCheck, beforeCallImage)) {
//                            System.out.println("BEFORE CALL IMAGE IS NOT EQUAL TO AFTER CALL IMAGE");
//                            System.out.println("holdPeriod before returning to 2 = " + holdPeriod);
                            Thread.sleep(300);
                            //holdPeriod = 2;
                        } else {
//                            System.out.println("BEFORE CALL IMAGE IS EQUAL TO AFTER CALL IMAGE, SO WAITING FOR THE CALL");
//                            System.out.println("holdPeriod before returning flag to false = " + holdPeriod);
                            flag = false;
                        }
                    }
                } else if (isSimilarSensitive(stopperCheck, stopperImage)) {
                    break;
                } else {
//                    System.out.println("STILL WAITING FOR THE CALL");
//                    System.out.println("***********************************************");
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
