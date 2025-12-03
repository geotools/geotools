/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.dggs.gstore;

import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.dggs.DGGSInstance;

/** DGGS specific extensions to {@link SimpleFeatureSource}, adding ability to native zone access */
public interface DGGSFeatureSource<I> extends SimpleFeatureSource {

    /**
     * Returns the DGGS instance used by this feature source
     *
     * @return
     */
    DGGSInstance<I> getDGGS();

    /**
     * Returns the attribute used to store zone identifiers
     *
     * @return
     */
    AttributeDescriptor getZoneIdAttribute();
}
