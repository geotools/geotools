/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.util;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.BigIntegerConverter;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.geotools.factory.Hints;

/**
 * ConverterFactory based on the apache commons {@link org.apache.commons.beanutils.Converter}
 * interface.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 * @since 2.4
 *
 * @source $URL$
 */
public class CommonsConverterFactory implements ConverterFactory {
	
	//some additional converters
    
    /**
     * converts a string to a uri.
     */
	static org.apache.commons.beanutils.Converter uri = new org.apache.commons.beanutils.Converter() {
		public Object convert( Class target, Object value ) {
			String string = (String) value;
			try {
				return new URI( string );
			} 
			catch (URISyntaxException e) { } 
		
			return null;
		}
	};
	/**
	 * converts a string to a number when the target class == Number, does so 
	 * by delegating to the other numeric converters
	 */
	static org.apache.commons.beanutils.Converter number = new org.apache.commons.beanutils.Converter() {
	    public Object convert(Class type, Object value) {
	        String string = (String) value;
	        Number parsed = null;
	        try {
	            //first try integer
	            parsed = (Number) new IntegerConverter().convert(Integer.class, string);
	        }
	        catch(Exception e) {}
	        
	        if ( parsed == null ) {
	            //try double
	            parsed = (Number) new DoubleConverter().convert(Double.class,string);
	        }
	        
	        return parsed;
	    };
	};
	
	static {
	    ConvertUtils.register( uri, URI.class );
		ConvertUtils.register( number, Number.class );
		
		//make sure numeric converters do not use default value
		ConvertUtils.register( new ByteConverter(null), Byte.class );
		ConvertUtils.register( new ShortConverter(null), Short.class );
		ConvertUtils.register( new IntegerConverter(null), Integer.class );
		ConvertUtils.register( new LongConverter(null), Long.class );
		ConvertUtils.register( new BigIntegerConverter(null), BigInteger.class );
		ConvertUtils.register( new FloatConverter(null), Float.class );
		ConvertUtils.register( new DoubleConverter(null), Double.class );
		ConvertUtils.register( new BigDecimalConverter(null), BigDecimalConverter.class );
		
		ConvertUtils.register( new BooleanConverter(null), Boolean.class );
		ConvertUtils.register( new CharacterConverter(null), Character.class );
	}
	
	
	/**
	 * Delegates to {@link ConvertUtils#lookup(java.lang.Class)} to create a 
	 * converter instance.
	 * 
	 * @see ConverterFactory#createConverter(Class, Class, Hints). 
	 */
	public Converter createConverter(Class source, Class target, Hints hints) {
		//only do strings
		if ( source.equals( String.class ) ) {
			org.apache.commons.beanutils.Converter converter = ConvertUtils.lookup( target );
			if ( converter != null ) {
				return new CommonsConverterWrapper( converter );
			}
		}
		
		return null;
	}

	/**
	 * Decorates a beanutils converter in a geotools converter.
	 * 
	 * @author Justin Deoliveira, The Open Planning Project
	 *
	 */
	static class CommonsConverterWrapper implements Converter {

		org.apache.commons.beanutils.Converter delegate;
		
		public CommonsConverterWrapper( org.apache.commons.beanutils.Converter delegate ) {
			this.delegate = delegate;
		}
		
		public Object convert(Object source, Class target) throws Exception {
			return delegate.convert( target, source );
		}
		
	}
	
	
}
