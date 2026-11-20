package co.edu.sena.adso.orm.dao;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Crea una sola SessionFactory para toda la aplicación, porque es un objeto pesado:
 * lee hibernate.cfg.xml, valida el mapeo contra la base de datos y prepara el pool de conexiones.
 */
public final class HibernateUtil {

    private static final SessionFactory FACTORY = new Configuration()
            .configure("hibernate.cfg.xml")
            .buildSessionFactory();

    private HibernateUtil() {
    }

    public static SessionFactory getSessionFactory() {
        return FACTORY;
    }

    public static void cerrar() {
        FACTORY.close();
    }
}
