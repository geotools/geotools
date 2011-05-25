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

import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.factory.Hints;
import org.geotools.renderer.i18n.ErrorKeys;
import org.geotools.renderer.i18n.Errors;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Style;
import org.geotools.styling.StyleVisitor;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.util.InternationalString;

/**
 * This class implements an adapter to allow a      {@link CoverageProcessingNode}      to feed itself by visiting an SLD      {@link Style}      . <p> This class can be used to tie together      {@link CoverageProcessingNode}      s built from a chains as specified by the      {@link RasterSymbolizer}      SLd element.
 * @author      Simone Giannecchini, GeoSolutions
 *
 *
 * @source $URL$
 */
public abstract class StyleVisitorCoverageProcessingNodeAdapter extends
		StyleVisitorAdapter implements StyleVisitor, CoverageProcessingNode {


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

	/**
     * Allows subclasses to access the  {@link CoverageProcessingNode}  we are adapting.
     * @return  the adaptee
     * @uml.property  name="adaptee"
     */
	protected CoverageProcessingNode getAdaptee() {
		return adaptee;
	}

	/**
	 * Instance of {@link CoverageProcessingNode} that we are adapting.
	 */
	private final CoverageProcessingNode adaptee;

	/**
	 * 
	 * @param adaptee
	 */
	public StyleVisitorCoverageProcessingNodeAdapter(
			CoverageProcessingNode adaptee) {
		ensureNotNull(adaptee, "CoverageProcessingNode");
		this.adaptee = adaptee;
	}

	/**
	 * Default constructor for {@link StyleVisitorCoverageProcessingNodeAdapter}
	 */
	public StyleVisitorCoverageProcessingNodeAdapter(InternationalString name,
			InternationalString description) {
		this(-1, name, description);
	}

	/**
	 * Default constructor that gives users the possibility
	 * 
	 * @param maxSources
	 *            maximum number of sources allowed for this node.
	 */
	public StyleVisitorCoverageProcessingNodeAdapter(int maxSources,
			InternationalString name, InternationalString description) {
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
	 */
	public StyleVisitorCoverageProcessingNodeAdapter(int maxSources,
			Hints hints, InternationalString name,
			InternationalString description) {
		adaptee = new BaseCoverageProcessingNode(maxSources,
				hints != null ? (Hints) hints.clone() : null, name, description) {

			protected GridCoverage execute() {
				synchronized (StyleVisitorCoverageProcessingNodeAdapter.this) {
					return StyleVisitorCoverageProcessingNodeAdapter.this
							.execute();
				}

			}
		};
	}

	/**
	 * 
	 * @see BaseCoverageProcessingNode#execute()
	 */
	protected abstract GridCoverage execute();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode#addSink(org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode)
	 */
	public void addSink(CoverageProcessingNode sink) {
		adaptee.addSink(sink);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode#addSource(org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode)
	 */
	public boolean addSource(CoverageProcessingNode source) {
		return adaptee.addSource(source);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode#getOutput()
	 */
	public GridCoverage getOutput() {
		return adaptee.getOutput();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode#getSink(int)
	 */
	public CoverageProcessingNode getSink(int index) {
		return adaptee.getSink(index);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode#getSinks()
	 */
	public List <CoverageProcessingNode> getSinks() {
		return adaptee.getSinks();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode#getSource(int)
	 */
	public CoverageProcessingNode getSource(int index) {
		return adaptee.getSource(index);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode#getSources()
	 */
	public List<CoverageProcessingNode>getSources() {
		return adaptee.getSources();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode#removeSink(org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode)
	 */
	public boolean removeSink(CoverageProcessingNode sink) {
		return adaptee.removeSink(sink);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode#removeSink(int)
	 */
	public CoverageProcessingNode removeSink(int index) {
		return adaptee.removeSink(index);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode#removeSource(org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode)
	 */
	public boolean removeSource(CoverageProcessingNode source) {
		return adaptee.removeSource(source);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode#removeSource(int)
	 */
	public CoverageProcessingNode removeSource(int index) {
		return adaptee.removeSource(index);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode#dispose(boolean)
	 */
	public void dispose(boolean force) {
		adaptee.dispose(force);


	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode#getHints()
	 */
	public Hints getHints() {
		return adaptee.getHints();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode#getCoverageFactory()
	 */
	public GridCoverageFactory getCoverageFactory() {
		return adaptee.getCoverageFactory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode#getNumberOfSinks()
	 */
	public int getNumberOfSinks() {
		return adaptee.getNumberOfSinks();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode#getNumberOfSources()
	 */
	public int getNumberOfSources() {
		return adaptee.getNumberOfSources();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode#getDescription()
	 */
	public InternationalString getDescription() {
		return adaptee.getDescription();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode#getName()
	 */
	public InternationalString getName() {
		return adaptee.getName();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return adaptee.toString();
	}



}
