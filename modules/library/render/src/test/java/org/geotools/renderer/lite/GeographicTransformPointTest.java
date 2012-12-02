package org.geotools.renderer.lite;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static org.junit.Assert.assertEquals;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import junit.framework.TestCase;

import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.Style;
import org.geotools.test.TestData;
import org.junit.Test;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * Test streaming renderer handling of 3D data (that must be transformed via WGS84).
 *
 * @source $URL$
 */
public class GeographicTransformPointTest extends TestCase {
    
    private static final long TIME = 4000;
    SimpleFeatureSource point_test;
    SimpleFeatureSource point_test_2d;
    
    @Override
    protected void setUp() throws Exception {
        // setup data
        File property = new File(TestData.getResource(this, "point_test.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        point_test = ds.getFeatureSource("point_test");
        point_test_2d = ds.getFeatureSource("point_test_2d");
    }
    
    File file(String name) {
        return new File("src/test/resources/org/geotools/renderer/lite/test-data/line/" + name
                + ".png");
    }
    
    public void testPointData() throws Exception {
        ReferencedEnvelope pointTestBounds = point_test.getBounds();
        ReferencedEnvelope geographic1 = pointTestBounds.transform( DefaultGeographicCRS.WGS84, true );
        ReferencedEnvelope geographic2 = JTS.toGeographic( pointTestBounds );
        
        assertEquals( geographic1.getMinX(), geographic2.getMinX(), 0.000005 );
        assertEquals( geographic1.getMinY(), geographic2.getMinY(), 0.000005 );
        assertEquals( geographic1.getMaxX(), geographic2.getMaxX(), 0.000005 );
        assertEquals( geographic1.getMaxY(), geographic2.getMaxY(), 0.000005 );
    }
    
    @Test
    public void testToGeographicGeometry() throws Exception {
        // This time we are in north / east order
        CoordinateReferenceSystem gda94 = CRS.decode("EPSG:4939");
        
        GeometryFactory gf = new GeometryFactory();
        Point point = gf.createPoint( new Coordinate( -16.4463909341494,130.882672103999, 97.009018073082));
        
        Point world = (Point) JTS.toGeographic( point, gda94 );
        assertEquals( point.getX(), world.getY(), 0.00000005 );
        assertEquals( point.getY(), world.getX(), 0.00000005 );
    }
    
    public void testGDA94Points() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "markTriangle.sld");
        
        MapContent content = new MapContent();
        ReferencedEnvelope bounds = JTS.toGeographic(point_test.getBounds());
        
        content.getViewport().setBounds( bounds );
        content.addLayer( new FeatureLayer( point_test, style ));
        
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(content);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        
        System.setProperty("org.geotools.test.interactive", "true");
        BufferedImage image = RendererBaseTest.showRender("GDA94 points with markTriangle stroke", renderer, TIME, bounds);
        assertNotNull( image );
        // ImageAssert.assertEquals(file("circle"), image, 10);
    }

    public void XtestWGS84Points() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "markCircle.sld");
        
        MapContent content = new MapContent();
        content.getViewport().setBounds( point_test_2d.getBounds() );
        content.addLayer( new FeatureLayer( point_test_2d, style ));
        
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(content);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        
        System.setProperty("org.geotools.test.interactive", "true");
        BufferedImage image = RendererBaseTest.showRender("WGS84 points with circle stroke", renderer, TIME, point_test_2d.getBounds());
        assertNotNull( image );
        // ImageAssert.assertEquals(file("circle"), image, 10);
    }
    
}
