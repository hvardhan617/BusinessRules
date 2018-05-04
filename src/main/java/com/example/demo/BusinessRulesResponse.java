package com.example.demo;

import java.util.HashMap;
import java.util.Map;


public class BusinessRulesResponse< T > {

	/**
	 * this holds rule Response
	 */
	Map< String, T > ruleResponse = new HashMap< String, T >();

	/**
	 * @return the ruleResponse
	 */
	public Map< String, T > getRuleResponse() {
		return ruleResponse;
	}

	/**
	 * @param ruleResponse
	 *            the ruleResponse to set
	 */
	public void setRuleResponse( final Map< String, T > ruleResponse ) {
		this.ruleResponse = ruleResponse;
	}

}

