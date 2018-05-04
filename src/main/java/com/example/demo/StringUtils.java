package com.example.demo;

import java.rmi.dgc.VMID;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class StringUtils {
	private static final Logger LOG = LoggerFactory.getLogger( StringUtils.class );

	private StringUtils() {
	}

	/**
	 * Utility method to check if a string is empty.
	 * 
	 * @param str
	 *            Input string which has to be checked.
	 * @return true, if checks if is empty
	 */
	public static boolean isEmpty( final String str ) {
		return str == null || (str.trim().length() == 0);
	}

	/**
	 * Utility method to check if a string is not empty.
	 * 
	 * @param str
	 *            Input string which has to be checked.
	 * @return true, if checks if is not empty
	 */
	public static boolean isNotEmpty( final String str ) {
		return !isEmpty( str );
	}

	/**
	 * This method checks if the given map is NULL or Empty
	 * 
	 * @param map
	 *            instance of <code>java.util.Map</code>
	 * @return true if this map is either NULL or contains no key-value mappings.
	 */
	public static boolean isEmpty( final Map< ?, ? > map ) {
		return map == null || map.isEmpty();
	}

	/**
	 * This method checks if the given map is NOT NULL and NOT Empty
	 * 
	 * @param map
	 *            instance of <code>java.util.Map</code>
	 * @return true if this map is NOT NULL and contains key-value mappings.
	 */
	public static boolean isNotEmpty( final Map< ?, ? > map ) {
		return !isEmpty( map );
	}

	/**
	 * using hashCode to reduce the length of the string.
	 * 
	 * @param inPrefix
	 *            the in prefix
	 * @return String
	 */
	public static String generateTransactionId() {
		final VMID vmid = new VMID();
		return vmid.toString();
	}

	/**
	 * this method is used to formatDate
	 * 
	 * @param date
	 * @param fromFormat
	 * @param toFormat
	 * @return
	 */
	public static String formatDate( final String date, final String fromFormat, final String toFormat ) {
		if ( date == null ) {
			return PlatformConstants.EMPTY_STRING;
		}
		final SimpleDateFormat sdf1 = new SimpleDateFormat( fromFormat );
		sdf1.setLenient( false );
		Date parsedDt;
		try {
			parsedDt = sdf1.parse( date );
		}
		catch( Exception e ) {
			LOG.error("Exception in FormatDate call ", e);
			return date;
		}
		final SimpleDateFormat sdf = new SimpleDateFormat( toFormat );
		return sdf.format( parsedDt );
	}

	/**
	 * Checks if the given objArr is null or size 0
	 * 
	 * @param byteArr
	 *            array of Byte.
	 * @return true if the array is null or size 0.
	 */
	public static boolean isEmpty( final byte[] byteArr ) {
		return (byteArr == null) || (byteArr.length < 1);
	}

	/**
	 * Checks if the given objArr is not null and size not 0
	 * 
	 * @param byteArr
	 *            array of Byte.
	 * @return true if the array is not null and size 0.
	 */
	public static boolean isNotEmpty( final byte[] byteArr ) {
		return !isEmpty( byteArr );
	}

	/**
	 * Checks if the given objArr is null or size 0
	 * 
	 * @param objArr
	 *            array of Object.
	 * @return true if the array is null or size 0.
	 */
	public static boolean isEmpty( final Object[] objArr ) {
		return (objArr == null) || (objArr.length < 1);
	}

	/**
	 * Checks if the given objArr is not null and size not 0
	 * 
	 * @param objArr
	 *            array of Object.
	 * @return true if the array is not null and size 0.
	 */
	public static boolean isNotEmpty( final Object[] objArr ) {
		return !isEmpty( objArr );
	}

	/**
	 * This method checks if the given list is null or is empty.
	 * 
	 * @param listObj
	 *            instance of java.util.List
	 * @return true if list is null or size == 0
	 */
	public static boolean isEmpty( final List< ? > listObj ) {
		return listObj == null || listObj.isEmpty();
	}

	/**
	 * This returns true is the given list is not null or not empty.
	 * 
	 * @param listObj
	 *            instance of java.util.List
	 * @return boolean value.
	 */
	public static boolean isNotEmpty( final List< ? > listObj ) {
		return !isEmpty( listObj );
	}

	/**
	 * Formats the given string if it is null.
	 * 
	 * @param string
	 *            input.
	 * @return either given string or empty string if null.
	 */
	public static String getValidString( final String string ) {
		if ( isNotEmpty( string ) ) {
			return string.trim();
		}
		else {
			return PlatformConstants.EMPTY_STRING;
		}
	}

	/**
	 * this method is used to isEmpty
	 * 
	 * @param collection
	 * @return
	 */
	public static boolean isEmpty( final Collection< ? > collection ) {
		return collection == null || collection.isEmpty();
	}

	/**
	 * This method checks if the given collection is NOT NULL and NOT Empty
	 * 
	 * @param collection
	 *            instance of <code>java.util.Collection</code>
	 * @return true if this set is NOT NULL.
	 */
	public static boolean isNotEmpty( final Collection< ? > collection ) {
		return !isEmpty( collection );
	}

	/**
	 * Utility method to parse the given string.
	 * 
	 * @param value
	 *            input string, a number in string format.
	 * @return an int value for the given string value.
	 */
	public static int parseInt( final String value ) {
		int nRet = 0;
		String temp = null;

		if ( isEmpty( value ) ) {
			return nRet;
		}
		temp = value.trim();
		try {
			nRet = Integer.parseInt( temp );
		}
		catch( NumberFormatException nEx ) {

		}
		return nRet;
	}

	/**
	 * this method is used to getStringFromBundle
	 * 
	 * @param res
	 * @param key
	 * @return
	 */
	public static String getStringFromBundle( final ResourceBundle res, final String key ) {
		String value = PlatformConstants.EMPTY_STRING;
		try {
			value = res.getString( key ).trim();
		}
		catch( Exception e ) {
			// since there will be too many exceptions of this
			// kind, dont report the exception
		}
		return value;
	}

	/**
	 * this method is used to getStringFromBundle
	 * 
	 * @param res
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getStringFromBundle( final ResourceBundle res, final String key, final String defaultValue ) {
		String value = getStringFromBundle( res, key );
		if ( isEmpty( value ) ) {
			value = defaultValue;
		}
		return value;
	}

	/**
	 * Returns a trimmed String if object is not null.
	 * 
	 * @param object
	 *            the object
	 * @return either given string or empty string if null.
	 */
	public static String trim( final Object object ) {
		if ( object != null ) {
			return object.toString().trim();
		}
		else {
			return "";
		}
	}

	/**
	 * this method is used to getFormattedCurrentDate
	 * 
	 * @param dateFormat
	 * @return
	 */
	public static String getFormattedCurrentDate( final String dateFormat ) {
		String dateVal = null;
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat( dateFormat );
			dateVal = sdf.format( new java.util.Date() );
		}
		catch( Exception e ) {
			LOG.error("Exception in getFormattedCurrentDate call ", e);
			return null;
		}
		return dateVal;
	}

}

