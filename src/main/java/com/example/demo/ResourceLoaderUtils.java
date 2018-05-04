package com.example.demo;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ResourceLoaderUtils {

	/** The cached resource bundles. */
	private static ConcurrentMap< String, Properties > cachedProperties = new ConcurrentHashMap< String, Properties >( 1 );

	/**
	 * this holds LOG
	 */
	private static final Logger LOG = LoggerFactory.getLogger( ResourceLoaderUtils.class );

	/**
	 * this method is used to loadProperties
	 * 
	 * @param fileName
	 * @return
	 */
	public static Properties loadProperties( final String fileName ) {

		try {

			if ( StringUtils.isEmpty( fileName ) ) {
				return null;
			}

			if ( null != cachedProperties.get( fileName ) ) {
				return cachedProperties.get( fileName );
			}

			final Properties properties = new Properties();
			properties.load( ResourceLoaderUtils.class.getClassLoader().getResourceAsStream( fileName ) );
			cachedProperties.put( fileName, properties );

			return properties;

		}
		catch( IOException e ) {
			// TODO Auto-generated catch block
			LOG.error( "IO exception", e );
			return null;
		}
	}
}

