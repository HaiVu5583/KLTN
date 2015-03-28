/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kltn.dao;

import java.util.List;
import kltn.entity.AtmLocation;
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
public class ATMLocationDAO {

    public ATMLocationDAO() {

    }

    public List<AtmLocation> listAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<AtmLocation> list = null;
        Transaction tx = null;
        try {

            tx = session.beginTransaction();
            Criteria cr = session.createCriteria(AtmLocation.class);
            list = cr.list();
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
        return list;
    }

    public List<AtmLocation> findByFullAddressNotNull() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<AtmLocation> list = null;
        Transaction tx = null;
        try {

            tx = session.beginTransaction();
            Criteria cr = session.createCriteria(AtmLocation.class);
            cr.add(Restrictions.isNotNull("fulladdress"));
//            cr.add(Restrictions.isNotEmpty("fulladdress"));
            list = cr.list();
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
        return list;
    }

    public List<AtmLocation> findByFullAddressAndDistrictNotNull() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<AtmLocation> list = null;
        Transaction tx = null;
        try {

            tx = session.beginTransaction();
            Criteria cr = session.createCriteria(AtmLocation.class);
            cr.add(Restrictions.isNotNull("fulladdress"));
            cr.add(Restrictions.isNotNull("district"));
            list = cr.list();
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
        return list;
    }

    public void insert(AtmLocation atm) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(atm);
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }

    public void insertAll(List<AtmLocation> atmList) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            for (AtmLocation atm : atmList) {
                session.save(atm);
            }
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }

    public void update(AtmLocation atm) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(atm);
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }

    public void updateAll(List<AtmLocation> atmList) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            for (AtmLocation atm : atmList) {
                session.update(atm);
            }
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }

    public void delete(AtmLocation atm) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(atm);
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }
}
