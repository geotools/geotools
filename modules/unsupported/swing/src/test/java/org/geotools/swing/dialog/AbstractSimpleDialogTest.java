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

import static org.junit.Assert.*;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.edt.GuiTask;
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.JButtonFixture;
import org.geotools.swing.testutils.GraphicsTestBase;
import org.geotools.swing.testutils.GraphicsTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for the {@linkplain AbstractSimpleDialog} base class.
 *
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
@RunWith(GraphicsTestRunner.class)
public class AbstractSimpleDialogTest extends GraphicsTestBase<Dialog> {

    private static final String TITLE = "Foo Dialog";

    private static final String CONTROL_PANEL_NAME = "ControlPanel";
    private static final int CONTROL_PANEL_WIDTH = 400;
    private static final int CONTROL_PANEL_HEIGHT = 300;

    private static final long DEFAULT_TIMEOUT = 1000;

    private MockDialog dialog;

    @Test(expected = IllegalStateException.class)
    public void forgettingToCallInitComponentsCausesException() {
        GuiActionRunner.execute(
                new GuiTask() {
                    @Override
                    protected void executeInEDT() throws Throwable {
                        MockDialog dialog = new MockDialog(TITLE);
                        dialog.setVisible(true);
                    }
                });
    }

    @Test
    public void dialogIsModalByDefault() {
        createWindowFixture();
        ((DialogFixture) windowFixture).requireModal();
    }

    @Test
    public void dialogIsNotResizableByDefault() {
        createWindowFixture();
        assertFalse(dialog.isResizable());
    }

    @Test
    public void createNonModalDialog() {
        createWindowFixture(false, false);
        assertFalse(dialog.isModal());
    }

    @Test
    public void createResizableDialog() {
        createWindowFixture(false, true);
        assertTrue(dialog.isResizable());
    }

    @Test
    public void dialogHasOKAndCancelButtons() {
        createWindowFixture();
        JButtonFixture button = getButton("OK");
        assertNotNull(button);

        button = getButton("Cancel");
        assertNotNull(button);
    }

    @Test
    public void okButtonCallsHandlerMethod() {
        assertButtonHandlerIsCalled(MockDialog.EventType.OK, "OK");
    }

    @Test
    public void cancelButtonCallsHandlerMethod() {
        assertButtonHandlerIsCalled(MockDialog.EventType.CANCEL, "Cancel");
    }

    @Test
    public void controlPanelIsCreated() {
        createWindowFixture();

        // FEST requires that the window be shown before we search for
        // the JPanel
        ((DialogFixture) windowFixture).show();
        assertNotNull(windowFixture.panel(CONTROL_PANEL_NAME));
    }

    private void createWindowFixture() {
        dialog =
                GuiActionRunner.execute(
                        new GuiQuery<MockDialog>() {
                            @Override
                            protected MockDialog executeInEDT() throws Throwable {
                                MockDialog dialog = new MockDialog(TITLE);
                                dialog.initComponents();
                                return dialog;
                            }
                        });

        windowFixture = new DialogFixture(dialog);
    }

    private void createWindowFixture(final boolean modal, final boolean resizable) {
        dialog =
                GuiActionRunner.execute(
                        new GuiQuery<MockDialog>() {
                            @Override
                            protected MockDialog executeInEDT() throws Throwable {
                                MockDialog dialog = new MockDialog(TITLE, modal, resizable);
                                dialog.initComponents();
                                return dialog;
                            }
                        });

        windowFixture = new DialogFixture(dialog);
    }

    private void assertButtonHandlerIsCalled(MockDialog.EventType et, String btnText) {
        createWindowFixture();
        ((DialogFixture) windowFixture).show();

        dialog.setExpected(et);
        JButtonFixture button = getButton(btnText);
        button.click();

        assertTrue(dialog.await(et, DEFAULT_TIMEOUT));
    }

    public static class MockDialog extends AbstractSimpleDialog {
        public static enum EventType {
            OK,
            CANCEL;
        }

        private static final int NUM_EVENT_TYPES = EventType.values().length;

        private final CountDownLatch[] latches = new CountDownLatch[NUM_EVENT_TYPES];

        public MockDialog(String title) {
            super(title);
        }

        public MockDialog(String title, boolean modal, boolean resizable) {
            super((JFrame) null, title, modal, resizable);
        }

        @Override
        public JPanel createControlPanel() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setName(CONTROL_PANEL_NAME);
            panel.setPreferredSize(new Dimension(CONTROL_PANEL_WIDTH, CONTROL_PANEL_HEIGHT));
            return panel;
        }

        @Override
        public void onOK() {
            CountDownLatch latch = latches[EventType.OK.ordinal()];
            if (latch != null) {
                latch.countDown();
            }
        }

        @Override
        public void onCancel() {
            CountDownLatch latch = latches[EventType.CANCEL.ordinal()];
            if (latch != null) {
                latch.countDown();
            }
        }

        public void setExpected(EventType et) {
            latches[et.ordinal()] = new CountDownLatch(1);
        }

        public boolean await(EventType et, long timeout) {
            CountDownLatch latch = latches[et.ordinal()];
            if (latch == null) {
                throw new IllegalStateException("latch not set for " + et);
            }

            try {
                return latch.await(timeout, TimeUnit.MILLISECONDS);
            } catch (InterruptedException ex) {
                return false;
            }
        }
    }
}
