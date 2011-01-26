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
package org.geotools.data.mysql;

import org.geotools.data.jdbc.FilterToSQL;
import org.geotools.data.jdbc.GeoAPISQLBuilder;
import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;


/**
 * A MySQL-specific instance of GeoAPISQLBuilder, which supports MySQL 4.1's geometric
 * datatypes.
 * @author Gary Sheppard garysheppard@psu.edu
 * @author Andrea Aime aaime@users.sourceforge.net
 * @source $URL$
 */
public class MySQLSQLBuilder extends GeoAPISQLBuilder {
    private boolean wkbEnabled;

    /**
     * @deprecated please use MySQLSQLBuilder(encoder, ft)
     * @param encoder
     */
    public MySQLSQLBuilder(FilterToSQL encoder) {
        super(encoder, null, null);
    }

    public MySQLSQLBuilder(FilterToSQL encoder, SimpleFeatureType ft) {
        super(encoder, ft, null);
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
     * @param mapper
     * @param attributes
     */
    public void sqlColumns(StringBuffer sql, FIDMapper mapper, AttributeDescriptor[] attributes) {
        for (int i = 0; i < mapper.getColumnCount(); i++) {
            sql.append(mapper.getColumnName(i));

            if ((attributes.length > 0) || (i < (mapper.getColumnCount() - 1))) {
                sql.append(", ");
            }
        }

        for (int i = 0; i < attributes.length; i++) {
            String colName = attributes[i].getLocalName();

            if (attributes[i] instanceof GeometryDescriptor) {
                if(wkbEnabled)
                    sql.append("AsBinary(");
                else
                    sql.append("AsText(");
                sql.append(attributes[i].getLocalName() + ") AS "
                        + attributes[i].getLocalName());
            } else {
                sql.append(colName);
            }

            if (i < (attributes.length - 1)) {
                sql.append(", ");
            }
        }
    }
    
    /**
     * Returns true if the WKB format is used to transfer geometries, false
     * otherwise
     *
     */
    public boolean isWKBEnabled() {
        return wkbEnabled;
    }

    /**
     * If turned on, WKB will be used to transfer geometry data instead of  WKT
     *
     * @param enabled
     */
    public void setWKBEnabled(boolean enabled) {
        wkbEnabled = enabled;
    }
}
