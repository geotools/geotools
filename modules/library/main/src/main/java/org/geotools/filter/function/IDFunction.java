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
 *
 */
package org.geotools.filter.function;

import static org.geotools.filter.capability.FunctionNameImpl.*;

import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.feature.Attribute;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.capability.FunctionName;

/**
 * Allow access to the value of Feature.getID() as an expression
 * 
 * @author Jody Garnett
 * @since 2.2, 2.5
 *
 *
 *
 * @source $URL$
 */
public class IDFunction extends FunctionExpressionImpl {

    public static FunctionName NAME = new FunctionNameImpl("id",
            String.class);
    
	public IDFunction() {
	    super(NAME);
	}

	public String toString() {
		return "ID()";
	}

	@Override
	public Object evaluate(Object obj) {
	    if( obj instanceof SimpleFeature){
	        SimpleFeature feature = (SimpleFeature) obj;
	        return feature.getID();
	    }
	    if( obj instanceof Attribute){
	    	Attribute attribute = (Attribute) obj;
	        return attribute.getIdentifier().getID();
	    }
		return ""; // no ID
	}

}
