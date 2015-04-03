/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kltn.test;

import java.util.List;
import kltn.dao.TestDAO;
import kltn.entity.TestGeo;

/**
 *
 * @author Vu
 */
public class Test {

    public static void main(String[] args) {
//        EssentialWordDAO dao = new EssentialWordDAO();
//        List<EssentialWord> list = dao.findByType('5');
//        System.out.println(list.size());
        TestDAO test = new TestDAO();
        List<TestGeo> l = test.listAll();
        System.out.println(l.get(0).getGeo().toText());
    }
}
