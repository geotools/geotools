/*
 *    GeoTools - The Open Source Java GIS Tookit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.demo.referencing;

// J2SE and JAI dependencies
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Map;

import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.operation.DefiningConversion;
import org.opengis.geometry.DirectPosition;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.cs.CSFactory;
import org.opengis.referencing.cs.CartesianCS;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.referencing.cs.EllipsoidalCS;
import org.opengis.referencing.datum.GeodeticDatum;
import org.opengis.referencing.operation.Conversion;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.CoordinateOperationFactory;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransformFactory;
import org.opengis.referencing.operation.TransformException;


/**
 * An example of application reading points from the standard input,  transforming
 * them and writting the result to the standard output. This class can be run from
 * the command-line using the following syntax:
 *
 * <blockquote><pre>
 * java TransformationConsole [classification]
 * </pre></blockquote>
 *
 * Where [classification] is the the classification name of the projection to perform.
 * The default value is "Mercator_1SP". The list of supported classification name is
 * available here:
 *
 *   http://docs.codehaus.org/display/GEOTOOLS/Coordinate+Transformation+Parameters
 *
 * To exit from the application, enter "exit".
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class TransformationConsole {
    /**
     * The program main entry point.
     *
     * @param  args Array of command-line arguments. This small demo accept only
     *              one argument: the classification name of the projection to
     *              perform.
     *
     * @throws IOException if an error occured while reading the input stream.
     * @throws FactoryException if a coordinate system can't be constructed.
     * @throws TransformException if a transform failed.
     */
    public static void main(String[] args) throws IOException, FactoryException, TransformException {
        /*
         * Check command-line arguments.
         */
        String classification;
        switch (args.length) {
            case  0: classification = "Mercator_1SP"; break;
            case  1: classification = args[0]; break;
            default: System.err.println("Expected 0 or 1 argument"); return;
        }
        /*
         * The factories to use for constructing coordinate systems. 
         */
        CRSFactory crsFactory = ReferencingFactoryFinder.getCRSFactory(null);
        CSFactory csFactory  = ReferencingFactoryFinder.getCSFactory(null);
        MathTransformFactory mtFactory = ReferencingFactoryFinder.getMathTransformFactory(null);
        /*
         * Construct the source CoordinateReferenceSystem. We will use a geographic coordinate
         * system,  i.e. one that use (latitude,longitude) coordinates.   Latitude values
         * are increasing north and longitude values area increasing east.  Angular units
         * are degrees and prime meridian is Greenwich.  Datum is WGS 84  (a commonly
         * used one for remote sensing data and GPS).      Note that the Geotools library
         * provides simpler ways to construct geographic coordinate systems using default
         * values for some arguments.  But we show here the complete way in order to show
         * the range of possibilities and to stay closer to the OpenGIS's specification.
         */
        CoordinateSystemAxis longAxis = org.geotools.referencing.cs.DefaultCoordinateSystemAxis.GEODETIC_LONGITUDE;
        CoordinateSystemAxis latAxis = org.geotools.referencing.cs.DefaultCoordinateSystemAxis.GEODETIC_LATITUDE;
        EllipsoidalCS ellipseCS = csFactory.createEllipsoidalCS(Collections.singletonMap("name", "Lat/Long"), 
            latAxis,longAxis);
        GeodeticDatum datum = org.geotools.referencing.datum.DefaultGeodeticDatum.WGS84;
        GeographicCRS sourceCRS = crsFactory.createGeographicCRS(Collections.singletonMap("name", "WGS 84"), 
            datum, ellipseCS);
        /*
         * Construct the target CoordinateReferenceSystem. We will use a projected coordinate
         * system, i.e. one that use linear (in metres) coordinates. We will use the
         * same ellipsoid than the source geographic coordinate system (i.e. WGS84).
         * Default parameters will be used for this projection, but false_easting and 
         * false_northing values could be set below.
         *
         * Note: The 'sourceCRS' argument below is the geographic coordinate system
         *       to base projection on. It is also the source coordinate system in
         *       this particular case, but this may not alway be the case.
         */
        ParameterValueGroup parameters = mtFactory.getDefaultParameters(classification);
        if (false) {
            // Set optional parameters here. This example set the false
            // easting and northing just for demonstration purpose.
            parameters.parameter("false_easting").setValue(1000.0);
            parameters.parameter("false_northing").setValue(1000.0);
            
        }
        CartesianCS cartCS = org.geotools.referencing.cs.DefaultCartesianCS.GENERIC_2D;
        Map<String, String> properties = Collections.singletonMap("name", classification);
        Conversion conversion = new DefiningConversion("Mercator", parameters);
    	ProjectedCRS targetCRS = crsFactory.createProjectedCRS(properties, sourceCRS, conversion,
            cartCS);
        
        /*
         * Now, we have built source and destination coordinate referenc systems ('sourceCRS'
         * and 'targetCRS'). Here are some observations about their relationships:
         *
         *   * We use the same ellipsoid (WGS 84) for both,  but it could as well be
         *     different. Using different ellipsoids would require a datum shift to 
         *     transform between the two datums.
         *
         *   * The axis order is inverted between the source (latitude,longitude)
         *     and the target (x,y).    This is up to the user to choose the axis
         *     order he want; Geotools should correctly swap them as needed. User
         *     could as well reverse axis orientation (e.g. make longitude values
         *     increasing West); Geotools should handle that correctly.
         *
         * Now, get the transformation.
         */
	CoordinateOperationFactory coFactory = ReferencingFactoryFinder.getCoordinateOperationFactory(null);	
        CoordinateOperation co = coFactory.createOperation(sourceCRS, targetCRS);

        /*
         * The CoordinateOperation object contains information about
         * the transformation. It does not actually perform the transform
         * operations on points. In order to transform points, we must get
         * the math transform.
         *
         * Because source and target coordinate reference systems are both two-dimensional,
         * this transform object will actually be an instance of MathTransform2D.
         * The MathTransform2D interface is a Geotools's extension that is not part
         * of the OpenGIS's specification. This class provides additional methods
         * for interoperability with Java2D. If the user want to use it, he have to
         * cast the transform to MathTransform2D.
         */
        MathTransform transform = co.getMathTransform();
        /*
         * Now, read lines from the standard input, transform them,
         * and write the result to the standard output. Note: Java
         * is not very good for console application.  See many bug
         * reports (e.g. http://developer.java.sun.com/developer/bugParade/bugs/4071281.html).
         */
        System.out.print("Projection classification is ");
        System.out.println(classification);
        System.out.println("Source CRS is:");
        System.out.println("    " + sourceCRS.toWKT());
        System.out.println("Target CRS is:");
        System.out.println("    " + targetCRS.toWKT());
        System.out.println("Enter (latitude longitude) coordinates separated by a space.");
        System.out.println("Enter \"exit\" to finish.");
        final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line; while ((line=in.readLine()) != null) {
            line = line.trim();
            if (line.equalsIgnoreCase("exit")) {
                break;
            }
            int split = line.indexOf(' ');
            if (split >= 0) {
                double latitude  = Double.parseDouble(line.substring(0, split));
                double longitude = Double.parseDouble(line.substring(   split));
                DirectPosition point = new GeneralDirectPosition(latitude,longitude);
                point = transform.transform(point, point);
                System.out.println(point);
            }
        }
    }
}
