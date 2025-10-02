/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.color.ColorSpace;

/**
 * This class is a subclass of {@link ComponentColorModelImageN} to work around serialization issues. It is deprecated
 * and you're not supposed to use it anymore. It is only kept around for backward compatibility. Won't be removed
 * anytime soon, in order to support long term serialization of ImageLayout descriptions.
 */
@Deprecated
public class ComponentColorModelJAI extends ComponentColorModelImageN {
    public ComponentColorModelJAI(
            ColorSpace colorSpace,
            int[] bits,
            boolean hasAlpha,
            boolean isAlphaPremultiplied,
            int transparency,
            int transferType) {
        super(colorSpace, bits, hasAlpha, isAlphaPremultiplied, transparency, transferType);
    }

    public ComponentColorModelJAI(
            ColorSpace colorSpace, boolean hasAlpha, boolean isAlphaPremultiplied, int transparency, int transferType) {
        super(colorSpace, hasAlpha, isAlphaPremultiplied, transparency, transferType);
    }
}
