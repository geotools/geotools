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
 *
 */
package org.geotools.gce.arcgrid;

import java.util.Iterator;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.coverage.grid.io.GridFormatFactorySpi;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.junit.Assert;
import org.junit.Test;

/**
 * Class for testing availaibility of arcgrid format factory
 *
 * @author Simone Giannecchini
 * @author ian
 */
public class ServiceTest {

    @Test
    public void testIsAvailable() throws NoSuchAuthorityCodeException, FactoryException {
        GridFormatFinder.scanForPlugins();
        Iterator<GridFormatFactorySpi> list = GridFormatFinder.getAvailableFormats().iterator();
        boolean found = false;
        GridFormatFactorySpi fac = null;
        while (list.hasNext()) {
            fac = list.next();

            if (fac instanceof ArcGridFormatFactory) {
                found = true;

                break;
            }
        }

        Assert.assertTrue("ArcGridFormatFactory not registered", found);
        Assert.assertTrue("ArcGridFormatFactory not available", fac.isAvailable());
        Assert.assertNotNull(new ArcGridFormatFactory().createFormat());
    }
}
