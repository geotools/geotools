/* (c) 2023 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geotools.data.graticule;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.vector.GraticuleLabelTestSupport;
import org.junit.Before;
import org.junit.Test;

public class GraticuleFeatureSourceTest extends GraticuleLabelTestSupport {

    private SimpleFeatureSource source;

    @Before
    public void localSetup() throws IOException {
        source = store.getFeatureSource(store.getTypeNames()[0]);
    }

    @Test
    public void testGetBounds() throws Exception {
        ReferencedEnvelope box = source.getBounds();
        assertEquals(bounds, box);
    }

    @Test
    public void testGetFeatures() throws Exception {
        SimpleFeatureCollection features = source.getFeatures();
        assertEquals(-1, source.getCount(Query.ALL)); // no optimized count
        assertEquals(76, features.size()); // ContentFeatureCollection scans to count
        assertEquals(76, DataUtilities.count(features)); // DataUtilities.count also scans to count
    }
}
