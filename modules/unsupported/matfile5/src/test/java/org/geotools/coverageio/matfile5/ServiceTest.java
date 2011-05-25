/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverageio.matfile5;

import java.util.Iterator;

import org.geotools.coverage.grid.io.GridFormatFactorySpi;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.junit.Test;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

/**
 * Class for testing availability of SAS Tile format factory
 * 
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini (simboss), GeoSolutions
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/matfile5/src/test/java/org/geotools/coverageio/matfile5/ServiceTest.java $
 */
public class ServiceTest extends AbstractSASTestCase {

    @Test
    public void testIsAvailable() throws NoSuchAuthorityCodeException,
            FactoryException {
        if (!testingEnabled()) {
            return;
        }

        GridFormatFinder.scanForPlugins();

        Iterator<GridFormatFactorySpi> list = GridFormatFinder.getAvailableFormats().iterator();
        boolean found = false;
        GridFormatFactorySpi fac = null;

        while (list.hasNext()) {
            fac = (GridFormatFactorySpi) list.next();

            if (fac instanceof MatFile5FormatFactory) {
                found = true;

                break;
            }
        }

        assertTrue("MatFile5FormatFactory not registered", found);
        assertTrue("MatFile5FormatFactory not available", fac.isAvailable());
        assertNotNull(new MatFile5FormatFactory().createFormat());
    }
}
