/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process;

import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

import org.geotools.util.SimpleInternationalString;
import org.opengis.util.InternationalString;
import org.opengis.util.ProgressListener;

/**
 * An implementation of the Progress interface.
 *
 * @author gdavis, Jody
 *
 * @source $URL$
 */
public class ProgressTask implements Runnable, Progress {

    /** Synchronization control */
    private final Synchronizer synchronizer;
     
    /**
     * Creates a ProgressTask that will execute the
     * given Process when run.
     *
     * @param  process the process to execute
     * @param input the inputs to use when executing the process
     * @throws NullPointerException if process is null
     */
    public ProgressTask(Process process, Map<String,Object> input) {
        if (process== null) {
            throw new NullPointerException();
        }
        synchronizer = new Synchronizer(process, input);            
    }	
	
    public float getProgress() {
        return synchronizer.getProgress();
    }
    
    public boolean isCancelled() {
        return synchronizer.innerIsCancelled();
    }
    
    public boolean isDone() {
        return synchronizer.innerIsDone();
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        return synchronizer.innerCancel(mayInterruptIfRunning);
    }
    
    public Map<String,Object> get() throws InterruptedException, ExecutionException {
        return synchronizer.innerGet();
    }

    public Map<String,Object> get(long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException {
        return synchronizer.innerGet(unit.toNanos(timeout));
    }

    /**
     * This protected method is invoked when this process transitions to state
     * isDone (whether normally or via cancellation). The
     * default implementation does nothing.  Subclasses may override
     * this method to invoke completion callbacks.  You can query status inside 
     * the implementation of this method to determine whether this task
     * has been canceled.
     */
    protected void done() { }

    /**
     * Sets the result of this ProgressTask to the given value unless
     * this ProgressTask has already been set or has been canceled.
     * 
     * @param value the value to set
     */ 
    protected void set(Map<String,Object> value) {
        synchronizer.innerSet(value);
    }

    /**
     * Causes this ProgressTask to report an ExecutionException
     * with the given throwable as its cause, unless this ProgressTask has
     * already been set or has been canceled.
     * 
     * @param t the cause of failure.
     */ 
    protected void setException(Throwable t) {
        synchronizer.innerSetException(t);
    }
    
    /**
     * Sets this ProgressTask to the result of the computation unless
     * it has been canceled.
     */
    public void run() {
        synchronizer.innerRun();
    }

    /**
     * Executes the process without setting its result, and then
     * resets this ProgressTask to its initial state, failing to do so if the
     * computation encounters an exception or is canceled.  This is
     * designed for use with processes that execute more
     * than once.
     * 
     * @return true if successfully run and reset
     */
    protected boolean runAndReset() {
        return synchronizer.innerRunAndReset();
    }

    /**
     * Synchronization control for ProgressTask. 
	 *
	 * This must be a non-static inner class in order to invoke the protected
     * done method. For clarity, all inner class support
     * methods are same as outer, prefixed with "inner".
     *
     * Uses AQS synchronizer state to represent run status
     */
	private final class Synchronizer extends AbstractQueuedSynchronizer implements ProgressListener {

		private static final long serialVersionUID = 6633428077533811475L;

        /** State for process running */
        private static final int RUNNING = 1;
        /** State for process completed */
        private static final int COMPLETED = 2;
        /** State for process canceled */
        private static final int CANCELED = 4;

        /** The process */
        private final Process process;
        
        /** The process input parameters */
        private Map<String,Object> input;
        
        /** The result to return from get() */
        private Map<String,Object> result;
        
        /** The exception to throw from get() */
        private Throwable exception;

        /** 
         * The thread running process. When it is nulled after set/cancel, this
         * indicates that the results are now accessible.  This must be
         * volatile to ensure visibility upon completion.
         */
        private volatile Thread runningThread;
        private float percentComplete;
        private InternationalString processName;

        Synchronizer(Process process, Map<String,Object> input ) {
            this.process = process;
            this.input = input;
        }

        private boolean ranOrCancelled(int state) {
            return (state & (COMPLETED | CANCELED)) != 0;
        }

        /**
         * Implements AQS base acquire to succeed if ran or canceled
         */
        @Override
        protected int tryAcquireShared(int ignore) {
            return innerIsDone()? 1 : -1;
        }

        /**
         * Implements AQS base release to always signal after setting
         * final done status by nulling the runningThread.
         */
        @Override
        protected boolean tryReleaseShared(int ignore) {
            runningThread = null;
            return true; 
        }

        boolean innerIsCancelled() {
            return getState() == CANCELED;
        }
        
        boolean innerIsDone() {
            return ranOrCancelled(getState()) && runningThread == null;
        }

        Map<String,Object> innerGet() throws InterruptedException, ExecutionException {
            acquireSharedInterruptibly(0);
            if (getState() == CANCELED) {
                throw new CancellationException();
            }
            if (exception != null) {
                throw new ExecutionException(exception);
            }
            
            return result;
        }

        Map<String,Object> innerGet(long nanosTimeout) throws InterruptedException, ExecutionException, TimeoutException {
            if (!tryAcquireSharedNanos(0, nanosTimeout)) {
                throw new TimeoutException();    
            }
            if (getState() == CANCELED) {
                throw new CancellationException();
            }
            if (exception != null) {
                throw new ExecutionException(exception);
            }
            
            return result;
        }

        void innerSet(Map<String,Object> v) {
	        for (;;) {
	        	int s = getState();
	        	if (ranOrCancelled(s)) {
	        		return;
	        	}
	        	if (compareAndSetState(s, COMPLETED)) {
	        		break;
	        	}
	        }
	        
            result = v;
            releaseShared(0);
            done();
        }

        void innerSetException(Throwable t) {
	        for (;;) {
		        int s = getState();
		        if (ranOrCancelled(s)) {
		            return;
		        }
		        if (compareAndSetState(s, COMPLETED)) {
		            break;
		        }
	        }
	        
	        exception = t;
	        result = null;
	        releaseShared(0);
	        done();
        }

        boolean innerCancel(boolean mayInterruptIfRunning) {
        	for (;;) {
        		int s = getState();
        		if (ranOrCancelled(s)) {
        			return false;
        		}
        		if (compareAndSetState(s, CANCELED)) {
        			break;
        		}
        	}
        	
            if (mayInterruptIfRunning) {
                Thread r = runningThread;
                if (r != null) {
                    r.interrupt();
                }
            }
            
            releaseShared(0);
            done();
            return true;
        }

        void innerRun() {
            if (!compareAndSetState(0, RUNNING)) {
                return;
            }
            
            try {
                runningThread = Thread.currentThread();
                innerSet(process.execute( input, this ));
            } catch(Throwable ex) {
                innerSetException(ex);
            } 
        }

        boolean innerRunAndReset() {
            if (!compareAndSetState(0, RUNNING)) {
                return false;
            }
            
            try {
                runningThread = Thread.currentThread();
                process.execute( input, this ); // don't set the result
                runningThread = null;
                return compareAndSetState(RUNNING, 0);
            } catch(Throwable ex) {
                innerSetException(ex);
                return false;
            } 
        }

        public void complete() {
            // ignore
        }

        public void dispose() {
            // ignore
        }

        public void exceptionOccurred( Throwable t ) {
            innerSetException( t );
        }

        @Deprecated
        public String getDescription() {
            return getTask().toString();
        }

        public float getProgress() {
            return percentComplete;
        }

        public InternationalString getTask() {
            return processName;
        }

        public boolean isCanceled() {
            return innerIsCancelled();
        }

        public void progress( float percent ) {
            this.percentComplete = percent;
        }

        public void setCanceled( boolean stop ) {
            innerCancel( stop );
        }

        @Deprecated
        public void setDescription( String description ) {
            processName = new SimpleInternationalString( description );
        }

        public void setTask( InternationalString arg0 ) {
            this.processName = arg0; 
        }

        public void started() {
        	// ignore
        }

        public void warningOccurred( String arg0, String arg1, String arg2 ) {
        	// ignore 
        }
        
    } // end Synchornizer inner class
}
