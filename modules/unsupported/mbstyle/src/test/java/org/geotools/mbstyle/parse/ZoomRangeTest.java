package org.geotools.mbstyle.parse;

import static org.junit.Assert.*;

import java.util.List;

import org.geotools.mbstyle.MapboxTestUtils;
import org.json.simple.JSONObject;
import org.junit.Test;

public class ZoomRangeTest {
    
    @Test
    public void zoomLevelPaint() throws Exception {
        // zoom function
        String jsonStr = 
                "{ 'fill-color' : " +
                "  {'type':'exponential', 'base': 1.0, 'stops':[[0,'blue'],[6,'red'],[12, 'lime']]}" +
                "}";
        ZoomRange layerRange = new ZoomRange();
        JSONObject paint = MapboxTestUtils.object(jsonStr);
        List<ZoomRange> range = ZoomRange.zoomLevelsPaint(paint, layerRange);
        assertTrue("ignore", range.isEmpty());
        
        // property function
        jsonStr = "{'property': 'numbervalue',"+
                  " 'type': 'interval',"+
                  " 'default': '#0F0F0F',"+
                  " 'stops': [[-1000, '#000000'], [-30, '#00FF00'], [0, '#0000FF'], [100, '#FFFFFF']]"+
                  "}";
        paint = MapboxTestUtils.object(jsonStr);
        range = ZoomRange.zoomLevelsPaint(paint, layerRange);
        assertTrue("ignore", range.isEmpty());
        
        // zoom and property exponential
        jsonStr =
            "{'fill-color': {"+
            "  'base': 1,"+
            "  'property': 'someNumericProperty',"+
            "  'stops': ["+
            "    [{'zoom':0 , 'value':0},'#3366FF'],"+
            "    [{'zoom':0 , 'value':50},'#336600'],"+
            "    [{'zoom':12 , 'value': 0},'#CC33FF'],"+
            "    [{'zoom':12 , 'value': 50 },'#CC3300'],"+
            "    [{'zoom':15, 'value': 0},'#FF3366'],"+
            "    [{'zoom':15, 'value': 50},'#FF3300'],"+
            "    [{'zoom': 22, 'value': 0 },'#FF6633'],"+
            "    [{'zoom': 22, 'value': 50},'#FF6600']"+
            "  ]"+
            "}}";
        paint = MapboxTestUtils.object(jsonStr);
        range = ZoomRange.zoomLevelsPaint(paint, layerRange);
        assertEquals(5,range.size());
    }

}
