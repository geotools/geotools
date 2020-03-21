/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
import static org.geotools.data.shapefile.files.ShpFileType.SHX;

import java.io.IOException;
import org.geotools.data.DataSourceException;
import org.geotools.data.PrjFileReader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.dbf.IndexedDbaseFileReader;
import org.geotools.data.shapefile.files.FileReader;
import org.geotools.data.shapefile.files.ShpFileType;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.shp.IndexFile;
import org.geotools.data.shapefile.shp.ShapefileException;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.referencing.FactoryException;

/**
 * Provides access to the various reader/writers for the group of files making up a Shapefile
 *
 * @author Andrea Aime - GeoSolutions
 */
class ShapefileSetManager implements FileReader {

    ShpFiles shpFiles;

    ShapefileDataStore store;

    public ShapefileSetManager(ShpFiles shpFiles, ShapefileDataStore store) {
        super();
        this.shpFiles = shpFiles;
        this.store = store;
    }

    /**
     * Convenience method for opening a ShapefileReader.
     *
     * @return A new ShapefileReader.
     * @throws IOException If an error occurs during creation.
     */
    protected ShapefileReader openShapeReader(GeometryFactory gf, boolean onlyRandomAccess)
            throws IOException {
        try {
            return new ShapefileReader(
                    shpFiles, true, store.isMemoryMapped(), gf, onlyRandomAccess);
        } catch (ShapefileException se) {
            throw new DataSourceException("Error creating ShapefileReader", se);
        }
    }

    /**
     * Convenience method for opening a DbaseFileReader.
     *
     * @return A new DbaseFileReader
     * @throws IOException If an error occurs during creation.
     */
    protected DbaseFileReader openDbfReader(boolean indexed) throws IOException {
        if (shpFiles.get(ShpFileType.DBF) == null) {
            return null;
        }

        if (shpFiles.isLocal() && !shpFiles.exists(DBF)) {
            return null;
        }

        try {
            if (indexed) {
                return new IndexedDbaseFileReader(
                        shpFiles, store.isMemoryMapped(), store.getCharset(), store.getTimeZone());
            } else {
                return new DbaseFileReader(
                        shpFiles, store.isMemoryMapped(), store.getCharset(), store.getTimeZone());
            }
        } catch (IOException e) {
            // could happen if dbf file does not exist
            return null;
        }
    }

    /**
     * Convenience method for opening a DbaseFileReader.
     *
     * @return A new DbaseFileReader
     * @throws IOException If an error occurs during creation.
     */
    protected PrjFileReader openPrjReader() throws IOException, FactoryException {

        if (shpFiles.get(PRJ) == null) {
            return null;
        }

        if (shpFiles.isLocal() && !shpFiles.exists(PRJ)) {
            return null;
        }

        try {
            return new PrjFileReader(shpFiles.getReadChannel(PRJ, this));
        } catch (IOException e) {
            // could happen if prj file does not exist remotely
            return null;
        }
    }

    /**
     * Convenience method for opening an index file.
     *
     * @return An IndexFile
     */
    protected IndexFile openIndexFile() throws IOException {
        if (shpFiles.get(SHX) == null) {
            return null;
        }

        if (shpFiles.isLocal() && !shpFiles.exists(SHX)) {
            return null;
        }

        try {
            return new IndexFile(shpFiles, store.isMemoryMapped());
        } catch (IOException e) {
            // could happen if shx file does not exist remotely
            return null;
        }
    }

    @Override
    public String id() {
        return getClass().getName();
    }
}
