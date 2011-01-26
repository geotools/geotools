package org.geotools.caching.grid.spatialindex;

import org.geotools.caching.spatialindex.Data;
import org.geotools.caching.spatialindex.Node;
import org.geotools.caching.spatialindex.Region;
import org.geotools.caching.spatialindex.Visitor;

public class NodeLockInvalidatingVisitor implements Visitor {
	
    private Region region;
    private GridSpatialIndex index;

    private int dataCount = 0;
    
    /**
     * Creates a new Invalidating Visitor.
     * 
     * @param r the region to invalid nodes within
     */
    public NodeLockInvalidatingVisitor(GridSpatialIndex index, Region r) {
        this.region = r;
        this.index = index;
    }
    
    /**
     * Creates a new Invalidating Visitor that will invalidate
     * all nodes visited (no matter where they are).
     */
    public NodeLockInvalidatingVisitor(GridSpatialIndex index) {
    	this(index, null);
    }
    
    public boolean isDataVisitor() {
        return false;
    }

    public void visitData(Data<?> d) {
        // do nothing
    }
    
    public void visitNode(Node n) {
        
		if (this.region == null || this.region.contains(n.getShape())) {
			//clear & write out the node
			//note this will not clear children; in this case
			//we assume the validation will visit these children
			//and clear them separately
		    try{
		        n.getIdentifier().writeLock();
		        try {
		            dataCount += n.getDataCount();
		            n.clear();
		            index.writeNode(n);
		        } finally {
		            n.getIdentifier().writeUnLock();
		        }
		    }catch(Exception ex){
		        ex.printStackTrace();
		    }
		}
	}
    
    public int getDataCount(){
        return this.dataCount;
    }
}