package org.geotools.styling.builder;

import static org.junit.Assert.*;

import org.geotools.styling.ColorMap;
import org.geotools.styling.ColorMapEntry;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.ShadedRelief;
import org.geotools.styling.Style;
import org.junit.Test;
import org.opengis.style.ContrastMethod;

/**
 * 
 *
 * @source $URL$
 */
public class CookbookRasterTest extends AbstractStyleTest {

    @Test
    public void testSimple() {
        Style style = new RasterSymbolizerBuilder().buildStyle();
        // print(style);

        // round up the basic elements and check its simple
        StyleCollector collector = new StyleCollector();
        style.accept(collector);
        assertSimpleStyle(collector);

        // check the symbolizer
        RasterSymbolizer rs = (RasterSymbolizer) collector.symbolizers.get(0);
        assertNull(rs.getChannelSelection());
        assertEquals(0, rs.getColorMap().getColorMapEntries().length);
    }

    @Test
    public void testTwoColorGradient() {
        ColorMapBuilder cm = new ColorMapBuilder();
        cm.entry().quantity(70).colorHex("#008000");
        cm.entry().quantity(256).colorHex("#663333");
        Style style = cm.buildStyle();
        // print(style);

        // round up the basic elements and check its simple
        StyleCollector collector = new StyleCollector();
        style.accept(collector);
        assertSimpleStyle(collector);

        // check the symbolizer
        RasterSymbolizer rs = (RasterSymbolizer) collector.symbolizers.get(0);
        assertNull(rs.getChannelSelection());
        ColorMap cmap = rs.getColorMap();
        assertEquals(ColorMap.TYPE_RAMP, cmap.getType());
        assertFalse(cmap.getExtendedColors());
        assertEntry("#008000", 70.0, 1.0, null, cmap.getColorMapEntry(0));
        assertEntry("#663333", 256.0, 1.0, null, cmap.getColorMapEntry(1));
    }

    @Test
    public void testTransparentGradient() {
        ColorMapBuilder cm = new RasterSymbolizerBuilder().opacity(0.3).colorMap();
        cm.entry().quantity(70).colorHex("#008000");
        cm.entry().quantity(256).colorHex("#663333");
        Style style = cm.buildStyle();
        // print(style);

        // round up the basic elements and check its simple
        StyleCollector collector = new StyleCollector();
        style.accept(collector);
        assertSimpleStyle(collector);

        // check the symbolizer
        RasterSymbolizer rs = (RasterSymbolizer) collector.symbolizers.get(0);
        assertEquals(0.3, rs.getOpacity().evaluate(null, Double.class), 0.0);
        assertNull(rs.getChannelSelection());
        ColorMap cmap = rs.getColorMap();
        assertEquals(ColorMap.TYPE_RAMP, cmap.getType());
        assertFalse(cmap.getExtendedColors());
        assertEntry("#008000", 70.0, 1.0, null, cmap.getColorMapEntry(0));
        assertEntry("#663333", 256.0, 1.0, null, cmap.getColorMapEntry(1));
    }

    @Test
    public void testBrightnessAndContrast() {
        RasterSymbolizerBuilder rsb = new RasterSymbolizerBuilder();
        rsb.contrastEnhancement().normalize().gamma(0.5);
        ColorMapBuilder cm = rsb.colorMap();
        cm.entry().quantity(70).colorHex("#008000");
        cm.entry().quantity(256).colorHex("#663333");
        Style style = cm.buildStyle();
        // print(style);

        // round up the basic elements and check its simple
        StyleCollector collector = new StyleCollector();
        style.accept(collector);
        assertSimpleStyle(collector);

        // check the symbolizer
        RasterSymbolizer rs = (RasterSymbolizer) collector.symbolizers.get(0);
        assertEquals(1.0, rs.getOpacity().evaluate(null, Double.class), 0.0);
        assertNotNull(rs.getContrastEnhancement());
        assertEquals(ContrastMethod.NORMALIZE, rs.getContrastEnhancement().getMethod());
        assertEquals(0.5, rs.getContrastEnhancement().getGammaValue().evaluate(null, Double.class),
                0.0);
        assertNull(rs.getChannelSelection());
        ColorMap cmap = rs.getColorMap();
        assertEquals(ColorMap.TYPE_RAMP, cmap.getType());
        assertFalse(cmap.getExtendedColors());
        assertEntry("#008000", 70.0, 1.0, null, cmap.getColorMapEntry(0));
        assertEntry("#663333", 256.0, 1.0, null, cmap.getColorMapEntry(1));
    }

    @Test
    public void testThreeColorGradient() {
        ColorMapBuilder cm = new ColorMapBuilder();
        cm.entry().quantity(150).colorHex("#0000FF");
        cm.entry().quantity(200).colorHex("#FFFF00");
        cm.entry().quantity(250).colorHex("#FF0000");
        Style style = cm.buildStyle();
        // print(style);

        // round up the basic elements and check its simple
        StyleCollector collector = new StyleCollector();
        style.accept(collector);
        assertSimpleStyle(collector);

        // check the symbolizer
        RasterSymbolizer rs = (RasterSymbolizer) collector.symbolizers.get(0);
        assertNull(rs.getChannelSelection());
        ColorMap cmap = rs.getColorMap();
        assertEquals(ColorMap.TYPE_RAMP, cmap.getType());
        assertFalse(cmap.getExtendedColors());
        assertEquals(3, cmap.getColorMapEntries().length);
        assertEntry("#0000FF", 150.0, 1.0, null, cmap.getColorMapEntry(0));
        assertEntry("#FFFF00", 200.0, 1.0, null, cmap.getColorMapEntry(1));
        assertEntry("#FF0000", 250.0, 1.0, null, cmap.getColorMapEntry(2));
    }

    @Test
    public void testAlphaChannel() {
        ColorMapBuilder cm = new ColorMapBuilder();
        cm.entry().quantity(70).colorHex("#008000");
        cm.entry().quantity(256).colorHex("#008000").opacity(0);
        Style style = cm.buildStyle();
        // print(style);

        // round up the basic elements and check its simple
        StyleCollector collector = new StyleCollector();
        style.accept(collector);
        assertSimpleStyle(collector);

        // check the symbolizer
        RasterSymbolizer rs = (RasterSymbolizer) collector.symbolizers.get(0);
        assertNull(rs.getChannelSelection());
        ColorMap cmap = rs.getColorMap();
        assertEquals(ColorMap.TYPE_RAMP, cmap.getType());
        assertFalse(cmap.getExtendedColors());
        assertEquals(2, cmap.getColorMapEntries().length);
        assertEntry("#008000", 70.0, 1.0, null, cmap.getColorMapEntry(0));
        assertEntry("#008000", 256.0, 0.0, null, cmap.getColorMapEntry(1));
    }

    @Test
    public void testDiscreteColors() {
        ColorMapBuilder cm = new ColorMapBuilder().type(ColorMap.TYPE_INTERVALS);
        cm.entry().quantity(150).colorHex("#008000");
        cm.entry().quantity(256).colorHex("#663333");
        Style style = cm.buildStyle();
        // print(style);

        // round up the basic elements and check its simple
        StyleCollector collector = new StyleCollector();
        style.accept(collector);
        assertSimpleStyle(collector);

        // check the symbolizer
        RasterSymbolizer rs = (RasterSymbolizer) collector.symbolizers.get(0);
        assertNull(rs.getChannelSelection());
        ColorMap cmap = rs.getColorMap();
        assertEquals(ColorMap.TYPE_INTERVALS, cmap.getType());
        assertFalse(cmap.getExtendedColors());
        assertEquals(2, cmap.getColorMapEntries().length);
        assertEntry("#008000", 150.0, 1.0, null, cmap.getColorMapEntry(0));
        assertEntry("#663333", 256.0, 1.0, null, cmap.getColorMapEntry(1));
    }

    void assertEntry(String colorHex, double quantity, double opacity, String label,
            ColorMapEntry colorMapEntry) {
        assertEquals(colorHex, colorMapEntry.getColor().evaluate(null, String.class));
        assertEquals(quantity, colorMapEntry.getQuantity().evaluate(null, Double.class), 0.0);
        assertEquals(opacity, colorMapEntry.getOpacity().evaluate(null, Double.class), 0.0);
        assertEquals(label, colorMapEntry.getLabel());
    }

    @Test
    public void testShadedRelief() {
        Style style = new ShadedReliefBuilder().factor(10).brightnessOnly(true).buildStyle();
        // print(style);
        
        // round up the basic elements and check its simple
        StyleCollector collector = new StyleCollector();
        style.accept(collector);
        assertSimpleStyle(collector);

        // check the symbolizer
        RasterSymbolizer rs = (RasterSymbolizer) collector.symbolizers.get(0);
        assertNull(rs.getChannelSelection());
        assertNull(rs.getColorMap());
        ShadedRelief sr = rs.getShadedRelief();
        assertEquals(10.0, sr.getReliefFactor().evaluate(null, Double.class), 0.0);
        assertTrue(sr.isBrightnessOnly());
    }

}
