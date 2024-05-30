package extractor;


import constants.LookUpConstants2;
import constants.LookupConstants;
import util.ExcelTransformationUtility;
import constants.ProjectConstants;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static workbook.WorkBookCopier.createNewSheet;

public class ExcelDrivenMain {

    public static void main(String[] args) throws IOException,Exception {

        //String filePath = "C://Project//MARS//PN-50k-100k.xlsx";
        String filePath = "C://Project//MARS//test-1-PN-2.xlsx";
        String extension = "Transformed-" +ExcelTransformationUtility.getCurrentTimestamp()+".xlsx";
        String destFilePath =System.getProperty("user.home")+ extension;
        //String destFilePath ="C://Project//MARS//" + extension;
        String sourceSheetName = "Data";
        String destinationSheetName = "Transformed";
        String destinationSheetName1 = "Final";
        String sourceSheetName_Transformed = "Transformed";
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

        //Lookup for Region & MarketingCountries
        String sourceColumnName_Region = "Region/Marketing Country 1";
        String destinationColumnName_Region = "Region";
        //ExcelTransformationUtility.parseAndLookup1(filePath, sourceSheetName, sourceColumnName_Region,destinationSheetName, destinationColumnName_Region, LookupConstants.getRegions());

        String sourceColumnName_MarketingCountry = "Region/Marketing Country 2";
        String destinationColumnName_MarketingCountry = "MarketingCountry";
        //ExcelTransformationUtility.parseAndLookup1(filePath, sourceSheetName, sourceColumnName_MarketingCountry,destinationSheetName, destinationColumnName_MarketingCountry, LookupConstants.getMarketingCountries());

        //not working properly ExcelTransformationUtility.pickAndConcatenate(filePath, destinationSheetName, destinationSheetName, destinationColumnName_Region, destinationColumnName_MarketingCountry, destinationColumnName1,';',"/DAM/MarketingRegionMarketingCountry/");
        String destinationColumnName1 = "Region/MarketingCountry";
        ExcelTransformationUtility.pickAndConcatenate(filePath, sourceSheetName, destinationSheetName,sourceColumnName_Region, sourceColumnName_MarketingCountry, destinationColumnName1,';',"/DAM/MarketingRegionMarketingCountry/",LookupConstants.getRegions(), LookupConstants.getMarketingCountries());

        String sourceColumnName_Brand = "Brand";
        String sourceColumnName_SubBrand = "Sub-Brand";
        //Lookup for Brand
        //ExcelTransformationUtility.parseAndLookup1(filePath, sourceSheetName, sourceColumnName_Brand,destinationSheetName, sourceColumnName_Brand, LookupConstants.getBrands());
        //Lookup for Sub Brand
        //ExcelTransformationUtility.parseAndLookup1(filePath, sourceSheetName, sourceColumnName_SubBrand,destinationSheetName, "SubBrand", LookupConstants.getSubBrandNames());

        String destinationColumnName_Combined = "BrandSubBrandHierarchy";
        ExcelTransformationUtility.pickAndConcatenate(filePath, sourceSheetName, destinationSheetName,sourceColumnName_Brand, sourceColumnName_SubBrand, destinationColumnName_Combined,';',"/DAM/SegmentBrandSubBrand/PetNutrition/", LookupConstants.getBrands(), LookupConstants.getSubBrandNames());


        //Additional Transformations
        String sourceColumnName_Expo = "GTIN/EAN/UPC";
        String destinationColumnName_Expo = "GTINEANUPC";
        ExcelTransformationUtility.parseExponentialFields(filePath, sourceSheetName, sourceColumnName_Expo,destinationSheetName, destinationColumnName_Expo);
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

        ExcelTransformationUtility.mapStatusValues(filePath, sourceSheetName_Transformed, destinationColumnName_Status, destinationSheetName, destinationColumnName_Status);

        String sourceColumnName_Description = "Description";
        String sourceColumnNmae_Keywords = "Description/Keywords";
        String destinationColumnName_Description = "Description";
        ExcelTransformationUtility.concatenateAndMapValues(filePath, sourceSheetName, sourceColumnName_Description, sourceColumnNmae_Keywords,destinationSheetName ,destinationColumnName_Description);

        //Lookup for product category
        String sourceColumnName_Product = "Segment/Product Category 2";
        String destinationColumnName_Product = "ProductCategory";
        ExcelTransformationUtility.parseAndMapProductCategories(filePath, sourceSheetName, sourceColumnName_Product,destinationSheetName, destinationColumnName_Product);

        // Lookup for flavor
        String sourceColumnName_Flavor = "Segment/Flavor 2";
        String destinationColumnName_Flavor = "Flavor";
        ExcelTransformationUtility.parseAndLookup(filePath, sourceSheetName, sourceColumnName_Flavor,destinationSheetName, destinationColumnName_Flavor, LookupConstants.getFlavorNames());

        //Lookup for Asset Category
        String sourceColumnName_Category = "Category/Type/Sub-Type 1";
        String destinationColumnName_Category = "AssetCategory";
        ExcelTransformationUtility.parseAndLookup(filePath, sourceSheetName, sourceColumnName_Category,destinationSheetName, destinationColumnName_Category, LookupConstants.getAssetCategories());

        //Lookup for Asset SubType
        String sourceColumnName_SubCategory = "Category/Type/Sub-Type 3";
        String destinationColumnName_SubCategor = "AssetSubType";
        ExcelTransformationUtility.parseAndLookup(filePath, sourceSheetName, sourceColumnName_SubCategory,destinationSheetName, destinationColumnName_SubCategor, LookupConstants.getAssetSubType());

        //Lookup for Asset Type
        String sourceColumnName_AssetType = "Category/Type/Sub-Type 2";
        String destinationColumnName_AssetType = "AssetType";
        ExcelTransformationUtility.parseAndLookup(filePath, sourceSheetName, sourceColumnName_AssetType,destinationSheetName, destinationColumnName_AssetType, LookupConstants.getAssetTypes());

        String sourceColumnName_AssetTypes = "AssetType";
        String destinationColumnName_AssetSubType = "AssetSubType";
        ExcelTransformationUtility.mapAssetTypeToAssetSubType(filePath, sourceSheetName_Transformed, sourceColumnName_AssetTypes, destinationSheetName, destinationColumnName_AssetSubType);

        String destinationColumnName_ACatATypeASubTypeHierarchy = "ACatATypeASubTypeHierarchy";
        ExcelTransformationUtility.pickAndConcatenateAssets(filePath, sourceSheetName_Transformed, destinationSheetName, destinationColumnName_Category, destinationColumnName_AssetType, destinationColumnName_AssetSubType, destinationColumnName_ACatATypeASubTypeHierarchy ,"/DAM/ACatATypeASubTypeHierarchy");

        //Lookup for Originating country
        String sourceColumnName_OriginatingCountry = "Segment/Flavor 2";
        String destinationColumnName_OriginatingCountry = "OriginatingCountry";
        ExcelTransformationUtility.parseAndLookup(filePath, sourceSheetName, sourceColumnName_OriginatingCountry,destinationSheetName, destinationColumnName_OriginatingCountry, LookupConstants.getOriginatingCountries());

        //Lookup for language
        String destinationColumnName_Language = "Language";
        ExcelTransformationUtility.parseAndLookup(filePath, sourceSheetName, destinationColumnName_Language,destinationSheetName, destinationColumnName_Language, LookUpConstants2.getLanguages());

        //Lookup for Occasion
        String sourceColumnName_Occasion = "Occasion 1";
        String destinationColumnName_Occasion = "Occasion";
        ExcelTransformationUtility.parseAndLookup(filePath, sourceSheetName, sourceColumnName_Occasion,destinationSheetName, destinationColumnName_Occasion, LookUpConstants2.getOccasion());

        //Lookup for Occasion Year
        String sourceColumnName_OccasionYear = "Occasion Year";
        String destinationColumnName_OccasionYear = "OccasionYear";
        ExcelTransformationUtility.parseAndLookup(filePath, sourceSheetName, sourceColumnName_OccasionYear,destinationSheetName, destinationColumnName_OccasionYear, LookUpConstants2.getOccasionyYears());

        //Lookup for Customer Specific
        String sourceColumnName_CustomerSpecific = "Customer Specific";
        String destinationColumnName_CustomerSpecific = "CustomerSpecific";
        ExcelTransformationUtility.parseAndLookup(filePath, sourceSheetName, sourceColumnName_CustomerSpecific,destinationSheetName, destinationColumnName_CustomerSpecific, LookUpConstants2.getCustomerSpecific());

        //Lookup for Pet Lifestage
        String sourceColumnName_PetLifestage = "Pet Lifestage";
        String destinationColumnName_PetLifestage = "PetLifestage";
        ExcelTransformationUtility.parseAndLookup(filePath, sourceSheetName, sourceColumnName_PetLifestage,destinationSheetName, destinationColumnName_PetLifestage, LookUpConstants2.getPetLifeStage());

        //Lookup for Pet Size
        String sourceColumnName_Petsize = "Pet Size";
        String destinationColumnName_Petsize = "PetSize";
        ExcelTransformationUtility.parseAndLookup(filePath, sourceSheetName, sourceColumnName_Petsize,destinationSheetName, destinationColumnName_Petsize, LookUpConstants2.getPetSizes());

        //Lookup for health Benefit
        String sourceColumnName_HealthBenefit = "Health Benefit";
        String destinationColumnName_HealthBenefit = "HealthBenefit";
        ExcelTransformationUtility.parseAndLookup(filePath, sourceSheetName, sourceColumnName_HealthBenefit,destinationSheetName, destinationColumnName_HealthBenefit, LookUpConstants2.getHealthBenefits());

        //Lookup for Display/Pack Type
        String sourceColumnName_DisplayPackType = "Display/Pack Type";
        String destinationColumnName_DisplayPackType = "Display/PackType";
        ExcelTransformationUtility.parseAndLookup(filePath, sourceSheetName, sourceColumnName_DisplayPackType,destinationSheetName, destinationColumnName_DisplayPackType, LookUpConstants2.getDisplayPackTypes());

        //Lookup for Rights Restricted?
        String sourceColumnName_RightsRestricted = "Rights Restricted?";
        String destinationColumnName_RightsRestricted = "RightsRestricted";
        ExcelTransformationUtility.parseAndLookup(filePath, sourceSheetName, sourceColumnName_RightsRestricted,destinationSheetName, destinationColumnName_RightsRestricted, LookUpConstants2.getRightsRestricted());

        //Lookup for Global/Regional/Local
        String ColumnName_GlobalRegionalLocal = "Global/Regional/Local";
        ExcelTransformationUtility.parseAndLookup(filePath, sourceSheetName, ColumnName_GlobalRegionalLocal,destinationSheetName, ColumnName_GlobalRegionalLocal, LookUpConstants2.getGlobalRegionalLocal());

        //Lookup for Rights Management Type
        String sourceColumnName_RightsManagementType = "Rights Management Type";
        String destinationColumnName_RightsManagementType = "RightsManagementType";
        ExcelTransformationUtility.parseAndLookup(filePath, sourceSheetName, sourceColumnName_RightsManagementType,destinationSheetName, destinationColumnName_RightsManagementType, LookUpConstants2.getRightsManagementType());

        //Rearranging column order
        List columnOrder =  projectConstants.getColumnOrder();
        ExcelTransformationUtility.rearrangeColumns(filePath, destinationSheetName, destinationSheetName1,  columnOrder);

        //Create a new sheet for transformed data
       //createNewSheet(destinationSheetName, filePath, destinationSheetName, destFilePath);

    }
    }


