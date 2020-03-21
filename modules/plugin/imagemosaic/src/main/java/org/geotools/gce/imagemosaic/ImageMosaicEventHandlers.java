/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2016, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
package org.geotools.gce.imagemosaic;

import java.io.File;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 * Base class to handle events
 *
 * @author carlo cancellieri - GeoSolutions SAS
 */
public class ImageMosaicEventHandlers {

    /** Default Logger * */
    static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(ImageMosaicEventHandlers.class);

    /** List containing all the objects that want to be notified during processing. */
    protected List<ProcessingEventListener> notificationListeners =
            new CopyOnWriteArrayList<ProcessingEventListener>();

    /**
     * Set this to false for command line UIs where the delayed event sending may prevent some
     * messages to be seen before the tool exits, to true for real GUI where you don't want the
     * processing to be blocked too long, or when you have slow listeners in general.
     */
    protected boolean sendDelayedMessages = false;

    public abstract static class ProcessingEventListener implements EventListener {
        public abstract void getNotification(final ProcessingEvent event);

        public abstract void exceptionOccurred(final ExceptionEvent event);
    }

    /** @author Simone Giannecchini, GeoSolutions. */
    public static class ProcessingEvent extends EventObject {

        private static final long serialVersionUID = 6930580659705360225L;

        private String message = null;

        private double percentage = 0;

        /** @param source */
        public ProcessingEvent(final Object source, final String message, final double percentage) {
            super(source);
            this.message = message;
            this.percentage = percentage;
        }

        public double getPercentage() {
            return percentage;
        }

        public String getMessage() {
            return message;
        }
    }

    /** A special ProcessingEvent raised when a file has completed/failed ingestion */
    public static class FileProcessingEvent extends ProcessingEvent {
        private File file;

        private boolean ingested;

        /** @param source */
        public FileProcessingEvent(
                final Object source,
                final File file,
                final boolean ingested,
                final String message,
                final double percentage) {
            super(source, message, percentage);
            this.file = file;
            this.ingested = ingested;
        }

        public File getFile() {
            return file;
        }

        public boolean isIngested() {
            return ingested;
        }
    }

    /**
     * Event launched when an exception occurs. Percentage and message may be missing, in this case
     * they will be -1 and the exception message (localized if available, standard otherwise)
     *
     * @author aaime, TOPP.
     */
    public static final class ExceptionEvent extends ProcessingEvent {

        private static final long serialVersionUID = 2272452028229922551L;

        private Exception exception;

        public ExceptionEvent(
                Object source, String message, double percentage, Exception exception) {
            super(source, message, percentage);
            this.exception = exception;
        }

        public ExceptionEvent(Object source, Exception exception) {
            super(source, Utils.getMessageFromException(exception), -1);
            this.exception = exception;
        }

        public Exception getException() {
            return exception;
        }
    }

    /**
     * Private Class which simply fires the events using a copy of the listeners list in order to
     * avoid problems with listeners that remove themselves or are removed by someone else
     */
    protected static final class ProgressEventDispatchThreadEventLauncher implements Runnable {

        /** The event we want to fire away. */
        private ProcessingEvent event;

        /** The list of listeners. */
        private Object[] listeners;

        /** Default constructor. */
        ProgressEventDispatchThreadEventLauncher() {}

        /**
         * Used to send an event to an array of listeners.
         *
         * @param evt is the {@link ProcessingEvent} to send.
         * @param listeners is the array of {@link ProcessingEventListener}s to notify.
         */
        synchronized void setEvent(final ProcessingEvent evt, final Object[] listeners) {
            if (listeners == null || evt == null)
                throw new NullPointerException("Input argumentBuilder cannot be null");
            this.listeners = listeners;
            this.event = evt;
        }

        /** Run the event launcher */
        public void run() {
            final int numListeners = listeners.length;
            if (event instanceof ExceptionEvent)
                for (int i = 0; i < numListeners; i++)
                    ((ProcessingEventListener) listeners[i])
                            .exceptionOccurred((ExceptionEvent) this.event);
            else
                for (int i = 0; i < numListeners; i++)
                    ((ProcessingEventListener) listeners[i]).getNotification(this.event);
        }
    }

    public ImageMosaicEventHandlers() {
        super();
    }

    /**
     * Adding a listener to the {@link ProcessingEventListener}s' list.
     *
     * @param listener to add to the list of listeners.
     */
    public final void addProcessingEventListener(final ProcessingEventListener listener) {
        notificationListeners.add(listener);
    }

    /**
     * Firing an event to listeners in order to inform them about what we are doing and about the
     * percentage of work already carried out.
     *
     * @param inMessage The message to show.
     * @param percentage The percentage for the process.
     */
    protected void fireEvent(Level level, final String inMessage, final double percentage) {
        if (LOGGER.isLoggable(level)) {
            LOGGER.log(level, inMessage);
        }
        synchronized (notificationListeners) {
            final String newLine = System.getProperty("line.separator");
            final StringBuilder message = new StringBuilder("Thread Name ");
            message.append(Thread.currentThread().getName()).append(newLine);
            message.append(this.getClass().toString()).append(newLine).append(inMessage);
            final ProcessingEvent evt = new ProcessingEvent(this, message.toString(), percentage);
            ProgressEventDispatchThreadEventLauncher eventLauncher =
                    new ProgressEventDispatchThreadEventLauncher();
            eventLauncher.setEvent(evt, this.notificationListeners.toArray());
            sendEvent(eventLauncher);
        }
    }

    /**
     * Firing an event to listeners in order to inform them about what we are doing and about the
     * percentage of work already carried out.
     *
     * @param inMessage The message to show.
     * @param percentage The percentage for the process.
     */
    protected void fireFileEvent(
            Level level,
            final File file,
            final boolean ingested,
            final String inMessage,
            final double percentage) {
        if (LOGGER.isLoggable(level)) {
            LOGGER.log(level, inMessage);
        }
        synchronized (notificationListeners) {
            final String newLine = System.getProperty("line.separator");
            final StringBuilder message = new StringBuilder("Thread Name ");
            message.append(Thread.currentThread().getName()).append(newLine);
            message.append(this.getClass().toString()).append(newLine).append(inMessage);
            final FileProcessingEvent evt =
                    new FileProcessingEvent(this, file, ingested, message.toString(), percentage);
            ProgressEventDispatchThreadEventLauncher eventLauncher =
                    new ProgressEventDispatchThreadEventLauncher();
            eventLauncher.setEvent(evt, this.notificationListeners.toArray());
            sendEvent(eventLauncher);
        }
    }

    /**
     * Firing an exception event to listeners in order to inform them that processing broke and we
     * can no longer proceed. This is a convenience method, it will call {@link
     * #fireException(String, double, Exception)} with the exception message and -1 as percentage.
     *
     * @param ex the actual exception occurred
     */
    protected void fireException(Exception ex) {
        synchronized (notificationListeners) {
            fireException(Utils.getMessageFromException(ex), -1, ex);
        }
    }

    /**
     * Firing an exception event to listeners in order to inform them that processing broke and we
     * can no longer proceed
     *
     * @param string The message to show.
     * @param percentage The percentage for the process.
     * @param ex the actual exception occurred
     */
    private void fireException(final String string, final double percentage, Exception ex) {
        synchronized (notificationListeners) {
            final String newLine = System.getProperty("line.separator");
            final StringBuilder message = new StringBuilder("Thread Name ");
            message.append(Thread.currentThread().getName()).append(newLine);
            message.append(this.getClass().toString()).append(newLine).append(string);
            final ExceptionEvent evt = new ExceptionEvent(this, string, percentage, ex);
            ProgressEventDispatchThreadEventLauncher eventLauncher =
                    new ProgressEventDispatchThreadEventLauncher();
            eventLauncher.setEvent(evt, this.notificationListeners.toArray());
            sendEvent(eventLauncher);
        }
    }

    public boolean isSendDelayedMessages() {
        return sendDelayedMessages;
    }

    public void setSendDelayedMessages(boolean sendDelayedMessages) {
        this.sendDelayedMessages = sendDelayedMessages;
    }

    /** Removing all the listeners. */
    public void removeAllProcessingEventListeners() {
        synchronized (notificationListeners) {
            notificationListeners.clear();
        }
    }

    /**
     * Removing a {@link ProcessingEventListener} from the listeners' list.
     *
     * @param listener {@link ProcessingEventListener} to remove from the list of listeners.
     */
    public void removeProcessingEventListener(final ProcessingEventListener listener) {
        notificationListeners.remove(listener);
    }

    private void sendEvent(ProgressEventDispatchThreadEventLauncher eventLauncher) {
        if (sendDelayedMessages) SwingUtilities.invokeLater(eventLauncher);
        else eventLauncher.run();
    }
}
