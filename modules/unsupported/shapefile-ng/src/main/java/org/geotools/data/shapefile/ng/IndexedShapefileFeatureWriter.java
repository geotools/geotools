/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.TimeZone;
import java.util.logging.Level;

import org.geotools.data.DataUtilities;
import org.geotools.data.shapefile.ng.fid.FidIndexer;
import org.geotools.data.shapefile.ng.fid.IndexedFidWriter;
import org.geotools.data.shapefile.ng.files.FileWriter;
import org.geotools.data.shapefile.ng.files.ShpFileType;
import org.geotools.data.shapefile.ng.files.StorageFile;
import org.opengis.feature.simple.SimpleFeature;

/**
 * A FeatureWriter for ShapefileDataStore. Uses a write and annotate technique to avoid buffering
 * attributes and geometries. Because the shape and dbf require header information which can only be
 * obtained by reading the entire series of Features, the headers are updated after the initial
 * write completes.
 */
class IndexedShapefileFeatureWriter extends ShapefileFeatureWriter implements FileWriter {

    private IndexedFidWriter fidWriter;

    private String currentFid;

    private IndexManager indexes;

    public IndexedShapefileFeatureWriter(IndexManager indexes,
            ShapefileFeatureReader featureReader, Charset charset, TimeZone timeZone)
            throws IOException {
        super(indexes.shpFiles, featureReader, charset, timeZone);
        this.indexes = indexes;
        if (!indexes.shpFiles.isLocal()) {
            this.fidWriter = IndexedFidWriter.EMPTY_WRITER;
        } else {
            StorageFile storageFile = shpFiles.getStorageFile(FIX);
            storageFiles.put(FIX, storageFile);
            this.fidWriter = new IndexedFidWriter(shpFiles, storageFile);
        }
    }

    @Override
    public SimpleFeature next() throws IOException {
        // closed already, error!
        if (featureReader == null) {
            throw new IOException("Writer closed");
        }

        // we have to write the current feature back into the stream
        if (currentFeature != null) {
            write();
        }

        long next = fidWriter.next();
        currentFid = getFeatureType().getTypeName() + "." + next;
        SimpleFeature feature = super.next();
        return feature;
    }

    @Override
    protected String nextFeatureId() {
        return currentFid;
    }

    @Override
    public void remove() throws IOException {
        fidWriter.remove();
        super.remove();
    }

    @Override
    public void write() throws IOException {
        fidWriter.write();
        super.write();
    }

    /**
     * Release resources and flush the header information.
     */
    public void close() throws IOException {
        super.close();
        fidWriter.close();

        try {
            if (shpFiles.isLocal()) {
                if (indexes.isIndexStale(ShpFileType.FIX)) {
                    FidIndexer.generate(shpFiles);
                }

                deleteFile(ShpFileType.QIX);
            }
        } catch (Throwable e) {
            ShapefileDataStoreFactory.LOGGER.log(Level.WARNING, "Error creating Spatial index", e);
        }
    }

    @Override
    protected void doClose() throws IOException {
        super.doClose();
        try {
            fidWriter.close();
        } catch (Throwable e) {
            ShapefileDataStoreFactory.LOGGER.log(Level.WARNING, "Error creating Feature ID index",
                    e);
        }
    }

    private void deleteFile(ShpFileType shpFileType) {
        URL url = shpFiles.acquireWrite(shpFileType, this);
        try {
            File toDelete = DataUtilities.urlToFile(url);

            if (toDelete.exists()) {
                toDelete.delete();
            }
        } finally {
            shpFiles.unlockWrite(url, this);
        }
    }

    public String id() {
        return getClass().getName();
    }
}
