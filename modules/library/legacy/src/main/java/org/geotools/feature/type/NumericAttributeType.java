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
 */
package org.geotools.feature.type;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.geotools.feature.DefaultAttributeType;
import org.geotools.feature.PrimativeAttributeType;
import org.opengis.filter.Filter;

/**
 * Class that represents a Numeric.
 *
 * @author Ian Schneider
 * @author Chris Holmes, TOPP
 * @source $URL$
 */
public class NumericAttributeType extends DefaultAttributeType implements PrimativeAttributeType {
    /**
     * Constructor with name, type and nillable.  Type should always be a
     * Number class.
     *
     * @param name Name of this attribute.
     * @param type Class type of this attribute.
     * @param nillable If nulls are allowed for the attribute of this type.
     * @param min 
     * @param max 
     * @param defaultValue default value when none is suppled
     * @param filter
     *
     * @throws IllegalArgumentException is type is not a Number.
     *
     * @task REVISIT: protected?
     */
    public NumericAttributeType(String name, 
                                Class type, 
                                boolean nillable,
                                int min, 
                                int max, 
                                Object defaultValue, 
                                Filter filter)
        throws IllegalArgumentException {
        super(name, type, nillable, min, max,  defaultValue);
        this.filter = filter;
        if (!Number.class.isAssignableFrom(type)) {
            throw new IllegalArgumentException(
                "Numeric requires Number class, " + "not " + type);
        }
    }
    private Filter filter;
    public NumericAttributeType(String name, Class type, boolean nillable,
         Object defaultValue,Filter filter)
        throws IllegalArgumentException {
        super(name, type, nillable, defaultValue);
        this.filter = filter;
        if (!Number.class.isAssignableFrom(type)) {
            throw new IllegalArgumentException(
                "Numeric requires Number class, " + "not " + type);
        }
    }

    /**
     * Duplicate the given Object. In this case, since Number classes are
     * immutable, lets return the Object.
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object duplicate(Object o) {
        return o;
    }

	/* (non-Javadoc)
	 * @see org.geotools.feature.PrimativeAttributeType#getRestriction()
	 */
	public Filter getRestriction() {
		return filter;
	}


}
