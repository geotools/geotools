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
package org.geotools.geometry.iso.complex;

import java.util.ArrayList;
import junit.framework.TestCase;
import org.geotools.geometry.iso.PositionFactoryImpl;
import org.geotools.geometry.iso.PrecisionModel;
import org.geotools.geometry.iso.aggregate.AggregateFactoryImpl;
import org.geotools.geometry.iso.coordinate.GeometryFactoryImpl;
import org.geotools.geometry.iso.coordinate.PositionImpl;
import org.geotools.geometry.iso.io.CollectionFactoryMemoryImpl;
import org.geotools.geometry.iso.primitive.CurveImpl;
import org.geotools.geometry.iso.primitive.PrimitiveFactoryImpl;
import org.geotools.geometry.iso.util.elem2D.Geo2DFactory;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.PositionFactory;
import org.opengis.geometry.Precision;
import org.opengis.geometry.complex.ComplexFactory;
import org.opengis.geometry.complex.CompositeCurve;
import org.opengis.geometry.coordinate.GeometryFactory;
import org.opengis.geometry.coordinate.Position;
import org.opengis.geometry.primitive.CurveSegment;
import org.opengis.geometry.primitive.OrientableCurve;
import org.opengis.geometry.primitive.PrimitiveFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

public class PicoCompositeCurveTest extends TestCase {

    public void testMain() {

        PicoContainer c = container(DefaultGeographicCRS.WGS84);

        this._testCompositeCurve(c);
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

    private void _testCompositeCurve(PicoContainer c) {

        PositionFactoryImpl positionFactory =
                (PositionFactoryImpl) c.getComponentInstanceOfType(PositionFactory.class);
        ComplexFactoryImpl complf =
                (ComplexFactoryImpl) c.getComponentInstanceOfType(ComplexFactory.class);
        PrimitiveFactoryImpl pf =
                (PrimitiveFactoryImpl) c.getComponentInstanceOfType(PrimitiveFactory.class);
        GeometryFactoryImpl geomFact =
                (GeometryFactoryImpl) c.getComponentInstanceOfType(GeometryFactory.class);

        // testcreateCompositeCurve()
        ArrayList<Position> line1 = new ArrayList<Position>();
        line1.add(new PositionImpl(positionFactory.createDirectPosition(new double[] {50, 20})));
        line1.add(new PositionImpl(positionFactory.createDirectPosition(new double[] {30, 30})));
        line1.add(new PositionImpl(positionFactory.createDirectPosition(new double[] {20, 50})));
        line1.add(new PositionImpl(positionFactory.createDirectPosition(new double[] {20, 70})));

        ArrayList<Position> line2 = new ArrayList<Position>();
        line2.add(new PositionImpl(positionFactory.createDirectPosition(new double[] {20, 70})));
        line2.add(new PositionImpl(positionFactory.createDirectPosition(new double[] {40, 80})));
        line2.add(new PositionImpl(positionFactory.createDirectPosition(new double[] {70, 80})));

        ArrayList<Position> line3 = new ArrayList<Position>();
        line3.add(new PositionImpl(positionFactory.createDirectPosition(new double[] {70, 80})));
        line3.add(new PositionImpl(positionFactory.createDirectPosition(new double[] {90, 70})));
        line3.add(new PositionImpl(positionFactory.createDirectPosition(new double[] {100, 60})));
        line3.add(new PositionImpl(positionFactory.createDirectPosition(new double[] {100, 40})));

        ArrayList<Position> line4 = new ArrayList<Position>();
        line4.add(new PositionImpl(positionFactory.createDirectPosition(new double[] {100, 40})));
        line4.add(new PositionImpl(positionFactory.createDirectPosition(new double[] {80, 30})));
        line4.add(new PositionImpl(positionFactory.createDirectPosition(new double[] {50, 20})));
        ArrayList<CurveSegment> tLineList1 = new ArrayList<CurveSegment>();
        tLineList1.add(geomFact.createLineString(line1));
        tLineList1.add(geomFact.createLineString(line2));

        ArrayList<CurveSegment> tLineList2 = new ArrayList<CurveSegment>();
        tLineList2.add(geomFact.createLineString(line3));
        tLineList2.add(geomFact.createLineString(line4));
        CurveImpl curve1 = pf.createCurve(tLineList1);
        CurveImpl curve2 = pf.createCurve(tLineList2);
        ArrayList<OrientableCurve> curveList = new ArrayList<OrientableCurve>();
        curveList.add(curve1);
        curveList.add(curve2);
        CompositeCurve compcurve = complf.createCompositeCurve(curveList);

        assertNotNull(compcurve);
        double[] dp = compcurve.getEnvelope().getLowerCorner().getCoordinate();
        assertEquals(dp[0], 20.0);
        assertEquals(dp[1], 20.0);
        dp = compcurve.getEnvelope().getUpperCorner().getCoordinate();
        assertEquals(dp[0], 100.0);
        assertEquals(dp[1], 80.0);

        // test equals
        assertTrue(compcurve.equals(new CompositeCurveImpl(curveList)));
    }
}
