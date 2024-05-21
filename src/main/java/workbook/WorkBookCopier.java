package workbook;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class WorkBookCopier {
    public static void createNewSheet(String sourceSheetName, String sourceFilePath, String destSheetName, String destFilePath) throws IOException {
        FileInputStream sourceFile = null;
        FileInputStream destFile = null;
        FileOutputStream outputFile = null;

        try {
            // Open the source workbook
            sourceFile = new FileInputStream(sourceFilePath);
            Workbook sourceWorkbook = new XSSFWorkbook(sourceFile);

            // Get the sheet from the source workbook
            Sheet sourceSheet = sourceWorkbook.getSheet(sourceSheetName);
            if (sourceSheet == null) {
                throw new IllegalArgumentException("Source sheet " + sourceSheetName + " does not exist.");
            }

            // Open or create the destination workbook
            Workbook destWorkbook;
            try {
                destFile = new FileInputStream(destFilePath);
                destWorkbook = new XSSFWorkbook(destFile);
            } catch (IOException e) {
                // If the destination file does not exist, create a new workbook
                destWorkbook = new XSSFWorkbook();
            }

            // Create the destination sheet
            Sheet destSheet = destWorkbook.createSheet(destSheetName);

            // Copy data from source sheet to destination sheet
            copySheetData(sourceSheet, destSheet);

            // Write the destination workbook to file
            outputFile = new FileOutputStream(destFilePath);
            destWorkbook.write(outputFile);

            // Close all resources
            sourceWorkbook.close();
            destWorkbook.close();
        } finally {
            if (sourceFile != null) {
                sourceFile.close();
            }
            if (destFile != null) {
                destFile.close();
            }
            if (outputFile != null) {
                outputFile.close();
            }
        }
    }

    private static void copySheetData(Sheet sourceSheet, Sheet destSheet) {
        for (int i = 0; i <= sourceSheet.getLastRowNum(); i++) {
            Row sourceRow = sourceSheet.getRow(i);
            Row destRow = destSheet.createRow(i);

            if (sourceRow != null) {
                for (int j = 0; j < sourceRow.getLastCellNum(); j++) {
                    Cell sourceCell = sourceRow.getCell(j);
                    Cell destCell = destRow.createCell(j);

                    if (sourceCell != null) {
                        copyCellData(sourceCell, destCell);
                    }
                }
            }
        }
    }

    private static void copyCellData(Cell sourceCell, Cell destCell) {
        switch (sourceCell.getCellType()) {
            case STRING:
                destCell.setCellValue(sourceCell.getStringCellValue());
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(sourceCell)) {
                    destCell.setCellValue(sourceCell.getDateCellValue());
                } else {
                    destCell.setCellValue(sourceCell.getNumericCellValue());
                }
                break;
            case BOOLEAN:
                destCell.setCellValue(sourceCell.getBooleanCellValue());
                break;
            case FORMULA:
                destCell.setCellFormula(sourceCell.getCellFormula());
                break;
            case BLANK:
                destCell.setCellType(CellType.BLANK);
                break;
            default:
                break;
        }

        // Copy cell style
        CellStyle newCellStyle = destCell.getSheet().getWorkbook().createCellStyle();
        newCellStyle.cloneStyleFrom(sourceCell.getCellStyle());
        destCell.setCellStyle(newCellStyle);
    }


}
