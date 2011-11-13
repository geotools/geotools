package org.geotools.data.shapefile.ng;

import static org.geotools.data.shapefile.ng.files.ShpFileType.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.geotools.data.DataSourceException;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.shapefile.ng.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.ng.dbf.DbaseFileReader;
import org.geotools.data.shapefile.ng.files.FileReader;
import org.geotools.data.shapefile.ng.files.ShpFileType;
import org.geotools.data.shapefile.ng.files.ShpFiles;
import org.geotools.data.shapefile.ng.prj.PrjFileReader;
import org.geotools.data.shapefile.ng.shp.IndexFile;
import org.geotools.data.shapefile.ng.shp.JTSUtilities;
import org.geotools.data.shapefile.ng.shp.ShapefileException;
import org.geotools.data.shapefile.ng.shp.ShapefileHeader;
import org.geotools.data.shapefile.ng.shp.ShapefileReader;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.Hints;
import org.geotools.factory.Hints.Key;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.type.BasicFeatureTypes;
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

public class ShapefileFeatureSource extends ContentFeatureSource {

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
            IndexFile file = openIndexFile();
            if (file != null) {
                try {
                    return file.getRecordCount();
                } finally {
                    file.close();
                }
            }

            // no Index file so use the number of shapefile records
            ShapefileReader reader = openShapeReader(new GeometryFactory());
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
        SimpleFeatureType resultSchema = getResultType(q);
        List<AttributeDescriptor> attributes = resultSchema.getAttributeDescriptors();
        GeometryFactory geometryFactory = getGeometryFactory(q);

        ShapefileReader shapeReader = openShapeReader(geometryFactory);
        ShapefileFeatureReader result;
        if (attributes.size() < 1
                || (attributes.size() == 1 && resultSchema.getGeometryDescriptor() != null)) {
            LOGGER.fine("The DBF file won't be opened since no attributes will be read from it");
            result = new ShapefileFeatureReader(resultSchema, shapeReader, null);
        } else {
            result = new ShapefileFeatureReader(resultSchema, shapeReader, openDbfReader());
        }

        // setup the target bbox if any, and the generalization hints if available
        if (q != null) {
            Envelope bbox = new ReferencedEnvelope();
            bbox = (Envelope) q.getFilter().accept(ExtractBoundsFilterVisitor.BOUNDS_VISITOR, bbox);
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

        return result;
    }

    SimpleFeatureType getResultType(Query q) {
        if (q.getPropertyNames() == null) {
            return getSchema();
        } else {
            return SimpleFeatureTypeBuilder.retype(getSchema(), q.getPropertyNames());
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
        ShapefileReader shp = openShapeReader(new GeometryFactory());
        DbaseFileReader dbf = openDbfReader();
        CoordinateReferenceSystem crs = null;

        PrjFileReader prj = null;
        try {
            prj = openPrjReader();

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

    /**
     * Convenience method for opening a ShapefileReader.
     * 
     * @return A new ShapefileReader.
     * 
     * @throws IOException If an error occurs during creation.
     */
    protected ShapefileReader openShapeReader(GeometryFactory gf) throws IOException {
        try {
            return new ShapefileReader(shpFiles, true, getDataStore().isMemoryMapped(), gf);
        } catch (ShapefileException se) {
            throw new DataSourceException("Error creating ShapefileReader", se);
        }
    }

    /**
     * Convenience method for opening a DbaseFileReader.
     * 
     * @return A new DbaseFileReader
     * 
     * @throws IOException If an error occurs during creation.
     */
    protected DbaseFileReader openDbfReader() throws IOException {
        ShapefileDataStore ds = getDataStore();

        if (shpFiles.get(ShpFileType.DBF) == null) {
            return null;
        }

        if (shpFiles.isLocal() && !shpFiles.exists(DBF)) {
            return null;
        }

        try {
            return new DbaseFileReader(shpFiles, ds.isMemoryMapped(), ds.getCharset(),
                    ds.getTimeZone());
        } catch (IOException e) {
            // could happen if dbf file does not exist
            return null;
        }
    }

    /**
     * Convenience method for opening a DbaseFileReader.
     * 
     * @return A new DbaseFileReader
     * 
     * @throws IOException If an error occurs during creation.
     * @throws FactoryException DOCUMENT ME!
     */
    protected PrjFileReader openPrjReader() throws IOException, FactoryException {

        if (shpFiles.get(PRJ) == null) {
            return null;
        }

        if (shpFiles.isLocal() && !shpFiles.exists(PRJ)) {
            return null;
        }

        try {
            return new PrjFileReader(shpFiles);
        } catch (IOException e) {
            // could happen if prj file does not exist remotely
            return null;
        }
    }

    /**
     * Convenience method for opening an index file.
     * 
     * @return An IndexFile
     * 
     * @throws IOException
     */
    protected IndexFile openIndexFile() throws IOException {
        if (shpFiles.get(SHX) == null) {
            return null;
        }

        if (shpFiles.isLocal() && !shpFiles.exists(SHX)) {
            return null;
        }

        try {
            return new IndexFile(shpFiles, getDataStore().isMemoryMapped());
        } catch (IOException e) {
            // could happen if shx file does not exist remotely
            return null;
        }
    }
    
    @Override
    protected boolean handleVisitor(Query query, FeatureVisitor visitor) throws IOException {
        return super.handleVisitor(query, visitor);
    }

}
