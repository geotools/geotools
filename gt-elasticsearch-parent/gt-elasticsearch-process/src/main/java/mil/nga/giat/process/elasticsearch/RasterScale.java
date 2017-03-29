/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.process.elasticsearch;

import java.util.List;

public class RasterScale {

    private boolean isMinMaxInitialized = false;
    private boolean scaleSet = false;
    private float dataMin;
    private float dataMax;
    private float scaleMin;
    private float scaleMax;

    public RasterScale(List<Float> range) {
        if (null != range && range.size() == 1) {
            scaleSet = true;
            scaleMin = 0;
            scaleMax = range.get(0);
        } else if (null != range && range.size() == 2) {
            scaleSet = true;
            scaleMax = range.get(0);
            scaleMin = range.get(1);
            if (scaleMin > scaleMax) {
                scaleMax = range.get(1);
                scaleMin = range.get(0);
            }
        }
        
        if (scaleSet && scaleMax == scaleMin) {
            throw new IllegalArgumentException();
        }
    }
    
    public float scaleValue(float value) {
        if (!scaleSet) {
            return value;
        } else if (dataMax == dataMin) {
            return scaleMax;
        } else {
            return ((scaleMax - scaleMin) * (value - dataMin) / (dataMax - dataMin)) + scaleMin;
        }
    }
    
    public void prepareScale(float value) {
        if (!scaleSet) return; 

        if (isMinMaxInitialized) {
            if (value < dataMin) {
                dataMin = value;
            }
            if (value > dataMax) {
                dataMax = value;
            }
        } else {
            dataMin = value;
            dataMax = value;
            isMinMaxInitialized = true;
        }
    }
    
    public boolean isScaleSet() {
        return scaleSet;
    }

    public float getDataMin() {
        return dataMin;
    }

    public float getDataMax() {
        return dataMax;
    }

    public float getScaleMin() {
        return scaleMin;
    }

    public float getScaleMax() {
        return scaleMax;
    }
}
