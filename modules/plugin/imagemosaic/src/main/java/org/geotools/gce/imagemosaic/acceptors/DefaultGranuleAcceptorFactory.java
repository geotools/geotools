/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2016, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */

package org.geotools.gce.imagemosaic.acceptors;

import java.util.Arrays;
import java.util.List;

/**
 * Factory to create the default granule acceptor.
 *
 * <p>The default mosaic do not accept granules with heterogeneous CRS and incompatible palettes.
 */
public class DefaultGranuleAcceptorFactory implements GranuleAcceptorFactorySPI {
    @Override
    public List<GranuleAcceptor> create() {
        return Arrays.asList(new ColorCheckAcceptor(), new HomogeneousCRSAcceptor());
    }
}
