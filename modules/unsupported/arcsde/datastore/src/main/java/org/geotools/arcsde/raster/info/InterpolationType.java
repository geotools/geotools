/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.arcsde.raster.info;

import com.esri.sde.sdk.client.SeRaster;
import java.util.NoSuchElementException;

/**
 * An enumeration that mirrors the different possible raster interpolation types in Arcsde (ie,
 * {@code SeRaster#SE_INTERPOLATION_*})
 *
 * @author Gabriel Roldan (OpenGeo)
 * @since 2.5.4
 */
public enum InterpolationType {
    INTERPOLATION_BICUBIC,
    INTERPOLATION_BILINEAR,
    INTERPOLATION_NEAREST,
    INTERPOLATION_NONE;

    static {
        INTERPOLATION_BICUBIC.setSdeTypeId(SeRaster.SE_INTERPOLATION_BICUBIC);
        INTERPOLATION_BILINEAR.setSdeTypeId(SeRaster.SE_INTERPOLATION_BILINEAR);
        INTERPOLATION_NEAREST.setSdeTypeId(SeRaster.SE_INTERPOLATION_NEAREST);
        INTERPOLATION_NONE.setSdeTypeId(SeRaster.SE_INTERPOLATION_NONE);
    }

    private int typeId;

    private void setSdeTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getSeInterpolationType() {
        return this.typeId;
    }

    public static InterpolationType valueOf(final int seInterpolationType) {
        for (InterpolationType type : InterpolationType.values()) {
            if (type.getSeInterpolationType() == seInterpolationType) {
                return type;
            }
        }
        throw new NoSuchElementException(
                "Interpolation type " + seInterpolationType + " does not exist");
    }
}
