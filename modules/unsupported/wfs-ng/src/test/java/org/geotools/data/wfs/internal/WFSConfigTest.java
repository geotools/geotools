/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.internal;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import org.geotools.data.wfs.impl.WFSDataAccessFactory;
import org.junit.Test;

public class WFSConfigTest {

    @Test
    public void testConfiguresConnectionPoolSize() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put(WFSDataAccessFactory.MAX_CONNECTION_POOL_SIZE.getName(), "10");
        WFSConfig config = WFSConfig.fromParams(params);

        assertEquals(10, config.getMaxConnectionPoolSize());
    }
}
