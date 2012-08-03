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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.geotools.data.EmptyFeatureReader;
import org.geotools.data.FeatureReader;
import org.geotools.data.FilteringFeatureReader;
import org.geotools.data.Query;
import org.geotools.data.ReTypeFeatureReader;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.data.store.ContentFeatureStore;
import org.geotools.factory.Hints;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * FeatureSource for the OGR store, based on the {@link ContentFeatureStore} framework
 * 
 * @author Andrea Aime - GeoSolutions
 * @source $URL$
 */
@SuppressWarnings("rawtypes")
class OGRFeatureSource extends ContentFeatureSource {

    OGR ogr;

    public OGRFeatureSource(ContentEntry entry, Query query, OGR ogr) {
        super(entry, query);
        this.ogr = ogr;
    }

    @Override
    public OGRDataStore getDataStore() {
        return (OGRDataStore) super.getDataStore();
    }

    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        CoordinateReferenceSystem crs = getSchema().getCoordinateReferenceSystem();

        // we need to know how much we can translate of the filter (the translator will also
        // simplify the filter)
        OGRFilterTranslator filterTx = new OGRFilterTranslator(getSchema(), query.getFilter());
        if (Filter.EXCLUDE.equals(filterTx.getFilter())) {
            // empty results
            return new ReferencedEnvelope(crs);
        } else if (!filterTx.isFilterFullySupported()) {
            return null;
        } else {
            // encodable, we then encode and get the bounds
            Object dataSource = null;
            Object layer = null;

            try {
                // grab the layer
                String typeName = getEntry().getTypeName();
                dataSource = getDataStore().openOGRDataSource(false);
                layer = getDataStore().openOGRLayer(dataSource, typeName);

                // filter it
                setLayerFilters(layer, filterTx);

                Object extent = ogr.LayerGetExtent(layer);
                if (extent == null) {
                    return null;
                }
                return ogr.toEnvelope(extent, crs);

            } finally {
                ogr.LayerRelease(layer);
                ogr.DataSourceRelease(dataSource);
            }
        }
    }

    /**
     * Sets the spatial filter and attribute filter on the specified layer
     * 
     * @param layer
     * @param filterTx
     * @throws IOException
     */
    private void setLayerFilters(Object layer, OGRFilterTranslator filterTx) throws IOException {
        Geometry spatialFilter = filterTx.getSpatialFilter();
        if (spatialFilter != null) {
            Object ogrGeometry = new GeometryMapper.WKB(new GeometryFactory(), ogr)
                    .parseGTGeometry(spatialFilter);
            ogr.LayerSetSpatialFilter(layer, ogrGeometry);
        }

        String attFilter = filterTx.getAttributeFilter();
        if (attFilter != null) {
            ogr.LayerSetAttributeFilter(layer, attFilter);
        }
    }

    @Override
    protected int getCountInternal(Query query) throws IOException {
        // check how much we can encode
        OGRFilterTranslator filterTx = new OGRFilterTranslator(getSchema(), query.getFilter());
        if (Filter.EXCLUDE.equals(filterTx.getFilter())) {
            return 0;
        } else if (!filterTx.isFilterFullySupported()) {
            // too expensive then
            return -1;
        } else {
            // encode and count
            Object dataSource = null;
            Object layer = null;

            try {
                // grab the layer
                String typeName = getEntry().getTypeName();
                dataSource = getDataStore().openOGRDataSource(false);
                layer = getDataStore().openOGRLayer(dataSource, typeName);

                // filter it
                setLayerFilters(layer, filterTx);

                return (int) ogr.LayerGetFeatureCount(layer);
            } finally {
                ogr.LayerRelease(layer);
                ogr.DataSourceRelease(dataSource);
            }
        }
    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query)
            throws IOException {
        return getReaderInternal(null, null, query);
    }

    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Object dataSource,
            Object layer, Query query) throws IOException {
        // check how much we can encode
        OGRFilterTranslator filterTx = new OGRFilterTranslator(getSchema(), query.getFilter());
        if (Filter.EXCLUDE.equals(filterTx.getFilter())) {
            return new EmptyFeatureReader<SimpleFeatureType, SimpleFeature>(getSchema());
        }

        // encode and count
        boolean cleanup = false;
        try {
            // grab the data source
            String typeName = getEntry().getTypeName();
            if (dataSource == null) {
                dataSource = getDataStore().openOGRDataSource(false);
                cleanup = true;
            }

            // extract the post filter
            Filter postFilter = null;
            if (!filterTx.isFilterFullySupported()) {
                postFilter = filterTx.getPostFilter();
            }

            // prepare the target schema
            SimpleFeatureType sourceSchema = getSchema();
            SimpleFeatureType querySchema = sourceSchema;
            SimpleFeatureType targetSchema = sourceSchema;
            String[] properties = query.getPropertyNames();
            if (properties != null && properties.length > 0) {
                targetSchema = SimpleFeatureTypeBuilder.retype(sourceSchema, properties);
                querySchema = targetSchema;
                // if we have a post filter we have to include in the queried features also the
                // attribute needed to evaluate the post-filter
                if (postFilter != null) {
                    Set<String> queriedAttributes = new HashSet<String>(Arrays.asList(properties));
                    FilterAttributeExtractor extraAttributeExtractor = new FilterAttributeExtractor();
                    postFilter.accept(extraAttributeExtractor, null);
                    Set<String> extraAttributeSet = new HashSet<String>(
                            extraAttributeExtractor.getAttributeNameSet());
                    extraAttributeSet.removeAll(queriedAttributes);
                    if (extraAttributeSet.size() > 0) {
                        String[] queryProperties = new String[properties.length
                                + extraAttributeSet.size()];
                        System.arraycopy(properties, 0, queryProperties, 0, properties.length);
                        String[] extraAttributes = (String[]) extraAttributeSet
                                .toArray(new String[extraAttributeSet.size()]);
                        System.arraycopy(extraAttributes, 0, queryProperties, properties.length,
                                extraAttributes.length);
                        querySchema = SimpleFeatureTypeBuilder
                                .retype(sourceSchema, queryProperties);
                    }
                }
            }

            // build the layer query and execute it
            if (layer == null) {
                String sql = getLayerSql(querySchema == sourceSchema ? null : querySchema,
                        filterTx.getAttributeFilter(), query.getSortBy());
                Object spatialFilterPtr = null;
                Geometry spatialFilter = filterTx.getSpatialFilter();
                if (spatialFilter != null) {
                    spatialFilterPtr = new GeometryMapper.WKB(new GeometryFactory(), ogr)
                            .parseGTGeometry(spatialFilter);

                }
                layer = ogr.DataSourceExecuteSQL(dataSource, sql, spatialFilterPtr);
                if (layer == null) {
                    throw new IOException("Failed to query the source layer with SQL: " + sql);
                }
            } else {
                setLayerFilters(layer, filterTx);
                // would be nice, but it's not really working...
                // setIgnoredFields(layer, querySchema, sourceSchema);
            }

            // see if we have a geometry factory to use
            GeometryFactory gf = getGeometryFactory(query);

            // build the reader
            FeatureReader<SimpleFeatureType, SimpleFeature> reader = new OGRFeatureReader(
                    dataSource, layer, querySchema, sourceSchema, gf, ogr);
            cleanup = false;

            // do we have to post-filter?
            if (!filterTx.isFilterFullySupported()) {
                reader = new FilteringFeatureReader<SimpleFeatureType, SimpleFeature>(reader,
                        filterTx.getPostFilter());
                if (targetSchema != querySchema) {
                    reader = new ReTypeFeatureReader(reader, targetSchema);
                }
            }

            return reader;
        } finally {
            if (cleanup) {
                ogr.LayerRelease(layer);
                ogr.DataSourceRelease(dataSource);
            }
        }
    }

    void setIgnoredFields(Object layer, SimpleFeatureType querySchema,
            SimpleFeatureType sourceSchema) throws IOException {
        if (ogr.LayerCanIgnoreFields(layer)) {
            List<String> ignoredFields = new ArrayList<String>();
            ignoredFields.add("OGR_STYLE");
            // if no geometry, skip it
            if (querySchema.getGeometryDescriptor() == null) {
                ignoredFields.add("OGR_GEOMETRY");
            }
            // process all other attributes
            for (AttributeDescriptor ad : sourceSchema.getAttributeDescriptors()) {
                if (!(ad instanceof GeometryDescriptor)) {
                    String name = ad.getLocalName();
                    if (querySchema.getDescriptor(name) == null) {
                        ignoredFields.add(name);
                    }
                }
            }
            if (ignoredFields.size() > 0) {
                // the list should be NULL terminated
                ignoredFields.add(null);
                String[] ignoredFieldsArr = (String[]) ignoredFields
                        .toArray(new String[ignoredFields.size()]);
                ogr.CheckError(ogr.LayerSetIgnoredFields(layer, ignoredFieldsArr));
            }
        }

    }

    private String getLayerSql(SimpleFeatureType targetSchema, String attributeFilter,
            SortBy[] sortBy) {
        StringBuilder sb = new StringBuilder();

        // list only the non geometry attributes
        
        // select attributes
        sb.append("SELECT FID, ");
        if (targetSchema == null) {
            sb.append("* ");
        } else {
            for (AttributeDescriptor attribute : targetSchema.getAttributeDescriptors()) {
                if(attribute instanceof GeometryDescriptor) {
                    continue;
                } else {
                    sb.append(attribute.getLocalName()).append(", ");
                }
            }
            sb.setLength(sb.length() - 2);
            sb.append(" ");
        }
        sb.append("FROM ").append("'").append(getSchema().getTypeName()).append("' ");

        // attribute filter
        if (attributeFilter != null) {
            sb.append("WHERE ").append(attributeFilter);
        }

        // order by
        if (sortBy != null && sortBy.length > 0) {
            sb.append("ORDER BY ");
            for (SortBy sort : sortBy) {
                if (sort == SortBy.NATURAL_ORDER) {
                    sb.append("FID, ");
                } else if (sort == SortBy.REVERSE_ORDER) {
                    sb.append("FID DESC, ");
                } else {
                    sb.append(sort.getPropertyName().getPropertyName());
                    if (sort.getSortOrder() == SortOrder.DESCENDING) {
                        sb.append(" DESC");
                    }
                    sb.append(", ");
                }
            }
            sb.setLength(sb.length() - 2);
        }

        return sb.toString();
    }

    GeometryFactory getGeometryFactory(Query query) {
        Hints hints = query.getHints();
        GeometryFactory gf = null;
        if (hints != null) {
            gf = (GeometryFactory) hints.get(Hints.JTS_GEOMETRY_FACTORY);
            if (gf == null) {
                // look for a coordinate sequence factory
                CoordinateSequenceFactory csFactory = (CoordinateSequenceFactory) hints
                        .get(Hints.JTS_COORDINATE_SEQUENCE_FACTORY);

                if (csFactory != null) {
                    gf = new GeometryFactory(csFactory);
                }
            }
        }
        if (gf == null) {
            gf = new GeometryFactory();
        }
        return gf;
    }

    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {
        String typeName = getEntry().getTypeName();
        String namespaceURI = getDataStore().getNamespaceURI();

        Object dataSource = null;
        Object layer = null;
        try {
            // grab the layer definition
            dataSource = getDataStore().openOGRDataSource(false);
            layer = getDataStore().openOGRLayer(dataSource, typeName);

            // map to geotools feature type
            return new FeatureTypeMapper(ogr).getFeatureType(layer, typeName, namespaceURI);
        } finally {
            ogr.LayerRelease(layer);
            ogr.DataSourceRelease(dataSource);
        }

    }

    @Override
    protected boolean canFilter() {
        return true;
    }

    @Override
    protected boolean canRetype() {
        return true;
    }

    @Override
    protected boolean canSort() {
        return true;
    }

    @Override
    protected boolean handleVisitor(Query query, FeatureVisitor visitor) throws IOException {
        return super.handleVisitor(query, visitor);
    }

}
