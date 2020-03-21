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
package org.geotools.geometry.iso.aggregate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import junit.framework.TestCase;
import org.geotools.geometry.iso.PositionFactoryImpl;
import org.geotools.geometry.iso.PrecisionModel;
import org.geotools.geometry.iso.complex.ComplexFactoryImpl;
import org.geotools.geometry.iso.coordinate.GeometryFactoryImpl;
import org.geotools.geometry.iso.io.CollectionFactoryMemoryImpl;
import org.geotools.geometry.iso.primitive.PrimitiveFactoryImpl;
import org.geotools.geometry.iso.primitive.SurfaceBoundaryImpl;
import org.geotools.geometry.iso.util.elem2D.Geo2DFactory;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.PositionFactory;
import org.opengis.geometry.Precision;
import org.opengis.geometry.aggregate.AggregateFactory;
import org.opengis.geometry.primitive.OrientableSurface;
import org.opengis.geometry.primitive.PrimitiveFactory;
import org.opengis.geometry.primitive.Ring;
import org.opengis.geometry.primitive.Surface;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

public class PicoMultiSurfaceTest extends TestCase {

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

    public void testMultiSurface() {

        CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
        PicoContainer container = container(crs); // normal 2D
        PositionFactoryImpl pf =
                (PositionFactoryImpl) container.getComponentInstanceOfType(PositionFactory.class);
        PrimitiveFactoryImpl primf =
                (PrimitiveFactoryImpl) container.getComponentInstanceOfType(PrimitiveFactory.class);
        AggregateFactoryImpl agf =
                (AggregateFactoryImpl) container.getComponentInstanceOfType(AggregateFactory.class);

        List<DirectPosition> directPositionList = new ArrayList<DirectPosition>();
        directPositionList.add(pf.createDirectPosition(new double[] {20, 10}));
        directPositionList.add(pf.createDirectPosition(new double[] {40, 10}));
        directPositionList.add(pf.createDirectPosition(new double[] {50, 40}));
        directPositionList.add(pf.createDirectPosition(new double[] {30, 50}));
        directPositionList.add(pf.createDirectPosition(new double[] {10, 30}));
        directPositionList.add(pf.createDirectPosition(new double[] {20, 10}));

        Ring exteriorRing = primf.createRingByDirectPositions(directPositionList);
        List<Ring> interiors = new ArrayList<Ring>();

        SurfaceBoundaryImpl surfaceBoundary1 = primf.createSurfaceBoundary(exteriorRing, interiors);
        Surface surface = primf.createSurface(surfaceBoundary1);

        Set<OrientableSurface> surfaces = new HashSet<OrientableSurface>();
        surfaces.add(surface);
        MultiSurfaceImpl ms = (MultiSurfaceImpl) agf.createMultiSurface(surfaces);
        // System.out.println(ms);
        // System.out.println(ms.getBoundary());
        // assertNotNull(ms.getBoundary());

        // test equals
        assertTrue(ms.equals(new MultiSurfaceImpl(ms.getCoordinateReferenceSystem(), surfaces)));
    }
}
