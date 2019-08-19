package org.geotools.data.ogr.bridj;

import org.bridj.Pointer;
import org.geotools.data.ogr.OGRDataStoreTest;

public class BridjOGRDataStoreTest extends OGRDataStoreTest {

    public BridjOGRDataStoreTest() {
        super(BridjOGRDataStoreFactory.class);
    }

    public void testVersion() {
        Pointer<Byte> result = OgrLibrary.GDALVersionInfo(Pointer.pointerToCString("--version"));
        // System.out.println(result.getCString());
    }
}
