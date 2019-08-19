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

import org.geotools.data.FeatureLocking;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Used to lock feature content to protect against other threads (or depending on the source of data
 * other applications) making modifications when you are not looking.
 *
 * <p>The locks operate more as a lease for a specific period of time. In effect you are only
 * locking for a set time period; so even if your application or machine crashes the lock will
 * eventually be released allowing others to play.
 */
public interface SimpleFeatureLocking
        extends SimpleFeatureStore, FeatureLocking<SimpleFeatureType, SimpleFeature> {}
