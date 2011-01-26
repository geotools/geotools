/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

import static org.geotools.data.shapefile.ShpFileType.DBF;
import static org.geotools.data.shapefile.ShpFileType.SHP;
import static org.geotools.data.shapefile.ShpFileType.SHX;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileWriter;
import org.geotools.data.shapefile.shp.JTSUtilities;
import org.geotools.data.shapefile.shp.ShapeHandler;
import org.geotools.data.shapefile.shp.ShapeType;
import org.geotools.data.shapefile.shp.ShapefileException;
import org.geotools.data.shapefile.shp.ShapefileWriter;
import org.geotools.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * A FeatureWriter for ShapefileDataStore. Uses a write and annotate technique
 * to avoid buffering attributes and geometries. Because the shapefile and dbf
 * require header information which can only be obtained by reading the entire
 * series of Features, the headers are updated after the initial write
 * completes.
 * 
 * @author Jesse Eichar
 *
 * @source $URL$
 */
public class ShapefileFeatureWriter implements FeatureWriter<SimpleFeatureType, SimpleFeature> {

    // the  FeatureReader<SimpleFeatureType, SimpleFeature> to obtain the current Feature from
    protected  FeatureReader<SimpleFeatureType, SimpleFeature> featureReader;

    // the AttributeReader
    protected ShapefileAttributeReader attReader;

    // the current Feature
    protected SimpleFeature currentFeature;

    // the FeatureType we are representing
    protected SimpleFeatureType featureType;

    // an array for reuse in Feature creation
    protected Object[] emptyAtts;

    // an array for reuse in writing to dbf.
    protected Object[] transferCache;
    protected ShapeType shapeType;
    protected ShapeHandler handler;

    // keep track of shapefile length during write, starts at 100 bytes for
    // required header
    protected int shapefileLength = 100;

    // keep track of the number of records written
    protected int records = 0;

    // hold 1 if dbf should write the attribute at the index, 0 if not
    protected byte[] writeFlags;
    protected ShapefileWriter shpWriter;
    protected DbaseFileWriter dbfWriter;
    private DbaseFileHeader dbfHeader;

    protected Map<ShpFileType, StorageFile> storageFiles = new HashMap<ShpFileType, StorageFile>();

    // keep track of bounds during write
    protected Envelope bounds = new Envelope();

    protected ShpFiles shpFiles;

    private FileChannel dbfChannel;

    private Charset dbfCharset;
    
    private GeometryFactory gf = new GeometryFactory();

    public ShapefileFeatureWriter(String typeName, ShpFiles shpFiles,
            ShapefileAttributeReader attsReader,  FeatureReader<SimpleFeatureType, SimpleFeature> featureReader,
            Charset charset)
            throws IOException {
        this.shpFiles = shpFiles;
        this.dbfCharset = charset;
        // set up reader
        this.attReader = attsReader;
        this.featureReader = featureReader;

        storageFiles.put(SHP, shpFiles.getStorageFile(SHP));
        storageFiles.put(SHX, shpFiles.getStorageFile(SHX));
        storageFiles.put(DBF, shpFiles.getStorageFile(DBF));

        this.featureType = featureReader.getFeatureType();

        // set up buffers and write flags
        emptyAtts = new Object[featureType.getAttributeCount()];
        writeFlags = new byte[featureType.getAttributeCount()];

        int cnt = 0;

        for (int i = 0, ii = featureType.getAttributeCount(); i < ii; i++) {
            // if its a geometry, we don't want to write it to the dbf...
            if (!(featureType.getDescriptor(i) instanceof GeometryDescriptor)) {
                cnt++;
                writeFlags[i] = (byte) 1;
            }
        }

        // dbf transfer buffer
        transferCache = new Object[cnt];

        // open underlying writers
        FileChannel shpChannel = storageFiles.get(SHP).getWriteChannel();
        FileChannel shxChannel = storageFiles.get(SHX).getWriteChannel();
        shpWriter = new ShapefileWriter(shpChannel, shxChannel);

        dbfHeader = ShapefileDataStore.createDbaseHeader(featureType);
        dbfChannel = storageFiles.get(DBF).getWriteChannel();
        dbfWriter = new DbaseFileWriter(dbfHeader, dbfChannel, dbfCharset);

        if(attReader != null) {
            // don't try to read a shx file we're writing to in parallel
            attReader.shp.disableShxUsage();
            if(attReader.hasNext()) {
                shapeType = attReader.shp.getHeader().getShapeType();
                handler = shapeType.getShapeHandler(new GeometryFactory());
                shpWriter.writeHeaders(bounds, shapeType, records, shapefileLength);
            }
        }
    }

    /**
     * Go back and update the headers with the required info.
     * 
     * @throws IOException
     *                 DOCUMENT ME!
     */
    protected void flush() throws IOException {
        // not sure the check for records <=0 is necessary,
        // but if records > 0 and shapeType is null there's probably
        // another problem.
        if ((records <= 0) && (shapeType == null)) {
            GeometryDescriptor geometryDescriptor = featureType
                    .getGeometryDescriptor();

            shapeType = JTSUtilities.getShapeType(geometryDescriptor);
        }

        shpWriter.writeHeaders(bounds, shapeType, records, shapefileLength);

        dbfHeader.setNumRecords(records);
        dbfChannel.position(0);
        dbfHeader.writeHeader(dbfChannel);
    }

    /**
     * In case someone doesn't close me.
     * 
     * @throws Throwable
     *                 DOCUMENT ME!
     */
    protected void finalize() throws Throwable {
        if (featureReader != null) {
            try {
                close();
            } catch (Exception e) {
                // oh well, we tried
            }
        }
    }

    /**
     * Clean up our temporary write if there was one
     * 
     * @throws IOException
     *                 DOCUMENT ME!
     */
    protected void clean() throws IOException {
        StorageFile.replaceOriginals(storageFiles.values().toArray(
                new StorageFile[0]));
    }

    /**
     * Release resources and flush the header information.
     * 
     * @throws IOException
     *                 DOCUMENT ME!
     */
    public void close() throws IOException {
        if (featureReader == null) {
            throw new IOException("Writer closed");
        }

        // make sure to write the last feature...
        if (currentFeature != null) {
            write();
        }

        // if the attribute reader is here, that means we may have some
        // additional tail-end file flushing to do if the Writer was closed
        // before the end of the file
        if (attReader != null && attReader.internalReadersHaveNext()) {
            shapeType = attReader.shp.getHeader().getShapeType();
            handler = shapeType.getShapeHandler(gf);

            // handle the case where zero records have been written, but the
            // stream is closed and the headers
            if (records == 0) {
                shpWriter.writeHeaders(bounds, shapeType, 0, 0);
            }

            // copy array for bounds
            double[] env = new double[4];

            while (attReader.internalReadersHaveNext()) {
                // transfer bytes from shapefile
                shapefileLength += attReader.shp.transferTo(shpWriter,
                        ++records, env);

                // bounds update
                bounds.expandToInclude(env[0], env[1]);
                bounds.expandToInclude(env[2], env[3]);

                // transfer dbf bytes
                attReader.dbf.transferTo(dbfWriter);
            }
        }

        doClose();
        clean();
    }

    protected void doClose() throws IOException {
        // close reader, flush headers, and copy temp files, if any
        try {
            featureReader.close();
        } finally {
            try {
                flush();
            } finally {
                shpWriter.close();
                dbfWriter.close();
            }

            featureReader = null;
            shpWriter = null;
            dbfWriter = null;
        }
    }

    public SimpleFeatureType getFeatureType() {
        return featureType;
    }

    public boolean hasNext() throws IOException {
        if (featureReader == null) {
            throw new IOException("Writer closed");
        }

        return featureReader.hasNext();
    }

    public SimpleFeature next() throws IOException {
        // closed already, error!
        if (featureReader == null) {
            throw new IOException("Writer closed");
        }

        // we have to write the current feature back into the stream
        if (currentFeature != null) {
            write();
        }

        // is there another? If so, return it
        if (featureReader.hasNext()) {
            try {
                return currentFeature = featureReader.next();
            } catch (IllegalAttributeException iae) {
                throw new DataSourceException("Error in reading", iae);
            }
        }

        // reader has no more (no were are adding to the file)
        // so return an empty feature
        try {
            String featureID = getFeatureType().getTypeName()+"."+(records+1);
            return currentFeature = DataUtilities.template(getFeatureType(), featureID,
                    emptyAtts);
        } catch (IllegalAttributeException iae) {
            throw new DataSourceException("Error creating empty Feature", iae);
        }
    }

    /**
     * Called when a new feature is being created and a new fid is required
     * 
     * @return a fid for the new feature
     */
    protected String nextFeatureId() {
        return getFeatureType().getTypeName()+"."+(records+1);
    }

    public void remove() throws IOException {
        if (featureReader == null) {
            throw new IOException("Writer closed");
        }

        if (currentFeature == null) {
            throw new IOException("Current feature is null");
        }

        // mark the current feature as null, this will result in it not
        // being rewritten to the stream
        currentFeature = null;
    }

    public void write() throws IOException {
        if (currentFeature == null) {
            throw new IOException("Current feature is null");
        }

        if (featureReader == null) {
            throw new IOException("Writer closed");
        }

        // writing of Geometry
        Geometry g = (Geometry) currentFeature.getDefaultGeometry();

        // if this is the first Geometry, find the shapeType and handler
        if (shapeType == null) {
            try {
                if(g != null) {
                    int dims = JTSUtilities.guessCoorinateDims(g.getCoordinates());
                    shapeType = JTSUtilities.getShapeType(g, dims);
                } else {
                    shapeType = JTSUtilities.getShapeType(currentFeature.getType().getGeometryDescriptor());
                }

                // we must go back and annotate this after writing
                shpWriter.writeHeaders(new Envelope(), shapeType, 0, 0);
                handler = shapeType.getShapeHandler(gf);
            } catch (ShapefileException se) {
                throw new RuntimeException("Unexpected Error", se);
            }
        }

        // convert geometry
        g = JTSUtilities.convertToCollection(g, shapeType);

        // bounds calculations
        if(g != null) {
	        Envelope b = g.getEnvelopeInternal();
	
	        if (!b.isNull()) {
	            bounds.expandToInclude(b);
	        }
        }

        // file length update
        if(g != null)
        	shapefileLength += (handler.getLength(g) + 8);
        else
        	shapefileLength += (4 + 8);

        // write it
        shpWriter.writeGeometry(g);

        // writing of attributes
        int idx = 0;

        for (int i = 0, ii = featureType.getAttributeCount(); i < ii; i++) {
            // skip geometries
            if (writeFlags[i] > 0) {
                transferCache[idx++] = currentFeature.getAttribute(i);
            }
        }

        dbfWriter.write(transferCache);

        // one more down...
        records++;

        // clear the currentFeature
        currentFeature = null;
    }
}
