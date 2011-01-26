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
package org.geotools.xml.gml;

import java.util.List;

import org.geotools.data.DataUtilities;
import org.geotools.feature.type.AttributeTypeImpl;
import org.geotools.util.SimpleInternationalString;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.util.InternationalString;

/**
 * Created for GML generated FeatureTypes.  Represents a Choice type.  
 *  
 *  
 * This is temporary and only for use by the parser.  It should never be public or in common use.
 * 
 * @author Jesse
 */
class ChoiceAttributeTypeImpl extends AttributeTypeImpl implements ChoiceAttributeType {
	private static final Class[] EMPTY = new Class[0];
    protected Class[] types;
    private boolean isNillable;
    private int maxOccurs;
    private int minOccurs;
    Object defaultValue;
    
	public ChoiceAttributeTypeImpl(Name name, Class<?>[] types, Class<?> defaultType, boolean nillable, int min, int max,
            Object defaultValue, List<Filter> filter) {
	    super( name, defaultType, false, false, filter, null, toDescription(types) );
	    if( defaultValue == null && !isNillable ){
	        defaultValue = DataUtilities.defaultValue( defaultType );
	    }
	    this.minOccurs = min;
	    this.maxOccurs = max;
    }

	public Class[] getChoices() {
		return EMPTY;
	}
	public Object convert(Object obj) {
		return obj;
	}

    public Object getDefaultValue() {
        return defaultValue;
    }

    public String getLocalName() {
        return getName().getLocalPart();
    }

    public AttributeType getType() {
        return this;
    }

    public int getMaxOccurs() {
        return minOccurs;
    }

    public int getMinOccurs() {
        return maxOccurs;
    }

    public boolean isNillable() {
        return isNillable;
    }

    static InternationalString toDescription( Class[] bindings ){
        StringBuffer buf = new StringBuffer();
        buf.append("Choice betwee ");
        for( Class bind : bindings ){
            buf.append( bind.getName() );
            buf.append( "," );
        }
        return new SimpleInternationalString( buf.toString() );
    }
}
