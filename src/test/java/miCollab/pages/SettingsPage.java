package miCollab.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SettingsPage extends CommonPageElements{

    @FindBy(xpath = "//li[.='Self Deployment']")
    public WebElement selfDeploymentButton;

    @FindBy(css = "[data-vn=\"dnBarcode\"]")
    public WebElement barcode;
}
