package demo;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;

public class YoutubeWrapper {

    ChromeDriver driver;
    WebDriverWait wait;

    public YoutubeWrapper(ChromeDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public WebElement findElement(By by) {
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (TimeoutException | NoSuchElementException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<WebElement> findElements(By by) {
        try {
            return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
        } catch (TimeoutException | NoSuchElementException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void clickElement(By by) {
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(by));
            element.click();
        } catch (TimeoutException | NoSuchElementException | StaleElementReferenceException e) {
            e.printStackTrace();
        }
    }

    public void sendKeys(By by, String keys) {
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(by));
            element.sendKeys(keys);
        } catch (TimeoutException | NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    public void sendKeysEnter(By by, String keys) {
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(by));
            element.sendKeys(keys);
            element.sendKeys(Keys.RETURN);
        } catch (TimeoutException | NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    public void dismissAlert() {
        try {
            driver.switchTo().alert().dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getElementText(By by) {
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(by));
            return element.getText();
        } catch (TimeoutException | NoSuchElementException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void scrollToRight(By by) {
        try {
            WebElement element = findElement(by);    
            while (element != null && element.isDisplayed()) {
                clickElement(by);
                element = findElement(by);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void scrollToBottom() {
        try {
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
