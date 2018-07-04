/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.function.color;

/**
 * Implements the lesscss.org desaturate function
 *
 * @author Andrea Aime - GeoSolutions
 */
public class DesaturateFunction extends AbstractHSLFunction {

    public DesaturateFunction() {
        super("desaturate");
    }

    protected void adjstRelative(float amount, HSLColor hsl) {
        hsl.setSaturation(hsl.getSaturation() * (1 - amount));
    }

    protected void adjustAbsolute(float amount, HSLColor hsl) {
        hsl.setSaturation(hsl.getSaturation() - amount);
    }
}
