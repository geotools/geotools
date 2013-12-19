package org.geotools.renderer.lite;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

import junit.framework.TestCase;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DefaultMapContext;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.label.LabelCacheImpl;
import org.geotools.styling.Style;
import org.geotools.test.TestData;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michaël on 17/12/13.
 */
public class TruncateLabelEnabledTest extends TestCase {

    private static final long TIME = 10000;
    SimpleFeatureSource fs_point;
    SimpleFeatureSource fs_line;
    SimpleFeatureSource fs_area;
    ReferencedEnvelope bounds;
    StreamingRenderer renderer;

    @Override
    protected void setUp() throws Exception {

        File property_point = new File(TestData.getResource(this, "truncatePointLabel.properties").toURI());
        PropertyDataStore ds_point = new PropertyDataStore(property_point.getParentFile());
        fs_point = ds_point.getFeatureSource("truncatePointLabel");

        File property_line = new File(TestData.getResource(this, "truncateLineLabel.properties").toURI());
        PropertyDataStore ds_line = new PropertyDataStore(property_line.getParentFile());
        fs_line = ds_line.getFeatureSource("truncateLineLabel");

        File property_area = new File(TestData.getResource(this, "truncateAreaLabel.properties").toURI());
        PropertyDataStore ds_area = new PropertyDataStore(property_area.getParentFile());
        fs_area = ds_area.getFeatureSource("truncateAreaLabel");

        bounds = new ReferencedEnvelope(0, 10, 0, 10, DefaultGeographicCRS.WGS84);

        renderer = new StreamingRenderer();
        Map rendererParams = new HashMap();
        LabelCacheImpl labelCache = new LabelCacheImpl();
        rendererParams.put(StreamingRenderer.LABEL_CACHE_KEY, labelCache);
        renderer.setRendererHints(rendererParams);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
    }

    /*
    public void testLabelNatural() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "truncatePointLabelEnabledNo.sld");

        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(fs_point, style);

        renderer.setContext(mc);

        RendererBaseTest.showRender("Lines with circle stroke", renderer, TIME, bounds);
    }
    */

    public void testTruncatePointLabelEnabledNo() throws Exception {
        //System.setProperty("java.awt.headless", "false");
        Thread.sleep(1000);
        Style style = RendererBaseTest.loadStyle(this, "truncatePointLabelEnabledNo.sld");

        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(fs_point, style);

        renderer.setContext(mc);
        final BufferedImage image = RendererBaseTest.renderImage(renderer, bounds, null);
        RendererBaseTest.assertPixel(image, 282,152, Color.WHITE);

        // Write to file
        // ImageIO.write(image, "png", new File("C:/Temp/testTruncatePointLabelEnabledNo.png"));
        // Interactive visualization
        // TruncateLabelEnabledTest.showImage("Point Truncate:No", TIME, image);
    }

    public void testTruncatePointLabelEnabledFalse() throws Exception {
        //System.setProperty("java.awt.headless", "false");
        Thread.sleep(1000);
        Style style = RendererBaseTest.loadStyle(this, "truncatePointLabelEnabledFalse.sld");

        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(fs_point, style);

        renderer.setContext(mc);
        final BufferedImage image = RendererBaseTest.renderImage(renderer, bounds, null);
        RendererBaseTest.assertPixel(image, 282,152, Color.WHITE);

        // Write to file
        // ImageIO.write(image, "png", new File("C:/Temp/testTruncatePointLabelEnabledFalse.png"));
        // Interactive visualization
        // TruncateLabelEnabledTest.showImage("Point Truncate:False", TIME, image);
    }

    public void testTruncatePointLabelEnabledTrue() throws Exception {
        //System.setProperty("java.awt.headless", "false");
        Thread.sleep(1000);
        Style style = RendererBaseTest.loadStyle(this, "truncatePointLabelEnabledTrue.sld");

        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(fs_point, style);

        renderer.setContext(mc);

        final BufferedImage image = RendererBaseTest.renderImage(renderer, bounds, null);
        RendererBaseTest.assertPixel(image, 282,152, Color.BLACK);

        // Write to file
        // ImageIO.write(image, "png", new File("C:/Temp/testTruncatePointLabelEnabledTrue.png"));
        // Interactive visualization
        // TruncateLabelEnabledTest.showImage("Point Truncate:True", TIME, image);
    }

    public void testTruncateLineLabelEnabledNo() throws Exception {
        //System.setProperty("java.awt.headless", "false");
        Thread.sleep(1000);
        Style style = RendererBaseTest.loadStyle(this, "truncateLineLabelEnabledNo.sld");

        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(fs_line, style);

        renderer.setContext(mc);

        final BufferedImage image = RendererBaseTest.renderImage(renderer, bounds, null);
        RendererBaseTest.assertPixel(image, 282,155, Color.WHITE);

        // Write to file
        // ImageIO.write(image, "png", new File("C:/Temp/testTruncateLineLabelEnabledNo.png"));
        // Interactive visualization
        // TruncateLabelEnabledTest.showImage("Line Truncate:No", TIME, image);
    }

    public void testTruncateLineLabelEnabledFalse() throws Exception {
        //System.setProperty("java.awt.headless", "false");
        Thread.sleep(1000);
        Style style = RendererBaseTest.loadStyle(this, "truncateLineLabelEnabledFalse.sld");

        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(fs_line, style);

        renderer.setContext(mc);

        final BufferedImage image = RendererBaseTest.renderImage(renderer, bounds, null);
        RendererBaseTest.assertPixel(image, 282,155, Color.WHITE);

        // Write to file
        // ImageIO.write(image, "png", new File("C:/Temp/testTruncateLineLabelEnabledFalse.png"));
        // Interactive visualization
        // TruncateLabelEnabledTest.showImage("Line Truncate:False", TIME, image);
    }

    public void testTruncateLineLabelEnabledTrue() throws Exception {
        //System.setProperty("java.awt.headless", "false");
        Thread.sleep(1000);
        Style style = RendererBaseTest.loadStyle(this, "truncateLineLabelEnabledTrue.sld");

        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(fs_line, style);

        renderer.setContext(mc);

        final BufferedImage image = RendererBaseTest.renderImage(renderer, bounds, null);
        RendererBaseTest.assertPixel(image, 282,155, Color.BLACK);

        // Write to file
        // ImageIO.write(image, "png", new File("C:/Temp/testTruncateLineLabelEnabledTrue.png"));
        // Interactive visualization
        // TruncateLabelEnabledTest.showImage("Line Truncate:True", TIME, image);
    }

    public void testTruncateAreaLabelEnabledNo() throws Exception {
        //System.setProperty("java.awt.headless", "false");
        Thread.sleep(1000);
        Style style = RendererBaseTest.loadStyle(this, "truncateAreaLabelEnabledNo.sld");

        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(fs_area, style);

        renderer.setContext(mc);

        final BufferedImage image = RendererBaseTest.renderImage(renderer, bounds, null);
        RendererBaseTest.assertPixel(image, 282,152, Color.WHITE);

        // Write to file
        // ImageIO.write(image, "png", new File("C:/Temp/testTruncateAreaLabelEnabledNo.png"));
        // Interactive visualization
        // TruncateLabelEnabledTest.showImage("Area Truncate:No", TIME, image);
    }

    public void testTruncateAreaLabelEnabledFalse() throws Exception {
        //System.setProperty("java.awt.headless", "false");
        Thread.sleep(1000);
        Style style = RendererBaseTest.loadStyle(this, "truncateAreaLabelEnabledFalse.sld");

        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(fs_area, style);

        renderer.setContext(mc);

        final BufferedImage image = RendererBaseTest.renderImage(renderer, bounds, null);
        RendererBaseTest.assertPixel(image, 282,152, Color.WHITE);

        // Write to file
        // ImageIO.write(image, "png", new File("C:/Temp/testTruncateAreaLabelEnabledFalse.png"));
        // Interactive visualization
        // TruncateLabelEnabledTest.showImage("Area Truncate:False", TIME, image);
    }

    public void testTruncateAreaLabelEnabledTrue() throws Exception {
        //System.setProperty("java.awt.headless", "false");
        Thread.sleep(1000);
        Style style = RendererBaseTest.loadStyle(this, "truncateAreaLabelEnabledTrue.sld");

        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(fs_area, style);

        renderer.setContext(mc);

        final BufferedImage image = RendererBaseTest.renderImage(renderer, bounds, null);
        RendererBaseTest.assertPixel(image, 282,152, Color.BLACK);

        // Write to file
        // ImageIO.write(image, "png", new File("C:/Temp/testTruncateAreaLabelEnabledTrue.png"));
        // Interactive visualization
        // TruncateLabelEnabledTest.showImage("Area Truncate:True", TIME, image);
    }


    public static void showImage(String testName, long timeOut, final BufferedImage image)
            throws InterruptedException {
        //final String headless = System.getProperty("java.awt.headless", "false");
        //if (!headless.equalsIgnoreCase("true") && TestData.isInteractiveTest()) {
            try {
                Frame frame = new Frame(testName);
                frame.addWindowListener(new WindowAdapter() {

                    public void windowClosing(WindowEvent e) {
                        e.getWindow().dispose();
                    }
                });

                Panel p = new Panel() {

                    /** <code>serialVersionUID</code> field */
                    private static final long serialVersionUID = 1L;

                    {
                        setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
                    }

                    public void paint(Graphics g) {
                        g.drawImage(image, 0, 0, this);
                    }

                };

                frame.add(p);
                frame.pack();
                frame.setVisible(true);

                Thread.sleep(timeOut);
                frame.dispose();
            } catch (HeadlessException exception) {
                // The test is running on a machine without X11 display. Ignore.
            }
        //}
    }

}
