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

import java.util.Collections;
import java.util.List;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.factory.Hints;
import org.geotools.renderer.i18n.ErrorKeys;
import org.geotools.renderer.i18n.Errors;
import org.geotools.renderer.i18n.Vocabulary;
import org.geotools.renderer.i18n.VocabularyKeys;
import org.geotools.util.SimpleInternationalString;
import org.opengis.util.InternationalString;

/**
 * {@link RootNode}  implements a  {@link BaseCoverageProcessingNode}  which act as the rot for a graph of  {@link CoverageProcessingNode} s
 * @author  Simone Giannecchini, GeoSolutions
 */
class RootNode extends BaseCoverageProcessingNode implements
		CoverageProcessingNode {

	/*
	 * (non-Javadoc)
	 * @see CoverageProcessingNode#getName() 
	 */
	public InternationalString getName() {
		return Vocabulary.formatInternational(VocabularyKeys.ROOT_NODE);
	}

	/**
	 * The source {@link GridCoverage2D} for this {@link RootNode}.
	 */
	private GridCoverage2D sourceCoverage;

//	/**
//	 * Tells me whether or not to adopt the source {@link GridCoverage2D},
//	 * which means dispose it with this node or not.
//	 */
//	private boolean adopt;



	/**
	 * Default constructor using a {@link GridCoverage2D} as a source.
	 * 
	 * 
	 * @param coverage
	 *            the {@link GridCoverage2D} that makes this
	 *            {@link CoverageProcessingNode}.
	 */
	public RootNode(GridCoverage2D coverage) {
		this(coverage,  null);
	}

	/**
	 * Default constructor using a {@link GridCoverage2D} as a source.
	 * 
	 * 
	 * @param coverage
	 *            the {@link GridCoverage2D} that makes this
	 *            {@link CoverageProcessingNode}.
	 * @param hints
	 *            to control internal creation of factories,
	 */
	public RootNode(GridCoverage2D coverage,Hints hints) {
		super(
				-1,
				hints,
				SimpleInternationalString.wrap("RootNode"),
				SimpleInternationalString
						.wrap("Root node which does not have sources but simply wraps a GridCoverage2D"));
		ensureNotNull(coverage, "GridCoverage2D");
		this.sourceCoverage = coverage;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.BaseCoverageProcessingNode#addSource(org.geotools.renderer.lite.gridcoverage2d.CoverageProcessingNode)
	 */
	public boolean addSource(CoverageProcessingNode source) {
		throw new UnsupportedOperationException(Errors.format(
				ErrorKeys.UNSUPPORTED_OPERATION_$1,
				"addSource(CoverageProcessingNode)"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.BaseCoverageProcessingNode#getSource(int)
	 */
	public CoverageProcessingNode getSource(int index) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.BaseCoverageProcessingNode#getSources()
	 */
	public List<CoverageProcessingNode> getSources() {
		return Collections.emptyList();
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.BaseCoverageProcessingNode#dispose(boolean)
	 */
	public void dispose(boolean force) {
		super.dispose(force);
		sourceCoverage.dispose(force);
		

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.BaseCoverageProcessingNode#execute()
	 */
	protected GridCoverage2D execute() {
		return sourceCoverage;

	}

}
