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

import java.awt.AWTEvent;
import java.awt.Dialog;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.JButtonFixture;
import org.fest.swing.fixture.JListFixture;
import org.fest.swing.fixture.JTextComponentFixture;
import org.geotools.factory.Hints;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.swing.testutils.GraphicsTestBase;
import org.geotools.swing.testutils.GraphicsTestRunner;
import org.geotools.swing.testutils.WindowActivatedListener;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Tests for {@linkplain JCRSChooser}.
 *
 * <p>This test class uses an {@linkplain ExecutorService} to launch the dialog which avoids a
 * deadlock between the dialog waiting for a user response and this class waiting for the dialog to
 * show up on the event thread.
 *
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
@RunWith(GraphicsTestRunner.class)
public class JCRSChooserTest extends GraphicsTestBase<Dialog> {

    // Max waiting time for return value
    private static final long RETURN_TIMEOUT = 1000;
    // A filter string that resolves to just one CRS list entry with EPSG
    private static final String UNIQUE_FILTER_STRING = "32766";
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Random rand = new Random();
    private static CRSAuthorityFactory FACTORY;
    private static List<String> CODES;
    private WindowActivatedListener listener;

    @BeforeClass
    public static void setUpOnce() throws Exception {
        // Specify longitude-first order for the authority factory
        Hints hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
        FACTORY =
                ReferencingFactoryFinder.getCRSAuthorityFactory(
                        JCRSChooser.DEFAULT_AUTHORITY, hints);

        CODES = new ArrayList<String>(FACTORY.getAuthorityCodes(CoordinateReferenceSystem.class));
    }

    @Before
    public void setup() {
        listener = new WindowActivatedListener(JCRSChooser.CRSDialog.class);
        Toolkit.getDefaultToolkit().addAWTEventListener(listener, AWTEvent.WINDOW_EVENT_MASK);
    }

    @After
    public void cleanup() {
        Toolkit.getDefaultToolkit().removeAWTEventListener(listener);
    }

    @Test
    public void checkDefaultDialogIsSetupCorrectly() throws Exception {
        showDialog();
        ((DialogFixture) windowFixture).requireModal();
        assertEquals(JCRSChooser.DEFAULT_TITLE, windowFixture.component().getTitle());

        // The text box should be editable and initially empty
        JTextComponentFixture textBox = windowFixture.textBox();
        assertNotNull(textBox);
        textBox.requireEditable();
        textBox.requireText("");

        // The list should have as many items as there are authority codes
        JListFixture list = windowFixture.list();
        assertNotNull(list);
        list.requireItemCount(CODES.size());
    }

    @Test
    public void customTitle() throws Exception {
        final String TITLE = "Custom title";
        showDialog(TITLE);
        assertEquals(TITLE, windowFixture.component().getTitle());
    }

    @Test
    public void cancellingDialogReturnsNull() throws Exception {
        Future<CoordinateReferenceSystem> future = showDialog();

        JButtonFixture button = getButton("Cancel");
        button.click();

        CoordinateReferenceSystem crs = retrieveCRS(future);
        assertNull(crs);
    }

    @Test
    public void okButtonDisabledInitially() throws Exception {
        showDialog();
        windowFixture.list().requireNoSelection();
        getButton("OK").requireDisabled();
    }

    @Test
    public void okButtonEnabledWhenListHasSelection() throws Exception {
        showDialog();
        windowFixture.list().clickItem(0);
        windowFixture.robot.waitForIdle();
        getButton("OK").requireEnabled();
    }

    @Test
    public void okButtonStateWithListSize() throws Exception {
        showDialog();
        JButtonFixture button = getButton("OK");

        // Filter to a single item - button should be enabled
        JTextComponentFixture textBox = windowFixture.textBox();
        textBox.enterText(UNIQUE_FILTER_STRING);
        windowFixture.robot.waitForIdle();
        button.requireEnabled();

        // Remove filter (no list selection) - button should be disabled
        textBox.deleteText();
        windowFixture.robot.waitForIdle();
        windowFixture.list().requireNoSelection();
        button.requireDisabled();
    }

    @Test
    public void okButtonDisabledWhenListSelectionIsCleared() throws Exception {
        showDialog();

        // Make a list selection, then clear it
        JListFixture list = windowFixture.list();
        list.clickItem(0);
        windowFixture.robot.waitForIdle();

        list.clearSelection();
        windowFixture.robot.waitForIdle();

        getButton("OK").requireDisabled();
    }

    @Test
    public void unmatchedFilterTextEmptiesListAndDisablesOKButton() throws Exception {
        showDialog();

        // Some filter text that will not match any CRS
        windowFixture.textBox().enterText("FooBar");
        windowFixture.robot.waitForIdle();

        windowFixture.list().requireItemCount(0);
        getButton("OK").requireDisabled();
    }

    @Test
    public void filterByCode() throws Exception {
        showDialog();
        JListFixture list = windowFixture.list();

        final String code = getRandomCode().toLowerCase();
        windowFixture.textBox().enterText(code);
        windowFixture.robot.waitForIdle();

        // Note: there may be more than one list item if the code
        // we just typed is also the start of longer codes or if the
        // code string happens to appear in descriptive text
        String[] items = list.contents();
        for (String item : items) {
            assertTrue(item.toLowerCase().contains(code));
        }
    }

    @Test
    public void filterByNameFragment() throws Exception {
        showDialog();
        JListFixture list = windowFixture.list();

        final int FILTER_STRING_LEN = 5;
        String filterStr = null;

        while (filterStr == null) {
            String code = getRandomCode();
            String desc =
                    FACTORY.getDescriptionText(JCRSChooser.DEFAULT_AUTHORITY + ":" + code)
                            .toString();

            // double check the text is suitable for filtering
            if (desc != null && desc.length() >= FILTER_STRING_LEN) {
                filterStr = desc.substring(0, FILTER_STRING_LEN).toLowerCase();
            }
        }

        // Filter on the first few characters
        windowFixture.textBox().enterText(filterStr);
        windowFixture.robot.waitForIdle();

        // Check list contents
        for (String item : list.contents()) {
            assertTrue(item.toLowerCase().contains(filterStr));
        }
    }

    @Test
    public void selectInListAndClickOKButton() throws Exception {
        Future<CoordinateReferenceSystem> future = showDialog();

        final int index = rand.nextInt(CODES.size());
        windowFixture.list().clickItem(index);
        windowFixture.robot.waitForIdle();

        getButton("OK").click();
        windowFixture.robot.waitForIdle();

        CoordinateReferenceSystem crs = retrieveCRS(future);
        assertNotNull(crs);

        // Compare return value to list selection
        CoordinateReferenceSystem expected =
                FACTORY.createCoordinateReferenceSystem(
                        JCRSChooser.DEFAULT_AUTHORITY + ":" + CODES.get(index));

        assertTrue(CRS.equalsIgnoreMetadata(expected, crs));
    }

    /**
     * Launches the dialog in a new thread.
     *
     * @return the Future for the dialog task
     */
    private Future<CoordinateReferenceSystem> showDialog() throws Exception {
        return showDialog(null);
    }

    /**
     * Launches the dialog in a new thread.
     *
     * @param title custom title (may be {@code null}
     * @return the Future for the dialog task
     */
    private Future<CoordinateReferenceSystem> showDialog(final String title) throws Exception {
        Future<CoordinateReferenceSystem> future =
                executor.submit(
                        new Callable<CoordinateReferenceSystem>() {
                            @Override
                            public CoordinateReferenceSystem call() throws Exception {
                                if (title == null) {
                                    return JCRSChooser.showDialog();
                                } else {
                                    return JCRSChooser.showDialog(title);
                                }
                            }
                        });

        assertComponentDisplayed(JCRSChooser.CRSDialog.class);
        windowFixture = listener.getFixture(DISPLAY_TIMEOUT);
        return future;
    }

    /**
     * Randomly chooses a code from the CODES list.
     *
     * @return the selected code
     */
    private String getRandomCode() {
        int index = rand.nextInt(CODES.size());
        return CODES.get(index);
    }

    /**
     * Retrieves a CRS from the given task future. If the value cannot be retrieved within the set
     * time-out, an assertion error is raised.
     *
     * @param future task future
     * @return the CRS
     */
    private CoordinateReferenceSystem retrieveCRS(Future<CoordinateReferenceSystem> future)
            throws Exception {

        CoordinateReferenceSystem crs = null;
        try {
            crs = future.get(RETURN_TIMEOUT, TimeUnit.MILLISECONDS);

        } catch (TimeoutException ex) {
            fail("Value not returned within max waiting time");
        }

        return crs;
    }
}
