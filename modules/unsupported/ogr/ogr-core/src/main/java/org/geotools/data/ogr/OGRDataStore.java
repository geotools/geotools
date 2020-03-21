/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.ogr;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.data.DataSourceException;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.NameImpl;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;

/**
 * A data store based on the OGR native library, bound to it via <a
 * href="http://code.google.com/p/bridj/">BridJ</a>
 *
 * @author Andrea Aime - GeoSolutions
 */
@SuppressWarnings("rawtypes")
public class OGRDataStore extends ContentDataStore {

    OGRDataSourcePool dataSourcePool;

    OGR ogr;

    String ogrSourceName;

    String ogrDriver;

    public OGRDataStore(
            String ogrName,
            String ogrDriver,
            URI namespace,
            OGR ogr,
            OGRDataSourcePool dataSourcePool) {
        if (namespace != null) {
            setNamespaceURI(namespace.toString());
        }
        this.ogrSourceName = ogrName;
        this.ogrDriver = ogrDriver;
        this.ogr = ogr;
        this.dataSourcePool = dataSourcePool;
    }

    public OGRDataStore(String ogrName, String ogrDriver, URI namespace, OGR ogr)
            throws IOException {
        this(
                ogrName,
                ogrDriver,
                namespace,
                ogr,
                new OGRDataSourcePool(ogr, ogrName, ogrDriver, Collections.emptyMap()));
    }

    @Override
    protected List<Name> createTypeNames() throws IOException {
        OGRDataSource dataSource = null;
        Object layer = null;
        try {
            dataSource = openOGRDataSource(false);

            List<Name> result = new ArrayList<>();
            int count = dataSource.getLayerCount();
            for (int i = 0; i < count; i++) {
                layer = dataSource.getLayer(i, false);
                String name = ogr.LayerGetName(layer);
                if (name != null) {
                    result.add(new NameImpl(getNamespaceURI(), name));
                }
                ogr.LayerRelease(layer);
            }
            return result;
        } finally {
            if (layer != null) {
                ogr.LayerRelease(layer);
            }
            if (dataSource != null) {
                dataSource.close();
            }
        }
    }

    OGRDataSource openOGRDataSource(boolean update) throws IOException {
        return this.dataSourcePool.getDataSource(update);
    }

    Object openOGRLayer(OGRDataSource dataSource, String layerName, boolean allowPriming)
            throws IOException {
        Object layer = dataSource.getLayerByName(layerName, allowPriming);
        if (layer == null) {
            throw new IOException("OGR could not find layer '" + layerName + "'");
        }
        return layer;
    }

    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
        if (supportsInPlaceWrite(entry.getTypeName())) {
            return new OGRFeatureStore(entry, Query.ALL, ogr);
        } else {
            return new OGRFeatureSource(entry, Query.ALL, ogr);
        }
    }

    public boolean supportsInPlaceWrite(String typeName) throws IOException {
        OGRDataSource ds = null;
        Object l = null;
        try {
            // try opening in update mode
            Object rawDs = ogr.Open(ogrSourceName, 1);
            if (rawDs == null) {
                return false;
            }
            ds = new OGRDataSource(ogr, null, rawDs, true);
            l = openOGRLayer(ds, typeName, false);

            // for the moment we support working only with random writers
            boolean canDelete = ogr.LayerCanDeleteFeature(l);
            boolean canWriteRandom = ogr.LayerCanWriteRandom(l);
            boolean canWriteSequential = ogr.LayerCanWriteSequential(l);
            return canDelete && canWriteRandom && canWriteSequential;
        } finally {
            if (l != null) ogr.LayerRelease(l);
            if (ds != null) ds.close();
        }
    }

    public void createSchema(SimpleFeatureType schema) throws IOException {
        // TODO: add a field to allow approximate definitions
        createSchema(schema, false, null);
    }

    /**
     * Creates a new OGR layer with provided schema and options
     *
     * @param schema the geotools schema
     * @param approximateFields if true, OGR will try to create fields that are approximations of
     *     the required ones when an exact match cannt be provided
     * @param options OGR data source/layer creation options
     */
    public void createSchema(SimpleFeatureType schema, boolean approximateFields, String[] options)
            throws IOException {
        OGRDataSource dataSource = null;
        Object layer = null;

        try {
            // either open datasource, or try creating one
            dataSource = dataSourcePool.openOrCreateDataSource(options);

            FeatureTypeMapper mapper = new FeatureTypeMapper(ogr);

            layer = createNewLayer(schema, dataSource, options, mapper);

            // check the ability to create fields
            Object driver = dataSource.getDriver();
            String driverName = ogr.DriverGetName(driver);
            ogr.DriverRelease(driver);
            if (!driverName.equalsIgnoreCase("georss")
                    && !driverName.equalsIgnoreCase("gpx")
                    && !driverName.equalsIgnoreCase("sosi")
                    && !ogr.LayerCanCreateField(layer)) {
                throw new DataSourceException(
                        "OGR reports it's not possible to create fields on this layer");
            }

            // create fields
            for (int i = 0; i < schema.getAttributeCount(); i++) {
                AttributeDescriptor ad = schema.getDescriptor(i);
                if (ad == schema.getGeometryDescriptor()) continue;

                Object fieldDefinition = mapper.getOGRFieldDefinition(ad);
                ogr.LayerCreateField(layer, fieldDefinition, approximateFields ? 1 : 0);
            }

            ogr.LayerSyncToDisk(layer);
        } finally {
            if (layer != null) {
                ogr.LayerRelease(layer);
            }
            if (dataSource != null) {
                dataSource.close();
            }
        }
    }

    /**
     * Creates a new OGR layer with provided data and options. This call is specifically made
     * available for the OGC store since for some data source types, such as GML or KML, it is not
     * possible to call createSchema() independently from a write, as the result will not contain
     * the schema definition without having data too. Also, in those formats, the output is writable
     * only so as long as it's empty, it's not possible to write against an existing GML file for
     * example.
     *
     * @param data The data to fill into the newly created layer
     * @param approximateFields if true, OGR will try to create fields that are approximations of
     *     the required ones when an exact match cannt be provided
     * @param options OGR data source/layer creation options
     */
    public void createSchema(
            SimpleFeatureCollection data, boolean approximateFields, String[] options)
            throws IOException {
        OGRDataSource dataSource = null;
        Object layer = null;
        SimpleFeatureType schema = data.getSchema();
        try {
            // either open datasource, or try creating one
            dataSource = dataSourcePool.openOrCreateDataSource(options);

            FeatureTypeMapper mapper = new FeatureTypeMapper(ogr);

            // layer = createNewLayer(schema, dataSource, optionsPointer, mapper);
            layer = createNewLayer(schema, dataSource, options, mapper);

            // check the ability to create fields
            Object driver = dataSource.getDriver();
            String driverName = ogr.DriverGetName(driver);
            ogr.DriverRelease(driver);
            if (!driverName.equalsIgnoreCase("georss")
                    && !driverName.equalsIgnoreCase("gpx")
                    && !driverName.equalsIgnoreCase("sosi")
                    && !driverName.equalsIgnoreCase("geojson")
                    && !ogr.LayerCanCreateField(layer)) {
                throw new DataSourceException(
                        "OGR reports it's not possible to create fields on this layer");
            }

            // create fields
            Map<String, String> nameMap = new HashMap<String, String>();
            for (int i = 0; i < schema.getAttributeCount(); i++) {
                AttributeDescriptor ad = schema.getDescriptor(i);
                if (ad == schema.getGeometryDescriptor()) {
                    continue;
                }

                Object fieldDefinition = mapper.getOGRFieldDefinition(ad);
                ogr.LayerCreateField(layer, fieldDefinition, approximateFields ? 1 : 0);
                // the data source might have changed the name of the field, map them
                String newName = ogr.FieldGetName(fieldDefinition);
                nameMap.put(newName, ad.getLocalName());
            }

            // get back the feature definition
            Object layerDefinition = ogr.LayerGetLayerDefn(layer);

            // remap positions, as the store might add extra attributes (and the field api is
            // positional)
            Map<Integer, Integer> indexMap = new HashMap<Integer, Integer>();
            int count = ogr.LayerGetFieldCount(layerDefinition);
            for (int i = 0; i < count; i++) {
                Object fd = ogr.LayerGetFieldDefn(layerDefinition, i);
                String newName = ogr.FieldGetName(fd);
                if (newName != null) {
                    String oldName = nameMap.get(newName);
                    // Check case insensitive because sqlite can convert names to lowercase
                    if (oldName == null) {
                        oldName = nameMap.get(newName.toLowerCase());
                    }
                    if (oldName == null) {
                        oldName = nameMap.get(newName.toUpperCase());
                    }
                    for (int j = 0; j < schema.getAttributeCount(); j++) {
                        if (schema.getDescriptor(j).getLocalName().equals(oldName)) {
                            indexMap.put(j, i);
                        }
                    }
                }
            }

            // iterate and write out without going throught the ContentDataStore api, which
            // assumes it's possible to let go of it later
            GeometryMapper geomMapper = new GeometryMapper.WKB(new GeometryFactory(), ogr);
            try (SimpleFeatureIterator features = data.features()) {
                while (features.hasNext()) {
                    SimpleFeature feature = features.next();

                    // create the equivalent ogr feature
                    Object ogrFeature = ogr.LayerNewFeature(layerDefinition);
                    for (int i = 0; i < schema.getAttributeCount(); i++) {
                        Object value = feature.getAttribute(i);
                        if (value instanceof Geometry) {
                            // using setGeoemtryDirectly the feature becomes the owner of the
                            // generated
                            // OGR geometry and we don't have to .delete() it (it's faster, too)
                            Object geometry = geomMapper.parseGTGeometry((Geometry) value);
                            ogr.FeatureSetGeometryDirectly(ogrFeature, geometry);
                        } else {
                            // remap index
                            int ogrIndex = indexMap.get(i);
                            FeatureMapper.setFieldValue(
                                    layerDefinition, ogrFeature, ogrIndex, value, ogr);
                        }
                    }

                    // write it out
                    ogr.CheckError(ogr.LayerCreateFeature(layer, ogrFeature));

                    ogr.FeatureDestroy(ogrFeature);
                }
            }
            ogr.LayerSyncToDisk(layer);
        } finally {
            if (layer != null) {
                ogr.LayerRelease(layer);
            }
            if (dataSource != null) {
                dataSource.close();
            }
        }
    }

    private Object createNewLayer(
            SimpleFeatureType schema,
            OGRDataSource dataSource,
            String[] options,
            FeatureTypeMapper mapper)
            throws IOException, DataSourceException {
        Object layer;
        // get the spatial reference corresponding to the default geometry
        GeometryDescriptor geomType = schema.getGeometryDescriptor();
        long ogrGeomType = mapper.getOGRGeometryType(geomType);
        Object spatialReference =
                mapper.getSpatialReference(geomType.getCoordinateReferenceSystem());

        // create the layer
        layer =
                dataSource.createLayer(
                        schema.getTypeName(), spatialReference, ogrGeomType, options);
        if (layer == null) {
            throw new DataSourceException(
                    "Could not create the OGR layer: " + ogr.GetLastErrorMsg());
        }
        return layer;
    }

    @Override
    public void dispose() {
        try {
            super.dispose();
        } finally {
            dataSourcePool.close();
        }
    }
}
