package org.geotools.renderer.lite;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.net.URL;
import javax.imageio.ImageIO;
import org.geotools.geometry.jts.LiteShape2;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.test.TestData;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.style.GraphicLegend;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public final class StyledShapePainterTest {

    @Test
    public void testGraphicLegend() throws Exception {
        
        // Load image directly from file, for comparison with painter output
        final URL imageURL = TestData.getResource(this, "icon64.png");
        final BufferedImage testImage = ImageIO.read(imageURL);        
        final int width = testImage.getWidth();
        final int height = testImage.getHeight();
        
        // Get graphic legend from style
        final Style style = RendererBaseTest.loadStyle(
                this, "testGraphicLegend.sld");
        final Rule rule = style.featureTypeStyles().get(0).rules().get(0);
        final GraphicLegend legend = rule.getLegend();
        
        // Paint legend using StyledShapePainter
        final Point point = new GeometryFactory().createPoint(
                new Coordinate(width / 2, height / 2));
        final LiteShape2 shape = new LiteShape2(point, null, null, false);
        final BufferedImage paintedImage =
                new BufferedImage(width, height, testImage.getType());
        final Graphics2D graphics = paintedImage.createGraphics();
        final StyledShapePainter painter = new StyledShapePainter();
        painter.paint(graphics, shape, legend, 1, false);
        graphics.dispose();
        
        // Ensure painted legend matches image from file
        Assert.assertTrue(imagesIdentical(paintedImage, testImage));
    }
    
    /** Determines whether two buffered images are identical. */
    private static boolean imagesIdentical(BufferedImage image1,
                                           BufferedImage image2) {
        if (image1.getType() != image2.getType()) {
            return false;
        }
        final WritableRaster raster1 = image1.getRaster();
        final WritableRaster raster2 = image2.getRaster();
        final int numBands = raster1.getNumBands();
        if (raster2.getNumBands() != numBands) {
            return false;
        }
        final int width = raster1.getWidth();
        if (raster2.getWidth() != width) {
            return false;
        }
        final int height = raster1.getHeight();
        if (raster2.getHeight() != height) {
            return false;
        }
        final int[] pixel1 = new int[numBands];
        final int[] pixel2 = new int[numBands];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                raster1.getPixel(x, y, pixel1);
                raster2.getPixel(x, y, pixel2);
                for (int i = 0; i < numBands; i++) {
                    if (pixel1[i] != pixel2[i]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
