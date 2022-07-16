package miCollab.utilities;

import io.appium.java_client.windows.WindowsDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static miCollab.MiCollabDesktopApp.driver;

public class Tools {

    public static byte testDurationInDays = 2;
    public static double screenHeight = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    public static double screenWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    public static short lineXcoordinate = (short) (0.5515 * screenWidth);
    public static short holdButtonXcoordinate = (short) (lineXcoordinate + (screenWidth - lineXcoordinate) / 2 + 90);
    public static short holdButtonYcoordinate = (short) (screenHeight - 100);
    public static short acceptButtonXcoordinate=(short)(lineXcoordinate + (screenWidth-lineXcoordinate)*0.685);
    public static short acceptButtonYcoordinate = (short) ((screenHeight-40)*0.774);
    public static BufferedImage stopperImage;
    public static BufferedImage beforeCallImage;
    public static BufferedImage incomingCallImage;
    public static BufferedImage ongoingCallImage;
    public static BufferedImage deleteHistoryImage;
    static {
        try {
            stopperImage = ImageIO.read(new File(System.getProperty("user.dir") + "\\src\\test\\resources\\images\\stopper.png"));
            beforeCallImage = ImageIO.read(new File(System.getProperty("user.dir") + "\\src\\test\\resources\\images\\beforeCall.png"));
            incomingCallImage = ImageIO.read(new File(System.getProperty("user.dir") + "\\src\\test\\resources\\images\\incomingCall.png"));
            ongoingCallImage = ImageIO.read(new File(System.getProperty("user.dir") + "\\src\\test\\resources\\images\\ongoingCall.png"));
            deleteHistoryImage = ImageIO.read(new File(System.getProperty("user.dir") + "\\src\\test\\resources\\images\\deleteHistoryTrigger.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void selectMicollabFromTaskbar() throws InterruptedException, MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("app", "Root");
        WindowsDriver newDriver = new WindowsDriver(new URL("http://127.0.0.1:4723"), capabilities);
        Actions actions = new Actions(newDriver);
        actions.contextClick(newDriver.findElement(By.name("MiCollab - 1 running window"))).build().perform();
        Thread.sleep(2000);
        newDriver.findElementByAccessibilityId("Open").click();
        newDriver.findElementByName("System").click();
        newDriver.findElementByName("Maximize").click();
        newDriver.quit();
    }

    public static boolean isSimilar(BufferedImage actual, BufferedImage expectedImage) {
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
//            System.out.println("Difference: " + percentage);
        }
        if (percentage > 5) {
            return false;
        } else return true;
    }


    public static boolean isSimilarSensitive(BufferedImage actual, BufferedImage expectedImage) {
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

    public static void maximizeScreen() throws InterruptedException {
        driver.findElement(By.name("System")).click();
        driver.findElement(By.name("Maximize")).click();
        Thread.sleep(3000);
    }

    public static void quitMicollab() throws MalformedURLException, InterruptedException {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("app", "Root");
        driver = new WindowsDriver(new URL("http://127.0.0.1:4723"), desiredCapabilities);
        Actions actions = new Actions(driver);
        actions.contextClick(driver.findElement(By.name("MiCollab - 1 running window"))).build().perform();
        Thread.sleep(1000);
        driver.findElement(By.name("Quit MiCollab")).click();
    }

    public static void startMicollab(){
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

    public static BufferedImage takePartialScreenshot(String imageName, int xStartPoint, int yStartPoint, int screenshotWidth, int screenshotHeight) throws IOException {
        File Screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        BufferedImage Image = ImageIO.read(Screenshot);
        BufferedImage ImagePartial = Image.getSubimage(xStartPoint,yStartPoint,screenshotWidth,screenshotHeight);
        ImageIO.write(ImagePartial, "png", Screenshot);
        FileUtils.copyFile(Screenshot, new File(System.getProperty("user.dir")+"\\src\\test\\resources\\images\\"+imageName+".png"));
        return ImagePartial;
    }

    public static BufferedImage takeFullScreenshot(String imageName) throws IOException {
        File Screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(Screenshot, new File(System.getProperty("user.dir") + "\\src\\test\\resources\\images\\"+imageName+".png"));
        return ImageIO.read(Screenshot);
    }
    public static void clickOnCoordinate(int xCoordinate, int yCoordinate) throws AWTException, InterruptedException {
        Robot robot = new Robot();
        robot.mouseMove(xCoordinate,yCoordinate);
        Thread.sleep(500);
        Actions actions = new Actions(driver);
        actions.click().build().perform();
    }
    public static void acceptCall(int xCoordinate, int yCoordinate) throws AWTException, InterruptedException {
        Robot robot = new Robot();
        Actions actions = new Actions(driver);
        robot.mouseMove(xCoordinate,yCoordinate);
        actions.click().build().perform();
    }

    public static void enableTheStatus(BufferedImage status) throws IOException, InterruptedException, AWTException {
        //Taking screenshot of the status
        Actions action = new Actions(driver);
        BufferedImage statusImage = ImageIO.read(new File(System.getProperty("user.dir")+"\\src\\test\\resources\\images\\status.png"));
        if (!isSimilarSensitive(statusImage, status)) {
            clickOnCoordinate((int) (screenWidth - 70), (int) (screenHeight - 60));
            action.release().build().perform();
            Thread.sleep(1000);
            clickOnCoordinate((int) (screenWidth - 50), (int) (screenHeight - 215));
            action.release().build().perform();
            Thread.sleep(1000);
            clickOnCoordinate((int) (screenWidth / 2), (int) (screenHeight / 2));
            action.release().build().perform();
        }
    }

    public static void deleteCallHistory() throws InterruptedException, AWTException {
        //Click on Call History button
        clickOnCoordinate(90, 270);
        clickOnCoordinate(90, 270);
        //Click on ellipsis
        clickOnCoordinate(1042, 90);
        //Click on Delete All button
        clickOnCoordinate(1000,160);
        //Click on Ok button
        clickOnCoordinate(1086,272);
        //Click on Home button
        clickOnCoordinate(90,115);
    }

}
