/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kltn.dao;

import java.util.List;

import kltn.hibernate.HibernateUtil;
import kltn.entity.Area;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Vu
 */
public class AreaDAO {
   
    public void AreaDAO(){
        
    }
    public List<Area> listAll(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Area> list = null;
        Transaction tx = null;
        try {

            tx = session.beginTransaction();
            Criteria cr = session.createCriteria(Area.class);
            list = cr.list();
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        }
        return list;
    }
}
