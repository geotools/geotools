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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.shapefile.shp.IndexFile;
import org.geotools.index.CloseableIterator;
import org.geotools.index.Data;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Java porting of mapserver quadtree implementation.<br>
 * <br>
 * Note that this implementation is <b>not thread safe</b>, so don't share the
 * same instance across two or more threads.
 * 
 * TODO: example of typical use...
 * 
 * @author Tommaso Nolli
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/shapefile/src/main/java/org/geotools/index/quadtree/QuadTree.java $
 */
public class QuadTree {

    private static final double SPLITRATIO = 0.55d;

    private static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger("org.geotools.index.quadtree");

    private Node root;
    private int numShapes;
    private int maxDepth;

    private IndexFile indexfile;

    private Set iterators = new HashSet();

    /**
     * Constructor. The maxDepth will be calculated.
     * 
     * @param numShapes
     *                The total number of shapes to index
     * @param maxBounds
     *                The bounds of all geometries to be indexed
     */
    public QuadTree(int numShapes, Envelope maxBounds, IndexFile file) {
        this(numShapes, 0, maxBounds, file);
    }

    /**
     * Constructor.
     * 
     * @param numShapes
     *                The total number of shapes to index
     * @param maxDepth
     *                The max depth of the index, must be <= 65535
     * @param maxBounds
     *                The bounds of all geometries to be indexed
     */
    public QuadTree(int numShapes, int maxDepth, Envelope maxBounds,
            IndexFile file) {
        if (maxDepth > 65535) {
            throw new IllegalArgumentException("maxDepth must be <= 65535");
        }

        this.numShapes = numShapes;
        this.maxDepth = maxDepth;

        if (maxBounds != null)
            this.root = new Node(new Envelope(maxBounds));

        if (maxDepth < 1) {
            /*
             * No max depth was defined, try to select a reasonable one that
             * implies approximately 8 shapes per node.
             */
            int numNodes = 1;
            this.maxDepth = 0;

            while (numNodes * 4 < numShapes) {
                this.maxDepth += 1;
                numNodes = numNodes * 2;
            }
        }
        this.indexfile = file;
    }

    /**
     * Constructor. WARNING: using this constructor, you have to manually set
     * the root
     * 
     * @param numShapes
     *                The total number of shapes to index
     * @param maxDepth
     *                The max depth of the index, must be <= 65535
     */
    public QuadTree(int numShapes, int maxDepth, IndexFile file) {
        this(numShapes, maxDepth, null, file);
    }

    /**
     * Inserts a shape record id in the quadtree
     * 
     * @param recno
     *                The record number
     * @param bounds
     *                The bounding box
     */
    public void insert(int recno, Envelope bounds) throws StoreException {
        this.insert(this.root, recno, bounds, this.maxDepth);
    }

    /**
     * Inserts a shape record id in the quadtree
     * 
     * @param node
     * @param recno
     * @param bounds
     * @param maxDepth
     * @throws StoreException
     */
    public void insert(Node node, int recno, Envelope bounds, int maxDepth)
            throws StoreException {

        if (maxDepth > 1 && node.getNumSubNodes() > 0) {
            /*
             * If there are subnodes, then consider whether this object will fit
             * in them.
             */
            Node subNode = null;
            for (int i = 0; i < node.getNumSubNodes(); i++) {
                subNode = node.getSubNode(i);
                if (subNode.getBounds().contains(bounds)) {
                    this.insert(subNode, recno, bounds, maxDepth - 1);
                    return;
                }
            }
        } 
        if (maxDepth > 1 && node.getNumSubNodes() < 4) {
            /*
             * Otherwise, consider creating four subnodes if could fit into
             * them, and adding to the appropriate subnode.
             */
            Envelope half1, half2, quad1, quad2, quad3, quad4;

            Envelope[] tmp = this.splitBounds(node.getBounds());
            half1 = tmp[0];
            half2 = tmp[1];

            tmp = this.splitBounds(half1);
            quad1 = tmp[0];
            quad2 = tmp[1];

            tmp = this.splitBounds(half2);
            quad3 = tmp[0];
            quad4 = tmp[1];

            Node subnode = null;            
            if (quad1.contains(bounds)) {
                subnode = new Node(quad1);
            } else if(quad2.contains(bounds)) {
                subnode = new Node(quad2);
            } else if(quad3.contains(bounds)) {
                subnode = new Node(quad3);
            } else if(quad4.contains(bounds)) {
                subnode = new Node(quad4);
            }
            
            if(subnode != null) {
                node.addSubNode(subnode);
                this.insert(subnode, recno, bounds, maxDepth - 1);
                return;
            }
        }

        // If none of that worked, just add it to this nodes list.
        node.addShapeId(recno);
    }

    /**
     * 
     * @param bounds
     * @return A List of Integer
     */
    public CloseableIterator<Data> search(Envelope bounds) throws StoreException {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, "Querying " + bounds);
        }

        try {
            return new LazySearchIterator(this, bounds);
        } catch (RuntimeException e) {
            LOGGER.warning("IOException occurred while reading root");
            return null;
        }
    }

    /**
     * Closes this QuadTree after use...
     * 
     * @throws StoreException
     */
    public void close(Iterator iter) throws IOException {
        iterators.remove(iter);
    }

    /**
     * 
     */
    public boolean trim() throws StoreException {
        LOGGER.fine("Trimming the tree...");
        return this.trim(this.root);
    }

    /**
     * Trim subtrees, and free subnodes that come back empty.
     * 
     * @param node
     *                The node to trim
     * @return true if this node has been trimmed
     */
    private boolean trim(Node node) throws StoreException {
        Node[] dummy = new Node[node.getNumSubNodes()];
        for (int i = 0; i < node.getNumSubNodes(); i++) {
            dummy[i] = node.getSubNode(i);
        }

        for (int i = 0; i < dummy.length; i++) {
            if (this.trim(dummy[i])) {
                node.removeSubNode(dummy[i]);
            }
        }

        /*
         * If I have only 1 subnode and no shape records, promote that subnode
         * to my position.
         */
        if (node.getNumSubNodes() == 1 && node.getNumShapeIds() == 0) {
            Node subNode = node.getSubNode(0);

            node.clearSubNodes();
            for (int i = 0; i < subNode.getNumSubNodes(); i++) {
                node.addSubNode(subNode.getSubNode(i));
            }

            node.setShapesId(subNode.getShapesId());
            node.setBounds(subNode.getBounds());
        }

        return (node.getNumSubNodes() == 0 && node.getNumShapeIds() == 0);
    }

    /**
     * Splits the specified Envelope
     * 
     * @param in
     *                an Envelope to split
     * @return an array of 2 Envelopes
     */
    private Envelope[] splitBounds(Envelope in) {
        Envelope[] ret = new Envelope[2];
        double range, calc;

        if ((in.getMaxX() - in.getMinX()) > (in.getMaxY() - in.getMinY())) {
            // Split in X direction
            range = in.getMaxX() - in.getMinX();

            calc = in.getMinX() + range * SPLITRATIO;
            ret[0] = new Envelope(in.getMinX(), calc, in.getMinY(), in
                    .getMaxY());

            calc = in.getMaxX() - range * SPLITRATIO;
            ret[1] = new Envelope(calc, in.getMaxX(), in.getMinY(), in
                    .getMaxY());
        } else {
            // Split in Y direction
            range = in.getMaxY() - in.getMinY();

            calc = in.getMinY() + range * SPLITRATIO;
            ret[0] = new Envelope(in.getMinX(), in.getMaxX(), in.getMinY(),
                    calc);

            calc = in.getMaxY() - range * SPLITRATIO;
            ret[1] = new Envelope(in.getMinX(), in.getMaxX(), calc, in
                    .getMaxY());
        }

        return ret;
    }

    /**
     * @return Returns the maxDepth.
     */
    public int getMaxDepth() {
        return this.maxDepth;
    }

    /**
     * @param maxDepth
     *                The maxDepth to set.
     */
    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    /**
     * @return Returns the numShapes.
     */
    public int getNumShapes() {
        return this.numShapes;
    }

    /**
     * @param numShapes
     *                The numShapes to set.
     */
    public void setNumShapes(int numShapes) {
        this.numShapes = numShapes;
    }

    /**
     * @return Returns the root.
     */
    public Node getRoot() {
        return this.root;
    }

    /**
     * @param root
     *                The root to set.
     */
    public void setRoot(Node root) {
        this.root = root;
    }

    public void close() throws StoreException {
        try {
            indexfile.close();
            root.close();
        } catch (IOException e) {
            throw new StoreException("error closing indexfile", e.getCause());
        }
        if (!iterators.isEmpty()) {
            throw new StoreException("There are still open iterators!!");
        }
    }

    public void registerIterator(Iterator object) {
        iterators.add(object);
    }

    public IndexFile getIndexfile() {
        return indexfile;
    }
}
