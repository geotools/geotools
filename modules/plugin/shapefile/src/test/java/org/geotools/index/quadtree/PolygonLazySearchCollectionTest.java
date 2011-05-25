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

import org.geotools.data.shapefile.TestCaseSupport;
import org.geotools.data.shapefile.indexed.IndexedShapefileDataStore;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * @author Jesse
 *
 *
 * @source $URL$
 */
public class PolygonLazySearchCollectionTest extends TestCaseSupport {

    private File file;
    private IndexedShapefileDataStore ds;
    private QuadTree tree;
    private Iterator iterator;
    private CoordinateReferenceSystem crs;

    public PolygonLazySearchCollectionTest() throws IOException {
        super("LazySearchIteratorTest");
    }

    protected void setUp() throws Exception {
        super.setUp();
        file = copyShapefiles("shapes/statepop.shp");
        ds = new IndexedShapefileDataStore(file.toURI().toURL());
        ds.buildQuadTree();
        tree = LineLazySearchCollectionTest.openQuadTree(file);
        crs = ds.getSchema().getCoordinateReferenceSystem();
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
        ReferencedEnvelope env = new ReferencedEnvelope(-125.5, -66, 23.6,
                53.0, crs);
        assertEquals(49, countIterator(new LazySearchIterator(tree, env)));
    }

    public void testGetOneFeatures() throws Exception {
        ReferencedEnvelope env = new ReferencedEnvelope(-70, -68.2, 44.5, 45.7,
                crs);
        assertEquals(14, countIterator(new LazySearchIterator(tree, env)));

    }

    public void testGetNoFeatures() throws Exception {
        ReferencedEnvelope env = new ReferencedEnvelope(0, 10, 0, 10, crs);
        assertEquals(0, countIterator(new LazySearchIterator(tree, env)));
    }
}
