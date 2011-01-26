/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite.gridcoverage2d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.processing.CoverageProcessingException;
import org.geotools.factory.Hints;
import org.geotools.renderer.i18n.ErrorKeys;
import org.geotools.renderer.i18n.Errors;
import org.geotools.renderer.i18n.Vocabulary;
import org.geotools.renderer.i18n.VocabularyKeys;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.util.InternationalString;

/**
 * Base implementation of a      {@link CoverageProcessingNode}      . <p> This implementation provides convenient methods for managing sinks and source for a      {@link CoverageProcessingNode}      . The      {@link #getOutput()}      is used to get the output of this      {@link CoverageProcessingNode}      (a     {@link CoverageProcessingException}      is thrown in case something bad happens while processing <p> Implementors must implement the abstract method      {@link #execute()}      which is guaranteed to run in a critical section where the sources and sinks for this     {@link CoverageProcessingNode}      will not be touched.
 * @author      Simone Giannecchini, GeoSolutions.
 *
 * @source $URL$
 */
public abstract class BaseCoverageProcessingNode implements
		CoverageProcessingNode {

	/**
	 * Very simple class to detect cycles in the graph of
	 * {@link CoverageProcessingNode}s.
	 * 
	 * @author Simone Giannecchini, GeoSlutions.
	 * 
	 */
	private final static class CoverageProcessingCycleDetector {

		public CoverageProcessingCycleDetector() {

		}

		public boolean detectCycle(CoverageProcessingNode startPoint) {
			return check(startPoint, startPoint);

		}

		private boolean check(CoverageProcessingNode baseNode,
				CoverageProcessingNode currentNode) {
			// get the sinks for this node
			if (currentNode.getNumberOfSinks() == 0)
				return false;
			final List<CoverageProcessingNode> sinks = currentNode.getSinks();
			for (CoverageProcessingNode node:sinks) {

				// check the start point
				if (baseNode.equals(node))
					return true;

				// no cyle here, let's dig
				if (check(baseNode, node))
					return true;

			}
			return false;

		}

	}

	/**
	 * Detects cycle in out graph
	 */
	private final static CoverageProcessingCycleDetector cycleDetector = new CoverageProcessingCycleDetector();

	/**
	 * Logger for this class. 
	 */
	private final static Logger LOGGER = Logger
			.getLogger(BaseCoverageProcessingNode.class.getName());

	/**
     * {@link List}     of sources for this     {@link CoverageProcessingNode}    .
     * @uml.property  name="sources"
     */
	private final List<CoverageProcessingNode> sources = new ArrayList<CoverageProcessingNode>();

	/**
     * {@link List}     of sinks for this     {@link CoverageProcessingNode}    .
     * @uml.property  name="sinks"
     */
	private final List<CoverageProcessingNode> sinks = new ArrayList<CoverageProcessingNode>();

	/**
     * Output of this     {@link CoverageProcessingNode}
     * @uml.property  name="output"
     */
	private GridCoverage2D output;

	/**
     * Tells me if we have executed this node or not.
     * @uml.property  name="executed"
     */
	private boolean executed = false;

	/**
     * Maximum number of allowed sources.
     * @uml.property  name="maximumNumberOfSources"
     */
	private int maximumNumberOfSources = -1;

	/**
     * User supplied     {@link Hints}     to control     {@link GridCoverageFactory}    creation.
     * @uml.property  name="hints"
     */
	private Hints hints;

	/**
     * {@link GridCoverageFactory}     to build     {@link GridCoverage2D}     objects as output of this operation.
     * @uml.property  name="coverageFactory"
     */
	final GridCoverageFactory coverageFactory;

	/**
	 * Store the error raised during execution, in case there was one.
	 */
	private Throwable error;

	/**
     * Tells me if this coverage has been already disposed or not.
     * @uml.property  name="disposed"
     */
	private boolean disposed;

	/**
     * Internationalized name for this     {@link CoverageProcessingNode}    .
     * @uml.property  name="name"
     */
	private InternationalString name = Vocabulary.formatInternational(VocabularyKeys.BASE_COVERAGE_PROCESSING);

	/**
     * Internationalized description for this     {@link CoverageProcessingNode}    .
     * @uml.property  name="description"
     */
	private InternationalString description;

	/**
	 * Default constructor
	 */
	public BaseCoverageProcessingNode(InternationalString name,
			InternationalString description) {
		this(-1, name, description);
	}

	/**
	 * Default constructor that gives users the possibility
	 * 
	 * @param maxSources
	 *            maximum number of sources allowed for this node.
	 * @param description
	 * @param name
	 */
	public BaseCoverageProcessingNode(int maxSources, InternationalString name,
			InternationalString description) {
		this(maxSources, null, name, description);
	}

	/**
	 * Default constructor that gives users the possibility
	 * 
	 * @param maxSources
	 *            maximum number of sources allowed for this node.
	 * @param hints
	 *            instance of {@link Hints} class to control creation of
	 *            internal factories. It can be <code>null</code>.
	 * @param description
	 * @param name
	 */
	public BaseCoverageProcessingNode(int maxSources, Hints hints,
			InternationalString name, InternationalString description) {
		ensureNotNull(name, "CoverageProcessingNode name ");
		ensureNotNull(description, "CoverageProcessingNode descripion ");
		maximumNumberOfSources = maxSources;
		this.hints = hints != null ? (Hints) hints.clone() : null;
		this.coverageFactory = CoverageFactoryFinder.getGridCoverageFactory(hints);
		this.name = name;
		this.description = description;
	}

	/**
	 * Checks whether or not we need to execute this node.
	 * 
	 * <p>
	 * This method records all the possible error conditions that may arise
	 * during execution.
	 */
	private void checkExecuted() {
		// precondition
		assert Thread.holdsLock(this);
		// /////////////////////////////////////////////////////////////////////
		//
		// Is this node disposed? If so throw an exception
		//
		// /////////////////////////////////////////////////////////////////////
		if (disposed) {
			error = new CoverageProcessingException(
					"Trying to process a disposed CoverageProcessingNode.");
			return;
		}

		// /////////////////////////////////////////////////////////////////////
		//
		// We cannot execute the same node twice in this simpe design
		//
		// /////////////////////////////////////////////////////////////////////
		if (!executed) {
			try {
			        output=null;
				// executes this node
			        final GridCoverage result = execute();
				if (result == null)
					error = new CoverageProcessingException("Something bad occurred while trying to execute this node.");
				if (!(result instanceof GridCoverage2D))
				    error = new CoverageProcessingException("Something bad occurred while trying to execute this node.");
				if(error==null)
				    output=(GridCoverage2D) result;
			} catch (Throwable t) {
				// something bad happened
				output = null;
				error = t;
			}

			// we have executed, let's register that
			executed = true;
		}

		// postconditions
		assert executed;
	}

	/**
         * Subclasses MUST override this method in order to do the actual
         * processing.
         * 
         * <p>
         * Note that this method is invoked through this framework hence it is
         * run within a critical section. Be careful with what you do within
         * this method since it is essentially an "alien" method running within
         * a synch section, hence all sort of bad things can happen.
         * 
         * @return a {@link GridCoverage2D} which is the result of the
         *         processing.
         */
	protected abstract GridCoverage execute();

	/**
	 * Disposes this {@link CoverageProcessingNode} along with all the resources
	 * it might have allocated
	 * 
	 * <p>
	 * The result for this {@link CoverageProcessingNode} is also disposed.
	 * 
	 * @param force
	 *            force the disposition of this node.
	 */
	public synchronized void dispose(boolean force) {

		// /////////////////////////////////////////////////////////////////////
		//
		// Do we need to dispose this node?
		//
		// /////////////////////////////////////////////////////////////////////
		if (disposed)
			return;

		// /////////////////////////////////////////////////////////////////////
		//
		// Cleaning the output we have generated
		//
		// /////////////////////////////////////////////////////////////////////
		if (output != null)
			output.dispose(force);

		// /////////////////////////////////////////////////////////////////////
		//
		// Removing myself as a sink for my sources
		//
		// /////////////////////////////////////////////////////////////////////
		final Iterator<CoverageProcessingNode>it = sources.iterator();
		while (it.hasNext()) {
			final CoverageProcessingNode source = it.next();
			source.removeSink(this);
		}
		sources.clear();

		// /////////////////////////////////////////////////////////////////////
		//
		// Cleaning sinks
		//
		// /////////////////////////////////////////////////////////////////////
		sinks.clear();

		// /////////////////////////////////////////////////////////////////////
		//
		// Done
		//
		// /////////////////////////////////////////////////////////////////////
		disposed = true;


		if(LOGGER.isLoggable(Level.FINE))
			LOGGER.fine("disposed node \n" + this.toString());

	}

	/**
     * This method is responsible for triggering the execution of this {@link CoverageProcessingNode}  and also of all its sources. <p> In case something bad happens a  {@link CoverageProcessingException}  is thrown.
     * @uml.property  name="output"
     */
	public synchronized GridCoverage2D getOutput()
			throws CoverageProcessingException {
		checkExecuted();
		if (error != null)
			throw new CoverageProcessingException(error);
		return output;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode#addSink(org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode)
	 */
	public synchronized void addSink(CoverageProcessingNode sink) {
		ensureNotNull(sink, "CoverageProcessingNode");
		sinks.add(sink);
		detectCycle();

	}

	/**
	 * Performs proper clean up on this {@link CoverageProcessingNode}.
	 */
	private void cleanOutput() {
		assert Thread.holdsLock(this);
		if (executed) {
			executed = false;
			output.dispose(true);
			error = null;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode#addSource(org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode)
	 */
	public synchronized boolean addSource(CoverageProcessingNode source) {
		ensureNotNull(source, "CoverageProcessingNode");
		checkNumSources(1);
		if (this.sources.add(source)) {
			cleanOutput();
			detectCycle();
			return true;
		}
		return false;

	}

	private void detectCycle() throws IllegalStateException {
		if (cycleDetector.detectCycle(this))
			throw new IllegalStateException(Errors
					.format(ErrorKeys.CYCLE_DETECTED));

	}

	private void checkNumSources(final int sourcesToAdd) {
		if (maximumNumberOfSources != -1)
			if (this.sources.size() + sourcesToAdd > maximumNumberOfSources)
				throw new IllegalStateException(Errors.format(
						ErrorKeys.TOO_MANY_SOURCES_$1, Integer.valueOf(
								maximumNumberOfSources)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode#getSink(int)
	 */
	public synchronized CoverageProcessingNode getSink(int index) {
		return (CoverageProcessingNode) sinks.get(index);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode#getSinks()
	 */
	/**
     * @return
     * @uml.property  name="sinks"
     */
	public synchronized List<CoverageProcessingNode> getSinks() {
		return Collections.unmodifiableList(sinks);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode#getSource(int)
	 */
	public synchronized CoverageProcessingNode getSource(int index) {
		return (CoverageProcessingNode) sources.get(index);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode#getSources()
	 */
	/**
     * @return
     * @uml.property  name="sources"
     */
	public synchronized List<CoverageProcessingNode> getSources() {
		return Collections.unmodifiableList(sources);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode#removeSink(org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode)
	 */
	public synchronized boolean removeSink(CoverageProcessingNode sink) {
		ensureNotNull(sink, "CoverageProcessingNode");
		// /////////////////////////////////////////////////////////////////////
		//
		// In case we manage to remove a sink for this node we
		//
		// /////////////////////////////////////////////////////////////////////
		return this.sinks.remove(sink);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode#removeSink(int)
	 */
	public synchronized CoverageProcessingNode removeSink(int index) {
		return (CoverageProcessingNode) this.sinks.remove(index);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode#removeSource(org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode)
	 */
	public synchronized boolean removeSource(CoverageProcessingNode source) {
		ensureNotNull(source, "CoverageProcessingNode");
		final boolean success = this.sources.remove(source);
		if (success)
			cleanOutput();
		return success;

	}

	/**
     * Getter for  {@link Hints}  .
     * @return   {@link Hints}  provided at construction time to control {@link GridCoverageFactory}  creation.
     * @uml.property  name="hints"
     */
	public synchronized Hints getHints() {
		return new Hints(hints);
	}

	/**
     * retrieves the maximum number of sources we are allowed to set for this {@link CoverageProcessingNode}
     * @return  the maximum number of sources we are allowed to set for this {@link CoverageProcessingNode}
     * @uml.property  name="maximumNumberOfSources"
     */
	public int getMaximumNumberOfSources() {
		return maximumNumberOfSources;
	}

	/**
     * The  {@link GridCoverageFactory}  we will internally use for build intermediate and output  {@link GridCoverage2D}  .
     * @return  a  {@link GridCoverageFactory}  we will internally use for build  intermediate and output  {@link GridCoverage2D}  .
     * @uml.property  name="coverageFactory"
     */
	public GridCoverageFactory getCoverageFactory() {
		return coverageFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode#getNumberOfSinks()
	 */
	public synchronized int getNumberOfSinks() {
		return this.sinks.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode#getNumberOfSources()
	 */
	public synchronized int getNumberOfSources() {
		return sources.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode#getDescription()
	 */
	/**
     * @return
     * @uml.property  name="description"
     */
	public InternationalString getDescription() {
		return description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode#getName()
	 */
	/**
     * @return
     * @uml.property  name="name"
     */
	public InternationalString getName() {
		return name;
	}

	/**
	 * Checks whether the provided source object is null or not. If it is null
	 * it throws an {@link IllegalArgumentException} exception.
	 * 
	 * @param source
	 *            the object to check.
	 * @param node
	 *            the operation we are trying to run.
	 */
	protected static void ensureSourceNotNull(final Object source,
			final String name) {
		if (source == null)
			throw new IllegalArgumentException(Errors.format(
					ErrorKeys.SOURCE_CANT_BE_NULL_$1, name));

	}

	/**
	 * Checks whether the provided object is null or not. If it is null it
	 * throws an {@link IllegalArgumentException} exception.
	 * 
	 * @param source
	 *            the object to check.
	 * @param node
	 *            the operation we are trying to run.
	 */
	protected static void ensureNotNull(final Object source, final String name) {
		if (source == null)
			throw new IllegalArgumentException(Errors.format(
					ErrorKeys.NULL_ARGUMENT_$1, name));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode#removeSource(int)
	 */
	public synchronized CoverageProcessingNode removeSource(int index)
			throws IndexOutOfBoundsException {
		return (CoverageProcessingNode) sources.remove(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("Node Name:").append(this.getName().toString()).append(
				"\n");
		buffer.append("Node Description:").append(
				this.getDescription().toString()).append("\n");
		// if(executed&&error==null)
		// buffer.append("Node executed fine with
		// output:\n\t").append(this.getOutput().toString()).append("\n");
		// else
		// if(executed&&error!=null)
		// buffer.append("Node executed with
		// error:\n\t").append(this.error.getLocalizedMessage()).append("\n").append(error.getStackTrace()).append("\n");
		// else
		// if(!executed)
		// buffer.append("Node not yet executed:\n");
		return buffer.toString();
	}

	/**
     * Tells me whether or not the node has been already disposed.
     * @return   <code>true</code> if the node has been already disposed,  <code>false</code> otherwise.
     * @uml.property  name="disposed"
     */
	public synchronized boolean isDisposed() {
		return disposed;
	}
	
	/**
     * Tells me whether or not the node has been already executed.
     * @return   <code>true</code> if the node has been already executed,  <code>false</code> otherwise.
     * @uml.property  name="executed"
     */
	public synchronized boolean isExecuted() {
		return  this.executed ;
	}

}
