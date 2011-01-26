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
 */
package org.geotools.data.shapefile.shp;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.net.URL;

import junit.framework.TestCase;

import org.geotools.data.Query;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.shapefile.ShapefileRendererUtil;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.matrix.GeneralMatrix;
import org.geotools.referencing.operation.transform.IdentityTransform;
import org.geotools.renderer.lite.RendererUtilities;
import org.geotools.renderer.shape.LabelingTest;
import org.geotools.renderer.shape.ShapefileRenderer;
import org.geotools.renderer.shape.TestUtilites;
import org.geotools.styling.Style;
import org.geotools.test.TestData;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Tests multilinehandler class
 * 
 * @author jeichar
 * @since 2.1.x
 * @source $URL$
 */
public class JTSMultiLineHandlerTest extends TestCase {

	private static final boolean INTERACTIVE = false;

	public void testRead() throws Exception{
		URL url=TestData.getResource(LabelingTest.class, "streams.shp");
		ShapefileDataStore ds=(ShapefileDataStore) new ShapefileDataStoreFactory().createDataStore(url);
		
		Envelope env=ds.getFeatureSource().getBounds();
		MathTransform mt=null;
		AffineTransform transform = RendererUtilities.worldToScreenTransform(env, new Rectangle(512,512));
		MathTransform at = ReferencingFactoryFinder.getMathTransformFactory(null)
				.createAffineTransform(new GeneralMatrix(transform));
		if (mt == null) {
			mt = at;
		} else {
			mt = ReferencingFactoryFinder.getMathTransformFactory(null)
					.createConcatenatedTransform(mt, at);
		}

		ShapefileReader reader=new ShapefileReader(ShapefileRendererUtil.getShpFiles(ds), false, false);
		reader.setHandler(new org.geotools.renderer.shape.shapehandler.jts.MultiLineHandler(reader.getHeader().getShapeType(), env, 
                        mt, false, new Rectangle(512,512) ));
		Object shape=reader.nextRecord().shape();
		assertNotNull( shape );
		assertTrue( shape instanceof Geometry);
                Coordinate[] coords = ((Geometry)shape).getCoordinates();
                for (int i = 0; i < coords.length; i++) {
                    Coordinate coordinate = coords[i];
                    assertNotNull(coordinate);
                }

		int i=0;
		while( reader.hasNext() ){
			i++;
			shape=reader.nextRecord().shape();
			assertNotNull( shape );
			assertTrue( shape instanceof Geometry);
			if( i==0 ){
                            Geometry geom=(Geometry) shape;
				assertEquals(13, geom.getCoordinates().length);
			}
		}
		assertEquals(ds.getFeatureSource().getCount(Query.ALL)-1, i);
	}
	public void testDecimation() throws Exception{
		URL url=TestData.getResource(LabelingTest.class, "theme1.shp");
		ShapefileDataStore ds=(ShapefileDataStore) new ShapefileDataStoreFactory().createDataStore(url);
		
		Envelope env=new Envelope(-7.105552354197932,8.20555235419793,-3.239388966356115,4.191388966388683);
		CoordinateReferenceSystem crs=DefaultGeographicCRS.WGS84;
		MathTransform mt=CRS.findMathTransform(crs, DefaultGeographicCRS.WGS84);
		AffineTransform at=RendererUtilities.worldToScreenTransform(env,new Rectangle(300,300));
		MathTransform worldToScreen=ReferencingFactoryFinder.getMathTransformFactory(null)
		.createAffineTransform(new GeneralMatrix(at));
		mt = ReferencingFactoryFinder.getMathTransformFactory(null)
		.createConcatenatedTransform(mt,worldToScreen);
                ShapefileReader reader=new ShapefileReader(ShapefileRendererUtil.getShpFiles(ds), false, false);
		reader.setHandler(new org.geotools.renderer.shape.shapehandler.jts.MultiLineHandler(reader.getHeader().getShapeType(), env, mt, false,new Rectangle(300,300)));
		Geometry shape=(Geometry) reader.nextRecord().shape();
		assertEquals( 3, shape.getCoordinates().length );
		
		shape=(Geometry) reader.nextRecord().shape();
		assertEquals( 2, shape.getCoordinates().length );

		shape=(Geometry) reader.nextRecord().shape();
		assertEquals( 2, shape.getCoordinates().length);
	}

	public void disabledtestFeatureNearBoundry() throws Exception{		
        ShapefileDataStore ds=(ShapefileDataStore) TestUtilites.getDataStore("theme1.shp");
		Style style=TestUtilites.createTestStyle(null,"theme1");
		assertNotNull(style);
		MapContext map = new DefaultMapContext();
        map.addLayer(ds.getFeatureSource(), style);
        ShapefileRenderer renderer = new ShapefileRenderer(map);
        Envelope env = new Envelope(-5,6,-1.4,0);
        TestUtilites.INTERACTIVE=INTERACTIVE;
        TestUtilites.showRender("testLineLabeling", renderer, 2000, env);
	}

	public void testBBoxIntersectSegment() throws Exception{
            org.geotools.renderer.shape.shapehandler.jts.MultiLineHandler handler=
                    new org.geotools.renderer.shape.shapehandler.jts.MultiLineHandler(ShapeType.ARC, new Envelope(0,10,0,10), 
                            IdentityTransform.create(2), false, 
                            new Rectangle(0,0,10,10));
		assertTrue("point contained in bbox", handler.bboxIntersectSegment(false, new double[]{1,1}, 2));
		assertFalse("point outside of bbox",handler.bboxIntersectSegment(false, new double[]{-1,1}, 2));
		assertTrue("Line enters bbox", handler.bboxIntersectSegment(false, new double[]{-1,1, 1,1}, 4));
		assertTrue("line crosses bbox, no vertices contained",
				handler.bboxIntersectSegment(false, new double[]{-1,1, 11,1}, 4));
		assertFalse("line misses bbox", handler.bboxIntersectSegment(false, new double[]{-1,-1, 11,-1}, 4));
		assertTrue("line diagonally crosses bbox, no vertices contained", 
				handler.bboxIntersectSegment(false, new double[]{2,-2, 12,6}, 4));
		assertFalse("diagonal line misses bbox, no vertices contained", 
				handler.bboxIntersectSegment(false, new double[]{8,-4, 14,2}, 4));
	}
}
