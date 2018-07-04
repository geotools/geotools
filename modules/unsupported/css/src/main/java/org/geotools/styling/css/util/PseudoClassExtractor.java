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
import org.opengis.filter.Filter;

/**
 * Takes a {@link Selector} and collects all available pseudo classes in it
 *
 * @author Andrea Aime
 */
public class PseudoClassExtractor extends AbstractSelectorVisitor {

    private final Set<PseudoClass> pseudoClasses = new LinkedHashSet<>();

    @Override
    public Object visit(Accept accept) {
        getPseudoClasses().add(PseudoClass.ROOT);
        return null;
    }

    @Override
    public Object visit(Reject reject) {
        getPseudoClasses().add(PseudoClass.ROOT);
        return null;
    }

    @Override
    public Object visit(Id id) {
        getPseudoClasses().add(PseudoClass.ROOT);
        return null;
    }

    @Override
    public Object visit(Data data) {
        getPseudoClasses().add(PseudoClass.ROOT);
        return null;
    }

    @Override
    public Object visit(TypeName typeName) {
        getPseudoClasses().add(PseudoClass.ROOT);
        return null;
    }

    @Override
    public Object visit(ScaleRange scaleRange) {
        getPseudoClasses().add(PseudoClass.ROOT);
        return null;
    }

    @Override
    public Object visit(PseudoClass pseudoClass) {
        getPseudoClasses().add(pseudoClass);
        return null;
    }

    @Override
    public Object visit(And and) {
        boolean found = false;
        for (Selector s : and.getChildren()) {
            if (s instanceof PseudoClass) {
                found = true;
                getPseudoClasses().add((PseudoClass) s);
            }
        }
        if (!found) {
            getPseudoClasses().add(PseudoClass.ROOT);
        }
        return null;
    }

    public Set<PseudoClass> getPseudoClasses() {
        return pseudoClasses;
    }

    public static Set<PseudoClass> getPseudoClasses(Selector selector) {
        PseudoClassExtractor extractor = new PseudoClassExtractor();
        Filter filter = (Filter) selector.accept(extractor);
        return extractor.getPseudoClasses();
    }
}
