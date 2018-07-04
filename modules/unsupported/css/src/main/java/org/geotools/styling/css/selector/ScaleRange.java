/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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

public class ScaleRange extends Selector {

    public static Selector combineAnd(List<ScaleRange> selectors, Object ctx) {
        if (selectors.size() == 1) {
            return selectors.get(0);
        }

        Range<?> range = selectors.get(0).range;
        for (ScaleRange selector : selectors) {
            range = range.intersect(selector.range);
            if (range.isEmpty()) {
                return REJECT;
            }
        }

        return new ScaleRange((Range<Double>) range);
    }

    public Range<Double> range;

    public ScaleRange(Range<Double> range) {
        this.range = range;
    }

    public ScaleRange(double min, boolean minIncluded, double max, boolean maxIncluded) {
        this.range = new Range<Double>(Double.class, min, minIncluded, max, maxIncluded);
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
        ScaleRange other = (ScaleRange) obj;
        if (range == null) {
            if (other.range != null) return false;
        } else if (!range.equals(other.range)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "ScaleRange " + range;
    }

    public Object accept(SelectorVisitor visitor) {
        return visitor.visit(this);
    }
}
