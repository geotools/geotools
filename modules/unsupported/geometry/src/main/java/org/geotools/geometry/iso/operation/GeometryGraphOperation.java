/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    
 *    (C) 2001-2006  Vivid Solutions
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.iso.operation;

import org.geotools.geometry.iso.PrecisionModel;
import org.geotools.geometry.iso.UnsupportedDimensionException;
import org.geotools.geometry.iso.root.GeometryImpl;
import org.geotools.geometry.iso.topograph2D.GeometryGraph;
import org.geotools.geometry.iso.util.algorithm2D.CGAlgorithms;
import org.geotools.geometry.iso.util.algorithm2D.LineIntersector;
import org.geotools.geometry.iso.util.algorithm2D.RobustLineIntersector;

/**
 * The base class for operations that require {@link org.geotools.geometry.iso.topograph2D.GeometryGraph)s.
 *
 * @source $URL$
 */
public abstract class GeometryGraphOperation {

	protected final CGAlgorithms cga = new CGAlgorithms();

	protected final LineIntersector li = new RobustLineIntersector();

	protected PrecisionModel resultPrecisionModel;

	/**
	 * The operation args into an array so they can be accessed by index
	 */
	protected GeometryGraph[] arg; // the arg(s) of the operation

	public GeometryGraphOperation(GeometryImpl g0, GeometryImpl g1)
			throws UnsupportedDimensionException {

		// use the most precise model for the result
		// TODO PRECISION CORRECTION?!
		// if (g0.getPrecisionModel().compareTo(g1.getPrecisionModel()) >= 0)
		// setComputationPrecision(g0.getPrecisionModel());
		// else
		// setComputationPrecision(g1.getPrecisionModel());

		// Throw Unsupported Dimension Exception if one of the geometries is not 2d or 2.5d
		//DimensionModel g0Dim = g0.getFeatGeometryFactory().getDimensionModel();
		//DimensionModel g1Dim = g1.getFeatGeometryFactory().getDimensionModel();
		int g0Dim = g0.getCoordinateReferenceSystem().getCoordinateSystem().getDimension();
		int g1Dim = g1.getCoordinateReferenceSystem().getCoordinateSystem().getDimension();
		if (g0Dim != 2 || g1Dim != 2) {
		//if (!g0Dim.is2D() || !g1Dim.is2D()) {
			throw new UnsupportedDimensionException(
					"This operation only works in 2D");
		}

		arg = new GeometryGraph[2];
		arg[0] = new GeometryGraph(0, g0);
		arg[1] = new GeometryGraph(1, g1);
	}

	public GeometryGraphOperation(GeometryImpl g0) {

		// TODO PRECISION CORRECTION
		// setComputationPrecision(g0.getPrecisionModel());

		arg = new GeometryGraph[1];
		arg[0] = new GeometryGraph(0, g0);
		;
	}

	public GeometryImpl getArgGeometry(int i) {
		return arg[i].getGeometry();
	}

	protected void setComputationPrecision(PrecisionModel pm) {
		resultPrecisionModel = pm;
		li.setPrecisionModel(resultPrecisionModel);
	}
}
