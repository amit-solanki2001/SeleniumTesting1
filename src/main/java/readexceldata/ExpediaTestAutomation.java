package readexceldata;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExpediaTestAutomation {

    public static void main(String[] args) throws IOException {
        // Set the path to ChromeDriver executable
//        System.setProperty("webdriver.chrome.driver", "C:/Users/I528638/Documents/chromedriver/chromedriver.exe");

        // Create a WebDriver instance
//        WebDriver driver = new ChromeDriver();
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        // Define path to the Excel file
        String excelFilePath = "C:/Users/I528638/Downloads/SQAT_Testing_DATA.xlsx";

        // Load the Excel file
        FileInputStream inputStream = new FileInputStream(excelFilePath);
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
//        driver.get("https://www.expedia.com/");
        // Iterate through each row (excluding header row)
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            // Read test data from Excel file
            String testCaseID = row.getCell(0).getStringCellValue();
            String testDesc = row.getCell(1).getStringCellValue();
            String inputField1 = row.getCell(2).getStringCellValue();
            String inputField2 = row.getCell(3).getStringCellValue();
            String expectedOutcome = row.getCell(4).getStringCellValue();

            // Launch the Expedia website
            driver.get("https://www.expedia.com/");

            // Perform test steps
            WebElement input1 = driver.findElement(By.id("location-field-leg1-origin"));
            WebElement input2 = driver.findElement(By.id("location-field-leg1-destination"));

            input1.clear();
            input2.clear();

            if (!inputField1.isEmpty()) {
                input1.sendKeys(inputField1);
            }

            if (!inputField2.isEmpty()) {
                input2.sendKeys(inputField2);
            }

            // Submit form or perform relevant actions
            WebElement submitButton = driver.findElement(By.xpath("//button[@data-testid='submit-button']"));
            submitButton.click();

            // Wait for the result to be displayed
            WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
            WebElement resultElement;
//            Thread.sleep(5000);
//            Wait<WebDriver> wait =
//                    new FluentWait<>(driver)
//                            .withTimeout(java.time.Duration.ofSeconds(10))
//                            .pollingEvery(java.time.Duration.ofMillis(400))
//                            .ignoring(NoSuchElementException.class);
            try {
                resultElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(), 'Error')]")));
            } catch (Exception e) {
                resultElement = driver.findElement(By.tagName("body"));
            }

            // Get the actual result
            String actualOutcome = resultElement.getText();

            // Compare actual and expected outcomes
            String status;
            if (actualOutcome.contains(expectedOutcome)) {
                status = "Pass";
            } else {
                status = "Fail";
            }

            // Record actual result and status in Excel file
            row.createCell(5).setCellValue(actualOutcome);
            row.createCell(6).setCellValue(status);
        }

        // Write the results back to the Excel file
        inputStream.close();
        FileOutputStream outputStream = new FileOutputStream(new File(excelFilePath));
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();

//         Close the WebDriver instance
        driver.quit();
    }
}