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

package org.geotools.data.ingres;

import java.io.InputStream;
import java.lang.reflect.Method;

import org.geotools.factory.Hints;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;

public class IngresBlobConverterFactory implements ConverterFactory {
	
	IngresBlobConverter converter = new IngresBlobConverter();
	static final Class<?> INGRES_BLOB;
	static final Method INGRES_GET_STREAM;
	static final Method INGRES_LENGTH;
	
	static {
	    Class<?> jdbcBlobClass = null;
	    try {
	        jdbcBlobClass = Class.forName("java.sql.Blob");
	    } catch (ClassNotFoundException e) {
	        // ojdbc*.jar not on the path
	    }
	    if (jdbcBlobClass == null) {
	        INGRES_BLOB = null;
	        INGRES_GET_STREAM = null;
	        INGRES_LENGTH = null;
	    } else {
	        try {
	            INGRES_BLOB = jdbcBlobClass;
	            INGRES_LENGTH = INGRES_BLOB.getMethod("length");
	            INGRES_GET_STREAM = INGRES_BLOB.getMethod("getBinaryStream");
	        } catch(Exception e) {
	            throw new RuntimeException("Could not initialize the ingres blob converter", e);
	        }
	    }
	}

	public Converter createConverter(Class<?> source, Class<?> target,
			Hints hints) {
	    // if the jdbc driver is not in the classpath don't bother trying to convert
	    if(INGRES_BLOB == null)
	        return null;
	    
		// can only convert towards byte[]
		if (!(byte[].class.equals(target)))
			return null;

		// can only deal with oracle specific blob classes
		if (!INGRES_BLOB.isAssignableFrom(source))
			return null;
		
		// converter is thread safe, so cache and return just one
		return converter;
	}
	
	class IngresBlobConverter implements Converter {

		public <T> T convert(Object source, Class<T> target) throws Exception {
		    int length = ((Long) INGRES_LENGTH.invoke(source)).intValue();
		    byte[] buffer = new byte[length]; 
		    InputStream dataStream = (InputStream) INGRES_GET_STREAM.invoke(source);
		    dataStream.read(buffer, 0, length);
		    return (T) buffer;
		}

	}

	
}
