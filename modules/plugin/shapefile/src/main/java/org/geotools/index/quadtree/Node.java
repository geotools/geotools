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
import java.util.Arrays;
import java.util.List;

import com.vividsolutions.jts.geom.Envelope;

/**
 * DOCUMENT ME!
 * 
 * @author Tommaso Nolli
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/shapefile/src/main/java/org/geotools/index/quadtree/Node.java $
 */
public class Node {
    protected int numShapesId;
    private boolean visited = false;
    private boolean childrenVisited = false;
    private Envelope bounds;
    protected int[] shapesId;
    protected List subNodes;

    public Node(Envelope bounds) {
        this.bounds = new Envelope(bounds);
        this.subNodes = new ArrayList(4);
        this.shapesId = null;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return Returns the bounds.
     */
    public Envelope getBounds() {
        return this.bounds;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param bounds
     *                The bounds to set.
     */
    public void setBounds(Envelope bounds) {
        this.bounds = bounds;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return Returns the numSubNodes.
     */
    public int getNumSubNodes() {
        return this.subNodes.size();
    }

    /**
     * DOCUMENT ME!
     * 
     * @return Returns the number of records stored.
     */
    public int getNumShapeIds() {
        return this.numShapesId;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param node
     * 
     * @throws NullPointerException
     *                 DOCUMENT ME!
     */
    public void addSubNode(Node node) {
        if (node == null) {
            throw new NullPointerException("Cannot add null to subnodes");
        }

        this.subNodes.add(node);
    }

    /**
     * Removes a subnode
     * 
     * @param node
     *                The subnode to remove
     * 
     * @return true if the subnode has been removed
     */
    public boolean removeSubNode(Node node) {
        return this.subNodes.remove(node);
    }

    /**
     * 
     * 
     */
    public void clearSubNodes() {
        this.subNodes.clear();
    }

    /**
     * Gets the Node at the requested position
     * 
     * @param pos
     *                The position
     * 
     * @return A Node
     * 
     * @throws StoreException
     *                 DOCUMENT ME!
     */
    public Node getSubNode(int pos) throws StoreException {
        return (Node) this.subNodes.get(pos);
    }

    /**
     * Add a shape id
     * 
     * @param id
     */
    public void addShapeId(int id) {
        if(this.shapesId == null) {
            this.shapesId = new int[4];
            Arrays.fill(this.shapesId, -1);
        } else if (this.shapesId.length == this.numShapesId) {
            // Increase the array
            int[] newIds = new int[(int) Math.ceil(this.numShapesId * 3.0 / 2.0)];
            Arrays.fill(newIds, -1);
            System.arraycopy(this.shapesId, 0, newIds, 0, this.numShapesId);
            this.shapesId = newIds;
        }

        this.shapesId[this.numShapesId] = id;
        this.numShapesId++;
    }

    /**
     * Gets a shape id
     * 
     * @param pos
     *                The position
     * 
     * @return The shape id (or recno) at the requested position
     * 
     * @throws ArrayIndexOutOfBoundsException
     *                 DOCUMENT ME!
     */
    public int getShapeId(int pos) {
        if (pos >= this.numShapesId) {
            throw new ArrayIndexOutOfBoundsException("Requsted " + pos
                    + " but size = " + this.numShapesId);
        }

        return this.shapesId[pos];
    }

    /**
     * Sets the shape ids
     * 
     * @param ids
     */
    public void setShapesId(int[] ids) {
        if (ids == null) {
            this.numShapesId = 0;
        } else {
            this.shapesId = ids;
            this.numShapesId = 0;

            for (int i = 0; i < ids.length; i++) {
                if (ids[i] == -1) {
                    break;
                }

                this.numShapesId++;
            }
        }
    }
    
    public void setShapesId(Node other) {
        this.numShapesId = other.numShapesId;
        this.shapesId = other.shapesId;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return Returns the shapesId.
     */
    public int[] getShapesId() {
        return this.shapesId;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isChildrenVisited() {
        return childrenVisited;
    }

    public void setChildrenVisited(boolean childrenVisited) {
        this.childrenVisited = childrenVisited;
    }

    public Node copy() throws IOException {
        Node copy = new Node(bounds);
        copy.setShapesId(shapesId);
        copy.numShapesId = numShapesId;
        
        return copy;
    }

    /**
     * Clears up whatever resources the node is hanging onto
     */
    public void close() {
        // TODO Auto-generated method stub
    }
    
    /**
     * To be used only against in memory nodes, allows to start over on
     * rebuilding this node
     */
    public void clean() {
        shapesId = null;
        numShapesId = 0;
        subNodes.clear();
    }

    public void pack() {
        if(numShapesId == 0) {
            shapesId = null;
        } else if(shapesId != null && shapesId.length > numShapesId) {
            int[] ids = new int[numShapesId];
            System.arraycopy(shapesId, 0, ids, 0, numShapesId);
            shapesId = ids;
        }
        
    }
}
