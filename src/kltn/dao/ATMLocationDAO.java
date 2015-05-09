/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kltn.dao;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kltn.entity.AtmLocation;
import kltn.hibernate.HibernateUtil;
import kltn.utils.Utils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
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

    public List<AtmLocation> findByFullAddressNull() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<AtmLocation> list = null;
        Transaction tx = null;
        try {

            tx = session.beginTransaction();
            Criteria cr = session.createCriteria(AtmLocation.class);
            cr.add(Restrictions.isNull("fulladdress"));
            cr.add(Restrictions.isNotNull("district"));
            cr.add(Restrictions.isNotNull("street"));
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

    public List<AtmLocation> findByFullAddressNotnull() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<AtmLocation> list = null;
        Transaction tx = null;
        try {

            tx = session.beginTransaction();
            Criteria cr = session.createCriteria(AtmLocation.class);
            cr.add(Restrictions.isNotNull("fulladdress"));
            cr.add(Restrictions.isNull("district"));
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

    public List<AtmLocation> findByStandardlizationStatus(char standardlization) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<AtmLocation> list = null;
        Transaction tx = null;
        try {

            tx = session.beginTransaction();
            if (standardlization != '0') {
                Criteria cr = session.createCriteria(AtmLocation.class);
                cr.add(Restrictions.eq("standardlization", standardlization));
                cr.add(Restrictions.ne("street", ""));
                list = cr.list();
            }else{
                Criteria cr = session.createCriteria(AtmLocation.class);
                cr.add(Restrictions.eqOrIsNull("standardlization", ""));
                list = cr.list();
            }
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
//    public List<Map.Entry<AtmLocation, Double>> find10NeareastATM(String lat1, String long1) {
//        Map<AtmLocation, Double> map = new HashMap();
//        Session session = HibernateUtil.getSessionFactory().openSession();
//        List<AtmLocation> list = null;
//        List<Map.Entry<AtmLocation, Double>> listDistance = new ArrayList<>();
//
//        Criteria cr = session.createCriteria(AtmLocation.class);
//        cr.add(Restrictions.isNotNull("latd"));
//        list = cr.list();
//        for (AtmLocation atm : list) {
//            listDistance.add(new AbstractMap.SimpleEntry<>(atm, Utils.distance(lat1, long1, atm.getLatd(), atm.getLongd())));
//        }
//        for (int i = 0; i < listDistance.size() - 1; i++) {
//            for (int j = i + 1; j < listDistance.size(); j++) {
//                if (listDistance.get(i).getValue() > listDistance.get(j).getValue()) {
//                    AtmLocation tempAtm = new AtmLocation();
//                    tempAtm.copy(listDistance.get(i).getKey());
//                    Double tempDis = listDistance.get(i).getValue();
//                    listDistance.get(i).getKey().copy(listDistance.get(j).getKey());
//                    listDistance.get(i).setValue(listDistance.get(j).getValue());
//                    listDistance.get(j).getKey().copy(tempAtm);
//                    listDistance.get(j).setValue(tempDis);
//                }
//            }
//        }
//        for (int i=0; i<20; i++) {
//            Map.Entry<AtmLocation, Double> entry = listDistance.get(i);
//            System.out.println(entry.getKey().getFulladdress());
//            System.out.println(entry.getKey().getLatd());
//            System.out.println(entry.getKey().getLongd());
//            System.out.println(entry.getValue());
//            System.out.println("-------------------------");
//        }
//        return null;
//
//    }
//
//    public void insert(AtmLocation atm) {
//        Session session = HibernateUtil.getSessionFactory().openSession();
//        Transaction tx = null;
//        try {
//            tx = session.beginTransaction();
//            session.save(atm);
//            tx.commit();
//        } catch (HibernateException he) {
//            if (tx != null && tx.isActive()) {
//                tx.rollback();
//            }
//        } finally {
//            session.close();
//        }
//    }
//
//    public void insertAll(List<AtmLocation> atmList) {
//        Session session = HibernateUtil.getSessionFactory().openSession();
//        Transaction tx = null;
//        try {
//            tx = session.beginTransaction();
//            for (AtmLocation atm : atmList) {
//                session.save(atm);
//            }
//            tx.commit();
//        } catch (HibernateException he) {
//            if (tx != null && tx.isActive()) {
//                tx.rollback();
//            }
//        } finally {
//            session.close();
//        }
//    }
//    public List<AtmLocation> find10NeareastATM(String lat1, String long1) {
//      
//        Session session = HibernateUtil.getSessionFactory().openSession();
//        List<AtmLocation> list = null;
//        
//
//        Criteria cr = session.createCriteria(AtmLocation.class);
//        cr.add(Restrictions.isNotNull("latd"));
//        list = cr.list();
//        for (AtmLocation atm : list) {
//            atm.setDistance();
//        }
//        for (int i = 0; i < listDistance.size() - 1; i++) {
//            for (int j = i + 1; j < listDistance.size(); j++) {
//                if (listDistance.get(i).getValue() > listDistance.get(j).getValue()) {
//                    AtmLocation tempAtm = new AtmLocation();
//                    tempAtm.copy(listDistance.get(i).getKey());
//                    Double tempDis = listDistance.get(i).getValue();
//                    listDistance.get(i).getKey().copy(listDistance.get(j).getKey());
//                    listDistance.get(i).setValue(listDistance.get(j).getValue());
//                    listDistance.get(j).getKey().copy(tempAtm);
//                    listDistance.get(j).setValue(tempDis);
//                }
//            }
//        }
//        for (int i=0; i<20; i++) {
//            Map.Entry<AtmLocation, Double> entry = listDistance.get(i);
//            System.out.println(entry.getKey().getFulladdress());
//            System.out.println(entry.getKey().getLatd());
//            System.out.println(entry.getKey().getLongd());
//            System.out.println(entry.getValue());
//            System.out.println("-------------------------");
//        }
//        return null;
//
//    }
//
//    public void insert(AtmLocation atm) {
//        Session session = HibernateUtil.getSessionFactory().openSession();
//        Transaction tx = null;
//        try {
//            tx = session.beginTransaction();
//            session.save(atm);
//            tx.commit();
//        } catch (HibernateException he) {
//            if (tx != null && tx.isActive()) {
//                tx.rollback();
//            }
//        } finally {
//            session.close();
//        }
//    }
//
//    public void insertAll(List<AtmLocation> atmList) {
//        Session session = HibernateUtil.getSessionFactory().openSession();
//        Transaction tx = null;
//        try {
//            tx = session.beginTransaction();
//            for (AtmLocation atm : atmList) {
//                session.save(atm);
//            }
//            tx.commit();
//        } catch (HibernateException he) {
//            if (tx != null && tx.isActive()) {
//                tx.rollback();
//            }
//        } finally {
//            session.close();
//        }
//    }

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
