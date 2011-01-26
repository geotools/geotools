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
package org.geotools.validation.relate;

import junit.framework.TestCase;

import org.geotools.data.DataUtilities;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.validation.ValidationResults;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

/**
 * SpatialTestCase<br>
 * @author bowens<br>
 * Created Apr 29, 2004<br>
 * @source $URL$
 * @version <br>
 *
 * <b>Puropse:</b><br>
 * <p>
 * DOCUMENT ME!!
 * </p>
 *
 * <b>Description:</b><br>
 * <p>
 * DOCUMENT ME!!
 * </p>
 *
 * <b>Usage:</b><br>
 * <p>
 * DOCUMENT ME!!
 * </p>
 */
public class SpatialTestCase extends TestCase
{
	protected GeometryFactory gf;
	protected SimpleFeatureType lineType;
	protected SimpleFeature[] lineFeatures;
	protected ReferencedEnvelope lineBounds;
	protected LineString ls0, ls1, ls2, ls3;
	protected String namespace;
	protected FilterFactory filterFactory;
	protected Filter lineFilter;

	MemoryDataStore mds;		// assumes a consistant data type
	ValidationResults vr;

	/**
	 * Constructor for OverlapsIntegrityTest.
	 * @param arg0
	 */
	public SpatialTestCase(String arg0)
	{
		super(arg0);
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 *
	 * <code><pre>
	 *
	 * 			 (0,2)				(2.6,2)
	 * 			    x					x
	 * 				 \  				|
	 * 				  \ls1				|ls2
	 * 				   \	 			|
	 * 			  (1,1)+				+
	 * 				   |				|
	 * 				   |				|
	 * 		ls0		   |	   (2,0.1)	|						ls3
	 * (0,0)x----------+----------+----------+==========x----------x
	 * 				   |				|			  (4,0)		 (5,0.1)
	 * 				   |				|
	 * 				   |				|
	 * 				   x				x
	 * 				(1,-1)			  (2,-1)
	 *
	 * </pre></code>
	 */
	protected void setUp() throws Exception
	{
		gf = new GeometryFactory();
		mds = new MemoryDataStore();
		namespace = getName();
		vr = new TempFeatureResults();

		lineFeatures = new SimpleFeature[4];
		ls0 = gf.createLineString(new Coordinate[]{	new Coordinate(0,0),
													new Coordinate(2,0.1),
													new Coordinate(3,0),
													new Coordinate(4,0)} );
		ls1 = gf.createLineString(new Coordinate[]{	new Coordinate(0,2),
													new Coordinate(1,1),
													new Coordinate(1,0),
													new Coordinate(1,-1)} );
		ls2 = gf.createLineString(new Coordinate[]{	new Coordinate(2.6,2),
													new Coordinate(2.5,1),
													new Coordinate(2.5,-1)} );
		ls3 = gf.createLineString(new Coordinate[]{	new Coordinate(3,0),
													new Coordinate(4,0),
													new Coordinate(5,0.1)} );

		lineType = DataUtilities.createType("my.line",
											"id:0,geom:LineString,name:String");
		lineFeatures[0] = SimpleFeatureBuilder.build(lineType, new Object[] {
										new Integer(0),
										ls0,
										"line0"},
									"line.line0");
		lineFeatures[1] = SimpleFeatureBuilder.build(lineType, new Object[] {
										new Integer(1),
										ls1,
										"line1"},
									"line.line1");
		lineFeatures[2] = SimpleFeatureBuilder.build(lineType, new Object[] {
										new Integer(2),
										ls2,
										"line2"},
									"line.line2");
		lineFeatures[3] = SimpleFeatureBuilder.build(lineType, new Object[] {
										new Integer(3),
										ls3,
										"line3"},
									"line.line3");
		lineBounds = new ReferencedEnvelope();
		lineBounds.include( lineFeatures[0].getBounds() );
		lineBounds.include( lineFeatures[1].getBounds() );
		lineBounds.include( lineFeatures[2].getBounds() );
		lineBounds.include( lineFeatures[3].getBounds() );

//		filterFactory = FilterFactoryFinder.createFilterFactory();
//		BBoxExpression bbex = filterFactory.createBBoxExpression(lineBounds);

		mds.addFeature(lineFeatures[0]);
		mds.addFeature(lineFeatures[1]);
		mds.addFeature(lineFeatures[2]);
		mds.addFeature(lineFeatures[3]);
	}

	protected void tearDown() throws Exception
	{
		gf = null;
		lineType = null;
		lineFeatures = null;
		lineBounds = null;
		ls0 = null;
		ls1 = null;
		ls2 = null;
		ls3 = null;
		namespace = null;
		vr = null;
	}

}
