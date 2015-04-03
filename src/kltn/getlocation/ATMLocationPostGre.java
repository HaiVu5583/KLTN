/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kltn.getlocation;

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
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Vu
 */
public class ATMLocationPostGre {

    /**
     * @throws java.io.IOException
     */
    //OK INSERTED DB
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
//        HashMap<Integer, String> maps = new HashMap();
        String hanoiID = null;
        for (Element e : locations) {
//            if (!e.val().equals("0")) {
//                maps.put(Integer.parseInt(e.val()), e.text());
//            }
            if (e.text().contains("Hà Nội")) {
                hanoiID = e.val();
            }
        }

        ArrayList<ATM> atmList = new ArrayList();
        ATM atm = new ATM();
//        Set<Integer> keys = maps.keySet();

//        for (Integer key : keys) {
        doc = Jsoup.connect("https://www.vietcombank.com.vn/ATM/default.aspx").timeout(10000)
                .data("__EVENTTARGET", pdv.getEVENTTARGET())
                .data("__EVENTARGUMENT", pdv.getEVENTARGUMENT())
                .data("__LASTFOCUS", pdv.getLASTFOCUS())
                .data("__VIEWSTATE", pdv.getVIEWSTATE())
                .data("__VIEWSTATEENCRYPTED", pdv.getVIEWSTATEENCRYPTED())
                .data("ctl00$Content$CityList", hanoiID)
                .post();
        Elements list = doc.select("table#ctl00_Content_ATMView tr td");
        for (int i = 0; i < list.size(); i++) {
            Element e = list.get(i);
            if (i % 4 == 0) {

            } else if (i % 4 == 1) {
                atm.setNumOfMachine(e.text());
            } else if (i % 4 == 2) {
                atm.setAddress(e.text());
            } else {
                atm.setOpenTime(e.text());
                atm.setProvince_city("Hà Nội");
                atm.setBank("vietcombank");
                atmList.add(atm);
                atm = new ATM();
            }
        }

//        }
        for (ATM a : atmList) {
            a.print();
        }
        System.out.println(Integer.toString(atmList.size()));
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/KLTN?useUnicode=true&characterEncoding=UTF-8", "postgres", "12345");

            PreparedStatement stmt = con.prepareStatement("INSERT INTO \"KLTN\".atm_location(fulladdress, bank, opentime, nummachine, province_city) VALUES (?, ?, ?, ?, ?)");
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

    //OK INSERTED DB
    public void getAgribankATMLocation() throws IOException {
        Document doc = Jsoup.connect("http://agribank.com.vn/71/1147/mang-luoi---atm-pos.aspx").timeout(10000).get();
        Element locationList = doc.select("#cphMain_Map1_ddlTinh").first();
        HashMap<Integer, String> maps = new HashMap();
        Elements locations = locationList.children();
        for (Element location : locations) {
            if (location.text().contains("Hà Nội")) {
                maps.put(Integer.parseInt(location.val()), location.text());
            }

        }
        Set<Integer> keys = maps.keySet();
        ArrayList<ATM> atmList = new ArrayList<>();
        ATM atm = new ATM();
        for (Integer key : keys) {
            doc = Jsoup.connect("http://agribank.com.vn/tim-kiem/atm/1147/" + key.toString() + "/0/ket-qua.aspx").timeout(10000).get();
            Element div = doc.select("#ATM").first();
            Elements tables = div.select(".agri-atm-tbl");
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
                    if (tr.select("th").text().equals("")) {
                        atm.setBank("agribank");
                        atm.setProvince_city("Hà Nội");
                        atm.setDistrict(district);
                        atm.setStreet(tr.select("td.agri-atm-tbl-name").text());
                        atm.setOpenTime(tr.select("td.agri-atm-tbl-xem").text());
                        String numMachine = tr.select("td.agri-atm-tbl-time").text();
                        String[] arr = numMachine.split("");
                        numMachine = arr[0].trim();
                        atm.setNumOfMachine(numMachine);
                        atmList.add(atm);
                        atm = new ATM();
                    }
                }

            }
        }
        System.out.println(Integer.toString(atmList.size()));
        for (ATM a : atmList) {
            a.print();
        }
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
//            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/kltn?useUnicode=true&characterEncoding=UTF-8", "root", "");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/KLTN?useUnicode=true&characterEncoding=UTF-8", "postgres", "12345");

            PreparedStatement stmt = con.prepareStatement("INSERT INTO \"KLTN\".atm_location(bank, openTime, nummachine, province_city, district, street) VALUES (?, ?, ?, ?, ?, ?)");
            for (ATM a : atmList) {
                stmt.setString(1, a.getBank());
                stmt.setString(2, a.getOpenTime());
                stmt.setInt(3, Integer.parseInt(a.getNumOfMachine()));
                stmt.setString(4, a.getProvince_city());
                stmt.setString(5, a.getDistrict());
                stmt.setString(6, a.getStreet());
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

    //OK INSERTED DB
    public void getVietinbankATMLocation() throws IOException {
        Document doc = Jsoup.connect("https://card.vietinbank.vn/sites/home/vi/diem-dat-atm/").timeout(20000).get();
        Element hanoiDistrictDropdown = doc.select("#ajaxmenu1").first();
//        System.out.println(hanoiDistrictList.toString());
        Elements hanoiDistrictList = hanoiDistrictDropdown.children();
        HashMap<String, String> maps = new HashMap<>();
        for (Element e : hanoiDistrictList) {
            if (!e.val().equals("")) {
                maps.put(e.text(), "https://card.vietinbank.vn" + e.val());
            }
        };

        Set<String> district = maps.keySet();
        System.out.println(district.toString());
        List<ATM> atmList = new ArrayList<>();
        for (String d : district) {
            Document doc2 = Jsoup.connect(maps.get(d)).timeout(10000).get();
//            System.out.println(doc2.toString());
            Elements tds = doc2.select("td");
            ATM a = new ATM();
            a.setProvince_city("Hà Nội");
            a.setDistrict(d);
            a.setBank("vietinbank");
            for (int i = 0; i < tds.size(); i++) {

                if (i % 5 == 0) {
//                    System.out.println("");
                } else if (i % 5 == 1) {
                    a.setAddress(tds.get(i).text());
//                    System.out.println("Address: " + tds.get(i).text());
                } else if (i % 5 == 2) {
                    a.setNumOfMachine(tds.get(i).text());
//                    System.out.println("Num Of Machine: " + tds.get(i).text());

                } else if (i % 5 == 3) {
//                    System.out.println("Branch: " + tds.get(i).text());
                } else if (i % 5 == 4) {
                    a.setPhone(tds.get(i).text());
                    atmList.add(a);
                    a = new ATM();
                    a.setProvince_city("Hà Nội");
                    a.setDistrict(d);
                    a.setBank("vietinbank");
//                    System.out.println("Phone: " + tds.get(i).text());
//                    System.out.println("--------------------------------------");
                }

            }
        }
        System.out.println(Integer.toString(atmList.size()));
        atmList.stream().forEach((a) -> {
            a.print();
        });
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
//            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/kltn?useUnicode=true&characterEncoding=UTF-8", "root", "");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/KLTN?useUnicode=true&characterEncoding=UTF-8", "postgres", "12345");

            PreparedStatement stmt = con.prepareStatement("INSERT INTO \"KLTN\".atm_location(fulladdress, bank, nummachine, province_city, district, phone) VALUES (?, ?, ?, ?, ?, ?)");
            for (ATM a : atmList) {
                stmt.setString(1, a.getAddress());
                stmt.setString(2, a.getBank());
                if (!a.getNumOfMachine().equals("-")) {
                    stmt.setInt(3, Integer.parseInt(a.getNumOfMachine()));
                } else {
                    stmt.setInt(3, 0);
                }
                stmt.setString(4, a.getProvince_city());
                stmt.setString(5, a.getDistrict());
                stmt.setString(6, a.getPhone());
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

    public void getBIDVATMLocation() throws FailingHttpStatusCodeException, IOException, InterruptedException {
        String url = "http://www.bidv.com.vn/chinhanh/ATM.aspx";
        WebClient web = new WebClient(BrowserVersion.FIREFOX_3_6);
        web.setAjaxController(new NicelyResynchronizingAjaxController());
        web.waitForBackgroundJavaScript(4000);
        web.setThrowExceptionOnScriptError(false);
        web.setThrowExceptionOnFailingStatusCode(false);
        web.setPrintContentOnFailingStatusCode(false);
        web.setJavaScriptEnabled(true);
        web.setRedirectEnabled(true);
        web.setCssEnabled(false);

        List<ATM> atmList = new ArrayList<>();
        HtmlPage page = web.getPage(url);
        HtmlSelect ddTinh = page.getHtmlElementById("plcRoot_Layout_zoneMenu_PagePlaceholder_PagePlaceholder_Layout_zoneContent_pageplaceholder_pageplaceholder_Layout_zoneContent_DSATM_ddlTinh");
        HtmlOption hanoi = ddTinh.getOptionByText("Hà Nội");

        ddTinh.setSelectedAttribute(hanoi, true);
        synchronized (page) {
            page.wait(10000);
        }
        HtmlSelect ddHuyen = page.getHtmlElementById("plcRoot_Layout_zoneMenu_PagePlaceholder_PagePlaceholder_Layout_zoneContent_pageplaceholder_pageplaceholder_Layout_zoneContent_DSATM_ddlHuyen");
        List<HtmlOption> huyen = ddHuyen.getOptions();
        for (HtmlOption ho : huyen) {
            System.out.println("Value: " + ho.getValueAttribute() + " // Text: " + ho.asText());
        }
        int size = huyen.size();
//        for (HtmlOption ho:huyen){
//            ddHuyen.setSelectedAttribute(ho, true);
//            System.out.println("Huyen: "+ho.asText());
//            System.out.println("==================================================");
//            synchronized(page){
//               page.wait(10000);
//               
//            }
//            HtmlDivision tableDiv = (HtmlDivision) page.getElementById("plcRoot_Layout_zoneMenu_PagePlaceholder_PagePlaceholder_Layout_zoneContent_pageplaceholder_pageplaceholder_Layout_zoneContent_DSATM_UpdatePanel1");
//            HtmlElement content = tableDiv.getElementsByAttribute("div", "class", "tim_atm_box").get(1);
//            DomNodeList<HtmlElement> list = content.getElementsByTagName("table");
//            for (HtmlElement he : list) {
//                if (!he.asText().equals("")) {
//                    System.out.println(he.asText());
//                    System.out.println("------------------------------");
//                }
//            }
//        }
//        System.out.println(huyen.toString());
        for (int i = 1; i < size; i++) {
            HtmlPage page2 = web.getPage(url);
            HtmlSelect ddTinh2 = page2.getHtmlElementById("plcRoot_Layout_zoneMenu_PagePlaceholder_PagePlaceholder_Layout_zoneContent_pageplaceholder_pageplaceholder_Layout_zoneContent_DSATM_ddlTinh");
            HtmlOption hanoi2 = ddTinh2.getOptionByText("Hà Nội");
            ddTinh2.setSelectedAttribute(hanoi, true);
            synchronized (page2) {
                page2.wait(10000);
            }
            HtmlSelect ddHuyen2 = page.getHtmlElementById("plcRoot_Layout_zoneMenu_PagePlaceholder_PagePlaceholder_Layout_zoneContent_pageplaceholder_pageplaceholder_Layout_zoneContent_DSATM_ddlHuyen");
            ddHuyen2.setSelectedAttribute(ddHuyen2.getOption(i), true);
            System.out.println("Huyện: " + ddHuyen2.getOption(i).asText());
            System.out.println("===============================================");
            synchronized (page2) {
                page2.wait(10000);
            }
            HtmlDivision tableDiv = (HtmlDivision) page2.getElementById("plcRoot_Layout_zoneMenu_PagePlaceholder_PagePlaceholder_Layout_zoneContent_pageplaceholder_pageplaceholder_Layout_zoneContent_DSATM_UpdatePanel1");
            HtmlElement content = tableDiv.getElementsByAttribute("div", "class", "tim_atm_box").get(1);
            DomNodeList<HtmlElement> list = content.getElementsByTagName("table");
            for (HtmlElement he : list) {
                if (!he.asText().equals("")) {
                    System.out.println(he.asText());
                    System.out.println("------------------------------");
                }
            }
        }
    }

    public void getBIDVATMLocation2() throws IOException, InterruptedException {
        String url = "http://www.bidv.com.vn/chinhanh/ATM.aspx";
        WebClient web = new WebClient(BrowserVersion.FIREFOX_3_6);
        web.setAjaxController(new NicelyResynchronizingAjaxController());
        web.waitForBackgroundJavaScript(4000);
        web.setThrowExceptionOnScriptError(false);
        web.setThrowExceptionOnFailingStatusCode(false);
        web.setPrintContentOnFailingStatusCode(false);
        web.setJavaScriptEnabled(true);
        web.setRedirectEnabled(true);
        web.setCssEnabled(false);

        List<ATM> atmList = new ArrayList<>();
        HtmlPage page = web.getPage(url);
        HtmlSelect ddTinh = page.getHtmlElementById("plcRoot_Layout_zoneMenu_PagePlaceholder_PagePlaceholder_Layout_zoneContent_pageplaceholder_pageplaceholder_Layout_zoneContent_DSATM_ddlTinh");
        HtmlOption hanoi = ddTinh.getOptionByText("Hà Nội");

        ddTinh.setSelectedAttribute(hanoi, true);
        synchronized (page) {
            page.wait(10000);
        }
        HtmlSelect ddHuyen = page.getHtmlElementById("plcRoot_Layout_zoneMenu_PagePlaceholder_PagePlaceholder_Layout_zoneContent_pageplaceholder_pageplaceholder_Layout_zoneContent_DSATM_ddlHuyen");
        List<HtmlOption> huyen = ddHuyen.getOptions();
        String hanoiID = hanoi.getValueAttribute();
        List<String> huyenID = new ArrayList<>();
        for (HtmlOption ho : huyen) {
            huyenID.add(ho.getValueAttribute());
        }
        int size = huyen.size();
        for (int i = 1; i < size; i++) {
            HtmlPage page2 = web.getPage(url);
//        System.out.println(page2.asText());
            HtmlSelect ddTinh2 = page2.getHtmlElementById("plcRoot_Layout_zoneMenu_PagePlaceholder_PagePlaceholder_Layout_zoneContent_pageplaceholder_pageplaceholder_Layout_zoneContent_DSATM_ddlTinh");
            HtmlOption hanoi2 = ddTinh2.getOptionByText("Hà Nội");
//
            ddTinh2.setSelectedAttribute(hanoi2, true);
            synchronized (page2) {
                page2.wait(20000);
            }
            HtmlSelect ddHuyen2 = page2.getHtmlElementById("plcRoot_Layout_zoneMenu_PagePlaceholder_PagePlaceholder_Layout_zoneContent_pageplaceholder_pageplaceholder_Layout_zoneContent_DSATM_ddlHuyen");
//            System.out.println("DDHuyen2: " + ddHuyen2.asXml());
            HtmlOption ho = ddHuyen2.getOption(i);
            System.out.println("Huyen: " + ho.asText());
            String district = ho.asText();
            System.out.println("==========================================================");
//            System.out.println("HO " + ho.asXml());
            ddHuyen2.setSelectedAttribute(ho, true);
            synchronized (page2) {
                page2.wait(20000);
            }
//            System.out.println(page2.asText());
            HtmlDivision div = (HtmlDivision) page2.getElementById("plcRoot_Layout_zoneMenu_PagePlaceholder_PagePlaceholder_Layout_zoneContent_pageplaceholder_pageplaceholder_Layout_zoneContent_DSATM_UpdatePanel1");
            HtmlDivision content = (HtmlDivision) div.getElementsByAttribute("div", "class", "tim_atm_box").get(1);
            DomNodeList<HtmlElement> atmTables = content.getElementsByTagName("table");
            for (int j = 1; j < atmTables.size(); j++) {
                HtmlElement currentElement = atmTables.get(j);
                ATM atm = new ATM();
                atm.setBank("bidv");
                atm.setProvince_city("Hà Nội");
                atm.setDistrict(district);
                String address = currentElement.getElementsByAttribute("td", "class", "diadanh_sub11").get(0).asText();
                String time = currentElement.getElementsByAttribute("td", "class", "hoatdong_sub11").get(0).asText();
                String machineCode = currentElement.getElementsByAttribute("td", "class", "hoatdong_sub11").get(1).asText();
                atm.setUniqueCode(machineCode);
                atm.setStreet(address);
                atm.setOpenTime(time);
                atmList.add(atm);
            }
        }
        for (ATM a : atmList) {
            a.print();
        }
        System.out.println(Integer.toString(atmList.size()));
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
//            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/kltn?useUnicode=true&characterEncoding=UTF-8", "root", "");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/KLTN?useUnicode=true&characterEncoding=UTF-8", "postgres", "12345");

            PreparedStatement stmt = con.prepareStatement("INSERT INTO \"KLTN\".atm_location(province_city, district, street, bank, opentime, uniqueCode) VALUES (?, ?, ?, ?, ?, ?)");
            for (ATM a : atmList) {
                stmt.setString(1, a.getProvince_city());
                stmt.setString(2, a.getDistrict());
                stmt.setString(3, a.getStreet());
                stmt.setString(4, a.getBank());
                stmt.setString(5, a.getOpenTime());
                stmt.setString(6, a.getUniqueCode());
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

    //OK INSERTED DB
    public void getMBBankLocation() throws IOException, GeneralSecurityException {
        String mainUrl = "https://www.mbbank.com.vn/mangluoi/Lists/ATM/AllItems.aspx";
        WebClient web = new WebClient(BrowserVersion.FIREFOX_3_6);
        web.waitForBackgroundJavaScript(10000);
        web.setUseInsecureSSL(true);
        web.setThrowExceptionOnScriptError(false);
        web.setThrowExceptionOnFailingStatusCode(false);
        web.setPrintContentOnFailingStatusCode(false);
        web.setJavaScriptEnabled(true);
        web.setRedirectEnabled(true);

        HtmlPage page = web.getPage(mainUrl);
        HtmlSelect cityList = page.getHtmlElementById("ctl00_m_g_4a61401d_237a_468a_b004_114aaf8a9af0_ctl01_DropDownList_Field");
        HtmlOption hanoi = cityList.getOptionByText("Hà Nội");
        cityList.setSelectedAttribute(hanoi, true);
        String url = mainUrl + hanoi.getValueAttribute();
        page = web.getPage(url);
        HtmlSelect districtList = (HtmlSelect) page.getHtmlElementById("ctl00_m_g_4a61401d_237a_468a_b004_114aaf8a9af0_ctl01_DropDownList_Field2");
        List<HtmlOption> districs = districtList.getOptions();
//        List<String> list = new ArrayList<>();
        List<ATM> atmList = new ArrayList<>();
        for (HtmlOption o : districs) {
            System.out.println(o.asText());
            String _url = mainUrl + o.getValueAttribute();
            HtmlPage _page = web.getPage(_url);
            HtmlDivision div = (HtmlDivision) _page.getElementById("WebPartWPQ12");
            HtmlTable table = (HtmlTable) div.getElementsByTagName("table").get(0);
            HtmlElement tBody = table.getElementsByTagName("tbody").get(0);
            DomNodeList<HtmlElement> trs = tBody.getElementsByTagName("tr");
            for (int i = 1; i < trs.size(); i++) {
                HtmlElement tr = trs.get(i);
                DomNodeList<HtmlElement> tds = tr.getElementsByTagName("td");
                ATM atm = new ATM();
                atm.setBank("mbbank");
                atm.setProvince_city("Hà Nội");
                atm.setDistrict(o.asText());
                atm.setAddress(tds.get(3).asText());
                atmList.add(atm);

            }
        }
        for (ATM a : atmList) {
            a.print();
        }
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
//            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/kltn?useUnicode=true&characterEncoding=UTF-8", "root", "");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/KLTN?useUnicode=true&characterEncoding=UTF-8", "postgres", "12345");

            PreparedStatement stmt = con.prepareStatement("INSERT INTO atm_location(fulladdress, bank, province_city, district) VALUES (?, ?, ?, ?)");
            for (ATM a : atmList) {
                stmt.setString(1, a.getAddress());
                stmt.setString(2, a.getBank());
                stmt.setString(3, a.getProvince_city());
                stmt.setString(4, a.getDistrict());
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

    public void transferMBATMlocation() {

        try {
            Connection conMysql = DriverManager.getConnection("jdbc:mysql://localhost:3306/kltn?useUnicode=true&characterEncoding=UTF-8", "root", "");
            Connection conPostgre = DriverManager.getConnection("jdbc:postgresql://localhost:5432/KLTN?useUnicode=true&characterEncoding=UTF-8", "postgres", "12345");
            PreparedStatement stmt = conMysql.prepareStatement("SELECT * FROM atm_location WHERE bank = ?");
            stmt.setString(1, "mbbank");
            ResultSet rs = stmt.executeQuery();
            List<ATM> atmList = new ArrayList<>();
            while (rs.next()) {
//                System.out.println(rs.getString("province_city"));
//                System.out.println(rs.getString("fullAddress"));
//                System.out.println("---------------------------------------------");
                ATM atm = new ATM();
                atm.setBank(rs.getString("bank"));
                atm.setProvince_city(rs.getString("province_city"));
                atm.setAddress(rs.getString("fullAddress"));
                atmList.add(atm);
            }
            System.out.println(atmList.size());
            for (ATM a : atmList) {
                a.print();
            }
            PreparedStatement stmt2 = conPostgre.prepareStatement("INSERT INTO \"KLTN\".atm_location(province_city, bank, fulladdress) VALUES(?, ?, ?)");
            for (ATM a : atmList) {
                stmt2.setString(1, a.getProvince_city());
                stmt2.setString(2, a.getBank());
                stmt2.setString(3, a.getAddress());
                stmt2.execute();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ATMLocationPostGre.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getSHBATMLocation() throws GeneralSecurityException, IOException {
        String url = "http://www.shb.com.vn/tabid/500/default.aspx";
        WebClient web = new WebClient(BrowserVersion.FIREFOX_3_6);
        web.waitForBackgroundJavaScript(10000);
        web.setUseInsecureSSL(true);
        web.setThrowExceptionOnScriptError(false);
        web.setThrowExceptionOnFailingStatusCode(false);
        web.setPrintContentOnFailingStatusCode(false);
        web.setJavaScriptEnabled(true);
        web.setRedirectEnabled(true);

        HtmlPage page = web.getPage(url);
        System.out.println(page.asXml());
//        HtmlInput radioATM = (HtmlInput) page.getElementById("dnn_ctr478_ViewGoogleMap_StandartView_option_chose_1");
//        radioATM.setAttribute("checked", "checked");
//        HtmlSelect cityList = (HtmlSelect)page.getHtmlElementById("dnn_ctr478_ViewGoogleMap_StandartView_drcate");
//        System.out.println(cityList.asXml());

    }

    //OK INSERTED DB
    public void getTechcombankATMLocation() throws IOException {
        String mainUrl = "https://www.techcombank.com.vn/mang-luoi-dia-diem-atm/danh-sach-chi-nhanh-phong-giao-dich-va-atm";
        Document doc = Jsoup.connect(mainUrl).timeout(10000).get();
        Element cityList = doc.getElementById("fCityId");
        Elements cities = cityList.children();
        Element hanoi = null;
        for (Element e : cities) {
            if (e.text().contains("Hà Nội")) {
                hanoi = e;
            }
        }
        String url = "https://www.techcombank.com.vn/mang-luoi-dia-diem-atm/danh-sach-chi-nhanh-phong-giao-dich-va-atm?fKeyword=&fCityId=" + hanoi.val() + "&fDistrictId=0&chkBranch=false&chkPriority=false&page=1&pageItems=50";
        Document doc2 = Jsoup.connect(url).timeout(10000).get();
        Element paging = doc2.select(".paging").first();
        Elements pageLinks = paging.select("a");
        String finalPageLink = null;
        for (Element e : pageLinks) {
            if (e.text().toLowerCase().contains("cuối")) {
                finalPageLink = e.attr("href");
            }
        }
        int finalPageNumber = Integer.parseInt(finalPageLink.split("&page=")[1].split("&")[0]);
        List<ATM> atmList = new ArrayList<>();
        for (int i = 1; i <= finalPageNumber; i++) {
            url = "https://www.techcombank.com.vn/mang-luoi-dia-diem-atm/danh-sach-chi-nhanh-phong-giao-dich-va-atm?fKeyword=&fCityId=" + hanoi.val() + "&fDistrictId=0&chkBranch=false&chkPriority=false&page=" + Integer.toString(i) + "&pageItems=50";
            doc2 = Jsoup.connect(url).timeout(10000).get();
            Element atmEntries = doc2.select(".entries").first();
            Elements atmDescriotions = atmEntries.select(".description");

            for (Element e : atmDescriotions) {

                ATM atm = new ATM();
                atm.setBank("techcombank");
                atm.setProvince_city("Hà Nội");
                atm.setAddress(e.select(".address").first().text());
                if (!e.select(".tel").isEmpty()) {
                    atm.setPhone(e.select(".tel").first().text());
                }
                atm.setOpenTime(e.select(".timer").first().text());
                atmList.add(atm);
            }
        }
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
//            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/kltn?useUnicode=true&characterEncoding=UTF-8", "root", "");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/KLTN?useUnicode=true&characterEncoding=UTF-8", "postgres", "12345");

            PreparedStatement stmt = con.prepareStatement("INSERT INTO \"KLTN\".atm_location(fulladdress, bank, province_city, opentime, phone) VALUES (?, ?, ?, ?, ?)");
            for (ATM a : atmList) {
                stmt.setString(1, a.getAddress());
                stmt.setString(2, a.getBank());
                stmt.setString(3, a.getProvince_city());
                String openTime = a.getOpenTime();
                String[] arr = openTime.split("Giờ mở cửa:");
                openTime = arr[1].trim();
                stmt.setString(4, openTime);
                stmt.setString(5, a.getPhone());
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
        System.out.println(Integer.toString(atmList.size()));
        for (ATM a : atmList) {
            a.print();
        }
    }

//    public static void main(String[] args) throws IOException, ScriptException, FailingHttpStatusCodeException, InterruptedException, GeneralSecurityException {
////        try {
//        // TODO code application logic here
//        ATMLocationPostGre atm = new ATMLocationPostGre();
////        atm.getVietcombankATMLocation();
////        atm.getVietinbankATMLocation();
////        atm.getAgribankATMLocation();
////        atm.getBIDVATMLocation2();
//        atm.transferMBATMlocation();
////        atm.getTechcombankATMLocation();
//    }

}
