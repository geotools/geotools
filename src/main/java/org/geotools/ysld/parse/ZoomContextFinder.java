package org.geotools.ysld.parse;

import java.util.Set;

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
     * @return The named context, or null if it is not present
     */
    public ZoomContext get(String name);
    
    /**
     * Get all valid names
     * @return
     */
    public Set<String> getNames();
    
    /**
     * Get one name for each available ZoomContext.
     * @return
     */
    public Set<String> getCanonicalNames();
}
