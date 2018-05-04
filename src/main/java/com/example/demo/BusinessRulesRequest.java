package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BusinessRulesRequest {

	/**
	 * this holds rule Request
	 */
	private List<Map<String, ?>> listInsertFacts = new ArrayList<Map<String, ?>>();
	private String containerId;
	private String defaultKieSessionName;
	
	//private String test;
	
	/**
	 * @return the containerId
	 */
	public String getContainerId() {
		return containerId;
	}

	/**
	 * @param containerId the containerId to set
	 */
	public void setContainerId(String containerId) {
		this.containerId = containerId;
	}

	/**
	 * @return the defaultKieSessionName
	 */
	public String getDefaultKieSessionName() {
		return defaultKieSessionName;
	}

	/**
	 * @param defaultKieSessionName the defaultKieSessionName to set
	 */
	public void setDefaultKieSessionName(String defaultKieSessionName) {
		this.defaultKieSessionName = defaultKieSessionName;
	}

	/**
	 * @return the listInsertFacts
	 */
	public List<Map<String, ?>> getListInsertFacts() {
		return listInsertFacts;
	}

	/**
	 * @param listInsertFacts the listInsertFacts to set
	 */
	public void setListInsertFacts(List<Map<String, ?>> listInsertFacts) {
		this.listInsertFacts = listInsertFacts;
	}

	
	

}

