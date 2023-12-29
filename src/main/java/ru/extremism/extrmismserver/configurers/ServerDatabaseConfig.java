package ru.extremism.extrmismserver.configurers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Properties;

@EnableJpaRepositories(
        entityManagerFactoryRef = ServerDatabaseConfig.ENTITY_MANAGER_FACTORY,
        transactionManagerRef = ServerDatabaseConfig.TRANSACTION_MANAGER,
        basePackages = ServerDatabaseConfig.JPA_REPOSITORY_PACKAGE
)
@Configuration
public class ServerDatabaseConfig {

    public static final String PROPERTY_PREFIX = "app.db.server.datasource";
    public static final String JPA_REPOSITORY_PACKAGE = "ru.extremism.extrmismserver.repository.server";
    public static final String ENTITY_PACKAGE = "ru.extremism.extrmismserver.model.server";
    public static final String ENTITY_MANAGER_FACTORY = "serverEntityManagerFactory";
    public static final String DATA_SOURCE = "serverDataSource";
    public static final String DATABASE_PROPERTY = "serverDatabaseProperty";
    public static final String TRANSACTION_MANAGER = "serverTransactionManager";

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

        Properties properties = new Properties();
        properties.setProperty("spring.jpa.show-sql","true");
        properties.setProperty("spring.jpa.generate-ddl","false");
        properties.setProperty("spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation","true");
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

}