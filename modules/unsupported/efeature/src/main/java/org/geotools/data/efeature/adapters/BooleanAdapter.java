/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.efeature.adapters;

import org.geotools.data.efeature.DataTypes;

/**
 * @author kengu - 12. juni 2011
 *
 */
public class BooleanAdapter extends org.eclipse.emf.query.conditions.booleans.BooleanAdapter {
    
    public static final org.geotools.data.efeature.adapters.BooleanAdapter 
        DEFAULT = new org.geotools.data.efeature.adapters.BooleanAdapter();

    /** Hide constructor */
    private BooleanAdapter() { /*NOP*/};
    
    @Override
    public Boolean getBoolean(Object value) {
        //
        // Direct conversion?
        //
        if(value instanceof Boolean) {
            return (Boolean)value;
        } 
        //
        // Try string conversion?
        //
        else if( DataTypes.isBoolean(value, true) ) {
            return Boolean.valueOf(value.toString());
        }
        //
        // Conversion failed
        //        
        throw new IllegalArgumentException("Value " 
                + value + " can not be converted into a boolean");
    }
}
