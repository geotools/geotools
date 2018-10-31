/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature.collection;

import org.geotools.data.simple.SimpleFeatureIterator;
import org.opengis.feature.simple.SimpleFeature;

/**
 * A feature iterator that completely delegates to a normal Iterator, simply allowing Java 1.4 code
 * to escape the caste (sic) system.
 *
 * <p>This implementation is not suitable for use with collections that make use of system
 * resources. As an alterantive please see ResourceFetaureIterator.
 *
 * @author Jody Garnett, Refractions Research, Inc.
 */
public class DecoratingSimpleFeatureIterator extends DecoratingFeatureIterator<SimpleFeature>
        implements SimpleFeatureIterator {

    /**
     * Wrap the provided iterator up as a FeatureIterator.
     *
     * @param iterator Iterator to be used as a delegate.
     */
    public DecoratingSimpleFeatureIterator(SimpleFeatureIterator iterator) {
        super(iterator);
    }
}
