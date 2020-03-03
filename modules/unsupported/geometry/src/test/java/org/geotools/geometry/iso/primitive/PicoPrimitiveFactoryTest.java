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
package org.geotools.geometry.iso.primitive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import junit.framework.TestCase;
import org.geotools.geometry.iso.PositionFactoryImpl;
import org.geotools.geometry.iso.PrecisionModel;
import org.geotools.geometry.iso.aggregate.AggregateFactoryImpl;
import org.geotools.geometry.iso.aggregate.MultiPrimitiveImpl;
import org.geotools.geometry.iso.complex.ComplexFactoryImpl;
import org.geotools.geometry.iso.coordinate.DirectPositionImpl;
import org.geotools.geometry.iso.coordinate.EnvelopeImpl;
import org.geotools.geometry.iso.coordinate.GeometryFactoryImpl;
import org.geotools.geometry.iso.coordinate.LineSegmentImpl;
import org.geotools.geometry.iso.io.CollectionFactoryMemoryImpl;
import org.geotools.geometry.iso.util.elem2D.Geo2DFactory;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.PositionFactory;
import org.opengis.geometry.Precision;
import org.opengis.geometry.complex.Complex;
import org.opengis.geometry.coordinate.LineSegment;
import org.opengis.geometry.primitive.Curve;
import org.opengis.geometry.primitive.CurveSegment;
import org.opengis.geometry.primitive.OrientableCurve;
import org.opengis.geometry.primitive.Primitive;
import org.opengis.geometry.primitive.PrimitiveFactory;
import org.opengis.geometry.primitive.Ring;
import org.opengis.geometry.primitive.SurfaceBoundary;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

public class PicoPrimitiveFactoryTest extends TestCase {

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

    public void testProcessBoundsToInitialSegment() {

        CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
        PicoContainer container = container(crs); // normal 2D
        PrimitiveFactoryImpl factory =
                (PrimitiveFactoryImpl)
                        container.getComponentInstanceOfType(PrimitiveFactoryImpl.class);
        PositionFactory positionFactory =
                (PositionFactory) container.getComponentInstanceOfType(PositionFactory.class);

        DirectPosition positionA = positionFactory.createDirectPosition(new double[] {10, 10});
        DirectPosition positionB = positionFactory.createDirectPosition(new double[] {70, 30});
        Envelope bounds = new EnvelopeImpl(positionA, positionB);

        LineSegmentImpl expected =
                new LineSegmentImpl(
                        crs, new double[] {10, Double.NaN}, new double[] {70, Double.NaN}, 0.0);

        LineSegment actual = factory.processBoundsToSegment(bounds);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    public void testProcessBoundsToRing() {

        CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
        PicoContainer container = container(crs); // normal 2D
        PrimitiveFactoryImpl factory =
                (PrimitiveFactoryImpl)
                        container.getComponentInstanceOfType(PrimitiveFactoryImpl.class);
        PositionFactory positionFactory =
                (PositionFactory) container.getComponentInstanceOfType(PositionFactory.class);

        DirectPosition positionA = positionFactory.createDirectPosition(new double[] {10, 10});
        DirectPosition positionB = positionFactory.createDirectPosition(new double[] {70, 30});
        Envelope bounds = new EnvelopeImpl(positionA, positionB);

        LineSegmentImpl segment =
                new LineSegmentImpl(
                        crs, new double[] {10, Double.NaN}, new double[] {70, Double.NaN}, 0.0);

        // create expected ring
        DirectPosition one = new DirectPositionImpl(segment.getStartPoint());
        one.setOrdinate(1, bounds.getMinimum(1));

        DirectPosition two = new DirectPositionImpl(segment.getEndPoint());
        two.setOrdinate(1, bounds.getMinimum(1));

        DirectPosition three = new DirectPositionImpl(two);
        three.setOrdinate(1, bounds.getMaximum(1));

        DirectPosition four = new DirectPositionImpl(one);
        four.setOrdinate(1, bounds.getMaximum(1));

        LineSegment edge1 = new LineSegmentImpl(one, two, 0.0);
        LineSegment edge2 = new LineSegmentImpl(two, three, 0.0);
        LineSegment edge3 = new LineSegmentImpl(three, four, 0.0);
        LineSegment edge4 = new LineSegmentImpl(four, one, 0.0);

        List<OrientableCurve> edges = new ArrayList<OrientableCurve>();
        edges.add(new CurveImpl(edge1));
        edges.add(new CurveImpl(edge2));
        edges.add(new CurveImpl(edge3));
        edges.add(new CurveImpl(edge4));
        Ring expected = new RingImpl(edges);

        // create ring and test
        Ring actual = factory.processBoundsToRing(bounds, segment, 1);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    public void testCreatePrimitive() {

        CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
        PicoContainer container = container(crs); // normal 2D
        PrimitiveFactoryImpl factory =
                (PrimitiveFactoryImpl)
                        container.getComponentInstanceOfType(PrimitiveFactoryImpl.class);
        PositionFactory positionFactory =
                (PositionFactory) container.getComponentInstanceOfType(PositionFactory.class);

        DirectPosition positionA = positionFactory.createDirectPosition(new double[] {10, 10});
        DirectPosition positionB = positionFactory.createDirectPosition(new double[] {70, 30});
        Envelope bounds = new EnvelopeImpl(positionA, positionB);

        // create expected ring
        DirectPosition one = new DirectPositionImpl(positionA);
        one.setOrdinate(1, bounds.getMinimum(1));

        DirectPosition two = new DirectPositionImpl(positionB);
        two.setOrdinate(1, bounds.getMinimum(1));

        DirectPosition three = new DirectPositionImpl(two);
        three.setOrdinate(1, bounds.getMaximum(1));

        DirectPosition four = new DirectPositionImpl(one);
        four.setOrdinate(1, bounds.getMaximum(1));

        LineSegment edge1 = new LineSegmentImpl(one, two, 0.0);
        LineSegment edge2 = new LineSegmentImpl(two, three, 0.0);
        LineSegment edge3 = new LineSegmentImpl(three, four, 0.0);
        LineSegment edge4 = new LineSegmentImpl(four, one, 0.0);

        List<OrientableCurve> edges = new ArrayList<OrientableCurve>();
        edges.add(new CurveImpl(edge1));
        edges.add(new CurveImpl(edge2));
        edges.add(new CurveImpl(edge3));
        edges.add(new CurveImpl(edge4));
        Ring expectedRing = new RingImpl(edges);

        SurfaceBoundaryImpl sb = new SurfaceBoundaryImpl(crs, expectedRing, null);
        // PrimitiveImpl expected = new PrimitiveImpl(crs,sb,null); //(PrimitiveImpl) sb;

        PrimitiveImpl impl = factory.createPrimitive(bounds);
        assertNotNull(impl);

        // test equals
        SurfaceBoundary boundary =
                new SurfaceBoundaryImpl(crs, expectedRing, Collections.EMPTY_LIST);
        SurfaceImpl expected = new SurfaceImpl(boundary);
        assertEquals(expected.getBoundary(), impl.getBoundary());
        assertTrue(expected.equals(impl));

        // test get/add ContainedPrimitives
        Set<Primitive> containedPrimitives = impl.getContainedPrimitives();
        assertTrue(containedPrimitives == null);
        impl.addContainedPrimitive((PrimitiveImpl) sb.getBoundary());
        containedPrimitives = new HashSet<Primitive>();
        containedPrimitives.add((PrimitiveImpl) sb.getBoundary());
        assertTrue(containedPrimitives.equals(impl.getContainedPrimitives()));

        // test get/add ContainingPrimitives
        Set<Primitive> containingPrimitives = impl.getContainingPrimitives();
        assertTrue(containingPrimitives == null);
        impl.addContainingPrimitive((PrimitiveImpl) sb.getBoundary());
        containingPrimitives = new HashSet<Primitive>();
        containingPrimitives.add((PrimitiveImpl) sb.getBoundary());
        assertTrue(containingPrimitives.equals(impl.getContainingPrimitives()));

        // test get/add Complexes
        Set<Complex> complexes = impl.getComplexes();
        assertTrue(complexes == null);
        impl.addComplex(sb);
        complexes = new HashSet<Complex>();
        complexes.add(sb);
        assertTrue(complexes.equals(impl.getComplexes()));

        // test getMaximalComplex
        Set<Complex> maximalComplexes = impl.getMaximalComplex();
        assertTrue(maximalComplexes.toArray()[0] == sb);
    }

    public void testBoundaryEquals() {

        CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
        PicoContainer container = container(crs); // normal 2D
        PositionFactoryImpl pf =
                (PositionFactoryImpl) container.getComponentInstanceOfType(PositionFactory.class);
        PrimitiveFactoryImpl prf =
                (PrimitiveFactoryImpl) container.getComponentInstanceOfType(PrimitiveFactory.class);

        // create surfaceboundary object

        // build exterior ring
        DirectPosition one = pf.createDirectPosition(new double[] {280, 560});
        DirectPosition two = pf.createDirectPosition(new double[] {60, 180});
        DirectPosition three = pf.createDirectPosition(new double[] {720, 80});
        LineSegment edge1 = new LineSegmentImpl(one, two, 0.0);
        LineSegment edge2 = new LineSegmentImpl(two, three, 0.0);
        LineSegment edge3 = new LineSegmentImpl(three, one, 0.0);

        List<OrientableCurve> edges = new ArrayList<OrientableCurve>();
        edges.add(new CurveImpl(edge1));
        edges.add(new CurveImpl(edge2));
        edges.add(new CurveImpl(edge3));
        Ring exterior = new RingImpl(edges);

        // build interior ring
        DirectPosition one2 = pf.createDirectPosition(new double[] {420, 360});
        DirectPosition two2 = pf.createDirectPosition(new double[] {200, 360});
        DirectPosition three2 = pf.createDirectPosition(new double[] {320, 180});
        LineSegment edge1_1 = new LineSegmentImpl(one2, two2, 0.0);
        LineSegment edge2_1 = new LineSegmentImpl(two2, three2, 0.0);
        LineSegment edge3_1 = new LineSegmentImpl(three2, one2, 0.0);

        edges = new ArrayList<OrientableCurve>();
        edges.add(new CurveImpl(edge1_1));
        edges.add(new CurveImpl(edge2_1));
        edges.add(new CurveImpl(edge3_1));
        RingImpl interior = new RingImpl(edges);
        List<Ring> intrings = new ArrayList<Ring>();
        intrings.add(interior);

        SurfaceBoundaryImpl sb = new SurfaceBoundaryImpl(crs, exterior, intrings);
        // System.out.println(sb);

        // create multiprimitive object
        List<CurveSegment> curves = new ArrayList<CurveSegment>();
        curves.add(edge1);
        curves.add(edge2);
        curves.add(edge3);
        Curve s = new CurveImpl(crs, curves);

        curves = new ArrayList<CurveSegment>();
        curves.add(edge1_1);
        curves.add(edge2_1);
        curves.add(edge3_1);
        Curve s2 = new CurveImpl(crs, curves);

        Set<Primitive> primitives = new HashSet<Primitive>();
        // System.out.println(s.getBoundary());
        primitives.add(s);
        primitives.add(s2);
        MultiPrimitiveImpl mp = new MultiPrimitiveImpl(crs, primitives);
        // System.out.println(mp);
        // System.out.println(mp.getBoundary());
        // assertEquals(sb, mp.getBoundary());
    }
}
