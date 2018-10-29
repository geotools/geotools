package org.geotools.renderer.style;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.function.EnvFunction;
import org.geotools.styling.*;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.PropertyName;

public class StyleAttributeExtractorTest {
    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
    StyleBuilder sb = new StyleBuilder(ff);

    @Test
    public void testPlainFilter() {
        Filter f = ff.greater(ff.property("attribute"), ff.literal(10));
        Rule r = sb.createRule(sb.createPointSymbolizer());
        r.setFilter(f);

        StyleAttributeExtractor extractor = new StyleAttributeExtractor();
        r.accept(extractor);

        Set<String> atts = extractor.getAttributeNameSet();
        assertTrue(atts.contains("attribute"));
        assertEquals(1, atts.size());
        assertTrue(extractor.getDefaultGeometryUsed());
    }

    @Test
    public void testGeometryTransformation() {
        PointSymbolizer ps = sb.createPointSymbolizer();
        ps.setGeometry(
                ff.function(
                        "offset",
                        ff.property("the_geom"),
                        ff.property("offx"),
                        ff.property("offy")));
        Rule r = sb.createRule(ps);

        StyleAttributeExtractor extractor = new StyleAttributeExtractor();
        r.accept(extractor);

        Set<String> atts = extractor.getAttributeNameSet();
        assertEquals(3, atts.size());
        assertTrue(atts.contains("the_geom"));
        assertTrue(atts.contains("offx"));
        assertTrue(atts.contains("offy"));
        assertFalse(extractor.getDefaultGeometryUsed());
    }

    @Test
    public void testPropertyFucntion() {
        PointSymbolizer ps = sb.createPointSymbolizer();
        ps.setGeometry(
                ff.function(
                        "offset",
                        ff.property("the_geom"),
                        ff.property("offx"),
                        ff.property("offy")));
        Function func = ff.function("property", ff.function("env", ff.literal("pname")));
        PropertyIsEqualTo filter = ff.equals(func, ff.literal("test"));
        Rule r = sb.createRule(ps);
        r.setFilter(filter);

        try {
            EnvFunction.setLocalValue("pname", "name");
            StyleAttributeExtractor extractor = new StyleAttributeExtractor();
            r.accept(extractor);

            // check the plain names
            Set<String> atts = extractor.getAttributeNameSet();
            assertEquals(4, atts.size());
            assertTrue(atts.contains("the_geom"));
            assertTrue(atts.contains("offx"));
            assertTrue(atts.contains("offy"));
            assertTrue(atts.contains("name"));
            assertFalse(extractor.getDefaultGeometryUsed());

            // checks also the property names, see they are consistent
            Set<PropertyName> propNames = extractor.getAttributes();
            assertNotNull(propNames);
            assertEquals(atts.size(), propNames.size());

            for (PropertyName pn : propNames) {
                assertTrue(atts.contains(pn.getPropertyName()));
            }
        } finally {
            EnvFunction.clearLocalValues();
        }
    }

    public void testGraphicAnchor() {
        Graphic g = sb.createGraphic();
        g.setAnchorPoint(sb.createAnchorPoint(ff.property("ax"), ff.property("ay")));
        StyleAttributeExtractor extractor = new StyleAttributeExtractor();
        g.accept(extractor);

        // check the plain names
        Set<String> atts = extractor.getAttributeNameSet();
        assertEquals(2, atts.size());
        assertTrue(atts.contains("ax"));
        assertTrue(atts.contains("ay"));
    }

    @Test
    public void testLineSymbolizer() {
        // Try with property
        Expression expression = ff.property("attribute");

        LineSymbolizer lineSymbolizer = sb.createLineSymbolizer();
        lineSymbolizer.setPerpendicularOffset(expression);
        Rule r = sb.createRule(lineSymbolizer);

        StyleAttributeExtractor extractor = new StyleAttributeExtractor();
        r.accept(extractor);

        Set<String> atts = extractor.getAttributeNameSet();
        assertTrue(atts.contains("attribute"));
        assertEquals(1, atts.size());

        // Try without property but with a literal value
        lineSymbolizer.setPerpendicularOffset(ff.literal(34.12));
        extractor = new StyleAttributeExtractor();
        r.accept(extractor);

        atts = extractor.getAttributeNameSet();
        assertEquals(0, atts.size());
    }
}
