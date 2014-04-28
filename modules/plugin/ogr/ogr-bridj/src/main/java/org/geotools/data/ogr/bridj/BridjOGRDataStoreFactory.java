package org.geotools.data.ogr.bridj;

import static org.geotools.data.ogr.bridj.OgrLibrary.OGRGetDriverCount;
import static org.geotools.data.ogr.bridj.OgrLibrary.OGRRegisterAll;

import org.geotools.data.ogr.OGR;
import org.geotools.data.ogr.OGRDataStoreFactory;

public class BridjOGRDataStoreFactory extends OGRDataStoreFactory {

    private static Boolean INIT = false;
    public static void init() {
        try {
            if (!INIT) {
                GdalInit.init();
                
                // perform OGR format registration once
                if (OGRGetDriverCount() == 0) {
                    OGRRegisterAll();
                }
            }
        }
        finally {
            INIT = true;
        }
    }

    protected boolean doIsAvailable() throws Exception {
        init();
        return OGRGetDriverCount() > 0;
    }

    @Override
    protected OGR createOGR() {
        return new BridjOGR();
    }
}
