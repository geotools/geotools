/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter;

import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.filter.function.InterpolateFunction;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Function;

import static org.junit.Assert.*;

/**
 * @author jody
 *
 *
 * @source $URL$
 */
public class FunctionFinderTest {
    static org.opengis.filter.FilterFactory ff;

    FunctionFinder finder;

    Function function;

    private FunctionName name;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        ff = CommonFactoryFinder.getFilterFactory(null);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        ff = null;
    }

    @Before
    public void setUp() throws Exception {
        finder = new FunctionFinder(new Hints(Hints.FILTER_FACTORY, ff));
    }

    @After
    public void tearDown() throws Exception {
        finder = null;
    }

    @Test
    public void testAllFunctionDescriptions() throws Exception {
        List<FunctionName> all = finder.getAllFunctionDescriptions();
        assertTrue(all.size() > 0);
        boolean found = false;
        for (FunctionName name : all) {
            if (name.getName().equals("Interpolate")) {
                found = true;
                break;
            }
        }
        assertTrue("Found Interpolate", found );
    }

    @Test
    public void testFindInteropolate() throws Exception {
        function = finder.findFunction("interpolate");
        assertNotNull("interpolate", function);
        assertTrue(function instanceof InterpolateFunction);

        function = finder.findFunction("Interpolate");
        assertNotNull("Interpolate", function);
        assertTrue(function instanceof InterpolateFunction);

        function = finder.findFunction("INTERPOLATE");
        assertNotNull("INTERPOLATE", function);
        assertTrue("fallback", function instanceof InterpolateFunction);

        name = finder.findFunctionDescription("interpolate");
        assertNull("interpolate", name);

        name = finder.findFunctionDescription("Interpolate");
        assertNotNull("Interpolate", name);

        name = finder.findFunctionDescription("INTERPOLATE");
        assertNull("INTERPOLATE", name);
    }

}
