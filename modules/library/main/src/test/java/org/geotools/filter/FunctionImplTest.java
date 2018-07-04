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

import java.util.Collections;
import java.util.Date;
import junit.framework.TestCase;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.parameter.Parameter;

/** @source $URL$ */
public class FunctionImplTest extends TestCase {

    public void testFunctionName() throws Exception {
        FunctionName fn =
                FunctionImpl.functionName(
                        "foo",
                        "bar:Integer",
                        "a",
                        "x:String:1,1",
                        "y:MultiPolygon",
                        "z:java.util.Date:1,");

        assertEquals("foo", fn.getName());
        check(fn.getReturn(), "bar", Integer.class, 1, 1);
        check(fn.getArguments().get(0), "a", Object.class, 1, 1);
        check(fn.getArguments().get(1), "x", String.class, 1, 1);
        check(fn.getArguments().get(2), "y", MultiPolygon.class, 1, 1);
        check(fn.getArguments().get(3), "z", Date.class, 1, -1);

        fn = FunctionImpl.functionName("foo", "a", "geom::1,1", "b:Object:,");
        check(fn.getArguments().get(0), "geom", Geometry.class, 1, 1);
        check(fn.getArguments().get(1), "b", Object.class, -1, -1);

        fn = FunctionImpl.functionName("foo", "value", "geom::,");
        check(fn.getArguments().get(0), "geom", Geometry.class, -1, -1);
    }

    public void testToString() throws Exception {
        FunctionImpl func = new FunctionImpl();
        Expression param = new LiteralExpressionImpl(42);
        func.setName("TestFunction");
        func.setParameters(Collections.singletonList(param));

        String result = func.toString();
        assertEquals("TestFunction([42])", result);
    }

    void check(Parameter p, String name, Class type, int min, int max) {
        assertEquals(name, p.getName());
        assertEquals(type, p.getType());
        assertEquals(min, p.getMinOccurs());
        assertEquals(max, p.getMaxOccurs());
    }
}
