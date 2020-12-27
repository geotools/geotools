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

package org.geotools.data.gen.info;

import java.io.IOException;
import java.util.Collection;
import junit.framework.TestCase;
import org.junit.Assert;

public class GeneralizationInfosTest extends TestCase {

    public void testGeneralizationInfos() {
        GeneralizationInfosProvider provider = new GeneralizationInfosProviderImpl();
        GeneralizationInfos infos = null;
        try {
            infos = provider.getGeneralizationInfos("src/test/resources/geninfo1.xml");
        } catch (IOException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail();
            return;
        }

        assertEquals("DSInfos", infos.getDataSourceName());
        assertEquals("WSInfos", infos.getDataSourceNameSpace());

        Collection<String> coll = null;
        coll = infos.getBaseFeatureNames();
        assertEquals(2, coll.size());
        assertTrue(coll.contains("BaseFeature1"));
        assertTrue(coll.contains("BaseFeature2"));

        coll = infos.getFeatureNames();
        assertEquals(2, coll.size());
        assertTrue(coll.contains("GenFeature1"));
        assertTrue(coll.contains("BaseFeature2"));

        GeneralizationInfo info1 = infos.getGeneralizationInfoForBaseFeatureName("BaseFeature1");
        assertEquals("DSInfo", info1.getDataSourceName());
        assertEquals("WSInfo", info1.getDataSourceNameSpace());

        assertSame(info1, infos.getGeneralizationInfoForFeatureName("GenFeature1"));

        GeneralizationInfo info2 = infos.getGeneralizationInfoForBaseFeatureName("BaseFeature2");
        assertEquals("DSInfos", info2.getDataSourceName());
        assertEquals("WSInfos", info2.getDataSourceNameSpace());

        assertSame(info2, infos.getGeneralizationInfoForFeatureName("BaseFeature2"));

        assertEquals("GenFeature1", info1.getFeatureName());
        assertEquals("BaseFeature1", info1.getBaseFeatureName());
        assertEquals("the_geom", info1.getGeomPropertyName());

        assertEquals("BaseFeature2", info2.getFeatureName());
        assertEquals("BaseFeature2", info2.getBaseFeatureName());
        assertEquals("the_geom", info2.getGeomPropertyName());

        assertEquals(2, info1.getGeneralizations().size());
        assertEquals(2, info2.getGeneralizations().size());

        assertNull(info1.getGeneralizationForDistance(99.0));
        assertEquals("GenFeature1", info1.getGeneralizationForDistance(100.0).getFeatureName());
        assertEquals("GenFeature1", info1.getGeneralizationForDistance(999.0).getFeatureName());
        assertEquals("DSInfo", info1.getGeneralizationForDistance(100.0).getDataSourceName());
        assertEquals("WSInfo", info1.getGeneralizationForDistance(100.0).getDataSourceNameSpace());

        assertEquals("GenFeature2", info1.getGeneralizationForDistance(1000.0).getFeatureName());
        assertEquals("GenFeature2", info1.getGeneralizationForDistance(10000.0).getFeatureName());
        assertEquals("DSDistance", info1.getGeneralizationForDistance(1000.0).getDataSourceName());
        assertEquals(
                "WSDistance", info1.getGeneralizationForDistance(1000.0).getDataSourceNameSpace());

        assertNull(info2.getGeneralizationForDistance(99.0));
        assertEquals("the_geom1", info2.getGeneralizationForDistance(100.0).getGeomPropertyName());
        assertEquals("the_geom1", info2.getGeneralizationForDistance(999.0).getGeomPropertyName());
        assertEquals("the_geom2", info2.getGeneralizationForDistance(1000.0).getGeomPropertyName());
        assertEquals(
                "the_geom2", info2.getGeneralizationForDistance(10000.0).getGeomPropertyName());

        assertEquals("DSInfos", info2.getGeneralizationForDistance(100.0).getDataSourceName());
        assertEquals("WSInfos", info2.getGeneralizationForDistance(100.0).getDataSourceNameSpace());

        GeneralizationInfo gi = infos.getGeneralizationInfoForFeatureName("GenFeature1");
        assertNotNull(gi);
        infos.removeGeneralizationInfo(gi);
        assertNull(infos.getGeneralizationInfoForFeatureName("GenFeature1"));
        infos.addGeneralizationInfo(gi);
        assertNotNull(infos.getGeneralizationInfoForFeatureName("GenFeature1"));
    }
}
