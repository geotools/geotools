/**
 * 
 */
package org.geotools.util;

import java.util.LinkedList;
import java.util.Queue;

import org.opengis.util.InternationalString;

/**
 * Default  Implementation of {@link ProgressListener} that does retain exceptions.
 * 
 * <p>
 * We do not put particular attention on the management of canceled, started, completed, this is a default implementation.
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 * @since 2.8
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/library/main/src/main/java/org/geotools/util/DefaultProgressListener.java $
 */
@SuppressWarnings("deprecation")
public class DefaultProgressListener extends NullProgressListener implements ProgressListener,
        org.opengis.util.ProgressListener {
    
    @Override
    public String toString() {
        return "DefaultProgressListener [completed=" + completed + ", progress=" + progress
                + ", started=" + started + ", task=" + task + "]";
    }

    /**
     * Collector class for warnings.
     * 
     * @author Simone Giannecchini, GeoSolutions SAS
     * @since 2.8
     * 
     */
    public static class Warning{
        @Override
        public String toString() {
            return "Warning [margin=" + margin + ", source=" + source + ", warning=" + warning
                    + "]";
        }
        public String getSource() {
            return source;
        }
        public void setSource(String source) {
            this.source = source;
        }
        public String getMargin() {
            return margin;
        }
        public void setMargin(String margin) {
            this.margin = margin;
        }
        public String getWarning() {
            return warning;
        }
        public void setWarning(String warning) {
            this.warning = warning;
        }
        private String source;
        private String margin; 
        private String warning;
    }

    /** List of warnings occurred during the execution.**/
    private final Queue<Warning> warnings= new LinkedList<Warning>();
    
    /** List of exceptions that were caught during executiong.**/
    private final Queue<Throwable> exceptionQueue= new LinkedList<Throwable>();
    
    /** IS the task we are listening for completed?.**/
    private boolean completed;

    /** What is the progress of the task we are listening for?.**/
    private float progress;

    /** Identifier of the task we are listening for.**/
    private InternationalString task;

    /** Has the task we are listening for started?**/
    private boolean started;

    /* (non-Javadoc)
     * @see org.geotools.util.ProgressListener#complete()
     */
    public void complete() {
        this.completed=true;
        
    }


    /* (non-Javadoc)
     * @see org.geotools.util.ProgressListener#dispose()
     */
    public void dispose() {
        exceptionQueue.clear();
        warnings.clear();        
    }

    /* (non-Javadoc)
     * @see org.geotools.util.ProgressListener#exceptionOccurred(java.lang.Throwable)
     */
    public void exceptionOccurred(Throwable exception) {
        this.exceptionQueue.add(exception);
        
    }


    /* (non-Javadoc)
     * @see org.geotools.util.ProgressListener#progress(float)
     */
    public void progress(float percent) {
        this.progress=percent;
        
    }


    /* (non-Javadoc)
     * @see org.geotools.util.ProgressListener#started()
     */
    public void started() {
        this.started=true;
        
    }

    /* (non-Javadoc)
     * @see org.geotools.util.ProgressListener#warningOccurred(java.lang.String, java.lang.String, java.lang.String)
     */
    public void warningOccurred(String source, String margin, String warning) {
        final Warning w= new Warning();
        w.setMargin(margin);
        w.setSource(source);
        w.setWarning(warning);
        warnings.add(w);
    }

    /* (non-Javadoc)
     * @see org.opengis.util.ProgressListener#getProgress()
     */
    public float getProgress() {
        return progress;
    }

    /* (non-Javadoc)
     * @see org.opengis.util.ProgressListener#getTask()
     */
    public InternationalString getTask() {
        return task;
    }

    /* (non-Javadoc)
     * @see org.opengis.util.ProgressListener#setTask(org.opengis.util.InternationalString)
     */
    public void setTask(InternationalString task) {
        this.task=task;
    }


    /**
     * Is the task we are listening is completed.
     * 
     * @return <code>true</code> if the task is completed, <code>false</code> if it is not.
     */
    public boolean isCompleted() {
        return completed;
    }
    
    /**
     * Return a copy of the {@link Queue} of exceptions that had happened. 
     * 
     * @return a copy of the {@link Queue} of exceptions that had happened.
     */
    public Queue<Throwable> getExceptions(){
        return new LinkedList<Throwable>(exceptionQueue);
    }

    /**
     * It tells us if we have exceptions or not.
     * 
     * @return <code>true</code> if there are exceptions, <code>false</code> otherwise.
     */
    public boolean hasExceptions() {
        return exceptionQueue.size()>0;
    }


    /**
     * Is the task we are listening for started.
     * 
     * @return <code>true</code> if the task is started, <code>false</code> if it is not.
     */
    public boolean isStarted() {
        return started;
    }

    /**
     * Retrieves a copy of the warnings occurred.
     * 
     * @return a copy of the warnings occurred.
     */
    public Queue<Warning> getWarnings() {
        return new LinkedList<Warning>(warnings);
    }    
}
