package org.geotools.mbtiles;

import org.junit.Test;

public class MBTilesMetadataTest {

    @Test
    public void testSetFormatStr() {
        MBTilesMetadata m = new MBTilesMetadata();
        m.setFormatStr("jpg"); // had thrown exception before JPG added to enum
    }
}
