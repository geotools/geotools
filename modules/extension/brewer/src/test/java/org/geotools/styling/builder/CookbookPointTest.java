package org.geotools.styling.builder;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.List;

import javax.measure.unit.SI;

import org.geotools.filter.function.CategorizeFunction;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.Font;
import org.geotools.styling.Graphic;
import org.geotools.styling.PointPlacement;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.TextSymbolizer;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Function;
import org.opengis.style.Fill;
import org.opengis.style.GraphicalSymbol;
import org.opengis.style.Mark;
import org.opengis.style.Stroke;

/**
 * 
 *
 * @source $URL$
 */
public class CookbookPointTest extends AbstractStyleTest {

    @Test
    public void testSimple() {
        Style style = new GraphicBuilder().size(6).mark().name("circle").fill().color(Color.RED)
                .buildStyle();
        // print(style);

        // round up the basic elements and check its simple
        StyleCollector collector = new StyleCollector();
        style.accept(collector);
        assertSimpleStyle(collector);

        // check the size
        PointSymbolizer ps = (PointSymbolizer) collector.symbolizers.get(0);
        Graphic graphic = ps.getGraphic();
        assertEquals(6, (int) graphic.getSize().evaluate(null, Integer.class));

        // check the mark
        List<GraphicalSymbol> symbols = graphic.graphicalSymbols();
        assertEquals(1, symbols.size());
        Mark mark = (Mark) symbols.get(0);
        assertEquals("circle", mark.getWellKnownName().evaluate(null));
        assertEquals(Color.RED, mark.getFill().getColor().evaluate(null, Color.class));
    }

    @Test
    public void testPointWithStroke() {
        MarkBuilder mb = new GraphicBuilder().size(6).mark().name("circle");
        mb.fill().color(Color.RED);
        mb.stroke().width(2).color(Color.BLUE);
        Style style = mb.buildStyle();
        // print(style);

        // round up the basic elements and check its simple
        StyleCollector collector = new StyleCollector();
        style.accept(collector);
        assertSimpleStyle(collector);

        // check the size
        PointSymbolizer ps = (PointSymbolizer) collector.symbolizers.get(0);
        Graphic graphic = ps.getGraphic();
        assertEquals(6, (int) graphic.getSize().evaluate(null, Integer.class));

        // check the mark
        List<GraphicalSymbol> symbols = graphic.graphicalSymbols();
        assertEquals(1, symbols.size());
        Mark mark = (Mark) symbols.get(0);
        assertEquals("circle", mark.getWellKnownName().evaluate(null));
        assertEquals(Color.RED, mark.getFill().getColor().evaluate(null, Color.class));
        Stroke stroke = mark.getStroke();
        assertEquals(Color.BLUE, stroke.getColor().evaluate(null, Color.class));
        assertEquals(2, (int) stroke.getWidth().evaluate(null, Integer.class));
    }

    @Test
    public void testRotatedSquare() {
        Style style = new GraphicBuilder().size(12).rotation(45).mark().name("square").fill()
                .color(Color.GREEN).buildStyle();
        // print(style);

        // round up the basic elements and check its simple
        StyleCollector collector = new StyleCollector();
        style.accept(collector);
        assertSimpleStyle(collector);

        // check the size and rotation
        PointSymbolizer ps = (PointSymbolizer) collector.symbolizers.get(0);
        Graphic graphic = ps.getGraphic();
        assertEquals(12, (int) graphic.getSize().evaluate(null, Integer.class));
        assertEquals(45, (int) graphic.getRotation().evaluate(null, Integer.class));

        // check the mark
        List<GraphicalSymbol> symbols = graphic.graphicalSymbols();
        assertEquals(1, symbols.size());
        Mark mark = (Mark) symbols.get(0);
        assertEquals("square", mark.getWellKnownName().evaluate(null));
        assertEquals(Color.GREEN, mark.getFill().getColor().evaluate(null, Color.class));
    }

    @Test
    public void testTransparentTriangle() {
        MarkBuilder mb = new GraphicBuilder().size(12).mark().name("triangle");
        mb.fill().color(Color.RED).opacity(0.2);
        mb.stroke().color(Color.BLUE).width(2);
        Style style = mb.buildStyle();
        // print(style);

        // round up the basic elements and check its simple
        StyleCollector collector = new StyleCollector();
        style.accept(collector);
        assertSimpleStyle(collector);

        // check the size and rotation
        PointSymbolizer ps = (PointSymbolizer) collector.symbolizers.get(0);
        Graphic graphic = ps.getGraphic();
        assertEquals(12, (int) graphic.getSize().evaluate(null, Integer.class));

        // check the mark
        List<GraphicalSymbol> symbols = graphic.graphicalSymbols();
        assertEquals(1, symbols.size());
        Mark mark = (Mark) symbols.get(0);
        assertEquals("triangle", mark.getWellKnownName().evaluate(null));
        Fill fill = mark.getFill();
        assertEquals(Color.RED, fill.getColor().evaluate(null, Color.class));
        assertEquals(0.2, (double) fill.getOpacity().evaluate(null, Double.class), 0);
        Stroke stroke = mark.getStroke();
        assertEquals(Color.BLUE, stroke.getColor().evaluate(null, Color.class));
        assertEquals(2, (int) stroke.getWidth().evaluate(null, Integer.class));
    }

    @Test
    public void testPointAsGraphics() {
        Style style = new GraphicBuilder().size(32).externalGraphic("smileyface.png", "image/png")
                .buildStyle();
        // print(style);

        // round up the basic elements and check its simple
        StyleCollector collector = new StyleCollector();
        style.accept(collector);
        assertSimpleStyle(collector);

        // check the size and rotation
        PointSymbolizer ps = (PointSymbolizer) collector.symbolizers.get(0);
        Graphic graphic = ps.getGraphic();
        assertEquals(32, (int) graphic.getSize().evaluate(null, Integer.class));

        // check the mark
        List<GraphicalSymbol> symbols = graphic.graphicalSymbols();
        assertEquals(1, symbols.size());
        ExternalGraphic eg = (ExternalGraphic) symbols.get(0);
        assertEquals("image/png", eg.getFormat());
        assertEquals("smileyface.png", eg.getOnlineResource().getLinkage().toString());
    }

    @Test
    public void testPointDefaultLabel() {
        RuleBuilder rb = new RuleBuilder();
        rb.point().graphic().size(6).mark().name("circle").fill().color(Color.RED);
        rb.text().label("name").fill().color(Color.BLACK);
        Style style = rb.buildStyle();

        // round up the basic elements and check its simple
        StyleCollector collector = new StyleCollector();
        style.accept(collector);
        basicPointWithLabelAssertions(collector);

        TextSymbolizer ps = (TextSymbolizer) collector.symbolizers.get(1);
        assertEquals(ff.property("name"), ps.getLabel());
        assertEquals(Color.BLACK, ps.getFill().getColor().evaluate(null, Color.class));
    }

    @Test
    public void testPointStyledLabel() {
        RuleBuilder rb = new RuleBuilder();
        rb.point().graphic().size(6).mark().name("circle").fill().color(Color.RED);
        TextSymbolizerBuilder tb = rb.text().label("name");
        tb.fill().color(Color.BLACK);
        tb.newFont().familyName("Arial").size(12).styleName(Font.Style.NORMAL)
                .weightName(Font.Weight.BOLD);
        tb.pointPlacement().displacement().x(0).y(5);
        tb.pointPlacement().anchor().x(0.5);
        Style style = rb.buildStyle();

        // round up the basic elements and check its simple
        StyleCollector collector = new StyleCollector();
        style.accept(collector);
        basicPointWithLabelAssertions(collector);

        TextSymbolizer ps = (TextSymbolizer) collector.symbolizers.get(1);
        assertEquals(ff.property("name"), ps.getLabel());
        assertEquals(Color.BLACK, ps.getFill().getColor().evaluate(null, Color.class));

        // font
        Font font = ps.getFont();
        assertEquals("Arial", font.getFamily().get(0).evaluate(null, String.class));
        assertEquals(12, (int) font.getSize().evaluate(null, Integer.class));
        assertEquals(Font.Style.NORMAL, font.getStyle().evaluate(null, String.class));
        assertEquals(Font.Weight.BOLD, font.getWeight().evaluate(null, String.class));

        // placement
        PointPlacement pp = (PointPlacement) ps.getLabelPlacement();
        assertEquals(0.5, pp.getAnchorPoint().getAnchorPointX().evaluate(null, Double.class), 0);
        assertEquals(0, pp.getAnchorPoint().getAnchorPointY().evaluate(null, Double.class), 0);
        assertEquals(0, (int) pp.getDisplacement().getDisplacementX().evaluate(null, Integer.class));
        assertEquals(5, (int) pp.getDisplacement().getDisplacementY().evaluate(null, Integer.class));
    }

    @Test
    public void testPointRotatedLabel() {
        RuleBuilder rb = new RuleBuilder();
        rb.point().graphic().size(6).mark().name("circle").fill().color(Color.RED);
        TextSymbolizerBuilder tb = rb.text().label("name");
        tb.fill().color(Color.BLACK);
        tb.newFont().familyName("Arial").size(12).styleName(Font.Style.NORMAL)
                .weightName(Font.Weight.BOLD);
        tb.pointPlacement().displacement().x(0).y(5);
        tb.pointPlacement().anchor().x(0.5);
        tb.pointPlacement().rotation(-45);
        Style style = rb.buildStyle();

        // round up the basic elements and check its simple
        StyleCollector collector = new StyleCollector();
        style.accept(collector);
        basicPointWithLabelAssertions(collector);

        TextSymbolizer ps = (TextSymbolizer) collector.symbolizers.get(1);
        assertEquals(ff.property("name"), ps.getLabel());
        assertEquals(Color.BLACK, ps.getFill().getColor().evaluate(null, Color.class));

        // font
        Font font = ps.getFont();
        assertEquals("Arial", font.getFamily().get(0).evaluate(null, String.class));
        assertEquals(12, (int) font.getSize().evaluate(null, Integer.class));
        assertEquals(Font.Style.NORMAL, font.getStyle().evaluate(null, String.class));
        assertEquals(Font.Weight.BOLD, font.getWeight().evaluate(null, String.class));

        // placement
        PointPlacement pp = (PointPlacement) ps.getLabelPlacement();
        assertEquals(-45, pp.getRotation().evaluate(null, Double.class), 0.0);
        assertEquals(0.5, pp.getAnchorPoint().getAnchorPointX().evaluate(null, Double.class), 0);
        assertEquals(0, pp.getAnchorPoint().getAnchorPointY().evaluate(null, Double.class), 0);
        assertEquals(0, (int) pp.getDisplacement().getDisplacementX().evaluate(null, Integer.class));
        assertEquals(5, (int) pp.getDisplacement().getDisplacementY().evaluate(null, Integer.class));
    }

    private void basicPointWithLabelAssertions(StyleCollector collector) {
        assertEquals(1, collector.featureTypeStyles.size());
        assertEquals(1, collector.rules.size());
        assertEquals(2, collector.symbolizers.size());

        // check the size and rotation of the point symbolizer
        PointSymbolizer ps = (PointSymbolizer) collector.symbolizers.get(0);
        Graphic graphic = ps.getGraphic();
        assertEquals(6, (int) graphic.getSize().evaluate(null, Integer.class));

        // check the mark
        List<GraphicalSymbol> symbols = graphic.graphicalSymbols();
        assertEquals(1, symbols.size());
        Mark mark = (Mark) symbols.get(0);
        assertEquals("circle", mark.getWellKnownName().evaluate(null));
        Fill fill = mark.getFill();
        assertEquals(Color.RED, fill.getColor().evaluate(null, Color.class));
    }

    @Test
    public void testAttributeBasedPoint() {
        Mark mark = (Mark) new MarkBuilder().name("circle").fill().color(new Color(0, 51, 204))
                .buildRoot();
        FeatureTypeStyleBuilder fts = new FeatureTypeStyleBuilder();
        fts.rule().name("SmallPop").title("1 to 50000").filter("pop < 50000").point().graphic()
                .size(8).mark().reset(mark);
        fts.rule().name("MediumPop").title("50000 to 100000")
                .filter("pop between 50000 and 100000").point().graphic().size(12).mark()
                .reset(mark);
        fts.rule().name("LargePop").title("Greater than 100000").filter("pop >= 100000").point()
                .graphic().size(16).mark().reset(mark);
        Style style = fts.buildStyle();
        // print(style);

        StyleCollector collector = new StyleCollector();
        style.accept(collector);
        assertEquals(1, collector.featureTypeStyles.size());
        assertEquals(3, collector.rules.size());
        assertEquals(3, collector.symbolizers.size());

        // check rules and styles
        checkAttributeBasedRule(collector.rules.get(0),
                ff.less(ff.property("pop"), ff.literal("50000")), 8);
        checkAttributeBasedRule(collector.rules.get(1),
                ff.between(ff.property("pop"), ff.literal("50000"), ff.literal("100000")), 12);
        checkAttributeBasedRule(collector.rules.get(2),
                ff.greaterOrEqual(ff.property("pop"), ff.literal("100000")), 16);
    }

    @Test
    public void testCagetorizeBasedPoint() {
        Function size = ff.function("categorize", ff.property("pop"), ff.literal(8),
                ff.literal(50000), ff.literal(12), ff.literal(100000), ff.literal(16));
        Style style = new GraphicBuilder().size(size).mark().name("circle").fill()
                .color(new Color(0, 51, 204)).buildStyle();
        // print(style);

        StyleCollector collector = new StyleCollector();
        style.accept(collector);
        assertSimpleStyle(collector);

        // check the function is there were we expect it
        PointSymbolizer ps = (PointSymbolizer) collector.symbolizers.get(0);
        Graphic graphic = ps.getGraphic();
        assertTrue(graphic.getSize() instanceof CategorizeFunction);
    }

    private void checkAttributeBasedRule(Rule rule, Filter filter, int size) {
        assertEquals(filter, rule.getFilter());
        assertEquals(1, rule.getSymbolizers().length);
        PointSymbolizer ps = (PointSymbolizer) rule.getSymbolizers()[0];
        assertEquals(size, (int) ps.getGraphic().getSize().evaluate(null, Integer.class));
    }

    @Test
    public void testZoomBasedPoint() {
        Mark mark = (Mark) new MarkBuilder().name("circle").fill().color(Color.RED).buildRoot();
        FeatureTypeStyleBuilder fts = new FeatureTypeStyleBuilder();
        fts.rule().name("Large").max(160000000).point().graphic().size(12).mark().reset(mark);
        fts.rule().name("Medium").min(160000000).max(320000000).point().graphic().size(8).mark()
                .reset(mark);
        fts.rule().name("Small").min(320000000).point().graphic().size(4).mark().reset(mark);
        Style style = fts.buildStyle();
        // print(style);

        StyleCollector collector = new StyleCollector();
        style.accept(collector);
        assertEquals(1, collector.featureTypeStyles.size());
        assertEquals(3, collector.rules.size());
        assertEquals(3, collector.symbolizers.size());

        // check rules and styles
        checkScaleBasedRule(collector.rules.get(0), "Large", 0, 160000000, 12);
        checkScaleBasedRule(collector.rules.get(1), "Medium", 160000000, 320000000, 8);
        checkScaleBasedRule(collector.rules.get(2), "Small", 320000000, Double.POSITIVE_INFINITY, 4);
    }

    private void checkScaleBasedRule(Rule rule, String name, double minDenominator,
            double maxDenominator, int size) {
        assertEquals(name, rule.getName());
        assertEquals(minDenominator, rule.getMinScaleDenominator(), 0.0);
        assertEquals(maxDenominator, rule.getMaxScaleDenominator(), 0.0);
        assertEquals(1, rule.getSymbolizers().length);
        PointSymbolizer ps = (PointSymbolizer) rule.getSymbolizers()[0];
        assertEquals(size, (int) ps.getGraphic().getSize().evaluate(null, Integer.class));
    }

    @Test
    public void testUomPoint() {
        Style style = new PointSymbolizerBuilder().uom(SI.METER).graphic().size(50).mark()
                .name("circle").fill().color(Color.RED).buildStyle();
        // print(style);

        StyleCollector collector = new StyleCollector();
        style.accept(collector);
        assertSimpleStyle(collector);

        PointSymbolizer ps = (PointSymbolizer) collector.symbolizers.get(0);
        assertEquals(SI.METER, ps.getUnitOfMeasure());
    }

}
