package com.example.distributedtranscation.config;

import com.alibaba.druid.pool.xa.DruidXADataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.mysql.cj.jdbc.MysqlXADataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;
import tk.mybatis.spring.annotation.MapperScan;
import tk.mybatis.spring.annotation.MapperScannerRegistrar;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import javax.sql.DataSource;
import javax.transaction.UserTransaction;
import java.util.Properties;

/**
 * @author jianganwei
 * @program study-v2
 * @description
 * @date 2019-04-12 15:02
 **/
@EnableTransactionManagement(proxyTargetClass = true)
@Configuration
@Slf4j
@MapperScan(basePackages = "com.example.distributedtranscation.db2mapper",sqlSessionFactoryRef = "SqlSessionFactory2")
public class DataSourceAndSqlSessionConfig {
    @Bean("db1")
    public DataSource dataSourceOne(DatabaseProperties.DatabaseOneProperties databaseOneProperties) throws Exception {
        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();

        MysqlXADataSource mysqlXADataSource = new MysqlXADataSource();
        mysqlXADataSource.setUrl(databaseOneProperties.getUrl());
        mysqlXADataSource.setPinGlobalTxToPhysicalConnection(true);
        mysqlXADataSource.setUser(databaseOneProperties.getUsername());
        mysqlXADataSource.setPassword(databaseOneProperties.getPassword());

        atomikosDataSourceBean.setXaDataSource(mysqlXADataSource);
        atomikosDataSourceBean.setUniqueResourceName("one");
        atomikosDataSourceBean.setMinPoolSize(databaseOneProperties.getMinPoolSize());
        atomikosDataSourceBean.setMaxPoolSize(databaseOneProperties.getMaxPoolSize());
        atomikosDataSourceBean.setBorrowConnectionTimeout(databaseOneProperties.getBorrowConnectionTimeout());
        atomikosDataSourceBean.setLoginTimeout(databaseOneProperties.getLoginTimeout());
        atomikosDataSourceBean.setMaintenanceInterval(databaseOneProperties.getMaintenanceInterval());
        atomikosDataSourceBean.setMaxIdleTime(databaseOneProperties.getMaxIdleTime());
        atomikosDataSourceBean.setTestQuery(databaseOneProperties.getTestQuery());

        return atomikosDataSourceBean;
    }
    @Bean(name = "db2")
    public DataSource dataSourceTwo(DatabaseProperties.DatabaseTwoProperties databaseTwoProperties) throws Exception {
        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();

        MysqlXADataSource mysqlXADataSource = new MysqlXADataSource();
        mysqlXADataSource.setUrl(databaseTwoProperties.getUrl());
        mysqlXADataSource.setPinGlobalTxToPhysicalConnection(true);
        mysqlXADataSource.setUser(databaseTwoProperties.getUsername());
        mysqlXADataSource.setPassword(databaseTwoProperties.getPassword());

        atomikosDataSourceBean.setXaDataSource(mysqlXADataSource);
        atomikosDataSourceBean.setUniqueResourceName("two");
        atomikosDataSourceBean.setMinPoolSize(databaseTwoProperties.getMinPoolSize());
        atomikosDataSourceBean.setMaxPoolSize(databaseTwoProperties.getMaxPoolSize());
        atomikosDataSourceBean.setBorrowConnectionTimeout(databaseTwoProperties.getBorrowConnectionTimeout());
        atomikosDataSourceBean.setLoginTimeout(databaseTwoProperties.getLoginTimeout());
        atomikosDataSourceBean.setMaintenanceInterval(databaseTwoProperties.getMaintenanceInterval());
        atomikosDataSourceBean.setMaxIdleTime(databaseTwoProperties.getMaxIdleTime());
        atomikosDataSourceBean.setTestQuery(databaseTwoProperties.getTestQuery());
        return atomikosDataSourceBean;

    }


    @Bean("db1Manager")
    public DataSourceTransactionManager db1Manager(@Qualifier("db1") DataSource db) {
        return new DataSourceTransactionManager(db);
    }

    @Bean("db2Manager")
    public DataSourceTransactionManager db2Manager(@Qualifier("db2") DataSource db) {
        return new DataSourceTransactionManager(db);
    }


    @Bean("userTransaction")
    public UserTransaction userTransaction() throws Exception {
        UserTransactionImp userTransactionImp = new UserTransactionImp();
        userTransactionImp.setTransactionTimeout(1000);
        return userTransactionImp;
    }

    @Bean(value = "userTransactionManager", initMethod = "init", destroyMethod = "close")
    public UserTransactionManager userTransactionManager() throws Exception {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setForceShutdown(false);
        return userTransactionManager;
    }

    @Bean("jtaManager")
    public JtaTransactionManager jtaTransactionManager(@Autowired UserTransactionManager userTransactionManager,
                                                       @Autowired UserTransaction userTransaction) {
        return new JtaTransactionManager(userTransaction, userTransactionManager);
    }

    @Bean("SqlSessionFactory1")
    public SqlSessionFactory db1Sql(@Qualifier("db1") DataSource db) throws Exception {
        return createSqlSession(db, "classpath:/db1xml/*.xml");
    }

    @Bean("SqlSessionFactory2")
    public SqlSessionFactory db2Sql(@Qualifier("db2") DataSource db) throws Exception {
        return createSqlSession(db, "classpath:/db2xml/*.xml");

    }

    @Bean
    public SqlSessionTemplate createSqlTemplate1(@Qualifier("SqlSessionFactory1") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public SqlSessionTemplate createSqlTemplate2(@Qualifier("SqlSessionFactory2") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    private static SqlSessionFactory createSqlSession(DataSource db, String path) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(db);
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        factoryBean.setMapperLocations(resolver.getResources(path));
        factoryBean.setConfiguration(configuration);
        return factoryBean.getObject();
    }
}
