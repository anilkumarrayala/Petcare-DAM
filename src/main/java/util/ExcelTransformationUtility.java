package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import constants.LookupConstants;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelTransformationUtility {

    public static Workbook workbook;
    private static Map<Integer, String> transformationStatusMap = new HashMap<>();
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

            System.out.println("FIELD to FIELD MAPPING --> LH Column "+sourceColumnName+" mapped successfully to Aprimo Column "+ destinationColumnName);
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
            if (sourceRow == null) continue;
            Row destinationRow = destinationSheet.getRow(i);
            if (destinationRow == null) {
                destinationRow = destinationSheet.createRow(i);
            }
            Cell sourceCell = sourceRow.getCell(sourceColumnIndex);
            Cell destinationCell = destinationRow.createCell(destinationColumnIndex);
            if (sourceCell != null) {
                String destinationCellValue = getCellValueAsString(sourceCell).trim();
                destinationCell.setCellValue(destinationCellValue.replace("N/A","NA"));
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
            for (int i = 0; i < destinationColumnNames.length; i++) {
                //iterating through the number of destination column names to check, create and set the column names passed as params
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

            System.out.println("SPLIT and MAP --> LH Column "+sourceColumnName+ "mapped successfully to Aprimo Column "+ destinationColumnNames);
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
    public static void parseAndMapSingleColumn(String filePath, String sourceSheetName, String destinationSheetName, String sourceColumnName, String destinationColumnName, char deLimiter) throws IOException {
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

            //parseAndMapSingleCellValues(sourceSheet, destinationSheet, sourceColumnIndex, destinationColumnIndex,deLimiter);
            for (int i = 1; i <= sourceSheet.getLastRowNum(); i++) {
                Row sourceRow = sourceSheet.getRow(i);
                if (sourceRow == null) continue;
                Row destinationRow = destinationSheet.getRow(i);
                if (destinationRow == null) {
                    destinationRow = destinationSheet.createRow(i);
                }
                Cell sourceCell = sourceRow.getCell(sourceColumnIndex);
                if (sourceCell != null) {
                    String sourceCellValue = getCellValueAsString(sourceCell);
                    if (sourceCellValue != null && !sourceCellValue.isEmpty()) {
                        Set<String> uniqueValues = new HashSet<>();
                        StringBuilder concatenatedValue = new StringBuilder();
                        String[] values = sourceCellValue.split("\\|\\|"); // Split the cell value with "||" delimiter
                        for (String value : values) {
                            value = value.trim();
                            value = value.replace("N/A","NA");
                            if (!uniqueValues.contains(value)) {
                                if (concatenatedValue.length() > 0) {
                                    concatenatedValue.append(deLimiter); // Adding delimiter ";" between values
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
            // Write changes to the workbook
            outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);

            System.out.println("Parse and Map single column --> LH Column "+sourceColumnName+" mapped successfully to Aprimo Column "+ destinationColumnName);
        } catch (IOException | IllegalArgumentException | IllegalStateException ex) {
            ex.printStackTrace();
        } finally {
            closeResources(fis, outputStream, workbook);
        }
    }

    /*
    Split the values with Delimiter "||" and append with "~" (or) append with ";"
     */
    private static void parseAndMapSingleCellValues(Sheet sourceSheet, Sheet destinationSheet, int sourceColumnIndex, int destinationColumnIndex, char deLimiter) {
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
                        value = value.replace("N/A","NA");
                        if (!uniqueValues.contains(value)) {
                            if (concatenatedValue.length() > 0) {
                                concatenatedValue.append(deLimiter); // Adding delimiter ";" between values
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

    public static void parseAndMapMultipleColumn(String filePath, String sourceSheetName, String destinationSheetName,
                                                 String sourceColumnName, String destinationColumnName1, String destinationColumnName2, char deLimiter) throws IOException {
        FileInputStream fis = null;
        FileOutputStream outputStream = null;

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

            parseAndMapMultipleCellValues(sourceSheet, destinationSheet, sourceColumnIndex, destinationColumnIndex1, destinationColumnIndex2,deLimiter);

            outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);
            System.out.println("Parse and Map Multiple columns --> LH Column "+sourceColumnName+" mapped successfully to Aprimo Column "+ destinationColumnName1 +destinationColumnName2);
        } finally {
            closeResources(fis, outputStream, workbook);
        }
    }

    private static void parseAndMapMultipleCellValues(Sheet sourceSheet, Sheet destinationSheet, int sourceColumnIndex,
                                                      int destinationColumnIndex1, int destinationColumnIndex2 , char deLimiter) {
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
                                    destinationColName2.append(deLimiter);
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
        workbook = WorkbookFactory.create(fis);

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
        System.out.println("Parse and Map2 --> LH Column "+sourceColumnName+" mapped successfully to Aprimo Column "+ destinationColumnName);
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

    /*
    Columns = Region/Marketing Country 1, Region/Marketing Country 2
    append with path = /DAM/MarketingRegionMarketingCountry/
     */
    public static void pickAndConcatenate(String filePath, String sourceSheetName, String destinationSheetName,
                                          String sourceColumnName1, String sourceColumnName2, String destinationColumnName,
                                          char deLimiter, String appendStringValue,
                                          List<String> lookupTable1, List<String> lookupTable2) throws IOException {
        Workbook workbook = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
            workbook = WorkbookFactory.create(fis);

            Sheet sourceSheet = workbook.getSheet(sourceSheetName);
            Sheet destinationSheet = workbook.getSheet(destinationSheetName);

            // Get column indices by column names
            int sourceColumnIndex1 = getColumnIndex(sourceSheet, sourceColumnName1);
            int sourceColumnIndex2 = getColumnIndex(sourceSheet, sourceColumnName2);
            int destinationColumnIndex = getColumnIndex(destinationSheet, destinationColumnName);

            if (destinationColumnIndex == -1) {
                destinationColumnIndex = createColumn(destinationSheet, destinationColumnName);
            }

            if (sourceColumnIndex1 == -1 || sourceColumnIndex2 == -1) {
                throw new IllegalArgumentException("Given source column name not found in the source sheet.");
            }

            // Iterate through each row in the source sheet
            for (int i = 1; i <= sourceSheet.getLastRowNum(); i++) {
                Row sourceRow = sourceSheet.getRow(i);
                if (sourceRow == null) continue;

                Row destinationRow = destinationSheet.getRow(i);
                if (destinationRow == null) {
                    destinationRow = destinationSheet.createRow(i);
                }

                // Get values from the two columns
                Cell cell1 = sourceRow.getCell(sourceColumnIndex1);
                Cell cell2 = sourceRow.getCell(sourceColumnIndex2);

                if (cell1 == null || cell1.getCellType() == CellType.BLANK) {
                    System.out.println("Null or blank cell found at row " + (i + 1) + ", column " + sourceColumnIndex1);
                    continue;
                }
                if (cell2 == null || cell2.getCellType() == CellType.BLANK) {
                    System.out.println("Null or blank cell found at row " + (i + 1) + ", column " + sourceColumnIndex2);
                    continue;
                }

                String value1 = cell1.getStringCellValue();
                String value2 = cell2.getStringCellValue();

                // Split value1 and value2 into parts based on the delimiter ||
                String[] parts1 = value1.split("\\|\\|");
                String[] parts2 = value2.split("\\|\\|");

                // Create StringBuilder to accumulate concatenated values
                StringBuilder concatenatedValues = new StringBuilder();

                // Iterate over parts of value1 and value2 to form the correct path
                for (int j = 0; j < parts1.length; j++) {
                    String part1 = parts1[j].trim().replaceAll("[/&'’\\-\\s]", "");
                    part1 = removeSpaces(part1);

                    if (!lookupTable1.contains(part1)) {
                        System.out.println("No " + sourceColumnName1 + " match found at row " + (i + 1) + ": " + part1);
                    }

                    if (j < parts2.length) {
                        String part2 = parts2[j].trim().replaceAll("[/&'’\\-\\s]", "");
                        part2 = removeSpaces(part2);

                        if (!lookupTable2.contains(part2)) {
                            System.out.println("No " + sourceColumnName2 + " match found at row " + (i + 1) + ": " + part2);
                        }

                        if (concatenatedValues.length() > 0) {
                            concatenatedValues.append(";");
                        }

                        // Checking for the condition and append the value
                        if (destinationColumnName.equals("BrandSubBrandHierarchy") && part2.equals("NA")) {
                            concatenatedValues.append(appendStringValue)
                                    .append(part1)
                                    .append("/")
                                    .append(part1).append(part2);
                        } else {
                            concatenatedValues.append(appendStringValue)
                                    .append(part1)
                                    .append("/")
                                    .append(part2);
                        }
                    }
                }

                // Create new cell in destination sheet and set the concatenated value
                Cell destCell = destinationRow.createCell(destinationColumnIndex);
                destCell.setCellValue(concatenatedValues.toString());
            }

            // Write the destination workbook to a file
            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                workbook.write(outputStream);
                System.out.println("Pick and Concatenate --> LH Column " + sourceColumnName1 + " and " + sourceColumnName2 + " mapped successfully to Aprimo Column " + destinationColumnName);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) fis.close();
            if (workbook != null) workbook.close();
        }
    }

    public static void rearrangeColumns(String filePath, String sourceSheetName, String destinationSheetName, List<String> columnOrder) throws IOException {
        FileInputStream fis = null;
        FileOutputStream outputStream = null;
        try {
            fis = new FileInputStream(filePath);
            workbook = WorkbookFactory.create(fis);
            Sheet sourceSheet = workbook.getSheet(sourceSheetName);

            // Create a mapping of column names to their corresponding column indices in the source sheet
            Map<String, Integer> columnIndexMap = new HashMap<>();
            Row headerRow = sourceSheet.getRow(0);
            for (Cell cell : headerRow) {
                columnIndexMap.put(cell.getStringCellValue(), cell.getColumnIndex());
            }

            // Create a new sheet with the desired column order
            Sheet destinationSheet = workbook.createSheet(destinationSheetName);
            Row destinationHeaderRow = destinationSheet.createRow(0);

            // Copy header row with rearranged columns
            for (int i = 0; i < columnOrder.size(); i++) {
                String columnName = columnOrder.get(i);
                if (columnIndexMap.containsKey(columnName)) {
                    int sourceColumnIndex = columnIndexMap.get(columnName);
                    Cell sourceCell = headerRow.getCell(sourceColumnIndex);
                    Cell destinationCell = destinationHeaderRow.createCell(i);
                    destinationCell.setCellValue(sourceCell.getStringCellValue());
                }
            }

            // Copy data from source sheet to destination sheet
            for (int rowIndex = 1; rowIndex <= sourceSheet.getLastRowNum(); rowIndex++) {
                Row sourceRow = sourceSheet.getRow(rowIndex);
                Row destinationRow = destinationSheet.createRow(rowIndex);
                for (int i = 0; i < columnOrder.size(); i++) {
                    String columnName = columnOrder.get(i);
                    if (columnIndexMap.containsKey(columnName)) {
                        int sourceColumnIndex = columnIndexMap.get(columnName);
                        Cell sourceCell = sourceRow.getCell(sourceColumnIndex);
                        Cell destinationCell = destinationRow.createCell(i);
                        if (sourceCell != null) {
                            destinationCell.setCellValue(sourceCell.getStringCellValue());
                        }
                    }
                }
            }

            // Remove the original sheet and rename the new sheet
            workbook.removeSheetAt(workbook.getSheetIndex(sourceSheet));
            workbook.setSheetName(workbook.getSheetIndex(destinationSheet), sourceSheetName);

            // Write the updated workbook to a file
            outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);
            System.out.println("Columns rearranged successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            closeResources(fis, outputStream, workbook);
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

    public static String dateFormatter(String dateColumnValue, String columnName) throws ParseException {
        // US date formatter
        String targetFormatStr = "MM/dd/yyyy HH:mm";
        String simpleDateFormatStr = "MM/dd/yyyy";
        String originalFormatStr1 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        String originalFormatStr2 = "yyyy-MM-dd HH:mm:ss.SSS";

        DateFormat targetFormat = new SimpleDateFormat(targetFormatStr);
        DateFormat simpleDateFormat = new SimpleDateFormat(simpleDateFormatStr);
        String formattedDate = null;

        try {
            if (!dateColumnValue.isEmpty()) {
                Date date = null;
                boolean parsed = false;
                if (originalFormatStr1.equals("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")) {
                    DateFormat originalFormat1 = new SimpleDateFormat(originalFormatStr1);
                    try {
                        date = originalFormat1.parse(dateColumnValue);
                        parsed = true;
                    } catch (ParseException e) {
                        // Parsing failed, try the next format
                    }
                }
                if (!parsed && originalFormatStr2.equals("yyyy-MM-dd HH:mm:ss.SSS")) {
                    DateFormat originalFormat2 = new SimpleDateFormat(originalFormatStr2);
                    try {
                        date = originalFormat2.parse(dateColumnValue);
                        parsed = true;
                    } catch (ParseException e) {
                        // Parsing failed for both formats
                        throw new ParseException("Unparseable date: " + dateColumnValue, 0);
                    }
                }
                if (parsed) {
                    if ("AssetCreationDate".equals(columnName)) {
                        formattedDate = simpleDateFormat.format(date);
                    } else {
                        formattedDate = targetFormat.format(date);
                    }
                }
            }
        } catch (ParseException pe) {
            // If parsing fails, print the error and return null
            pe.printStackTrace();
        }
        return formattedDate;
    }

    public static Date stringToDateConversion(String formattedDate) {
        DateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        Date date = new Date();
        try {
            if(!formattedDate.isEmpty()){
                date = targetFormat.parse(formattedDate);
            }} catch (Exception pe) {
            System.out.println(pe);
        }
        return date;
    }
    public static void mapDateFields(String filePath, String sourceSheetName, String destinationSheetName, String sourceColumnName, String destinationColumnName, char deLimiter) {
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
            //Apply date transformation to each value
            for (int i = 1; i <= sourceSheet.getLastRowNum(); i++) {
                Row sourceRow = sourceSheet.getRow(i);
                if (sourceRow == null) continue;
                Row destinationRow = destinationSheet.getRow(i);
                if (destinationRow == null) {
                    destinationRow = destinationSheet.createRow(i);
                }
                Cell sourceCell = sourceRow.getCell(sourceColumnIndex);
                if (sourceCell != null) {
                    String sourceCellValue = sourceCell.getStringCellValue();
                    if (sourceCellValue != null && !sourceCellValue.isEmpty()) {
                        Set<String> uniqueValues = new HashSet<>();
                        StringBuilder concatenatedValue = new StringBuilder();
                        String[] values = sourceCellValue.split("\\|\\|"); // Split the cell value with "||" delimiter
                        for (String value : values) { //check for null
                            value = dateFormatter(value.trim(), destinationColumnName);
                            if (!uniqueValues.contains(value)) {
                                if (concatenatedValue.length() > 0) {
                                    concatenatedValue.append(deLimiter); // Adding delimiter "," between values
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
            // Write changes to the workbook
            outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);

            System.out.println("Dates Transformation applied in column " + sourceColumnName + " mapped successfully to " + destinationColumnName);
        } catch(IOException | IllegalArgumentException | IllegalStateException | ParseException ex){
            ex.printStackTrace();
        } finally{
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

    public static String getCurrentTimestamp() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        // Format the current date and time as a string
        String formattedTimestamp = currentDateTime.format(formatter);
        return formattedTimestamp;
    }


    public static void parseExponentialFields(String filePath, String sourceSheetName, String sourceColumnName, String destinationSheetName, String destinationColumnName) {
        FileInputStream fis = null;
        FileOutputStream outputStream = null;
        Workbook workbook = null;

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

            DecimalFormat decimalFormat = new DecimalFormat("#");

            for (int i = 1; i <= sourceSheet.getLastRowNum(); i++) {
                Row sourceRow = sourceSheet.getRow(i);
                if (sourceRow == null) continue;

                Row destinationRow = destinationSheet.getRow(i);
                if (destinationRow == null) {
                    destinationRow = destinationSheet.createRow(i);
                }

                Cell sourceCell = sourceRow.getCell(sourceColumnIndex);
                if (sourceCell != null) {
                    String formattedValue = null;

                    if (sourceCell.getCellType() == CellType.NUMERIC) {
                        double cellValue = sourceCell.getNumericCellValue();
                        String cellValueAsString = Double.toString(cellValue);

                        if (cellValueAsString.contains("E")) {
                            formattedValue = decimalFormat.format(cellValue);
                        } else {
                            formattedValue = cellValueAsString;
                        }
                    } else if (sourceCell.getCellType() == CellType.STRING) {
                        String cellValueAsString = sourceCell.getStringCellValue();
                        if (cellValueAsString.matches(".*E\\+.*")) {
                            try {
                                double numericValue = Double.parseDouble(cellValueAsString);
                                formattedValue = decimalFormat.format(numericValue);
                            } catch (NumberFormatException e) {
                                formattedValue = cellValueAsString;
                            }
                        } else {
                            formattedValue = cellValueAsString;
                        }
                    }

                    if (formattedValue != null) {
                        // Split the value by "||"
                        String[] parts = formattedValue.split("\\|\\|");

                        // Remove empty parts, trim each part, and join the remaining parts with ";"
                        formattedValue = String.join(";", Arrays.stream(parts)
                                .map(String::trim)  // Trim each part
                                .filter(part -> !part.isEmpty())  // Remove empty parts
                                .toArray(String[]::new));

                        // Remove trailing semicolons
                        if (formattedValue.endsWith(";")) {
                            formattedValue = formattedValue.substring(0, formattedValue.length() - 1);
                        }

                        Cell destinationCell = destinationRow.createCell(destinationColumnIndex);
                        destinationCell.setCellValue(formattedValue);
                    }
                }
            }

            outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);

            System.out.println("Parse Exponential Fields --> LH Column "+sourceColumnName+" mapped successfully to Aprimo Column "+ destinationColumnName);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) fis.close();
                if (outputStream != null) outputStream.close();
                if (workbook != null) workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void mapPoliciesAndPackageValues(String filePath, String sourceSheetName, String sourceColumnName, String destinationSheetName, String destinationColumnName) {
        FileInputStream fis = null;
        FileOutputStream outputStream = null;
        Workbook workbook = null;
        String[] PackagingValueNames = new String[] {"Evoke Group Asset", "Vidsy Asset","Elmwood Asset","Juice Asset"};
        List<String> Policylist = Arrays.asList(PackagingValueNames);
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

            for (int i = 1; i <= sourceSheet.getLastRowNum(); i++) {
                Row sourceRow = sourceSheet.getRow(i);
                if (sourceRow == null) continue;

                Row destinationRow = destinationSheet.getRow(i);
                if (destinationRow == null) {
                    destinationRow = destinationSheet.createRow(i);
                }

                Cell sourceCell = sourceRow.getCell(sourceColumnIndex);
                if (sourceCell != null) {
                    String sourceValue = sourceCell.getStringCellValue();
                    String destinationValue;
                    //Applies for all the policies for agency assets
                    if (sourceValue.contains("Packaging")) {
                        destinationValue = "Final Packaging";
                    } else if(Policylist.contains(sourceValue))
                    {
                        destinationValue = "InReview";
                    }else
                    {
                        destinationValue = "Reference";
                    }

                    Cell destinationCell = destinationRow.createCell(destinationColumnIndex);
                    destinationCell.setCellValue(destinationValue);
                }
            }

            outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);

            System.out.println("Mapping for "+ sourceColumnName+" completed successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) fis.close();
                if (outputStream != null) outputStream.close();
                if (workbook != null) workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void mapPoliciesAndVideoAssetsValues(String filePath, String sourceSheetName, String sourceColumnName, String destinationSheetName, String destinationColumnName) {
        FileInputStream fis = null;
        FileOutputStream outputStream = null;
        Workbook workbook = null;

        try {
            fis = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(fis);

            Sheet sourceSheet = workbook.getSheet(sourceSheetName);
            Sheet destinationSheet = workbook.getSheet(destinationSheetName);

            int sourceColumnIndex = getColumnIndex(sourceSheet, sourceColumnName);
            int destinationColumnIndex = getColumnIndex(destinationSheet, destinationColumnName);

            if (sourceColumnIndex == -1) {
                throw new IllegalArgumentException("Given source column name "+sourceColumnName+" not found in the source sheet.");
            }

            if (destinationColumnIndex == -1) {
                destinationColumnIndex = createColumn(destinationSheet, destinationColumnName);
            }

            for (int i = 1; i <= sourceSheet.getLastRowNum(); i++) {
                Row sourceRow = sourceSheet.getRow(i);
                if (sourceRow == null) continue;

                Row destinationRow = destinationSheet.getRow(i);
                if (destinationRow == null) {
                    destinationRow = destinationSheet.createRow(i);
                }

                Cell sourceCell = sourceRow.getCell(sourceColumnIndex);
                if (sourceCell != null) {
                    String sourceValue = sourceCell.getStringCellValue();
                    String destinationValue;

                    if ("Video Localization Assets".equals(sourceValue)) {
                        destinationValue = "Yes";
                    } else {
                        destinationValue = "No";
                    }

                    Cell destinationCell = destinationRow.createCell(destinationColumnIndex);
                    destinationCell.setCellValue(destinationValue);
                }
            }

            outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);

            System.out.println("Mapping for "+sourceColumnName+" completed successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) fis.close();
                if (outputStream != null) outputStream.close();
                if (workbook != null) workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void mapExportPathAndArchivedValues(String filePath, String sourceSheetName, String sourceColumnName1, String sourceColumnName2, String destinationSheetName, String destinationColumnName) {
        FileInputStream fis = null;
        FileOutputStream outputStream = null;
        Workbook workbook = null;

        try {
            fis = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(fis);

            Sheet sourceSheet = workbook.getSheet(sourceSheetName);
            Sheet destinationSheet = workbook.getSheet(destinationSheetName);

            int sourceColumn1Index = getColumnIndex(sourceSheet, sourceColumnName1);
            int sourceColumn2Index = getColumnIndex(sourceSheet, sourceColumnName2);
            int destinationColumnIndex = getColumnIndex(destinationSheet, destinationColumnName);

            if (sourceColumn1Index == -1) {
                throw new IllegalArgumentException("Given source column 1 name : "+sourceColumnName1+" not found in the source sheet.");
            }

            if (sourceColumn2Index == -1) {
                throw new IllegalArgumentException("Given source column 2 name: "+sourceColumnName2+" not found in the source sheet.");
            }

            if (destinationColumnIndex == -1) {
                destinationColumnIndex = createColumn(destinationSheet, destinationColumnName);
            }

            for (int i = 1; i <= sourceSheet.getLastRowNum(); i++) {
                Row sourceRow = sourceSheet.getRow(i);
                if (sourceRow == null) continue;

                Row destinationRow = destinationSheet.getRow(i);
                if (destinationRow == null) {
                    destinationRow = destinationSheet.createRow(i);
                }

                Cell sourceCell1 = sourceRow.getCell(sourceColumn1Index);
                Cell sourceCell2 = sourceRow.getCell(sourceColumn2Index);
                if (sourceCell1 != null) {
                    String sourceValue1 = sourceCell1.getStringCellValue();
                    String destinationValue;

                    if (sourceValue1.contains("_lhv1") || sourceValue1.contains("_lhv2")) {
                        destinationValue = "ARCHIVED";
                    } else {
                        String sourceValue2 = sourceCell2 != null ? sourceCell2.getStringCellValue() : "";

                        // Split the value by "||"
                        String[] parts = sourceValue2.split("\\|\\|");

                        // Join the parts with ";"
                        String joinedValue = String.join(";", parts);

                        // Check if the resulting value contains only "||" or "||||"
                        if (joinedValue.equals("||") || joinedValue.equals("||||")) {
                            destinationValue = ""; // Empty string
                        } else {
                            destinationValue = joinedValue; // Joined value
                        }
                    }

                    Cell destinationCell = destinationRow.createCell(destinationColumnIndex);
                    destinationCell.setCellValue(destinationValue);
                }
            }

            outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);

            System.out.println("Mapping for "+sourceColumnName2+" completed successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) fis.close();
                if (outputStream != null) outputStream.close();
                if (workbook != null) workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void mapStatusValues(String filePath, String sourceSheetName, String sourceColumnName, String destinationSheetName, String destinationColumnName) {
        FileInputStream fis = null;
        FileOutputStream outputStream = null;
        Workbook workbook = null;

        String[] targetValues = new String[] {"SOURCE", "REFERENCE", "FINAL PACKAGING"};
        List<String> targetList = Arrays.asList(targetValues);

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

            for (int i = 1; i <= sourceSheet.getLastRowNum(); i++) {
                Row sourceRow = sourceSheet.getRow(i);
                if (sourceRow == null) continue;

                Row destinationRow = destinationSheet.getRow(i);
                if (destinationRow == null) {
                    destinationRow = destinationSheet.createRow(i);
                }

                Cell sourceCell = sourceRow.getCell(sourceColumnIndex);
                if (sourceCell != null) {
                    String sourceValue = sourceCell.getStringCellValue().toUpperCase();
                    String destinationValue;

                    if (targetList.contains(sourceValue)) {
                        destinationValue = "Approved";
                    } else {
                        destinationValue = convertToLowercaseExceptFirst(sourceValue);
                    }

                    Cell destinationCell = destinationRow.createCell(destinationColumnIndex);
                    destinationCell.setCellValue(destinationValue);
                }
            }

            outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);

            System.out.println("Mapping for " + sourceColumnName + " completed successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) fis.close();
                if (outputStream != null) outputStream.close();
                if (workbook != null) workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void concatenateAndMapValues(String filePath, String sourceSheetName, String sourceColumnName1, String sourceColumnName2, String destinationSheetName, String destinationColumnName) {
        FileInputStream fis = null;
        FileOutputStream outputStream = null;
        Workbook workbook = null;

        try {
            fis = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(fis);

            Sheet sourceSheet = workbook.getSheet(sourceSheetName);
            Sheet destinationSheet = workbook.getSheet(destinationSheetName);

            int sourceColumnIndex1 = getColumnIndex(sourceSheet, sourceColumnName1);
            int sourceColumnIndex2 = getColumnIndex(sourceSheet, sourceColumnName2);
            int destinationColumnIndex = getColumnIndex(destinationSheet, destinationColumnName);

            if (sourceColumnIndex1 == -1 || sourceColumnIndex2 == -1) {
                throw new IllegalArgumentException("Given source column name not found in the source sheet.");
            }

            if (destinationColumnIndex == -1) {
                destinationColumnIndex = createColumn(destinationSheet, destinationColumnName);
            }

            for (int i = 1; i <= sourceSheet.getLastRowNum(); i++) {
                Row sourceRow = sourceSheet.getRow(i);
                if (sourceRow == null) continue;

                Row destinationRow = destinationSheet.getRow(i);
                if (destinationRow == null) {
                    destinationRow = destinationSheet.createRow(i);
                }

                Cell sourceCell1 = sourceRow.getCell(sourceColumnIndex1);
                Cell sourceCell2 = sourceRow.getCell(sourceColumnIndex2);
                String value1 = (sourceCell1 != null) ? sourceCell1.getStringCellValue() : "";
                String value2 = (sourceCell2 != null) ? sourceCell2.getStringCellValue() : "";

                String concatenatedValue;
                if (value1.isEmpty()) {
                    concatenatedValue = value2;
                } else {
                    concatenatedValue = value1 + " " + value2;
                }

                Cell destinationCell = destinationRow.createCell(destinationColumnIndex);
                destinationCell.setCellValue(concatenatedValue);
            }

            outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);

            System.out.println("Concatenation and mapping for " + sourceColumnName1 + " and " + sourceColumnName2 + " completed successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) fis.close();
                if (outputStream != null) outputStream.close();
                if (workbook != null) workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void parseAndMapProductCategories(String filePath, String sourceSheetName, String sourceColumnName, String destinationSheetName, String destinationColumnName) {

        // Create the ArrayList with given product categories
        List<String> productCategories = new ArrayList<>(Arrays.asList(
                "CatTreats", "DogTreats", "DryCat", "DryDog", "Ferret",
                "WetCat", "WetDog", "Bird", "Fish", "Litter"
        ));

        FileInputStream fis = null;
        FileOutputStream outputStream = null;
        Workbook workbook = null;

        try {
            fis = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(fis);

            Sheet sourceSheet = workbook.getSheet(sourceSheetName);
            Sheet destinationSheet = workbook.getSheet(destinationSheetName);

            int sourceColumnIndex = getColumnIndex(sourceSheet, sourceColumnName);
            int destinationColumnIndex = getColumnIndex(destinationSheet, destinationColumnName);

            if (sourceColumnIndex == -1) {
                throw new IllegalArgumentException("Given source column name " + sourceColumnName + " not found in the source sheet.");
            }

            if (destinationColumnIndex == -1) {
                destinationColumnIndex = createColumn(destinationSheet, destinationColumnName);
            }

            for (int i = 1; i <= sourceSheet.getLastRowNum(); i++) {
                Row sourceRow = sourceSheet.getRow(i);
                if (sourceRow == null) continue;

                Row destinationRow = destinationSheet.getRow(i);
                if (destinationRow == null) {
                    destinationRow = destinationSheet.createRow(i);
                }
                Cell sourceCell = sourceRow.getCell(sourceColumnIndex);
                Cell destinationCell = destinationRow.createCell(destinationColumnIndex);

                if (sourceCell != null) {
                    String sourceValue = sourceCell.getStringCellValue();
                    if (sourceValue.isEmpty()) {
                        destinationCell.setCellValue("");
                    } else {
                        String[] splitValues = sourceValue.split("\\|\\|");
                        boolean allMatched = true;
                        StringBuilder formattedValue = new StringBuilder();

                        for (String value : splitValues) {
                            if (!value.isEmpty()) {
                                value = removeSpaces(value.trim().replace(" ", ""));
                                if (!productCategories.contains(value)) {
                                    System.out.println("No Product Category match found at row " + (i + 1) + ": " + value);
//                                    allMatched = false;
//                                    break;
                                } else {
                                    if (formattedValue.length() > 0) {
                                        formattedValue.append(";");
                                    }
                                    formattedValue.append(value);
                                }
                            }
                        }

                        if (allMatched) {
                            destinationCell.setCellValue(formattedValue.toString());
                        } else {
                            destinationCell.setCellValue("");
                        }
                    }
                } else {
                    destinationCell.setCellValue("");
                }
            }

            outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);

            System.out.println("Mapping for " + sourceColumnName + " completed successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) fis.close();
                if (outputStream != null) outputStream.close();
                if (workbook != null) workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    public static void parseAndLookup(String filePath, String sourceSheetName, String sourceColumnName, String sourceColumnName2, String destinationSheetName, String destinationColumnName, List<String> LookUpTable) {
        FileInputStream fis = null;
        FileOutputStream outputStream = null;
        Workbook workbook = null;

        try {
            fis = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(fis);

            Sheet sourceSheet = workbook.getSheet(sourceSheetName);
            if (sourceSheet == null) {
                throw new IllegalArgumentException("Source sheet " + sourceSheetName + " does not exist.");
            }

            Sheet destinationSheet = workbook.getSheet(destinationSheetName);
            if (destinationSheet == null) {
                destinationSheet = workbook.createSheet(destinationSheetName);
            }

            int sourceColumnIndex = getColumnIndex(sourceSheet, sourceColumnName);
            int sourceColumnIndex2 = getColumnIndex(sourceSheet, sourceColumnName2);
            int destinationColumnIndex = getColumnIndex(destinationSheet, destinationColumnName);
            int statusColumnIndex = getColumnIndex(destinationSheet, "TransformationStatus");


            if (sourceColumnIndex == -1) {
                throw new IllegalArgumentException("Given source column name " + sourceColumnName + " not found in the source sheet.");
            }

            if (sourceColumnIndex2 == -1) {
                throw new IllegalArgumentException("Given additional source column name " + sourceColumnName2 + " not found in the source sheet.");
            }

            if (destinationColumnIndex == -1) {
                destinationColumnIndex = createColumn(destinationSheet, destinationColumnName);
            }

            if (statusColumnIndex == -1) {
                statusColumnIndex = createColumn(destinationSheet, "TransformationStatus");
            }

            // Creating the lookup replacements map
            HashMap<String, String> lookupMap = new HashMap<>();
            lookupMap.put("ImageRights", "Image Rights");
            lookupMap.put("MusicLicense", "Music License");
            lookupMap.put("OnCameraTalentModel", "On-Camera Talent/Model");
            lookupMap.put("Infeed", "InFeed");
            lookupMap.put("GK", "GKGraphics");
            lookupMap.put("ThisisPegasus", "ThisIsPegasus");
            lookupMap.put("SGSUSA", "SGSUS");
            lookupMap.put("TheAndPartnership", "TheandPartnership");
            lookupMap.put("MediaCom", "EssenceMediaCom");
            lookupMap.put("POS", "POSCollateral");
            lookupMap.put("eCommerceEnhancedContent", "ECommerceEnhancedContent");

            // Process rows and update transformationStatusMap
            for (int i = 1; i <= sourceSheet.getLastRowNum(); i++) {
                Row sourceRow = sourceSheet.getRow(i);
                if (sourceRow == null) continue;

                Row destinationRow = destinationSheet.getRow(i);
                if (destinationRow == null) {
                    destinationRow = destinationSheet.createRow(i);
                }
                Cell sourceCell = sourceRow.getCell(sourceColumnIndex);
                Cell sourceCell2 = sourceRow.getCell(sourceColumnIndex2);
                String sourceColumn2Value = getCellValueAsString(sourceCell2);
                Cell statusCell = destinationRow.getCell(statusColumnIndex);
                if (statusCell == null) {
                    statusCell = destinationRow.createCell(statusColumnIndex);
                }

                //System.out.println("\nProcessing Row: " + (i + 1));

                if (sourceCell != null) {
                    String sourceValue = getCellValueAsString(sourceCell);
                    //System.out.println("Source Value: " + sourceValue);

                    Cell destinationCell = destinationRow.createCell(destinationColumnIndex);
                    if (sourceValue.isEmpty()) {
                        destinationCell.setCellValue("");
                    } else {
                        String[] splitValues = sourceValue.split("\\|\\|");
                        boolean allMatched = true;
                        StringBuilder formattedValue = new StringBuilder();
                        StringBuilder failureMessages = new StringBuilder();

                        for (String value : splitValues) {
                            if (!value.isEmpty()) {
                                String cleanedValue = removeSpacesAndSpecialChars(value.trim().replaceAll("N/A", "NA"));
                                cleanedValue = replaceLookupValues(cleanedValue, lookupMap);
                                if (!LookUpTable.contains(cleanedValue)) {
                                    allMatched = false; // Ensure allMatched is set to false on failure
                                    String failureMessage = "No " + destinationColumnName + " match found at row " + (i + 1) + ": " + cleanedValue + " with " + sourceColumnName2 + " value: " + sourceColumn2Value;
                                    if (failureMessages.length() > 0) {
                                        failureMessages.append("; ");
                                    }
                                    failureMessages.append(failureMessage);
                                    System.out.println(failureMessage);
                                    // Set the destination cell value to the cleaned value
                                    if (formattedValue.length() > 0) {
                                        formattedValue.append("; ");
                                    }
                                    formattedValue.append(cleanedValue); // Append the cleaned value
                                } else {
                                    if (formattedValue.length() > 0) {
                                        formattedValue.append("; ");
                                    }
                                    formattedValue.append(cleanedValue);
                                }
                            }
                        }
                        destinationCell.setCellValue(formattedValue.toString().replaceAll("\\|\\|", "; "));
                        if (allMatched) {
                            //System.out.println("Formatted Value (Success): " + formattedValue.toString().replaceAll("\\|\\|", "; "));
                            updateTransformationStatus(i, "Success");
                        } else {
                            updateTransformationStatus(i, failureMessages.toString());
                        }
                    }
                } else {
                    Cell destinationCell = destinationRow.createCell(destinationColumnIndex);
                    destinationCell.setCellValue("");
                }
            }

            // Write the status column based on accumulated transformationStatusMap
            System.out.println("\nUpdating Transformation Status column...");
            for (Map.Entry<Integer, String> entry : transformationStatusMap.entrySet()) {
                int rowIndex = entry.getKey();
                String status = entry.getValue();

                Row row = destinationSheet.getRow(rowIndex);
                if (row == null) {
                    row = destinationSheet.createRow(rowIndex);
                }
                Cell statusCell = row.getCell(statusColumnIndex);
                if (statusCell == null) {
                    statusCell = row.createCell(statusColumnIndex);
                }
                statusCell.setCellValue(status);

                //System.out.println("Row " + (rowIndex + 1) + " Status: " + status);
            }

            outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);

            System.out.println("\nColumn " + destinationColumnName + " Mapping completed successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) fis.close();
                if (outputStream != null) outputStream.close();
                if (workbook != null) workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String replaceLookupValues(String value, HashMap<String, String> lookupMap) {
        return lookupMap.getOrDefault(value, value);
    }

    private static void updateTransformationStatus(int rowIndex, String status) {
        String existingStatus = transformationStatusMap.get(rowIndex);
        if (existingStatus == null) {
            transformationStatusMap.put(rowIndex, status);
        } else {
            if (status.equals("Success")) {
                // Only one "Success" should be stored if all statuses are successes
                if (!existingStatus.contains("No ")) {
                    transformationStatusMap.put(rowIndex, "Success");
                }
            } else {
                // Append new failure messages separated by ";"
                if (!existingStatus.startsWith("Success")) {
                    transformationStatusMap.put(rowIndex, existingStatus + "; " + status);
                } else {
                    transformationStatusMap.put(rowIndex, status);
                }
            }
        }
    }


//    public static void writeTransformationStatus(String filePath, String sheetName) {
//        FileInputStream fis = null;
//        FileOutputStream outputStream = null;
//        Workbook workbook = null;
//
//        try {
//            fis = new FileInputStream(filePath);
//            workbook = new XSSFWorkbook(fis);
//
//            Sheet sheet = workbook.getSheet(sheetName);
//            int statusColumnIndex = getColumnIndex(sheet, "TransformationStatus");
//
//            if (statusColumnIndex == -1) {
//                statusColumnIndex = createColumn(sheet, "TransformationStatus");
//            }
//
//            for (Map.Entry<Integer, StringBuilder> entry : transformationStatusMap.entrySet()) {
//                int rowIndex = entry.getKey();
//                String status = entry.getValue().toString();
//
//                Row row = sheet.getRow(rowIndex);
//                if (row == null) {
//                    row = sheet.createRow(rowIndex);
//                }
//                Cell statusCell = row.getCell(statusColumnIndex);
//                if (statusCell == null) {
//                    statusCell = row.createCell(statusColumnIndex);
//                }
//                statusCell.setCellValue(status);
//            }
//
//            outputStream = new FileOutputStream(filePath);
//            workbook.write(outputStream);
//
//            System.out.println("TransformationStatus column updated successfully.");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (fis != null) fis.close();
//                if (outputStream != null) outputStream.close();
//                if (workbook != null) workbook.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public static void mapAssetTypeToAssetSubType(String filePath, String sourceSheetName, String sourceColumnName, String destinationSheetName, String destinationColumnName) {
        FileInputStream fis = null;
        FileOutputStream outputStream = null;
        Workbook workbook = null;

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

            for (int i = 1; i <= sourceSheet.getLastRowNum(); i++) {
                Row sourceRow = sourceSheet.getRow(i);
                if (sourceRow == null) continue;

                Row destinationRow = destinationSheet.getRow(i);
                if (destinationRow == null) {
                    destinationRow = destinationSheet.createRow(i);
                }

                Cell sourceCell = sourceRow.getCell(sourceColumnIndex);
                if (sourceCell != null) {
                    String sourceValue = sourceCell.getStringCellValue();
                    String destinationValue;

                    if (sourceValue.equals("eCommerce Enhanced Content")) {
                        destinationValue = "A+ Content";
                    } else {
                        destinationValue = sourceValue;
                    }

                    Cell destinationCell = destinationRow.createCell(destinationColumnIndex);
                    destinationCell.setCellValue(destinationValue);
                }
            }

            outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);

            System.out.println("AssetType To AssetSubType Mapping completed successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) fis.close();
                if (outputStream != null) outputStream.close();
                if (workbook != null) workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void pickAndConcatenateAssets(String filePath, String sourceSheetName, String destinationSheetName,
                                                String sourceColumnName1, String sourceColumnName2, String sourceColumnName3,
                                                String sourceColumnName4, String destinationColumnName, String appendStringValue) throws IOException {
        FileInputStream fis = null;
        FileOutputStream outputStream = null;
        Workbook workbook = null;

        try {
            fis = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(fis);

            Sheet sourceSheet = workbook.getSheet(sourceSheetName);
            Sheet destinationSheet = workbook.getSheet(destinationSheetName);

            // Get column indices by column names
            int sourceColumnIndex1 = getColumnIndex(sourceSheet, sourceColumnName1);
            int sourceColumnIndex2 = getColumnIndex(sourceSheet, sourceColumnName2);
            int sourceColumnIndex3 = getColumnIndex(sourceSheet, sourceColumnName3);
            int sourceColumnIndex4 = getColumnIndex(sourceSheet, sourceColumnName4);
            int destinationColumnIndex = getColumnIndex(destinationSheet, destinationColumnName);

            if (destinationColumnIndex == -1) {
                destinationColumnIndex = createColumn(destinationSheet, destinationColumnName);
            }

            if (sourceColumnIndex1 == -1 || sourceColumnIndex2 == -1 || sourceColumnIndex3 == -1 || sourceColumnIndex4 == -1) {
                throw new IllegalArgumentException("Given source column name not found in the source sheet.");
            }

            // Iterate through each row in the source sheet
            for (int i = 1; i <= sourceSheet.getLastRowNum(); i++) {
                Row sourceRow = sourceSheet.getRow(i);
                if (sourceRow == null) continue;

                Row destinationRow = destinationSheet.getRow(i);
                if (destinationRow == null) {
                    destinationRow = destinationSheet.createRow(i);
                }

                // Get values from the columns
                String value1 = sourceRow.getCell(sourceColumnIndex1).getStringCellValue();
                String value2 = sourceRow.getCell(sourceColumnIndex2).getStringCellValue();
                String value3 = sourceRow.getCell(sourceColumnIndex3).getStringCellValue();
                String value4 = sourceRow.getCell(sourceColumnIndex4).getStringCellValue();

                // Check if value2 and value3 are the same and log the information
                String concatenatedValue;
                if (value2.equals(value3)) {
                    concatenatedValue = String.format("%s/%s/%s", appendStringValue, value1, value2);
                    System.out.println("Duplicate value found: " + value2 + " at " + sourceColumnName2 + " and " + sourceColumnName3 + " in row " + (i + 1) +
                            " with " + sourceColumnName4 + " value: " + value4);
                } else {
                    concatenatedValue = String.format("%s/%s/%s/%s", appendStringValue, value1, value2, value3);
                }

                // Create new cell in destination sheet and set the concatenated value
                Cell destCell = destinationRow.createCell(destinationColumnIndex);
                destCell.setCellValue(concatenatedValue);
            }

            // Write the destination workbook to a file
            outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);

            System.out.println("Pick and Concatenate --> Columns " + sourceColumnName1 + ", " + sourceColumnName2 + ", " + sourceColumnName3 + ", and " + sourceColumnName4 + " mapped successfully to column " + destinationColumnName);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) fis.close();
            if (outputStream != null) outputStream.close();
            if (workbook != null) workbook.close();
        }
    }

    public static void pickAndConcatenateAssociatedAssets(String filePath, String sourceSheetName, String destinationSheetName,
                                                String[] sourceColumnNames, String destinationColumnName) throws IOException {
        FileInputStream fis = null;
        FileOutputStream outputStream = null;
        Workbook workbook = null;

        try {
            fis = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(fis);

            Sheet sourceSheet = workbook.getSheet(sourceSheetName);
            Sheet destinationSheet = workbook.getSheet(destinationSheetName);

            // Get column indices by column names
            int[] sourceColumnIndices = new int[sourceColumnNames.length];
            for (int i = 0; i < sourceColumnNames.length; i++) {
                sourceColumnIndices[i] = getColumnIndex(sourceSheet, sourceColumnNames[i]);
                if (sourceColumnIndices[i] == -1) {
                    throw new IllegalArgumentException("Given source column name " + sourceColumnNames[i] + " not found in the source sheet.");
                }
            }
            int destinationColumnIndex = getColumnIndex(destinationSheet, destinationColumnName);
            if (destinationColumnIndex == -1) {
                destinationColumnIndex = createColumn(destinationSheet, destinationColumnName);
            }

            // Iterate through each row in the source sheet
            for (int i = 1; i <= sourceSheet.getLastRowNum(); i++) {
                Row sourceRow = sourceSheet.getRow(i);
                if (sourceRow == null) continue;

                Row destinationRow = destinationSheet.getRow(i);
                if (destinationRow == null) {
                    destinationRow = destinationSheet.createRow(i);
                }

                StringBuilder concatenatedValue = new StringBuilder();
                for (int sourceColumnIndex : sourceColumnIndices) {
                    Cell cell = sourceRow.getCell(sourceColumnIndex);
                    if (cell != null) {
                        String cellValue = cell.getStringCellValue().replaceAll(":N|:Y", "");
                        if (concatenatedValue.length() > 0) {
                            concatenatedValue.append(";");
                        }
                        concatenatedValue.append(cellValue);
                    }
                }

                Cell destCell = destinationRow.createCell(destinationColumnIndex);
                destCell.setCellValue(concatenatedValue.toString());
            }

            // Write the destination workbook to a file
            outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);

            System.out.println("Pick and Concatenate --> Columns mapped successfully to column " + destinationColumnName);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) fis.close();
            if (outputStream != null) outputStream.close();
            if (workbook != null) workbook.close();
        }
    }

    public static void findAndPrintDuplicates(String filePath, String sourceSheetName, String sourceColumnName, String sourceColumnName2) {
        FileInputStream fis = null;
        FileOutputStream outputStream = null;
        Workbook workbook = null;

        try {
            fis = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(fis);

            Sheet sourceSheet = workbook.getSheet(sourceSheetName);

            int sourceColumnIndex = getColumnIndex(sourceSheet, sourceColumnName);
            int sourceColumn2Index = getColumnIndex(sourceSheet, sourceColumnName2);

            if (sourceColumnIndex == -1) {
                throw new IllegalArgumentException("Given source column name " + sourceColumnName + " not found in the source sheet.");
            }

            if (sourceColumn2Index == -1) {
                throw new IllegalArgumentException("Given source column name " + sourceColumnName2 + " not found in the source sheet.");
            }

            Set<String> uniqueValues = new HashSet<>();
            Set<String> duplicateValues = new HashSet<>();

            for (int i = 1; i <= sourceSheet.getLastRowNum(); i++) {
                Row sourceRow = sourceSheet.getRow(i);
                if (sourceRow == null) continue;

                Cell sourceCell = sourceRow.getCell(sourceColumnIndex);
                if (sourceCell != null) {
                    String sourceValue = sourceCell.getStringCellValue();
                    if (!uniqueValues.add(sourceValue)) {
                        duplicateValues.add(sourceValue);
                    }
                }
            }

            for (int i = 1; i <= sourceSheet.getLastRowNum(); i++) {
                Row sourceRow = sourceSheet.getRow(i);
                if (sourceRow == null) continue;

                Cell sourceCell = sourceRow.getCell(sourceColumnIndex);
                Cell sourceColumn2Cell = sourceRow.getCell(sourceColumn2Index);

                if (sourceCell != null) {
                    String sourceValue = sourceCell.getStringCellValue();
                    if (duplicateValues.contains(sourceValue)) {
                        String sourceColumn2Value = sourceColumn2Cell != null ? sourceColumn2Cell.getStringCellValue() : "N/A";
                        System.out.println("Duplicate value found: " + sourceValue + " at row " + (i + 1) + " with " + sourceColumnName2 + " value: " + sourceColumn2Value);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) fis.close();
                if (workbook != null) workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * This method removes extra spaces between words in a given string.
     *
     * @param input The input string to be cleaned.
     * @return A string with no more than one space between any two words.
     */
    public static String removeExtraSpaces(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        // Use a regular expression to replace multiple spaces with a single space
        String cleanedString = input.replaceAll("\\s{2,}", " ");

        return cleanedString;
    }
    // Method to remove spaces from a string
    private static String removeSpaces(String input) {
        return input.replaceAll("\\s+", "");
    }
    private static String removeSpacesAndSpecialChars(String input) {
        return input.replaceAll("[ &()+,/:'’\\-]", "");
    }

    public static String convertToLowercaseExceptFirst(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        // Split the string into the first character and the rest
        char firstChar = str.charAt(0);
        String restOfString = str.substring(1).toLowerCase();

        // Concatenate the first character and the rest of the string
        return firstChar + restOfString;
    }
    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }

        if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        }

        return cell.getStringCellValue();
    }
}


