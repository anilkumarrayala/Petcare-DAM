package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
                                          String sourceColumnName1, String sourceColumnName2, String destinationColumnName, char deLimiter, String appendStringValue) throws IOException {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            workbook = WorkbookFactory.create(fis);

            Sheet sourceSheet = workbook.getSheet(sourceSheetName);
            Sheet destinationSheet = workbook.getSheet(destinationSheetName);

            // Get column indices by column names
            int sourceColumnIndex1 = getColumnIndex(sourceSheet, sourceColumnName1);
            int sourceColumnIndex2 = getColumnIndex(sourceSheet, sourceColumnName2);

            int destinationColumnIndex = createColumn(destinationSheet, destinationColumnName);

            if (sourceColumnIndex1 == -1) {
                throw new IllegalArgumentException("Given source column name not found in the source sheet.");
            }

            if (sourceColumnIndex2 == -1) {
                throw new IllegalArgumentException("Given source column name not found in the source sheet.");
            }

            // Iterate through each row in the source sheet
            for (int i = 1; i <= sourceSheet.getLastRowNum(); i++) {
                Row sourceRow = sourceSheet.getRow(i);
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

                // Create StringBuilder to accumulate concatenated values
                StringBuilder concatenatedValues = new StringBuilder();

                // Iterate over parts of value1 and value2
                for (int j = 0; j < Math.min(parts1.length, parts2.length); j++)
                    // Append concatenated value to StringBuilder
                    {
                        //Concat region and marketing Country field with formula "/DAM/MarketingRegionMarketingCountry/North America/United States (formula)"
                    if (appendStringValue.contains("MarketingRegionMarketingCountry")){

                        concatenatedValues.append(appendStringValue)
                                .append(parts1[j]).append("/")
                                .append(parts2[j]);
                } else
                {
                    //Divide the multifield value in column "/DAM/SegmentBrandSubBrand/MarsWrigley/Snickers/SnickersNA"
                    concatenatedValues.append(appendStringValue)
                        .append(parts1[j]).append("/")
                        .append(parts1[0]).append(parts2[j]);

                }
                    // Append ";" if it's not the last iteration
                    if (j < Math.min(parts1.length, parts2.length) - 1) {
                        concatenatedValues.append(deLimiter);
                    }
                }

                // Create new cell in destination sheet and set the concatenated value
                Cell destCell = destinationRow.createCell(destinationColumnIndex);
                destCell.setCellValue(concatenatedValues.toString());
            }

            // Write the destination workbook to a file
            FileOutputStream outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);
            System.out.println("Pick and Concatenate --> LH Column "+sourceColumnName1+" and " +sourceColumnName2+" mapped successfully to Aprimo Column "+ destinationColumnName);
            // Close resources
            outputStream.close();
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

//    public static String dateFormatter(String dateColumnValue) {
//        DateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
//        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//        String formattedDate = null;
//        Date date = new Date();
//        try {
//            if(!dateColumnValue.isEmpty()){
//                date = originalFormat.parse(dateColumnValue);
//                formattedDate = targetFormat.format(date);
//            }} catch (Exception pe) {
//            System.out.println(pe);
//        }
//        return formattedDate;
//    }

    public static String dateFormatter(String dateColumnValue) throws ParseException {
        String targetFormatStr = "dd/MM/yyyy HH:mm:ss a";
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
                            for (String value : values) {
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

                    if ("Package".equals(sourceValue)) {
                        destinationValue = "Final Packaging";
                    } else {
                        destinationValue = "Reference";
                    }

                    Cell destinationCell = destinationRow.createCell(destinationColumnIndex);
                    destinationCell.setCellValue(destinationValue);
                }
            }

            outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);

            System.out.println("Mapping completed successfully.");

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

            System.out.println("Mapping completed successfully.");

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
                throw new IllegalArgumentException("Given source column 1 name not found in the source sheet.");
            }

            if (sourceColumn2Index == -1) {
                throw new IllegalArgumentException("Given source column 2 name not found in the source sheet.");
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
                        destinationValue = "Archived";
                    } else if (sourceCell2 != null) {
                        destinationValue = sourceCell2.getStringCellValue();
                    } else {
                        destinationValue = "";
                    }

                    Cell destinationCell = destinationRow.createCell(destinationColumnIndex);
                    destinationCell.setCellValue(destinationValue);
                }
            }

            outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);

            System.out.println("Mapping completed successfully.");

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
                    if (sourceValue.isEmpty()) {
                        Cell destinationCell = destinationRow.createCell(destinationColumnIndex);
                        destinationCell.setCellValue("");
                    } else {
                        String[] splitValues = sourceValue.split("\\|\\|");
                        boolean allMatched = true;
                        for (String value : splitValues) {
                            value = value.trim();
                            if (!productCategories.contains(value)) {
                                System.out.println("No match found at row " + (i + 1) + ": " + value);
                                allMatched = false;
                            }
                        }
                        if (allMatched) {
                            Cell destinationCell = destinationRow.createCell(destinationColumnIndex);
                            destinationCell.setCellValue(sourceValue);
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

            System.out.println("Mapping completed successfully.");

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

    public static void parseAndMapFlavors(String filePath, String sourceSheetName, String sourceColumnName, String destinationSheetName, String destinationColumnName) {

        // Create the ArrayList with given flavor names
        List<String> flavorNames = new ArrayList<>(Arrays.asList(
                "Apple", "Bacon", "Bacon and Beef", "Bacon and Egg", "Bacon and Filet Mignon",
                "Bacon, Egg, and Cheese", "Banana", "BBQ Chicken", "Beef", "Beef and Bacon",
                "Beef and Barley", "Beef and Cheese", "Beef and Chicken", "Beef and Liver",
                "Beef and Potato", "Beef and Rice", "Beef and Salmon", "Beef and Stew",
                "Beef and Vegetable", "Beef and Vegetables", "Beef Stew", "Beef Stroganoff",
                "Beef, Bacon, and Cheese", "Beef, Bacon, and Chicken", "Beef, Broccoli, and Brown Rice",
                "Beef, Cheese, and Bacon", "Beef, Chicken, and Liver", "Beef, Chicken, and Salmon",
                "Beef, Filet Mignon, Chicken, and Steak", "Beef, Milk, Vegetables, and Cereals",
                "Beef, Noodles, and Vegetables", "Beef, Rice, and Cheese", "Blend", "Blueberry",
                "Braised Rib", "Catfish and Tuna", "Catnip", "Catnip and Chicken", "Catnip, Tuna, and Dairy",
                "Cheese", "Cheese and Ham", "Cheeseburger", "Cheesy Chicken", "Cheesy Chicken Pasta",
                "Chef Inspired Chicken Entrée", "Chicken", "Chicken and Salmon", "Chicken & Parsley",
                "Chicken and Barley", "Chicken and Beef", "Chicken and Beef Stew", "Chicken and Carrot",
                "Chicken and Catfish", "Chicken and Cheddar", "Chicken and Cheese", "Chicken and Chickpeas",
                "Chicken and Egg", "Chicken and Filet Mignon", "Chicken and Lamb", "Chicken and Lentils",
                "Chicken and Liver", "Chicken and Mint", "Chicken and Oatmeal", "Chicken and Ocean Fish",
                "Chicken and Parsley", "Chicken and Pea", "Chicken and Potato", "Chicken and Rice",
                "Chicken and Salmon", "Chicken and Seafood", "Chicken and Shrimp", "Chicken and Tuna",
                "Chicken and Turkey", "Chicken and Turkey Casserole", "Chicken and Veal", "Chicken and Vegetable",
                "Chicken and Vegetables", "Chicken Rice and Lamb Rice", "Chicken, Bacon, and Cheese",
                "Chicken, Beef, and Lamb", "Chicken, Beef, and Liver", "Chicken, Beef, and Rice",
                "Chicken, Beef, and Turkey", "Chicken, Carrots, and Greens", "Chicken, Catnip, and Cheddar",
                "Chicken, Catnip, and Cheese", "Chicken, Egg, and Salmon", "Chicken, Fish, and Vegetables",
                "Chicken, Lamb, and Rice", "Chicken, Lamb, and Salmon", "Chicken, Lamb, Turkey & Salmon",
                "Chicken, liver & shrimp", "Chicken, Liver, and Beef", "Chicken, Liver, Beef, Bacon, and Cheese",
                "Chicken, Noodle, and Vegetables", "Chicken, Potato, and Peas", "Chicken, Rice, and Oatmeal",
                "Chicken, Rice, and Turkey", "Chicken, Rice, and Vegetable", "Chicken, Rice, and Vegetables",
                "Chicken, Rice, Beef, Rice, and Cheese", "Chicken, Salmon, and Beef", "Chicken, Salmon, and Egg",
                "Chicken, Salmon, and Tuna", "Chicken, Salmon, and Turkey", "Chicken, Tuna, Catnip, and Seafood",
                "Chicken, Tuna, Dairy, and Seafood", "Chicken, Tuna, Turkey, and Beef", "Chicken, Turkey, and Cheddar",
                "Chunky Chicken and Turkey Stew", "Cod and Shrimp", "Country Stew", "Crab", "Crab and Tuna",
                "Crisp Apple", "Crisp Apple, Chicken and parsley", "Dairy", "Deep Ocean Fish", "Deep Seafood and Fish",
                "Duck", "Duck and Lentils", "Duck and Pea", "Duck and Potato", "Duck and Rice",
                "Egg and Sausage", "Farm-Raised Chicken,Lentils & Sweet Potato Recipe",
                "Farm-Raised Chicken,Rice & Sweet Potato Recipe", "Filet Mignon", "Filet Mignon and Beef",
                "Filet Mignon and New York Strip", "Filet Mignon and Steak", "Filet Mignon Flavor",
                "Filet Mignon, Bacon, and Potato", "Fillet Steak Baked with Dill", "Fish", "Fish and Rice",
                "Fish and Sweet Potato", "Fish and Tuna", "Fish and Vegetable", "Fish, Rice, and Potato",
                "Fish, Whitefish, Tuna, Cod, and Shrimp", "Flavor", "Fresh", "Fresh Liver Flavor", "Freshmint",
                "Fried Lamb Chops with Cauliflower Potato", "Fruit", "Grain Free", "Grandmas Farm Stew with Lamb and Rice",
                "Grass Fed Lamb and Brown Rice Dinner", "Grilled Chicken and NY Strip", "Grilled Chicken Flavor",
                "Grilled Salmon, Rice, and Vegetable", "Grilled Steak and Vegetable", "Hairtail", "Ham",
                "Ham and Egg", "Harvest Potluck with Turkey", "Healthy Chicken and Rice Stew",
                "Healthy Dinner with Lamb and Vegetables", "Healthy Skin and Coat", "Herring", "Herring and Salmon",
                "Herring and Sweet Potato", "Hickory Smoke", "Hickory Smoked BBQ", "Hip and Joint Care", "Holiday Spice",
                "Kangaroo", "Kangaroo and Red Lentils", "Lamb", "Lamb and Lentils", "Lamb and Liver", "Lamb and Potato",
                "Lamb and Rice", "Lamb and Turkey", "Lamb and Vegetables", "Lamb and Venison", "Lamb Meal and Rice",
                "Lamb, Potato, and Peas", "Lamb, Rice, and Vegetable", "Lamb, Vegetables, and Chicken",
                "LID Fish and Potato Recipe", "LID Lamb and Potato Recipe", "LID Turkey and Potato Recipe",
                "Little Yellow Croaker", "Liver", "Liver and Beef", "Lobster", "Mackerel and Whitefish",
                "Meat Lasagna", "Meaty Lamb and Rice Stew", "Milk", "Minced Chicken", "Mint", "Mixed Berry",
                "Mixed Grill", "Multiple Flavors", "New York Strip", "NY Strip", "Oatmeal and Pumpkin",
                "Ocean Fish", "Ocean Fish and Rice", "Ocean Fish and Tuna", "Ocean Whitefish and Tuna", "Original",
                "Original, Fresh, and Beef", "Other", "Peanut", "Peanut Butter", "Peanut Butter and Honey",
                "Poached Salmon", "Pork", "Pork, Potatoes, and Green Beans", "Porterhouse Steak",
                "Porterhouse Steak and Vegetable", "Pot Roast", "Pot Roast and Vegetables", "Potherb and Beef",
                "Poultry", "Prime Rib", "Prime Rib Filet Mignon New York Strip", "Prime Rib, Rice, and Vegetable",
                "Pumpkin Spice", "Real Apple", "Real Banana", "Real Peanut Butter", "Ribeye Steak", "Roasted Chicken",
                "Roasted Lamb, Rice, and Vegetable", "Roasted Turkey and Vegetable Entrée", "Rustic Chicken and Vegetable dinner",
                "Salmon", "Salmon & Trout", "Salmon and Chicken", "Salmon and Egg", "Salmon and Garden Greens",
                "Salmon and Herring", "Salmon and Lentil", "Salmon and Oceanfish", "Salmon and Potato", "Salmon and Red Lentils",
                "Salmon and Rice", "Salmon and Shrimp", "Salmon and Tuna", "Salmon Meal and Sunflower Oil",
                "Salmon, Chicken, and Turkey", "Salmon, Potato, and Peas", "Salmon, Rice, and Vegetable",
                "Salmon, Whitefish, and Tuna", "Salmon,Brown Rice & Sweet Potato Recipe", "Sardines", "Sausage",
                "Sausage, Egg, and Cheese", "Savory Chicken", "Savory Lamb and Garden Variety Entrée", "Seafood",
                "Seafood and Tomato Bisque", "Seafood Medley", "Seafood, Salmon, and Tuna", "Seafood, Salmon, Cod, and Shrimp",
                "Shrimp", "Shrimp and Tuna", "Signature Beef and Potato Entrée", "Silver Fish and Tuna", "Sirloin",
                "Sirloin and Chicken", "Skipjack Tuna", "Smoked Bacon and Cheddar", "Smoked Beef", "Smoked Salmon",
                "Steak", "Steak and Chicken", "Steak and Egg, Chicken, and Liver", "Steak and Eggs", "Steak and Shrimp",
                "Steak and Tuna", "Steak and Vegetable", "Steak, Chicken, and Bacon", "Steak, Egg, and Cheese",
                "Steak, Peas, and Carrots", "Steak, Potatoes, Bacon, and Cheese", "Steamed Fish and Sweet Potato Dinner",
                "Stew", "Tender Beef and Vegetable Recipe", "Tender Chicken and Oatmeal Dinner", "Tender Chicken and Rice Recipe",
                "Tender Chicken and Turkey Recipe", "Tender lamb and Rice Recipe", "Tender Lamb Recipe", "Thunnus Tonggol and Skipjack Tuna",
                "Thunnus Tonggol, Grunion, and Skipjack Tuna", "Trout", "Tuna", "Tuna and Cheese", "Tuna and Chicken",
                "Tuna and Egg", "Tuna and Mackerel", "Tuna and Salmon", "Tuna and Shrimp", "Tuna and Turkey", "Tuna Flavor",
                "Tuna, Chicken, and Salmon", "Tuna, Chicken, and Vegetable", "Tuna, Salmon, and Turkey",
                "Tuna, Salmon, Chicken, and Turkey", "Tuna, Shrimp, and Crab", "Tuna, Shrimp, and Salmon", "Turkey",
                "Turkey and Bacon", "Turkey and Chicken", "Turkey and Cranberry", "Turkey and Giblets", "Turkey and Lamb",
                "Turkey and Liver", "Turkey and Pot Roast", "Turkey and Potato", "Turkey and Rice", "Turkey and Vegetable",
                "Turkey, Green Beans, and Potatoes", "Turkey, Lamb, and Chicken", "Turkey, Potato, and Peas",
                "Turkey, Rice, and Vegetables", "Turkey, Spinach, and Cheese", "Variety", "Vegetable", "Venison",
                "Venison and Green Lentils", "Venison, Rice, and Oatmeal", "Weight Management", "White Fish and Rice",
                "Whitefish", "Whitefish and Tuna", "Wild Blueberry and Pomegranate", "Ocean Fish Excluding Hairball",
                "Seafood Flavor"
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
                    if (sourceValue.isEmpty()) {
                        Cell destinationCell = destinationRow.createCell(destinationColumnIndex);
                        destinationCell.setCellValue("");
                    } else {
                        String[] splitValues = sourceValue.split(";");
                        boolean allMatched = true;
                        for (String value : splitValues) {
                            value = value.trim();
                            if (!flavorNames.contains(value)) {
                                System.out.println("No match found at row " + (i + 1) + ": " + value);
                                allMatched = false;
                            }
                        }
                        if (allMatched) {
                            Cell destinationCell = destinationRow.createCell(destinationColumnIndex);
                            destinationCell.setCellValue(sourceValue);
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

            System.out.println("Mapping completed successfully.");

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
}


