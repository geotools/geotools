/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.util;

import org.opengis.annotation.Extension;


/**
 * Monitor the progress of some lengthly operation, and allows cancelation.
 * This interface makes no assumption about the output device. Additionnaly, this
 * interface provides support for non-fatal warning and exception reports.
 * <p>
 * All implementations should be multi-thread safe, even the ones that provide
 * feedback to a user interface thread.
 * <p>
 * Usage example:
 * <blockquote><pre>
 * float scale = 100f / maximumCount;
 * listener.started();
 * for (int counter=0; counter&lt;maximumCount; counter++) {
 *     if (listener.isCanceled()) {
 *         break;
 *     }
 *     listener.progress(scale * counter);
 *     try {
 *         // Do some work...
 *     } catch (NonFatalException e) {
 *         listener.exceptionOccurred(e);
 *     }
 * }
 * listener.complete();
 * </pre></blockquote>
 *
 * @since  GeoAPI 2.1
 * @author Martin Desruisseaux
 * @author Jody Garnet
 */
@Extension
public interface ProgressListener {
    /**
     * Returns the description of the current task being performed, or {@code null} if none.
     * It is assumed that if the task is {@code null} applications may simply report that the
     * process is "in progress" or "working" as represented in the current locale.
     *
     * @return Description of the task being performed, or {@code null} if none.
     */
    InternationalString getTask();

    /**
     * Description for the lengthly operation to be reported, or {@code null} if none.
     *
     * @return The task description.
     *
     * @deprecated Replaced by getTask().toString()
     */
    @Deprecated
    String getDescription();

    /**
     * Sets the description of the current task being performed. This method is usually invoked
     * before any progress begins. However, it is legal to invoke this method at any time during
     * the operation, in which case the description display is updated without any change to the
     * percentage accomplished.
     *
     * @param task Description of the task being performed, or {@code null} if none.
     *
     * @todo Replace the argument type by {@link CharSequence} so the user can specify
     *       an {@link InternationalString} or a {@link String} at his choice.
     */
    void setTask(InternationalString task);

    /**
     * Sets the description for the lenghtly operation to be reported. This method is usually
     * invoked before any progress begins. However, it is legal to invoke this method at any
     * time during the operation, in which case the description display is updated without
     * any change to the percentage accomplished.
     *
     * @param description The new description, or {@code null} if none.
     *
     * @deprecated Replaced by setTask
     */
    @Deprecated
    void setDescription(String description);

    /**
     * Notifies this listener that the operation begins.
     */
    void started();

    /**
     * Notifies this listener of progress in the lengthly operation. Progress are reported
     * as a value between 0 and 100 inclusive. Values out of bounds will be clamped.
     *
     * @param percent The progress as a value between 0 and 100 inclusive.
     *
     * @todo Should be renamed setProgress(float) for consistency with getProgress().
     */
    void progress(float percent);

    /**
     * Returns the current progress as a percent completed.
     *
     * @return Percent completed between 0 and 100 inclusive.
     *
     * @since GeoAPI 2.2
     */
    float getProgress();

    /**
     * Notifies this listener that the operation has finished. The progress indicator will
     * shows 100% or disappears, at implementor choice. If warning messages were pending,
     * they will be displayed now.
     */
    void complete();

    /**
     * Releases any resources used by this listener. If the progress were reported in a window,
     * this window may be disposed.
     */
    void dispose();

    /**
     * Returns {@code true} if this job is cancelled.
     *
     * @return {@code true} if this job is cancelled.
     */
    boolean isCanceled();

    /**
     * Indicates that task should be cancelled.
     *
     * @param cancel {@code true} for cancelling the task.
     *
     * @todo Should be a {@code cancel()} method without arguments.
     */
    void setCanceled(boolean cancel);

    /**
     * Reports a warning. This warning may be {@linkplain java.util.logger.Logger logged}, printed
     * to the {@linkplain System#err standard error stream}, appears in a windows or be ignored,
     * at implementor choice.
     *
     * @param source
     *          Name of the warning source, or {@code null} if none. This is typically the
     *          filename in process of being parsed or the URL of the data being processed
     * @param location
     *          Text to write on the left side of the warning message, or {@code null} if none.
     *          This is typically the line number where the error occured in the {@code source}
     *          file or the feature ID of the feature that produced the message
     * @param warning
     *          The warning message.
     */
    void warningOccurred(String source, String location, String warning);

    /**
     * Reports an exception. This method may prints the stack trace to the {@linkplain System#err
     * standard error stream} or display it in a dialog box, at implementor choice.
     *
     * @param exception The exception to report.
     */
    void exceptionOccurred(Throwable exception);
}
