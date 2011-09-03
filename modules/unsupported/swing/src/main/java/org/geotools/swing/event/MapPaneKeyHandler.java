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

package org.geotools.swing.event;

import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.swing.MapPane;

/**
 * Handles keyboard events for a map pane. This is the default handler for classes derived from
 * {@linkplain AbstractMapPane}. It provides for keyboard-controlled scrolling and zooming of 
 * the display. The default key bindings for actions should be suitable for most keyboards.
 * <p>
 * 
 * While the Java Swing toolkit provides its own mechanism for linking key events to actions,
 * this class is somewhat easier to use and provides a model that could be implemented in other
 * toolkits such as SWT. However, you are free to ignore this class and use your own key 
 * handler instead since the map pane classes only require that the handler implements 
 * the {@linkplain java.awt.event.KeyListener} interface.
 * 
 * <p>
 * Key bindings for an individual action can be set like this:
 * <pre><code>
 * // Bind left-scroll action to the 'h' key (for Vim fans)
 * KeyInfo key = new KeyInfo(KeyEvent.VK_H, 0);
 * mapPaneKeyHandler.setBinding(key, MapPaneKeyHandler.Action.SCROLL_LEFT);
 * </code></pre>
 * 
 * Multiple bindings can be set with the {@linkplain #setBindings(Map)} or 
 * {@linkplain #setAllBindings(Map)} methods:
 * <pre><code>
 * Map&lt;KeyInfo, MapPaneKeyHandler.Action&gt; bindings =
 *         new HashMap&lt;KeyInfo, MapPaneKeyHandler.Action&gt;();
 * 
 * bindings.put(new KeyInfo(KeyEvent.VK_H, 0), MapPaneKeyHandler.Action.SCROLL_LEFT);
 * bindings.put(new KeyInfo(KeyEvent.VK_L, 0), MapPaneKeyHandler.Action.SCROLL_RIGHT);
 * bindings.put(new KeyInfo(KeyEvent.VK_K, 0), MapPaneKeyHandler.Action.SCROLL_UP);
 * bindings.put(new KeyInfo(KeyEvent.VK_J, 0), MapPaneKeyHandler.Action.SCROLL_DOWN);
 * 
 * mapPaneKeyHandler.setBindings( bindings );
 * </code></pre>
 * 
 * @see KeyInfo
 * @see AbstractMapPane#setKeyHandler(java.awt.event.KeyListener)
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class MapPaneKeyHandler extends KeyAdapter {
    
    private static final double SCROLL_FRACTION = 0.05;
    private static final double ZOOM_FRACTION = 1.5;

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
    private static final Map<KeyInfo, Action> defaultBindings = new HashMap<KeyInfo, Action>();
    
    static {
        defaultBindings.put(
                new KeyInfo(KeyEvent.VK_LEFT, 0, "Left"),
                Action.SCROLL_LEFT);
        
        defaultBindings.put( 
                new KeyInfo(KeyEvent.VK_RIGHT, 0, "Right"),
                Action.SCROLL_RIGHT);
        
        defaultBindings.put(
                new KeyInfo(KeyEvent.VK_UP, 0, "Up"),
                Action.SCROLL_UP);
        
        defaultBindings.put(
                new KeyInfo(KeyEvent.VK_DOWN, 0, "Down"),
                Action.SCROLL_DOWN);
        
        defaultBindings.put(
                new KeyInfo(KeyEvent.VK_UP, KeyEvent.SHIFT_DOWN_MASK, "Shift+Up"),
                Action.ZOOM_IN);
        
        defaultBindings.put(
                new KeyInfo(KeyEvent.VK_DOWN, KeyEvent.SHIFT_DOWN_MASK, "Shift+Down"),
                Action.ZOOM_OUT);
        
        defaultBindings.put(
                new KeyInfo(KeyEvent.VK_EQUALS, 0, "="),
                Action.ZOOM_FULL_EXTENT);
    }
    
    private final Map<KeyInfo, Action> bindings;
    private final MapPane mapPane;

    /**
     * Creates a new instance with the default key bindings for actions.
     * 
     * @param mapPane the map pane associated with this handler
     */
    public MapPaneKeyHandler(MapPane mapPane) {
        this.bindings = new HashMap<KeyInfo, Action>(defaultBindings);
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
    public Map<KeyInfo, Action> getBindings() {
        Map<KeyInfo, Action> map = new HashMap<KeyInfo, Action>();
        
        for (Map.Entry<KeyInfo, Action> e : bindings.entrySet()) {
            map.put(new KeyInfo(e.getKey()), e.getValue());
        }
        return map;
    }
    
    /**
     * Gets the current key binding for the given action. The object
     * returned is a copy.
     * 
     * @param action the action
     * @return the key binding; or {@code null} if there is no binding
     * @throws IllegalArgumentException if {@code action} is {@code null}
     */
    public KeyInfo getBindingForAction(Action action) {
        if (action == null) {
            throw new IllegalArgumentException("action must not be null");
        }
        
        KeyInfo keyInfo = null;
        
        for (Map.Entry<KeyInfo, Action> e : bindings.entrySet()) {
            if (e.getValue() == action) {
                keyInfo = new KeyInfo(e.getKey());
                break;
            }
        }

        return keyInfo;
    }

    /**
     * Sets the key binding for a single action.
     * 
     * @param keyInfo the key binding
     * @param action the action
     * @throws IllegalArgumentException if either argument is {@code null}
     */
    public void setBinding(KeyInfo keyInfo, Action action) {
        if (keyInfo == null) {
            throw new IllegalArgumentException("keyInfo must not be null");
        }
        if (action == null) {
            throw new IllegalArgumentException("action must not be null");
        }
        
        bindings.put(new KeyInfo(keyInfo), action);
    }
    
    /**
     * Sets one or more key bindings for actions. This method can be used to
     * set a subset of the key bindings while leaving others unchanged.
     * 
     * @param newBindings new key bindings
     * @throws IllegalArgumentException if {@code newBindings} is {@code null}
     */
    public void setBindings(Map<KeyInfo, Action> newBindings) {
        if (newBindings == null) {
            throw new IllegalArgumentException("argument must not be null");
        }
        
        for (Map.Entry<KeyInfo, Action> e : newBindings.entrySet()) {
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
    public void setAllBindings(Map<KeyInfo, Action> newBindings) {
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
        for (KeyInfo keyInfo : bindings.keySet()) {
            if (keyInfo.matchesEvent(e)) {
                processAction(bindings.get(keyInfo));
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
