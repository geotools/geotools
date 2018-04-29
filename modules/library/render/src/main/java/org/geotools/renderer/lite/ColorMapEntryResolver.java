/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.filter.visitor.SimplifyingFilterVisitor;
import org.geotools.renderer.style.ExpressionExtractor;
import org.geotools.styling.ColorMapEntry;
import org.geotools.styling.visitor.DuplicatingStyleVisitor;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;

/**
 * Simple support class created with the intention of expanding env function in raster symbolizer
 * before passing it to another thread for rendering
 *
 * @author Andrea Aime - GeoSolutions
 */
class ColorMapEntryResolver extends DuplicatingStyleVisitor {

    SimplifyingFilterVisitor simplifier = new SimplifyingFilterVisitor();

    protected Expression copyCqlExpression(Expression expression) {
        if (expression == null) return null;
        Expression simplified = null;
        if (expression instanceof Literal) {
            String value = expression.evaluate(null, String.class);
            if (value != null && value.startsWith("${")) ;
            expression = ExpressionExtractor.extractCqlExpressions(value);
        }
        simplified = (Expression) expression.accept(simplifier, ff);
        return simplified;
    }

    public void visit(ColorMapEntry colorMapEntry) {
        ColorMapEntry copy = sf.createColorMapEntry();
        copy.setColor(copyCqlExpression(colorMapEntry.getColor()));
        copy.setLabel(colorMapEntry.getLabel());
        copy.setOpacity(copyCqlExpression(colorMapEntry.getOpacity()));
        copy.setQuantity(copyCqlExpression(colorMapEntry.getQuantity()));

        if (STRICT && !copy.equals(colorMapEntry)) {
            throw new IllegalStateException(
                    "Was unable to duplicate provided ColorMapEntry:" + colorMapEntry);
        }
        pages.push(copy);
    }
}
