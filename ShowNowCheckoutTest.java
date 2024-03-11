package Tests;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import java.util.Set;

public class ShowNowCheckoutTest {
    private WebDriver driver;
    private String baseUrl = "https://www.flipkart.com";

    @BeforeClass
    @Parameters("browser")
    public void setUp() {
    	if (browser.equalsIgnoreCase("chrome")) {
        System.setProperty("webdriver.chrome.driver", "C:\\Drivers\\chromedriver.exe");
        driver = new ChromeDriver();
    	}
    	
    	if (browser.equalsIgnoreCase("Firefox")) {
            System.setProperty("webdriver.gecko.driver", "C:\\Drivers\\geckodriver.exe");
            driver = new FirefoxDriver();
        	}
    	
    	
        driver.manage().window().maximize();
    
    }

    @Test(priority = 1)
    public void testHomePageLoad() {
        driver.get(baseUrl);
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.titleContains("Online Shopping Site for Mobiles, Electronics, Furniture, Grocery, Lifestyle, Books & More. Best Offers!"));
        Assert.assertTrue(driver.getTitle().contains("Online Shopping Site for"), "Home page did not load successfully");
    }

    @Test(priority = 2)
    public void testSearchAndAddToCart() throws Exception {
        WebElement searchBox = driver.findElement(By.name("q"));
        searchBox.sendKeys("laptop");
        searchBox.sendKeys(Keys.ENTER);

        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement laptopLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"container\"]/div/div[3]/div[1]/div[2]/div[2]/div/div/div/a/div[2]/div[1]/div[2]")));
        laptopLink.click();
        
        Set<String> windowHandles = driver.getWindowHandles();

        // Switch to the second window opened
        String[] handles = windowHandles.toArray(new String[0]);
        driver.switchTo().window(handles[1]);
        Thread.sleep(3000); 
     
        WebElement scrollintoview=driver.findElement(By.xpath("//*[@id=\"container\"]/div/div[3]/div[1]/div[2]/div[8]/div[6]/div/div[2]/button"));
        JavascriptExecutor js= (JavascriptExecutor)driver;
        js.executeScript("arguments[0].scrollIntoView(true);", scrollintoview);
        Thread.sleep(3000);
        WebElement AddToCart = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"container\"]/div/div[3]/div[1]/div[1]/div[2]/div/ul/li[1]/button")));
        AddToCart.click();
        Thread.sleep(3000);
        String cartStatus=driver.findElement(By.xpath("//*[@id=\"container\"]/div/div[1]/div/div/div[2]")).getText();
       Assert.assertEquals(cartStatus, "Product Successfully Added To Cart");
     
    }

    @Test(priority = 3)
    public void testProceedToCheckout() {
    	WebElement proceedToCheckoutButton = driver.findElement(By.xpath("//button[@class='_2KpZ6l _2ObP_T _3AWRsL']"));
        proceedToCheckoutButton.click();
    }

    @Test(priority = 4)
    public void testUserAuthentication() throws Exception {
    	driver.findElement(By.xpath("//*[@id=\"container\"]/div/div[2]/div/div/div[1]/div/div[5]/div/form/button/span"));
    	Thread.sleep(2000);
    	WebElement usernameField = driver.findElement(By.className("_2IX_2- _17N0em"));
        usernameField.sendKeys("Harshaljoshi3579@gmail.com");
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("Pass@123");
        WebElement loginButton = driver.findElement(By.xpath("//button[@class='_2KpZ6l _2HKlqd _3AWRsL']"));
        loginButton.click();
        
        assert driver.getTitle().contains("Checkout") : "Login unsuccessful";
    }

    @Test(priority = 5)
    public void testShippingInformation() {
    	
    	WebElement addressField = driver.findElement(By.id("address"));
        addressField.sendKeys("Shree Ganesha apt, Fulsundar estate");
        WebElement cityField = driver.findElement(By.id("city"));
        cityField.sendKeys("Nashik");
        WebElement stateField = driver.findElement(By.id("state"));
        stateField.sendKeys("Maharashtra");
        WebElement zipCodeField = driver.findElement(By.id("zipCode"));
        zipCodeField.sendKeys("422006");
    }

    @Test(priority = 6)
    public void testPaymentInformation() {
    	
    	 WebElement nextButton = driver.findElement(By.xpath("//button[@class='_2KpZ6l _3AWRsL']"));
         nextButton.click();
         
         WebElement paymentMethod = driver.findElement(By.xpath("//input[@id='creditCard']"));
         paymentMethod.click();

    }

    @Test(priority = 7)
    public void testReviewOrder() {
        // Verify order summary
    	
    	  WebElement orderSummary = driver.findElement(By.xpath("//div[@class='_1hfvO3']"));
          assert orderSummary.isDisplayed() : "Order summary not displayed";
        // Do not proceed to final order placement
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
