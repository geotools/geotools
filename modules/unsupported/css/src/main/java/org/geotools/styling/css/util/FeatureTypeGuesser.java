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

import org.geotools.filter.visitor.SimplifyingFilterVisitor;
import org.geotools.styling.css.CssRule;
import org.geotools.styling.css.selector.AbstractSelectorVisitor;
import org.geotools.styling.css.selector.Data;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Guesses the structure of the feature type given the list of rules. Mostly used so that we can
 * drive range based simplification in the {@link SimplifyingFilterVisitor} without knowing what the
 * target feature type is (which comes in handy if we really don't have it, e.g., when doing a blind
 * CSS to SLD translation)
 *
 * @author Andrea Aime - GeoSolutions
 */
public class FeatureTypeGuesser {

    TypeAggregator aggregator = new TypeAggregator();

    /**
     * Parses a rule selector structure and takes it into account when building the final feature
     * type
     */
    public void addRule(CssRule rule) {
        // apply duck typing on the selector
        if (rule.getSelector() != null) {
            rule.getSelector()
                    .accept(
                            new AbstractSelectorVisitor() {
                                @Override
                                public Object visit(Data data) {
                                    if (data.filter != null) {
                                        data.filter.accept(new FilterTypeVisitor(aggregator), null);
                                    }
                                    return null;
                                }
                            });
        }

        // maybe we could apply duck typing to the
        // property values as well... but the whole reason of the exercise here
        // is to find a type for range based comparisons, so checking the
        // filters is probably good enough
    }

    public SimpleFeatureType getFeatureType() {
        return aggregator.getFeatureType();
    }
}
