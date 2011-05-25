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
package org.geotools.coverage.io;

import java.util.Map;

import org.geotools.coverage.io.impl.CoverageReadRequest;
import org.geotools.coverage.io.impl.CoverageResponse;
import org.geotools.coverage.io.impl.CoverageUpdateRequest;
import org.geotools.data.Parameter;
import org.opengis.util.ProgressListener;

/**
 * Provided read-write access to a coverage data product.
 * 
 * @author Simone Giannecchini, GeoSolutions
 * @author Jody Garnett
 * @todo revisit and improve when feedback starts to flow in
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/coverage-experiment/coverage-api/src/main/java/org/geotools/coverage/io/CoverageStore.java $
 */
public interface CoverageStore extends CoverageSource {

	/**
	 * Describes the required (and optional) parameters that
	 * can be passed to the {@link #update(CoverageReadRequest, ProgressListener)} method.
	 * <p>
	 * @return Param a {@link Map} describing the {@link Map} for {@link #update(CoverageReadRequest, ProgressListener)}.
	 */
	public Map<String, Parameter<?>> getUpdateParameterInfo();	

	/**
	 * Issue a writeRequest to the coverage store.
	 * <p>
	 * The writeRequest should be constructed within the guidelines provided by the 
	 * getUpdateParameterInfo method; and should be limited to the abilities
	 * laid out by the getCapabilities method.
	 * </p>
	 * @param writeRequest
	 * @param progress
	 * @return response capturing the success/failure and side effects of performing the update. 
	 */
    public CoverageResponse update(CoverageUpdateRequest writeRequest,
            ProgressListener progress);
}
