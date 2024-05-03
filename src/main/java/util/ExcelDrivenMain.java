package util;


import java.io.IOException;

public class ExcelDrivenMain {

    public static void main(String[] args) throws IOException {

        String filePath = "C://Users//anil.kumar.rayala//Downloads//test-data-new.xlsx";
        String sourceSheetName = "Data";
        String destinationSheetName = "Transforming";
        String sourceColumnName = "ID";
        String destinationColumnName = "AssetID";
        int destinationColumnIndex = 0;

        ExcelTransformationUtility.fieldToFieldColumnMapping(filePath, sourceSheetName, destinationSheetName, sourceColumnName, destinationColumnName);
        ExcelTransformationUtility.splitAndMap(filePath, sourceSheetName, destinationSheetName,sourceColumnName, destinationColumnName);


    }

}
