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

import java.util.LinkedHashSet;
import java.util.Set;
import org.geotools.styling.css.selector.AbstractSelectorVisitor;
import org.geotools.styling.css.selector.Accept;
import org.geotools.styling.css.selector.And;
import org.geotools.styling.css.selector.Data;
import org.geotools.styling.css.selector.Id;
import org.geotools.styling.css.selector.PseudoClass;
import org.geotools.styling.css.selector.Reject;
import org.geotools.styling.css.selector.ScaleRange;
import org.geotools.styling.css.selector.Selector;
import org.geotools.styling.css.selector.TypeName;

/**
 * Grabs the type names mentioned in the selectors given to it
 *
 * @author Andrea Aime
 */
public class TypeNameExtractor extends AbstractSelectorVisitor {

    private final Set<TypeName> typeNames = new LinkedHashSet<>();

    @Override
    public Object visit(Accept accept) {
        getTypeNames().add(TypeName.DEFAULT);
        return null;
    }

    @Override
    public Object visit(Reject reject) {
        getTypeNames().add(TypeName.DEFAULT);
        return null;
    }

    @Override
    public Object visit(Id id) {
        getTypeNames().add(TypeName.DEFAULT);
        return null;
    }

    @Override
    public Object visit(Data data) {
        getTypeNames().add(TypeName.DEFAULT);
        return null;
    }

    @Override
    public Object visit(TypeName typeName) {
        getTypeNames().add(typeName);
        return null;
    }

    @Override
    public Object visit(ScaleRange scaleRange) {
        getTypeNames().add(TypeName.DEFAULT);
        return null;
    }

    @Override
    public Object visit(PseudoClass pseudoClass) {
        getTypeNames().add(TypeName.DEFAULT);
        return null;
    }

    @Override
    public Object visit(And and) {
        boolean found = false;
        for (Selector s : and.getChildren()) {
            if (s instanceof TypeName) {
                found = true;
                getTypeNames().add((TypeName) s);
            }
        }
        if (!found) {
            getTypeNames().add(TypeName.DEFAULT);
        }
        return null;
    }

    public Set<TypeName> getTypeNames() {
        return typeNames;
    }

    public void reset() {
        typeNames.clear();
    }
}
