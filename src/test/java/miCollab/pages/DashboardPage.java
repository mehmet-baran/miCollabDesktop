package miCollab.pages;

import miCollab.utilities.ExcelUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DashboardPage extends CommonPageElements {
    public DashboardPage() {
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "(//div[.='Skip'])[2]")
    public WebElement skipButton;

    @FindBy(xpath = "(//div[@data-vn='inboundCall']//div[starts-with(@class,'cust-button-content')])[2]")
    public WebElement acceptCallButton;

    @FindBy(xpath = "//div[@class='flex-btn incall-end-btn tappable']")
    public WebElement endCallButton;

    @FindBy(css = "[data-vn=\"dnBarcode\"]")
    public WebElement barcode;

    @FindBy(css = "[data-hotkey-id='acceptCall']")
    public WebElement acceptCallHotKey;

    @FindBy(css = "[data-hotkey-id='declineCall']")
    public WebElement endCallHotkey;

    @FindBy(xpath = "//li[.='Settings']")
    public WebElement settingsButton;

    public void clickOnButton(String buttonText) {
        driver.findElement(By.xpath("//div[@class='button-content'][.='" + buttonText + "']")).click();
    }

    public void goToLeftMenuOption(String option) {
        driver.findElement(By.xpath("//li[starts-with(@class,'left-menu-li')]//span[.='" + option + "']")).click();
    }

    public void changeSettingsOf(String option) {
        driver.findElement(By.xpath("//div[@class='mtl-li-content'][.='" + option + "']/..")).click();
    }

    public WebElement getHotkey(String hotkeyText) {
        return driver.findElement(By.xpath("//div[@class='hotkey-title'][.='" + hotkeyText + "']"));
    }


    public boolean isIncomingCallAvailable() {
        List<WebElement> incomingCallList = driver.findElements(By.cssSelector("[class=\"cust-button-content no-label inbound-btn-answer\"]"));
        return incomingCallList.size() != 0;
    }

    public boolean isEndCallButtonAvailable() {
        List<WebElement> endCallList = driver.findElements(By.xpath("//div[@class='flex-btn incall-end-btn tappable']"));
        return endCallList.size() != 0;
    }

    public boolean doesCallContinue() {
        List<WebElement> controlElements = driver.findElements(By.xpath("(//div[@class=\"header-btn tappable\"])[6]"));
        return controlElements.size() == 0;
    }

    public boolean isControlElementAvailable() {
        List<WebElement> controlElements = driver.findElements(By.xpath("//div[.='MiTeam']"));
        return controlElements.size() != 0;
    }

    public void writeTestData() throws InterruptedException {
        ExcelUtil testData = new ExcelUtil("src/test/resources/MiCollabTestData.xlsx", "Sheet1");
        List<WebElement> elements = driver.findElements(By.xpath("//ul[@data-vn=\"allList\"]//span[starts-with(@class,'width-constrained-1-h')]"));
        List<String> callFromList = new ArrayList<>();
        List<String> dateAndTimeList = new ArrayList<>();
        List<String> durationList = new ArrayList<>();

        Thread.sleep(1000);
        for (int i = 0; i < elements.size(); i++) {
            callFromList.add(driver.findElements(By.xpath("//ul[@data-vn=\"allList\"]//span[starts-with(@class,'width-constrained-1-h')]")).get(i).getText());
            dateAndTimeList.add(driver.findElements(By.xpath("//ul[@data-vn=\"allList\"]//div[@class=\"timestamp width-constrained-1-h\"]")).get(i).getText());
            durationList.add(driver.findElements(By.xpath("//ul[@data-vn='allList']//div[@class=\"right-hand-upper\"]")).get(i).getText());
        }

        for (int i = 0; i < callFromList.size(); i++) {
            testData.setCellData(callFromList.get(i), "callFrom", i + 1);
            testData.setCellData("" + (i + 1), "SN", i + 1);
            if (durationList.get(i).isEmpty()) {
                testData.setCellData("Missed Call", "Duration", i + 1);
            } else {
                testData.setCellData(durationList.get(i).substring(1, 6), "Duration", i + 1);
            }
            if (dateAndTimeList.get(i).contains("Today")) {
                testData.setCellData(LocalDate.now().format(DateTimeFormatter.ofPattern("d MMM")) + " 2021", "Date", i + 1);
            } else if (dateAndTimeList.get(i).contains("Yesterday")) {
                testData.setCellData(LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("d MMM")) + " 2021", "Date", i + 1);
            } else {
                testData.setCellData(dateAndTimeList.get(i).substring(0, 5) + " 2021", "Date", i + 1);
            }
            testData.setCellData(dateAndTimeList.get(i).substring(dateAndTimeList.get(i).length() - 7), "Time", i + 1);
        }
    }

}
