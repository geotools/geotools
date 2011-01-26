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

package org.geotools.data.oracle;

import java.lang.reflect.Method;

import org.geotools.factory.Hints;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;

public class OracleBlobConverterFactory implements ConverterFactory {
	
	OracleDateConverter converter = new OracleDateConverter();
	static final Class<?> ORA_BLOB;
	static final Method ORA_GET_BYTES;
	static final Method ORA_LENGTH;
	
	static {
	    Class<?> oracleBlobClass = null;
	    try {
	        oracleBlobClass = Class.forName("oracle.sql.BLOB");
	    } catch (ClassNotFoundException e) {
	        // ojdbc*.jar not on the path
	    }
	    if (oracleBlobClass == null) {
	        ORA_BLOB = null;
	        ORA_GET_BYTES = null;
	        ORA_LENGTH = null;
	    } else {
	        try {
	            ORA_BLOB = oracleBlobClass;
	            ORA_LENGTH = ORA_BLOB.getMethod("length");
	            ORA_GET_BYTES = ORA_BLOB.getMethod("getBytes", long.class, int.class);
	        } catch(Exception e) {
	            throw new RuntimeException("Could not initialize the oracle blob converter", e);
	        }
	    }
	}

	public Converter createConverter(Class<?> source, Class<?> target,
			Hints hints) {
	    // if the jdbc driver is not in the classpath don't bother trying to convert
	    if(ORA_BLOB == null)
	        return null;
	    
		// can only convert towards byte[]
		if (!(byte[].class.equals(target)))
			return null;

		// can only deal with oracle specific blob classes
		if (!ORA_BLOB.isAssignableFrom(source))
			return null;
		
		// converter is thread safe, so cache and return just one
		return converter;
	}
	
	class OracleDateConverter implements Converter {

		public <T> T convert(Object source, Class<T> target) throws Exception {
		    int length = ((Long) ORA_LENGTH.invoke(source)).intValue();
		    return (T) ORA_GET_BYTES.invoke(source, 1l, length);
		}

	}

	
}
