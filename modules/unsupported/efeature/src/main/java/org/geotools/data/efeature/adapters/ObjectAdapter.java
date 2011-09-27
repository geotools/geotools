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

import org.eclipse.emf.query.conditions.IDataTypeAdapter;
import org.geotools.data.efeature.DataBuilder;
import org.geotools.data.efeature.DataTypes;

import com.vividsolutions.jts.io.ParseException;

/**
 * @author kengu - 15. juni 2011
 *
 *
 * @source $URL$
 */
public class ObjectAdapter implements IDataTypeAdapter<Object> {
    
    public static final ObjectAdapter DEFAULT = new ObjectAdapter();

    /** Hide constructor */
    private ObjectAdapter() { /*NOP*/};

    @Override
    public Object adapt(Object value) {
        
        if(DataTypes.isNumeric(value)) {
            return DataBuilder.toNumber(value);
        }
        else if(DataTypes.isGeometry(value)) {
            try {
                return DataBuilder.toGeometry(value);
            } catch (ParseException e) {
                throw new IllegalArgumentException("Failed to adapt into Geometry",e);
            }
        }
        else if(DataTypes.isDate(value)) {
            return DataBuilder.toDate(value);
        }
        else if(DataTypes.isString(value)) {
            return DataBuilder.toString(value);
        }
        else if(DataTypes.isBoolean(value,false)) {
            return DataBuilder.toBoolean(value);
        }
        else if(DataTypes.isCharacter(value)) {
            return DataBuilder.toCharacter(value);
        }
        //
        // Illegal data type
        //
        throw new IllegalArgumentException(String.valueOf(value) + " not supported");
    }

}
