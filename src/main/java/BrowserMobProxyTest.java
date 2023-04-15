import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.proxy.CaptureType;

public class BrowserMobProxyTest {

    public static void main(String[] args) throws IOException {
        BrowserMobProxyServer proxyServer = new BrowserMobProxyServer();
        proxyServer.start();

        Proxy proxy = new Proxy().setHttpProxy(proxyServer.getClientBindAddress())
                .setSslProxy(proxyServer.getClientBindAddress())
                .setFtpProxy(proxyServer.getClientBindAddress())
                .setSocksProxy(proxyServer.getClientBindAddress())
                .setNoProxy("localhost")
                .setProxyType(Proxy.ProxyType.MANUAL);
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, proxy);
        capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        ChromeOptions options = new ChromeOptions();
        options.merge(capabilities);

        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().window().setSize(new Dimension(1024, 768));


        proxyServer.addHeader("Accept-Language", "ru");
        proxyServer.addHeader("Content-Language", "ru_RU");
        proxyServer.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT, CaptureType.RESPONSE_HEADERS);


        driver.get("https://www.youtube.com/");
        driver.findElement(By.id("search")).sendKeys("no war");
        driver.findElement(By.id("search-icon-legacy")).click();
        File screenshot = ((ChromeDriver) driver).getScreenshotAs(org.openqa.selenium.OutputType.FILE);
        System.out.println("Screenshot saved to " + screenshot.getAbsolutePath());


        proxyServer.abort();
        driver.quit();
    }

}
