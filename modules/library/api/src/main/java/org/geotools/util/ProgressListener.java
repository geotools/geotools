/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 1999-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.util;


/**
 * Monitor the progress of some lengthly operation. This interface makes no
 * assumption about the output device. It may be the standard output stream
 * (see {@link org.geotools.gui.headless.ProgressPrinter} implementation),
 * a window ({@link org.geotools.gui.swing.ProgressWindow}) or mails automatically
 * sent to some address ({@link org.geotools.gui.headless.ProgressMailer}).
 * Additionnaly, this interface provides support for non-fatal warning and
 * exception reports.
 * <p>
 * All {@code ProgressListener} implementations are multi-thread safe,  even the
 * <cite>Swing</cite> implemention. {@code ProgressListener} can be invoked from
 * any thread, which never need to be the <cite>Swing</cite>'s thread. This is usefull
 * for performing lenghtly operation in a background thread. Example:
 *
 * <blockquote><pre>
 * &nbsp;ProgressListener p = new {@link org.geotools.gui.headless.ProgressPrinter}();
 * &nbsp;p.setDecription("Loading data");
 * &nbsp;p.start();
 * &nbsp;for (int j=0; j&lt;1000; j++) {
 * &nbsp;    // ... some process...
 * &nbsp;    if ((j &amp; 255) == 0)
 * &nbsp;        p.progress(j/10f);
 * &nbsp;}
 * &nbsp;p.complete();
 * </pre></blockquote>
 *
 * <strong>Note:</strong> The line <code>if ((j&nbsp;&amp;&nbsp;255)&nbsp;==&nbsp;0)</code>
 * is used for reducing the amount of calls to {@link #progress} (only once every 256 steps).
 * This is not mandatory, but may speed up the process.
 *
 * <p>
 * Here is another example showing how to cancel:
 * <pre><code>
 *      Iterator iterator = null;
 *      try{
 *          float size = size();
 *          float position = 0;
 *          progress.started();
 *          for( iterator = iterator(); !progress.isCanceled() && iterator.hasNext(); progress.progress( (position++)/size )){
 *              try {
 *                  Feature feature = (Feature) iterator.next();
 *                  visitor.visit(feature);
 *              }
 *              catch( Exception erp ){
 *                  progress.exceptionOccurred( erp );
 *              }
 *          }
 *          progress.complete();
 *      }
 *      finally {
 *          close( iterator );
 *      }
 * </code></pre>
 * Note the use of try and catch to report exceptions.
 *
 * @since 2.0
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (PMO, IRD)
 *
 * @see org.geotools.gui.headless.ProgressPrinter
 * @see org.geotools.gui.headless.ProgressMailer
 * @see org.geotools.gui.swing.ProgressWindow
 * @see javax.swing.ProgressMonitor
 *
 * @deprecated Please use org.opengis.util.ProgressListener
 */
public interface ProgressListener extends org.opengis.util.ProgressListener {
    /**
     * Returns the description for the lengthly operation to be reported, or {@code null} if none.
     */
    String getDescription();

    /**
     * Set the description for the lenghtly operation to be reported. This method is usually
     * invoked before any progress begins. However, it is legal to invoke this method at any
     * time during the operation, in which case the description display is updated without
     * any change to the percentage accomplished.
     *
     * @param description The new description, or {@code null} if none.
     */
    void setDescription(String description);

    /**
     * Notifies this listener that the operation begins.
     */
    void started();

    /**
     * Notifies this listener of progress in the lengthly operation. Progress are reported
     * as a value between 0 and 100 inclusive. Values out of bounds will be clamped.
     */
    void progress(float percent);

    /**
     * Notifies this listener that the operation has finished. The progress indicator will
     * shows 100% or disaspears, at implementor choice. If warning messages were pending,
     * they will be displayed now.
     */
    void complete();

    /**
     * Release any resources used by this listener. If the progress were reported in a window,
     * this window may be disposed.
     */
    void dispose();

    /**
     * Is this job canceled?
     */
    boolean isCanceled();

    /**
     * Indicate that progress should is canceled.
     */
    void setCanceled(boolean cancel);

    /**
     * Reports a warning. This warning may be printed to the {@linkplain System#err standard error
     * stream}, appears in a windows or be ignored, at implementor choice.
     *
     * @param source The source of the warning, or {@code null} if none. This is typically the
     *        filename in process of being parsed.
     * @param margin Text to write on the left side of the warning message, or {@code null} if none.
     *        This is typically the line number where the error occured in the {@code source} file.
     * @param warning The warning message.
     */
    void warningOccurred(String source, String margin, String warning);

    /**
     * Reports an exception. This method may prints the stack trace to the {@linkplain System#err
     * standard error stream} or display it in a dialog box, at implementor choice.
     */
    void exceptionOccurred(Throwable exception);
}
