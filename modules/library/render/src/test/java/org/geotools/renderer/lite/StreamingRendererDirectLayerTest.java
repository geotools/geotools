package org.geotools.renderer.lite;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.swing.SwingUtilities;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DirectLayer;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.test.TestData;

import org.jaitools.swing.ImageFrame;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author michael
 */
public class StreamingRendererDirectLayerTest {
    
    private static final ReferencedEnvelope WORLD = 
            new ReferencedEnvelope(0, 1, 0, 1, DefaultGeographicCRS.WGS84);
    
    private static final Rectangle SCREEN = new Rectangle(200, 200);

    private class MockLayer extends DirectLayer {
        
        /*
         * Just draws a rectangle using graphics coordinates. 
         * 
         * TODO: Replace this with something using world coordinates
         * and the world to screen transform.
         */
        @Override
        public void draw(Graphics2D graphics, MapContent map, MapViewport viewport) {
            Graphics2D localGraphics = (Graphics2D) graphics.create();
            localGraphics.setPaint(Color.WHITE);
            localGraphics.fill(SCREEN);
            
            Rectangle inner = new Rectangle(
                    SCREEN.x + SCREEN.width / 4,
                    SCREEN.y + SCREEN.height / 4,
                    SCREEN.width / 2, SCREEN.height / 2);
            localGraphics.setPaint(Color.BLACK);
            localGraphics.fill(inner);
        }

        @Override
        public ReferencedEnvelope getBounds() {
            return WORLD;
        }
        
    }
    
    @Test
    public void rendererRecognizesDirectLayer() {
        MapContent mapContent = new MapContent();
        mapContent.addLayer( new MockLayer() );
        
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mapContent);
        
        BufferedImage image = new BufferedImage(SCREEN.width, SCREEN.height, BufferedImage.TYPE_INT_ARGB);
        mapContent.getViewport().setScreenArea(SCREEN);
        
        Graphics2D destGraphics = image.createGraphics();
        renderer.paint(destGraphics, SCREEN, WORLD);
        
        // PerceptualDiff could be used to examine the resulting image.
        // For now we just do a quick and dirty check
        Map<Integer, Integer> colorFreq = getColorFreq(image);
        assertEquals(2, colorFreq.size());
        assertTrue(colorFreq.containsKey(Color.BLACK.getRGB()));
        assertTrue(colorFreq.containsKey(Color.WHITE.getRGB()));
        
        double ratio = (double) colorFreq.get(Color.BLACK.getRGB()) /
                colorFreq.get(Color.WHITE.getRGB());
        
        assertTrue(ratio > 0.95 && ratio < 1.05);
        
        if (TestData.isInteractiveTest()) {
            CountDownLatch latch = new CountDownLatch(1);
            displayImage(image, latch);
            try {
                latch.await();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    
    /*
     * TODO: replace this with a PerceptualDiff check or something
     */
    private Map<Integer, Integer>  getColorFreq(BufferedImage image) {
        Map<Integer, Integer> colorFreq = new HashMap<Integer, Integer>();
        for (int y = image.getMinY(), ny = 0; ny < image.getHeight(); y++, ny++) {
            for (int x = image.getMinX(), nx = 0; nx < image.getWidth(); x++, nx++) {
                int rgb = image.getRGB(x, y);
                Integer freq = colorFreq.get(rgb);
                if (freq == null) {
                    colorFreq.put(rgb, 1);
                } else {
                    freq++ ;
                }
            }
        }
        return colorFreq;
    }

    private void displayImage(final BufferedImage image, final CountDownLatch latch) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TestImageFrame frame = new TestImageFrame(image, latch);
                frame.setVisible(true);
            }
        });
    }

    private static class TestImageFrame extends ImageFrame {
        BufferedImage image;
        
        public TestImageFrame(final BufferedImage image, final CountDownLatch latch) {
            super(image, "Rendered image");
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    latch.countDown();
                }
            });
            
            setSize(image.getWidth(), image.getHeight() + 21);
        }
    }
    
}
