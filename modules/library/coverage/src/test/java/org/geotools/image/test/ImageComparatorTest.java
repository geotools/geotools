package org.geotools.image.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import org.geotools.image.test.ImageComparator.Mode;
import org.junit.Test;

public class ImageComparatorTest {

    @Test
    public void testDifferentImage() {
        compareDifferentImage(BufferedImage.TYPE_4BYTE_ABGR);
        compareDifferentImage(BufferedImage.TYPE_3BYTE_BGR);
        compareDifferentImage(BufferedImage.TYPE_BYTE_GRAY);
        compareDifferentImage(BufferedImage.TYPE_BYTE_INDEXED);
    }

    private void compareDifferentImage(int imageType) {
        BufferedImage image1 = new BufferedImage(200, 200, imageType);
        Graphics2D gr = image1.createGraphics();
        gr.setColor(Color.BLUE);
        gr.setStroke(new BasicStroke(4));
        gr.drawLine(0, 0, 200, 200);
        gr.dispose();

        BufferedImage image2 = new BufferedImage(200, 200, imageType);
        gr = image2.createGraphics();
        gr.setColor(Color.BLUE);
        gr.setStroke(new BasicStroke(4));
        gr.drawLine(0, 0, 200, 200);
        gr.fillRect(180, 50, 10, 10);
        gr.dispose();

        ImageComparator comparator = new ImageComparator(Mode.IgnoreNothing, image1, image2);
        assertEquals(100, comparator.getMismatchCount());
        assertEquals(0.0025, comparator.getMismatchPercent(), 0.0001);
    }

    @Test
    public void testSameImage() {
        compareSameImage(BufferedImage.TYPE_4BYTE_ABGR);
        compareSameImage(BufferedImage.TYPE_3BYTE_BGR);
        compareSameImage(BufferedImage.TYPE_BYTE_GRAY);
        compareSameImage(BufferedImage.TYPE_BYTE_INDEXED);
    }

    private void compareSameImage(int imageType) {
        BufferedImage image1 = new BufferedImage(200, 200, imageType);
        Graphics2D gr = image1.createGraphics();
        gr.setColor(Color.BLUE);
        gr.setStroke(new BasicStroke(4));
        gr.drawLine(0, 0, 200, 200);
        gr.dispose();

        BufferedImage image2 = new BufferedImage(200, 200, imageType);
        gr = image2.createGraphics();
        gr.setColor(Color.BLUE);
        gr.setStroke(new BasicStroke(4));
        gr.drawLine(0, 0, 200, 200);
        gr.dispose();

        ImageComparator comparator = new ImageComparator(Mode.IgnoreNothing, image1, image2);
        assertEquals(0l, comparator.getMismatchCount());
        assertEquals(0d, comparator.getMismatchPercent(), 0d);
    }

    @Test
    public void testAntialiasDifferences() throws Exception {
        compareAntialiasedImage(BufferedImage.TYPE_4BYTE_ABGR);
        compareAntialiasedImage(BufferedImage.TYPE_3BYTE_BGR);
        compareAntialiasedImage(BufferedImage.TYPE_BYTE_GRAY);
        compareAntialiasedImage(BufferedImage.TYPE_BYTE_INDEXED);
    }

    private void compareAntialiasedImage(int imageType) throws IOException {
        BufferedImage image1 = new BufferedImage(200, 200, imageType);
        Graphics2D gr = image1.createGraphics();
        gr.setColor(Color.BLUE);
        gr.setStroke(new BasicStroke(4));
        gr.drawLine(0, 0, 200, 200);
        gr.dispose();

        BufferedImage image2 = new BufferedImage(200, 200, imageType);
        gr = image2.createGraphics();
        gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gr.setColor(Color.BLUE);
        gr.setStroke(new BasicStroke(4));
        gr.drawLine(0, 0, 200, 200);
        gr.dispose();

        ImageComparator comparatorStrict = new ImageComparator(Mode.IgnoreNothing, image1, image2);
        assertTrue(comparatorStrict.getMismatchCount() > 0);
        assertTrue(comparatorStrict.getMismatchPercent() < 0.05);

        ImageComparator comparatorAA = new ImageComparator(Mode.IgnoreAntialiasing, image1, image2);
        assertEquals(0l, comparatorAA.getMismatchCount());
        assertEquals(0d, comparatorAA.getMismatchPercent(), 0d);
    }

    @Test
    public void testColorDifferences() throws Exception {
        compareDifferentColor(BufferedImage.TYPE_4BYTE_ABGR);
        compareDifferentColor(BufferedImage.TYPE_3BYTE_BGR);
        compareDifferentColor(BufferedImage.TYPE_BYTE_GRAY);
        compareDifferentColor(BufferedImage.TYPE_BYTE_INDEXED);
    }

    private void compareDifferentColor(int imageType) throws IOException {
        BufferedImage image1 = new BufferedImage(200, 200, imageType);
        Graphics2D gr = image1.createGraphics();
        gr.setColor(new Color(0, 0, 255));
        gr.setStroke(new BasicStroke(4));
        gr.drawLine(0, 0, 200, 200);
        gr.dispose();

        BufferedImage image2 = new BufferedImage(200, 200, imageType);
        gr = image2.createGraphics();
        gr.setColor(new Color((int) Math.round(255 * 0.11 / 0.3), 0, 0));
        gr.setStroke(new BasicStroke(4));
        gr.drawLine(0, 0, 200, 200);
        gr.dispose();

        ImageComparator comparatorIC = new ImageComparator(Mode.IgnoreColors, image1, image2);
        assertEquals(0l, comparatorIC.getMismatchCount());
        assertEquals(0d, comparatorIC.getMismatchPercent(), 0d);
    }
}
