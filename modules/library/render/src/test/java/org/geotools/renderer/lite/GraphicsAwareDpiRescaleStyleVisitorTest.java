package org.geotools.renderer.lite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.styling.Graphic;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.visitor.RescaleStyleVisitor;
import org.geotools.styling.visitor.UomRescaleStyleVisitor;
import org.geotools.util.URLs;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;

public class GraphicsAwareDpiRescaleStyleVisitorTest {

    private StyleBuilder sb;
    private FilterFactory2 ff;

    @Before
    public void setup() {
        sb = new StyleBuilder();
        ff = CommonFactoryFinder.getFilterFactory2(null);
    }

    @Test
    public void testResizeMark() {
        PointSymbolizer ps =
                sb.createPointSymbolizer(sb.createGraphic(null, sb.createMark("square"), null));
        GraphicsAwareDpiRescaleStyleVisitor visitor = new GraphicsAwareDpiRescaleStyleVisitor(2);
        ps.accept(visitor);
        PointSymbolizer resized = (PointSymbolizer) visitor.getCopy();
        Expression size = resized.getGraphic().getSize();
        assertTrue(size instanceof Literal);
        assertEquals(32, size.evaluate(null, Integer.class).intValue());
    }

    @Test
    public void testResizeExternalGraphic() throws IOException {
        File imageFile =
                new File("./src/test/resources/org/geotools/renderer/lite/test-data/draw.png")
                        .getCanonicalFile();
        assertTrue(imageFile.exists());
        String fileUrl = URLs.fileToUrl(imageFile).toExternalForm();
        PointSymbolizer ps =
                sb.createPointSymbolizer(
                        sb.createGraphic(
                                null, null, sb.createExternalGraphic(fileUrl, "image/png")));
        GraphicsAwareDpiRescaleStyleVisitor visitor = new GraphicsAwareDpiRescaleStyleVisitor(2);
        ps.accept(visitor);
        PointSymbolizer resized = (PointSymbolizer) visitor.getCopy();
        Expression size = resized.getGraphic().getSize();
        assertTrue(size instanceof Literal);
        // original image height was 22
        assertEquals(44, size.evaluate(null, Integer.class).intValue());
    }

    /**
     * Tests size calculation of dynamically sized feature, using real-world units combined with
     * DPI-based resizing.
     */
    @Test
    public void testCombinedResizingDpiUom() {
        // given: Point with dynamic size in real-world  units
        int sizeNum = 10;
        double scaleDpi = 2;
        double scaleUom = 3;
        PointSymbolizer symbolizer = sb.createPointSymbolizer();
        Graphic graphic = sb.createGraphic();
        graphic.setSize(ff.function("strConcat", ff.property("size"), ff.literal("m")));
        symbolizer.setGraphic(graphic);

        // when: DPI based resizing is applied
        RescaleStyleVisitor dpiVisitor = new GraphicsAwareDpiRescaleStyleVisitor(scaleDpi);
        symbolizer.accept(dpiVisitor);
        symbolizer = (PointSymbolizer) dpiVisitor.getCopy();

        // and: UOM resizing is applied
        UomRescaleStyleVisitor uomVisitor = new UomRescaleStyleVisitor(scaleUom);
        uomVisitor.visit(symbolizer);
        symbolizer = (PointSymbolizer) uomVisitor.getCopy();

        SimpleFeature feature = createFeatureSized(sizeNum);

        // then: only the UOM resizing must have an effect
        Double size = symbolizer.getGraphic().getSize().evaluate(feature, Double.class);
        assertEquals(sizeNum * scaleUom, size.doubleValue(), 0d);
    }

    SimpleFeature createFeatureSized(int sizeNum) {
        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.setName("geotools");
        ftb.add("size", Integer.class);
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(ftb.buildFeatureType());
        fb.set("size", sizeNum);
        SimpleFeature feature = fb.buildFeature(null);
        return feature;
    }
}
