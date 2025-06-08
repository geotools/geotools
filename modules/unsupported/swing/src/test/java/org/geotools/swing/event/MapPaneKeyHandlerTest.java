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

package org.geotools.swing.event;

import static org.assertj.swing.core.KeyPressInfo.keyCode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Dimension;
import java.awt.Frame;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import org.assertj.swing.core.KeyPressInfo;
import org.assertj.swing.driver.FrameDriver;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.edt.GuiQuery;
import org.assertj.swing.fixture.FrameFixture;
import org.geotools.api.geometry.Bounds;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.swing.testutils.GraphicsTestBase;
import org.geotools.swing.testutils.GraphicsTestRunner;
import org.geotools.swing.testutils.MockMapPane;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Unit tests for MapPaneKeyHandler. Requires graphics environment.
 *
 * @author Michael Bedward
 * @since 8.0
 * @version $Id$
 */
@RunWith(GraphicsTestRunner.class)
public class MapPaneKeyHandlerTest extends GraphicsTestBase<FrameFixture, Frame, FrameDriver> {
    private static final long WAIT_TIMEOUT = 1000;
    private static final int WIDTH = 200;
    private static final int HEIGHT = 150;

    private MapPaneKeyHandler handler;
    private MockMapPane2 mapPane;

    @Before
    public void setup() {
        TestFrame frame = GuiActionRunner.execute(new GuiQuery<>() {
            @Override
            protected TestFrame executeInEDT() throws Throwable {
                mapPane = new MockMapPane2();
                mapPane.setName("pane");
                handler = new MapPaneKeyHandler(mapPane);
                mapPane.addKeyListener(handler);

                TestFrame frame = new TestFrame(mapPane);
                return frame;
            }
        });

        windowFixture = new FrameFixture(frame);
        windowFixture.show(new Dimension(WIDTH, HEIGHT));
    }

    @Test
    public void scrollLeft() throws Exception {
        assertScroll(MapPaneKeyHandler.Action.SCROLL_LEFT, 1, 0);
    }

    @Test
    public void scrollRight() throws Exception {
        assertScroll(MapPaneKeyHandler.Action.SCROLL_RIGHT, -1, 0);
    }

    @Test
    public void scrollUp() throws Exception {
        assertScroll(MapPaneKeyHandler.Action.SCROLL_UP, 0, 1);
    }

    @Test
    public void scrollDown() throws Exception {
        assertScroll(MapPaneKeyHandler.Action.SCROLL_DOWN, 0, -1);
    }

    @Ignore("problem with this test")
    @Test
    public void zoomIn() throws Exception {
        ReferencedEnvelope startEnv = mapPane.getDisplayArea();

        KeyPressInfo info = getKeyPressInfo(MapPaneKeyHandler.Action.ZOOM_IN);
        windowFixture.panel("pane").pressAndReleaseKey(info);

        assertTrue(mapPane.latch.await(WAIT_TIMEOUT, TimeUnit.MILLISECONDS));

        ReferencedEnvelope endEnv = mapPane.getDisplayArea();
        assertEquals(-1, sign(endEnv.getWidth() - startEnv.getWidth()));
    }

    private void assertScroll(MapPaneKeyHandler.Action action, int expectedDx, int expectedDy) throws Exception {

        KeyPressInfo info = getKeyPressInfo(action);
        windowFixture.panel("pane").pressAndReleaseKey(info);

        assertTrue(mapPane.latch.await(WAIT_TIMEOUT, TimeUnit.MILLISECONDS));
        assertEquals(sign(expectedDx), sign(mapPane.dx));
        assertEquals(sign(expectedDy), sign(mapPane.dy));
    }

    private int sign(int i) {
        return i < 0 ? -1 : i > 0 ? 1 : 0;
    }

    private int sign(double d) {
        return Double.compare(d, 0);
    }

    /**
     * Looks up the key binding for an action and converts it to a FEST KeyPressInfo object.
     *
     * @param action the action
     * @return a new KeyPressInfo object
     */
    private KeyPressInfo getKeyPressInfo(MapPaneKeyHandler.Action action) {
        KeyInfo keyId = handler.getBindingForAction(action);
        return keyCode(keyId.getKeyCode()).modifiers(keyId.getModifiers());
    }

    /** A frame containing a mock map pane. */
    private static class TestFrame extends JFrame {
        public TestFrame(final MockMapPane mapPane) {
            add(mapPane);
        }
    }

    private static class MockMapPane2 extends MockMapPane {
        CountDownLatch latch = new CountDownLatch(1);

        ReferencedEnvelope env = new ReferencedEnvelope(0, 100, 0, 100, null);
        int dx = 0;
        int dy = 0;

        @Override
        public void moveImage(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
            latch.countDown();
        }

        @Override
        public ReferencedEnvelope getDisplayArea() {
            return new ReferencedEnvelope(env);
        }

        @Override
        public void setDisplayArea(Bounds envelope) {
            this.env = new ReferencedEnvelope(envelope);
            latch.countDown();
        }

        @Override
        public void reset() {
            latch.countDown();
        }
    }
}
