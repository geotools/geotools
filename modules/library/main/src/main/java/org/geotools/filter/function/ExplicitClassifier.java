/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.function;

import java.util.Set;

public final class ExplicitClassifier extends Classifier {

    Set[] values = null; //the contents of each bin (set of objects)
    
    public ExplicitClassifier(Set[] values) {
        this.values = values;
        //initialize titles
        this.titles = new String[values.length];
        for (int i = 0; i < titles.length; i++) {
            Object[] set = values[i].toArray();
            if (set.length > 0) {
                StringBuffer title = new StringBuffer(set[0] == null ? "null" : set[0].toString());
                for (int j = 1; j < set.length; j++) {
                    title.append(", ");
                    title.append(set[j] == null ? "null" : set[j].toString());
                }
                titles[i] = title.toString();
            } else {
                titles[i] = "";
            }
        }
    }
    
    public int getSize() {
        return values.length;
    }
    
    /**
     * Returns all the unique values for a particular slot.
     * 
     * @param index
     * @return all applicable values for a slot
     */
    public Set getValues(int index) {
        return values[index];
    }

    
    public int classify(Object value) {
        for (int i = 0; i < values.length; i++) {
            if (values[i].contains(value)) {
                return i;
            }
        }
        return -1;
    }
    
}
