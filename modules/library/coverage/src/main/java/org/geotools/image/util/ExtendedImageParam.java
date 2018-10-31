/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.util;

import javax.imageio.ImageReadParam;

/**
 * Utility class that should be used to pass extra parameters to the NetCdf image reader machinery.
 *
 * @deprecated use {@link it.geosolutions.imageio.imageioimpl.EnhancedImageReadParam}
 */
@Deprecated
public class ExtendedImageParam extends ImageReadParam {

    // the bands parameter define the order and which bands should be returned
    private int[] bands;

    public int[] getBands() {
        return bands;
    }

    public void setBands(int[] bands) {
        this.bands = bands;
    }
}
