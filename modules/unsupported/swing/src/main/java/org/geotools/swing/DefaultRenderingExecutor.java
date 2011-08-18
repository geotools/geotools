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
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.geotools.map.MapContent;
import org.geotools.renderer.GTRenderer;

/**
 * The default implementation of {@code RenderingExecutor} which is used by 
 * {@linkplain JMapPane} and {@linkplain JLayeredMapPane}. It runs no more than 
 * one rendering task at any given time, although that task may involve multiple
 * threads (e.g. each layer of a map being rendered into separate destinations.
 * While a task is running any other submitted tasks are rejected.
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
 *     executor.setPollingInterval( 10 );  // 10 milliseconds
 * </code></pre>
 *
 * @author Michael Bedward
 * @since 2.7
 * @source $URL$
 * @version $Id$
 * 
 * @see RenderingExecutorListener
 */
public class DefaultRenderingExecutor implements RenderingExecutor {

    private final AtomicLong NEXT_ID = new AtomicLong(1);
    
    private final ExecutorService taskExecutor;
    private final ScheduledExecutorService watchExecutor;
    private ScheduledFuture<?> watcher;
    
    private CountDownLatch tasksLatch = new CountDownLatch(0);

    /** The default interval (milliseconds) for polling the result of a rendering task */
    public static final long DEFAULT_POLLING_INTERVAL = 20L;

    private long pollingInterval;

    private static class DaemonThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        }
    }

    private static class TaskInfo {
        final long id;
        final RenderingTask task;
        final MapContent mapContent;
        final Future<Boolean> future;
        final RenderingExecutorListener listener;
        boolean polledDone;

        TaskInfo(long id, RenderingTask task, MapContent mapContent,
                Future<Boolean> future, RenderingExecutorListener listener) {
            this.id = id;
            this.task = task;
            this.mapContent = mapContent;
            this.future = future;
            this.listener = listener;
            this.polledDone = false;
        }
    }
    
    private List<TaskInfo> currentTasks;

    /**
     * Creates a new executor.
     */
    public DefaultRenderingExecutor() {
        currentTasks = new CopyOnWriteArrayList<TaskInfo>();
        
        taskExecutor = Executors.newCachedThreadPool();
        pollingInterval = DEFAULT_POLLING_INTERVAL;
        
        watchExecutor = Executors.newSingleThreadScheduledExecutor(new DaemonThreadFactory());
        startPolling();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getPollingInterval() {
        return pollingInterval;
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
    @Override
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
        
        
        if (tasksLatch.getCount() == 0) {
            tasksLatch = new CountDownLatch(1);

            long id = NEXT_ID.getAndIncrement();
            RenderingExecutorEvent event = new RenderingExecutorEvent(this, id);
            listener.onRenderingStarted(event);
            
            RenderingTask task = new RenderingTask(mapContent, graphics, renderer);
            Future<Boolean> future = taskExecutor.submit(task);
            currentTasks.add( new TaskInfo(id, task, mapContent, future, listener) );
            rtnValue = id;
        }
        
        return rtnValue;
    }
    
    @Override
    public long submit(MapContent mapContent, 
            List<RenderingOperands> operands,
            RenderingExecutorListener listener) {
        
        long rtnValue = RenderingExecutor.TASK_REJECTED;
        
        if (taskExecutor.isShutdown()) {
            throw new IllegalStateException("Calling submit after the executor has been shutdown");
        }
        
        if (mapContent == null) {
            throw new IllegalArgumentException("mapContent must not be null");
        }
        if (mapContent.getViewport().isEmpty()) {
            throw new IllegalArgumentException("The viewport must not be empty");
        }
        if (operands == null || operands.isEmpty()) {
            throw new IllegalArgumentException("operands list must not be null or empty");
        }
        if (listener == null) {
            throw new IllegalArgumentException("listener must not be null");
        }
        
        if (tasksLatch.getCount() == 0) {
            tasksLatch = new CountDownLatch(operands.size());
            
            long id = NEXT_ID.getAndIncrement();
            RenderingExecutorEvent event = new RenderingExecutorEvent(this, id);
            listener.onRenderingStarted(event);
            
            for (RenderingOperands op : operands) {
                MapContent mc = new SingleLayerMapContent(op.getLayer());
                mc.setViewport(mapContent.getViewport());
                op.getRenderer().setMapContent(mc);
                RenderingTask task = new RenderingTask(mapContent, op.getGraphics(), op.getRenderer());
                Future<Boolean> future = taskExecutor.submit(task);
                currentTasks.add( new TaskInfo(id, task, mc, future, listener) );
            }
            rtnValue = id;
        }
        
        return rtnValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void cancel(long taskId) {
        if (!currentTasks.isEmpty() && currentTasks.get(0).id == taskId) {
            cancelAll();
        }
    }
    
    /**
     * {@inheritDoc} 
     * Since this task can only ever have a single task running, and 
     * no tasks queued, this method simply checks for a running task 
     * and, if one exists, cancels it.
     */
    @Override
    public synchronized void cancelAll() {
        for (TaskInfo info : currentTasks) {
            info.task.cancel();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdown() {
        if (taskExecutor != null && !taskExecutor.isShutdown()) {
            taskExecutor.shutdown();
            watchExecutor.shutdown();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isShutdown() {
        return taskExecutor.isShutdown();
    }
    
    private void pollTaskResult() {
        for (TaskInfo info : currentTasks) {
            if (!info.polledDone && info.future.isDone()) {
                info.polledDone = true;
                
                Boolean result = null;
                try {
                    result = info.future.get();
                } catch (CancellationException ex) {
                    result = false;
                } catch (Exception ex) {
                    throw new IllegalStateException("When getting rendering result", ex);
                }

                RenderingExecutorEvent event = new RenderingExecutorEvent(this, info.id);
                if (!result) {
                    info.listener.onRenderingFailed(event);
                } else {
                    tasksLatch.countDown();
                    if (tasksLatch.getCount() == 0) {
                        info.listener.onRenderingCompleted(event);
                        currentTasks.clear();
                        break;
                    }
                }
            }
        }
    }

    private void startPolling() {
        watcher = watchExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
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

