/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opengis.filter.expression.Expression;
import org.opengis.style.ContrastMethod;

/** @author iant */
public class ExponentialContrastMethodStrategy extends AbstractContrastMethodStrategy {

    /** CORRECTION_FACTOR */
    private static final String CORRECTION_FACTOR = "correctionFactor";

    /** NORMALIZATION_FACTOR */
    private static final String NORMALIZATION_FACTOR = "normalizationFactor";

    static final List<String> PARAM_NAMES = Arrays.asList(NORMALIZATION_FACTOR, CORRECTION_FACTOR);

    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(ExponentialContrastMethodStrategy.class);

    /** */
    public ExponentialContrastMethodStrategy() {

        method = ContrastMethod.EXPONENTIAL;
    }

    public void addParameter(String key, Expression value) {
        if (!PARAM_NAMES.contains(key)) {
            LOGGER.log(
                    Level.WARNING,
                    "Adding unexpected parameter {0} to {1} Contrast Enhancer",
                    new Object[] {key, method.name()});
        }
        super.addParameter(key, value);
    }
}
