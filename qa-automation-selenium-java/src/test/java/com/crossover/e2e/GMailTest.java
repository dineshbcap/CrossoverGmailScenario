package com.crossover.e2e;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.commons.lang3.RandomStringUtils;

import junit.framework.Assert;
import junit.framework.TestCase;

public class GMailTest extends TestCase{
    private WebDriver driver;
    private WebDriverWait wait;
    private String dynamicSubject;
    private String dynamicBody;
    private Properties properties = new Properties();
    private static final int IMPLICIT_TIMEOUT_IN_SECONDS = 15;
    private static final int EXPLICIT_TIMEOUT_IN_SECONDS = 30;
    private static final By TXT_USER_NAME = By.id("identifierId");
    private static final By BTN_USER_NAME_NEXT = By.id("identifierNext");
    private static final By TXT_PASSWORD = By.name("password");
    private static final By BTN_PASSWORD_NEXT = By.id("passwordNext");
    private static final By BTN_COMPOSE = By.xpath("//*[@role='button' and (.)='Compose']");
    private static final By TXT_TO_ADDRESS = By.name("to");
    private static final By TXT_SUBJECT = By.name("subjectbox");
    private static final By TXT_BODY = By.xpath("//*[@aria-label='Message Body' and (contains(@class, 'editable'))]"); 
    private static final By BTN_SEND_MAIL = By.xpath("//*[@role='button' and text()='Send']");
    
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\kpdinesh\\Downloads\\chromedriver_win32\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(IMPLICIT_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, EXPLICIT_TIMEOUT_IN_SECONDS);
        properties.load(new FileReader(new File("test.properties")));
    }

    public void tearDown() throws Exception {
        driver.quit();
    }

    @SuppressWarnings("deprecation")
	@Test
    public void testSendEmail() throws Exception {
    	System.out.println(generateUniqueString("_Subject"));
        driver.get("https://mail.google.com/");
        WebElement userElement = driver.findElement(TXT_USER_NAME);
        userElement.sendKeys(properties.getProperty("username"));

        driver.findElement(BTN_USER_NAME_NEXT).click();


        WebElement passwordElement = driver.findElement(TXT_PASSWORD);
        wait.until(ExpectedConditions.elementToBeClickable(passwordElement));
        passwordElement.sendKeys(properties.getProperty("password"));
        driver.findElement(BTN_PASSWORD_NEXT).click();


        WebElement composeElement = driver.findElement(BTN_COMPOSE);
        composeElement.click();

        dynamicSubject = generateUniqueString("_Subject");
        dynamicBody = generateUniqueString("_Body Thanks, Dinesh.P");
        
        driver.findElement(TXT_TO_ADDRESS).clear();
        driver.findElement(TXT_TO_ADDRESS).sendKeys(String.format("%s@gmail.com", properties.getProperty("username")));
        driver.findElement(TXT_SUBJECT).clear();
        driver.findElement(TXT_SUBJECT).sendKeys(dynamicSubject);
        
		driver.findElement(TXT_BODY).clear();
        driver.findElement(TXT_BODY).sendKeys(dynamicBody);
        		
        driver.findElement(BTN_SEND_MAIL).click();
        
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='"+dynamicSubject+"']/parent::span")));
        
        driver.findElement(By.xpath("//span[text()='"+dynamicSubject+"']/parent::span/parent::div/parent::div/parent::div/parent::td/preceding-sibling::td/span[@title='Not starred']")).click();
        
        Assert.assertTrue("Successfully starred the received mail!!", true);
        
        driver.findElement(By.xpath("//span[text()='"+dynamicSubject+"']/parent::span")).click();
      
        Assert.assertTrue("Subject verification successfully verified!!", 
        		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//h2[contains(text(),'"+dynamicSubject+"')]")))) != null);
        
        Assert.assertTrue("Body verification successfully verified!!", 
        		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//div[contains(text(),'"+dynamicBody+"')]")))) != null);
        
    }
    
    /**
     * Method used to generate Random String
     * @param lastStringForIdentification => String used to append at last for identification
     * @return Returning after prefix random String
     */
    private String generateUniqueString(String lastStringForIdentification) {
    	String randomString = null;
    	try {
    		randomString = RandomStringUtils.randomAlphabetic(10) + lastStringForIdentification;
    	}catch (Exception e) {
			randomString = "Using Static String" + lastStringForIdentification;
		}
    	return randomString;
    }
}
