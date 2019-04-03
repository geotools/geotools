/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.simple;

import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;

/**
 * Streaming access to simple features, required to {@link #close()} after use.
 *
 * <p>This is an explicit interface for FeatureIterator<SimpleFeature>.
 *
 * <p>Sample use:
 *
 * <pre> SimpleFeatureIterator i = featureCollection.features()
 * try {
 *    while( i.hasNext() ){
 *        SimpleFeature feature = i.next();
 *    }
 * }
 * finally {
 *    i.close();
 * }
 * </pre>
 *
 * @author Jody Garnett
 */
public interface SimpleFeatureIterator extends FeatureIterator<SimpleFeature> {}
