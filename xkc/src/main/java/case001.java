import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class case001 {
    private AndroidDriver<WebElement> driver;
    SimpleDateFormat timer = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String datatime = timer.format(new Date());
    @BeforeSuite()
    public void setUp() throws Exception
    {
        File app = new File("/Users/Time/Desktop/git_code/AppiumCase/xkc/lib/release-2.0.0.5_19.apk");//appium设置apk路径
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("deviceName","127.0.0.1:62001");
        cap.setCapability("app", app.getAbsolutePath());
        cap.setCapability("appPackage", "com.getai.xiangkucun");
        cap.setCapability("noReset","True");
        cap.setCapability("appActivity", "com.getai.xiangkucun.view.main.SplashActivity");
        cap.setCapability("unicodeKeyboard","True");//支持中文输入
        cap.setCapability("resetKeyboard","True");
        driver = new AndroidDriver<WebElement>(new URL("http://127.0.0.1:4723/wd/hub"), cap);

    }
    public void hua() {
        Dimension size = driver.manage().window().getSize();
        int height = size.height;
        int width = size.width;
        new TouchAction(driver)
                .longPress(PointOption.point(width / 2, height - 100))
                .moveTo(PointOption.point(width / 2, 100)).release().perform();
    }

    @Test(testName="login")
    public void login() throws Exception {
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        List<WebElement> list = driver.findElements(By.id("com.getai.xiangkucun:id/smallLabel"));
        list.get(0).click();
        Thread.sleep(1000);
        List<WebElement> text =driver.findElements(By.className("android.widget.TextView"));
        text.get(2).click();
        driver.findElement(By.id("etMobile")).sendKeys("18516523004");
        driver.findElement(By.id("buttonGetValiCode")).click();
        driver.findElement(By.id("etValiCode")).sendKeys("777888");
        driver.findElement(By.id("buttonRegister")).click();
        //截图
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//      System.out.println(time.format(new Date()));// new Date()为获取当前系统时间
        String img_name=time.format(new Date());
        Thread.sleep(2000);
        File screen = driver.getScreenshotAs(OutputType.FILE);
        File screenFile = new File("../xkc/img/"+img_name+".png");
        try {
            FileUtils.copyFile(screen,screenFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        driver.findElement(By.id("layoutSearch")).click();
        driver.findElement(By.id("etSearch")).sendKeys("测试");
        Thread.sleep(2000);
        driver.pressKeyCode(AndroidKeyCode.ENTER);
    }
}