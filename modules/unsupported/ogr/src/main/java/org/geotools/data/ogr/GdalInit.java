/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.ogr;

import org.bridj.BridJ;
import org.geotools.data.ogr.bridj.OgrLibrary;

/**
 * Helper class allowing to locate the GDAL library and attach to it 
 *
 * @source $URL$
 */
class GdalInit {

    private static volatile String NATIVE_NAME;

    public static void init() {
        if (NATIVE_NAME == null) {
            synchronized (GdalInit.class) {
                if (NATIVE_NAME == null) {
                    NATIVE_NAME = System.getProperty("GDAL_LIBRARY_NAME");
                    if(NATIVE_NAME != null) {
                        // someone told us its name
                    	if(!checkNativeName(NATIVE_NAME)) {
                    		throw new RuntimeException(
                            "Failed to bind to user specified gdal library named: " + NATIVE_NAME);
                    	}
                    } else {
	                    // on linux platforms libgdal is normally associated with its version,
	                    // painful...
	                    // generate a bunch of the known release numbers, plus more for the future
	                    // :)
	                    if (!checkNativeName("gdal") && !(checkNativeName("gdal_fw"))) {
	                        int x = 1;
	                        // for (int x = 2; x >= 1; x--) {
	                            for (int y = 10; y >= 0; y--) {
	                                if (checkNativeName("gdal" + x + "." + y)) {
	                                    return;
	                                }
	                                for (int z = 5; z >= 0; z--) {
	                                    if (checkNativeName("gdal" + x + "." + y + "." + z)) {
	                                        return;
	                                    }
	                                }
	                            }
	                        // }
	                        throw new RuntimeException(
	                                "Failed to automatically guess the gdal library name, please set the GDAL_LIBRARY_NAME system variable");
	                    }
                    }
                }
            }
        }
    }

    static boolean checkNativeName(String name) {
        try {
            if (BridJ.getNativeLibrary(name) != null) {
                registerAlias(name);
                OgrLibrary.OGRGetDriverCount();

                NATIVE_NAME = name;

                return true;
            } else {
                return false;
            }
        } catch (Throwable t) {
            return false;
        }
    }

    private static void registerAlias(String name) {
        System.out.println("Registered library as " + name);
        BridJ.addNativeLibraryAlias("ogr", name);
        BridJ.addNativeLibraryAlias("osr", name);
        BridJ.addNativeLibraryAlias("cplError", name);
        BridJ.addNativeLibraryAlias("gdal", name);
    }

    
}
