/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.function;

import java.awt.image.RenderedImage;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.util.Converters;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.filter.capability.FunctionName;

/**
 * Return the number of bands of the evaluation context, which is supposed to be a coverage, or an
 * object that can be converted to on.
 */
public class BandsFunction extends FunctionExpressionImpl {
    public static FunctionName NAME = new FunctionNameImpl("bands", Integer.class);

    public BandsFunction() {
        super(NAME);
    }

    @Override
    public Object evaluate(Object object) {
        // try as a rendered image
        RenderedImage image = Converters.convert(object, RenderedImage.class);
        if (image != null) {
            return ((RenderedImage) object).getSampleModel().getNumBands();
        }

        // try as a coverage
        GridCoverage coverage = Converters.convert(object, GridCoverage.class);
        if (coverage != null) {
            return coverage.getNumSampleDimensions();
        }

        // fall back
        return null;
    }
}
