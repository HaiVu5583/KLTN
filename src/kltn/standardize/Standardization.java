/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kltn.standardize;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kltn.dao.ATMLocationDAO;
import kltn.dao.AreaDAO;
import kltn.dao.EssentialWordDAO;
import kltn.entity.Area;
import kltn.entity.AtmLocation;
import kltn.entity.EssentialWord;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Vu
 */
public class Standardization {

    public static String[] check(List<Area> areaList, Character type, String str) {
        String[] result = {"0", ""};
        for (Area a : areaList) {
            String shortName = a.getShortname();
            String[] names = shortName.split(",");

//            System.out.println(names.);
            for (String s : names) {
                if (str.toLowerCase().contains(s.trim())) {
                    if (a.getType() == type && type == '1' && !checkRegex(str.toLowerCase(), "((.*)(\\d+)(.*))")) {
                        result[0] = "1";
                        result[1] = a.getProvince();
                    } else if (a.getType() == type && type == '2' && !checkRegex(str.toLowerCase(), "((.*)(\\d+)(.*))")) {
                        result[0] = "2";
                        result[1] = a.getDistrict();
                    } else if (a.getType() == type && type == '3' && !checkRegex(str.toLowerCase(), "((.*)(\\d+)(.*))")) {
                        result[0] = "3";
                        result[1] = a.getPrecinct();
                    } else if (a.getType() == type && type == '4') {
                        result[0] = "4";
                        result[1] = a.getStreet();
                    }
                }
            }
        }
        return result;
    }

    public static boolean check(List<Area> areaList, String str) {
        for (Area a : areaList) {
            String shortName = a.getShortname();
            String[] names = shortName.split(",");
            for (String s : names) {
                if (str.toLowerCase().contains(s.trim())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean checkRegex(String s, String regex) {
        Pattern r = Pattern.compile(regex);
        Matcher m = r.matcher(s.toLowerCase());
        return m.find();
    }

    public static int findNumberPosition(String s) {
        String[] arr = s.split(" ");
        System.out.println(arr.length);
        if (arr.length == 1) {
            return 0;
        } else {
            for (int i = 0; i < arr.length; i++) {
                try {
                    int z = Integer.parseInt(arr[i]);
                    return i;
                } catch (NumberFormatException nfe) {

                }
            }
            return 0;
        }
    }

    public static boolean checkNumber(String s) {
        try {
            int z = Integer.parseInt(s);
        } catch (NumberFormatException nbe) {
            return false;
        }
        return true;
    }

    public static boolean checkUpper(String s) {
        if (s.length() >= 1) {
            return Character.isUpperCase(s.charAt(0));
        }
        return false;
    }

    public static List split(String s, String pattern) {
        List<String> list = new ArrayList();
        String[] arr = s.split(pattern);
        for (int i = 0; i < arr.length; i++) {
            if (!arr[i].trim().isEmpty()) {
                list.add(arr[i].trim());
            }
        }
        return list;
    }

    public static int checkType(String s) {
        String[] arr = s.split(" ");
//        System.out.println(arr.length);
        if (arr.length == 1) {
            return 0;
        } else {
            if (s.toLowerCase().contains("thành phố") || s.toLowerCase().contains("tỉnh")) {
                return 1;
            } else if (s.toLowerCase().contains("quận") || s.toLowerCase().contains("huyện")) {
                return 2;
            } else if (s.toLowerCase().contains("xã") || s.toLowerCase().contains("thị trấn")) {
                return 3;
            } else if (arr[0].toLowerCase().trim().equals("tp") || arr[0].toLowerCase().trim().equals("tỉnh") || arr[0].toLowerCase().trim().equals("thành phố")) {
                return 1;
            } else if (arr[0].toLowerCase().contains("q.") || arr[0].toLowerCase().trim().equals("q")
                    || arr[0].toLowerCase().trim().equals("quận") || arr[0].toLowerCase().contains("h.")
                    || arr[0].toLowerCase().trim().equals("h") || arr[0].toLowerCase().trim().equals("huyện")) {
                return 2;
            } else if (arr[0].toLowerCase().contains("p.") || arr[0].toLowerCase().trim().equals("p")
                    || arr[0].toLowerCase().trim().equals("phường") || arr[0].toLowerCase().contains("tx.")
                    || arr[0].toLowerCase().trim().equals("tx") || arr[0].toLowerCase().trim().equals("thị xã")
                    || arr[0].toLowerCase().contains("tt.") || arr[0].toLowerCase().trim().equals("tt")
                    || arr[0].toLowerCase().trim().equals("thị trấn") || arr[0].toLowerCase().contains("x.")
                    || arr[0].toLowerCase().trim().equals("x") || arr[0].toLowerCase().trim().equals("xã")) {
                return 3;
            }
            return 0;
        }
    }

    public static String cutString(String s, String[] sub) {
        String[] arr = s.split(" ");
        StringBuilder sb = new StringBuilder();
        List<String> sub1 = new ArrayList<>();
        List<String> sub2 = new ArrayList<>();
        for (String _sub : sub) {
            if (_sub.length() == 1) {
                sub1.add(_sub);
            } else {
                sub2.add(_sub);
            }
        }
        for (String s1 : arr) {
            int i = 0;
            for (String _sub1 : sub1) {
                if (!s1.toLowerCase().equals(_sub1)) {
                    i++;
                }
            }
            if (i == sub1.size()) {
                sb.append(s1);
                sb.append(" ");
            }
        }
        s = sb.toString().trim();
//        System.out.println(s);
        for (String _sub2 : sub2) {
            if (s.contains(_sub2)) {
                s = s.substring(0, s.indexOf(_sub2)) + s.substring(s.indexOf(_sub2) + _sub2.length(), s.length());
            }
        }
        return oneSpace(s.trim());

    }

    public static String oneSpace(String s) {
        List<String> ls = split(s, " ");
        StringBuilder sb = new StringBuilder();
        for (String s1 : ls) {
            sb.append(s1);
            sb.append(" ");
        }
        return sb.toString().trim();
    }

    public static boolean isNotAddress(String s, List<EssentialWord> list) {
        for (EssentialWord ew : list) {
            String[] arr = ew.getDisplay().split(",");
            for (String str : arr) {
                if (s.toLowerCase().contains(str.trim())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isExactAddress(String s) {
        return checkRegex(s, "(.)*(\\d+)(.)*");
    }

    public static void main(String[] args) {
//        ATMLocationDAO atmDAO = new ATMLocationDAO();
//        AreaDAO areaDAO = new AreaDAO();
//        List<AtmLocation> atmList = atmDAO.findByFullAddressAndDistrictNotNull();
//        List<Area> areaList = areaDAO.listAll();
//        System.out.println(atmList.size());
//        for (AtmLocation a : atmList) {
//            String s = a.getFulladdress().replace(".", " ");
//            s = oneSpace(s);
//            List<String> arr = split(s, "\\,|\\-|\\(|\\)|\\/");
//            if (arr.isEmpty()) {
//            } else if (arr.size() == 1) {
//                a.setStreet(s);
//
//            } else if (arr.size() == 2) {
//                if (checkType(arr.get(1)) == 2 || checkType(arr.get(1)) == 1) {
//                    a.setStreet(arr.get(0));
//                } else if (check(areaList, '2', arr.get(1))[0].equals("2")) {
//                    a.setStreet(arr.get(0));
//                } else {
//                    String s1 = arr.get(0) + "," + arr.get(1);
//                    a.setStreet(s1);
//                }
//
//            } else {
//                if (checkType(arr.get(arr.size() - 1)) != 1 && checkType(arr.get(arr.size() - 1)) != 2 && checkType(arr.get(arr.size() - 1)) != 3
//                        && !check(areaList, '1', arr.get(arr.size() - 1))[0].equals("1") && !check(areaList, '2', arr.get(arr.size() - 1))[0].equals("2")
//                        && !check(areaList, '3', arr.get(arr.size() - 1))[0].equals("3")) {
//                    StringBuilder sb = new StringBuilder();
//                    for (int i = 0; i <= arr.size() - 1; i++) {
//                        sb.append(arr.get(i));
//                        if (i < arr.size() - 1) {
//                            sb.append(",");
//                        }
//                    }
//                    a.setStreet(sb.toString());
//                } else {
//                    StringBuilder sb = new StringBuilder();
//                    for (String s1 : arr) {
//
//                        if (checkType(s1) != 1 && checkType(s1) != 2 && checkType(s1) != 3
//                                && !check(areaList, '1', s1)[0].equals("1") && !check(areaList, '2', s1)[0].equals("2")
//                                && !check(areaList, '3', s1)[0].equals("3")) {
//                            sb.append(s1);
//                            sb.append(",");
//                        } else if (checkType(s1) == 3 || check(areaList, '3', s1)[0].equals("3")) {
//                            String[] precinctTitle = {"xã", "thị trấn", "tt", "x", "tx", "thị xã"};
//                            a.setPrecinct(StringUtils.capitaliseAllWords(cutString(s1.toLowerCase(), precinctTitle)));
//                        }
//                    }
//                    a.setStreet(sb.toString());
//                    a.print();
//                }
//            }
//
//        }
//        atmDAO.updateAll(atmList);
//        System.out.println(checkType("hn") != 1 && checkType("hn") != 2 && checkType("hn") != 3
//                && !check(areaList, '1', "hn")[0].equals("1") && !check(areaList, '2', "hn")[0].equals("2")
//                && !check(areaList, '3', "hn")[0].equals("3"));
//        EssentialWordDAO essentialWordDAO = new EssentialWordDAO();
//        List<EssentialWord> rejectWord = essentialWordDAO.findByType('5');
//        System.out.println(isNotAddress("qtk 7", rejectWord));
        
        
        ATMLocationDAO atmDAO = new ATMLocationDAO();
        AreaDAO areaDAO = new AreaDAO();
        EssentialWordDAO wordDAO = new EssentialWordDAO();
        List<AtmLocation> atmList = atmDAO.findByFullAddressAndDistrictNotNull();
        List<Area> areaList = areaDAO.listAll();
        List<EssentialWord> rejectWord = wordDAO.findByType('5');
        for (AtmLocation atm:atmList){
            String s = atm.getFulladdress().replace(".", " ");
            s = oneSpace(s);
            List<String> splitStr = split(s, "\\,|\\-|\\(|\\)|\\/");
            for (int i=splitStr.size()-1; i>=0; i--){
                String element = splitStr.get(i).toLowerCase().trim();
                if(check(areaList, '1', element)[0].equals("1")){
                    atm.setProvinceCity(check(areaList, '1', element)[1]);
                }else if(check(areaList, '2', element)[0].equals("2")){
                    atm.setDistrict(check(areaList, '2', element)[1]);
                }else if(check(areaList, '3', element)[0].equals("3")){
                    atm.setPrecinct(check(areaList, '3', element)[1]);
                }else{
                    if(isExactAddress(element))
                        atm.setStreet(element);
                    break;
                }
            }
        }
        for (AtmLocation atm:atmList)
            atm.print();

    }

}
