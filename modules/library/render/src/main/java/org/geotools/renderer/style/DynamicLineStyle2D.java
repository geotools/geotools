/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.style;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Paint;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.style.LineSymbolizer;
import org.geotools.api.style.Stroke;

/**
 * A dynamic line style, that will compute its parameters each time they are requested instead of caching them
 *
 * @author jamesm
 */
public class DynamicLineStyle2D extends org.geotools.renderer.style.LineStyle2D {
    /** The feature that will be styled as a polygon */
    protected SimpleFeature feature;

    /** The line symbolizer used to get stroke/composite/... */
    protected LineSymbolizer ls;

    /** Creates a new instance of DynamicLineStyle2D */
    public DynamicLineStyle2D(SimpleFeature feature, LineSymbolizer sym) {
        this.feature = feature;
        ls = sym;
    }

    /** Computes and returns the stroke */
    @Override
    public java.awt.Stroke getStroke() {
        Stroke stroke = ls.getStroke();

        if (stroke == null) {
            return null;
        }

        // resolve join type into a join code

        String joinType = evaluateExpression(stroke.getLineJoin(), feature, "miter");

        int joinCode = SLDStyleFactory.lookUpJoin(joinType);

        // resolve cap type into a cap code

        String capType = evaluateExpression(stroke.getLineCap(), feature, "square");
        int capCode = SLDStyleFactory.lookUpCap(capType);

        // get the other properties needed for the stroke
        float[] dashes = SLDStyleFactory.evaluateDashArray(stroke, feature);
        float width = stroke.getWidth().evaluate(feature, Float.class).floatValue();
        float dashOffset = stroke.getDashOffset().evaluate(feature, Float.class).floatValue();

        // Simple optimization: let java2d use the fast drawing path if the line width
        // is small enough...
        if (width <= 1) {
            width = 0;
        }

        // now set up the stroke
        BasicStroke stroke2d;

        if (dashes != null && dashes.length > 0) {
            stroke2d = new BasicStroke(width, capCode, joinCode, 1, dashes, dashOffset);
        } else {
            stroke2d = new BasicStroke(width, capCode, joinCode, 1);
        }

        return stroke2d;
    }

    @Override
    public double getPerpendicularOffset() {
        if (ls.getPerpendicularOffset() == null) {
            return 0d;
        }
        Double offset = ls.getPerpendicularOffset().evaluate(feature, Double.class);
        if (offset == null) {
            return 0d;
        } else {
            return offset;
        }
    }

    /** Computes and returns the contour style */
    @Override
    public java.awt.Composite getContourComposite() {
        Stroke stroke = ls.getStroke();

        if (stroke == null) {
            return null;
        }

        float opacity = stroke.getOpacity().evaluate(feature, Float.class).floatValue();
        Composite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);

        return composite;
    }

    /**
     * Returns the contour paint
     *
     * @return the contour paint
     */
    @Override
    public java.awt.Paint getContour() {
        Stroke stroke = ls.getStroke();

        if (stroke == null) {
            return null;
        }

        // the foreground color
        Paint contourPaint = stroke.getColor().evaluate(feature, Color.class);
        if (contourPaint == null) {
            String text = stroke.getColor().evaluate(feature, String.class);
            if (text != null) {
                contourPaint = Color.decode(text);
            }
        }

        // if a graphic fill is to be used, prepare the paint accordingly....
        org.geotools.api.style.Graphic gr = stroke.getGraphicFill();
        SLDStyleFactory fac = new SLDStyleFactory();

        if (gr != null) {
            contourPaint = fac.getTexturePaint(gr, feature, ls);
        }

        return contourPaint;
    }

    /**
     * Evaluates an expression over the passed feature, if the expression or the result is null, the default value will
     * be returned
     */
    private String evaluateExpression(Expression e, SimpleFeature feature, String defaultValue) {
        String result = defaultValue;

        if (e != null) {
            result = e.evaluate(feature, defaultValue.getClass());

            if (result == null) {
                result = defaultValue;
            }
        }

        return result;
    }
}
