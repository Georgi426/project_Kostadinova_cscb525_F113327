package org.cscb525.dao;

import java.util.List;

import org.cscb525.configure.SessionFactoryUtil;
import org.cscb525.entity.Transport;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class TransportDao {

    // ======= create =======
    public static void save(Transport transport) {
        Transaction tx = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(transport);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    // ======= read =======
    public static List<Transport> getAll() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Transport t join fetch t.company join fetch t.driver join fetch t.vehicle join fetch t.client", Transport.class).list();
        }
    }

    // ======= read/stats =======
    public static java.math.BigDecimal getIncomeForPeriod(java.time.LocalDate from, java.time.LocalDate to) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "SELECT COALESCE(SUM(t.price), 0) FROM Transport t " +
                "WHERE (cast(:from as date) is null OR t.departureDateTime >= :from) " +
                "AND (cast(:to as date) is null OR t.departureDateTime <= :to)", 
                java.math.BigDecimal.class)
                .setParameter("from", from != null ? from.atStartOfDay() : null)
                .setParameter("to", to != null ? to.atTime(23, 59, 59) : null)
                .getSingleResult();
        }
    }

    // ======= validation =======
    public static long countDriverOverlaps(Long driverId, java.time.LocalDateTime start, java.time.LocalDateTime end) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "SELECT count(t) FROM Transport t " +
                "WHERE t.driver.id = :driverId " +
                "AND t.departureDateTime < :end " +
                "AND t.arrivalDateTime > :start", 
                Long.class)
                .setParameter("driverId", driverId)
                .setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult();
        }
    }

    // ======= validation =======
    public static long countVehicleOverlaps(Long vehicleId, java.time.LocalDateTime start, java.time.LocalDateTime end) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "SELECT count(t) FROM Transport t " +
                "WHERE t.vehicle.id = :vehicleId " +
                "AND t.departureDateTime < :end " +
                "AND t.arrivalDateTime > :start", 
                Long.class)
                .setParameter("vehicleId", vehicleId)
                .setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult();
        }
    }

}
