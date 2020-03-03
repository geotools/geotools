/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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

/**
 * A point style can be used to depict a point, or can be repeated along a line or inside a fill.
 * Point styles are implemented in rather different ways, shapes, icons, static images, this
 * interface collects the traits shared by all
 *
 * @author Andrea Aime - GeoSolutions
 */
public interface PointStyle2D {

    /** The point symbol rotation, in radians. */
    public float getRotation();

    /** The point symbol x displacement */
    public float getDisplacementX();

    /** The point symbol y displacement */
    public float getDisplacementY();

    /** The point symbol x anchor, between 0 and 1, defaults to 0.5 */
    public float getAnchorPointX();

    /** The point symbol y anchor, between 0 and 1, defaults to 0.5 */
    public float getAnchorPointY();

    /** Sets the point symbol rotation, in radians. */
    public void setRotation(float rotation);

    /** Sets the point symbol x displacement */
    public void setDisplacementX(float displacementX);

    /** Sets the point symbol y displacement */
    public void setDisplacementY(float displacementY);

    /** Sets the point symbol x anchor, between 0 and 1 */
    public void setAnchorPointX(float anchorPointX);

    /** Sets the point symbol y anchor, between 0 and 1 */
    public void setAnchorPointY(float anchorPointY);

    /** Returns the composite for this point symbol */
    public Composite getComposite();

    /** Sets the composite for this point symbol */
    public void setComposite(Composite composite);
}
