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

import static org.geotools.data.shapefile.files.ShpFileType.FIX;
import static org.geotools.data.shapefile.files.ShpFileType.QIX;
import static org.geotools.data.shapefile.files.ShpFileType.SHP;
import static org.geotools.data.shapefile.files.ShpFileType.SHX;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.CloseableIterator;
import org.geotools.data.DataSourceException;
import org.geotools.data.shapefile.fid.FidIndexer;
import org.geotools.data.shapefile.fid.IndexedFidReader;
import org.geotools.data.shapefile.files.FileWriter;
import org.geotools.data.shapefile.files.ShpFileType;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.index.CachedQuadTree;
import org.geotools.data.shapefile.index.Data;
import org.geotools.data.shapefile.index.DataDefinition;
import org.geotools.data.shapefile.index.TreeException;
import org.geotools.data.shapefile.index.quadtree.QuadTree;
import org.geotools.data.shapefile.index.quadtree.StoreException;
import org.geotools.data.shapefile.index.quadtree.fs.FileSystemIndexStore;
import org.geotools.data.shapefile.shp.IndexFile;
import org.geotools.data.util.NullProgressListener;
import org.geotools.util.URLs;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Envelope;
import org.opengis.filter.Id;
import org.opengis.filter.identity.Identifier;

/**
 * Manages the index files on behalf of the the {@link ShapefileDataStore}
 *
 * @author Andrea Aime - GeoSolutions
 */
class IndexManager {

    static final Logger LOGGER = Logging.getLogger(IndexManager.class);

    static final int DEFAULT_MAX_QIX_CACHE_SIZE;

    ShpFiles shpFiles;

    int maxQixCacheSize = DEFAULT_MAX_QIX_CACHE_SIZE;

    CachedQuadTree cachedTree;

    ShapefileDataStore store;

    /** Used to lock the files when doing accesses to check indexes and the like */
    FileWriter writer =
            new FileWriter() {

                @Override
                public String id() {
                    return "ShapefileDataStore-" + store.getTypeName().getLocalPart();
                }
            };

    static {
        int max = -1;
        try {
            String smax = System.getProperty("org.geotools.shapefile.maxQixCacheSize");
            if (smax != null) {
                max = Integer.parseInt(smax);
            }
        } catch (Throwable t) {
            LOGGER.log(Level.SEVERE, "Could not set the max qix cache size", t);
        }
        DEFAULT_MAX_QIX_CACHE_SIZE = max;
    }

    public IndexManager(ShpFiles shpFiles, ShapefileDataStore store) {
        this.shpFiles = shpFiles;
        this.store = store;
    }

    /**
     * Creates the spatial index is appropriate.
     *
     * @param force Forces the index re-creation even if the spatial index seems to be up to date
     * @return true if the spatial index has been created/updated
     */
    public boolean createSpatialIndex(boolean force) {
        // create index as needed
        try {
            if (shpFiles.isLocal() && (isIndexStale(QIX) || force)) {
                ShapefileDataStoreFactory.LOGGER.fine(
                        "Creating spatial index for " + shpFiles.get(SHP));

                ShapeFileIndexer indexer = new ShapeFileIndexer();
                indexer.setShapeFileName(shpFiles);
                indexer.index(false, new NullProgressListener());

                return true;
            }
        } catch (Throwable t) {
            ShapefileDataStoreFactory.LOGGER.log(Level.SEVERE, t.getLocalizedMessage(), t);
        }
        return false;
    }

    /** If the fid index can be used and it is missing this method will try to create it */
    boolean hasFidIndex(boolean createIfMissing) {
        if (isIndexUseable(FIX)) {
            return true;
        } else {
            if (shpFiles.isLocal() && (shpFiles.exists(FIX) || createIfMissing)) {
                return createFidIndex();
            } else {
                return false;
            }
        }
    }

    public boolean createFidIndex() {
        try {
            FidIndexer.generate(shpFiles);
            return true;
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to create fid index");
            return false;
        }
    }

    /** Returns true if the specified index exists, is up to date, and can be read */
    boolean isIndexUseable(ShpFileType indexType) {
        if (shpFiles.isLocal()) {
            if (isIndexStale(indexType) || !shpFiles.exists(indexType)) {
                return false;
            }
        } else {

            ReadableByteChannel read = null;
            try {
                read = shpFiles.getReadChannel(indexType, writer);
            } catch (IOException e) {
                return false;
            } finally {
                if (read != null) {
                    try {
                        read.close();
                    } catch (IOException e) {
                        ShapefileDataStoreFactory.LOGGER.log(
                                Level.WARNING, "could not close stream", e);
                    }
                }
            }
        }

        return true;
    }

    /** Returns true if the index file is available */
    boolean isSpatialIndexAvailable() {
        return shpFiles.isLocal() && shpFiles.exists(QIX);
    }

    /**
     * Returns true if the specified index file is outdated compared to the shapefile .shp and .shx
     * files
     */
    boolean isIndexStale(ShpFileType indexType) {
        if (!shpFiles.isLocal())
            throw new IllegalStateException(
                    "This method only applies if the files are local and the file can be created");

        URL shpURL = shpFiles.acquireRead(SHP, writer);
        URL indexURL = shpFiles.acquireRead(indexType, writer);
        try {

            if (indexURL == null) {
                return true;
            }
            // indexes require both the SHP and SHX so if either or missing then
            // you don't need to
            // index
            if (!shpFiles.exists(SHX) || !shpFiles.exists(SHP)) {
                return false;
            }

            File indexFile = URLs.urlToFile(indexURL);
            File shpFile = URLs.urlToFile(shpURL);
            long indexLastModified = indexFile.lastModified();
            long shpLastModified = shpFile.lastModified();
            boolean shpChangedMoreRecently = indexLastModified < shpLastModified;
            return !indexFile.exists() || shpChangedMoreRecently;
        } finally {
            if (shpURL != null) {
                shpFiles.unlockRead(shpURL, writer);
            }
            if (indexURL != null) {
                shpFiles.unlockRead(indexURL, writer);
            }
        }
    }

    /**
     * Uses the Fid index to quickly lookup the shp offset and the record number for the list of
     * fids
     *
     * @param fidFilter the fid filter identifying the ids
     * @return a list of Data objects
     */
    List<Data> queryFidIndex(Id fidFilter) throws IOException {
        // sort by fid to increase performance and allow skipping on natural order
        TreeSet<Identifier> idsSet =
                new TreeSet<Identifier>(
                        new IdentifierComparator(store.getTypeName().getLocalPart()));
        idsSet.addAll(fidFilter.getIdentifiers());

        IndexedFidReader reader = new IndexedFidReader(shpFiles);

        List<Data> records = new ArrayList(idsSet.size());
        try {
            IndexFile shx = store.shpManager.openIndexFile();
            try {

                DataDefinition def = new DataDefinition("US-ASCII");
                def.addField(Integer.class);
                def.addField(Long.class);
                for (Identifier identifier : idsSet) {
                    String fid = identifier.toString();
                    long recno = reader.findFid(fid);
                    if (recno == -1) {
                        if (LOGGER.isLoggable(Level.FINEST)) {
                            LOGGER.finest(
                                    "fid "
                                            + fid
                                            + " not found in index, continuing with next queried fid...");
                        }
                        continue;
                    }
                    try {
                        Data data = new Data(def);
                        data.addValue(Integer.valueOf((int) recno + 1));
                        data.addValue(Long.valueOf(shx.getOffsetInBytes((int) recno)));
                        if (LOGGER.isLoggable(Level.FINEST)) {
                            LOGGER.finest(
                                    "fid "
                                            + fid
                                            + " found for record #"
                                            + data.getValue(0)
                                            + " at index file offset "
                                            + data.getValue(1));
                        }
                        records.add(data);
                    } catch (Exception e) {
                        IOException exception = new IOException();
                        exception.initCause(e);
                        throw exception;
                    }
                }
            } finally {
                shx.close();
            }
        } finally {
            reader.close();
        }

        return records;
    }

    /** Queries the spatial index for features available in the specified bbox */
    protected CloseableIterator<Data> querySpatialIndex(Envelope bbox)
            throws DataSourceException, IOException, TreeException {
        CloseableIterator<Data> tmp = null;

        // check if the spatial index needs recreating
        createSpatialIndex(false);

        if (cachedTree == null) {
            boolean canCache = false;
            URL treeURL = shpFiles.acquireRead(QIX, writer);
            try {
                File treeFile = URLs.urlToFile(treeURL);

                if (treeFile != null
                        && treeFile.exists()
                        && treeFile.length() < 1024 * maxQixCacheSize) {
                    canCache = true;
                }
            } finally {
                shpFiles.unlockRead(treeURL, writer);
            }

            if (canCache) {
                try (QuadTree quadTree = openQuadTree()) {
                    if (quadTree != null) {
                        LOGGER.warning(
                                "Experimental: loading in memory the quadtree for "
                                        + shpFiles.get(SHP));
                        cachedTree = new CachedQuadTree(quadTree);
                        quadTree.close();
                    }
                }
            }
        }
        if (cachedTree != null) {
            if (!bbox.contains(cachedTree.getBounds())) {
                return cachedTree.search(bbox);
            } else {
                return null;
            }
        } else {
            try {
                @SuppressWarnings("PMD.CloseResource") // managed as part of the return
                QuadTree quadTree = openQuadTree();
                if ((quadTree != null) && !bbox.contains(quadTree.getRoot().getBounds())) {
                    tmp = quadTree.search(bbox);
                }
                if (tmp == null && quadTree != null) {
                    quadTree.close();
                }
            } catch (Exception e) {
                throw new DataSourceException("Error querying QuadTree", e);
            }
        }

        return tmp;
    }

    /**
     * Convenience method for opening a QuadTree index.
     *
     * @return A new QuadTree
     */
    protected QuadTree openQuadTree() throws StoreException {
        if (!shpFiles.isLocal()) {
            return null;
        }
        URL treeURL = shpFiles.acquireRead(QIX, writer);
        try {
            File treeFile = URLs.urlToFile(treeURL);

            if (!treeFile.exists() || (treeFile.length() == 0)) {
                return null;
            }

            try {
                FileSystemIndexStore idxStore = new FileSystemIndexStore(treeFile);
                return idxStore.load(store.shpManager.openIndexFile(), store.isMemoryMapped());
            } catch (IOException e) {
                throw new StoreException(e);
            }
        } finally {
            shpFiles.unlockRead(treeURL, writer);
        }
    }

    public void dispose() {
        this.cachedTree = null;
    }
}
