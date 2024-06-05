package demo;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.asserts.SoftAssert;

public class TestCases {
        ChromeDriver driver;
        YoutubeWrapper wrapper;
        SoftAssert softAssert;

        @BeforeClass(alwaysRun = true)
        public void initialize_driver() {
                driver = new ChromeDriver();
                driver.manage().window().maximize();
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                wrapper = new YoutubeWrapper(driver);
                softAssert = new SoftAssert();
        }

        @AfterClass(alwaysRun = true)
        public void close_and_quit_driver() {
                driver.close();
                driver.quit();
        }

        // Testcase 01 - Verify the about section in Youtube
        @Test(priority = 0, description = "Verify the about section in Youtube")
        public void About_Section() throws InterruptedException {
                System.out.println("Start Test case: Verify the about section in Youtube");
                driver.get("https://youtube.com");
                softAssert.assertTrue(driver.getCurrentUrl().contains("youtube.com"), "URL is incorrect");

                wrapper.clickElement(By.xpath("//*[text()='About']"));
                String aboutContent = wrapper.getElementText(By.xpath("//section[@class='ytabout__content']"));

                System.out.println(aboutContent);

                softAssert.assertAll();
                System.out.println("End Test case: Verify the about section in Youtube");
        }

        // Testcase 02 - Verify the Film section in Youtube
        @Test(priority = 0, description = "Verify the Film section in Youtube")
        public void FilmsTab() throws InterruptedException {
                System.out.println("Start Test case: Verify the Film section in Youtube");
                driver.get("https://youtube.com");
                wrapper.clickElement(By.xpath("//yt-formatted-string[text()='Movies']"));
                wrapper.scrollToRight(By.xpath("//div[@id='right-arrow']//button"));

                List<WebElement> rating = wrapper.findElements(By.xpath(
                                "//div[@id='scroll-outer-container']/div/div/ytd-grid-movie-renderer/ytd-badge-supported-renderer/div[2]/p"));
                String lastRatingText = rating.get(rating.size() - 1).getText();
                System.out.println(lastRatingText);
                softAssert.assertTrue(lastRatingText.equals("A"), "Movie is not marked as Mature");

                List<WebElement> genre = wrapper
                                .findElements(By.xpath(
                                                "//div[@id='scroll-outer-container']/div/div/ytd-grid-movie-renderer/a/span"));
                String lastGenreText = genre.get(genre.size() - 1).getText();
                String[] lastGenreTextSplit = lastGenreText.split("•");
                System.out.println(lastGenreTextSplit[0].trim());
                softAssert.assertTrue(
                                lastGenreTextSplit[0].trim().equals("Comedy") || lastGenreText.equals("Animation"),
                                "Movie is neither Comedy nor Animation");

                softAssert.assertAll();
                System.out.println("End Test case: Verify the Film section in Youtube");
        }

        // Testcase 03 - Verify the Music section in Youtube
        @Test(priority = 0, description = "Verify the Music section in Youtube")
        public void MusicTab() throws InterruptedException {
                System.out.println("Start Test case: Verify the Music section in Youtube");
                driver.get("https://youtube.com");
                wrapper.clickElement(By.xpath("//yt-formatted-string[text()='Music']"));
                wrapper.scrollToRight(
                                By.xpath("//span[contains(.,'Biggest Hits')]//ancestor::div[6]//div[@id='right-arrow']//button"));

                List<WebElement> musicPlayList = wrapper.findElements(By.xpath(
                                "//span[contains(.,'Biggest Hits')]//ancestor::div[6]//div[@id='scroll-outer-container']/div/div/ytd-compact-station-renderer/div/a/h3"));
                String lastMusicPlayList = musicPlayList.get(musicPlayList.size() - 1).getText();
                System.out.println(lastMusicPlayList);

                List<WebElement> musicTrackList = wrapper
                                .findElements(By.xpath(
                                                "//span[contains(.,'Biggest Hits')]//ancestor::div[6]//div[@id='scroll-outer-container']/div/div/ytd-compact-station-renderer/div/a/p"));
                String lastMusicTrackList = musicTrackList.get(musicTrackList.size() - 1).getText();
                String[] lastMusicTrackListSplit = lastMusicTrackList.split("tracks");
                System.out.println(lastMusicTrackListSplit[0].trim());
                int trackCount = Integer.parseInt(lastMusicTrackListSplit[0].trim());
                softAssert.assertTrue(trackCount <= 50,
                                "Number of tracks listed is not less than or equal to 50");

                softAssert.assertAll();
                System.out.println("End Test case: Verify the Music section in Youtube");
        }

        // Testcase 04 - Verify the News section in Youtube
        @Test(priority = 0, description = "Verify the News section in Youtube")
        public void NewsTab() throws InterruptedException {
                System.out.println("Start Test case: Verify the News section in Youtube");
                driver.get("https://youtube.com");
                wrapper.clickElement(By.xpath("//yt-formatted-string[text()='News']"));
                int totalLikes = 0;
                for (int i = 1; i <= 3; i++) {
                        String header = wrapper.getElementText(By.xpath(
                                        "(//div[@id='rich-shelf-header-container' and contains(.,'Latest news posts')]//ancestor::div[1]//div[@id='contents']//div[@id='header'])["
                                                        + i + "]"));
                        String[] headerSplit = header.split("•");
                        System.out.println(headerSplit[0].trim());
                        String body = wrapper.getElementText(By.xpath(
                                        "(//div[@id='rich-shelf-header-container' and contains(.,'Latest news posts')]//ancestor::div[1]//div[@id='contents']//div[@id='body'])["
                                                        + i + "]"));
                        System.out.println(body);
                        String likes = wrapper.getElementText(By.xpath(
                                        "(//div[@id='rich-shelf-header-container' and contains(.,'Latest news posts')]//ancestor::div[1]//div[@id='contents']//span[@id='vote-count-middle'])["
                                                        + i + "]"));
                        if (likes == "") {
                                likes = "0";
                        }
                        int likesCount = Integer.parseInt(likes);
                        totalLikes += likesCount;
                }
                System.out.println("Total likes of top 3 posts : " + totalLikes);
                System.out.println("End Test case: Verify the News section in Youtube");
        }
}
