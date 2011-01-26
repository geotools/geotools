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

import junit.framework.TestCase;

import org.geotools.data.DataStoreFinder;
import org.geotools.data.DefaultRepository;
import org.junit.Assert;

public class PreGeneralizedDataStoreFactoryTest extends TestCase {
    public void testDataStoreFactory() {

        Map<String, Serializable> paramMap = new HashMap<String, Serializable>();
        try {
            paramMap.put(PreGeneralizedDataStoreFactory.REPOSITORY_CLASS.key,
                    DefaultRepository.class.getName());
            assertNull(DataStoreFinder.getDataStore(paramMap));

            paramMap.clear();
            paramMap.put(PreGeneralizedDataStoreFactory.GENERALIZATION_INFOS_PROVIDER_CLASS.key,
                    "org.geotools.data.gen.info.GeneralizationInfosProviderImpl");
            assertNull(DataStoreFinder.getDataStore(paramMap));

            paramMap.clear();
            paramMap.put(PreGeneralizedDataStoreFactory.REPOSITORY_CLASS.key,
                    DefaultRepository.class.getName());
            paramMap.put(PreGeneralizedDataStoreFactory.GENERALIZATION_INFOS_PROVIDER_CLASS.key,
                    "org.geotools.data.gen.info.GeneralizationInfosProviderImpl");
            paramMap.put(PreGeneralizedDataStoreFactory.GENERALIZATION_INFOS_PROVIDER_PARAM.key,
                    "src/test/resources/geninfo1.xml");
            assertNotNull(DataStoreFinder.getDataStore(paramMap));
        } catch (IOException ex) {
            Assert.fail();
        }

        paramMap.clear();
        boolean error = false;
        try {
            paramMap.put(PreGeneralizedDataStoreFactory.REPOSITORY_CLASS.key,
                    DefaultRepository.class.getName());
            paramMap.put(PreGeneralizedDataStoreFactory.GENERALIZATION_INFOS_PROVIDER_CLASS.key,
                    "org.geotools.data.gen.info.GeneralizationInfosProviderImpl");
            paramMap.put(PreGeneralizedDataStoreFactory.GENERALIZATION_INFOS_PROVIDER_PARAM.key,
                    "yyyy");
            DataStoreFinder.getDataStore(paramMap);
        } catch (IOException ex) {
            error = true;
        }
        assertTrue(error);

        paramMap.clear();
        error = false;
        try {
            paramMap.put(PreGeneralizedDataStoreFactory.REPOSITORY_CLASS.key, "XXX");
            paramMap.put(PreGeneralizedDataStoreFactory.GENERALIZATION_INFOS_PROVIDER_CLASS.key,
                    "org.geotools.data.gen.info.GeneralizationInfosProviderImpl");
            paramMap.put(PreGeneralizedDataStoreFactory.GENERALIZATION_INFOS_PROVIDER_PARAM.key,
                    "src/test/resources/geninfo1.xml");
            DataStoreFinder.getDataStore(paramMap);
        } catch (IOException ex) {
            error = true;
        }
        assertTrue(error);

    }

}
