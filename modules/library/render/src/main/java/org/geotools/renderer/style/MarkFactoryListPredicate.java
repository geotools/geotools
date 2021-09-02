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

import java.util.List;
import java.util.function.Predicate;

/** Mark factory filtering predicate based on a provided class name identifiers list. */
public class MarkFactoryListPredicate implements Predicate<MarkFactory> {

    private final List<String> markFactoryList;

    public MarkFactoryListPredicate(List<String> markFactoryList) {
        this.markFactoryList = markFactoryList;
    }

    @Override
    public boolean test(MarkFactory markFactory) {
        if (markFactory == null) return false;
        return markFactoryList.contains(markFactory.getClass().getSimpleName());
    }
}
