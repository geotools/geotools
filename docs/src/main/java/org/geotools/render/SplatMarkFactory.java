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
package org.geotools.render;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import org.geotools.renderer.style.MarkFactory;
import org.geotools.api.feature.Feature;
import org.geotools.api.filter.expression.Expression;

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
