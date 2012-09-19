/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.memory;

import org.geotools.data.DataTestCase;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultQuery;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * @author Frank Gasdorf, fgdrf@users.sourceforge.net
 * @author Sebastian Graca, ISPiK S.A.
 *
 *
 *
 *
 * @source $URL$
 */
public class MemoryDataStoreBoundsTest extends DataTestCase {
    MemoryDataStore data;
    
	public MemoryDataStoreBoundsTest(String name) {
		super(name);
	}

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        data = new MemoryDataStore();
        data.addFeatures(roadFeatures);
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        data = null;
        super.tearDown();
    }

	public void testGetBounds() throws Exception {
        // the Bounds of the queried features should be equal to the bounding 
        // box of the road2 feature, because of the road2 FID filter  
        Query query = new DefaultQuery("road", rd2Filter);
        assertEquals(roadFeatures[1].getBounds(), data.getBounds(query));
    }

    public void testGetBoundsSupportsFeaturesWithoutGeometry() throws Exception {
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(data.getSchema("road"));
        featureBuilder.init(roadFeatures[0]);
        featureBuilder.set("geom", null);
        SimpleFeature feature = featureBuilder.buildFeature("road.rd0");
        data.addFeature(feature);

        SimpleFeatureSource road = data.getFeatureSource("road");
        assertEquals(roadBounds, road.getBounds(Query.ALL));
    }

    public void testGetBoundsSupportsEmptyBounds() throws Exception {
        SimpleFeatureType type = DataUtilities.createType(getName() + ".test",
                "id:0,geom:LineString,name:String");
        SimpleFeature[] features = new SimpleFeature[3];
        features[0] = SimpleFeatureBuilder.build(type, new Object[] {1, null, "r1"}, "test.f1");
        features[1] = SimpleFeatureBuilder.build(type, new Object[] {2, null, "r2"}, "test.f2");
        features[2] = SimpleFeatureBuilder.build(type, new Object[] {3, null, "r3"}, "test.f3");
        data.addFeatures(features);

        SimpleFeatureSource featureSource = data.getFeatureSource("test");
        assertTrue(featureSource.getBounds(Query.ALL).isEmpty());
    }
}
