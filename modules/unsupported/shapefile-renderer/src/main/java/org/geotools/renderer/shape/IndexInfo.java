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
package org.geotools.renderer.shape;

import static org.geotools.data.shapefile.ShpFileType.QIX;
import static org.geotools.data.shapefile.ShpFileType.SHX;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;

import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.shapefile.FileReader;
import org.geotools.data.shapefile.ShpFiles;
import org.geotools.data.shapefile.indexed.IndexType;
import org.geotools.data.shapefile.indexed.RecordNumberTracker;
import org.geotools.data.shapefile.shp.IndexFile;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.geotools.index.CloseableIterator;
import org.geotools.index.Data;
import org.geotools.index.TreeException;
import org.geotools.index.quadtree.QuadTree;
import org.geotools.index.quadtree.StoreException;
import org.geotools.index.quadtree.fs.FileSystemIndexStore;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Encapsulates index information for a layer in the MapContext. The associated
 * layer can be obtained by
 * 
 * @author jones
 *
 * @source $URL$
 */
public class IndexInfo implements FileReader {
    final IndexType treeType;
    private QuadTree qtree;
    private ShpFiles shpFiles;

    public IndexInfo(IndexType treeType, ShpFiles shpFiles) {
        this.treeType = treeType;
        this.shpFiles = shpFiles;
    }


    /**
     * QuadTree Query
     * 
     * @param bbox
     * @return
     * @throws DataSourceException
     * @throws IOException
     * @throws TreeException
     *                 DOCUMENT ME!
     */
    CloseableIterator<Data> queryQuadTree(Envelope bbox) throws DataSourceException,
            IOException, TreeException {
        try {
            // old code was checking the resulting collection wasn't empty and
            // it that
            // case it closed the qtree straight away. qtree gets closed anyways
            // with
            // this code path, but it's quite a bit faster because it avoid one
            // disk access
            // just to check the collection is not empty
            if ((qtree != null) && !bbox.contains(qtree.getRoot().getBounds()))
                return qtree.search(bbox);
        } catch (Exception e) {
            ShapefileRenderer.LOGGER.warning(e.getLocalizedMessage());
        }

        return null;
    }


    /**
     * Convenience method for opening a QuadTree index.
     * 
     * @return A new QuadTree
     * @throws StoreException
     */
    QuadTree openQuadTree() throws StoreException {
        URL url = shpFiles.acquireRead(QIX, this);
        try {
            File file = DataUtilities.urlToFile(url);
            FileSystemIndexStore store = new FileSystemIndexStore(file);

            try {
                return store.load(openIndexFile(), false);
            } catch (IOException e) {
                throw new StoreException(e);
            }
        } finally {
            shpFiles.unlockRead(url, this);
        }
    }

    /**
     * Convenience method for opening a ShapefileReader.
     * 
     * @return An IndexFile
     * @throws IOException
     */
    IndexFile openIndexFile() throws IOException {

        if (shpFiles.get(SHX) == null || (shpFiles.isLocal() && !shpFiles.exists(SHX))) {
            return null;
        }
        try{
            return new IndexFile(shpFiles, false);
        }catch (Exception e) {
            return null;
        }
    }

    private CloseableIterator<Data> queryTree(Envelope bbox) throws IOException,
            TreeException {
        if (treeType == IndexType.QIX) {
            return queryQuadTree(bbox);
        }

        // should not happen
        return null;
    }

    static class Reader implements RecordNumberTracker {
        private ShapefileReader shp;
        Iterator goodRecs;
        private int recno = 1;
        private Data next;
        private IndexInfo info;

        public Reader(IndexInfo info, ShapefileReader reader, Envelope bbox)
                throws IOException {
            shp = reader;

            try {

                if (info.treeType == IndexType.QIX) {
                    info.qtree = info.openQuadTree();
                }

                goodRecs = info.queryTree(bbox);
            } catch (Exception e) {
                ShapefileRenderer.LOGGER.log(Level.FINE,
                        "Exception occured attempting to use indexing:", e);
                goodRecs = null;
            }

            this.info = info;
        }

        public int getRecordNumber() {
            return this.recno;
        }

        public boolean hasNext() throws IOException {
            if (this.goodRecs != null) {
                if (next != null)
                    return true;
                if (this.goodRecs.hasNext()) {

                    next = (Data) goodRecs.next();
                    this.recno = ((Integer) next.getValue(0)).intValue();
                    return true;
                }
                return false;
            }

            return shp.hasNext();
        }

        public ShapefileReader.Record next() throws IOException {
            if (!hasNext())
                throw new IndexOutOfBoundsException(
                        "No more features in reader");
            if (this.goodRecs != null) {

                Long l = (Long) next.getValue(1);
                ShapefileReader.Record record = shp.recordAt(l.intValue());
                next = null;
                return record;
            }
            recno++;
            return shp.nextRecord();
        }

        public void close() throws IOException {
            shp.close();
            try {
                if (info.qtree != null) {
                    info.qtree.close(goodRecs);
                    info.qtree.close();
                }
            } catch (StoreException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public String id() {
        return getClass().getName();
    }
}
