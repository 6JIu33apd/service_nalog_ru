import captcha.Captcha;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

@SuppressWarnings("BusyWait")
public class Main {

    static String bic = "0445252011";
    static String[] states = {"init", "error", "done"};
    static ChromeOptions options = new ChromeOptions(); //.addArguments("--headless");
    static WebDriver driver = new ChromeDriver(options);
    static WebDriverWait wait = new WebDriverWait(driver, 10);
    static String resultData;

    public static void getCaptchaImage() throws IOException, InterruptedException {
        driver.switchTo().frame("uniDialogFrame");
        Thread.sleep(1000);
        String logoSrc = driver.findElement(By.xpath("//*[@id=\"dialogContent\"]//img")).getAttribute("src");
        URL imageURL = new URL(logoSrc);
        BufferedImage saveImage = ImageIO.read(imageURL);
        ImageIO.write(saveImage, "jpg", new File("./main/img/captcha.jpg"));
        driver.switchTo().defaultContent();
    }

    public static void setBic() {

        for (char b : bic.toCharArray()) {
            String num = Character.toString(b);
            driver.findElement(By.xpath("//*[@id=\"bikAFN\"]")).sendKeys(num);
        }
    }

    public static void setDate() throws InterruptedException {

        do Thread.sleep(1000);
        while (Captcha.getDate() == null);
        for (char d : Captcha.getDate().toCharArray()) {
            String day = Character.toString(d);
            driver.findElement(By.xpath("//*[@id=\"dateAFN\"]")).sendKeys(day);
        }
    }

    public static void sendCaptcha() throws InterruptedException {

        driver.switchTo().frame("uniDialogFrame");
        do Thread.sleep(1000);
        while (Captcha.getCaptcha() == null);
        driver.findElement(By.xpath("//input[@id=\"captcha\"][@type=\"text\"]")).clear();
        for (char c : Captcha.getCaptcha().toCharArray()) {
            String ch = Character.toString(c);
            driver.findElement(By.xpath("//input[@id=\"captcha\"][@type=\"text\"]")).sendKeys(ch);
        }
        driver.findElement(By.xpath("//*[@id=\"btnOk\"]")).click();
        Captcha.setCaptcha(null);
        driver.switchTo().defaultContent();
    }

    public static void getHTML() {

        resultData = driver.findElement(By.xpath("//*[@id=\"pnlResultData\"]")).getAttribute("innerHTML");
    }

    public static void main(String[] args) {

        System.setProperty("webdriver.chrome.driver", "./main/bin/chromedriver");

        try {
            driver.get("https://service.nalog.ru/bi.do");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"btnSearch\"]")));
            driver.findElement(By.xpath("//*[@id='unirad_4']")).click();
            Captcha.guiDate();
            setBic();
            setDate();
            driver.findElement(By.xpath("//*[@id=\"btnSearch\"]")).click();
            Thread.sleep(1000);

            if (driver.findElement(By.xpath("//*[@id=\"pnlSearchResult\"]")).getAttribute("style").equals("display: block;")) {
                Captcha.guiCaptcha(states[2]);
            }
            else {
                getCaptchaImage();
                Captcha.guiCaptcha(states[0]);
                sendCaptcha();
                Thread.sleep(1000);

                if (driver.findElement(By.xpath("//*[@id=\"pnlSearchResult\"]")).getAttribute("style").equals("display: block;")) {
                    Captcha.guiCaptcha(states[2]);
                }
                else {
                    while (driver.findElement(By.xpath("//*[@id=\"pnlSearchResult\"]")).getAttribute("style").equals("display: none;")) {
                        getCaptchaImage();
                        Captcha.guiCaptcha(states[1]);
                        sendCaptcha();
                        Thread.sleep(1000);

                        if (driver.findElement(By.xpath("//*[@id=\"pnlSearchResult\"]")).getAttribute("style").equals("display: block;")) {
                            Captcha.guiCaptcha(states[2]);
                            break;
                        }
                    }
                }
            }
            getHTML();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}