/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.io.mosaic;

import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import org.geotools.referencing.operation.matrix.XAffineTransform;


/**
 * An affine transform which is translated relative to an original transform.
 * The translation terms are memorized. This class if for internal use by
 * {@link RegionCalculator} only.
 *
 * @since 2.6
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
final class TranslatedTransform {
    /**
     * The translated "grid to real world" transform, as an immutable instance.
     */
    private final AffineTransform gridToCRS;

    /**
     * The translation in "absolute units". This is the same units
     * than for tiles at subsampling (1,1).
     */
    private final int dx, dy;

    /**
     * Creates a new translated transform. The translation is specified in "absolute units",
     * i.e. in the same units than for tiles at subsampling (1,1).
     *
     * @param subsampling The {@linkplain Tile#getSubsampling tile subsampling}.
     * @param reference The "grid to real world" transform at subsampling (1,1).
     * @param dx The translation along <var>x</var> axis in "absolute units".
     * @param dy The translation along <var>y</var> axis in "absolute units".
     */
    TranslatedTransform(final Dimension subsampling, AffineTransform reference, int dx, int dy) {
        this.dx = dx / subsampling.width;  // It is okay to round toward zero.
        this.dy = dy / subsampling.height;
        dx %= subsampling.width;
        dy %= subsampling.height;
        reference = new AffineTransform(reference);
        reference.scale(subsampling.width, subsampling.height);
        reference.translate(dx, dy); // Correction for non-integer division of (dx,dy).
        gridToCRS = new XAffineTransform(reference);
    }

    /**
     * Applies the translation and the new "grid to CRS" transform on the given tile.
     *
     * @param tile The tile on which to apply the translation.
     */
    final void applyTo(final Tile tile) {
        tile.translate(dx, dy);
        tile.setGridToCRS(gridToCRS);
    }
}
