package miCollab;

import io.appium.java_client.windows.WindowsDriver;
import miCollab.utilities.CommonSteps;
import miCollab.utilities.ConfigurationReader;
import miCollab.utilities.Tools;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

public class GoogleDriveWinApp extends CommonSteps {
    public static void main(String[] args) throws InterruptedException, AWTException, MalformedURLException {
        WindowsDriver driver = null;
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("app", "C:\\Program Files\\Mozilla Firefox\\firefox.exe");
        desiredCapabilities.setCapability("platformName", "Windows");
        desiredCapabilities.setCapability("deviceName", "WindowsPC");
        try {
            driver = new WindowsDriver(new URL("http://127.0.0.1:4723"), desiredCapabilities);
            //driver = new WindowsDriver<WindowsElement>(new URL("http://127.0.0.1:4723"), desiredCapabilities);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        Robot robot = new Robot();
        Actions actions = new Actions(driver);
        waitFor(2);
        if(driver.findElementsByName("Search with Google or enter address").size()!=0){
            driver.findElementByName("Search with Google or enter address").sendKeys("https://drive.google.com/drive/u/0/my-drive");
        }else {
            robot.mouseMove(300,60);
            actions.click().build().perform();
            actions.sendKeys("https://drive.google.com/drive/u/0/my-drive").build().perform();
        }

        actions.sendKeys(Keys.ENTER).build().perform();
        Thread.sleep(3000);

        robot.mouseMove(400, 360);
        //TO-DO NEW USER LIST SHOULD BE LOCATED IN A CORRECT POSITION
        actions.doubleClick().build().perform();
        Thread.sleep(5000);
        for (int i=2;i<54;i++){
            actions.keyDown(Keys.CONTROL).sendKeys("j").keyUp(Keys.CONTROL).build().perform();
            Thread.sleep(500);
            actions.sendKeys("D"+i).sendKeys(Keys.ENTER).build().perform();
            waitFor(5);
            if(driver.findElementsByName("DONE").size()!=0){
                continue;
            }else {
                String username = "cleartmp"+(i-1);
                actions.sendKeys("IN PROGRESS").build().perform();
                registerMicollab(username);
                robot.mouseMove(200,200);
                actions.click().build().perform();
                waitFor(1);
                actions.keyDown(Keys.CONTROL).sendKeys("j").keyUp(Keys.CONTROL).build().perform();
                waitFor(1);
                actions.sendKeys("D"+i).sendKeys(Keys.ENTER).build().perform();
                waitFor(1);
                actions.keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).build().perform();
                actions.sendKeys("DONE").build().perform();
                actions.sendKeys(Keys.ENTER).build().perform();
                actions.keyDown(Keys.CONTROL).sendKeys("s").keyUp(Keys.CONTROL).build().perform();
                break;
            }


        }
        waitFor(5);
        driver.quit();
        LocalTime afterRegistration = LocalTime.now();
        System.out.println("afterRegistration = " + afterRegistration);


    }
    public static void registerMicollab(String username) throws InterruptedException, AWTException, MalformedURLException {

        WindowsDriver registerDriver = null;
        LocalTime registrationStart = LocalTime.now();
        System.out.println("registrationStart = " + registrationStart);
        Robot robot = new Robot();
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("app", "C:\\Program Files\\Mozilla Firefox\\firefox.exe");
        desiredCapabilities.setCapability("platformName", "Windows");
        desiredCapabilities.setCapability("deviceName", "WindowsPC");
        try {
            registerDriver = new WindowsDriver(new URL("http://127.0.0.1:4723"), desiredCapabilities);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        registerDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        waitFor(2);
        Actions registerActions = new Actions(registerDriver);

        if(registerDriver.findElementsByName("Search with Google or enter address").size()!=0){
            registerDriver.findElementByName("Search with Google or enter address").sendKeys("https://micollab-micc.qmul.ac.uk/ucs/micollab");
        }else {
            robot.mouseMove(300,60);
            registerActions.click().build().perform();
            registerActions.sendKeys("https://micollab-micc.qmul.ac.uk/ucs/micollab").build().perform();
        }
        registerActions.sendKeys(Keys.ENTER).build().perform();
        if(registerDriver.findElementsByName("Accept").size()!=0){
            registerDriver.findElementByName("Accept").click();
        }
        registerDriver.findElementByName("Login ID").clear();
        registerDriver.findElementByName("Login ID").sendKeys(username);
        registerDriver.findElementByName("Password").sendKeys(ConfigurationReader.get("miCollabPassword"));
        registerDriver.findElementByName("Login").click();
        waitFor(5);
        if(registerDriver.findElementsByName("Skip").size()!=0){
            registerDriver.findElementByName("Skip").click();
        }
        registerDriver.findElementByName("OK").click();
        registerDriver.findElementByName("Settings").click();
        registerDriver.findElementByName("Self Deployment").click();

        robot.mouseMove(Tools.lineXcoordinate+(int) (Tools.screenWidth-Tools.lineXcoordinate)/2,400);
        registerActions.click().build().perform();
        waitFor(2);
        registerDriver.findElementByName("Open Link").click();
        waitFor(40);

        if(Tools.screenWidth>1920){
            robot.mouseMove((int) (Tools.screenWidth/2),495);
            registerActions.click().build().perform();
            registerActions.sendKeys(ConfigurationReader.get("miCollabPassword"));
            robot.mouseMove((int) (Tools.screenWidth/2)+100,541);
            registerActions.click().build().perform();
        }else {
            robot.mouseMove((int) (Tools.screenWidth/2),440);
            registerActions.click().build().perform();
            registerActions.sendKeys(ConfigurationReader.get("miCollabPassword"));
            robot.mouseMove((int) (Tools.screenWidth/2)+100,490);
            registerActions.click().build().perform();
        }


        waitFor(30);
        robot.mouseMove(1468,156);
        registerActions.click().build().perform();
        waitFor(2);
        robot.mouseMove(960,545);
        registerActions.click().build().perform();
        Tools.quitMicollab();
        registerDriver.quit();

    }
}
