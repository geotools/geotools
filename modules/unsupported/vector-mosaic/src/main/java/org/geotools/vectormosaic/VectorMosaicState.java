/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.vectormosaic;

import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentState;
import org.opengis.feature.simple.SimpleFeatureType;

public class VectorMosaicState extends ContentState {

    SimpleFeatureType granuleFeatureType;

    public VectorMosaicState(ContentEntry entry) {
        super(entry);
    }

    public SimpleFeatureType getGranuleFeatureType() {
        return granuleFeatureType;
    }

    public void setGranuleFeatureType(SimpleFeatureType granuleFeatureType) {
        this.granuleFeatureType = granuleFeatureType;
    }
}
