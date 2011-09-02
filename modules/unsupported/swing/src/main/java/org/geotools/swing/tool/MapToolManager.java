/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geotools.swing.tool;

import java.awt.event.MouseWheelListener;
import javax.swing.event.MouseInputListener;
import org.geotools.swing.event.MapMouseListener;

/**
 *
 * @author michael
 */
public interface MapToolManager extends MouseInputListener, MouseWheelListener {

    /**
     * Adds a listener for map pane mouse events.
     *
     * @param listener the new listener
     * @return true if successful; false otherwise
     * @throws IllegalArgumentException if the {@code listener} is {@code null}
     */
    boolean addMouseListener(MapMouseListener listener);

    /**
     * Get the active cursor tool
     *
     * @return live reference to the active cursor tool or {@code null} if no
     * tool is active
     */
    CursorTool getCursorTool();

    /**
     * Removes the given listener.
     *
     * @param listener the listener to remove
     * @return true if successful; false otherwise
     * @throws IllegalArgumentException if the {@code listener} is {@code null}
     */
    boolean removeMouseListener(MapMouseListener listener);

    /**
     * Sets the cursor tool.
     *
     * @param tool the tool to set or {@code null} for no tool
     *
     * @return true if successful; false otherwise
     */
    boolean setCursorTool(CursorTool tool);
    
}
