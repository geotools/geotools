/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2016, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */

package org.geotools.gce.imagemosaic;

import static org.junit.Assert.assertNotNull;

import java.util.Map;
import org.geotools.gce.imagemosaic.granulehandler.DefaultGranuleHandler;
import org.geotools.gce.imagemosaic.granulehandler.DefaultGranuleHandlerFactory;
import org.geotools.gce.imagemosaic.granulehandler.GranuleHandler;
import org.geotools.gce.imagemosaic.granulehandler.GranuleHandlerFactoryFinder;
import org.geotools.gce.imagemosaic.granulehandler.GranuleHandlerFactorySPI;
import org.junit.Assert;
import org.junit.Test;

/** Testing that granule handler correctly get configured and initialized */
public class GranuleHandlerSPITest {

    @Test
    public void basicTest() {
        // get the SPIs
        Map<String, GranuleHandlerFactorySPI> spiMap =
                GranuleHandlerFactoryFinder.getGranuleHandlersSPI();

        // make sure it is not empty
        assertNotNull(spiMap);
        Assert.assertTrue(!spiMap.isEmpty());

        // check the default one is there
        Assert.assertTrue(spiMap.containsKey(DefaultGranuleHandlerFactory.class.getName()));

        // check the content

        // DefaultGranuleHandlerFactory
        assertNotNull(spiMap.get(DefaultGranuleHandlerFactory.class.getName()));
        GranuleHandlerFactorySPI spi = spiMap.get(DefaultGranuleHandlerFactory.class.getName());
        GranuleHandler handler = spi.create();
        assertNotNull(handler);
        Assert.assertTrue(handler.getClass().equals(DefaultGranuleHandler.class));
    }
}
