/*
 *    GeoTools Sample code and Tutorials by Open Source Geospatial Foundation, and others
 *    https://docs.geotools.org
 *
 *    To the extent possible under law, the author(s) have dedicated all copyright
 *    and related and neighboring rights to this software to the public domain worldwide.
 *    This software is distributed without any warranty.
 *
 *    You should have received a copy of the CC0 Public Domain Dedication along with this
 *    software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package org.geotools.opengis;

import java.awt.Color;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.geotools.api.filter.FilterFactory2;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.style.AnchorPoint;
import org.geotools.api.style.Displacement;
import org.geotools.api.style.Fill;
import org.geotools.api.style.Graphic;
import org.geotools.api.style.GraphicalSymbol;
import org.geotools.api.style.PointSymbolizer;
import org.geotools.api.style.Stroke;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.metadata.iso.citation.OnLineResourceImpl;

public class StyleExamples {

    /**
     * This example is limited to just the gt-opengis style interfaces which are immutable once
     * created.
     */
    private void styleFactoryExample() throws Exception {
        // styleFactoryExample start
        //
        org.geotools.api.style.StyleFactory sf = CommonFactoryFinder.getStyleFactory();
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
