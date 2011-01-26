package org.geotools.caching.grid.spatialindex;

import org.geotools.caching.spatialindex.Data;
import org.geotools.caching.spatialindex.Node;
import org.geotools.caching.spatialindex.Region;
import org.geotools.caching.spatialindex.Visitor;

/**
 * Visitor that invalidates nodes
 * 
 * @author Emily
 *
 *
 * @source $URL$
 */
public class GridInvalidatingVisitor implements Visitor {
	
    private Region region;
    private GridSpatialIndex index;

    /**
     * Creates a new Invalidating Visitor.
     * 
     * @param r the region to invalid nodes within
     */
    public GridInvalidatingVisitor(GridSpatialIndex index, Region r) {
        this.region = r;
        this.index = index;
    }
    
    /**
     * Creates a new Invalidating Visitor that will invalidate
     * all nodes visited (no matter where they are).
     */
    public GridInvalidatingVisitor(GridSpatialIndex index) {
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
			n.clear();
			index.writeNode(n);
		}
	}
}
