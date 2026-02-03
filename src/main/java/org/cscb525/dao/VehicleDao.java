package org.cscb525.dao;

import org.cscb525.configure.SessionFactoryUtil;
import org.cscb525.entity.Vehicle;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class VehicleDao {

    // ======= create =======
    public static void saveVehicle(Vehicle vehicle) {
        Transaction tx = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(vehicle);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    // ======= delete =======
    public static void deleteVehicle(long id) {
        Transaction tx = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Vehicle vehicle = session.find(Vehicle.class, id);
            if (vehicle != null) {
                session.remove(vehicle);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    // ======= read =======
    public static Vehicle getById(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.get(Vehicle.class, id);
        }
    }
    // ======= update =======
    public static void updateVehicle(Vehicle vehicle) {
        Transaction tx = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(vehicle);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }
    // ======= read =======
    public static java.util.List<Vehicle> getAllVehicles() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Vehicle", Vehicle.class).list();
        }
    }
}
