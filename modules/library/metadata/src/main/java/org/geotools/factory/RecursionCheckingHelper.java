package org.geotools.factory;

import java.util.HashSet;
import java.util.Set;

class RecursionCheckingHelper {

    private final ThreadLocal<Set> threadLocalSet = new ThreadLocal<Set>();
    
    boolean addAndCheck(Object item) {
        Set set = threadLocalSet.get();
        if(set == null) {
            set = new HashSet<Class<?>>();
            threadLocalSet.set(set);
        }
        return set.add(item);
    }
    
    boolean contains(Object item) {
        Set<Class<?>> set = threadLocalSet.get();
        if(set == null) {
            return false;
        }
        return set.contains(item);
    }
    
    void removeAndCheck(Object item) {
        Set<Class<?>> set = threadLocalSet.get();
        if(set == null) {
            throw new AssertionError(null); // Should never happen.
        } else if(!set.remove(item)) {
            throw new AssertionError(item); // Should never happen.
        } 
        if(set.isEmpty()) {
            threadLocalSet.remove();
        }
    }

}
