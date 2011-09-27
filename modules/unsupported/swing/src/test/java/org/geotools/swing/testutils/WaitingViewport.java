/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.geotools.swing.testutils;

import java.awt.Rectangle;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapViewport;

/**
 *
 * @author michael
 *
 * @source $URL$
 */
public class WaitingViewport extends MapViewport {
    
    private CountDownLatch boundsLatch;
    private CountDownLatch screenAreaLatch;

    public void setExpected(WaitingMapContent.Type type) {
        switch (type) {
            case BOUNDS:
                boundsLatch = new CountDownLatch(1);
                break;
                
            case SCREEN_AREA:
                screenAreaLatch = new CountDownLatch(1);
                break;
        }
    }
    
    public boolean await(WaitingMapContent.Type type, long millisTimeout) {
        boolean result = false;
        try {
            switch (type) {
                case BOUNDS:
                    boundsLatch.await(millisTimeout, TimeUnit.MILLISECONDS);
                    break;
                    
                case SCREEN_AREA:
                    screenAreaLatch.await(millisTimeout, TimeUnit.MILLISECONDS);
                    break;
            }
            
        } catch (InterruptedException ex) {
            // do nothing
        } finally {
            return result;
        }
    }
    
    @Override
    public void setBounds(ReferencedEnvelope requestedBounds) {
        super.setBounds(requestedBounds);
        if (boundsLatch != null) {
            boundsLatch.countDown();
        }
    }

    @Override
    public void setScreenArea(Rectangle screenArea) {
        super.setScreenArea(screenArea);
        if (screenAreaLatch != null) {
            screenAreaLatch.countDown();
        }
    }

}
