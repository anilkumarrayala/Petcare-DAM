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

    public String getMatchingValue(String A, String B, String C) {
        String key = A + "," + B +"," + C;
        return propertiesMap.getOrDefault(key, "No match found");
    }

}
