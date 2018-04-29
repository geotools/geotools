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
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Base class for selectors that have a list of children. Internally we keep the children in a set
 * because "A and B" == "B and A" and "A or B" == "B or A"
 *
 * @author Andrea Aime - GeoSolutions
 */
public abstract class Composite extends Selector {
    private LinkedHashSet<Selector> children;

    public Composite(Selector... selectors) {
        this.children = new LinkedHashSet<>(Arrays.asList(selectors));
    }

    public Composite(List<Selector> selectors) {
        this.children = new LinkedHashSet<>(selectors);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getChildren() == null) ? 0 : getChildren().hashCode());
        return result;
    }

    /** Equality uses set oriented equality for the children, not list oriented one */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Composite other = (Composite) obj;
        if (getChildren() == null) {
            if (other.getChildren() != null) return false;
        } else if (!getChildren().equals(other.getChildren())) return false;
        return true;
    }

    public List<Selector> getChildren() {
        return new ArrayList<>(children);
    }
}
