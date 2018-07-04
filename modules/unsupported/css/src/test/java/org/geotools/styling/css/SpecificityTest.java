/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.styling.css;

import static org.junit.Assert.assertEquals;

import org.geotools.styling.css.selector.Data;
import org.geotools.styling.css.selector.Id;
import org.geotools.styling.css.selector.Or;
import org.geotools.styling.css.selector.PseudoClass;
import org.geotools.styling.css.selector.ScaleRange;
import org.geotools.styling.css.selector.Selector;
import org.geotools.styling.css.selector.Specificity;
import org.geotools.styling.css.selector.TypeName;
import org.junit.Test;

public class SpecificityTest {

    public static Selector ALL = Selector.ACCEPT;

    public static Selector ID = new Id("states.1");

    public static Selector ID_COUPLE = new Id("states.1", "states.2");

    public static Selector TYPENAME = new TypeName("topp:states");

    public static Selector PSEUDO = PseudoClass.newPseudoClass("mark");

    public static Selector PSEUDO_NTH = PseudoClass.newPseudoClass("mark", 1);

    public static Selector SCALE_MIN_10000 =
            new ScaleRange(10000, true, Double.POSITIVE_INFINITY, false);

    public static Selector PROPERTY = new Data("att > 10");

    public static Selector PROPERTY_TWICE = new Data("att > 10 and att < 20");

    public static Selector PROPERTY_TWO = new Data("att1 > 10 and att2 < 20");

    public static Selector ENV = new Data("env('abc') = 10");

    public static Selector VOLATILE = new Data("random() = 0.5");

    private void assertSpecificity(int b, int c, int d, Selector selector) {
        assertEquals(new Specificity(b, c, d), selector.getSpecificity());
    }

    @Test
    public void testAll() {
        assertSpecificity(0, 0, 0, ALL);
    }

    @Test
    public void testId() {
        assertSpecificity(1, 0, 0, ID);
    }

    @Test
    public void testId2() {
        assertSpecificity(2, 0, 0, ID_COUPLE);
    }

    @Test
    public void testTypename() {
        assertSpecificity(0, 0, 1, TYPENAME);
    }

    @Test
    public void testPseudo() {
        assertSpecificity(0, 1, 0, PSEUDO);
    }

    @Test
    public void testPseudoNth() {
        assertSpecificity(0, 2, 0, PSEUDO_NTH);
    }

    @Test
    public void testScaleMin10k() {
        assertSpecificity(0, 1, 0, SCALE_MIN_10000);
    }

    @Test
    public void testProperty() {
        assertSpecificity(0, 1, 0, PROPERTY);
    }

    @Test
    public void testPropertyTwice() {
        assertSpecificity(0, 1, 0, PROPERTY_TWICE);
    }

    @Test
    public void testTwoProperties() {
        assertSpecificity(0, 2, 0, PROPERTY_TWO);
    }

    @Test
    public void testEnvVariable() {
        assertSpecificity(0, 1, 0, ENV);
    }

    @Test
    public void testVolatile() {
        assertSpecificity(0, 1, 0, VOLATILE);
    }

    @Test
    public void testAnd() {
        assertSpecificity(0, 1, 0, Selector.and(ALL, PROPERTY));
        assertSpecificity(0, 2, 0, Selector.and(ENV, PROPERTY));
        assertSpecificity(1, 1, 0, Selector.and(ID, ENV));
        assertSpecificity(1, 1, 1, Selector.and(ID, Selector.and(ENV, TYPENAME)));
    }

    @Test
    public void testOr() {
        assertSpecificity(0, 1, 0, new Or(ALL, PROPERTY));
        assertSpecificity(0, 1, 0, new Or(ENV, PROPERTY));
        assertSpecificity(1, 0, 0, new Or(ID, ENV));
        assertSpecificity(1, 0, 0, new Or(ID, Selector.and(ENV, TYPENAME)));
    }
}
