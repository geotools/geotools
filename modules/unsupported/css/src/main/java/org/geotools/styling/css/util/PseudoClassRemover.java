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
 * Simplifies out all pseudo classes, replacing them with {@link Selector#ACCEPT}
 *
 * @author Andrea Aime - GeoSolutions
 */
public class PseudoClassRemover implements SelectorVisitor {

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
        return id;
    }

    @Override
    public Object visit(Data data) {
        return data;
    }

    @Override
    public Object visit(And and) {
        List<Selector> simplified = new ArrayList<>();
        for (Selector child : and.getChildren()) {
            Selector newChild = (Selector) child.accept(this);
            if (newChild == Selector.REJECT) {
                return Selector.REJECT;
            } else if (newChild != Selector.ACCEPT) {
                simplified.add(newChild);
            }
        }
        if (simplified.size() == 0) {
            return Selector.ACCEPT;
        } else if (simplified.size() == 1) {
            return simplified.get(0);
        } else {
            return new And(simplified);
        }
    }

    @Override
    public Object visit(Or or) {
        List<Selector> simplified = new ArrayList<>();
        for (Selector child : or.getChildren()) {
            Selector newChild = (Selector) child.accept(this);
            if (newChild == Selector.ACCEPT) {
                return Selector.ACCEPT;
            } else if (newChild != Selector.REJECT) {
                simplified.add(newChild);
            }
        }
        if (simplified.size() == 0) {
            return Selector.REJECT;
        } else if (simplified.size() == 1) {
            return simplified.get(0);
        } else {
            return new Or(simplified);
        }
    }

    @Override
    public Object visit(TypeName typeName) {
        return typeName;
    }

    @Override
    public Object visit(ScaleRange scaleRange) {
        return scaleRange;
    }

    @Override
    public Object visit(PseudoClass pseudoClass) {
        // simplify out all pseudo classes
        return Selector.ACCEPT;
    }
}
