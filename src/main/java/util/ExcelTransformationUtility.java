package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelTransformationUtility {

    public static Workbook workbook;

    /*
    Logic for Field To Field Mapping
     */
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
            closeResources(fis, outputStream, workbook);
        }
    }

    /*
    To get the column index in source and destination sheet
     */
    private static int getColumnIndex(Sheet sheet, String columnName) {
        Row headerRow = sheet.getRow(0);
        if (headerRow != null) {
            for (Cell cell : headerRow) {
                if (cell.getStringCellValue().equals(columnName)) {
                    return cell.getColumnIndex();
                }
            }
        }
        return -1;
    }


    /*
    To check and create column and set the column name in destination sheet
     */
    private static int createColumn(Sheet sheet, String columnName) {
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            headerRow = sheet.createRow(0); // Create the header row if it doesn't exist
        }
        int columnIndex = headerRow.getLastCellNum();
        if (columnIndex == -1) {
            columnIndex = 0; // If no cell exists, It'll start writing from the first column
        }
        Cell cell = headerRow.createCell(columnIndex);
        cell.setCellValue(columnName);
        return columnIndex;
    }


    /*
    Logic for column mapping and to set the values
     */
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
    Logic to map split values separated w.r.t delimiter with n number of column names in destination sheet
    Columns: Category/Type/Sub-Type, Region/Marketing Country,
     */
    public static void splitAndMap(String filePath, String sourceSheetName, String destinationSheetName, String sourceColumnName, String... destinationColumnNames) throws IOException {
        FileInputStream fis = null;
        FileOutputStream outputStream = null;
        try {
            fis = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(fis);

            Sheet sourceSheet = workbook.getSheet(sourceSheetName);
            Sheet destinationSheet = workbook.getSheet(destinationSheetName);

            int sourceColumnIndex = getColumnIndex(sourceSheet, sourceColumnName);
            int[] destinationColumnIndexes = new int[destinationColumnNames.length];
            for (int i = 0; i < destinationColumnNames.length; i++) { //iterating through the number of destination column names to check, create and set the column names passed as params
                destinationColumnIndexes[i] = getColumnIndex(destinationSheet, destinationColumnNames[i]);
                if (destinationColumnIndexes[i] == -1) {
                    destinationColumnIndexes[i] = createColumn(destinationSheet, destinationColumnNames[i]);
                }
            }

            if (sourceColumnIndex == -1) {
                throw new IllegalArgumentException("Given source column name not found in the source sheet.");
            }

            splitCellValueBasedOnDelimiter(sourceSheet, destinationSheet, sourceColumnIndex, destinationColumnIndexes);

            // Write changes to the workbook
            outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);

            System.out.println("Columns mapped successfully!");
        } catch (IOException | IllegalArgumentException | IllegalStateException ex) {
            ex.printStackTrace();
        } finally {
            closeResources(fis, outputStream, workbook);
        }
    }

    /*
    Split the cell values based on delimiter and set the cell values w.r.t destination column index
     */
    private static void splitCellValueBasedOnDelimiter(Sheet sourceSheet, Sheet destinationSheet, int sourceColumnIndex, int... destinationColumnIndexes) {
        for (int i = 1; i <= sourceSheet.getLastRowNum(); i++) {
            Row sourceRow = sourceSheet.getRow(i);
            Row destinationRow = destinationSheet.getRow(i);
            if (destinationRow == null) {
                destinationRow = destinationSheet.createRow(i);
            }
            Cell sourceCell = sourceRow.getCell(sourceColumnIndex);
            if (sourceCell != null) {
                String cellValue = sourceCell.getStringCellValue();
                if (cellValue != null && !cellValue.isEmpty()) {
                    String[] values = cellValue.split("\\^"); // Split the cell value with delimiter ^

                    for (int j = 0; j < destinationColumnIndexes.length; j++) {
                        if (j < values.length) {
                            Cell destinationCell = destinationRow.createCell(destinationColumnIndexes[j]);
                            destinationCell.setCellValue(values[j].trim());
                        }
                    }
                }
            }
        }
    }

    /*
    Columns = Product Image Angle, FERT, Language
     */
    public static void parseAndMap(String filePath, String sourceSheetName, String destinationSheetName, String sourceColumnName, String destinationColumnName) throws IOException {
        FileInputStream fis = null;
        FileOutputStream outputStream = null;
        Workbook workbook = null;
        try {
            fis = new FileInputStream(filePath);
            workbook = WorkbookFactory.create(fis);

            Sheet sourceSheet = workbook.getSheet(sourceSheetName);
            Sheet destinationSheet = workbook.getSheet(destinationSheetName);

            int sourceColumnIndex = getColumnIndex(sourceSheet, sourceColumnName);
            int destinationColumnIndex = createColumn(destinationSheet, destinationColumnName);

            if (sourceColumnIndex == -1) {
                throw new IllegalArgumentException("Given source column name not found in the source sheet.");
            }

            parseAndMapCellValues(sourceSheet, destinationSheet, sourceColumnIndex, destinationColumnIndex);

            // Write changes to the workbook
            outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);

            System.out.println("Column mapped successfully!");
        } catch (IOException | IllegalArgumentException | IllegalStateException ex) {
            ex.printStackTrace();
        } finally {
            closeResources(fis, outputStream, workbook);
        }
    }

    /*
    Split the values with Delimiter "||" and append with "~" (or) append with ","
     */
    private static void parseAndMapCellValues(Sheet sourceSheet, Sheet destinationSheet, int sourceColumnIndex, int destinationColumnIndex) {
        for (int i = 0; i <= sourceSheet.getLastRowNum(); i++) {
            Row sourceRow = sourceSheet.getRow(i);
            Row destinationRow = destinationSheet.getRow(i);
            if (destinationRow == null) {
                destinationRow = destinationSheet.createRow(i);
            }
            Cell sourceCell = sourceRow.getCell(sourceColumnIndex);
            if (sourceCell != null) {
                String cellValue = sourceCell.getStringCellValue();
                if (cellValue != null && !cellValue.isEmpty()) {
                    Set<String> uniqueValues = new HashSet<>();
                    StringBuilder concatenatedValue = new StringBuilder();
                    String[] values = cellValue.split("\\|\\|"); // Split the cell value with "||" delimiter
                    for (String value : values) {
                        value = value.trim();
                        if (!uniqueValues.contains(value)) {
                            if (concatenatedValue.length() > 0) {
                                concatenatedValue.append("~"); // Adding delimiter "~" between values
                                //concatenatedValue.append(","); // Adding delimiter "," between values
                            }
                            concatenatedValue.append(value);
                            uniqueValues.add(value);
                        }
                    }
                    Cell destinationCell = destinationRow.createCell(destinationColumnIndex);
                    destinationCell.setCellValue(concatenatedValue.toString());
                }
            }
        }
    }

    /*
    Column = Occasion
    Cleansing Example:Christmas^2018||Christmas^2018||Holiday^2019
            -> Occasion = Christmas~Holiday
            -> Year = 2018~2019
     */

    public static void parseAndMap(String filePath, String sourceSheetName, String destinationSheetName,
                                   String sourceColumnName, String destinationColumnName1, String destinationColumnName2) throws IOException {
        FileInputStream fis = null;
        FileOutputStream outputStream = null;
        Workbook workbook = null;

        try {
            fis = new FileInputStream(filePath);
            workbook = WorkbookFactory.create(fis);

            Sheet sourceSheet = workbook.getSheet(sourceSheetName);
            Sheet destinationSheet = workbook.getSheet(destinationSheetName);

            int sourceColumnIndex = getColumnIndex(sourceSheet, sourceColumnName);

            int destinationColumnIndex1 = createColumn(destinationSheet, destinationColumnName1);
            int destinationColumnIndex2 = createColumn(destinationSheet, destinationColumnName2);

            if (sourceColumnIndex == -1) {
                throw new IllegalArgumentException("Given source column name not found in the source sheet.");
            }

            parseAndMapCellValues(sourceSheet, destinationSheet, sourceColumnIndex, destinationColumnIndex1, destinationColumnIndex2);

            outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);
        } finally {
            closeResources(fis, outputStream, workbook);
        }
    }

    private static void parseAndMapCellValues(Sheet sourceSheet, Sheet destinationSheet, int sourceColumnIndex,
                                              int destinationColumnIndex1, int destinationColumnIndex2) {
        for (int i = 1; i <= sourceSheet.getLastRowNum(); i++) {
            Row sourceRow = sourceSheet.getRow(i);
            Row destinationRow = destinationSheet.getRow(i);
            if (destinationRow == null) {
                destinationRow = destinationSheet.createRow(i);
            }
            Cell sourceCell = sourceRow.getCell(sourceColumnIndex);
            if (sourceCell != null) {
                String cellValue = sourceCell.getStringCellValue();
                if (cellValue != null && !cellValue.isEmpty()) {
                    Set<String> uniqueValues1 = new HashSet<>();
                    Set<String> uniqueValues2 = new HashSet<>();
                    StringBuilder destinationColName1 = new StringBuilder();
                    StringBuilder destinationColName2 = new StringBuilder();
                    String[] values = cellValue.split("\\|\\|");
                    for (String value : values) {
                        String[] subParts = value.split("\\^");
                        if (subParts.length == 2) {
                            String v1 = subParts[0].trim();
                            String v2 = subParts[1].trim();
                            if (!uniqueValues1.contains(v1)) {
                                if (destinationColName1.length() > 0) {
                                    destinationColName1.append("~");
                                }
                                destinationColName1.append(v1);
                                uniqueValues1.add(v1);
                            }
                            if (!uniqueValues2.contains(v2)) {
                                if (destinationColName2.length() > 0) {
                                    destinationColName2.append("~");
                                }
                                destinationColName2.append(v2);
                                uniqueValues2.add(v2);
                            }
                        }
                    }
                    Cell destinationCell1 = destinationRow.createCell(destinationColumnIndex1);
                    destinationCell1.setCellValue(destinationColName1.toString());
                    Cell destinationCell2 = destinationRow.createCell(destinationColumnIndex2);
                    destinationCell2.setCellValue(destinationColName2.toString());
                }
            }
        }
    }

    /*
    Cleansing Example: Occasion
           Christmas^2018||Christmas^2018||Holiday^2018
           -> Occasion = Christmas~Holiday
     */

    public static void parseAndMap1(String filePath, String sourceSheetName, String destinationSheetName,
                                   String sourceColumnName, String destinationColumnName) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sourceSheet = workbook.getSheet(sourceSheetName);
            Sheet destinationSheet = workbook.getSheet(destinationSheetName);

            if (sourceSheet == null || destinationSheet == null) {
                System.out.println("Source or Destination sheet not found.");
                return;
            }

            int sourceColumnIndex = getColumnIndex(sourceSheet, sourceColumnName);
            int destinationColumnIndex = createColumn(destinationSheet, destinationColumnName);

            if (sourceColumnIndex == -1) {
                throw new IllegalArgumentException("Given source column name not found in the source sheet.");
            }

            // Process each row in the source sheet
            for (int i = sourceSheet.getFirstRowNum()+1; i <= sourceSheet.getLastRowNum(); i++) {
                Row sourceRow = sourceSheet.getRow(i);
                if (sourceRow != null) {
                    Cell sourceCell = sourceRow.getCell(sourceColumnIndex);
                    if (sourceCell != null) {
                        String cellValue = sourceCell.getStringCellValue();
                        if (cellValue != null && !cellValue.isEmpty()) {
                            Set<String> uniqueValues = parseAndExtractUniqueValues(cellValue);
                            String concatenatedValues = String.join("~", uniqueValues);

                            // Write the concatenated values to the destination column
                            Row destinationRow = destinationSheet.getRow(i);
                            if (destinationRow == null) {
                                destinationRow = destinationSheet.createRow(i);
                            }
                            Cell destinationCell = destinationRow.createCell(destinationColumnIndex);
                            destinationCell.setCellValue(concatenatedValues);
                        }
                    }
                }
            }

            // Save changes to the workbook
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
                System.out.println("Excel file updated successfully.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Set<String> parseAndExtractUniqueValues(String cellValue) {
        Set<String> uniqueValues = new HashSet<>();
        String[] parts = cellValue.split("\\|\\|");
        for (String part : parts) {
            String[] subParts = part.split("\\^");
            if (subParts.length == 2) {
                uniqueValues.add(subParts[0]);
            }
        }
        return uniqueValues;
    }

        /*
        Column = Occasion year
        Christmas^2018||Christmas^2018||Holiday^2019
        -> Year = 2018~2019
        */

    public static void parseAndMap2(String filePath, String sourceSheetName, String sourceColumnName,
                                   String destinationSheetName, String destinationColumnName) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = WorkbookFactory.create(fis);

        Sheet sourceSheet = workbook.getSheet(sourceSheetName);
        Sheet destinationSheet = workbook.getSheet(destinationSheetName);

        int sourceColumnIndex = getColumnIndex(sourceSheet, sourceColumnName);
        int destinationColumnIndex = createColumn(destinationSheet, destinationColumnName);

        if (sourceColumnIndex == -1) {
            throw new IllegalArgumentException("Given source column name not found in the source sheet.");
        }

        parseAndMapCellValues2(sourceSheet, destinationSheet, sourceColumnIndex, destinationColumnIndex);

        // Write changes to the workbook
        FileOutputStream outputStream = new FileOutputStream(filePath);
        workbook.write(outputStream);
        System.out.println("Column Mapped Successfully");
        // Close resources
        outputStream.close();
        fis.close();
        workbook.close();
    }

    private static void parseAndMapCellValues2(Sheet sourceSheet, Sheet destinationSheet,
                                              int sourceColumnIndex, int destinationColumnIndex) {
        for (int i = 1; i <= sourceSheet.getLastRowNum(); i++) {
            Row sourceRow = sourceSheet.getRow(i);
            Row destinationRow = destinationSheet.getRow(i);
            if (destinationRow == null) {
                destinationRow = destinationSheet.createRow(i);
            }
            Cell sourceCell = sourceRow.getCell(sourceColumnIndex);
            if (sourceCell != null) {
                String cellValue = sourceCell.getStringCellValue();
                if (cellValue != null && !cellValue.isEmpty()) {
                    Set<String> uniqueValues = new HashSet<>();
                    String[] values = cellValue.split("\\|\\|");
                    for (String value : values) {
                        String[] subParts = value.split("\\^");
                        if (subParts.length >= 2) {
                            String year = subParts[1].trim();
                            uniqueValues.add(year);
                        }
                    }
                    //To sort the values in ascending order (Ex: 2018~2019)
                    List<String> sortedValues = new ArrayList<>(uniqueValues);
                    Collections.sort(sortedValues);
                    String concatenatedValue = String.join("~", sortedValues);
                    Cell destinationCell = destinationRow.createCell(destinationColumnIndex);
                    destinationCell.setCellValue(concatenatedValue);
                }
            }
        }
    }


    private static void closeResources(FileInputStream fis, FileOutputStream outputStream, Workbook workbook) {
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


