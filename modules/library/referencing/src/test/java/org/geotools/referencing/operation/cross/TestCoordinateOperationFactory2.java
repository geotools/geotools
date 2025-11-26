/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.referencing.operation.cross;

import java.io.File;
import java.net.URL;
import org.geotools.referencing.operation.PropertyCoordinateOperationFactory;
import org.geotools.util.URLs;
import org.geotools.util.factory.Hints;

/**
 * Test coordinate operation factory reading definitions from a test properties file. Uses a lower priority than
 * {@link TestCoordinateOperationFactory1}.
 */
public class TestCoordinateOperationFactory2 extends PropertyCoordinateOperationFactory {

    protected static final int PRIORITY = MAXIMUM_PRIORITY - 20;

    public TestCoordinateOperationFactory2() {
        this(null);
    }

    public TestCoordinateOperationFactory2(Hints hints) {
        super(hints, PRIORITY);
    }

    @Override
    protected URL getDefinitionsURL() {
        return URLs.fileToUrl(
                new File("./src/test/resources/org/geotools/referencing/operation/cross/test_operations2.properties"));
    }
}
