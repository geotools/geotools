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

import java.io.IOException;
import org.geotools.api.data.DataStore;
import org.geotools.api.feature.type.AttributeDescriptor;

/**
 * Interface for stores implementing DGGS access. Each feature type returned by a DGGSStore must have at a minimum a
 * zone identifier column and a {@link #RESOLUTION} field
 */
public interface DGGSStore<I> extends DataStore {

    /** A view parameter to forcefully choose a resolution */
    String VP_RESOLUTION = "res";

    /** A view parameter to forcefully choose a resolution */
    String VP_RESOLUTION_DELTA = "resOffset";

    String RESOLUTION = "resolution";

    /**
     * Marks attributes that are well known properties of a DGGS, that a DGGS aware client would not need to know about.
     * Added in the {@link AttributeDescriptor#getUserData()}, with a value of {@link Boolean#TRUE}. Examples of such
     * properties are the geometry and resolution of the zone, which are both implied by the zone id itself.
     */
    String DGGS_INTRINSIC = "dggsInstrisic";

    DGGSFeatureSource<I> getDGGSFeatureSource(String typeName) throws IOException;
}
