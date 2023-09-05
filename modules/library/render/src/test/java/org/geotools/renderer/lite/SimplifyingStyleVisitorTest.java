/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite;

import static org.junit.Assert.assertEquals;

import org.geotools.api.style.LineSymbolizer;
import org.geotools.api.style.Rule;
import org.geotools.api.style.Style;
import org.geotools.filter.text.cql2.CQL;
import org.junit.Test;

public class SimplifyingStyleVisitorTest {

    @Test
    public void testSimplify() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "simplify.sld");
        SimplifyingStyleVisitor visitor = new SimplifyingStyleVisitor();
        style.accept(visitor);

        Style copy = (Style) visitor.getCopy();
        Rule rule = copy.featureTypeStyles().get(0).rules().get(0);
        // filter has been simplified
        assertEquals(CQL.toFilter("value = 11"), rule.getFilter());
        LineSymbolizer symbolizer = (LineSymbolizer) rule.symbolizers().get(0);
        assertEquals(CQL.toExpression("65536"), symbolizer.getStroke().getWidth());
    }
}
