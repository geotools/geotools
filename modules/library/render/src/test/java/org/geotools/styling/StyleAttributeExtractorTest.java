package org.geotools.styling;

import static org.junit.Assert.*;

import java.util.Set;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.function.EnvFunction;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.PropertyName;

/**
 * 
 *
 * @source $URL$
 */
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
    
    public void testGeometryTransformation() {
        PointSymbolizer ps = sb.createPointSymbolizer();
        ps.setGeometry(ff.function("offset", ff.property("the_geom"), ff.property("offx"), ff.property("offy")));
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
    
    public void testPropertyFucntion() {
        PointSymbolizer ps = sb.createPointSymbolizer();
        ps.setGeometry(ff.function("offset", ff.property("the_geom"), ff.property("offx"), ff.property("offy")));
        Function func = ff.function("property", ff.function("env", ff.literal("pname")));
        PropertyIsEqualTo filter = ff.equals(func, ff.literal("test"));
        Rule r = sb.createRule(ps);
        r.setFilter(filter);
        
        try  {
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

}
