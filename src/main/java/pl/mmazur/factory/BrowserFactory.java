package pl.mmazur.factory;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import pl.mmazur.utils.Properties;

public class BrowserFactory {
    private Playwright playwright;
    private Browser browser;

    public Playwright getPlaywright() {
        if (playwright == null) {
            playwright = Playwright.create();
        }
        return playwright;
    }

    public Browser getBrowser() {
        String browserName = Properties.getProperty("browser");

        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                .setHeadless(Boolean.parseBoolean(Properties.getProperty("browser.headless")))
                .setSlowMo(Integer.parseInt(Properties.getProperty("browser.slow.mo")));

        switch (browserName) {
            case "chromium" -> browser = getPlaywright().chromium().launch(launchOptions);
            case "firefox" -> browser = getPlaywright().firefox().launch(launchOptions);
            case "webkit" -> browser = getPlaywright().webkit().launch(launchOptions);
            case "chrome" -> browser = getPlaywright().chromium().launch(launchOptions.setChannel("chrome"));
            case "msedge" -> browser = getPlaywright().chromium().launch(launchOptions.setChannel("msedge"));
            default -> throw new IllegalArgumentException("Browser not supported: " + browserName);

        }

        return browser;

    }
}
