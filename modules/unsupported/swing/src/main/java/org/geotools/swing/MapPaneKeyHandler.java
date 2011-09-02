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

import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import org.geotools.geometry.jts.ReferencedEnvelope;

/**
 * The default key event handler which provides scrolling and zooming of
 * a map pane display using the keyboard.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/swing/src/main/java/org/geotools/swing/JMapFrame.java $
 * @version $Id: JMapFrame.java 37840 2011-08-16 04:26:41Z mbedward $
 */
public class MapPaneKeyHandler extends KeyAdapter {
    
    private static final double SCROLL_FRACTION = 0.05;
    private static final double ZOOM_FRACTION = 1.5;
    
    /**
     * Represents a keyboard key, identified by key code and optional modifiers.
     * <p>
     * Create instances using values of {@code keyCode} and {@code modifiers} 
     * taken from constants in the {@linkplain KeyEvent} class. For example:
     * <pre><code>
     * KeyId left = new KeyId(KeyEvent.VK_LEFT, 0, "Left");
     * KeyId shiftUp = new KeyId(KeyEvent.VK_UP, KeyEvent.SHIFT_DOWN_MASK, "Shift+Up");
     * </code></pre>
     */
    public static class KeyId {
        private final int keyCode;
        private final int modifiers;
        private final String desc;
        
        /**
         * Creates a new instance. 
         * 
         * @param keyCode key code
         * @param modifiers modifiers (0 for none)
         * @param desc short description suitable for GUI labels etc.
         */
        public KeyId(int keyCode, int modifiers, String desc) {
            this.keyCode = keyCode;
            this.modifiers = modifiers;
            this.desc = desc;
        }

        /**
         * Creates a copy of an existing instance.
         * 
         * @param keyId the instance to copy
         * @throws IllegalArgumentException if {@code keyId} is {@code null}
         */
        private KeyId(KeyId keyId) {
            if (keyId == null) {
                throw new IllegalArgumentException("keyId must not be null");
            }
            
            this.keyCode = keyId.keyCode;
            this.modifiers = keyId.modifiers;
            this.desc = keyId.desc;
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
         * Tests whether the key code and modifiers of this {@code KeyId}
         * match that of a given {@code KeyEvent}. For convenience, this
         * method will return {@code false} if the input event is {@code null}.
         * 
         * @param e the input event
         * 
         * @return {@code true} if the key code and modifier values match those
         *     of the input event
         */
        public boolean matchesEvent(KeyEvent e) {
            if (e != null && e.getKeyCode() == keyCode) {
                int emod = e.getModifiersEx();
                if (modifiers == 0) {
                    return emod == 0;
                } else {
                    return (emod & modifiers) == modifiers;
                }
            }
            
            return false;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final KeyId other = (KeyId) obj;
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

    /**
     * Constants for supported actions.
     */
    public static enum Action {
        SCROLL_LEFT,
        SCROLL_RIGHT,
        SCROLL_UP,
        SCROLL_DOWN,
        ZOOM_IN,
        ZOOM_OUT,
        ZOOM_FULL_EXTENT;
    }
    
    /*
     * Default key bindings
     */
    private static final Map<KeyId, Action> defaultBindings = new HashMap<KeyId, Action>();
    
    static {
        defaultBindings.put(
                new KeyId(KeyEvent.VK_LEFT, 0, "Left"),
                Action.SCROLL_LEFT);
        
        defaultBindings.put( 
                new KeyId(KeyEvent.VK_RIGHT, 0, "Right"),
                Action.SCROLL_RIGHT);
        
        defaultBindings.put(
                new KeyId(KeyEvent.VK_UP, 0, "Up"),
                Action.SCROLL_UP);
        
        defaultBindings.put(
                new KeyId(KeyEvent.VK_DOWN, 0, "Down"),
                Action.SCROLL_DOWN);
        
        defaultBindings.put(
                new KeyId(KeyEvent.VK_UP, KeyEvent.SHIFT_DOWN_MASK, "Shift+Up"),
                Action.ZOOM_IN);
        
        defaultBindings.put(
                new KeyId(KeyEvent.VK_DOWN, KeyEvent.SHIFT_DOWN_MASK, "Shift+Down"),
                Action.ZOOM_OUT);
        
        defaultBindings.put(
                new KeyId(KeyEvent.VK_EQUALS, 0, "="),
                Action.ZOOM_FULL_EXTENT);
    }
    
    private final Map<KeyId, Action> bindings;
    private final MapPane mapPane;

    /**
     * Creates a new instance with the default key bindings for actions.
     * 
     * @param mapPane the map pane associated with this handler
     */
    MapPaneKeyHandler(MapPane mapPane) {
        this.bindings = new HashMap<KeyId, Action>(defaultBindings);
        this.mapPane = mapPane;
    }
    
    /**
     * Sets all key bindings to their default value.
     */
    public void setDefaultBindings() {
        bindings.clear();
        bindings.putAll(defaultBindings);
    }
    
    /**
     * Gets the current key bindings. The bindings are copied into the 
     * destination {@code Map}, so subsequent changes to it will not affect
     * this handler.
     * 
     * @return the current key bindings
     */
    public Map<KeyId, Action> getBindings() {
        Map<KeyId, Action> map = new HashMap<KeyId, Action>();
        
        for (Map.Entry<KeyId, Action> e : bindings.entrySet()) {
            map.put(new KeyId(e.getKey()), e.getValue());
        }
        return map;
    }
    
    /**
     * Sets the key binding for a single action.
     * 
     * @param keyId the key binding
     * @param action the action
     * @throws IllegalArgumentException if either argument is {@code null}
     */
    public void setBinding(KeyId keyId, Action action) {
        if (keyId == null) {
            throw new IllegalArgumentException("keyId must not be null");
        }
        if (action == null) {
            throw new IllegalArgumentException("action must not be null");
        }
        
        bindings.put(new KeyId(keyId), action);
    }
    
    /**
     * Sets one or more key bindings for actions. This method can be used to
     * set a subset of the key bindings while leaving others unchanged.
     * 
     * @param newBindings new key bindings
     * @throws IllegalArgumentException if {@code newBindings} is {@code null}
     */
    public void setBindings(Map<KeyId, Action> newBindings) {
        if (newBindings == null) {
            throw new IllegalArgumentException("argument must not be null");
        }
        
        for (Map.Entry<KeyId, Action> e : newBindings.entrySet()) {
            setBinding(e.getKey(), e.getValue());
        }
    }
    
    /**
     * Sets the bindings to those specified in {@code newBindings}. This method
     * differs to {@linkplain #setBindings(java.util.Map)} in that any actions
     * which do not appear in the input map are disabled.
     * 
     * @param newBindings new key bindings
     * @throws IllegalArgumentException if {@code newBindings} is {@code null}
     */
    public void setAllBindings(Map<KeyId, Action> newBindings) {
        if (newBindings == null) {
            throw new IllegalArgumentException("argument must not be null");
        }

        bindings.clear();
        setBindings(newBindings);
    }

    /**
     * Handles a key-pressed event.
     * 
     * @param e input key event
     */
    @Override
    public void keyPressed(KeyEvent e) {
        for (KeyId keyId : bindings.keySet()) {
            if (keyId.matchesEvent(e)) {
                processAction(bindings.get(keyId));
            }
        }
    }
    
    /**
     * Directs a requested action to the corresponding method.
     * 
     * @param action the action
     */
    private void processAction(Action action) {
        switch (action) {
            case SCROLL_LEFT:
            case SCROLL_RIGHT:
            case SCROLL_UP:
            case SCROLL_DOWN:
                scroll(action);
                break;

            case ZOOM_IN:
            case ZOOM_OUT:
            case ZOOM_FULL_EXTENT:
                zoom(action);
                break;
        }
    }

    /**
     * Scrolls the map pane image. We use {@linkplain MapPane#moveImage(int, int)}
     * rather than {@linkplain MapPane#setDisplayArea(<any>)} in this method
     * because it gives much smoother scrolling when the key is held down.
     * 
     * @param action scroll direction
     */
    private void scroll(Action action) {
        Rectangle r = ((JComponent) mapPane).getVisibleRect();
        if (!(r == null || r.isEmpty())) {
            int dx = 0;
            int dy = 0;
            switch (action) {
                case SCROLL_LEFT:
                    dx = Math.max(1, (int) (r.getWidth() * SCROLL_FRACTION));
                    break;

                case SCROLL_RIGHT:
                    dx = Math.min(-1, (int) (-r.getWidth() * SCROLL_FRACTION));
                    break;

                case SCROLL_UP:
                    dy = Math.max(1, (int) (r.getWidth() * SCROLL_FRACTION));
                    break;

                case SCROLL_DOWN:
                    dy = Math.min(-1, (int) (-r.getWidth() * SCROLL_FRACTION));
                    break;

                default:
                    throw new IllegalArgumentException("Invalid action argument: " + action);
            }

            mapPane.moveImage(dx, dy);
        }
    }
    
    /**
     * Zooms the map pane image.
     * 
     * @param action zoom action
     */
    private void zoom(Action action) {
        ReferencedEnvelope env = mapPane.getDisplayArea();
        double zoom;

        if (!env.isEmpty()) {
            switch (action) {
                case ZOOM_FULL_EXTENT:
                    mapPane.reset();
                    return;
                    
                case ZOOM_IN:
                    zoom = 1.0 / ZOOM_FRACTION;
                    break;

                case ZOOM_OUT:
                    zoom = ZOOM_FRACTION;
                    break;

                default:
                    throw new IllegalArgumentException("invalid action argument: " + action);
            }

            double centreX = env.getMedian(0);
            double centreY = env.getMedian(1);

            double w = env.getWidth() * zoom;
            double h = env.getHeight() * zoom;

            ReferencedEnvelope newEnv = new ReferencedEnvelope(
                    centreX - w / 2,
                    centreX + w / 2,
                    centreY - h / 2,
                    centreY + h / 2,
                    env.getCoordinateReferenceSystem());

            mapPane.setDisplayArea(newEnv);
        }
    }
}
