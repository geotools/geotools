/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import org.geotools.util.NumberRange;

/** Base class for gpkg_schema data colum constraints */
public abstract class DataColumnConstraint {
    String name;

    public DataColumnConstraint(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /** A enumeration restriction, with possible values and their description */
    public static class Enum extends DataColumnConstraint {

        private final Map<String, String> values;

        public Enum(String name, Map<String, String> values) {
            super(name);
            this.values = values;
        }

        public Enum(String name, String... values) {
            super(name);
            if (values == null || values.length == 0)
                throw new IllegalArgumentException("Enum values cannot be empty or null");
            this.values = new LinkedHashMap<>();
            for (int i = 0; i < values.length; i++) {
                String value = values[i];
                this.values.put(String.valueOf(i), value);
            }
        }

        public Map<String, String> getValues() {
            return Collections.unmodifiableMap(values);
        }

        @Override
        public String toString() {
            return "Enum{" + "name='" + name + '\'' + ", values=" + values + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Enum anEnum = (Enum) o;
            return Objects.equals(values, anEnum.values);
        }

        @Override
        public int hashCode() {
            return Objects.hash(values);
        }
    }

    /** A <a href="https://www.sqlite.org/lang_expr.html#glob">SQLite GLOB</a> restriction */
    public static class Glob extends DataColumnConstraint {

        private final String glob;

        public Glob(String name, String glob) {
            super(name);
            this.glob = glob;
        }

        public String getGlob() {
            return glob;
        }

        @Override
        public String toString() {
            return "Glob{" + "name='" + name + '\'' + ", glob='" + glob + '\'' + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Glob glob1 = (Glob) o;
            return Objects.equals(glob, glob1.glob);
        }

        @Override
        public int hashCode() {
            return Objects.hash(glob);
        }
    }

    /** A numeric range restriction */
    public static class Range<T extends Number & Comparable<? super T>> extends DataColumnConstraint {

        private final NumberRange<T> range;

        public Range(String name, NumberRange<T> range) {
            super(name);
            this.range = range;
        }

        public NumberRange<T> getRange() {
            return range;
        }

        @Override
        public String toString() {
            return "Range{" + "name='" + name + '\'' + ", range=" + range + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Range<?> range1 = (Range<?>) o;
            return Objects.equals(range, range1.range);
        }

        @Override
        public int hashCode() {
            return Objects.hash(range);
        }
    }
}
