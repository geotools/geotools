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

import junit.framework.TestCase;
import org.geotools.geometry.iso.PositionFactoryImpl;
import org.geotools.geometry.iso.PrecisionModel;
import org.geotools.geometry.iso.aggregate.AggregateFactoryImpl;
import org.geotools.geometry.iso.coordinate.GeometryFactoryImpl;
import org.geotools.geometry.iso.io.CollectionFactoryMemoryImpl;
import org.geotools.geometry.iso.primitive.PointImpl;
import org.geotools.geometry.iso.primitive.PrimitiveFactoryImpl;
import org.geotools.geometry.iso.util.elem2D.Geo2DFactory;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.PositionFactory;
import org.opengis.geometry.Precision;
import org.opengis.geometry.complex.ComplexFactory;
import org.opengis.geometry.complex.CompositePoint;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

public class PicoCompositePointTest extends TestCase {

    public void testMain() {

        PicoContainer c = container(DefaultGeographicCRS.WGS84);

        this._testCompositePoint(c);
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

    private void _testCompositePoint(PicoContainer c) {

        PositionFactoryImpl positionFactory =
                (PositionFactoryImpl) c.getComponentInstanceOfType(PositionFactory.class);
        ComplexFactoryImpl complf =
                (ComplexFactoryImpl) c.getComponentInstanceOfType(ComplexFactory.class);

        // testcreateCompositeCurve()
        PointImpl p1 = new PointImpl(positionFactory.createDirectPosition(new double[] {50, 20}));
        PointImpl p2 = new PointImpl(positionFactory.createDirectPosition(new double[] {70, 80}));

        CompositePoint comppoint1 = complf.createCompositePoint(p1);
        CompositePoint comppoint2 = complf.createCompositePoint(p2);

        assertNotNull(comppoint1);
        assertNotNull(comppoint2);
        double[] dp = comppoint1.getEnvelope().getLowerCorner().getCoordinate();
        assertEquals(dp[0], 50.0);
        assertEquals(dp[1], 20.0);
        dp = comppoint2.getEnvelope().getLowerCorner().getCoordinate();
        assertEquals(dp[0], 70.0);
        assertEquals(dp[1], 80.0);

        assertEquals(comppoint1.getCoordinateDimension(), p1.getCoordinateDimension());
        assertEquals(comppoint2.getCoordinateDimension(), p2.getCoordinateDimension());

        // test equals
        assertTrue(comppoint1.equals(new CompositePointImpl(p1)));
    }
}
