/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2010, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.demo.grid;

import java.awt.Color;
import java.util.Map;

import com.vividsolutions.jts.geom.Polygon;

import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.grid.GridFeatureBuilder;
import org.geotools.grid.GridElement;
import org.geotools.grid.Grids;
import org.geotools.map.DefaultMapContext;
import org.geotools.styling.Fill;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.SLD;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.swing.JMapFrame;

import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory2;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Demonstrates creating a vector grid of square elements and setting
 * the value of an attribute for each grid element based on its position.
 *
 * @author mbedward
 */
public class SquareExample {

    public static void main(String[] args) {
        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        typeBuilder.setName("oblongs");
        typeBuilder.add("oblong", Polygon.class, (CoordinateReferenceSystem)null);
        typeBuilder.add("color", Color.class);
        final SimpleFeatureType TYPE = typeBuilder.buildFeatureType();

        final ReferencedEnvelope bounds = new ReferencedEnvelope(0, 100, 0, 100, null);

        GridFeatureBuilder builder = new GridFeatureBuilder(TYPE) {
            public void setAttributes(GridElement el, Map<String, Object> attributes) {
                int g = (int) (255 * el.getCenter().x / bounds.getWidth());
                int b = (int) (255 * el.getCenter().y / bounds.getHeight());
                attributes.put("color", new Color(0, g, b));
            }
        };

        final double sideLen = 10.0;
        SimpleFeatureSource grid = Grids.createSquareGrid(bounds, sideLen, -1, builder);

        DefaultMapContext map = new DefaultMapContext();
        map.addLayer(grid, createStyle("color"));
        JMapFrame.showMap(map);
    }

    private static Style createStyle(String propName) {
        FilterFactory2 ff2 = CommonFactoryFinder.getFilterFactory2(null);
        StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);

        Stroke stroke = sf.createStroke(ff2.literal(Color.BLACK), ff2.literal(1.0));
        Fill fill = sf.createFill(ff2.property(propName));
        PolygonSymbolizer sym = sf.createPolygonSymbolizer(stroke, fill, null);
        return SLD.wrapSymbolizers(sym);
    }
}
