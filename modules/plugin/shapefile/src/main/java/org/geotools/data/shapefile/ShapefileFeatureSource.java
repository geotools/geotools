/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile;

import static org.geotools.data.shapefile.files.ShpFileType.SHP;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.data.CloseableIterator;
import org.geotools.api.data.DataSourceException;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.FeatureSource;
import org.geotools.api.data.Query;
import org.geotools.api.feature.FeatureVisitor;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.feature.type.GeometryType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.Id;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.filter.spatial.Beyond;
import org.geotools.api.filter.spatial.Contains;
import org.geotools.api.filter.spatial.Crosses;
import org.geotools.api.filter.spatial.DWithin;
import org.geotools.api.filter.spatial.Disjoint;
import org.geotools.api.filter.spatial.Touches;
import org.geotools.api.filter.temporal.TOverlaps;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.data.EmptyFeatureReader;
import org.geotools.data.PrjFileReader;
import org.geotools.data.ReTypeFeatureReader;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.fid.IndexedFidReader;
import org.geotools.data.shapefile.files.FileReader;
import org.geotools.data.shapefile.files.ShpFileType;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.index.Data;
import org.geotools.data.shapefile.index.TreeException;
import org.geotools.data.shapefile.shp.IndexFile;
import org.geotools.data.shapefile.shp.JTSUtilities;
import org.geotools.data.shapefile.shp.ShapefileHeader;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.data.util.ScreenMap;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.type.BasicFeatureTypes;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.filter.visitor.ExtractBoundsFilterVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.Classes;
import org.geotools.util.factory.Hints;
import org.geotools.util.factory.Hints.Key;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.CoordinateSequenceFactory;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

/**
 * A {@link FeatureSource} for shapefiles based on {@link ContentFeatureSource}
 *
 * @author Andrea Aime - GeoSolutions
 */
class ShapefileFeatureSource extends ContentFeatureSource {

    /** Attribute extract that resolves empty PropertyName references to the default geometry where appropriate. */
    private static final class AbsoluteAttributeExtractor extends FilterAttributeExtractor {
        private AbsoluteAttributeExtractor(SimpleFeatureType featureType) {
            super(featureType);
        }

        @Override
        public Object visit(final BBOX filter, Object data) {
            data = geom(filter.getExpression1(), data);
            data = geom(filter.getExpression2(), data);
            return data;
        }

        @Override
        public Object visit(Beyond filter, Object data) {
            data = geom(filter.getExpression1(), data);
            data = geom(filter.getExpression2(), data);
            return data;
        }

        @Override
        public Object visit(Contains filter, Object data) {
            data = geom(filter.getExpression1(), data);
            data = geom(filter.getExpression2(), data);
            return data;
        }

        @Override
        public Object visit(Crosses filter, Object data) {
            data = geom(filter.getExpression1(), data);
            data = geom(filter.getExpression2(), data);
            return data;
        }

        @Override
        public Object visit(Disjoint filter, Object data) {
            data = geom(filter.getExpression1(), data);
            data = geom(filter.getExpression2(), data);
            return data;
        }

        @Override
        public Object visit(DWithin filter, Object data) {
            data = geom(filter.getExpression1(), data);
            data = geom(filter.getExpression2(), data);
            return data;
        }

        @Override
        public Object visit(Touches filter, Object data) {
            data = geom(filter.getExpression1(), data);
            data = geom(filter.getExpression2(), data);
            return data;
        }

        @Override
        public Object visit(TOverlaps filter, Object data) {
            data = geom(filter.getExpression1(), data);
            data = geom(filter.getExpression2(), data);
            return data;
        }

        // Fill in geometries rather than XPath
        @SuppressWarnings("unchecked")
        Object geom(Expression expr, Object data) {
            String propertyName = expr instanceof PropertyName ? ((PropertyName) expr).getPropertyName() : null;
            if (propertyName != null && propertyName.trim().isEmpty()) {
                if (data != null && data != attributeNames) {
                    this.attributeNames = (Set<String>) data;
                }
                propertyNames.add((PropertyName) expr);
                // fill in all geometries .. for shapefile there is only one
                GeometryDescriptor geometryDescriptor = this.featureType.getGeometryDescriptor();
                this.attributeNames.add(geometryDescriptor.getName().getLocalPart());
                return data;
            } else {
                return expr.accept(this, data);
            }
        }
    }

    static final Logger LOGGER = Logging.getLogger(ShapefileFeatureSource.class);

    ShpFiles shpFiles;

    public ShapefileFeatureSource(ContentEntry entry, ShpFiles shpFiles) {
        super(entry, Query.ALL);
        this.shpFiles = shpFiles;
        HashSet<Key> hints = new HashSet<>();
        hints.add(Hints.FEATURE_DETACHED);
        hints.add(Hints.JTS_GEOMETRY_FACTORY);
        hints.add(Hints.JTS_COORDINATE_SEQUENCE_FACTORY);
        hints.add(Hints.GEOMETRY_DISTANCE);
        hints.add(Hints.SCREENMAP);
        this.hints = Collections.unmodifiableSet(hints);
    }

    @Override
    public ShapefileDataStore getDataStore() {
        return (ShapefileDataStore) super.getDataStore();
    }

    @Override
    protected boolean canFilter(Query query) {
        return true;
    }

    @Override
    protected boolean canRetype(Query query) {
        return true;
    }

    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        if (query.getFilter() != Filter.INCLUDE) {
            return null;
        }

        ByteBuffer buffer = ByteBuffer.allocate(100);
        FileReader reader = () -> "Shapefile Datastore's getBounds Method";
        try (ReadableByteChannel in = shpFiles.getReadChannel(SHP, reader)) {
            in.read(buffer);
            buffer.flip();

            ShapefileHeader header = new ShapefileHeader();
            header.read(buffer, true);

            SimpleFeatureType schema = getSchema();

            Envelope env;

            // If it is a shapefile without any data (file length equals 50), return an empty
            // envelope as expected
            if (header.getFileLength() == 50) {
                env = new Envelope();
            } else {
                env = new Envelope(header.minX(), header.maxX(), header.minY(), header.maxY());
            }

            CoordinateReferenceSystem crs = null;
            if (schema != null) {
                crs = schema.getCoordinateReferenceSystem();
            }
            return new ReferencedEnvelope(env, crs);
        } catch (IOException ioe) {
            throw new DataSourceException("Problem getting Bbox", ioe);
        }
    }

    @Override
    protected int getCountInternal(Query query) throws IOException {
        if (query.getFilter() == Filter.INCLUDE) {
            try (IndexFile file = getDataStore().shpManager.openIndexFile()) {
                if (file != null) {
                    return file.getRecordCount();
                }
            }

            // no Index file so use the number of shapefile records
            int count = -1;
            try (ShapefileReader reader = getDataStore().shpManager.openShapeReader(new GeometryFactory(), false)) {
                count = reader.getCount(count);
            } catch (IOException e) {
                throw e;
            }

            return count;
        }

        return -1;
    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query q) throws IOException {
        SimpleFeatureType resultSchema = getResultSchema(q);
        SimpleFeatureType readSchema = getReadSchema(q);
        GeometryFactory geometryFactory = getGeometryFactory(q);

        // grab the target bbox, if any
        Envelope bbox = new ReferencedEnvelope();
        if (q != null && q.getFilter() != null) {
            bbox = (Envelope) q.getFilter().accept(ExtractBoundsFilterVisitor.BOUNDS_VISITOR, bbox);
            if (bbox == null) {
                bbox = new ReferencedEnvelope();
            }
        }

        // see if we can use indexing to speedup the data access
        Filter filter = q != null ? q.getFilter() : null;
        IndexManager indexManager = getDataStore().indexManager;
        @SuppressWarnings("PMD.CloseResource") // eventually gets returned and managed in the reader
        CloseableIterator<Data> goodRecs = null;
        if (getDataStore().isFidIndexed() && filter instanceof Id && indexManager.hasFidIndex(false)) {
            Id fidFilter = (Id) filter;
            if (indexManager.isIndexStale(ShpFileType.FIX)) {
                indexManager.createFidIndex();
            }
            List<Data> records = indexManager.queryFidIndex(fidFilter);
            if (records != null) {
                goodRecs = new CloseableIteratorWrapper<>(records.iterator());
            }
        } else if (getDataStore().isIndexed()
                && !bbox.isNull()
                && !Double.isInfinite(bbox.getWidth())
                && !Double.isInfinite(bbox.getHeight())) {
            try {
                if (indexManager.isSpatialIndexAvailable() || getDataStore().isIndexCreationEnabled()) {
                    goodRecs = indexManager.querySpatialIndex(bbox);
                }
            } catch (TreeException e) {
                throw new IOException("Error querying index: " + e.getMessage());
            }
        }
        // do we have anything to read at all? If not don't bother opening all the files
        if (goodRecs != null && !goodRecs.hasNext()) {
            LOGGER.log(Level.FINE, "Empty results for " + resultSchema.getName().getLocalPart() + ", skipping read");
            goodRecs.close();
            return new EmptyFeatureReader<>(resultSchema);
        }

        // get the .fix file reader, if we have a .fix file
        @SuppressWarnings("PMD.CloseResource") // wrapped and returned
        IndexedFidReader fidReader = null;
        if (getDataStore().isFidIndexed() && indexManager.hasFidIndex(false)) {
            fidReader = new IndexedFidReader(shpFiles);
        }

        // setup the feature readers
        ShapefileSetManager shpManager = getDataStore().shpManager;
        @SuppressWarnings("PMD.CloseResource") // managed as a field of the return value
        ShapefileReader shapeReader = null;
        final boolean shpFileMayExist = !shpManager.shpFiles.isLocal() || shpManager.shpFiles.exists(ShpFileType.SHP);
        if (shpFileMayExist) {
            try {
                @SuppressWarnings("PMD.CloseResource") // managed as a field of the return value
                final ShapefileReader sr = shpManager.openShapeReader(geometryFactory, goodRecs != null);
                shapeReader = sr;
            } catch (final FileNotFoundException e) {
                final String format = "Ignoring missing shp-file and moving on: %s";
                LOGGER.fine(() -> String.format(format, e.getMessage()));
            }
        } else {
            LOGGER.fine("Ignoring missing shp-file and moving on.");
        }
        @SuppressWarnings("PMD.CloseResource") // managed as a field of the return value
        DbaseFileReader dbfReader = null;
        List<AttributeDescriptor> attributes = readSchema.getAttributeDescriptors();
        if (attributes.isEmpty() || attributes.size() == 1 && readSchema.getGeometryDescriptor() != null) {
            LOGGER.fine("The DBF file won't be opened since no attributes will be read from it");
        } else {
            dbfReader = shpManager.openDbfReader(goodRecs != null);
        }
        ShapefileFeatureReader reader;
        if (goodRecs != null) {
            reader = new IndexedShapefileFeatureReader(readSchema, shapeReader, dbfReader, fidReader, goodRecs);
        } else {
            reader = new ShapefileFeatureReader(readSchema, shapeReader, dbfReader, fidReader);
        }
        if (filter != null && !Filter.INCLUDE.equals(filter)) {
            reader.setFilter(filter);
        }

        // setup the target bbox if any, and the generalization hints if available
        if (q != null) {
            if (bbox != null && !bbox.isNull()) {
                reader.setTargetBBox(bbox);
            }

            Hints hints = q.getHints();
            if (hints != null) {
                Number simplificationDistance = (Number) hints.get(Hints.GEOMETRY_DISTANCE);
                if (simplificationDistance != null) {
                    reader.setSimplificationDistance(simplificationDistance.doubleValue());
                }
                reader.setScreenMap((ScreenMap) hints.get(Hints.SCREENMAP));

                if (Boolean.TRUE.equals(hints.get(Hints.FEATURE_2D))) {
                    shapeReader.setFlatGeometry(true);
                }
            }
        }

        // do the retyping
        if (!FeatureTypes.equals(readSchema, resultSchema)) {
            return new ReTypeFeatureReader(reader, resultSchema);
        } else {
            return reader;
        }
    }

    SimpleFeatureType getResultSchema(Query q) {
        if (q.getPropertyNames() == null) {
            return getSchema();
        } else {
            return SimpleFeatureTypeBuilder.retype(getSchema(), q.getPropertyNames());
        }
    }

    SimpleFeatureType getReadSchema(Query q) {
        if (q.getPropertyNames() == Query.ALL_NAMES) {
            return getSchema();
        }
        // Step 1: start with requested property names
        LinkedHashSet<String> attributes = new LinkedHashSet<>();
        attributes.addAll(Arrays.asList(q.getPropertyNames()));

        Filter filter = q.getFilter();
        if (filter != null && !Filter.INCLUDE.equals(filter)) {
            // Step 2: Add query attributes (if needed)
            // Step 3: Fill empty XPath with appropriate property names
            FilterAttributeExtractor fat = new AbsoluteAttributeExtractor(getSchema());
            filter.accept(fat, null);
            attributes.addAll(fat.getAttributeNameSet());
        }
        return SimpleFeatureTypeBuilder.retype(getSchema(), new ArrayList<>(attributes));
    }

    /** Builds the most appropriate geometry factory depending on the available query hints */
    protected GeometryFactory getGeometryFactory(Query query) {
        // if no hints, use the default geometry factory
        if (query == null || query.getHints() == null) {
            final GeometryFactory geometryFactory = entry.getDataStore().getGeometryFactory();
            return geometryFactory != null ? geometryFactory : new GeometryFactory();
        }

        // grab a geometry factory... check for a special hint
        Hints hints = query.getHints();
        GeometryFactory geometryFactory = (GeometryFactory) hints.get(Hints.JTS_GEOMETRY_FACTORY);
        if (geometryFactory == null) {
            // look for a coordinate sequence factory
            CoordinateSequenceFactory csFactory =
                    (CoordinateSequenceFactory) hints.get(Hints.JTS_COORDINATE_SEQUENCE_FACTORY);

            if (csFactory != null) {
                geometryFactory = new GeometryFactory(csFactory);
            } else {
                geometryFactory = entry.getDataStore().getGeometryFactory();
            }
        }

        if (geometryFactory == null) {
            // fall back on the default one
            geometryFactory = new GeometryFactory();
        }
        return geometryFactory;
    }

    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {
        List<AttributeDescriptor> types = readAttributes();

        SimpleFeatureType parent = null;
        final GeometryDescriptor geomDescriptor =
                types.get(0) instanceof GeometryDescriptor ? (GeometryDescriptor) types.get(0) : null;
        if (geomDescriptor != null) {
            Class<?> geomBinding = geomDescriptor.getType().getBinding();

            if (geomBinding == Point.class || geomBinding == MultiPoint.class) {
                parent = BasicFeatureTypes.POINT;
            } else if (geomBinding == Polygon.class || geomBinding == MultiPolygon.class) {
                parent = BasicFeatureTypes.POLYGON;
            } else if (geomBinding == LineString.class || geomBinding == MultiLineString.class) {
                parent = BasicFeatureTypes.LINE;
            }
        }

        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        if (geomDescriptor != null) {
            builder.setDefaultGeometry(geomDescriptor.getLocalName());
        }
        builder.addAll(types);
        builder.setName(entry.getName());
        builder.setAbstract(false);
        if (parent != null) {
            builder.setSuperType(parent);
        }
        return builder.buildFeatureType();
    }

    /**
     * Create the AttributeDescriptor contained within this DataStore.
     *
     * @return List of new AttributeDescriptor
     * @throws IOException If AttributeType reading fails
     */
    @SuppressWarnings("PMD.UseTryWithResources") // resources might be null
    protected List<AttributeDescriptor> readAttributes() throws IOException {
        ShapefileSetManager shpManager = getDataStore().shpManager;
        PrjFileReader prj = null;
        ShapefileReader shp = null;
        DbaseFileReader dbf = null;
        CoordinateReferenceSystem crs = null;

        AttributeTypeBuilder build = new AttributeTypeBuilder();
        List<AttributeDescriptor> attributes = new ArrayList<>();
        try {
            try {
                shp = shpManager.openShapeReader(new GeometryFactory(), false);
            } catch (final FileNotFoundException e) {
                final String format = "Ignoring missing shp-file and moving on: %s";
                LOGGER.fine(() -> String.format(format, e.getMessage()));
            }
            dbf = shpManager.openDbfReader(false);
            try {
                prj = shpManager.openPrjReader();

                if (prj != null) {
                    crs = prj.getCoordinateReferenceSystem();
                }
            } catch (FactoryException fe) {
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.log(Level.FINER, "Ignoring invalid prj file and moving on: " + fe.getMessage());
                }
                crs = null;
            }
            Set<String> usedNames = new HashSet<>(); // record names in case of duplicates
            if (shp != null) {
                Class<? extends Geometry> geometryClass =
                        JTSUtilities.findBestGeometryClass(shp.getHeader().getShapeType());
                build.setName(Classes.getShortName(geometryClass));
                build.setNillable(true);
                build.setCRS(crs);
                build.setBinding(geometryClass);

                GeometryType geometryType = build.buildGeometryType();
                attributes.add(build.buildDescriptor(BasicFeatureTypes.GEOMETRY_ATTRIBUTE_NAME, geometryType));
                usedNames.add(BasicFeatureTypes.GEOMETRY_ATTRIBUTE_NAME);
            }

            // take care of the case where no dbf and query wants all =>
            // geometry only
            if (dbf != null) {
                DbaseFileHeader header = dbf.getHeader();
                for (int i = 0, ii = header.getNumFields(); i < ii; i++) {
                    Class<?> attributeClass = header.getFieldClass(i);
                    String name = header.getFieldName(i);
                    if (usedNames.contains(name)) {
                        String origional = name;
                        int count = 1;
                        name = name + count;
                        while (usedNames.contains(name)) {
                            count++;
                            name = origional + count;
                        }
                        build.addUserData(ShapefileDataStore.ORIGINAL_FIELD_NAME, origional);
                        build.addUserData(ShapefileDataStore.ORIGINAL_FIELD_DUPLICITY_COUNT, count);
                    }
                    usedNames.add(name);
                    int length = header.getFieldLength(i);

                    build.setNillable(true);
                    build.setLength(length);
                    build.setBinding(attributeClass);
                    attributes.add(build.buildDescriptor(name));
                }
            }
            return attributes;
        } finally {

            try {
                if (prj != null) {
                    prj.close();
                }
            } catch (IOException ioe) {
                // do nothing
            }
            try {
                if (dbf != null) {
                    dbf.close();
                }
            } catch (IOException ioe) {
                // do nothing
            }
            try {
                if (shp != null) {
                    shp.close();
                }
            } catch (IOException ioe) {
                // do nothing
            }
        }
    }

    @Override
    protected boolean handleVisitor(Query query, FeatureVisitor visitor) throws IOException {
        return super.handleVisitor(query, visitor);
    }
}
