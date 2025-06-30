/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling.visitor;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.style.FeatureTypeStyle;
import org.geotools.api.style.Rule;
import org.geotools.util.Converters;
import org.geotools.util.logging.Logging;

/**
 * * This visitor applies the max and min scale denominators from the FeatureTypeStyle options to the copied Rules. It
 * is used to ensure that the scale denominators are correctly set from the vendor options to the children Rules.
 *
 * <p>It reads the options {@link FeatureTypeStyle#MAX_SCALE_DENOMINATOR} and
 * {@link FeatureTypeStyle#MIN_SCALE_DENOMINATOR} from the current FeatureTypeStyle being visited.
 */
public class FeatureStyleScaleStyleVisitor extends DuplicatingStyleVisitor {

    private static final Logger LOGGER = Logging.getLogger(FeatureStyleScaleStyleVisitor.class);

    private FeatureTypeStyle currentFeatureTypeStyle;

    public FeatureStyleScaleStyleVisitor() {
        super();
    }

    @Override
    public void visit(FeatureTypeStyle fts) {
        this.currentFeatureTypeStyle = fts;
        super.visit(fts);
    }

    @Override
    public void visit(Rule rule) {
        super.visit(rule);
        Rule copy = (Rule) pages.peek();
        if (copy.getMaxScaleDenominator() == Double.POSITIVE_INFINITY) {
            copy.setMaxScaleDenominator(getMaxScaleDenominator());
        }
        if (copy.getMinScaleDenominator() == 0d) {
            copy.setMinScaleDenominator(getMinScaleDenominator());
        }
    }

    private Double getScaleDenominator(String optionKey, double defaultValue) {
        if (currentFeatureTypeStyle != null) {
            String sdStr = currentFeatureTypeStyle.getOptions().get(optionKey);
            Double scaleDenominator = Converters.convert(sdStr, Double.class);
            if (scaleDenominator != null) {
                return scaleDenominator;
            } else if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, optionKey + " value not valid in FeatureTypeStyle options: {0}", sdStr);
            }
        }
        return defaultValue;
    }

    private Double getMaxScaleDenominator() {
        return getScaleDenominator(FeatureTypeStyle.MAX_SCALE_DENOMINATOR, Double.POSITIVE_INFINITY);
    }

    private Double getMinScaleDenominator() {
        return getScaleDenominator(FeatureTypeStyle.MIN_SCALE_DENOMINATOR, 0d);
    }
}
