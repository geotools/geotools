package org.geotools.data.ogr;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.WKTReader;

public abstract class GeometryMapperTest extends TestCaseSupport {

    GeometryFactory gf = new GeometryFactory();

    protected GeometryMapperTest(Class<? extends OGRDataStoreFactory> dataStoreFactoryClass) {
        super(dataStoreFactoryClass);
    }

    @Test
    public void testLine() throws Exception {
        checkRoundTrip("LINESTRING(0 0, 10 10)");
    }

    @Test
    public void testPolygon() throws Exception {
        checkRoundTrip("POLYGON((0 0, 0 10, 10 10, 10 0, 0 0))");
    }

    @Test
    public void testPoint() throws Exception {
        checkRoundTrip("POINT(0 0)");
    }

    void checkRoundTrip(String geometryWkt) throws Exception {
        checkRoundTrip(geometryWkt, new GeometryMapper.WKB(gf, dataStoreFactory.createOGR()));
        checkRoundTrip(geometryWkt, new GeometryMapper.WKT(gf, dataStoreFactory.createOGR()));
    }

    void checkRoundTrip(String geometryWkt, GeometryMapper mapper) throws Exception {
        Geometry geometry = new WKTReader().read(geometryWkt);

        // to ogr and back
        OGR ogr = dataStoreFactory.createOGR();

        Object ogrGeometry = mapper.parseGTGeometry(geometry);
        Geometry remapped = mapper.parseOgrGeometry(ogrGeometry);

        ogr.GeometryDestroy(ogrGeometry);

        assertEquals(geometry, remapped);
    }
}
