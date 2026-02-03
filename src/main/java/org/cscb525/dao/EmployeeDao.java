package org.cscb525.dao;

import java.util.List;

import org.cscb525.configure.SessionFactoryUtil;
import org.cscb525.entity.Employee;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class EmployeeDao {

    // ======= create =======
    public static void save(Employee employee) {
        Transaction tx = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(employee);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    // ======= update =======
    public static void updateEmployee(Employee employee) {
        Transaction tx = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(employee);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    // ======= delete =======
    public static void deleteEmployee(Employee employee) {
        Transaction tx = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.delete(employee);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    // ======= read =======
    public static List<Employee> getAll() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("select distinct e from Employee e join fetch e.company", Employee.class).list();
        }
    }

    // ======= read/stats =======
    public static List<org.cscb525.dto.DriverIncomeDto> getDriverRevenue() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "SELECT new org.cscb525.dto.DriverIncomeDto(e.name, SUM(t.price)) " +
                "FROM Transport t JOIN t.driver e " +
                "GROUP BY e.name " +
                "ORDER BY SUM(t.price) DESC", 
                org.cscb525.dto.DriverIncomeDto.class).list();
        }
    }

    // ======= read/stats =======
    public static List<org.cscb525.dto.DriverTransportCountDto> getDriverTransportCounts() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "SELECT new org.cscb525.dto.DriverTransportCountDto(e.name, COUNT(t.id)) " +
                "FROM Transport t JOIN t.driver e " +
                "GROUP BY e.name " +
                "ORDER BY COUNT(t.id) DESC", 
                org.cscb525.dto.DriverTransportCountDto.class).list();
        }
    }
}
