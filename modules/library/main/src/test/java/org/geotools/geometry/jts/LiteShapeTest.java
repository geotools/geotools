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
 *
 *    Created on 27 May 2002, 15:40
 */
package org.geotools.geometry.jts;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.TopologyException;

/** @author jamesm,iant */
public class LiteShapeTest {

    @Test
    public void testLineShape() throws TransformException, FactoryException {
        GeometryFactory geomFac = new GeometryFactory();
        LineString lineString = makeSampleLineString(geomFac, 0, 0);
        AffineTransform affineTransform = new AffineTransform();
        LiteShape lineShape = new LiteShape(lineString, affineTransform, false);

        Assert.assertFalse(lineShape.contains(0, 0));
        Assert.assertTrue(lineShape.contains(60, 60));
        Assert.assertFalse(lineShape.contains(50, 50, 10, 10));
        Assert.assertTrue(lineShape.contains(new Point(60, 60)));
        Assert.assertFalse(lineShape.contains(new Rectangle2D.Float(50, 50, 10, 10)));
        Assert.assertEquals(lineShape.getBounds2D(), new Rectangle2D.Double(50, 50, 80, 250));
        Assert.assertEquals(lineShape.getBounds(), new Rectangle(50, 50, 80, 250));
        Assert.assertTrue(lineShape.intersects(0, 0, 100, 100));
        Assert.assertTrue(lineShape.intersects(new Rectangle2D.Double(0, 0, 100, 100)));
        Assert.assertFalse(lineShape.intersects(55, 55, 3, 100));
        Assert.assertFalse(lineShape.intersects(new Rectangle2D.Double(55, 55, 3, 100)));
    }

    @Test
    public void testLineShape2() throws TransformException, FactoryException {
        GeometryFactory geomFac = new GeometryFactory();
        LineString lineString = makeSampleLineString(geomFac, 0, 0);
        MathTransform transform = ProjectiveTransform.create(new AffineTransform());
        Decimator decimator = new Decimator(transform, new Rectangle());
        LiteShape2 lineShape = new LiteShape2(lineString, transform, decimator, false);

        Assert.assertFalse(lineShape.contains(0, 0));
        Assert.assertTrue(lineShape.contains(60, 60));
        Assert.assertFalse(lineShape.contains(50, 50, 10, 10));
        Assert.assertTrue(lineShape.contains(new Point(60, 60)));
        Assert.assertFalse(lineShape.contains(new Rectangle2D.Float(50, 50, 10, 10)));
        Assert.assertEquals(lineShape.getBounds2D(), new Rectangle2D.Double(50, 50, 80, 250));
        Assert.assertEquals(lineShape.getBounds(), new Rectangle(50, 50, 80, 250));
        Assert.assertTrue(lineShape.intersects(0, 0, 100, 100));
        Assert.assertTrue(lineShape.intersects(new Rectangle2D.Double(0, 0, 100, 100)));
        Assert.assertFalse(lineShape.intersects(55, 55, 3, 100));
        Assert.assertFalse(lineShape.intersects(new Rectangle2D.Double(55, 55, 3, 100)));
    }

    @Test
    public void testPolygonShape() throws TransformException, FactoryException {
        GeometryFactory geomFac = new GeometryFactory();
        Polygon polygon = makeSamplePolygon(geomFac, 0, 0);
        LiteShape2 lineShape = new LiteShape2(
                polygon,
                ProjectiveTransform.create(new AffineTransform()),
                new Decimator(ProjectiveTransform.create(new AffineTransform()), new Rectangle()),
                false);

        Assert.assertFalse(lineShape.contains(0, 0));
        Assert.assertTrue(lineShape.contains(100, 100));
        Assert.assertFalse(lineShape.contains(50, 50, 10, 10));
        Assert.assertTrue(lineShape.contains(100, 100, 10, 10));
        Assert.assertTrue(lineShape.contains(new Point(70, 90)));
        Assert.assertFalse(lineShape.contains(new Rectangle2D.Float(50, 50, 10, 10)));
        Assert.assertEquals(lineShape.getBounds2D(), new Rectangle2D.Double(60, 70, 70, 50));
        Assert.assertEquals(lineShape.getBounds(), new Rectangle(60, 70, 70, 50));
        Assert.assertTrue(lineShape.intersects(0, 0, 100, 100));
        Assert.assertTrue(lineShape.intersects(new Rectangle2D.Double(0, 0, 100, 100)));
        Assert.assertFalse(lineShape.intersects(55, 55, 3, 100));
        Assert.assertFalse(lineShape.intersects(new Rectangle2D.Double(55, 55, 3, 100)));
    }

    @Test
    public void testCloning() throws TransformException, FactoryException {
        LiteCoordinateSequenceFactory csFac = new LiteCoordinateSequenceFactory();
        GeometryFactory geomFac = new GeometryFactory(csFac);
        CoordinateSequence cs = csFac.create(4, 2);
        cs.setOrdinate(0, 0, 10);
        cs.setOrdinate(0, 1, 10);
        cs.setOrdinate(1, 0, 12);
        cs.setOrdinate(1, 1, 12);
        cs.setOrdinate(2, 0, 14);
        cs.setOrdinate(2, 1, 12);
        cs.setOrdinate(3, 0, 30);
        cs.setOrdinate(3, 1, 10);

        LineString ls = geomFac.createLineString(cs);
        LineString copy = (LineString) ls.copy();
        new LiteShape2(
                ls, ProjectiveTransform.create(AffineTransform.getScaleInstance(10, 10)), new Decimator(4, 4), true);
        Assert.assertTrue(ls.equalsExact(copy));

        new LiteShape2(
                ls,
                ProjectiveTransform.create(AffineTransform.getScaleInstance(10, 10)),
                new Decimator(4, 4),
                true,
                false);
        Assert.assertFalse(ls.equalsExact(copy));
    }

    private LineString makeSampleLineString(final GeometryFactory geomFac, double xoff, double yoff) {
        Coordinate[] linestringCoordinates = new Coordinate[8];
        linestringCoordinates[0] = new Coordinate(50.0d + xoff, 50.0d + yoff);
        linestringCoordinates[1] = new Coordinate(60.0d + xoff, 50.0d + yoff);
        linestringCoordinates[2] = new Coordinate(60.0d + xoff, 60.0d + yoff);
        linestringCoordinates[3] = new Coordinate(70.0d + xoff, 60.0d + yoff);
        linestringCoordinates[4] = new Coordinate(70.0d + xoff, 70.0d + yoff);
        linestringCoordinates[5] = new Coordinate(80.0d + xoff, 70.0d + yoff);
        linestringCoordinates[6] = new Coordinate(80.0d + xoff, 80.0d + yoff);
        linestringCoordinates[7] = new Coordinate(130.0d + xoff, 300.0d + yoff);
        LineString line = geomFac.createLineString(linestringCoordinates);

        return line;
    }

    private org.locationtech.jts.geom.Polygon makeSamplePolygon(
            final GeometryFactory geomFac, double xoff, double yoff) {
        Coordinate[] polygonCoordinates = new Coordinate[10];
        polygonCoordinates[0] = new Coordinate(70 + xoff, 70 + yoff);
        polygonCoordinates[1] = new Coordinate(60 + xoff, 90 + yoff);
        polygonCoordinates[2] = new Coordinate(60 + xoff, 110 + yoff);
        polygonCoordinates[3] = new Coordinate(70 + xoff, 120 + yoff);
        polygonCoordinates[4] = new Coordinate(90 + xoff, 110 + yoff);
        polygonCoordinates[5] = new Coordinate(110 + xoff, 120 + yoff);
        polygonCoordinates[6] = new Coordinate(130 + xoff, 110 + yoff);
        polygonCoordinates[7] = new Coordinate(130 + xoff, 90 + yoff);
        polygonCoordinates[8] = new Coordinate(110 + xoff, 70 + yoff);
        polygonCoordinates[9] = new Coordinate(70 + xoff, 70 + yoff);
        try {
            LinearRing ring = geomFac.createLinearRing(polygonCoordinates);
            org.locationtech.jts.geom.Polygon polyg = geomFac.createPolygon(ring, null);
            return polyg;
        } catch (TopologyException te) {
            Assert.fail("Error creating sample polygon for testing " + te);
        }
        return null;
    }
}
