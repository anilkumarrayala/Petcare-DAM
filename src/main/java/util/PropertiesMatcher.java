package util;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class PropertiesMatcher {
    private Map<String, String> propertiesMap;

    public PropertiesMatcher(String filePath) throws IOException {
        propertiesMap = new HashMap<>();
        loadProperties(filePath);
    }

    private void loadProperties(String filePath) throws IOException {
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(filePath)) {
            properties.load(inputStream);
        }

        for (String key : properties.stringPropertyNames()) {
            propertiesMap.put(key, properties.getProperty(key));
        }
    }

    public String getMatchingValue(String A, String B, String C, int rowNum, String colName1, String colName2, String colName3) {
        String key = A + "," + B + "," + C;
        String match = propertiesMap.get(key);
        if (match == null) {
            System.out.println("No match found for: " + key + " in row: " + rowNum + " columns: " + colName1 + ", " + colName2 + ", " + colName3);
            return "No match found for: " + key;
        }
        return match;
    }

    public String getMatchingValue2(String A, String B, int rowNum, String colName1, String colName2) {
        String key = A + "," + B;
        String match = propertiesMap.get(key);
        if (match == null) {
            System.out.println("No match found for: " + key + " in row: " + rowNum + " columns: " + colName1 + ", " + colName2);
            return "No match found for: " + key;
        }
        return match;
    }

}
