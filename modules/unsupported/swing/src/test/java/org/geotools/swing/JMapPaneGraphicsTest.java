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

package org.geotools.swing;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.edt.GuiQuery;
import org.assertj.swing.edt.GuiTask;
import org.assertj.swing.fixture.FrameFixture;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.geotools.swing.event.MapPaneEvent;
import org.geotools.swing.testutils.GraphicsTestRunner;
import org.geotools.swing.testutils.WaitingMapPaneListener;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Unit tests for JMapPane methods which require graphics.
 *
 * @author Michael Bedward
 * @since 8.0
 * @version $Id$
 */
@RunWith(GraphicsTestRunner.class)
public class JMapPaneGraphicsTest extends JMapPaneGraphicsTestBase {

    /**
     * Set this to true to display the screen shot image of the label in the test {@linkplain
     * #labelTextIsFittedProperly()}.
     */
    private static final boolean displayLabelImage = false;

    @BeforeClass
    public static void setUpOnce() {
        FailOnThreadViolationRepaintManager.install();
    }

    @Before
    public void setup() {
        listener = new WaitingMapPaneListener();

        TestFrame frame =
                GuiActionRunner.execute(
                        new GuiQuery<TestFrame>() {
                            @Override
                            protected TestFrame executeInEDT() throws Throwable {
                                return new TestFrame(listener);
                            }
                        });

        window = new FrameFixture(frame);
    }

    @After
    public void cleanup() {
        window.cleanUp();
        listener = null;
        mapPane = null;
    }

    /** Test for GEOT-6342, background color is not used in 1st rendering of map */
    @Test
    public void drawLayersSetsBackgroundonStartup()
            throws InvocationTargetException, InterruptedException, IOException {

        window.show(new Dimension(WIDTH, HEIGHT));
        MapContent mapContent = createMapContent(createMatchedBounds(mapPane.getVisibleRect()));
        mapPane.setMapContent(mapContent);
        SwingUtilities.invokeAndWait(
                new Runnable() {

                    @Override
                    public void run() {
                        mapPane.setBackground(Color.BLUE);
                    }
                });

        mapPane.drawLayers(true);
        BufferedImage image = (BufferedImage) mapPane.getBaseImage();

        WritableRaster raster = image.getRaster();
        int[] pixel = new int[4];
        raster.getPixel(1, 1, pixel);
        assertEquals("Pixel at (1,1) was not renderered", 0, pixel[0]);
        assertEquals("Pixel at (1,1) was not renderered", 0, pixel[1]);
        assertEquals("Pixel at (1,1) was not renderered", 255, pixel[2]);
    }

    @Test
    public void resizingPaneFiresEvent() {
        window.show(new Dimension(WIDTH, HEIGHT));
        MapContent mapContent = createMapContent(createMatchedBounds(mapPane.getVisibleRect()));
        mapPane.setMapContent(mapContent);

        listener.setExpected(MapPaneEvent.Type.DISPLAY_AREA_CHANGED);
        window.resizeTo(new Dimension(WIDTH * 2, HEIGHT * 2));
        assertTrue(listener.await(MapPaneEvent.Type.DISPLAY_AREA_CHANGED, WAIT_TIMEOUT));

        Object obj = listener.getEvent(MapPaneEvent.Type.DISPLAY_AREA_CHANGED).getData();
        assertNotNull(obj);
        assertTrue(obj instanceof ReferencedEnvelope);
    }

    @Test
    public void moveImageUp() {
        // Remember: moving image up means negative dy
        assertMoveImage(0, -10);
    }

    @Test
    public void moveImageDown() {
        // Remember: moving image down means positive dy
        assertMoveImage(0, 10);
    }

    @Test
    public void moveImageLeft() {
        assertMoveImage(-10, 0);
    }

    @Test
    public void moveImageRight() {
        assertMoveImage(10, 0);
    }

    private void assertMoveImage(int dx, int dy) {
        window.show(new Dimension(WIDTH, HEIGHT));
        MapContent mapContent = createMapContent(createMatchedBounds(mapPane.getVisibleRect()));

        // Wait for the display area and new map content events to be
        // processed
        listener.setExpected(MapPaneEvent.Type.DISPLAY_AREA_CHANGED);
        listener.setExpected(MapPaneEvent.Type.NEW_MAPCONTENT);
        mapPane.setMapContent(mapContent);

        assertTrue(listener.await(MapPaneEvent.Type.DISPLAY_AREA_CHANGED, WAIT_TIMEOUT));
        assertTrue(listener.await(MapPaneEvent.Type.NEW_MAPCONTENT, WAIT_TIMEOUT));
        ReferencedEnvelope startEnv = mapContent.getViewport().getBounds();

        listener.setExpected(MapPaneEvent.Type.DISPLAY_AREA_CHANGED);
        mapPane.moveImage(dx, dy);
        assertTrue(listener.await(MapPaneEvent.Type.DISPLAY_AREA_CHANGED, WAIT_TIMEOUT));

        ReferencedEnvelope newEnv = mapPane.getDisplayArea();

        assertEquals(startEnv.getWidth(), newEnv.getWidth(), TOL);
        assertEquals(startEnv.getHeight(), newEnv.getHeight(), TOL);

        if (dx == 0) {
            assertEquals(startEnv.getMinX(), newEnv.getMinX(), TOL);
        } else if (dx < 0) {
            assertTrue(startEnv.getMinX() < newEnv.getMinX());
        } else {
            assertTrue(startEnv.getMinX() > newEnv.getMinX());
        }

        if (dy == 0) {
            assertEquals(startEnv.getMinY(), newEnv.getMinY(), TOL);
        } else if (dy < 0) {
            assertTrue(startEnv.getMinY() > newEnv.getMinY());
        } else {
            assertTrue(startEnv.getMinY() < newEnv.getMinY());
        }
    }

    @Test
    public void mapPaneShouldHonourInitialViewportBounds() throws Exception {
        window.show(new Dimension(WIDTH, HEIGHT));
        Rectangle visRect = mapPane.getVisibleRect();
        GuiActionRunner.execute(
                new GuiTask() {
                    @Override
                    protected void executeInEDT() throws Throwable {
                        window.target().setVisible(false);
                    }
                });

        ReferencedEnvelope fullBounds = createMatchedBounds(mapPane.getVisibleRect());
        MapContent mapContent = createMapContent(fullBounds);

        ReferencedEnvelope subBounds =
                new ReferencedEnvelope(
                        fullBounds.getMinX(),
                        fullBounds.getMinX() + fullBounds.getWidth() / 2,
                        fullBounds.getMinY(),
                        fullBounds.getMinY() + fullBounds.getHeight() / 2,
                        fullBounds.getCoordinateReferenceSystem());

        mapContent.getViewport().setBounds(subBounds);
        listener.setExpected(MapPaneEvent.Type.NEW_MAPCONTENT);
        mapPane.setMapContent(mapContent);

        // wait for the map pane to be ready
        assertTrue(listener.await(MapPaneEvent.Type.NEW_MAPCONTENT, WAIT_TIMEOUT));

        window.show(new Dimension(WIDTH, HEIGHT));
        ReferencedEnvelope displayArea = mapPane.getDisplayArea();

        assertTrue(subBounds.boundsEquals2D(displayArea, TOL));
    }
}
