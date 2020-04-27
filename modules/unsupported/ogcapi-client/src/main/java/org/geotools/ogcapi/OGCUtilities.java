package org.geotools.ogcapi;

import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class OGCUtilities {
    static CoordinateReferenceSystem parseCRS(String supportedCRS) {
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
            String version = remainder.substring(index + 1); // we only have one EPSG
            // db so let's pretend we
            // didn't seen this!
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
