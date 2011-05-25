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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.geotools.caching.EvictableTree;
import org.geotools.caching.EvictionPolicy;
import org.geotools.caching.LRUEvictionPolicy;
import org.geotools.caching.grid.spatialindex.store.StorageFactory;
import org.geotools.caching.spatialindex.AbstractSpatialIndex;
import org.geotools.caching.spatialindex.Node;
import org.geotools.caching.spatialindex.NodeIdentifier;
import org.geotools.caching.spatialindex.Region;
import org.geotools.caching.spatialindex.RegionNodeIdentifier;
import org.geotools.caching.spatialindex.Shape;
import org.geotools.caching.spatialindex.SpatialIndex;
import org.geotools.caching.spatialindex.Storage;
import org.geotools.caching.spatialindex.Visitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;


/** A grid implementation of SpatialIndex.
 * A grid is a regular division of space, and is implemented as a very simple tree.
 * It has two levels, a top level consisting of one root node, and
 * a bottom level of nodes of the same size forming a grid.
 * Data is either inserted at the top level or at the bottom level,
 * and may be inserted more than once, if data intersects more than one node.
 * If data's shape is too big, it is inserted at the top level.
 * For the grid to be efficient, data should evenly distributed in size and in space,
 * and grid size should twice the mean size of data's shape.
 *
 * @author Christophe Rousson, SoC 2007, CRG-ULAVAL
 *
 *
 *
 * @source $URL$
 */
public class GridSpatialIndex extends AbstractSpatialIndex implements EvictableTree  {
    public static final String GRID_SIZE_PROPERTY = "Grid.GridSize";
    public static final String GRID_CAPACITY_PROPERTY = "Grid.GridCapacity";
    public static final String ROOT_MBR_MINX_PROPERTY = "Grid.RootMbrMinX";
    public static final String ROOT_MBR_MINY_PROPERTY = "Grid.RootMbrMinY";
    public static final String ROOT_MBR_MAXX_PROPERTY = "Grid.RootMbrMaxX";
    public static final String ROOT_MBR_MAXY_PROPERTY = "Grid.RootMbrMaxY";
    
    protected int MAX_INSERTION = 4;
    
    protected int gridsize;
    private int featureCapacity;
    
    protected Region mbr;
    private EvictionPolicy policy;
    private boolean doRecordAccess = true;
    

    /** Constructor. Creates a new Grid covering space given by <code>mbr</code>
     * and with at least <code>capacity</code> nodes.
     *
     * @param mbr
     * @param capacity - the number of tiles in the index
     * @param store - the backend index storage
     */
    public GridSpatialIndex(Region mbr, int gridsize, Storage store, int capacity) {
        this.gridsize = gridsize;
        this.mbr = mbr;
        this.store = store;
        this.stats = new GridSpatialIndexStatistics();
        this.policy = new LRUEvictionPolicy(this);
        this.featureCapacity = capacity;
        
        this.root = null;
        
        try{
            initializeFromStorage(this.store);
        }catch (Exception ex){
            //ignore any errors and move on
        }
        
        if (this.root == null){
            this.dimension = mbr.getDimension();
            //nothing read from storage so we need to create new ones
            this.store.clear();
            this.root = findUniqueInstance(new RegionNodeIdentifier(mbr));
            GridRootNode root = new GridRootNode(gridsize, (RegionNodeIdentifier)this.root);
            root.split(this);
            writeNode(root);
            this.stats.addToNodesCounter(root.getCapacity() + 1); // root has root.capacity nodes, +1 for root itself :)
        }
    }

    protected GridSpatialIndex() {
    }

    public static SpatialIndex createInstance(Properties pset) {
        Storage storage = StorageFactory.getInstance().createStorage(pset);
        
        int gridsize = Integer.parseInt(pset.getProperty(GRID_SIZE_PROPERTY));
        int gridcapacity = Integer.parseInt(pset.getProperty(GRID_CAPACITY_PROPERTY));
        double minx = Double.parseDouble(pset.getProperty(ROOT_MBR_MINX_PROPERTY));
        double miny = Double.parseDouble(pset.getProperty(ROOT_MBR_MINY_PROPERTY));
        double maxx = Double.parseDouble(pset.getProperty(ROOT_MBR_MAXX_PROPERTY));
        double maxy = Double.parseDouble(pset.getProperty(ROOT_MBR_MAXY_PROPERTY));
        Region mbr = new Region(new double[] { minx, miny }, new double[] { maxx, maxy });
        
        GridSpatialIndex instance = new GridSpatialIndex(mbr, gridsize, storage, gridcapacity);
        return instance;
    }

    /**
     * 
     * @return the root node of the grid
     */
    public GridRootNode getRootNode() {
		return (GridRootNode) this.rootNode;
	}
    
    public void dispose(){
    	this.store.dispose();
    }
    
    
    protected void visitData(Node n, Visitor v, Shape query, int type) {
        GridNode node = (GridNode) n;

        if (type == GridSpatialIndex.IntersectionQuery){
            for (Iterator<GridData> it = node.data.iterator(); it.hasNext();) {
                GridData d = it.next();
                if (query.intersects(d.getShape())){
                    v.visitData(d);
                }
            }
        }else if (type == GridSpatialIndex.ContainmentQuery){
            for (Iterator<GridData> it = node.data.iterator(); it.hasNext();) {
                GridData d = it.next();
                if (query.contains(d.getShape())){
                    v.visitData(d);
                }
            }
        }
    }

    public void clear() throws IllegalStateException {
        // we drop all nodes and recreate grid ; GC will do the rest
        this.store.clear();
        //create a new root node
        this.root = findUniqueInstance(new RegionNodeIdentifier(this.mbr));
        GridRootNode root = new GridRootNode(gridsize, (RegionNodeIdentifier)this.root);
        
        root.split(this);
        writeNode(root);
        this.stats.reset();
        this.stats.addToNodesCounter(root.getCapacity() + 1); // root has root.capacity nodes, +1 for root itself :)
        this.flush();
        
    }

    public Properties getIndexProperties() {
        Properties pset = store.getPropertySet();
        pset.setProperty(INDEX_TYPE_PROPERTY, GridSpatialIndex.class.getCanonicalName());
        pset.setProperty(GRID_SIZE_PROPERTY, new Integer(gridsize).toString());
        pset.setProperty(GRID_CAPACITY_PROPERTY, new Integer(featureCapacity).toString());
        pset.setProperty(ROOT_MBR_MINX_PROPERTY, new Double(mbr.getLow(0)).toString());
        pset.setProperty(ROOT_MBR_MINY_PROPERTY, new Double(mbr.getLow(1)).toString());
        pset.setProperty(ROOT_MBR_MAXX_PROPERTY, new Double(mbr.getHigh(0)).toString());
        pset.setProperty(ROOT_MBR_MAXY_PROPERTY, new Double(mbr.getHigh(1)).toString());

        return pset;
    }

    private boolean insertDataToNode(GridNode node, Object data, Shape shape){  
        GridData gd = new GridData(shape, data);
        if (node.getIdentifier().isWritable() && node.insertData(gd)) {
            writeNode(node);
            return true;
        }
        return false;

    }
    
    
    private boolean insertDataToNodeID(NodeIdentifier n, Object data, Shape shape) {
        if (!n.isValid()) return false;     //no point in writing to an invalid node
        GridNode node = (GridNode)readNode(n);
        return insertDataToNode(node, data, shape);
    }

    protected void insertData(NodeIdentifier n, Object data, Shape shape) {
        /* so we prefer this version :
         * data may be inserted more than one time, in each tile intersecting data's MBR.
         * However, very big MBR will cause data to be inserted in a large number of tiles :
         * given a threshold, data is inserted at root node.
         * */
        NodeCursor cc = new NodeCursor(getRootNode(),shape);
        boolean added = false;
        if (cc.getChildCount() > MAX_INSERTION) {
            GridRootNode node = getRootNode();
            added = insertDataToNode(node, data, shape);
        } else {
            NodeIdentifier next = null;
            while( (next = cc.getNext()) != null ){
                if (insertDataToNodeID(next, data, shape)){
                    added = true;
                }
            }
        }
        if (added){
        	this.stats.addToDataCounter(1); 	//even though the feature may be added to multiple nodes; it only counts as one more feature in the cache.
        	String x= "abc";
        }
    }

    protected void insertDataOutOfBounds(Object data, Shape shape) {
        throw new IllegalArgumentException("Grids cannot expand : Shape out of grid : " + shape);
    }

    public boolean isIndexValid() {
        // TODO Auto-generated method stub
        return true;
    }

    public NodeIdentifier findUniqueInstance(NodeIdentifier id) {
        return store.findUniqueInstance(id);
    }

    public void initializeFromStorage( Storage storage ) {
        //add feature types to marshaller so it'll know how to build features
        Collection<FeatureType> types = store.getFeatureTypes();
        for( Iterator<FeatureType> iterator = types.iterator(); iterator.hasNext(); ) {
            GridData.getFeatureMarshaller().registerType((SimpleFeatureType)iterator.next());            
        }
        
        //find the root node an initialize it here
        ReferencedEnvelope bounds = store.getBounds();
        if(bounds == null){
            return;             //cannot do anything because we need to know the bounds of the data.
        }
        this.mbr = new Region(new double[] { bounds.getMinX(), bounds.getMinY() }, new double[] { bounds.getMaxX(), bounds.getMaxY() });
        this.dimension = this.mbr.getDimension();
        NodeIdentifier id = findUniqueInstance(new RegionNodeIdentifier(this.mbr));
        //GridRootNode tmpRootNode = new GridRootNode(gridsize, (RegionNodeIdentifier)id);
        
        this.rootNode = null;
        try{
            this.rootNode = storage.get(id);
        }catch (Exception ex){
            //could not find root node in storage
        }
        
        if (this.rootNode == null){
            this.root = null;
        }else{
        	this.stats.reset();
            this.root = this.rootNode.getIdentifier();
            this.gridsize = ((GridRootNode)this.rootNode).getCapacity();
        
            this.stats.addToDataCounter(((GridRootNode)this.rootNode).getCapacity() + 1);	//children + 1 for root
            this.stats.addToDataCounter(this.rootNode.getDataCount());
            
            //here we need to match node identifies in the root.children list to the 
            //node identifiers in the data store
            for (int i = 0; i < this.rootNode.getChildrenCount(); i ++){
            	RegionNodeIdentifier cid = (RegionNodeIdentifier)findUniqueInstance(this.rootNode.getChildIdentifier(i));
                ((GridRootNode)this.rootNode).setChildIdentifier(i, cid);
                if (cid.isValid()){
                	Node n = readNode(cid);
                	this.stats.addToDataCounter(n.getDataCount());
                }
            }
        }
    }
    
    /** Common algorithm used by both intersection and containment queries.
    *
    * @param type
    * @param query
    * @param v
    *
    */
    @Override
   protected void rangeQuery(int type, Shape query, Visitor v) {
       GridRootNode tmpRoot = (GridRootNode)this.rootNode;
       
       //first we visit the root node
       v.visitNode(tmpRoot);
       if (v.isDataVisitor()){
    	   visitData(tmpRoot, v, query, type);
       }
       
       //here we need to visit just the children that may intersect
        List<Integer> childrenindex = tmpRoot.getChildren(query);
        for( Iterator<Integer> iterator = childrenindex.iterator(); iterator.hasNext(); ) {
            Integer childid = (Integer) iterator.next();
            NodeIdentifier child = tmpRoot.getChildIdentifier(childid);
            Node childNode = readNode(child);
            v.visitNode(childNode);
            if (v.isDataVisitor()) {
                visitData(childNode, v, query, type);
            }
        }
   }
    
    /**
     * Searches the index for missing tiles
     * 
     * Returns both the "valid" tiles and the "invalid" tiles
     * 
     * @param search	must be within the mbr of the index
     * @return
     */
    public List<NodeIdentifier>[] findMissingTiles(Region search) { 
        List<NodeIdentifier> missing = new ArrayList<NodeIdentifier>();
        List<NodeIdentifier> found = new ArrayList<NodeIdentifier>();

        if (!this.root.isValid()) {
            NodeCursor cc = new NodeCursor(getRootNode(), search);
            NodeIdentifier next = null;
            while ((next=cc.getNext()) != null){
                if (!next.isValid()){
                    missing.add(next);
                }else{
                    found.add(next);
                }
            }
        }

        List<NodeIdentifier>[] ret = new List[2];
        ret[0] = missing;
        ret[1] = found;
        return ret;
    }

    public int getEvictions() {
        return getStatistics().getEvictions();
    }

    public EvictionPolicy getEvictionPolicy(){
        return this.policy;
    }
    
    
    /**
     * must deal with synchronization outside this method.
     * 
     * This will blindly evict the node.
     */
    public void evict(NodeIdentifier node) {
        int ret = 0;
        int evictcnt = 1;

        // we need to write lock the entire cache here to prevent
        GridNode nodeToEvict = (GridNode) readNode(node); // FIXME: avoid to read node before
                                                          // eviction
        ret = nodeToEvict.getDataCount();
        // lets first evict all the children
        for( int i = 0; i < nodeToEvict.getChildrenCount(); i++ ) {
            Node n = readNode(nodeToEvict.getChildIdentifier(i));
            ret += n.getDataCount();
            n.clear();
            writeNode(n);
            evictcnt++;
        }
        // now evict the main node
        nodeToEvict.clear();
        writeNode(nodeToEvict);
        getStatistics().addToDataCounter(-ret);
        getStatistics().addToEvictionCounter(evictcnt);
    }

    public Node readNode(NodeIdentifier id) {
        if (doRecordAccess) {
            policy.access(id);
        }
        return super.readNode(id);
    }

    public GridSpatialIndexStatistics getStatistics(){
        return ((GridSpatialIndexStatistics)super.getStatistics());
    }
    
    /**
     * Assumes you have a write lock on the node
     * you are writing.
     */
    public void writeNode(Node node) {
        super.writeNode(node);
        if (doRecordAccess) {
            policy.access(node.getIdentifier());
        }
    }

    public boolean getDoRecordAccess(){
    	return this.doRecordAccess;
    }
    
    public void setDoRecordAccess(boolean b) {
        doRecordAccess = b;
    }

    /**
     * This function assumes that you have a lock on the necessary
     * nodes.
     */
    public void insertData(Object data, Shape shape) {
    	if (shape.getDimension() != dimension) {
            throw new IllegalArgumentException(
                "insertData: Shape has the wrong number of dimensions.");
        }

        if (this.root.getShape().contains(shape)) {
        	if (this.featureCapacity != Integer.MAX_VALUE) {
				while (this.getStatistics().getNumberOfData() >= this.featureCapacity) {
					if (!getEvictionPolicy().evict()) {
						// no more space left and nothing else to evict
						// need to evict the areas covered by this feature
						NodeCursor cc = new NodeCursor(getRootNode(), shape);
						NodeIdentifier next = null;
						int cnt = 0;
						while ((next = cc.getNext()) != null) {
							Node n = readNode(next);
							cnt += n.getDataCount();
							n.clear();
							writeNode(n);
							getStatistics().addToEvictionCounter(1);
						}
						getStatistics().addToDataCounter(-cnt);
						return;
					}
				}
			}
            insertData(this.root, data, shape);
        } else {
            insertDataOutOfBounds(data, shape);
        }
    }
}
