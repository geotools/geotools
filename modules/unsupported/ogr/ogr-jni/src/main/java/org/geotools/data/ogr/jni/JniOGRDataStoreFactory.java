package org.geotools.data.ogr.jni;

import org.geotools.data.ogr.OGR;
import org.geotools.data.ogr.OGRDataStoreFactory;

public class JniOGRDataStoreFactory extends OGRDataStoreFactory {

    @Override
    protected OGR createOGR() {
        return new JniOGR();
    }
    
    @Override
    protected boolean doIsAvailable() throws Throwable {
        System.loadLibrary("gdal");
        return true;
    }

}
