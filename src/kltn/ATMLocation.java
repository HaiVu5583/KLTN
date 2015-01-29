/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kltn;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Vu
 */
public class ATMLocation {

    /**
     * @param args the command line arguments
     */
    public void getVietcombankATMLocation() throws IOException {
        Document doc = Jsoup.connect("https://www.vietcombank.com.vn/ATM/default.aspx").timeout(10000).get();
        
        PostData pd = new PostData();
        PostData.PostDataVietcomBank pdv = pd.new PostDataVietcomBank();
//        pdv.setEVENTARGUMENT(doc.select("#__EVENTARGUMENT").first().val());
//        pdv.setEVENTTARGET(doc.select("#__EVENTTARGET").first().val());
//        pdv.setLASTFOCUS(doc.select("#__LASTFOCUS").first().val());
        pdv.setVIEWSTATE(doc.select("#__VIEWSTATE").first().val());
//        pdv.setVIEWSTATEENCRYPTED(doc.select("#__VIEWSTATEENCRYPTED").first().val());

        Element locationList = doc.select("#ctl00_Content_CityList").first();
        Elements locations = locationList.children();
        HashMap<Integer, String> maps = new HashMap();
        for (Element e : locations) {
            if (!e.val().equals("0")) {
                maps.put(Integer.parseInt(e.val()), e.text());
            }
        }

//        System.out.println(doc.select("table#ctl00_Content_ATMView tr td").toString());
        ArrayList<ATM> atmList = new ArrayList();
        ATM atm = new ATM();
        Set<Integer> keys = maps.keySet();
        
        for (Integer key : keys) {
            doc = Jsoup.connect("https://www.vietcombank.com.vn/ATM/default.aspx").timeout(10000)
                    .data("__EVENTTARGET", pdv.getEVENTTARGET())
                    .data("__EVENTARGUMENT", pdv.getEVENTARGUMENT())
                    .data("__LASTFOCUS", pdv.getLASTFOCUS())
                    .data("__VIEWSTATE", pdv.getVIEWSTATE())
                    .data("__VIEWSTATEENCRYPTED", pdv.getVIEWSTATEENCRYPTED())
                    .data("ctl00$Content$CityList", key.toString())
                    .post();
            Elements list = doc.select("table#ctl00_Content_ATMView tr td");
            for (int i = 0; i < list.size(); i++) {
                Element e = list.get(i);
                if (i % 4 == 0) {
//                System.out.println("Diem Dat May: " + e.text());

                } else if (i % 4 == 1) {
//                System.out.println("So Luong May: " + e.text());
                    atm.setNumOfMachine(e.text());
                } else if (i % 4 == 2) {
//                System.out.println("Dia chi: " + e.text());
                    atm.setAddress(e.text());
                } else {
//                System.out.println("Gio phuc vu: " + e.text());
//                System.out.println("------------------------------------------------");
                    atm.setOpenTime(e.text());
                    atm.setProvince_city(maps.get(key));
                    atm.setBank("Vietcombank");
                    atmList.add(atm);
                    atm = new ATM();
                }
            }
            
        }
        for (ATM a : atmList) {
            a.print();
        }
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/kltn?useUnicode=true&characterEncoding=UTF-8", "root", "");
            
            PreparedStatement stmt = con.prepareStatement("INSERT INTO atm_location(fullAddress, bank, openTime, numMachine, province_city) VALUES (?, ?, ?, ?, ?)");
            for (ATM a : atmList) {
                stmt.setString(1, a.getAddress());
                stmt.setString(2, a.getBank());
                stmt.setString(3, a.getOpenTime());
                stmt.setInt(4, Integer.parseInt(a.getNumOfMachine()));
                stmt.setString(5, a.getProvince_city());
                stmt.execute();
            }
            stmt.close();
            con.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ATMLocation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(ATMLocation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ATMLocation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ATMLocation.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Can't connect");
        }
    }
    
    public void getAgribankATMLocation() throws IOException {
        Document doc = Jsoup.connect("http://agribank.com.vn/71/1147/mang-luoi---atm-pos.aspx").timeout(10000).get();
        Element locationList = doc.select("#cphMain_Map1_ddlTinh").first();
        HashMap<Integer, String> maps = new HashMap();
        Elements locations = locationList.children();
        for (Element location : locations) {
            maps.put(Integer.parseInt(location.val()), location.text());
        }
        Set<Integer> keys = maps.keySet();
        ArrayList<ATM> atmList = new ArrayList<>();
        ATM atm = new ATM();
        for (Integer key : keys) {
            doc = Jsoup.connect("http://agribank.com.vn/tim-kiem/atm/1147/" + key.toString() + "/0/ket-qua.aspx").timeout(10000).get();
            Elements tables = doc.select(".agri-atm-tbl");
            for (Element table : tables) {
                
                
                String district = table.select("tr th").first().text();
                char[] c = district.toCharArray();
                StringBuilder sb = new StringBuilder();
                for (int i=0; i<c.length; i++){
                    if (!Character.toString(c[i]).equals(".")){
                        sb.append(c[i]);
                    }
                }
                district = sb.toString();
                
                
                Elements trs = table.select("tr");
                for (Element tr : trs) {
//                    System.out.println("th: "+tr.select("th").toString());
//                    if (tr.select("th").text().equals(""))System.out.println("TRUE");
//                    System.out.println("td: "+tr.select("td").toString());

                    if (tr.select("th").text().equals("")) {
                        System.out.println(tr.select("td.agri-atm-tbl-name").text());
                        System.out.println(tr.select("td.agri-atm-tbl-time").text());
                        System.out.println(tr.select("td.agri-atm-tbl-xem").text());
                        atm.setBank("AgriBank");
                        atm.setProvince_city(maps.get(key));
                        atm.setDistrict(district);
                        atm.setStreet(tr.select("td.agri-atm-tbl-name").text());
                        atm.setOpenTime(tr.select("td.agri-atm-tbl-xem").text());
                        atm.setNumOfMachine(tr.select("td.agri-atm-tbl-time").text());
                        atmList.add(atm);
                        atm = new ATM();
//                    if (tr.select("th").text() == "") {
//                        System.out.println("NULL");
//
////                    }
//                    } else {
//                        System.out.println("NOT NULL");
//                    }
                    }
                }
                for (ATM a : atmList) {
                    a.print();
                }
//            System.out.println(doc.toString());
//            System.out.println("------------------------------------------------------");
            }
            System.out.println(maps.toString());
            //System.out.println(locationList.toString());
        }
    }

    

    public static void main(String[] args) throws IOException {
//        try {
        // TODO code application logic here
        ATMLocation atm = new ATMLocation();
//        atm.getVietcombankATMLocation();
        atm.getAgribankATMLocation();
    }
    
}
//http://agribank.com.vn/tim-kiem/atm/1147/1109/0/ket-qua.aspx
//http://agribank.com.vn/71/1147/mang-luoi---atm-pos.aspx
//cphMain_Map1_ddlTinh
