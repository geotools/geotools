/**
 * 
 */
package org.geotools.util;

import junit.framework.Assert;

import org.junit.Test;


/**
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/library/main/src/test/java/org/geotools/util/TestDefaultProgressListener.java $
 */
public class TestDefaultProgressListener extends Assert {

    @Test
    public void testA(){
        final DefaultProgressListener listener = new DefaultProgressListener();
        listener.setTask(SimpleInternationalString.wrap("task"));
        assertNotNull(listener.getTask());
        assertTrue(listener.getTask().toString().equalsIgnoreCase("task"));
        
        listener.complete();
        assertTrue(listener.isCompleted());
        assertFalse(listener.isCanceled());
        assertFalse(listener.isStarted());
        
        listener.setCanceled(true);
        assertTrue(listener.isCompleted());
        assertTrue(listener.isCanceled());
        assertFalse(listener.isStarted());
        
        listener.complete();
        assertTrue(listener.isCompleted());
        assertTrue(listener.isCanceled());
        assertFalse(listener.isStarted());
        
        listener.started();
        assertTrue(listener.isCompleted());
        assertTrue(listener.isCanceled());
        assertTrue(listener.isStarted());
        
        listener.exceptionOccurred(new RuntimeException("error"));
        assertTrue(listener.hasExceptions());
        assertTrue(listener.getExceptions().peek() instanceof RuntimeException);
        
        
        
    }
}
