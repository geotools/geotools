/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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


import java.io.IOException;

import org.geotools.factory.OptionalFactory;


/**
 * Constructs a live GridCoverageFormat.
 *
 * <p>
 * In addition to implementing
 * this interface datastores should have a services file:
 * </p>
 *
 * <p>
 * <code>META-INF/services/org.geotools.data.GridCoverageFormatFactorySpi</code>
 * </p>
 *
 * <p>
 * The file should contain a single line which gives the full name of the
 * implementing class.
 * </p>
 *
 * <p>
 * example:<br/><code>e.g.
 * org.geotools.data.arcgrid.ArcGridFormatFactory</code>
 * </p>
 *
 * <p>
 * The factories are never called directly by users, instead the
 * GridFormatFinder class is used.
 * </p>
 *
 * @author Jody Garnett, Refractions Research
 * @author Simone Giannecchini, GeoSolutions
 * @source $URL$
 */
public interface GridFormatFactorySpi extends OptionalFactory {
    /**
     * Construct a live grid format.
     * 
     *
     * @throws IOException (Warning: the rest of the javadoc comment was wrong) 
     *                     if there were any problems creating or connecting
     *                     the datasource.
     */
    AbstractGridFormat createFormat();




}
