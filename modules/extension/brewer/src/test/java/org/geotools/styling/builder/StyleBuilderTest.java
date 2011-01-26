package org.geotools.styling.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.awt.Color;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.expression.ExpressionBuilder;
import org.geotools.styling.AnchorPoint;
import org.geotools.styling.Description;
import org.geotools.styling.FeatureTypeConstraint;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.UserLayer;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Expression;
import org.opengis.style.Halo;

public class StyleBuilderTest {

    public void example() {
        StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
        StyledLayerDescriptor sld = sf.createStyledLayerDescriptor();
        sld.setName("example");
        sld.setAbstract("Example Style Layer Descriptor");

        UserLayer layer = sf.createUserLayer();
        layer.setName("layer");

        FeatureTypeConstraint constraint = sf.createFeatureTypeConstraint("Feature",
                Filter.INCLUDE, null);

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
    public void emailExample() {
        StyleBuilder<?> builder = new StyleBuilder();

        RuleBuilder rule = builder.newFeatureTypeStyle().featureTypeName("Feature").rule();
        rule.newPoint().graphic().externalGraphic("file:///C:/images/house.gif", "image/gid").mark(
                "circle");

        Style style = builder.build();

        assertNotNull(style);
    }

    @Test
    public void anchorPoint() {
        AnchorPointBuilder<?> b = new AnchorPointBuilder();

        AnchorPoint anchor = b.build();
        assertEquals(0.0, anchor.getAnchorPointX().evaluate(null, Double.class), 0.0);
        assertEquals(0.0, anchor.getAnchorPointY().evaluate(null, Double.class), 0.0);

        anchor = b.x(0.5).y(0.9).build();
        assertEquals(0.5, anchor.getAnchorPointX().evaluate(null, Double.class), 0.0);
        assertEquals(0.9, anchor.getAnchorPointY().evaluate(null, Double.class), 0.0);

    }

    @Test
    public void description() {
        DescriptionBuilder<?> b = new DescriptionBuilder();
        Description d = b.build();
        assertNull(d.getTitle());
        assertNull(d.getAbstract());

        assertNull(b.unset().build());

        b = new DescriptionBuilder();
        b.description("here be dragons");
        d = b.build();

        assertEquals("here be dragons", d.getAbstract().toString());
        assertNull(b.build().getAbstract());

        b = new DescriptionBuilder(this);
        b.description("here be dragons");
        d = b.build();

        assertEquals("here be dragons", d.getAbstract().toString());
        assertNotNull(b.build().getAbstract());
    }

    @Test
    public void testDisplacementBuilder() {
        DisplacementBuilder<?> b = new DisplacementBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());

        b = new DisplacementBuilder(this);
    }

    @Test
    public void testExtensionSymbolizerBuilder() {
        ExtensionSymbolizerBuilder<?> b = new ExtensionSymbolizerBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());

        b = new ExtensionSymbolizerBuilder(this);
    }

    @Test
    public void testExtentBuilder() {
        ExtentBuilder<?> b = new ExtentBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());

        b = new ExtentBuilder(this);
    }

    @Test
    public void testExternalGraphicBuilder() {
        ExternalGraphicBuilder<?> b = new ExternalGraphicBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());

        b = new ExternalGraphicBuilder(this);
    }

    @Test
    public void testExternalMarkBuilder() {
        ExternalMarkBuilder<?> b = new ExternalMarkBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());

        b = new ExternalMarkBuilder(this);
    }

    @Test
    public void testFeatureTypeConstraintBuilder() {
        FeatureTypeConstraintBuilder<?> b = new FeatureTypeConstraintBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());

        b = new FeatureTypeConstraintBuilder(this);
    }

    @Test
    public void testFeatureTypeStyleBuilder() {
        FeatureTypeStyleBuilder<?> b = new FeatureTypeStyleBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());

        b = new FeatureTypeStyleBuilder(this);
    }

    @Test
    public void testFillBuilder() {
        FillBuilder<?> b = new FillBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());

        b = new FillBuilder(this);
    }

    @Test
    public void testFontBuilder() {
        DisplacementBuilder<?> b = new DisplacementBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());

        b = new DisplacementBuilder(this);
    }

    @Test
    public void testGraphicBuilder() {
        GraphicBuilder<?> b = new GraphicBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());

        b = new GraphicBuilder(this);
    }

    @Test
    public void testGraphicLegendBuilder() {
        GraphicLegendBuilder<?> b = new GraphicLegendBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());

        b = new GraphicLegendBuilder(this);
    }

    @Test
    public void testHaloBuilder() {
        HaloBuilder<?> b = new HaloBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());

        b = new HaloBuilder(this);
    }

    @Test
    public void testImageOutlineBuilder() {
        ImageOutlineBuilder<?> b = new ImageOutlineBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());

        b = new ImageOutlineBuilder(this);
    }

    @Test
    public void testLabelPlacementBuilder() {
        LabelPlacementBuilder<?> b = new LabelPlacementBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());

        b = new LabelPlacementBuilder(this);
    }

    @Test
    public void testLayerFeatureConstraintsBuilder() {
        LayerFeatureConstraintsBuilder<?> b = new LayerFeatureConstraintsBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());

        b = new LayerFeatureConstraintsBuilder(this);
    }

    @Test
    public void testLinePlacementBuilder() {
        LinePlacementBuilder<?> b = new LinePlacementBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());

        b = new LinePlacementBuilder(this);
    }

    @Test
    public void testLineSymbolizerBuilder() {
        LineSymbolizerBuilder<?> b = new LineSymbolizerBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());

        b = new LineSymbolizerBuilder(this);
    }

    @Test
    public void testMarkBuilder() {
        MarkBuilder<?> b = new MarkBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());

        b = new MarkBuilder(this);
    }

    @Test
    public void testNamedLayerBuilder() {
        NamedLayerBuilder<?> b = new NamedLayerBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());

        b = new NamedLayerBuilder(this);
    }

    @Test
    public void testPointPlacementBuilder() {
        PointPlacementBuilder<?> b = new PointPlacementBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());

        b = new PointPlacementBuilder(this);
    }

    @Test
    public void testPointSymbolizerBuilder() {
        PointSymbolizerBuilder<?> b = new PointSymbolizerBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());

        b = new PointSymbolizerBuilder(this);
    }

    @Test
    public void testPolygonSymbolizerBuilder() {
        PolygonSymbolizerBuilder<?> b = new PolygonSymbolizerBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());

        b = new PolygonSymbolizerBuilder(this);
    }

    @Test
    public void testRasterSymbolizerBuilder() {
        RasterSymbolizerBuilder<?> b = new RasterSymbolizerBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());

        b = new RasterSymbolizerBuilder(this);
    }

    @Test
    public void testRemoteOWSBuilder() {
        RemoteOWSBuilder<?> b = new RemoteOWSBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().resource("localhost").service("WMS").build());

        b = new RemoteOWSBuilder(this);
    }

    @Test
    public void testRuleBuilder() {
        RuleBuilder<?> b = new RuleBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());

        b = new RuleBuilder(this);
    }

    @Test
    public void testSelectedChannelTypeBuilder() {
        SelectedChannelTypeBuilder<?> b = new SelectedChannelTypeBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());

        b = new SelectedChannelTypeBuilder(this);
    }

    @Test
    public void testShadedReliefBuilder() {
        ShadedReliefBuilder<?> b = new ShadedReliefBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());

        b = new ShadedReliefBuilder(this);
    }

    @Test
    public void testStrokeBuilder() {
        StrokeBuilder<?> b = new StrokeBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());

        b = new StrokeBuilder(this);
    }

    @Test
    public void testStyleBuilder() {
        StyleBuilder<?> b = new StyleBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());

        b = new StyleBuilder(this);
    }

    @Test
    public void testStyledLayerBuilder() {
        StyledLayerBuilder<?> b = new StyledLayerBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());

        b = new StyledLayerBuilder(this);
    }

    @Test
    public void testStyledLayerDescriptorBuilder() {
        StyledLayerDescriptorBuilder b = new StyledLayerDescriptorBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());
    }

    @Test
    public void testSymbolBuilder() {
        DisplacementBuilder<?> b = new DisplacementBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());

        b = new DisplacementBuilder(this);
    }

    @Test
    public void testSymbolizerBuilder() {
        SymbolizerBuilder<?> b = new SymbolizerBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());

        b = new SymbolizerBuilder(this);
    }

    @Test
    public void testTextSymbolizerBuilder() {
        TextSymbolizerBuilder<?> b = new TextSymbolizerBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());

        b = new TextSymbolizerBuilder(this);
    }

    @Test
    public void testUserLayerBuilder() {
        UserLayerBuilder<?> b = new UserLayerBuilder();
        assertNull(b.unset().build());
        assertNotNull(b.reset().build());

        b = new UserLayerBuilder(this);
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

    public void defaults() {
        Style style = new StyleBuilder().build();

        assertNotNull(style);
        assertNull(style.getName());
        assertEquals(1, style.featureTypeStyles().size());

        FeatureTypeStyle fts = style.featureTypeStyles().get(0);
        assertNotNull(fts);
        assertEquals(0, fts.featureTypeNames().size());
        assertEquals(1, fts.rules().size());

        Rule r = fts.rules().get(0);
        assertNotNull(r);
        assertNull(r.getName());
        assertEquals(1, r.symbolizers().size());

        PointSymbolizer ps = (PointSymbolizer) r.symbolizers().get(0);
        assertNull(ps.getGeometryPropertyName());
        assertEquals(1, ps.getGraphic().graphicalSymbols().size());

        Mark mark = (Mark) ps.getGraphic().graphicalSymbols().get(0);
        assertEquals("square", mark.getWellKnownName().evaluate(null));
    }

    @Test
    public void fill() throws Exception {
        FillBuilder<?> b = new FillBuilder();
        Fill fill = b.color("#0000FF").opacity(0.75).build();
        assertNotNull(fill);
        assertNotNull(b.color);
        assertEquals(Color.BLUE, fill.getColor().evaluate(null, Color.class));
    }

    @Test
    public void halo() {
        HaloBuilder<?> b = new HaloBuilder(null);
        Halo halo = b.build();

        assertNotNull(halo);
    }

    @Ignore
    @Test
    public void testPolygonStyle() {
        StyleBuilder sb = new StyleBuilder();

        PolygonSymbolizerBuilder symb = sb.newFeatureTypeStyle().name("Simple polygon style")
                .rule().newPolygon();
        symb.stroke().color(Color.BLUE).width(1).opacity(0.5);
        symb.fill().color(Color.CYAN).opacity(0.5);

        Style style = sb.build();

        // now what do we test :-)
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
     * properties, true, false, Collections.EMPTY_LIST, null, null );
     * 
     * 
     * }
     */
}
