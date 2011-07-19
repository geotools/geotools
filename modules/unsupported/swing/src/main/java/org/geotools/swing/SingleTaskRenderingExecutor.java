/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2011, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.Graphics2D;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.geotools.map.MapContent;
import org.geotools.renderer.GTRenderer;

/**
 * A single threaded, non-queueing {@code RenderingExecutor}. Only one 
 * rendering task can run at any given time and, while it is running, any 
 * other submitted tasks will be rejected.
 * <p>
 * Whether a rendering task is accepted or rejected can be tested on submission:
 * <pre><code>
 * taskId = executor.submit(areaToDraw, graphicsToDrawInto);
 * if (taskId == RenderingExecutor.TASK_REJECTED) {
 *     ...
 * }
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
 * @see RenderingExecutorListener
 */
public class SingleTaskRenderingExecutor implements RenderingExecutor {

    //private final JMapPane mapPane;
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

    private static class DaemonThreadFactory implements ThreadFactory {
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        }
    }

    private RenderingTask task;
    private Future<RenderingTask.Status> taskFuture;
    private RenderingExecutorListener listener;
    private ScheduledFuture<?> watcher;
    private AtomicBoolean notifiedStart;

    /**
     * Creates a new executor.
     */
    public SingleTaskRenderingExecutor() {
        taskExecutor = Executors.newSingleThreadExecutor();
        pollingInterval = DEFAULT_POLLING_INTERVAL;
        cancelLatch = new CountDownLatch(0);
        notifiedStart = new AtomicBoolean();
        
        watchExecutor = Executors.newSingleThreadScheduledExecutor(new DaemonThreadFactory());
        startPolling();
    }

    /**
     * {@inheritDoc}
     */
    public long getPollingInterval() {
        return pollingInterval;
    }

    /**
     * {@inheritDoc}
     */
    public void setPollingInterval(long interval) {
        if (interval > 0 && interval != pollingInterval) {
            pollingInterval = interval;
            restartPolling();
        }
    }
    
    /**
     * {@inheritDoc}
     * If no rendering task is presently running this new task will be accepted, 
     * otherwise it will be rejected (ie. there is no task queue).
     */
    public synchronized long submit(MapContent mapContent, GTRenderer renderer,
            Graphics2D graphics,
            RenderingExecutorListener listener) {
        
        long rtnValue = RenderingExecutor.TASK_REJECTED;
        
        if (taskExecutor.isShutdown()) {
            throw new IllegalStateException("Calling submit after the executor has been shutdown");
        }
        
        if (mapContent == null) {
            throw new IllegalArgumentException("mapContent must not be null");
        }
        if (graphics == null) {
            throw new IllegalArgumentException("graphics must not be null");
        }
        if (mapContent.getViewport().isEmpty()) {
            throw new IllegalArgumentException("The viewport must not be empty");
        }
        if (listener == null) {
            throw new IllegalArgumentException("listener must not be null");
        }
        
        
        if (!hasUnfinishedTask()) {
            try {
                // wait for any cancelled task to finish its shutdown
                cancelLatch.await();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

            task = new RenderingTask(mapContent, renderer, graphics);
            this.listener = listener;
            notifiedStart.set(false);
            taskFuture = taskExecutor.submit(task);
            rtnValue = task.getId();
        }
        
        return rtnValue;
    }
    
    /**
     * {@inheritDoc}
     * Since this task can only ever have a single task running, 
     * and no tasks queued, this method simply checks if the running
     * task has the specified ID value and, if so, cancels it.
     */
    public synchronized void cancel(long taskId) {
        // Allow for the possibility that cancel has been called before the 
        // task has started running
        if (hasUnfinishedTask(taskId)) {
            task.cancel();
            cancelLatch = new CountDownLatch(1);
        } else {
            throw new RuntimeException("no task");
        }
    }
    
    /**
     * {@inheritDoc} 
     * Since this task can only ever have a single task running, and 
     * no tasks queued, this method simply checks for a running task 
     * and, if one exists, cancels it.
     */
    public synchronized void cancelAll() {
        if (hasUnfinishedTask()) {
            task.cancel();
            cancelLatch = new CountDownLatch(1);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void shutdown() {
        if (taskExecutor != null && !taskExecutor.isShutdown()) {
            taskExecutor.shutdown();
            watchExecutor.shutdown();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isShutdown() {
        return taskExecutor.isShutdown();
    }
    
    /**
     * Checks if the executor is holding a specific task that is either running
     * or yet to start running.
     * 
     * @param taskId the task ID value
     * 
     * @return {@code true} if the given task exists and is unfinished
     */
    private boolean hasUnfinishedTask(long taskId) {
        return (task != null && task.getId() == taskId &&
                (taskFuture == null || !taskFuture.isDone()));
    }
    
    /**
     * Checks if the executor is holding a task that is either running
     * or yet to start running.
     * 
     * @return {@code true} if a task exists and is unfinished
     */
    private boolean hasUnfinishedTask() {
        return (task != null && (taskFuture == null || !taskFuture.isDone()));
    }

    private void notifyStarted(boolean force) {
        if (!notifiedStart.get() && (force || task.isRunning())) {
            RenderingExecutorEvent event = new RenderingExecutorEvent(this, task.getId());
            listener.onRenderingStarted(event);
            notifiedStart.set(true);
        }
    }
    
    private void pollTaskResult() {
        if (taskFuture == null) {
            return;
        } else if (!taskFuture.isDone()) {
            notifyStarted(false);
            return;
        }

        // call again in case the task was so quick we missed the start
        notifyStarted(true);
        
        RenderingTask.Status result = null;
        long taskId = task.getId();
        try {
            result = taskFuture.get();
        } catch (Exception ex) {
            throw new IllegalStateException("When getting rendering result", ex);
        }

        /*
         * We zero the cancel latch here because it's possible that the job
         * completed (or failed) before it could be cancelled. When this statement 
         * was only executed for the CANCELLED case (below) it caused apps
         * to freeze occasionally.
         */
        cancelLatch.countDown();
        
        task = null;
        taskFuture = null;

        RenderingExecutorEvent event = new RenderingExecutorEvent(this, taskId);
        switch (result) {
            case CANCELLED:
                listener.onRenderingCancelled(event);
                break;

            case COMPLETED:
                listener.onRenderingCompleted(event);
                break;

            case FAILED:
                listener.onRenderingFailed(event);
                break;
        }
    }

    private void startPolling() {
        watcher = watchExecutor.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    pollTaskResult();
                }
            }, pollingInterval, pollingInterval, TimeUnit.MILLISECONDS);
    }
    
    private void restartPolling() {
        stopPolling();
        startPolling();
    }
    
    private void stopPolling() {
        if (watcher != null && !watcher.isDone()) {
            watcher.cancel(false);
        }
    }
}

