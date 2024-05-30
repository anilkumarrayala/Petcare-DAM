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
            Row destinationRow = destinationSheet.getRow(i);
            if (destinationRow == null) {
                destinationRow = destinationSheet.createRow(i);
            }
            Cell sourceCell = sourceRow.getCell(sourceColumnIndex);
            Cell destinationCell = destinationRow.createCell(destinationColumnIndex);
            if (sourceCell != null) {
                String destinationCellValue = sourceCell.getStringCellValue().trim();
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
        try {
            FileInputStream fis = new FileInputStream(filePath);
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
                String value1 = sourceRow.getCell(sourceColumnIndex1).getStringCellValue();
                String value2 = sourceRow.getCell(sourceColumnIndex2).getStringCellValue();

                // Split value1 and value2 into parts based on the delimiter ||
                String[] parts1 = value1.split("\\|\\|");
                String[] parts2 = value2.split("\\|\\|");

                // Eliminate duplicate values in parts1 and parts2
                parts1 = Arrays.stream(parts1).distinct().toArray(String[]::new);
                parts2 = Arrays.stream(parts2).distinct().toArray(String[]::new);

                // Create StringBuilder to accumulate concatenated values
                StringBuilder concatenatedValues = new StringBuilder();

                boolean allMatched = true; // To track if all values match the lookup tables

                // Iterate over parts of value1 and value2 to form the correct path
                for (String part1 : parts1) {
                    part1 = removeSpaces(part1.trim().replace("N/A", "NA"));

                    if (!lookupTable1.contains(part1)) {
                        System.out.println("No " + sourceColumnName1 + " match found at row " + (i + 1) + ": " + part1);
                        allMatched = false;
                    }

                    for (String part2 : parts2) {
                        part2 = removeSpaces(part2.trim().replace("N/A", "NA"));

                        if (!lookupTable2.contains(part2)) {
                            System.out.println("No " + sourceColumnName2 + " match found at row " + (i + 1) + ": " + part2);
                            allMatched = false;
                        }

                        // Append concatenated value to StringBuilder
                        concatenatedValues.append(appendStringValue)
                                .append(part1)
                                .append("/")
                                .append(part1)
                                .append(part2)
                                .append(";");
                    }
                }

                if (allMatched) {
                    // Remove the last ';' if present
                    if (concatenatedValues.length() > 0 && concatenatedValues.charAt(concatenatedValues.length() - 1) == ';') {
                        concatenatedValues.setLength(concatenatedValues.length() - 1);
                    }

                    // Create new cell in destination sheet and set the concatenated value
                    Cell destCell = destinationRow.createCell(destinationColumnIndex);
                    destCell.setCellValue(concatenatedValues.toString());
                }
            }

            // Write the destination workbook to a file
            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                workbook.write(outputStream);
                System.out.println("Pick and Concatenate --> LH Column " + sourceColumnName1 + " and " + sourceColumnName2 + " mapped successfully to Aprimo Column " + destinationColumnName);
            }

            // Close resources
            fis.close();
            workbook.close();

        } catch (IOException e) {
            e.printStackTrace();
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

    public static String dateFormatter(String dateColumnValue) throws ParseException {
       //US date formatter
        String targetFormatStr = "MM/dd/yyyy HH:mm:ss a";
        String originalFormatStr1 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        String originalFormatStr2 = "yyyy-MM-dd HH:mm:ss.SSS";
        DateFormat targetFormat = new SimpleDateFormat(targetFormatStr);
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
                    formattedDate = targetFormat.format(date);
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
                                value = dateFormatter(value.trim());
                                if (!uniqueValues.contains(value)) {
                                    if (concatenatedValue.length() > 0) {
                                        concatenatedValue.append(deLimiter); // Adding delimiter "," between values
                                    }
                                    concatenatedValue.append(value);
                                    uniqueValues.add(value);
                                }
                            }
                        Cell destinationCell = destinationRow.createCell(destinationColumnIndex);
                        //destinationCell.setCellValue(concatenatedValue.toString());
                        destinationCell.setCellValue(concatenatedValue.toString());
                    }
                }
            }
            // Write changes to the workbook
            outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);

            System.out.println("Dates Tranformation applied in column " + sourceColumnName + "mapped successfully to " + destinationColumnName);
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

                        if (parts.length > 1) {
                            // Join the parts with ";"
                            formattedValue = String.join(";", parts);
                        }

                        // Replace any remaining "||" with ";"
                        formattedValue = formattedValue.replace("||", ";");

                        Cell destinationCell = destinationRow.createCell(destinationColumnIndex);
                        destinationCell.setCellValue(formattedValue);
                    }
                }
            }

            outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);

            System.out.println("Formatting completed successfully.");

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
                    //Applies for two values - Mars Inc. Final Packaging Asset / Mars Inc. Final Packaging Mechanical Artwork Asset
                    if (sourceValue.contains("Packaging")) {
                        destinationValue = "Final Packaging";
                    } else if(Policylist.contains(sourceValue))
                    {
                        destinationValue = "Draft";
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

    public static void parseAndMapProductCategories(String filePath, String sourceSheetName, String sourceColumnName, String destinationSheetName, String destinationColumnName) {

        // Create the ArrayList with given product categories
        List<String> productCategories = new ArrayList<>(Arrays.asList(
                "Cat Treats", "Dog Treats", "Dry Cat", "Dry Dog", "Ferret",
                "Wet Cat", "Wet Dog", "Bird", "Fish", "Litter"
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
                    if (sourceValue.isEmpty()) {
                        Cell destinationCell = destinationRow.createCell(destinationColumnIndex);
                        destinationCell.setCellValue("");
                    } else {
                        String[] splitValues = sourceValue.split("\\|\\|");
                        boolean allMatched = true;
                        for (String value : splitValues) {
                            if (!value.isEmpty()) {
                                value = removeExtraSpaces(value.trim());
                                if (!productCategories.contains(value)) {
                                    System.out.println("No Product Category match found at row " + (i + 1) + ": " + value);
                                    allMatched = false;
                                }
                            }
                            Cell destinationCell = destinationRow.createCell(destinationColumnIndex);
                            if (allMatched) {
                                // Join the parts with ";" and replace any remaining "||" with ";"
                                String formattedValue = String.join(";", splitValues).replace("||", ";");
                                destinationCell.setCellValue(formattedValue);
                            } else {
                                destinationCell.setCellValue("");
                            }
                        }
                    }
                } else {
                    // If the source cell is null, set the destination cell to empty as well
                    Cell destinationCell = destinationRow.createCell(destinationColumnIndex);
                    destinationCell.setCellValue("");
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

    public static void parseAndLookup(String filePath, String sourceSheetName, String sourceColumnName, String destinationSheetName, String destinationColumnName, List<String> LookUpTable) {

        // Create the ArrayList with given flavor names


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
                throw new IllegalArgumentException("Given source column name "+sourceColumnName+"not found in the source sheet.");
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
                    if (sourceValue.isEmpty()) {
                        Cell destinationCell = destinationRow.createCell(destinationColumnIndex);
                        destinationCell.setCellValue("");
                    } else {
                        String[] splitValues = sourceValue.split("\\|\\|");
                        boolean allMatched = true;
                        for (String value : splitValues) {
                            if (!value.isEmpty()) {
                                value = removeExtraSpaces(value.trim());
                                if (!LookUpTable.contains(value)) {
                                    System.out.println("No "+destinationColumnName+" match found at row " + (i + 1) + ": " + value);
                                    allMatched = false;
                                }
                            }
                            Cell destinationCell = destinationRow.createCell(destinationColumnIndex);
                            if (allMatched) {
                                // Join the parts with ";" and replace any remaining "||" with ";"
                                String formattedValue = String.join(";", splitValues).replace("||", ";");
                                destinationCell.setCellValue(formattedValue);
                            } else {
                                destinationCell.setCellValue("");
                            }
                        }
                    }
                } else {
                    // If the source cell is null, set the destination cell to empty as well
                    Cell destinationCell = destinationRow.createCell(destinationColumnIndex);
                    destinationCell.setCellValue("");
                }
            }

            outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);

            System.out.println("Column "+destinationColumnName+" Mapping completed successfully.");

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

    public static void parseAndLookup1(String filePath, String sourceSheetName, String sourceColumnName, String destinationSheetName, String destinationColumnName, List<String> LookUpTable) {

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
                if (sourceCell != null) {
                    String sourceValue = sourceCell.getStringCellValue();
                    Cell destinationCell = destinationRow.createCell(destinationColumnIndex);
                    if (sourceValue.isEmpty()) {
                        destinationCell.setCellValue("");
                    } else {
                        String[] splitValues = sourceValue.split("\\|\\|");
                        boolean allMatched = true;
                        StringBuilder formattedValue = new StringBuilder();

                        for (String value : splitValues) {
                            if (!value.isEmpty()) {
                                String cleanedValue = removeSpaces(value.trim().replace("N/A", "NA"));
                                if (!LookUpTable.contains(cleanedValue)) {
                                    System.out.println("No " + destinationColumnName + " match found at row " + (i + 1) + ": " + cleanedValue);
                                    allMatched = false;
                                }
                                if (formattedValue.length() > 0) {
                                    formattedValue.append(";");
                                }
                                formattedValue.append(cleanedValue);
                            }
                        }
                        if (allMatched) {
                            destinationCell.setCellValue(formattedValue.toString().replaceAll("\\|\\|", ";"));
                        } else {
                            destinationCell.setCellValue("");
                        }
                    }
                } else {
                    Cell destinationCell = destinationRow.createCell(destinationColumnIndex);
                    destinationCell.setCellValue("");
                }
            }

            outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);

            System.out.println("Column " + destinationColumnName + " Mapping completed successfully.");

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
}


