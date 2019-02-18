/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.util.factory;

import java.util.HashSet;
import java.util.Set;

class RecursionCheckingHelper {

    private final ThreadLocal<Set> threadLocalSet = new ThreadLocal<Set>();

    boolean addAndCheck(Object item) {
        Set set = threadLocalSet.get();
        if (set == null) {
            set = new HashSet<Class<?>>();
            threadLocalSet.set(set);
        }
        return set.add(item);
    }

    boolean contains(Object item) {
        Set<Class<?>> set = threadLocalSet.get();
        if (set == null) {
            return false;
        }
        return set.contains(item);
    }

    void removeAndCheck(Object item) {
        Set<Class<?>> set = threadLocalSet.get();
        if (set == null) {
            throw new AssertionError(null); // Should never happen.
        } else if (!set.remove(item)) {
            throw new AssertionError(item); // Should never happen.
        }
        if (set.isEmpty()) {
            threadLocalSet.remove();
        }
    }
}
