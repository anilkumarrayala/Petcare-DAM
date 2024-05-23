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
            Arrays.asList(new String[] {"&OBJECT_TYPE",	"&ACTION","&ASSET_ID","&FOLDER_TYPE","&MODEL","&PARENTS","&ASSET_NAME","LighthouseSecurityPolicy","&EXPORT_TIME","&EXPORT_PATH","AssetID", "Title" ,
                    "LegacyAssetVersion","ImportedDate", "EmbargoDate","ExpirationDate","IsLatestVersion",
                    "AgencyPartner", "AssetCategory","AssetType","AssetSubType", "AssetCreationDate","AssetDate","Status","DeliverableType","CampaignName", "MarsCampaignCode","CampaignEndDate","CampaignStartDate",
                    "CustomerSpecific","DataClassification","Description","Keywords","FileName","HealthBenefit","SourceFile", "TransparentBackground",
                    "NetWeight","PetLifestage","PieceCount","PlatformSpecifications","RightsRestricted","PetSize","Brightcove Asset ID","Brightcove Asset Name","BrightcoveRemovedBy","BrightcoveRemovedDate",
                    "BrightcoveAssetStatus", "MasterOrLocalizationAdaptation","Segment","Flavor","ProductCategory","RightsManagementType","Recipe", "Region/MarketingCountry","ProductImageAngle",
                    "Occasion","OccasionYear","OriginatingCountry","FERT","Claims", "Display/PackType","CampaignPromotionalYear","AssetEmail", "Tag","InPerpetuity", "Brand","Language","GTIN/EAN/UPC","ImagePosition",
                    "VERP","SubBrand","BrandandSub-Brand","CoverageStartDate","CoverageEndDate","InternalZREP","Global/Regional/Local","IncludedCountries","ExcludedCountries","RestrictionDescription","MediaChannel"
                    ,"is-related-to","extracted-pdf","copy-content-of","extracted-pdf-child","participant-subscribed","belong-to-video","participant-collection-on-subscribed","extracted-xml",
                    "is-derivative-of","contains","is-child-of","derivative","is-place-graphic-of", "belong-to","has-video-clips", "copy-contains","is-parent-of","extracted-xml-child","placed-graphics"
            });


    private    List LightHouseColumnName =
            Arrays.asList(new String[] {"ID", "Name" , "Asset Version" ,
                    "Agency/Partner/Vendor","Category/Type/Sub-Type 2", "Category/Type/Sub-Type 3", "Asset Status","Description"
                    ,"Description/Keywords","Original File Name","Health Benefit","Net Weight","Pet Lifestage","Piece Count","Pet Size","Brightcove Asset ID",
                    "Brightcove Asset Name","Removed By","Brightcove Asset Status","Segment/Product Category 2","Recipe",
                    "Occasion 1","Occasion Year","Originating Country","Claims","Display/Pack Type","Campaign/Promotional Year","Asset Owner"});

    private List AprimoColumnName = Arrays.asList(new String[] {"AssetID", "Title" , "LegacyAssetVersion",
            "AgencyPartner","AssetType","AssetSubType","Status","Description","Keywords","FileName","HealthBenefit",
            "NetWeight","PetLifestage","PieceCount","PetSize","Brightcove Asset ID","BrightcoveAssetName","BrightcoveRemovedBy",
            "BrightcoveAssetStatus","ProductCategory","Recipe", "Occasion","OccasionYear","OriginatingCountry","Claims",
            "Display/PackType","CampaignPromotionalYear","AssetEmail"});

    public List getAprimoDateColumnName() {
        return AprimoDateColumnName;
    }

    public List getLHDateColumnName() {
        return LHDateColumnName;
    }


    private  List AprimoDateColumnName = Arrays.asList(new String[] {"&EXPORT_TIME","AssetCreationDate","ImportedDate", "EmbargoDate","ExpirationDate","AssetDate",
            "CampaignEndDate","CampaignStartDate", "BrightcoveRemovedDate","CoverageStartDate","CoverageEndDate","is-related-to"});

    private    List LHDateColumnName =
            Arrays.asList(new String[] {"&EXPORT_TIME","Asset Creation Date","Date: Imported", "Embargo Date","Expiration Date","Asset Live Date",

                    "Campaign End Date","Campaign Start Date", "Removed Dt", "Coverage Start Date","Coverage End Date", "&LINKS:RELATED"});


    public List getAprimoSplitReplaceColumnNames() {
        return AprimoSplitReplaceColumnNames;
    }

    public List getLHSplitReplaceColumnNames() {
        return LHSplitReplaceColumnNames;
    }





    private  List AprimoSplitReplaceColumnNames = Arrays.asList(new String[] { "&OBJECT_TYPE",	"&ACTION","&ASSET_ID","&FOLDER_TYPE","&MODEL","&PARENTS","&ASSET_NAME","LighthouseSecurityPolicy","&EXPORT_PATH",
            "Tag","InPerpetuity","Language","CustomerSpecific","CampaignName", "MarsCampaignCode","RightsRestricted","IsLatestVersion",
            "TransparentBackground","PlatformSpecifications","SourceFile","DataClassification","Language","ImagePosition","SubBrand","Segment",
            "MasterOrLocalizationAdaptation","ProductImageAngle", "InternalZREP","Global/Regional/Local","IncludedCountries","ExcludedCountries","MediaChannel","RestrictionDescription",
            "RightsManagementType","extracted-pdf","copy-content-of","extracted-pdf-child",
            "participant-subscribed","belong-to-video","participant-collection-on-subscribed","extracted-xml","is-derivative-of","contains",
            "derivative","is-place-graphic-of",
            "belong-to", "copy-contains","is-parent-of","extracted-xml-child","placed-graphics","is-child-of","has-video-clips"});

    private    List LHSplitReplaceColumnNames =
            Arrays.asList(new String[] {"&OBJECT_TYPE",	"&ACTION","&ASSET_ID","&FOLDER_TYPE","&MODEL","&PARENTS","&ASSET_NAME","&POLICIES","&EXPORT_PATH"
                    ,"Tag","Asset in Perpetuity?","Language","Customer Specific","Campaign/Promotional Name", "Mars Campaign Code","Rights Restricted?","Is Latest Version",
                    "Transparent Background?","Platform Specifications","Source File?","Data Classification","Language","Image Position/Slot", "Sub-Brand","Segment",
                    "Master or Localization/Adaptation?","Product Image Angle", "Internal ZREP","Global/Regional/Local","Included Countries","Excluded Countries",
                    "Media Channel","Restriction Description","Rights Management Type","&LINKS:ARTESIA.LINKTYPE.EXTRACTEDPDF", "&LINKS:ARTESIA.LINKTYPE.COPYCONTENTOF",
                    "&LINKS:ARTESIA.LINKTYPE.EXTRACTEDPDF.CHILD","&LINKS:USR-AST",
                    "&LINKS:PV","&LINKS:USR-CST","&LINKS:ARTESIA.LINKTYPE.EXTRACTEDXML","&LINKS:DERIVATIVE_OF","&LINKS:CONTAINS","&LINKS:DERIVATIVE","&LINKS:ARTESIA.LINKTYPE.ISPLACEDGROF",
                    "&LINKS:BELONGTO", "&LINKS:COPYCONTENT","&LINKS:PARENT","&LINKS:ARTESIA.LINKTYPE.EXTRACTEDXML.CHILD","&LINKS:PLACEDGR","&LINKS:CHILD","&LINKS:CLIP2VID"});

}
