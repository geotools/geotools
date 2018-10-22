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
package org.geotools.process;

import java.util.Map;
import org.geotools.data.Query;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.coverage.grid.GridGeometry;
import org.opengis.parameter.GeneralParameterValue;

/**
 * An interface to be implemented by processes meant to be integrated as feature collection/grid
 * coverage transformations in a rendering chain.
 *
 * <p>The method provide information about how the data is altered so that the renderer can query
 * the appropriate part of the input data that will feed the process
 *
 * @author Andrea Aime - GeoSolutions
 */
public interface RenderingProcess extends Process {

    /**
     * Given a target query and a target grid geometry returns the query to be used to read the
     * input data of the process involved in rendering. This method will be called only if the input
     * data is a feature collection.
     *
     * @param input The process inputs
     * @param targetQuery The query against the transformation outputs
     * @param gridGeometry The grid geometry desired for the outputs of the transformation
     * @return The transformed query, or null if no inversion is possible/meaningful
     */
    Query invertQuery(Map<String, Object> input, Query targetQuery, GridGeometry gridGeometry)
            throws ProcessException;

    /**
     * Given a target query and a target grid geometry returns the grid geometry to be used to read
     * the input data of the process involved in rendering. This method will be called only if the
     * input data is a grid coverage or a grid coverage reader
     *
     * @param input The process inputs
     * @param targetQuery The query against the transformation outputs
     * @param gridGeometry The grid geometry desired for the outputs of the transformation
     * @return The transformed query, or null if no inversion is possible/meaningful
     */
    GridGeometry invertGridGeometry(
            Map<String, Object> input, Query targetQuery, GridGeometry targetGridGeometry)
            throws ProcessException;

    /**
     * Allows the transformation to customize the read parameters (asl
     *
     * @param input The process inputs
     * @param reader The reader involved
     * @param params The current parameters
     * @return The original parameters, or a customized version of them
     */
    default GeneralParameterValue[] customizeReadParams(
            Map<String, Object> input, GridCoverageReader reader, GeneralParameterValue[] params) {
        return params;
    }
}
