/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.style;

import java.util.Comparator;
import java.util.List;

/** Mark factory comparator based on a simple class name identifiers list. */
public class MarkFactoryListComparator implements Comparator<MarkFactory> {

    private final List<String> markFactoryList;

    public MarkFactoryListComparator(List<String> markFactoryList) {
        this.markFactoryList = markFactoryList;
    }

    @Override
    public int compare(MarkFactory mf1, MarkFactory mf2) {
        return Integer.compare(getIndex(mf1), getIndex(mf2));
    }

    private int getIndex(MarkFactory mf) {
        if (mf == null) return Integer.MAX_VALUE;
        String simpleName = mf.getClass().getSimpleName();
        if (markFactoryList.contains(simpleName)) {
            return markFactoryList.indexOf(simpleName);
        }
        return Integer.MAX_VALUE;
    }
}
