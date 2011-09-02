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
 * Unit tests for KeyId class.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class KeyIdTest {
    
    @Test
    public void createInstanceAndRetrieveValues() {
        KeyId keyId = new KeyId(KeyEvent.VK_UP, KeyEvent.SHIFT_DOWN_MASK, "Shift+Up");
        
        assertEquals(KeyEvent.VK_UP, keyId.getKeyCode());
        assertEquals(KeyEvent.SHIFT_DOWN_MASK, keyId.getModifiers());
        assertEquals("Shift+Up", keyId.toString());
    }
    
    @Test
    public void createInstanceWithNoDescription() {
        String regex = "\\s*KeyId\\(\\s*38,\\s*64\\)\\s*";
        
        KeyId keyId = new KeyId(38, 64, null);
        assertTrue(keyId.toString().matches(regex));
        
        keyId = new KeyId(38, 64, " ");
        assertTrue(keyId.toString().matches(regex));
    }
    
    @Test
    public void copyConstructor() {
        KeyId keyId1 = new KeyId(KeyEvent.VK_UP, KeyEvent.SHIFT_DOWN_MASK, "Shift+Up");
        KeyId keyId2 = new KeyId(keyId1);
        
        assertEquals(keyId1.getKeyCode(), keyId2.getKeyCode());
        assertEquals(keyId1.getModifiers(), keyId2.getModifiers());
        assertEquals(keyId1.toString(), keyId2.toString());
    }
    
    @Test
    public void equalsIgnoresDescription() {
        KeyId keyId1 = new KeyId(KeyEvent.VK_UP, KeyEvent.SHIFT_DOWN_MASK, "Shift+Up");
        KeyId keyId2 = new KeyId(KeyEvent.VK_UP, KeyEvent.SHIFT_DOWN_MASK, "");
        assertEquals(keyId1, keyId2);
    }
    
    @Test
    public void matchKeyEvent() {
        KeyId keyId = new KeyId(KeyEvent.VK_UP, KeyEvent.SHIFT_DOWN_MASK, "Shift+Up");
        
        KeyEvent sameCodeDiffMods = createKeyEvent(KeyEvent.VK_UP, KeyEvent.CTRL_DOWN_MASK);
        KeyEvent sameCodeZeroMods = createKeyEvent(KeyEvent.VK_UP, 0);
        KeyEvent diffCodeSameMods = createKeyEvent(KeyEvent.VK_DOWN, KeyEvent.SHIFT_DOWN_MASK);
        KeyEvent sameCodeSameMods = createKeyEvent(KeyEvent.VK_UP, KeyEvent.SHIFT_DOWN_MASK);
        
        assertFalse(keyId.matchesEvent(sameCodeDiffMods));
        assertFalse(keyId.matchesEvent(sameCodeZeroMods));
        assertFalse(keyId.matchesEvent(diffCodeSameMods));
        assertTrue(keyId.matchesEvent(sameCodeSameMods));
    }

    private KeyEvent createKeyEvent(int keyCode, int modifiers) {
        return new KeyEvent(new Component(){}, 0, 0, modifiers, keyCode, '0');
    }
    
}
