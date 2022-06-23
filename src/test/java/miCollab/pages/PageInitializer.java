package miCollab.pages;

import miCollab.utilities.Driver;

public class PageInitializer extends Driver {

    public static LicensePage licensePage;
    public static LoginPage loginPage;
    public static DashboardPage dashboardPage;
    public static SettingsPage settingsPage;



    public static void initialize() {

        licensePage = new LicensePage();
        loginPage = new LoginPage();
        dashboardPage = new DashboardPage();
        settingsPage = new SettingsPage();

    }
}
