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
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.matrix.GeneralMatrix;
import org.geotools.renderer.lite.RendererUtilities;
import org.geotools.renderer.shape.LabelingTest;
import org.geotools.renderer.shape.SimpleGeometry;
import org.geotools.test.TestData;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import com.vividsolutions.jts.geom.Envelope;

/**
 * @TODO class description
 * 
 * @author jeichar
 * @since 2.1.x
 * @source $URL$
 */
public class SimplePolygonHandlerTest extends TestCase {

	public void testRead() throws Exception{
		URL url=TestData.getResource(LabelingTest.class, "lakes.shp");
		ShapefileDataStore ds=(ShapefileDataStore) new ShapefileDataStoreFactory().createDataStore(url);
		
		ReferencedEnvelope env=(ReferencedEnvelope) ds.getFeatureSource().getBounds();
//		
                AffineTransform transform = RendererUtilities.worldToScreenTransform(env, new Rectangle(500,500));
                MathTransform mt = ReferencingFactoryFinder.getMathTransformFactory(null).createAffineTransform(new GeneralMatrix(transform));
                
                ShapefileReader reader=new ShapefileReader(ShapefileRendererUtil.getShpFiles(ds), false, false);
		reader.setHandler(new org.geotools.renderer.shape.shapehandler.simple.PolygonHandler(reader.getHeader().getShapeType(), env, mt, false));
		Object shape=reader.nextRecord().shape();
		assertNotNull( shape );
		assertTrue( shape instanceof SimpleGeometry);
		int i=0;
		while( reader.hasNext() ){
			i++;
			shape=reader.nextRecord().shape();
			assertNotNull( shape );
			assertTrue( shape instanceof SimpleGeometry);
		}
		assertEquals(ds.getFeatureSource().getCount(Query.ALL)-1, i);
	}
	

	public void testPolgyonPartDecimation() throws Exception{
		URL url=TestData.getResource(LabelingTest.class, "smallMultiPoly.shp");
		ShapefileDataStore ds=(ShapefileDataStore) new ShapefileDataStoreFactory().createDataStore(url);
		
		Envelope env=new Envelope(-116.61514977458947,-115.06357335975156,31.826799280244018,32.590528603609826);
//		Envelope env=new Envelope(-180,180,-90,90);
//		CoordinateReferenceSystem crs=ds.getSchema().getDefaultGeometry().getCoordinateSystem();
		CoordinateReferenceSystem crs=DefaultGeographicCRS.WGS84;
		MathTransform mt= CRS.findMathTransform(crs, DefaultGeographicCRS.WGS84);
		AffineTransform at=RendererUtilities.worldToScreenTransform(env,new Rectangle(300,300));
		mt = ReferencingFactoryFinder.getMathTransformFactory(null)
		.createConcatenatedTransform(mt, ReferencingFactoryFinder.getMathTransformFactory(null)
				.createAffineTransform(new GeneralMatrix(at)));

                ShapefileReader reader=new ShapefileReader(ShapefileRendererUtil.getShpFiles(ds), false, false);
		reader.setHandler(new org.geotools.renderer.shape.shapehandler.simple.PolygonHandler(reader.getHeader().getShapeType(), env, mt, false));
		Object shape=reader.nextRecord().shape();
		assertNotNull( shape );
		assertTrue( shape instanceof SimpleGeometry);
		SimpleGeometry geom=(SimpleGeometry) shape;
		assertEquals(1, geom.coords.length);
		assertEquals(10, geom.coords[0].length);
	}
		
}
