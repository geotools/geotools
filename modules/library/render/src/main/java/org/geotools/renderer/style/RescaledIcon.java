/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

import javax.swing.Icon;

/**
 * An icon that paints a delegate icon at a specified scale
 * 
 * @author Andrea Aime - OpenGeo
 * 
 */
class RescaledIcon implements Icon {
    double scale;

    Icon icon;

    /**
     * Builds a new rescaled icon
     * 
     * @param icon
     *            The icon to be rescaled
     * @param scale
     *            The scale factory (shrinks the icon between 0 and 1, enlarges it above 1)
     */
    public RescaledIcon(Icon icon, double scale) {
        this.icon = icon;
        this.scale = scale;
    }

    public int getIconHeight() {
        return (int) Math.round(icon.getIconHeight() * scale);
    }

    public int getIconWidth() {
        return (int) Math.round(icon.getIconWidth() * scale);
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform tmp = g2d.getTransform();
        Object oldInterpolation = g2d.getRenderingHint(RenderingHints.KEY_INTERPOLATION);
        if (oldInterpolation == null) {
            oldInterpolation = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
        }
        try {
            AffineTransform at = new AffineTransform(tmp);
            at.translate(x, y);
            at.scale(scale, scale);
            g2d.setTransform(at);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            icon.paintIcon(c, g2d, 0, 0);
        } finally {
            g2d.setTransform(tmp);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, oldInterpolation);
        }
    }

}
