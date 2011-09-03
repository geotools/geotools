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

import java.awt.Component;
import java.awt.event.KeyEvent;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for KeyInfo class.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class KeyInfoTest {
    
    @Test
    public void createInstanceAndRetrieveValues() {
        KeyInfo keyInfo = new KeyInfo(KeyEvent.VK_UP, KeyEvent.SHIFT_DOWN_MASK, "Shift+Up");
        
        assertEquals(KeyEvent.VK_UP, keyInfo.getKeyCode());
        assertEquals(KeyEvent.SHIFT_DOWN_MASK, keyInfo.getModifiers());
        assertEquals("Shift+Up", keyInfo.toString());
    }
    
    @Test
    public void createInstanceWithNoDescription() {
        String regex = "\\s*KeyInfo\\(\\s*38,\\s*64\\)\\s*";
        
        KeyInfo keyInfo = new KeyInfo(38, 64, null);
        assertTrue(keyInfo.toString().matches(regex));
        
        keyInfo = new KeyInfo(38, 64, " ");
        assertTrue(keyInfo.toString().matches(regex));
    }
    
    @Test
    public void copyConstructor() {
        KeyInfo keyInfo1 = new KeyInfo(KeyEvent.VK_UP, KeyEvent.SHIFT_DOWN_MASK, "Shift+Up");
        KeyInfo keyInfo2 = new KeyInfo(keyInfo1);
        
        assertEquals(keyInfo1.getKeyCode(), keyInfo2.getKeyCode());
        assertEquals(keyInfo1.getModifiers(), keyInfo2.getModifiers());
        assertEquals(keyInfo1.toString(), keyInfo2.toString());
    }
    
    @Test
    public void equalsIgnoresDescription() {
        KeyInfo keyInfo1 = new KeyInfo(KeyEvent.VK_UP, KeyEvent.SHIFT_DOWN_MASK, "Shift+Up");
        KeyInfo keyInfo2 = new KeyInfo(KeyEvent.VK_UP, KeyEvent.SHIFT_DOWN_MASK, "");
        assertEquals(keyInfo1, keyInfo2);
    }
    
    @Test
    public void matchKeyEvent() {
        KeyInfo keyInfo = new KeyInfo(KeyEvent.VK_UP, KeyEvent.SHIFT_DOWN_MASK, "Shift+Up");
        
        KeyEvent sameCodeDiffMods = createKeyEvent(KeyEvent.VK_UP, KeyEvent.CTRL_DOWN_MASK);
        KeyEvent sameCodeZeroMods = createKeyEvent(KeyEvent.VK_UP, 0);
        KeyEvent diffCodeSameMods = createKeyEvent(KeyEvent.VK_DOWN, KeyEvent.SHIFT_DOWN_MASK);
        KeyEvent sameCodeSameMods = createKeyEvent(KeyEvent.VK_UP, KeyEvent.SHIFT_DOWN_MASK);
        
        assertFalse(keyInfo.matchesEvent(sameCodeDiffMods));
        assertFalse(keyInfo.matchesEvent(sameCodeZeroMods));
        assertFalse(keyInfo.matchesEvent(diffCodeSameMods));
        assertTrue(keyInfo.matchesEvent(sameCodeSameMods));
    }

    private KeyEvent createKeyEvent(int keyCode, int modifiers) {
        return new KeyEvent(new Component(){}, 0, 0, modifiers, keyCode, '0');
    }
    
}
