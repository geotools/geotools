/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.vectormosaic;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.geotools.api.filter.FilterFactory2;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultRepository;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.property.PropertyDataStoreFactory;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Before;

public class VectorMosaicTest {
    public static DefaultRepository REPOSITORY;
    public static VectorMosaicStoreFactory VECTOR_MOSAIC_STORE_FACTORY;
    public static DataStore MOSAIC_STORE;
    public static DataStore MOSAIC_STORE_WITH_SPI;
    public static DataStore MOSAIC_STORE_FROM_PROPERTIES;
    static boolean initialized = false;
    protected static final String MOSAIC_TYPE_NAME = "mosaic_delegate_mosaic";
    protected static final String MOSAIC_PROPERTIES_TYPE_NAME = "mosaic_delegate_mosaic";

    protected static FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2();

    public static void initialize() {
        if (initialized) return;
        try {
            REPOSITORY = new DefaultRepository();
            File delegate =
                    new File(
                            "src/test/resources/org.geotools.vectormosaic.data/mosaic_delegate.shp");
            URL url = delegate.toURI().toURL();

            ShapefileDataStore ds =
                    (ShapefileDataStore) new ShapefileDataStoreFactory().createDataStore(url);
            REPOSITORY.register("delegate", ds);
            VECTOR_MOSAIC_STORE_FACTORY = new VectorMosaicStoreFactory();
            Map<String, Object> params = new HashMap<>();
            params.put(VectorMosaicStoreFactory.DELEGATE_STORE_NAME.getName(), "delegate");
            params.put(VectorMosaicStoreFactory.NAMESPACE.getName(), "topp");
            params.put(VectorMosaicStoreFactory.REPOSITORY_PARAM.getName(), REPOSITORY);
            MOSAIC_STORE = VECTOR_MOSAIC_STORE_FACTORY.createDataStore(params);
            params.put(
                    VectorMosaicStoreFactory.PREFERRED_DATASTORE_SPI.getName(),
                    "org.geotools.data.shapefile.ShapefileDataStoreFactory");
            MOSAIC_STORE_WITH_SPI = VECTOR_MOSAIC_STORE_FACTORY.createDataStore(params);

            Map<String, Serializable> propertyParams = new HashMap<>();
            File directory = new File("src/test/resources/org.geotools.vectormosaic.data/");
            propertyParams.put("directory", directory);
            PropertyDataStore propertyDataStore =
                    (PropertyDataStore)
                            new PropertyDataStoreFactory().createDataStore(propertyParams);
            REPOSITORY.register("propertyDelegate", propertyDataStore);
            params.put(VectorMosaicStoreFactory.DELEGATE_STORE_NAME.getName(), "propertyDelegate");
            MOSAIC_STORE_FROM_PROPERTIES = VECTOR_MOSAIC_STORE_FACTORY.createDataStore(params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        initialized = true;
    }

    @Before
    public void setup() {
        initialize();
    }
}
