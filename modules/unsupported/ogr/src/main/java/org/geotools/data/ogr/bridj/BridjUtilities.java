package org.geotools.data.ogr.bridj;

import org.bridj.Pointer;

/**
 * 
 *
 * @source $URL: https://svn.osgeo.org/geotools/trunk/modules/unsupported/ogr/src/main/java/org/geotools/data/ogr/bridj/BridjUtilities.java $
 */
public class BridjUtilities {

    public static String getCString(Pointer<?> ptr) {
        if (ptr == null) {
            return null;
        } else {
            return ptr.getCString();
        }
    }
}
