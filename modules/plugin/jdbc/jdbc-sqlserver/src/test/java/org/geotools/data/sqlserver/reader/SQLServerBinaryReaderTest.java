package org.geotools.data.sqlserver.reader;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;
import com.vividsolutions.jts.io.WKTReader;
import junit.framework.TestCase;

import java.io.IOException;

/**
 * Binary test data have been produced by executing this query in SQL Server, and then removing the prefix '0x':
 * select geometry::STGeomFromText('wktstring',srid);
 *
 * @source $URL$
 */
public class SQLServerBinaryReaderTest extends TestCase {

    public void testPoint() throws Exception {
        String geometryPointWKT = "POINT (5 10)";
        String geometryPointBinary = "E6100000010C00000000000014400000000000002440";
        testGeometry(geometryPointBinary, geometryPointWKT, 4326);
    }

    public void testEmptyPointGeometry() throws Exception {
        String geometryEmptyWKT = "POINT EMPTY";
        String geometryEmptyBinary = "000000000104000000000000000001000000FFFFFFFFFFFFFFFF01";
        testGeometry(geometryEmptyBinary, geometryEmptyWKT);
    }

    public void testEmptyPolygonGeometry() throws Exception {
        String geometryEmptyWKT = "POLYGON EMPTY";
        String geometryEmptyBinary = "000000000104000000000000000001000000FFFFFFFFFFFFFFFF03";
        testGeometry(geometryEmptyBinary, geometryEmptyWKT);
    }

    public void testEmptyGeometryCollection() throws Exception {
        String geometryEmptyWKT = "GEOMETRYCOLLECTION EMPTY";
        String geometryEmptyBinary = "000000000104000000000000000001000000FFFFFFFFFFFFFFFF07";
        testGeometry(geometryEmptyBinary, geometryEmptyWKT);
    }

    public void testPolygon() throws Exception {
        String geometryPolygonWkt = "POLYGON ((-680000 6100000, -670000 6100000, -670000 6090000, -680000 6090000, -680000 6100000))";  //32633
        String geometryPolygonBinary = "797F00000104050000000000000080C024C1000000000845574100000000607224C1000000000845574100000000607224C100000000443B57410000000080C024C100000000443B57410000000080C024C1000000000845574101000000020000000001000000FFFFFFFF0000000003";
        testGeometry(geometryPolygonBinary, geometryPolygonWkt, 32633);
    }

    public void testLineStringWithZ() throws Exception {
        String geometryLineStringWithZWKT = "LINESTRING (0 1 1, 3 2 2, 4 5 NaN)";
        String geometryLineStringWithZBinary = "E61000000105030000000000000000000000000000000000F03F0000000000000840000000000000004000000000000010400000000000001440000000000000F03F0000000000000040000000000000F8FF01000000010000000001000000FFFFFFFF0000000002";
        Geometry geometry = testGeometry(geometryLineStringWithZBinary, geometryLineStringWithZWKT, 4326);
        assertEquals(1.0, geometry.getCoordinates()[0].z, 0);
        assertEquals(2.0, geometry.getCoordinates()[1].z, 0);
        assertEquals(Double.NaN, geometry.getCoordinates()[2].z, 0);
    }

    public void testGeometryCollection() throws Exception {
        String geometryCollectionWKT = "GEOMETRYCOLLECTION (POINT (4 0), LINESTRING (4 2, 5 3), POLYGON ((0 0, 3 0, 3 3, 0 3, 0 0),(1 1, 1 2, 2 2, 2 1, 1 1)))";
        String geometryCollectionBinary = "0000000001040D0000000000000000001040000000000000000000000000000010400000000000000040000000000000144000000000000008400000000000000000000000000000000000000000000008400000000000000000000000000000084000000000000008400000000000000000000000000000084000000000000000000000000000000000000000000000F03F000000000000F03F000000000000F03F0000000000000040000000000000004000000000000000400000000000000040000000000000F03F000000000000F03F000000000000F03F04000000010000000001010000000203000000000800000004000000FFFFFFFF0000000007000000000000000001000000000100000002000000000200000003";
        testGeometry(geometryCollectionBinary, geometryCollectionWKT,0);

        String wkt2 = "GEOMETRYCOLLECTION (POINT (4.2585 0), MULTILINESTRING ((10 10, 20 20, 10 40.999), (40 40, 30 30, 40 20, 30 10)), POLYGON ((0 0, 3 0, 3 3, 0 3, 0 0),(1 1, 1 2, 2 2, 2 1, 1 1)))";
        String binary2 = "0000000001041200000062105839B40811400000000000000000000000000000244000000000000024400000000000003440000000000000344000000000000024401D5A643BDF7F4440000000000000444000000000000044400000000000003E400000000000003E40000000000000444000000000000034400000000000003E4000000000000024400000000000000000000000000000000000000000000008400000000000000000000000000000084000000000000008400000000000000000000000000000084000000000000000000000000000000000000000000000F03F000000000000F03F000000000000F03F0000000000000040000000000000004000000000000000400000000000000040000000000000F03F000000000000F03F000000000000F03F050000000100000000010100000001040000000208000000000D00000006000000FFFFFFFF0000000007000000000000000001000000000100000005020000000100000002020000000200000002000000000300000003";
        testGeometry(binary2, wkt2, 0);
    }

    public void testMultiLinestring() throws Exception {
        String wkt = "MULTILINESTRING ((10 10, 20 20, 10 40), (40 40, 30 30, 40 20, 30 10)) "; //4326
        String binary =  "00000000010407000000000000000000244000000000000024400000000000003440000000000000344000000000000024400000000000004440000000000000444000000000000044400000000000003E400000000000003E40000000000000444000000000000034400000000000003E400000000000002440020000000100000000010300000003000000FFFFFFFF0000000005000000000000000002000000000100000002";
        testGeometry(binary, wkt, 0);
    }

    public void testMultiPoint() throws Exception {
        String wkt = "MULTIPOINT ((10 40), (40 30), (20 20), (30 10))";
        String binary = "000000000104040000000000000000002440000000000000444000000000000044400000000000003E40000000000000344000000000000034400000000000003E40000000000000244004000000010000000001010000000102000000010300000005000000FFFFFFFF0000000004000000000000000001000000000100000001000000000200000001000000000300000001";
        testGeometry(binary, wkt, 0);
    }

    public void testMultiPolygon() throws Exception {
        String wkt = "MULTIPOLYGON (((30 20, 10 40, 45 40, 30 20)),((15 5, 40 10, 10 20, 5 10, 15 5)))";
        String binary = "000000000104090000000000000000003E40000000000000344000000000000024400000000000004440000000000080464000000000000044400000000000003E4000000000000034400000000000002E4000000000000014400000000000004440000000000000244000000000000024400000000000003440000000000000144000000000000024400000000000002E400000000000001440020000000200000000020400000003000000FFFFFFFF0000000006000000000000000003000000000100000003";
        testGeometry(binary, wkt, 0);
    }

    public void testSingleLineSegment() throws Exception {
        String geometryPointWKT = "LINESTRING (5 10, 10 10)";
        String geometryPointBinary = "0000000001140000000000001440000000000000244000000000000024400000000000002440";
        testGeometry(geometryPointBinary, geometryPointWKT, 0);
    }

    public void testGeographyExtremeValues() throws Exception {
        String wkt = "LINESTRING (-90 -15069, 90 15069)";
        String binary = "E6100000011400000000008056C000000000806ECDC0000000000080564000000000806ECD40";
        testGeometry(binary,wkt,4326);
    }

    private Geometry testGeometry(String geometryBinary, String geometryWKT) throws Exception {
        WKTReader readerWkt = new WKTReader((new GeometryFactory(new PrecisionModel(), 0)));
        return testGeometry(geometryBinary, geometryWKT, readerWkt);
    }

    private Geometry testGeometry(String geometryBinary, String geometryWKT, WKTReader wktReader) throws ParseException, IOException {
        byte[] bytes = WKBReader.hexToBytes(geometryBinary);
        Geometry geometryFromWkt = wktReader.read(geometryWKT);
        SqlServerBinaryReader reader = new SqlServerBinaryReader();
        Geometry geometryFromBinary = reader.read(bytes);
        assertEquals(geometryFromWkt, geometryFromBinary);
        return geometryFromBinary;
    }

    private Geometry testGeometry(String geometryBinary, String geometryWKT, int srid) throws Exception {
        WKTReader readerWkt = new WKTReader((new GeometryFactory(new PrecisionModel(), srid)));
        Geometry geometry = testGeometry(geometryBinary, geometryWKT, readerWkt);
        assertEquals(srid, geometry.getSRID());
        return geometry;
    }
}
