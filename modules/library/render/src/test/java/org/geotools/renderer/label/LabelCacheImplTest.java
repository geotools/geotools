package org.geotools.renderer.label;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.api.style.Font;
import org.geotools.api.style.TextSymbolizer;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.LiteShape2;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.RenderListener;
import org.geotools.renderer.lite.RendererBaseTest;
import org.geotools.renderer.style.GraphicStyle2D;
import org.geotools.renderer.style.MarkStyle2D;
import org.geotools.renderer.style.Style2D;
import org.geotools.renderer.style.TextStyle2D;
import org.geotools.renderer.style.WellKnownMarkFactory;
import org.geotools.styling.StyleBuilder;
import org.geotools.test.TestGraphics;
import org.geotools.util.NumberRange;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.mockito.Mockito;

public class LabelCacheImplTest {

    private static final Geometry L4 = geometry("POLYGON((0 0, 0 0.1, 0.1 0.1, 0.1 0, 0 0))");

    private static final Geometry L3 = geometry("LINESTRING(20 20, 30 30)");

    private static final Geometry L2 = geometry("LINESTRING(10 10, 20 20)");

    private static final Geometry L1 = geometry("LINESTRING(0 0, 10 10)");

    private static final String LAYER_ID = "layerId";

    SimpleFeatureType schema;

    SimpleFeatureBuilder fb;

    LabelCacheImpl cache;

    StyleBuilder sb;

    NumberRange<Double> ALL_SCALES =
            new NumberRange<>(Double.class, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

    @Before
    public void setupSchema() {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("test");
        tb.add("name", String.class);
        tb.add("geom", Geometry.class, DefaultGeographicCRS.WGS84);
        schema = tb.buildFeatureType();
        fb = new SimpleFeatureBuilder(schema);
        cache = new LabelCacheImpl();
        cache.startLayer(LAYER_ID);
        sb = new StyleBuilder();
    }

    @Before
    public void setVeraFont() throws IOException, FontFormatException {
        RendererBaseTest.setupVeraFonts();
    }

    @Test
    public void testSimpleGrouping() throws Exception {
        TextSymbolizer ts = sb.createTextSymbolizer(Color.BLACK, (Font) null, "name");
        ts.getOptions().put(org.geotools.api.style.TextSymbolizer.GROUP_KEY, "true");
        SimpleFeature f1 = createFeature("label1", L1);
        SimpleFeature f2 = createFeature("label1", L2);
        addToCache(cache, ts, f1);
        addToCache(cache, ts, f2);

        // we have just one
        List<LabelCacheItem> labels = cache.getActiveLabels();
        assertEquals(1, labels.size());
        LabelCacheItem item = labels.get(0);
        assertEquals("label1", item.getLabel());
        assertEquals(2, item.getGeoms().size());
    }

    @Test
    public void testGroupDifferentSymbolizers() throws Exception {
        TextSymbolizer ts1 = sb.createTextSymbolizer(Color.BLACK, (Font) null, "name");
        ts1.getOptions().put(org.geotools.api.style.TextSymbolizer.GROUP_KEY, "true");
        TextSymbolizer ts2 = sb.createTextSymbolizer(Color.YELLOW, (Font) null, "name");
        ts2.getOptions().put(org.geotools.api.style.TextSymbolizer.GROUP_KEY, "true");

        SimpleFeature f1 = createFeature("label1", L1);
        SimpleFeature f2 = createFeature("label1", L2);
        addToCache(cache, ts1, f1);
        addToCache(cache, ts2, f2);

        // two different symbolizers, we should have two
        List<LabelCacheItem> labels = cache.getActiveLabels();
        assertEquals(2, labels.size());
    }

    @Test
    public void testMinNonGrouped() throws Exception {
        TextSymbolizer ts = sb.createTextSymbolizer(Color.BLACK, (Font) null, "name");
        TextSymbolizer tsGroup = sb.createTextSymbolizer(Color.YELLOW, (Font) null, "name");
        tsGroup.getOptions().put(org.geotools.api.style.TextSymbolizer.GROUP_KEY, "true");

        SimpleFeature f1 = createFeature("label1", L1);
        SimpleFeature f2 = createFeature("label1", L2);
        SimpleFeature f3 = createFeature("label1", L3);
        addToCache(cache, tsGroup, f1);
        addToCache(cache, ts, f2);
        addToCache(cache, tsGroup, f3);

        // two different symbolizers, we should have two (the first grouped with the third)
        List<LabelCacheItem> labels = cache.getActiveLabels();
        assertEquals(2, labels.size());
        LabelCacheItem item1 = labels.get(0);
        assertEquals("label1", item1.getLabel());
        assertEquals(Arrays.asList(L1, L3), item1.getGeoms());
        LabelCacheItem item2 = labels.get(1);
        assertEquals("label1", item2.getLabel());
        assertEquals(Collections.singletonList(L2), item2.getGeoms());
    }

    @Test
    public void testCustomBehaviour() throws TransformException, FactoryException {
        final List<String> labels = new ArrayList<>();
        Graphics2D graphics = Mockito.mock(Graphics2D.class);
        LabelCacheImpl myCache = new LabelCacheImpl() {
            @Override
            int paintLabel(
                    Graphics2D graphics,
                    Rectangle displayArea,
                    LabelIndex glyphs,
                    int paintedLineLabels,
                    LabelPainter painter,
                    LabelCacheItem labelItem) {
                labels.add(labelItem.getLabel());
                return super.paintLabel(graphics, displayArea, glyphs, paintedLineLabels, painter, labelItem);
            }
        };
        TextSymbolizer ts = sb.createTextSymbolizer(Color.BLACK, (Font) null, "name");
        SimpleFeature f1 = createFeature("label1", L1);
        myCache.enableLayer(LAYER_ID);
        addToCache(myCache, ts, f1);

        myCache.endLayer(LAYER_ID, graphics, new Rectangle(0, 0, 256, 256));
        myCache.end(graphics, new Rectangle(0, 0, 256, 256));
        assertEquals(1, labels.size());
    }

    @Test
    public void testRendererListener() throws Exception {
        TextSymbolizer ts = sb.createTextSymbolizer(Color.BLACK, (Font) null, "name");

        AtomicReference<Exception> exception = new AtomicReference<>(null);
        RenderListener listener = new RenderListener() {

            @Override
            public void featureRenderer(SimpleFeature feature) {
                // TODO Auto-generated method stub

            }

            @Override
            public void errorOccurred(Exception e) {
                exception.set(e);
            }
        };
        cache.addRenderListener(listener);
        SimpleFeature f1 = createFeature("label1", L1);
        addToCache(cache, ts, f1);
        cache.endLayer("layerId", null, null);
        TestGraphics testGraphics = new TestGraphics();
        testGraphics.setRenderingHints(Collections.emptyMap());
        cache.end(testGraphics, new Rectangle(0, 0, 10, 10));
        // got here, did we get the exception
        assertNotNull(exception.get());
    }

    @Test
    public void testUsesCustomLabelPainter() throws Exception {
        LabelPainter painter = Mockito.mock(LabelPainter.class);
        Graphics2D graphics = Mockito.mock(Graphics2D.class);

        cache.setConstructPainter((x, y) -> painter);
        TextSymbolizer ts = sb.createTextSymbolizer(Color.BLACK, (Font) null, "name");
        SimpleFeature f1 = createFeature("label1", L1);
        addToCache(cache, ts, f1);

        cache.endLayer(LAYER_ID, graphics, new Rectangle(0, 0, 256, 256));
        cache.end(graphics, new Rectangle(0, 0, 256, 256));

        Mockito.verify(painter).setLabel(Mockito.any(LabelCacheItem.class));
        Mockito.verify(painter, Mockito.atLeastOnce()).getLabel();
    }

    @Test
    public void testDecimateSmallRing() throws Exception {
        Font font = sb.createFont("Bitstream Vera Sans", 12);
        TextSymbolizer ts = sb.createTextSymbolizer(Color.BLACK, font, "name");
        ts.getOptions().put(org.geotools.api.style.TextSymbolizer.FOLLOW_LINE_KEY, "true");

        AtomicReference<Exception> exception = new AtomicReference<>(null);
        RenderListener listener = new RenderListener() {

            @Override
            public void featureRenderer(SimpleFeature feature) {
                // TODO Auto-generated method stub

            }

            @Override
            public void errorOccurred(Exception e) {
                exception.set(e);
            }
        };
        cache.addRenderListener(listener);
        SimpleFeature f1 = createFeature("label1", L4);
        addToCache(cache, ts, f1);
        cache.endLayer("layerId", null, null);
        BufferedImage bi = new BufferedImage(10, 10, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = bi.createGraphics();
        cache.end(graphics, new Rectangle(0, 0, 10, 10));
        // got here, no exception
        assertNull(exception.get());
    }

    /** A line label made only of newlines paints nothing, and must not fail the map. */
    @Test
    public void testNewlineOnlyLineLabel() throws Exception {
        TextSymbolizer ts = sb.createTextSymbolizer(Color.BLACK, sb.createFont("Bitstream Vera Sans", 12), "name");
        List<Exception> errors = new ArrayList<>();
        cache.addRenderListener(new RenderListener() {
            @Override
            public void featureRenderer(SimpleFeature feature) {}

            @Override
            public void errorOccurred(Exception e) {
                errors.add(e);
            }
        });
        addToCache(cache, ts, createFeature("\n", L1));
        cache.endLayer(LAYER_ID, null, null);
        BufferedImage bi = new BufferedImage(10, 10, BufferedImage.TYPE_4BYTE_ABGR);
        cache.end(bi.createGraphics(), new Rectangle(0, 0, 10, 10));
        assertEquals(List.of(), errors);
    }

    @Test
    public void testFollowLineAutoWrap() throws Exception {
        // these two right now are not compatible
        Font font = sb.createFont("Bitstream Vera Sans", 12);
        TextSymbolizer ts = sb.createTextSymbolizer(Color.BLACK, font, "name");
        ts.getOptions().put(org.geotools.api.style.TextSymbolizer.AUTO_WRAP_KEY, "10");
        ts.getOptions().put(org.geotools.api.style.TextSymbolizer.FOLLOW_LINE_KEY, "true");

        // put in cache
        SimpleFeature f1 = createFeature("label1", L4);
        addToCache(cache, ts, f1);

        // check autowrap got disabled
        List<LabelCacheItem> items = cache.getActiveLabels();
        LabelCacheItem item = items.get(0);
        assertTrue(item.isFollowLineEnabled());
        assertEquals(0, item.getAutoWrap());
    }

    private SimpleFeature createFeature(String label, Geometry geom) {
        fb.add(label);
        fb.add(geom);
        return fb.buildFeature(null);
    }

    private static Geometry geometry(String wkt) {
        try {
            return new WKTReader().read(wkt);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSymbolizerCaching() throws Exception {
        // these two right now are not compatible
        Font font = sb.createFont("Bitstream Vera Sans", 12);
        TextSymbolizer ts = sb.createTextSymbolizer(Color.BLACK, font, "name");

        // put in cache
        SimpleFeature f1 = createFeature("label1", L4);
        addToCache(cache, ts, f1);
        SimpleFeature f2 = createFeature("label2", L4);
        addToCache(cache, ts, f2);

        // check TextStyle2D got cached
        List<LabelCacheItem> items = cache.getActiveLabels();
        assertEquals(2, items.size());
        assertEquals("label1", items.get(0).getLabel());
        assertEquals("label2", items.get(1).getLabel());
        assertSame(items.get(0).getTextStyle(), items.get(1).getTextStyle());
    }

    private void addToCache(LabelCacheImpl cache, TextSymbolizer ts, SimpleFeature f1)
            throws TransformException, FactoryException {
        cache.put(LAYER_ID, ts, f1, new LiteShape2((Geometry) f1.getDefaultGeometry(), null, null, false), ALL_SCALES);
    }

    /** A plain label that lands on an INDEPENDENT icon is blocked, while the icon itself paints. */
    @Test
    public void testIndependentGraphicConflict() throws Exception {
        BufferedImage image =
                renderLabels(independentIconSymbolizer(), geometry("POINT(100 75)"), geometry("POINT(100 75)"));
        assertEquals(0, countPixels(image, LabelCacheImplTest::isBlue));
        assertThat(countPixels(image, LabelCacheImplTest::isGreen), greaterThan(0));
    }

    /** A plain label away from the INDEPENDENT icon paints. */
    @Test
    public void testIndependentGraphicFreeSpace() throws Exception {
        BufferedImage image =
                renderLabels(independentIconSymbolizer(), geometry("POINT(100 75)"), geometry("POINT(230 75)"));
        assertThat(countPixels(image, LabelCacheImplTest::isBlue), greaterThan(0));
    }

    /** A label whose INDEPENDENT icon crosses the display area edge is skipped, even when its text fits. */
    @Test
    public void testIndependentGraphicOutside() throws Exception {
        BufferedImage image =
                renderLabels(independentIconSymbolizer(), geometry("POINT(5 75)"), geometry("POINT(230 75)"));
        assertThat(countPixels(image, LabelCacheImplTest::isBlue), greaterThan(0));
        assertEquals(0, countPixels(image, LabelCacheImplTest::isGreen));
        assertEquals(0, countPixels(image, LabelCacheImplTest::isRed));
    }

    /** A label whose INDEPENDENT icon ends exactly on the display area edge paints. */
    @Test
    public void testIndependentGraphicOnEdge() throws Exception {
        BufferedImage image =
                renderLabels(independentIconSymbolizer(), geometry("POINT(100 134)"), geometry("POINT(230 75)"));
        assertThat(countPixels(image, LabelCacheImplTest::isGreen), greaterThan(0));
        assertThat(countPixels(image, LabelCacheImplTest::isRed), greaterThan(0));
    }

    /** With partials enabled, a label whose INDEPENDENT icon crosses the display area edge paints. */
    @Test
    public void testIndependentGraphicOutsidePartials() throws Exception {
        TextSymbolizer ts = independentIconSymbolizer();
        ts.getOptions().put(TextSymbolizer.PARTIALS_KEY, "true");
        BufferedImage image = renderLabels(ts, geometry("POINT(5 75)"), geometry("POINT(230 75)"));
        assertThat(countPixels(image, LabelCacheImplTest::isGreen), greaterThan(0));
        assertThat(countPixels(image, LabelCacheImplTest::isRed), greaterThan(0));
    }

    /** An INDEPENDENT image graphic is reserved where the painter draws it, following its anchor point. */
    @Test
    public void testIndependentGraphicAnchor() throws Exception {
        GraphicStyle2D graphic = new GraphicStyle2D(new BufferedImage(10, 20, BufferedImage.TYPE_INT_ARGB), 0);
        graphic.setAnchorPointX(0.5f);
        graphic.setAnchorPointY(0f);
        // anchored at its bottom center, the image sits above the point
        assertEquals(new Rectangle2D.Double(95, 55, 10, 20), independentGraphicBounds(graphic, "POINT(100 75)"));
    }

    /** An INDEPENDENT mark with explicit shape bounds, such as a cross, is reserved centered on the point. */
    @Test
    public void testIndependentMarkBounds() throws Exception {
        MarkStyle2D mark = new MarkStyle2D();
        mark.setShape(
                new WellKnownMarkFactory().getShape(null, sb.getFilterFactory().literal("cross"), null));
        mark.setSize(30);
        assertEquals(new Rectangle2D.Double(85, 60, 30, 30), independentGraphicBounds(mark, "POINT(100 75)"));
    }

    /** An INDEPENDENT vertical line mark, whose box has no width, paints inside the display area. */
    @Test
    public void testIndependentLineMark() throws Exception {
        TextSymbolizer ts = independentIconSymbolizer();
        ts.setGraphic(sb.createGraphic(null, sb.createMark("shape://vertline", Color.GREEN, 2), null, 1.0, 30, 0));
        BufferedImage image = renderLabels(ts, geometry("POINT(100 75)"), geometry("POINT(230 75)"));
        assertThat(countPixels(image, LabelCacheImplTest::isGreen), greaterThan(0));
        assertThat(countPixels(image, LabelCacheImplTest::isRed), greaterThan(0));
    }

    /** Returns the box the cache reserves for the graphic, placed INDEPENDENT at the given point. */
    private Rectangle2D independentGraphicBounds(Style2D graphic, String wkt) throws Exception {
        TextStyle2D textStyle = new TextStyle2D();
        textStyle.setGraphic(graphic);
        Point point = (Point) geometry(wkt);
        LabelCacheItem item =
                new LabelCacheItem(LAYER_ID, textStyle, new LiteShape2(point, null, null, false), "A", null);
        item.setGraphicPlacement(TextSymbolizer.GraphicPlacement.INDEPENDENT);
        return cache.independentGraphicBounds(item, point);
    }

    /** A red label pushed 60 pixels right of its green INDEPENDENT square icon, 30 pixels wide. */
    private TextSymbolizer independentIconSymbolizer() {
        Font font = sb.createFont("Bitstream Vera Sans", 12);
        TextSymbolizer ts = sb.createTextSymbolizer(Color.RED, font, "name");
        ts.setGraphic(sb.createGraphic(null, sb.createMark("square", Color.GREEN), null, 1.0, 30, 0));
        ts.getOptions().put(TextSymbolizer.GRAPHIC_PLACEMENT_KEY, "INDEPENDENT");
        ts.setLabelPlacement(sb.createPointPlacement(0.5, 0.5, 60, 0, 0));
        return ts;
    }

    /** Renders the icon label A and a plain blue label "BBBB" at the given points. */
    private BufferedImage renderLabels(TextSymbolizer iconTs, Geometry iconGeom, Geometry plain) throws Exception {
        TextSymbolizer plainTs = sb.createTextSymbolizer(Color.BLUE, sb.createFont("Bitstream Vera Sans", 12), "name");
        LabelCacheImpl local = new LabelCacheImpl();
        local.startLayer(LAYER_ID);
        // ordered labels paint last-inserted first, so insert the plain label first and the icon last: the icon
        // is painted (reserving its box) before the plain label is tested for conflict against it
        addToCache(local, plainTs, createFeature("BBBB", plain));
        addToCache(local, iconTs, createFeature("A", iconGeom));
        local.endLayer(LAYER_ID, null, null);
        BufferedImage image = new BufferedImage(300, 150, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        local.end(graphics, new Rectangle(0, 0, 300, 150));
        graphics.dispose();
        return image;
    }

    private static int countPixels(BufferedImage image, Predicate<Color> test) {
        int count = 0;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color c = new Color(image.getRGB(x, y), true);
                if (c.getAlpha() > 0 && test.test(c)) {
                    count++;
                }
            }
        }
        return count;
    }

    private static boolean isBlue(Color c) {
        return c.getBlue() > 150 && c.getRed() < 80 && c.getGreen() < 80;
    }

    private static boolean isRed(Color c) {
        return c.getRed() > 150 && c.getGreen() < 80 && c.getBlue() < 80;
    }

    private static boolean isGreen(Color c) {
        return c.getGreen() > 100 && c.getRed() < 80 && c.getBlue() < 80;
    }
}
