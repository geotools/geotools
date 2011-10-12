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
import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.WindowEvent;
import java.util.regex.Pattern;

import javax.swing.JDialog;

import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.JLabelFixture;

import org.geotools.swing.testutils.GraphicsTestBase;
import org.geotools.swing.testutils.GraphicsTestRunner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;


/**
 * Tests for the ExceptionReporter class.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
@RunWith(GraphicsTestRunner.class)
public class ExceptionReporterTest extends GraphicsTestBase<Dialog> {

    @Before
    public void setup() {
        Toolkit.getDefaultToolkit().addAWTEventListener(listener, AWTEvent.WINDOW_EVENT_MASK);
    }
    
    @After
    public void cleanup() {
        Toolkit.getDefaultToolkit().removeAWTEventListener(listener);
    }

    @Test
    public void showException() throws Exception {
        final String MSG = "Foo is not Bar";
        
        ExceptionReporter.showDialog(new IllegalArgumentException(MSG));
        assertComponentDisplayed(ExceptionReporter.ReportingDialog.class);

        ((DialogFixture) windowFixture).requireModal();
        assertEquals("IllegalArgumentException", windowFixture.component().getTitle());
        
        JLabelFixture label = windowFixture.label();
        label.requireText(Pattern.compile(".*" + MSG + ".*"));
    }
    
    @Test
    public void showExceptionWithUserMessage() throws Exception {
        final String EXCEPTION_MSG = "Foo is not Bar";
        final String USER_MSG = "You should see this message";
        
        ExceptionReporter.showDialog(new IllegalArgumentException(EXCEPTION_MSG), USER_MSG);
        assertComponentDisplayed(ExceptionReporter.ReportingDialog.class);
        
        assertEquals("IllegalArgumentException", windowFixture.component().getTitle());
        
        JLabelFixture label = windowFixture.label();
        label.requireText(Pattern.compile(".*" + USER_MSG + ".*"));
    }
    
    @Test
    public void emptyUserMessage() throws Exception {
        final String EXCEPTION_MSG = "You should see this message";
        final String USER_MSG = "";
        
        ExceptionReporter.showDialog(new IllegalArgumentException(EXCEPTION_MSG), USER_MSG);
        assertComponentDisplayed(ExceptionReporter.ReportingDialog.class);
        
        assertEquals("IllegalArgumentException", windowFixture.component().getTitle());
        
        JLabelFixture label = windowFixture.label();
        label.requireText(Pattern.compile(".*" + EXCEPTION_MSG + ".*"));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void nullExceptionArg() throws Exception {
        ExceptionReporter.showDialog(null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void nullExceptionArg2() throws Exception {
        ExceptionReporter.showDialog(null, "User message");
    }
    
    /*
     * This AWTEventListener is used to grab the dialog as a FEST windowFixture.
     */
    AWTEventListener listener = new AWTEventListener() {
        @Override
        public void eventDispatched(AWTEvent event) {
            if (event.getSource() instanceof ExceptionReporter.ReportingDialog
                    && event.getID() == WindowEvent.WINDOW_ACTIVATED) {

                windowFixture = new DialogFixture((JDialog) event.getSource());
                ((DialogFixture) windowFixture).requireModal();
            }
        }
    };
}
