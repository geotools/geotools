/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis;

import org.geotools.jdbc.JDBCDataStore;
import org.geotools.util.Version;
import org.opengis.feature.type.GeometryDescriptor;

public class GeometryColumnEncoder {

    private final boolean atLeast2_2_0;
    private final boolean stSimplifyEnabled;
    private final boolean encodeBase64;
    private final PostGISDialect dialect;

    GeometryColumnEncoder(
            Version version,
            boolean stSimplifyEnabled,
            boolean encodeBase64,
            PostGISDialect dialect) {
        // version should not be null in normal usage, as it's set when SQLDialect
        // initializeConnection is called, however, let's cover all the bases and consider
        // possible usage outside JDBCDataStore (or unforeseens usages inside of it)
        this.atLeast2_2_0 = version != null && version.compareTo(PostGISDialect.V_2_2_0) >= 0;
        this.stSimplifyEnabled = stSimplifyEnabled;
        this.encodeBase64 = encodeBase64;
        this.dialect = dialect;
    }

    public void encode(
            GeometryDescriptor gatt,
            String prefix,
            StringBuffer sql,
            boolean force2D,
            Double distance) {

        if (encodeBase64) {
            sql.append("encode(");
        }

        if (distance == null) {
            encodeNotSimplified(gatt, prefix, sql, force2D);
        } else {
            encodeSimplified(gatt, prefix, sql, force2D, distance);
        }

        if (encodeBase64) {
            sql.append(", 'base64')");
        }
    }

    private void encodeNotSimplified(
            GeometryDescriptor gatt, String prefix, StringBuffer sql, boolean force2D) {

        boolean geography =
                "geography".equals(gatt.getUserData().get(JDBCDataStore.JDBC_NATIVE_TYPENAME));
        if (geography) {
            encodeGeography(gatt, prefix, sql);
        } else {
            if (force2D) {
                sql.append("ST_AsBinary(");
                dialect.encodeColumnName(prefix, gatt.getLocalName(), sql);
                sql.append(")");
            } else {
                sql.append("ST_AsEWKB(");
                dialect.encodeColumnName(prefix, gatt.getLocalName(), sql);
                sql.append(")");
            }
        }
    }

    private void encodeGeography(GeometryDescriptor gatt, String prefix, StringBuffer sql) {
        sql.append("ST_AsBinary(");
        dialect.encodeColumnName(prefix, gatt.getLocalName(), sql);
        sql.append(")");
    }

    private void encodeSimplified(
            GeometryDescriptor gatt,
            String prefix,
            StringBuffer sql,
            boolean force2D,
            double distance) {
        boolean geography =
                "geography".equals(gatt.getUserData().get(JDBCDataStore.JDBC_NATIVE_TYPENAME));

        if (geography) {
            encodeGeography(gatt, prefix, sql);
            return;
        }

        if (dialect.isStraightSegmentsGeometry(gatt)) {
            if (atLeast2_2_0) {
                sql.append("ST_AsTWKB(");
                encode2DGeometry(gatt, prefix, sql, stSimplifyEnabled ? distance : null);
                sql.append("," + getTWKBDigits(distance) + ")");
            } else {
                sql.append("ST_AsBinary(");
                encode2DGeometry(gatt, prefix, sql, stSimplifyEnabled ? distance : null);
                sql.append(")");
            }
        } else {
            // may have curves mixed in, cannot use TWKB and need to guard ST_Simplify
            sql.append("ST_AsBinary(");
            sql.append("CASE WHEN ST_HasArc(");
            dialect.encodeColumnName(prefix, gatt.getLocalName(), sql);
            sql.append(") THEN ");
            dialect.encodeColumnName(prefix, gatt.getLocalName(), sql);
            sql.append(" ELSE ");
            encode2DGeometry(gatt, prefix, sql, distance);
            sql.append(" END)");
        }
    }

    private void encode2DGeometry(
            GeometryDescriptor gatt, String prefix, StringBuffer sql, Double distance) {
        if (distance != null) {
            sql.append("ST_Simplify(");
        }

        sql.append(dialect.getForce2DFunction() + "(");
        dialect.encodeColumnName(prefix, gatt.getLocalName(), sql);
        sql.append(")");

        if (distance != null) {
            String preserveCollapsed = atLeast2_2_0 ? ", true" : "";
            sql.append(", " + distance + preserveCollapsed + ")");
        }
    }

    /**
     * Computes the number of digits preserved by TWKB based on the magnitude of the simplification
     * distance
     */
    private int getTWKBDigits(Double distance) {
        int result = -(int) Math.floor(Math.log10(distance));
        return result;
    }
}
