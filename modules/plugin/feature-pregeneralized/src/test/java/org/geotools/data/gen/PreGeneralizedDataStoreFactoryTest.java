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
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.geotools.api.data.DataStoreFinder;
import org.geotools.data.DefaultRepository;
import org.junit.Assert;
import org.junit.Test;

public class PreGeneralizedDataStoreFactoryTest {
    @Test
    public void testDataStoreFactory() {

        Map<String, Serializable> paramMap = new HashMap<>();
        try {
            paramMap.put(
                    PreGeneralizedDataStoreFactory.REPOSITORY_CLASS.key,
                    DefaultRepository.class.getName());
            Assert.assertNull(DataStoreFinder.getDataStore(paramMap));

            paramMap.clear();
            paramMap.put(
                    PreGeneralizedDataStoreFactory.GENERALIZATION_INFOS_PROVIDER_CLASS.key,
                    "org.geotools.data.gen.info.GeneralizationInfosProviderImpl");
            Assert.assertNull(DataStoreFinder.getDataStore(paramMap));

            paramMap.clear();
            paramMap.put(
                    PreGeneralizedDataStoreFactory.REPOSITORY_CLASS.key,
                    DefaultRepository.class.getName());
            paramMap.put(
                    PreGeneralizedDataStoreFactory.GENERALIZATION_INFOS_PROVIDER_CLASS.key,
                    "org.geotools.data.gen.info.GeneralizationInfosProviderImpl");
            paramMap.put(
                    PreGeneralizedDataStoreFactory.GENERALIZATION_INFOS_PROVIDER_PARAM.key,
                    "src/test/resources/geninfo1.xml");
            Assert.assertNotNull(DataStoreFinder.getDataStore(paramMap));
        } catch (IOException ex) {
            Assert.fail();
        }

        paramMap.clear();
        boolean error = false;
        try {
            paramMap.put(
                    PreGeneralizedDataStoreFactory.REPOSITORY_CLASS.key,
                    DefaultRepository.class.getName());
            paramMap.put(
                    PreGeneralizedDataStoreFactory.GENERALIZATION_INFOS_PROVIDER_CLASS.key,
                    "org.geotools.data.gen.info.GeneralizationInfosProviderImpl");
            paramMap.put(
                    PreGeneralizedDataStoreFactory.GENERALIZATION_INFOS_PROVIDER_PARAM.key, "yyyy");
            DataStoreFinder.getDataStore(paramMap);
        } catch (IOException ex) {
            error = true;
        }
        Assert.assertTrue(error);

        paramMap.clear();
        error = false;
        try {
            paramMap.put(PreGeneralizedDataStoreFactory.REPOSITORY_CLASS.key, "XXX");
            paramMap.put(
                    PreGeneralizedDataStoreFactory.GENERALIZATION_INFOS_PROVIDER_CLASS.key,
                    "org.geotools.data.gen.info.GeneralizationInfosProviderImpl");
            paramMap.put(
                    PreGeneralizedDataStoreFactory.GENERALIZATION_INFOS_PROVIDER_PARAM.key,
                    "src/test/resources/geninfo1.xml");
            DataStoreFinder.getDataStore(paramMap);
        } catch (IOException ex) {
            error = true;
        }
        Assert.assertTrue(error);
    }
}
