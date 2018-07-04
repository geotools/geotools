package org.geotools.coverage.io.netcdf;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.DataStore;
import org.geotools.test.TestData;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;

/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
public class NetCDFAuxiliaryTest {

    @Test
    public void test() throws Exception {

        File file = TestData.file(this, "fivedim.nc");

        // first create reader to build index
        new NetCDFFormat().getReader(file);

        NetCDFAuxiliaryStoreFactory fac = new NetCDFAuxiliaryStoreFactory();
        Map<String, Serializable> params = new HashMap<>();
        params.put(NetCDFAuxiliaryStoreFactory.FILE_PARAM.getName(), file);

        assertTrue(fac.canProcess(params));

        DataStore store = fac.createDataStore(params);

        assertNotNull(store);

        assertArrayEquals(new String[] {"D"}, store.getTypeNames());

        SimpleFeatureType type = store.getSchema("D");

        assertNotNull(type);

        assertEquals(5, type.getAttributeCount());

        assertEquals("the_geom", type.getAttributeDescriptors().get(0).getName().getLocalPart());
        assertEquals("imageindex", type.getAttributeDescriptors().get(1).getName().getLocalPart());
        assertEquals("time", type.getAttributeDescriptors().get(2).getName().getLocalPart());
        assertEquals("z", type.getAttributeDescriptors().get(3).getName().getLocalPart());
        assertEquals("runtime", type.getAttributeDescriptors().get(4).getName().getLocalPart());
    }

    @Test
    public void testAvailability() {
        NetCDFAuxiliaryStoreFactory fac = new NetCDFAuxiliaryStoreFactory();

        assertFalse(fac.isAvailable());

        System.setProperty(NetCDFAuxiliaryStoreFactory.AUXILIARY_STORE_KEY, "true");

        assertTrue(fac.isAvailable());

        System.clearProperty(NetCDFAuxiliaryStoreFactory.AUXILIARY_STORE_KEY);

        assertFalse(fac.isAvailable());
    }
}
