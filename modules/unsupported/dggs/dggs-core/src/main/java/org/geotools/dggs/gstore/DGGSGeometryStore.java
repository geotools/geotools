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
import java.util.List;
import org.geotools.api.feature.type.Name;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.dggs.DGGSInstance;
import org.geotools.feature.NameImpl;

/**
 * A store returning DGGS zones as GeoTools {@link org.geotools.api.feature.simple.SimpleFeature}, without any actual
 * data associated to them. It's pure DGGS structure description.
 */
public class DGGSGeometryStore<I> extends ContentDataStore implements DGGSStore<I> {

    public static final String ZONE_ID = "zoneId";
    public static final String RESOLUTION = "resolution";
    public static final String GEOMETRY = "geometry";

    DGGSInstance<I> dggs;
    DGGSResolutionCalculator resolutions;

    public DGGSGeometryStore(DGGSInstance<I> dggs) {
        this.dggs = dggs;
        this.resolutions = new DGGSResolutionCalculator(dggs, null);
    }

    @Override
    protected List<Name> createTypeNames() throws IOException {
        return List.of(new NameImpl(namespaceURI, dggs.getIdentifier()));
    }

    @Override
    protected DGGSGeometryFeatureSource<I> createFeatureSource(ContentEntry entry) throws IOException {
        return new DGGSGeometryFeatureSource<>(entry, this);
    }

    @Override
    public void dispose() {
        dggs.close();
    }

    @Override
    public DGGSFeatureSource<I> getDGGSFeatureSource(String typeName) throws IOException {
        ContentEntry entry = ensureEntry(new NameImpl(namespaceURI, typeName));
        return new DGGSGeometryFeatureSource<>(entry, this);
    }
}
