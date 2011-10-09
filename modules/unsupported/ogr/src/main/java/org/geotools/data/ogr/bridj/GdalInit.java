package org.geotools.data.ogr.bridj;

import org.bridj.BridJ;

/**
 * 
 *
 * @source $URL$
 */
public class GdalInit {

    private static volatile String NATIVE_NAME;

    public static void init() {
        if (NATIVE_NAME == null) {
            synchronized (GdalInit.class) {
                if (NATIVE_NAME == null) {
                    NATIVE_NAME = System.getProperty("GDAL_LIBRARY_NAME");
                    if(NATIVE_NAME != null) {
                        // someone told us its name
                    }
                    
                    
                    // on linux platforms libgdal is normally associated with its version,
                    // painful...
                    // generate a bunch of the known release numbers, plus more for the future
                    // :)
                    if (!checkNativeName("gdal")) {
                        for (int x = 3; x >= 1; x--) {
                            for (int y = 10; y >= 0; y--) {
                                if (checkNativeName("gdal" + x + "." + y)) {
                                    return;
                                }
                                for (int z = 10; z >= 0; z--) {
                                    if (checkNativeName("gdal" + x + "." + y + "." + z)) {
                                        return;
                                    }
                                }
                            }
                        }
                        throw new RuntimeException(
                                "Failed to automatically guess the gdal library name");
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
