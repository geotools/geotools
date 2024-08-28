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
import org.geotools.styling.css.selector.ZoomRange;
import org.geotools.styling.zoom.WellKnownZoomContextFinder;
import org.geotools.styling.zoom.ZoomContext;
import org.geotools.util.Range;

/**
 * Extracts the scale range from a {@link Selector}, throws an exception if the selector contains
 * more than one scale range in a OR
 *
 * @author Andrea Aime - GeoSolutions
 */
public class ScaleRangeExtractor extends AbstractSelectorVisitor {

    public static final ZoomContext WEB_MERCATOR_ZOOM_CONTEXT =
            WellKnownZoomContextFinder.getInstance().get("WebMercatorQuad");

    /**
     * Flags whether we are inside a OR or not. If a OR contains a range, we are going to reject the
     * extraction
     */
    private boolean insideOr;

    /** The extracted range */
    private Range<Double> range;

    private ZoomContext zoomContext = WEB_MERCATOR_ZOOM_CONTEXT;

    public static Range<Double> getScaleRange(CssRule cssRule) {
        Selector selector = cssRule.getSelector();
        return getScaleRange(selector, WEB_MERCATOR_ZOOM_CONTEXT);
    }

    public static Range<Double> getScaleRange(CssRule cssRule, ZoomContext zoomContext) {
        Selector selector = cssRule.getSelector();
        return getScaleRange(selector, zoomContext);
    }

    public static Range<Double> getScaleRange(Selector selector) {
        return getScaleRange(selector, WEB_MERCATOR_ZOOM_CONTEXT);
    }

    public static Range<Double> getScaleRange(Selector selector, ZoomContext zoomContext) {
        try {
            ScaleRangeExtractor extractor = new ScaleRangeExtractor(zoomContext);
            selector.accept(extractor);
            return extractor.range;
        } catch (IllegalStateException e) {
            throw new IllegalArgumentException("Failed to extract scale range from: " + selector);
        }
    }

    public ScaleRangeExtractor() {}

    public ScaleRangeExtractor(ZoomContext zoomContext) {
        this.zoomContext = zoomContext;
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

    @Override
    public Object visit(ZoomRange zoomRange) {
        if (insideOr) {
            throw new IllegalStateException(
                    "Cannot translate to SLD when a scale range is used inside a OR in the selector");
        }

        // convert between the expectactions of ZoomRange and ZoomContext:
        // - ZoomRange does not use nulls for non bounded ranges, ZoomContext does
        // - ZoomRange can have bounds included or not, ZoomContext assumes always included
        Integer min = zoomRange.range.getMinValue();
        Integer max = zoomRange.range.getMaxValue();
        if (min == 0) {
            min = null;
        } else if (!zoomRange.range.isMinIncluded()) {
            min++;
        }
        if (max == Integer.MAX_VALUE) {
            max = null;
        } else if (!zoomRange.range.isMaxIncluded()) {
            max--;
        }

        org.geotools.styling.zoom.ScaleRange scaleRange = zoomContext.getRange(min, max);
        Range<Double> localRange =
                new Range<>(Double.class, scaleRange.getMinDenom(), scaleRange.getMaxDenom());
        if (range == null) {
            range = localRange;
        } else {
            range.intersect(localRange);
        }
        return null;
    }
}
