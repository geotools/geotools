/*
 *    GeoTools Sample code and Tutorials by Open Source Geospatial Foundation, and others
 *    https://docs.geotools.org
 *
 *    To the extent possible under law, the author(s) have dedicated all copyright
 *    and related and neighboring rights to this software to the public domain worldwide.
 *    This software is distributed without any warranty.
 *
 *    You should have received a copy of the CC0 Public Domain Dedication along with this
 *    software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package org.geotools.referencing;

import java.awt.RenderingHints.Key;
import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.geotools.api.metadata.Identifier;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.IdentifiedObject;
import org.geotools.api.referencing.crs.CRSAuthorityFactory;
import org.geotools.api.referencing.crs.CRSFactory;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.crs.GeographicCRS;
import org.geotools.api.referencing.crs.ProjectedCRS;
import org.geotools.api.referencing.cs.AxisDirection;
import org.geotools.api.referencing.cs.CSFactory;
import org.geotools.api.referencing.cs.CartesianCS;
import org.geotools.api.referencing.cs.CoordinateSystemAxis;
import org.geotools.api.referencing.cs.EllipsoidalCS;
import org.geotools.api.referencing.datum.DatumFactory;
import org.geotools.api.referencing.datum.Ellipsoid;
import org.geotools.api.referencing.datum.GeodeticDatum;
import org.geotools.api.referencing.datum.PrimeMeridian;
import org.geotools.api.referencing.operation.Conversion;
import org.geotools.api.referencing.operation.MathTransformFactory;
import org.geotools.api.util.GenericName;
import org.geotools.geometry.jts.JTS;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.datum.BursaWolfParameters;
import org.geotools.referencing.datum.DefaultGeodeticDatum;
import org.geotools.referencing.factory.ReferencingFactoryContainer;
import org.geotools.referencing.operation.DefiningConversion;
import org.geotools.referencing.wkt.Formattable;
import org.geotools.util.SuppressFBWarnings;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Coordinate;
import si.uom.NonSI;
import si.uom.SI;

/**
 * The following examples are taken from CTSTutorial provided by Rueben Schulz. The examples were constructed for a wiki
 * page and have been repurposed for our sphinx documentation.
 *
 * @author Jody Garnett
 */
@SuppressFBWarnings("DLS_DEAD_LOCAL_STORE")
public class ReferencingExamples {

    @SuppressWarnings("unused")
    private ProjectedCRS utm10NCRS;

    @SuppressWarnings("unused")
    private GeographicCRS nad27CRS;

    ReferencingExamples() {
        try {
            premadeObjects();
            creatCRSFromWKT();
            createFromEPSGCode();
            createFromEPSGCode2();
            createCRSByHand1();
            createCRSByHand2();
            createCRSByHand3();
            // createMathTransformBetweenCRSs();
            // transformUsingCRSUtility();
            // createAndUseMathTransform();
            // hintExample();
            // createTransformFromAuthorityCode();
            toWKT();
            toWKTFormat();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    void factories() {
        // factories start
        Hints hints = null; // configure hints for the group of factories
        ReferencingFactoryContainer group = new ReferencingFactoryContainer(hints);
        CRSFactory crsFactory = group.getCRSFactory();
        CSFactory csFactory = group.getCSFactory();
        DatumFactory datumFactory = group.getDatumFactory();
        // factories end
    }

    @SuppressWarnings("unused")
    void referencingFactoryContainer() {
        Object datumFactory = null;
        Object csFactory = null;
        Object crsFactory = null;
        Object mtFactory = null;

        // referencingFactoryContainer start
        Map<Key, Object> map = new HashMap<>();

        map.put(Hints.DATUM_FACTORY, datumFactory);
        map.put(Hints.CS_FACTORY, csFactory);
        map.put(Hints.CRS_FACTORY, crsFactory);
        map.put(Hints.MATH_TRANSFORM_FACTORY, mtFactory);

        Hints hints = new Hints(map);

        ReferencingFactoryContainer container = new ReferencingFactoryContainer(hints);
        // referencingFactoryContainer end
    }

    void referencingFactoryContainer2() {
        // referencingFactoryContainer2 start
        Hints hints = GeoTools.getDefaultHints();
        DatumFactory datumFactory = ReferencingFactoryFinder.getDatumFactory(hints);

        ReferencingFactoryContainer container = new ReferencingFactoryContainer(hints);

        if (datumFactory == container.getDatumFactory()) {
            System.out.println("Will be the same DatumFactory");
        }
        // referencingFactoryContainer2 end
    }
    /** A method with some examples of premade static objects. */
    @SuppressWarnings("unused")
    void premadeObjects() {
        // premadeObjects start
        GeographicCRS geoCRS = org.geotools.referencing.crs.DefaultGeographicCRS.WGS84;
        GeodeticDatum wgs84Datum = org.geotools.referencing.datum.DefaultGeodeticDatum.WGS84;
        PrimeMeridian greenwichMeridian = org.geotools.referencing.datum.DefaultPrimeMeridian.GREENWICH;
        CartesianCS cartCS = org.geotools.referencing.cs.DefaultCartesianCS.GENERIC_2D;
        CoordinateSystemAxis latAxis = org.geotools.referencing.cs.DefaultCoordinateSystemAxis.GEODETIC_LATITUDE;
        // premadeObjects end
    }

    /**
     * An example of creating a CRS from a WKT string.
     *
     * <p>Additional examples of WKT strings can be found in the test data.
     */
    void creatCRSFromWKT() throws Exception {
        System.out.println("------------------------------------------");
        System.out.println("Creating a CRS from a WKT string:");
        // creatCRSFromWKT start
        CRSFactory crsFactory = ReferencingFactoryFinder.getCRSFactory(null);
        String wkt = "PROJCS[\"UTM_Zone_10N\", "
                + "GEOGCS[\"WGS84\", "
                + "DATUM[\"WGS84\", "
                + "SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], "
                + "PRIMEM[\"Greenwich\", 0.0], "
                + "UNIT[\"degree\",0.017453292519943295], "
                + "AXIS[\"Longitude\",EAST], "
                + "AXIS[\"Latitude\",NORTH]], "
                + "PROJECTION[\"Transverse_Mercator\"], "
                + "PARAMETER[\"semi_major\", 6378137.0], "
                + "PARAMETER[\"semi_minor\", 6356752.314245179], "
                + "PARAMETER[\"central_meridian\", -123.0], "
                + "PARAMETER[\"latitude_of_origin\", 0.0], "
                + "PARAMETER[\"scale_factor\", 0.9996], "
                + "PARAMETER[\"false_easting\", 500000.0], "
                + "PARAMETER[\"false_northing\", 0.0], "
                + "UNIT[\"metre\",1.0], "
                + "AXIS[\"x\",EAST], "
                + "AXIS[\"y\",NORTH]]";

        CoordinateReferenceSystem crs = crsFactory.createFromWKT(wkt);
        // creatCRSFromWKT end
        System.out.println("  CRS: " + crs.toWKT());
        System.out.println("Identified CRS object:");
        printIdentifierStuff(crs);
        System.out.println("Identified Datum object:");
        printIdentifierStuff(((ProjectedCRS) crs).getDatum());
        System.out.println("------------------------------------------");
    }

    /**
     * Creates a CRS from an EPSG code. There are a few different EPSG authority factories in geotools that do roughly
     * the same thing:
     *
     * <ul>
     *   <li>gt2-epsg-access.jar is backed by the official EPSG MS Access database (only works on MS Windows, therefore
     *       I have not shown how to configure it here).
     *   <li>gt2-epsg-hsql.jar provides an embeded hsql database created from the EPSG SQL scripts. This contains the
     *       same information as the MS Arcess database.
     *   <li>other factories allow the EPSG information to be in an external database (postgresql, mysql, oracle)
     *   <li>gt2-epsg-wkt.jar is a simple properties file with WKT descriptions for EPSG defined CRS codes. This file
     *       does not derive directly from the official EPSG database, so its should be used with caution. It provides a
     *       very simple method of creating a new authority factory and named objects.
     * </ul>
     *
     * The specific authority factory returned by getCRSAuthorityFactory is dependent on the different factories on your
     * classpath (ie WKT or Access or HSQL) and the hints you provide. By default the "better" authority factory should
     * be used if more than one is available.
     *
     * <p>TODO check on the use of hints TODO expand on how to use EPSG data in a postgres db (this may be a 2.2
     * feature, but FactoryUsingANSISQL may work)
     */
    void createFromEPSGCode2() throws Exception {
        System.out.println("------------------------------------------");
        System.out.println("Creating a CRS from an authority factory:");
        // createFromEPSGCode2 start
        String code = "26910";
        CRSAuthorityFactory crsAuthorityFactory = ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG", null);
        CoordinateReferenceSystem crs = crsAuthorityFactory.createCoordinateReferenceSystem(code);
        // createFromEPSGCode2 end
        System.out.println("  CRS: " + crs.toWKT());
        System.out.println("Identified CRS object:");
        printIdentifierStuff(crs);
        System.out.println("------------------------------------------");
    }

    /** Creating using the CRS facade */
    void createFromEPSGCode() throws Exception {
        System.out.println("------------------------------------------");
        System.out.println("Creating a CRS from an authority factory:");
        // createFromEPSGCode start
        CoordinateReferenceSystem crs = CRS.decode("EPSG:26910", false);
        // createFromEPSGCode end
        System.out.println("  CRS: " + crs.toWKT());
        System.out.println("Identified CRS object:");
        printIdentifierStuff(crs);
        System.out.println("------------------------------------------");
    }

    /**
     * Creates a WGS 84/UTM Zone 10N CRS mostly (uses some premade objects) by hand. Uses the higher level FactoryGroup
     * instead of the lower level MathTransformFactory (commented out).
     */
    void createCRSByHand1() throws Exception {
        System.out.println("------------------------------------------");
        System.out.println("Creating a CRS by hand:");
        // createCRSByHand1 start
        MathTransformFactory mtFactory = ReferencingFactoryFinder.getMathTransformFactory(null);
        CRSFactory crsFactory = ReferencingFactoryFinder.getCRSFactory(null);

        GeographicCRS geoCRS = org.geotools.referencing.crs.DefaultGeographicCRS.WGS84;
        CartesianCS cartCS = org.geotools.referencing.cs.DefaultCartesianCS.GENERIC_2D;

        ParameterValueGroup parameters = mtFactory.getDefaultParameters("Transverse_Mercator");
        parameters.parameter("central_meridian").setValue(-111.0);
        parameters.parameter("latitude_of_origin").setValue(0.0);
        parameters.parameter("scale_factor").setValue(0.9996);
        parameters.parameter("false_easting").setValue(500000.0);
        parameters.parameter("false_northing").setValue(0.0);
        Conversion conversion = new DefiningConversion("Transverse_Mercator", parameters);

        Map<String, ?> properties = Collections.singletonMap("name", "WGS 84 / UTM Zone 12N");
        ProjectedCRS projCRS = crsFactory.createProjectedCRS(properties, geoCRS, conversion, cartCS);
        // createCRSByHand1 end

        // parameters.parameter("semi_major").setValue(((GeodeticDatum)geoCRS.getDatum()).getEllipsoid().getSemiMajorAxis());
        // parameters.parameter("semi_minor").setValue(((GeodeticDatum)geoCRS.getDatum()).getEllipsoid().getSemiMinorAxis());

        // MathTransform trans = mtFactory.createParameterizedTransform(parameters);
        // ProjectedCRS projCRS = crsFactory.createProjectedCRS(
        // Collections.singletonMap("name", "WGS 84 / UTM Zone 12N"),
        // new org.geotools.referencing.operation.OperationMethod(trans),
        // geoCRS, trans, cartCS);
        System.out.println("  Projected CRS: " + projCRS.toWKT());
        System.out.println("------------------------------------------");

        // save for later use in createMathTransformBetweenCRSs()
        this.utm10NCRS = projCRS;
    }

    /**
     * Creates a NAD 27 geographic CRS. Notice that the datum factory automatically adds aliase names to the datum
     * (because "North American Datum 1927" has an entry in
     * module/referencing/src/org/geotools/referencing/factory/DatumAliasesTable.txt ). Also notice that toWGS84
     * information (used in a datum transform) was also added to the datum.
     */
    void createCRSByHand2() throws Exception {
        System.out.println("------------------------------------------");
        System.out.println("Creating a CRS by hand:");
        // createCRSByHand2 start
        CRSFactory crsFactory = ReferencingFactoryFinder.getCRSFactory(null);
        DatumFactory datumFactory = ReferencingFactoryFinder.getDatumFactory(null);
        CSFactory csFactory = ReferencingFactoryFinder.getCSFactory(null);

        Map<String, Object> map = new HashMap<>();
        map.put("name", "Clarke 1866");

        Ellipsoid clark1866ellipse = datumFactory.createFlattenedSphere(map, 6378206.4, 294.978698213901, SI.METRE);

        PrimeMeridian greenwichMeridian = org.geotools.referencing.datum.DefaultPrimeMeridian.GREENWICH;

        final BursaWolfParameters toWGS84 = new BursaWolfParameters(DefaultGeodeticDatum.WGS84);
        toWGS84.dx = -3.0;
        toWGS84.dy = 142;
        toWGS84.dz = 183;

        map.clear();
        map.put("name", "North American Datum 1927");
        map.put(DefaultGeodeticDatum.BURSA_WOLF_KEY, toWGS84);

        GeodeticDatum clark1866datum = datumFactory.createGeodeticDatum(map, clark1866ellipse, greenwichMeridian);
        System.out.println(clark1866datum.toWKT());
        // notice all of the lovely datum aliases (used to determine if two
        // datums are the same)
        System.out.println("Identified Datum object:");
        printIdentifierStuff(clark1866datum);

        map.clear();
        map.put("name", "<lat>, <long>");
        CoordinateSystemAxis latAxis = org.geotools.referencing.cs.DefaultCoordinateSystemAxis.GEODETIC_LATITUDE;
        CoordinateSystemAxis longAxis = org.geotools.referencing.cs.DefaultCoordinateSystemAxis.GEODETIC_LONGITUDE;
        EllipsoidalCS ellipsCS = csFactory.createEllipsoidalCS(map, latAxis, longAxis);

        map.clear();
        map.put("name", "NAD 27");
        map.put("authority", "9999");
        // TODO add an authority code here (should be an identifier)
        GeographicCRS nad27CRS = crsFactory.createGeographicCRS(map, clark1866datum, ellipsCS);
        // createCRSByHand2 end
        System.out.println(nad27CRS.toWKT());
        System.out.println("Identified CRS object:");
        printIdentifierStuff(nad27CRS);

        System.out.println("------------------------------------------");

        // save for latter use in createMathTransformBetweenCRSs()
        this.nad27CRS = nad27CRS;
    }

    /**
     * Creates two coordinate reference system by hand without using any of the GT2 APIs (except FactoryFinder to get
     * things started). It does not use any of the static objects available in geotools implementations. The following
     * example creates a CRS to represent the Airy 1830 ellipsoid with the incoming data in the order of
     * (long,lat,height) and a geocentric CRS with (x,y,z) axises.
     *
     * <p>TODO the Airy CRS described below is actually wgs84, FIX this.
     */
    void createCRSByHand3() throws FactoryException {
        System.out.println("------------------------------------------");
        System.out.println("Creating two CRSs by hand:");

        // createCRSByHand3 start
        CRSFactory crsFactory = ReferencingFactoryFinder.getCRSFactory(null);
        DatumFactory datumFactory = ReferencingFactoryFinder.getDatumFactory(null);
        CSFactory csFactory = ReferencingFactoryFinder.getCSFactory(null);
        Map<String, Object> map = new HashMap<>();

        //
        // Create a datum used for each CRS
        //
        map.clear();
        map.put("name", "Greenwich Meridian");
        PrimeMeridian greenwichMeridian = datumFactory.createPrimeMeridian(map, 0, NonSI.DEGREE_ANGLE);

        map.clear();
        map.put("name", "WGS 84 Ellipsoid Datum");
        Ellipsoid wgs84Ellipsoid = datumFactory.createFlattenedSphere(map, 6378137, 298.257223563, SI.METRE);

        map.clear();
        map.put("name", "WGS84 Height Datum");
        GeodeticDatum wgs84Datum = datumFactory.createGeodeticDatum(map, wgs84Ellipsoid, greenwichMeridian);

        //
        // Create a geocentric CRS
        //
        // Create a collection of axes for the coordinate system.
        map.clear();
        map.put("name", "Cartesian X axis");
        CoordinateSystemAxis xAxis =
                csFactory.createCoordinateSystemAxis(map, "X", AxisDirection.GEOCENTRIC_X, SI.METRE);

        map.clear();
        map.put("name", "Cartesian Y axis");
        CoordinateSystemAxis yAxis =
                csFactory.createCoordinateSystemAxis(map, "Y", AxisDirection.GEOCENTRIC_Y, SI.METRE);

        map.clear();
        map.put("name", "Cartesian Z axis");
        CoordinateSystemAxis zAxis =
                csFactory.createCoordinateSystemAxis(map, "Z", AxisDirection.GEOCENTRIC_Z, SI.METRE);

        map.clear();
        map.put("name", "Rendered Cartesian CS");
        CartesianCS worldCS = csFactory.createCartesianCS(map, xAxis, yAxis, zAxis);

        // Now, the geocentric coordinate reference system that we'd use for output - eg to a 3D
        // renderer
        map.clear();
        map.put("name", "Output Cartesian CS");
        CoordinateReferenceSystem geocentricCRS = crsFactory.createGeocentricCRS(map, wgs84Datum, worldCS);
        System.out.println("Geocentric CRS: " + geocentricCRS.toWKT());

        //
        // Create a geograyhic CRS for the Airy 1830 ellipsoid
        // map.clear();
        // map.put("name", "Airy 1830");
        // Ellipsoid airyEllipse =
        // datumFactory.createFlattenedSphere(map, 6377563.396, 299.3249646, SI.METRE);

        map.clear();
        map.put("name", "Geodetic North axis");
        CoordinateSystemAxis northAxis =
                csFactory.createCoordinateSystemAxis(map, "N", AxisDirection.NORTH, NonSI.DEGREE_ANGLE);

        map.clear();
        map.put("name", "Geodetic East axis");
        CoordinateSystemAxis eastAxis =
                csFactory.createCoordinateSystemAxis(map, "E", AxisDirection.EAST, NonSI.DEGREE_ANGLE);

        map.clear();
        map.put("name", "Geodetic Height axis");
        CoordinateSystemAxis heightAxis = csFactory.createCoordinateSystemAxis(map, "Up", AxisDirection.UP, SI.METRE);

        map.clear();
        map.put("name", "<long>,<lat> Airy 1830 geodetic");
        EllipsoidalCS airyCS = csFactory.createEllipsoidalCS(map, eastAxis, northAxis, heightAxis);

        // finally create the source geographic CRS
        CoordinateReferenceSystem airyCRS = crsFactory.createGeographicCRS(map, wgs84Datum, airyCS);

        // createCRSByHand3 end

        // TODO crs.toWKT() throws exceptions here (.toString() works)
        System.out.println("Geographic CRS: " + airyCRS.toString());

        System.out.println("Identified CRS object:");
        printIdentifierStuff(airyCRS);
        System.out.println("Identified Datum object:");
        printIdentifierStuff(((GeographicCRS) airyCRS).getDatum());

        // you could now use these two CRS's to create a transform between them
        // as done below in createMathTransformBetweenCRSs(). The transform can
        // be used to convert points from lat,long to geocentric x,y,z.
        System.out.println("------------------------------------------");
    }

    /** Print out information about an identified object */
    // START SNIPPET: identifiedObject
    void printIdentifierStuff(IdentifiedObject identObj) {
        System.out.println("  getName().getCode() - " + identObj.getName().getCode());
        System.out.println("  getName().getAuthority() - " + identObj.getName().getAuthority());
        System.out.println("  getRemarks() - " + identObj.getRemarks());
        System.out.println("  getAliases():");
        Iterator<GenericName> aliases = identObj.getAlias().iterator();
        if (!aliases.hasNext()) {
            System.out.println("    no aliases");
        } else {
            for (int i = 0; aliases.hasNext(); i++) {
                System.out.println("    alias(" + i + "): " + aliases.next());
            }
        }

        System.out.println("  getIdentifiers():");
        // Identifier[]
        Iterator<? extends Identifier> idents = identObj.getIdentifiers().iterator();
        if (!idents.hasNext()) {
            System.out.println("    no extra identifiers");
        } else {
            for (int i = 0; idents.hasNext(); i++) {
                Identifier ident = idents.next();
                System.out.println("    identifier(" + i + ").getCode() - " + ident.getCode());
                System.out.println("    identifier(" + i + ").getAuthority() - " + ident.getAuthority());
            }
        }
    }

    // END SNIPPET: identifiedObject

    public void distance() throws Exception {
        Coordinate start = null;
        Coordinate end = null;
        CoordinateReferenceSystem crs = null;
        // distance start

        // the following code is based on JTS.orthodromicDistance( start, end, crs )
        GeodeticCalculator gc = new GeodeticCalculator(crs);
        gc.setStartingPosition(JTS.toDirectPosition(start, crs));
        gc.setDestinationPosition(JTS.toDirectPosition(end, crs));

        double distance = gc.getOrthodromicDistance();

        int totalmeters = (int) distance;
        int km = totalmeters / 1000;
        int meters = totalmeters - km * 1000;
        float remaining_cm = (float) (distance - totalmeters) * 10000;
        remaining_cm = Math.round(remaining_cm);
        float cm = remaining_cm / 100;

        System.out.println("Distance = " + km + "km " + meters + "m " + cm + "cm");
        // distance end
        // angle start
        double angle = gc.getAzimuth();

        System.out.println("Angle = " + angle);
        // angle end
    }

    public void movePoint() {
        // movePoint start
        GeodeticCalculator calc = new GeodeticCalculator();
        // mind, this is lon/lat
        calc.setStartingGeographicPoint(45.4644, 9.1908);
        calc.setDirection(90 /* azimuth */, 200 /* distance */);
        Point2D dest = calc.getDestinationGeographicPoint();
        System.out.println("Longitude: " + dest.getX() + " Latitude: " + dest.getY());
        // movePoint end
    }

    public void toWKT() throws Exception {
        // toWKT start
        CoordinateReferenceSystem crs = CRS.decode("EPSG:32735");
        String wkt = crs.toWKT();
        System.out.println("wkt for EPSG:32735");
        System.out.println(wkt);
        // toWKT end
    }

    @SuppressWarnings("unused")
    public void toWKTFormat() throws Exception {
        // toWKTFormat start
        CoordinateReferenceSystem crs = CRS.decode("EPSG:32735");
        Formattable f = (Formattable) CRS.decode("EPSG:32735", true);
        String wkt = f.toWKT(Citations.ESRI, 2); // use 0 indent for single line

        System.out.println("wkt for EPSG:32735 (ESRI)");
        System.out.println(wkt);
        // toWKTFormat end
    }

    public static void main(String[] args) {
        new ReferencingExamples();
    }
}
