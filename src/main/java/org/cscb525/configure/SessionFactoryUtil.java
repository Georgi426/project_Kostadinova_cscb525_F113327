package org.cscb525.configure;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class SessionFactoryUtil {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration configuration = new Configuration();

            configuration.addAnnotatedClass(org.cscb525.entity.Company.class);
            configuration.addAnnotatedClass(org.cscb525.entity.Client.class);
            configuration.addAnnotatedClass(org.cscb525.entity.Employee.class);
            configuration.addAnnotatedClass(org.cscb525.entity.Vehicle.class);
            configuration.addAnnotatedClass(org.cscb525.entity.Transport.class);
            configuration.addAnnotatedClass(org.cscb525.entity.Payment.class);

            ServiceRegistry serviceRegistry
                    = new StandardServiceRegistryBuilder()
                            .applySettings(configuration.getProperties()).build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        }
        return sessionFactory;
    }
}
