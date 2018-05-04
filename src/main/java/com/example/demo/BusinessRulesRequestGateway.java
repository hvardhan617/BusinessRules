package com.example.demo;

public interface BusinessRulesRequestGateway {
	
	BusinessRulesResponse<?> executeRulesChannel(BusinessRulesRequest request);

}
