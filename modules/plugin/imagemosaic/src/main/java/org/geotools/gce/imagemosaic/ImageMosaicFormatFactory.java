/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic;

import java.awt.RenderingHints;
import java.util.Collections;
import java.util.Map;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFactorySpi;

/**
 * Implementation of the GridCoverageFormat service provider interface for mosaic of georeferenced images.
 *
 * @author Simone Giannecchini, GeoSolutions S.A.S.
 * @since 2.3
 */
public final class ImageMosaicFormatFactory implements GridFormatFactorySpi {

    /** @see GridFormatFactorySpi#createFormat(). */
    @Override
    public AbstractGridFormat createFormat() {
        return new ImageMosaicFormat();
    }

    /**
     * Returns the implementation hints. The default implementation returns an empty map.
     *
     * @return An empty map.
     */
    @Override
    public Map<RenderingHints.Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }

    /**
     * Tells me if this plugin will work on not given the actual installation.
     *
     * <p>Dependecies are mostly from ImageN and ImageIO so if they are installed you should not have many problems.
     *
     * @return False if something's missing, true otherwise.
     */
    @Override
    public boolean isAvailable() {
        boolean available = true;

        // if these classes are here, then the runtine environment has
        // access to ImageN and the ImageN ImageI/O toolbox.
        try {
            Class.forName("org.eclipse.imagen.ImageN");
            Class.forName("org.eclipse.imagen.media.imageread.ImageReadDescriptor");
        } catch (ClassNotFoundException cnf) {
            available = false;
        }

        return available;
    }
}
