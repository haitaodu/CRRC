package com.smartflow.crrc;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;


@Configuration
@EnableTransactionManagement
public class HibernateConfiguration {

    @Autowired
    private JpaProperties jpaProperties;

    @Value("${spring.datasource.driver-class-name}")
    private String DRIVER;

    @Value("${spring.datasource.url}")
    private String URL;

    @Value("${spring.datasource.username}")
    private String USERNAME;

    @Value("${spring.datasource.password}")
    private String PASSWORD;

    @Value("${spring.jpa.hibernate.dialect}")
    private String DIALECT;

    @Value("${spring.jpa.show-sql}")
    private String SHOW_SQL;

//   @Value("${spring.jpa.hibernate.hbm2ddl.auto}")
//    private String HBM2DDL_AUTO;

    @Value("${spring.entitymanager.packagesToScan}")
    private String PACKAGES_TO_SCAN;

    /* @Autowired
     private EntityManagerFactory entityManagerFactory;*/
    @Value("${spring.hibernate.current_session_context_class}")
    private String CURRENT_SESSION_CONTEXT_CLASS;
    /*@Value("${spring.hibernate.cache.use_second_level_cache}")
    private String USE_SECOND_LEVEL_CACHE;
    @Value("${spring.hibernate.cache.region.factory_class}")
    private String FACTORY_CLASS;
    @Value("${spring.hibernate.cache.use_query_cache}")
    private String USE_QUERY_CACHE;*/

    @Bean
    public DataSource dataSource() throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(DRIVER);
        dataSource.setJdbcUrl(URL);
        dataSource.setUser(USERNAME);
        dataSource.setPassword(PASSWORD);
        //最大空闲时间，60秒内未使用则连接被丢弃。若为0则永不丢弃。默认值: 0
        dataSource.setMaxIdleTime(60);
        dataSource.setMinPoolSize(10);
        dataSource.setMaxPoolSize(50);
        dataSource.setInitialPoolSize(10);
        dataSource.setIdleConnectionTestPeriod(100);
        dataSource.setAcquireIncrement(10);
        dataSource.setAcquireRetryAttempts(30);
        dataSource.setAutoCommitOnClose(true);
        //dataSource.setPreferredTestQuery("select * from receipttype");
        dataSource.setAcquireRetryDelay(100);
        dataSource.setBreakAfterAcquireFailure(false);
        return dataSource;
    }

   /* @Bean(name = "entityManagerFactory")
    public EntityManagerFactory  entityManagerFactory() throws PropertyVetoException{
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setGenerateDdl(true); //hibernate基本配置
        jpaVendorAdapter.setDatabase(Database.SQL_SERVER);
        jpaVendorAdapter.setShowSql(true);
        LocalContainerEntityManagerFactoryBean  entityManagerFactory = new LocalContainerEntityManagerFactoryBean ();
        entityManagerFactory.setDataSource(dataSource());
        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter);
        entityManagerFactory.setPackagesToScan(PACKAGES_TO_SCAN);
        return entityManagerFactory.getObject();
    }*/

    @Bean(value = "sessionFactory")
    public LocalSessionFactoryBean sessionFactory() throws PropertyVetoException {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(PACKAGES_TO_SCAN);
        Properties hibernateProperties = new Properties();
        hibernateProperties.put("hibernate.dialect", DIALECT);
        hibernateProperties.put("hibernate.show_sql", SHOW_SQL);
//        hibernateProperties.put("hibernate.hbm2ddl.auto", HBM2DDL_AUTO);
        hibernateProperties.setProperty("hibernate.current_session_context_class", CURRENT_SESSION_CONTEXT_CLASS);
/*        hibernateProperties.setProperty("hibernate.cache.use_second_level_cache", USE_SECOND_LEVEL_CACHE);
        hibernateProperties.setProperty("hibernate.cache.region.factory_class", FACTORY_CLASS);
        hibernateProperties.setProperty("hibernate.cache.use_query_cache", USE_QUERY_CACHE);*/
        sessionFactory.setHibernateProperties(hibernateProperties);
        return sessionFactory;
    }

    /*@Bean
    public SessionFactory getSessionFactory() throws PropertyVetoException{
        if(entityManagerFactory.unwrap(SessionFactory.class) == null){
            throw new NullPointerException("factory is not a hibernate factory");
        }
        return entityManagerFactory.unwrap(SessionFactory.class);
    }*/

   /* @Configuration
    public class HibernateConfig {
        @Bean
        public SessionFactory sessionFactory(@Qualifier("entityManagerFactory") EntityManagerFactory emf){
            return emf.unwrap(SessionFactory.class);
        }
    }*/


}
