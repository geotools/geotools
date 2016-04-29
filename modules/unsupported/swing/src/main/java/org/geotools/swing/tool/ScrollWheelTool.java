package org.geotools.swing.tool;

import java.awt.Cursor;
import java.awt.Rectangle;

import javax.swing.JComponent;

import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.Envelope2D;
import org.geotools.swing.JMapPane;
import org.geotools.swing.event.MapMouseEvent;

public class ScrollWheelTool extends AbstractZoomTool{
  
  
  public ScrollWheelTool(JMapPane mapPane) {

    setMapPane(mapPane);
  }

  @Override
  public Cursor getCursor() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void onMouseWheelMoved(MapMouseEvent ev) {
  
    Rectangle paneArea = ((JComponent) getMapPane()).getVisibleRect();
    
    DirectPosition2D mapPos = ev.getWorldPos();

    double scale = getMapPane().getWorldToScreenTransform().getScaleX();
    int clicks = ev.getWheelAmount();
   
    double actualZoom = 1;
    //positive clicks are down - zoom out
  
    if(clicks>0) {
       actualZoom =  -1.0 / (clicks*getZoom());
    }else {
      actualZoom = clicks * getZoom();
      
    }
    double newScale = scale * actualZoom;
  
    DirectPosition2D corner = new DirectPosition2D(
            mapPos.getX() - 0.5d * paneArea.getWidth() / newScale,
            mapPos.getY() + 0.5d * paneArea.getHeight() / newScale);
    
    Envelope2D newMapArea = new Envelope2D();
    newMapArea.setFrameFromCenter(mapPos, corner);
    getMapPane().setDisplayArea(newMapArea);
    
  }
}
