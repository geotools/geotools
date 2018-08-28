package org.geotools.geometry.jts.coordinatesequence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.geotools.geometry.jts.GeometryBuilder;
import org.geotools.geometry.jts.LiteCoordinateSequence;
import org.geotools.geometry.jts.LiteCoordinateSequenceFactory;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

public class CoordinateSequencesTest {

    static GeometryFactory gf = new GeometryFactory();

    static LiteCoordinateSequenceFactory liteCSF = new LiteCoordinateSequenceFactory();

    static GeometryFactory liteGF = new GeometryFactory(liteCSF);

    static GeometryBuilder geomBuilder = new GeometryBuilder();

    @Test
    public void testCoordinateDimensionPointLite1D() {
        Geometry geom = geomBuilder.point(1);
        assertEquals(1, CoordinateSequences.coordinateDimension(geom));
    }

    @Test
    public void testCoordinateDimensionPointLite2D() {
        Geometry geom = liteGF.createPoint(new LiteCoordinateSequence(new double[] {1, 2}, 2));
        assertEquals(2, CoordinateSequences.coordinateDimension(geom));
    }

    @Test
    public void testCoordinateDimensionPointLite3D() {
        Geometry geom = liteGF.createPoint(new LiteCoordinateSequence(new double[] {1, 2, 99}, 3));
        assertEquals(3, CoordinateSequences.coordinateDimension(geom));
    }

    @Test
    public void testCoordinateDimensionLineString1D() {
        Geometry geom =
                gf.createLineString(
                        new Coordinate[] {
                            new Coordinate(1, Coordinate.NULL_ORDINATE),
                            new Coordinate(3, Coordinate.NULL_ORDINATE)
                        });
        assertEquals(1, CoordinateSequences.coordinateDimension(geom));
    }

    @Test
    public void testCoordinateDimensionLineString2D() {
        Geometry geom =
                gf.createLineString(new Coordinate[] {new Coordinate(1, 2), new Coordinate(3, 4)});
        assertEquals(2, CoordinateSequences.coordinateDimension(geom));
    }

    @Test
    public void testCoordinateDimensionLineStringLite3D() {
        Geometry geom =
                liteGF.createLineString(liteCSF.create(new double[] {1, 2, 100, 3, 4, 200}, 3));
        assertEquals(3, CoordinateSequences.coordinateDimension(geom));
    }

    @Test
    public void testCoordinateDimensionPolygonLite2D() {
        Geometry geom =
                liteGF.createPolygon(
                        liteGF.createLinearRing(
                                liteCSF.create(new double[] {1, 1, 2, 1, 2, 2, 1, 2, 1, 1}, 2)),
                        null);
        assertEquals(2, CoordinateSequences.coordinateDimension(geom));
    }

    @Test
    public void testCoordinateDimensionPolygonLite3D() {
        Geometry geom =
                liteGF.createPolygon(
                        liteGF.createLinearRing(
                                liteCSF.create(
                                        new double[] {
                                            1, 1, 100, 2, 1, 99, 2, 2, 98, 1, 2, 97, 1, 1, 100
                                        },
                                        3)),
                        null);
        assertEquals(3, CoordinateSequences.coordinateDimension(geom));
    }

    @Test
    public void testCoordinateDimensionPolygonEmpty() {
        Geometry geom = gf.createPolygon(gf.createLinearRing((Coordinate[]) null), null);
        // empty geometries using CoordinateArraySequence always report 3
        assertEquals(3, CoordinateSequences.coordinateDimension(geom));
    }

    @Test
    public void testCoordinateDimensionPolygonEmptyLite2D() {
        Geometry geom =
                liteGF.createPolygon(
                        liteGF.createLinearRing(liteCSF.create(new double[0], 2)), null);
        assertEquals(2, CoordinateSequences.coordinateDimension(geom));
    }

    @Test
    public void testCoordinateDimensionPolygonEmptyLite3D() {
        Geometry geom =
                liteGF.createPolygon(
                        liteGF.createLinearRing(liteCSF.create(new double[0], 3)), null);
        assertEquals(3, CoordinateSequences.coordinateDimension(geom));
    }

    @Test
    public void testCoordinateDimensionGeometryCollectionEmptyLite3D() {
        Geometry geom = liteGF.createGeometryCollection(new Geometry[0]);
        assertEquals(3, CoordinateSequences.coordinateDimension(geom));
    }

    @Test
    public void testCoordinateDimensionGeometryCollectionEmpty() {
        Geometry geom = gf.createGeometryCollection(new Geometry[0]);
        // empty GCs have no sequences to carry dimension, so always report dim=3
        assertEquals(3, CoordinateSequences.coordinateDimension(geom));
    }

    @Test
    public void testEqualityND() {
        Geometry g1 =
                liteGF.createPolygon(
                        liteGF.createLinearRing(
                                liteCSF.create(
                                        new double[] {
                                            1, 1, 100, 2, 1, 99, 2, 2, 98, 1, 2, 97, 1, 1, 100
                                        },
                                        3)),
                        null);
        Geometry g2 =
                liteGF.createPolygon(
                        liteGF.createLinearRing(
                                liteCSF.create(new double[] {1, 1, 2, 1, 2, 2, 1, 2, 1, 1}, 2)),
                        null);
        Geometry g3 =
                liteGF.createPolygon(
                        liteGF.createLinearRing(
                                liteCSF.create(
                                        new double[] {
                                            1, 1, 200, 2, 1, 199, 2, 2, 198, 1, 2, 197, 1, 1, 200
                                        },
                                        3)),
                        null);
        Geometry g4 =
                liteGF.createPolygon(
                        liteGF.createLinearRing(
                                liteCSF.create(
                                        new double[] {
                                            1, 1, 100, 2, 1, 99, 2, 2, 98, 1, 2, 97, 1, 1, 100
                                        },
                                        3)),
                        null);
        assertTrue(CoordinateSequences.equalsND(g1, g4));
        assertFalse(CoordinateSequences.equalsND(g1, g2));
        assertFalse(CoordinateSequences.equalsND(g1, g3));
    }
}
