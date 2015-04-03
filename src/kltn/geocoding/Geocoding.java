/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kltn.geocoding;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLEncoder;
import javax.net.ssl.HttpsURLConnection;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Vu
 */
public class Geocoding {

    public static boolean checkContainNumber(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }

//    public static void main(String[] args) throws IOException, Exception {
//        
//        String link = "https://maps.googleapis.com/maps/api/geocode/json?&key=AIzaSyALCgmmer3Cht-mFQiaJC9yoWdSqvfdAiM";
//        String address = "hà nội,việt nam";
//        link = link+"&address="+URLEncoder.encode(address);
//        System.out.println(link);
//        URL url = new URL(link);
//        HttpsURLConnection httpsCon = (HttpsURLConnection) url.openConnection();
//        InputStream is = httpsCon.getInputStream();
//        StringWriter writer = new StringWriter();
//        IOUtils.copy(is, writer, "UTF-8");
//        
//        String jsonString = writer.toString();
//        System.out.println(jsonString);
////        JSONParser parse = new JSONParser();
////        Object obj = parse.parse(jsonString);
////        JSONObject jsonObject = (JSONObject) obj;
////        JSONArray resultArr = (JSONArray) jsonObject.get("results");
////        Object subObj = parse.parse(resultArr.get(0).toString());
////        JSONObject resultObject = (JSONObject) subObj;
////        System.out.println(resultObject.toJSONString());
////        System.out.println(resultObject.get("types").toString());
//    }
}
