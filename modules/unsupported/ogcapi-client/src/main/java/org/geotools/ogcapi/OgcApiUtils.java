/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ogcapi;

import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public final class OgcApiUtils {
    private OgcApiUtils() {}
    // TODO - upgrade CRS.decode to handle URIs
    public static CoordinateReferenceSystem parseCRS(String supportedCRS) {
        for (char seperator : new char[] {'/', ':'}) { // handle both
            // http://www.opengis.net/def/crs/EPSG/0/27700
            // &
            // urn:ogc:def:crs:EPSG::27700
            int index = supportedCRS.lastIndexOf(seperator);

            if (index < 6) // allow for the : in URIs
            continue;
            String code = supportedCRS.substring(index + 1);
            String remainder = supportedCRS.substring(0, index - 1);
            index = remainder.lastIndexOf(seperator);
            // TODO handle version of DB?
            remainder = remainder.substring(0, index);
            index = remainder.lastIndexOf(seperator);
            String auth = remainder.substring(index + 1);
            if (code.equalsIgnoreCase("900913")) { // Die, Die, Die
                code = "3857";
            }
            if ("EPSG".equalsIgnoreCase(auth)) {
                try {
                    return (CRS.decode(auth + ":" + code));
                } catch (FactoryException e) {

                    // e.printStackTrace();
                }
            } else if ("CRS84".equalsIgnoreCase(code)) {
                return (DefaultGeographicCRS.WGS84);
            }
        }
        return null;
    }
}
