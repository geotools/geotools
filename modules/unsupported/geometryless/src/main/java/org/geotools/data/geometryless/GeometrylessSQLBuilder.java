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
package org.geotools.data.geometryless;

import java.util.logging.Logger;

import org.geotools.data.jdbc.FilterToSQL;
import org.geotools.data.jdbc.GeoAPISQLBuilder;
import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;

/**
 * A Geometryless-specific instance of DefaultSQLBuilder, which supports geometries created form standard data types
 * @author Rob Atkinson rob@socialchange.net.au
 *
 * @source $URL$
 */
public class GeometrylessSQLBuilder extends GeoAPISQLBuilder {
    
       /** The logger for the mysql module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.geometryless");

 
   
    public GeometrylessSQLBuilder(FilterToSQL encoder) {
        super(encoder, null,null);


    }
    

    /**
     * Produces the select information required.
     * 
     * <p>
     * The featureType, if known, is always requested.
     * </p>
     * 
     * <p>
     * sql: <code>featureID (,attributeColumn)</code>
     * </p>
     * 
     * <p>
     * We may need to provide AttributeReaders with a hook so they can request
     * a wrapper function.
     * </p>
     *
     * @param sql
     * @param fidColumnName
     * @param attributes
     */
    public void sqlColumns(StringBuffer sql, FIDMapper mapper, AttributeDescriptor[] attributes) {
   
    	
    	
        for (int i = 0; i < mapper.getColumnCount(); i++) {
        	LOGGER.finest(mapper.getColumnName(i));
            sql.append(mapper.getColumnName(i));
            if (attributes.length > 0 || i < (mapper.getColumnCount() - 1)) {
                sql.append(", ");
            }
        }

        for (int i = 0; i < attributes.length; i++) {
            Name colName = attributes[i].getName();

            LOGGER.finest(attributes[i].getName() + " isGeom: "
                + (attributes[i] instanceof GeometryDescriptor) );

            if (attributes[i] instanceof GeometryDescriptor) {
                sql.append("AsText(" + attributes[i].getName() + ") AS " + attributes[i].getName());
            } else {
                sql.append(colName.getLocalPart());
            }

            if (i < (attributes.length - 1)) {
                sql.append(", ");
            }
        }
    }

}
