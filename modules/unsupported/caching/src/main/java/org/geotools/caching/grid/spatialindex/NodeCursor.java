package org.geotools.caching.grid.spatialindex;

import org.geotools.caching.spatialindex.NodeIdentifier;
import org.geotools.caching.spatialindex.Region;
import org.geotools.caching.spatialindex.Shape;

/**
 * Class allows you to iterator over the nodes
 * that interest a given shape.
 * 
 * @author Emily
 * @since 1.2.0
 *
 *
 * @source $URL$
 */
public class NodeCursor {

    private GridRootNode root;
    private int dimension;
    private int[] mins ;
    private int[] maxs ;
    private int[] cursor;
    private boolean hasNext;
    
    
    public NodeCursor(GridRootNode root, Shape s){
        this.dimension = root.getShape().getDimension();
        this.root = root;
        
        mins = new int[this.dimension];
        maxs = new int[this.dimension];
        cursor = new int[this.dimension];

        findMatchingTiles(s, cursor, mins, maxs);
        hasNext = true;
    }
    
    private void findMatchingTiles(Shape shape, int[] cursor, final int[] mins, final int[] maxs) {
        Region shapembr = shape.getMBR();
        for (int i = 0; i < this.dimension; i++) {
            mins[i] = (int) ((shapembr.getLow(i) - this.root.getShape().getMBR().getLow(i)) / ((GridRootNode)this.root).tiles_size);
            cursor[i] = mins[i];
            maxs[i] = (int) ((shapembr.getHigh(i) - this.root.getShape().getMBR().getLow(i)) / ((GridRootNode)this.root).tiles_size);
            int maxcnt = ((GridRootNode)this.root).getMaximumTileCount(i);
            if (maxs[i] >= maxcnt){
                //max tile count is 2 then we was the maximum index to be 1
                maxs[i] = maxcnt-1;
            }
        }
    }
    
    /**
     * The number of tiles that match the given shape
     *
     * @return
     */
    public int getChildCount(){
        int tiles = 1;
        for (int i = 0; i < this.dimension; i++) {
            tiles *= (maxs[i] - mins[i] + 1);
        }
        return tiles;
    }
    
    
    /**
     *
     * @return the next node id in the cursor; will return null if no more nodes
     */
    public NodeIdentifier getNext(){
        NodeIdentifier ret = null;
        if (hasNext){
            ret = root.getChildIdentifier(root.gridIndexToNodeId(cursor));
        }
        int dims = cursor.length;
        for (int i = 0; i < dims; i++) {
            cursor[i]++;
            if (cursor[i] > maxs[i]) {
                cursor[i] = mins[i];
                if (i == (dims - 1)) {
                    //return null;
                    hasNext = false;
                    break;
                }
            } else {
                break;
            }
        }
        return ret;
        
    }
}
