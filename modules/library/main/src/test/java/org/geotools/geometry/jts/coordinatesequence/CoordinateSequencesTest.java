package org.geotools.geometry.jts.coordinatesequence;

import static org.junit.Assert.assertEquals;

import org.geotools.geometry.jts.LiteCoordinateSequence;
import org.geotools.geometry.jts.LiteCoordinateSequenceFactory;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

public class CoordinateSequencesTest {

    static GeometryFactory gf = new GeometryFactory();

    static LiteCoordinateSequenceFactory liteCSF = new LiteCoordinateSequenceFactory();

    static GeometryFactory liteGF = new GeometryFactory(liteCSF);

    @Test
    public void testCoordinateDimensionPointLite2D() {
        Geometry geom = liteGF.createPoint(new LiteCoordinateSequence(new double[] { 1, 2 }, 2));
        assertEquals(2, CoordinateSequences.coordinateDimension(geom));
    }

    @Test
    public void testCoordinateDimensionPointLite3D() {
        Geometry geom = liteGF
                .createPoint(new LiteCoordinateSequence(new double[] { 1, 2, 99 }, 3));
        assertEquals(3, CoordinateSequences.coordinateDimension(geom));
    }

    @Test
    public void testCoordinateDimensionLineString2D() {
        Geometry geom = gf.createLineString(new Coordinate[] { new Coordinate(1, 2),
                new Coordinate(3, 4) });
        assertEquals(2, CoordinateSequences.coordinateDimension(geom));
    }

    @Test
    public void testCoordinateDimensionLineStringLite3D() {
        Geometry geom = liteGF.createLineString(liteCSF.create(
                new double[] { 1, 2, 100, 3, 4, 200 }, 3));
        assertEquals(3, CoordinateSequences.coordinateDimension(geom));
    }

    @Test
    public void testCoordinateDimensionPolygonLite2D() {
        Geometry geom = liteGF.createPolygon(liteGF.createLinearRing(liteCSF.create(new double[] {
                1, 1, 2, 1, 2, 2, 1, 2, 1, 1 }, 2)), null);
        assertEquals(2, CoordinateSequences.coordinateDimension(geom));
    }

    @Test
    public void testCoordinateDimensionPolygonLite3D() {
        Geometry geom = liteGF.createPolygon(
                liteGF.createLinearRing(liteCSF.create(new double[] { 1, 1, 100, 2, 1, 99, 2, 2,
                        98, 1, 2, 97, 1, 1, 100 }, 3)), null);
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
        Geometry geom = liteGF.createPolygon(
                liteGF.createLinearRing(liteCSF.create(new double[0], 2)), null);
        assertEquals(2, CoordinateSequences.coordinateDimension(geom));
    }

    @Test
    public void testCoordinateDimensionPolygonEmptyLite3D() {
        Geometry geom = liteGF.createPolygon(
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

}
