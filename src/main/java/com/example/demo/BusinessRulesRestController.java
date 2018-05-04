package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BusinessRulesRestController {

	/**
	 * this holds LOG
	 */
	private static final Logger LOG = LoggerFactory.getLogger( BusinessRulesRestController.class );
	
	@Autowired
	BusinessRulesComponent businessRulesComponent;
	

	/**
	 * this method is used to executeRules
	 * 
	 * @param request
	 * @param containerId
	 * @param defaultKieSessionName
	 * @return
	 */
	@RequestMapping( value = "/fabric/businessrules/executeRules", method = RequestMethod.POST, consumes = "application/json",
			produces = "application/json" )
	public @ResponseBody BusinessRulesResponse<?> executeRules( @RequestBody final BusinessRulesRequest request,
			@RequestParam( required = true, value = "containerId" ) final String containerId,
			@RequestParam( required = true, value = "defaultKieSessionName" ) final String defaultKieSessionName ) {
		final BusinessRulesResponse<?> businessRulesResponse = new BusinessRulesResponse<>();
		try {
			request.setContainerId(containerId);
			request.setDefaultKieSessionName(defaultKieSessionName);
			return businessRulesComponent.executeRules(request);
			}
		catch( Exception e ) {
			LOG.error( "exception in executing rules", e );
		}
		return businessRulesResponse;
	}

	/**
	 * @return
	 */
	@GetMapping( "/fabric/businessrules/ping" )
	public String hello() {
		return "Ping Success";
	}

	
}

