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

package org.geotools.swing.testutils;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.RenderedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CountDownLatch;

import javax.swing.SwingUtilities;

import org.jaitools.swing.ImageFrame;


/**
 * Wraps a JAITools {@linkplain ImageFrame} and a {@linkplain CountDownLatch} to enable test
 * methods to display the image and then wait until the frame is dismissed.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class TestImageFrame {
    
    /**
     * Displays the given image in an {@linkplain ImageFrame} and counts down the
     * returned latch when the frame is closed. This allows test methods to display 
     * an image and then wait until the user dismisses the frame.
     * <pre><code>
     * // In test method
     * RenderedImage img = ...
     * CountDownLatch latch = TestImageFrame.showImage(img, "Look at this image");
     * 
     * // Wait for the user to close the frame
     * latch.await();
     * </code></pre>
     * 
     * This method can be safely called from any thread.
     * 
     * @param image image to display
     * @param title frame title (may be {@code null}
     * 
     * @return the latch which will be set to zero when the frame is closed
     * 
     * @throws InterruptedException if the thread is interrupted while the image
     *     frame is being created and shown
     * 
     * @throws InvocationTargetException on internal error
     */
    public static CountDownLatch showImage(final RenderedImage image, final String title) 
            throws InterruptedException, InvocationTargetException {
        
        if (image == null) {
            throw new IllegalArgumentException("image must not be null");
        }
        
        final String frameTitle;
        if (title == null || title.trim().length() == 0) {
            frameTitle = "Image";
        } else {
            frameTitle = title;
        }
        
        final CountDownLatch latch = new CountDownLatch(1);
        
        if (SwingUtilities.isEventDispatchThread()) {
            doShowImage(image, frameTitle, latch);
        } else {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    doShowImage(image, frameTitle, latch);
                }
            });
        }
        
        return latch;
    }

    /**
     * Helper method which always runs on the event dispatch thread.
     * 
     * @param image image to display
     * @param title frame title
     * @param latch latch to count down when the frame is closed
     */
    private static void doShowImage(final RenderedImage image, 
            final String title,
            final CountDownLatch latch) {
        
        ImageFrameWithLatch frame = new ImageFrameWithLatch(image, title, latch);
        frame.setVisible(true);
    }
    
    private static class ImageFrameWithLatch extends ImageFrame {
        private final CountDownLatch closeLatch;

        public ImageFrameWithLatch(RenderedImage img, String title, CountDownLatch latch) {
            super(img, title);
            this.closeLatch = latch;
            
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    closeLatch.countDown();
                }
            });
        }
        
    }

}
