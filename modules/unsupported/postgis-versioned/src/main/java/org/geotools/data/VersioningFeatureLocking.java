/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data;

import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Feature locking with versioning capabilities. <p>See {@link VersioningFeatureStore} and
 * {@link VersioningFeatureSource} for actual extra capabilities compared to a basic
 * {@link FeatureLocking}.
 * 
 * @author Andrea Aime, TOPP
 *
 *
 * @source $URL$
 */
public interface VersioningFeatureLocking extends VersioningFeatureStore,
        FeatureLocking<SimpleFeatureType, SimpleFeature> {

}
