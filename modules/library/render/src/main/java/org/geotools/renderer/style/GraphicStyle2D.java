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

import java.awt.image.BufferedImage;

/**
 * A style class used to depict a point, polygon centroid or line with a small
 * graphic icon
 * 
 * @author Andrea Aime
 *
 *
 * @source $URL$
 * @version $Id$
 */
public class GraphicStyle2D extends Style2D implements PointStyle2D {
	BufferedImage image;
	int border = 0;
	float rotation;
	float opacity;

    float displacementX;

    float displacementY;

    float anchorPointX = 0.5f;

    float anchorPointY = 0.5f;

	/**
	 * Creates a new GraphicStyle2D object.
	 * 
	 * @param image
	 *            The image that will be used to depict the centroid/point/...
	 * @param rotation
	 *            The image rotation
	 * @param opacity
	 *            The image opacity
	 */
	public GraphicStyle2D(BufferedImage image, float rotation, float opacity) {
		this.image = image;
		this.rotation = rotation;
		this.opacity = opacity;
	}
	

	public GraphicStyle2D(BufferedImage image, float rotation, float opacity, int border) {
		this.image = image;
		this.rotation = rotation;
		this.opacity = opacity;
		this.border = border;
	}

    public GraphicStyle2D(BufferedImage image, float opacity, int border) {
        this.image = image;
        this.opacity = opacity;
        this.border = border;
    }


	/**
     */
	public BufferedImage getImage() {
		return image;
	}

	/**
     */
	public float getOpacity() {
		return opacity;
	}

	/**
	 * The graphic rotation in radians.
	 * 
	 * @return graphic rotation in radians
         */
	public float getRotation() {
		return rotation;
	}

	/**
	 * @param image
	 */
	public void setImage(BufferedImage image) {
		this.image = image;
	}

	/**
	 * @param f
	 */
	public void setOpacity(float f) {
		opacity = f;
	}

	/**
	 * Graphic rotation in radians
	 * @param radians graphic rotation
	 */
	public void setRotation(float radians) {
		rotation = radians;
	}

	/**
	 * The actual image size might have been extended with an extra border
	 * (usually of one pixel) to preserve antialiasing pixels
	 * 
	 * @return
	 */
	public int getBorder() {
		return border;
	}

	public void setBorder(int border) {
		this.border = border;
	}

    public float getDisplacementX() {
        return displacementX;
    }

    public void setDisplacementX(float displacementX) {
        this.displacementX = displacementX;
    }

    public float getDisplacementY() {
        return displacementY;
    }

    public void setDisplacementY(float displacementY) {
        this.displacementY = displacementY;
    }

    public float getAnchorPointX() {
        return anchorPointX;
    }

    public void setAnchorPointX(float anchorPointX) {
        this.anchorPointX = anchorPointX;
    }

    public float getAnchorPointY() {
        return anchorPointY;
    }

    public void setAnchorPointY(float anchorPointY) {
        this.anchorPointY = anchorPointY;
    }

}
