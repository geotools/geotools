/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.iso.coordinate;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.geotools.geometry.iso.PositionFactoryImpl;
import org.geotools.geometry.iso.PrecisionModel;
import org.geotools.geometry.iso.aggregate.AggregateFactoryImpl;
import org.geotools.geometry.iso.complex.ComplexFactoryImpl;
import org.geotools.geometry.iso.io.CollectionFactoryMemoryImpl;
import org.geotools.geometry.iso.primitive.PrimitiveFactoryImpl;
import org.geotools.geometry.iso.primitive.SurfaceBoundaryImpl;
import org.geotools.geometry.iso.util.elem2D.Geo2DFactory;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.PositionFactory;
import org.opengis.geometry.Precision;
import org.opengis.geometry.aggregate.MultiPrimitive;
import org.opengis.geometry.coordinate.GeometryFactory;
import org.opengis.geometry.coordinate.LineSegment;
import org.opengis.geometry.coordinate.Polygon;
import org.opengis.geometry.coordinate.Position;
import org.opengis.geometry.primitive.Ring;
import org.opengis.geometry.primitive.SurfaceBoundary;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

public class PicoGeometryFactoryTest extends TestCase {

    public void testMain() {

        PicoContainer c = container(DefaultGeographicCRS.WGS84_3D);

        this._testCoordinateObjects(c);
    }

    /**
     * Creates a pico container that knows about all the geom factories
     *
     * @return container
     */
    protected PicoContainer container(CoordinateReferenceSystem crs) {

        DefaultPicoContainer container = new DefaultPicoContainer(); // parent

        // Teach Container about Factory Implementations we want to use
        container.registerComponentImplementation(PositionFactoryImpl.class);
        container.registerComponentImplementation(AggregateFactoryImpl.class);
        container.registerComponentImplementation(ComplexFactoryImpl.class);
        container.registerComponentImplementation(GeometryFactoryImpl.class);
        container.registerComponentImplementation(CollectionFactoryMemoryImpl.class);
        container.registerComponentImplementation(PrimitiveFactoryImpl.class);
        container.registerComponentImplementation(Geo2DFactory.class);

        // Teach Container about other dependacies needed
        container.registerComponentInstance(crs);
        Precision pr = new PrecisionModel();
        container.registerComponentInstance(pr);

        return container;
    }

    private void _testCoordinateObjects(PicoContainer c) {

        GeometryFactoryImpl cf =
                (GeometryFactoryImpl) c.getComponentInstanceOfType(GeometryFactory.class);
        PrimitiveFactoryImpl tPrimFactory =
                (PrimitiveFactoryImpl) c.getComponentInstanceOfType(PrimitiveFactoryImpl.class);
        PositionFactory positionFactory =
                (PositionFactory) c.getComponentInstanceOfType(PositionFactory.class);

        // public DirectPositionImpl createDirectPosition();
        DirectPosition dp1 = positionFactory.createDirectPosition(null);
        assertTrue(Double.compare(dp1.getOrdinate(0), Double.NaN) == 0);
        assertTrue(Double.compare(dp1.getOrdinate(1), Double.NaN) == 0);
        assertTrue(Double.compare(dp1.getOrdinate(2), Double.NaN) == 0);

        // public DirectPositionImpl createDirectPosition(double[] coord);
        double[] da = new double[3];
        da[0] = 10.0;
        da[1] = -115000.0;
        da[2] = 0.0000000125;
        DirectPosition dp2 = positionFactory.createDirectPosition(da);
        assertTrue(dp2.getOrdinate(0) == 10.0);
        assertTrue(dp2.getOrdinate(1) == -115000.0);
        assertTrue(dp2.getOrdinate(2) == 0.0000000125);

        // public Envelope createEnvelope(
        //			DirectPosition lowerCorner,
        //			DirectPosition upperCorner)
        Envelope env1 = cf.createEnvelope(dp1, dp2);
        DirectPosition lc = env1.getLowerCorner();
        assertTrue(Double.compare(lc.getOrdinate(0), Double.NaN) == 0);
        assertTrue(Double.compare(lc.getOrdinate(1), Double.NaN) == 0);
        assertTrue(Double.compare(lc.getOrdinate(2), Double.NaN) == 0);
        DirectPosition uc = env1.getUpperCorner();
        assertTrue(uc.getOrdinate(0) == 10.0);
        assertTrue(uc.getOrdinate(1) == -115000.0);
        assertTrue(uc.getOrdinate(2) == 0.0000000125);
        env1 = cf.createEnvelope(dp2, dp1);
        lc = env1.getLowerCorner();
        assertTrue(lc.getOrdinate(0) == 10.0);
        assertTrue(lc.getOrdinate(1) == -115000.0);
        assertTrue(lc.getOrdinate(2) == 0.0000000125);
        uc = env1.getUpperCorner();
        assertTrue(Double.compare(uc.getOrdinate(0), Double.NaN) == 0);
        assertTrue(Double.compare(uc.getOrdinate(1), Double.NaN) == 0);
        assertTrue(Double.compare(uc.getOrdinate(2), Double.NaN) == 0);

        // public Position createPosition(DirectPosition dp);
        Position pos1 = cf.createPosition(dp2);
        assertTrue(pos1.getDirectPosition().getOrdinate(0) == 10.0);
        assertTrue(pos1.getDirectPosition().getOrdinate(1) == -115000.0);
        assertTrue(pos1.getDirectPosition().getOrdinate(2) == 0.0000000125);

        // public LineSegment createLineSegment(Position startPoint, Position endPoint);
        Position pos2 = cf.createPosition(dp1);
        LineSegment seg1 = cf.createLineSegment(pos1, pos2);
        assertTrue(Double.compare(seg1.getEndPoint().getOrdinate(0), Double.NaN) == 0.0);
        assertTrue(Double.compare(seg1.getEndPoint().getOrdinate(1), Double.NaN) == 0.0);
        assertTrue(Double.compare(seg1.getEndPoint().getOrdinate(2), Double.NaN) == 0.0);
        assertTrue(seg1.getStartPoint().getOrdinate(0) == 10.0);
        assertTrue(seg1.getStartPoint().getOrdinate(1) == -115000.0);
        assertTrue(seg1.getStartPoint().getOrdinate(2) == 0.0000000125);

        // test creating multiprimitive (only creates an empty obj right now)
        MultiPrimitive mp = cf.createMultiPrimitive();
        assertNotNull(mp);
        assertEquals(mp.getCoordinateReferenceSystem(), cf.getCoordinateReferenceSystem());

        // test creating polygon
        List<DirectPosition> directPositionList = new ArrayList<DirectPosition>();
        directPositionList.add(positionFactory.createDirectPosition(new double[] {20, 10, 0.0}));
        directPositionList.add(positionFactory.createDirectPosition(new double[] {40, 10, 0.0}));
        directPositionList.add(positionFactory.createDirectPosition(new double[] {50, 40, 0.0}));
        directPositionList.add(positionFactory.createDirectPosition(new double[] {30, 50, 0.0}));
        directPositionList.add(positionFactory.createDirectPosition(new double[] {10, 30, 0.0}));
        directPositionList.add(positionFactory.createDirectPosition(new double[] {20, 10, 0.0}));

        Ring exteriorRing = tPrimFactory.createRingByDirectPositions(directPositionList);
        List<Ring> interiors = new ArrayList<Ring>();

        SurfaceBoundary boundary =
                new SurfaceBoundaryImpl(cf.getCoordinateReferenceSystem(), exteriorRing, interiors);
        Polygon poly = cf.createPolygon(boundary);
        assertNotNull(poly);
        assertNotNull(poly.getBoundary());

        PolygonImpl expected = new PolygonImpl((SurfaceBoundaryImpl) boundary);
        assertEquals(poly, expected);

        // test the PolygonImpl equals
        assertTrue(expected.equals((Object) poly));
        assertTrue(expected.equals((Object) expected));
        assertFalse(expected.equals((Object) interiors));
        assertFalse(expected.equals((Object) null));
    }
}
