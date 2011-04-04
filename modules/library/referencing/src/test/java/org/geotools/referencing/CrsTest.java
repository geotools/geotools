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

import static org.junit.Assert.*;

import java.awt.geom.Rectangle2D;
import java.util.Set;

import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS.AxisOrder;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.resources.geometry.XRectangle2D;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;


/**
 * Tests the {@link CRS} utilities methods.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class CrsTest {
    /**
     * Tests the {@link CRS#getSupportedAuthorities} method.
     */
    @Test
    public void testSupportedAuthorities() {
        final Set<String> withoutAlias = CRS.getSupportedAuthorities(false);
        assertTrue (withoutAlias.contains("CRS"));
        assertTrue (withoutAlias.contains("AUTO2"));
        assertTrue (withoutAlias.contains("urn:ogc:def"));
        assertTrue (withoutAlias.contains("http://www.opengis.net"));
        assertFalse(withoutAlias.contains("AUTO"));
        assertFalse(withoutAlias.contains("urn:x-ogc:def"));

        final Set<String> withAlias = CRS.getSupportedAuthorities(true);
        assertTrue (withAlias.containsAll(withoutAlias));
        assertFalse(withoutAlias.containsAll(withAlias));
        assertTrue (withAlias.contains("AUTO"));
        assertTrue (withAlias.contains("urn:x-ogc:def"));
    }

    /**
     * Tests simple decode.
     */
    @Test
    public void testDecode() throws FactoryException {
        assertSame(DefaultGeographicCRS.WGS84, CRS.decode("WGS84(DD)"));
    }

    /**
     * Tests an ESRI code.
     *
     * @todo Not yet working.
     */
    @Test
    @Ignore
    public void testESRICode() throws Exception {
        String wkt = "PROJCS[\"Albers_Conic_Equal_Area\",\n"                  +
                     "  GEOGCS[\"GCS_North_American_1983\",\n"                +
                     "    DATUM[\"D_North_American_1983\",\n"                 +
                     "    SPHEROID[\"GRS_1980\",6378137.0,298.257222101]],\n" +
                     "    PRIMEM[\"Greenwich\",0.0],\n"                       +
                     "    UNIT[\"Degree\",0.0174532925199433]],\n"            +
                     "  PROJECTION[\"Equidistant_Conic\"],\n"                 +
                     "  PARAMETER[\"False_Easting\",0.0],\n"                  +
                     "  PARAMETER[\"False_Northing\",0.0],\n"                 +
                     "  PARAMETER[\"Central_Meridian\",-96.0],\n"             +
                     "  PARAMETER[\"Standard_Parallel_1\",33.0],\n"           +
                     "  PARAMETER[\"Standard_Parallel_2\",45.0],\n"           +
                     "  PARAMETER[\"Latitude_Of_Origin\",39.0],\n"            +
                     "  UNIT[\"Meter\",1.0]]";
        CoordinateReferenceSystem crs = CRS.parseWKT(wkt);
        final CoordinateReferenceSystem WGS84  = DefaultGeographicCRS.WGS84;
        final MathTransform crsTransform = CRS.findMathTransform(WGS84, crs, true);
        assertFalse(crsTransform.isIdentity());
    }
    
    /**
     * Checks X is equated to Easting and Y to Northing
     * @throws Exception
     */
    public void testAxisAliases() throws Exception {
        String wkt1 = "PROJCS[\"NAD_1927_Texas_Statewide_Mapping_System\"," +
        		"GEOGCS[\"GCS_North_American_1927\"," +
        		"DATUM[\"D_North_American_1927\"," +
        		"SPHEROID[\"Clarke_1866\",6378206.4,294.9786982]]," +
        		"PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]]," +
        		"PROJECTION[\"Lambert_Conformal_Conic\"]," +
        		"PARAMETER[\"False_Easting\",3000000.0]," +
        		"PARAMETER[\"False_Northing\",3000000.0]," +
        		"PARAMETER[\"Central_Meridian\",-100.0]," +
        		"PARAMETER[\"Standard_Parallel_1\",27.416666666666668]," +
        		"PARAMETER[\"Standard_Parallel_2\",34.916666666666664]," +
        		"PARAMETER[\"Latitude_Of_Origin\",31.166666666666668]," +
        		"UNIT[\"Foot\",0.3048]]";
        
        String wkt2 = "PROJCS[\"NAD_1927_Texas_Statewide_Mapping_System\"," +
                "GEOGCS[\"GCS_North_American_1927\"," +
                "DATUM[\"D_North_American_1927\"," +
                "SPHEROID[\"Clarke_1866\",6378206.4,294.9786982]]," +
                "PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]]," +
                "PROJECTION[\"Lambert_Conformal_Conic\"]," +
                "PARAMETER[\"False_Easting\",3000000.0]," +
                "PARAMETER[\"False_Northing\",3000000.0]," +
                "PARAMETER[\"Central_Meridian\",-100.0]," +
                "PARAMETER[\"Standard_Parallel_1\",27.416666666666668]," +
                "PARAMETER[\"Standard_Parallel_2\",34.916666666666664]," +
                "PARAMETER[\"Latitude_Of_Origin\",31.166666666666668]," +
                "UNIT[\"Foot\",0.3048]" +
                "AXIS[\"Easting\", EAST]," + 
                "AXIS[\"Northing\", NORTH]]";
        
        CoordinateReferenceSystem crs1 = CRS.parseWKT(wkt1);
        CoordinateReferenceSystem crs2 = CRS.parseWKT(wkt2);
        assertTrue(CRS.equalsIgnoreMetadata(crs1, crs2));
    }

    /**
     * Tests the transformations of an envelope.
     */
    @Test
    public void testEnvelopeTransformation() throws FactoryException, TransformException {
        final CoordinateReferenceSystem mapCRS = CRS.parseWKT(WKT.UTM_10N);
        final CoordinateReferenceSystem WGS84  = DefaultGeographicCRS.WGS84;
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
        assertTrue(oldEnvelope.equals  (firstEnvelope, 0.02, true));
    }
    
    /**
     * Tests the transformations of an envelope when the two CRS have identify
     * transforms but different datum names 
     */
    @Test
    public void testEnvelopeTransformation2() throws FactoryException, TransformException {
        final CoordinateReferenceSystem WGS84Altered = CRS.parseWKT(WKT.WGS84_ALTERED);
        final CoordinateReferenceSystem WGS84  = DefaultGeographicCRS.WGS84;
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

    /**
     * Tests the transformations of a rectangle using a coordinate operation.
     * With assertions enabled, this also test the transformation of an envelope.
     */
    @Test
    public void testTransformationOverPole() throws FactoryException, TransformException {
        final CoordinateReferenceSystem mapCRS = CRS.parseWKT(WKT.POLAR_STEREOGRAPHIC);
        final CoordinateReferenceSystem WGS84  = DefaultGeographicCRS.WGS84;
        final CoordinateOperation operation =
                CRS.getCoordinateOperationFactory(false).createOperation(mapCRS, WGS84);
        final MathTransform transform = operation.getMathTransform();
        assertTrue(transform instanceof MathTransform2D);
        /*
         * The rectangle to test, which contains the South pole.
         */
        Rectangle2D envelope = XRectangle2D.createFromExtremums(
                -3943612.4042124213, -4078471.954436003,
                 3729092.5890516187,  4033483.085688618);
        /*
         * This is what we get without special handling of singularity point.
         * Note that is doesn't include the South pole as we would expect.
         */
        Rectangle2D expected = XRectangle2D.createFromExtremums(
                -178.49352310409273, -88.99136583196398,
                 137.56220967463082, -40.905775004205864);
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
        assertEquals( DefaultEngineeringCRS.GENERIC_2D, CRS.getHorizontalCRS(DefaultEngineeringCRS.GENERIC_2D));
    }
    
    @Test
    public void testGetAxisOrder() throws FactoryException {
        String wkt = 
        "GEOGCS[\"WGS84(DD)\"," +  
          "DATUM[\"WGS84\", " +
            "SPHEROID[\"WGS84\", 6378137.0, 298.257223563]]," + 
          "PRIMEM[\"Greenwich\", 0.0]," + 
          "UNIT[\"degree\", 0.017453292519943295]," + 
          "AXIS[\"Geodetic longitude\", EAST]," + 
          "AXIS[\"Geodetic latitude\", NORTH]]";
        assertEquals(AxisOrder.LON_LAT, CRS.getAxisOrder(CRS.parseWKT(wkt)));
        assertEquals(AxisOrder.EAST_NORTH, CRS.getAxisOrder(CRS.parseWKT(wkt)));
        
        wkt = 
        "GEOGCS[\"WGS84(DD)\"," +  
          "DATUM[\"WGS84\", " +
            "SPHEROID[\"WGS84\", 6378137.0, 298.257223563]]," + 
          "PRIMEM[\"Greenwich\", 0.0]," + 
          "UNIT[\"degree\", 0.017453292519943295]," + 
          "AXIS[\"Geodetic latitude\", NORTH]," +
          "AXIS[\"Geodetic longitude\", EAST]]";
        assertEquals(AxisOrder.LAT_LON, CRS.getAxisOrder(CRS.parseWKT(wkt)));
        assertEquals(AxisOrder.NORTH_EAST, CRS.getAxisOrder(CRS.parseWKT(wkt)));
        
        assertEquals(AxisOrder.INAPPLICABLE, CRS.getAxisOrder(CRS.getHorizontalCRS(DefaultEngineeringCRS.GENERIC_2D)));
        
        wkt = 
        "PROJCS[\"ED50 / UTM zone 31N\", "+
        "  GEOGCS[\"ED50\", "+
        "    DATUM[\"European Datum 1950\", "+
        "      SPHEROID[\"International 1924\", 6378388.0, 297.0, AUTHORITY[\"EPSG\",\"7022\"]], "+
        "      TOWGS84[-157.89, -17.16, -78.41, 2.118, 2.697, -1.434, -1.1097046576093785], "+
        "      AUTHORITY[\"EPSG\",\"6230\"]], "+
        "    PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]], "+
        "    UNIT[\"degree\", 0.017453292519943295], "+
        "    AXIS[\"Geodetic latitude\", NORTH], "+
        "    AXIS[\"Geodetic longitude\", EAST], "+
        "    AUTHORITY[\"EPSG\",\"4230\"]], "+
        "  PROJECTION[\"Transverse Mercator\", AUTHORITY[\"EPSG\",\"9807\"]], "+
        "  PARAMETER[\"central_meridian\", 3.0], "+
        "  PARAMETER[\"latitude_of_origin\", 0.0], "+
        "  PARAMETER[\"scale_factor\", 0.9996], "+
        "  PARAMETER[\"false_easting\", 500000.0], "+
        "  PARAMETER[\"false_northing\", 0.0], "+
        "  UNIT[\"m\", 1.0], "+
        "  AXIS[\"Easting\", EAST], "+
        "  AXIS[\"Northing\", NORTH], "+
        "  AUTHORITY[\"EPSG\",\"23031\"]]";
        assertEquals(AxisOrder.EAST_NORTH, CRS.getAxisOrder(CRS.parseWKT(wkt)));
        assertEquals(AxisOrder.NORTH_EAST, CRS.getAxisOrder(CRS.parseWKT(wkt), true));
    }
}
