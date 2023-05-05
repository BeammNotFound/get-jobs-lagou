import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Set;

/**
 * @author BeamStark
 * 拉钩网自动投递简历
 * @date 2023-05-04-15:21
 */
@Slf4j
public class ResumeSubmission {
    static Integer page = 1;
    static Integer maxPage = 10;
    static String baseUrl = "https://www.lagou.com/wn/jobs?px=new&pn=";
    static String loginUrl = "https://www.lagou.com";
    static ChromeDriver driver = new ChromeDriver();
    static WebDriverWait wait10s = new WebDriverWait(driver, 10000);

    public static void main(String[] args) {
        login();
        for (int i = page; i <= maxPage; i++) {
            domain(i);
        }
    }

    @SneakyThrows
    private static void domain(int index) {
        driver.get(baseUrl + index);
        wait10s.until(ExpectedConditions.presenceOfElementLocated(By.id("openWinPostion")));

        for (int i = 0; i < driver.findElements(By.id("openWinPostion")).size(); i++) {
            newTab(i);
        }
    }

    @SneakyThrows
    private static void newTab(int index) {
        String windowHandle = driver.getWindowHandle();
        driver.findElements(By.id("openWinPostion")).get(index).click();
        Set<String> windowHandles = driver.getWindowHandles();
        windowHandles.remove(windowHandle);
        String newWindowHandle = windowHandles.iterator().next();
        driver.switchTo().window(newWindowHandle);
        wait10s.until(ExpectedConditions.presenceOfElementLocated(By.className("resume-deliver")));
        if (!"已投递".equals(driver.findElements(By.className("resume-deliver")).get(0).getText())) {
            driver.findElements(By.className("resume-deliver")).get(0).click();
            log.info("投递 {}", driver.findElement(By.className("position-head-wrap-name")).getText());
        }
        Thread.sleep(1000);
        driver.close();
        driver.switchTo().window(windowHandle);
    }

    @SneakyThrows
    private static void login() {
        driver.get(loginUrl);
        wait10s.until(ExpectedConditions.presenceOfElementLocated(By.id("cboxClose")));
        driver.findElement(By.id("cboxClose")).click();
        driver.findElement(By.className("login")).click();
        wait10s.until(ExpectedConditions.presenceOfElementLocated(By.className("text-tips-info")));
        driver.findElements(By.className("text-tips-info")).get(1).click();
        log.info("等待扫码..倒计时10s");
        Thread.sleep(10000);
    }
}
