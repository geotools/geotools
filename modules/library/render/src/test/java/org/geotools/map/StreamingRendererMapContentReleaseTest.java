package org.geotools.map;

import static org.geotools.map.MapContent.UNDISPOSED_MAPCONTENT_ERROR;
import static org.hamcrest.MatcherAssert.assertThat;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.Random;
import java.util.logging.Level;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.FilterFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.RenderListener;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.*;
import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

/**
 * This test checks that we are not leaving undisposed map contents created inside the streaming
 * renderer. Done my best to isolate from other tests being run, but specific JVMs might ignore
 * that... if we see it is breaking the build on some platform we'll have to remove it
 */
@Ignore // sigh, as expected it's not working all the time
public class StreamingRendererMapContentReleaseTest extends LoggerTest {

    private static final StyleFactory sf = CommonFactoryFinder.getStyleFactory();
    private static final FilterFactory ff = CommonFactoryFinder.getFilterFactory();
    private static final GeometryFactory geom = JTSFactoryFinder.getGeometryFactory();

    @BeforeClass
    public static void setupOnce() {
        // isolate this test from previously run ones that might have left undisposed map contents
        System.gc();
        Runtime.getRuntime().runFinalization();
    }

    @Test
    public void testMapContentRelease() {
        // create a map content
        MapContent content = new MapContent();

        DefaultFeatureCollection coll = new DefaultFeatureCollection();
        FeatureLayer layer = new FeatureLayer(coll, createDefaultStyle());
        FeatureLayer layer2 = new FeatureLayer(coll, createDefaultStyle());
        content.addLayer(layer);
        content.addLayer(layer2);

        SimpleFeatureTypeBuilder tbuilder = new SimpleFeatureTypeBuilder();
        tbuilder.setName("feature1");
        tbuilder.setCRS(DefaultGeographicCRS.WGS84);
        tbuilder.add("geometry", Geometry.class);
        tbuilder.add("name", String.class);
        SimpleFeatureType type = tbuilder.buildFeatureType();

        // populate with random features
        int featureNumber = 50;
        ReferencedEnvelope bounds =
                new ReferencedEnvelope(-20, 20, -30, 30, DefaultGeographicCRS.WGS84);
        PrimitiveIterator.OfDouble rand =
                new Random().doubles(bounds.getMinX(), bounds.getMaxX()).iterator();
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(type);
        for (int i = 0; i < featureNumber; i++) {

            List<Coordinate> points = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                points.add(new Coordinate(rand.next(), rand.next()));
            }

            fb.add(geom.createLineString(points.toArray(new Coordinate[points.size()])));
            fb.add("Feature " + i);
            coll.add(fb.buildFeature(null));
        }

        // render in a loop, test logs
        for (int i = 0; i < 10; i++) {
            grabLogger(Level.SEVERE);
            renderAndStop(content, bounds);
            Runtime.getRuntime().runFinalization();
            String messages = getLogOutput();
            assertThat(
                    messages,
                    CoreMatchers.not(CoreMatchers.containsString(UNDISPOSED_MAPCONTENT_ERROR)));
            releaseLogger();
        }
    }

    private void renderAndStop(MapContent content, ReferencedEnvelope bounds) {
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(content);

        // stop rendering after 10 features
        renderer.addRenderListener(
                new RenderListener() {
                    int count = 0;

                    @Override
                    public void featureRenderer(SimpleFeature feature) {
                        count++;
                        if (count > 10) {
                            renderer.stopRendering();
                        }
                    }

                    @Override
                    public void errorOccurred(Exception e) {}
                });

        Rectangle area = new Rectangle(0, 0, 2000, 2000);
        BufferedImage img = new BufferedImage(area.width, area.height, BufferedImage.TYPE_INT_ARGB);
        renderer.paint((Graphics2D) img.getGraphics(), area, bounds);
    }

    private static StyleImpl createDefaultStyle() {

        Color foreground = Color.darkGray;
        int thick = 3;

        // create stroke
        StrokeImpl stroke =
                sf.stroke(ff.literal(foreground), null, ff.literal(thick), null, null, null, null);

        // create line symbolizer
        LineSymbolizerImpl lineSym = (LineSymbolizerImpl) sf.createLineSymbolizer(stroke, null);

        // create rule
        RuleImpl r = (RuleImpl) sf.createRule();
        r.symbolizers().add(lineSym);

        // add it to style
        StyleImpl style = sf.createStyle();
        FeatureTypeStyleImpl fts = sf.createFeatureTypeStyle();
        fts.rules().add(r);
        style.featureTypeStyles().add(fts);

        return style;
    }
}
