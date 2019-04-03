package org.geotools.renderer.lite;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import junit.framework.TestCase;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.map.DirectLayer;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.Style;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.visitor.DuplicatingStyleVisitor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class LabelWrapTest extends TestCase {

    private static final long TIME = 10000;
    SimpleFeatureSource fs;
    ReferencedEnvelope bounds;

    @Override
    protected void setUp() throws Exception {
        RendererBaseTest.setupVeraFonts();

        bounds = new ReferencedEnvelope(0, 10, 0, 10, null);

        // System.setProperty("org.geotools.test.interactive", "true");

        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.add("geom", Point.class);
        builder.add("label", String.class);
        builder.setName("labelWrap");
        SimpleFeatureType type = builder.buildFeatureType();

        GeometryFactory gf = new GeometryFactory();
        SimpleFeature f1 =
                SimpleFeatureBuilder.build(
                        type,
                        new Object[] {
                            gf.createPoint(new Coordinate(5, 8)), "A long label, no newlines"
                        },
                        null);
        SimpleFeature f2 =
                SimpleFeatureBuilder.build(
                        type,
                        new Object[] {
                            gf.createPoint(new Coordinate(5, 5)), "A long label\nwith newlines"
                        },
                        null);
        SimpleFeature f3 =
                SimpleFeatureBuilder.build(
                        type,
                        new Object[] {
                            gf.createPoint(new Coordinate(5, 2)), "A long label with (parenthesis)"
                        },
                        null);

        MemoryDataStore data = new MemoryDataStore();
        data.addFeature(f1);
        data.addFeature(f2);
        data.addFeature(f3);
        fs = data.getFeatureSource("labelWrap");
    }

    public void testNoAutoWrap() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "textWrapDisabled.sld");
        BufferedImage image = renderLabels(fs, style, "Label wrap disabled");
        String refPath =
                "./src/test/resources/org/geotools/renderer/lite/test-data/textWrapDisabled.png";
        ImageAssert.assertEquals(new File(refPath), image, 3000);
    }

    public void testAutoWrap() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "textWrapEnabled.sld");
        BufferedImage image = renderLabels(fs, style, "Label wrap enabled");
        String refPath =
                "./src/test/resources/org/geotools/renderer/lite/test-data/textWrapEnabled.png";
        ImageAssert.assertEquals(new File(refPath), image, 3000);
    }

    public void testAutoWrapWithIncreasedSpacing() throws Exception {
        Style spacedStyle =
                getCharSpacedStyle("textWrapEnabled.sld", TextSymbolizer.CHAR_SPACING_KEY, 5);
        BufferedImage image =
                renderLabels(fs, spacedStyle, "Label wrap enabled with extra char spacing");
        String refPath =
                "./src/test/resources/org/geotools/renderer/lite/test-data/textWrapEnabledSpaceIncreased.png";
        ImageAssert.assertEquals(new File(refPath), image, 3000);
    }

    public void testAutoWrapWithDecreasedSpacing() throws Exception {
        Style spacedStyle =
                getCharSpacedStyle("textWrapEnabled.sld", TextSymbolizer.CHAR_SPACING_KEY, -2);
        BufferedImage image =
                renderLabels(fs, spacedStyle, "Label wrap enabled with extra char spacing");
        String refPath =
                "./src/test/resources/org/geotools/renderer/lite/test-data/textWrapEnabledSpaceDecreased.png";
        ImageAssert.assertEquals(new File(refPath), image, 3000);
    }

    public void testAutoWrapWithIncreasedWordSpacing() throws Exception {
        Style spacedStyle =
                getCharSpacedStyle("textWrapEnabled.sld", TextSymbolizer.WORD_SPACING_KEY, 15);
        BufferedImage image =
                renderLabels(fs, spacedStyle, "Label wrap enabled with extra char spacing");
        String refPath =
                "./src/test/resources/org/geotools/renderer/lite/test-data/textWrapEnabledWordSpaceIncreased.png";
        ImageAssert.assertEquals(new File(refPath), image, 3000);
    }

    private Style getCharSpacedStyle(String styleFile, String key, float spacing)
            throws IOException {
        Style style = RendererBaseTest.loadStyle(this, styleFile);
        DuplicatingStyleVisitor visitor =
                new DuplicatingStyleVisitor() {
                    @Override
                    public void visit(TextSymbolizer text) {
                        super.visit(text);
                        TextSymbolizer ts = (TextSymbolizer) getCopy();
                        ts.getOptions().put(key, String.valueOf(spacing));
                    }
                };
        style.accept(visitor);
        Style spacedStyle = (Style) visitor.getCopy();
        return spacedStyle;
    }

    public void testAutoWrapLocalTransform() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "textWrapEnabled.sld");

        MapContent mc = new MapContent();
        mc.getViewport().setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
        mc.addLayer(new FeatureLayer(fs, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        renderer.setMapContent(mc);

        int SIZE = 300;
        final BufferedImage image = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, SIZE, SIZE);
        g.setTransform(
                new AffineTransform(
                        1.1,
                        Math.sin(Math.toRadians(15)),
                        -Math.sin(Math.toRadians(15)),
                        1.1,
                        15,
                        20));
        renderer.paint(g, new Rectangle(SIZE, SIZE), bounds);
        mc.dispose();
        renderer.getMapContent().dispose();

        String refPath =
                "./src/test/resources/org/geotools/renderer/lite/test-data/textWrapEnabledLocalTransform.png";
        ImageAssert.assertEquals(new File(refPath), image, 3000);
    }

    public void testDirectLayerLabelInteraction() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "textWrapEnabled.sld");
        MapContent mc = new MapContent();
        mc.getViewport().setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
        mc.addLayer(new FeatureLayer(fs, style));
        mc.addLayer(
                new DirectLayer() {

                    @Override
                    public ReferencedEnvelope getBounds() {
                        // no dice
                        return null;
                    }

                    @Override
                    public void draw(Graphics2D graphics, MapContent map, MapViewport viewport) {
                        // nothing to do, just want to check labels are painted anyways
                    }
                });

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        renderer.setMapContent(mc);

        BufferedImage image =
                RendererBaseTest.showRender("Label and direct layers", renderer, TIME, bounds);
        String refPath =
                "./src/test/resources/org/geotools/renderer/lite/test-data/textWrapEnabled.png";
        ImageAssert.assertEquals(new File(refPath), image, 3000);
    }

    private BufferedImage renderLabels(SimpleFeatureSource fs, Style style, String title)
            throws Exception {
        MapContent mc = new MapContent();
        mc.getViewport().setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
        mc.addLayer(new FeatureLayer(fs, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        renderer.setMapContent(mc);

        return RendererBaseTest.showRender(title, renderer, TIME, bounds);
    }
}
