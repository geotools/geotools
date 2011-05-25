/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.index.quadtree;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.geotools.data.shapefile.ShpFiles;
import org.geotools.data.shapefile.TestCaseSupport;
import org.geotools.data.shapefile.indexed.IndexedShapefileDataStore;
import org.geotools.data.shapefile.shp.IndexFile;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.index.quadtree.fs.FileSystemIndexStore;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * @author Jesse
 *
 *
 * @source $URL$
 */
public class LineLazySearchCollectionTest extends TestCaseSupport {

    private File file;
    private IndexedShapefileDataStore ds;
    private QuadTree tree;
    private Iterator iterator;
    private CoordinateReferenceSystem crs;

    public LineLazySearchCollectionTest() throws IOException {
        super("LazySearchIteratorTest");
    }

    protected void setUp() throws Exception {
        super.setUp();
        file = copyShapefiles("shapes/streams.shp");
        ds = new IndexedShapefileDataStore(file.toURI().toURL());
        ds.buildQuadTree();
        tree = openQuadTree(file);
        crs = ds.getSchema().getCoordinateReferenceSystem();
    }

    public static QuadTree openQuadTree(File file) throws StoreException {
        File qixFile = sibling(file, "qix");
        FileSystemIndexStore store = new FileSystemIndexStore(qixFile);
        try {
            ShpFiles shpFiles = new ShpFiles(qixFile);

            IndexFile indexFile = new IndexFile(shpFiles, false);
            return store.load(indexFile, true);

        } catch (IOException e) {
            throw new StoreException(e);
        }
    }

    protected void tearDown() throws Exception {
        if (iterator != null)
            tree.close(iterator);
        tree.close();
        ds.dispose();
        super.tearDown();
        file.getParentFile().delete();
    }

    public void testGetAllFeatures() throws Exception {
        ReferencedEnvelope env = new ReferencedEnvelope(585000, 610000,
                4910000, 4930000, crs);
        assertEquals(116, countIterator(new LazySearchIterator(tree, env)));
    }

    public void testGetOneFeatures() throws Exception {
        ReferencedEnvelope env = new ReferencedEnvelope(588993, 589604,
                4927443, 4927443, crs);
        assertEquals(22, countIterator(new LazySearchIterator(tree, env)));

    }

    public void testGetNoFeatures() throws Exception {
        ReferencedEnvelope env = new ReferencedEnvelope(592211, 597000,
                4910947, 4913500, crs);
        assertEquals(0, countIterator(new LazySearchIterator(tree, env)));
    }
}
