/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.referencing.factory.epsg.hsql;

import java.util.Set;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CRSAuthorityFactory;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.referencing.CRS;
import org.geotools.referencing.factory.OrderedAxisAuthorityFactory;

@SuppressWarnings("PMD.SystemPrintln")
public class Decoder {

    public static void main(String[] args) throws FactoryException {
        final CRSAuthorityFactory factory = new OrderedAxisAuthorityFactory("EPSG", null);
        final Set codes = factory.getAuthorityCodes(CoordinateReferenceSystem.class);

        System.out.println("About to decode " + codes.size() + " codes");
        int successes = 0, failures = 0;
        for (Object code : codes) {
            try {
                CRS.decode("EPSG:" + code);
                successes++;
                System.out.println(code + ": Success.");
            } catch (Exception e) {
                failures++;
                System.out.println(code + ": Failed. " + e.getMessage());
            }
        }

        System.out.println("Done decoding, successes " + successes + " and failures " + failures);
    }
}
