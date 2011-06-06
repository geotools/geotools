package org.geotools.data.efeature;

/**
 * Interface for {@link EFeature} status.
 * 
 * @author kengu, 22. apr. 2011
 * 
 */
public interface EFeatureStatus {
    public static final int SUCCESS = 0;

    public static final int WARNING = 2;

    public static final int FAILURE = 255;

    /**
     * Get status type
     */
    public int getType();

    /**
     * Get status message
     */
    public String getMessage();

    /**
     * Get {@link Throwable thowable} cause
     */
    public Throwable getCause();

    /**
     * Get {@link Throwable#getStackTrace() stack trace}
     */
    public StackTraceElement[] getStackTrace();
    
    /**
     * Get status source
     */
    public Object getSource();

    /**
     * Check if status is given type
     */
    public boolean isType(int type);

    /**
     * Check if status is {@link #SUCCESS}
     */
    public boolean isSuccess();

    /**
     * Check if status is {@link #WARNING}
     */
    public boolean isWarning();

    /**
     * Check if status is {@link #FAILURE}
     */
    public boolean isFailure();

    /**
     * Create new status instance with given message.
     * 
     * @param message - new message
     * @return a new {@link EFeatureStatus} instance
     */
    public EFeatureStatus clone(String message);
    
    /**
     * Create new status instance with given message.
     * 
     * @param message - new message
     * @param cause - (optional) {@link Throwable thowable} or 
     * {@link Throwable#getStackTrace() stack trace} cause
     * @return a new {@link EFeatureStatus} instance
     * @throws IllegalArgumentException If cause is not <code>null</code>,
     * and not a {@link Throwable} or {@link StackTraceElement} array. 
     */
    public EFeatureStatus clone(String message, Object cause);    

}
