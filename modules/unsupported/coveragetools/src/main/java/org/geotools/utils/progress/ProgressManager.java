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
package org.geotools.utils.progress;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
/**
 * 
 * @author Simone Giannecchini, GeoSolutions.
 *
 *
 * @source $URL$
 */
public abstract class ProgressManager {

	/**
	 * Private Class which simply fires the events using a copy of the listeners
	 * list in order to avoid problems with listeners that remove themselves or
	 * are removed by someone else
	 */
	protected final static class ProgressEventDispatchThreadEventLauncher
			implements Runnable {

		/**
		 * The event we want to fire away.
		 */
		private ProcessingEvent event;

		/**
		 * The list of listeners.
		 */
		private Object[] listeners;

		/**
		 * Default constructor.
		 * 
		 */
		ProgressEventDispatchThreadEventLauncher() {
		}

		/**
		 * Used to send an event to an array of listeners.
		 * 
		 * @param evt
		 *            is the {@link ProcessingEvent} to send.
		 * @param listeners
		 *            is the array of {@link ProcessingEventListener}s to
		 *            notify.
		 */
		synchronized void setEvent(final ProcessingEvent evt,
				final Object[] listeners) {
			if (listeners == null || evt == null)
				throw new NullPointerException("Input argumentBuilder cannot be null");
			this.listeners = listeners;
			this.event = evt;

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			final int numListeners = listeners.length;
			if (event instanceof ExceptionEvent)
				for (int i = 0; i < numListeners; i++)
					((ProcessingEventListener) listeners[i])
							.exceptionOccurred((ExceptionEvent) this.event);
			else
				for (int i = 0; i < numListeners; i++)
					((ProcessingEventListener) listeners[i])
							.getNotification(this.event);
		}

	}

	/**
	 * Set this to false for command line UIs where the delayed event sending
	 * may prevent some messages to be seen before the tool exits, to true for
	 * real GUI where you don't want the processing to be blocked too long, or
	 * when you have slow listeners in general.
	 */
	private boolean sendDelayedMessages = false;

	/**
	 * Proper way to stop a thread is not by calling Thread.stop() but by using
	 * a shared variable that can be checked in order to notify a terminating
	 * condition.
	 */
	private volatile boolean stopThread = false;

	/**
	 * List containing all the objects that want to be notified during
	 * processing.
	 */
	private List<ProcessingEventListener> notificationListeners = new ArrayList<ProcessingEventListener>();

	public ProgressManager(boolean sendDelayedMessages) {
		this.sendDelayedMessages = sendDelayedMessages;
	}

	/**
	 * Default constructor.
	 * 
	 */
	public ProgressManager() {

	}

	/**
	 * Adding a listener to the {@link ProcessingEventListener}s' list.
	 * 
	 * @param listener
	 *            to add to the list of listeners.
	 */
	public final synchronized void addProcessingEventListener(
			final ProcessingEventListener listener) {
		notificationListeners.add(listener);
	}

	/**
	 * Removing a {@link ProcessingEventListener} from the listeners' list.
	 * 
	 * @param listener
	 *            {@link ProcessingEventListener} to remove from the list of
	 *            listeners.
	 */
	public final synchronized void removeProcessingEventListener(
			final ProcessingEventListener listener) {
		notificationListeners.remove(listener);
	}

	/**
	 * Removing all the listeners.
	 * 
	 */
	public final synchronized void removeAllProcessingEventListeners() {
		notificationListeners.clear();

	}

	/**
	 * Firing an event to listeners in order to inform them about what we are
	 * doing and about the percentage of work already carried out.
	 * 
	 * @param string
	 *            The message to show.
	 * @param percentage
	 *            The percentage for the process.
	 */
	public synchronized void fireEvent(final String string,
			final double percentage) {
		final String newLine = System.getProperty("line.separator");
		final StringBuffer message = new StringBuffer("Thread Name ");
		message.append(Thread.currentThread().getName()).append(newLine);
		message.append(this.getClass().toString()).append(newLine).append(
				string);
		final ProcessingEvent evt = new ProcessingEvent(this, string,
				percentage);
		ProgressEventDispatchThreadEventLauncher eventLauncher = new ProgressEventDispatchThreadEventLauncher();
		eventLauncher.setEvent(evt, this.notificationListeners.toArray());
		sendEvent(eventLauncher);
	}

	private void sendEvent(
			ProgressEventDispatchThreadEventLauncher eventLauncher) {
		if (sendDelayedMessages)
			SwingUtilities.invokeLater(eventLauncher);
		else
			eventLauncher.run();
	}

	/**
	 * Firing an exception event to listeners in order to inform them that
	 * processing broke and we can no longer proceed
	 * 
	 * @param string
	 *            The message to show.
	 * @param percentage
	 *            The percentage for the process.
	 * @param ex
	 *            the actual exception occurred
	 */
	public synchronized void fireException(final String string,
			final double percentage, Throwable ex) {
		final String newLine = System.getProperty("line.separator");
		final StringBuffer message = new StringBuffer("Thread Name ");
		message.append(Thread.currentThread().getName()).append(newLine);
		message.append(this.getClass().toString()).append(newLine).append(string);
		final ExceptionEvent evt = new ExceptionEvent(this, string, percentage,ex);
		ProgressEventDispatchThreadEventLauncher eventLauncher = new ProgressEventDispatchThreadEventLauncher();
		eventLauncher.setEvent(evt, this.notificationListeners.toArray());
		sendEvent(eventLauncher);
	}

	/**
	 * Firing an exception event to listeners in order to inform them that
	 * processing broke and we can no longer proceed. This is a convenience
	 * method, it will call {@link #fireException(String, double, Exception)}
	 * with the exception message and -1 as percentage.
	 * 
	 * @param ex
	 *            the actual exception occurred
	 */
	public synchronized void fireException(Throwable ex) {
		fireException(ExceptionEvent.getMessageFromException(ex), -1, ex);
	}

	/**
	 * Should this thread be stopped?
	 * 
	 */
	public final boolean getStopThread() {
		return stopThread;
	}

	/**
	 * Stop this thread.
	 * 
	 * @param stop
	 */
	public final void stopThread() {
		stopThread = true;
	}

	/**
	 * Perform proper clean up.
	 * 
	 */
	public synchronized void dispose() {
		removeAllProcessingEventListeners();
	}

	/**
	 * This method is responsible for doing the actual processing.
	 * 
	 */
	public abstract void run() throws Throwable;

}
