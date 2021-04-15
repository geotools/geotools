package org.geotools.styling.visitor;

import static org.geotools.styling.FeatureTypeStyle.RenderingSelectionOptions.LEGENDONLY;
import static org.geotools.styling.FeatureTypeStyle.RenderingSelectionOptions.MAPONLY;
import static org.geotools.styling.FeatureTypeStyle.RenderingSelectionOptions.NORMAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Arrays;
import java.util.List;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbolizer;
import org.junit.Before;
import org.junit.Test;
import org.opengis.filter.FilterFactory2;

public class RenderingSelectorStyleVisitorTest {

    StyleBuilder sb;
    StyleFactory sf;
    FilterFactory2 ff;

    @Before
    public void setUp() throws Exception {
        sf = CommonFactoryFinder.getStyleFactory(null);
        ff = CommonFactoryFinder.getFilterFactory2(null);
        sb = new StyleBuilder(sf, ff);
    }

    @Test
    public void testLegendRenderingSelection() {

        Style noOption = buildStyleWithInclusionOption(null);

        RenderingSelectorStyleVisitor selectorStyleVisitor =
                new LegendRenderingSelectorStyleVisitor();

        selectorStyleVisitor.visit(noOption);
        assertEquals(noOption, selectorStyleVisitor.getCopy());

        Style normal = buildStyleWithInclusionOption(NORMAL.getOption());

        selectorStyleVisitor = new LegendRenderingSelectorStyleVisitor();
        selectorStyleVisitor.visit(normal);
        assertEquals(normal, selectorStyleVisitor.getCopy());

        Style legendOnly = buildStyleWithInclusionOption(LEGENDONLY.getOption());

        selectorStyleVisitor = new LegendRenderingSelectorStyleVisitor();

        selectorStyleVisitor.visit(legendOnly);
        assertEquals(legendOnly, selectorStyleVisitor.getCopy());

        Style mapOnly = buildStyleWithInclusionOption(MAPONLY.getOption());

        selectorStyleVisitor = new LegendRenderingSelectorStyleVisitor();

        selectorStyleVisitor.visit(mapOnly);

        Style renderingSelected = (Style) selectorStyleVisitor.getCopy();
        assertNotEquals(mapOnly, renderingSelected);

        List<FeatureTypeStyle> featureTypeStyles = renderingSelected.featureTypeStyles();
        // original had 2
        assertEquals(1, featureTypeStyles.size());

        List<Rule> rules = featureTypeStyles.get(0).rules();
        // original had 2
        assertEquals(1, rules.size());

        List<Symbolizer> symbolizers = rules.get(0).symbolizers();
        // original had 2
        assertEquals(symbolizers.size(), 1);
    }

    @Test
    public void testMapRenderingSelection() {

        Style noOption = buildStyleWithInclusionOption(null);

        RenderingSelectorStyleVisitor selectorStyleVisitor = new MapRenderingSelectorStyleVisitor();

        selectorStyleVisitor.visit(noOption);
        assertEquals(noOption, selectorStyleVisitor.getCopy());

        Style normal = buildStyleWithInclusionOption(NORMAL.getOption());

        selectorStyleVisitor = new MapRenderingSelectorStyleVisitor();
        selectorStyleVisitor.visit(normal);
        assertEquals(normal, selectorStyleVisitor.getCopy());

        Style mapOnly = buildStyleWithInclusionOption(MAPONLY.getOption());

        selectorStyleVisitor = new MapRenderingSelectorStyleVisitor();

        selectorStyleVisitor.visit(mapOnly);
        assertEquals(mapOnly, selectorStyleVisitor.getCopy());

        Style legendOnly = buildStyleWithInclusionOption(LEGENDONLY.getOption());
        selectorStyleVisitor = new MapRenderingSelectorStyleVisitor();

        selectorStyleVisitor.visit(legendOnly);
        Style renderingSelected = (Style) selectorStyleVisitor.getCopy();

        assertNotEquals(legendOnly, renderingSelected);

        List<FeatureTypeStyle> featureTypeStyles = renderingSelected.featureTypeStyles();
        // original had 2
        assertEquals(1, featureTypeStyles.size());

        List<Rule> rules = featureTypeStyles.get(0).rules();
        // original had 2
        assertEquals(1, rules.size());

        List<Symbolizer> symbolizers = rules.get(0).symbolizers();
        // original had 2
        assertEquals(symbolizers.size(), 1);
    }

    private Style buildStyleWithInclusionOption(String inclusionValue) {
        Style style = sb.createStyle("FTSName", sf.createPolygonSymbolizer());
        if (inclusionValue != null)
            style.featureTypeStyles()
                    .get(0)
                    .getOptions()
                    .put(FeatureTypeStyle.VENDOR_OPTION_INCLUSION, inclusionValue);

        Rule rule = sf.createRule();
        Symbolizer symb1 = sf.createLineSymbolizer(sf.getDefaultStroke(), "geometry");
        rule.symbolizers().add(symb1);
        if (inclusionValue != null)
            rule.getOptions().put(FeatureTypeStyle.VENDOR_OPTION_INCLUSION, inclusionValue);

        Rule rule2 = sf.createRule();
        Symbolizer symb2 =
                sf.createPolygonSymbolizer(sf.getDefaultStroke(), sf.getDefaultFill(), "shape");
        rule2.symbolizers().add(symb2);
        Symbolizer symb3 =
                sf.createPolygonSymbolizer(sf.getDefaultStroke(), sf.getDefaultFill(), "extension");
        if (inclusionValue != null)
            symb3.getOptions().put(FeatureTypeStyle.VENDOR_OPTION_INCLUSION, inclusionValue);
        rule2.symbolizers().add(symb3);

        FeatureTypeStyle fts = sf.createFeatureTypeStyle(rule);
        fts.rules().addAll(Arrays.asList(rule, rule2));
        style.featureTypeStyles().add(fts);

        return style;
    }
}
