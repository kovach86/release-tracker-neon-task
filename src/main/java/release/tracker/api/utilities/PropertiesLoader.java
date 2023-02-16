package release.tracker.api.utilities;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hibernate.cfg.Environment;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesLoader {

    public static Map<String, Object> getDbConfiguration() throws IOException {
        InputStream inputStream = TypeReference.class.getResourceAsStream("/application.properties");
        Properties appProperties = new Properties();
        appProperties.load(inputStream);

        Map<String, Object> dbSettings = new HashMap<>();
        dbSettings.put(Environment.URL, appProperties.getProperty("spring.datasource.url"));
        dbSettings.put(Environment.USER, appProperties.getProperty("spring.datasource.username"));
        dbSettings.put(Environment.PASS, appProperties.getProperty("spring.datasource.password"));
        dbSettings.put(Environment.DRIVER, appProperties.getProperty("spring.datasource.driverClassName"));
        dbSettings.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
        dbSettings.put(Environment.SHOW_SQL, "true");
        dbSettings.put(Environment.FORMAT_SQL, "true");
        dbSettings.put(Environment.HBM2DDL_AUTO, appProperties.getProperty("spring.jpa.hibernate.ddl-auto"));

        return dbSettings;
    }
}
