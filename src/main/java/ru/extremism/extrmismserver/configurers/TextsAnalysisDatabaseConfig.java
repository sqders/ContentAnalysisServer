package ru.extremism.extrmismserver.configurers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.impossibl.postgres.api.jdbc.PGConnection;
import com.impossibl.postgres.jdbc.PGDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.integration.jdbc.channel.PostgresSubscribableChannel;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import ru.extremism.extrmismserver.listener.SqlCodePart;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;
import java.util.function.Supplier;

@EnableJpaRepositories(
        entityManagerFactoryRef = TextsAnalysisDatabaseConfig.ENTITY_MANAGER_FACTORY,
        transactionManagerRef = TextsAnalysisDatabaseConfig.TRANSACTION_MANAGER,
        basePackages = TextsAnalysisDatabaseConfig.JPA_REPOSITORY_PACKAGE
)
@Configuration
public class TextsAnalysisDatabaseConfig {

    public static final String PROPERTY_PREFIX = "app.db.text.datasource";
    public static final String JPA_REPOSITORY_PACKAGE = "ru.extremism.extrmismserver.repository.text";
    public static final String ENTITY_PACKAGE = "ru.extremism.extrmismserver.model.text";
    public static final String ENTITY_MANAGER_FACTORY = "textEntityManagerFactory";
    public static final String DATA_SOURCE = "textDataSource";
    public static final String DATABASE_PROPERTY = "textDatabaseProperty";
    public static final String TRANSACTION_MANAGER = "textTransactionManager";

    @Bean(DATABASE_PROPERTY)
    @ConfigurationProperties(prefix = PROPERTY_PREFIX)
    public DatabaseProperty appDatabaseProperty() {
        return new DatabaseProperty();
    }

    @Bean(DATA_SOURCE)
    public DataSource appDataSource(
            @Qualifier(DATABASE_PROPERTY) DatabaseProperty databaseProperty
    ) {
        return DataSourceBuilder
                .create()
                .username(databaseProperty.getUsername())
                .password(databaseProperty.getPassword())
                .url(databaseProperty.getUrl())
                .driverClassName(databaseProperty.getClassDriver())
                .build();
    }

    @Bean(name = ENTITY_MANAGER_FACTORY)
    public LocalContainerEntityManagerFactoryBean appEntityManager(
            @Qualifier(DATA_SOURCE) DataSource dataSource
    ) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPersistenceUnitName(ENTITY_MANAGER_FACTORY);
        em.setPackagesToScan(ENTITY_PACKAGE);
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());


        //HashMap<String, Object> properties = new HashMap<>();
        Properties properties = new Properties();
        properties.setProperty("jakarta.persistence.validation.mode", "none");
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        em.setJpaProperties(properties);
        return em;
    }

    @Bean(TRANSACTION_MANAGER)
    public PlatformTransactionManager sqlSessionTemplate(
            @Qualifier(ENTITY_MANAGER_FACTORY) LocalContainerEntityManagerFactoryBean entityManager,
            @Qualifier(DATA_SOURCE) DataSource dataSource
    ) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManager.getObject());
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }

    @Bean
    public PGConnection pgConnection(@Qualifier(DATABASE_PROPERTY) DatabaseProperty databaseProperty,
    @Value("${app.db.notification.host}")String host,
    @Value("${app.db.notification.port}")int port,
    @Value("${app.db.notification.name}") String dbName) throws SQLException {
        PGDataSource dataSource=  new PGDataSource();
        dataSource.setHost(host);
        dataSource.setPort(port);
        dataSource.setDatabaseName(dbName);
        dataSource.setUser(databaseProperty.getUsername());
        dataSource.setPassword(databaseProperty.getPassword());
        return (PGConnection) dataSource.getConnection();
    }


}
