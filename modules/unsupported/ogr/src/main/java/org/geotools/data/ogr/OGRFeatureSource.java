package org.geotools.data.ogr;

import static org.bridj.Pointer.*;
import static org.geotools.data.ogr.bridj.OgrLibrary.*;
import static org.geotools.data.ogr.bridj.OsrLibrary.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.bridj.Pointer;
import org.bridj.ValuedEnum;
import org.geotools.data.EmptyFeatureReader;
import org.geotools.data.FeatureReader;
import org.geotools.data.FilteringFeatureReader;
import org.geotools.data.Query;
import org.geotools.data.ReTypeFeatureReader;
import org.geotools.data.ogr.bridj.OGREnvelope;
import org.geotools.data.ogr.bridj.OgrLibrary.OGRFieldType;
import org.geotools.data.ogr.bridj.OgrLibrary.OGRwkbGeometryType;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.Hints;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.type.BasicFeatureTypes;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

@SuppressWarnings("rawtypes")
/**
 * 
 *
 * @source $URL$
 */
public class OGRFeatureSource extends ContentFeatureSource {

    public OGRFeatureSource(ContentEntry entry, Query query) {
        super(entry, query);
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
            Pointer dataSource = null;
            Pointer layer = null;

            try {
                // grab the layer
                String typeName = getEntry().getTypeName();
                dataSource = getDataStore().openOGRDataSource(false);
                layer = openOGRLayer(dataSource, typeName);

                // filter it
                setLayerFilters(layer, filterTx);

                Pointer<OGREnvelope> boundsPtr = allocate(OGREnvelope.class);
                int code = OGR_L_GetExtent(layer, boundsPtr, 0);
                if (code == OGRERR_FAILURE) {
                    return null;
                } else {
                    OGREnvelope bounds = boundsPtr.get();
                    return new ReferencedEnvelope(bounds.MinX(), bounds.MaxX(), bounds.MinY(),
                            bounds.MaxY(), crs);
                }
            } finally {
                OGRUtils.releaseLayer(layer);
                OGRUtils.releaseDataSource(dataSource);
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
    private void setLayerFilters(Pointer layer, OGRFilterTranslator filterTx) throws IOException {
        Geometry spatialFilter = filterTx.getSpatialFilter();
        if (spatialFilter != null) {
            Pointer ogrGeometry = new GeometryMapper.WKB(new GeometryFactory())
                    .parseGTGeometry(spatialFilter);
            OGR_L_SetSpatialFilter(layer, ogrGeometry);
        }

        String attFilter = filterTx.getAttributeFilter();
        if (attFilter != null) {
            OGR_L_SetAttributeFilter(layer, pointerToCString(attFilter));
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
            Pointer dataSource = null;
            Pointer layer = null;

            try {
                // grab the layer
                String typeName = getEntry().getTypeName();
                dataSource = getDataStore().openOGRDataSource(false);
                layer = openOGRLayer(dataSource, typeName);

                // filter it
                setLayerFilters(layer, filterTx);

                return OGR_L_GetFeatureCount(layer, 0);
            } finally {
                OGRUtils.releaseLayer(layer);
                OGRUtils.releaseDataSource(dataSource);
            }
        }
    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query)
            throws IOException {
        // check how much we can encode
        OGRFilterTranslator filterTx = new OGRFilterTranslator(getSchema(), query.getFilter());
        if (Filter.EXCLUDE.equals(filterTx.getFilter())) {
            return new EmptyFeatureReader<SimpleFeatureType, SimpleFeature>(getSchema());
        }

        // encode and count
        Pointer dataSource = null;
        Pointer layer = null;
        boolean cleanup = true;
        try {
            // grab the layer
            String typeName = getEntry().getTypeName();
            dataSource = getDataStore().openOGRDataSource(false);
            layer = openOGRLayer(dataSource, typeName);

            // filter it
            setLayerFilters(layer, filterTx);

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
                    Set<String> extraAttributeSet = new HashSet<String>(extraAttributeExtractor.getAttributeNameSet());
                    extraAttributeSet.removeAll(queriedAttributes);
                    if (extraAttributeSet.size() > 0) {
                        String[] queryProperties = new String[properties.length
                                + extraAttributeSet.size()];
                        System.arraycopy(properties, 0, queryProperties, 0, properties.length);
                        String[] extraAttributes = (String[]) extraAttributeSet
                                .toArray(new String[extraAttributeSet.size()]);
                        System.arraycopy(extraAttributes, 0, queryProperties, properties.length,
                                extraAttributes.length);
                        querySchema = SimpleFeatureTypeBuilder.retype(sourceSchema, queryProperties);
                    }
                }
            }

            // tell OGR not to load all the attributes
            // OGR_L_SetIgnoredFields(layer, charPtrPtr1)

            // see if we have a geometry factory to use
            GeometryFactory gf = getGeometryFactory(query);

            // build the reader
            FeatureReader<SimpleFeatureType, SimpleFeature> reader = new OGRFeatureReader(
                    dataSource, layer, querySchema, sourceSchema, gf);
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
                OGRUtils.releaseLayer(layer);
                OGRUtils.releaseDataSource(dataSource);
            }
        }
    }

    private GeometryFactory getGeometryFactory(Query query) {
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
        
        Pointer dataSource = null;
        Pointer layer = null;
        try {
            // grab the layer definition
            dataSource = getDataStore().openOGRDataSource(false);
            layer = openOGRLayer(dataSource, typeName);

            // map to geotools feature type
            return new FeatureTypeMapper().getFeatureType(layer, typeName, namespaceURI);
        } finally {
            OGRUtils.releaseLayer(layer);
            OGRUtils.releaseDataSource(dataSource);
        }

    }

    Pointer openOGRLayer(Pointer dataSource, String layerName) throws IOException {
        Pointer layer = OGR_DS_GetLayerByName(dataSource, pointerToCString(layerName));
        if (layer == null) {
            throw new IOException("OGR could not find layer '" + layerName + "'");
        }
        return layer;
    }

    @Override
    protected boolean canFilter() {
        return true;
    }

    @Override
    protected boolean canRetype() {
        return true;
    }

}
