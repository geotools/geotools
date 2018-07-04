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

package org.geotools.styling.css.util;

import static org.junit.Assert.assertEquals;

import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.styling.css.selector.Data;
import org.geotools.styling.css.selector.Or;
import org.geotools.styling.css.selector.ScaleRange;
import org.geotools.styling.css.selector.Selector;
import org.geotools.styling.css.selector.TypeName;
import org.geotools.util.NumberRange;
import org.junit.Test;

public class TypeNameSimplifierTest {

    @Test
    public void testBasicTypeName() {
        TypeNameSimplifier simplifier = new TypeNameSimplifier(new TypeName("abc"));
        assertEquals(Selector.ACCEPT, new TypeName("abc").accept(simplifier));
        assertEquals(Selector.REJECT, new TypeName("other").accept(simplifier));
        assertEquals(Selector.REJECT, TypeName.DEFAULT.accept(simplifier));
    }

    @Test
    public void testDefaultTypeName() {
        TypeNameSimplifier simplifier = new TypeNameSimplifier(TypeName.DEFAULT);
        assertEquals(Selector.REJECT, new TypeName("abc").accept(simplifier));
        assertEquals(Selector.REJECT, new TypeName("other").accept(simplifier));
        assertEquals(Selector.ACCEPT, TypeName.DEFAULT.accept(simplifier));
    }

    @Test
    public void testComplex() throws CQLException {
        // one selector with typename
        Data code2 = new Data(ECQL.toFilter("code = '2'"));
        TypeName restricted = new TypeName("restricted");
        Selector s1 = Selector.and(restricted, code2);
        // two without
        Selector s2 = new Data(ECQL.toFilter("code = '3'"));
        Selector s3 =
                Selector.and(
                        new Data(ECQL.toFilter("code = '4'")),
                        new ScaleRange(new NumberRange<Double>(Double.class, 10000d, 20000d)));

        Selector combined = new Or(s1, s2, s3);

        // visit with default typename as the target, only the rules with default will match
        TypeNameSimplifier simplifier = new TypeNameSimplifier(TypeName.DEFAULT);
        Selector defaultSelector = (Selector) combined.accept(simplifier);
        assertEquals(new Or(s2, s3), defaultSelector);

        // now with restricted as the typename, rules with restricted as well as those with no
        // typename will match
        simplifier = new TypeNameSimplifier(restricted);
        Selector restrictedSelector = (Selector) combined.accept(simplifier);
        assertEquals(new Or(code2, s2, s3), restrictedSelector);
    }
}
