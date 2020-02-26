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

/**
 * The Specificity class represents a CSS specificity following an approach similar to CSS
 * specification: <a href="http://www.w3.org/TR/CSS21/cascade.html#specificity">"Calculating a
 * selector's specificity", CSS 2.1 Specification </a>
 *
 * <p>Given in geocss there is no way to inline a style in a dataset, the "a" specifity rank is
 * omitted, leaving us with 3 elements:
 *
 * <ul>
 *   <li>b: the * number of ID attributes in the selector (see {@link Id})
 *   <li>c: the number of other attributes and pseudo-classes (see the scale and data filtering
 *       selectors)
 *   <li>d: the number of element names and pseudo-elements in the selector (see {@link TypeName})
 * </ul>
 *
 * @see
 */
public class Specificity implements Comparable<Specificity> {

    /** The Specificity with all zero components */
    public static final Specificity ZERO = new Specificity(0, 0, 0);

    /** The Specificity with a single 1 as the pseudo-class specificity */
    public static final Specificity ID_1 = new Specificity(1, 0, 0);

    /** The Specificity with a single 1 as the pseudo-class specificity */
    public static final Specificity PSEUDO_1 = new Specificity(0, 1, 0);

    /** The Specificity with a single 2 as the pseudo-class specificity */
    public static final Specificity PSEUDO_2 = new Specificity(0, 2, 0);

    /** The Specificity with a single 1 as the type name specificity */
    public static final Specificity ELEMENT_1 = new Specificity(0, 0, 1);

    int b;

    int c;

    int d;

    public Specificity(int b, int c, int d) {
        this.b = b;
        this.c = c;
        this.d = d;
    }

    /**
     * Returns a new Specificity object representing by performing a element by element sum of the
     * components
     */
    public Specificity sum(Specificity other) {
        if (this == ZERO) {
            return other;
        } else if (other == ZERO) {
            return this;
        }
        return new Specificity(b + other.b, c + other.c, d + other.d);
    }

    /** Returns the max between the two specificities */
    public static Specificity max(Specificity s1, Specificity s2) {
        return s1.compareTo(s2) > 0 ? s1 : s2;
    }

    @Override
    public String toString() {
        return "Specificity (" + b + "," + c + "," + d + ")";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + b;
        result = prime * result + c;
        result = prime * result + d;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Specificity other = (Specificity) obj;
        if (b != other.b) return false;
        if (c != other.c) return false;
        if (d != other.d) return false;
        return true;
    }

    @Override
    public int compareTo(Specificity other) {
        if (other.b != b) {
            return b - other.b;
        }
        if (other.c != c) {
            return c - other.c;
        }
        if (other.d != d) {
            return d - other.d;
        }
        return 0;
    }
}
