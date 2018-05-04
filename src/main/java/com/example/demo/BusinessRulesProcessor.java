package com.example.demo;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.drools.core.command.impl.GenericCommand;
import org.drools.core.command.runtime.BatchExecutionCommandImpl;
import org.drools.core.command.runtime.rule.FireAllRulesCommand;
import org.drools.core.command.runtime.rule.InsertObjectCommand;
import org.kie.api.runtime.ExecutionResults;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.KieContainerResource;
import org.kie.server.api.model.KieContainerResourceList;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.RuleServicesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;


public class BusinessRulesProcessor {

	private static final Logger log = LoggerFactory.getLogger(BusinessRulesProcessor.class);

	public Hashtable executeRules(Hashtable ht)  {
		List<InsertObjectCommand> insertObjectCommandList = (List<InsertObjectCommand>) ht
				.get("insertObjectCommandList");
		String containerId = (String) ht.get("containerId");
		String defaultKieSessionName = (String) ht.get("defaultKieSessionName");
		Set<Class<?>>  allClasses = (Set<Class<?>> ) ht.get("allClasses");
		Hashtable responseHash = new Hashtable();
		try {

			long start = System.currentTimeMillis();

			Properties properties = ResourceLoaderUtils.loadProperties("DroolsConfig.properties");

			String serverUrl = properties.getProperty("serverUrl");
			String user = properties.getProperty("user");
			String password = properties.getProperty("password");
			String connectionTimeout=properties.getProperty("BRMSconnectionTimeout");
			// String containerId =properties.getProperty("containerId");
			if(log.isDebugEnabled()){
				log.debug("serverUrl :: :: " + serverUrl);
				log.debug("user :: :: " + user);
				log.debug("password :: :: " + password);
				log.debug("containerId :: :: " + containerId);
				log.debug("allClasses :: :: " + allClasses);
				log.debug("insertObjectCommandList :: :: " + insertObjectCommandList);
			}
			KieServicesConfiguration configuration = KieServicesFactory.newRestConfiguration(serverUrl, user, password);
			configuration.setMarshallingFormat(MarshallingFormat.JSON);
			
			configuration.setTimeout(Long.valueOf(connectionTimeout));
			
			
	        if(null!=allClasses){
	        	configuration.addJaxbClasses(allClasses);
	        }
			
			KieServicesClient kieServicesClient = KieServicesFactory.newKieServicesClient(configuration);
			if(log.isDebugEnabled()){
				log.debug("kieServicesClient :: :: " + kieServicesClient);
			}
			ServiceResponse<KieContainerResourceList> containersList = kieServicesClient.listContainers();
			KieContainerResourceList containers = containersList.getResult();
			// check if the container is not yet deployed, if not deploy it
			if (containers != null) {
				for (KieContainerResource kieContainerResource : containers.getContainers()) {
					if (kieContainerResource.getContainerId().equals(containerId)) {
						if(log.isDebugEnabled()){
							log.debug("\t######### Found container " + containerId + " skipping deployment...");
						}
						break;
					}
				}
			}

			// work with rules
			List<GenericCommand<?>> commands = new ArrayList<GenericCommand<?>>();
			BatchExecutionCommandImpl executionCommand = new BatchExecutionCommandImpl(commands);
			if (!"".equals(defaultKieSessionName)) {
				executionCommand.setLookup(defaultKieSessionName);
			} else {
				executionCommand.setLookup("defaultStatelessKieSession");
				log.debug("defaultStatelessKieSession :: Session value .." + executionCommand.getLookup() );
			}

			for (InsertObjectCommand insertObjectCommand : insertObjectCommandList) {
				commands.add(insertObjectCommand);
			}

			FireAllRulesCommand fireAllRulesCommand = new FireAllRulesCommand();
			commands.add(fireAllRulesCommand);
			
			// System.setProperty("org.kie.server.json.date_format",
			// "dd/MM/yyyy");

			//RuleServicesClient ruleClient = kieServicesClient.getServicesClient(RuleServicesClient.class);
			ObjectMapper objMapper = new ObjectMapper();
			if(log.isDebugEnabled()){
				log.debug("objMapper.writeValueAsString(commands) :: :: " + objMapper.writeValueAsString(commands));
			}
			
			
			RuleServicesClient ruleClient = kieServicesClient.getServicesClient(RuleServicesClient.class);
			
			//ServiceResponse<String> serviceRes = ruleClient.executeCommands(containerId, executionCommand);
			ServiceResponse<ExecutionResults> exResults = ruleClient.executeCommandsWithResults(containerId, executionCommand);
			
			
				log.debug("exResults :: :: " + exResults);
			
			/*HashSet<Class<?>> sampSet = new HashSet<Class<?>>();
			sampSet.add(List.class);
			Marshaller marshaller = MarshallerFactory.getMarshaller(sampSet, configuration.getMarshallingFormat(),
					List.class.getClassLoader());
			org.kie.api.runtime.ExecutionResults exResults = marshaller.unmarshall(serviceRes.getResult(),
					ExecutionResultImpl.class);*/

			log.info("Execution completed in " + (System.currentTimeMillis() - start));

			responseHash.put("exResults", exResults);
			responseHash.put("insertObjectCommandList", insertObjectCommandList);
			responseHash.put("insertObjectCommandKeys", ht.get("insertObjectCommandKeys"));
			
			
			return responseHash;

		}
		catch (Exception e) {
			log.error("Executing rules call exception",e);
			responseHash.put("Error:",String.valueOf(e.getCause()));
		}

		return responseHash;
	}

}

