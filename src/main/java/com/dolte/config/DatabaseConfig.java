package com.dolte.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableConfigurationProperties(DatabaseProperties.class)
@EnableTransactionManagement
@MapperScan(basePackages = { "com.dolte.**.dao" })
public class DatabaseConfig {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	DatabaseProperties properties;

	@Bean
	public DataSource dataSource() {
		org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();

		dataSource.setDriverClassName(properties.getDriverClassName());
		dataSource.setUrl(properties.getUrl());
		dataSource.setUsername(properties.getUsername());
		dataSource.setPassword(properties.getPassword());
		dataSource.setTestOnBorrow(properties.isTestOnBorrow());
		dataSource.setTestWhileIdle(properties.isTestWhileIdle());
		dataSource.setValidationQuery(properties.getValidationQuery());
		dataSource.setValidationInterval(properties.getValidationInterval());
		dataSource.setTimeBetweenEvictionRunsMillis(properties.getTimeBetweenEvictionRunsMillis());

		return dataSource;
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource());
		transactionManager.setGlobalRollbackOnParticipationFailure(false);
		return transactionManager;
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		sessionFactoryBean.setDataSource(dataSource());
		sessionFactoryBean.setTypeAliasesPackage("hanwha.scu.com.entity");
		sessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:META-INF/mybatis/mybatis-config.xml"));
		sessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:META-INF/mybatis/mapper/**/*.xml"));
		return sessionFactoryBean.getObject();
	}
}
