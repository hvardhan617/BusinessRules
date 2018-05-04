package com.example.demo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.drools.core.command.runtime.rule.InsertObjectCommand;
import org.json.simple.JSONObject;
import org.kie.api.runtime.ExecutionResults;
import org.kie.server.api.model.ServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@SuppressWarnings("deprecation")
@Component
public class BusinesRulesTransformers implements BusinessRulesRequestGateway {

	private static final Logger LOG = LoggerFactory.getLogger(BusinesRulesTransformers.class);

	@Autowired
	BusinessRulesResponse<Object> businessRulesResponse;
	
	public Hashtable<String, Object> transformRuleFacts(BusinessRulesRequest request) throws Exception {
		
		LOG.info("BusinesRulesTransformers :: transformRuleFacts :: ");
		
		final List<InsertObjectCommand> insertObjectCommandList = new ArrayList<InsertObjectCommand>();
		final Map<InsertObjectCommand, String> insertObjectCommandKeys = new HashMap<InsertObjectCommand, String>();
		
		Hashtable<String,Object> rulesHashTable = new Hashtable<String,Object>();
		
		if(request.getListInsertFacts()!=null && request.getListInsertFacts().size()>0){
			
			Set<Class<?>> allClasses = new HashSet<Class<?>>();
			
			for(Map<String,?> ruleFact:request.getListInsertFacts() ){
				
				final JSONObject jsonObject = new JSONObject();
				jsonObject.putAll(ruleFact);
				final JSONObject jsonObjectOutIdentifiers = new JSONObject();
				
					LOG.info("jsonObject :: :: " + jsonObject);
				
				for (final Iterator iterator = jsonObject.keySet().iterator(); iterator.hasNext();) {
					String key = (String) iterator.next();
					
					if ("outIdentifiers".equalsIgnoreCase(key)) {
						jsonObjectOutIdentifiers.putAll((Map) jsonObject.get(key));

						break;
					}

				}
				
					LOG.info("jsonObjectOutIdentifiers :: :: " + jsonObjectOutIdentifiers);
				

				
				
				for (final Iterator iterator = jsonObject.keySet().iterator(); iterator.hasNext();) {
					String key = (String) iterator.next();
					if ("outIdentifiers".equalsIgnoreCase(key)) {
						continue;
					}
					final InsertObjectCommand insertObjectCommand = new InsertObjectCommand(createInserObject(key, jsonObject.get(key)),false);
				
			         
			    /*     Class ruleFactClass = Class.forName(key);  
			         if(LOG.isDebugEnabled()){
			        	 LOG.debug("ruleFactClass :: :: "+ruleFactClass.getName());
			         }
			         
					allClasses.add(ruleFactClass.getClass());*/

					if (jsonObjectOutIdentifiers != null && jsonObjectOutIdentifiers.containsKey(key)) {
						insertObjectCommand.setOutIdentifier(String.valueOf(jsonObjectOutIdentifiers.get(key)));
						insertObjectCommand.setReturnObject(Boolean.TRUE);
						insertObjectCommand.setEntryPoint("DEFAULT");
					}
					insertObjectCommandKeys.put(insertObjectCommand, key);
					insertObjectCommandList.add(insertObjectCommand);
				}
			}
			
			rulesHashTable.put("insertObjectCommandList", insertObjectCommandList);
			rulesHashTable.put("allClasses", allClasses);
			rulesHashTable.put("containerId", request.getContainerId());
			rulesHashTable.put("defaultKieSessionName", request.getDefaultKieSessionName());
			rulesHashTable.put("insertObjectCommandKeys", insertObjectCommandKeys);
			
		}
		return rulesHashTable;
		
	}

	
	public BusinessRulesResponse<Object> transformRuleResponse(Hashtable responseHashTable) throws Exception {

		ServiceResponse<ExecutionResults> exResultsResponse = (ServiceResponse<ExecutionResults>)responseHashTable.get("exResults");
		/*org.kie.api.runtime.ExecutionResults exResults = (org.kie.api.runtime.ExecutionResults) responseHashTable
				.get("exResults");*/
		org.kie.api.runtime.ExecutionResults exResults = null;
		
		if(null!=exResultsResponse){
			exResults = exResultsResponse.getResult();
			LOG.info("exResultsResponse.getResult() :: :: "+exResultsResponse.getResult());
		}
		List<InsertObjectCommand> insertObjectCommandList = (List<InsertObjectCommand>) responseHashTable
				.get("insertObjectCommandList");
		Map<InsertObjectCommand, String> insertObjectCommandKeys = (Map<InsertObjectCommand, String>) responseHashTable
				.get("insertObjectCommandKeys");
		
		Map<String, Object> returnObjects = new HashMap<String, Object>();
		if (exResults != null) {
			
			Map<String, Object> parseObjects = new HashMap<String, Object>();
			for (final InsertObjectCommand insertObjectCommand : insertObjectCommandList) {
				LOG.info("insertObjectCommand.getOutIdentifier():: :: "+insertObjectCommand.getOutIdentifier());
				LOG.info("exResults.getValue(insertObjectCommand.getOutIdentifier()) :: :: "+exResults.getValue(insertObjectCommand.getOutIdentifier()));
				if (insertObjectCommand.getOutIdentifier() != null
						&& !"".equals(insertObjectCommand.getOutIdentifier())) {
					/*parseObjects = parseOutputForRuleStatus(
							(Map) exResults.getValue(insertObjectCommand.getOutIdentifier()),
							insertObjectCommandKeys.get(insertObjectCommand));
					parseObjects = parseOutputForRuleObject(parseObjects);*/
					returnObjects.put(insertObjectCommand.getOutIdentifier(), exResults.getValue(insertObjectCommand.getOutIdentifier()));
				}
			}

			//returnObjects = parseOutputForRuleObject(returnObjects);

			businessRulesResponse.setRuleResponse(returnObjects);
		}else if(responseHashTable.get("Error:")!=null){
			returnObjects.put("Error", responseHashTable.get("Error:"));
			businessRulesResponse.setRuleResponse(returnObjects);
		}
		else{
			returnObjects.put("Error:", "Error while executing rules");
			businessRulesResponse.setRuleResponse(returnObjects);
		}
		return businessRulesResponse;

	}

	private Map<String, Object> createInserObject(final String className, final Object inputObj) {
		final Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put(className, inputObj);
		return inputMap;
	}

	/*private static Map parseOutputForRuleStatus(final Map valueMap, final String className) {
		if (valueMap == null)
			return null;
		return (Map) valueMap.get(className);

	}*/

	private Map<String, Object> parseOutputForRuleObject(final Map<String, Object> mapObject) {
		final Map<String, Object> returnObjectMap = new HashMap<String, Object>();
		if (null != mapObject.entrySet()) {
			for (final Entry<String, Object> property : mapObject.entrySet()) {
				final String key = property.getKey();
				final Object value = property.getValue();
				/*if (LOG.isDebugEnabled()) {
					LOG.debug("key :: :: :: " + key + ":: ::: value ::: " + value);
				}*/
				if (value != null) {
					if (value instanceof java.util.LinkedHashMap) {
						final LinkedHashMap<String, Object> valueMap = (LinkedHashMap<String, Object>) value;
						for (final Entry<String, Object> property2 : valueMap.entrySet()) {
							final Object valueInMap = property2.getValue();
							if (valueInMap != null) {
								switch (property2.getKey()) {
								case "java.lang.Integer": {
									returnObjectMap.put(property.getKey(), Integer.valueOf(valueInMap.toString()));
									break;
								}
								case "java.lang.Long": {
									returnObjectMap.put(property.getKey(), Long.valueOf(valueInMap.toString()));
									break;
								}

								case "java.lang.Double": {
									returnObjectMap.put(property.getKey(), new Double(valueInMap.toString()));
									break;
								}

								case "java.math.BigDecimal": {
									returnObjectMap.put(property.getKey(), new BigDecimal(valueInMap.toString()));
									break;
								}

								case "java.lang.Boolean": {
									returnObjectMap.put(property.getKey(), Boolean.valueOf(valueInMap.toString()));
									break;
								}
								case "java.util.Date": {

									returnObjectMap.put(property.getKey(),
											new Date(Long.valueOf(valueInMap.toString())));

									break;
								}

								default: {
									if (LOG.isDebugEnabled()) {
										LOG.debug("Un handled type " + property2.getKey());
										break;
									}
								}
								}
							}

						}
					} else {

						returnObjectMap.put(property.getKey(), property.getValue());
					}
				} else {
					returnObjectMap.put(property.getKey(), null);
				}
			}

		}

		return returnObjectMap;
	}
	
	@Autowired
	private BusinessRulesProcessor businessRulesProcessor;

	@Override
	public BusinessRulesResponse<?> executeRulesChannel(BusinessRulesRequest request) {
		BusinessRulesResponse<Object> response = null;
		try {
			Hashtable<String, Object> result=transformRuleFacts(request);
			Hashtable<?, ?> ht=businessRulesProcessor.executeRules(result);
			response=transformRuleResponse(ht);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

}

