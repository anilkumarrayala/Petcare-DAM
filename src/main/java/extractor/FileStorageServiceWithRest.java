package extractor;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.Base64.*;

public class FileStorageServiceWithRest {
    private static final String account = "mystorageaccount3788";
    static String filePath = "C://Project//MARS//output.xlsx";
    private static final String key = "w8jf/3bOEgKMsMIpmNMwK8TfzV1bWVzD7hxuxezPAKCrGAd6NUcLB9kXjfsyH4i3YhZd72ZeVs5P+AStGzEeTQ==";
    static String sourceSheetName = "Data";
    static String destinationSheetName = "Transformed";
    String destinationSheetName1 = "Final";
    public static Workbook workbook;

    public static void main(String args[]) throws Exception{
        String urlString = "https://" + account + ".file.core.windows.net/migration/test-1-PN-1.xlsx";
        HttpURLConnection connection = (HttpURLConnection)(new URL(urlString)).openConnection();
        getFileRequest(connection, account, key);
        connection.connect();
        System.out.println("Response message : "+connection.getResponseMessage());
        System.out.println("Response code : "+connection.getResponseCode());

        BufferedReader br = null;

        if(connection.getResponseCode() != 200){
            br = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
        }else{
            br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
            System.out.println("Response body : "+br.readLine());
            Workbook workbook = new XSSFWorkbook();

                Sheet sheet = workbook.createSheet("Sheet1");
                String line;
                int rowNum = 0;

                while ((line = br.readLine()) != null) {
                    Row row = sheet.createRow(rowNum++);
                    Cell cell = row.createCell(0);
                    cell.setCellValue(line);
                }

                try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                    workbook.write(fileOut);
                }

                System.out.println("Data has been written to the Excel file successfully.");

            }
        }


    public static void getFileRequest(HttpURLConnection request, String account, String key) throws Exception{
        SimpleDateFormat fmt = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
        fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = fmt.format(Calendar.getInstance().getTime()) + " GMT";
        String stringToSign =  "GET\n"
                + "\n" // content encoding
                + "\n" // content language
                + "\n" // content length
                + "\n" // content md5
                + "\n" // content type
                + "\n" // date
                + "\n" // if modified since
                + "\n" // if match
                + "\n" // if none match
                + "\n" // if unmodified since
                + "\n" // range
                + "x-ms-date:" + date + "\nx-ms-version:2014-02-14\n" //headers
                + "/"+account + request.getURL().getPath(); // resources
        System.out.println("stringToSign : "+stringToSign);
        String auth = getAuthenticationString(stringToSign);
        request.setRequestMethod("GET");
        request.setRequestProperty("x-ms-date", date);
        request.setRequestProperty("x-ms-version", "2014-02-14");
        request.setRequestProperty("Authorization", auth);
    }

    private static String getAuthenticationString(String stringToSign) throws Exception{
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(Base64.decode(key.getBytes()), "HmacSHA256"));
        String authKey = new String(Base64.encode(mac.doFinal(stringToSign.getBytes("UTF-8"))));
        String auth = "SharedKey " + account + ":" + authKey;
        return auth;
    }
}
