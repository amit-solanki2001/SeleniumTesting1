package readexceldata;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;

public class IRCTC {
    public static void main(String[] args) throws IOException, InterruptedException {
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));//it is used when sth is already in page,just waiting
        //to appear on the page

//        driver.get("https://www.irctc.co.in/nget/");

        String excelFilePath = "C:/Users/I528638/Downloads/IRCTC.xlsx";
        FileInputStream inputStream = new FileInputStream(excelFilePath);
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

//        driver.findElement(By.xpath("//input[@class='ng-tns-c57-8 ui-inputtext ui-widget ui-state-default ui-corner-all ui-autocomplete-input ng-star-inserted']")).sendKeys("JAIPUR - JP (JAIPUR)");
//        driver.findElement(By.xpath("//input[@class='ng-tns-c57-9 ui-inputtext ui-widget ui-state-default ui-corner-all ui-autocomplete-input ng-star-inserted']")).sendKeys("BHOPAL  JN - BPL (BHOPAL)");
//        Thread.sleep(5000);

//        driver.findElement(By.cssSelector("button[type='submit']")).click();
        int count = 0;
        for (Row row : sheet){
            if(count==0){
                count++;
                continue;
            }

            String testCaseID = row.getCell(0).getStringCellValue();
            String testDesc = row.getCell(1).getStringCellValue();
            String inputField1 = row.getCell(2).getStringCellValue();
            String inputField2 = row.getCell(3).getStringCellValue();
            String expectedOutcome = row.getCell(4).getStringCellValue();

            driver.get("https://www.irctc.co.in/nget/");
            Thread.sleep(5000);
            WebElement input1 = driver.findElement(By.xpath("//input[@class='ng-tns-c57-8 ui-inputtext ui-widget ui-state-default ui-corner-all ui-autocomplete-input ng-star-inserted']"));
            WebElement input2 = driver.findElement(By.xpath("//input[@class='ng-tns-c57-9 ui-inputtext ui-widget ui-state-default ui-corner-all ui-autocomplete-input ng-star-inserted']"));

            input1.clear();
            input2.clear();

            if (!inputField1.isEmpty()) {
                input1.sendKeys(inputField1);
            }

            if (!inputField2.isEmpty()) {
                input2.sendKeys(inputField2);
            }

//            Thread.sleep(3000);
            WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
            submitButton.click();
            Thread.sleep(4000);
            // Wait for the result to be displayed
            WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
            //
//            try {
//                resultElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(), 'Error')]")));
//            } catch (Exception e) {
//                resultElement = driver.findElement(By.tagName("body"));
//            }
//
//            // Get the actual result
            String actualOutcome = driver.getCurrentUrl();

            // Compare actual and expected outcomes
            String status;
            if (actualOutcome.equals("https://www.irctc.co.in/nget/booking/train-list")) {
                status = "Pass";
            } else {
                status = "Fail";
            }

            // Record actual result and status in Excel file
            row.createCell(5).setCellValue(actualOutcome);
            row.createCell(6).setCellValue(status);

        }


        FileOutputStream outputStream = new FileOutputStream("C:/Users/I528638/Downloads/IRCTC.xlsx");
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
        inputStream.close();

//         Close the WebDriver instance
        driver.quit();

    }
}
