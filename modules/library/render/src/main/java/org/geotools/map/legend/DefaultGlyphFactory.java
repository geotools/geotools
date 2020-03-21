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
package org.geotools.map.legend;

import java.awt.*;
import javax.swing.*;
import org.geotools.map.Layer;
import org.geotools.styling.Rule;
import org.geotools.styling.SLD;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;

public class DefaultGlyphFactory implements GlyphFactory {

    /**
     * Glyph for the provided layer.
     *
     * <p>At a minimum the icon will be based on:
     *
     * <ul>
     *   <li>layer schema, will be considered a generic geometry if not recognized
     *   <li>layer style, defaults will be used if not recognized
     * </ul>
     *
     * @return Icon For the provided layer
     */
    public Icon icon(Layer layer) {
        if (layer == null || layer.getFeatureSource() == null) {
            return geometry(null, null);
        }
        FeatureType schema = layer.getFeatureSource().getSchema();

        if ("GridCoverage".equals(schema.getName().getLocalPart())) {
            return grid(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        }
        Rule rule = SLD.rules(layer.getStyle())[0];

        Class<?> binding = schema.getBinding();
        if (isPolygon(binding)) {
            return polygon(rule);
        } else if (isLine(binding)) {
            return line(rule);
        } else if (isPoint(binding)) {
            return point(rule);
        } else {
            return geometry(rule);
        }
    }

    private boolean isPolygon(Class<?> type) {
        return type == Polygon.class || type == MultiPolygon.class;
    }

    private boolean isPoint(Class<?> type) {
        return true;
    }

    private boolean isLine(Class<?> type) {
        return true;
    }

    public Icon polygon(Rule rule) {
        // TODO Auto-generated method stub
        return null;
    }

    public Icon geometry(Color color, Color fill) {
        // TODO Auto-generated method stub
        return null;
    }

    public Icon geometry(Rule rule) {
        // TODO Auto-generated method stub
        return null;
    }

    public Icon grid(Color color1, Color color2, Color color3, Color color4) {
        // TODO Auto-generated method stub
        return null;
    }

    public Icon icon(SimpleFeatureType schema) {
        // TODO Auto-generated method stub
        return null;
    }

    public Icon line(Color line, int width) {
        // TODO Auto-generated method stub
        return null;
    }

    public Icon line(Rule rule) {
        // TODO Auto-generated method stub
        return null;
    }

    public Icon palette(Color[] colors) {
        // TODO Auto-generated method stub
        return null;
    }

    public Icon point(Color point, Color fill) {
        // TODO Auto-generated method stub
        return null;
    }

    public Icon point(Rule rule) {
        // TODO Auto-generated method stub
        return null;
    }

    public Icon polygon(Color color, Color fill, int width) {
        // TODO Auto-generated method stub
        return null;
    }

    public Icon swatch(Color color) {
        // TODO Auto-generated method stub
        return null;
    }
}
