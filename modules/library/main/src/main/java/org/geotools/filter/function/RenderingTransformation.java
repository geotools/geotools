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
package org.geotools.filter.function;

import org.geotools.data.Query;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.coverage.grid.GridGeometry;
import org.opengis.filter.expression.Function;
import org.opengis.parameter.GeneralParameterValue;

/**
 * An interface to be implemented by processes or by filter functions meant to be integrated as
 * feature collection/grid coverage transformations in a rendering chain.
 *
 * <p>These functions are meant to take one layer (raster or vector) and apply transformations on
 * top of it
 *
 * <p>The method provide information about how the data is altered so that the renderer can query
 * the appropriate part of the input data that will feed the process/function
 *
 * @author Andrea Aime - GeoSolutions
 */
public interface RenderingTransformation extends Function {

    /**
     * Given a target query and a target grid geometry returns the query to be used to read the
     * input data of the function This method will be called only if the input data is a feature
     * collection.
     *
     * @return The transformed query, or null if no inversion is possible/meaningful
     */
    Query invertQuery(Query targetQuery, GridGeometry gridGeometry);

    /**
     * Given a target query and a target grid geometry returns the grid geometry to be used to read
     * the input data of the function involved in rendering. This method will be called only if the
     * input data is a grid coverage or a grid coverage reader
     *
     * @return The transformed query, or null if no inversion is possible/meaningful
     */
    GridGeometry invertGridGeometry(Query targetQuery, GridGeometry targetGridGeometry);

    /** Allows the transformation to customize the read */
    default GeneralParameterValue[] customizeReadParams(
            GridCoverageReader reader, GeneralParameterValue[] params) {
        return params;
    }
}
