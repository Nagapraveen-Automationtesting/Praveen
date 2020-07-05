package com.flipkart.config;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class BrowserOpen extends BaseClass {


        public WebDriver driver = null;
        public static boolean isMobile = false;
        public static long timeOut, driverWait, emailTimeOut;
        public static WebDriverWait wait;
        public static JavascriptExecutor js;
        private AppiumDriver<MobileElement> appium = null;
        private AndroidDriver<MobileElement> android = null;
        private RemoteWebDriver remote = null;
        protected Properties baseConfig;

        public WebDriver OpenBrowser(String testCategory, String browser, String webURL) throws IOException {
//        baseConfig = new Properties();
//        String configFile = "baseConfig.properties";
//        InputStream io = new FileInputStream(System.getProperty("user.dir")+"\\baseConfig.properties");
//        baseConfig.load(io);
        // WebDriver driver = null;
        AppiumDriver<MobileElement> appium = null;
        AndroidDriver<MobileElement> android = null;
        RemoteWebDriver remote = null;
        DesiredCapabilities capabilities = new DesiredCapabilities();
        if (testCategory.equalsIgnoreCase("web")) {
            switch (browser.toLowerCase()) {
                case "chrome":
//                    String downloadFilepath = GlobalTestData.DOWNLOAD_FOLDER_PATH;
                    HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
                    chromePrefs.put("profile.default_content_settings.popups", 0);
//                    chromePrefs.put("download.default_directory", downloadFilepath);
                    chromePrefs.put("profile.content_settings.exceptions.automatic_downloads.*.setting", 1);//New change

                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("--start-maximized");
                    options.setExperimentalOption("prefs", chromePrefs);
                    options.addArguments("--test-type");
                    options.addArguments("--disable-extensions");

                    DesiredCapabilities cap = DesiredCapabilities.chrome();
                    cap.setCapability(ChromeOptions.CAPABILITY, options);
                    cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                    System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"\\Drivers\\chromedriver_74.exe");
//				    WebDriverManager.chromedriver();
                    driver = new ChromeDriver(cap);
                    break;
                case "firefox":
                    System.setProperty("webdriver.gecko.driver", "Drivers/" + "geckodriver.exe");
                    driver = new FirefoxDriver();
                    break;
                case "internet explorer":
                    System.setProperty("webdriver.ie.driver", "Drivers/" + "IEDriverServer.exe");
                    capabilities = DesiredCapabilities.internetExplorer();
                    capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
                            true);
                    capabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
                    capabilities.setCapability("nativeEvents", true);
                    capabilities.setCapability("unexpectedAlertBehaviour", "accept");
                    capabilities.setCapability("ignoreProtectedModeSettings", true);
                    capabilities.setCapability("disable-popup-blocking", true);
                    capabilities.setCapability("enablePersistentHover", true);
                    capabilities.setCapability("ignoreZoomSetting", true);
                    capabilities.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, true);
                    capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                    capabilities.setCapability("ie.ensureCleanSession", true);
                    capabilities.setJavascriptEnabled(true);
                    driver = new InternetExplorerDriver(capabilities);
                    break;
                case "microsoft edge":
                    System.setProperty("webdriver.edge.driver", "Drivers/" + "MicrosoftWebDriver.exe");
                    capabilities = new DesiredCapabilities("MicrosoftEdge", "", Platform.WINDOWS);
                    capabilities.setJavascriptEnabled(true);
                    driver = new EdgeDriver(capabilities);
                    break;
                default:
                    throw new FrameworkException("Browser not configured.");
            }
            try {
                driver.get(webURL);
                driver.manage().window().maximize();
//				Report.infoTest("Browser Succesfully launched with URL : '" + webURL + "'");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }  else if (testCategory.toLowerCase().equals("mobile")) {
            if (browser.toLowerCase().equals("chrome")) {
                ChromeOptions option = new ChromeOptions();
                option.setExperimentalOption("w3c", false);
                DesiredCapabilities cap = new DesiredCapabilities();
                cap.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Device");
                cap.setCapability(MobileCapabilityType.BROWSER_NAME, "Chrome");
                cap.setCapability("unicodeKeyboard", true);
                cap.setCapability("resetKeyboard", true);
                cap.setCapability("noReset", true);
                cap.setCapability("appium:chromeOptions", ImmutableMap.of("w3c", false));
                // cap.setCapability(MobileCapabilityType.AUTO_WEBVIEW, true);
                cap.setCapability("chromedriverExecutable", System.getProperty("user.dir")+"\\Drivers\\chromedriver_74.exe");
                // System.setProperty("webdriver.chrome.driver","E:\\HybridFrameWork\\HRM\\resources\\chromedriver.exe");
                // WebDriverManager.chromedriver();
                try {
                    android = new AndroidDriver<>(new URL("http://0.0.0.0:4723/wd/hub"), cap);
                    this.android = android;
                    this.driver = android;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    driver.get(webURL);
                    driver.manage().deleteAllCookies();
                } catch (Exception e) {
                    driver.navigate().refresh();
                }
                isMobile = true;
            } else if (browser.toLowerCase().equals("native")) {
                DesiredCapabilities cap = new DesiredCapabilities();
                cap.setCapability("deviceName", "Pixel 210");
                cap.setCapability("udid", "emulator-5556");
                cap.setCapability("platformName", "Android");
                cap.setCapability("platformVersion", "10");	//required only for emulator
                cap.setCapability("appPackage", "com.flipkart.android");
                cap.setCapability("appActivity", "com.flipkart.android.activity.HomeFragmentHolderActivity");
                try {
                    android = new AndroidDriver<MobileElement>(new URL(baseConfig.getProperty("appiumURL")), cap);
                    this.appium = android;
                    this.driver = android;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            } else {
            }

            /*
             * try { driver = new RemoteWebDriver(new
             * URL("http://127.0.0.1:4723/wd/hub"), capabilities); } catch
             * (MalformedURLException e) { throw new FrameworkException(
             * "Not able to initialize driver.---" + e.getClass() + "---" +
             * e.getMessage()); }
             */
        }

        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
//        logg.info(browser+" successfully launched, and '"+webURL+"' opened. ");
        return driver;
    }

        /*
         * public static WebDriver mobileBrowserOpen(String testCategory, String
         * browser, String webURL){ // driver = new
         * AndroidDriver<MobileElement>(null); DesiredCapabilities capabilities =
         * new DesiredCapabilities(); if
         * (testCategory.toLowerCase().equals("mobile")) { //
         * Utilities.setConfigurations("mobile", browser);
         * if(browser.toLowerCase().equals("chrome")) { DesiredCapabilities cap=new
         * DesiredCapabilities();
         * cap.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Device");
         * cap.setCapability(MobileCapabilityType.BROWSER_NAME, "Chrome");
         * cap.setCapability("autoDismissAlerts", true);
         *
         * cap.setCapability("noReset", true); //
         * cap.setCapability(MobileCapabilityType.AUTO_WEBVIEW, true);
         * cap.setCapability("chromedriverExecutable",
         * "E:\\HybridFrameWork\\HRM\\chromedriver.exe"); //
         * System.setProperty("webdriver.chrome.driver",
         * "E:\\HybridFrameWork\\HRM\\resources\\chromedriver.exe"); //
         * WebDriverManager.chromedriver(); try { driver=new AndroidDriver<>(new
         * URL("http://0.0.0.0:4723/wd/hub"), cap);
         *
         * } catch (MalformedURLException e) { e.printStackTrace(); } isMobile =
         * true; }else {
         *
         * if (RemoteConfigurations.BROWSER != "") {
         * capabilities.setCapability(MobileCapabilityType.BROWSER_NAME,
         * RemoteConfigurations.BROWSER); } if (RemoteConfigurations.PLATFORM != "")
         * { capabilities.setCapability(MobileCapabilityType.PLATFORM,
         * RemoteConfigurations.PLATFORM);
         * capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME,
         * RemoteConfigurations.PLATFORM); }
         * capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, browser); if
         * (RemoteConfigurations.OS_VERSION != "") {
         * capabilities.setCapability(MobileCapabilityType.VERSION,
         * RemoteConfigurations.OS_VERSION); }
         * capabilities.setCapability("realMobile", "true");
         * capabilities.setCapability("nativeWebScreenshot", "true");
         * capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT,
         * 100000); isMobile = true; } try { driver = new RemoteWebDriver(new
         * URL("http://127.0.0.1:4723/wd/hub"), capabilities); } catch
         * (MalformedURLException e) { throw new FrameworkException(
         * "Not able to initialize driver.---" + e.getClass() + "---" +
         * e.getMessage()); } } try { driver.get(webURL);
         * driver.manage().deleteAllCookies(); } catch (Exception e) {
         * driver.navigate().refresh(); }
         *
         * return driver; }
         */

}
