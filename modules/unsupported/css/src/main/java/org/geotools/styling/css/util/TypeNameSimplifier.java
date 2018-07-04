/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.styling.css.util;

import java.util.ArrayList;
import java.util.List;
import org.geotools.styling.css.selector.Accept;
import org.geotools.styling.css.selector.And;
import org.geotools.styling.css.selector.Data;
import org.geotools.styling.css.selector.Id;
import org.geotools.styling.css.selector.Or;
import org.geotools.styling.css.selector.PseudoClass;
import org.geotools.styling.css.selector.Reject;
import org.geotools.styling.css.selector.ScaleRange;
import org.geotools.styling.css.selector.Selector;
import org.geotools.styling.css.selector.SelectorVisitor;
import org.geotools.styling.css.selector.TypeName;

/**
 * Extracts a subset of a Selector that is compatible with the given TypeName. In case the default
 * typename is provided, only selector bits with no typename attached will be preserved
 *
 * @author Andrea Aime - GeoSolutions
 */
public class TypeNameSimplifier implements SelectorVisitor {

    TypeName targetTypeName;

    public TypeNameSimplifier(TypeName targetTypeName) {
        this.targetTypeName = targetTypeName;
    }

    @Override
    public Object visit(Accept accept) {
        return accept;
    }

    @Override
    public Object visit(Reject reject) {
        return reject;
    }

    @Override
    public Object visit(Id id) {
        // making no assumption on the id structure, even if normally
        // an id is built as typename.idx
        return id;
    }

    @Override
    public Object visit(Data data) {
        return data;
    }

    @Override
    public Object visit(And and) {
        List<Selector> selectors = new ArrayList<>();
        for (Selector child : and.getChildren()) {
            Selector converted = (Selector) child.accept(this);
            if (converted instanceof Reject) {
                return Selector.REJECT;
            } else if (!(converted instanceof Accept)) {
                selectors.add(converted);
            }
        }

        if (selectors.size() == 0) {
            return Selector.ACCEPT;
        } else if (selectors.size() == 1) {
            return selectors.get(0);
        } else {
            return new And(selectors);
        }
    }

    @Override
    public Object visit(Or or) {
        List<Selector> selectors = new ArrayList<>();
        for (Selector child : or.getChildren()) {
            Selector converted = (Selector) child.accept(this);
            if (converted instanceof Accept) {
                return Selector.ACCEPT;
            } else if (!(converted instanceof Reject)) {
                selectors.add(converted);
            }
        }

        if (selectors.size() == 0) {
            return Selector.REJECT;
        } else if (selectors.size() == 1) {
            return selectors.get(0);
        } else {
            return new Or(selectors);
        }
    }

    @Override
    public Object visit(TypeName typeName) {
        if (targetTypeName.equals(typeName)) {
            return Selector.ACCEPT;
        } else {
            return Selector.REJECT;
        }
    }

    @Override
    public Object visit(ScaleRange scaleRange) {
        return scaleRange;
    }

    @Override
    public Object visit(PseudoClass pseudoClass) {
        return pseudoClass;
    }
}
