package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;


public class BusinessRulesComponent {
	
	@Autowired
	private BusinessRulesRequestGateway businessRulesRequestGateway;

	public BusinessRulesResponse<?> executeRules(BusinessRulesRequest request){
		return businessRulesRequestGateway.executeRulesChannel(request);
	}
}
