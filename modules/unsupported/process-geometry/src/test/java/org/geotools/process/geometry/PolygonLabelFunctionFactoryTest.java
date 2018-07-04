/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.geometry;

import static org.junit.Assert.*;

import java.util.List;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FunctionFactory;
import org.junit.Before;
import org.junit.Test;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;

/** @author ian */
public class PolygonLabelFunctionFactoryTest {

    /** @throws java.lang.Exception */
    @Before
    public void setUp() throws Exception {}

    /**
     * Test method for {@link
     * org.geotools.process.geometry.PolygonLabelFunctionFactory#getFunctionNames()}.
     */
    @Test
    public void testGetFunctionNames() {
        FunctionFactory ff = new PolygonLabelFunctionFactory();
        List<FunctionName> names = ff.getFunctionNames();
        assertEquals("labelPoint", names.get(0).toString());
    }

    @Test
    public void testFactory() {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        PropertyName geom = ff.property("the_geom");
        Literal d = ff.literal(1.0);
        Function func = ff.function("labelPoint", new Expression[] {geom, d});
        assertNotNull(func);
    }
}
