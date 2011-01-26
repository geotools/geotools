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

import junit.framework.TestCase;

import org.geotools.geometry.iso.PositionFactoryImpl;
import org.geotools.geometry.iso.PrecisionModel;
import org.geotools.geometry.iso.aggregate.AggregateFactoryImpl;
import org.geotools.geometry.iso.complex.ComplexFactoryImpl;
import org.geotools.geometry.iso.coordinate.DirectPositionImpl;
import org.geotools.geometry.iso.coordinate.GeometryFactoryImpl;
import org.geotools.geometry.iso.io.CollectionFactoryMemoryImpl;
import org.geotools.geometry.iso.primitive.PrimitiveImpl;
import org.geotools.geometry.iso.util.elem2D.Geo2DFactory;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.AbstractSingleCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.PositionFactory;
import org.opengis.geometry.Precision;
import org.opengis.geometry.coordinate.GeometryFactory;
import org.opengis.geometry.coordinate.Position;
import org.opengis.geometry.primitive.Point;
import org.opengis.geometry.primitive.PrimitiveFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

public class PrimitiveFactoryTest extends TestCase {

/*
	public void test2o5() throws Exception  {
		
		String wkt = "GEOGCS[\"WGS84\","+ 
	"  DATUM[\"WGS84\", "+
    "SPHEROID[\"WGS84\", 6378137.0, 298.257223563]],"+ 
  "PRIMEM[\"Greenwich\", 0.0], "+
  "UNIT[\"degree\", 0.017453292519943295],"+ 
  "AXIS[\"Geodetic longitude\", EAST], "+
  "AXIS[\"Fred\", OTHER], "+
  "AXIS[\"Geodetic latitude\", NORTH]]";
		CoordinateReferenceSystem crs = CRS.parseWKT(wkt);
		
	}
*/	
	
	public void testMain() {
		
		//FeatGeomFactoryImpl tGeomFactory2o5D = FeatGeomFactoryImpl.getDefault2o5D();
		//FeatGeomFactoryImpl tGeomFactory3D = FeatGeomFactoryImpl.getDefault3D();
		
		PicoContainer c2D = container(DefaultGeographicCRS.WGS84 );
		assertNotNull(c2D);
		PicoContainer c2o5D = container(DefaultGeographicCRS.WGS84_3D );
		assertNotNull(c2o5D);
		PicoContainer c3D = container( DefaultGeographicCRS.WGS84_3D );
		assertNotNull(c3D);
		
		this._testPrimitiveObjects2D(c2D);
		this._testPrimitiveObjects2o5D(c2o5D);
		this._testPrimitiveObjects3D(c3D);
		
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
	
	private void _testPrimitiveObjects2D(PicoContainer c) {
		
		PrimitiveFactoryImpl pf = (PrimitiveFactoryImpl) c.getComponentInstanceOfType(PrimitiveFactory.class);
		GeometryFactory cf = (GeometryFactoryImpl)c.getComponentInstanceOfType(GeometryFactory.class);
		PositionFactoryImpl posf = (PositionFactoryImpl) c.getComponentInstanceOfType(PositionFactory.class);
		//PrimitiveFactory pf = aFactory.getPrimitiveFactory();
		//GeometryFactory cf = aFactory.getGeometryFactoryImpl();
		
		// public PrimitiveImpl createPrimitive(Envelope envelope);
		// indirect: public SurfaceImpl createSurface(SurfaceBoundary boundary);
		// indirect: public SurfaceImpl createSurfaceByDirectPositions(List<DirectPosition> positions);
		// indirect: public SurfaceBoundaryImpl createSurfaceBoundary(Ring exterior, List<Ring> interiors);
		// indirect: public Ring createRingByDirectPositions(List<DirectPosition> directPositions);
		DirectPosition dp1 = posf.createDirectPosition(new double[]{10, 10});
		DirectPosition dp2 = posf.createDirectPosition(new double[]{70, 30});
		Envelope env1 = cf.createEnvelope(dp1, dp2);
		PrimitiveImpl prim1 = (PrimitiveImpl) pf.createPrimitive(env1);
		assertNotNull(prim1);
		
	}
	
	private void _testPrimitiveObjects2o5D(PicoContainer c) {
		
		PrimitiveFactoryImpl pf = (PrimitiveFactoryImpl) c.getComponentInstanceOfType(PrimitiveFactory.class);
		GeometryFactory cf = (GeometryFactoryImpl)c.getComponentInstanceOfType(GeometryFactory.class);
		PositionFactoryImpl posf = (PositionFactoryImpl) c.getComponentInstanceOfType(PositionFactory.class);
		//PrimitiveFactory pf = aFactory.getPrimitiveFactory();
		//GeometryFactory cf = aFactory.getGeometryFactoryImpl();
		
		// public PrimitiveImpl createPrimitive(Envelope envelope);
		// indirect: public SurfaceImpl createSurface(SurfaceBoundary boundary);
		// indirect: public SurfaceImpl createSurfaceByDirectPositions(List<DirectPosition> positions);
		// indirect: public SurfaceBoundaryImpl createSurfaceBoundary(Ring exterior, List<Ring> interiors);
		// indirect: public Ring createRingByDirectPositions(List<DirectPosition> directPositions);
		
		// 2.5D (and 3D) is not supported for envelope creation yet, so skip that test
//		DirectPosition dp1 = posf.createDirectPosition(new double[]{10, 10, 10});
//		DirectPosition dp2 = posf.createDirectPosition(new double[]{70, 30, 90});
//		Envelope env1 = cf.createEnvelope(dp1, dp2);
//		PrimitiveImpl prim1 = (PrimitiveImpl) pf.createPrimitive(env1);
//		assertNotNull(prim1);
		
		// test 2.5D point creation
		double[] da = new double[3];
		da[0] = 10.0;
		da[1] = -115000.0;
		da[2] = 0.0000000125;
		Point p1 = pf.createPoint(da);
		assertTrue(p1.getPosition().getOrdinate(0) == 10.0);
		assertTrue(p1.getPosition().getOrdinate(1) == -115000.0);
		assertTrue(p1.getPosition().getOrdinate(2) == 0.0000000125);

		// public PointImpl createPoint(Position position);
		// public PointImpl createPoint(DirectPositionImpl dp);
		da[0] = 999999999.0;
		da[1] = 100.0;
		da[2] = -0.00000565;
		Position pos1 = new DirectPositionImpl( pf.getCoordinateReferenceSystem(),  da );
		Point p2 = pf.createPoint(pos1);
		assertTrue(p2.getPosition().getOrdinate(0) == 999999999.0);
		assertTrue(p2.getPosition().getOrdinate(1) == 100.0);
		assertTrue(p2.getPosition().getOrdinate(2) == -0.00000565);
		
	}


	private void _testPrimitiveObjects3D(PicoContainer c) {
		
		PrimitiveFactory pf = (PrimitiveFactoryImpl) c.getComponentInstanceOfType(PrimitiveFactory.class);
		//PrimitiveFactory pf = c3d.getPrimitiveFactory();
		//GeometryFactory cf = c3d.getGeometryFactoryImpl();
		
		// public PointImpl createPoint(double[] coord);
		double[] da = new double[3];
		da[0] = 10.0;
		da[1] = -115000.0;
		da[2] = 0.0000000125;
		Point p1 = pf.createPoint(da);
		assertTrue(p1.getPosition().getOrdinate(0) == 10.0);
		assertTrue(p1.getPosition().getOrdinate(1) == -115000.0);
		assertTrue(p1.getPosition().getOrdinate(2) == 0.0000000125);

		// public PointImpl createPoint(Position position);
		// public PointImpl createPoint(DirectPositionImpl dp);
		da[0] = 999999999.0;
		da[1] = 100.0;
		da[2] = -0.00000565;
		Position pos1 = new DirectPositionImpl( pf.getCoordinateReferenceSystem(),  da );
		Point p2 = pf.createPoint(pos1);
		assertTrue(p2.getPosition().getOrdinate(0) == 999999999.0);
		assertTrue(p2.getPosition().getOrdinate(1) == 100.0);
		assertTrue(p2.getPosition().getOrdinate(2) == -0.00000565);

	}

}
