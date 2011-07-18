/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geotools.swing;

import java.awt.Graphics2D;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

import org.geotools.map.MapContent;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.RenderListener;
import org.opengis.feature.simple.SimpleFeature;

/**
 * A rendering task to be run by a {@code RenderingExecutor}.
 */
public class RenderingTask implements Callable<RenderingTask.Status>, RenderListener {
    
    private static long NEXT_ID = 1L;

    /**
     * Constants to indicate the result of a rendering task
     */
    public static enum Status {
        PENDING,
        COMPLETED,
        CANCELLED,
        FAILED;
    }

    private final long taskId;
    private final MapContent mapContent;
    private final GTRenderer renderer;
    private final Graphics2D graphics;

    private final AtomicBoolean running;
    private final AtomicBoolean cancelled;
    private final AtomicBoolean failed;
    
    private long numFeaturesRendered;

    /**
     * Creates a new rendering task.
     * 
     * @param mapContent
     * @param renderer 
     * @param graphics 
     */
    public RenderingTask(MapContent mapContent, 
            GTRenderer renderer, 
            Graphics2D graphics) {
        
        if (mapContent == null) {
            throw new IllegalArgumentException("mapContent must not be null");
        }
        if (renderer == null) {
            throw new IllegalArgumentException("renderer must not be null");
        }
        if (graphics == null) {
            throw new IllegalArgumentException("graphics must not be null");
        }
        
        this.taskId = NEXT_ID++;
        this.mapContent = mapContent;
        this.renderer = renderer;
        this.graphics = graphics;
        
        running = new AtomicBoolean(false);
        cancelled = new AtomicBoolean(false);
        failed = new AtomicBoolean(false);
        
        numFeaturesRendered = 0;
    }

    /**
     * Gets the unique ID value assigned to this task.
     * 
     * @return ID value
     */
    public long getId() {
        return taskId;
    }
    
    /**
     * Called by the executor to run this rendering task.
     *
     * @return result of the task: completed, cancelled or failed
     * @throws Exception
     */
    public Status call() throws Exception {
        if (!isCancelled()) {
            try {
                renderer.addRenderListener(this);
                running.set(true);
                renderer.paint(graphics, 
                        mapContent.getViewport().getScreenArea(), 
                        mapContent.getViewport().getBounds(), 
                        mapContent.getViewport().getWorldToScreen());
            } finally {
                renderer.removeRenderListener(this);
                running.set(false);
            }
        }
        
        if (isCancelled()) {
            return Status.CANCELLED;
        } else if (isFailed()) {
            return Status.FAILED;
        } else {
            return Status.COMPLETED;
        }
    }

    /**
     * Cancel the rendering task if it is running. If called before
     * being run the task will be abandoned.
     */
    public synchronized void cancel() {
        cancelled.set(true);
        if (isRunning()) {
            renderer.stopRendering();
            running.set(false);
        }
    }

    /**
     * Called by the renderer when each feature is drawn.
     *
     * @param feature the feature just drawn
     */
    public void featureRenderer(SimpleFeature feature) {
        numFeaturesRendered++ ;
    }

    /**
     * Called by the renderer on error
     *
     * @param e cause of the error
     */
    public void errorOccurred(Exception e) {
        running.set(false);
        failed.set(true);
    }
    
    public boolean isRunning() {
        return running.get();
    }
    
    public boolean isCancelled() {
        return cancelled.get();
    }
    
    public boolean isFailed() {
        return failed.get();
    }
    
}
