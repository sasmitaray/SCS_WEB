package com.sales.crm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;

@Configuration
public class HikariDSConfig {
	
	
	@Bean
	public HikariConfig hikariConfig() {
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
		hikariConfig.setJdbcUrl("jdbc:mysql://"+ System.getProperty("RDS_HOSTNAME", "localhost") + ":3306/"+ System.getProperty("RDS_DB_NAME", "salescrm"));
		hikariConfig.setUsername(System.getProperty("RDS_USERNAME", "salescrm"));
		hikariConfig.setPassword(System.getProperty("RDS_PASSWORD", "salescrm123"));
		return hikariConfig;
	}	

}
