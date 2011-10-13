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

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;

import org.geotools.swing.testutils.GraphicsTestBase;
import org.geotools.swing.testutils.GraphicsTestRunner;

import org.fest.swing.core.GenericTypeMatcher;
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.JComboBoxFixture;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/**
 * Tests for {@linkplain JFileImageChooser}.
 * <p>
 * 
 * This test class uses an {@linkplain ExecutorService} to launch the dialog which 
 * avoids a deadlock between the modal dialog and this class waiting for the dialog
 * to show up on the event thread.
 * <p>
 * 
 * TODO: it would be good to test display of the "Confirm file name" dialog but 
 * I haven't worked out how to detect its appearance on the EDT reliably without
 * blocking the test.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
@RunWith(GraphicsTestRunner.class)
public class JFileImageChooserTest extends GraphicsTestBase<Dialog> {
    
    private static final Class<? extends Component> DIALOG_CLASS = JFileImageChooser.class;
    
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();;
    private static List<String> readerFileSuffixes;
    private static List<String> writerFileSuffixes;
    
    @BeforeClass
    public static void setupOnce() {
        readerFileSuffixes = Arrays.asList( ImageIO.getReaderFileSuffixes() );
        writerFileSuffixes = Arrays.asList( ImageIO.getWriterFileSuffixes() );
    }
    
    @Before
    public void setup() {
        Toolkit.getDefaultToolkit().addAWTEventListener(chooserListener, AWTEvent.WINDOW_EVENT_MASK);
    }
    
    @After
    public void cleanup() {
        Toolkit.getDefaultToolkit().removeAWTEventListener(chooserListener);
    }
    
    @Test
    public void showOpenFileDialogHasOpenButton() throws Exception {
        showOpenDialog(null, null);
        
        // Can't use base class getButton method because JFileChooser has
        // duplicate components
        windowFixture.button(new GenericTypeMatcher<JButton>(JButton.class) {
            @Override
            protected boolean isMatching(JButton component) {
                return component.isVisible() && "Open".equals(component.getText());
            }
        });
    }
    
    @Test
    public void showSaveFileDialogHasSaveButton() throws Exception {
        showSaveDialog(null, null);
        
        // Can't use base class getButton method because JFileChooser has
        // duplicate components
        windowFixture.button(new GenericTypeMatcher<JButton>(JButton.class) {
            @Override
            protected boolean isMatching(JButton component) {
                return component.isVisible() && "Save".equals(component.getText());
            }
        });
    }
    
    @Test
    public void showOpenFileDisplaysSupportedFormats() throws Exception {
        showOpenDialog(null, null);

        JComboBoxFixture comboBox = getFileFormatComboBox();
        final int N = comboBox.contents().length;
        for (int i = 0; i < N; i++) {
            JFileImageChooser.FormatFilter filter = 
                    (JFileImageChooser.FormatFilter) comboBox.component().getItemAt(i);
            
            String suffix = filter.getDefaultSuffix();
            if (suffix.startsWith(".")) {
                suffix = suffix.substring(1);
            }
            assertTrue("file suffix not a supported format: " + suffix,
                    readerFileSuffixes.contains(suffix));
        }
    }

    @Test
    public void showSaveFileDisplaysSupportedFormats() throws Exception {
        showSaveDialog(null, null);

        JComboBoxFixture comboBox = getFileFormatComboBox();
        final int N = comboBox.contents().length;
        for (int i = 0; i < N; i++) {
            JFileImageChooser.FormatFilter filter = 
                    (JFileImageChooser.FormatFilter) comboBox.component().getItemAt(i);
            
            String suffix = filter.getDefaultSuffix();
            if (suffix.startsWith(".")) {
                suffix = suffix.substring(1);
            }
            assertTrue("file suffix not a supported format: " + suffix,
                    writerFileSuffixes.contains(suffix));
        }
    }
    
    /**
     * Calls {@linkplain JFileImageChooser#showOpenFile(java.awt.Component, java.io.File)}
     * in a separate thread and confirms that the dialog is displayed.
     * 
     * @param parent optional parent component
     * @param workingDir optional initial working dir
     * 
     * @return a future for the task being run in a separate thread
     */
    private Future<File> showOpenDialog(final Component parent, final File workingDir) {
        
        Future<File> future = executor.submit(
                new Callable<File>() {
                    @Override
                    public File call() throws Exception {
                        return JFileImageChooser.showOpenFile(parent, workingDir);
                    }
                });
        
        assertComponentDisplayed(DIALOG_CLASS);
        return future;
    }

    /**
     * Calls {@linkplain JFileImageChooser#showSaveFile(java.awt.Component, java.io.File)}
     * in a separate thread and confirms that the dialog is displayed.
     * 
     * @param parent optional parent component
     * @param workingDir optional initial working dir
     * 
     * @return a future for the task being run in a separate thread
     */
    private Future<File> showSaveDialog(final Component parent, final File workingDir) {
        
        Future<File> future = executor.submit(
                new Callable<File>() {
                    @Override
                    public File call() throws Exception {
                        return JFileImageChooser.showSaveFile(parent, workingDir);
                    }
                });
        
        assertComponentDisplayed(DIALOG_CLASS);
        return future;
    }

    /**
     * Gets the file format combo box.
     * 
     * @return FEST fixture for the component
     */
    private JComboBoxFixture getFileFormatComboBox() {
        return windowFixture.comboBox(new GenericTypeMatcher<JComboBox>(JComboBox.class) {
            @Override
            protected boolean isMatching(JComboBox component) {
                return component.getItemAt(0) instanceof JFileImageChooser.FormatFilter;
            }
        });
    }

    /*
     * This AWTEventListener is used to grab the dialog as a FEST windowFixture.
     * Note that we have to listen for a JDialog, not a JFileImageChooser or JFileChooser,
     * because this is what the underlying Swing file chooser creates and displays.
     */
    AWTEventListener chooserListener = new AWTEventListener() {
        @Override
        public void eventDispatched(AWTEvent event) {
            if (windowFixture == null &&
                    event.getSource() instanceof JDialog &&
                    event.getID() == WindowEvent.WINDOW_ACTIVATED) {

                windowFixture = new DialogFixture((JDialog) event.getSource());
            }
        }
    };
    
}
