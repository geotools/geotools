/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.swing.dialog;

import java.awt.image.Raster;
import java.awt.image.ColorModel;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.util.concurrent.CountDownLatch;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.geotools.swing.testutils.TestUtils;
import org.geotools.swing.testutils.GraphicsTestRunner;

import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JLabelFixture;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/**
 * Tests for the DialogUtils class.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
@RunWith(GraphicsTestRunner.class)
public class DialogUtilsTest {
    
    /** 
     * Set this to true to display the screen shot image of the label
     * in the test {@linkplain #labelTextIsFittedProperly()}.
     */
    private static final boolean displayLabelImage = false;
    
    @BeforeClass 
    public static void setUpOnce() {
        FailOnThreadViolationRepaintManager.install();
    }

    @Test
    public void labelTextExtentCanBeCalledSafelyOutsideEDT() {
        // Just testing there is no exception
        Dimension dim = DialogUtils.getHtmlLabelTextExtent("foo", 300, true);
    }

    /**
     * Tests display of a long html text string in a label. The string includes a
     * red dot and the start and a blue dot at the end. When the label is rendered
     * we grab a screen shot and then search for the dots to check that the whole of
     * the text string was visible.
     * <p>
     * TODO: It would also be good to have a way of checking that the label does not
     * include too much slack space.
     */
    @Test
    public void labelTextIsFittedProperly() throws Exception {
        final StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        
        // Red dot at start of text
        sb.append("<span style=\"color: #FF0000;\">.</span>");
        
        // Long text
        sb.append("This is a very very very very very very very very very very ");
        sb.append("very very very very very very very very very very very very ");
        sb.append("very very very very very very very very very very very very ");
        sb.append("long message");
        
        // Blue dot at end of text
        sb.append("<span style=\"color: #0000FF;\">.</span>");
        sb.append("</html>");
    
        final int labelWidth = 300;
        final Dimension dim = DialogUtils.getHtmlLabelTextExtent(sb.toString(), labelWidth, true);
        
        JFrame frame = GuiActionRunner.execute(new GuiQuery<JFrame>() {
            @Override
            protected JFrame executeInEDT() throws Throwable {
                JFrame frame = new JFrame();
                
                /**
                 * mbedward: 
                 * I tried overriding the label's paintComponent method to
                 * disable text anti-aliasing in order to make searching for
                 * the red and blue dots easier, but the rendering hint did
                 * not seem to affect HTML rendering. So instead, the
                 * findColorInRange method is used to allow for the fuzz of
                 * colour values.
                 */
                JLabel label = new JLabel(sb.toString());
                label.setName("TheLabel");
                
                label.setPreferredSize(dim);
                frame.add(label);
                
                frame.pack();
                return frame;
            }
        });
        
        FrameFixture fixture = new FrameFixture(frame);
        Insets insets = frame.getInsets();
        fixture.show();
        
        JLabelFixture lf = fixture.label("TheLabel");
        Point pos = lf.component().getLocationOnScreen();
        Dimension size = lf.component().getSize();
        
        Robot robot = new Robot();
        BufferedImage img = robot.createScreenCapture(new Rectangle(pos, dim));
        fixture.close();
        
        if (displayLabelImage) {
            CountDownLatch latch = TestUtils.showImage(img, "Label screen shot");
            latch.await();
        }

        // Search for the red-ish start dot
        int[] lower = new int[] {200, 0, 0};
        int[] upper = new int[] {255, 80, 80};
        Rectangle bounds = new Rectangle(img.getMinX(), img.getMinY(), 20, 20);
        assertTrue( findColorInRange(img, bounds, lower, upper) );
        
        // Search for the blue-ish end dot
        lower = new int[] {0, 0, 200};
        upper = new int[] {80, 80, 255};
        bounds = new Rectangle(
                img.getMinX(), img.getMinY() + img.getHeight() - 20, 
                img.getWidth(), 20);
        assertTrue( findColorInRange(img, bounds, lower, upper));
        
        fixture.cleanUp();
    }
    
    private boolean findColorInRange(BufferedImage img, 
            Rectangle bounds, 
            int[] lowerRGB, 
            int[] upperRGB) {
        
        final Raster raster = img.getData();
        final ColorModel cm = img.getColorModel();
        boolean found = false;
        
        for (int y = bounds.y, ny = 0; ny < bounds.height; y++, ny++) {
            for (int x = bounds.x, nx = 0; nx < bounds.width; x++, nx++) {
                int sample = img.getRGB(x, y);
                int red = cm.getRed(sample);
                int green = cm.getGreen(sample);
                int blue = cm.getBlue(sample);
                
                if (red >= lowerRGB[0] && red <= upperRGB[0] &&
                        green >= lowerRGB[1] && green <= upperRGB[1] &&
                        blue >= lowerRGB[2] && blue <= upperRGB[2]) {
                    found = true;
                    break;
                }
            }
        }
        
        return found;
    }
    
}
