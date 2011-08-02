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

    private static class ResultItem {
        String id;
        Map<String, Object> data;
        
        ResultItem(String id) {
            this.id = id;
            data = new LinkedHashMap<String, Object>();
        }
        
        boolean isEmpty() {
            return data.isEmpty();
        }
        
        void put(String name, Object value) {
            data.put(name, value);
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            
            if (id != null) {
                sb.append(id).append("\n");
            }
            
            for (Entry<String, Object> e : data.entrySet()) {
                sb.append(e.getKey()).append(": ").append(e.getValue()).append('\n');
            }
            
            return sb.toString();
        }
    }
    
    private final List<ResultItem> items;
    private ResultItem currentItem;

    public InfoToolResult() {
        items = new ArrayList<ResultItem>();
    }

    /**
     * Adds a new feature entry to this result. This <strong>must</strong>
     * be called prior to calling the {@code setFeatureValue} methods.
     */
    public void newFeature(String id) {
        currentItem = new ResultItem(id);
        items.add(currentItem);
    }

    public void setFeatureValue(Name name, Object value) {
        if (currentItem == null) {
            throw new IllegalStateException("Missing prior call to newFeature method");
        }
        currentItem.put(name.toString(), value);
    }

    public void setFeatureValue(String name, Object value) {
        if (currentItem == null) {
            throw new IllegalStateException("Missing prior call to newFeature method");
        }
        currentItem.put(name, value);
    }

    public int getNumFeatures() {
        return items.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        int k = 0;
        for (ResultItem item : items) {
            sb.append(item.toString());
            if (++k < items.size()) {
                sb.append('\n');
            }
        }

        return sb.toString();
    }

    public Map<String, Object> getFeatureData(int featureIndex) {
        if (featureIndex < 0 || featureIndex >= getNumFeatures()) {
            throw new IndexOutOfBoundsException("Invalid index value: " + featureIndex);
        }

        return Collections.unmodifiableMap(items.get(featureIndex).data);
    }
    
    public String getFeatureId(int featureIndex) {
        if (featureIndex < 0 || featureIndex >= getNumFeatures()) {
            throw new IndexOutOfBoundsException("Invalid index value: " + featureIndex);
        }
        
        return items.get(featureIndex).id;
    }
    
}
