package utils;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtils {

    // Method to read data from an Excel file
    public static @NotNull List<Object[]> getExcelData(String excelFilePath) throws IOException {
        List<Object[]> data = new ArrayList<>();

        // Open the Excel file
        FileInputStream fis = new FileInputStream(new File(excelFilePath));

        // Create a Workbook instance for .xlsx files
        Workbook workbook = new XSSFWorkbook(fis);  // XSSFWorkbook for .xlsx files

        // Get the first sheet (index 0)
        Sheet sheet = workbook.getSheetAt(0);

        // Loop through rows (skip header row)
        for (int rowIndex = 1; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);

            // Extract data from each cell in the row
            String cityCodes = row.getCell(0).getStringCellValue();
            String travelerCountryCode = row.getCell(1).getStringCellValue();
            String destinationCountryCodes = row.getCell(2).getStringCellValue();

            // Add the extracted data to the list (returning as Object[])
            data.add(new Object[]{cityCodes, travelerCountryCode, destinationCountryCodes});
        }

        // Close the workbook and file input streams
        workbook.close();
        fis.close();

        return data;
    }
}
