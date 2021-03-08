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

import java.awt.Composite;
import java.awt.image.BufferedImage;

/**
 * A style class used to depict a point, polygon centroid or line with a small graphic icon
 *
 * @author Andrea Aime
 * @version $Id$
 */
public class GraphicStyle2D extends Style2D implements PointStyle2D {
    BufferedImage image;
    int border = 0;
    float rotation;

    float displacementX;

    float displacementY;

    float anchorPointX = 0.5f;

    float anchorPointY = 0.5f;

    Composite composite;

    /**
     * Creates a new GraphicStyle2D object.
     *
     * @param image The image that will be used to depict the centroid/point/...
     * @param rotation The image rotation
     */
    public GraphicStyle2D(BufferedImage image, float rotation) {
        this.image = image;
        this.rotation = rotation;
    }

    public GraphicStyle2D(BufferedImage image, float rotation, int border) {
        this.image = image;
        this.rotation = rotation;
        this.border = border;
    }

    public GraphicStyle2D(BufferedImage image, int border) {
        this.image = image;
        this.border = border;
    }

    /** */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * The graphic rotation in radians.
     *
     * @return graphic rotation in radians
     */
    @Override
    public float getRotation() {
        return rotation;
    }

    /** @param image */
    public void setImage(BufferedImage image) {
        this.image = image;
    }

    /**
     * Graphic rotation in radians
     *
     * @param radians graphic rotation
     */
    @Override
    public void setRotation(float radians) {
        rotation = radians;
    }

    /**
     * The actual image size might have been extended with an extra border (usually of one pixel) to
     * preserve antialiasing pixels
     */
    public int getBorder() {
        return border;
    }

    public void setBorder(int border) {
        this.border = border;
    }

    @Override
    public float getDisplacementX() {
        return displacementX;
    }

    @Override
    public void setDisplacementX(float displacementX) {
        this.displacementX = displacementX;
    }

    @Override
    public float getDisplacementY() {
        return displacementY;
    }

    @Override
    public void setDisplacementY(float displacementY) {
        this.displacementY = displacementY;
    }

    @Override
    public float getAnchorPointX() {
        return anchorPointX;
    }

    @Override
    public void setAnchorPointX(float anchorPointX) {
        this.anchorPointX = anchorPointX;
    }

    @Override
    public float getAnchorPointY() {
        return anchorPointY;
    }

    @Override
    public void setAnchorPointY(float anchorPointY) {
        this.anchorPointY = anchorPointY;
    }

    @Override
    public Composite getComposite() {
        return composite;
    }

    @Override
    public void setComposite(Composite composite) {
        this.composite = composite;
    }
}
