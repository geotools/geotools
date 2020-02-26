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

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class PseudoClass extends Selector {

    public static Selector combineAnd(List<PseudoClass> selectors, Object ctx) {
        // just remove duplicate pseudo classes
        return new And(new ArrayList<>(new LinkedHashSet<Selector>(selectors)));
    }

    public static final PseudoClass ROOT =
            new PseudoClass(null, -1) {
                public String toString() {
                    return "ROOT";
                };
            };

    String className;

    int number = -1;

    public String getClassName() {
        return className;
    }

    public int getNumber() {
        return number;
    }

    public static PseudoClass newPseudoClass(String className) {
        return newPseudoClass(className, -1);
    }

    public static PseudoClass newPseudoClass(String className, int number) {
        return new PseudoClass(className, number);
    }

    private PseudoClass(String className, int number) {
        this.className = className;
        this.number = number;
    }

    @Override
    public String toString() {
        return "PseudoClass [className=" + className + ", number=" + number + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((className == null) ? 0 : className.hashCode());
        result = prime * result + number;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        PseudoClass other = (PseudoClass) obj;
        if (className == null) {
            if (other.className != null) return false;
        } else if (!className.equals(other.className)) return false;
        if (number != other.number) return false;
        return true;
    }

    @Override
    public Specificity getSpecificity() {
        if (number < 0) {
            return Specificity.PSEUDO_1;
        } else {
            return Specificity.PSEUDO_2;
        }
    }

    public Object accept(SelectorVisitor visitor) {
        return visitor.visit(this);
    }

    /** Returns true if this pseudo class is equals, or contains, the other */
    public boolean contains(PseudoClass pc) {
        if (this.equals(PseudoClass.ROOT)) {
            return true;
        } else if (pc == null || PseudoClass.ROOT.equals(pc)) {
            return false;
        }

        // contain all case, or same class
        if ("symbol".equals(className) || className.equals(pc.className)) {
            return number < 0 || pc.number == number;
        }

        return false;
    }

    /**
     * Returns the most specific pseudo class in the set, or null if the set contains inconsistent
     * pseudo classes (e.g., "mark" and "fill")
     */
    public static PseudoClass getMostSpecific(Set<PseudoClass> pseudoClasses) {
        PseudoClass mostSpecific = null;
        for (PseudoClass pc : pseudoClasses) {
            if (mostSpecific == null || mostSpecific.contains(pc)) {
                mostSpecific = pc;
            } else if (!pc.contains(mostSpecific)) {
                // found two that are not compatible
                return null;
            }
        }

        return mostSpecific;
    }
}
