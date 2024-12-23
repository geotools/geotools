/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid.io;

import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.filter.function.RenderingTransformation;

/**
 * Marks a {@link RenderingTransformation} that is able to perform its own data read from a
 * {@link org.geotools.coverage.grid.io.GridCoverage2DReader} and an array of
 * {@link org.geotools.api.parameter.GeneralParameterValue}. The rendering machinery will then invoke the transformation
 * providing a ReaderAndParams object, rather than doing its own read and then calling it with the resulting
 * {@link org.geotools.api.coverage.grid.GridCoverage}
 */
public interface CoverageReadingTransformation extends RenderingTransformation {

    class ReaderAndParams {
        GridCoverage2DReader reader;
        GeneralParameterValue[] readParameters;

        public ReaderAndParams(GridCoverage2DReader reader, GeneralParameterValue[] readParameters) {
            this.reader = reader;
            this.readParameters = readParameters;
        }

        public GridCoverage2DReader getReader() {
            return reader;
        }

        public GeneralParameterValue[] getReadParameters() {
            return readParameters;
        }
    }
}
