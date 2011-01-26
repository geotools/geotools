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
package org.geotools.renderer.shape;

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;

import junit.framework.TestCase;

import org.geotools.data.shapefile.shp.ShapeType;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Tests MultiLineShape class
 * 
 * @author jeichar
 * @since 2.1.x
 * @source $URL$
 */
public class MultiLineShapeTest extends TestCase {

	/**
	 * Class under test for PathIterator getPathIterator(AffineTransform)
	 */
	public void testGetPathIteratorAffineTransform() {
		double[] coord1=new double[]{0.0,0.0, 10.0, 10.0 ,15.0,0.0};
		double[] coord2=new double[]{0.0,15.0, 10.0, 15.0 ,15.0,15.0};
		double[][]coords=new double[][]{coord1,coord2};
		
		SimpleGeometry geom=new SimpleGeometry(ShapeType.ARC, coords,new Envelope( 0,15,0,15));
		
		MultiLineShape shape=new MultiLineShape(geom);
		
		PathIterator i=shape.getPathIterator(new AffineTransform());
		double[] tmp=new double[6];
		int result=i.currentSegment(tmp);
		assertFalse( i.isDone() );
		assertEquals(0.0,tmp[0], 0.001);
		assertEquals(0.0,tmp[1], 0.001);
		assertEquals(PathIterator.SEG_MOVETO, result);
		
		i.next();

		tmp=new double[6];
		result=i.currentSegment(tmp);
		
		assertFalse( i.isDone() );
		assertEquals(10.0,tmp[0], 0.001);
		assertEquals(10.0,tmp[1], 0.001);
		assertEquals(PathIterator.SEG_LINETO, result);

		i.next();

		tmp=new double[6];
		result=i.currentSegment(tmp);
		
		assertFalse( i.isDone() );
		assertEquals(15.0,tmp[0], 0.001);
		assertEquals(0.0,tmp[1], 0.001);
		assertEquals(PathIterator.SEG_LINETO, result);

		i.next();

		tmp=new double[6];
		result=i.currentSegment(tmp);
		
		assertFalse( i.isDone() );
		assertEquals(0.0,tmp[0], 0.001);
		assertEquals(15.0,tmp[1], 0.001);
		assertEquals(PathIterator.SEG_MOVETO, result);

		i.next();

		tmp=new double[6];
		result=i.currentSegment(tmp);
		
		assertFalse( i.isDone() );
		assertEquals(10.0,tmp[0], 0.001);
		assertEquals(15.0,tmp[1], 0.001);
		assertEquals(PathIterator.SEG_LINETO, result);

		i.next();

		tmp=new double[6];
		result=i.currentSegment(tmp);
		
		assertFalse( i.isDone() );
		assertEquals(15.0,tmp[0], 0.001);
		assertEquals(15.0,tmp[1], 0.001);
		assertEquals(PathIterator.SEG_LINETO, result);

		i.next();
		assertTrue( i.isDone() );
	}

}
