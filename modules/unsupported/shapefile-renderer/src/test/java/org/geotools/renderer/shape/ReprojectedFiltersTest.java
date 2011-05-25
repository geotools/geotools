package org.geotools.renderer.shape;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.geotools.TestData;
import org.geotools.data.DefaultQuery;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.DefaultMapLayer;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.RenderListener;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * @author svn history suggests Andrea Aime was here
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 *
 *
 * @source $URL$
 */
public class ReprojectedFiltersTest extends TestCase {

    private ShapefileDataStore ds;
    protected int features;
    protected int errors;


    @Override
    protected void setUp() throws Exception {
        String shapeName = "shapes/statepop.shp";
        final File shape = TestData.copy(this, shapeName);
        assertTrue(shape.canRead());
        assertTrue(TestData.copy(this, sibling(shapeName, "dbf")).canRead());
        assertTrue(TestData.copy(this, sibling(shapeName, "shx")).canRead());
        assertTrue(TestData.copy(this, sibling(shapeName, "prj")).canRead());
        
        ds = new ShapefileDataStore(shape.toURI().toURL());
    }
    
    @Override
    protected void tearDown() throws Exception {
    	ds.dispose();
    	super.tearDown();
    }

    /**
     * Helper method for {@link #copyShapefiles}.
     */
    private static String sibling(String name, final String ext) {
        final int s = name.lastIndexOf('.');
        if (s >= 0) {
            name = name.substring(0, s);
        }
        return name + '.' + ext;
    }

    
    public void testReprojectFilter() throws Exception {
        CoordinateReferenceSystem mercator = CRS.decode("EPSG:3395", true);
        ReferencedEnvelope world = new ReferencedEnvelope(-180, 180, -80, 80, DefaultGeographicCRS.WGS84);
        ReferencedEnvelope worldMercator = world.transform(mercator, true);
        
        StyleBuilder sb = new StyleBuilder();
        Style s = sb.createStyle(sb.createPolygonSymbolizer()); 
        
        Filter filter = CQL.toFilter("CONTAINS(the_geom, POINT(-100 32))");
        MapContext mc = new DefaultMapContext(mercator);
        DefaultMapLayer l = new DefaultMapLayer(ds.getFeatureSource(), s);
        l.setQuery(new DefaultQuery("statepop", filter));
        mc.addLayer(l);
        
        ShapefileRenderer r = new ShapefileRenderer(mc);
        r.addRenderListener(new RenderListener() {

            public void featureRenderer(SimpleFeature feature) {
                features++;
                assertEquals("statepop.15", feature.getID());
            }
        
            public void errorOccurred(Exception e) {
                errors++;
            }
        });
        
        BufferedImage image = new BufferedImage(400, 300, BufferedImage.TYPE_4BYTE_ABGR);
        final Graphics2D graphics = (Graphics2D) image.getGraphics();
        r.paint(graphics, new Rectangle(image.getWidth(), image.getHeight()), worldMercator);
        
        assertEquals(1, features);
        assertEquals(0, errors);
    }

    /**
     * Test {@link FeatureSourceMapLayer} using a variant of {@link #testReprojectFilter()}.
     * 
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    public void testReprojectFilterFeatureSourceMapLayer() throws Exception {
        CoordinateReferenceSystem mercator = CRS.decode("EPSG:3395", true);
        ReferencedEnvelope world = new ReferencedEnvelope(-180, 180, -80, 80, DefaultGeographicCRS.WGS84);
        ReferencedEnvelope worldMercator = world.transform(mercator, true);
        StyleBuilder sb = new StyleBuilder();
        Style s = sb.createStyle(sb.createPolygonSymbolizer()); 
        Filter f = CQL.toFilter("CONTAINS(the_geom, POINT(-100 32))");
        MapContext mc = new DefaultMapContext(mercator);
        MapLayer l = new DefaultMapLayer( new FeatureLayer(ds.getFeatureSource(), s));
        l.setQuery(new DefaultQuery("statepop", f));
        mc.addLayer(l);
        
        SimpleFeatureCollection sanityCheck = ds.getFeatureSource().getFeatures(f);
        SimpleFeatureIterator iter = sanityCheck.features();
        final Set<String> expected = new HashSet<String>();
        while( iter.hasNext() ){
            SimpleFeature feature = iter.next();
            String fid = feature.getID();
            expected.add( fid );
        }
        iter.close();
        System.out.println(expected);
        
        ShapefileRenderer r = new ShapefileRenderer(mc);
        r.addRenderListener(new RenderListener() {
            public void featureRenderer(SimpleFeature feature) {
                features++;
                String fid = feature.getID();
                assertTrue("Unexpected result " + fid + " - " + feature.getAttribute("STATE_NAME"), expected.contains( fid ));
            }        
            public void errorOccurred(Exception e) {
                errors++;
            }
        });
        BufferedImage image = new BufferedImage(400, 300, BufferedImage.TYPE_4BYTE_ABGR);
        final Graphics2D graphics = (Graphics2D) image.getGraphics();
        r.paint(graphics, new Rectangle(image.getWidth(), image.getHeight()), worldMercator);
        assertEquals(1, features);
        assertEquals(0, errors);
    }

}
