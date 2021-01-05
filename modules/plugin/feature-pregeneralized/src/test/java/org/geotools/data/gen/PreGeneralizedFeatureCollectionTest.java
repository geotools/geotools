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

package org.geotools.data.gen;

import java.io.IOException;
import org.geotools.data.gen.info.GeneralizationInfos;
import org.geotools.data.gen.info.GeneralizationInfosProvider;
import org.geotools.data.gen.info.GeneralizationInfosProviderImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PreGeneralizedFeatureCollectionTest {

    @Before
    public void setUp() throws Exception {
        TestSetup.initialize();
    }

    @Test
    public void testNotSupportedFeatures() {

        GeneralizationInfosProvider provider = new GeneralizationInfosProviderImpl();
        GeneralizationInfos ginfos = null;
        PreGeneralizedDataStore ds = null;
        String typeName = null;
        try {
            ginfos = provider.getGeneralizationInfos("src/test/resources/geninfo_only_base.xml");
            ds = new PreGeneralizedDataStore(ginfos, TestSetup.REPOSITORY);
            typeName = ds.getTypeNames()[0];
            ds.getFeatureSource(typeName).getFeatures();
        } catch (IOException ex) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", ex);
            Assert.fail();
        }

        // the non-supported features
    }
}
