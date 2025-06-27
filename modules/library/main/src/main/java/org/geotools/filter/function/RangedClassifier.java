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

/**
 * Classifies into ranges of minimum and maximum values.
 *
 * <p>The buckets are defined such that:<br>
 * min <= x < max
 *
 * <p>So if you provide the following min/max values:<br>
 * min = {3, 6, 9}<br>
 * max = {4, 10, 30}<br>
 *
 * <p>The classify function will classify items based on:<br>
 * 3 <= x < 4<br>
 * 6 <= x < 10<br>
 * 9 <= x <= 30<br>
 *
 * @author Cory Horner, Refractions Research
 */
@SuppressWarnings("unchecked") // for the life of me I cannot turn this into a type safe thing
public final class RangedClassifier extends Classifier {

    Comparable<?>[] min;
    Comparable<?>[] max;

    public RangedClassifier(Comparable[] min, Comparable[] max) {
        this.min = min;
        this.max = max;
        // initialize titles
        this.titles = new String[min.length];
        for (int i = 0; i < titles.length; i++) {
            titles[i] = generateTitle(min[i], max[i]);
        }
    }
    /**
     * Null safe title generation.
     *
     * @return generated title
     */
    private String generateTitle(Comparable<?> min, Comparable<?> max) {
        if (min == null && max == null) {
            return "Other";
        } else if (min == null) {
            return "Below " + truncateZeros(String.valueOf(max));
        } else if (max == null) {
            return "Above " + truncateZeros(String.valueOf(min));
        } else {
            return truncateZeros(String.valueOf(min)) + ".." + truncateZeros(String.valueOf(max));
        }
    }
    /**
     * Used to remove trailing zeros; preventing out put like 1.00000.
     *
     * @return origional string with any trailing decimal places removed.
     */
    private String truncateZeros(String str) {
        if (str.indexOf(".") > -1) {
            while (str.endsWith("0")) {
                str = str.substring(0, str.length() - 1);
            }
            if (str.endsWith(".")) {
                str = str.substring(0, str.length() - 1);
            }
        }
        return str;
    }

    @Override
    public int getSize() {
        return Math.min(min.length, max.length);
    }

    public Object getMin(int slot) {
        return min[slot];
    }

    public Object getMax(int slot) {
        return max[slot];
    }

    @Override
    public int classify(Object value) {
        return classify((Comparable) value);
    }

    @SuppressWarnings("CompareToZero")
    private int classify(Comparable<?> val) {
        Comparable<?> value = val;
        if (val instanceof Integer) { // convert to double as java is stupid
            value = Double.valueOf(((Integer) val).intValue());
        }
        // check each slot and see if: min <= value < max
        int last = min.length - 1;
        for (int i = 0; i <= last; i++) {
            Comparable localMin = this.min[i];
            Comparable localMax = this.max[i];

            if ((localMin == null || localMin.compareTo(value) < 1)
                    && (localMax == null || localMax.compareTo(value) > 0)) {
                return i;
            }
        }
        if (compareTo(max[last], value) == 0) { // if value = max, put it in the last slot
            return last;
        }
        return -1; // value does not fit into any of the provided categories
    }

    private int compareTo(Comparable compare, Comparable value) {
        if (compare == null && value == null) {
            return 0;
        } else if (compare == null) {
            return -1;
        } else if (value == null) {
            return +1;
        }
        return value.compareTo(compare);
    }
}
