package constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LookupConstants {

    public static List<String> getFlavorNames() {return flavorNames;}

    private static List<String> flavorNames = new ArrayList<>(Arrays.asList(
            "Apple", "Bacon", "BaconandBeef", "BaconandEgg", "BaconandFiletMignon", "BaconEggandCheese", "Banana",
            "BBQChicken", "Beef", "BeefandBacon", "BeefandBarley", "BeefandCheese", "BeefandChicken", "BeefandLiver",
            "BeefandPotato", "BeefandRice", "BeefandSalmon", "BeefandStew", "BeefandVegetable", "BeefandVegetables",
            "BeefStew", "BeefStroganoff", "BeefBaconandCheese", "BeefBaconandChicken", "BeefBroccoliandBrownRice", "BeefCheeseandBacon", "BeefChickenandLiver", "BeefChickenandSalmon", "BeefFiletMignonChickenandSteak",
            "BeefMilkVegetablesandCereals", "BeefNoodlesandVegetables", "BeefRiceandCheese", "Blend", "Blueberry",
            "BraisedRib", "CatfishandTuna", "Catnip", "CatnipandChicken", "CatnipTunaandDairy", "Cheese", "CheeseandHam", "Cheeseburger",
            "CheesyChicken", "CheesyChickenPasta", "ChefInspiredChickenEntree", "Chicken", "ChickenandSalmon", "ChickenParsley",
            "ChickenandBarley", "ChickenandBeef", "ChickenandBeefStew", "ChickenandCarrot", "ChickenandCatfish", "ChickenandCheddar",
            "ChickenandCheese", "ChickenandChickpeas", "ChickenandEgg", "ChickenandFiletMignon", "ChickenandLamb", "ChickenandLentils",
            "ChickenandLiver", "ChickenandMint", "ChickenandOatmeal", "ChickenandOceanFish", "ChickenandParsley", "ChickenandPea",
            "ChickenandPotato", "ChickenandRice", "ChickenandSeafood", "ChickenandShrimp", "ChickenandTuna", "ChickenandTurkey",
            "ChickenandTurkeyCasserole", "ChickenandVeal", "ChickenandVegetable", "ChickenandVegetables", "ChickenRiceandLambRice", "ChickenBaconandCheese",
            "ChickenBeefandLamb", "ChickenBeefandLiver", "ChickenBeefandRice", "ChickenBeefandTurkey", "ChickenCarrotsandGreens",
            "ChickenCatnipandCheddar", "ChickenCatnipandCheese", "ChickenEggandSalmon", "ChickenFishandVegetables", "ChickenLambandRice", "ChickenLambandSalmon",
            "ChickenLambTurkeySalmon", "Chickenlivershrimp", "ChickenLiverandBeef", "ChickenLiverBeefBaconandCheese", "ChickenNoodleandVegetables",
            "ChickenPotatoandPeas", "ChickenRiceandOatmeal", "ChickenRiceandTurkey", "ChickenRiceandVegetable", "ChickenRiceandVegetables",
            "ChickenRiceBeefRiceandCheese", "ChickenSalmonandBeef", "ChickenSalmonandEgg", "ChickenSalmonandTuna", "ChickenSalmonandTurkey",
            "ChickenTunaCatnipandSeafood", "ChickenTunaDairyandSeafood", "ChickenTunaTurkeyandBeef", "ChickenTurkeyandCheddar",
            "ChunkyChickenandTurkeyStew", "CodandShrimp", "CountryStew", "Crab", "CrabandTuna", "CrispApple", "CrispAppleChickenandparsley",
            "Dairy", "DeepOceanFish", "DeepSeafoodandFish", "Duck", "DuckandLentils", "DuckandPea", "DuckandPotato", "DuckandRice",
            "EggandSausage", "FarmRaisedChickenLentilsSweetPotatoRecipe", "FarmRaisedChickenRiceSweetPotatoRecipe", "FiletMignon",
            "FiletMignonandBeef", "FiletMignonandNewYorkStrip", "FiletMignonandSteak", "FiletMignonFlavor", "FiletMignonBaconandPotato",
            "FilletSteakBakedwithDill", "Fish", "FishandRice", "FishandSweetPotato", "FishandTuna", "FishandVegatable",
            "FishRiceandPotato", "FishWhitefishTunaCodandShrimp", "Fresh", "FreshLiverFlavor", "Freshmint",
            "FriedLambChopswithCauliflowerPotato", "Fruit", "GrainFree", "GrandmasFarmStewwithLambandRice", "GrassFedLambandBrownRiceDinner",
            "GrilledChickenandNYStrip", "GrilledChickenFlavor", "GrilledSalmonRiceandVegetable", "GrilledSteakandVegetable",
            "Hairtail", "Ham", "HamandEgg", "HarvestPotluckwithTurkey", "HealthyChickenandRiceStew", "HealthyDinnerwithLambandVegetables", "HealthySkinandCoat",
            "Herring", "HerringandSalmon", "HerringandSweetPotato", "HickorySmoke", "HickorySmokedBBQ", "HipandJointCare", "HolidaySpice",
            "Kangaroo", "KangarooandRedLentils", "Lamb", "LambandLentils", "LambandLiver", "LambandPotato", "LambandRice", "LambandTurkey",
            "LambandVegetables", "LambandVenison", "LambMealandRice", "LambPotatoandPeas", "LambRiceandVegetable", "LambVegetablesandChicken",
            "LIDFishandPotatoRecipe", "LIDLambandPotatoRecipe", "LIDTurkeyandPotatoRecipe", "LittleYellowCroaker", "Liver", "LiverandBeef", "Lobster", "MackerelandWhitefish",
            "MeatLasagna", "MeatyLambandRiceStew", "Milk", "MincedChicken", "Mint", "MixedBerry", "MixedGrill", "MultipleFlavors", "NewYorkStrip",
            "NYStrip", "OatmealandPumpkin", "OceanFish", "OceanFishandRice", "OceanFishandTuna", "OceanWhitefishandTuna", "Original",
            "OriginalFreshandBeef", "Other", "Peanut", "PeanutButter", "PeanutButterandHoney", "PoachedSalmon", "Pork", "PorkPotatoesandGreenBeans",
            "PorterhouseSteak", "PorterhouseSteakandVegetable", "PotRoast", "PotRoastandVegetables", "PotherbandBeef", "Poultry", "PrimeRib",
            "PrimeRibFiletMignonNewYorkStrip", "PrimeRibRiceandVegetable", "PumpkinSpice", "RealApple", "RealBanana", "RealPeanutButter", "RibeyeSteak",
            "RoastedChicken", "RoastedLambRiceandVegetable", "RoastedTurkeyandVegetableEntree", "RusticChickenandVegetabledinner",
            "Salmon", "SalmonTrout", "SalmonandChicken", "SalmonandEgg", "SalmonandGardenGreens", "SalmonandHerring", "SalmonandLentil",
            "SalmonandOceanfish", "SalmonandPotato", "SalmonandRedLentils", "SalmonandRice", "SalmonandShrimp", "SalmonandTuna",
            "SalmonMealandSunflowerOil", "SalmonChickenandTurkey", "SalmonPotatoandPeas", "SalmonRiceandVegetable", "SalmonWhitefishandTuna",
            "SalmonBrownRiceSweetPotatoRecipe", "Sardines", "Sausage", "SausageEggandCheese", "SavoryChicken", "SavoryLambandGardenVarietyEntree",
            "Seafood", "SeafoodandTomatoBisque", "SeafoodMedley", "SeafoodSalmonandTuna", "SeafoodSalmonCodandShrimp", "Shrimp",
            "ShrimpandTuna", "SignatureBeefandPotatoEntree", "SilverFishandTuna", "Sirloin", "SirloinandChicken", "SkipjackTuna",
            "SmokedBaconandCheddar", "SmokedBeef", "SmokedSalmon", "Steak", "SteakandChicken", "SteakandEggChickenandLiver", "SteakandEggs",
            "SteakandShrimp", "SteakandTuna", "SteakandVegetable", "SteakChickenandBacon", "SteakEggandCheese", "SteakPeasandCarrots",
            "SteakPotatoesBaconandCheese", "SteamedFishandSweetPotatoDinner", "Stew", "TenderBeefandVegetableRecipe", "TenderChickenandOatmealDinner",
            "TenderChickenandRiceRecipe", "TenderChickenandTurkeyRecipe", "TenderlambandRiceRecipe", "TenderLambRecipe", "ThunnusTonggolandSkipjackTuna",
            "ThunnusTonggolGrunionandSkipjackTuna", "Trout", "Tuna", "TunaandCheese", "TunaandChicken", "TunaandEgg", "TunaandMackerel",
            "TunaandSalmon", "TunaandShrimp", "TunaandTurkey", "TunaFlavor", "TunaChickenandSalmon", "TunaChickenandVegetable", "TunaSalmonandTurkey",
            "TunaSalmonChickenandTurkey", "TunaShrimpandCrab", "TunaShrimpandSalmon", "Turkey", "TurkeyandBacon", "TurkeyandChicken",
            "TurkeyandCranberry", "TurkeyandGiblets", "TurkeyandLamb", "TurkeyandLiver", "TurkeyandPotRoast", "TurkeyandPotato", "TurkeyandRice",
            "TurkeyandVegetable", "TurkeyGreenBeansandPotatoes", "TurkeyLambandChicken", "TurkeyPotatoandPeas", "TurkeyRiceandVegetables", "TurkeySpinachandCheese",
            "Variety","Vegetable", "Venison", "VenisonandGreenLentils", "VenisonRiceandOatmeal", "WeightManagement", "WhiteFishandRice",
            "Whitefish", "WhitefishandTuna", "WildBlueberryandPomegranate", "OceanFishExcludingHairball", "SeafoodFlavor", "ChefInspiredChickenEntrée",
            "Flavor"

            ));

    public static ArrayList<String> getBrands() {
        return brands;
    }

    public static ArrayList<String> getSubBrandNames() {
        return SubBrandNames;
    }

    public static ArrayList<String> getAssetCategories() {
        return AssetCategories;
    }

    public static ArrayList<String> getAssetTypes() {
        return AssetTypes;
    }

    public static ArrayList<String> getAssetSubType() {
        return AssetSubType;
    }

    public static ArrayList<String> getRegions() {
        return regions;
    }

    public static ArrayList<String> getMarketingCountries() {
        return marketingCountries;
    }

    public static ArrayList<String> getOriginatingCountries() {
        return originatingCountries;
    }

    static ArrayList<String> brands = new ArrayList<>(Arrays.asList(
            "Aquarian", "Canigou", "Catsan", "Cesar", "Chappi", "Chappie", "Chum", "Corporate",
            "Crave", "Dreamies", "DreamiesTemptations", "Frolic", "GoodlifeRecipe", "Greenies",
            "Iams", "JamesWellbeloved", "Karma", "Kitekat", "Koziol", "Misfits", "MultiBrand",
            "NaturesTable", "Natusan", "Nutro", "Optimum", "Pal", "Pedigree", "PerfectFit",
            "Promanage", "Sheba", "Thomas", "Trill", "Unbranded", "Waltham", "Whiskas",
            "Whistle", "WildFrontier", "Advance", "Schmackos", "Dine", "MyDog", "Spillers",
            "Nuzzle", "Champ", "Exelpet", "GoldenGrain", "Goodo", "Harmony", "Quartett"

            ));

    static ArrayList<String> SubBrandNames = new ArrayList<>(Arrays.asList(
            "Aquarian", "Canigou", "ActiveFresh", "BiancoFresh", "HygienePlus", "Catsan", "NaturalClumping",
            "Naturelle", "Ultra", "Bakies", "Bistro", "BreakfastDinner", "Classics", "Crunchy", "Entrees",
            "Filets", "FinestCuts", "HomeDelights", "JerkyBites", "MeatyBites", "MiniSticks", "Mixed",
            "Mixer", "Cesar", "NaturalGoodness", "NongQingHuaYu", "Pal", "PoultryLovers", "Puppy",
            "SavoryDelights", "SimplyCrafted", "Softies", "SteakChickenLovers", "SteakPoultryLovers",
            "SteakLovers", "Sunrise", "Supreme", "Temptations", "WetMinis", "WholesomeBowls", "Chappi",
            "Chappie", "Chum", "Corporate", "MarrowboneAncientGrains", "Creamy", "MeatySticks", "Mix",
            "ShakeUps", "CheezyMiddles", "JumboStuff", "MixUps", "DreamiesTemptations", "SnackyMouse",
            "SnackyPenguin", "Tumblers", "Frolic", "IndoorRecipe", "Natural", "AgingCare", "Bites",
            "DentalChews", "FelineNutrition", "FlavorFusion","GrillStyle", "HipJoint", "Jointcare",
            "Jumbone", "Greenies", "NaturalBalance", "OXY2", "PillPockets", "Pockets", "SeasonsGreenies",
            "SmartBites", "Supplements", "WeightManagement", "BreedSpecificBulldog", "BreedSpecificChihuahua",
            "BreedSpecificDachshund", "BreedSpecificGermanShepherd", "BreedSpecificLabradorRetriever",
            "BreedSpecificYorkshireTerrier", "Chunks", "GrainFreeNaturals", "GrainFreeRecipe", "HairballCare",
            "HealthyAdultOriginal", "HealthyDigestion", "HealthyKitten", "HealthyNaturals", "HealthySenior",
            "HighProtein", "IndoorWeightHairballCare", "Kitten", "LargeBreed", "LivelySenior", "MatureAdult",
            "MatureAdultHairballCare", "Minichunks", "MultiCatComplete", "Iams", "OptimalMetabolism", "OptimalWeight",
            "OralCareComplete", "PerfectPortions", "PremiumPate", "PremiumProtection", "ProActive", "ProActiveHealth",
            "ProActiveHealthHairball", "ProActiveHealthOralCare", "PurrfectDelicacies", "PurrfectDelights", "PurrfectGrainFree",
            "SeniorPlus", "SensitiveNaturals", "SensitiveSkinStomach", "SensitiveStomach", "Shakeables", "SmallToyBreed",
            "SmartPuppy", "SoGood", "Treats", "UrinaryTractHealth", "WoofDelights", "WoofGrainFree",
            "JamesWellbeloved", "Superfoods", "Karma", "Kitekat", "Koziol", "Misfits", "MultiBrand", "NaturesTable",
            "Natusan", "1200Series", "FarmsHarvest", "GrainFree", "HeartyStews", "KitchenClassics", "LimitedIngredientDiet",
            "Max", "MiniBites", "Nutro", "NaturalChoice", "NaturalChoiceCompleteCare", "Ocean", "PetiteEats", "PremiumLoaf",
            "Supremo", "WholesomeEssentials", "WildFrontier", "Optimum", "Pal", "BigDogs", "Biscrok", "Breathbuster",
            "ButchersSelects", "Casseroles", "ChoiceCuts", "ChoppedGround", "ChunkyGround", "Cuisine", "DeepClean", "Dentabone",
            "Dentarask", "Dentastix", "DentastixChewyChunk", "DentaX", "Expert", "HealthyMaturity", "HealthyWeight", "HomestyleFavorites",
            "LittleChampions", "Marathon", "Markies", "Marrobites", "Marrobone", "Mealbone", "Mealtime", "Meatloaf", "Minis",
            "Pedigree", "NoodleBowl", "NutriTwists", "Prime", "ProfessionalNutrition", "Protein", "PuppyDentatubos", "Ranchos",
            "Rodeo", "RodeoDuos", "Schmackos", "SmallDog", "Smilers", "SportingChampions", "Stackerz", "Superchew", "Tabs",
            "TastyMinis", "TenderGoodness", "Traditional", "Trainer", "PerfectFit", "NaturalVitality", "Promanage", "Sheba",
            "Puree", "SignatureBroths", "TenderSticks", "Thomas", "Trill", "Unbranded", "CanineVeterinaryDiet", "FelineVeterinaryDiet",
            "Waltham", "CatMilk", "CatTreats", "Chunky", "Crunch", "Dentabites", "Duo", "Favourites", "MilkyTreats",
            "Whiskas", "Perfection", "PureDelight", "PurrfectlyChicken", "PurrfectlyFish", "SeafoodSelections", "Selection",
            "SpecialtyRices", "TastyMix", "TenderBites", "TenderMorsels", "Ultramilk", "Whistle", "WildFrontier",
            "Advance", "BackyardCookout", "BaconFeast", "DoubleDelights", "FreshChef", "Crave", "CreamyPurrree",
            "VitalityPlus", "Schmackos", "Dine", "MyDog", "Spillers", "Nuzzle", "NA","Dreamies",
            "GoodlifeRecipe",
            "AquarianNA",
            "CanigouNA",
            "CatsanNA",
            "CesarNA",
            "ChappiNA",
            "ChappieNA",
            "ChumNA",
            "CorporateNA",
            "DreamiesTemptationsNA",
            "FrolicNA",
            "GreeniesNA",
            "IamsNA",
            "JamesWellbelovedNA",
            "KarmaNA",
            "KitekatNA",
            "KoziolNA",
            "MisfitsNA",
            "MultiBrandNA",
            "NaturesTableNA",
            "NatusanNA",
            "NutroNA",
            "OptimumNA",
            "PalNA",
            "PedigreeNA",
            "PerfectFitNA",
            "PromanageNA",
            "ShebaNA",
            "Purée",
            "ThomasNA",
            "TrillNA",
            "UnbrandedNA",
            "WalthamNA",
            "WhiskasNA",
            "WhistleNA",
            "WildFrontierNA",
            "AdvanceNA",
            "SchmackosNA",
            "DineNA",
            "MyDogNA",
            "SpillersNA",
            "NuzzleNA",
            "Champ",
            "ChampNA",
            "Exelpet",
            "ExelpetNA",
            "GoldenGrain",
            "GoldenGrainNA",
            "Goodo",
            "GoodoNA",
            "Harmony",
            "HarmonyNA",
            "Quartett",
            "QuartettNA",
            "PetNutrition",
            "RoyalCanin",
            "CraveNA",
            "Core",
            "ClassicSoups",
            "ClassicsInTerrine",
            "FineFlakes",
            "Kitten",
            "ShebaNA",
            "NaturesCollection",
            "SauceCollection",
            "SelectSlices",
            "PromanageNA",
            "FarmsHarvestCore"



    ));

    static ArrayList<String> AssetCategories = new ArrayList<>(Arrays.asList(
            "3DRenders", "BrandAssets", "CorporateAssets", "Digital", "LighthouseTemplating", "Print", "SalesMaterials",
            "Video"

            ));
    static ArrayList<String> AssetTypes = new ArrayList<>(Arrays.asList(
            "LabellingBarcodes", "OptimizedImages", "Packaging", "BrandGuidelines", "CreativeTools",
            "Logos", "ProductImages", "Toolkits", "Documents", "Online", "Templates", "Banners", "ECommerce", "Mobile",
            "Signage", "SocialContent", "WebsiteMicrosite", "Motion", "Static", "Ads", "Coupon", "InStoreMerchandising",
            "Film", "InShowSponsorships", "TraditionalOnlineVideoContent", "Video", "ECommerceEnhancedContent"

            ));

    static ArrayList<String> AssetSubType = new ArrayList<>(Arrays.asList(
            "FeedingGuidelines", "GTINBarcode", "GuaranteedAnalysis", "Ingredients", "IngredientsNFP", "Nutrition",
            "MobileHero", "OnPackCreative", "DesignLock", "MarketReady", "Mechanical", "StyleGuides", "Character",
            "CreativeSupportElement", "Font", "KeyVisuals", "Photography", "PPTTemplate", "Recipes", "TaglineAsset",
            "WallGraphic", "LogoElements", "LogoGuideline", "LogoVariants", "PrimaryLogos", "SecondaryLogos", "BareProduct",
            "WrappedProduct", "PrimaryAsset", "SupplementalAssets", "CreativeSupportElements", "Iconography", "Illustration",
            "InfographicandDiagrams", "MIcon", "Purpose", "TheFivePrinciples", "CodeofConduct", "Playbook", "Policy", "Poster",
            "PressRelease", "Report", "BrandSquares", "InternalIdentifier", "Type1", "Type2", "Type3", "Banners", "GIFs",
            "SocialContent", "Website", "BusinessStationery", "PowerPoint", "Word", "BrandPurpose", "BRoll", "CorporateEssential",
            "OnlineVideoContent", "Animated", "Static", "BrandEquity", "Channel", "Claims", "Count", "EnhancedImages",
            "EnhancedIngredientList", "FeaturesandBenefits", "Lifestyle", "OutofPackage", "ProductAssortment", "SizeComparison",
            "VisualIngredients", "VisualInstructions", "Application", "InAppUnit", "InFeed", "Reels", "Stories", "ContentUpload",
            "DesignBuild", "LandingPage", "Updates", "External", "Internal", "ExternalDigital", "ExternalPrint", "InternalDigital",
            "InternalPrint", "Billboards", "PlaceBased", "StreetFurniture", "Transit", "POSCollateral", "PrepackDisplay", "CinemaexclusivelyforCinemause",
            "TVC", "ProductPlacement", "LongFormOver1minute", "ShortFormlessthan1minute", "NA", "A+Content"

            ));

    static ArrayList<String> regions = new ArrayList<>(Arrays.asList(

            "AsiaPacific", "Europe", "Global", "NorthAmerica",
            "MiddleEastAfrica", "LatinAmerica"
    ));
    static ArrayList<String> marketingCountries = new ArrayList<>(Arrays.asList(
            "Australia", "China", "FrenchPolynesia", "HongKong", "India", "Indonesia", "Japan", "Korea",
            "Malaysia", "Mongolia", "NewCaledonia", "NewZealand", "PapuaNewGuinea", "Philippines",
            "Singapore", "Taiwan", "Thailand", "Vietnam", "Albania", "Austria", "Belgium",
            "BosniaHerzegovina", "Bulgaria", "Croatia", "Cyprus", "CzechRepublic", "Denmark", "Estonia", "Finland",
            "France", "Germany", "Greece", "Hungary", "Iceland", "Ireland", "Italy", "Kosovo", "Latvia",
            "Lithuania", "Macedonia", "Moldova", "Montenegro", "Netherlands", "Norway", "Poland",
            "Portugal", "Romania", "Serbia", "SlovakRepublic", "Slovenia", "Spain", "Sweden", "Switzerland",
            "Ukraine", "UnitedKingdom", "Global", "MarsITR", "Argentina", "Brazil", "Chile", "Colombia",
            "CostaRica", "DominicanRepublic", "Ecuador", "Guatemala", "Mexico", "Panama", "Peru", "PuertoRico",
            "Venezuela", "Algeria", "Angola", "Armenia", "Azerbaijan", "Bahrain", "Belarus", "Benin",
            "Botswana", "Congo", "Djibouti", "Egypt", "Ethiopia", "Georgia", "Ghana", "Iraq", "Israel",
            "IvoryCoast", "Jordan", "Kazakhstan", "Kenya", "Kuwait", "Kyrgyzstan", "Lebanon", "Libya",
            "Morocco", "Mozambique", "Namibia", "Nigeria", "Oman", "Qatar", "Russia", "Rwanda",
            "SaudiArabia", "Senegal", "SierraLeone", "SouthAfrica", "Sudan", "Syria", "Tajikistan", "Tanzania",
            "Tunisia", "Turkey", "Turkmenistan", "Uganda", "UnitedArabEmirates", "Uzbekistan",
            "Yemen", "Zimbabwe", "Canada", "UnitedStates"

            ));
    static ArrayList<String> originatingCountries = new ArrayList<>(Arrays.asList(
            "UnitedStates", "UnitedKingdom", "Canada", "China", "Poland", "Germany", "France", "Brazil",
            "Mexico", "NewZealand", "Australia", "Japan", "Russia", "Korea", "Philippines", "Argentina", "India",
            "Ukraine", "Ireland", "Chile", "Malaysia", "PuertoRico", "Thailand", "Colombia", "Italy", "Belgium",
            "Greece", "AsiaPacific", "Taiwan", "NorthAmerica", "Spain", "Europe", "HongKong", "Global",
            "Netherlands", "Denmark", "Portugal", "Sweden", "NA"

            ));

    public static ArrayList<String> getDivision() {
        return division;
    }

    static ArrayList<String> division = new ArrayList<>(Arrays.asList(
            "PetNutrition", "RoyalCanin"
    ));
}
