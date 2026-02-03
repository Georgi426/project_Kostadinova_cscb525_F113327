package org.cscb525.dao;

import java.util.List;

import org.cscb525.configure.SessionFactoryUtil;
import org.cscb525.entity.Client;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ClientDao {

    // ======= create =======
    public static void save(Client client) {
        Transaction tx = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(client);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    // ======= read =======
    public static Client getById(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.get(Client.class, id);
        }
    }

    // ======= read =======
    public static List<Client> getAll() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Client", Client.class).list();
        }
    }
    // ======= update =======
    public static void updateClient(Client client) {
        Transaction tx = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(client);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    // ======= delete =======
    public static void deleteClient(Client client) {
        Transaction tx = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.delete(client);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }
}
