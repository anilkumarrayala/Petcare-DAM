package Test;



import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

    public class Test {


        public static void fieldToFieldColumnMapping(String filePath, String sourceSheetName, String destinationSheetName, String sourceColumnName, String destinationColumnName) {
            try {

                FileInputStream fis = new FileInputStream(filePath);
                Workbook workbook = new XSSFWorkbook(fis);


                Sheet sourceSheet = workbook.getSheet(sourceSheetName);
                Sheet destinationSheet = workbook.getSheet(destinationSheetName);


                int sourceColumnIndex = -1;
                
                //Mandatory to change the index value to get the mapping consecutively
                int destinationColumnIndex = 0; /* When 0, New column will be the first column in the destination sheet, we change the index based on requirement*/


            /*
            Find the source column index
             */
                Row sourceHeaderRow = sourceSheet.getRow(0);
                for (Cell cell : sourceHeaderRow) {
                    if (cell.getStringCellValue().equals(sourceColumnName)) {
                        sourceColumnIndex = cell.getColumnIndex();
                        break;
                    }
                }

                if (sourceColumnIndex == -1) {
                    throw new IllegalArgumentException("Specified source column name not found in the source sheet.");
                }

                /* setting the destination column name */
                if(destinationSheet.getRow(0) == null)
                {
                    Row destinationfirstrow = destinationSheet.createRow(0);
                    Cell cellvalue = destinationfirstrow.createCell(destinationColumnIndex);
                    cellvalue.setCellValue(destinationColumnName);
                }
                else
                {
                    destinationSheet.getRow(0).createCell(destinationColumnIndex).setCellValue(destinationColumnName);
                }


                /* Mapping the column values */
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

                // Write changes to the workbook
                FileOutputStream outputStream = new FileOutputStream(filePath);
                workbook.write(outputStream);

                // Close all resources
                outputStream.close();
                fis.close();
                workbook.close();

                System.out.println("Column mapped successfully!");
            } catch (IOException | IllegalArgumentException | IllegalStateException ex) {
                ex.printStackTrace();
            }
        }

        public static void main(String[] args) {
            // Example usage
            //String filePath = "C://Users//Project//MARS//Downloads//test-data-new.xlsx";
            String filePath = "C://Users//anil.kumar.rayala//Downloads//test-data-new.xlsx";
            String sourceSheetName = "Data";
            String destinationSheetName = "Transforming";
            String sourceColumnName = "ID";
            String destinationColumnName = "AssetID";

            fieldToFieldColumnMapping(filePath, sourceSheetName, destinationSheetName, sourceColumnName, destinationColumnName);
        }

    }

