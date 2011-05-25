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
package org.geotools.renderer.shape;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;

import junit.framework.TestCase;

import org.geotools.TestData;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileRendererUtil;
import org.geotools.data.shapefile.dbf.IndexedDbaseFileReader;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.Filter;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.DefaultMapLayer;
import org.geotools.map.MapContext;
import org.geotools.referencing.operation.transform.IdentityTransform;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.RenderListener;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.TextSymbolizer;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Id;
import org.opengis.filter.identity.FeatureId;
import org.opengis.referencing.operation.MathTransform;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;

/**
 * Tests ShapeRenderer class
 * 
 * @author jeichar
 * @since 2.1.x
 *
 * @source $URL$
 */
public class ShapeRendererTest extends TestCase {
    private static final boolean INTERACTIVE = false;

    private static final MathTransform IDENTITY = IdentityTransform.create(2);

    private File shp2;

    private File shx2;

    private File prj2;

    private File dbf2;

    private String typename;

    private File directory;
    
    private SimpleFeature sf;

    protected void setUp() throws Exception {
        org.geotools.util.logging.Logging.getLogger("org.geotools").setLevel(Level.FINE);
        File shp = DataUtilities.urlToFile(TestData.url(Rendering2DTest.class, "theme1.shp"));
        File shx = DataUtilities.urlToFile(TestData.url(Rendering2DTest.class, "theme1.shx"));
        File prj = DataUtilities.urlToFile(TestData.url(Rendering2DTest.class, "theme1.prj"));
        File dbf = DataUtilities.urlToFile(TestData.url(Rendering2DTest.class, "theme1.dbf"));

        directory = TestData.file(Rendering2DTest.class, ".");
        
        shp2 = File.createTempFile("theme2", ".shp", directory);
        typename = shp2.getName().substring(0, shp2.getName().lastIndexOf("."));
        shx2 = new File(directory, typename + ".shx");
        prj2 = new File(directory, typename + ".prj");
        dbf2 = new File(directory, typename + ".dbf");

        copy(shp, shp2);
        copy(shx, shx2);
        copy(prj, prj2);
        copy(dbf, dbf2);
        
        // setup a sample feature
        ShapefileDataStore ds = TestUtilites.getDataStore(shp2.getName());
        SimpleFeatureType type = ds.getSchema();
        GeometryFactory gf = new GeometryFactory();
        LineString ls = gf.createLineString(new Coordinate[] {new Coordinate(0,0), new Coordinate(10,10)});
        MultiLineString mls = gf.createMultiLineString(new LineString[] {ls});
        sf = SimpleFeatureBuilder.build( type, new Object[] {mls, new Integer(0), "Hi"}, "newFeature");
    }

    protected void tearDown() throws Exception {
        dbf2.deleteOnExit();
        shx2.deleteOnExit();
        shp2.deleteOnExit();
        prj2.deleteOnExit();
        File fix=new File( directory, typename+".fix");
        File qix=new File( directory, typename+".qix");
        
        if( shp2.exists() && !shp2.delete() )
            System.out.println("failed to delete: "+shp2.getAbsolutePath());
        if( shx2.exists() && !shx2.delete() )
            System.out.println("failed to delete: "+shx2.getAbsolutePath());

        if( prj2.exists() && !prj2.delete()) 
            System.out.println("failed to delete: "+prj2.getAbsolutePath());

        if( dbf2.exists() && !dbf2.delete() )
            System.out.println("failed to delete: "+dbf2.getAbsolutePath());
        
        if( fix.exists() && !fix.delete() ){
            fix.deleteOnExit();
            System.out.println("failed to delete: "+fix.getAbsolutePath());
        }
        if( qix.exists() && !qix.delete() ){
            qix.deleteOnExit();
            System.out.println("failed to delete: "+qix.getAbsolutePath());
        }
    }

    void copy(File src, File dst) throws IOException {
        InputStream in = null;
        OutputStream out = null;

        try {
            in = new FileInputStream(src);
            out = new FileOutputStream(dst, false);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;

            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } finally {
            if (in != null) {
                in.close();
            }

            if (out != null) {
                out.close();
            }
        }
    }
    
    

    /**
     * What an ugly bug! It only happens if the DefaultMapLayer has been created with a
     * FeatureSource (not a FeatureCollection) in combination with ShapefileRenderer. With
     * StramingRenderer it works. Also works with ShapefileRenderer, if the MapLayer is created with
     * a FeatureCollection.
     * 
     * How does this test work: <br/>
     * 1. Using countries.shp which is not standard in this test package. But it's hard to show the
     * bug with lakes. <br/>
     * 2. Putting every third feature into a FidFilter 3. Creating a Style with two rules: First
     * rule paints red borders for all countries. Second rule is filtered by FidS and paints the
     * polygons with a red fill.<br/>
     * 3. Moving the BBOX 20 degree east, so the center is above a feature.<br/>
     * 4. Rendering the image once (this is the correct image!), and remembering the color of the
     * center point.<br/>
     * 5. Zooming in iteratively.. We expect the center point color not to change.<br/>
     * <br/>
     * But it changes with FeatureSource + ShapefileRenderer. Run the test interactively to see how
     * messy it is! <br/>
     * <br/>
     * It happens with lines, points and polygons. For questions contact alfonx on #geotools
     */
    public void testFidFilterWithFeatureSource() throws Exception {
        TestUtilites.INTERACTIVE = INTERACTIVE;

        // Sorry! Not part of this testing resources, but too hard to show it with lakes.
        ShapefileDataStore dataStore = TestUtilites.getDataStore("countries.shp");

        final SimpleFeatureSource featureSource = dataStore
                .getFeatureSource(dataStore.getTypeNames()[0]);
        SimpleFeatureCollection features = featureSource.getFeatures();

        // Preparing the Filter
        Set<FeatureId> selectedFids = new HashSet<FeatureId>();
        {
            Iterator<SimpleFeature> fIt = features.iterator();

            int count = 0;
            while (fIt.hasNext()) {
                SimpleFeature sf = fIt.next();

                // Add every third to the filter
                if (count++ % 3 != 0)
                    continue;

                selectedFids.add(sf.getIdentifier());
            }

            features.close(fIt);
        }
        assertEquals(84, selectedFids.size());
        Id filter = CommonFactoryFinder.getFilterFactory2(null).id(selectedFids);

        // Preparing the Style
        final StyleBuilder SB = new StyleBuilder();
        org.geotools.styling.Rule rule1 = SB.createRule(SB.createLineSymbolizer(Color.red));
        org.geotools.styling.Rule rule2 = SB.createRule(SB.createPolygonSymbolizer(Color.red));
        rule2.setFilter(filter);

        Style style = SB.createStyle();
        assertEquals(0, style.featureTypeStyles().size());

        style.featureTypeStyles().add(
                SB.createFeatureTypeStyle("Feature", new Rule[] { rule1, rule2 }));

        // One featuretypes, two rules
        assertEquals(1, style.featureTypeStyles().size());
        assertEquals(2, style.featureTypeStyles().get(0).rules().size());
        // second rule has a FID filter
        assertTrue(style.featureTypeStyles().get(0).rules().get(1).getFilter() instanceof Id);

        // WORKS: DefaultMapLayer layer = new DefaultMapLayer(featureSource.getFeatures(), style);
        DefaultMapLayer layer = new DefaultMapLayer(featureSource, style);
        MapContext mapContext = new DefaultMapContext();
        mapContext.addLayer(layer);

//        GTRenderer renderer = new StreamingRenderer();
        GTRenderer renderer = new ShapefileRenderer();
        renderer.setContext(mapContext);

        // Moving the bounds "over afrika".
        ReferencedEnvelope fullBounds = features.getBounds();
        fullBounds.translate(20, 0);
        fullBounds = zoomIn(fullBounds);

        BufferedImage correctImage = TestUtilites.showRender("full", renderer, 4000, fullBounds);
        int correctRgb = correctImage.getRGB(150, 150);
        assertEquals(-1, correctRgb);
        {
            ReferencedEnvelope zoomIn = fullBounds;
            for (int i = 1; i < 5; i++) {
                zoomIn = zoomIn(zoomIn);
                BufferedImage testImage = TestUtilites.showRender("zomming in step " + i, renderer,
                        4000, zoomIn);
                int testRgb = testImage.getRGB(150, 150);

                // Because we are zooming "into" the center, the color shouldn't change
                assertEquals(
                        "Just zooming into the mapContext, should not change the color of the center point in this test:",
                        correctRgb, testRgb);
            }
        }
    }

    /**
     * Zooms into the center. Helper-method for #testFidFilterWithFeatureSource
     * 
     * @param bounds
     * @return
     */
    private ReferencedEnvelope zoomIn(ReferencedEnvelope bounds) {
        ReferencedEnvelope b2 = new ReferencedEnvelope(bounds);
        double c = 1. / 8.;
        b2.expandBy(-b2.getSpan(0) * c, -b2.getSpan(1) * c);
        return b2;
    }

    

    public void testCreateFeature() throws Exception {
        ShapefileRenderer renderer = new ShapefileRenderer(null);
        Style style = LabelingTest.loadStyle("LineStyle.sld");
        ShapefileDataStore ds = TestUtilites.getDataStore(shp2.getName());
        IndexedDbaseFileReader reader = ShapefileRendererUtil
                        .getDBFReader(ds);
        SimpleFeatureType type = renderer.createFeatureType(null, style, ds);
        assertEquals(2, type.getAttributeCount());
        assertEquals("NAME", type.getDescriptor(0).getLocalName());
        Envelope bounds = ds.getFeatureSource().getBounds();
        ShapefileReader shpReader = ShapefileRendererUtil
                        .getShpReader(ds, bounds, 
                                new Rectangle(0,0,(int)bounds.getWidth(), (int)bounds.getHeight()),
                                IDENTITY, false, false);
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);
        SimpleFeature feature = renderer.createFeature(builder, shpReader.nextRecord(), reader, "id");
        shpReader.close();
        reader.close();
        
        assertEquals("id", feature.getID());
        assertEquals("dave street", feature.getAttribute(0));
    }

    public void testRemoveTransaction() throws Exception {
        ShapefileDataStore ds = TestUtilites.getDataStore(shp2.getName());
        System.out.println("Count: " + ds.getFeatureSource().getCount(Query.ALL));
        Style st = TestUtilites.createTestStyle(null, typename);
        final SimpleFeatureStore store;
        store = (SimpleFeatureStore) ds.getFeatureSource();
        Transaction t = new DefaultTransaction();
        store.setTransaction(t);
        SimpleFeatureCollection collection = store.getFeatures();
        SimpleFeatureIterator iter = collection.features();
        FeatureId id = TestUtilites.filterFactory.featureId(iter.next().getID());
        Id createFidFilter = TestUtilites.filterFactory.id(Collections.singleton(id));
        collection.close(iter);
        store.removeFeatures(createFidFilter);

        MapContext context = new DefaultMapContext();
        context.addLayer(store, st);
        ShapefileRenderer renderer = new ShapefileRenderer(context);
        TestUtilites.CountingRenderListener listener = new TestUtilites.CountingRenderListener(getName());
        renderer.addRenderListener(listener);
        Envelope env = context.getLayerBounds();
        int boundary = 7;
        TestUtilites.INTERACTIVE = INTERACTIVE;
        env = new Envelope(env.getMinX() - boundary, env.getMaxX() + boundary,
                env.getMinY() - boundary, env.getMaxY() + boundary);
        TestUtilites.showRender("testTransaction", renderer, 2000, env);
        assertEquals(2, listener.count);
        t.commit();

        collection = store.getFeatures();
        iter = collection.features();
        final SimpleFeature feature = iter.next();
        collection.close(iter);

        // now add a new feature new fid should be theme2.4 remove it and assure
        // that it is not rendered
        SimpleFeatureType type = store.getSchema();
        store.addFeatures(DataUtilities.collection(new SimpleFeature[] { sf } )); //$NON-NLS-1$
        t.commit();
        System.out.println("Count: " + ds.getFeatureSource().getCount(Query.ALL));
        listener.count = 0;
        TestUtilites.showRender("testTransaction", renderer, 2000, env);
        assertEquals(3, listener.count);

        iter = store.getFeatures().features();
        SimpleFeature last = null;
        while (iter.hasNext()) {
            last = iter.next();
        }
        iter.close();

        id = TestUtilites.filterFactory.featureId(last.getID());
        store.removeFeatures(TestUtilites.filterFactory.id(Collections.singleton(id)));

        listener.count = 0;
        TestUtilites.showRender("testTransaction", renderer, 2000, env);
        assertEquals(2, listener.count);

    }

    public void testAddTransaction() throws Exception {
        final ShapefileDataStore ds = TestUtilites.getDataStore(shp2.getName());
        Style st = TestUtilites.createTestStyle(null, typename);
        SimpleFeatureStore store = (SimpleFeatureStore) ds.getFeatureSource();
        Transaction t = new DefaultTransaction();
        store.setTransaction(t);
        SimpleFeatureCollection collection = store.getFeatures();
        SimpleFeatureIterator iter = collection.features();
        final SimpleFeature feature = iter.next();
        collection.close(iter);

        SimpleFeatureType type = ds.getSchema();
        store.addFeatures(DataUtilities.collection(sf));

        MapContext context = new DefaultMapContext();
        context.addLayer(store, st);
        ShapefileRenderer renderer = new ShapefileRenderer(context);
        TestUtilites.CountingRenderListener listener = new TestUtilites.CountingRenderListener(getName());
        renderer.addRenderListener(listener);
        Envelope env = context.getLayerBounds();
        int boundary = 7;
        TestUtilites.INTERACTIVE = INTERACTIVE;
        env = new Envelope(env.getMinX() - boundary, env.getMaxX() + boundary,
                env.getMinY() - boundary, env.getMaxY() + boundary);
        TestUtilites.showRender("testTransaction", renderer, 2000, env);

        assertEquals(4, listener.count);
    }

    public void testModifyTransaction() throws Exception {
        ShapefileDataStore ds = TestUtilites.getDataStore(shp2.getName());
        Style st = TestUtilites.createTestStyle(null, typename);
        SimpleFeatureStore store = (SimpleFeatureStore) ds.getFeatureSource();
        Transaction t = new DefaultTransaction();
        store.setTransaction(t);
        store.modifyFeatures(ds.getSchema().getDescriptor("NAME"), "bleep",
                Filter.NONE);

        MapContext context = new DefaultMapContext();
        context.addLayer(store, st);
        ShapefileRenderer renderer = new ShapefileRenderer(context);
        TestUtilites.CountingRenderListener listener = new TestUtilites.CountingRenderListener(getName());
        renderer.addRenderListener(listener);
        renderer.addRenderListener(new RenderListener() {

            public void featureRenderer(SimpleFeature feature) {
                assertEquals("bleep", feature.getAttribute("NAME"));
            }

            public void errorOccurred(Exception e) {
                e.printStackTrace();
                assertFalse(true);
            }

        });
        Envelope env = context.getLayerBounds();
        int boundary = 7;
        TestUtilites.INTERACTIVE = INTERACTIVE;
        env = new Envelope(env.getMinX() - boundary, env.getMaxX() + boundary,
                env.getMinY() - boundary, env.getMaxY() + boundary);
        TestUtilites.showRender("testTransaction", renderer, 2000, env);

        assertEquals(3, listener.count);
    }
    
    
        /**
     * The {@link ShapefileRenderer} is case-sensitive about its propertynames. This test checks for
     * the correct Exception when the property is wrongly spelled.
     * 
     * @throws Exception
     */
    public void testExceptionWhenPropertyDoesntExist() throws Exception {

        ShapefileDataStore store = TestUtilites.getDataStore("lakes.shp");

        // Create a Style with a wrongly spelled property name
        StyleBuilder sb = new StyleBuilder();
        TextSymbolizer ts = sb.createTextSymbolizer(Color.red, sb.createFont("serif", 15.),
                "ELEVaTION"); // Here is the mistake. The ShapefileRenderer is case-sensitive for
        // the attributes.
        Style styleWithWronglySpelledPropertyName = sb.createStyle(ts);

        /**
         * ComplexStyle and ShapefileRenderer works NOT !
         */
        {
            MapContext context = new DefaultMapContext();
            context.addLayer(store.getFeatureSource(), styleWithWronglySpelledPropertyName);

            int w = 300;
            int h = 300;
            final BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics g = image.getGraphics();
            g.setColor(Color.white);
            g.fillRect(0, 0, w, h);

            ShapefileRenderer renderer = new ShapefileRenderer();
            TestUtilites.ExceptionCollectorRenderListener listenerForEx = new TestUtilites.ExceptionCollectorRenderListener(getName());
            renderer.addRenderListener(listenerForEx);
            renderer.setContext(context);

            renderer.paint((Graphics2D) g, new Rectangle(w, h), context.getLayerBounds());

            assertEquals("Exactly one exception should have been thrown", 1,
                    listenerForEx.exceptions.size());
            assertTrue("The Exception catched is not of expected type IllegalArgumentException", listenerForEx.exceptions.get(0) instanceof IllegalArgumentException);
            assertEquals("The IllegalArgumentException catched doesn't have the expected message.",
                    "Attribute ELEVaTION does not exist. Maybe it has just been spelled wrongly?",
                    listenerForEx.exceptions.get(0).getMessage());
        }
    }                                                                                                                                 
                                                                                                                                          
               

}
