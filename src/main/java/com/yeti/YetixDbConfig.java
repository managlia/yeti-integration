package com.yeti;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
  entityManagerFactoryRef = "yetiEntityManagerFactory",
  transactionManagerRef = "yetiTransactionManager",
  basePackages = { "com.yetix.core.repository" }
)
public class YetixDbConfig {
	
  @Bean(name = "yetiDataSource")
  @ConfigurationProperties(prefix = "yetix.datasource")
  public DataSource yetiDataSource() {
    return DataSourceBuilder.create().build();
  }
  
  @Bean(name = "yetiEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean yetiEntityManagerFactory
  						(EntityManagerFactoryBuilder builder, @Qualifier("yetiDataSource") DataSource yetiDataSource) {

	  Map<String, Object> vendorProperties = new HashMap<String, Object>();
      vendorProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
      vendorProperties.put("hibernate.show_sql", "true");
      
	  return
      builder
        .dataSource(yetiDataSource)
        .packages("com.yetix.model")
        .properties(vendorProperties)
        .persistenceUnit("yeti")
        .build();
  }
  

  @Bean(name = "yetiTransactionManager")
  public PlatformTransactionManager yetiTransactionManager
  						(@Qualifier("yetiEntityManagerFactory") EntityManagerFactory yetiEntityManagerFactory) {
    return new JpaTransactionManager(yetiEntityManagerFactory);
  }
}