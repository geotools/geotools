/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.geotools.swing.testutils;

import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;

/**
 *
 * @author michael
 */
public class WaitingMapContent extends MapContent {
    
    public static enum Type {
        BOUNDS,
        SCREEN_AREA;
    }
    
    public WaitingMapContent() {
        viewport = new WaitingViewport();
    }

    @Override
    public synchronized MapViewport getViewport() {
        return viewport;
    }
    
    public void setExpected(Type type) {
        ((WaitingViewport) viewport).setExpected(type);
    }
    
    public void await(Type type, long millisTimeout) {
        ((WaitingViewport) viewport).await(type, millisTimeout);
    }
}
