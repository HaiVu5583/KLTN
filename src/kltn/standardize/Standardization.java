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
                        result[1] = a.getProvince().toLowerCase().trim();
                    } else if (a.getType() == type && type == '2' && !checkRegex(str.toLowerCase(), "((.*)(\\d+)(.*))")) {
                        result[0] = "2";
                        result[1] = a.getDistrict().toLowerCase().trim();
                    } else if (a.getType() == type && type == '3' && !checkRegex(str.toLowerCase(), "((.*)(\\d+)(.*))")) {
                        result[0] = "3";
                        result[1] = a.getPrecinct().toLowerCase().trim();
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

    public static String cutString(String s, List<EssentialWord> worldList) {
        String[] arr = s.split(" ");
        StringBuilder sb = new StringBuilder();
        List<String> sub1 = new ArrayList<>();
        List<String> sub2 = new ArrayList<>();
        List<String> sub = new ArrayList<>();
        for (EssentialWord ew : worldList) {
            sub.add(ew.getDisplay().toLowerCase().trim());
        }
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

    public static String cutString2(String s, String[] sub) {
        for (String _sub : sub) {
            if (s.contains(_sub)) {
                s = s.substring(0, s.indexOf(_sub)) + s.substring(s.indexOf(_sub) + _sub.length(), s.length());
            }
        }
        return oneSpace(s);

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
        boolean check1 = false;
        for (EssentialWord ew : list) {
            String[] arr = ew.getDisplay().split(",");
            for (String str : arr) {
                if (s.toLowerCase().contains(str.trim())) {
                    check1 = true;
                }
            }
        }
        String[] arrBySpace = s.split(" ");
        if (check1 == true && checkNumber(arrBySpace[arrBySpace.length - 1]) == true) {
            return true;
        } else if (check1 == true && !isExactAddress(s)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isDetermineLocation(String s, List<EssentialWord> list) {
        for (EssentialWord ew : list) {
            String[] arr = ew.getDisplay().split(",");
            String[] spaceSplit = s.split(" ");
            for (String str : arr) {
                if (str.split(" ").length == 1) {
                    for (String sp:spaceSplit){
                        if(sp.toLowerCase().trim().equals(str.trim())) return true;
                    }
                }else{
                    if (s.toLowerCase().contains(str.trim())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isExactAddress(String s) {
        return checkRegex(s, "(.)*(\\d+)(.)*");
    }

    public static boolean checkPrecinct(String precinct, String district, String province, List<Area> areaList) {
        for (Area a : areaList) {
            if (a.getType() == '3' && a.getPrecinct().toLowerCase().trim().equals(precinct.toLowerCase().trim())) {
                if (district != null) {
                    if (a.getDistrict().toLowerCase().trim().equals(district.toLowerCase().trim()) && a.getProvince().toLowerCase().trim().equals(province.toLowerCase().trim())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String replaceShortWriting(String s, List<EssentialWord> worldList) {
        List<String> list = split(s, " |//,");
        StringBuilder sb = new StringBuilder();
        for (String str : list) {
            System.out.println("sub: " + str);
            int i = 0;
            for (EssentialWord ew : worldList) {
                List<String> sub = split(ew.getDisplay(), ",");
                for (String _sub : sub) {
                    if (_sub.toLowerCase().trim().equals(str.toLowerCase().trim())) {
                        sb.append(ew.getName());
                        System.out.println(ew.getName() + "-_-");
                        sb.append(" ");
                        i++;
                    }
                }
            }
            if (i == 0) {
                sb.append(str);
                sb.append(" ");
            }
        }
        return sb.toString().trim();

    }
/*
    public static void main(String[] args) {
        ATMLocationDAO atmDAO = new ATMLocationDAO();
        AreaDAO areaDAO = new AreaDAO();
        EssentialWordDAO wordDAO = new EssentialWordDAO();

        List<AtmLocation> atmList = atmDAO.findByFullAddressAndDistrictNotNull();
        List<Area> areaList = areaDAO.listAll();
        List<EssentialWord> rejectWord = wordDAO.findByType('5');
        List<EssentialWord> determineWord = wordDAO.findByType('1');
        List<EssentialWord> wordList = wordDAO.listAll();
        List<EssentialWord> deleteWord = wordDAO.findByType('7');

        for (AtmLocation atm : atmList) {
            String s = atm.getFulladdress();
            StringBuilder exactAddress = new StringBuilder();
            s = oneSpace(s);
            List<String> splitStr = split(s, "\\,|\\-|\\(|\\)|\\.|\\;");
            List<String> exactAddressList = new ArrayList<>();
            for (int i = splitStr.size() - 1; i >= 0; i--) {
                String element = splitStr.get(i).toLowerCase().trim();
                if (!isNotAddress(element, rejectWord)) {

                    if (check(areaList, '1', element)[0].equals("1")) {
                        atm.setProvinceCity(check(areaList, '1', element)[1]);

                    }
                    if (check(areaList, '2', element)[0].equals("2")) {

                        atm.setDistrict(check(areaList, '2', element)[1]);

                    }

                    if (check(areaList, '3', element)[0].equals("3")) {

                        String precinct = check(areaList, '3', element)[1];

                        if (checkPrecinct(precinct, atm.getDistrict(), atm.getProvinceCity(), areaList)) {
                            atm.setPrecinct(precinct);

                        }

                    }

                    if (isExactAddress(element) && !isDetermineLocation(element, determineWord)) {
//                    exactAddress.append(element + ",");
                        exactAddressList.add(element);

                    } else if (isDetermineLocation(element, determineWord)) {
                        atm.setDetermineLocation(element);
                    }
                }
            }
            for (int i = exactAddressList.size() - 1; i >= 0; i--) {
                exactAddress.append(exactAddressList.get(i));
                if (i > 0) {
                    exactAddress.append(",");
                }
            }
            atm.setStreet(exactAddress.toString());
            if (exactAddressList.size() == 1) {
                atm.setStandardlization('1');
            } else {
                atm.setStandardlization('2');
            }
            if (atm.getPrecinct() != null && atm.getPrecinct().equals(atm.getDistrict())) {
                atm.setStandardlization('2');
            }
        }
        for (AtmLocation atm: atmList) {
            if (atm.getStreet() != null && !atm.getStreet().isEmpty()) {
                atm.setStreet(cutString(atm.getStreet(), deleteWord));
            }
        }

        atmDAO.updateAll(atmList);
        for (AtmLocation atm : atmList) {
            atm.print();
        }

        List<String> strL = split("34 Tô Hiệu. P. Nguyễn Trãi", "\\,|\\-|\\(|\\)|\\.|\\;");
        for (int i = strL.size() - 1; i >= 0; i--) {
                System.out.println(strL.get(i));
        }
//        List<AtmLocation> atmList2 = atmDAO.findByFullAddressNotnull();
//        for (AtmLocation atm : atmList2) {
//            String s = atm.getFulladdress();
//            StringBuilder exactAddress = new StringBuilder();
//            s = oneSpace(s);
//            List<String> splitStr = split(s, "\\,|\\-|\\(|\\)|\\|\\.|/");
//            List<String> exactAddressList = new ArrayList<>();
//            for (int i = splitStr.size() - 1; i >= 0; i--) {
//                String element = splitStr.get(i).toLowerCase().trim();
//                if (check(areaList, '1', element)[0].equals("1")) {
//                    atm.setProvinceCity(check(areaList, '1', element)[1]);
//                }
//                if (check(areaList, '2', element)[0].equals("2")) {
//                    atm.setDistrict(check(areaList, '2', element)[1]);
//                }
//                if (check(areaList, '3', element)[0].equals("3")) {
//                    atm.setPrecinct(check(areaList, '3', element)[1]);
//                }
//
//                if (isExactAddress(element) && !isNotAddress(element, rejectWord) && !isDetermineLocation(element, determineWord)) {
//                    exactAddressList.add(element);
//                } else if (isDetermineLocation(element, determineWord)) {
//                    atm.setDetermineLocation(element);
//                }
//            }
//            for (int i = exactAddressList.size() - 1; i >= 0; i--) {
//                exactAddress.append(exactAddressList.get(i));
//                if (i > 0) {
//                    exactAddress.append(",");
//                }
//            }
//            atm.setStreet(exactAddress.toString());
//            atm.setStandardlization('1');
//        }
//        atmDAO.updateAll(atmList2);
//    
//    
//        List<AtmLocation> atmList3 = atmDAO.findByFullAddressNull();
//        for (AtmLocation atm : atmList3) {
//            String s = atm.getStreet();
//            StringBuilder exactAddress = new StringBuilder();
//            s = oneSpace(s);
//            List<String> splitStr = split(s, "\\,|\\-|\\(|\\)|\\|\\.|/");
//            List<String> exactAddressList = new ArrayList<>();
//            for (int i = splitStr.size() - 1; i >= 0; i--) {
//                String element = splitStr.get(i).toLowerCase().trim();
//                if (check(areaList, '1', element)[0].equals("1")) {
//                    atm.setProvinceCity(check(areaList, '1', element)[1]);
//                }
//                if (check(areaList, '2', element)[0].equals("2")) {
//                    atm.setDistrict(check(areaList, '2', element)[1]);
//                }
//                if (check(areaList, '3', element)[0].equals("3")) {
//                    atm.setPrecinct(check(areaList, '3', element)[1]);
//                }
//
//                if (isExactAddress(element) && !isNotAddress(element, rejectWord) && !isDetermineLocation(element, determineWord)) {
//                    exactAddressList.add(element);
//                } else if (isDetermineLocation(element, determineWord)) {
//                    atm.setDetermineLocation(element);
//                }
//            }
//            for (int i = exactAddressList.size() - 1; i >= 0; i--) {
//                exactAddress.append(exactAddressList.get(i));
//                if (i > 0) {
//                    exactAddress.append(",");
//                }
//            }
//            atm.setStreet(exactAddress.toString());
//            atm.setStandardlization('1');
//        }
//        for (AtmLocation atm:atmList3)
//            atm.print();
//        atmDAO.updateAll(atmList3);
    }
    */
}
