/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling;

import java.util.Map;
import junit.framework.TestCase;
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.style.ContrastMethod;

/**
 * The ContrastEnhancementImpl UnitTest
 *
 * @author Jared Erickson
 */
public class ContrastEnhancementImplTest extends TestCase {

    private static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);

    /** Test of getGammaValue method, of class ContrastEnhancementImpl. */
    public void testGetSetGammaValue() {
        // System.out.println("getGammaValue");
        ContrastEnhancementImpl contrastEnhancementImpl = new ContrastEnhancementImpl();
        double expected = 1.5;
        contrastEnhancementImpl.setGammaValue(filterFactory.literal(expected));
        double actual =
                ((Double) ((Literal) contrastEnhancementImpl.getGammaValue()).getValue())
                        .doubleValue();
        assertEquals(expected, actual, 0.1);
    }

    /** Test of setMethod method, of class ContrastEnhancementImpl. */
    public void testGetSetMethod() {
        // System.out.println("setMethod");
        ContrastMethod expected = ContrastMethod.HISTOGRAM;
        ContrastEnhancementImpl contrastEnhancementImpl = new ContrastEnhancementImpl();
        contrastEnhancementImpl.setMethod(expected);
        ContrastMethod actual = contrastEnhancementImpl.getMethod();
        assertEquals(expected, actual);
    }

    public void testNormalize() {
        NormalizeContrastMethodStrategy normalize = new NormalizeContrastMethodStrategy();
        normalize.setAlgorithm(filterFactory.literal("ClipToMinimumMaximum"));
        Map<String, Expression> params = normalize.getParameters();
        assertNotNull("Null parameters returned by Normalize", params);
        normalize.addParameter("min", filterFactory.literal(45.9));
        params = normalize.getParameters();
        assertEquals("Wrong number of parameters returned", 1, params.size());
        normalize.addParameter(
                "max",
                filterFactory.function(
                        "env", filterFactory.literal("arg1"), filterFactory.literal("arg2")));
        params = normalize.getParameters();

        Expression max = params.get("max");
        assertEquals("mangled the function in normalize", "env([arg1], [arg2])", max.toString());
    }
}
