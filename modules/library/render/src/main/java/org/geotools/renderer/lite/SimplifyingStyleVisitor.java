/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite;

import java.util.List;
import java.util.stream.Collectors;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.visitor.SimplifyingFilterVisitor;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.FeatureTypeStyleImpl;
import org.geotools.styling.Rule;
import org.geotools.styling.visitor.DuplicatingStyleVisitor;
import org.opengis.feature.type.FeatureType;

/**
 * A style visitor returning a simplified copy of the style, in particular, simplfying filters and
 * the expressions used in symbolizer properties, when they are found to be static within the
 * context of the current rendering evaluation. It's used inside {@link StreamingRenderer} to
 * simplify the styles before they are used for the current render.
 *
 * <p>Implementation has a quirk: rendering transformations cannot be simplified, they can look
 * static but will be evaluated against the input feature collection/coverage, so they are preserved
 * as-is.
 */
class SimplifyingStyleVisitor extends DuplicatingStyleVisitor {

    SimplifyingStyleVisitor() {
        super(
                CommonFactoryFinder.getStyleFactory(null),
                CommonFactoryFinder.getFilterFactory2(null),
                new SimplifyingFilterVisitor());
    }

    SimplifyingStyleVisitor(FeatureType schema) {
        super(
                CommonFactoryFinder.getStyleFactory(null),
                CommonFactoryFinder.getFilterFactory2(null),
                getFilterSimplifier(schema));
    }

    private static SimplifyingFilterVisitor getFilterSimplifier(FeatureType schema) {
        SimplifyingFilterVisitor filterSimplifier = new SimplifyingFilterVisitor();
        filterSimplifier.setFeatureType(schema);
        return filterSimplifier;
    }

    @Override
    public void visit(FeatureTypeStyle fts) {

        FeatureTypeStyle copy = new FeatureTypeStyleImpl(fts);

        List<Rule> rulesCopy =
                fts.rules().stream()
                        .filter(r -> r != null)
                        .map(
                                r -> {
                                    r.accept(this);
                                    return !pages.isEmpty() ? (Rule) pages.pop() : null;
                                })
                        .filter(r -> r != null)
                        .collect(Collectors.toList());

        copy.rules().clear();
        copy.rules().addAll(rulesCopy);

        // this one is preserved without simplification, not the usual function,
        // will accept a grid coverage or a collection, needs to be evaluated at runtime
        if (fts.getTransformation() != null) {
            copy.setTransformation(fts.getTransformation());
        }

        if (fts.getOnlineResource() != null) {
            copy.setOnlineResource(fts.getOnlineResource());
        }
        copy.getOptions().clear();
        copy.getOptions().putAll(fts.getOptions());

        if (STRICT && !copy.equals(fts)) {
            throw new IllegalStateException(
                    "Was unable to duplicate provided FeatureTypeStyle:" + fts);
        }

        pages.push(copy);
    }
}
