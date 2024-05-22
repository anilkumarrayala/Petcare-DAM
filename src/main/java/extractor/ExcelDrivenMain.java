package extractor;


import util.ExcelTransformationUtility;
import constants.ProjectConstants;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static workbook.WorkBookCopier.createNewSheet;

public class ExcelDrivenMain {

    public static void main(String[] args) throws IOException,Exception {

        String filePath = "C://Project//MARS//test-1-PN-test.xlsx";
        String extension = "Transformed-" +ExcelTransformationUtility.getCurrentTimestamp()+".xlsx";
        String destFilePath =System.getProperty("user.home")+ extension;
        String sourceSheetName = "Data";
        String destinationSheetName = "Transformed";
        String destinationSheetName1 = "Final";
        ProjectConstants projectConstants = new ProjectConstants();
        // Get current size of heap in bytes.
        long heapSize = Runtime.getRuntime().totalMemory();
        System.out.println("Java Heap Size "+heapSize);
        // Get maximum size of heap in bytes. The heap cannot grow beyond this size.
        // Any attempt will result in an OutOfMemoryException.
        long heapMaxSize = Runtime.getRuntime().maxMemory();
        System.out.println("Java Heap Max Size "+heapMaxSize);
        // Get amount of free memory within the heap in bytes. This size will
        // increase after garbage collection and decrease as new objects are created.
        long heapFreeSize = Runtime.getRuntime().freeMemory();
        System.out.println("Java Heap Free Size "+heapFreeSize);
        // Parse and map
        List LHSplitColumnName =  projectConstants.getLHSplitReplaceColumnNames();
        List AprimoSplitColumnName =  projectConstants.getAprimoSplitReplaceColumnNames();
        Iterator<String> it5 = LHSplitColumnName.iterator();
        Iterator<String> it6 = AprimoSplitColumnName.iterator();
        while(it5.hasNext() && it6.hasNext())
        {
            ExcelTransformationUtility.parseAndMapSingleColumn(filePath,sourceSheetName, destinationSheetName, it5.next(), it6.next(),';');

        }
        //Field to Field mapping with date transformations
        List LHDateColumns =  projectConstants.getLHDateColumnName();
        List AprimoDateColumns =  projectConstants.getAprimoDateColumnName();
        Iterator<String> it3 = LHDateColumns.iterator();
        Iterator<String> it4 = AprimoDateColumns.iterator();
        while(it3.hasNext() && it4.hasNext())
        {
            ExcelTransformationUtility.mapDateFields(filePath,sourceSheetName, destinationSheetName, it3.next(), it4.next(),';');
        }

        //Field to Field mapping
        List LHColumnName =  projectConstants.getLightHouseColumnName();
        List AprimoColumnName =  projectConstants.getAprimoColumnName();
        Iterator<String> it1 = LHColumnName.iterator();
        Iterator<String> it2 = AprimoColumnName.iterator();
        while(it1.hasNext() && it2.hasNext())
        {
            ExcelTransformationUtility.fieldToFieldColumnMapping(filePath,sourceSheetName, destinationSheetName, it1.next(), it2.next());
        }

        String sourceColumnName1 = "Region/Marketing Country 1";
        String sourceColumnName2 = "Region/Marketing Country 2";
        String destinationColumnName1 = "Region/MarketingCountry";
        ExcelTransformationUtility.pickAndConcatenate(filePath, sourceSheetName, destinationSheetName,sourceColumnName1, sourceColumnName2, destinationColumnName1,';',"/DAM/MarketingRegionMarketingCountry/");


        String sourceColumnName_Brand = "Brand";
        String sourceColumnName_SubBrand = "Sub-Brand";
        String destinationColumnName_Combined = "BrandandSub-Brand";
        ExcelTransformationUtility.pickAndConcatenate(filePath, sourceSheetName, destinationSheetName,sourceColumnName_Brand, sourceColumnName_Brand, destinationColumnName_Combined,';',"/DAM/SegmentBrandSubBrand/PetNutrition/");

        //Rearranging column order
        List columnOrder =  projectConstants.getColumnOrder();
        ExcelTransformationUtility.rearrangeColumns(filePath, destinationSheetName, destinationSheetName1,  columnOrder);

        //Additional Transformations
        String sourceColumnName_Expo = "GTIN/EAN/UPC";
        ExcelTransformationUtility.parseExponentialFields(filePath, sourceSheetName, sourceColumnName_Expo,destinationSheetName, sourceColumnName_Expo);
        String sourceColumnName_VERP = "VERP";
        ExcelTransformationUtility.parseExponentialFields(filePath, sourceSheetName, sourceColumnName_VERP,destinationSheetName, sourceColumnName_VERP);
        String sourceColumnName_FERT = "FERT";
        ExcelTransformationUtility.parseExponentialFields(filePath, sourceSheetName, sourceColumnName_FERT,destinationSheetName, sourceColumnName_FERT);

        String sourceColumnName_Policies = "&POLICIES";
        String destinationColumnName_Policies = "DeliverableType";
        ExcelTransformationUtility.mapPoliciesAndPackageValues(filePath, sourceSheetName, sourceColumnName_Policies,destinationSheetName, destinationColumnName_Policies);

        String sourceColumn1Name_Status = "&EXPORT_PATH";
        String sourceColumn2Name_Status = "Asset Status";
        String destinationColumnName_Status = "Status";
        ExcelTransformationUtility.mapExportPathAndArchivedValues(filePath, sourceSheetName, sourceColumn1Name_Status,sourceColumn2Name_Status,destinationSheetName, destinationColumnName_Status);

        String sourceColumnName_Product = "Segment/Product Category 2";
        String destinationColumnName_Product = "ProductCategory";
        ExcelTransformationUtility.parseAndMapProductCategories(filePath, sourceSheetName, sourceColumnName_Product,destinationSheetName, destinationColumnName_Product);

        String sourceColumnName_Flavor = "Segment/Flavor 2";
        String destinationColumnName_Flavor = "Flavor";
        ExcelTransformationUtility.parseAndMapFlavors(filePath, sourceSheetName, sourceColumnName_Flavor,destinationSheetName, destinationColumnName_Flavor);


        //Create a new sheet for transformed data
       createNewSheet(destinationSheetName, filePath, destinationSheetName, destFilePath);
    }
    }


