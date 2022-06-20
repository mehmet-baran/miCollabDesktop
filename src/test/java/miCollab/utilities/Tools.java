package miCollab.utilities;

import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static miCollab.MiCollabDesktopApp.driver;

public class Tools {

    public byte testDurationInDays = 30;
    public double screenHeight = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    public double screenWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    public short lineXcoordinate = (short) (0.5515 * screenWidth);
    public short holdButtonXcoordinate = (short) (lineXcoordinate + (screenWidth - lineXcoordinate) / 2 + 90);
    public short holdButtonYcoordinate = (short) (screenHeight - 100);
    public short acceptButtonXcoordinate=(short)(lineXcoordinate + (screenWidth-lineXcoordinate)*0.685);
    public short acceptButtonYcoordinate = (short) ((screenHeight-40)*0.774);
    public int statusXcoordinate = (int) (screenWidth-200);
    public int statusYcoordinate = (int) (screenHeight-75);
    public short holdPeriod = 2;

    public static void selectMicollabFromTaskbar() throws InterruptedException, MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("app", "Root");
        WindowsDriver newDriver = new WindowsDriver(new URL("http://127.0.0.1:4723"), capabilities);
        Actions actions = new Actions(newDriver);
        actions.contextClick(newDriver.findElement(By.name("MiCollab - 1 running window"))).build().perform();
        Thread.sleep(2000);
        newDriver.findElementByAccessibilityId("Open").click();
        newDriver.quit();
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
}
