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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.geotools.geometry.iso.root.GeometryImpl;
import org.geotools.geometry.iso.PositionFactoryImpl;
import org.geotools.geometry.iso.PrecisionModel;
import org.geotools.geometry.iso.aggregate.AggregateFactoryImpl;
import org.geotools.geometry.iso.complex.ComplexFactoryImpl;
import org.geotools.geometry.iso.coordinate.DirectPositionImpl;
import org.geotools.geometry.iso.coordinate.GeometryFactoryImpl;
import org.geotools.geometry.iso.coordinate.SurfacePatchImpl;
import org.geotools.geometry.iso.io.CollectionFactoryMemoryImpl;
import org.geotools.geometry.iso.util.elem2D.Geo2DFactory;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.PositionFactory;
import org.opengis.geometry.Precision;
import org.opengis.geometry.TransfiniteSet;
import org.opengis.geometry.coordinate.GeometryFactory;
import org.opengis.geometry.coordinate.LineString;
import org.opengis.geometry.coordinate.PointArray;
import org.opengis.geometry.coordinate.Position;
import org.opengis.geometry.coordinate.Triangle;
import org.opengis.geometry.primitive.Curve;
import org.opengis.geometry.primitive.CurveSegment;
import org.opengis.geometry.primitive.OrientableCurve;
import org.opengis.geometry.primitive.Point;
import org.opengis.geometry.primitive.Primitive;
import org.opengis.geometry.primitive.Ring;
import org.opengis.geometry.primitive.Surface;
import org.opengis.geometry.primitive.SurfaceBoundary;
import org.opengis.geometry.primitive.SurfacePatch;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

import junit.framework.TestCase;

public class PicoSurfaceTest extends TestCase {

	public void testMain() {
		
		CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
		PicoContainer container = container( crs ); // normal 2D
		
		GeometryFactoryImpl tGeomFactory = (GeometryFactoryImpl) container.getComponentInstanceOfType(GeometryFactory.class);
		PrimitiveFactoryImpl tPrimFactory = (PrimitiveFactoryImpl) container.getComponentInstanceOfType( PrimitiveFactoryImpl.class );
		//PositionFactory positionFactory = (PositionFactory ) container.getComponentInstanceOfType( PositionFactory.class );

		// Creates by SurfaceBoundary
		this._testSurface2(tGeomFactory, tPrimFactory);

		// Created by Patches
		this._testSurface1(tGeomFactory, tPrimFactory);
		
		// test other surface methods
		this._testOhterSurfaceMethods(tGeomFactory, tPrimFactory);
	}
	
	/**
	 * Creates a pico container that knows about all the geom factories
	 * @param crs
	 * @return container
	 */
	protected PicoContainer container( CoordinateReferenceSystem crs ){
		
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
		container.registerComponentInstance( crs );
		Precision pr = new PrecisionModel();
		container.registerComponentInstance( pr );
		
		return container;		
	}

	private List<Triangle> _testTriangle1(GeometryFactoryImpl aGeomFactory, PrimitiveFactoryImpl tPrimFactory) {

		ArrayList<double[][]> tDoubleList = new ArrayList<double[][]>();
		tDoubleList.add(new double[][]{{0,0},{100,100},{0, 100}});
		tDoubleList.add(new double[][]{{0,100},{100,100},{50,200}});
		tDoubleList.add(new double[][]{{50,200},{100,100},{150,200}});
		ArrayList<Triangle> triangleList = aGeomFactory.createTriangles(tDoubleList);
	    
		for (int i=0; i < triangleList.size(); i++) {
			Triangle triangle1 = triangleList.get(i);
			//System.out.println(triangle1);
		}
	    
	    //System.out.println(triangle1.get.getEnvelope());
	    
	    //System.out.println(triangle1.getBoundary());
	    
		return triangleList;
	    
	}

	/**
	 * Create a surface on basis of SurfacePatches (Triangles)
	 * @param aGeomFactory
	 */
	private void _testSurface1(GeometryFactoryImpl aGeomFactory, PrimitiveFactoryImpl tPrimFactory) {
		
		List<? extends SurfacePatch> triangleList = this._testTriangle1(aGeomFactory, tPrimFactory);
		
		List<SurfacePatch> surfacePatches1 = (List<SurfacePatch>)triangleList;

		Surface surface1 = tPrimFactory.createSurface(surfacePatches1);
		
		//System.out.print("\n******************* SURFACE GENERATED BY SURFACEPATCHES");
		this.testSurfaces((SurfaceImpl) surface1);
		

	}

	public Surface _testSurface2(GeometryFactoryImpl aGeomFactory, PrimitiveFactoryImpl tPrimFactory) {

		List<DirectPosition> directPositionList = new ArrayList<DirectPosition>();
		directPositionList.add(aGeomFactory.createDirectPosition(new double[] {20, 10}));
		directPositionList.add(aGeomFactory.createDirectPosition(new double[] {40, 10}));
		directPositionList.add(aGeomFactory.createDirectPosition(new double[] {50, 40}));
		directPositionList.add(aGeomFactory.createDirectPosition(new double[] {30, 50}));
		directPositionList.add(aGeomFactory.createDirectPosition(new double[] {10, 30}));
		directPositionList.add(aGeomFactory.createDirectPosition(new double[] {20, 10}));

		Ring exteriorRing = tPrimFactory.createRingByDirectPositions(directPositionList);
		List<Ring> interiors = new ArrayList<Ring>();
		
		SurfaceBoundaryImpl surfaceBoundary1 = tPrimFactory.createSurfaceBoundary(exteriorRing, interiors );
		
		Surface surface2 = tPrimFactory.createSurface(surfaceBoundary1);
		
		//System.out.print("\n******************* SURFACE GENERATED BY SURFACEBOUNDARY");

		
		this.testSurfaces((SurfaceImpl) surface2);
		
		// ***** clone()
		SurfaceImpl surface3 = null;
		try {
			surface3 = (SurfaceImpl) surface2.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		assertTrue(surface2 != surface3);
		this.testSurfaces((SurfaceImpl) surface3);
		
		// ***** getRepresentativePoint()
		double[] dp = surface2.getRepresentativePoint().getCoordinates();
		assertTrue(dp[0] == 20);
		assertTrue(dp[1] == 10);

		assertTrue(surface2.equals(surface3));
		
		return surface2;

	}
	
	
	private void testSurfaces(SurfaceImpl surface) {

		try {
			//System.out.print("\nSurface: " + surface);
		} catch (NullPointerException e) {
		}
//		System.out.print("\ngetBoundary: " + surface.getBoundary());
        assertNotNull( surface.getBoundary() );
//		System.out.print("\ngetEnvelope: " + surface.getEnvelope());
        assertNotNull( surface.getEnvelope() );
//		System.out.print("\ngetCoordinateDimension: " + surface.getCoordinateDimension());\
        assertNotNull( surface.getCoordinateDimension() );
//		System.out.print("\ngetDimension: " + surface.getDimension(null));
		assertTrue(surface.isCycle() == false);
		
	}
	
	public void _testOhterSurfaceMethods(GeometryFactoryImpl aGeomFactory, PrimitiveFactoryImpl tPrimFactory) {

		List<DirectPosition> directPositionList = new ArrayList<DirectPosition>();
		directPositionList.add(aGeomFactory.createDirectPosition(new double[] {20, 10}));
		directPositionList.add(aGeomFactory.createDirectPosition(new double[] {40, 10}));
		directPositionList.add(aGeomFactory.createDirectPosition(new double[] {50, 40}));
		directPositionList.add(aGeomFactory.createDirectPosition(new double[] {30, 50}));
		directPositionList.add(aGeomFactory.createDirectPosition(new double[] {10, 30}));
		directPositionList.add(aGeomFactory.createDirectPosition(new double[] {20, 10}));

		Ring exteriorRing = tPrimFactory.createRingByDirectPositions(directPositionList);
		List<Ring> interiors = new ArrayList<Ring>();
		
		SurfaceBoundaryImpl surfaceBoundary1 = tPrimFactory.createSurfaceBoundary(exteriorRing, interiors );
		
		SurfaceImpl surface = tPrimFactory.createSurface(surfaceBoundary1);
		
		// ***** clone()
		SurfaceImpl surface2 = null;
		SurfaceImpl surface_bak = null;
		try {
			surface2 = surface.clone();
			surface_bak = surface.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			fail();
		}
		
		// test setBoundary
		surface.setBoundary(surfaceBoundary1);
		assertTrue(surface.getBoundary().equals(surface2.getBoundary()));
		
		// test set/get Patches
		List<? extends SurfacePatch> triangleList = this._testTriangle1(aGeomFactory, tPrimFactory);
		List<SurfacePatch> surfacePatches1 = (List<SurfacePatch>)triangleList;
		surface.setPatches(surfacePatches1);
		List<? extends SurfacePatch> surfacePatches2 = surface.getPatches();
		assertTrue(surfacePatches1.equals(surfacePatches2));

		// test toString
		String toS = surface.toString();
		assertTrue(toS != null);
		assertTrue(toS.length() > 0);
		
		// test obj equals
		assertTrue(surface_bak.equals((Object) surface2));
		assertTrue(surface_bak.equals((Object) surface_bak));
		assertFalse(surface_bak.equals((Object) surfacePatches1));
		assertFalse(surface_bak.equals((Object) null));
		SurfaceImpl surface3 = tPrimFactory.createSurface(surfacePatches1);
		assertFalse(surface_bak.equals(surface3));
		
		// test some SurfaceBoundaryImpl methods now
		// test getDimension
		assertTrue(surfaceBoundary1.getDimension(null) == 1);

		// test toString
		String toS2 = surfaceBoundary1.toString();
		assertTrue(toS2 != null);
		assertTrue(toS2.length() > 0);
		
		// test obj equals
		assertTrue(surfaceBoundary1.equals((Object) tPrimFactory.createSurfaceBoundary(exteriorRing, interiors )));
		assertTrue(surfaceBoundary1.equals((Object) surfaceBoundary1));
		assertFalse(surfaceBoundary1.equals((Object) surfacePatches1));
		assertFalse(surfaceBoundary1.equals((Object) null));
		directPositionList.remove(directPositionList.size()-1);
		directPositionList.add(aGeomFactory.createDirectPosition(new double[] {15, 25}));
		directPositionList.add(aGeomFactory.createDirectPosition(new double[] {20, 10}));
		Ring exteriorRing2 = tPrimFactory.createRingByDirectPositions(directPositionList);
		SurfaceBoundaryImpl surfaceBoundary2 = tPrimFactory.createSurfaceBoundary(exteriorRing2, interiors );
		assertFalse(surfaceBoundary1.equals(surfaceBoundary2));
		
		// test more surfacepathces methods
		SurfacePatchImpl patch = (SurfacePatchImpl) surfacePatches1.get(0);
		assertNotNull(patch.getInterpolation());
                // Following is commented-out because we are supposed to have a TriangulatedSurface, not plain Surface.
                // This Surface is built above in this test case, so its seems to be something to fix in the test suite.
		//assertTrue(surface.equals(patch.getSurface()));
		assertNotNull(patch.getNumDerivativesOnBoundary());
		
		
		
//		 create a list of connected directpositions
		List<Position> dps = new ArrayList<Position>();
		dps.add(aGeomFactory.createDirectPosition( new double[] {20, 10} ));
		dps.add(aGeomFactory.createDirectPosition( new double[] {40, 10} ));
		dps.add(aGeomFactory.createDirectPosition( new double[] {50, 40} ));
		dps.add(aGeomFactory.createDirectPosition( new double[] {30, 50} ));
		dps.add(aGeomFactory.createDirectPosition( new double[] {10, 30} ));
		dps.add(aGeomFactory.createDirectPosition( new double[] {20, 10} ));

//		 create linestring from directpositions
		LineString line = aGeomFactory.createLineString(dps);

//		 create curvesegments from line
		ArrayList<CurveSegment> segs = new ArrayList<CurveSegment>();
		segs.add(line);

//		 Create list of OrientableCurves that make up the surface
		OrientableCurve curve = tPrimFactory.createCurve(segs);
		List<OrientableCurve> orientableCurves = new ArrayList<OrientableCurve>();
		orientableCurves.add(curve);

//		 create the interior ring and a list of empty interior rings (holes)
		Ring extRing = tPrimFactory.createRing(orientableCurves);
		List<Ring> intRings = new ArrayList<Ring>();

//		 create the surfaceboundary from the rings
		SurfaceBoundary sb = tPrimFactory.createSurfaceBoundary(extRing, intRings);
				
//		 create the surface
		Surface surface22 = tPrimFactory.createSurface(sb);		
		
		// get rings
		SurfaceBoundary sb2 = (SurfaceBoundary) surface22.getBoundary();
		Ring exterior = sb2.getExterior();
		List<Ring> interiors2 = sb2.getInteriors();
		Collection<? extends Primitive> extCurve = exterior.getElements();
		Iterator<? extends Primitive> iter = extCurve.iterator();
		PointArray samplePoints = null;
		while (iter.hasNext()) {
			Curve curve2 = (Curve) iter.next();
			List<? extends CurveSegment> segs2 = curve2.getSegments();
			Iterator<? extends CurveSegment> iter2 = segs2.iterator();
			while (iter2.hasNext()) {
				if (samplePoints == null) {
					samplePoints = iter2.next().getSamplePoints();
				}
				else {
					samplePoints.addAll(iter2.next().getSamplePoints());
				}
			}
		}
		
	}

}
