/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kltn.test;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.io.ParseException;
import java.util.List;
import kltn.dao.TestDAO;
import kltn.entity.TestGeo;
import com.vividsolutions.jts.io.WKTReader;

/**
 *
 * @author Vu
 */
public class Test {

//    public static void main(String[] args) {
////        EssentialWordDAO dao = new EssentialWordDAO();
////        List<EssentialWord> list = dao.findByType('5');
////        System.out.println(list.size());
//        TestDAO test = new TestDAO();
//        List<TestGeo> l = test.listAll();
//        System.out.println(l.get(0).getGeo().toText());
//        Geometry geo = wktToGeometry("POINT(21.0329942 105.8084637)");
//        Geometry geo2 = wktToGeometry("POLYGON((20.9950991 105.8764459,21.0502942 105.8764459,21.0502942 105.7974815,20.9950991 105.7974815, 20.9950991 105.8764459))");
//        System.out.println(geo.within(geo2));
//    }
//    private static Geometry wktToGeometry(String wktPoint) {
//        WKTReader fromText = new WKTReader();
//        Geometry geom = null;
//        try {
//            geom = fromText.read(wktPoint);
//        } catch (ParseException e) {
//            throw new RuntimeException("Not a WKT string:" + wktPoint);
//        }
//        return geom;
//    }
}
