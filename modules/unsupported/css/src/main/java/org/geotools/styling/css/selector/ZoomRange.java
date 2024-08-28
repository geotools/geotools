/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling.css.selector;

import java.util.List;
import org.geotools.util.Range;

public class ZoomRange extends Selector {

    public static Selector combineAnd(List<ZoomRange> selectors, Object ctx) {
        if (selectors.size() == 1) {
            return selectors.get(0);
        }

        Range<Integer> range = selectors.get(0).range;
        for (ZoomRange selector : selectors) {
            @SuppressWarnings("unchecked")
            Range<Integer> intersect = (Range<Integer>) range.intersect(selector.range);
            range = intersect;
            if (range.isEmpty()) {
                return REJECT;
            }
        }

        return new ZoomRange(range);
    }

    public Range<Integer> range;

    public ZoomRange(Range<Integer> range) {
        this.range = range;
    }

    public ZoomRange(int min, boolean minIncluded, int max, boolean maxIncluded) {
        this.range = new Range<>(Integer.class, min, minIncluded, max, maxIncluded);
    }

    @Override
    public Specificity getSpecificity() {
        return Specificity.PSEUDO_1;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((range == null) ? 0 : range.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ZoomRange other = (ZoomRange) obj;
        if (range == null) {
            if (other.range != null) return false;
        } else if (!range.equals(other.range)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "ZoomRange " + range;
    }

    @Override
    public Object accept(SelectorVisitor visitor) {
        return visitor.visit(this);
    }
}
