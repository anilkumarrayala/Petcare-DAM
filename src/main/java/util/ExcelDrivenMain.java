package util;


import java.io.IOException;

public class ExcelDrivenMain {

    public static void main(String[] args) throws IOException {

        String filePath = "C://Project//MARS//test-1-PN-1.xlsx";
        String sourceSheetName = "Data";
        String destinationSheetName = "Transformed";
        String sourceColumnName = "Occasion";
        //String destinationColumnName = "Occasion";

        /*
        Field to Field Mapping
         */

        //ExcelTransformationUtility.fieldToFieldColumnMapping(filePath, sourceSheetName, destinationSheetName, sourceColumnName, destinationColumnName);


        /*
        Split with Delimiter "^" and map the values into diff columns
         */

        //String destinationColumnName1 = "Region";
        //String destinationColumnName2 = "Marketing Country";
        //String destinationColumnName3 = "Sub-Type";
        //ExcelTransformationUtility.splitAndMap(filePath, sourceSheetName, destinationSheetName,sourceColumnName, destinationColumnName1, destinationColumnName2);


        /*
        Split the values with delimeter "||" and append with "~" with unique values
        (or) change the delimiter to be appended as per requirement in parseAndMapCellValues method
        Split the values with delimeter "||" and append with "," with unique values
        Ex: AVO||AVO||Music License||Music License -> AVO~Music License
         */

        //ExcelTransformationUtility.parseAndMap(filePath, sourceSheetName, destinationSheetName, sourceColumnName, destinationColumnName);


        /*
        Occasion = Christmas^2018||Christmas^2018||Holiday^2019 ->
            Occasion = Christmas~Holiday
            Year = 2018~2019
         */
        //ExcelTransformationUtility.parseAndMapping(filePath, sourceSheetName,destinationSheetName,sourceColumnName, destinationColumnName1, destinationColumnName2);


        /*
        Cleansing Example:Christmas^2018||Christmas^2018||Holiday^2019
        -> Occasion = Christmas~Holiday
        -> Year = 2018~2019
         */
       String destinationColumnName = "Occasion";
       //String destinationColumnName2 = "Year";
        ExcelTransformationUtility.parseAndMap1(filePath, sourceSheetName, destinationSheetName, sourceColumnName,
                destinationColumnName);

    }

}
