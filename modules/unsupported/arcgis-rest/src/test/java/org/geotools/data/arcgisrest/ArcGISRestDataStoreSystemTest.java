/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.arcgisrest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;
import org.geotools.feature.NameImpl;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;

public class ArcGISRestDataStoreSystemTest {

    /** Helper method to create a default data store on ArcGIS Server */
    public static DataStore createDefaultOpenDataTestDataStore(String url) throws IOException {

        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put(ArcGISRestDataStoreFactory.NAMESPACE_PARAM.key, "http://aurin.org.au");
        params.put(ArcGISRestDataStoreFactory.URL_PARAM.key, url);
        params.put(ArcGISRestDataStoreFactory.ISOPENDATA_PARAM.key, true);
        params.put(ArcGISRestDataStoreFactory.USER_PARAM.key, null);
        params.put(ArcGISRestDataStoreFactory.PASSWORD_PARAM.key, null);
        return (new ArcGISRestDataStoreFactory()).createDataStore(params);
    }

    @Test
    public void testCityOfDarwin() throws Exception {

        ArcGISRestDataStore dataStore =
                (ArcGISRestDataStore)
                        ArcGISRestDataStoreSystemTest.createDefaultOpenDataTestDataStore(
                                "http://open-darwin.opendata.arcgis.com/data.json");
        List<Name> names = dataStore.createTypeNames();

        assertEquals(38, names.size());
        names.forEach(
                (n) -> {
                    System.out.println(n.getURI());
                });

        String[] namesArray = ((DataStore) dataStore).getTypeNames();
        for (int i = 0; i < namesArray.length; i++) {
            System.out.println(String.format("%d of %d %s", i, namesArray.length, namesArray[i]));
            FeatureSource<SimpleFeatureType, SimpleFeature> src =
                    dataStore.createFeatureSource(
                            dataStore.getEntry(
                                    new NameImpl(
                                            ArcGISRestDataStoreFactoryTest.NAMESPACE,
                                            namesArray[i])));
            assertNotNull(src.getSchema());
            assertNotNull(src.getSchema().getTypeName());
            assertNotNull(src.getBounds());
            assertNotNull(src.getFeatures());
            System.out.println(
                    String.format(
                            "      %d %s %s",
                            src.getFeatures().size(),
                            src.getSchema().getTypeName(),
                            src.getBounds()));
        }
    }
}
