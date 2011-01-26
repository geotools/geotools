/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

import org.geotools.data.shapefile.shp.IndexFile;
import org.geotools.index.Data;
import org.geotools.index.DataDefinition;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Iterator that search the quad tree depth first. 32000 indices are cached at a
 * time and each time a node is visited the indices are removed from the node so
 * that the memory footprint is kept small. Note that if other iterators operate
 * on the same tree then they can interfere with each other.
 * 
 * @author Jesse
 *
 * @source $URL$
 */
public class LazySearchIterator implements Iterator<Data> {

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

    Iterator data;

    private IndexFile indexfile;
    
    ArrayList<Node> parents = new ArrayList<Node>();

    public LazySearchIterator(Node root, IndexFile indexfile, Envelope bounds) {
        super();
        this.current = root;
        this.bounds = bounds;
        this.closed = false;
        this.next = null;
        this.indexfile = indexfile;
    }

    public boolean hasNext() {
        if (closed)
            throw new IllegalStateException("Iterator has been closed!");
        if (next != null)
            return true;
        if (data != null && data.hasNext()) {
            next = (Data) data.next();
        } else {
            data = null;
            fillCache();
            if (data != null && data.hasNext())
                next = (Data) data.next();
        }
        return next != null;
    }

    private void fillCache() {
        List indices = new ArrayList(MAX_INDICES);
        ArrayList dataList = null;
        try {
            while (indices.size() < MAX_INDICES && current != null) {
                if (idIndex < current.getNumShapeIds() && !current.isVisited()
                        && current.getBounds().intersects(bounds)) {
                    indices.add(new Integer(current.getShapeId(idIndex)));
                    idIndex++;
                } else {
                    // free the shapes id array of the current node and prepare to move to the next
                    current.setShapesId(new int[0]);
                    idIndex = 0;
                    
                    boolean foundUnvisited = false;
                    for (int i = 0; i < current.getNumSubNodes(); i++) {
                        Node node = current.getSubNode(i);
                        if (!node.isVisited()
                                && node.getBounds().intersects(bounds)) {
                            foundUnvisited = true;
                            parents.add(current);
                            current = node;
                            break;
                        }
                    }
                    if (!foundUnvisited) {
                        // mark as visited and free the subnodes
                        current.setVisited(true);
                        current.clearSubNodes();
                        
                        // move up to parent
                        if(parents.isEmpty())
                            current = null;
                        else
                            current = parents.remove(parents.size() - 1);
                    }
                }
            }
            
            // sort so offset lookup is faster
            Collections.sort(indices);
            dataList = new ArrayList(indices.size());
            for (Iterator iter = indices.iterator(); iter.hasNext();) {
                Integer recno = (Integer) iter.next();
                Data data = new Data(DATA_DEFINITION);
                data.addValue(new Integer(recno.intValue() + 1));
                data.addValue(new Long(indexfile.getOffsetInBytes(recno
                        .intValue())));
                dataList.add(data);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (StoreException e) {
            throw new RuntimeException(e);
        }
        data = dataList.iterator();
    }

    public Data next() {
        if (!hasNext())
            throw new NoSuchElementException("No more elements available");
        Data temp = next;
        next = null;
        return temp;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public void close() throws StoreException {
        this.closed = true;
    }

}
