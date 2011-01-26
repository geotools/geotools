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


/**
 * The policy for {@link MosaicImageReader#getImageTypes computing image types} in a mosaic.
 *
 * @since 2.5
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 *
 * @see MosaicImageReadParam#getImageTypePolicy
 * @see MosaicImageReader#getDefaultImageTypePolicy
 */
public enum ImageTypePolicy {
    /**
     * Returns {@linkplain javax.imageio.ImageTypeSpecifier type specifiers} that are supported
     * by every tiles. This is the most robust policy, but also the most expensive. This policy
     * should be selected when the tiles may be stored in heterogeneous formats.
     */
    SUPPORTED_BY_ALL(true),

    /**
     * Returns {@linkplain javax.imageio.ImageTypeSpecifier type specifiers} that are supported
     * by one tile, selected arbitrary. This policy is appropriate when the tiles are stored in
     * a homogeneous format.
     * <p>
     * When Java assertions are enabled, {@linkplain MosaicImageReader} will ensures that this
     * policy produces the same result than the {@link #SUPPORTED_BY_ALL} policy.
     */
    SUPPORTED_BY_ONE(true),

    /**
     * Returns a single {@linkplain javax.imageio.ImageTypeSpecifier type specifier} for images
     * of {@link java.awt.image.BufferedImage#TYPE_INT_ARGB TYPE_INT_ARGB}. This policy should
     * be used only when tiles are known in advance to be compatible with the ARGB model, and
     * this model is wanted.
     */
    ALWAYS_ARGB(false);

    /**
     * {@code true} if reading a single tile with this policy can be delegated directly to the
     * underlying image reader as an optimization.
     */
    final boolean canDelegate;

    /**
     * Creates a new enum.
     */
    private ImageTypePolicy(final boolean canDelegate) {
        this.canDelegate = canDelegate;
    }
}
