/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.sld.bindings;

import org.geotools.styling.Rule;


public class SLDRuleBindingTest extends SLDTestSupport {
    public void testType() throws Exception {
        assertEquals(Rule.class, new SLDRuleBinding(null).getType());
    }

    public void test() throws Exception {
        SLDMockData.rule(document, document);

        Rule rule = (Rule) parse();

        assertNotNull(rule);
        assertEquals("theName", rule.getName());
        assertEquals("theAbstract", rule.getAbstract());
        assertEquals("theTitle", rule.getTitle());

        assertEquals(1, rule.getLegendGraphic().length);
        assertEquals(1d, rule.getMinScaleDenominator(), 0d);
        assertEquals(1d, rule.getMaxScaleDenominator(), 0d);
        assertEquals(5, rule.getSymbolizers().length);
    }
}
