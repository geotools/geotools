/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.DataSourceException;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.util.URLs;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;

/**
 * Class specializing in dumping a feature collection onto one or more shapefiles into a target
 * directory.
 *
 * <p>The collection will be distributed among different shapefiles if needed do respect certain
 * limitations:
 *
 * <ul>
 *   <li>Only a single geometry type per shapefile, in case the source feature collection contains
 *       more than one parallel shapefiles will be generated, by default appending the type of
 *       geometry at the end of the file name
 *   <li>Maximum file size, by default, 2GB for the shp file, 4GB for the dbf file. In case the
 *       maximum size is exceeded the code will create a new shapefile appending a counter at the
 *       end of the file name
 * </ul>
 *
 * @author Andrea Aime - GeoSolutions
 */
public class ShapefileDumper {

    private class StoreWriter {
        int currentFileId = 0;

        ShapefileDataStore dstore;

        FeatureWriter<SimpleFeatureType, SimpleFeature> writer;

        SimpleFeatureType schema;

        /** */
        public StoreWriter(SimpleFeatureType schema)
                throws MalformedURLException, FileNotFoundException, IOException {
            // create the datastore for the current geom type
            this.schema = schema;
            createStoreAndWriter(schema);
        }

        private void createStoreAndWriter(SimpleFeatureType schema)
                throws MalformedURLException, FileNotFoundException, IOException {
            this.dstore = buildStore(schema);
            this.writer = dstore.getFeatureWriter(schema.getTypeName(), Transaction.AUTO_COMMIT);
        }

        public void nextWriter() throws IOException {
            // close the old shapefile
            this.writer.close();
            this.dstore.dispose();

            // prepare the new one
            currentFileId++;
            SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
            tb.init(schema);
            tb.setName(schema.getTypeName() + String.valueOf(currentFileId));
            SimpleFeatureType ft = tb.buildFeatureType();

            // set it up at the current store and writer
            createStoreAndWriter(ft);
        }
    }

    static final Logger LOGGER = Logging.getLogger(ShapefileDumper.class);

    File targetDirectory;

    long maxShpSize = ShapefileFeatureWriter.DEFAULT_MAX_SHAPE_SIZE;

    long maxDbfSize = ShapefileFeatureWriter.DEFAULT_MAX_DBF_SIZE;

    boolean emptyShapefileAllowed = true;

    Charset charset = (Charset) ShapefileDataStoreFactory.DBFCHARSET.getDefaultValue();

    public ShapefileDumper(File targetDirectory) {
        this.targetDirectory = targetDirectory;
    }

    /** Maximum size of the shapefiles being generated */
    public long getMaxShpSize() {
        return maxShpSize;
    }

    /**
     * Sets the maximum size of the shp files the dumper will generate. The default is 2GB. When the
     * threshold is reached a new shapefile with a progressive number at the end will be written to
     * continue dumping features.
     */
    public void setMaxShpSize(long maxShapeSize) {
        this.maxShpSize = maxShapeSize;
    }

    /** Maximums size of the DBF files being generated */
    public long getMaxDbfSize() {
        return maxDbfSize;
    }

    /**
     * Sets the maximum size of the DBF files the dumper will generate. The default is 4GB, but some
     * systems might be able to only read DBF files up to 2GB. When the threshold is reached a new
     * shapefile with a progressive number at the end will be written to continue dumping features.
     */
    public void setMaxDbfSize(long maxDbfSize) {
        this.maxDbfSize = maxDbfSize;
    }

    /** The charset used in the DBF files. It's ISO-8859-1 by default (per DBF spec) */
    public Charset getCharset() {
        return charset;
    }

    /** Sets the charset used to dump the DBF files. */
    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    /** Returns true if empty shpaefile dumping is allowed (true by default) */
    public boolean isEmptyShapefileAllowed() {
        return emptyShapefileAllowed;
    }

    /** Settings this flag to false will avoid empty shapefiles to be created */
    public void setEmptyShapefileAllowed(boolean emptyShapefileAllowed) {
        this.emptyShapefileAllowed = emptyShapefileAllowed;
    }

    /**
     * Dumps the collection into one or more shapefiles. Multiple files will be geneated when the
     * input collection contains multiple geometry types, or as the size limit for output files get
     * reached
     *
     * @param fc The input feature collection
     * @return True if at least one feature got written, false otherwise
     */
    public boolean dump(SimpleFeatureCollection fc) throws IOException {
        return dump(null, fc);
    }

    /**
     * Dumps the collection into one or more shapefiles. Multiple files will be geneated when the
     * input collection contains multiple geometry types, or as the size limit for output files get
     * reached
     *
     * @param fc The input feature collection
     * @return True if at least one feature got written, false otherwise
     */
    public boolean dump(String fileName, SimpleFeatureCollection fc) throws IOException {
        // make sure we are not trying to write out a geometryless data set
        if (fc.getSchema().getGeometryDescriptor() == null) {
            throw new DataSourceException(
                    "Cannot write geometryless shapefiles, yet "
                            + fc.getSchema()
                            + " has no geometry field");
        }

        // Takes a feature collection with a generic schema and remaps it to one whose schema
        // respects the limitations of the shapefile format
        fc = RemappingFeatureCollection.getShapefileCompatibleCollection(fc);
        SimpleFeatureType schema = fc.getSchema();

        Map<Class<?>, StoreWriter> writers = new HashMap<Class<?>, StoreWriter>();
        boolean featuresWritten = false;
        Class<?> geomType = schema.getGeometryDescriptor().getType().getBinding();
        // let's see if we will need to write multiple geometry types
        boolean multiWriter =
                GeometryCollection.class.equals(geomType) || Geometry.class.equals(geomType);
        // we write all the features with no geometry type defined and NULL geometries to the same
        // file
        StoreWriter nullStoreWriter = null;
        try (SimpleFeatureIterator it = fc.features()) {
            while (it.hasNext()) {
                SimpleFeature f = it.next();
                // if the geometry type is not defined and the geometry value is NULL we write it
                // to the NULL geometries file otherwise we write it to the correspondent geometry
                // file
                StoreWriter storeWriter;
                if (multiWriter && f.getDefaultGeometry() == null) {
                    // lazy instantiation of NULL geometries writer
                    nullStoreWriter =
                            nullStoreWriter == null
                                    ? getStoreWriter(
                                            fileName,
                                            schema,
                                            null,
                                            multiWriter,
                                            Point.class,
                                            "_NULL")
                                    : nullStoreWriter;
                    storeWriter = nullStoreWriter;
                } else {
                    storeWriter = getStoreWriter(fileName, f, writers, multiWriter);
                }
                // try to write, the shapefile size limits could be reached
                try {
                    writeToShapefile(f, storeWriter.writer);
                } catch (ShapefileSizeException e) {
                    // make one attempt to move to the next file (just one, since
                    // we could be trying to write a feature that won't fit the size limits)
                    storeWriter.nextWriter();
                    writeToShapefile(f, storeWriter.writer);
                }
                featuresWritten = true;
            }

            // force writing out a empty shapefile if required
            if (!featuresWritten && emptyShapefileAllowed) {
                if (multiWriter) {
                    // force the dump of a point file
                    getStoreWriter(fileName, fc.getSchema(), writers, true, Point.class, null);
                } else {
                    getStoreWriter(fileName, fc.getSchema(), writers, false, geomType, null);
                }
            }

        } catch (ShapefileSizeException e) {
            throw e;
        } catch (IOException ioe) {
            LOGGER.log(
                    Level.WARNING,
                    "Error while writing featuretype '" + schema.getTypeName() + "' to shapefile.",
                    ioe);
            throw new IOException(ioe);
        } finally {
            // close all writers, dispose all datastores, even if an exception occurs
            // during closeup (shapefile datastore will have to copy the shapefiles, that migh
            // fail in many ways)
            IOException stored = null;
            // add the not defined and NULL geometries store writer if defined
            List<StoreWriter> writersValues = new ArrayList<>(writers.values());
            if (nullStoreWriter != null) {
                writersValues.add(nullStoreWriter);
            }
            for (StoreWriter sw : writersValues) {
                try {
                    SimpleFeatureType writerSchema = sw.dstore.getSchema();
                    sw.writer.close();
                    sw.dstore.dispose();
                    // notify subclasses that the file has been completed
                    shapefileDumped(writerSchema.getTypeName(), writerSchema);
                } catch (IOException e) {
                    stored = e;
                }
            }
            // if an exception occurred make the world aware of it
            if (stored != null) {
                throw new IOException(stored);
            }
        }

        return featuresWritten;
    }

    private void writeToShapefile(
            SimpleFeature f, FeatureWriter<SimpleFeatureType, SimpleFeature> writer)
            throws IOException {
        SimpleFeature fw = writer.next();

        // we cannot trust attribute order, shapefile changes the location and name of the geometry
        for (AttributeDescriptor d : fw.getFeatureType().getAttributeDescriptors()) {
            fw.setAttribute(d.getLocalName(), f.getAttribute(d.getLocalName()));
        }
        fw.setDefaultGeometry(f.getDefaultGeometry());
        writer.write();
    }

    /**
     * Allows subsclasses to perform extra actions against a shapefile that was completely written.
     */
    protected void shapefileDumped(String fileName, SimpleFeatureType remappedSchema)
            throws IOException {
        // By default nothing extra is done
    }

    /** Creates a shapefile data store for the specified schema */
    private ShapefileDataStore buildStore(SimpleFeatureType schema)
            throws MalformedURLException, FileNotFoundException, IOException {
        File file = new File(targetDirectory, schema.getTypeName() + ".shp");
        ShapefileDataStore sfds = new ShapefileDataStore(URLs.fileToUrl(file));

        // handle shapefile encoding
        // and dump the charset into a .cst file, for debugging and control purposes
        // (.cst is not a standard extension)
        sfds.setCharset(charset);
        File charsetFile = new File(targetDirectory, schema.getTypeName() + ".cst");
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(charsetFile);
            pw.write(charset.name());
        } finally {
            if (pw != null) pw.close();
        }

        // create the shapefile
        try {
            sfds.createSchema(schema);
        } catch (NullPointerException e) {
            LOGGER.warning(
                    "Error in shapefile schema. It is possible you don't have a geometry set in the output. \n"
                            + "Please specify a <wfs:PropertyName>geom_column_name</wfs:PropertyName> in the request");
            throw new IOException(
                    "Error in shapefile schema. It is possible you don't have a geometry set in the output.");
        }

        // create the prj file
        try {
            if (schema.getCoordinateReferenceSystem() != null) {
                sfds.forceSchemaCRS(schema.getCoordinateReferenceSystem());
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Could not properly create the .prj file", e);
        }

        // enforce the limits
        sfds.setMaxShpSize(this.maxShpSize);
        sfds.setMaxDbfSize(this.maxDbfSize);

        return sfds;
    }

    private Map<String, Object> getGeometryType(SimpleFeature f) {
        Class<?> target;
        String geometryType = null;

        Geometry g = (Geometry) f.getDefaultGeometry();
        if (g instanceof Point) {
            target = Point.class;
            geometryType = "Point";
        } else if (g instanceof MultiPoint) {
            target = MultiPoint.class;
            geometryType = "MPoint";
        } else if (g instanceof MultiPolygon || g instanceof Polygon) {
            target = MultiPolygon.class;
            geometryType = "Polygon";
        } else if (g instanceof LineString || g instanceof MultiLineString) {
            target = MultiLineString.class;
            geometryType = "Line";
        } else {
            throw new RuntimeException(
                    "This should never happen, "
                            + "there's a bug in the SHAPE-ZIP output format. I got a geometry of type "
                            + g.getClass());
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("target", target);
        map.put("geometryType", geometryType);
        return map;
    }

    /**
     * Returns the feature writer for a specific geometry type, creates a new datastore and a new
     * writer if there are none so far
     */
    private StoreWriter getStoreWriter(
            String fileName,
            SimpleFeature f,
            Map<Class<?>, StoreWriter> writers,
            boolean multiWriter)
            throws IOException {

        // get the target class
        Class<?> target = null;
        String geometryType = null;
        if (multiWriter) {
            // geometry type is not defined (we have the generic Geometry type) so we iterate
            // over all geometries objects and extract the correct type
            Map<String, Object> map = getGeometryType(f);
            target = (Class<?>) map.get("target");
            geometryType = (String) map.get("geometryType");
        } else {
            // we have a specific geometry type defined (Point, LineString, etc ...)
            target = Geometry.class;
            geometryType = "Geometry";
        }

        return getStoreWriter(
                fileName, f.getFeatureType(), writers, multiWriter, target, geometryType);
    }

    private StoreWriter getStoreWriter(
            String fileName,
            SimpleFeatureType original,
            Map<Class<?>, StoreWriter> writers,
            boolean multiWriter,
            Class<?> target,
            String geometryType)
            throws IOException {
        // see if we already have a cached writer
        StoreWriter storeWriter = writers != null ? writers.get(target) : null;
        if (storeWriter == null) {
            // retype the schema
            SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
            for (AttributeDescriptor d : original.getAttributeDescriptors()) {
                if (Geometry.class.isAssignableFrom(d.getType().getBinding()) && multiWriter) {
                    GeometryDescriptor gd = (GeometryDescriptor) d;
                    builder.add(gd.getLocalName(), target, gd.getCoordinateReferenceSystem());
                    builder.setDefaultGeometry(gd.getLocalName());
                } else {
                    builder.add(d);
                }
            }
            builder.setNamespaceURI(original.getName().getURI());

            // we need to associate the geometry type to the file name only if we can have be
            // multiple types
            if (multiWriter) {
                fileName = getShapeName(fileName, original, geometryType);
            } else {
                fileName = getShapeName(fileName, original, null);
            }
            builder.setName(fileName);

            SimpleFeatureType retyped = builder.buildFeatureType();

            // cache it if cache map provided
            storeWriter = new StoreWriter(retyped);
            if (writers != null) {
                writers.put(target, storeWriter);
            }
        }
        return storeWriter;
    }

    /**
     * Returns the shapefile name from the given schema and geometry type. By default it's simple
     * typeName and geometryType concatenated, subclasses can override this behavior
     *
     * <p>This method will only be called if a custom name was not provided.
     *
     * @param geometryType The name of the geometry type, will be null if there is no need for a
     *     geometry type suffix
     */
    protected String getShapeName(SimpleFeatureType schema, String geometryType) {
        return getShapeName(schema.getTypeName(), geometryType);
    }

    /**
     * Returns the shape name from the given suggested name (if available), schema and geometry
     * type.
     */
    private String getShapeName(String fileName, SimpleFeatureType schema, String geometryType) {
        if (fileName == null) {
            return getShapeName(schema, geometryType);
        } else {
            return getShapeName(fileName, geometryType);
        }
    }

    /** Returns the shape name from the given suggested name and geometry type. */
    private String getShapeName(String fileName, String geometryType) {
        if (geometryType == null) {
            return fileName;
        } else {
            return fileName + geometryType;
        }
    }
}
