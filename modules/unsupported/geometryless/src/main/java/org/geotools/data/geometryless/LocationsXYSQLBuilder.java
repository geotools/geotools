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

import org.geotools.data.geometryless.filter.SQLEncoderLocationsXY;
import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.geotools.data.sql.BypassSqlFeatureTypeHandler;
import org.geotools.data.sql.BypassSqlSQLBuilder;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;

// import org.geotools.filter.SQLEncoder;

/**
 * A an extension of DefaultSQLBuilder, which supports point geometries that are
 * specified with x,y columns
 * 
 * @author Chris Holmes, TOPP
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/unsupported/geometryless/src/main/java/org/geotools/data/geometryless/LocationsXYSQLBuilder.java $
 * @version $Id: LocationsXYSQLBuilder.java 25031 2007-04-05 09:52:31Z
 *          robatkinson $
 */
public class LocationsXYSQLBuilder extends BypassSqlSQLBuilder {
    /** The logger for the mysql module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.geometryless");

    private String xCoordColumnName = null;

    private String yCoordColumnName = null;

    private String geomName;

    public LocationsXYSQLBuilder(SQLEncoderLocationsXY encoder, String geomName, String x, String y,
            BypassSqlFeatureTypeHandler typeHandler) {
        super(encoder, typeHandler);
        this.geomName = geomName;
        this.xCoordColumnName = x;
        this.yCoordColumnName = y;
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
     * We may need to provide AttributeReaders with a hook so they can request a
     * wrapper function.
     * </p>
     * 
     * @param sql
     * @param mapper
     * @param attributes
     */
    public void sqlColumns(StringBuffer sql, FIDMapper mapper, AttributeDescriptor[] attributes) {
        for (int i = 0; i < mapper.getColumnCount(); i++) {
            LOGGER.finest(mapper.getColumnName(i));
            sql.append(mapper.getColumnName(i));

            if ((attributes.length > 0) || (i < (mapper.getColumnCount() - 1))) {
                sql.append(", ");
            }
        }

        for (int i = 0; i < attributes.length; i++) {
            Name colName = attributes[i].getName();

            LOGGER.finest(attributes[i].getName() + " isGeom: "
                    + (attributes[i] instanceof GeometryDescriptor));

            // Here we want the x and y columns to be requested.
            if (attributes[i] instanceof GeometryDescriptor) {

                sql.append(xCoordColumnName + ", " + yCoordColumnName);

                // "AsText(" + attributes[i].getName() + ") AS " +
                // attributes[i].getName());
            } else {
                sql.append(colName.getLocalPart());
            }

            if (i < (attributes.length - 1)) {
                sql.append(", ");
            }
        }
    }

    public void sqlGeometryColumn(StringBuffer sql, AttributeDescriptor geomAttribute) {
        if (null == super.fieldAliases) {
            sql.append(xCoordColumnName + ", " + yCoordColumnName);
        } else {
            String xSqlExpression = (String) fieldAliases.get(xCoordColumnName);
            String ySqlExpression = (String) fieldAliases.get(yCoordColumnName);

            String xfieldName = xSqlExpression;
            String yfieldName = ySqlExpression;

            if (!xCoordColumnName.equalsIgnoreCase(xSqlExpression)) {
                xfieldName += " AS " + xCoordColumnName;
            }
            if (!yCoordColumnName.equalsIgnoreCase(ySqlExpression)) {
                yfieldName += " AS " + yCoordColumnName;
            }
            sql.append(xfieldName);
            sql.append(", ");
            sql.append(yfieldName);
        }
    }
}
