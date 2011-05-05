/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.swing;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.RenderListener;
import org.opengis.feature.simple.SimpleFeature;

/**
 * This class is used by {@code JMapPane} to handle the scheduling and running of
 * rendering tasks on a background thread. It functions as a single thread, non-
 * queueing executor, ie. only one rendering task can run at any given time and,
 * while it is running, any other submitted tasks will be rejected.
 * <p>
 * Whether a rendering task is accepted or rejected can be tested on submission:
 * <pre><code>
 *     ReferencedEnvelope areaToDraw = ...
 *     Graphics2D graphicsToDrawInto = ...
 *     boolean accepted = renderingExecutor.submit(areaToDraw, graphicsToDrawInto);
 * </code></pre>
 *
 * The status of the executor can also be checked at any time like this:
 * <pre><code>
 *     boolean busy = renderingExecutor.isRunning();
 * </code></pre>
 *
 * While a rendering task is running it is regularly polled to see if it has completed
 * and, if so, whether it finished normally, was cancelled or failed. The interval between
 * polling can be adjusted which might be useful to tune the executor for particular
 * applications:
 * <pre><code>
 *     RenderingExecutor re = new RenderingExecutor( mapPane );
 *     re.setPollingInterval( 10 );  // 10 milliseconds
 * </code></pre>
 *
 * @author Michael Bedward
 * @since 2.7
 * @source $URL$
 * @version $Id$
 *
 * @see JMapPane
 */
public class RenderingExecutor {

    private final JMapPane mapPane;
    private final ExecutorService taskExecutor;
    private final ScheduledExecutorService watchExecutor;

    /** The default interval (milliseconds) for polling the result of a rendering task */
    public static final long DEFAULT_POLLING_INTERVAL = 20L;

    private long pollingInterval;

    /*
     * This latch is used to avoid a race between the cancellation of
     * a current task and the submittal of a new task
     */
    private CountDownLatch cancelLatch;

    /**
     * Constants to indicate the result of a rendering task
     */
    public enum TaskResult {
        PENDING,
        COMPLETED,
        CANCELLED,
        FAILED;
    }

    private long numFeatures;

    private static class DaemonThreadFactory implements ThreadFactory {
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        }
    }

    /**
     * A rendering task
     */
    private class Task implements Callable<TaskResult>, RenderListener {

        private final ReferencedEnvelope envelope;
        private final Rectangle paintArea;
        private final Graphics2D graphics;

        private boolean cancelled;
        private boolean failed;

        /**
         * Constructor. Creates a new rendering task
         *
         * @param envelope map area to render (world coordinates)
         * @param paintArea drawing area (image or display coordinates)
         * @param graphics graphics object used to draw into the image or display
         */
        public Task(final ReferencedEnvelope envelope, final Rectangle paintArea, final Graphics2D graphics) {
            this.envelope = envelope;
            this.paintArea = paintArea;
            this.graphics = graphics;
            this.cancelled = false;
            failed = false;
        }

        /**
         * Called by the executor to run this rendering task.
         *
         * @return result of the task: completed, cancelled or failed
         * @throws Exception
         */
        public TaskResult call() throws Exception {
            if (!cancelled) {
                GTRenderer renderer = mapPane.getRenderer();
                try {
                    renderer.addRenderListener(this);

                    Composite composite = graphics.getComposite();
                    //graphics.setComposite(AlphaComposite.Src);
                    //graphics.setBackground(Color.WHITE);
                    graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
                    graphics.fill(paintArea);
                    graphics.setComposite(composite);

                    numFeatures = 0;

                    renderer.paint(graphics, mapPane.getVisibleRect(), envelope, mapPane.getWorldToScreenTransform());

                } finally {
                    renderer.removeRenderListener(this);
                }
            }

            if (cancelled) {
                return TaskResult.CANCELLED;
            } else if (failed) {
                return TaskResult.FAILED;
            } else {
                return TaskResult.COMPLETED;
            }
        }

        /**
         * Cancel the rendering task if it is running. If called before
         * being run the task will be abandoned.
         */
        public synchronized void cancel() {
            if (isRunning()) {
                cancelled = true;
                mapPane.getRenderer().stopRendering();
            }
        }

        /**
         * Called by the renderer when each feature is drawn.
         *
         * @param feature the feature just drawn
         */
        public void featureRenderer(SimpleFeature feature) {
            // @todo update a progress listener
            numFeatures++ ;
        }

        /**
         * Called by the renderer on error
         *
         * @param e cause of the error
         */
        public void errorOccurred(Exception e) {
            failed = true;
        }

    }

    private AtomicBoolean taskRunning;
    private Task task;
    private Future<TaskResult> taskResult;
    private ScheduledFuture<?> watcher;

    /**
     * Constructor. Creates a new executor to service the specified map pane.
     *
     * @param mapPane the map pane to be serviced
     */
    public RenderingExecutor(final JMapPane mapPane) {
        taskRunning = new AtomicBoolean(false);
        this.mapPane = mapPane;
        taskExecutor = Executors.newSingleThreadExecutor();
        watchExecutor = Executors.newSingleThreadScheduledExecutor(new DaemonThreadFactory());
        pollingInterval = DEFAULT_POLLING_INTERVAL;
        cancelLatch = new CountDownLatch(0);
    }

    /**
     * Get the interval for polling the result of a rendering task
     *
     * @return polling interval in milliseconds
     */
    public long getPollingInterval() {
        return pollingInterval;
    }

    /**
     * Set the interval for polling the result of a rendering task
     *
     * @param interval interval in milliseconds (values {@code <=} 0 are ignored)
     */
    public void setPollingInterval(long interval) {
        if (interval > 0) {
            pollingInterval = interval;
        }
    }

    /**
     * Submit a new rendering task. If no rendering task is presently running
     * this new task will be accepted; otherwise it will be rejected (ie. there
     * is no task queue).
     *
     * @param envelope the map area (world coordinates) to be rendered
     * @param graphics the graphics object to draw on
     *
     * @return true if the rendering task was accepted; false if it was
     *         rejected
     */
    public synchronized boolean submit(ReferencedEnvelope envelope, Rectangle paintArea, Graphics2D graphics) {
        if (!isRunning() || cancelLatch.getCount() > 0) {
            try {
                // wait for any cancelled task to finish its shutdown
                cancelLatch.await();
            } catch (InterruptedException ex) {
                return false;
            }

            task = new Task(envelope, paintArea, graphics);
            taskRunning.set(true);
            taskResult = taskExecutor.submit(task);
            watcher = watchExecutor.scheduleAtFixedRate(new Runnable() {

                public void run() {
                    pollTaskResult();
                }
            }, DEFAULT_POLLING_INTERVAL, DEFAULT_POLLING_INTERVAL, TimeUnit.MILLISECONDS);

            return true;
        }

        return false;
    }

    /**
     * Cancel the current rendering task if one is running
     */
    public synchronized void cancelTask() {
        if (isRunning()) {
            task.cancel();
            cancelLatch = new CountDownLatch(1);
        }
    }

    private void pollTaskResult() {
        if (!taskResult.isDone()) {
            return;
        }

        TaskResult result = TaskResult.PENDING;

        try {
            result = taskResult.get();
        } catch (Exception ex) {
            throw new IllegalStateException("When getting rendering result", ex);
        }

        watcher.cancel(false);
        taskRunning.set(false);

        /*
         * We zero the cancel latch here because it's possible that the job
         * completed (or failed) before it could be cancelled. When this statement 
         * was only executed for the CANCELLED case (below) it led to 
         * apps somtimes freezing.
         */
        cancelLatch.countDown();

        switch (result) {
            case CANCELLED:
                mapPane.onRenderingCancelled();
                break;

            case COMPLETED:
                mapPane.onRenderingCompleted();
                break;

            case FAILED:
                mapPane.onRenderingFailed();
                break;
        }
    }

    public synchronized boolean isRunning() {
        return taskRunning.get();
    }

}

