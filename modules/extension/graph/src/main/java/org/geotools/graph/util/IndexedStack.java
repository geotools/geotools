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
package org.geotools.graph.util;

import java.util.HashMap;
import java.util.Map;

public class IndexedStack<E> extends java.util.Stack<E> {
    private Map<E, Integer> m_index; // object to index in stack

    public IndexedStack() {
        super();
        m_index = new HashMap<>();
    }

    @Override
    public E push(E item) {
        m_index.put(item, Integer.valueOf(size()));
        return super.push(item);
    }

    @Override
    public E pop() {
        E value = super.pop();
        m_index.remove(value);
        return value;
    }

    @Override
    public boolean contains(Object elem) {
        return m_index.get(elem) != null;
    }
}
