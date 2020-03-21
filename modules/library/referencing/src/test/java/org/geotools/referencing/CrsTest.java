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
package org.geotools.referencing;

import static org.geotools.referencing.crs.DefaultGeographicCRS.WGS84;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.geom.Rectangle2D;
import java.util.Set;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.util.XRectangle2D;
import org.geotools.referencing.CRS.AxisOrder;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.projection.MapProjection;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;

/**
 * Tests the {@link CRS} utilities methods.
 *
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class CrsTest {
    /** Tests the {@link CRS#getSupportedAuthorities} method. */
    @Test
    public void testSupportedAuthorities() {
        final Set<String> withoutAlias = CRS.getSupportedAuthorities(false);
        assertTrue(withoutAlias.contains("CRS"));
        assertTrue(withoutAlias.contains("AUTO2"));
        assertTrue(withoutAlias.contains("urn:ogc:def"));
        assertTrue(withoutAlias.contains("http://www.opengis.net/gml"));
        assertTrue(withoutAlias.contains("http://www.opengis.net/def"));
        assertFalse(withoutAlias.contains("AUTO"));
        assertFalse(withoutAlias.contains("urn:x-ogc:def"));

        final Set<String> withAlias = CRS.getSupportedAuthorities(true);
        assertTrue(withAlias.containsAll(withoutAlias));
        assertFalse(withoutAlias.containsAll(withAlias));
        assertTrue(withAlias.contains("AUTO"));
        assertTrue(withAlias.contains("urn:x-ogc:def"));
    }

    /** Tests simple decode. */
    @Test
    public void testDecode() throws FactoryException {
        assertSame(WGS84, CRS.decode("WGS84(DD)"));
    }

    /**
     * Tests an ESRI code.
     *
     * @todo Not yet working.
     */
    @Test
    @Ignore
    public void testESRICode() throws Exception {
        String wkt =
                "PROJCS[\"Albers_Conic_Equal_Area\",\n"
                        + "  GEOGCS[\"GCS_North_American_1983\",\n"
                        + "    DATUM[\"D_North_American_1983\",\n"
                        + "    SPHEROID[\"GRS_1980\",6378137.0,298.257222101]],\n"
                        + "    PRIMEM[\"Greenwich\",0.0],\n"
                        + "    UNIT[\"Degree\",0.0174532925199433]],\n"
                        + "  PROJECTION[\"Equidistant_Conic\"],\n"
                        + "  PARAMETER[\"False_Easting\",0.0],\n"
                        + "  PARAMETER[\"False_Northing\",0.0],\n"
                        + "  PARAMETER[\"Central_Meridian\",-96.0],\n"
                        + "  PARAMETER[\"Standard_Parallel_1\",33.0],\n"
                        + "  PARAMETER[\"Standard_Parallel_2\",45.0],\n"
                        + "  PARAMETER[\"Latitude_Of_Origin\",39.0],\n"
                        + "  UNIT[\"Meter\",1.0]]";
        CoordinateReferenceSystem crs = CRS.parseWKT(wkt);
        final CoordinateReferenceSystem WGS84 = DefaultGeographicCRS.WGS84;
        final MathTransform crsTransform = CRS.findMathTransform(WGS84, crs, true);
        assertFalse(crsTransform.isIdentity());
    }

    /** Checks X is equated to Easting and Y to Northing */
    @Test
    public void testAxisAliases() throws Exception {
        String wkt1 =
                "PROJCS[\"NAD_1927_Texas_Statewide_Mapping_System\","
                        + "GEOGCS[\"GCS_North_American_1927\","
                        + "DATUM[\"D_North_American_1927\","
                        + "SPHEROID[\"Clarke_1866\",6378206.4,294.9786982]],"
                        + "PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]],"
                        + "PROJECTION[\"Lambert_Conformal_Conic\"],"
                        + "PARAMETER[\"False_Easting\",3000000.0],"
                        + "PARAMETER[\"False_Northing\",3000000.0],"
                        + "PARAMETER[\"Central_Meridian\",-100.0],"
                        + "PARAMETER[\"Standard_Parallel_1\",27.416666666666668],"
                        + "PARAMETER[\"Standard_Parallel_2\",34.916666666666664],"
                        + "PARAMETER[\"Latitude_Of_Origin\",31.166666666666668],"
                        + "UNIT[\"Foot\",0.3048]]";

        String wkt2 =
                "PROJCS[\"NAD_1927_Texas_Statewide_Mapping_System\","
                        + "GEOGCS[\"GCS_North_American_1927\","
                        + "DATUM[\"D_North_American_1927\","
                        + "SPHEROID[\"Clarke_1866\",6378206.4,294.9786982]],"
                        + "PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]],"
                        + "PROJECTION[\"Lambert_Conformal_Conic\"],"
                        + "PARAMETER[\"False_Easting\",3000000.0],"
                        + "PARAMETER[\"False_Northing\",3000000.0],"
                        + "PARAMETER[\"Central_Meridian\",-100.0],"
                        + "PARAMETER[\"Standard_Parallel_1\",27.416666666666668],"
                        + "PARAMETER[\"Standard_Parallel_2\",34.916666666666664],"
                        + "PARAMETER[\"Latitude_Of_Origin\",31.166666666666668],"
                        + "UNIT[\"Foot\",0.3048],"
                        + "AXIS[\"Easting\", EAST],"
                        + "AXIS[\"Northing\", NORTH]]";

        CoordinateReferenceSystem crs1 = CRS.parseWKT(wkt1);
        CoordinateReferenceSystem crs2 = CRS.parseWKT(wkt2);
        assertTrue(CRS.equalsIgnoreMetadata(crs1, crs2));
    }

    /** Tests the transformations of an envelope. */
    @Test
    public void testEnvelopeTransformation() throws FactoryException, TransformException {
        final CoordinateReferenceSystem mapCRS = CRS.parseWKT(WKT.UTM_10N);
        final CoordinateReferenceSystem WGS84 = DefaultGeographicCRS.WGS84;
        final MathTransform crsTransform = CRS.findMathTransform(WGS84, mapCRS, true);
        assertFalse(crsTransform.isIdentity());

        final GeneralEnvelope firstEnvelope, transformedEnvelope, oldEnvelope;
        firstEnvelope = new GeneralEnvelope(new double[] {-124, 42}, new double[] {-122, 43});
        firstEnvelope.setCoordinateReferenceSystem(WGS84);

        transformedEnvelope = CRS.transform(crsTransform, firstEnvelope);
        transformedEnvelope.setCoordinateReferenceSystem(mapCRS);

        oldEnvelope = CRS.transform(crsTransform.inverse(), transformedEnvelope);
        oldEnvelope.setCoordinateReferenceSystem(WGS84);

        assertTrue(oldEnvelope.contains(firstEnvelope, true));
        assertTrue(oldEnvelope.equals(firstEnvelope, 0.02, true));
    }

    /**
     * Tests the transformations of an envelope when the two CRS have identify transforms but
     * different datum names
     */
    @Test
    public void testEnvelopeTransformation2() throws FactoryException, TransformException {
        final CoordinateReferenceSystem WGS84Altered = CRS.parseWKT(WKT.WGS84_ALTERED);
        final CoordinateReferenceSystem WGS84 = DefaultGeographicCRS.WGS84;
        final MathTransform crsTransform = CRS.findMathTransform(WGS84, WGS84Altered, true);
        assertTrue(crsTransform.isIdentity());

        final GeneralEnvelope firstEnvelope;
        firstEnvelope = new GeneralEnvelope(new double[] {-124, 42}, new double[] {-122, 43});
        firstEnvelope.setCoordinateReferenceSystem(WGS84);

        // this triggered a assertion error in GEOT-2934
        Envelope transformed = CRS.transform(firstEnvelope, WGS84Altered);

        // check the envelope is what we expect
        assertEquals(transformed.getCoordinateReferenceSystem(), WGS84Altered);
        double EPS = 1e-9;
        assertEquals(transformed.getMinimum(0), firstEnvelope.getMinimum(0), EPS);
        assertEquals(transformed.getMinimum(1), firstEnvelope.getMinimum(1), EPS);
        assertEquals(transformed.getMaximum(0), firstEnvelope.getMaximum(0), EPS);
        assertEquals(transformed.getMaximum(1), firstEnvelope.getMaximum(1), EPS);
    }

    @Test
    @Ignore
    public void testEnvelopeTransformClipping() throws Exception {
        final CoordinateReferenceSystem source = WGS84;
        final CoordinateReferenceSystem target;
        target =
                CRS.parseWKT(
                        "GEOGCS[\"GCS_North_American_1983\","
                                + "DATUM[\"North_American_Datum_1983\", "
                                + "SPHEROID[\"GRS_1980\", 6378137.0, 298.257222101]], "
                                + "PRIMEM[\"Greenwich\", 0.0], "
                                + "UNIT[\"degree\", 0.017453292519943295], "
                                + "AXIS[\"Longitude\", EAST], "
                                + "AXIS[\"Latitude\", NORTH]]");
        // bounds from geotiff
        GeneralEnvelope geotiff = new GeneralEnvelope(source);
        geotiff.add(new DirectPosition2D(source, -179.9, -90.0));
        geotiff.add(new DirectPosition2D(source, 180.0, 89.9));

        Envelope transformed = CRS.transform(geotiff, target);
        assertNotNull(transformed);
        assertTrue("clipped y", transformed.getUpperCorner().getOrdinate(1) > 88.0);
        assertTrue("clipped y", transformed.getLowerCorner().getOrdinate(1) < -88.0);
        assertTrue("clipped x", transformed.getUpperCorner().getOrdinate(0) > 170.0);
        assertTrue("clipped x", transformed.getLowerCorner().getOrdinate(0) < -170.0);
    }

    /**
     * Tests the transformations of a rectangle using a coordinate operation. With assertions
     * enabled, this also test the transformation of an envelope.
     */
    @Test
    public void testTransformationOverPole() throws FactoryException, TransformException {
        final CoordinateReferenceSystem mapCRS = CRS.parseWKT(WKT.POLAR_STEREOGRAPHIC);
        final CoordinateReferenceSystem WGS84 = DefaultGeographicCRS.WGS84;
        final CoordinateOperation operation =
                CRS.getCoordinateOperationFactory(false).createOperation(mapCRS, WGS84);
        final MathTransform transform = operation.getMathTransform();
        assertTrue(transform instanceof MathTransform2D);
        /*
         * The rectangle to test, which contains the South pole.
         */
        Rectangle2D envelope =
                XRectangle2D.createFromExtremums(
                        -3943612.4042124213,
                        -4078471.954436003,
                        3729092.5890516187,
                        4033483.085688618);
        /*
         * This is what we get without special handling of singularity point.
         * Note that is doesn't include the South pole as we would expect.
         */
        Rectangle2D expected =
                XRectangle2D.createFromExtremums(
                        -178.49352310409273,
                        -88.99136583196398,
                        137.56220967463082,
                        -40.905775004205864);
        /*
         * Tests what we actually get.
         */
        Rectangle2D actual = CRS.transform((MathTransform2D) transform, envelope, null);
        assertTrue(XRectangle2D.equalsEpsilon(expected, actual));
        /*
         * Using the transform(CoordinateOperation, ...) method,
         * the singularity at South pole is taken in account.
         */
        expected = XRectangle2D.createFromExtremums(-180, -90, 180, -40.905775004205864);
        actual = CRS.transform(operation, envelope, actual);
        assertTrue(XRectangle2D.equalsEpsilon(expected, actual));
        /*
         * The rectangle to test, which contains the South pole, but this time the south
         * pole is almost in a corner of the rectangle
         */
        envelope = XRectangle2D.createFromExtremums(-4000000, -4000000, 300000, 30000);
        expected = XRectangle2D.createFromExtremums(-180, -90, 180, -41.03163170198091);
        actual = CRS.transform(operation, envelope, actual);
        assertTrue(XRectangle2D.equalsEpsilon(expected, actual));
    }

    @Test
    public void testGetHorizontalCrs() {
        assertEquals(
                DefaultEngineeringCRS.GENERIC_2D,
                CRS.getHorizontalCRS(DefaultEngineeringCRS.GENERIC_2D));
    }

    @Test
    public void testGetAxisOrder() throws FactoryException {
        String wkt =
                "GEOGCS[\"WGS84(DD)\","
                        + "DATUM[\"WGS84\", "
                        + "SPHEROID[\"WGS84\", 6378137.0, 298.257223563]],"
                        + "PRIMEM[\"Greenwich\", 0.0],"
                        + "UNIT[\"degree\", 0.017453292519943295],"
                        + "AXIS[\"Geodetic longitude\", EAST],"
                        + "AXIS[\"Geodetic latitude\", NORTH]]";
        assertEquals(AxisOrder.EAST_NORTH, CRS.getAxisOrder(CRS.parseWKT(wkt)));

        wkt =
                "GEOGCS[\"WGS84(DD)\","
                        + "DATUM[\"WGS84\", "
                        + "SPHEROID[\"WGS84\", 6378137.0, 298.257223563]],"
                        + "PRIMEM[\"Greenwich\", 0.0],"
                        + "UNIT[\"degree\", 0.017453292519943295],"
                        + "AXIS[\"Geodetic latitude\", NORTH],"
                        + "AXIS[\"Geodetic longitude\", EAST]]";
        assertEquals(AxisOrder.NORTH_EAST, CRS.getAxisOrder(CRS.parseWKT(wkt)));

        assertEquals(
                AxisOrder.INAPPLICABLE,
                CRS.getAxisOrder(CRS.getHorizontalCRS(DefaultEngineeringCRS.GENERIC_2D)));

        wkt =
                "PROJCS[\"ED50 / UTM zone 31N\", "
                        + "  GEOGCS[\"ED50\", "
                        + "    DATUM[\"European Datum 1950\", "
                        + "      SPHEROID[\"International 1924\", 6378388.0, 297.0, AUTHORITY[\"EPSG\",\"7022\"]], "
                        + "      TOWGS84[-157.89, -17.16, -78.41, 2.118, 2.697, -1.434, -1.1097046576093785], "
                        + "      AUTHORITY[\"EPSG\",\"6230\"]], "
                        + "    PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]], "
                        + "    UNIT[\"degree\", 0.017453292519943295], "
                        + "    AXIS[\"Geodetic latitude\", NORTH], "
                        + "    AXIS[\"Geodetic longitude\", EAST], "
                        + "    AUTHORITY[\"EPSG\",\"4230\"]], "
                        + "  PROJECTION[\"Transverse Mercator\", AUTHORITY[\"EPSG\",\"9807\"]], "
                        + "  PARAMETER[\"central_meridian\", 3.0], "
                        + "  PARAMETER[\"latitude_of_origin\", 0.0], "
                        + "  PARAMETER[\"scale_factor\", 0.9996], "
                        + "  PARAMETER[\"false_easting\", 500000.0], "
                        + "  PARAMETER[\"false_northing\", 0.0], "
                        + "  UNIT[\"m\", 1.0], "
                        + "  AXIS[\"Easting\", EAST], "
                        + "  AXIS[\"Northing\", NORTH], "
                        + "  AUTHORITY[\"EPSG\",\"23031\"]]";
        assertEquals(AxisOrder.EAST_NORTH, CRS.getAxisOrder(CRS.parseWKT(wkt)));
        assertEquals(AxisOrder.NORTH_EAST, CRS.getAxisOrder(CRS.parseWKT(wkt), true));
    }

    @Test
    public void testReprojectEnvelope() throws Exception {
        String wkt =
                "GEOGCS[\"WGS84(DD)\","
                        + "DATUM[\"WGS84\", "
                        + "SPHEROID[\"WGS84\", 6378137.0, 298.257223563]],"
                        + "PRIMEM[\"Greenwich\", 0.0],"
                        + "UNIT[\"degree\", 0.017453292519943295],"
                        + "AXIS[\"Geodetic longitude\", EAST],"
                        + "AXIS[\"Geodetic latitude\", NORTH]]";
        CoordinateReferenceSystem wgs84 = CRS.parseWKT(wkt);
        wkt =
                "PROJCS[\"WGS 84 / UTM zone 32N\", \n"
                        + "  GEOGCS[\"WGS 84\", \n"
                        + "    DATUM[\"World Geodetic System 1984\", \n"
                        + "      SPHEROID[\"WGS 84\", 6378137.0, 298.257223563, AUTHORITY[\"EPSG\",\"7030\"]], \n"
                        + "      AUTHORITY[\"EPSG\",\"6326\"]], \n"
                        + "    PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]], \n"
                        + "    UNIT[\"degree\", 0.017453292519943295], \n"
                        + "    AXIS[\"Geodetic longitude\", EAST], \n"
                        + "    AXIS[\"Geodetic latitude\", NORTH], \n"
                        + "    AUTHORITY[\"EPSG\",\"4326\"]], \n"
                        + "  PROJECTION[\"Transverse Mercator\", AUTHORITY[\"EPSG\",\"9807\"]], \n"
                        + "  PARAMETER[\"central_meridian\", 9.0], \n"
                        + "  PARAMETER[\"latitude_of_origin\", 0.0], \n"
                        + "  PARAMETER[\"scale_factor\", 0.9996], \n"
                        + "  PARAMETER[\"false_easting\", 500000.0], \n"
                        + "  PARAMETER[\"false_northing\", 0.0], \n"
                        + "  UNIT[\"m\", 1.0], \n"
                        + "  AXIS[\"Easting\", EAST], \n"
                        + "  AXIS[\"Northing\", NORTH], \n"
                        + "  AUTHORITY[\"EPSG\",\"32632\"]]";
        CoordinateReferenceSystem utm32n = CRS.parseWKT(wkt);

        GeneralEnvelope envelope = new GeneralEnvelope(utm32n);
        envelope.setEnvelope(895817.968, 4439270.710, 1081186.865, 4617454.766);
        // used to throw an exception here
        GeneralEnvelope transformed = CRS.transform(envelope, wgs84);
        assertEquals(13.63, transformed.getMinimum(0), 0.01);
        assertEquals(39.9, transformed.getMinimum(1), 0.01);
        assertEquals(15.96, transformed.getMaximum(0), 0.01);
        assertEquals(41.61, transformed.getMaximum(1), 0.01);
    }

    @Test
    public void parseWKT() throws Exception {
        String[] wkts = {
            "PROJCS[\"Wagner_IV\", GEOGCS[\"WGS84\",  DATUM[\"WGS84\",  SPHEROID[\"WGS84\",  6378137.0, 298.257223563]], PRIMEM[\"Greenwich\", 0.0], UNIT[\"degree\",0.017453292519943295], AXIS[\"Longitude\",EAST], AXIS[\"Latitude\",NORTH]], PROJECTION[\"Wagner_IV\"], UNIT[\"m\", 1.0], AXIS[\"Easting\", EAST], AXIS[\"Northing\", NORTH]]",
            "PROJCS[\"Wagner_V\", GEOGCS[\"WGS84\",  DATUM[\"WGS84\",  SPHEROID[\"WGS84\",  6378137.0, 298.257223563]], PRIMEM[\"Greenwich\", 0.0], UNIT[\"degree\",0.017453292519943295], AXIS[\"Longitude\",EAST], AXIS[\"Latitude\",NORTH]], PROJECTION[\"Wagner_V\"], UNIT[\"m\", 1.0], AXIS[\"Easting\", EAST], AXIS[\"Northing\", NORTH]]"
        };
        for (String wkt : wkts) {
            assertNotNull(CRS.parseWKT(wkt));
        }
    }

    @Test
    public void parseEsriWebMercator() throws Exception {
        String wkt =
                "PROJCS[\"WGS_1984_Web_Mercator_Auxiliary_Sphere\","
                        + "GEOGCS[\"GCS_WGS_1984\",DATUM[\"D_WGS_1984\",SPHEROID[\"WGS_1984\",6378137.0,298.257223563]],"
                        + "PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]],"
                        + "PROJECTION[\"Mercator_Auxiliary_Sphere\"],PARAMETER[\"False_Easting\",0.0],"
                        + "PARAMETER[\"False_Northing\",0.0],PARAMETER[\"Central_Meridian\",0.0],"
                        + "PARAMETER[\"Standard_Parallel_1\",0.0],PARAMETER[\"Auxiliary_Sphere_Type\",0.0],"
                        + "UNIT[\"Meter\",1.0]]";
        ProjectedCRS esriCrs = (ProjectedCRS) CRS.parseWKT(wkt);
        assertEquals(
                "Popular Visualisation Pseudo Mercator",
                esriCrs.getConversionFromBase().getMethod().getName().getCode());

        String wkt3857 =
                "PROJCS[\"WGS 84 / Pseudo-Mercator\", \n"
                        + "  GEOGCS[\"WGS 84\", \n"
                        + "    DATUM[\"World Geodetic System 1984\", \n"
                        + "      SPHEROID[\"WGS 84\", 6378137.0, 298.257223563, AUTHORITY[\"EPSG\",\"7030\"]], \n"
                        + "      AUTHORITY[\"EPSG\",\"6326\"]], \n"
                        + "    PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]], \n"
                        + "    UNIT[\"degree\", 0.017453292519943295], \n"
                        + "    AXIS[\"Geodetic longitude\", EAST], \n"
                        + "    AXIS[\"Geodetic latitude\", NORTH], \n"
                        + "    AUTHORITY[\"EPSG\",\"4326\"]], \n"
                        + "  PROJECTION[\"Popular Visualisation Pseudo Mercator\", AUTHORITY[\"EPSG\",\"1024\"]], \n"
                        + "  PARAMETER[\"semi_minor\", 6378137.0], \n"
                        + "  PARAMETER[\"latitude_of_origin\", 0.0], \n"
                        + "  PARAMETER[\"central_meridian\", 0.0], \n"
                        + "  PARAMETER[\"scale_factor\", 1.0], \n"
                        + "  PARAMETER[\"false_easting\", 0.0], \n"
                        + "  PARAMETER[\"false_northing\", 0.0], \n"
                        + "  UNIT[\"m\", 1.0], \n"
                        + "  AXIS[\"Easting\", EAST], \n"
                        + "  AXIS[\"Northing\", NORTH], \n"
                        + "  AUTHORITY[\"EPSG\",\"3857\"]]";
        CoordinateReferenceSystem epsg3857 = CRS.parseWKT(wkt);

        assertTrue(CRS.equalsIgnoreMetadata(esriCrs, epsg3857));
    }

    @Test
    public void testLambertParsing() throws FactoryException {
        String initialLambertWkt =
                "PROJCS[\"LAMBERT WKT\",GEOGCS[\"GCS_WGS_1984\",DATUM[\"D_WGS_1984\","
                        + "SPHEROID[\"WGS_1984\",6371200.0,0]],PRIMEM[\"Greenwich\",0],UNIT[\"Degree\","
                        + "0.017453292519943295]],PROJECTION[\"Lambert_Conformal_Conic\"],"
                        + "PARAMETER[\"standard_parallel_1\",25.0],PARAMETER[\"latitude_of_origin\",25.0],"
                        + "PARAMETER[\"central_meridian\",-95.0],PARAMETER[\"false_easting\",0],"
                        + "PARAMETER[\"false_northing\",0],PARAMETER[\"Scale_Factor\",1.0],UNIT[\"m\",1]]";
        CoordinateReferenceSystem lambertCRS = CRS.parseWKT(initialLambertWkt);
        String parsedLambertWkt = lambertCRS.toWKT();
        CoordinateReferenceSystem lambertCRS2 = CRS.parseWKT(parsedLambertWkt);
        assertTrue(CRS.equalsIgnoreMetadata(lambertCRS, lambertCRS2));
    }

    @Test
    public void reprojectEnvelopeRotatedPole() throws Exception {
        try {
            MapProjection.SKIP_SANITY_CHECKS = true;
            String wkt =
                    "PROJCS[\"WGS 84 / North Pole LAEA Alaska\", \n"
                            + "  GEOGCS[\"WGS 84\", \n"
                            + "    DATUM[\"World Geodetic System 1984\", \n"
                            + "      SPHEROID[\"WGS 84\", 6378137.0, 298.257223563, AUTHORITY[\"EPSG\",\"7030\"]], \n"
                            + "      AUTHORITY[\"EPSG\",\"6326\"]], \n"
                            + "    PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]], \n"
                            + "    UNIT[\"degree\", 0.017453292519943295], \n"
                            + "    AXIS[\"Geodetic latitude\", NORTH], \n"
                            + "    AXIS[\"Geodetic longitude\", EAST], \n"
                            + "    AUTHORITY[\"EPSG\",\"4326\"]], \n"
                            + "  PROJECTION[\"Lambert_Azimuthal_Equal_Area\", AUTHORITY[\"EPSG\",\"9820\"]], \n"
                            + "  PARAMETER[\"latitude_of_center\", 90.0], \n"
                            + "  PARAMETER[\"longitude_of_center\", -150.0], \n"
                            + "  PARAMETER[\"false_easting\", 0.0], \n"
                            + "  PARAMETER[\"false_northing\", 0.0], \n"
                            + "  UNIT[\"m\", 1.0], \n"
                            + "  AXIS[\"Easting\", \"South along 60 deg West\"], \n"
                            + "  AXIS[\"Northing\", \"South along 30 deg East\"], \n"
                            + "  AUTHORITY[\"EPSG\",\"3572\"]]";

            CoordinateReferenceSystem crs = CRS.parseWKT(wkt);
            // a legit originalEnvelope in rotated pole
            GeneralEnvelope originalEnvelope = new GeneralEnvelope(crs);
            originalEnvelope.setEnvelope(-9000000, -9000000, 900000, 9000000);
            // back to wgs84
            GeneralEnvelope wgs84Envelope = CRS.transform(originalEnvelope, WGS84);
            // System.out.println(wgs84Envelope);
            // and then again in target crs, the result should contain the input, but not taking
            // into account the origin in -150, it ended up not doing so
            GeneralEnvelope transformed = CRS.transform(wgs84Envelope, crs);
            // System.out.println(transformed);
            assertTrue(transformed.contains(originalEnvelope, true));
        } finally {
            MapProjection.SKIP_SANITY_CHECKS = false;
        }
    }

    @Test
    public void testReprojectionRequiredBasics() throws Exception {
        // basic cases
        assertFalse(CRS.isTransformationRequired(null, WGS84));
        assertFalse(CRS.isTransformationRequired(WGS84, null));
        assertFalse(CRS.isTransformationRequired(WGS84, WGS84));
        assertFalse(CRS.isTransformationRequired(WGS84, DefaultEngineeringCRS.GENERIC_2D));
        // basic true case
        assertTrue(CRS.isTransformationRequired(DefaultGeographicCRS.WGS84_3D, WGS84));
    }

    @Test
    public void testReprojectionRequiredDatumAxisSwap() throws Exception {
        CoordinateReferenceSystem lonLatWebMercator =
                CRS.parseWKT(
                        "PROJCS[\"WGS 84 / "
                                + "Pseudo-Mercator\", \n"
                                + "  GEOGCS[\"WGS 84\", \n"
                                + "    DATUM[\"World Geodetic System 1984\", \n"
                                + "      SPHEROID[\"WGS 84\", 6378137.0, 298.257223563, AUTHORITY[\"EPSG\","
                                + "\"7030\"]], \n"
                                + "      AUTHORITY[\"EPSG\",\"6326\"]], \n"
                                + "    PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]], \n"
                                + "    UNIT[\"degree\", 0.017453292519943295], \n"
                                + "    AXIS[\"Geodetic longitude\", EAST], \n"
                                + "    AXIS[\"Geodetic latitude\", NORTH], \n"
                                + "    AUTHORITY[\"EPSG\",\"4326\"]], \n"
                                + "  PROJECTION[\"Popular Visualisation Pseudo Mercator\", AUTHORITY[\"EPSG\","
                                + "\"1024\"]], \n"
                                + "  PARAMETER[\"semi_minor\", 6378137.0], \n"
                                + "  PARAMETER[\"latitude_of_origin\", 0.0], \n"
                                + "  PARAMETER[\"central_meridian\", 0.0], \n"
                                + "  PARAMETER[\"scale_factor\", 1.0], \n"
                                + "  PARAMETER[\"false_easting\", 0.0], \n"
                                + "  PARAMETER[\"false_northing\", 0.0], \n"
                                + "  UNIT[\"m\", 1.0], \n"
                                + "  AXIS[\"Easting\", EAST], \n"
                                + "  AXIS[\"Northing\", NORTH], \n"
                                + "  AUTHORITY[\"EPSG\",\"3857\"]]");
        CoordinateReferenceSystem latLonWebMercator =
                CRS.parseWKT(
                        "PROJCS[\"WGS 84 / "
                                + "Pseudo-Mercator\", \n"
                                + "  GEOGCS[\"WGS 84\", \n"
                                + "    DATUM[\"World Geodetic System 1984\", \n"
                                + "      SPHEROID[\"WGS 84\", 6378137.0, 298.257223563, AUTHORITY[\"EPSG\","
                                + "\"7030\"]], \n"
                                + "      AUTHORITY[\"EPSG\",\"6326\"]], \n"
                                + "    PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]], \n"
                                + "    UNIT[\"degree\", 0.017453292519943295], \n"
                                + "    AXIS[\"Geodetic latitude\", NORTH], \n"
                                + "    AXIS[\"Geodetic longitude\", EAST], \n"
                                + "    AUTHORITY[\"EPSG\",\"4326\"]], \n"
                                + "  PROJECTION[\"Popular Visualisation Pseudo Mercator\", AUTHORITY[\"EPSG\","
                                + "\"1024\"]], \n"
                                + "  PARAMETER[\"semi_minor\", 6378137.0], \n"
                                + "  PARAMETER[\"latitude_of_origin\", 0.0], \n"
                                + "  PARAMETER[\"central_meridian\", 0.0], \n"
                                + "  PARAMETER[\"scale_factor\", 1.0], \n"
                                + "  PARAMETER[\"false_easting\", 0.0], \n"
                                + "  PARAMETER[\"false_northing\", 0.0], \n"
                                + "  UNIT[\"m\", 1.0], \n"
                                + "  AXIS[\"Easting\", EAST], \n"
                                + "  AXIS[\"Northing\", NORTH], \n"
                                + "  AUTHORITY[\"EPSG\",\"3857\"]]");

        // the projected ordinates are the same, no need to actually run a transformation
        assertFalse(CRS.isTransformationRequired(lonLatWebMercator, latLonWebMercator));
    }
}
