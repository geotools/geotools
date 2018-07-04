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

public class And extends Composite {

    public And(Selector... selectors) {
        super(selectors);
    }

    public And(List<Selector> selectors) {
        super(selectors);
    }

    @Override
    public Specificity getSpecificity() {
        Specificity sum = Specificity.ZERO;
        for (Selector s : getChildren()) {
            sum = sum.sum(s.getSpecificity());
        }

        return sum;
    }

    @Override
    public String toString() {
        return "And [children=" + getChildren() + "]";
    }

    public Object accept(SelectorVisitor visitor) {
        return visitor.visit(this);
    }
}
