/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.elasticsearch;

class RasterScale {

    private static final float DEFAULT_SCALE_MIN = 0f;

    private final Float scaleMin;

    private final Float scaleMax;

    private final boolean scaleLog;

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
