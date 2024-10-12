package org.geotools.brewer.styling.builder;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.awt.Color;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.style.AnchorPoint;
import org.geotools.api.style.Description;
import org.geotools.api.style.ExternalGraphic;
import org.geotools.api.style.FeatureTypeConstraint;
import org.geotools.api.style.FeatureTypeStyle;
import org.geotools.api.style.Fill;
import org.geotools.api.style.Graphic;
import org.geotools.api.style.GraphicLegend;
import org.geotools.api.style.GraphicalSymbol;
import org.geotools.api.style.Halo;
import org.geotools.api.style.Rule;
import org.geotools.api.style.Stroke;
import org.geotools.api.style.Style;
import org.geotools.api.style.StyleFactory;
import org.geotools.api.style.StyledLayerDescriptor;
import org.geotools.api.style.UserLayer;
import org.geotools.brewer.styling.filter.expression.ExpressionBuilder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.util.GrowableInternationalString;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class StyleBuilderTest {

    FilterFactory FF = CommonFactoryFinder.getFilterFactory();

    public void example() {
        StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
        StyledLayerDescriptor sld = sf.createStyledLayerDescriptor();
        sld.setName("example");
        sld.setAbstract("Example Style Layer Descriptor");

        UserLayer layer = sf.createUserLayer();
        layer.setName("layer");

        FeatureTypeConstraint constraint = sf.createFeatureTypeConstraint("Feature", Filter.INCLUDE, null);

        layer.layerFeatureConstraints().add(constraint);

        Style style = sf.createStyle();

        style.getDescription().setTitle("Style");
        style.getDescription().setAbstract("Definition of Style");

        // define feature type styles used to actually
        // define how features are rendered
        //
        layer.userStyles().add(style);

        sld.layers().add(layer);
    }

    @Test
    public void testRuleResetTitle() {
        StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
        Rule rule = sf.createRule();
        String ruleName = "Rule name";
        rule.setName(ruleName);
        String title = "Rule title";
        rule.getDescription().setTitle(title);

        Style resetStyle = new RuleBuilder().reset(rule).name(ruleName).buildStyle();
        Rule resetRule = resetStyle.featureTypeStyles().get(0).rules().get(0);

        assertEquals(resetRule.getDescription().getTitle().toString(), title);
    }

    @Test
    public void testRuleResetI18nTitle() {
        StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
        Rule rule = sf.createRule();
        String ruleName = "Rule name";
        rule.setName(ruleName);
        GrowableInternationalString title = new GrowableInternationalString();
        title.add(Locale.ENGLISH, "Title in English");
        title.add(Locale.FRENCH, "Titre en français");
        rule.getDescription().setTitle(title);

        Style resetStyle = new RuleBuilder().reset(rule).name(ruleName).buildStyle();
        Rule resetRule = resetStyle.featureTypeStyles().get(0).rules().get(0);

        assertEquals(
                rule.getDescription().getTitle().toString(Locale.ENGLISH),
                resetRule.getDescription().getTitle().toString(Locale.ENGLISH));
        assertEquals(
                rule.getDescription().getTitle().toString(Locale.FRENCH),
                resetRule.getDescription().getTitle().toString(Locale.FRENCH));
    }

    @Test
    public void testRuleResetAbstract() {
        StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
        Rule rule = sf.createRule();
        String ruleName = "Rule name";
        rule.setName(ruleName);
        String ruleAbstract = "Rule abstract";
        rule.getDescription().setAbstract(ruleAbstract);

        Style resetStyle = new RuleBuilder().reset(rule).name(ruleName).buildStyle();
        Rule resetRule = resetStyle.featureTypeStyles().get(0).rules().get(0);

        assertEquals(resetRule.getDescription().getAbstract().toString(), ruleAbstract);
    }

    @Test
    public void testRuleResetI18nAbstract() {
        StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
        Rule rule = sf.createRule();
        String ruleName = "Rule name";
        rule.setName(ruleName);
        GrowableInternationalString ruleAbstract = new GrowableInternationalString();
        ruleAbstract.add(Locale.ENGLISH, "Abstract");
        ruleAbstract.add(Locale.FRENCH, "Résumé");
        rule.getDescription().setAbstract(ruleAbstract);

        Style resetStyle = new RuleBuilder().reset(rule).name(ruleName).buildStyle();
        Rule resetRule = resetStyle.featureTypeStyles().get(0).rules().get(0);

        assertEquals(
                rule.getDescription().getAbstract().toString(Locale.ENGLISH),
                resetRule.getDescription().getAbstract().toString(Locale.ENGLISH));
        assertEquals(
                rule.getDescription().getAbstract().toString(Locale.FRENCH),
                resetRule.getDescription().getAbstract().toString(Locale.FRENCH));
    }

    @Test
    public void emailExample() {
        StyleBuilder builder = new StyleBuilder();

        RuleBuilder rule = builder.featureTypeStyle().featureTypeName("Feature").rule();
        rule.point()
                .graphic()
                .externalGraphic("file:///C:/images/house.gif", "image/gid")
                .mark()
                .name("circle");

        Style style = builder.build();

        assertNotNull(style);
    }

    @Test
    public void ftsOptions() {
        StyleBuilder builder = new StyleBuilder();

        RuleBuilder rule = builder.featureTypeStyle()
                .featureTypeName("Feature")
                .option(
                        org.geotools.api.style.FeatureTypeStyle.KEY_EVALUATION_MODE,
                        org.geotools.api.style.FeatureTypeStyle.VALUE_EVALUATION_MODE_FIRST)
                .rule();
        rule.point().graphic().mark().name("circle");

        Style style = builder.build();

        assertNotNull(style);
        FeatureTypeStyle fts = style.featureTypeStyles().get(0);
        assertEquals(1, fts.getOptions().size());
        assertEquals(
                org.geotools.api.style.FeatureTypeStyle.VALUE_EVALUATION_MODE_FIRST,
                fts.getOptions().get(org.geotools.api.style.FeatureTypeStyle.KEY_EVALUATION_MODE));
    }

    @Test
    public void ftsTransformation() {
        StyleBuilder builder = new StyleBuilder();

        // don't have a RT handy, we'll just use a random function instead
        builder.featureTypeStyle().featureTypeName("Feature").transformation(FF.function("abs", FF.literal("123")));

        Style style = builder.build();

        assertNotNull(style);
        FeatureTypeStyle fts = style.featureTypeStyles().get(0);
        Expression ex = fts.getTransformation();
        assertThat(ex, instanceOf(Function.class));
        Function tx = (Function) ex;
        assertEquals("abs", tx.getName());
        assertEquals(1, tx.getParameters().size());
        assertEquals("123", tx.getParameters().get(0).evaluate(null));
    }

    @Test
    public void anchorPoint() {
        AnchorPointBuilder b = new AnchorPointBuilder();

        AnchorPoint anchor = b.build();
        assertEquals(0.0, anchor.getAnchorPointX().evaluate(null, Double.class), 0.0);
        assertEquals(0.0, anchor.getAnchorPointY().evaluate(null, Double.class), 0.0);

        anchor = b.x(0.5).y(0.9).build();
        assertEquals(0.5, anchor.getAnchorPointX().evaluate(null, Double.class), 0.0);
        assertEquals(0.9, anchor.getAnchorPointY().evaluate(null, Double.class), 0.0);
    }

    @Test
    public void description() {
        DescriptionBuilder b = new DescriptionBuilder();
        Description d = b.build();
        assertNull(d.getTitle());
        assertNull(d.getAbstract());

        assertNull(b.unset().build());

        b = new DescriptionBuilder();
        b.description("here be dragons");
        d = b.build();

        assertEquals("here be dragons", d.getAbstract().toString());
        assertNull(b.build().getAbstract());

        b = new DescriptionBuilder();
        b.description("here be dragons");
        d = b.build();

        assertEquals("here be dragons", d.getAbstract().toString());
    }

    @Test
    public void testDisplacementBuilder() {
        DisplacementBuilder b = new DisplacementBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());
    }

    @Test
    public void testExtensionSymbolizerBuilder() {
        ExtensionSymbolizerBuilder b = new ExtensionSymbolizerBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());
    }

    @Test
    public void testExtentBuilder() {
        ExtentBuilder b = new ExtentBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());

        b = new ExtentBuilder<>(this);
    }

    @Test
    public void testExternalGraphicBuilder() {
        ExternalGraphicBuilder b = new ExternalGraphicBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());
    }

    @Test
    public void testExternalMarkBuilder() {
        ExternalMarkBuilder b = new ExternalMarkBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());
    }

    @Test
    public void testFeatureTypeConstraintBuilder() {
        FeatureTypeConstraintBuilder b = new FeatureTypeConstraintBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());
    }

    @Test
    public void testFeatureTypeStyleBuilder() {
        FeatureTypeStyleBuilder b = new FeatureTypeStyleBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());
    }

    @Test
    public void testFillBuilder() {
        FillBuilder b = new FillBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());
    }

    @Test
    public void testFontBuilder() {
        DisplacementBuilder b = new DisplacementBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());
    }

    @Test
    public void testGraphicBuilder() {
        GraphicBuilder b = new GraphicBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());
    }

    @Test
    public void testGraphicLegendBuilder() {
        GraphicLegendBuilder b = new GraphicLegendBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());
    }

    @Test
    public void testHaloBuilder() {
        HaloBuilder b = new HaloBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());
    }

    @Test
    public void testLayerFeatureConstraintsBuilder() {
        LayerFeatureConstraintsBuilder b = new LayerFeatureConstraintsBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());

        b = new LayerFeatureConstraintsBuilder<>(this);
    }

    @Test
    public void testLinePlacementBuilder() {
        LinePlacementBuilder b = new LinePlacementBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());
    }

    @Test
    public void testLineSymbolizerBuilder() {
        LineSymbolizerBuilder b = new LineSymbolizerBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());
    }

    @Test
    public void testMarkBuilder() {
        MarkBuilder b = new MarkBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());
    }

    @Test
    public void testNamedLayerBuilder() {
        NamedLayerBuilder b = new NamedLayerBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());
    }

    @Test
    public void testPointPlacementBuilder() {
        PointPlacementBuilder b = new PointPlacementBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());
    }

    @Test
    public void testPointSymbolizerBuilder() {
        PointSymbolizerBuilder b = new PointSymbolizerBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());
    }

    @Test
    public void testPolygonSymbolizerBuilder() {
        PolygonSymbolizerBuilder b = new PolygonSymbolizerBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());
    }

    @Test
    public void testRasterSymbolizerBuilder() {
        RasterSymbolizerBuilder b = new RasterSymbolizerBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());
    }

    @Test
    public void testRemoteOWSBuilder() {
        RemoteOWSBuilder b = new RemoteOWSBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().resource("localhost").service("WMS").build());
    }

    @Test
    public void testRuleBuilder() {
        RuleBuilder b = new RuleBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());
    }

    /**
     * Testing if the ExternalGraphic within LegendGraphic is kept after a call to
     * RuleBuilder.reset(Rule)
     *
     * @throws Exception
     */
    @Test
    public void testRuleBuilderWithLegendGraphicClone() throws Exception {
        org.geotools.styling.StyleBuilder builder = new org.geotools.styling.StyleBuilder();
        Graphic pointGraphic = builder.createGraphic(
                builder.createExternalGraphic("file:/point-symbolizer.png", "image/png"), null, null);
        Rule rule = builder.createRule(builder.createPointSymbolizer(pointGraphic));

        Graphic legendGraphic =
                builder.createGraphic(builder.createExternalGraphic("file:/nice-legend.png", "image/png"), null, null);

        rule.setLegend((GraphicLegend) legendGraphic);

        RuleBuilder rb = new RuleBuilder();
        Rule cloneRule = rb.reset(rule).build();
        Assert.assertEquals(1, cloneRule.getLegend().graphicalSymbols().size());
        GraphicalSymbol symbol = cloneRule.getLegend().graphicalSymbols().get(0);
        Assert.assertTrue(symbol instanceof ExternalGraphic);
        ExternalGraphic cloneExternal = (ExternalGraphic) symbol;
        Assert.assertNotNull(cloneExternal.getLocation());
        Assert.assertEquals(
                "file:/nice-legend.png",
                cloneExternal.getOnlineResource().getLinkage().toString());
    }

    @Test
    public void testSelectedChannelTypeBuilder() {
        SelectedChannelTypeBuilder b = new SelectedChannelTypeBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());
    }

    @Test
    public void testShadedReliefBuilder() {
        ShadedReliefBuilder b = new ShadedReliefBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());
    }

    @Test
    public void testStrokeBuilder() {
        StrokeBuilder b = new StrokeBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());
    }

    @Test
    public void testStrokeBuilderDashArrayExpression() {
        StrokeBuilder b = new StrokeBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());
    }

    @Test
    public void testStyleBuilder() {
        StyleBuilder b = new StyleBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());
    }

    @Test
    public void testStyledLayerDescriptorBuilder() {
        StyledLayerDescriptorBuilder b = new StyledLayerDescriptorBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());
    }

    @Test
    public void testSymbolBuilder() {
        DisplacementBuilder b = new DisplacementBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());
    }

    @Test
    public void testTextSymbolizerBuilder() {
        TextSymbolizerBuilder b = new TextSymbolizerBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());
    }

    @Test
    public void testUserLayerBuilder() {
        UserLayerBuilder b = new UserLayerBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());
    }

    @Test
    public void expression() {
        ExpressionBuilder b = new ExpressionBuilder();
        assertNotNull(b.build());
        assertNull(b.unset().build());
        assertEquals(Expression.NIL, b.reset().build());
        assertNotNull(b.reset().literal(1).build());

        assertEquals(b.unset().literal(1).build(), b.reset().literal(1).build());
    }

    @Test
    public void fill() throws Exception {
        FillBuilder b = new FillBuilder();
        Fill fill = b.color(Color.BLUE).opacity(0.75).build();
        assertNotNull(fill);
        assertNotNull(b.color);
        assertEquals(Color.BLUE, fill.getColor().evaluate(null, Color.class));
    }

    @Test
    public void halo() {
        HaloBuilder b = new HaloBuilder(null);
        Halo halo = b.build();

        assertNotNull(halo);
    }

    @Ignore
    @Test
    public void testPolygonStyle() {
        StyleBuilder sb = new StyleBuilder();

        PolygonSymbolizerBuilder symb =
                sb.featureTypeStyle().name("Simple polygon style").rule().polygon();
        symb.stroke().color(Color.BLUE).width(1).opacity(0.5);
        symb.fill().color(Color.CYAN).opacity(0.5);

        sb.build();

        // now what do we test :-)
    }

    @Test
    public void testStrokeBuilderDashArray() {
        ExpressionBuilder eb = new ExpressionBuilder();
        StrokeBuilder sb = new StrokeBuilder();
        Expression expression = (Expression) eb.property("foo").build();
        Literal literal = (Literal) eb.literal(10).build();
        sb.dashArray(Arrays.asList(expression, literal));
        Stroke stroke = sb.build();

        assertEquals(2, stroke.dashArray().size());
        assertEquals(expression, stroke.dashArray().get(0));
        assertEquals(literal, stroke.dashArray().get(1));
    }

    @Test
    public void testBackgroundFill() {
        StyleBuilder sb = new StyleBuilder();
        sb.background().color(Color.RED);
        Style style = sb.build();

        Fill background = style.getBackground();
        assertNotNull(background);
        assertEquals(Color.RED, background.getColor().evaluate(null, Color.class));
    }

    @Test
    public void testRuleOptions() {
        StyleBuilder sb = new StyleBuilder();
        RuleBuilder ruleBuilder = sb.featureTypeStyle()
                .rule()
                .option("RuleOption", "RuleOptionValue")
                .option("RuleOption2", "RuleOptionValue2");
        Rule rule = ruleBuilder.build();
        Map<String, String> options = rule.getOptions();
        assertEquals("RuleOptionValue", options.get("RuleOption"));
        assertEquals("RuleOptionValue2", options.get("RuleOption2"));
    }

    /*
     * public void test(){ FeatureTypeFactory factory =
     * CommonFactoryFinder.getFeatureTypeFactory(null);
     *
     * AttributeTypeBuilder b = new AttributeTypeBuilder(factory); AttributeType ANY_URI =
     * b.name("anyURI").binding(URI.class).buildType(); AttributeType DOUBLE =
     * b.name("Double").binding(Double.class).buildType();
     *
     * AttributeDescriptor uom = b.buildDescriptor("uom", ANY_URI ); AttributeDescriptor value =
     * b.inline(true).buildDescriptor("value", DOUBLE );
     *
     * Set<PropertyDescriptor> properties = new HashSet<PropertyDescriptor>(); properties.add( value
     * ); properties.add( uom );
     *
     * ComplexType MEASURE_TYPE = factory.createComplexType( new NameImpl("MeasureType"),
     * properties, true, false, Collections.emptyList(), null, null );
     *
     *
     * }
     */
}
