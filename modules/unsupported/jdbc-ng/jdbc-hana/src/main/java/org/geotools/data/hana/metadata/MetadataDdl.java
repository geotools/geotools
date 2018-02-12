/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.hana.metadata;

import org.geotools.data.hana.HanaUtil;

/**
 * Helper class to create DDL statements for creating unit-of-measures and spatial reference
 * systems.
 *
 * @author Stefan Uhrig, SAP SE
 */
public final class MetadataDdl {

    public static String getUomDdl(Uom uom) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE SPATIAL UNIT OF MEASURE ");
        sql.append(HanaUtil.encodeIdentifier(uom.getName()));
        sql.append(" TYPE ");
        switch (uom.getType()) {
            case LINEAR:
                sql.append("LINEAR");
                break;
            case ANGULAR:
                sql.append("ANGULAR");
                break;
        }
        sql.append(" CONVERT USING ");
        sql.append(Double.toString(uom.getFactor()));
        return sql.toString();
    }

    public static String getSrsDdl(Srs srs) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE SPATIAL REFERENCE SYSTEM ");
        sql.append(HanaUtil.encodeIdentifier(srs.getName()));
        sql.append(" IDENTIFIED BY ");
        sql.append(Integer.toString(srs.getSrid()));
        sql.append(" ORGANIZATION ");
        sql.append(HanaUtil.encodeIdentifier(srs.getOrganization()));
        sql.append(" IDENTIFIED BY ");
        sql.append(srs.getOrganizationId());
        String wkt = srs.getWkt();
        if (wkt != null) {
            sql.append(" DEFINITION ");
            sql.append(HanaUtil.toStringLiteral(wkt));
        }
        String proj4 = srs.getProj4();
        if (proj4 != null) {
            sql.append(" TRANSFORM DEFINITION ");
            sql.append(HanaUtil.toStringLiteral(proj4));
        }
        sql.append(" LINEAR UNIT OF MEASURE ");
        sql.append(HanaUtil.encodeIdentifier(srs.getLinearUom()));
        String angularUom = srs.getAngularUom();
        if (angularUom != null) {
            sql.append(" ANGULAR UNIT OF MEASURE ");
            sql.append(HanaUtil.encodeIdentifier(angularUom));
        }
        sql.append(" TYPE ");
        switch (srs.getType()) {
            case PROJECTED:
            case FLAT:
                sql.append("PLANAR");
                break;
            case GEOGRAPHIC:
                sql.append("ROUND EARTH");
                break;
        }
        if (srs.getMajorAxis() != null) {
            sql.append(" ELLIPSOID SEMI MAJOR AXIS ");
            sql.append(Double.toString(srs.getMajorAxis()));
            if (srs.getMinorAxis() != null) {
                sql.append(" SEMI MINOR AXIS ");
                sql.append(Double.toString(srs.getMinorAxis()));
            }
            if (srs.getInverseFlattening() != null) {
                sql.append(" INVERSE FLATTENING ");
                sql.append(Double.toString(srs.getInverseFlattening()));
            }
        }
        sql.append(" COORDINATE ");
        sql.append(srs.getType() == Srs.Type.PROJECTED ? "X" : "LONGITUDE");
        sql.append(" BETWEEN ");
        sql.append(Double.toString(srs.getMinX()));
        sql.append(" AND ");
        sql.append(Double.toString(srs.getMaxX()));
        sql.append(" COORDINATE ");
        sql.append(srs.getType() == Srs.Type.PROJECTED ? "Y" : "LATITUDE");
        sql.append(" BETWEEN ");
        sql.append(Double.toString(srs.getMinY()));
        sql.append(" AND ");
        sql.append(Double.toString(srs.getMaxY()));
        sql.append(" TOLERANCE 0 ");
        sql.append(" SNAP TO GRID 0 ");
        sql.append(" POLYGON FORMAT 'EvenOdd' ");
        sql.append(" STORAGE FORMAT ");
        sql.append(
                HanaUtil.toStringLiteral(
                        srs.getType() == Srs.Type.GEOGRAPHIC ? "Mixed" : "Internal"));
        return sql.toString();
    }

    private MetadataDdl() {}
}
