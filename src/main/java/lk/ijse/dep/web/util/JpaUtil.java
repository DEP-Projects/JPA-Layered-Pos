package lk.ijse.dep.web.util;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.cfg.AvailableSettings;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author: Yohan Ishara <yohanishara01@gmail.com>
 * @since : 2021-01-30
 **/
public class JpaUtil {
    private static EntityManagerFactory factory;

    public JpaUtil() {
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        Map<String, Object> properties = new HashMap();
        properties.put(AvailableSettings.DATASOURCE, getDataSource());
        return factory == null ? (factory = Persistence.createEntityManagerFactory("dep-6", properties)) : factory;
    }

    private static BasicDataSource getDataSource() {
        try {
            Properties prop = new Properties();
            prop.load(JpaUtil.class.getResourceAsStream("/application.properties"));
            BasicDataSource bds = new BasicDataSource();
            bds.setInitialSize(5);
            bds.setMaxTotal(10);
            bds.setUrl(prop.getProperty("dbcp.connection.url"));
            bds.setDriverClassName(prop.getProperty("dbcp.connection.driver_class"));
            bds.setUsername(prop.getProperty("dbcp.connection.username"));
            bds.setPassword(prop.getProperty("dbcp.connection.password"));
            return bds;
        } catch (IOException var2) {
            throw new RuntimeException(var2);
        }
    }
}
