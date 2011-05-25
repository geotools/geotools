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

import java.io.IOException;

import org.geotools.data.FeatureReader;
import org.geotools.data.jdbc.JDBCTextFeatureWriter;
import org.geotools.data.jdbc.QueryData;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTWriter;


/**
 * Feature writer handling specific geometric function from MySQL 4.1
 *
 * TODO This ought to handle MySQL 4.1's geometric datatypes, but it does not work.
 * This is because 4.1 sends geometric data in a different packet format than other
 * datatypes, and because of this the MySQL driver does not allow ResultSet objects
 * with geometric data to be updatable.  I have not found anything about this MySQL
 * bug in the MySQL bug database, so I will add a new bug there.  In the meantime,
 * this package should work fine for writing non-geometric data.
 * @author Gary Sheppard garysheppard@psu.edu
 *
 * @author wolf
 * @author Gary Sheppard garysheppard@psu.edu
 *
 * @source $URL$
 */
public class MySQLFeatureWriter extends JDBCTextFeatureWriter {
    private static WKTWriter geometryWriter = new WKTWriter();

    public MySQLFeatureWriter(FeatureReader<SimpleFeatureType, SimpleFeature> fReader, QueryData queryData)
        throws IOException {
        super(fReader, queryData);
    }

    /**
     * @see org.geotools.data.jdbc.JDBCTextFeatureWriter#getGeometryInsertText(com.vividsolutions.jts.geom.Geometry,
     *      int)
     */
    protected String getGeometryInsertText(Geometry geom, int srid) {
        if (geom == null) {
            return "NULL";
        }

        String geoText = geometryWriter.write(geom);
        String sql = null;

        if (GeometryCollection.class.isAssignableFrom(geom.getClass())) {
            if (MultiPoint.class.isAssignableFrom(geom.getClass())) {
                sql = "MultiPointFromText";
            } else if (MultiLineString.class.isAssignableFrom(geom.getClass())) {
                sql = "MultiLineStringFromText";
            } else if (MultiPolygon.class.isAssignableFrom(geom.getClass())) {
                sql = "MultiPolygonFromText";
            } else {
                sql = "GeometryCollectionFromText";
            }
        } else {
            if (Point.class.isAssignableFrom(geom.getClass())) {
                sql = "PointFromText";
            } else if (LineString.class.isAssignableFrom(geom.getClass())) {
                sql = "LineStringFromText";
            } else if (Polygon.class.isAssignableFrom(geom.getClass())) {
                sql = "PolygonFromText";
            } else {
                sql = "GeometryFromText";
            }
        }

        sql += ("('" + geoText + "', " + srid + ")");

        return sql;
    }
}
