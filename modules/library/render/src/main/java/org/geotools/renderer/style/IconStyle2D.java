/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.Composite;
import java.awt.geom.AffineTransform;

import javax.swing.Icon;

/**
 * Represents a {@link Style2D} backed by an {@link Icon}
 * 
 * @author milton
 */
public class IconStyle2D extends Style2D {
    private static final AffineTransform IDENTITY_TRANSFORM = new AffineTransform();

    private Icon icon;

    private float rotation;

    private Composite composite;

    private float displacementX;

    private float displacementY;

    /**
     * Constructor.
     * 
     * @param renderer
     *            GlyphRenderer to be used for rendering this GlyphStyle2D
     * @param graphic
     *            Graphic for defining the glyph.
     * @param externalGraphic
     *            ExternalGraphic for defining the glyph.
     * @param The
     *            rotation of the icon
     * @param The
     *            opacity of the icon
     */
    public IconStyle2D(Icon icon, Object feature, float displacementX, float displacementY,
            float rotation, Composite composite) {
        this.icon = icon;
        this.rotation = rotation;
        this.composite = composite;
        this.displacementX = displacementX;
        this.displacementY = displacementY;
    }

    /**
     * The Icon rotation
     * @return
     */
    public float getRotation() {
        return rotation;
    }

    /**
     * The icon composite
     * @return
     */
    public Composite getComposite() {
        return composite;
    }

    /**
     * The icon x displacement
     * @return
     */
    public float getDisplacementX() {
        return displacementX;
    }

    /**
     * The icon y displacement
     * @return
     */
    public float getDisplacementY() {
        return displacementY;
    }

    /**
     * Returns the icon backing this style
     * 
     * @return
     */
    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void setComposite(Composite composite) {
        this.composite = composite;
    }

    public void setDisplacementX(float displacementX) {
        this.displacementX = displacementX;
    }

    public void setDisplacementY(float displacementY) {
        this.displacementY = displacementY;
    };
}
