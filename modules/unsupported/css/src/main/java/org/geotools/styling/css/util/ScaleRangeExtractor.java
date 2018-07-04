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

import org.geotools.styling.css.CssRule;
import org.geotools.styling.css.selector.AbstractSelectorVisitor;
import org.geotools.styling.css.selector.Or;
import org.geotools.styling.css.selector.ScaleRange;
import org.geotools.styling.css.selector.Selector;
import org.geotools.util.Range;

/**
 * Extracts the scale range from a {@link Selector}, throws an exception if the selector contains
 * more than one scale range in a OR
 *
 * @author Andrea Aime - GeoSolutions
 */
public class ScaleRangeExtractor extends AbstractSelectorVisitor {

    /**
     * Flags whether we are inside a OR or not. If a OR contains a range, we are going to reject the
     * extraction
     */
    private boolean insideOr;

    /** The extracted range */
    private Range<Double> range;

    public static Range<Double> getScaleRange(CssRule cssRule) {
        Selector selector = cssRule.getSelector();
        return getScaleRange(selector);
    }

    public static Range<Double> getScaleRange(Selector selector) {
        try {
            ScaleRangeExtractor extractor = new ScaleRangeExtractor();
            selector.accept(extractor);
            return extractor.range;
        } catch (IllegalStateException e) {
            throw new IllegalArgumentException("Failed to extract scale range from: " + selector);
        }
    }

    @Override
    public Object visit(Or or) {
        try {
            insideOr = true;
            super.visit(or);
        } finally {
            insideOr = false;
        }
        return null;
    }

    @Override
    public Object visit(ScaleRange scaleRange) {
        if (insideOr) {
            throw new IllegalStateException(
                    "Cannot translate to SLD when a scale range is used inside a OR in the selector");
        }
        if (range == null) {
            range = scaleRange.range;
        } else {
            range.intersect(scaleRange.range);
        }
        return null;
    }
}
