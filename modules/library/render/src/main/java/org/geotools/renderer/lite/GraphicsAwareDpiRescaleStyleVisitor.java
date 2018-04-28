/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.renderer.style.GraphicStyle2D;
import org.geotools.renderer.style.IconStyle2D;
import org.geotools.renderer.style.MarkStyle2D;
import org.geotools.renderer.style.SLDStyleFactory;
import org.geotools.renderer.style.Style2D;
import org.geotools.styling.Graphic;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.visitor.DpiRescaleStyleVisitor;
import org.geotools.util.Range;
import org.opengis.filter.expression.Expression;

/**
 * This class extends {@link DpiRescaleStyleVisitor} to add support for rescaling external graphics
 * and marks whose size has not been explicitly set.
 *
 * <p>Works properly as long as the expression in an eventual dynamic symbolizer are not setting the
 * size of the symbol, as we don't have the feature here, and there is no way to know which bit of
 * the url will setup the size
 *
 * @author Andrea Aime - GeoSolutions
 */
public class GraphicsAwareDpiRescaleStyleVisitor extends DpiRescaleStyleVisitor {

    static final StyleFactory sf = CommonFactoryFinder.getStyleFactory();
    static final Range<Double> INFINITE_RANGE =
            new Range<Double>(Double.class, 0d, Double.POSITIVE_INFINITY);
    SLDStyleFactory ssf = new SLDStyleFactory();

    public GraphicsAwareDpiRescaleStyleVisitor(double scale) {
        super(scale);
    }

    @Override
    protected Expression rescaleGraphicSize(Graphic gr) {
        Expression size = gr.getSize();
        if (size == null || size == Expression.NIL) {
            PointSymbolizer symbolizer = sf.createPointSymbolizer(gr, null);
            Style2D style = ssf.createStyle(null, symbolizer, INFINITE_RANGE);
            if (style instanceof IconStyle2D) {
                IconStyle2D is = (IconStyle2D) style;
                size = ff.literal(is.getIcon().getIconHeight());
            } else if (style instanceof GraphicStyle2D) {
                GraphicStyle2D gs = (GraphicStyle2D) style;
                size = ff.literal(gs.getImage().getHeight());
            } else if (style instanceof MarkStyle2D) {
                MarkStyle2D ms = (MarkStyle2D) style;
                size = ff.literal(ms.getSize());
            }
        }

        return rescale(size);
    }
}
