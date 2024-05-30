package constants;

import java.util.ArrayList;
import java.util.Arrays;

public class LookUpConstants2 {

    static ArrayList<String> languages = new ArrayList<>(Arrays.asList(
            "Albanian", "Arabic", "Armenian", "Azerbaijani", "Bosnian", "Bulgarian", "Croatian",
            "Czech", "Danish", "Dutch", "English", "Estonian", "Farsi", "Finnish", "French",
            "Georgian", "German", "Greek", "Hebrew", "Hindi", "Hungarian", "Icelandic",
            "Indonesian", "Italian", "Japanese", "Kazakh", "Korean", "Kyrgyz", "Latvian",
            "Lithuanian", "Macedonian", "Malay", "Mandarin", "Moldavian", "Norwegian", "Polish",
            "Portuguese", "Romanian", "Russian", "Serbian", "Slovak", "Slovenian", "Spanish",
            "Swedish", "Tadjik", "Taglish", "Thai", "Turkmen", "Ukrainian", "Uzbek", "Vietnamese",
            "Turkish", "Flemish", "Cantonese", "ChineseSimplified", "ChineseTraditional"
    ));

    static ArrayList<String> customerSpecific = new ArrayList<>(Arrays.asList(
            "JD", "Tmall", "Amazon", "Target", "Costco", "Walmart", "HealthPets", "Petco",
            "Kroger", "Woolworths", "Chewy", "Loblaws", "SamsClub", "Coles", "NA"
    ));
    static ArrayList<String> rightsRestricted = new ArrayList<>(Arrays.asList(
            "Yes",
            "No"));
    //need to check
    static ArrayList<String> rightsManagementType = new ArrayList<>(Arrays.asList(
            "AVO",
            "Image Rights",
            "Music License",
            "Musicians",
            "On-Camera Talent/Model"
    ));
    //need to check
    static ArrayList<String> MasterOrLocalised = new ArrayList<>(Arrays.asList(
            "Master File",
            "Localized File",
            "Adapted File"
    ));

    static ArrayList<String> PetLifeStage = new ArrayList<>(Arrays.asList(
            "All",
            "Adult",
            "Senior",
            "Kitten",
            "Puppy",
            "N/A"
    ));
    static ArrayList<String> PetSizes = new ArrayList<>(Arrays.asList(
            "Small", "Medium", "Large", "Mini", "Regular", "SmallMedium", "MediumLarge", "ExtraLarge", "NA"
    ));

    static ArrayList<String> healthBenefits = new ArrayList<>(Arrays.asList(
            "GrainFree", "FurShinyFormula", "ControlHairBallFormula",
            "DentalCare", "HealthyBodyFormula", "DigestibleFormula",
            "BrightEyeHairFormula", "BoneStrongFormula", "ResistanceFormula",
            "NA"
    ));

    static ArrayList<String> species = new ArrayList<>(Arrays.asList(
            "Cat",
            "Dog",
            "Rabbit" ));


    static ArrayList<String> displayPackTypes = new ArrayList<>(Arrays.asList(
            "BackerCard", "Bag", "BagPaper", "BagPlastic", "Base", "BlisterCard", "BlisterPack",
            "Bottle", "Box", "Can", "CanLabel", "Carton", "ClipStrip", "Cup", "DisplayReadyCase",
            "DisplayReadyTray", "DisplayableCasesTrays", "Floorstand", "Flowbag", "Flowwrap",
            "FlowwrapBag", "FlowwrapSingleStick", "GiftBox", "GravityDisplay", "HalfPallet",
            "HolidayPack", "Label", "LabelCanisterWraparound", "LaydownBag", "Lid", "MetalTin",
            "MultiPack", "MultiWallBag", "NA", "OuterShrinkWrap", "Pack", "Packet", "PalletDisplay",
            "PegBag", "PlasticPack", "Polybag", "Pot", "Pouch", "ReclosablePackaging", "Sachet",
            "SeasonalOverwrap", "ShelfReadyPackaging", "Shipper", "ShipperProductasItShips",
            "ShipperProductInsideBox", "ShipperProductOutsideBox", "ShippingCase", "ShrinkWrap",
            "ShrinkWrapTin", "Sleeve", "StandUpBag", "StandUpPouch", "Stickers", "Tin",
            "TinShrinkWrap", "TowerDisplay", "Tray", "TrayBase", "TrayCover", "TrayDisplay",
            "TrayLid", "Tub", "Tube", "Wrapper"
    ));

    static ArrayList<String> breeds = new ArrayList<>(Arrays.asList(
            "Abyssinian", "AmericanBobtail", "AmericanBurmese", "AmericanCurl", "AmericanLonghair", "AmericanPolydactyl",
            "AmericanShorthair", "AmericanWirehair", "AphroditeGiant", "ArabianMau", "Asian", "AustralianBombay",
            "AustralianMist", "Balinese", "Bambino", "Bengal", "Bombay", "BrazilianShorthair", "BritishLonghair",
            "BritishShorthair", "Burmese", "Burmilla", "CaliforniaSpangled", "CalifornianRex", "CelticShorthair", "Ceylan", "ChantillyTiffany",
            "Chartreux", "Chausie", "ChinchillaLonghair", "Classicat", "CornishRex", "Cymric", "DevonRex", "DomesticLynx",
            "DonSphynx", "Donskoy", "EgyptianMau", "European", "EuropeanBurmese", "EuropeanShorthair", "ExoticShorthair",
            "GermanRex", "Giant", "Havana", "HighlandStraight", "IsleofMan", "JapaneseBobtail", "KhaoManee", "Korat",
            "KurilianBobtail", "LaPerm", "Lykoi", "MaineCoon", "Mandalay", "Manx", "Mau", "Medium", "MixedBreed",
            "MixedBreedGiant", "MixedBreedMedium", "MixedBreedXSmall", "Nebelung", "NevaMasquerade", "NorwegianForestCat", "Ocicat",
            "Oriental", "OrientalShorthair", "Persian", "Peterbald", "Pixiebob", "RagaMuffin", "Ragdoll", "Russian", "RussianBlue",
            "Russie", "SacredBirman", "Savannah", "Scottish", "ScottishFold", "Selkirk", "SelkirkRex", "Seychellois", "Siamese",
            "Siberian", "SiberianColourpoint", "Singapura", "Small", "Snowshoe", "Sokoke", "Somali", "Sphynx", "SuffolkCat",
            "Thai", "Tonkinese", "TurkishAngora", "TurkishVan", "Undefined", "UndefinedGiant", "Vankedisi", "XSmall",
            "Affenpinscher", "AkbashDog", "Akita", "AlaskanHusky", "AlaskanKleeKai", "AlaskanMalamute", "AmericanBulldog",
            "AmericanBully", "AmericanEskimoDog", "AmericanHairlessTerrier", "AmericanLeopardHound", "AmericanPitBullTerrier", "AmericanStaffordshireTerrier",
            "Anatoli", "AnatolianShepherd", "AngloFrancaisdePetiteVenerie", "AnjingKintamaniBali", "AppenzellCattleDog", "AppenzellerSennenhund",
            "AriegePointingDog", "Ariegeois", "ArtesianNormanBasset", "ArtoisHound", "AtlasMountainDogAidi", "AustralianCattleDog", "AustralianKelpie",
            "AustralianShepherd", "AustralianSilkyTerrier", "AustralianStumpyTailCattleDog", "AustrianBlackandTanHound", "AustrianPinscher",
            "AuvergnePointer", "Azawakh", "Barbet", "Basenji", "BassetArtesienNormand", "BassetBleudeGascogne", "BassetFauvedeBretagne",
            "BavarianMountainHound", "BeagleHarrier", "BeardedCollie", "BelgianGroenendaelSheepdog", "BelgianLaekenoisSheepdog", "BelgianMalinoisSheepdog",
            "BelgianSheepdog", "BergamascoShepherdDog", "BergerBlancSuisse", "BergerdaAuvergne", "BergerdeBeauce", "BergerPicard",
            "BernerSennenhund", "BerneseMountainDog", "BichonaPoilFrise", "BichonFrise", "BichonHavanais", "BlackandTanCoonhound",
            "BlackMouthCur", "Bloodhound", "BluetickCoonhound", "Bolognese", "BorderCollie", "BorderTerrier", "BosanskiOstrodlakiGonicBarak",
            "BostonTerrier", "BouledogueFrancais", "BouvierdesArdennes", "Boxer", "BraccoItaliano", "BrandlbrackeVieraugl", "BraquedaAuvergne",
            "BraquedelaAriege", "BraqueduBourbonnais", "BraqueFrancaisTypeGascogne", "BraqueFrancaisTypePyrenees", "BraqueSaintGermain",
            "BriquetdeProvence", "BriquetGriffonVendeen", "BrittanySpaniel", "Broholmer", "BrusselsGriffon", "Bulldog", "CadeBestiar",
            "CairnTerrier", "CanaanDog", "CaneCorso", "CaneCorsoItaliano", "CanedaPastoreBergamasco", "CanedaPastoreMaremmanoAbruzzese", "Caniche",
            "CanicheNain", "CanicheToy", "CaodaSerradaEstrela", "CaodeaguaPortugues", "CaodeCastroLaboreiro", "CaoFiladeSaoMiguel", "CarolinaDog",
            "CatahoulaLeopardDog", "CavalierKingCharles", "CavalierKingCharlesSpaniel", "CeskoslovenskyVlciak", "CeskyFousek", "CeskyTerier",
            "ChartPolski", "ChesapeakeBayRetriever", "ChiendaArtois", "ChiendeBergerBelge", "ChiendeBergerdesPyreneesaFaceRase", "ChiendeBergerdesPyreneesaPoilLong",
            "ChiendeMontagnedesPyrenees", "ChiendeSaintHubert", "Chihuahua", "Chihuahueno", "ChihuahuenodePeloLargo", "Chin", "ChineseCrestedDog", "ChineseLiHua", "ChodksyPes",
            "ChowChow", "CimarronUruguayo", "CiobanescRomanescCarpatin", "CiobanescRomanescdeBucovina", "CiobanescRomanescMioritic", "CirnecodellaEtna",
            "ClumberSpaniel", "Cockapoo", "Collie", "CollieRough", "CollieSmooth", "ContinentalBulldog", "CoonhoundRedbone", "CotondeTulear",
            "CrnogorskiPlaninskiGonic", "CurlyCoatedRetriever", "Dachshund", "DachshundLonghaired", "DachshundLonghairedMiniature", "DachshundLonghairedRabbit", "DachshundSmoothhaired",
            "DachshundSmoothhairedMiniature", "DachshundSmoothhairedRabbit", "DachshundWirehaired", "DachshundWirehairedMiniature", "DachshundWirehairedRabbit", "Dalmatian",
            "DalmatinskiPas", "DandieDinmontTerrier", "DanishSwedishFarmdog", "DanskSvenskGardshund", "Deerhound", "DeutschDrahthaar",
            "DeutschKurzhaar", "DeutschLanghaar", "DeutschStichelhaar", "DeutscheBracke", "DeutscherBoxer", "DeutscherJagdterrier",
            "DeutscherPinscher", "DeutscherSchaferhund", "DeutscherSpitzKlein", "DeutscherSpitzMittel", "DeutscherWachtelhund", "Doberman",
            "DobermanPinscher", "Dobermann", "DogoArgentino", "DoguedeBordeaux", "DoKhyi", "DrentschePatrijshond", "Drever", "DrotzoruMagyarVizsla",
            "Dunker", "EnglishBulldog", "EnglishCockerSpaniel", "EnglishCoonhound", "EnglishFoxhound", "EnglishPointer", "EnglishSetter",
            "EnglishShepherd", "EnglishSpringerSpaniel", "EnglishToyTerrier", "EntlebucherSennenhund", "EpagneulBleudePicardie", "EpagneulBreton",
            "EpagneuldePontAudemer", "EpagneulFrancais", "EpagneulNainContinental", "EpagneulNainContinentalPapillon", "EpagneulPicard",
            "ErdelyiKopo", "Eurasier", "FieldSpaniel", "FilaBrasileiro", "FinnishHound", "FlatCoatedRetriever", "FoxTerrierSmooth", "FoxTerrierWire",
            "FrancaisBlancetNoir", "FrancaisBlancetOrange", "FrancaisTricolore", "FrenchBulldog", "FrenchSpaniel", "GalgoEspanol", "GammelDanskHonsehund",
            "GermanPinscher", "GermanShepherd", "GermanShorthairedPointer", "GermanWireHairedPointer", "GiantSchnauzer", "GlenofImaalTerriers",
            "GoldenRetriever", "Goldendoodle", "GonczyPolski", "GordonSetter", "GosdaAturaCatala", "GotlanHound", "GrandAngloFrancaisBlancetNoir",
            "GrandAngloFrancaisBlancetOrange", "GrandAngloFrancaisTricolore", "GrandBassetGriffonVendeen", "GrandBleudeGascogne", "GrandGasconSaintongeois", "GrandGriffonVendeen",
            "GreatDane", "Greyhound", "GriffonaPoilDurKorthals", "GriffonBelge", "GriffonBleudeGascogne", "GriffonBruxellois", "GriffonFauvedeBretagne",
            "GriffonNivernais", "Gronlandshund", "GrosserMunsterlanderVorstehhund", "GrosserSchweizerSennenhund", "HaldenHound", "Haldenstover",
            "Hamiltonstovare", "HannoverscherSchweisshund", "Harrier", "Havanese", "HellinikosIchnilatis", "Hokkaido", "HollandseHerdershond",
            "HollandseSmoushond", "Hovawart", "HrvatskiOvcar", "Husky", "HygenHound", "Hygenhund", "IcelandicSheepdog", "IrishGlenofImaalTerrier",
            "IrishRedandWhiteSetter", "IrishRedSetter", "IrishSetter", "IrishSoftCoatedWheatenTerrier", "IrishTerrier", "IrishWaterSpaniel",
            "IrishWolfhound", "islenskurFjarhundur", "IstarskiKradkodlakiGonic", "IstarskiOstrodlakiGonic", "ItalianGreyhound", "JackRusselTerrier", "JackRussellTerrier",
            "Jamthund", "JugoslovenskiOvcarskiPasSarplaninac", "Kai", "KangalCobanKopegi", "KangalShepherdDog", "Kaninchen", "Karjalankarhukoira", "KavkazskaiaOvtcharka",
            "KerryBlueTerrier", "KingCharlesSpaniel", "Kishu", "KleinerMunsterlander", "Komondor", "KoreaJindoDog", "KraskiOvcar", "Kromfohrlander",
            "Kuvasz", "Labradoodle", "Labrador", "LabradorHusky", "LabradorRetriever", "LagottoRomagnolo", "LakelandTerrier", "LancashireHeeler",
            "LandseerEuropaischKontinentalerTyp", "Lapinporokoira", "Leonberg", "Leonberger", "LhasaApso", "LittleLionDog", "MagyarAgar", "Malamute",
            "Maltese", "ManchesterTerrier", "Mastiff", "MastindelPirineo", "MastinEspanol", "MastinoNapoletano", "MiniatureBullTerrier", "MiniatureContinentalPapillonSpaniel",
            "MiniaturePinscher", "MiniatureSchnauzer", "MixedBreedMaxi", "MixedBreedMini", "Mudi", "NederlandseKooikerhondje", "NederlandseSchapendoes", "Newfoundland",
            "NihonSupittsu", "NihonTeria", "NorfolkTerrier", "Norrbottenspets", "NorskBuhund", "NorskElghundGra", "NorskElghundSort", "NorskLundehund",
            "NorwegianBuhund", "NorwichTerrier", "NovaScotiaDuckTollingRetriever", "OgarPolski", "OldEnglishBulldog", "OldEnglishSheepdog", "Otterhound",
            "Papillon", "ParsonRussellTerrier", "Pekinese", "Pekingese", "PerdigueiroPortugues", "PerdiguerodeBurgos", "PerrodeAguaEspanol", "PerroDogoMallorquin",
            "PerroSinPelodelPeru", "PetitBassetGriffonVendeen", "PetitBleudeGascogne", "PetitBrabancon", "PetitChienLion", "PetitGasconSaintongeois",
            "PharaohHound", "PicardySheepdog", "PiccoloLevrieroItaliano", "Pinscher", "PodencoCanario", "PodencoIbicenco", "PodengoPortugues",
            "Poitevin", "PolskiOwczarekPodhalanski", "PolskiOwzarekNizinny", "Pomeranian", "Poodle", "PoodleMiniature", "PoodleMulticolored", "PoodleToy", "Porcelaine",
            "PortuguesePodengoGrande", "PosavskiGonic", "PrazskyKrysarik", "PresaCanario", "Pudelpointer", "Pug", "Puli", "Pumi", "PyreneanShepherd",
            "RafeirodoAlentejo", "RatTerrier", "RhodesianRidgeback", "Riesenschnauzer", "RomanianMioriticShepherd", "Rottweiler", "RovidszoruMagyarVizsla",
            "RussianSpaniel", "RusskayaPsovayaBorzaya", "RusskiyTchiornyTerrier", "RusskiyToy", "RusskoEvropeiskaiaLaika", "SaarloosWolfhound",
            "Saarlooswolfhond", "SabuesoEspanol", "SaintBernard", "Saluki", "SamoiedskaiaSabaka", "Samoyed", "Schillerstovare", "Schipperke",
            "Schnauzer", "SchweizerLaufhundChienCourantSuisse", "SchweizerNiederlaufhundPetitChienCourantSuisse", "SchweizerSchwyzerLaufhundChienCourantSchwyz",
            "ScottishTerrier", "SealyhamTerrier", "SegugioItaliano", "SegugioItalianoaPeloForte", "SegugioItalianoaPeloRaso", "SegugioMaremmano",
            "SerbianTricolourHound", "SharPei", "ShetlandSheepdog", "Shiba", "ShibaInu", "ShihTzu", "Shikoku", "SiberianHusky", "SkyeTerrier", "Sloughi",
            "SlovenskyCuvac", "SlovenskyHrubosrstyStavacOhar", "SlovenskyKopov", "Smalandsstovare", "SmallMunsterlander", "SpanishWaterDog",
            "SpinoneItaliano", "Spitz", "SredneasiatskayaOvtcharka", "SrpskiGonic", "SrpskiTrobojniGonic", "StBernhardshundShorthairedBernhardinerShorthaired",
            "Stabijhoun", "StaffordshireBullTerrier", "SteirischeRauhhaarbracke", "Suomenajokoira", "Suomenlapinkoira", "Suomenpystykorva",
            "SussexSpaniel", "SvenskLapphund", "SwedishElkhound", "TaiwanDog", "TerrierBrasileiro", "ThaiBangkaewDog", "ThaiRidgebackDog", "TibetanMastiff",
            "TibetanSpaniel", "TibetanTerrier", "TirolerBracke", "Tornjak", "Tosa", "TyroleanHound", "Vastgotaspets", "Vizsla", "VolpinoItaliano", "VostotchnoSibirskaiaLaika",
            "Weimarana", "Weimaraner", "WelshCorgiCardigan", "WelshSpringerSpaniel", "WelshTerrier", "WestHighlandWhiteTerrier", "WestHighlandWhiteTerrierWestie",
            "WestSiberianLaika", "WestfalischeDachsbracke", "Wetterhoun", "Whippet", "Wolfsspitz", "Xoloitzcuintle", "YakutskayaLaika",
            "YorkshireTerrier", "YuzhnorusskayaOvcharka", "ZapadnoSibirskaiaLaika", "Zwergpinscher", "NA"));


    static ArrayList<String> GlobalRegionalLocal = new ArrayList<>(Arrays.asList(
            "Global",
            "Regional",
            "Local",
            "Europe",
            "NorthAmerica",
            "AsiaPacific",
            "LatinAmerica",
            "MiddleEastAfrica"
    ));

    static ArrayList<String> OccasionyYears = new ArrayList<>(Arrays.asList(
            "2015",
            "2016",
            "2017",
            "2018",
            "2019",
            "2020",
            "2021",
            "2022",
            "2023",
            "2024",
            "2025",
            "N/A"
    ));

    public static ArrayList<String> getLanguages() {
        return languages;
    }

    public static  ArrayList<String> getCustomerSpecific() {
        return customerSpecific;
    }

    public static ArrayList<String> getRightsRestricted() {
        return rightsRestricted;
    }

    public static ArrayList<String> getRightsManagementType() {
        return rightsManagementType;
    }

    public static ArrayList<String> getMasterOrLocalised() {
        return MasterOrLocalised;
    }

    public static ArrayList<String> getPetLifeStage() {
        return PetLifeStage;
    }

    public static ArrayList<String> getPetSizes() {
        return PetSizes;
    }

    public static ArrayList<String> getHealthBenefits() {
        return healthBenefits;
    }

    public static ArrayList<String> getSpecies() {
        return species;
    }

    public static ArrayList<String> getDisplayPackTypes() {
        return displayPackTypes;
    }

    public static ArrayList<String> getBreeds() {
        return breeds;
    }

    public static ArrayList<String> getGlobalRegionalLocal() {
        return GlobalRegionalLocal;
    }

    public static ArrayList<String> getOccasionyYears() {
        return OccasionyYears;
    }

    public static ArrayList<String> getOccasion() {
        return Occasion;
    }

    public static ArrayList<String> getAssetStatus() {
        return assetStatus;
    }

    static ArrayList<String> Occasion = new ArrayList<>(Arrays.asList(
            "BackToSchool", "Christmas", "DentalHealthMonth", "Easter", "Fall",
            "FathersDay", "Halloween", "Holiday", "LimitedEdition", "MothersDay",
            "NFL", "NewYear", "Promotional", "RedWhiteBlue", "Seasonal",
            "Spring", "Summer", "SuperBowl", "ValentinesDay", "CNY", "Winter"
    ));

    //need to check
    static ArrayList<String> assetStatus = new ArrayList<>(Arrays.asList(
            "In Revisions",
            "Approved",
            "Embargoed",
            "Expired",
            "Archived",
            "Approved",
            "Draft"
    ));
}
