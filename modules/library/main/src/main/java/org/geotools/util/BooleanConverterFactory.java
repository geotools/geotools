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

import org.geotools.factory.Hints;

/**
 * ConverterFactory for handling boolean conversions.
 * <p>
 * Supported conversions:
 * <ul>
 * 	<li>"true" -> Boolean.TRUE
 * 	<li>"false" -> Boolean.FALSE
 * 	<li>"1" -> Boolean.TRUE
 *  <li>"0" -> Boolean.FALSE
 *  <li>1 -> Boolean.TRUE
 *  <li>0 -> Boolean.FALSE 
 * </ul>
 * </p>
 * @author Justin Deoliveira, The Open Planning Project
 * @since 2.4
 *
 *
 * @source $URL$
 */
public class BooleanConverterFactory implements ConverterFactory {

	public Converter createConverter(Class source, Class target, Hints hints) {
		if ( target.equals( Boolean.class ) ) {
			
			//string to boolean
			if ( source.equals( String.class ) ) {
				return new Converter() {

					public Object convert(Object source, Class target) throws Exception {
						if ( "true".equals( source ) || "1".equals( source ) ) {
							return Boolean.TRUE;
						}
						if ( "false".equals( source ) || "0".equals( source ) ) {
							return Boolean.FALSE;
						}
						
						return null;
					}
					
				};
			}
			
			//integer to boolean
			if ( source.equals( Integer.class ) ) {
				return new Converter() {

					public Object convert(Object source, Class target) throws Exception {
						if ( new Integer( 1 ).equals( source ) ) {
							return Boolean.TRUE;
						}
						if ( new Integer( 0 ).equals( source ) ) {
							return Boolean.FALSE;
						}
						
						return null;
					}
					
				};
			}
			
		}
		
		return null;
	}

}
