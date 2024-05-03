package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelTransformationUtility {

    public static Workbook workbook;

    public static void fieldToFieldColumnMapping(String filePath, String sourceSheetName, String destinationSheetName, String sourceColumnName, String destinationColumnName) {
        FileInputStream fis = null;
        FileOutputStream outputStream = null;
        try {
            fis = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(fis);

            Sheet sourceSheet = workbook.getSheet(sourceSheetName);
            Sheet destinationSheet = workbook.getSheet(destinationSheetName);

            int sourceColumnIndex = getColumnIndex(sourceSheet, sourceColumnName);
            int destinationColumnIndex = getColumnIndex(destinationSheet, destinationColumnName);

            if (sourceColumnIndex == -1) {
                throw new IllegalArgumentException("Given source column name not found in the source sheet.");
            }

            if (destinationColumnIndex == -1) {
                destinationColumnIndex = createColumn(destinationSheet, destinationColumnName);
            }

            mapColumnValues(sourceSheet, destinationSheet, sourceColumnIndex, destinationColumnIndex);

            // Write changes to the workbook
            outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);

            System.out.println("Column mapped successfully!");
        } catch (IOException | IllegalArgumentException | IllegalStateException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (fis != null) {
                    fis.close();
                }
                if (workbook != null) {
                    workbook.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static int getColumnIndex(Sheet sheet, String columnName) {
        Row headerRow = sheet.getRow(0);
        for (Cell cell : headerRow) {
            if (cell.getStringCellValue().equals(columnName))
            {
                return cell.getColumnIndex();
            }
        }
        return -1;
    }

    private static int createColumn(Sheet sheet, String columnName) {
        int columnIndex = sheet.getRow(0).getLastCellNum();
        Row headerRow = sheet.getRow(0);
        Cell cell = headerRow.createCell(columnIndex);
        cell.setCellValue(columnName);
        return columnIndex;
    }

    private static void mapColumnValues(Sheet sourceSheet, Sheet destinationSheet, int sourceColumnIndex, int destinationColumnIndex) {
        for (int i = 1; i <= sourceSheet.getLastRowNum(); i++) {
            Row sourceRow = sourceSheet.getRow(i);
            Row destinationRow = destinationSheet.getRow(i);
            if (destinationRow == null) {
                destinationRow = destinationSheet.createRow(i);
            }
            Cell sourceCell = sourceRow.getCell(sourceColumnIndex);
            Cell destinationCell = destinationRow.createCell(destinationColumnIndex);
            if (sourceCell != null) {
                destinationCell.setCellValue(sourceCell.getStringCellValue());
            }
        }
    }

   /*
    the values should be split and mapped into n diff columns

   public static void splitAndMap(String filePath, String sourceSheet, String destinationSheet,String columnName, String desColName) throws IOException {
        FileInputStream fis = null;
        FileOutputStream outputStream = null;
        try {
            fis = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(fis);
            Sheet sourceSheetName = workbook.getSheet(sourceSheet);
            Sheet destinationSheetName = workbook.getSheet(destinationSheet);
            int sourceColumnIndex = getColumnIndex(sourceSheetName,columnName);
            int destinationColumnIndex = createColumn(destinationSheetName, desColName);
            splitCellValueAndMapFirstIndex(sourceSheetName, destinationSheetName, sourceColumnIndex, destinationColumnIndex);
            outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (fis != null) {
                    fis.close();
                }
                if (workbook != null) {
                    workbook.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/

    private static void splitCellValueAndMapFirstIndex(Sheet sourceSheet, Sheet destinationSheet, int sourceColumnIndex, int destinationColumnIndex) {
        for (int i = 1; i <= sourceSheet.getLastRowNum(); i++) {
            Row sourceRow = sourceSheet.getRow(i);
            Row destinationRow = destinationSheet.getRow(i);
            if (destinationRow == null) {
                destinationRow = destinationSheet.createRow(i);
            }
            Cell sourceCell = sourceRow.getCell(sourceColumnIndex);
            Cell destinationCell = destinationRow.createCell(destinationColumnIndex);
            if (sourceCell != null) {
                String[] values = sourceCell.getStringCellValue().split("\\^"); // Split the cell value with delimiter ^
                // write a condition to get the array values based on arguments passed and the values should be split and mapped into n diff columns
                if (values.length > 0) {
                    destinationCell.setCellValue(values[0]); // Map the value of the first index into the destination column
                }
            }
        }
    }
}
