/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.renderer.lite;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.geotools.referencing.crs.DefaultGeographicCRS.WGS84;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.util.concurrent.AtomicDouble;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.geometry.BoundingBox;
import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.api.style.Rule;
import org.geotools.api.style.Style;
import org.geotools.api.style.Symbolizer;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.util.FeatureUtilities;
import org.geotools.data.collection.CollectionFeatureSource;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.property.PropertyFeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.GeneralBounds;
import org.geotools.geometry.jts.LiteCoordinateSequence;
import org.geotools.geometry.jts.LiteShape2;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DirectLayer;
import org.geotools.map.FeatureLayer;
import org.geotools.map.GridCoverageLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.geotools.referencing.CRS;
import org.geotools.renderer.RenderListener;
import org.geotools.renderer.crs.ProjectionHandler;
import org.geotools.renderer.lite.StreamingRenderer.RenderingRequest;
import org.geotools.styling.DescriptionImpl;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.styling.StyleImpl;
import org.geotools.test.TestData;
import org.geotools.util.factory.Hints;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

/**
 * Test the inner workings of StreamingRenderer.
 *
 * <p>Rendering is a pretty high level concept
 *
 * @author Jody
 * @author PHustad
 */
public class StreamingRendererTest {

    private SimpleFeatureType testLineFeatureType;
    private SimpleFeatureType testPointFeatureType;
    private GeometryFactory gf = new GeometryFactory();
    protected int errors;
    protected int features;

    private static final class MergeLayerRequestTester extends StreamingRenderer {
        Graphics2D graphics;
        List<LiteFeatureTypeStyle> styles;

        public MergeLayerRequestTester(Graphics2D graphics, List<LiteFeatureTypeStyle> styles) {
            super();
            this.graphics = graphics;
            this.styles = styles;
            this.styles = styles;
        }

        public void mergeRequest() {

            MergeLayersRequest request = new MergeLayersRequest(graphics, styles);
            request.execute();
        }
    }

    @Before
    public void setUp() throws Exception {

        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("Lines");
        builder.add("geom", LineString.class, WGS84);
        testLineFeatureType = builder.buildFeatureType();
        builder = new SimpleFeatureTypeBuilder();
        builder.setName("Points");
        builder.add("geom", Point.class, WGS84);
        testPointFeatureType = builder.buildFeatureType();
    }

    public SimpleFeatureCollection createLineCollection() throws Exception {
        DefaultFeatureCollection fc = new DefaultFeatureCollection();
        fc.add(createLine(-177, 0, -177, 10));
        fc.add(createLine(-177, 0, -200, 0));
        fc.add(createLine(-177, 0, -177, 100));
        return fc;
    }

    private SimpleFeature createLine(double x1, double y1, double x2, double y2) {
        Coordinate[] coords = {new Coordinate(x1, y1), new Coordinate(x2, y2)};
        return SimpleFeatureBuilder.build(testLineFeatureType, new Object[] {gf.createLineString(coords)}, null);
    }

    private SimpleFeature createPoint(double x, double y) {
        Coordinate coord = new Coordinate(x, y);
        return SimpleFeatureBuilder.build(testPointFeatureType, new Object[] {gf.createPoint(coord)}, null);
    }

    private Style createLineStyle() {
        StyleBuilder sb = new StyleBuilder();
        return sb.createStyle(sb.createLineSymbolizer());
    }

    private Style createRasterStyle() {
        StyleBuilder sb = new StyleBuilder();
        return sb.createStyle(sb.createRasterSymbolizer());
    }

    private Style createPointStyle() {
        StyleBuilder sb = new StyleBuilder();
        return sb.createStyle(sb.createPointSymbolizer());
    }

    @Test
    public void testDensification() throws Exception {
        // build a feature source with two zig-zag line occupying the same position
        LiteCoordinateSequence cs = new LiteCoordinateSequence(new double[] {13, 10, 13, 40});
        SimpleFeature line =
                SimpleFeatureBuilder.build(testLineFeatureType, new Object[] {gf.createLineString(cs)}, "zz1");
        DefaultFeatureCollection fc = new DefaultFeatureCollection();
        fc.add(line);
        SimpleFeatureSource zzSource = new CollectionFeatureSource(fc);

        // prepare the map
        MapContent mc = new MapContent();
        StyleBuilder sb = new StyleBuilder();
        mc.addLayer(new FeatureLayer(zzSource, sb.createStyle(sb.createLineSymbolizer())));
        StreamingRenderer sr = new StreamingRenderer();
        Map<Object, Object> hints = new HashMap<>();
        hints.put(StreamingRenderer.ADVANCED_PROJECTION_HANDLING_KEY, true);
        hints.put(StreamingRenderer.ADVANCED_PROJECTION_DENSIFICATION_KEY, true);
        sr.setRendererHints(hints);
        sr.setMapContent(mc);

        Graphics2D graphics = Mockito.mock(Graphics2D.class);
        CoordinateReferenceSystem utm32n = CRS.decode("EPSG:32632", true);
        ReferencedEnvelope env = new ReferencedEnvelope(10, 20, 0, 40, WGS84);
        ReferencedEnvelope mapEnv = env.transform(utm32n, true);
        sr.paint(graphics, new Rectangle(0, 0, 100, 100), mapEnv);
        ArgumentCaptor<Shape> shape = ArgumentCaptor.forClass(Shape.class);
        Mockito.verify(graphics).draw(shape.capture());
        LiteShape2 drawnShape = (LiteShape2) shape.getValue();
        assertTrue(drawnShape.getGeometry().getCoordinates().length > 2);
        graphics.dispose();

        hints.put(StreamingRenderer.ADVANCED_PROJECTION_DENSIFICATION_KEY, false);
        sr.setRendererHints(hints);
        graphics = Mockito.mock(Graphics2D.class);
        sr.paint(graphics, new Rectangle(0, 0, 100, 100), mapEnv);
        shape = ArgumentCaptor.forClass(Shape.class);
        Mockito.verify(graphics).draw(shape.capture());
        drawnShape = (LiteShape2) shape.getValue();
        assertEquals(2, drawnShape.getGeometry().getCoordinates().length);
        graphics.dispose();
    }

    @Test
    public void testDensificationWithSmallDomain() throws Exception {
        // build a feature source with two zig-zag line occupying the same position
        LiteCoordinateSequence cs = new LiteCoordinateSequence(new double[] {10, 10, 10, 40});
        SimpleFeature line =
                SimpleFeatureBuilder.build(testLineFeatureType, new Object[] {gf.createLineString(cs)}, "zz1");
        DefaultFeatureCollection fc = new DefaultFeatureCollection();
        fc.add(line);
        SimpleFeatureSource zzSource = new CollectionFeatureSource(fc);

        // prepare the map
        MapContent mc = new MapContent();
        StyleBuilder sb = new StyleBuilder();
        mc.addLayer(new FeatureLayer(zzSource, sb.createStyle(sb.createLineSymbolizer())));
        StreamingRenderer sr = new StreamingRenderer();
        Map<Object, Object> hints = new HashMap<>();
        hints.put(StreamingRenderer.ADVANCED_PROJECTION_HANDLING_KEY, true);
        hints.put(StreamingRenderer.ADVANCED_PROJECTION_DENSIFICATION_KEY, true);
        sr.setRendererHints(hints);
        sr.setMapContent(mc);

        Graphics2D graphics = Mockito.mock(Graphics2D.class);
        CoordinateReferenceSystem utm32n = CRS.decode("EPSG:32632", true);
        ReferencedEnvelope env = new ReferencedEnvelope(10, 10.5, 39, 39.5, WGS84);
        ReferencedEnvelope mapEnv = env.transform(utm32n, true);
        sr.paint(graphics, new Rectangle(0, 0, 100, 100), mapEnv);
        ArgumentCaptor<Shape> shape = ArgumentCaptor.forClass(Shape.class);
        Mockito.verify(graphics).draw(shape.capture());
        LiteShape2 drawnShape = (LiteShape2) shape.getValue();
        // no densification performed
        assertEquals(2, drawnShape.getGeometry().getCoordinates().length);
        graphics.dispose();
    }

    @Test
    public void testDensificationSteps() throws Exception {
        // create a line emanating from the origin
        LiteCoordinateSequence cs = new LiteCoordinateSequence(new double[] {0, 80, 0, 81});
        SimpleFeature line =
                SimpleFeatureBuilder.build(testLineFeatureType, new Object[] {gf.createLineString(cs)}, "line");
        DefaultFeatureCollection fc = new DefaultFeatureCollection();
        fc.add(line);
        SimpleFeatureSource lineSource = new CollectionFeatureSource(fc);
        MapContent mc = new MapContent();
        StyleBuilder sb = new StyleBuilder();
        mc.addLayer(new FeatureLayer(lineSource, sb.createStyle(sb.createLineSymbolizer())));

        // setup the streaming renderer
        AtomicDouble densification = new AtomicDouble();
        StreamingRenderer sr = new StreamingRenderer() {
            @Override
            void setupDensificationHints(
                    CoordinateReferenceSystem mapCRS,
                    CoordinateReferenceSystem featCrs,
                    Rectangle screenSize,
                    AffineTransform worldToScreenTransform,
                    Map<String, Object> projectionHints,
                    double tolerance,
                    ReferencedEnvelope sourceEnvelope)
                    throws NoninvertibleTransformException, FactoryException {
                super.setupDensificationHints(
                        mapCRS,
                        featCrs,
                        screenSize,
                        worldToScreenTransform,
                        projectionHints,
                        tolerance,
                        sourceEnvelope);
                Object o = projectionHints.get(ProjectionHandler.ADVANCED_PROJECTION_DENSIFY);
                assertThat(o, Matchers.instanceOf(Double.class));
                densification.set((double) o);
            }
        };
        Map<Object, Object> hints = new HashMap<>();
        hints.put(StreamingRenderer.ADVANCED_PROJECTION_HANDLING_KEY, true);
        hints.put(StreamingRenderer.ADVANCED_PROJECTION_DENSIFICATION_KEY, true);
        sr.setRendererHints(hints);
        sr.setMapContent(mc);

        // call with just one meter width, thin but tall, envelope
        CoordinateReferenceSystem webMercator = CRS.decode("EPSG:3857");
        MathTransform mt = CRS.findMathTransform(WGS84, webMercator);
        double[] point = {0, 80};
        mt.transform(point, 0, point, 0, 1);
        ReferencedEnvelope referencedEnvelope =
                new ReferencedEnvelope(new Rectangle2D.Double(0, point[1], 1000, point[1] + 1000000), webMercator);
        Graphics2D graphics = Mockito.mock(Graphics2D.class);
        sr.paint(graphics, new Rectangle(0, 0, 1, 1000), referencedEnvelope);
        // the minimum is taken from the width of the envelope, internally before it was
        // computed with the rows and cols flipped, resulting in a different value
        assertEquals(0.045, densification.get(), 1e-3);
    }

    @Test
    public void testInterpolationByLayer() throws Exception {
        StreamingRenderer sr = new StreamingRenderer();
        Layer layer = new FeatureLayer(createLineCollection(), createLineStyle());
        // default is nearest neighbor
        assertEquals(sr.getRenderingInterpolation(layer), Interpolation.getInstance(Interpolation.INTERP_NEAREST));

        // test all possible values
        layer.getUserData()
                .put(StreamingRenderer.BYLAYER_INTERPOLATION, Interpolation.getInstance(Interpolation.INTERP_BICUBIC));
        assertEquals(sr.getRenderingInterpolation(layer), Interpolation.getInstance(Interpolation.INTERP_BICUBIC));
        layer.getUserData()
                .put(StreamingRenderer.BYLAYER_INTERPOLATION, Interpolation.getInstance(Interpolation.INTERP_BILINEAR));
        assertEquals(sr.getRenderingInterpolation(layer), Interpolation.getInstance(Interpolation.INTERP_BILINEAR));
        layer.getUserData()
                .put(StreamingRenderer.BYLAYER_INTERPOLATION, Interpolation.getInstance(Interpolation.INTERP_NEAREST));
        assertEquals(sr.getRenderingInterpolation(layer), Interpolation.getInstance(Interpolation.INTERP_NEAREST));
    }

    @Test
    public void testDrawIntepolation() throws Exception {

        MapContent mc = new MapContent();
        ReferencedEnvelope reWgs = new ReferencedEnvelope(new Envelope(-180, 180, -90, 90), WGS84);

        BufferedImage testImage = new BufferedImage(200, 200, BufferedImage.TYPE_4BYTE_ABGR);

        GridCoverage2D testCoverage = new GridCoverageFactory().create("test", testImage, reWgs);
        GridCoverage2D coverage = new GridCoverage2D("test", testCoverage);

        // mocking a GridCoverageReader to wrap the testing coverage
        GridCoverage2DReader gridCoverageReader = Mockito.mock(GridCoverage2DReader.class);
        Mockito.when(gridCoverageReader.getOriginalEnvelope()).thenReturn(new GeneralBounds(reWgs));
        Mockito.when(gridCoverageReader.getCoordinateReferenceSystem()).thenReturn(WGS84);
        Mockito.when(gridCoverageReader.read(Mockito.any(GeneralParameterValue[].class)))
                .thenReturn(coverage);

        Layer layer = new FeatureLayer(
                FeatureUtilities.wrapGridCoverageReader(gridCoverageReader, new GeneralParameterValue[] {}),
                createRasterStyle());
        layer.getUserData()
                .put(
                        StreamingRenderer.BYLAYER_INTERPOLATION,
                        Interpolation.getInstance(Interpolation.INTERP_BICUBIC_2));
        mc.addLayer(layer);

        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_4BYTE_ABGR);

        StreamingRenderer sr = new StreamingRenderer();
        sr.setMapContent(mc);

        Graphics2D graphics = (Graphics2D) image.getGraphics();

        sr.paint(graphics, new Rectangle(200, 200), reWgs);
        // test right interpolation hint is set on Graphics2D
        assertEquals(
                graphics.getRenderingHint(JAI.KEY_INTERPOLATION),
                Interpolation.getInstance(Interpolation.INTERP_BICUBIC_2));

        mc.dispose();
    }

    @Test
    public void testEventAfterDrawing() throws Exception {
        // build map context
        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(createLineCollection(), createLineStyle()));

        // build projected envelope to work with (small one around the area of
        // validity of utm zone 1, which being a Gauss projection is a vertical
        // slice parallel to the central meridian, -177°)
        ReferencedEnvelope reWgs = new ReferencedEnvelope(new Envelope(-180, -170, 20, 40), WGS84);
        CoordinateReferenceSystem utm1N = CRS.decode("EPSG:32601");
        ReferencedEnvelope reUtm = reWgs.transform(utm1N, true);

        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_4BYTE_ABGR);

        // setup the renderer and listen for errors
        final AtomicInteger commandsCount = new AtomicInteger(0);
        final BlockingQueue<RenderingRequest> queue = new ArrayBlockingQueue<>(10) {
            @Override
            public void put(RenderingRequest e) throws InterruptedException {
                commandsCount.incrementAndGet();
                super.put(e);
            }
        };
        StreamingRenderer sr = new StreamingRenderer() {
            @Override
            protected BlockingQueue<RenderingRequest> getRequestsQueue() {
                return queue;
            }
        };
        sr.setMapContent(mc);
        sr.addRenderListener(new RenderListener() {
            @Override
            public void featureRenderer(SimpleFeature feature) {
                assertTrue(commandsCount.get() > 0);
                features++;
            }

            @Override
            public void errorOccurred(Exception e) {
                errors++;
            }
        });
        errors = 0;
        features = 0;
        sr.paint((Graphics2D) image.getGraphics(), new Rectangle(200, 200), reUtm);
        mc.dispose();

        // we should get errors since there are two features that cannot be
        // projected but the renderer itself should not throw exceptions
        assertTrue(errors > 0);
    }

    @Test
    public void testInfiniteLoopAvoidance() throws Exception {
        final Exception sentinel = new RuntimeException("This is the one that should be thrown in hasNext()");

        // setup the mock necessary to have the renderer hit into the exception in hasNext()
        @SuppressWarnings("PMD.CloseResource") // just a mock
        SimpleFeatureIterator it2 = createNiceMock(SimpleFeatureIterator.class);
        expect(it2.hasNext()).andThrow(sentinel).anyTimes();
        replay(it2);

        SimpleFeatureCollection fc = createNiceMock(SimpleFeatureCollection.class);
        expect(fc.features()).andReturn(it2);
        expect(fc.size()).andReturn(200);
        expect(fc.getSchema()).andReturn(testLineFeatureType).anyTimes();
        replay(fc);

        SimpleFeatureSource fs = createNiceMock(SimpleFeatureSource.class);
        expect(fs.getFeatures((Query) anyObject())).andReturn(fc);
        expect(fs.getSchema()).andReturn(testLineFeatureType).anyTimes();
        expect(fs.getSupportedHints()).andReturn(new HashSet<>()).anyTimes();
        replay(fs);

        // build map context
        MapContent mapContext = new MapContent();
        mapContext.addLayer(new FeatureLayer(fs, createLineStyle()));

        // setup the renderer and listen for errors
        final StreamingRenderer sr = new StreamingRenderer();
        sr.setMapContent(mapContext);
        sr.addRenderListener(new RenderListener() {
            @Override
            public void featureRenderer(SimpleFeature feature) {
                features++;
            }

            @Override
            public void errorOccurred(Exception e) {
                errors++;

                if (errors > 2) {
                    // we dont' want to block the loop in case of regression on this bug
                    sr.stopRendering();
                }

                // but we want to make sure we're getting
                Throwable t = e;
                while (t != sentinel && t.getCause() != null) t = t.getCause();
                assertSame(sentinel, t);
            }
        });
        errors = 0;
        features = 0;
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_4BYTE_ABGR);
        ReferencedEnvelope reWgs = new ReferencedEnvelope(new Envelope(-180, -170, 20, 40), WGS84);
        sr.paint((Graphics2D) image.getGraphics(), new Rectangle(200, 200), reWgs);
        mapContext.dispose();

        // we should get two errors since there are two features that cannot be
        // projected but the renderer itself should not throw exceptions
        assertEquals(0, features);
        assertEquals(1, errors);
    }

    @Test
    public void testDeadlockOnException() throws Exception {

        ReferencedEnvelope reWgs = new ReferencedEnvelope(new Envelope(-180, 180, -90, 90), WGS84);

        // create the grid coverage that throws a OOM
        BufferedImage testImage = new BufferedImage(200, 200, BufferedImage.TYPE_4BYTE_ABGR);
        GridCoverage2D testCoverage = new GridCoverageFactory().create("test", testImage, reWgs);
        GridCoverage2D oomCoverage = new GridCoverage2D("test", testCoverage) {

            @Override
            public RenderedImage getRenderedImage() {
                throw new OutOfMemoryError("Boom!");
            }
        };

        // also have a collections of features to create the deadlock once the painter
        // thread is dead
        SimpleFeatureCollection lines = createLineCollection();

        Style rasterStyle = createRasterStyle();
        Style lineStyle = createLineStyle();

        MapContent mapContent = new MapContent();
        mapContent.addLayer(new GridCoverageLayer(oomCoverage, rasterStyle));
        mapContent.addLayer(new FeatureLayer(lines, lineStyle));

        final StreamingRenderer sr = new StreamingRenderer() {

            // makes it easy to reproduce the deadlock, just two features are sufficient
            @Override
            protected RenderingBlockingQueue getRequestsQueue() {
                return new RenderingBlockingQueue(1);
            }
        };
        sr.setMapContent(mapContent);
        final List<Exception> exceptions = new ArrayList<>();
        sr.addRenderListener(new RenderListener() {
            @Override
            public void featureRenderer(SimpleFeature feature) {
                features++;
            }

            @Override
            public void errorOccurred(Exception e) {
                errors++;
                exceptions.add(e);
            }
        });
        errors = 0;
        features = 0;
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_4BYTE_ABGR);
        sr.paint((Graphics2D) image.getGraphics(), new Rectangle(200, 200), reWgs);
        mapContent.dispose();

        // all the lines should have been painted, the coverage reports as painted too
        // since the reporting happens in the main thread that does not error, but with
        // the new queue draining on some systems it might not
        assertTrue(features == 4 || features == 3);
        assertEquals(1, errors);
        assertTrue(exceptions.get(0).getCause() instanceof OutOfMemoryError);
    }

    /**
     * Test that point features are rendered at the expected image coordinates when the map is rotated.
     * StreamingRenderer
     */
    @Test
    public void testRotatedTransform() throws Exception {
        // If we rotate the world rectangle + 90 degrees around (0,0), we get the screen rectangle
        final Rectangle screen = new Rectangle(0, 0, 100, 50);
        final Envelope world = new Envelope(0, 50, 0, -100);
        final AffineTransform worldToScreen = AffineTransform.getRotateInstance(Math.toRadians(90), 0, 0);
        DefaultFeatureCollection fc = new DefaultFeatureCollection();
        fc.add(createPoint(0, 0));
        fc.add(createPoint(world.getMaxX(), world.getMinY()));
        MapContent map = new MapContent();
        map.addLayer(new FeatureLayer(fc, createPointStyle()));
        BufferedImage image = new BufferedImage(screen.width, screen.height, BufferedImage.TYPE_4BYTE_ABGR);
        final StreamingRenderer sr = new StreamingRenderer();
        sr.setMapContent(map);
        sr.paint(image.createGraphics(), screen, map.getMaxBounds(), worldToScreen);
        assertNotEquals("Pixel should be drawn at 0,0 ", image.getRGB(0, 0), 0);
        assertEquals(
                "Pixel should not be drawn in image centre ", 0, image.getRGB(screen.width / 2, screen.height / 2));
        assertNotEquals(
                "Pixel should be drawn at image max corner ", image.getRGB(screen.width - 1, screen.height - 1), 0);
    }

    @Test
    public void testRepeatedEnvelopeExpansion() throws Exception {
        final List<Filter> filters = new ArrayList<>();

        SimpleFeatureSource testSource = new CollectionFeatureSource(createLineCollection()) {
            @Override
            public SimpleFeatureCollection getFeatures(Query query) {
                filters.add(query.getFilter());
                return super.getFeatures(query);
            }
        };

        StyleBuilder sb = new StyleBuilder();
        Style style20 = sb.createStyle(sb.createLineSymbolizer(20));
        Style style10 = sb.createStyle(sb.createLineSymbolizer(10));

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(testSource, style20));
        mc.addLayer(new FeatureLayer(testSource, style10));

        StreamingRenderer sr = new StreamingRenderer();
        sr.setMapContent(mc);
        BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = bi.createGraphics();
        sr.paint(graphics, new Rectangle(0, 0, 100, 100), new ReferencedEnvelope(0, 100, 0, 100, WGS84));
        graphics.dispose();
        mc.dispose();

        // System.out.println(filters);
        assertEquals(2, filters.size());
        Filter f1 = filters.get(0);
        assertTrue(f1 instanceof BBOX);
        BoundingBox bbox1 = ((BBOX) f1).getBounds();
        ReferencedEnvelope expected = new ReferencedEnvelope(-11, 111, -11, 111, WGS84);
        assertEquals(expected, bbox1);
        Filter f2 = filters.get(1);
        assertTrue(f2 instanceof BBOX);
        BoundingBox bbox2 = ((BBOX) f2).getBounds();
        assertEquals(new ReferencedEnvelope(-6, 106, -6, 106, WGS84), bbox2);
    }

    @Test
    public void testScreenMapMemory() {
        // build a feature source with two zig-zag line occupying the same position
        LiteCoordinateSequence cs = new LiteCoordinateSequence(new double[] {0, 0, 1, 1, 2, 0, 3, 1, 4, 0});
        SimpleFeature zigzag1 =
                SimpleFeatureBuilder.build(testLineFeatureType, new Object[] {gf.createLineString(cs)}, "zz1");
        SimpleFeature zigzag2 =
                SimpleFeatureBuilder.build(testLineFeatureType, new Object[] {gf.createLineString(cs)}, "zz2");
        DefaultFeatureCollection fc = new DefaultFeatureCollection();
        fc.add(zigzag1);
        fc.add(zigzag2);
        SimpleFeatureSource zzSource = new CollectionFeatureSource(fc);

        // prepare the map
        MapContent mc = new MapContent();
        StyleBuilder sb = new StyleBuilder();
        mc.addLayer(new FeatureLayer(zzSource, sb.createStyle(sb.createLineSymbolizer())));
        StreamingRenderer sr = new StreamingRenderer();
        sr.setMapContent(mc);

        // collect rendered features
        final List<SimpleFeature> features = new ArrayList<>();
        RenderListener renderedFeaturesCollector = new RenderListener() {

            @Override
            public void featureRenderer(SimpleFeature feature) {
                features.add(feature);
            }

            @Override
            public void errorOccurred(Exception e) {
                // nothing to do
            }
        };
        sr.addRenderListener(renderedFeaturesCollector);
        BufferedImage bi = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = bi.createGraphics();
        // have the lines be smaller than a 1/3 of a pixel
        sr.paint(graphics, new Rectangle(0, 0, 1, 1), new ReferencedEnvelope(0, 8, 0, 8, WGS84));

        // check we only rendered one feature
        assertEquals(1, features.size());
        assertEquals("zz1", features.get(0).getID());

        // now have the lines be big enough to be painted instead
        features.clear();
        sr.paint(graphics, new Rectangle(0, 0, 1, 1), new ReferencedEnvelope(0, 1, 0, 1, WGS84));
        mc.dispose();
        assertEquals(2, features.size());
        assertEquals("zz1", features.get(0).getID());
        assertEquals("zz2", features.get(1).getID());

        graphics.dispose();
    }

    /**
     * Test that we don't have the geometry added twice by StreamingRenderer#findStyleAttributes when geofence is
     * filtering a layer.
     */
    @Test
    public void testFindLineStyleAttributeWithAddedFilter() throws Exception {
        final List<Filter> filters = new ArrayList<>();

        SimpleFeatureSource testSource = new CollectionFeatureSource(createLineCollection()) {
            @Override
            public SimpleFeatureCollection getFeatures(Query query) {
                filters.add(query.getFilter());
                return super.getFeatures(query);
            }
        };

        Style style = createPointStyle();
        MapContent mc = new MapContent();
        FeatureLayer layer = new FeatureLayer(testSource, style);
        mc.addLayer(layer);

        StreamingRenderer sr = new StreamingRenderer();
        sr.setMapContent(mc);

        ReferencedEnvelope envelope = new ReferencedEnvelope(0, 100, 0, 100, WGS84);

        // simulate geofence adding a bbox
        BBOX bbox = StreamingRenderer.filterFactory.bbox("", 30, 60, 30, 60, "EPSG:4326");
        StyleFactoryImpl sf = new StyleFactoryImpl();
        Rule bboxRule = sf.createRule(new Symbolizer[0], new DescriptionImpl(), null, "bbox", bbox, false, 1e12, 0);
        style.featureTypeStyles().get(0).rules().add(bboxRule);

        BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = bi.createGraphics();
        try {
            sr.paint(graphics, new Rectangle(5, 5, 7, 7), envelope);
        } finally {
            graphics.dispose();
            mc.dispose();
        }

        // must have only one bbox, not two
        assertEquals(1, filters.size());
        assertEquals(FastBBOX.class, filters.get(0).getClass());
    }

    /*
     * https://osgeo-org.atlassian.net/browse/GEOT-5287
     */
    @Test
    public void testEmptyGeometryRendering() throws Exception {

        MapContent mc = new MapContent();

        /*
         * We simulate reading empty geometries with this properties and mocking the capability to
         * filter, so that no filter layer is installed over our data and the empty geometry reaches
         * rendering code. These geometries are in EPSG:32717 because the 0,0 coordinate is in the
         * pole.
         */
        File dir = new File(
                TestData.getResource(this, "empty-geom-rendering.properties").toURI());
        PropertyDataStore dataStore = new PropertyDataStore(dir.getParentFile()) {
            @Override
            protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
                return new PropertyFeatureSource(entry, Query.ALL) {
                    @Override
                    protected boolean canFilter(Query query) {
                        return true;
                    }
                };
            }
        };
        /*
         * Set up the rendering of previous empty geometry
         */
        StyleBuilder sb = new StyleBuilder();
        Style style = sb.createStyle(sb.createPolygonSymbolizer());
        Layer layer = new FeatureLayer(dataStore.getFeatureSource("empty-geom-rendering"), style);
        mc.addLayer(layer);
        StreamingRenderer sr = new StreamingRenderer();
        sr.setMapContent(mc);
        BufferedImage img = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = img.createGraphics();
        Rectangle paintArea = new Rectangle(40, 40);
        // An EPSG:8357 extent on the EPSG:32717 area of application.
        double minx = -8929252.1;
        double maxx = -8708634.6;
        double miny = -491855.7;
        double maxy = -271204.3;
        ReferencedEnvelope referencedEnvelope = new ReferencedEnvelope(
                new Rectangle2D.Double(minx, miny, maxx - minx, maxy - miny), CRS.decode("EPSG:3857"));
        sr.addRenderListener(new RenderListener() {
            @Override
            public void featureRenderer(SimpleFeature feature) {}

            @Override
            public void errorOccurred(Exception e) {
                errors++;
            }
        });
        errors = 0;

        sr.paint(graphics, paintArea, referencedEnvelope);
        mc.dispose();

        assertEquals(0, errors);
    }

    @Test
    public void testStyleThatUsesGeometryDefaultAttribute() throws Exception {
        // preparing the layer to be rendered, the provided style as a filter that will use
        // the default geometry attribute "", this will allow us to test that using geometry
        // default attribute "" is correctly handled
        StyleImpl style = (StyleImpl) RendererBaseTest.loadStyle(this, "genericLines.sld");
        File vectorDataFile =
                new File(TestData.getResource(this, "genericLines.properties").toURI());
        PropertyDataStore dataStore = new PropertyDataStore(vectorDataFile.getParentFile());
        Layer layer = new FeatureLayer(dataStore.getFeatureSource("genericLines"), style);
        // prepare map content and instantiate a streaming reader
        MapContent mapContent = new MapContent();
        mapContent.addLayer(layer);
        StreamingRenderer gRender = new StreamingRenderer();
        gRender.setMapContent(mapContent);
        gRender.addRenderListener(new RenderListener() {
            @Override
            public void featureRenderer(SimpleFeature feature) {
                features++;
            }

            @Override
            public void errorOccurred(Exception e) {
                errors++;
            }
        });
        features = 0;
        errors = 0;
        // defining the paint area and performing the rendering
        BufferedImage image = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        Rectangle paintArea = new Rectangle(40, 40);
        double minx = -2;
        double maxx = 2;
        double miny = -2;
        double maxy = 2;
        ReferencedEnvelope referencedEnvelope = new ReferencedEnvelope(
                new Rectangle2D.Double(minx, miny, maxx - minx, maxy - miny), CRS.decode("EPSG:4326"));
        gRender.paint(graphics, paintArea, referencedEnvelope);
        mapContent.dispose();
        // checking that four features were rendered, if the default geometry attribute was not
        // correctly handled no geometries were selected and so no features were rendered
        Assert.assertEquals(features, 4);
        Assert.assertEquals(errors, 0);
    }

    @Test
    public void testMergeLayerWithNullImage() {
        BufferedImage finalImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D finalGraphics = (Graphics2D) finalImage.getGraphics();
        finalGraphics.setColor(Color.RED);
        finalGraphics.fillRect(0, 0, 100, 100);

        List<LiteFeatureTypeStyle> lfts = new ArrayList<>();
        Layer layer = new DirectLayer() {

            @Override
            public void draw(Graphics2D graphics, MapContent map, MapViewport viewport) {}

            @Override
            public ReferencedEnvelope getBounds() {
                return null;
            }
        };

        // style with empty (null) image
        DelayedBackbufferGraphic graphics = new DelayedBackbufferGraphic(finalGraphics, new Rectangle(100, 100));
        LiteFeatureTypeStyle style1 =
                new LiteFeatureTypeStyle(layer, graphics, new ArrayList<>(), new ArrayList<>(), null);
        style1.composite = AlphaComposite.DstIn;
        lfts.add(style1);

        MergeLayerRequestTester renderer = new MergeLayerRequestTester(finalGraphics, lfts);
        renderer.mergeRequest();
        int color = finalImage.getRGB(0, 0);
        int red = (color & 0x00ff0000) >> 16;
        int green = (color & 0x0000ff00) >> 8;
        int blue = color & 0x000000ff;
        assertEquals(0, red);
        assertEquals(0, green);
        assertEquals(0, blue);
    }

    @Test
    public void testNPEOnMissingProjectionHandler() throws FactoryException, TransformException {
        StreamingRendererTester tester = new StreamingRendererTester();
        String wkt =
                "PROJCS[\"World_Eckert_IV\",GEOGCS[\"WGS_1984\",DATUM[\"WGS_1984\",SPHEROID[\"WGS_1984\",6378137.0,298.257223563]],PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]],PROJECTION[\"Eckert_IV\"],PARAMETER[\"Central_Meridian\",0.0],UNIT[\"Meter\",1.0]]";
        CoordinateReferenceSystem eckert = CRS.parseWKT(wkt);
        ReferencedEnvelope re = new ReferencedEnvelope(-100, 100, -100, 100, eckert);

        // this call used to throw a NPE
        ReferencedEnvelope reprojected = tester.transformEnvelope(re, WGS84);
        assertFalse(reprojected.isNull());
    }

    private static class StreamingRendererTester extends StreamingRenderer {

        /** This method is currently only used when densification is enabled */
        @Override
        public ReferencedEnvelope transformEnvelope(ReferencedEnvelope envelope, CoordinateReferenceSystem crs)
                throws TransformException, FactoryException {
            return super.transformEnvelope(envelope, crs);
        }
    }

    @Test
    public void testNPEOutsideValidArea() throws Exception {
        String wkt =
                "PROJCS[\"Homolosine\",GEOGCS[\"WGS 84\",DATUM[\"WGS_1984\",SPHEROID[\"WGS 84\",6378137,298.257223563 ] ], PRIMEM[\"Greenwich\",0.0], UNIT[\"degree\",0.01745329251994328 ]],PROJECTION[\"Goode_Homolosine\"],UNIT[\"m\",1.0] ]";
        CoordinateReferenceSystem homolosine = CRS.parseWKT(wkt);
        ReferencedEnvelope re = new ReferencedEnvelope(-180, -170, -90, -80, WGS84);
        ReferencedEnvelope cornerHomolosine = re.transform(homolosine, true);
        // move it outside of valid space
        cornerHomolosine.translate(0, -cornerHomolosine.getHeight() * 2);

        // create the map content
        SimpleFeatureCollection lines = createLineCollection();
        Style lineStyle = createLineStyle();
        MapContent mapContent = new MapContent();
        mapContent.addLayer(new FeatureLayer(lines, lineStyle));

        // render
        StreamingRenderer sr = new StreamingRenderer();
        Map<Object, Object> hints = new HashMap<>();
        hints.put(StreamingRenderer.ADVANCED_PROJECTION_HANDLING_KEY, true);
        hints.put(StreamingRenderer.ADVANCED_PROJECTION_DENSIFICATION_KEY, true);
        sr.setRendererHints(hints);
        sr.addRenderListener(new RenderListener() {
            @Override
            public void featureRenderer(SimpleFeature feature) {
                features++;
            }

            @Override
            public void errorOccurred(Exception e) {
                errors++;
            }
        });
        errors = 0;
        sr.setMapContent(mapContent);
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_4BYTE_ABGR);
        sr.paint((Graphics2D) image.getGraphics(), new Rectangle(200, 200), cornerHomolosine);
        mapContent.dispose();
        assertEquals(0, errors);
    }

    @Test
    public void testGEOT6995() throws IOException, FactoryException {
        URL url = TestData.url(StreamingRenderer.class, "mzdata/170715_1_TrjLines.shp");
        ShapefileDataStore store = new ShapefileDataStore(url);
        ContentFeatureCollection inFeatures = store.getFeatureSource().getFeatures();
        Layer fred = new FeatureLayer(inFeatures, createPointStyle());
        MapContent mapContent = new MapContent();
        mapContent.addLayer(fred);
        StreamingRenderer gRender = new StreamingRenderer();
        gRender.setMapContent(mapContent);
        gRender.addRenderListener(new RenderListener() {
            @Override
            public void featureRenderer(SimpleFeature feature) {
                features++;
            }

            @Override
            public void errorOccurred(Exception e) {
                errors++;
            }
        });
        features = 0;
        errors = 0;
        // defining the paint area and performing the rendering
        BufferedImage image = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        Rectangle paintArea = new Rectangle(40, 40);

        gRender.paint(graphics, paintArea, fred.getBounds());
        mapContent.dispose();

        Assert.assertEquals(0, errors);
        Assert.assertEquals(inFeatures.size(), features);
    }

    /**
     * When a test point for simplfication spans cannot be reprojected back to source, a simplification distance of "0"
     * is returned. It should not be sent down to the store.
     */
    @Test
    public void testInvalidSimplificationDistance() throws Exception {
        // TODO: advertise supported hints and cycle through the distance variations
        Style lineStyle = createLineStyle();
        testInvalidSimplificationDistance2(Hints.GEOMETRY_DISTANCE, lineStyle);
        testInvalidSimplificationDistance2(Hints.GEOMETRY_SIMPLIFICATION, lineStyle);
        testInvalidSimplificationDistance2(Hints.GEOMETRY_GENERALIZATION, lineStyle);
        Style rtStyle = RendererBaseTest.loadStyle(this, "attributeRename.sld");
        testInvalidSimplificationDistance2(Hints.GEOMETRY_DISTANCE, rtStyle);
        testInvalidSimplificationDistance2(Hints.GEOMETRY_SIMPLIFICATION, rtStyle);
        testInvalidSimplificationDistance2(Hints.GEOMETRY_GENERALIZATION, rtStyle);
    }

    /**
     * Tests that the simplification distance is not sent to the store when it is zero. Allows to check different
     * siplification hints, and vary the style.
     */
    void testInvalidSimplificationDistance2(final RenderingHints.Key hint, Style style) throws Exception {
        AtomicReference<Object> reference = new AtomicReference<>();
        SimpleFeatureSource testSource = new CollectionFeatureSource(createLineCollection()) {
            @Override
            public SimpleFeatureCollection getFeatures(Query query) {
                reference.set(query.getHints().get(hint));
                return super.getFeatures(query);
            }

            @Override
            public synchronized Set<RenderingHints.Key> getSupportedHints() {
                return Set.of(hint);
            }
        };

        MapContent mc = new MapContent();
        FeatureLayer layer = new FeatureLayer(testSource, style);
        mc.addLayer(layer);

        StreamingRenderer sr = new StreamingRenderer();
        sr.setMapContent(mc);

        ReferencedEnvelope envelope = new ReferencedEnvelope(-1e7, 1e7, -1e7, 1e7, CRS.decode("AUTO:42003,9001,45,0"));

        BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = bi.createGraphics();
        try {
            sr.paint(graphics, new Rectangle(5, 5, 7, 7), envelope);
        } finally {
            graphics.dispose();
            mc.dispose();
        }
        assertNull("Got a distance simplification reference, unexpected", reference.get());
    }
}
