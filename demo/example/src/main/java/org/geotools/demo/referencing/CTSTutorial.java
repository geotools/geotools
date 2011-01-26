/*
 *    GeoTools - The Open Source Java GIS Tookit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
/*
 * This tutorial code was developed and tested with the geotools 2.2.x svn.
 * If using geotools 2.1, note that referenging.jar is not needed (it was part of main.jar)
 * and some geotools referencing classes were renamed to Default* in geotools 2.2.x.
 * 
 * Geotools dependancies:
 *     main-2.2.x.jar
 *     referencing-2.2.x.jar
 *     epsg-wkt-2.2.x.jar or epsg-hsql-2.2.x.jar or epsg-access-2.2.x
 * 
 * Other dependancies:
 *     geoapi-2.0.jar
 *     units-0.01.jar
 *     vecmath-1.3.jar
 *     hsqldb-1.8.0.1.jar (if using epsg-hsql-2.2.x.jar)
 */
package org.geotools.demo.referencing;

//J2SE dependancies
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.measure.unit.NonSI;
import javax.measure.unit.SI;

import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.datum.BursaWolfParameters;
import org.geotools.referencing.datum.DefaultGeodeticDatum;
import org.geotools.referencing.operation.DefiningConversion;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.metadata.Identifier;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CSFactory;
import org.opengis.referencing.cs.CartesianCS;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.referencing.cs.EllipsoidalCS;
import org.opengis.referencing.datum.DatumFactory;
import org.opengis.referencing.datum.Ellipsoid;
import org.opengis.referencing.datum.GeodeticDatum;
import org.opengis.referencing.datum.PrimeMeridian;
import org.opengis.referencing.operation.Conversion;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.CoordinateOperationAuthorityFactory;
import org.opengis.referencing.operation.CoordinateOperationFactory;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransformFactory;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.GenericName;

/**
 *
 * Code for the geotools coordinate transformation services tutorial:
 * http://docs.codehaus.org/display/GEOTOOLS/Coordinate+Transformation+Services+for+Geotools+2.1
 *
 * The following code is made up of many, short methods used for discussion in the tutorial,
 * and does not create a coherent program. 
 * 
 * These examples cover the following topics:
 * <ul>
 *   <li>creating coordinate reference systems (CRS) by hand</li>
 *   <li>creating CRS's from well known text (WKT) strings</li>
 *   <li>creating CRS's from authority codes </li>
 *   <li>creating math transforms between CRS's </li>
 *   <li>creating math transforms by hand </li>
 *   <li> </li> 
 * </ul>
 *
 * START SNIPPET and END SNIPPET comments are used by the wiki to display code snippets from svn.
 * Factory creation is repeated below so that it shows up in the tutorial code snippets.
 * 
 * @source $URL$
 * @version $Id$
 * @author Rueben Schulz
 *
 * TODO using the auto crs factory
 */
public class CTSTutorial {
		
	/** CRS stored for latter use in createMathTransformBetweenCRSs() */
	private CoordinateReferenceSystem nad27CRS = null;
	/** CRS stored for latter use in createMathTransformBetweenCRSs() */
	private CoordinateReferenceSystem utm10NCRS = null;
	
	CTSTutorial() {
        try {
            creatCRSFromWKT();
            createFromEPSGCode();
            createCRSByHand1();
            createCRSByHand2();
            //createCRSByHand3();
            createMathTransformBetweenCRSs();
            //transformUsingCRSUtility();
            createAndUseMathTransform();
        	hintExample();
        	//createTransformFromAuthorityCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
        
        
    /**
     * A method with some examples of premade static objects. 
     */
    void premadeObjects() {
        // START SNIPPET: premadeObjects
        GeographicCRS geoCRS = org.geotools.referencing.crs.DefaultGeographicCRS.WGS84;
        GeodeticDatum wgs84Datum = org.geotools.referencing.datum.DefaultGeodeticDatum.WGS84;
        PrimeMeridian greenwichMeridian = org.geotools.referencing.datum.DefaultPrimeMeridian.GREENWICH;
        CartesianCS cartCS = org.geotools.referencing.cs.DefaultCartesianCS.GENERIC_2D;
        CoordinateSystemAxis latAxis = org.geotools.referencing.cs.DefaultCoordinateSystemAxis.GEODETIC_LATITUDE;
        // END SNIPPET: premadeObjects
    }
	
	/**
	 * An example of creating a CRS from a WKT string. Additonal examples of WKT strings
	 * can be found in
     * http://svn.geotools.org/geotools/trunk/gt/module/referencing/test/org/geotools/referencing/test-data/
	 * 
	 * TODO Brief description of what a CRS is (and what it is composed of)
	 * 
	 * @throws Exception
	 */
	void creatCRSFromWKT() throws Exception {
		System.out.println("------------------------------------------"); 
		System.out.println("Creating a CRS from a WKT string:");
        // START SNIPPET: crsFromWKT
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
        // END SNIPPET: crsFromWKT
		System.out.println("  CRS: " + crs.toWKT());
        System.out.println("Identified CRS object:");
		printIdentifierStuff(crs);
        System.out.println("Identified Datum object:");
        printIdentifierStuff(((ProjectedCRS)crs).getDatum());
		System.out.println("------------------------------------------"); 
	}

	/**
	 * Creates a CRS from an EPSG code. There are a few different EPSG authority
	 * factories in geotools that do roughly the same thing:
	 * 
	 * <ul>
	 * <li>gt2-epsg-access.jar is backed by the official EPSG MS Access
	 * database (only works on MS Windows, therefore I have not shown how to
	 * configure it here).</li>
	 * <li>gt2-epsg-hsql.jar provides an embeded hsql database created from the
	 * EPSG SQL scripts. This contains the same information as the MS Arcess
	 * database.</li>
	 * <li>other factories allow the EPSG information to be in an external
	 * database (postgresql, mysql, oracle)</li>
	 * <li>gt2-epsg-wkt.jar is a simple properties file with WKT descriptions
	 * for EPSG defined CRS codes. This file does not derive directly from the
	 * official EPSG database, so its should be used with caution. It provides a
	 * very simple method of creating a new authority factory and named objects.</li>
	 * </ul>
	 * 
	 * The specific authority factory returned by getCRSAuthorityFactory is
	 * dependent on the different factories on your classpath (ie WKT or Access
	 * or HSQL) and the hints you provide. By default the "better" authority
	 * factory should be used if more than one is available.
	 * 
	 * TODO check on the use of hints 
	 * TODO expand on how to use EPSG data in a
	 * postgres db (this may be a 2.2 feature, but FactoryUsingANSISQL may work)
	 * 
	 */
	void createFromEPSGCode() throws Exception {
		System.out.println("------------------------------------------");
		System.out.println("Creating a CRS from an authority factory:");
		// START SNIPPET: crsFromCode
		String code = "26910";
		CoordinateReferenceSystem crs = ReferencingFactoryFinder.getCRSAuthorityFactory(
				"EPSG", null).createCoordinateReferenceSystem(code);
		// END SNIPPET: crsFromCode
		System.out.println("  CRS: " + crs.toWKT());
		System.out.println("Identified CRS object:");
		printIdentifierStuff(crs);
		System.out.println("------------------------------------------");
	}

	/**
	 * Creates a WGS 84/UTM Zone 10N CRS mostly (uses some premade objects) by
	 * hand. Uses the higher level FactoryGroup instead of the lower level
	 * MathTransformFactory (commented out).
	 * 
	 * @throws Exception
	 */
	void createCRSByHand1() throws Exception {
		System.out.println("------------------------------------------"); 
		System.out.println("Creating a CRS by hand:");
//		 START SNIPPET: UTM10NcrsByHand
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
//		 END SNIPPET: UTM10NcrsByHand
		
		//parameters.parameter("semi_major").setValue(((GeodeticDatum)geoCRS.getDatum()).getEllipsoid().getSemiMajorAxis());
		//parameters.parameter("semi_minor").setValue(((GeodeticDatum)geoCRS.getDatum()).getEllipsoid().getSemiMinorAxis());

		//MathTransform trans = mtFactory.createParameterizedTransform(parameters);
		//ProjectedCRS projCRS = crsFactory.createProjectedCRS(
		//	Collections.singletonMap("name", "WGS 84 / UTM Zone 12N"), 
		//    new org.geotools.referencing.operation.OperationMethod(trans),
		//    geoCRS, trans, cartCS);
		System.out.println("  Projected CRS: " + projCRS.toWKT());
		System.out.println("------------------------------------------"); 
        
		// save for later use in createMathTransformBetweenCRSs()
		this.utm10NCRS = projCRS;
	}
        
    /**
	 * Creates a NAD 27 geographic CRS. Notice that the datum factory
	 * automatically adds aliase names to the datum (because "North American
	 * Datum 1927" has an entry in
	 * http://svn.geotools.org/geotools/trunk/gt/module/referencing/src/org/geotools/referencing/factory/DatumAliasesTable.txt ).
	 * Also notice that toWGS84 information (used in a datum transform) was
	 * also added to the datum.
	 */
	void createCRSByHand2() throws Exception {
		System.out.println("------------------------------------------");
		System.out.println("Creating a CRS by hand:");
		// START SNIPPET: nad27crsByHand
		CRSFactory crsFactory = ReferencingFactoryFinder.getCRSFactory(null);
		DatumFactory datumFactory = ReferencingFactoryFinder.getDatumFactory(null);
		CSFactory csFactory = ReferencingFactoryFinder.getCSFactory(null);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "Clarke 1866");

		Ellipsoid clark1866ellipse = datumFactory.createFlattenedSphere(map,
				6378206.4, 294.978698213901, SI.METER);

		PrimeMeridian greenwichMeridian = org.geotools.referencing.datum.DefaultPrimeMeridian.GREENWICH;

		final BursaWolfParameters toWGS84 = new BursaWolfParameters(
				DefaultGeodeticDatum.WGS84);
		toWGS84.dx = -3.0;
		toWGS84.dy = 142;
		toWGS84.dz = 183;

		map.clear();
		map.put("name", "North American Datum 1927");
		map.put(DefaultGeodeticDatum.BURSA_WOLF_KEY, toWGS84);

		GeodeticDatum clark1866datum = datumFactory.createGeodeticDatum(map,
				clark1866ellipse, greenwichMeridian);
		System.out.println(clark1866datum.toWKT());
		// notice all of the lovely datum aliases (used to determine if two
		// datums are the same)
		System.out.println("Identified Datum object:");
		printIdentifierStuff(clark1866datum);

		map.clear();
		map.put("name", "<lat>, <long>");
		CoordinateSystemAxis latAxis = org.geotools.referencing.cs.DefaultCoordinateSystemAxis.GEODETIC_LATITUDE;
		CoordinateSystemAxis longAxis = org.geotools.referencing.cs.DefaultCoordinateSystemAxis.GEODETIC_LONGITUDE;
		EllipsoidalCS ellipsCS = csFactory.createEllipsoidalCS(map, latAxis,
				longAxis);

		map.clear();
		map.put("name", "NAD 27");
		map.put("authority", "9999");
		// TODO add an authority code here (should be an identifier)
		GeographicCRS nad27CRS = crsFactory.createGeographicCRS(map, clark1866datum, ellipsCS);
		System.out.println(nad27CRS.toWKT());
		// END SNIPPET: nad27crsByHand
		System.out.println("Identified CRS object:");
		printIdentifierStuff(nad27CRS);

		System.out.println("------------------------------------------");
		
		// save for latter use in createMathTransformBetweenCRSs()
		this.nad27CRS = nad27CRS;
	}
	
	/**
	 * Creates two coordinate reference system by hand without using any of the
	 * GT2 APIs (except FactoryFinder to get things started). It does not use
	 * any of the static objects available in geotools implementations. The
	 * following example creates a CRS to represent the Airy 1830 ellipsoid with
	 * the incoming data in the order of (long,lat,height) and a geocentric CRS
	 * with (x,y,z) axises.
	 * 
	 * TODO the Airy CRS described below is actually wgs84, FIX this.
	 * 
	 * @throws FactoryException
	 */
	void createCRSByHand3() throws FactoryException {
                System.out.println("------------------------------------------"); 
		System.out.println("Creating two CRSs by hand:");
		CRSFactory crsFactory = ReferencingFactoryFinder.getCRSFactory(null);
		DatumFactory               datumFactory     = ReferencingFactoryFinder.getDatumFactory(null);
		CSFactory                  csFactory        = ReferencingFactoryFinder.getCSFactory(null);
		Map<String, Object> map = new HashMap<String, Object>();
                
		//
                // Create a datum used for each CRS
                //
		map.clear();
		map.put("name", "Greenwich Meridian");
		PrimeMeridian greenwichMeridian =
			datumFactory.createPrimeMeridian(map, 0, NonSI.DEGREE_ANGLE);
		
		map.clear();
		map.put("name", "WGS 84 Ellipsoid Datum");
		Ellipsoid wgs84Ellipsoid = 
			datumFactory.createFlattenedSphere(map, 6378137, 298.257223563, SI.METER);
		
		map.clear();
		map.put("name", "WGS84 Height Datum");
		GeodeticDatum wgs84Datum = 
			datumFactory.createGeodeticDatum(map, wgs84Ellipsoid, greenwichMeridian);
		
        //
        //Create a geocentric CRS
        //
		// Create a collection of axes for the coordinate system.
		map.clear();
		map.put("name", "Cartesian X axis");
		CoordinateSystemAxis xAxis =
			csFactory.createCoordinateSystemAxis(map, "X", AxisDirection.GEOCENTRIC_X, SI.METER);
		
		map.clear();
		map.put("name", "Cartesian Y axis");
		CoordinateSystemAxis yAxis =
			csFactory.createCoordinateSystemAxis(map, "Y", AxisDirection.GEOCENTRIC_Y, SI.METER);
		
		map.clear();
		map.put("name", "Cartesian Z axis");
		CoordinateSystemAxis zAxis =
			csFactory.createCoordinateSystemAxis(map, "Z", AxisDirection.GEOCENTRIC_Z, SI.METER);
		
                map.clear();
		map.put("name", "Rendered Cartesian CS");
		CartesianCS worldCS = csFactory.createCartesianCS(map, xAxis, yAxis, zAxis);
                
		// Now, the geocentric coordinate reference system that we'd use for output - eg to a 3D renderer
		map.clear();
		map.put("name", "Output Cartesian CS");
		CoordinateReferenceSystem geocentricCRS = crsFactory.createGeocentricCRS(map, wgs84Datum, worldCS);
		System.out.println("Geocentric CRS: " + geocentricCRS.toWKT());
		
        //
		// Create a geograyhic CRS for the Airy 1830 ellipsoid
		//map.clear();
		//map.put("name", "Airy 1830");
		//Ellipsoid airyEllipse = 
		//	datumFactory.createFlattenedSphere(map, 6377563.396, 299.3249646, SI.METER);
		
                map.clear();
		map.put("name", "Geodetic North axis");
		CoordinateSystemAxis northAxis = csFactory.createCoordinateSystemAxis(map, "N",AxisDirection.NORTH, NonSI.DEGREE_ANGLE);
		
		map.clear();
		map.put("name", "Geodetic East axis");
		CoordinateSystemAxis eastAxis = csFactory.createCoordinateSystemAxis(map, "E", AxisDirection.EAST, NonSI.DEGREE_ANGLE);
		
		map.clear();
		map.put("name", "Geodetic Height axis");
		CoordinateSystemAxis heightAxis = csFactory.createCoordinateSystemAxis(map, "Up", AxisDirection.UP, SI.METER);
                
		map.clear();
		map.put("name", "<long>,<lat> Airy 1830 geodetic");
		EllipsoidalCS airyCS = csFactory.createEllipsoidalCS(map, eastAxis, northAxis, heightAxis);
	
        // finally create the source geographic CRS
		CoordinateReferenceSystem airyCRS = crsFactory.createGeographicCRS(map, wgs84Datum, airyCS);
//TODO crs.toWKT() throws exceptions here (.toString() works)
		System.out.println("Geographic CRS: " + airyCRS.toString());
                
        System.out.println("Identified CRS object:");
        printIdentifierStuff(airyCRS);
        System.out.println("Identified Datum object:");
        printIdentifierStuff(((GeographicCRS)airyCRS).getDatum());
        
        // you could now use these two CRS's to create a transform between them
        // as done below in createMathTransformBetweenCRSs(). The transform can 
        // be used to convert points from lat,long to geocentric x,y,z.
        System.out.println("------------------------------------------");      
	}
	
	/**
	 * Creates a math transform between the CRS's created in createCRSByHand2() 
	 * and createCRSByHand1(). The resulting transformation is a concatenation 
	 * of the following transforms:
	 * 
	 * <ul>
	 *   <li>Affine - to switch axis order from (latitude,longitude) to (longitude,latitude)</li>
	 *   <li>Molodenski - to preform a datum shift between NAD27 and WGS84, 
	 *                    using the NAD 27 CRS BursaWolfParameters parameters </li>
	 *   <li>Transverse Mercator - to convert from geographic to projected UTM coordinates</li>
	 * </ul>
	 * 
	 * @throws Exception
	 */
	void createMathTransformBetweenCRSs() throws Exception {
		System.out.println("------------------------------------------");
		System.out.println("Creating a math transform between two CRSs:");
		
// 		START SNIPPET: mathTransformBetweenCRSs
		CoordinateOperationFactory coFactory = ReferencingFactoryFinder
				.getCoordinateOperationFactory(null);

		// Nad 27 geographic (lat,long)
		CoordinateReferenceSystem sourceCRS = nad27CRS;
		// UTM Zone 10N, WGS 84 (x,y)
		CoordinateReferenceSystem targetCRS = utm10NCRS;

		CoordinateOperation op = coFactory.createOperation(sourceCRS, targetCRS);
		MathTransform trans = op.getMathTransform();
		System.out.println("Math Transform: " + trans.toWKT());

		// transform some points
		DirectPosition pt = new GeneralDirectPosition(45.1, -120.0);
		System.out.println("Input point: " + pt);
		pt = trans.transform(pt, null);
		System.out.println("Output point: " + pt);
		System.out.println("Inverse of output point: "
				+ trans.inverse().transform(pt, null));
//		END SNIPPET: mathTransformBetweenCRSs
		System.out.println("------------------------------------------");
	}
	
	/**
	 * Uses the CRS utility class to create two CRSs and a tranformation between them.
	 */
	void transformUsingCRSUtility () throws Exception {
		System.out.println("------------------------------------------");
		System.out.println("Using the CRS utility to create a math transform between two CRSs:");
		
		CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:4978"); //WGS84 geocentrique
		CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:4979"); //WGS84 geographique 3D
		System.out.println("Source CRS: " + sourceCRS.getName().getCode());
		System.out.println("Target CRS: " + targetCRS.getName().getCode());
		
		MathTransform mathTransform =CRS.findMathTransform( sourceCRS, targetCRS );
		System.out.println("MT: " + mathTransform.toWKT());
	    DirectPosition pt = new GeneralDirectPosition(4089881.3, -4874130.7, 441946.6); //x,y,z
	    System.out.println("Input point: " + pt);
	    pt = mathTransform.transform(pt, null);
	    System.out.println("Output point: " + pt); //longitude,latitude,height
	    
	    System.out.println("------------------------------------------");
	}
	
	
	/**
	 * Creates a low level math transform by hand. This is essentially just an equation (with some parameters)
	 * used to transform input to output points.
	 * 
	 * @throws TransformException
	 * @throws MismatchedDimensionException
	 */
	void createAndUseMathTransform() throws FactoryException, MismatchedDimensionException, TransformException {
		System.out.println("------------------------------------------"); 
		System.out.println("Creating a math transform by hand:");
// 		START SNIPPET: mathTransformByHand
		MathTransformFactory mtFactory = ReferencingFactoryFinder.getMathTransformFactory(null);
		
		ParameterValueGroup params = mtFactory.getDefaultParameters("Hotine_Oblique_Mercator");
		params.parameter("semi_major").setValue(6377298.556);
		params.parameter("semi_minor").setValue(6356097.5503009);
		params.parameter("longitude_of_center").setValue(115.0);
		params.parameter("latitude_of_center").setValue(4.0);
		params.parameter("azimuth").setValue(53.315820472222200);
		params.parameter("rectified_grid_angle").setValue(53.130102361111100);
		params.parameter("scale_factor").setValue(0.99984);
		params.parameter("false_easting").setValue(0.0);
		params.parameter("false_northing").setValue(0.0);
		MathTransform trans = mtFactory.createParameterizedTransform(params);
		System.out.println("Math Transform: " + trans.toWKT());
		
		//transform some points
		DirectPosition pt = new GeneralDirectPosition(120.0,6.0);        
		System.out.println("Input point: " + pt);
        pt = trans.transform(pt, null); 
        System.out.println("Output point: " + pt);
        System.out.println("Inverse of output point: " + trans.inverse().transform(pt,null));
// 		END SNIPPET: mathTransformByHand
        System.out.println("------------------------------------------"); 
	}
	
    /*
     * An example of using a hint to turn off datum shifts
     */
	void hintExample() throws Exception {
		System.out.println("------------------------------------------"); 
		System.out.println("Using hints to create a transform without a datum shift:");
        // Source CRS: Belge 1972 / Belge Lambert 72
        String sourceCode = "31300";
        // Target CRS: NTF (Paris) / Nord France
        String targetCode = "27591";
        Hints hints = new Hints(Hints.LENIENT_DATUM_SHIFT, Boolean.TRUE);
        // This instructs Geotools to be tolerant to missing Bursa-Wolf parameters. 
        // However, you may get one kilometer error in such case if a datum shift 
        // is applied without such parameters.
        CRSAuthorityFactory crsFactory = ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG", hints);
        CoordinateReferenceSystem sourceCRS = crsFactory.createCoordinateReferenceSystem(sourceCode);
        CoordinateReferenceSystem targetCRS = crsFactory.createCoordinateReferenceSystem(targetCode);

        CoordinateOperationFactory opFactory =  ReferencingFactoryFinder.getCoordinateOperationFactory(hints);
        MathTransform mt = opFactory.createOperation(sourceCRS, targetCRS).getMathTransform();
        System.out.println("Math Transform: " + mt.toWKT());
        System.out.println("------------------------------------------"); 
	}
      
	/**
	 * 
	 * @since 2.2
	 * @throws Exception
	 */
    //also want to play with  the new operation authority factory (especially the nad shift case)
    void createOperationFromAuthorityCode() throws Exception {
        //This is only on head, not in geotools 2.1rc
        CoordinateOperationAuthorityFactory coaf = ReferencingFactoryFinder.getCoordinateOperationAuthorityFactory("EPSG",null);
        CoordinateOperation co = coaf.createCoordinateOperation("");
//TODO find an operation code
    }
    
    /**
     * 
     * @since 2.2
     * @throws Exception
     */
    void createTransformFromAuthorityCode() throws Exception {
        //This is only on head, not in geotools 2.1rc
        CoordinateOperationAuthorityFactory coaf = ReferencingFactoryFinder.getCoordinateOperationAuthorityFactory("EPSG",null);
        Set<CoordinateOperation> coordOperations = coaf.createFromCoordinateReferenceSystemCodes("EPSG:4267","EPSG:4269");
//TODO it seems that no operations are being returned here, figure out why
        for (CoordinateOperation co : coordOperations) {
        	System.out.println("  " + co.toWKT());
        	
        }
    }

    /**
     * Print out information about an identified object
     */
//  START SNIPPET: identifiedObject
    void printIdentifierStuff(IdentifiedObject identObj) {
        System.out.println("  getName().getCode() - " + identObj.getName().getCode());
        System.out.println("  getName().getAuthority() - " + identObj.getName().getAuthority());
        System.out.println("  getRemarks() - " + identObj.getRemarks());
        System.out.println("  getAliases():");
        Iterator<GenericName> aliases = identObj.getAlias().iterator();
        if (! aliases.hasNext()) {
            System.out.println("    no aliases");
        } else {
            for (int i=0; aliases.hasNext(); i++) {
                System.out.println("    alias(" + i + "): " + (GenericName) aliases.next());
            }
        }
        
        System.out.println("  getIdentifiers():");
        //Identifier[]
        Iterator<? extends Identifier> idents = identObj.getIdentifiers().iterator();
        if (! idents.hasNext()) {
            System.out.println("    no extra identifiers");
        } else {
            for (int i=0; idents.hasNext(); i++) {
                Identifier ident = idents.next();
                System.out.println("    identifier(" + i + ").getCode() - " + ident.getCode());
                System.out.println("    identifier(" + i + ").getAuthority() - " + ident.getAuthority());
            }
        } 
    }
//  END SNIPPET: identifiedObject

	public static void main(String[] args) {
		new CTSTutorial();
	}
}
