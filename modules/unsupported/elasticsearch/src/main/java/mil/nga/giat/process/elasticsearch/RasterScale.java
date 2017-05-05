/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.process.elasticsearch;

public class RasterScale {

    private static final float DEFAULT_SCALE_MIN = 0f;

    private final Float scaleMin;

    private final Float scaleMax;

    private  boolean scaleLog;

    private Float dataMin;

    private Float dataMax;

    public RasterScale() {
        this(null, null, false);
    }

    public RasterScale(boolean useLog) {
        this(null, null, useLog);
    }

    public RasterScale(Float scaleMax) {
        this(DEFAULT_SCALE_MIN, scaleMax, false);
    }

    public RasterScale(Float scaleMin, Float scaleMax) {
        this(scaleMin, scaleMax, false);
    }

    public RasterScale(Float scaleMin, Float scaleMax, boolean scaleLog) {
        this.scaleMin = scaleMin;
        this.scaleMax = scaleMax;
        this.scaleLog = scaleLog;
        if (scaleMax != null && (scaleMin == null || scaleMax.floatValue() == scaleMin)) {
            throw new IllegalArgumentException();
        }
    }

    public float scaleValue(float value) {
        if (scaleLog && value > 0) {
            value = (float) Math.log10(value);
        }
        if (scaleMax == null) {
            return value;
        } else if (dataMax.floatValue() == dataMin) {
            return scaleMax;
        } else {
            return ((scaleMax - scaleMin) * (value - dataMin) / (dataMax - dataMin)) + scaleMin;
        }
    }

    public void prepareScale(float value) {
        if (scaleLog && value > 0) {
            value = (float) Math.log10(value);
        }
        if (scaleMax != null && dataMin != null) {
            if (value < dataMin) {
                dataMin = value;
            }
            if (value > dataMax) {
                dataMax = value;
            }
        } else if (scaleMax != null) {
            dataMin = value;
            dataMax = value;
        }
    }

    public boolean isScaleSet() {
        return scaleMax != null;
    }

    public Float getScaleMin() {
        return scaleMin;
    }

    public Float getScaleMax() {
        return scaleMax;
    }

}
