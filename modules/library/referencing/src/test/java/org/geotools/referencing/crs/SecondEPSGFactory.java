/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.crs;

import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.factory.MockCRSAuthorityFactory;
import org.geotools.util.Version;
import org.geotools.util.factory.Hints;

/**
 * This mock CRS provider is referenced in META-INF/services.
 *
 * <p>It should only be used if the system property <br>
 * org.geotools.referencing.crs.usemockcrsfactory=True
 *
 * @author Roar Brænden
 */
public class SecondEPSGFactory extends MockCRSAuthorityFactory {

    public SecondEPSGFactory() {
        super(
                "Second",
                Citations.EPSG,
                MAXIMUM_PRIORITY - 51,
                new Hints(
                        Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER,
                        Boolean.FALSE,
                        Hints.VERSION,
                        new Version("")));
    }
}
