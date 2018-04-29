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
 * Base class for selector visitors
 *
 * @author Andrea Aime - GeoSolutions
 */
public class AbstractSelectorVisitor implements SelectorVisitor {

    @Override
    public Object visit(Accept accept) {
        return null;
    }

    @Override
    public Object visit(Reject reject) {
        return null;
    }

    @Override
    public Object visit(Id id) {
        return null;
    }

    @Override
    public Object visit(Data data) {
        return null;
    }

    @Override
    public Object visit(And and) {
        for (Selector s : and.getChildren()) {
            s.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(Or or) {
        for (Selector s : or.getChildren()) {
            s.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(TypeName typeName) {
        return null;
    }

    @Override
    public Object visit(ScaleRange scaleRange) {
        return null;
    }

    @Override
    public Object visit(PseudoClass pseudoClass) {
        return null;
    }
}
