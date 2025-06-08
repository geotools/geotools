/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile.index.quadtree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.geotools.api.data.CloseableIterator;
import org.geotools.data.shapefile.index.Data;
import org.geotools.data.shapefile.index.DataDefinition;
import org.geotools.data.shapefile.shp.IndexFile;
import org.locationtech.jts.geom.Envelope;

/**
 * Iterator that search the quad tree depth first. 32000 indices are cached at a time and each time a node is visited
 * the indices are removed from the node so that the memory footprint is kept small. Note that if other iterators
 * operate on the same tree then they can interfere with each other.
 *
 * @author Jesse
 */
public class LazySearchIterator implements CloseableIterator<Data> {

    static final int[] ZERO = new int[0];

    static final DataDefinition DATA_DEFINITION = new DataDefinition("US-ASCII");

    private static final int MAX_INDICES = 32768;

    static {
        DATA_DEFINITION.addField(Integer.class);
        DATA_DEFINITION.addField(Long.class);
    }

    Data next = null;

    Node current;

    int idIndex = 0;

    private boolean closed;

    private Envelope bounds;

    Iterator<Data> data;

    private IndexFile indexfile;

    ArrayList<Node> parents = new ArrayList<>();

    Indices indices = new Indices();

    QuadTree tree;

    public LazySearchIterator(QuadTree tree, Envelope bounds) {
        super();
        this.tree = tree;
        this.indexfile = tree.getIndexfile();
        tree.registerIterator(this);
        this.current = tree.getRoot();
        this.bounds = bounds;
        this.closed = false;
        this.next = null;
    }

    @Override
    public boolean hasNext() {
        if (closed) throw new IllegalStateException("Iterator has been closed!");
        if (next != null) return true;
        if (data != null && data.hasNext()) {
            next = data.next();
        } else {
            data = null;
            fillCache();
            if (data != null && data.hasNext()) next = data.next();
        }
        return next != null;
    }

    private void fillCache() {
        indices.clear();
        ArrayList<Data> dataList = null;
        try {
            while (indices.size() < MAX_INDICES && current != null) {
                if (idIndex < current.getNumShapeIds()
                        && !current.isVisited()
                        && current.getBounds().intersects(bounds)) {
                    indices.add(current.getShapeId(idIndex));
                    idIndex++;
                } else {
                    // free the shapes id array of the current node and prepare to move to the next
                    current.setShapesId(new int[0]);
                    idIndex = 0;

                    boolean foundUnvisited = false;
                    for (int i = 0; i < current.getNumSubNodes(); i++) {
                        Node node = current.getSubNode(i);
                        if (!node.isVisited() && node.getBounds().intersects(bounds)) {
                            foundUnvisited = true;
                            parents.add(current);
                            current = node;
                            break;
                        }
                    }
                    if (!foundUnvisited) {
                        // mark as visited and free the subnodes
                        current.setVisited(true);
                        current.clean();

                        // move up to parent
                        if (parents.isEmpty()) current = null;
                        else current = parents.remove(parents.size() - 1);
                    }
                }
            }

            // sort so offset lookup is faster
            indices.sort();
            int size = indices.size();
            dataList = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                int recno = indices.get(i);
                Data data = new Data(DATA_DEFINITION);
                data.addValue(recno + 1);
                data.addValue(Long.valueOf(indexfile.getOffsetInBytes(recno)));
                dataList.add(data);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        data = dataList.iterator();
    }

    @Override
    public Data next() {
        if (!hasNext()) throw new NoSuchElementException("No more elements available");
        Data temp = next;
        next = null;
        return temp;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() throws IOException {
        tree.close(this);
        tree.close();
        this.closed = true;
    }

    /** An efficient wrapper around an array of integers */
    static class Indices {
        /** The current coordinate */
        int curr;

        /** The ordinates holder */
        int[] indices;

        public Indices() {
            indices = new int[100];
            curr = -1;
        }

        /** The number of coordinates */
        int size() {
            return curr + 1;
        }

        /** Adds a coordinate to this list */
        void add(int index) {
            curr++;
            if (curr * 2 + 1 >= indices.length) {
                int newSize = indices.length * 3 / 2;
                if (newSize < 10) {
                    newSize = 10;
                }
                int[] resized = new int[newSize];
                System.arraycopy(indices, 0, resized, 0, indices.length);
                indices = resized;
            }
            indices[curr] = index;
        }

        /** Resets the indices */
        void clear() {
            curr = -1;
        }

        int get(int position) {
            return indices[position];
        }

        void sort() {
            Arrays.sort(indices, 0, curr + 1);
        }
    }
}
