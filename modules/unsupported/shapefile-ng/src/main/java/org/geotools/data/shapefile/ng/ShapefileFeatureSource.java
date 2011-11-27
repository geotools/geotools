/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile.ng;

import static org.geotools.data.shapefile.ng.files.ShpFileType.*;

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

import org.geotools.data.DataSourceException;
import org.geotools.data.EmptyFeatureReader;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.FilteringFeatureReader;
import org.geotools.data.Query;
import org.geotools.data.ReTypeFeatureReader;
import org.geotools.data.shapefile.ng.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.ng.dbf.DbaseFileReader;
import org.geotools.data.shapefile.ng.fid.IndexedFidReader;
import org.geotools.data.shapefile.ng.files.FileReader;
import org.geotools.data.shapefile.ng.files.ShpFiles;
import org.geotools.data.shapefile.ng.index.CloseableIterator;
import org.geotools.data.shapefile.ng.index.Data;
import org.geotools.data.shapefile.ng.index.TreeException;
import org.geotools.data.shapefile.ng.prj.PrjFileReader;
import org.geotools.data.shapefile.ng.shp.IndexFile;
import org.geotools.data.shapefile.ng.shp.JTSUtilities;
import org.geotools.data.shapefile.ng.shp.ShapefileHeader;
import org.geotools.data.shapefile.ng.shp.ShapefileReader;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.Hints;
import org.geotools.factory.Hints.Key;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.type.BasicFeatureTypes;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.filter.visitor.ExtractBoundsFilterVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.renderer.ScreenMap;
import org.geotools.resources.Classes;
import org.geotools.util.logging.Logging;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.GeometryType;
import org.opengis.filter.Filter;
import org.opengis.filter.Id;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * A {@link FeatureSource} for shapefiles based on {@link ContentFeatureSource}
 * 
 * @author Andrea Aime - GeoSolutions
 */
class ShapefileFeatureSource extends ContentFeatureSource {

    static final Logger LOGGER = Logging.getLogger(ShapefileFeatureSource.class);

    ShpFiles shpFiles;

    public ShapefileFeatureSource(ContentEntry entry, ShpFiles shpFiles) {
        super(entry, Query.ALL);
        this.shpFiles = shpFiles;
        HashSet<Key> hints = new HashSet<Hints.Key>();
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
    protected boolean canFilter() {
        return true;
    }

    @Override
    protected boolean canRetype() {
        return true;
    }

    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        if (query.getFilter() != Filter.INCLUDE) {
            return null;
        }

        ReadableByteChannel in = null;
        try {
            ByteBuffer buffer = ByteBuffer.allocate(100);
            FileReader reader = new FileReader() {
                public String id() {
                    return "Shapefile Datastore's getBounds Method";
                }
            };

            in = shpFiles.getReadChannel(SHP, reader);
            try {
                in.read(buffer);
                buffer.flip();

                ShapefileHeader header = new ShapefileHeader();
                header.read(buffer, true);

                SimpleFeatureType schema = getSchema();
                ReferencedEnvelope bounds = new ReferencedEnvelope(
                        schema.getCoordinateReferenceSystem());
                bounds.include(header.minX(), header.minY());
                bounds.include(header.minX(), header.minY());

                Envelope env = new Envelope(header.minX(), header.maxX(), header.minY(),
                        header.maxY());

                CoordinateReferenceSystem crs = null;
                if (schema != null) {
                    crs = schema.getCoordinateReferenceSystem();
                }
                return new ReferencedEnvelope(env, crs);
            } finally {
                in.close();
            }

        } catch (IOException ioe) {
            throw new DataSourceException("Problem getting Bbox", ioe);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ioe) {
                // do nothing
            }
        }
    }

    @Override
    protected int getCountInternal(Query query) throws IOException {
        if (query.getFilter() == Filter.INCLUDE) {
            IndexFile file = getDataStore().shpManager.openIndexFile();
            if (file != null) {
                try {
                    return file.getRecordCount();
                } finally {
                    file.close();
                }
            }

            // no Index file so use the number of shapefile records
            ShapefileReader reader = getDataStore().shpManager.openShapeReader(
                    new GeometryFactory(), false);
            int count = -1;

            try {
                count = reader.getCount(count);
            } catch (IOException e) {
                throw e;
            } finally {
                reader.close();
            }

            return count;

        }

        return -1;

    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query q)
            throws IOException {
        SimpleFeatureType resultSchema = getResultSchema(q);
        SimpleFeatureType readSchema = getReadSchema(q);
        GeometryFactory geometryFactory = getGeometryFactory(q);

        // grab the target bbox, if any
        Envelope bbox = new ReferencedEnvelope();
        if (q.getFilter() != null) {
            bbox = (Envelope) q.getFilter().accept(ExtractBoundsFilterVisitor.BOUNDS_VISITOR, bbox);
        }

        // see if we can use indexing to speedup the data access
        Filter filter = q != null ? q.getFilter() : null;
        IndexManager indexManager = getDataStore().indexManager;
        CloseableIterator<Data> goodRecs = null;
        if (getDataStore().isFidIndexed() && filter instanceof Id && indexManager.hasFidIndex(false)) {
            Id fidFilter = (Id) filter;
            List<Data> records = indexManager.queryFidIndex(fidFilter);
            if (records != null) {
                goodRecs = new CloseableIteratorWrapper<Data>(records.iterator());
            }
        } else if (getDataStore().isIndexed() && !bbox.isNull()
                && !Double.isInfinite(bbox.getWidth()) && !Double.isInfinite(bbox.getHeight())) {
            try {
                goodRecs = indexManager.querySpatialIndex(bbox);
            } catch (TreeException e) {
                throw new IOException("Error querying index: " + e.getMessage());
            }
        }
        // do we have anything to read at all? If not don't bother opening all the files
        if (goodRecs != null && !goodRecs.hasNext()) {
            LOGGER.log(Level.FINE, "Empty results for " + resultSchema.getName().getLocalPart()
                    + ", skipping read");
            goodRecs.close();
            return new EmptyFeatureReader<SimpleFeatureType, SimpleFeature>(resultSchema);
        }
        
        // get the .fix file reader, if we have a .fix file
        IndexedFidReader fidReader = null;
        if (getDataStore().isFidIndexed() && filter instanceof Id && indexManager.hasFidIndex(false)) {
            fidReader = new IndexedFidReader(shpFiles);
        }

        // setup the feature readers
        ShapefileSetManager shpManager = getDataStore().shpManager;
        ShapefileReader shapeReader = shpManager.openShapeReader(geometryFactory, goodRecs != null);
        DbaseFileReader dbfReader = null;
        List<AttributeDescriptor> attributes = readSchema.getAttributeDescriptors();
        if (attributes.size() < 1
                || (attributes.size() == 1 && readSchema.getGeometryDescriptor() != null)) {
            LOGGER.fine("The DBF file won't be opened since no attributes will be read from it");
        } else {
            dbfReader = shpManager.openDbfReader(goodRecs != null);
        }
        ShapefileFeatureReader result;
        if (goodRecs != null) {
            result = new IndexedShapefileFeatureReader(readSchema, shapeReader, dbfReader, fidReader, 
                    goodRecs);
        } else {
            result = new ShapefileFeatureReader(readSchema, shapeReader, dbfReader, fidReader);
        }

        // setup the target bbox if any, and the generalization hints if available
        if (q != null) {
            if (bbox != null && !bbox.isNull()) {
                result.setTargetBBox(bbox);
            }

            Hints hints = q.getHints();
            if (hints != null) {
                Number simplificationDistance = (Number) hints.get(Hints.GEOMETRY_DISTANCE);
                if (simplificationDistance != null) {
                    result.setSimplificationDistance(simplificationDistance.doubleValue());
                }
                result.setScreenMap((ScreenMap) hints.get(Hints.SCREENMAP));

                if (Boolean.TRUE.equals(hints.get(Hints.FEATURE_2D))) {
                    shapeReader.setFlatGeometry(true);
                }
            }

        }

        // do the filtering
        FeatureReader<SimpleFeatureType, SimpleFeature> reader;
        if(filter != null && !Filter.INCLUDE.equals(filter)) {
            reader = new FilteringFeatureReader<SimpleFeatureType, SimpleFeature>(result, filter);
        } else {
            reader = result;
        }
        
        // do the retyping
        if(!FeatureTypes.equals(readSchema, resultSchema)) {
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
        if (q.getPropertyNames() == null) {
            return getSchema();
        } else {
            LinkedHashSet<String> attributes = new LinkedHashSet<String>();
            attributes.addAll(Arrays.asList(q.getPropertyNames()));
            Filter filter = q.getFilter();
            if(filter != null && !Filter.INCLUDE.equals(filter)) {
                FilterAttributeExtractor fat = new FilterAttributeExtractor();
                filter.accept(fat, null);
                attributes.addAll(fat.getAttributeNameSet());
            }
            
            return SimpleFeatureTypeBuilder.retype(getSchema(), new ArrayList<String>(attributes));
        }
    }

    /**
     * Builds the most appropriate geometry factory depending on the available query hints
     * 
     * @param query
     * @return
     */
    protected GeometryFactory getGeometryFactory(Query query) {
        // if no hints, use the default geometry factory
        if (query == null || query.getHints() == null) {
            return new GeometryFactory();
        }

        // grab a geometry factory... check for a special hint
        Hints hints = query.getHints();
        GeometryFactory geometryFactory = (GeometryFactory) hints.get(Hints.JTS_GEOMETRY_FACTORY);
        if (geometryFactory == null) {
            // look for a coordinate sequence factory
            CoordinateSequenceFactory csFactory = (CoordinateSequenceFactory) hints
                    .get(Hints.JTS_COORDINATE_SEQUENCE_FACTORY);

            if (csFactory != null) {
                geometryFactory = new GeometryFactory(csFactory);
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
        GeometryDescriptor geomDescriptor = (GeometryDescriptor) types.get(0);
        Class<?> geomBinding = geomDescriptor.getType().getBinding();

        if ((geomBinding == Point.class) || (geomBinding == MultiPoint.class)) {
            parent = BasicFeatureTypes.POINT;
        } else if ((geomBinding == Polygon.class) || (geomBinding == MultiPolygon.class)) {
            parent = BasicFeatureTypes.POLYGON;
        } else if ((geomBinding == LineString.class) || (geomBinding == MultiLineString.class)) {
            parent = BasicFeatureTypes.LINE;
        }

        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setDefaultGeometry(geomDescriptor.getLocalName());
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
    protected List<AttributeDescriptor> readAttributes() throws IOException {
        ShapefileSetManager shpManager = getDataStore().shpManager;
        ShapefileReader shp = shpManager.openShapeReader(new GeometryFactory(), false);
        DbaseFileReader dbf = shpManager.openDbfReader(false);
        CoordinateReferenceSystem crs = null;

        PrjFileReader prj = null;
        try {
            prj = shpManager.openPrjReader();

            if (prj != null) {
                crs = prj.getCoodinateSystem();
            }
        } catch (FactoryException fe) {
            crs = null;
        }

        AttributeTypeBuilder build = new AttributeTypeBuilder();
        List<AttributeDescriptor> attributes = new ArrayList<AttributeDescriptor>();
        try {
            Class<?> geometryClass = JTSUtilities.findBestGeometryClass(shp.getHeader()
                    .getShapeType());
            build.setName(Classes.getShortName(geometryClass));
            build.setNillable(true);
            build.setCRS(crs);
            build.setBinding(geometryClass);

            GeometryType geometryType = build.buildGeometryType();
            attributes.add(build.buildDescriptor(BasicFeatureTypes.GEOMETRY_ATTRIBUTE_NAME,
                    geometryType));
            Set<String> usedNames = new HashSet<String>(); // record names in
            // case of
            // duplicates
            usedNames.add(BasicFeatureTypes.GEOMETRY_ATTRIBUTE_NAME);

            // take care of the case where no dbf and query wants all =>
            // geometry only
            if (dbf != null) {
                DbaseFileHeader header = dbf.getHeader();
                for (int i = 0, ii = header.getNumFields(); i < ii; i++) {
                    Class attributeClass = header.getFieldClass(i);
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
