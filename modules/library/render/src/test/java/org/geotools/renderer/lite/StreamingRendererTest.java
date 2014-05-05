/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createNiceMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.data.Query;
import org.geotools.data.collection.CollectionFeatureSource;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.LiteCoordinateSequence;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.FeatureLayer;
import org.geotools.map.GridCoverageLayer;
import org.geotools.map.MapContent;
import org.geotools.map.MapContext;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.RenderListener;
import org.geotools.renderer.lite.StreamingRenderer.RenderingRequest;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.spatial.BBOX;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

/**
 * Test the inner workings of StreamingRenderer.
 * <p>
 * Rendering is a pretty high level concept
 * @author Jody
 * @author PHustad
 *
 *
 *
 * @source $URL$
 */
public class StreamingRendererTest {

    private SimpleFeatureType testLineFeatureType;
    private SimpleFeatureType testPointFeatureType;
    private GeometryFactory gf = new GeometryFactory();
    protected int errors;
    protected int features;
    
    @Before
    public void setUp() throws Exception {

        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("Lines");
        builder.add("geom", LineString.class, DefaultGeographicCRS.WGS84);
        testLineFeatureType = builder.buildFeatureType();
        builder =  new SimpleFeatureTypeBuilder();
        builder.setName("Points");
        builder.add("geom", Point.class, DefaultGeographicCRS.WGS84);
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
        Coordinate[] coords = new Coordinate[] { new Coordinate(x1, y1), new Coordinate(x2, y2) };
        return SimpleFeatureBuilder.build(testLineFeatureType, new Object[] { gf.createLineString(coords) }, null);
    }
    
    private SimpleFeature createPoint(double x, double y) {
        Coordinate coord = new Coordinate(x, y);
        return SimpleFeatureBuilder.build(testPointFeatureType, new Object[] { gf.createPoint(coord) }, null);
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
    public void testEventAfterDrawing() throws Exception {
        // build map context
        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(createLineCollection(), createLineStyle()));

        // build projected envelope to work with (small one around the area of
        // validity of utm zone 1, which being a Gauss projection is a vertical 
        // slice parallel to the central meridian, -177°)
        ReferencedEnvelope reWgs = new ReferencedEnvelope(new Envelope(-180,
                -170, 20, 40), DefaultGeographicCRS.WGS84);
        CoordinateReferenceSystem utm1N = CRS.decode("EPSG:32601");
        ReferencedEnvelope reUtm = reWgs.transform(utm1N, true);

        BufferedImage image = new BufferedImage(200, 200,BufferedImage.TYPE_4BYTE_ABGR);

        // setup the renderer and listen for errors
        final AtomicInteger commandsCount = new AtomicInteger(0);
        final BlockingQueue<RenderingRequest> queue = new ArrayBlockingQueue<RenderingRequest>(10) {
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
            public void featureRenderer(SimpleFeature feature) {
                assertTrue(commandsCount.get() > 0);
                features++;
            }
            public void errorOccurred(Exception e) {
                errors++;
            }
        });
        errors = 0;
        features = 0;
        sr.paint((Graphics2D) image.getGraphics(), new Rectangle(200, 200),reUtm);
        
        // we should get errors since there are two features that cannot be
        // projected but the renderer itself should not throw exceptions
        assertTrue(errors > 0);
    }

    @Test
    public void testInfiniteLoopAvoidance() throws Exception {
        final Exception sentinel = new RuntimeException("This is the one that should be thrown in hasNext()");
        
        // setup the mock necessary to have the renderer hit into the exception in hasNext()
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
        expect(fs.getSupportedHints()).andReturn(new HashSet()).anyTimes();
        replay(fs);
        
        // build map context
        MapContext mapContext = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mapContext.addLayer(fs, createLineStyle());
        
        // setup the renderer and listen for errors
        final StreamingRenderer sr = new StreamingRenderer();
        sr.setContext(mapContext);
        sr.addRenderListener(new RenderListener() {
            public void featureRenderer(SimpleFeature feature) {
                features++;
            }
            public void errorOccurred(Exception e) {
                errors++;
                
                if(errors > 2) {
                    // we dont' want to block the loop in case of regression on this bug
                    sr.stopRendering();
                }

                // but we want to make sure we're getting
                Throwable t = e;
                while(t != sentinel && t.getCause() != null)
                    t = t.getCause();
                assertSame(sentinel, t);
            }
        });
        errors = 0;
        features = 0;
        BufferedImage image = new BufferedImage(200, 200,BufferedImage.TYPE_4BYTE_ABGR);
        ReferencedEnvelope reWgs = new ReferencedEnvelope(new Envelope(-180,
                -170, 20, 40), DefaultGeographicCRS.WGS84);
        sr.paint((Graphics2D) image.getGraphics(), new Rectangle(200, 200),reWgs);
        
        // we should get two errors since there are two features that cannot be
        // projected but the renderer itself should not throw exceptions
        assertEquals(0, features);
        assertEquals(1, errors);
    }
    
    @Test
    public void testDeadlockOnException() throws Exception {
        
        ReferencedEnvelope reWgs = new ReferencedEnvelope(new Envelope(-180,
                180, -90, 90), DefaultGeographicCRS.WGS84);
        
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
            protected BlockingQueue<RenderingRequest> getRequestsQueue() {
                return new RenderingBlockingQueue(1);
            }  
        };
        sr.setMapContent(mapContent);
        final List<Exception> exceptions = new ArrayList<Exception>();
        sr.addRenderListener(new RenderListener() {
            public void featureRenderer(SimpleFeature feature) {
                features++;
            }
            
            public void errorOccurred(Exception e) {
                errors++;
                exceptions.add(e);
            }
        });
        errors = 0;
        features = 0;
        BufferedImage image = new BufferedImage(200, 200,BufferedImage.TYPE_4BYTE_ABGR);
        sr.paint((Graphics2D) image.getGraphics(), new Rectangle(200, 200),reWgs);
        
        // all the lines should have been painted, the coverage reports as painted too 
        // since the reporting happens in the main thread that does not error
        assertEquals(4, features);
        assertEquals(1, errors);
        assertTrue(exceptions.get(0).getCause() instanceof OutOfMemoryError);
    }
    
    
    /**
     * Test that point features are rendered at the expected 
     * image coordinates when the map is rotated.
     * StreamingRenderer
     * @throws Exception
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
        MapContext mapContext = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mapContext.addLayer((FeatureCollection)fc, createPointStyle());
        BufferedImage image = new BufferedImage(screen.width, screen.height,
                BufferedImage.TYPE_4BYTE_ABGR);
        final StreamingRenderer sr = new StreamingRenderer();
        sr.setContext(mapContext);
        sr.paint(image.createGraphics(), screen, worldToScreen);
        assertTrue("Pixel should be drawn at 0,0 ", image.getRGB(0, 0) != 0);
        assertTrue("Pixel should not be drawn in image centre ", image.getRGB(screen.width / 2,
                screen.height / 2) == 0);
        assertTrue("Pixel should be drawn at image max corner ", image.getRGB(screen.width - 1,
                screen.height - 1) != 0);
    }
    
    @Test
    public void testRepeatedEnvelopeExpansion() throws Exception {
        final List<Filter> filters = new ArrayList<Filter>();
        
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
        sr.paint(graphics, new Rectangle(0, 0, 100, 100), new ReferencedEnvelope(0, 100, 0, 100, DefaultGeographicCRS.WGS84));
        graphics.dispose();
        
        System.out.println(filters);
        assertEquals(2, filters.size());
        Filter f1 = filters.get(0);
        assertTrue(f1 instanceof BBOX);
        BoundingBox bbox1 = ((BBOX) f1).getBounds();
        ReferencedEnvelope expected = new ReferencedEnvelope(-11, 111, -11, 111, DefaultGeographicCRS.WGS84);
        assertEquals(expected, bbox1);
        Filter f2 = filters.get(1);
        assertTrue(f2 instanceof BBOX);
        BoundingBox bbox2 = ((BBOX) f2).getBounds();
        assertEquals(new ReferencedEnvelope(-6, 106, -6, 106, DefaultGeographicCRS.WGS84), bbox2);
    }
    
    @Test
    public void testScreenMapMemory() {
        // build a feature source with two zig-zag line occupying the same position
        LiteCoordinateSequence cs = new LiteCoordinateSequence(new double[] {0, 0, 1, 1, 2, 0, 3, 1, 4, 0});
        SimpleFeature zigzag1 = SimpleFeatureBuilder.build(testLineFeatureType, new Object[] { gf.createLineString(cs) }, "zz1");
        SimpleFeature zigzag2 = SimpleFeatureBuilder.build(testLineFeatureType, new Object[] { gf.createLineString(cs) }, "zz2");
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
        final List<SimpleFeature> features = new ArrayList<SimpleFeature>();
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
        sr.paint(graphics, new Rectangle(0, 0, 1, 1), new ReferencedEnvelope(0, 8, 0, 8, DefaultGeographicCRS.WGS84));
        
        // check we only rendered one feature
        assertEquals(1, features.size());
        assertEquals("zz1", features.get(0).getID());
        
        // now have the lines be big enough to be painted instead
        features.clear();
        sr.paint(graphics, new Rectangle(0, 0, 1, 1), new ReferencedEnvelope(0, 1, 0, 1, DefaultGeographicCRS.WGS84));
        assertEquals(2, features.size());
        assertEquals("zz1", features.get(0).getID());
        assertEquals("zz2", features.get(1).getID());

        
        graphics.dispose();
    }
    
}


