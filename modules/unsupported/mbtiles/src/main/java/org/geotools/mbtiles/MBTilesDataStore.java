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
package org.geotools.mbtiles;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.geotools.data.DataSourceException;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class MBTilesDataStore extends ContentDataStore {

    static final Logger LOGGER = Logging.getLogger(MBTilesDataStore.class);

    private static final int MAX_ATTEMPTS = 1000;
    protected static final CoordinateReferenceSystem DEFAULT_CRS;

    static {
        CoordinateReferenceSystem crs;
        try {
            // try with web mercator
            crs = CRS.decode("EPSG:3857", true);
        } catch (FactoryException e) {
            LOGGER.log(
                    Level.WARNING,
                    "Could not initialize web mercator, geometry fields will use an engineering CRS",
                    e);
            crs = DefaultEngineeringCRS.GENERIC_2D;
        }
        DEFAULT_CRS = crs;
    }

    private final MBtilesCache tileCache;

    MBTilesFile mbtiles;
    LinkedHashMap<String, SimpleFeatureType> schemas;

    public MBTilesDataStore(MBTilesFile mbtiles) throws IOException {
        this(null, mbtiles);
    }

    public MBTilesDataStore(String namespaceURI, MBTilesFile mbtiles) throws IOException {
        this.namespaceURI = namespaceURI;
        this.mbtiles = mbtiles;
        MBTilesMetadata metadata = mbtiles.loadMetaData();
        if (!MBTilesMetadata.t_format.PBF.equals(metadata.getFormat())) {
            throw new DataSourceException(
                    "Expected 'PBF' as the format, but found " + metadata.getFormat());
        }
        if (metadata.getJson() == null) {
            throw new DataSourceException(
                    "Cannot find 'json' metadata field, required to load the layers and their structure");
        }
        try {
            LinkedHashMap<String, VectorLayerMetadata> layersMap =
                    VectorLayersMetadata.parseMetadata(metadata.getJson()).getLayersMap();
            schemas = new LinkedHashMap<>();
            for (String layerName : layersMap.keySet()) {
                VectorLayerMetadata lm = layersMap.get(layerName);
                schemas.put(layerName, buildFeatureType(lm));
            }
        } catch (Exception e) {
            throw new DataSourceException(
                    "Could not parse the 'json' metadata field, failed to initialize the store", e);
        }

        this.tileCache = new MBtilesCache(schemas);
    }

    private SimpleFeatureType buildFeatureType(VectorLayerMetadata layerMetadata) {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName(layerMetadata.getId());
        tb.setNamespaceURI(this.namespaceURI);
        LinkedHashMap<String, Class> fieldBindings = layerMetadata.getFieldBindings();
        fieldBindings.entrySet().forEach(e -> tb.add(e.getKey(), e.getValue()));
        String geometryName = guessGeometryName(fieldBindings.keySet());
        tb.add(geometryName, Geometry.class, DEFAULT_CRS);

        return tb.buildFeatureType();
    }

    private String guessGeometryName(Set<String> attributeNames) {
        String geometryName = "the_geom";
        for (int i = 0; i < MAX_ATTEMPTS && attributeNames.contains(geometryName); i++) {
            geometryName = geometryName + i;
        }
        if (attributeNames.contains(geometryName)) {
            throw new RuntimeException(
                    "Unexpected, could not find a unique geometry name after appending the first "
                            + MAX_ATTEMPTS
                            + " integers to 'the_geom'");
        }
        return geometryName;
    }

    @Override
    protected List<Name> createTypeNames() throws IOException {
        return schemas.keySet().stream()
                .map(id -> new NameImpl(getNamespaceURI(), id))
                .collect(Collectors.toList());
    }

    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
        SimpleFeatureType schema = schemas.get(entry.getTypeName());
        return new MBTilesFeatureSource(entry, schema, mbtiles, tileCache);
    }
}
