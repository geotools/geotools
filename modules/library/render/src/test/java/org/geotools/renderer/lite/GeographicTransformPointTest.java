package org.geotools.renderer.lite;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static org.junit.Assert.assertEquals;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import junit.framework.TestCase;

import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureSource;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.GeoTools;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.referencing.CRS;
import org.geotools.referencing.CRS.AxisOrder;
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
    SimpleFeatureSource point_test_strict;
    SimpleFeatureSource point_test_2d;
    
    @Override
    protected void setUp() throws Exception {
        System.setProperty( GeoTools.FORCE_LONGITUDE_FIRST_AXIS_ORDER, "true" );
        CRS.reset("all");
        // setup data
        File property = new File(TestData.getResource(this, "point_test.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        point_test = ds.getFeatureSource("point_test");
        point_test_strict = ds.getFeatureSource("point_test_strict");
        point_test_2d = ds.getFeatureSource("point_test_2d");
    }
    
    File file(String name) {
        return new File("src/test/resources/org/geotools/renderer/lite/test-data/line/" + name
                + ".png");
    }
    @Test
    public void testBounds() throws Exception {
        ReferencedEnvelope bounds2d = point_test_2d.getBounds();
        ReferencedEnvelope bounds3d = point_test.getBounds();
        double aspect2d = bounds2d.getWidth() / bounds2d.getHeight();
        double aspect3d = bounds3d.getWidth() / bounds3d.getHeight();
        assertEquals( aspect2d, aspect3d, 0.0005 );
        
        ReferencedEnvelope bbox2d = JTS.toGeographic( bounds2d );
        ReferencedEnvelope bbox3d = JTS.toGeographic( bounds3d );
        
        aspect2d = bbox2d.getWidth() / bbox2d.getHeight();
        aspect3d = bbox3d.getWidth() / bbox3d.getHeight();
        assertEquals( aspect2d, aspect3d, 0.0005 );
        
        
    }
    @Test
    public void testToGeographicGeometry() throws Exception {
        // This time we are in north / east order
        CoordinateReferenceSystem gda94 = CRS.decode("EPSG:4939", true);
        
        GeometryFactory gf = new GeometryFactory();
        Point point = gf.createPoint( new Coordinate( 130.882672103999, -16.4463909341494, 97.009018073082));
        
        Point world = (Point) JTS.toGeographic( point, gda94 );
        assertEquals( point.getX(), world.getX(), 0.00000005 );
        assertEquals( point.getY(), world.getY(), 0.00000005 );
    }
    
    @Test
    public void testGDA94Points() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "markCircle.sld");
        
        BufferedImage reference = toImage( point_test_2d, style );
        BufferedImage actual = null;
        if( CRS.getAxisOrder( point_test_strict.getSchema().getCoordinateReferenceSystem() ) == AxisOrder.NORTH_EAST  ){
            actual = toImage( point_test_strict, style );
        }
        if( CRS.getAxisOrder( point_test.getSchema().getCoordinateReferenceSystem() ) == AxisOrder.EAST_NORTH ){
            actual = toImage( point_test, style );
        }
        ImageAssert.assertEquals( reference, actual, 10 );
    }

    private BufferedImage toImage(SimpleFeatureSource featuerSource, Style style) throws Exception {
        String typeName = featuerSource.getSchema().getTypeName();
        
        MapContent content = new MapContent();
        ReferencedEnvelope dataBounds = featuerSource.getBounds();
        assertNotNull( typeName+" bounds",dataBounds);
        assertFalse( typeName+" bounds empty",dataBounds.isEmpty() );
        assertFalse( typeName+" bounds null",dataBounds.isNull() );
        
        ReferencedEnvelope bounds = JTS.toGeographic(dataBounds);
        assertNotNull(typeName + " world", bounds);
        assertTrue(
                typeName + " world WGS84",
                CRS.equalsIgnoreMetadata(DefaultGeographicCRS.WGS84,
                        bounds.getCoordinateReferenceSystem()));
        assertFalse(typeName + " world empty", bounds.isEmpty());
        assertFalse(typeName + " world null", bounds.isNull());
        
        ReferencedEnvelope reference = point_test_2d.getBounds();
        
        content.getViewport().setBounds(reference);
        assertTrue(CRS.equalsIgnoreMetadata(DefaultGeographicCRS.WGS84, content.getViewport()
                .getCoordinateReferenceSystem()));
        
        content.addLayer( new FeatureLayer( featuerSource, style ));
        
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(content);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        
        BufferedImage image = RendererBaseTest.showRender( typeName, renderer, TIME, bounds);
        assertNotNull( image );
        
        return image;
    }

}
