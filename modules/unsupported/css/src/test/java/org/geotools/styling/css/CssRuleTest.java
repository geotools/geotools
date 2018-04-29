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
import static org.junit.Assert.assertNull;

import java.util.Iterator;
import java.util.Set;
import org.geotools.styling.css.selector.PseudoClass;
import org.junit.Test;

public class CssRuleTest extends CssBaseTest {

    @Test
    public void testNoZIndexes() {
        CssRule rule = parseSingleRule("* {fill:black; }");
        // just the base level
        Set<Integer> zeds = rule.getZIndexes();
        assertEquals(1, zeds.size());
        assertEquals(CssRule.NO_Z_INDEX, zeds.iterator().next());
        // the subrule is the rule
        CssRule subRule = rule.getSubRuleByZIndex(0);
        assertEquals(subRule, rule);
        // and since this is index independent, any other level will get the same rule
        assertEquals(rule, rule.getSubRuleByZIndex(1));
    }

    @Test
    public void testSingleZIndex() {
        CssRule rule = parseSingleRule("* {fill:black; z-index: 10;}");
        // just the declared level
        Set<Integer> zeds = rule.getZIndexes();
        assertEquals(1, zeds.size());
        assertEquals(new Integer(10), zeds.iterator().next());
        CssRule subRule = rule.getSubRuleByZIndex(10);
        assertEquals(1, subRule.getProperties().size());
        assertEquals(2, subRule.getProperties().get(PseudoClass.ROOT).size());
        assertProperty(subRule, 0, "fill", new Value.Literal("#000000"));
        assertProperty(subRule, 1, "z-index", new Value.Literal("10"));
        // nothing at the base level
        assertNull(rule.getSubRuleByZIndex(0));
    }

    @Test
    public void testSaneMultiIndex() {
        CssRule rule = parseSingleRule("* {fill:black, red; z-index: 1, 3;}");
        // just the declared level
        Set<Integer> zeds = rule.getZIndexes();
        assertEquals(2, zeds.size());
        Iterator<Integer> it = zeds.iterator();
        assertEquals(new Integer(1), it.next());
        assertEquals(new Integer(3), it.next());
        // nothing at the base level
        assertNull(rule.getSubRuleByZIndex(0));
        // first subrule
        CssRule subRule1 = rule.getSubRuleByZIndex(1);
        assertEquals(1, subRule1.getProperties().size());
        assertEquals(2, subRule1.getProperties().get(PseudoClass.ROOT).size());
        assertProperty(subRule1, 0, "fill", new Value.Literal("#000000"));
        assertProperty(subRule1, 1, "z-index", new Value.Literal("1"));
        // nothing at level 2
        assertNull(rule.getSubRuleByZIndex(2));
        // second subrule
        CssRule subRule3 = rule.getSubRuleByZIndex(3);
        assertEquals(1, subRule3.getProperties().size());
        assertEquals(2, subRule3.getProperties().get(PseudoClass.ROOT).size());
        assertProperty(subRule3, 0, "fill", new Value.Literal("#ff0000"));
        assertProperty(subRule3, 1, "z-index", new Value.Literal("3"));
    }

    @Test
    public void testReversedMultiIndex() {
        CssRule rule = parseSingleRule("* {fill:black, red; z-index: 3, 1;}");
        // just the declared level
        Set<Integer> zeds = rule.getZIndexes();
        assertEquals(2, zeds.size());
        Iterator<Integer> it = zeds.iterator();
        assertEquals(new Integer(1), it.next());
        assertEquals(new Integer(3), it.next());
        // nothing at the base level
        assertNull(rule.getSubRuleByZIndex(0));
        // first subrule
        CssRule subRule1 = rule.getSubRuleByZIndex(1);
        assertEquals(1, subRule1.getProperties().size());
        assertEquals(2, subRule1.getProperties().get(PseudoClass.ROOT).size());
        assertProperty(subRule1, 0, "fill", new Value.Literal("#ff0000"));
        assertProperty(subRule1, 1, "z-index", new Value.Literal("1"));
        // nothing at level 2
        assertNull(rule.getSubRuleByZIndex(2));
        // second subrule
        CssRule subRule3 = rule.getSubRuleByZIndex(3);
        assertEquals(1, subRule3.getProperties().size());
        assertEquals(2, subRule3.getProperties().get(PseudoClass.ROOT).size());
        assertProperty(subRule3, 0, "fill", new Value.Literal("#000000"));
        assertProperty(subRule3, 1, "z-index", new Value.Literal("3"));
    }

    private CssRule parseSingleRule(String css) {
        Stylesheet ss = parse(css);
        assertEquals(1, ss.getRules().size());
        return ss.getRules().get(0);
    }
}
