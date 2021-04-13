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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Locale;
import java.util.Map;
import org.geotools.styling.Rule;
import org.junit.Test;

public class SLDRuleBindingTest extends SLDTestSupport {
    @Test
    public void testType() throws Exception {
        assertEquals(Rule.class, new SLDRuleBinding(null).getType());
    }

    @Test
    public void test() throws Exception {
        SLDMockData.rule(document, document);

        Rule rule = (Rule) parse();

        assertNotNull(rule);
        assertEquals("theName", rule.getName());
        assertEquals("theAbstract", rule.getDescription().getAbstract().toString());
        assertEquals("theTitle", rule.getDescription().getTitle().toString());

        assertNotNull(rule.getLegend());
        assertEquals(1d, rule.getMinScaleDenominator(), 0d);
        assertEquals(1d, rule.getMaxScaleDenominator(), 0d);
        assertEquals(5, rule.symbolizers().size());
    }

    @Test
    public void testLocalized() throws Exception {
        SLDMockData.localizedRule(document, document);

        Rule rule = (Rule) parse();

        assertNotNull(rule);

        assertEquals("theTitle", rule.getDescription().getTitle().toString());
        assertEquals("english", rule.getDescription().getTitle().toString(Locale.ENGLISH));
        assertEquals("italian", rule.getDescription().getTitle().toString(Locale.ITALIAN));
    }

    @Test
    public void testVendorOptions() throws Exception {
        SLDMockData.ruleWithVendorOptions(document, document);
        Rule rule = (Rule) parse();

        assertNotNull(rule);

        Map<String, String> options = rule.getOptions();
        assertEquals(2, options.size());
        assertEquals("value", options.get("name"));
        assertEquals("value2", options.get("name2"));
    }
}
