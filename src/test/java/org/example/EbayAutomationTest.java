package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

public class EbayAutomationTest {

    @Test
    public void testAddItemToCart() {
        // Set up WebDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();

        // Initialize ChromeDriver
        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless");
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        WebDriver driver = new ChromeDriver(options);

        try {
            // Navigate to eBay
            driver.get("https://www.ebay.com");

            // Search for 'book' in the search box
            WebElement searchBox = driver.findElement(By.id("gh-ac"));
            searchBox.sendKeys("book");
            WebElement searchButton = driver.findElement(By.id("gh-btn"));
            searchButton.click();

            // Store the original window handle
            String originalWindow = driver.getWindowHandle();

            // Wait for new window to open using WebDriverWait
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Wait up to 10 seconds
            wait.until(ExpectedConditions.numberOfWindowsToBe(2));  // Wait until there are 2 windows

            // Switch to the new window
            for (String windowHandle : driver.getWindowHandles()) {
                if (!windowHandle.equals(originalWindow)) {
                    driver.switchTo().window(windowHandle);
                    break;
                }
            }

            // Now you're in the new window. Click on the first book in the list
            WebElement firstBook = driver.findElement(By.xpath("(//div[@class='s-item__info clearfix'])[3]"));
            firstBook.click();

            // Click 'Add to cart'
            WebElement addToCartButton = driver.findElement(By.xpath("//a[@id='atcBtn_btn_1']"));
            addToCartButton.click();

            // Verify that the cart has been updated
            WebElement cartIcon = driver.findElement(By.xpath("//*[@id=\"gh-minicart-hover\"]/div/a"));
            assertTrue(cartIcon.getText().contains("1 item"), "Cart was not updated correctly");

        } finally {
            driver.quit();
        }
    }
}
