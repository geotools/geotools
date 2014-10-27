package org.geotools.ysld.parse;

/**
 * Mapping of names to ZoomContexts
 * 
 * @author Kevin Smith, Boundless
 *
 */
public interface ZoomContextFinder {

    /**
     * Get a named ZoomContext
     * @param name
     * @return
     */
    public ZoomContext get(String name);
    
}
