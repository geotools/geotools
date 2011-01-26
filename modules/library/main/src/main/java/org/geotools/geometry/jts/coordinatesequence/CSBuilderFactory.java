/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 * Created on 31-dic-2004
 */
package org.geotools.geometry.jts.coordinatesequence;

/**
 * A factory to obtain a builder for JTS CoordinateSequence objects.
 * 
 * @author wolf
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 * @source $URL$
 */
public class CSBuilderFactory {
	private static Class defaultBuilderClass;
	
	public static CSBuilder getDefaultBuilder() {
		try {
			return (CSBuilder) getDefaultBuilderClass().newInstance();
		} catch (Exception e) {
			// TODO: should we throw a better exception here? It's a fatal error anyway...
			throw new RuntimeException("Could not create a coordinate sequence builder", e);
		}
	}
	
	private static Class getDefaultBuilderClass() {
		if(defaultBuilderClass == null) {
			defaultBuilderClass = DefaultCSBuilder.class;
		}
		return defaultBuilderClass;
	}

	/**
	 * @param builderClass
	 */
	public static void setDefaultBuilderClass(Class builderClass) {
		if(!CSBuilder.class.isAssignableFrom(builderClass))
			throw new RuntimeException(builderClass.getName() + " does not implement the CSBuilder interface");
		defaultBuilderClass = builderClass;
		
	}
	
	
}
