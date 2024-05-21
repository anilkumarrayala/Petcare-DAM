package util;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

public class ExcelDrivenMain {

    public static void main(String[] args) throws IOException,Exception {
      String filePath = "C://Project//MARS//test-1-PN-1.xlsx";
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

//            String sourceColumnName = "GTIN/EAN/UPC";
//            String destinationColumnName = "test4";
//            ExcelTransformationUtility.parseExponentialFields(filePath, sourceSheetName, sourceColumnName, destinationSheetName, destinationColumnName);
//
//            String sourceColumnName = "&POLICIES";
//            String destinationColumnName = "DeliverableType";
//            ExcelTransformationUtility.mapPoliciesAndPackageValues(filePath, sourceSheetName, sourceColumnName, destinationSheetName, destinationColumnName);
//
//            String sourceColumnName = "&POLICIES";
//            String destinationColumnName = "RestrictDownload";
//            ExcelTransformationUtility.mapPoliciesAndVideoAssetsValues(filePath, sourceSheetName, sourceColumnName, destinationSheetName, destinationColumnName);
//
//            String sourceColumnName1 = "&EXPORT_PATH";
//            String sourceColumnName2 = "Asset Status";
//            String destinationColumnName = "Status";
//            ExcelTransformationUtility.mapExportPathAndArchivedValues(filePath, sourceSheetName, sourceColumnName1, sourceColumnName2, destinationSheetName, destinationColumnName);
    }

    }


