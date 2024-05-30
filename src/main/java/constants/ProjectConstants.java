package constants;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProjectConstants {


    public List getLightHouseColumnName() {
        return LightHouseColumnName;
    }

    public List getAprimoColumnName() {
        return AprimoColumnName;
    }


    // Fields that are not included due to no fields in Aprimo --> Original Asset ID,	Version Description,	Food Category/Sub-Category,	Petcare Platform


    public List getColumnOrder() {
        return ColumnOrder;
    }

    private    List ColumnOrder =
            Arrays.asList(new String[] {"&ASSET_ID","&ASSET_NAME","LighthouseSecurityPolicy","&EXPORT_PATH","AssetID", "Title" ,
                    "LegacyAssetVersion","ImportedDate", "EmbargoDate","ExpirationDate","IsLatestVersion",
                    "AgencyPartner", "AssetCategory","AssetType","AssetSubType", "AssetCreationDate","AssetDate","Status","DeliverableType","CampaignPromotionalName", "MarsCampaignCode","CampaignEndDate","CampaignStartDate",
                    "CustomerSpecific","DataClassification","Description","Keywords","OriginalFileName","HealthBenefit", "TransparentBackground",
                    "NetWeight","PetLifestage","PieceCount","PlatformSpecifications","RightsRestricted","PetSize","BrightcoveAssetID","BrightcoveAssetName","BrightcoveRemovedBy","BrightcoveRemovedDate",
                    "BrightcoveAssetStatus", "MasterOrLocalizationAdaptation","Segment","Flavor","ProductCategory",
                    "RightsManagementType","RightsManagementType1","RightsManagementType2","RightsManagementType3","RightsManagementType4","RightsManagementType5","RightsManagementType6","RightsManagementType7","RightsManagementType8",
                    "Recipe","Region/MarketingCountry","ProductImageAngle",
                    "Occasion","OccasionYear","OriginatingCountry","FERT","Claims", "Display/PackType","CampaignPromotionalYear","AssetEmail",
                    "Keywords","InPerpetuity","InPerpetuity1","InPerpetuity2","InPerpetuity3","InPerpetuity4","InPerpetuity5","InPerpetuity6","InPerpetuity7","InPerpetuity8","Language","GTINEANUPC","ImagePosition",
                    "VERP","BrandSubBrandHierarchy","CoverageStartDate","CoverageStartDate1","CoverageStartDate2","CoverageStartDate3","CoverageStartDate4","CoverageStartDate5",
                    "CoverageStartDate6","CoverageEndDate7","CoverageStartDate8","CoverageEndDate","CoverageEndDate1","CoverageEndDate2","CoverageEndDate3","CoverageEndDate4","CoverageEndDate5","CoverageEndDate6",
                    "CoverageEndDate7","CoverageEndDate8","InternalZREP","Global/Regional/Local","Global/Regional/Local1","Global/Regional/Local2","Global/Regional/Local3","Global/Regional/Local4"
                    ,"Global/Regional/Local5","Global/Regional/Local6","Global/Regional/Local7","Global/Regional/Local8","IncludedCountries","IncludedCountries1","IncludedCountries2","IncludedCountries3","IncludedCountries4",
                    "IncludedCountries5","IncludedCountries6","IncludedCountries7","IncludedCountries8", "ExcludedCountries", "ExcludedCountries1", "ExcludedCountries2", "ExcludedCountries3", "ExcludedCountries4", "ExcludedCountries5",
                    "ExcludedCountries6", "ExcludedCountries7", "ExcludedCountries8",
                    "MediaChannel","MediaChannel1","MediaChannel2","MediaChannel3","MediaChannel4","MediaChannel5","MediaChannel6","MediaChannel7","MediaChannel8",
                    "RestrictionDescription","RestrictionDescription1","RestrictionDescription2","RestrictionDescription3","RestrictionDescription4","RestrictionDescription5","RestrictionDescription6","RestrictionDescription7","RestrictionDescription8"
                    ,"is-related-to","extracted-pdf","copy-content-of","extracted-pdf-child","participant-subscribed","belong-to-video","participant-collection-on-subscribed","extracted-xml",
                    "is-derivative-of","contains","is-child-of","derivative","is-place-graphic-of", "belong-to","has-video-clips", "copy-contains","is-parent-of","extracted-xml-child","placed-graphics"
            });


    private    List LightHouseColumnName =
            Arrays.asList(new String[] {"ID", "Name" , "Asset Version" ,
                    "Agency/Partner/Vendor","Category/Type/Sub-Type 2", "Category/Type/Sub-Type 3", "Asset Status","Description"
                    ,"Description/Keywords","Original File Name","Health Benefit","Net Weight","Pet Lifestage","Piece Count","Pet Size","Brightcove Asset ID",
                    "Brightcove Asset Name","Removed By","Brightcove Asset Status","Segment/Product Category 2","Recipe",
                    "Occasion 1","Occasion Year","Originating Country","Claims","Display/Pack Type","Campaign/Promotional Year","Asset Owner"});

    private List AprimoColumnName = Arrays.asList(new String[] {"LighthouseAssetID", "Title" , "LegacyAssetVersion",
            "AgencyPartner","AssetType","AssetSubType","Status","Description","Keywords","OriginalFileName","HealthBenefit",
            "NetWeight","PetLifestage","PieceCount","PetSize","BrightcoveAssetID","BrightcoveAssetName","BrightcoveRemovedBy",
            "BrightcoveAssetStatus","ProductCategory","Recipe", "Occasion","OccasionYear","OriginatingCountry","Claims",
            "Display/PackType","CampaignPromotionalYear","AssetEmail"});

    public List getAprimoDateColumnName() {
        return AprimoDateColumnName;
    }

    public List getLHDateColumnName() {
        return LHDateColumnName;
    }


    private  List AprimoDateColumnName = Arrays.asList(new String[] {"&EXPORT_TIME","AssetCreationDate","ImportedDate", "EmbargoDate","ExpirationDate","AssetDate",
            "CampaignEndDate","CampaignStartDate", "BrightcoveRemovedDate","CoverageStartDate","CoverageStartDate1","CoverageStartDate2","CoverageStartDate3","CoverageStartDate4","CoverageStartDate5",
            "CoverageStartDate6","CoverageEndDate7","CoverageStartDate8","CoverageEndDate","CoverageEndDate1","CoverageEndDate2","CoverageEndDate3","CoverageEndDate4","CoverageEndDate5","CoverageEndDate6",
            "CoverageEndDate7","CoverageEndDate8","is-related-to"});

    private    List LHDateColumnName =
            Arrays.asList(new String[] {"&EXPORT_TIME","Asset Creation Date","Date: Imported", "Embargo Date","Expiration Date","Asset Live Date",

                    "Campaign End Date","Campaign Start Date", "Removed Dt", "Coverage Start Date","Coverage Start Date 1","Coverage Start Date 2","Coverage Start Date 3","Coverage Start Date 4","Coverage Start Date 5",
                    "Coverage Start Date 6","Coverage Start Date 7","Coverage Start Date 8","Coverage End Date","Coverage End Date 1","Coverage End Date 2","Coverage End Date 3","Coverage End Date 4","Coverage End Date 5",
                    "Coverage End Date 6","Coverage End Date 7","Coverage End Date 8","&LINKS:RELATED"});


    public List getAprimoSplitReplaceColumnNames() {
        return AprimoSplitReplaceColumnNames;
    }

    public List getLHSplitReplaceColumnNames() {
        return LHSplitReplaceColumnNames;
    }





    private  List AprimoSplitReplaceColumnNames = Arrays.asList(new String[] { "&OBJECT_TYPE",	"&ACTION","OriginalAssetID","&FOLDER_TYPE","&MODEL","&PARENTS","Title","LighthouseSecurityPolicy","ModifiedFileName",
            "Keywords","InPerpetuity","InPerpetuity1","InPerpetuity2","InPerpetuity3","InPerpetuity4","InPerpetuity5","InPerpetuity6","InPerpetuity7","InPerpetuity8",
            "Language","CustomerSpecific","CampaignPromotionalName", "MarsCampaignCode","RightsRestricted","IsLatestVersion",
            "TransparentBackground","PlatformSpecifications","SourceFile","DataClassification","Language","ImagePosition","Brand","SubBrand","Segment",
            "MasterOrLocalizationAdaptation","ProductImageAngle", "InternalZREP","Global/Regional/Local","Global/Regional/Local1","Global/Regional/Local2","Global/Regional/Local3","Global/Regional/Local4"
            ,"Global/Regional/Local5","Global/Regional/Local6","Global/Regional/Local7","Global/Regional/Local8","IncludedCountries","IncludedCountries1","IncludedCountries2","IncludedCountries3","IncludedCountries4",
            "IncludedCountries5","IncludedCountries6","IncludedCountries7","IncludedCountries8", "ExcludedCountries", "ExcludedCountries1", "ExcludedCountries2", "ExcludedCountries3", "ExcludedCountries4", "ExcludedCountries5",
            "ExcludedCountries6", "ExcludedCountries7", "ExcludedCountries8",
            "MediaChannel","MediaChannel1","MediaChannel2","MediaChannel3","MediaChannel4","MediaChannel5","MediaChannel6","MediaChannel7","MediaChannel8",
            "RestrictionDescription","RestrictionDescription1","RestrictionDescription2","RestrictionDescription3","RestrictionDescription4","RestrictionDescription5","RestrictionDescription6","RestrictionDescription7","RestrictionDescription8",
            "RightsManagementType","RightsManagementType1","RightsManagementType2","RightsManagementType3","RightsManagementType4","RightsManagementType5","RightsManagementType6","RightsManagementType7","RightsManagementType8",
            "extracted-pdf","copy-content-of","extracted-pdf-child", "participant-subscribed","belong-to-video","participant-collection-on-subscribed","extracted-xml","is-derivative-of","contains",
            "derivative","is-place-graphic-of",
            "belong-to", "copy-contains","is-parent-of","extracted-xml-child","placed-graphics","is-child-of","has-video-clips"});

    private    List LHSplitReplaceColumnNames =
            Arrays.asList(new String[] {"&OBJECT_TYPE",	"&ACTION","&ASSET_ID","&FOLDER_TYPE","&MODEL","&PARENTS","&ASSET_NAME","&POLICIES","&EXPORT_PATH","Tag",
                    "Asset in Perpetuity?","Asset in Perpetuity? 1","Asset in Perpetuity? 2","Asset in Perpetuity? 3","Asset in Perpetuity? 4","Asset in Perpetuity? 5","Asset in Perpetuity? 6","Asset in Perpetuity? 7","Asset in Perpetuity? 8",
                    "Language","Customer Specific","Campaign/Promotional Name", "Mars Campaign Code","Rights Restricted?","Is Latest Version",
                    "Transparent Background?","Platform Specifications","Source File?","Data Classification","Language","Image Position/Slot","Brand","Sub-Brand","Segment",
                    "Master or Localization/Adaptation?","Product Image Angle", "Internal ZREP","Global/Regional/Local","Global/Regional/Local 1","Global/Regional/Local 2","Global/Regional/Local 3",
                    "Global/Regional/Local 4", "Global/Regional/Local 5","Global/Regional/Local 6","Global/Regional/Local 7","Global/Regional/Local 8","Included Countries","Included Countries 1",
                    "Included Countries 2","Included Countries 3","Included Countries 4","Included Countries 5","Included Countries 6","Included Countries 7","Included Countries 8",
                    "Excluded Countries","Excluded Countries 1","Excluded Countries 2 ","Excluded Countries 3","Excluded Countries 4","Excluded Countries 5","Excluded Countries 6","Excluded Countries 7","Excluded Countries 8",
                    "Media Channel", "Media Channel 1", "Media Channel 2", "Media Channel 3", "Media Channel 4", "Media Channel 5", "Media Channel 6", "Media Channel 7", "Media Channel 8",
                    "Restriction Description","Restriction Description 1","Restriction Description 2","Restriction Description 3","Restriction Description 4","Restriction Description 5","Restriction Description 6","Restriction Description 7","Restriction Description 8",
                    "Rights Management Type","Rights Management Type 1","Rights Management Type 2","Rights Management Type 3","Rights Management Type 4","Rights Management Type 5","Rights Management Type 6","Rights Management Type 7","Rights Management Type 8",
                    "&LINKS:ARTESIA.LINKTYPE.EXTRACTEDPDF", "&LINKS:ARTESIA.LINKTYPE.COPYCONTENTOF",
                    "&LINKS:ARTESIA.LINKTYPE.EXTRACTEDPDF.CHILD","&LINKS:USR-AST",
                    "&LINKS:PV","&LINKS:USR-CST","&LINKS:ARTESIA.LINKTYPE.EXTRACTEDXML","&LINKS:DERIVATIVE_OF","&LINKS:CONTAINS","&LINKS:DERIVATIVE","&LINKS:ARTESIA.LINKTYPE.ISPLACEDGROF",
                    "&LINKS:BELONGTO", "&LINKS:COPYCONTENT","&LINKS:PARENT","&LINKS:ARTESIA.LINKTYPE.EXTRACTEDXML.CHILD","&LINKS:PLACEDGR","&LINKS:CHILD","&LINKS:CLIP2VID"});

}
