package org.geotools.styling;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.Set;

import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

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
    
    @Test
    public void testAcceptsTextSymbolizerProperty() {
        Filter f = ff.greater(ff.property("attribute"), ff.literal(10));
        Rule r = sb.createRule(new Symbolizer[]{
                sb.createPointSymbolizer(),
                sb.createTextSymbolizer(Color.BLACK, sb.createFont("Family", 11), "labelAttribute")
        });
        r.setFilter(f);

        StyleAttributeExtractor extractor = new StyleAttributeExtractor();
        r.accept(extractor);

        Set<String> atts = extractor.getAttributeNameSet();
        assertTrue(atts.contains("attribute"));
        assertTrue(atts.contains("labelAttribute"));
        assertEquals(2, atts.size());
        assertTrue(extractor.getDefaultGeometryUsed());
    }

    @Test
    public void testIgnoresTextSymbolizerProperty() {
        Filter f = ff.greater(ff.property("attribute"), ff.literal(10));
        Rule r = sb.createRule(new Symbolizer[]{
                sb.createPointSymbolizer(),
                sb.createTextSymbolizer(Color.BLACK, sb.createFont("Family", 11), "labelAttribute")
        });
        r.setFilter(f);

        StyleAttributeExtractor extractor = new StyleAttributeExtractor(true);
        r.accept(extractor);

        Set<String> atts = extractor.getAttributeNameSet();
        assertTrue(atts.contains("attribute"));
        assertEquals(1, atts.size());
        assertTrue(extractor.getDefaultGeometryUsed());
    }

    @Test
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

}
