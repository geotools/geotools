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
            e.printStackTrace();
            Assert.fail();
            return;
        }

        assertTrue("DSInfos".equals(infos.getDataSourceName()));
        assertTrue("WSInfos".equals(infos.getDataSourceNameSpace()));

        Collection<String> coll = null;
        coll = infos.getBaseFeatureNames();
        assertTrue(coll.size() == 2);
        assertTrue(coll.contains("BaseFeature1"));
        assertTrue(coll.contains("BaseFeature2"));

        coll = infos.getFeatureNames();
        assertTrue(coll.size() == 2);
        assertTrue(coll.contains("GenFeature1"));
        assertTrue(coll.contains("BaseFeature2"));

        GeneralizationInfo info1 = infos.getGeneralizationInfoForBaseFeatureName("BaseFeature1");
        assertTrue("DSInfo".equals(info1.getDataSourceName()));
        assertTrue("WSInfo".equals(info1.getDataSourceNameSpace()));

        assertTrue(info1 == infos.getGeneralizationInfoForFeatureName("GenFeature1"));

        GeneralizationInfo info2 = infos.getGeneralizationInfoForBaseFeatureName("BaseFeature2");
        assertTrue("DSInfos".equals(info2.getDataSourceName()));
        assertTrue("WSInfos".equals(info2.getDataSourceNameSpace()));

        assertTrue(info2 == infos.getGeneralizationInfoForFeatureName("BaseFeature2"));

        assertTrue(info1.getFeatureName().equals("GenFeature1"));
        assertTrue(info1.getBaseFeatureName().equals("BaseFeature1"));
        assertTrue(info1.getGeomPropertyName().equals("the_geom"));

        assertTrue(info2.getFeatureName().equals("BaseFeature2"));
        assertTrue(info2.getBaseFeatureName().equals("BaseFeature2"));
        assertTrue(info2.getGeomPropertyName().equals("the_geom"));

        assertTrue(info1.getGeneralizations().size() == 2);
        assertTrue(info2.getGeneralizations().size() == 2);

        assertTrue(info1.getGeneralizationForDistance(99.0) == null);
        assertTrue(info1.getGeneralizationForDistance(100.0).getFeatureName().equals("GenFeature1"));
        assertTrue(info1.getGeneralizationForDistance(999.0).getFeatureName().equals("GenFeature1"));
        assertTrue("DSInfo".equals(info1.getGeneralizationForDistance(100.0).getDataSourceName()));
        assertTrue("WSInfo".equals(info1.getGeneralizationForDistance(100.0)
                .getDataSourceNameSpace()));

        assertTrue(info1.getGeneralizationForDistance(1000.0).getFeatureName()
                .equals("GenFeature2"));
        assertTrue(info1.getGeneralizationForDistance(10000.0).getFeatureName().equals(
                "GenFeature2"));
        assertTrue("DSDistance".equals(info1.getGeneralizationForDistance(1000.0)
                .getDataSourceName()));
        assertTrue("WSDistance".equals(info1.getGeneralizationForDistance(1000.0)
                .getDataSourceNameSpace()));

        assertTrue(info2.getGeneralizationForDistance(99.0) == null);
        assertTrue(info2.getGeneralizationForDistance(100.0).getGeomPropertyName().equals(
                "the_geom1"));
        assertTrue(info2.getGeneralizationForDistance(999.0).getGeomPropertyName().equals(
                "the_geom1"));
        assertTrue(info2.getGeneralizationForDistance(1000.0).getGeomPropertyName().equals(
                "the_geom2"));
        assertTrue(info2.getGeneralizationForDistance(10000.0).getGeomPropertyName().equals(
                "the_geom2"));

        assertTrue("DSInfos".equals(info2.getGeneralizationForDistance(100.0).getDataSourceName()));
        assertTrue("WSInfos".equals(info2.getGeneralizationForDistance(100.0)
                .getDataSourceNameSpace()));

        GeneralizationInfo gi = infos.getGeneralizationInfoForFeatureName("GenFeature1");
        assertNotNull(gi);
        infos.removeGeneralizationInfo(gi);
        assertNull(infos.getGeneralizationInfoForFeatureName("GenFeature1"));
        infos.addGeneralizationInfo(gi);
        assertNotNull(infos.getGeneralizationInfoForFeatureName("GenFeature1"));
    }
}
