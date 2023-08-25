package org.geotools.styling.visitor;

import static org.geotools.api.style.FeatureTypeStyle.RenderingSelectionOptions.LEGENDONLY;
import static org.geotools.api.style.FeatureTypeStyle.RenderingSelectionOptions.MAPONLY;
import static org.geotools.api.style.FeatureTypeStyle.RenderingSelectionOptions.NORMAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Arrays;
import java.util.List;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.style.FeatureTypeStyle;
import org.geotools.api.style.Rule;
import org.geotools.api.style.Symbolizer;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.*;
import org.junit.Before;
import org.junit.Test;

public class RenderingSelectorStyleVisitorTestImpl {

    StyleBuilder sb;
    StyleFactory sf;
    FilterFactory ff;

    @Before
    public void setUp() throws Exception {
        sf = CommonFactoryFinder.getStyleFactory(null);
        ff = CommonFactoryFinder.getFilterFactory(null);
        sb = new StyleBuilder(sf, ff);
    }

    @Test
    public void testLegendRenderingSelection() {

        StyleImpl noOption = buildStyleWithInclusionOption(null);

        RenderingSelectorStyleVisitor selectorStyleVisitor =
                new LegendRenderingSelectorStyleVisitor();

        selectorStyleVisitor.visit(noOption);
        assertEquals(noOption, selectorStyleVisitor.getCopy());

        StyleImpl normal = buildStyleWithInclusionOption(NORMAL.getOption());

        selectorStyleVisitor = new LegendRenderingSelectorStyleVisitor();
        selectorStyleVisitor.visit(normal);
        assertEquals(normal, selectorStyleVisitor.getCopy());

        StyleImpl legendOnly = buildStyleWithInclusionOption(LEGENDONLY.getOption());

        selectorStyleVisitor = new LegendRenderingSelectorStyleVisitor();

        selectorStyleVisitor.visit(legendOnly);
        assertEquals(legendOnly, selectorStyleVisitor.getCopy());

        StyleImpl mapOnly = buildStyleWithInclusionOption(MAPONLY.getOption());

        selectorStyleVisitor = new LegendRenderingSelectorStyleVisitor();

        selectorStyleVisitor.visit(mapOnly);

        StyleImpl renderingSelected = (StyleImpl) selectorStyleVisitor.getCopy();
        assertNotEquals(mapOnly, renderingSelected);

        List<FeatureTypeStyle> featureTypeStyles = renderingSelected.featureTypeStyles();
        // original had 2
        assertEquals(1, featureTypeStyles.size());

        List<Rule> rules = featureTypeStyles.get(0).rules();
        // original had 2
        assertEquals(1, rules.size());

        RuleImpl rule = (RuleImpl) rules.get(1);
        List<Symbolizer> symbolizers = rule.symbolizers();
        // original had 2
        assertEquals(symbolizers.size(), 1);
    }

    @Test
    public void testMapRenderingSelection() {

        StyleImpl noOption = buildStyleWithInclusionOption(null);

        RenderingSelectorStyleVisitor selectorStyleVisitor = new MapRenderingSelectorStyleVisitor();

        selectorStyleVisitor.visit(noOption);
        assertEquals(noOption, selectorStyleVisitor.getCopy());

        StyleImpl normal = buildStyleWithInclusionOption(NORMAL.getOption());

        selectorStyleVisitor = new MapRenderingSelectorStyleVisitor();
        selectorStyleVisitor.visit(normal);
        assertEquals(normal, selectorStyleVisitor.getCopy());

        StyleImpl mapOnly = buildStyleWithInclusionOption(MAPONLY.getOption());

        selectorStyleVisitor = new MapRenderingSelectorStyleVisitor();

        selectorStyleVisitor.visit(mapOnly);
        assertEquals(mapOnly, selectorStyleVisitor.getCopy());

        StyleImpl legendOnly = buildStyleWithInclusionOption(LEGENDONLY.getOption());
        selectorStyleVisitor = new MapRenderingSelectorStyleVisitor();

        selectorStyleVisitor.visit(legendOnly);
        StyleImpl renderingSelected = (StyleImpl) selectorStyleVisitor.getCopy();

        assertNotEquals(legendOnly, renderingSelected);

        List<FeatureTypeStyle> featureTypeStyles = renderingSelected.featureTypeStyles();
        // original had 2
        assertEquals(1, featureTypeStyles.size());

        List<Rule> rules = featureTypeStyles.get(0).rules();
        // original had 2
        assertEquals(1, rules.size());

        List<Symbolizer> symbolizers = (List<Symbolizer>) rules.get(0).symbolizers();
        // original had 2
        assertEquals(symbolizers.size(), 1);
    }

    private StyleImpl buildStyleWithInclusionOption(String inclusionValue) {
        StyleImpl style = sb.createStyle("FTSName", sf.createPolygonSymbolizer());
        if (inclusionValue != null)
            style.featureTypeStyles()
                    .get(0)
                    .getOptions()
                    .put(
                            org.geotools.styling.FeatureTypeStyleImpl.VENDOR_OPTION_INCLUSION,
                            inclusionValue);

        RuleImpl rule = (RuleImpl) sf.createRule();
        Symbolizer symb1 = sf.createLineSymbolizer(sf.getDefaultStroke(), "geometry");
        rule.symbolizers().add(symb1);
        if (inclusionValue != null)
            symb1.getOptions()
                    .put(
                            org.geotools.styling.FeatureTypeStyleImpl.VENDOR_OPTION_INCLUSION,
                            inclusionValue);

        RuleImpl rule2 = (RuleImpl) sf.createRule();
        Symbolizer symb2 =
                sf.createPolygonSymbolizer(sf.getDefaultStroke(), sf.getDefaultFill(), "shape");
        rule2.symbolizers().add(symb2);
        Symbolizer symb3 =
                sf.createPolygonSymbolizer(sf.getDefaultStroke(), sf.getDefaultFill(), "extension");
        if (inclusionValue != null)
            symb3.getOptions()
                    .put(
                            org.geotools.styling.FeatureTypeStyleImpl.VENDOR_OPTION_INCLUSION,
                            inclusionValue);
        rule2.symbolizers().add(symb3);

        FeatureTypeStyleImpl fts = sf.createFeatureTypeStyle();
        fts.rules().addAll(Arrays.asList(rule, rule2));
        style.featureTypeStyles().add(fts);

        return style;
    }
}
