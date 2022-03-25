package vietnb1.com.fs;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import javafx.scene.control.Button;

public class AutoSimulate extends Thread {

    public static WebDriver driver;

    private String username;

    private String password;

    private String path;

    public static boolean running;

    private int count = 0;
    
    private Button btnRun;

    public AutoSimulate(String username, String password, String path,Button btnRun) {
        super();
        this.username = username;
        this.password = password;
        this.path = path;
        running = true;
        this.btnRun = btnRun;
    }

    public void run() {
        String driverLocation = null;
        driverLocation = System.getProperty("user.dir") + "//chromedriver.exe";

        System.setProperty("webdriver.chrome.driver", driverLocation);
        driver = new ChromeDriver();

        driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.manage().window().maximize();

        // launching the specified URL
        driver.get("https://platform.worldquantbrain.com/simulate");

        // List<String> authen = readFile("authen.txt");

        driver.findElement(By.id("email")).sendKeys(username);

        driver.findElement(By.id("password")).sendKeys(password);

        driver.findElement(By.xpath("//button/span[contains(., \"Accept\")]")).click();

        JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript("arguments[0].click();", driver.findElement(By.xpath("//button[@type='submit']")));

        try {
            Thread.sleep(2000L);
            js.executeScript("arguments[0].click();",
                    driver.findElement(By.xpath("//div[@class='introjs-tooltipbuttons']/a[contains(., \"Skip\")]")));

            Thread.sleep(1000L);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Clipboard clipboard = toolkit.getSystemClipboard();

        List<String> listCode = readFile(path);

        Actions action = new Actions(driver);

        for (int i = 0; i < listCode.size(); i++) {
            if (!running) {
                break;
            }
            StringSelection strSel = new StringSelection(listCode.get(i));
            clipboard.setContents(strSel, null);
            try {
                WebElement elementViewLine = driver.findElement(By.xpath("//div[@class='view-line']"));
                elementViewLine.click();
                
                action.keyDown(Keys.CONTROL);
                action.sendKeys("a");
                action.keyUp(Keys.CONTROL);
                action.build().perform();
                
                action.sendKeys(Keys.DELETE).build().perform();
                

                action.keyDown(Keys.CONTROL);
                action.sendKeys("v");
                action.keyUp(Keys.CONTROL);
                action.build().perform();

//                Screen s = new Screen();
//                Pattern pattern = new Pattern(System.getProperty("user.dir") + "//simulate.PNG");
//                pattern.similar(0.7f);
//                s.click(pattern);

                js.executeScript("arguments[0].click();",
                        driver.findElement(By.className("editor-simulate-button-text--is-code")));

                Thread.sleep(2000L);
                boolean flag = true;
                while (flag) {
                    try {
                        driver.findElement(By.xpath("//*[@class='bar']"));
                        Thread.sleep(1000L);
                    } catch (Exception e) {
                        flag = false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            count++;
        }
        
        btnRun.setDisable(false);
        FileWriter writer;
        try {
            writer = new FileWriter(path);
            for (int i = count; i < listCode.size(); i++) {
                writer.write(listCode.get(i) + System.lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (driver != null) {
                driver.close();
            }
        } catch (Exception e) {
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("close driver.");
        if (driver != null)
            driver.close();
    }

    private static List<String> readFile(String fileName) {
        List<String> list = new ArrayList<>();
        try {
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                list.add(myReader.nextLine());
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean isRunning() {
        return running;
    }

}
