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

import java.awt.event.KeyEvent;

/**
 * Represents a keyboard key or key combination. It is used by {@linkplain MapPaneKeyHandler}
 * to store key bindings associated with map pane actions. 
 * <p>
 * You create instances using values of {@code keyCode} and {@code modifiers} taken from 
 * constants in the {@linkplain KeyEvent} class:
 * <pre><code>
 *    KeyInfo left = new KeyInfo(KeyEvent.VK_LEFT, 0, "Left");
 *    KeyInfo shiftUp = new KeyInfo(KeyEvent.VK_UP, KeyEvent.SHIFT_DOWN_MASK, "Shift+Up");
 * </code></pre>
 * The String argument can later be retrieved with {@linkplain KeyInfo#toString()}
 * and can be useful for GUI elements such as menu items.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class KeyInfo {
    private final int keyCode;
    private final int modifiers;
    private final String desc;

    /**
     * Creates a new instance. If {@code desc} is {@code null} or empty, 
     * the description will be set to "KeyInfo(keyCode, modifiers)".
     *
     * @param keyCode key code
     * @param modifiers modifiers (0 for none)
     * @param desc short description suitable for GUI labels etc.
     */
    public KeyInfo(int keyCode, int modifiers, String desc) {
        this.keyCode = keyCode;
        this.modifiers = modifiers;
        
        if (desc == null || desc.trim().length() == 0) {
            this.desc = String.format("KeyInfo(%d, %d)", keyCode, modifiers);
        } else {
            this.desc = desc;
        }
    }

    /**
     * Creates a copy of an existing instance.
     *
     * @param keyInfo the instance to copy
     * @throws IllegalArgumentException if {@code keyInfo} is {@code null}
     */
    public KeyInfo(KeyInfo keyInfo) {
        if (keyInfo == null) {
            throw new IllegalArgumentException("keyInfo must not be null");
        }
        this.keyCode = keyInfo.keyCode;
        this.modifiers = keyInfo.modifiers;
        this.desc = keyInfo.desc;
    }

    /**
     * Gets the key code.
     *
     * @return the key code
     */
    public int getKeyCode() {
        return keyCode;
    }

    /**
     * Gets the modifiers.
     *
     * @return the modifiers
     */
    public int getModifiers() {
        return modifiers;
    }

    /**
     * Gets the short text description for this object. This can be useful for
     * GUI labels, menu items etc.
     *
     * @return the description
     */
    @Override
    public String toString() {
        return desc;
    }

    /**
     * Tests whether the key code and modifiers of this {@code KeyInfo}
     * match that of a given {@code KeyEvent}. For convenience, this
     * method will return {@code false} if the input event is {@code null}.
     *
     * @param e the input event
     *
     * @return {@code true} if the key code and modifier values match those
     *     of the input event
     */
    public boolean matchesEvent(KeyEvent e) {
        return e != null && e.getKeyCode() == keyCode && (e.getModifiersEx() ^ modifiers) == 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final KeyInfo other = (KeyInfo) obj;
        if (this.keyCode != other.keyCode) {
            return false;
        }
        if (this.modifiers != other.modifiers) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + this.keyCode;
        hash = 59 * hash + this.modifiers;
        return hash;
    }
    
}
