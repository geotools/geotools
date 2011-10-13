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

import java.awt.Component;
import java.awt.Window;

import javax.swing.JButton;

import org.fest.swing.core.BasicComponentFinder;
import org.fest.swing.core.ComponentFoundCondition;
import org.fest.swing.core.GenericTypeMatcher;
import org.fest.swing.core.TypeMatcher;
import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.fixture.JButtonFixture;
import org.fest.swing.fixture.WindowFixture;
import org.fest.swing.timing.Pause;

import org.junit.After;
import org.junit.BeforeClass;
import static org.junit.Assert.assertNotNull;

/**
 * Base for test classes which use a FEST {@linkplain WindowFixture} to hold dialogs,
 * frames etc. 
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public abstract class GraphicsTestBase<T extends Window> {
    
    // Max waiting time for dialog display (milliseconds)
    public static final long DISPLAY_TIMEOUT = 1000;
    
    protected WindowFixture<T> windowFixture;

    /**
     * Installs the FEST repaint manager.
     */
    @BeforeClass 
    public static void baseSetUpOnce() {
        FailOnThreadViolationRepaintManager.install();
    }

    /**
     * Run after each test to call {@linkplain WindowFixture#cleanUp()}.
     */
    @After
    public void baseCleanup() {
        if (windowFixture != null) {
            windowFixture.cleanUp();
        }
    }
    
    /**
     * Waits up to {@linkplain #DISPLAY_TIMEOUT} milliseconds for a given
     * dialog class to be displayed. If the waiting time is exceeded an
     * assertion error is thrown.
     * 
     * @param dialogClass dialog class
     */
    protected void assertComponentDisplayed(Class<? extends Component> componentClass) {
        Pause.pause(new ComponentFoundCondition("component to be displayed", 
                BasicComponentFinder.finderWithCurrentAwtHierarchy(), 
                new TypeMatcher(componentClass, true)),
                DISPLAY_TIMEOUT);
    }

    /**
     * Gets a dialog button with the specified text.
     * 
     * @param buttonText button text
     * 
     * @return the button fixture
     */
    protected JButtonFixture getButton(final String buttonText) {
        JButtonFixture button = windowFixture.button(new GenericTypeMatcher<JButton>(JButton.class) {
            @Override
            protected boolean isMatching(JButton component) {
                return buttonText.equals(component.getText());
            }
        });
        
        assertNotNull(button);
        return button;
    }

}
