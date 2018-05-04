package com.example.demo;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.annotation.IntegrationComponentScan;


@Configuration
//@ImportResource("classpath*:invoices-context.xml")
@ComponentScan(basePackages = { "com.intellect.platform" })
//@IntegrationComponentScan("com.intellect.platform")
public class BRIntegrationConfig {
	
	Properties properties = ResourceLoaderUtils.loadProperties("DroolsConfig.properties");

	@Bean
	public BusinessRulesComponent businessRulesComponent() {
		return new BusinessRulesComponent();
	}
	
	@Bean
	public BusinessRulesResponse<?> businessRulesResponse() {
		return new BusinessRulesResponse<>();
	}
	
	@Bean
	public BusinessRulesProcessor businessRulesProcessor() {
		return new BusinessRulesProcessor();
	}
	

}

