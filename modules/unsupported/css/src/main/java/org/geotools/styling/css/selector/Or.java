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

public class Or extends Composite {

    public Or(Selector... selectors) {
        super(selectors);
    }

    public Or(List<Selector> selectors) {
        super(selectors);
    }

    @Override
    public Specificity getSpecificity() {
        Specificity max = Specificity.ZERO;
        for (Selector s : getChildren()) {
            Specificity curr = s.getSpecificity();
            if (curr.compareTo(max) > 0) {
                max = curr;
            }
        }

        return max;
    }

    public Object accept(SelectorVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "Or [children=" + getChildren() + "]";
    }
}
