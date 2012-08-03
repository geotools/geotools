package org.geotools.data.ogr.bridj;

import org.geotools.data.ogr.GeometryMapperTest;
public class BridjGeometryMapperTest extends GeometryMapperTest {

    public BridjGeometryMapperTest() {
        super(BridjOGRDataStoreFactory.class);
    }

    @Override
    protected void setUp() throws Exception {
        GdalInit.init();
    }

}
