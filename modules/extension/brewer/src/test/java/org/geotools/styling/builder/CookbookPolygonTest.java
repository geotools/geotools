package org.geotools.styling.builder;

import static org.junit.Assert.*;

import java.awt.Color;

import javax.measure.unit.SI;

import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.Font;
import org.geotools.styling.Mark;
import org.geotools.styling.PointPlacement;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Style;
import org.geotools.styling.TextSymbolizer2;
import org.junit.Test;

/**
 * 
 *
 * @source $URL$
 */
public class CookbookPolygonTest extends AbstractStyleTest {

    @Test
    public void testSimple() {
        Style style = new FillBuilder().color(Color.BLUE).buildStyle();
        // print(style);

        // round up the basic elements and check its simple
        StyleCollector collector = new StyleCollector();
        style.accept(collector);
        assertSimpleStyle(collector);

        // check the symbolizer
        PolygonSymbolizer ps = (PolygonSymbolizer) collector.symbolizers.get(0);
        assertEquals(Color.BLUE, ps.getFill().getColor().evaluate(null, Color.class));
        assertNull(ps.getStroke());
    }

    @Test
    public void testSimpleWithStroke() {
        PolygonSymbolizerBuilder psb = new PolygonSymbolizerBuilder();
        psb.fill().color(Color.BLUE);
        Style style = psb.stroke().color(Color.WHITE).width(2).buildStyle();
        // print(style);

        // round up the basic elements and check its simple
        StyleCollector collector = new StyleCollector();
        style.accept(collector);
        assertSimpleStyle(collector);

        // check the symbolizer
        PolygonSymbolizer ps = (PolygonSymbolizer) collector.symbolizers.get(0);
        assertEquals(Color.BLUE, ps.getFill().getColor().evaluate(null, Color.class));
        assertEquals(Color.WHITE, ps.getStroke().getColor().evaluate(null, Color.class));
        assertEquals(2, (int) ps.getStroke().getWidth().evaluate(null, Integer.class));
    }

    @Test
    public void testTransparent() {
        PolygonSymbolizerBuilder psb = new PolygonSymbolizerBuilder();
        psb.fill().color(Color.BLUE).opacity(0.5);
        psb.stroke().color(Color.WHITE).width(2);
        Style style = psb.buildStyle();
        // print(style);

        // round up the basic elements and check its simple
        StyleCollector collector = new StyleCollector();
        style.accept(collector);
        assertSimpleStyle(collector);

        // check the symbolizer
        PolygonSymbolizer ps = (PolygonSymbolizer) collector.symbolizers.get(0);
        assertEquals(Color.BLUE, ps.getFill().getColor().evaluate(null, Color.class));
        assertEquals(0.5, ps.getFill().getOpacity().evaluate(null, Double.class), 0.0);
        assertEquals(Color.WHITE, ps.getStroke().getColor().evaluate(null, Color.class));
        assertEquals(2, (int) ps.getStroke().getWidth().evaluate(null, Integer.class));
    }

    @Test
    public void testGraphicFill() {
        Style style = new FillBuilder().graphicFill().size(93)
                .externalGraphic("colorblocks.png", "image/png").buildStyle();
        // print(style);

        // round up the basic elements and check its simple
        StyleCollector collector = new StyleCollector();
        style.accept(collector);
        assertSimpleStyle(collector);

        // check the symbolizer
        PolygonSymbolizer ps = (PolygonSymbolizer) collector.symbolizers.get(0);
        assertNull(ps.getStroke());
        ExternalGraphic eg = (ExternalGraphic) ps.getFill().getGraphicFill().graphicalSymbols()
                .get(0);
        assertEquals("colorblocks.png", eg.getOnlineResource().getLinkage().toString());
        assertEquals("image/png", eg.getFormat());
    }

    @Test
    public void testHatch() {
        Style style = new FillBuilder().graphicFill().size(93).mark().name("shape://times")
                .stroke().colorHex("#990099").width(1).buildStyle();
        // print(style);

        // round up the basic elements and check its simple
        StyleCollector collector = new StyleCollector();
        style.accept(collector);
        assertSimpleStyle(collector);

        // check the symbolizer
        PolygonSymbolizer ps = (PolygonSymbolizer) collector.symbolizers.get(0);
        assertNull(ps.getStroke());
        Mark mark = (Mark) ps.getFill().getGraphicFill().graphicalSymbols().get(0);
        assertEquals("shape://times", mark.getWellKnownName().evaluate(null, String.class));
        assertEquals("#990099", mark.getStroke().getColor().evaluate(null, String.class));
        assertNull(mark.getFill());
    }

    @Test
    public void testStyledLabel() {
        // with an extra halo so that we can do all in one round
        RuleBuilder rb = new RuleBuilder();
        PolygonSymbolizerBuilder pb = rb.polygon();
        pb.fill().colorHex("#40FF40");
        pb.stroke().colorHex("#FFFFFF").width(2);
        TextSymbolizerBuilder tb = rb.text().label("name");
        tb.pointPlacement().anchor().x(0.5).y(0.5);
        tb.newFont().familyName("Arial").size(11).styleName("normal").weightName("bold");
        tb.fill().color(Color.BLACK);
        tb.halo().radius(3).fill().color(Color.WHITE);
        tb.option("autoWrap", 60).option("maxDisplacement", 150);
        Style style = rb.buildStyle();
        // print(style);

        // round up the basic elements and check its simple
        StyleCollector collector = new StyleCollector();
        style.accept(collector);

        PolygonSymbolizer ps = (PolygonSymbolizer) collector.symbolizers.get(0);
        assertEquals("#40FF40", ps.getFill().getColor().evaluate(null, String.class));
        assertEquals(Color.WHITE, ps.getStroke().getColor().evaluate(null, Color.class));
        assertEquals(2, (int) ps.getStroke().getWidth().evaluate(null, Integer.class));

        TextSymbolizer2 ts = (TextSymbolizer2) collector.symbolizers.get(1);
        assertEquals(ff.property("name"), ts.getLabel());
        assertEquals(Color.BLACK, ts.getFill().getColor().evaluate(null, Color.class));

        // font
        Font font = ts.getFont();
        assertEquals("Arial", font.getFamily().get(0).evaluate(null, String.class));
        assertEquals(11, (int) font.getSize().evaluate(null, Integer.class));
        assertEquals(Font.Style.NORMAL, font.getStyle().evaluate(null, String.class));
        assertEquals(Font.Weight.BOLD, font.getWeight().evaluate(null, String.class));

        // placement
        PointPlacement pp = (PointPlacement) ts.getLabelPlacement();
        assertEquals(0.5, pp.getAnchorPoint().getAnchorPointX().evaluate(null, Double.class), 0);
        assertEquals(0.5, pp.getAnchorPoint().getAnchorPointY().evaluate(null, Double.class), 0);

        // halo
        assertEquals(3, (int) ts.getHalo().getRadius().evaluate(null, Integer.class));
        assertEquals(Color.WHITE, ts.getHalo().getFill().getColor().evaluate(null, Color.class));

        // vendor options
        assertEquals(2, ts.getOptions().size());
        assertEquals("60", ts.getOptions().get("autoWrap"));
        assertEquals("150", ts.getOptions().get("maxDisplacement"));
    }

    @Test
    public void testAttributeBased() {
        FeatureTypeStyleBuilder fts = new FeatureTypeStyleBuilder();
        fts.rule().name("SmallPop").title("Less Than 200,000").filter("pop < 200000").polygon()
                .fill().colorHex("#66FF66");
        fts.rule().name("MediumPop").title("200,000 to 500,000")
                .filter("pop between 200000 and 500000").polygon().fill().colorHex("#33CC33");
        fts.rule().name("LargePop").title("More than 500,000").filter("pop > 500000").polygon()
                .fill().colorHex("#009900");
        Style style = fts.buildStyle();
        // print(style);

        // round up the elements and check the basics
        StyleCollector collector = new StyleCollector();
        style.accept(collector);
        assertEquals(1, collector.featureTypeStyles.size());
        assertEquals(3, collector.rules.size());
        assertEquals(3, collector.symbolizers.size());

        // check rules
        assertEquals(ff.less(ff.property("pop"), ff.literal("200000")), collector.rules.get(0)
                .getFilter());
        assertEquals(ff.between(ff.property("pop"), ff.literal("200000"), ff.literal("500000")),
                collector.rules.get(1).getFilter());
        assertEquals(ff.greater(ff.property("pop"), ff.literal("500000")), collector.rules.get(2)
                .getFilter());

        // check symbolizers
        PolygonSymbolizer ps = (PolygonSymbolizer) collector.symbolizers.get(0);
        assertEquals("#66FF66", ps.getFill().getColor().evaluate(null, String.class));
        ps = (PolygonSymbolizer) collector.symbolizers.get(1);
        assertEquals("#33CC33", ps.getFill().getColor().evaluate(null, String.class));
        ps = (PolygonSymbolizer) collector.symbolizers.get(2);
        assertEquals("#009900", ps.getFill().getColor().evaluate(null, String.class));
    }
    
    @Test
    public void testZoomBased() {
        FeatureTypeStyleBuilder fts = new FeatureTypeStyleBuilder();

        RuleBuilder rb = fts.rule().name("Large").max(100000000);
        PolygonSymbolizerBuilder pb = rb.polygon();
        pb.fill().colorHex("#0000CC");
        pb.stroke().color(Color.BLACK).width(7);
        TextSymbolizerBuilder tb = rb.text().label("name");
        tb.newFont().familyName("Arial").size(14).styleName("normal").weightName("bold");
        tb.pointPlacement().anchor().x(0.5).y(0.5);
        tb.fill().color(Color.WHITE);
        
        pb = fts.rule().name("Medium").min(100000000).max(200000000).polygon();
        pb.stroke().color(Color.BLACK).width(4);
        pb.fill().colorHex("#0000CC");
        
        pb = fts.rule().name("Small").min(200000000).polygon();
        pb.stroke().color(Color.BLACK).width(1);
        pb.fill().colorHex("#0000CC");
        
        Style style = fts.buildStyle();
        // print(style);
        
        StyleCollector collector = new StyleCollector();
        style.accept(collector);
        assertEquals(1, collector.featureTypeStyles.size());
        assertEquals(3, collector.rules.size());
        assertEquals(4, collector.symbolizers.size());
        
        // happy that it built, does not really add anything that we don't have already tested
    }
    
    @Test
    public void testUomBased() {
        FeatureTypeStyleBuilder fts = new FeatureTypeStyleBuilder();
        PolygonSymbolizerBuilder pb = fts.rule().polygon();
        pb.uom(SI.METER);
        pb.fill().colorHex("#0000CC");
        pb.stroke().color(Color.BLACK).width(7);
        RuleBuilder rb = fts.rule().name("TextLarge").max(100000000);
        TextSymbolizerBuilder tb = rb.text().label("name").uom(SI.METER);
        tb.newFont().familyName("Arial").size(14).styleName("normal").weightName("bold");
        tb.pointPlacement().anchor().x(0.5).y(0.5);
        tb.fill().color(Color.WHITE);
        Style style = fts.buildStyle();
        // print(style);
        
        StyleCollector collector = new StyleCollector();
        style.accept(collector);
        assertEquals(1, collector.featureTypeStyles.size());
        assertEquals(2, collector.rules.size());
        assertEquals(2, collector.symbolizers.size());
        
        assertEquals(SI.METER, collector.symbolizers.get(0).getUnitOfMeasure());
        // happy that it built, does not really add anything that we don't have already tested
    }

}
