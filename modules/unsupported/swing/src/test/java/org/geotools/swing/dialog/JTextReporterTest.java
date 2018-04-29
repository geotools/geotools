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
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;
import javax.swing.JTextArea;
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.JTextComponentFixture;
import org.geotools.swing.dialog.JTextReporter.Connection;
import org.geotools.swing.testutils.GraphicsTestBase;
import org.geotools.swing.testutils.GraphicsTestRunner;
import org.geotools.swing.testutils.WindowActivatedListener;
import org.geotools.util.logging.Logging;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@linkplain JTextReporter}.
 *
 * <p>This test class uses an {@linkplain ExecutorService} to launch the dialog which avoids a
 * deadlock between the dialog waiting for its Connection object and this class waiting for the
 * dialog to show up on the event thread.
 *
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
@RunWith(GraphicsTestRunner.class)
public class JTextReporterTest extends GraphicsTestBase<Dialog> {

    private static final Class<? extends Dialog> DIALOG_CLASS = JTextReporter.TextDialog.class;

    private static final long LISTENER_TIMEOUT = 1000;

    private static final String TITLE = "Test text reporter";

    private static final String[] TEXT = {
        "A thing is called finite after its kind,",
        "when it can be limited by another thing of the same nature;",
        "for instance, a body is called finite because we always",
        "conceive another greater body.",
        "So, also, a thought is limited by another thought,",
        "but a body is not limited by thought, nor a thought by body"
    };

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    private WindowActivatedListener listener;
    private StreamHandler handler;
    private ByteArrayOutputStream out;

    @Before
    public void setup() {
        listener = new WindowActivatedListener(DIALOG_CLASS);
        Toolkit.getDefaultToolkit().addAWTEventListener(listener, AWTEvent.WINDOW_EVENT_MASK);
    }

    @After
    public void cleanup() {
        Toolkit.getDefaultToolkit().removeAWTEventListener(listener);
    }

    @Test
    public void showDefaultDialog() throws Exception {
        showDialog(TITLE);
        assertEquals(TITLE, windowFixture.component().getTitle());

        // check text area is present and correct size
        JTextComponentFixture textBox = windowFixture.textBox();
        assertTrue(textBox.component() instanceof JTextArea);
        JTextArea textArea = (JTextArea) textBox.component();
        assertEquals(JTextReporter.DEFAULT_TEXTAREA_ROWS, textArea.getRows());
        assertEquals(JTextReporter.DEFAULT_TEXTAREA_COLS, textArea.getColumns());

        // check all buttons are present
        getButton("Copy to clipboard");
        getButton("Save...");
        getButton("Clear");
        getButton("Close");

        // check dialog state
        DialogFixture df = (DialogFixture) windowFixture;

        boolean expectModal = (JTextReporter.DEFAULT_FLAGS & JTextReporter.FLAG_MODAL) > 0;
        assertEquals(expectModal, df.component().isModal());

        boolean expectResizable = (JTextReporter.DEFAULT_FLAGS & JTextReporter.FLAG_RESIZABLE) > 0;
        assertEquals(expectResizable, df.component().isResizable());

        boolean expectAlwaysOnTop =
                (JTextReporter.DEFAULT_FLAGS & JTextReporter.FLAG_ALWAYS_ON_TOP) > 0;
        assertEquals(expectAlwaysOnTop, df.component().isAlwaysOnTop());
    }

    @Test
    public void nullTitleIsOK() throws Exception {
        showDialog(null);
    }

    @Test
    public void nullInitialTextIsOK() throws Exception {
        showDialog(TITLE, null);
    }

    @Test
    public void initialText() throws Exception {
        showDialog(TITLE, TEXT[0]);
        windowFixture.textBox().requireText(TEXT[0]);
    }

    @Test
    public void connectionObjectIsReturned() throws Exception {
        Connection conn = null;
        try {
            conn = showDialog(TITLE).get(DISPLAY_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (TimeoutException ex) {
            fail("connection object not returned");
        }

        assertNotNull(conn);
    }

    @Test
    public void appendText() throws Exception {
        Connection conn = showDialog(TITLE).get();

        conn.append(TEXT[0]);
        windowFixture.robot.waitForIdle();

        windowFixture.textBox().requireText(TEXT[0]);
    }

    @Test
    public void appendTextWithFormattedNewlines() throws Exception {
        final String text = getConcatenatedText(String.format("%n"));

        Connection conn = showDialog(TITLE).get();
        conn.append(text);

        windowFixture.robot.waitForIdle();
        String displayedText = windowFixture.textBox().text();

        assertEquals(text, displayedText);
    }

    @Test
    public void appendTextWithEmbeddedNewlines() throws Exception {
        final String text = getConcatenatedText("\n");

        Connection conn = showDialog(TITLE).get();
        conn.append(text);

        windowFixture.robot.waitForIdle();
        String displayedText = windowFixture.textBox().text();

        String regex = "(\\n|\\r)+";
        assertEquals(text.replaceAll(regex, "|"), displayedText.replaceAll(regex, "|"));
    }

    @Test
    public void appendNewline() throws Exception {
        Connection conn = showDialog(TITLE, TEXT[0]).get();

        conn.appendNewline();
        conn.appendNewline();
        conn.append(TEXT[1]);
        conn.appendNewline();

        windowFixture.robot.waitForIdle();
        String displayedText = windowFixture.textBox().text();
        String expectedText = String.format("%s%n%n%s%n", TEXT[0], TEXT[1]);
        assertEquals(expectedText, displayedText);
    }

    @Test
    public void appendDefaultSeparatorLine() throws Exception {
        Connection conn = showDialog(TITLE, TEXT[0] + "\n").get();

        final int N = 10;
        conn.appendSeparatorLine(N);

        char[] chars = new char[N];
        Arrays.fill(chars, JTextReporter.DEFAULT_SEPARATOR_CHAR);
        String expected = String.format("%s%n%s%n", TEXT[0], String.valueOf(chars));

        String displayedText = windowFixture.textBox().text();
        assertEquals(expected, displayedText);
    }

    @Test
    public void appendCustomSeparatorLine() throws Exception {
        Connection conn = showDialog(TITLE, TEXT[0] + "\n").get();

        final int N = 10;
        final char c = '#';
        conn.appendSeparatorLine(N, c);

        char[] chars = new char[N];
        Arrays.fill(chars, c);
        String expected = String.format("%s%n%s%n", TEXT[0], String.valueOf(chars));

        String displayedText = windowFixture.textBox().text();
        assertEquals(expected, displayedText);
    }

    @Test
    public void appendMethodsCanBeChained() throws Exception {
        Connection conn = showDialog(TITLE).get();

        conn.append(TEXT[0]).appendNewline().append(TEXT[1]);
        windowFixture.robot.waitForIdle();

        String actual = windowFixture.textBox().text();
        String expected = String.format("%s%n%s", TEXT[0], TEXT[1]);
        assertEquals(expected, actual);
    }

    @Test
    public void appendAfterDialogDismissedCausesLoggerMessage() throws Exception {
        Connection conn = showDialog(TITLE).get();
        windowFixture.close();
        windowFixture.robot.waitForIdle();

        captureLogger();
        conn.append("This should not work");

        assertLogMessage(Level.SEVERE.toString());
        assertLogMessage("appending text to an expired JTextReporter connection");

        releaseLogger();
    }

    @Test
    public void getTextFromConnection() throws Exception {
        final String text = getConcatenatedText(String.format("%n"));
        Connection conn = showDialog(TITLE, text).get();
        assertEquals(text, conn.getText());
    }

    @Test
    public void getTextWhenNoneIsDisplayedReturnsEmptyString() throws Exception {
        Connection conn = showDialog(TITLE).get();
        String text = conn.getText();
        assertNotNull(text);
        assertTrue(text.length() == 0);
    }

    @Test
    public void listenerInformedWhenTextIsUpdated() throws Exception {
        Connection conn = showDialog(TITLE).get();

        final CountDownLatch latch = new CountDownLatch(1);
        conn.addListener(
                new TextReporterListener() {
                    @Override
                    public void onReporterClosed() {}

                    @Override
                    public void onReporterUpdated() {
                        latch.countDown();
                    }
                });

        conn.append(TEXT[0]);
        assertTrue(latch.await(LISTENER_TIMEOUT, TimeUnit.MILLISECONDS));
    }

    @Test
    public void listenerInformedWhenDialogIsClosedViaSystemButton() throws Exception {
        Connection conn = showDialog(TITLE).get();

        final CountDownLatch latch = new CountDownLatch(1);
        conn.addListener(
                new TextReporterListener() {
                    @Override
                    public void onReporterClosed() {
                        latch.countDown();
                    }

                    @Override
                    public void onReporterUpdated() {}
                });

        windowFixture.close();
        assertTrue(latch.await(LISTENER_TIMEOUT, TimeUnit.MILLISECONDS));
    }

    @Test
    public void listenerInformedWhenDialogIsClosedViaCloseButton() throws Exception {
        Connection conn = showDialog(TITLE).get();

        final CountDownLatch latch = new CountDownLatch(1);
        conn.addListener(
                new TextReporterListener() {
                    @Override
                    public void onReporterClosed() {
                        latch.countDown();
                    }

                    @Override
                    public void onReporterUpdated() {}
                });

        getButton("Close").click();
        assertTrue(latch.await(LISTENER_TIMEOUT, TimeUnit.MILLISECONDS));
    }

    @Ignore("have to work out how to do this one")
    @Test
    public void saveTextToFile() throws Exception {
        showDialog(TITLE, TEXT[0]).get();

        getButton("Save...").click();
    }

    @Test
    public void clearText() throws Exception {
        showDialog(TITLE, TEXT[0]).get();

        getButton("Clear").click();
        windowFixture.robot.waitForIdle();

        assertEquals(0, windowFixture.textBox().component().getDocument().getLength());
    }

    @Test
    public void copyTextToClipboard() throws Exception {
        final String text = getConcatenatedText(String.format("%n"));
        showDialog(TITLE, text).get();

        getButton("Copy to clipboard").click();
        windowFixture.robot.waitForIdle();

        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        String clipText = (String) clip.getData(DataFlavor.stringFlavor);
        assertEquals(text, clipText);
    }

    @Test
    public void closeDialogViaConnection() throws Exception {
        Connection conn = showDialog(TITLE).get();
        conn.closeDialog();

        windowFixture.robot.waitForIdle();
        windowFixture.requireNotVisible();
        assertFalse(conn.isOpen());
    }

    @Test
    public void callingCloseDialogTwiceRaisesLogMessage() throws Exception {
        Connection conn = showDialog(TITLE).get();
        conn.closeDialog();
        windowFixture.robot.waitForIdle();
        captureLogger();

        conn.closeDialog();
        assertLogMessage("INFO");
        assertLogMessage("connection has expired");

        releaseLogger();
    }

    @Test
    public void callingCloseDialogAfterGUICloseRaisesLogMessage() throws Exception {
        Connection conn = showDialog(TITLE).get();
        getButton("Close").click();
        windowFixture.robot.waitForIdle();
        captureLogger();

        conn.closeDialog();
        assertLogMessage("INFO");
        assertLogMessage("connection has expired");

        releaseLogger();
    }

    /**
     * Launches the dialog in a new thread.
     *
     * @param dialog title
     * @return the Future for the dialog task
     */
    private Future<JTextReporter.Connection> showDialog(String title) throws Exception {
        return showDialog(title, null);
    }

    /**
     * Launches the dialog in a new thread.
     *
     * @param dialog title
     * @param initial text to be displayed
     * @return the Future for the dialog task
     */
    private Future<JTextReporter.Connection> showDialog(
            final String title, final String initialText) throws Exception {

        Future<Connection> future =
                executor.submit(
                        new Callable<JTextReporter.Connection>() {

                            @Override
                            public Connection call() throws Exception {
                                return JTextReporter.showDialog(title, initialText);
                            }
                        });

        assertComponentDisplayed(DIALOG_CLASS);
        windowFixture = listener.getFixture(DISPLAY_TIMEOUT);
        return future;
    }

    private void captureLogger() {
        Logger logger = Logging.getLogger("org.geotools.swing");
        Formatter formatter = new SimpleFormatter();
        out = new ByteArrayOutputStream();
        handler = new StreamHandler(out, formatter);
        logger.addHandler(handler);
        logger.setUseParentHandlers(false);
    }

    private void releaseLogger() {
        Logger logger = Logging.getLogger("org.geotools.swing");
        logger.removeHandler(handler);
        logger.setUseParentHandlers(true);
    }

    private void assertLogMessage(String expectedMsg) {
        handler.flush();
        String logMsg = out.toString();

        assertNotNull(logMsg);
        assertTrue(logMsg.toLowerCase().contains(expectedMsg.toLowerCase()));
    }

    private String getConcatenatedText(String concatStr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < TEXT.length; i++) {
            sb.append(TEXT[i]);
            if (i < TEXT.length - 1) {
                sb.append(concatStr);
            }
        }
        return sb.toString();
    }
}
