package demo;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class TestCases {
    ChromeDriver driver;
    FlipkartWrapper wrapper;

    @BeforeSuite(alwaysRun = true)
    public void initialize_driver() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wrapper = new FlipkartWrapper(driver);
    }

    @AfterSuite(alwaysRun = true)
    public void close_and_quit_driver() {
        driver.close();
        driver.quit();
    }
    
    @Test(priority = 0, description = "Get the count of Washing Machine has rating less than 4")
    public void Washing_Machine() throws InterruptedException {
        System.out.println("Start Test case: Get the count of Washing Machine has rating less than 4");
        driver.get("http://www.flipkart.com");
        Thread.sleep(2000);
    
         wrapper.sendKeysEnter(By.xpath("//input[@placeholder='Search for Products, Brands and More']"), "Washing Machine");
        System.out.println("Step 01: Search Washing Machine - Completed");

        wrapper.clickElement(By.xpath("//div[@class='zg-M3Z' and text()='Popularity']"));
        System.out.println("Step 02: Sort by Popularity - Completed");
        
        List<WebElement> productsRatings = wrapper.findElements(By.xpath("//div[@class='_75nlfW']//div[@class='XQDdHH']"));
        System.out.println("Step 03: Get rating of all products - Completed");

        double ratingCriteria = 4.0;
        int count = 0;

        for (int i = 0; i < productsRatings.size(); i++) {
            try {
                WebElement productRating = productsRatings.get(i);
                String rating = productRating.getText();
                float ratingValue = Float.parseFloat(rating);
                if(ratingCriteria >= ratingValue){
                    count++;
                }
            } catch (StaleElementReferenceException e) {
                productsRatings = wrapper.findElements(By.xpath("//div[@class='_75nlfW']//div[@class='XQDdHH']"));
                i--;
            }
            Thread.sleep(1000);
        }
        System.out.println("Result: Count of all products who has less than 4.0 rating is "+count);
        System.out.println("End Test case: Get the count of Washing Machine has rating less than 4");
    }

    @Test(priority = 1, description = "Print the Titles and discount % of items with more than 17% discount")
    public void iPhone() throws InterruptedException {
        System.out.println("Start Test case: Print the Titles and discount % of items with more than 17% discount");
        driver.get("http://www.flipkart.com");
        Thread.sleep(2000);
    
         wrapper.sendKeysEnter(By.xpath("//input[@placeholder='Search for Products, Brands and More']"), "iPhone");
        System.out.println("Step 01: Search iPhone - Completed");
        
        List<WebElement> products = wrapper.findElements(By.xpath("//div[@class='_75nlfW']"));
        System.out.println("Step 02: Get all products in the list - Completed");

        int discountCriteria = 17;

        for (int i = 0; i < products.size(); i++) {
            try {
                WebElement product = products.get(i);
                String title = product.findElement(By.xpath(".//div[@class='KzDlHZ']")).getText();
                String discountStr = product.findElement(By.xpath(".//div[@class='UkUFwK']/span")).getText();
                String discountStrTrim = discountStr.replace("% off", "").trim();
                int discount = Integer.parseInt(discountStrTrim);
                if(discountCriteria <= discount){
                    System.out.println(title+" : "+discount);
                }
            } catch (StaleElementReferenceException e) {
                products = wrapper.findElements(By.xpath("//div[@class='_75nlfW']"));
                i--;
            }
            Thread.sleep(1000);
        }
        System.out.println("End Test case: Print the Titles and discount % of items with more than 17% discount");
    }
    
    @Test(priority = 2, description = "Print the Title and image URL of the 5 items with highest number of reviews")
    public void Coffee_Mug() throws InterruptedException {
        System.out.println("Start Test case: Print the Title and image URL of the 5 items with highest number of reviews");
        driver.get("http://www.flipkart.com");
        Thread.sleep(2000);
    
         wrapper.sendKeysEnter(By.xpath("//input[@placeholder='Search for Products, Brands and More']"), "Coffee Mug");
        System.out.println("Step 01: Search Coffee Mug - Completed");

        wrapper.clickElement(By.xpath("//div[@title='4★ & above']//div[@class='XqNaEv']"));
        System.out.println("Step 02: Filter 4* and above - Completed");
        
        List<WebElement> products = wrapper.findElements(By.xpath("//div[@class='slAVV4']"));
        System.out.println("Step 03: Get all products in the list - Completed");

        HashMap<String, Integer> titleAndReview = new HashMap<String, Integer>();
        HashMap<String, String> titleAndURL = new HashMap<String, String>();

        for (int i = 0; i < products.size(); i++) {
            try {
                WebElement product = products.get(i);
                String title = product.findElement(By.xpath(".//a[@class='wjcEIp']")).getText();
                WebElement imgEle = product.findElement(By.xpath(".//img"));
                String imgURL = imgEle.getAttribute("src");
                String reviewStr = product.findElement(By.xpath(".//span[@class='Wphh3N']")).getText();
                reviewStr = reviewStr.replace("(", "").replace(")", "").replace(",", "").trim();
                int review = Integer.parseInt(reviewStr);                
                titleAndReview.putIfAbsent(title, review);
                titleAndURL.putIfAbsent(title, imgURL);

            } catch (StaleElementReferenceException e) {
                products = wrapper.findElements(By.xpath("//div[@class='_75nlfW']"));
                i--;
            }
            Thread.sleep(1000);
        }
        List<String> topFiveTitles = TopFive(titleAndReview);

        for (String title : topFiveTitles) {
            if (titleAndURL.containsKey(title)) {
                String url = titleAndURL.get(title);
                System.out.println("Title: " + title + ", Image URL: " + url);
            }
        }
        System.out.println("End Test case: Print the Title and image URL of the 5 items with highest number of reviews");
    }
    public static List<String> TopFive(Map<String, Integer> topfive) {
        return topfive.entrySet()
                  .stream()
                  .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                  .limit(5)
                  .map(Map.Entry::getKey)
                  .collect(Collectors.toList());
    }
}
