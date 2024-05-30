/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.expression;

import static org.junit.Assert.assertTrue;

import java.util.Collections;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;
import org.opengis.filter.expression.PropertyName;

public class MapPropertyAccessorTest {

    @Test
    public void testAccessWithJavaMethod() {
        PropertyName property =
                CommonFactoryFinder.getFilterFactory().property("java.lang.Thread.sleep(30000)");
        long start = System.currentTimeMillis();
        property.evaluate(Collections.emptyMap());
        long runtime = System.currentTimeMillis() - start;
        assertTrue("java.lang.Thread.sleep(30000) was executed", runtime < 30000);
    }
}
