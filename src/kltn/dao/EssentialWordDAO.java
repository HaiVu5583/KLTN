/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kltn.dao;

import java.util.List;
import kltn.entity.Area;
import kltn.entity.EssentialWord;
import kltn.hibernate.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Vu
 */
public class EssentialWordDAO {

    public List<EssentialWord> listAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<EssentialWord> list = null;
        Transaction tx = null;
        try {

            tx = session.beginTransaction();
            Criteria cr = session.createCriteria(EssentialWord.class);
            list = cr.list();
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        }
        return list;
    }

    public List<EssentialWord> findByType(char c) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<EssentialWord> list = null;
        Transaction tx = null;
        try {

            tx = session.beginTransaction();
            Criteria cr = session.createCriteria(EssentialWord.class);
            cr.add(Restrictions.eq("type", c));
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
