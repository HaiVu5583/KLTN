/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kltn;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import java.awt.BorderLayout;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
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
    //OK
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

    //OK
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
                for (int i = 0; i < c.length; i++) {
                    if (!Character.toString(c[i]).equals(".")) {
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

    //OK
    public void getVietinbankATMLocation() throws IOException {
        Document doc = Jsoup.connect("https://card.vietinbank.vn/sites/home/vi/diem-dat-atm/").timeout(10000).get();
        Element hanoiDistrictDropdown = doc.select("#ajaxmenu1").first();
//        System.out.println(hanoiDistrictList.toString());
        Elements hanoiDistrictList = hanoiDistrictDropdown.children();
        HashMap<String, String> maps = new HashMap<>();
        for (Element e : hanoiDistrictList) {

            maps.put(e.text(), "https://card.vietinbank.vn" + e.val());
        };
        Set<String> district = maps.keySet();
        for (String d : district) {
            Document doc2 = Jsoup.connect(maps.get(d)).timeout(10000).get();
//            System.out.println(doc2.toString());
            Elements tds = doc2.select("td");
            for (int i = 0; i < tds.size(); i++) {
                if (i % 5 == 0) {
//                    System.out.println("");
                } else if (i % 5 == 1) {
                    System.out.println("Address: " + tds.get(i).text());
                } else if (i % 5 == 2) {
                    System.out.println("Num Of Machine: " + tds.get(i).text());
                } else if (i % 5 == 3) {
                    System.out.println("Branch: " + tds.get(i).text());
                } else if (i % 5 == 4) {
                    System.out.println("Phone: " + tds.get(i).text());
                    System.out.println("--------------------------------------");
                }

            }
        }

    }

//    public void getBIDVATMLocation() throws IOException {
////        plcRoot_Layout_zoneMenu_PagePlaceholder_PagePlaceholder_Layout_zoneContent_pageplaceholder_pageplaceholder_Layout_zoneContent_DSATM_ddlTinh
////        plcRoot_Layout_zoneMenu_PagePlaceholder_PagePlaceholder_Layout_zoneContent_pageplaceholder_pageplaceholder_Layout_zoneContent_DSATM_ddlHuyen
////        plcRoot_Layout_zoneMenu_PagePlaceholder_PagePlaceholder_Layout_zoneContent_pageplaceholder_pageplaceholder_Layout_zoneContent_DSATM_ddlXa
//        String url = "http://www.bidv.com.vn/chinhanh/ATM.aspx";
////        Document doc = Jsoup.connect("http://www.bidv.com.vn/chinhanh/ATM.aspx").timeout(10000).get();
////        Element input = doc.select("#__EVENTTARGET").first();
////        System.out.println(input.val());
////        System.out.println(input.toString());
////        Element provinceDropdown = doc.select("#plcRoot_Layout_zoneMenu_PagePlaceholder_PagePlaceholder_Layout_zoneContent_pageplaceholder_pageplaceholder_Layout_zoneContent_DSATM_ddlTinh").first();
////        Elements provinces = provinceDropdown.children();
////        System.out.println(provinces.toString());
////        HashMap<String, String> maps = new HashMap();
////        for (Element e : provinces) {
////            if (!e.val().equals("0")) {
////                maps.put(e.val(), e.text());
////            }
////        }
////        =
////__ASYNCPOST=true
////__EVENTARGUMENT=
////__EVENTTARGET=plcRoot$Layout$zoneMenu$PagePlaceholder$PagePlaceholder$Layout$zoneContent$pageplaceholder$pageplaceholder$Layout$zoneContent$DSATM$ddlTinh
////__LASTFOCUS=
////__SCROLLPOSITIONX=0
////__SCROLLPOSITIONY=0
////__VIEWSTATE=/wEPDwUKMjExOTgxNDYyMA9kFgICARBkZBYEAgMPZBYCZg9kFgJmD2QWCAIBD2QWAmYPZBYCZg9kFgICBQ8QZGQWAWZkAgMPZBYCZg9kFgYCAQ8WAh4EVGV4dAWMATxsaT48YSByZWw9InN1YiIgaHJlZj0ifi9kZWZhdWx0LmFzcHgiIHN0eWxlPSIiIGNsYXNzPSIiPjxzcGFuIGNsYXNzPSJpdGVtX3JpZ2h0Ij48c3BhbiBjbGFzcz0iaXRlbV9sZWZ0Ij5UcmFuZyBjaOG7pzwvc3Bhbj48L3NwYW4+PC9hPjwvbGk+ZAIDDxYCHgtfIUl0ZW1Db3VudAIGFgxmD2QWAmYPFQMDMzA4EH4vR2lvaXRoaWV1LmFzcHgOR2nhu5tpIHRoaeG7h3VkAgEPZBYCZg8VAwUyMzE4MhF+L05oYS1kYXUtdHUuYXNweA9OaMOgIMSR4bqndSB0xrBkAgIPZBYCZg8VAwMzMTcUfi9TYW5waGFtZGljaHZ1LmFzcHgaU+G6o24gcGjhuqltIC0gROG7i2NoIHbhu6VkAgMPZBYCZg8VAwMzMDkWfi9UaW4tdHVjLXN1LWtpZW4uYXNweBdUaW4gdOG7qWMgLSBT4buxIGtp4buHbmQCBA9kFgJmDxUDAzMxMA9+L2NoaW5oYW5oLmFzcHgOTeG6oW5nIGzGsOG7m2lkAgUPZBYCZg8VAwQxNjA2En4vTmdoZS1uZ2hpZXAuYXNweA5UdXnhu4NuIGThu6VuZ2QCBQ8WAh8ABbQPPGRpdiBzdHlsZT0iZGlzcGxheTogbm9uZTsiIGNsYXNzPSJ0YWJjb250ZW50IiBpZD0iMzA4Ij48YSBocmVmPSJ+L0dpb2l0aGlldS9MaWNoLXN1LXBoYXQtdHJpZW4uYXNweCI+TOG7i2NoIHPhu60gcGjDoXQgdHJp4buDbjwvYT48YSBocmVmPSJ+L0dpb2l0aGlldS9HaW9pLXRoaWV1LWNodW5nLSgxKS5hc3B4Ij5HaeG7m2kgdGhp4buHdSBjaHVuZzwvYT48YSBocmVmPSJ+L0dpb2l0aGlldS9CYW4tbGFuaC1kYW8uYXNweCI+QmFuIGzDo25oIMSR4bqhbzwvYT48L2Rpdj48ZGl2IHN0eWxlPSJkaXNwbGF5OiBub25lOyIgY2xhc3M9InRhYmNvbnRlbnQiIGlkPSIyMzE4MiI+PGEgaHJlZj0ifi9OaGEtZGF1LXR1L1RvbmctcXVhbi12ZS1CSURWLmFzcHgiPlThu5VuZyBxdWFuIHbhu4EgQklEVjwvYT48YSBocmVmPSJ+L05oYS1kYXUtdHUvQmFvLWNhby10YWktY2hpbmguYXNweCI+QsOhbyBjw6FvICYgVMOgaSBsaeG7h3U8L2E+PGEgaHJlZj0ifi9OaGEtZGF1LXR1L0RpZXUtbGUtdmEtcXVhbi10cmktbmdhbi1oYW5nLmFzcHgiPsSQaeG7gXUgbOG7hyB2w6AgcXXhuqNuIHRy4buLIG5nw6JuIGjDoG5nPC9hPjxhIGhyZWY9In4vTmhhLWRhdS10dS9UaG9uZy10aW4tZGFuaC1jaG8tbmhhLWRhdS10dS5hc3B4Ij5UaMO0bmcgdGluIGTDoG5oIGNobyBOxJBUPC9hPjxhIGhyZWY9In4vTmhhLWRhdS10dS9Ib2ktZGFwLWxpZW4taGUuYXNweCI+SOG7j2kgxJHDoXAmbGnDqm4gaOG7hzwvYT48L2Rpdj48ZGl2IHN0eWxlPSJkaXNwbGF5OiBub25lOyIgY2xhc3M9InRhYmNvbnRlbnQiIGlkPSIzMTciPjxhIGhyZWY9In4vU2FucGhhbWRpY2h2dS9raGFjaGhhbmdjYW5oYW4uYXNweCI+S2jDoWNoIGjDoG5nIGPDoSBuaMOibjwvYT48YSBocmVmPSJ+L1NhbnBoYW1kaWNodnUvS2hhY2hoYW5nZG9hbmhuZ2hpZXAuYXNweCI+S2jDoWNoIGjDoG5nIGRvYW5oIG5naGnhu4dwPC9hPjxhIGhyZWY9In4vU2FucGhhbWRpY2h2dS9EaW5oLWNoZS10YWktY2hpbmguYXNweCI+xJDhu4tuaCBjaOG6vyB0w6BpIGNow61uaDwvYT48L2Rpdj48ZGl2IHN0eWxlPSJkaXNwbGF5OiBub25lOyIgY2xhc3M9InRhYmNvbnRlbnQiIGlkPSIzMDkiPjxhIGhyZWY9In4vVGluLXR1Yy1zdS1raWVuL1Rpbi1CSURWLmFzcHgiPlRpbiBCSURWPC9hPjxhIGhyZWY9In4vVGluLXR1Yy1zdS1raWVuL0dpYWktYm9uZy3EkWEtQklEVi0tLU1hbi1VdGQtQ1VQLmFzcHgiPkdp4bqj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LXBoaS9HaWFvLWRpY2gtdGFpLWtob2FuLVZORC5hc3B4KVBoJiMyMzc7IEThu4tjaCB24bulIHQmIzIyNDtpIGtob+G6o24gVk5EZAICD2QWAmYPFQI8L05nYW4taGFuZy1iYW4tbGUvQmlldS1waGkvUGhpLXNhbi1waGFtLWNodXllbi10aWVuLVZORC5hc3B4KVBoJiMyMzc7IHPhuqNuIHBo4bqpbSBjaHV54buDbiB0aeG7gW4gVk5EZAIDD2QWAmYPFQIsL05nYW4taGFuZy1iYW4tbGUvQmlldS1waGkvUGhpLWJhby1sYW5oLmFzcHgcUGgmIzIzNzsgYuG6o28gbCYjMjI3O25oIFZORGQCBA9kFgJmDxUCNC9OZ2FuLWhhbmctYmFuLWxlL0JpZXUtcGhpL1BoaS1kaWNoLXZ1LW5nYW4tcXV5LmFzcHgoUGgmIzIzNzsgZOG7i2NoIHbhu6UgbmcmIzIyNjtuIHF14bu5IFZORGQCBQ9kFgJmDxUCOy9OZ2FuLWhhbmctYmFuLWxlL0JpZXUtcGhpL1BoaS1naWFvLWRpY2gtdGFpLWtob2FuLVVTRC5hc3B4MlBoJiMyMzc7IEThu4tjaCB24bulIHQmIzIyNDtpIGtob+G6o24gbmdv4bqhaSB04buHZAIGD2QWAmYPFQIwL05nYW4taGFuZy1iYW4tbGUvQmlldS1waGkvUGhpLWJhby1sYW5oLVVTRC5hc3B4JVBoJiMyMzc7IGLhuqNvIGwmIzIyNztuaCBuZ2/huqFpIHThu4dkAgcPZBYCZg8VAjgvTmdhbi1oYW5nLWJhbi1sZS9CaWV1LXBoaS9QaGktZGljaC12dS1uZ2FuLXF1eS1VU0QuYXNweDFQaCYjMjM3OyBk4buLY2ggduG7pSBuZyYjMjI2O24gcXXhu7kgbmdv4bqhaSB04buHZAIID2QWAmYPFQJBL05nYW4taGFuZy1iYW4tbGUvQmlldS1waGkvQmlldS1waC0tMjM3Oy1EaWNoLXZ1LUJJRFYtTW9iaWxlLmFzcHgnQmnhu4N1IHBoJiMyMzc7IEThu4tjaCB24bulIEJJRFYgTW9iaWxlZAIJD2QWAmYPFQJSL05nYW4taGFuZy1iYW4tbGUvQmlldS1waGkvQmlldS1waC0tMjM3Oy1EaWNoLXZ1LUJJRFYt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LjAwMGQCAw8WAh8ABQkzLjUzMS4wMDBkAgMPZBYGZg8VAQlTSkMgKDEwTClkAgEPFgIfAAUJMy41MjcuMDAwZAIDDxYCHwAFCTMuNTMxLjAwMGQCBw8WAh8ABRAwMS8wMi8yMDE1IDA5OjExZAIHD2QWAmYPZBYCAgQPFgIfAAXLBzxkaXYgY2xhc3M9Im1hcnF1ZWUiIGlkPSJteWNyYXdsZXIyIj48aW1nIHN0eWxlPSJoZWlnaHQ6NDBweDsiIHNyYz0iL0FjY291bnRpbmcvR2V0RmlsZTIuYXNweD9GaWxlX0lEPXs0M3g5dks5U3Z4ZUhhR25GVm8rUjd6Zkc2UHBTQWNNTX0iICBib3JkZXI9IjAiLz48aW1nIHN0eWxlPSJoZWlnaHQ6NDBweDsiIHNyYz0iL0FjY291bnRpbmcvR2V0RmlsZTIuYXNweD9GaWxlX0lEPXs0M3g5dks5U3Z4ZUhhR25GVm8rUjd6Zkc2UHBTQWNNTX0iICBib3JkZXI9IjAiLz48aW1nIHN0eWxlPSJoZWlnaHQ6NDBweDsiIHNyYz0iL0FjY291bnRpbmcvR2V0RmlsZTIuYXNweD9GaWxlX0lEPXs0M3g5dks5U3Z4ZUhhR25GVm8rUjd6Zkc2UHBTQWNNTX0iICBib3JkZXI9IjAiLz48aW1nIHN0eWxlPSJoZWlnaHQ6NDBweDsiIHNyYz0iL0FjY291bnRpbmcvR2V0RmlsZTIuYXNweD9GaWxlX0lEPXs0M3g5dks5U3Z4ZUhhR25GVm8rUjd6Zkc2UHBTQWNNTX0iICBib3JkZXI9IjAiLz48aW1nIHN0eWxlPSJoZWlnaHQ6NDBweDsiIHNyYz0iL0FjY291bnRpbmcvR2V0RmlsZTIuYXNweD9GaWxlX0lEPXs0M3g5dks5U3Z4ZUhhR25GVm8rUjd6Zkc2UHBTQWNNTX0iICBib3JkZXI9IjAiLz48L2Rpdj48c2NyaXB0IHR5cGU9InRleHQvamF2YXNjcmlwdCI+Cm1hcnF1ZWVJbml0KHsKdW5pcXVlaWQ6ICdteWNyYXdsZXIyJywKc3R5bGU6IHsKJ3BhZGRpbmcnOiAnMnB4JywKJ3dpZHRoJzogJzYwMHB4JywKJ2hlaWdodCc6ICc0MHB4Jwp9LAppbmM6IDUsIC8vc3BlZWQgLSBwaXhlbCBpbmNyZW1lbnQgZm9yIGVhY2ggaXRlcmF0aW9uIG9mIHRoaXMgbWFycXVlZSdzIG1vdmVtZW50Cm1vdXNlOiAnY3Vyc29yIGRyaXZlbicsIC8vbW91c2VvdmVyIGJlaGF2aW9yICgncGF1c2UnICdjdXJzb3IgZHJpdmVuJyBvciBmYWxzZSkKbW92ZWF0bGVhc3Q6IDIsCm5ldXRyYWw6IDE1MCwKc2F2ZWRpcmVjdGlvbjogdHJ1ZQp9KTsKPC9zY3JpcHQ+ZAIFD2QWAmYPDxYCHwZoZGQYAwUeX19Db250cm9sc1JlcXVpcmVQb3N0QmFja0tleV9fFgEFNXBsY1Jvb3QkTGF5b3V0JHpvbmVTZWFyY2gkY21zc2VhcmNoYm94JGJ0bkltYWdlQnV0dG9uBRR2aWV3U3RhdGUkZ3JpZFN0YXRlcw9nZAUUbG9nUXVlcnkkZ3JpZFF1ZXJpZXMPZ2R1SEgqGqtYX2mDJLJ6GzZr8xtpcA==
////__VIEWSTATEGENERATOR=A5343185
////lng=vi-VN
////manScript=plcRoot$Layout$zoneMenu$PagePlaceholder$PagePlaceholder$Layout$zoneContent$pageplaceholder$pageplaceholder$Layout$zoneContent$DSATM$UpdatePanel1|plcRoot$Layout$zoneMenu$PagePlaceholder$PagePlaceholder$Layout$zoneContent$pageplaceholder$pageplaceholder$Layout$zoneContent$DSATM$ddlTinh
////plcRoot$Layout$zoneMenu$PagePlaceholder$PagePlaceholder$Layout$zoneContent$pageplaceholder$pageplaceholder$Layout$zoneContent$DSATM$ddlHuyen=0
////plcRoot$Layout$zoneMenu$PagePlaceholder$PagePlaceholder$Layout$zoneContent$pageplaceholder$pageplaceholder$Layout$zoneContent$DSATM$ddlTinh=460
////plcRoot$Layout$zoneMenu$PagePlaceholder$PagePlaceholder$Layout$zoneContent$pageplaceholder$pageplaceholder$Layout$zoneContent$DSATM$ddlXa=0
////plcRoot$Layout$zoneMenu$PagePlaceholder$PagePlaceholder$Layout$zoneContent$pageplaceholder$pageplaceholder$Layout$zoneContent$DSATM$hddCurrPage=1
////plcRoot$Layout$zoneSearch$cmssearchbox$txtWord=Tìm kiếm
////        Element hanoi = maps.get("460");
//        Document doc2 = Jsoup.connect("http://www.bidv.com.vn/chinhanh/ATM.aspx").timeout(10000)
//                .data("__ASYNCPOST", "true")
//                .data("__EVENTTARGET", "	plcRoot$Layout$zoneMenu$PagePlaceholder$PagePlaceholder$Layout$zoneContent$pageplaceholder$pageplaceholder$Layout$zoneContent$DSATM$ddlTinh")
//                .data("__SCROLLPOSITIONX", "0")
//                .data("__SCROLLPOSITIONY", "0")
//                .data("__VIEWSTATE", "/wEPDwUKMjExOTgxNDYyMA9kFgICARBkZBYEAgMPZBYCZg9kFgJmD2QWCAIBD2QWAmYPZBYCZg9kFgICBQ8QZGQWAWZkAgMPZBYCZg9kFgYCAQ8WAh4EVGV4dAWMATxsaT48YSByZWw9InN1YiIgaHJlZj0ifi9kZWZhdWx0LmFzcHgiIHN0eWxlPSIiIGNsYXNzPSIiPjxzcGFuIGNsYXNzPSJpdGVtX3JpZ2h0Ij48c3BhbiBjbGFzcz0iaXRlbV9sZWZ0Ij5UcmFuZyBjaOG7pzwvc3Bhbj48L3NwYW4+PC9hPjwvbGk+ZAIDDxYCHgtfIUl0ZW1Db3VudAIGFgxmD2QWAmYPFQMDMzA4EH4vR2lvaXRoaWV1LmFzcHgOR2nhu5tpIHRoaeG7h3VkAgEPZBYCZg8VAwUyMzE4MhF+L05oYS1kYXUtdHUuYXNweA9OaMOgIMSR4bqndSB0xrBkAgIPZBYCZg8VAwMzMTcUfi9TYW5waGFtZGljaHZ1LmFzcHgaU+G6o24gcGjhuqltIC0gROG7i2NoIHbhu6VkAgMPZBYCZg8VAwMzMDkWfi9UaW4tdHVjLXN1LWtpZW4uYXNweBdUaW4gdOG7qWMgLSBT4buxIGtp4buHbmQCBA9kFgJmDxUDAzMxMA9+L2NoaW5oYW5oLmFzcHgOTeG6oW5nIGzGsOG7m2lkAgUPZBYCZg8VAwQxNjA2En4vTmdoZS1uZ2hpZXAuYXNweA5UdXnhu4NuIGThu6VuZ2QCBQ8WAh8ABbQPPGRpdiBzdHlsZT0iZGlzcGxheTogbm9uZTsiIGNsYXNzPSJ0YWJjb250ZW50IiBpZD0iMzA4Ij48YSBocmVmPSJ+L0dpb2l0aGlldS9MaWNoLXN1LXBoYXQtdHJpZW4uYXNweCI+TOG7i2NoIHPhu60gcGjDoXQgdHJp4buDbjwvYT48YSBocmVmPSJ+L0dpb2l0aGlldS9HaW9pLXRoaWV1LWNodW5nLSgxKS5hc3B4Ij5HaeG7m2kgdGhp4buHdSBjaHVuZzwvYT48YSBocmVmPSJ+L0dpb2l0aGlldS9CYW4tbGFuaC1kYW8uYXNweCI+QmFuIGzDo25oIMSR4bqhbzwvYT48L2Rpdj48ZGl2IHN0eWxlPSJkaXNwbGF5OiBub25lOyIgY2xhc3M9InRhYmNvbnRlbnQiIGlkPSIyMzE4MiI+PGEgaHJlZj0ifi9OaGEtZGF1LXR1L1RvbmctcXVhbi12ZS1CSURWLmFzcHgiPlThu5VuZyBxdWFuIHbhu4EgQklEVjwvYT48YSBocmVmPSJ+L05oYS1kYXUtdHUvQmFvLWNhby10YWktY2hpbmguYXNweCI+QsOhbyBjw6FvICYgVMOgaSBsaeG7h3U8L2E+PGEgaHJlZj0ifi9OaGEtZGF1LXR1L0RpZXUtbGUtdmEtcXVhbi10cmktbmdhbi1oYW5nLmFzcHgiPsSQaeG7gXUgbOG7hyB2w6AgcXXhuqNuIHRy4buLIG5nw6JuIGjDoG5nPC9hPjxhIGhyZWY9In4vTmhhLWRhdS10dS9UaG9uZy10aW4tZGFuaC1jaG8tbmhhLWRhdS10dS5hc3B4Ij5UaMO0bmcgdGluIGTDoG5oIGNobyBOxJBUPC9hPjxhIGhyZWY9In4vTmhhLWRhdS10dS9Ib2ktZGFwLWxpZW4taGUuYXNweCI+SOG7j2kgxJHDoXAmbGnDqm4gaOG7hzwvYT48L2Rpdj48ZGl2IHN0eWxlPSJkaXNwbGF5OiBub25lOyIgY2xhc3M9InRhYmNvbnRlbnQiIGlkPSIzMTciPjxhIGhyZWY9In4vU2FucGhhbWRpY2h2dS9raGFjaGhhbmdjYW5oYW4uYXNweCI+S2jDoWNoIGjDoG5nIGPDoSBuaMOibjwvYT48YSBocmVmPSJ+L1NhbnBoYW1kaWNodnUvS2hhY2hoYW5nZG9hbmhuZ2hpZXAuYXNweCI+S2jDoWNoIGjDoG5nIGRvYW5oIG5naGnhu4dwPC9hPjxhIGhyZWY9In4vU2FucGhhbWRpY2h2dS9EaW5oLWNoZS10YWktY2hpbmguYXNweCI+xJDhu4tuaCBjaOG6vyB0w6BpIGNow61uaDwvYT48L2Rpdj48ZGl2IHN0eWxlPSJkaXNwbGF5OiBub25lOyIgY2xhc3M9InRhYmNvbnRlbnQiIGlkPSIzMDkiPjxhIGhyZWY9In4vVGluLXR1Yy1zdS1raWVuL1Rpbi1CSURWLmFzcHgiPlRpbiBCSURWPC9hPjxhIGhyZWY9In4vVGluLXR1Yy1zdS1raWVuL0dpYWktYm9uZy3EkWEtQklEVi0tLU1hbi1VdGQtQ1VQLmFzcHgiPkdp4bqj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LXBoaS9HaWFvLWRpY2gtdGFpLWtob2FuLVZORC5hc3B4KVBoJiMyMzc7IEThu4tjaCB24bulIHQmIzIyNDtpIGtob+G6o24gVk5EZAICD2QWAmYPFQI8L05nYW4taGFuZy1iYW4tbGUvQmlldS1waGkvUGhpLXNhbi1waGFtLWNodXllbi10aWVuLVZORC5hc3B4KVBoJiMyMzc7IHPhuqNuIHBo4bqpbSBjaHV54buDbiB0aeG7gW4gVk5EZAIDD2QWAmYPFQIsL05nYW4taGFuZy1iYW4tbGUvQmlldS1waGkvUGhpLWJhby1sYW5oLmFzcHgcUGgmIzIzNzsgYuG6o28gbCYjMjI3O25oIFZORGQCBA9kFgJmDxUCNC9OZ2FuLWhhbmctYmFuLWxlL0JpZXUtcGhpL1BoaS1kaWNoLXZ1LW5nYW4tcXV5LmFzcHgoUGgmIzIzNzsgZOG7i2NoIHbhu6UgbmcmIzIyNjtuIHF14bu5IFZORGQCBQ9kFgJmDxUCOy9OZ2FuLWhhbmctYmFuLWxlL0JpZXUtcGhpL1BoaS1naWFvLWRpY2gtdGFpLWtob2FuLVVTRC5hc3B4MlBoJiMyMzc7IEThu4tjaCB24bulIHQmIzIyNDtpIGtob+G6o24gbmdv4bqhaSB04buHZAIGD2QWAmYPFQIwL05nYW4taGFuZy1iYW4tbGUvQmlldS1waGkvUGhpLWJhby1sYW5oLVVTRC5hc3B4JVBoJiMyMzc7IGLhuqNvIGwmIzIyNztuaCBuZ2/huqFpIHThu4dkAgcPZBYCZg8VAjgvTmdhbi1oYW5nLWJhbi1sZS9CaWV1LXBoaS9QaGktZGljaC12dS1uZ2FuLXF1eS1VU0QuYXNweDFQaCYjMjM3OyBk4buLY2ggduG7pSBuZyYjMjI2O24gcXXhu7kgbmdv4bqhaSB04buHZAIID2QWAmYPFQJBL05nYW4taGFuZy1iYW4tbGUvQmlldS1waGkvQmlldS1waC0tMjM3Oy1EaWNoLXZ1LUJJRFYtTW9iaWxlLmFzcHgnQmnhu4N1IHBoJiMyMzc7IEThu4tjaCB24bulIEJJRFYgTW9iaWxlZAIJD2QWAmYPFQJSL05nYW4taGFuZy1iYW4tbGUvQmlldS1waGkvQmlldS1waC0tMjM3Oy1EaWNoLXZ1LUJJRFYt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LjAwMGQCAw8WAh8ABQkzLjUzMS4wMDBkAgMPZBYGZg8VAQlTSkMgKDEwTClkAgEPFgIfAAUJMy41MjcuMDAwZAIDDxYCHwAFCTMuNTMxLjAwMGQCBw8WAh8ABRAwMS8wMi8yMDE1IDA5OjExZAIHD2QWAmYPZBYCAgQPFgIfAAXLBzxkaXYgY2xhc3M9Im1hcnF1ZWUiIGlkPSJteWNyYXdsZXIyIj48aW1nIHN0eWxlPSJoZWlnaHQ6NDBweDsiIHNyYz0iL0FjY291bnRpbmcvR2V0RmlsZTIuYXNweD9GaWxlX0lEPXs0M3g5dks5U3Z4ZUhhR25GVm8rUjd6Zkc2UHBTQWNNTX0iICBib3JkZXI9IjAiLz48aW1nIHN0eWxlPSJoZWlnaHQ6NDBweDsiIHNyYz0iL0FjY291bnRpbmcvR2V0RmlsZTIuYXNweD9GaWxlX0lEPXs0M3g5dks5U3Z4ZUhhR25GVm8rUjd6Zkc2UHBTQWNNTX0iICBib3JkZXI9IjAiLz48aW1nIHN0eWxlPSJoZWlnaHQ6NDBweDsiIHNyYz0iL0FjY291bnRpbmcvR2V0RmlsZTIuYXNweD9GaWxlX0lEPXs0M3g5dks5U3Z4ZUhhR25GVm8rUjd6Zkc2UHBTQWNNTX0iICBib3JkZXI9IjAiLz48aW1nIHN0eWxlPSJoZWlnaHQ6NDBweDsiIHNyYz0iL0FjY291bnRpbmcvR2V0RmlsZTIuYXNweD9GaWxlX0lEPXs0M3g5dks5U3Z4ZUhhR25GVm8rUjd6Zkc2UHBTQWNNTX0iICBib3JkZXI9IjAiLz48aW1nIHN0eWxlPSJoZWlnaHQ6NDBweDsiIHNyYz0iL0FjY291bnRpbmcvR2V0RmlsZTIuYXNweD9GaWxlX0lEPXs0M3g5dks5U3Z4ZUhhR25GVm8rUjd6Zkc2UHBTQWNNTX0iICBib3JkZXI9IjAiLz48L2Rpdj48c2NyaXB0IHR5cGU9InRleHQvamF2YXNjcmlwdCI+Cm1hcnF1ZWVJbml0KHsKdW5pcXVlaWQ6ICdteWNyYXdsZXIyJywKc3R5bGU6IHsKJ3BhZGRpbmcnOiAnMnB4JywKJ3dpZHRoJzogJzYwMHB4JywKJ2hlaWdodCc6ICc0MHB4Jwp9LAppbmM6IDUsIC8vc3BlZWQgLSBwaXhlbCBpbmNyZW1lbnQgZm9yIGVhY2ggaXRlcmF0aW9uIG9mIHRoaXMgbWFycXVlZSdzIG1vdmVtZW50Cm1vdXNlOiAnY3Vyc29yIGRyaXZlbicsIC8vbW91c2VvdmVyIGJlaGF2aW9yICgncGF1c2UnICdjdXJzb3IgZHJpdmVuJyBvciBmYWxzZSkKbW92ZWF0bGVhc3Q6IDIsCm5ldXRyYWw6IDE1MCwKc2F2ZWRpcmVjdGlvbjogdHJ1ZQp9KTsKPC9zY3JpcHQ+ZAIFD2QWAmYPDxYCHwZoZGQYAwUeX19Db250cm9sc1JlcXVpcmVQb3N0QmFja0tleV9fFgEFNXBsY1Jvb3QkTGF5b3V0JHpvbmVTZWFyY2gkY21zc2VhcmNoYm94JGJ0bkltYWdlQnV0dG9uBRR2aWV3U3RhdGUkZ3JpZFN0YXRlcw9nZAUUbG9nUXVlcnkkZ3JpZFF1ZXJpZXMPZ2R1SEgqGqtYX2mDJLJ6GzZr8xtpcA==")
//                .data("__VIEWSTATEGENERATOR", "A5343185")
//                .data("lng", "vi-VN")
//                .data("manScript", "plcRoot$Layout$zoneMenu$PagePlaceholder$PagePlaceholder$Layout$zoneContent$pageplaceholder$pageplaceholder$Layout$zoneContent$DSATM$UpdatePanel1|plcRoot$Layout$zoneMenu$PagePlaceholder$PagePlaceholder$Layout$zoneContent$pageplaceholder$pageplaceholder$Layout$zoneContent$DSATM$ddlTinh")
//                .data("plcRoot$Layout$zoneMenu$PagePlaceholder$PagePlaceholder$Layout$zoneContent$pageplaceholder$pageplaceholder$Layout$zoneContent$DSATM$ddlHuyen", "0")
//                .data("plcRoot$Layout$zoneMenu$PagePlaceholder$PagePlaceholder$Layout$zoneContent$pageplaceholder$pageplaceholder$Layout$zoneContent$DSATM$ddlTinh", "460")
//                .data("plcRoot$Layout$zoneMenu$PagePlaceholder$PagePlaceholder$Layout$zoneContent$pageplaceholder$pageplaceholder$Layout$zoneContent$DSATM$ddlXa", "0")
//                .data("plcRoot$Layout$zoneMenu$PagePlaceholder$PagePlaceholder$Layout$zoneContent$pageplaceholder$pageplaceholder$Layout$zoneContent$DSATM$hddCurrPage", "1")
//                .data("plcRoot$Layout$zoneSearch$cmssearchbox$txtWord", "Tìm kiếm")
//                .post();
//        System.out.println(doc2.toString());
//    }
    public void getBIDVATMLocation() throws FailingHttpStatusCodeException, IOException, InterruptedException {
        String url = "http://www.bidv.com.vn/chinhanh/ATM.aspx";
        WebClient web = new WebClient(BrowserVersion.FIREFOX_3_6);
//        web.setAjaxController(new NicelyResynchronizingAjaxController());
//        web.setJavaScriptTimeout(2000);
        web.waitForBackgroundJavaScript(2000);
        web.setThrowExceptionOnScriptError(false);
        web.setThrowExceptionOnFailingStatusCode(false);
        web.setPrintContentOnFailingStatusCode(false);
        web.setJavaScriptEnabled(true);
        web.setRedirectEnabled(true);
//            web.setThrowExceptionOnFailingStatusCode(true);
//        web.setPrintContentOnFailingStatusCode(false);
        HtmlPage page = web.getPage(url);
        HtmlSelect ddTinh = page.getHtmlElementById("plcRoot_Layout_zoneMenu_PagePlaceholder_PagePlaceholder_Layout_zoneContent_pageplaceholder_pageplaceholder_Layout_zoneContent_DSATM_ddlTinh");
//        List<HtmlOption> tinh = ddTinh.getOptions();
//        for (HtmlOption ho:tinh){
//            System.out.println(ho.asText()+"     "+ho.getAttribute("value"));
//        }
        HtmlOption hanoi = ddTinh.getOptionByText("Hà Nội");
        ddTinh.setSelectedAttribute(hanoi, true);
        synchronized (page) {
            page.wait(2000);
        }
//        HtmlSelect ddHuyen = page.getHtmlElementById("plcRoot_Layout_zoneMenu_PagePlaceholder_PagePlaceholder_Layout_zoneContent_pageplaceholder_pageplaceholder_Layout_zoneContent_DSATM_ddlHuyen");
//        List<HtmlOption> huyen = ddHuyen.getOptions();
//        for (HtmlOption ho:huyen){
//            System.out.println(ho.asText()+"      "+ho.getValueAttribute());
//        }
        HtmlDivision tableDiv = (HtmlDivision) page.getElementById("plcRoot_Layout_zoneMenu_PagePlaceholder_PagePlaceholder_Layout_zoneContent_pageplaceholder_pageplaceholder_Layout_zoneContent_DSATM_UpdatePanel1");
        DomNodeList<HtmlElement> list = tableDiv.getElementsByTagName("table");
        for (HtmlElement he:list){
//            HtmlTable ht = (HtmlTable) he;
//            System.out.println(ht.asText());
            System.out.println(he.asText());
            System.out.println("------------------------------");
        }
        
        
//        HtmlTable table = (HtmlTable) tableDiv.querySelector("table");;
//        System.out.println(table.asText());

    }

    public static void main(String[] args) throws IOException, ScriptException, FailingHttpStatusCodeException, InterruptedException {
//        try {
        // TODO code application logic here
        ATMLocation atm = new ATMLocation();
        atm.getBIDVATMLocation();
//        atm.getEximATMLocation();
//        atm.getDongAATMLocation();
    }

}
//manScript=plcRoot$Layout$zoneMenu$PagePlaceholder$PagePlaceholder$Layout$zoneContent$pageplaceholder$pageplaceholder$Layout$zoneContent$DSATM$UpdatePanel1|plcRoot$Layout$zoneMenu$PagePlaceholder$PagePlaceholder$Layout$zoneContent$pageplaceholder$pageplaceholder$Layout$zoneContent$DSATM$ddlTinh

//manScript = plcRoot$Layout$zoneMenu$PagePlaceholder$PagePlaceholder$Layout$zoneContent$pageplaceholder$pageplaceholder$Layout$zoneContent$DSATM$UpdatePanel1|plcRoot$Layout$zoneMenu$PagePlaceholder$PagePlaceholder$Layout$zoneContent$pageplaceholder$pageplaceholder$Layout$zoneContent$DSATM$ddlHuyen
