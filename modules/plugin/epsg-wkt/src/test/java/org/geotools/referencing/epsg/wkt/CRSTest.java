/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.epsg.wkt;

import static org.junit.Assert.assertArrayEquals;

import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.MathTransform;

/** @author Jody Garnett */
public class CRSTest extends TestCase {
    /** Makes sure that the transform between two EPSG:4326 is the identity transform. */
    public void testFindMathTransformIdentity() throws FactoryException {
        CoordinateReferenceSystem crs1default = CRS.decode("EPSG:4326", false);
        CoordinateReferenceSystem crs2default = CRS.decode("EPSG:4326", false);
        MathTransform tDefault = CRS.findMathTransform(crs1default, crs2default);
        assertTrue("WSG84 transformed to WSG84 should be Identity", tDefault.isIdentity());

        CoordinateReferenceSystem crs1force = CRS.decode("EPSG:4326", true);
        CoordinateReferenceSystem crs2force = CRS.decode("EPSG:4326", true);
        MathTransform tForce = CRS.findMathTransform(crs1force, crs2force);
        assertTrue("WSG84 transformed to WSG84 should be Identity", tForce.isIdentity());
    }

    public void testEPSG42102() throws Exception {
        CoordinateReferenceSystem bc = CRS.decode("EPSG:42102");
        assertNotNull("bc", bc);
    }

    public void testEPSG28992toWGS84() throws Exception {
        /*
         * Unit test to accompany the fix for https://osgeo-org.atlassian.net/browse/GEOT-5077
         */
        CoordinateReferenceSystem epsg28992 = CRS.decode("EPSG:28992");
        CoordinateReferenceSystem wgs84 = DefaultGeographicCRS.WGS84;

        MathTransform transform = CRS.findMathTransform(epsg28992, wgs84);

        /*
         * Test using 4 reference points from the corners of the CRS, obtained from the website of the Dutch national
         * mapping agency at https://rdinfo.kadaster.nl/
         *
         * https://rdinfo.kadaster.nl/overzicht_rd_medewerker/rd_punt.html?punt={ID}
         *
         * ID      X-RD         Y-RD          Latitude             Longitude
         * 250317  121784.6113  487036.9695   52° 22' 12.74367''   4° 53' 58.15203''
         *                                    52.370206575         4.899486675
         *
         * 610306  176135.0779  317654.5066   50° 50' 54.01748''   5° 41' 14.23435''
         *                                    50.8483381889        5.68728731944
         *
         * 079342  233473.7307  581727.0264   53° 12' 59.05571''   6° 33' 43.15655''
         *                                    53.2164043639        6.56198793056
         *
         * 489101   31935.2867  391557.3350   51° 29' 58.46250''   3° 36' 53.15985''
         *                                    51.4995729167        3.614766625
         */
        double[] srcCoords =
                new double[] {
                    /* Id: 250317 */ 121784.6113, 487036.9695,
                    /* Id: 610306 */ 176135.0779, 317654.5066,
                    /* Id: 079342 */ 233473.7307, 581727.0264,
                    /* Id: 489101 */ 31935.2867, 391557.3350
                };

        double[] expectedTransformedCoords =
                new double[] {
                    /* Id: 250317 */ 4.899486675, 52.370206575,
                    /* Id: 610306 */ 5.68728731944, 50.8483381889,
                    /* Id: 079342 */ 6.56198793056, 53.2164043639,
                    /* Id: 489101 */ 3.614766625, 51.4995729167,
                };

        double[] transformedCoords = new double[8];

        transform.transform(srcCoords, 0, transformedCoords, 0, 4);

        // No transformation is perfect, so we allow a small delta
        assertArrayEquals(expectedTransformedCoords, transformedCoords, 0.000003);
    }

    public void testAUTO4200() throws Exception {
        CoordinateReferenceSystem utm = CRS.decode("AUTO:42001,0.0,0.0");
        assertNotNull("auto-utm", utm);
    }

    public void test4269() throws Exception {
        CoordinateReferenceSystem latlong = CRS.decode("EPSG:4269");
        assertNotNull("latlong", latlong);
        try {
            latlong = CRS.decode("4269");
            fail("Shoudl not be able to decode 4269 without EPSG authority");
        } catch (NoSuchAuthorityCodeException e) {
            // expected
        }
        assertNotNull("latlong", latlong);
    }

    public void testManditoryTranform() throws Exception {
        CoordinateReferenceSystem WGS84 =
                (CoordinateReferenceSystem) CRS.decode("EPSG:4326"); // latlong
        CoordinateReferenceSystem NAD83 = (CoordinateReferenceSystem) CRS.decode("EPSG:4269");
        CoordinateReferenceSystem NAD83_UTM10 =
                (CoordinateReferenceSystem) CRS.decode("EPSG:26910");
        CoordinateReferenceSystem BC_ALBERS = (CoordinateReferenceSystem) CRS.decode("EPSG:42102");

        CoordinateOperation op =
                ReferencingFactoryFinder.getCoordinateOperationFactory(null)
                        .createOperation(WGS84, WGS84);
        MathTransform math = op.getMathTransform();

        DirectPosition pt1 = new GeneralDirectPosition(0.0, 0.0);
        DirectPosition pt2 = math.transform(pt1, null);
        assertNotNull(pt2);

        double pts[] =
                new double[] {
                    1187128, 395268, 1187128, 396027,
                    1188245, 396027, 1188245, 395268,
                    1187128, 395268
                };
        double tst[] = new double[pts.length];
        math.transform(pts, 0, new double[pts.length], 0, pts.length / 2);
        for (int i = 0; i < pts.length; i++) assertTrue("pts[" + i + "]", pts[i] != tst[i]);
    }
    /** Taken from empty udig map calculation of scale. */
    public void testSamplePixel() throws Exception {
        Map map = new HashMap();
        // map.put( Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, true );
        // map.put( Hints.FORCE_STANDARD_AXIS_DIRECTIONS, true );
        // map.put( Hints.FORCE_STANDARD_AXIS_UNITS, true );

        Hints global = new Hints(map);
        GeoTools.init(global);

        // ReferencedEnvelope[-0.24291497975705742 : 0.24291497975711265, -0.5056179775280899 :
        // -0.0]
        CoordinateReferenceSystem EPSG4326 = CRS.decode("EPSG:4326");
        double[] pixelBounds =
                new double[] {-0.24291497975705742, 0.24291497975711265, -0.5056179775280899, 0.0};
        CoordinateReferenceSystem WGS84 = DefaultGeographicCRS.WGS84;
        MathTransform mt = CRS.findMathTransform(EPSG4326, WGS84, true);
        double[] result = new double[4];
        mt.transform(pixelBounds, 0, result, 0, 2);
        assertArrayEquals(result, pixelBounds, 0);
    }

    public void testReprojection() throws Exception {
        // origional bc alberts
        double[] poly1 =
                new double[] {
                    1187128, 395268, 1187128, 396027,
                    1188245, 396027, 1188245, 395268,
                    1187128, 395268
                };

        // transformed
        double[] poly3 =
                new double[] {
                    -123.47009555832284, 48.543261561072285,
                    -123.46972894676578, 48.55009592117936,
                    -123.45463828850829, 48.54973520267304,
                    -123.4550070827961, 48.54290089070186,
                    -123.47009555832284, 48.543261561072285
                };

        CoordinateReferenceSystem WGS84 =
                (CoordinateReferenceSystem) CRS.decode("EPSG:4326"); // latlong
        CoordinateReferenceSystem BC_ALBERS = (CoordinateReferenceSystem) CRS.decode("EPSG:42102");

        MathTransform transform = CRS.findMathTransform(BC_ALBERS, WGS84);

        double[] polyAfter = new double[10];
        transform.transform(poly1, 0, polyAfter, 0, 5);

        assertArrayEquals(poly3, polyAfter, 0.00000000000001);
    }

    public void testReprojectionDefault() throws Exception {
        // origional bc alberts
        double[] poly1 =
                new double[] {
                    1187128, 395268, 1187128, 396027,
                    1188245, 396027, 1188245, 395268,
                    1187128, 395268
                };

        // transformed
        double[] poly3 =
                new double[] {
                    -123.47009555832284, 48.543261561072285,
                    -123.46972894676578, 48.55009592117936,
                    -123.45463828850829, 48.54973520267304,
                    -123.4550070827961, 48.54290089070186,
                    -123.47009555832284, 48.543261561072285
                };

        CoordinateReferenceSystem WGS84 = DefaultGeographicCRS.WGS84;
        CoordinateReferenceSystem BC_ALBERS = (CoordinateReferenceSystem) CRS.decode("EPSG:42102");

        MathTransform transform = CRS.findMathTransform(BC_ALBERS, WGS84);
        double[] polyAfter = new double[poly1.length];
        transform.transform(poly1, 0, polyAfter, 0, 5);
        assertArrayEquals(poly3, polyAfter, 0.00000000000001);
    }

    public static GeometryFactory factory = new GeometryFactory();

    protected void assertEnvelopeEquals(Geometry a, Geometry b, double tolerance) {
        Envelope aEnv = a.getEnvelopeInternal();
        Envelope bEnv = b.getEnvelopeInternal();

        assertEquals(aEnv.getMinX(), bEnv.getMinX(), tolerance);
        assertEquals(aEnv.getMaxX(), bEnv.getMaxX(), tolerance);
        assertEquals(aEnv.getMinY(), bEnv.getMinY(), tolerance);
        assertEquals(aEnv.getMaxY(), bEnv.getMaxY(), tolerance);
    }
}
