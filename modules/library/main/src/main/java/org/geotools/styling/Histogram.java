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

import org.opengis.filter.FilterFactory;
import org.opengis.style.ContrastMethod;

/**
 * @author iant
 *
 */
public class Histogram extends AbstractContrastEnhancementMethod {

    public Histogram() {
        NAME = "Histogram";
        method = ContrastMethod.HISTOGRAM;
    }

    public Histogram(ContrastMethod method) {
        if (!(ContrastMethod.HISTOGRAM.equals(method))) {
            throw new RuntimeException("tried to construct Histogram with " + method.getClass());
        }
    }

    /**
     * @param filterFactory
     */
    public Histogram(FilterFactory filterFactory) {
        this.filterFactory = filterFactory;
    }
}
