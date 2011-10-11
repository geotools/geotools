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

import java.awt.Dialog;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.util.List;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JTextArea;

import org.geotools.factory.GeoTools;
import org.geotools.swing.testutils.GraphicsTestBase;
import org.geotools.swing.testutils.GraphicsTestRunner;

import org.fest.swing.core.GenericTypeMatcher;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.JButtonFixture;
import org.fest.swing.fixture.JTextComponentFixture;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/**
 * Tests for the {@linkplain AboutDialog} class.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
@RunWith(GraphicsTestRunner.class)
public class AboutDialogTest extends GraphicsTestBase<Dialog> {
    
    private static final String DIALOG_TITLE = "About dialog test";
    private static final String APP_INFO = "GeoFoo: mapping Foos in real time";
    
    private boolean showAppInfo;
    
    @Test
    public void dialogWithoutApplicationInfo() throws Exception {
        createAndShow(false);
        
        assertEquals(DIALOG_TITLE, windowFixture.component().getTitle());
        assertCategories();
        
        // should be showing 'Environment' category
        String[] selection = windowFixture.list().selection();
        assertEquals(1, selection.length);
        assertEquals(AboutDialog.Category.ENVIRONMENT.toString(), selection[0]);
    }

    @Test
    public void dialogWithApplicationInfo() throws Exception {
        createAndShow(true);
        
        assertEquals(DIALOG_TITLE, windowFixture.component().getTitle());
        assertCategories();
        
        // should be showing 'Application' category
        String[] selection = windowFixture.list().selection();
        assertEquals(1, selection.length);
        assertEquals(AboutDialog.Category.APPLICATION.toString(), selection[0]);
    }
    
    @Test
    public void ensureTextAreaIsDisplayed() {
        createAndShow(false);
        JTextComponentFixture textArea = getDialogTextArea();
        assertNotNull(textArea);
        textArea.requireVisible();
    }
    
    @Test
    public void applicationInfo() {
        createAndShow(true);
        assertTextDisplayExact(AboutDialog.Category.APPLICATION, APP_INFO);
    }
    
    @Test
    public void environmentInfo() {
        createAndShow(false);
        assertTextDisplayExact(AboutDialog.Category.ENVIRONMENT, GeoTools.getEnvironmentInfo());
    }
    
    @Ignore("Licence info not implemented by dialog yet")
    @Test
    public void licenceInfo() {
        createAndShow(false);
        
    }
    
    @Test
    public void jarInfo() {
        createAndShow(false);
        assertTextDisplayExact(AboutDialog.Category.JARS, GeoTools.getGeoToolsJarInfo());
    }
    
    @Test
    public void allInfo() {
        createAndShow(true);
        assertTextDisplayContains(AboutDialog.Category.ALL, new String[] {
            APP_INFO,
            GeoTools.getEnvironmentInfo(),
            GeoTools.getGeoToolsJarInfo()
        });
    }
                
    @Test
    public void copyToClipboard() throws Exception {
        createAndShow(true);
        
        
        JButtonFixture button = windowFixture.button(new GenericTypeMatcher<JButton>(JButton.class) {
            @Override
            protected boolean isMatching(JButton component) {
                return "Copy to clipboard".equals(component.getText());
            }
        });
        
        assertNotNull(button);
        
        windowFixture.list().clickItem(AboutDialog.Category.ALL.toString());
        windowFixture.robot.waitForIdle();
        button.click();
        windowFixture.robot.waitForIdle();
        
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        String clipText = (String) clip.getData(DataFlavor.stringFlavor);
        
        String expected = getDialogTextArea().text();
        assertEquals(expected, clipText);
    }

    private void createAndShow(final boolean showAppInfo) {
        this.showAppInfo = showAppInfo;
        AboutDialog dialog = GuiActionRunner.execute(new GuiQuery<AboutDialog>() {
            @Override
            protected AboutDialog executeInEDT() throws Throwable {
                AboutDialog dialog;
                if (showAppInfo) {
                    dialog = new AboutDialog(DIALOG_TITLE, APP_INFO);
                } else {
                    dialog = new AboutDialog(DIALOG_TITLE);
                }
                return dialog;
            }
        });

        windowFixture = new DialogFixture(dialog);
        ((DialogFixture) windowFixture).show();
    }
    
    private JTextComponentFixture getDialogTextArea() {
        return windowFixture.textBox(new GenericTypeMatcher<JTextArea>(JTextArea.class, true) {

            @Override
            protected boolean isMatching(JTextArea component) {
                return true;
            }
        });
    }
    private void assertCategories() {
        List<String> listItems = Arrays.asList( windowFixture.list().contents() );
        
        int expectedN = AboutDialog.Category.values().length - (showAppInfo ? 0 : 1);
        assertEquals(expectedN, listItems.size());
        
        for (AboutDialog.Category cat : AboutDialog.Category.values()) {
            boolean b = listItems.contains(cat.toString()) || 
                    (cat == AboutDialog.Category.APPLICATION && !showAppInfo);
            assertTrue("List is missing " + cat.toString(), b);
        }
    }

    private void assertTextDisplayExact(AboutDialog.Category cat, String expected) {
        windowFixture.list().clickItem(cat.toString());
        windowFixture.robot.waitForIdle();
        
        JTextComponentFixture textArea = getDialogTextArea();
        textArea.requireText(expected);
    }
    
    private void assertTextDisplayContains(AboutDialog.Category cat, String[] expected) {
        windowFixture.list().clickItem(cat.toString());
        windowFixture.robot.waitForIdle();
        
        JTextComponentFixture textArea = getDialogTextArea();
        final String TEXT = textArea.text();
        for (String s : expected) {
            assertTrue("Did not match " + s, TEXT.contains(s));
        }
    }
    
}
