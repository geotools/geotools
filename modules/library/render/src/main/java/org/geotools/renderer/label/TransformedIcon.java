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
package org.geotools.renderer.label;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import javax.swing.Icon;

/**
 * Applies a generic transformation on top of an icon
 * 
 * @author Andrea Aime - OpenGeo
 * 
 */
class TransformedIcon implements Icon {
    AffineTransform at;

    Icon icon;

    int width;

    int height;

    /**
     * Builds a new transformed icon
     * 
     * @param icon
     *            The icon to be rescaled
     * @param transformation
     *            The icon transformation
     */
    public TransformedIcon(Icon icon, AffineTransform at) {
        this.icon = icon;
        this.at = at;
        Rectangle2D bounds = new Rectangle2D.Double(0, 0, icon.getIconWidth(), icon.getIconHeight());
        bounds = at.createTransformedShape(bounds).getBounds2D();
        this.width = (int) Math.round(bounds.getWidth());
        this.height = (int) Math.round(bounds.getHeight());
    }

    public int getIconHeight() {
        return height;
    }

    public int getIconWidth() {
        return width;
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
            at.concatenate(this.at);
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
