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

import static org.geotools.data.shapefile.files.ShpFileType.DBF;
import static org.geotools.data.shapefile.files.ShpFileType.PRJ;
import static org.geotools.data.shapefile.files.ShpFileType.SHP;
import static org.geotools.data.shapefile.files.ShpFileType.SHX;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import org.geotools.api.data.DataSourceException;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.FeatureWriter;
import org.geotools.api.data.FileDataStore;
import org.geotools.api.data.Query;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.data.shapefile.dbf.DbaseFileException;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.files.ShpFileType;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.files.StorageFile;
import org.geotools.data.shapefile.shp.ShapeType;
import org.geotools.data.shapefile.shp.ShapefileWriter;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.NameImpl;
import org.geotools.referencing.wkt.Formattable;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

public class ShapefileDataStore extends ContentDataStore implements FileDataStore {

    // User-data keyword names for storing the original field name and its
    // instance number when the field name is replaced with a new name built
    // using the original+count convention.
    public static final String ORIGINAL_FIELD_NAME = "original";

    public static final String ORIGINAL_FIELD_DUPLICITY_COUNT = "count";

    public static final Charset DEFAULT_STRING_CHARSET =
            (Charset) ShapefileDataStoreFactory.DBFCHARSET.getDefaultValue();

    public static final TimeZone DEFAULT_TIMEZONE = (TimeZone) ShapefileDataStoreFactory.DBFTIMEZONE.getDefaultValue();

    /**
     * When true, the stack trace that got a lock that wasn't released is recorded and then printed out when warning the
     * user about this.
     */
    protected static final Boolean TRACE_ENABLED = "true".equalsIgnoreCase(System.getProperty("gt2.shapefile.trace"));

    /** The stack trace used to track code that grabs the data store without disposing it */
    Exception trace;

    ShpFiles shpFiles;

    Charset charset = DEFAULT_STRING_CHARSET;

    TimeZone timeZone = DEFAULT_TIMEZONE;

    boolean memoryMapped = false;

    boolean bufferCachingEnabled = true;

    boolean indexed = true;

    boolean indexCreationEnabled = true;

    boolean fidIndexed = true;

    IndexManager indexManager;

    ShapefileSetManager shpManager;

    long maxShpSize = ShapefileFeatureWriter.DEFAULT_MAX_SHAPE_SIZE;

    long maxDbfSize = ShapefileFeatureWriter.DEFAULT_MAX_DBF_SIZE;

    private boolean tryCPGFile = false;

    public ShapefileDataStore(URL url) {
        this(url, ShpFiles.DEFAULT_SKIP_SCAN);
    }

    public ShapefileDataStore(URL url, boolean skipScan) {
        shpFiles = new ShpFiles(url, skipScan);
        if (TRACE_ENABLED) {
            trace = new Exception();
            trace.fillInStackTrace();
        }
        shpManager = new ShapefileSetManager(shpFiles, this);
        indexManager = new IndexManager(shpFiles, this);
    }

    @Override
    protected List<Name> createTypeNames() throws IOException {
        return Collections.singletonList(getTypeName());
    }

    Name getTypeName() {
        String typeName = "Null";
        if (shpFiles != null) {
            typeName = shpFiles.getTypeName();
        }
        return new NameImpl(namespaceURI, typeName);
    }

    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
        return getFeatureSource();
    }

    @Override
    public ContentFeatureSource getFeatureSource() throws IOException {
        ContentEntry entry = ensureEntry(getTypeName());
        if (shpFiles.isWritable()) {
            return new ShapefileFeatureStore(entry, shpFiles);
        } else {
            return new ShapefileFeatureSource(entry, shpFiles);
        }
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public boolean isMemoryMapped() {
        return memoryMapped;
    }

    public void setMemoryMapped(boolean memoryMapped) {
        this.memoryMapped = memoryMapped;
    }

    public boolean isBufferCachingEnabled() {
        return bufferCachingEnabled;
    }

    public void setBufferCachingEnabled(boolean bufferCachingEnabled) {
        this.bufferCachingEnabled = bufferCachingEnabled;
    }

    public boolean isIndexed() {
        return indexed;
    }

    /**
     * When set to true, will use the spatial index if available (but will not create it if missing, unless also
     * indexCreationEnabled is true)
     */
    public void setIndexed(boolean indexed) {
        this.indexed = indexed;
    }

    /** The current max shapefile size */
    long getMaxShpSize() {
        return maxShpSize;
    }

    /** Allows to set the maximum shapefile size (the natural limit of 2GB is used by default) */
    void setMaxShpSize(long maxShapeSize) {
        this.maxShpSize = maxShapeSize;
    }

    /** The current max dbf file size */
    long getMaxDbfSize() {
        return maxDbfSize;
    }

    /** Allows to set the maximum DBF size (the natural limit of 4GB is used by default) */
    void setMaxDbfSize(long maxDbfSize) {
        this.maxDbfSize = maxDbfSize;
    }

    /** Returns true, if the store tries to guess DBF file charset from CPG file */
    public boolean isTryCPGFile() {
        return tryCPGFile;
    }

    /**
     * Makes the store try to figure out DBF file charset from CPG file. If succeeds, the {@link #charset} property will
     * be rewritten by guessed value.
     */
    public void setTryCPGFile(boolean tryCPGFile) {
        this.tryCPGFile = tryCPGFile;
    }

    @Override
    public SimpleFeatureType getSchema() throws IOException {
        return getSchema(getTypeName());
    }

    @Override
    public FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader() throws IOException {
        return super.getFeatureReader(new Query(getTypeName().getLocalPart()), Transaction.AUTO_COMMIT);
    }

    public long getCount(Query query) throws IOException {
        return getFeatureSource().getCount(query);
    }

    /**
     * Set the FeatureType of this DataStore. This method will delete any existing local resources or throw an
     * IOException if the DataStore is remote.
     *
     * @param featureType The desired FeatureType.
     * @throws IOException If the DataStore is remote.
     */
    @Override
    public void createSchema(SimpleFeatureType featureType) throws IOException {
        if (!shpFiles.isLocal() || shpFiles.isGz()) {
            throw new IOException("Cannot create FeatureType on remote or in-classpath or gzipped shapefile");
        }

        shpFiles.delete();

        CoordinateReferenceSystem crs = featureType.getGeometryDescriptor().getCoordinateReferenceSystem();
        final Class<?> geomType = featureType.getGeometryDescriptor().getType().getBinding();
        final ShapeType shapeType;

        if (Point.class.isAssignableFrom(geomType)) {
            shapeType = ShapeType.POINT;
        } else if (MultiPoint.class.isAssignableFrom(geomType)) {
            shapeType = ShapeType.MULTIPOINT;
        } else if (LineString.class.isAssignableFrom(geomType) || MultiLineString.class.isAssignableFrom(geomType)) {
            shapeType = ShapeType.ARC;
        } else if (Polygon.class.isAssignableFrom(geomType) || MultiPolygon.class.isAssignableFrom(geomType)) {
            shapeType = ShapeType.POLYGON;
        } else {
            throw new DataSourceException("Cannot create a shapefile whose geometry type is " + geomType);
        }

        StorageFile shpStoragefile = shpFiles.getStorageFile(SHP);
        StorageFile shxStoragefile = shpFiles.getStorageFile(SHX);
        StorageFile dbfStoragefile = shpFiles.getStorageFile(DBF);
        StorageFile prjStoragefile = shpFiles.getStorageFile(PRJ);

        try (FileChannel shpChannel = shpStoragefile.getWriteChannel();
                FileChannel shxChannel = shxStoragefile.getWriteChannel();
                ShapefileWriter writer = new ShapefileWriter(shpChannel, shxChannel)) {
            // by spec, if the file is empty, the shape envelope should be ignored
            writer.writeHeaders(new Envelope(), shapeType, 0, 100);
        }

        DbaseFileHeader dbfheader = createDbaseHeader(featureType, charset);

        dbfheader.setNumRecords(0);

        try (WritableByteChannel dbfChannel = dbfStoragefile.getWriteChannel()) {
            dbfheader.writeHeader(dbfChannel);
        }

        if (crs != null) {
            String s = toSingleLineWKT(crs);

            try (FileWriter prjWriter = new FileWriter(prjStoragefile.getFile(), StandardCharsets.UTF_8)) {
                prjWriter.write(s);
            }
        } else {
            LOGGER.fine("PRJ file not generated for null CoordinateReferenceSystem");
        }
        StorageFile.replaceOriginals(shpStoragefile, shxStoragefile, dbfStoragefile, prjStoragefile);
    }

    /** Turns the CRS into a single line WKT, more compatible with ESRI software */
    String toSingleLineWKT(CoordinateReferenceSystem crs) {
        String wkt = null;
        try {
            // this is a lenient transformation, works with polar stereographics too
            Formattable formattable = (Formattable) crs;
            wkt = formattable.toWKT(0, false);
        } catch (ClassCastException e) {
            wkt = crs.toWKT();
        }

        wkt = wkt.replaceAll("\n", "").replaceAll("  ", "");
        return wkt;
    }

    /**
     * Attempt to create a DbaseFileHeader for the FeatureType. Note, we cannot set the number of records until the
     * write has completed.
     */
    protected static DbaseFileHeader createDbaseHeader(SimpleFeatureType featureType, Charset charset)
            throws IOException, DbaseFileException {

        DbaseFileHeader header = new DbaseFileHeader(charset);

        for (int i = 0, ii = featureType.getAttributeCount(); i < ii; i++) {
            AttributeDescriptor type = featureType.getDescriptor(i);

            Class<?> colType = type.getType().getBinding();
            String colName = type.getLocalName();

            int fieldLen = FeatureTypes.getFieldLength(type);
            if (fieldLen == FeatureTypes.ANY_LENGTH) fieldLen = 255;
            if (colType == Integer.class || colType == Short.class || colType == Byte.class) {
                header.addColumn(colName, 'N', Math.min(fieldLen, 9), 0);
            } else if (colType == Long.class) {
                header.addColumn(colName, 'N', Math.min(fieldLen, 19), 0);
            } else if (colType == BigInteger.class) {
                header.addColumn(colName, 'N', Math.min(fieldLen, 33), 0);
            } else if (colType == Float.class) {
                int l = Math.min(fieldLen, 24);
                // GDAL format default is 15 decimal places of precision
                // http://www.gdal.org/drv_shapefile.html
                int d = Math.min(Math.max(l - 2, 0), 15);
                header.addColumn(colName, 'N', l, d);
            } else if (colType == Double.class) {
                int l = Math.min(fieldLen, 33);
                int d = Math.min(Math.max(l - 2, 0), 15);
                header.addColumn(colName, 'N', l, d);
            } else if (Number.class.isAssignableFrom(colType)) {
                int l = Math.min(fieldLen, 33);
                int d = Math.max(l - 2, 0);
                header.addColumn(colName, 'N', l, d);
                // This check has to come before the Date one or it is never reached
                // also, this field is only activated with the following system property:
                // org.geotools.shapefile.datetime=true
            } else if (java.util.Date.class.isAssignableFrom(colType)
                    && Boolean.getBoolean("org.geotools.shapefile.datetime")) {
                header.addColumn(colName, '@', fieldLen, 0);
            } else if (java.util.Date.class.isAssignableFrom(colType) || Calendar.class.isAssignableFrom(colType)) {
                header.addColumn(colName, 'D', fieldLen, 0);
            } else if (colType == Boolean.class) {
                header.addColumn(colName, 'L', 1, 0);
            } else if (CharSequence.class.isAssignableFrom(colType) || colType == java.util.UUID.class) {
                // Possible fix for GEOT-42 : ArcExplorer doesn't like 0 length
                // ensure that maxLength is at least 1
                header.addColumn(colName, 'C', Math.min(254, fieldLen), 0);
            } else if (Geometry.class.isAssignableFrom(colType)) {
                continue;
                // skip binary data types
            } else if (colType == byte[].class) {
                continue;
            } else {
                // Fallback to char columns
                header.addColumn(colName, 'C', Math.min(254, fieldLen), 0);
            }
        }

        return header;
    }

    /**
     * This method is used to force the creation of a .prj file.
     *
     * <p>The internally cached FeatureType will be removed, so the next call to getSchema() will read in the created
     * file. This method is not thread safe and will have dire consequences for any other thread making use of the
     * shapefile.
     *
     * <p>
     */
    public void forceSchemaCRS(CoordinateReferenceSystem crs) throws IOException {
        if (crs == null) throw new NullPointerException("CRS required for .prj file");

        String s = toSingleLineWKT(crs);
        StorageFile storageFile = shpFiles.getStorageFile(PRJ);

        try (FileWriter out = new FileWriter(storageFile.getFile(), StandardCharsets.UTF_8)) {
            out.write(s);
        }
        storageFile.replaceOriginal();
        entries.clear();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (shpFiles != null) {
            shpFiles.dispose();
            shpFiles = null;
        }
    }

    @Override
    @SuppressWarnings("deprecation") // finalize is deprecated in Java 9
    protected void finalize() throws Throwable {
        super.finalize();
        if (shpFiles != null && trace != null) {
            LOGGER.log(
                    Level.SEVERE, "Undisposed of shapefile, you should call dispose() on all shapefile stores", trace);
        }
        dispose();
    }

    /**
     * Returns true if the store uses the .fix index file for feature ids. The .fix file speeds up filters by feature id
     * and allows for stable ids in face of feature removals, without it the feature id is simply the position of the
     * feature in the shapefile, something which changes when data is removed
     */
    public boolean isFidIndexed() {
        return fidIndexed;
    }

    /** Enables/disables the feature id index. The index is enabled by default */
    public void setFidIndexed(boolean fidIndexed) {
        this.fidIndexed = fidIndexed;
    }

    @Override
    public String toString() {
        return "ShapefileDataStore [file="
                + shpFiles.get(SHP)
                + ", charset="
                + charset
                + ", timeZone="
                + timeZone
                + ", memoryMapped="
                + memoryMapped
                + ", bufferCachingEnabled="
                + bufferCachingEnabled
                + ", indexed="
                + indexed
                + ", fidIndexed="
                + fidIndexed
                + "]";
    }

    @Override
    public void updateSchema(SimpleFeatureType featureType) throws IOException {
        updateSchema(getTypeName().getLocalPart(), featureType);
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(Filter filter, Transaction transaction)
            throws IOException {
        return getFeatureWriter(getTypeName().getLocalPart(), filter, transaction);
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(Transaction transaction)
            throws IOException {
        return getFeatureWriter(getTypeName().getLocalPart(), transaction);
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(Transaction transaction)
            throws IOException {
        return getFeatureWriterAppend(getTypeName().getLocalPart(), transaction);
    }

    public boolean isIndexCreationEnabled() {
        return indexCreationEnabled;
    }

    /** If true (default) the index file will be created on demand if missing */
    public void setIndexCreationEnabled(boolean indexCreationEnabled) {
        this.indexCreationEnabled = indexCreationEnabled;
    }

    @Override
    public void removeSchema(String typeName) throws IOException {
        removeSchema(new NameImpl(null, typeName));
    }

    @Override
    public void removeSchema(Name typeName) throws IOException {
        // check file
        ContentEntry entry = ensureEntry(typeName);
        org.geotools.data.shapefile.files.FileWriter writer = () -> "TheShapefileRemover";
        for (ShpFileType type : ShpFileType.values()) {
            File file = shpFiles.acquireWriteFile(type, writer);
            try {
                if (file.exists()) {
                    if (!file.delete()) {
                        throw new IOException("Failed to delete " + file.getAbsolutePath());
                    }
                }
            } finally {
                shpFiles.unlockWrite(file, writer);
            }
        }
        removeEntry(entry.getName());
    }
}
