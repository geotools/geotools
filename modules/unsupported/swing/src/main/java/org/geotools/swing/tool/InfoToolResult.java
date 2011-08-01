/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.swing.tool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.opengis.feature.type.Name;

/**
 * Used by {@code InfoToolHelper} classes to pass feature data to the
 * parent {@code InfoTool} object.
 *
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $URL$
 */
public class InfoToolResult {
    private final List<Map<String, Object>> featureEntries;
    private Map<String, Object> currentEntry;

    public InfoToolResult() {
        featureEntries = new ArrayList<Map<String, Object>>();
        addNewFeatureEntry();
    }

    /**
     * Adds a new feature entry to this result. Subsequent calls to
     * {@code #setFeatureValue} methods wiill act on the new feature.
     * Repeated calls without intervening {@code setFeatureValue} calls
     * are ignored.
     */
    public void newFeature() {
        if (!currentEntry.isEmpty()) {
            addNewFeatureEntry();
        }
    }

    public void setFeatureValue(Name name, Object value) {
        currentEntry.put(name.toString(), value);
    }

    public void setFeatureValue(String name, Object value) {
        currentEntry.put(name, value);
    }

    public int getNumFeatures() {
        int n = featureEntries.size();
        if (currentEntry.isEmpty()) {
            return n - 1;
        }
        return n;
    }

    private void addNewFeatureEntry() {
        currentEntry = new LinkedHashMap<String, Object>();
        featureEntries.add(currentEntry);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        int k = 0;
        for (Map<String, Object> entry : featureEntries) {
            for (Entry<String, Object> e : entry.entrySet()) {
                sb.append(e.getKey()).append(": ").append(e.getValue()).append('\n');
            }
            if (++k < featureEntries.size()) {
                sb.append('\n');
            }
        }

        return sb.toString();
    }

    public Map<String, Object> getFeatureData(int featureIndex) {
        if (featureIndex < 0 || featureIndex >= getNumFeatures()) {
            return Collections.emptyMap();
        }

        return Collections.unmodifiableMap(featureEntries.get(featureIndex));
    }
}
