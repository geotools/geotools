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

import java.util.List;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.processing.CoverageProcessingException;
import org.geotools.factory.Hints;
import org.opengis.coverage.Coverage;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.util.InternationalString;

/**
 * Basic interface for Coverage Processing as used by SLD 1.0. The immediate
 * goal here is to implement RasterSymbolizer support for SLD 1.0 but in future
 * it could be extended/used for an elaborate coverage processing framework.
 * 
 * <p>
 * This interface can be used to chain together {@link CoverageProcessingNode}s
 * in order to create graphs of operations. A single node can have multiple
 * sources but only one output in this design.
 * 
 * @author Simone Giannecchini, GeoSolutions.
 * 
 *
 *
 * @source $URL$
 */
public interface CoverageProcessingNode {

	/***************************************************************************
	 * 
	 * 
	 * Output Management
	 * 
	 * 
	 **************************************************************************/

	/**
	 * Forces this node to create the output coverage for the operation
	 * represented by this node.
	 * 
	 * @return the {@link Coverage} which represents the output for this
	 *         {@link CoverageProcessingNode}.
	 */
	public GridCoverage getOutput() throws CoverageProcessingException;

	/***************************************************************************
	 * 
	 * 
	 * Sources Management
	 * 
	 * 
	 **************************************************************************/

	/**
	 * Retrieves the {@link List} of source for this
	 * {@link CoverageProcessingNode}. Each source <strong>MUST</strong> be a
	 * {@link CoverageProcessingNode}.
	 * 
	 * @return a {@link List} of {@link CoverageProcessingNode} which represents
	 *         the sources for this {@link CoverageProcessingNode}.
	 */
	public List<CoverageProcessingNode> getSources();

	/**
	 * Retrieves the source located at index <code>index</code> in the
	 * {@link List} of sources for this {@link CoverageProcessingNode}.
	 * 
	 * @param index
	 *            is the zero-based index for the sink we want to get.
	 * @return the {@link CoverageProcessingNode} which represents the sink at
	 *         index <code>index</code> for this
	 *         {@link CoverageProcessingNode}.
	 */
	public CoverageProcessingNode getSource(final int index)
			throws IndexOutOfBoundsException;

	/**
	 * Adds a source {@link CoverageProcessingNode} to the list of sources for
	 * this {@link CoverageProcessingNode}.
	 * 
	 * @param source
	 *            the {@link CoverageProcessingNode} to add to the {@link List}
	 *            of sources for this {@link CoverageProcessingNode}.
	 * @return <code>true</code> if everything goes fine, <code>false</code>
	 *         otherwise.
	 */
	public boolean addSource(final CoverageProcessingNode source);

	/**
	 * Removes a source {@link CoverageProcessingNode} to the list of sources
	 * for this {@link CoverageProcessingNode}.
	 * 
	 * @param index
	 *            the index at which we want to remove a source from the
	 *            {@link List} of sources for this
	 *            {@link CoverageProcessingNode}.
	 * @return the {@link CoverageProcessingNode} we actually remove from the
	 *         sources list.
	 */
	public CoverageProcessingNode removeSource(final int index)
			throws IndexOutOfBoundsException;

	/**
	 * Removes a source {@link CoverageProcessingNode} to the list of sources
	 * for this {@link CoverageProcessingNode}.
	 * 
	 * @param sources
	 *            the {@link CoverageProcessingNode} to remove from the
	 *            {@link List} of sources for this
	 *            {@link CoverageProcessingNode}.
	 * @return <code>true</code> in case we remove something,
	 *         <code>false</code> otherwise.
	 */
	public boolean removeSource(final CoverageProcessingNode source);

	/**
	 * Retrieves the {@link List} of sinks for this
	 * {@link CoverageProcessingNode}. Each sink <strong>MUST</strong> be a
	 * {@link CoverageProcessingNode}.
	 * 
	 * @return a {@link List} of {@link CoverageProcessingNode} which represents
	 *         the sinks for this {@link CoverageProcessingNode}.
	 */
	public List <CoverageProcessingNode> getSinks();

	/**
	 * Retrieves the sink located at index <code>index</code> in the
	 * {@link List} of sinks for this {@link CoverageProcessingNode}.
	 * 
	 * @param index
	 *            is the zero-based index for the source we want to access.
	 * @return the {@link CoverageProcessingNode} which represents the source at
	 *         index <code>index</code> for this
	 *         {@link CoverageProcessingNode}.
	 */
	public CoverageProcessingNode getSink(final int index)
			throws IndexOutOfBoundsException;

	/**
	 * Adds a sink {@link CoverageProcessingNode} to the list of sinks for this
	 * {@link CoverageProcessingNode}.
	 * 
	 * @param source
	 *            the {@link CoverageProcessingNode} to add to the {@link List}
	 *            of sinks for this {@link CoverageProcessingNode}.
	 */
	public void addSink(final CoverageProcessingNode sink);

	/**
	 * Removes a sink {@link CoverageProcessingNode} from the list of sinks for
	 * this {@link CoverageProcessingNode}.
	 * 
	 * @param index
	 *            the index at which we want to remove a sink from the
	 *            {@link List} of sinks for this {@link CoverageProcessingNode}.
	 * @return the {@link CoverageProcessingNode} we actually remove from the
	 *         sinks list.
	 */
	public CoverageProcessingNode removeSink(final int index)
			throws IndexOutOfBoundsException;

	/**
	 * Removes a sink {@link CoverageProcessingNode} from the list of sinks for
	 * this {@link CoverageProcessingNode}.
	 * 
	 * @param sink
	 *            the {@link CoverageProcessingNode} to remove from the
	 *            {@link List} of sinks for this {@link CoverageProcessingNode}.
	 * @return <code>true</code> in case we remove something,
	 *         <code>false</code> otherwise.
	 */
	public boolean removeSink(final CoverageProcessingNode sink);

	/**
	 * Returns the number of sinks for this {@link CoverageProcessingNode}.
	 * 
	 * @return the number of sinks for this {@link CoverageProcessingNode}.
	 */
	public int getNumberOfSinks();

	/**
	 * Returns the number of sources for this {@link CoverageProcessingNode}.
	 * 
	 * @return the number of sources for this {@link CoverageProcessingNode}.
	 */
	public int getNumberOfSources();

	/**
	 * Disposes all the resources used by this {@link CoverageProcessingNode}.
	 * 
	 * 
	 * @param force
	 *            <code>true</code> to force disposal, <code>false</code> to
	 *            suggest disposal.
	 */
	public void dispose(boolean force);

	/**
	 * Getter for {@link Hints}.
	 * 
	 * @return {@link Hints} provided at construction time to control
	 *         {@link GridCoverageFactory} creation.
	 */
	public Hints getHints();

	/**
	 * The {@link GridCoverageFactory} we will internally use for build
	 * intermediate and output {@link GridCoverage2D}.
	 * 
	 * @return a {@link GridCoverageFactory} we will internally use for build
	 *         intermediate and output {@link GridCoverage2D}.
	 */
	public GridCoverageFactory getCoverageFactory();

	/**
	 * Retrieves the name for this {@link CoverageProcessingNode}
	 * 
	 * @return the name for this {@link CoverageProcessingNode}
	 */
	public InternationalString getName();

	/**
	 * Retrieves the description for this {@link CoverageProcessingNode}
	 * 
	 * @return the description for this {@link CoverageProcessingNode}
	 */
	public InternationalString getDescription();

	/**
	 * Provides a descriptive description for this
	 * {@link CoverageProcessingNode}.
	 * 
	 * @return a descriptive description for this {@link CoverageProcessingNode}
	 */
	public String toString();

}
