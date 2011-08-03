/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geotools.swing.testutils;

import org.geotools.swing.event.MapMouseEvent;
import org.geotools.swing.event.MapMouseListener;

/**
 * A MapMouseListener that can be set to expect specified events
 * and test if they are received.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class WaitingMapMouseListener 
        extends WaitingListener<MapMouseEvent, WaitingMapMouseListener.Type> 
        implements MapMouseListener {
    
    public static enum Type {
        CLICKED,
        DRAGGED,
        ENTERED,
        EXITED,
        MOVED,
        PRESSED,
        RELEASED,
        WHEEL_MOVED;
    }

    public WaitingMapMouseListener() {
        super(Type.values().length);
    }
    

    @Override
    public void onMouseClicked(MapMouseEvent ev) {
        catchEvent(Type.CLICKED.ordinal(), ev);
    }

    @Override
    public void onMouseDragged(MapMouseEvent ev) {
        catchEvent(Type.DRAGGED.ordinal(), ev);
    }

    @Override
    public void onMouseEntered(MapMouseEvent ev) {
        catchEvent(Type.ENTERED.ordinal(), ev);
    }

    @Override
    public void onMouseExited(MapMouseEvent ev) {
        catchEvent(Type.EXITED.ordinal(), ev);
    }

    @Override
    public void onMouseMoved(MapMouseEvent ev) {
        catchEvent(Type.MOVED.ordinal(), ev);
    }

    @Override
    public void onMousePressed(MapMouseEvent ev) {
        catchEvent(Type.PRESSED.ordinal(), ev);
    }

    @Override
    public void onMouseReleased(MapMouseEvent ev) {
        catchEvent(Type.RELEASED.ordinal(), ev);
    }

    @Override
    public void onMouseWheelMoved(MapMouseEvent ev) {
        catchEvent(Type.WHEEL_MOVED.ordinal(), ev);
    }
    
}
