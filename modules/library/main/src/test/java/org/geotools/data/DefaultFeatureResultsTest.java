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
package org.geotools.data;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import org.geotools.api.data.DataStore;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.junit.Assert;
import org.junit.Test;

public class DefaultFeatureResultsTest {

    @Test
    public void testMaxFeatureOptimized() throws Exception {
        Query q = new Query("roads");
        q.setMaxFeatures(10);

        // mock up the feature source so that it'll return a count of 20
        SimpleFeatureType type = DataUtilities.createType("roads", "_=the_geom:Point,FID:String,NAME:String");
        SimpleFeatureSource fs = createMock(SimpleFeatureSource.class);
        expect(fs.getSchema()).andReturn(type).anyTimes();
        expect(fs.getCount(q)).andReturn(20);
        replay(fs);

        DefaultFeatureResults results = new DefaultFeatureResults(fs, q);
        Assert.assertEquals(10, results.size());
    }

    @Test
    public void testMaxfeaturesHandCount() throws Exception {
        Query q = new Query("roads");
        q.setMaxFeatures(1);

        // mock up the feature source so that it'll return a count of -1 (too expensive)
        // and then will return a reader
        @SuppressWarnings("PMD.CloseResource")
        FeatureReader<SimpleFeatureType, SimpleFeature> fr = createNiceMock(FeatureReader.class);
        expect(fr.hasNext()).andReturn(true).times(2).andReturn(false);
        replay(fr);

        DataStore ds = createMock(DataStore.class);
        expect(ds.getFeatureReader(q, Transaction.AUTO_COMMIT)).andReturn(fr);
        replay(ds);

        SimpleFeatureType type = DataUtilities.createType("roads", "_=the_geom:Point,FID:String,NAME:String");
        SimpleFeatureSource fs = createMock(SimpleFeatureSource.class);
        expect(fs.getSchema()).andReturn(type).anyTimes();
        expect(fs.getCount(q)).andReturn(-1);
        expect(fs.getDataStore()).andReturn(ds);
        replay(fs);

        DefaultFeatureResults results = new DefaultFeatureResults(fs, q);
        Assert.assertEquals(1, results.size());
    }
}
