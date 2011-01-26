/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2009, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.geometry.jts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;

/**
 *
 * @author Michael Bedward
 * @since 2.6
 * @source $URL$
 * @version $Id$
 */
public class GeometriesTest {

    private static GeometryFactory geomFactory = JTSFactoryFinder.getGeometryFactory(null);

    private static final Coordinate[] coords = {
            new Coordinate(0, 0),
            new Coordinate(0, 10),
            new Coordinate(10, 10),
            new Coordinate(10, 0),
            new Coordinate(0, 0)
        };

    @Test
    public void testGetBinding() {
        System.out.println("   getBinding");

        assertEquals(Point.class, Geometries.POINT.getBinding());
        assertEquals(MultiPoint.class, Geometries.MULTIPOINT.getBinding());
        assertEquals(LineString.class, Geometries.LINESTRING.getBinding());
        assertEquals(MultiLineString.class, Geometries.MULTILINESTRING.getBinding());
        assertEquals(Polygon.class, Geometries.POLYGON.getBinding());
        assertEquals(MultiPolygon.class, Geometries.MULTIPOLYGON.getBinding());
        assertEquals(Geometry.class, Geometries.GEOMETRY.getBinding());
        assertEquals(GeometryCollection.class, Geometries.GEOMETRYCOLLECTION.getBinding());
    }

    @Test
    public void testGetByObject() {
        System.out.println("   get (by object)");

        Geometry point = geomFactory.createPoint(coords[0]);
        assertEquals(Geometries.POINT, Geometries.get(point));

        Geometry multiPoint = geomFactory.createMultiPoint(coords);
        assertEquals(Geometries.MULTIPOINT, Geometries.get(multiPoint));

        Geometry line = geomFactory.createLineString(coords);
        assertEquals(Geometries.LINESTRING, Geometries.get(line));

        LineString[] lines = {
            geomFactory.createLineString(new Coordinate[]{coords[0], coords[1]}),
            geomFactory.createLineString(new Coordinate[]{coords[2], coords[3]})
        };
        Geometry multiLine = geomFactory.createMultiLineString(lines);
        assertEquals(Geometries.MULTILINESTRING, Geometries.get(multiLine));

        Polygon poly = geomFactory.createPolygon(geomFactory.createLinearRing(coords), null);
        assertEquals(Geometries.POLYGON, Geometries.get(poly));

        Polygon[] polys = {poly, poly};
        Geometry multiPoly = geomFactory.createMultiPolygon(polys);
        assertEquals(Geometries.MULTIPOLYGON, Geometries.get(multiPoly));

        Geometry gc = geomFactory.createGeometryCollection(polys);
        assertEquals(Geometries.GEOMETRYCOLLECTION, Geometries.get(gc));
    }

    @Test
    public void testGetSubclass() {
        class DerivedLine extends LineString {
            DerivedLine(CoordinateSequence seq, GeometryFactory gf) {
                super(seq, gf);
            }
        }
        assertEquals(Geometries.LINESTRING, Geometries.getForBinding(DerivedLine.class));

        abstract class DerivedGeometry extends Geometry {
            DerivedGeometry(GeometryFactory gf) {
                super(gf);
            }
        }
        assertEquals(Geometries.GEOMETRY, Geometries.getForBinding(DerivedGeometry.class));
    }

    @Test
    public void testGetSubclassByObject() {
        class DerivedLine extends LineString {
            DerivedLine(CoordinateSequence seq, GeometryFactory gf) {
                super(seq, gf);
            }
        }

        DerivedLine p = new DerivedLine(new CoordinateArraySequence(coords), geomFactory);
        assertEquals(Geometries.LINESTRING, Geometries.getForBinding(DerivedLine.class));
    }

    /**
     * Tests getName and getSimpleName
     */
    @Test
    public void testGetName() {
        System.out.println("   getName and getSimpleName");

        for (Geometries type : Geometries.values()) {
            String className = type.getBinding().getSimpleName();
            assertTrue(type.getName().equalsIgnoreCase(className));

            if (className.startsWith("Multi")) {
                assertTrue(type.getSimpleName().equalsIgnoreCase(className.substring(5)));
            } else {
                assertTrue(type.getSimpleName().equalsIgnoreCase(className));
            }
        }
    }

    @Test
    public void testGetForSQLType() {
        System.out.println("   getSQLType and getForSQLType");
        for (Geometries type : Geometries.values()) {
            int sqlType = type.getSQLType();
            assertEquals(type, Geometries.getForSQLType(sqlType));
        }
    }

}