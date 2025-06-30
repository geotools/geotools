/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.util.List;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.VolatileFunction;
import org.geotools.api.style.Font;
import org.geotools.api.style.StyleVisitor;
import org.geotools.api.style.TextSymbolizer;

/**
 * A simple visitor whose purpose is to extract the set of attributes used by a Style, that is, those that the Style
 * expects to find in order to work properly
 *
 * <p>This is very similiar to StyleAttributeExtractor, but with these differences: a) it doesnt the count the
 * <PropertyName> tag in the <Geometry> b) it doesnt count anything in the <TextSymbolizer>'s <Priority> tag c) it
 * doesnt count anything in the <TextSymbolizer>'s <Label> tag
 *
 * <p>The reasons for this are because these fields are ALWAYS taken directly from the feature, not from the style.
 *
 * <p>So, for making queries (knowing any property that might be possibily be used in the SLD), use
 * StyleAttributeExtractor. If you want to know what a symbolizer actually needs to cache, then use this
 * (StyleAttributeExtractorTruncated).
 *
 * @author dblasby
 */
public class StyleAttributeExtractorTruncated extends StyleAttributeExtractor implements StyleVisitor {

    boolean usingVolatileFunctions = false;

    public StyleAttributeExtractorTruncated() {
        setSymbolizerGeometriesVisitEnabled(false);
    }

    @Override
    public void clear() {
        super.clear();
        usingVolatileFunctions = false;
    }

    public boolean isUsingVolatileFunctions() {
        return usingVolatileFunctions;
    }

    @Override
    public Object visit(org.geotools.api.filter.expression.Function expression, Object data) {
        usingVolatileFunctions |= expression instanceof VolatileFunction;
        return super.visit(expression, data);
    }
    /** @see StyleVisitor#visit(org.geotools.api.style.TextSymbolizer) */
    @Override
    public void visit(TextSymbolizer text) {

        if (text instanceof TextSymbolizer) {
            if (text.getGraphic() != null) text.getGraphic().accept(this);
        }

        if (text.getFill() != null) {
            text.getFill().accept(this);
        }

        if (text.getHalo() != null) {
            text.getHalo().accept(this);
        }

        if (text.fonts() != null) {
            List<Font> fonts = text.fonts();
            for (Font font : fonts) {
                if (font.getFamily() != null) {
                    for (Expression family : font.getFamily()) {
                        family.accept(this, null);
                    }
                }
                if (font.getSize() != null) {
                    font.getSize().accept(this, null);
                }
                if (font.getStyle() != null) {
                    font.getStyle().accept(this, null);
                }
                if (font.getWeight() != null) {
                    font.getWeight().accept(this, null);
                }
            }
        }

        if (text.getHalo() != null) {
            text.getHalo().accept(this);
        }

        if (text.getLabelPlacement() != null) {
            text.getLabelPlacement().accept(this);
        }
    }
}
