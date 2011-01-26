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
package org.geotools.caching.grid.spatialindex;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;

import org.geotools.caching.spatialindex.Node;
import org.geotools.caching.spatialindex.NodeIdentifier;
import org.geotools.caching.spatialindex.RegionNodeIdentifier;
import org.geotools.caching.spatialindex.Shape;

/** 
 * 
 * A node in the grid.
 * <p>Data objects are stored in an HashSet.</p>
 *
 * @author Christophe Rousson, SoC 2007, CRG-ULAVAL
 *
 *
 * @source $URL$
 */
public class GridNode implements Node, Serializable {
    private static final long serialVersionUID = 7786313461725794946L;
    
    protected HashSet<GridData> data; //data contained in node   
    transient protected RegionNodeIdentifier id = null;	//identifier of node

    /**
     * No-arg constructor for serialization purpose.
     * Note: after deserialized you need to call setIdentifier(NodeIdentifier) to setup
     * the node properly.
     *
     */
    protected GridNode() {
    }

    protected GridNode(RegionNodeIdentifier id) {
        this.data = new HashSet<GridData>();
        this.id = id;
    }
    
    public void setIdentifier(NodeIdentifier id){
    	this.id = (RegionNodeIdentifier)id;
    }

    public NodeIdentifier getChildIdentifier(int index)
        throws IndexOutOfBoundsException {
        throw new UnsupportedOperationException("GridNode have no children.");
    }

    public int getChildrenCount() {
        return 0;
    }

    public int getLevel() {
        return 0;
    }

    public boolean isIndex() {
        return false;
    }

    public boolean isLeaf() {
        return true;
    }

    public NodeIdentifier getIdentifier() {
        return id;
    }

    public Shape getShape() {
        return this.getIdentifier().getShape();
    }

    /** Insert new data in this node.
     *
     * @param id of data
     * @param data
     */
    protected boolean insertData(GridData data) {
    	synchronized (this.data) {
    		if (this.data.contains(data)) {
                return false;
            } else {
                this.data.add(data);
                return true;
            }	
		}
        
    }

    protected void deleteData(GridData data) {
    	this.data.remove(data);
    }

    /** 
     * Erase all data referenced by this node
     * and clears the valid field of associated node id.
     */
    public void clear() {
    	synchronized (this.data) {
    		this.data.clear();
            getIdentifier().setValid(false);	
		}
        
    }

    public int getDataCount() {
    	return this.data.size();
    }
    
    public HashSet<GridData> getData(){
    	return this.data;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("GridNode: MBR:" + this.getIdentifier().getShape());

        return sb.toString();
    }

    public String toReadableText() {
        StringBuffer sb = new StringBuffer();
        sb.append("GridNode ******");
        sb.append("\tMBR= " + this.getIdentifier().getShape() + "\n");
        sb.append("\t#Data= " + this.data.size() + "\n");

        for (Iterator<GridData> it = data.iterator(); it.hasNext();) {
            sb.append("\t\t" + it.next().getData().toString() + "\n");
        }
        return sb.toString();
    }
    
    
    private void writeObject(ObjectOutputStream stream) throws IOException{
    	synchronized (this.data) {
    		stream.writeObject(data);			
		}
    }
    
    private  void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
        this.data = (HashSet<GridData>)stream.readObject();
    }
    
}
