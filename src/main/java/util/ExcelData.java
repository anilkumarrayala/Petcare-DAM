package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelData {
    public static void main(String[] args) {
        try {
            // Load the Excel file
            FileInputStream fis = new FileInputStream("C://Project//MARS//Pet-automation-test.xlsx");
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0); // Assuming you are working with the first sheet

            // Iterate through each row
            for (Row row : sheet) {
                Cell cell = row.getCell(0); // Assuming the cell with delimited data is in the first column

                // Read cell data
                String cellData = cell.getStringCellValue();
                String[] splitValues = cellData.split("\\|\\|"); // Split using the delimiter ||

                // Write splitted values into different columns
                for (int i = 0; i < splitValues.length; i++) {
                    Cell newCell = row.createCell(i + 1); // Start writing from the next column
                    newCell.setCellValue(splitValues[i]);
                }

                int colIndex = splitValues.length+1; // Start writing from the second column
                for (String value : splitValues) {

                    // Append splitted values into new columns with prefix "string"
                    Cell appendCell = row.createCell(colIndex++);
                    appendCell.setCellValue("/DAM/SegmentBrandSubBrand/MarsWrigley/" + value);
                }
            }

            // Write the changes back to the Excel file
            FileOutputStream fos = new FileOutputStream("C://Project//MARS//Pet-automation-test.xlsx");
            workbook.write(fos);

            // Close streams
            fos.close();
            fis.close();
            workbook.close();

            System.out.println("Splitting and writing completed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

