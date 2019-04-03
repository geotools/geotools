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

package org.geotools.render;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import org.geotools.renderer.style.MarkFactory;
import org.opengis.feature.Feature;
import org.opengis.filter.expression.Expression;

class SplatMarkFactory implements MarkFactory {

    private static GeneralPath SPLAT;

    static {
        SPLAT = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        SPLAT.moveTo(-0.2f, 0.9f);
        SPLAT.lineTo(0.266f, -0.5f);
        SPLAT.lineTo(-0.366f, -0.7f);
        SPLAT.lineTo(0.4f, 1.12f);
        SPLAT.lineTo(0.3f, 1.10f);
    }

    public Shape getShape(Graphics2D graphics, Expression symbolUrl, Feature feature)
            throws Exception {
        if (symbolUrl == null) {
            // cannot handle a null url
            return null;
        }
        // Evaluate the expression as a String
        String wellKnownName = symbolUrl.evaluate(feature, String.class);

        if (wellKnownName != null && wellKnownName.equalsIgnoreCase("splat")) {
            return SPLAT;
        }
        return null; // we do not know this one
    }
}
