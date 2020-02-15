/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
 *
 */

package org.geotools.opengis;

import java.awt.Color;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.metadata.iso.citation.OnLineResourceImpl;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.style.AnchorPoint;
import org.opengis.style.Displacement;
import org.opengis.style.Fill;
import org.opengis.style.Graphic;
import org.opengis.style.GraphicalSymbol;
import org.opengis.style.PointSymbolizer;
import org.opengis.style.Stroke;

public class StyleExamples {

    /**
     * This example is limited to just the gt-opengis style interfaces which are immutable once
     * created.
     */
    private void styleFactoryExample() throws Exception {
        // styleFactoryExample start
        //
        org.opengis.style.StyleFactory sf = CommonFactoryFinder.getStyleFactory();
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

        //
        // create the graphical mark used to represent a city
        Stroke stroke = sf.stroke(ff.literal("#000000"), null, null, null, null, null, null);
        Fill fill = sf.fill(null, ff.literal(Color.BLUE), ff.literal(1.0));

        // OnLineResource implemented by gt-metadata - so no factory!
        OnLineResourceImpl svg = new OnLineResourceImpl(new URI("file:city.svg"));
        svg.freeze(); // freeze to prevent modification at runtime

        OnLineResourceImpl png = new OnLineResourceImpl(new URI("file:city.png"));
        png.freeze(); // freeze to prevent modification at runtime

        //
        // List of symbols is considered in order with the rendering engine choosing
        // the first one it can handle. Allowing for svg, png, mark order
        List<GraphicalSymbol> symbols = new ArrayList<>();
        symbols.add(sf.externalGraphic(svg, "svg", null)); // svg preferred
        symbols.add(sf.externalGraphic(png, "png", null)); // png preferred
        symbols.add(sf.mark(ff.literal("circle"), fill, stroke)); // simple circle backup plan

        Expression opacity = null; // use default
        Expression size = ff.literal(10);
        Expression rotation = null; // use default
        AnchorPoint anchor = null; // use default
        Displacement displacement = null; // use default

        // define a point symbolizer of a small circle
        Graphic circle = sf.graphic(symbols, opacity, size, rotation, anchor, displacement);
        PointSymbolizer pointSymbolizer =
                sf.pointSymbolizer("point", ff.property("the_geom"), null, null, circle);
        // styleFactoryExample end
    }
}
