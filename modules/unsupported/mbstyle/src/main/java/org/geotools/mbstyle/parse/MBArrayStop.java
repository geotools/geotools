/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.mbstyle.parse;

import org.json.simple.JSONArray;

/**
 * Wrapper for a "stop" in an array function (See {@link MBFunction#isArrayFunction()}), providing
 * methods to access the stop key and stop value array, as well as a method to reduce it from an
 * array stop to a single-value stop (for a given index).
 *
 * <p>For example, the following "stops" list contains two {@link MBArrayStop}s. (for each stop, the
 * stop value is an array).
 *
 * <pre>
 *  'stops': [
 *          // [stopkey, stopValueArray]
 *             [0,       [0,10]],
 *             [100,     [2,15]]
 *    ]
 * </pre>
 *
 * <p>Used by {@link MBFunction#splitArrayFunction()} to split an array function into multiple
 * functions, one for each dimension in the output array.
 */
public class MBArrayStop {
    public JSONArray json;

    public JSONArray stopValueArray;

    public MBArrayStop(JSONArray array) {
        if (array.size() != 2) {
            throw new MBFormatException(
                    "Exception parsing function stop: stop must be an array with length 2.");
        }
        if (!(array.get(1) instanceof JSONArray)) {
            throw new MBFormatException(
                    "Exception parsing array function stop: stop value must be an array.");
        }
        this.json = array;
        this.stopValueArray = ((JSONArray) json.get(1));
    }

    /**
     * Get the stop key for this stop.
     *
     * <p>For example, for the following {@link MBArrayStop}: <code>[1,   [0,10]]</code> the output
     * of stop.getStopKey() is 1.
     */
    public Object getStopKey() {
        return json.get(0);
    }

    /**
     * Returns the size of the array of stop values.
     *
     * <p>For example, for the following {@link MBArrayStop}: <code>[0,   [0,10]]</code> the output
     * of stop.getStopValueCount() is 2.
     *
     * @return the size of the array of stop values.
     */
    public int getStopValueCount() {
        return stopValueArray.size();
    }

    /**
     * Returns the value from the stop value array at the provided index.
     *
     * <p>For example, for the following {@link MBArrayStop}: <code>[0,   [0,10]]</code>, the output
     * of stop.getStopValue(1) is 10.
     *
     * @param idx The index to look up in the stop value array.
     * @return The value from the stop value array at the provided index.
     */
    public Object getStopValue(int idx) {
        if (stopValueArray.size() <= idx) {
            throw new MBFormatException(
                    "Exception parsing array function stop: stop value requested at index "
                            + idx
                            + ", but stop value array length is "
                            + stopValueArray.size());
        }
        return stopValueArray.get(idx);
    }

    /**
     * Returns the JSON for a new stop derived from this stop, but reduced to a single-value stop
     * with the value from the provided index.
     *
     * <p>For example, for the {@link MBArrayStop} <code>[0,   [0,10]]</code>, The output of
     * stop.reducedToIndex(1) is the following new stop: <code>[0, 10]</code>
     *
     * @param idx The index to use as the single value for the new stop.
     * @return A new stop, with a single stop value rather than a stop value array.
     */
    public JSONArray reducedToIndex(int idx) {
        if (stopValueArray.size() <= idx) {
            throw new MBFormatException(
                    "Exception parsing array function stop: stop value requested at index "
                            + idx
                            + ", but stop value array length is "
                            + stopValueArray.size());
        }
        JSONArray newStopJson = new JSONArray();
        newStopJson.add(getStopKey());
        newStopJson.add(getStopValue(idx));
        return newStopJson;
    }
}
