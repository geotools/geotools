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
import org.junit.Assert;
import org.junit.Test;

public class GeneralizationInfosTest {

    @Test
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

        Assert.assertEquals("DSInfos", infos.getDataSourceName());
        Assert.assertEquals("WSInfos", infos.getDataSourceNameSpace());

        Collection<String> coll = infos.getBaseFeatureNames();
        Assert.assertEquals(2, coll.size());
        Assert.assertTrue(coll.contains("BaseFeature1"));
        Assert.assertTrue(coll.contains("BaseFeature2"));

        coll = infos.getFeatureNames();
        Assert.assertEquals(2, coll.size());
        Assert.assertTrue(coll.contains("GenFeature1"));
        Assert.assertTrue(coll.contains("BaseFeature2"));

        GeneralizationInfo info1 = infos.getGeneralizationInfoForBaseFeatureName("BaseFeature1");
        Assert.assertEquals("DSInfo", info1.getDataSourceName());
        Assert.assertEquals("WSInfo", info1.getDataSourceNameSpace());

        Assert.assertSame(info1, infos.getGeneralizationInfoForFeatureName("GenFeature1"));

        GeneralizationInfo info2 = infos.getGeneralizationInfoForBaseFeatureName("BaseFeature2");
        Assert.assertEquals("DSInfos", info2.getDataSourceName());
        Assert.assertEquals("WSInfos", info2.getDataSourceNameSpace());

        Assert.assertSame(info2, infos.getGeneralizationInfoForFeatureName("BaseFeature2"));

        Assert.assertEquals("GenFeature1", info1.getFeatureName());
        Assert.assertEquals("BaseFeature1", info1.getBaseFeatureName());
        Assert.assertEquals("the_geom", info1.getGeomPropertyName());

        Assert.assertEquals("BaseFeature2", info2.getFeatureName());
        Assert.assertEquals("BaseFeature2", info2.getBaseFeatureName());
        Assert.assertEquals("the_geom", info2.getGeomPropertyName());

        Assert.assertEquals(2, info1.getGeneralizations().size());
        Assert.assertEquals(2, info2.getGeneralizations().size());

        Assert.assertNull(info1.getGeneralizationForDistance(99.0));
        Assert.assertEquals(
                "GenFeature1", info1.getGeneralizationForDistance(100.0).getFeatureName());
        Assert.assertEquals(
                "GenFeature1", info1.getGeneralizationForDistance(999.0).getFeatureName());
        Assert.assertEquals("DSInfo", info1.getGeneralizationForDistance(100.0).getDataSourceName());
        Assert.assertEquals("WSInfo", info1.getGeneralizationForDistance(100.0).getDataSourceNameSpace());

        Assert.assertEquals(
                "GenFeature2", info1.getGeneralizationForDistance(1000.0).getFeatureName());
        Assert.assertEquals(
                "GenFeature2", info1.getGeneralizationForDistance(10000.0).getFeatureName());
        Assert.assertEquals(
                "DSDistance", info1.getGeneralizationForDistance(1000.0).getDataSourceName());
        Assert.assertEquals(
                "WSDistance", info1.getGeneralizationForDistance(1000.0).getDataSourceNameSpace());

        Assert.assertNull(info2.getGeneralizationForDistance(99.0));
        Assert.assertEquals(
                "the_geom1", info2.getGeneralizationForDistance(100.0).getGeomPropertyName());
        Assert.assertEquals(
                "the_geom1", info2.getGeneralizationForDistance(999.0).getGeomPropertyName());
        Assert.assertEquals(
                "the_geom2", info2.getGeneralizationForDistance(1000.0).getGeomPropertyName());
        Assert.assertEquals(
                "the_geom2", info2.getGeneralizationForDistance(10000.0).getGeomPropertyName());

        Assert.assertEquals("DSInfos", info2.getGeneralizationForDistance(100.0).getDataSourceName());
        Assert.assertEquals("WSInfos", info2.getGeneralizationForDistance(100.0).getDataSourceNameSpace());

        GeneralizationInfo gi = infos.getGeneralizationInfoForFeatureName("GenFeature1");
        Assert.assertNotNull(gi);
        infos.removeGeneralizationInfo(gi);
        Assert.assertNull(infos.getGeneralizationInfoForFeatureName("GenFeature1"));
        infos.addGeneralizationInfo(gi);
        Assert.assertNotNull(infos.getGeneralizationInfoForFeatureName("GenFeature1"));
    }
}
