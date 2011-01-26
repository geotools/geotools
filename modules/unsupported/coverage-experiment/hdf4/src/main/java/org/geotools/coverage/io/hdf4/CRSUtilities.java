/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.hdf4;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.measure.quantity.Length;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import org.geotools.coverage.io.util.Utilities;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.CRS;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.cs.DefaultCartesianCS;
import org.geotools.referencing.datum.DefaultEllipsoid;
import org.geotools.referencing.datum.DefaultGeodeticDatum;
import org.geotools.referencing.datum.DefaultPrimeMeridian;
import org.geotools.referencing.factory.ReferencingFactoryContainer;
import org.geotools.referencing.operation.DefaultMathTransformFactory;
import org.geotools.referencing.operation.matrix.GeneralMatrix;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransformFactory;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.opengis.referencing.operation.TransformException;

public class CRSUtilities {
	
	private final static Logger LOGGER = Logger.getLogger(CRSUtilities.class.toString());

    /** Caches a MathTransformFactory */
    private final static MathTransformFactory mtFactory = new DefaultMathTransformFactory();

    public static ReferenceIdentifier[] getIdentifiers(
            final String nameIdentifier) {
        if (nameIdentifier.equalsIgnoreCase("WGS84")) {
            final ReferenceIdentifier[] identifiers = {
                    new NamedIdentifier(Citations.OGC, "WGS84"),
                    new NamedIdentifier(Citations.ORACLE, "WGS 84"),
                    new NamedIdentifier(null, "WGS_84"),
                    new NamedIdentifier(null, "WGS 1984"),
                    new NamedIdentifier(Citations.EPSG, "WGS_1984"),
                    new NamedIdentifier(Citations.ESRI, "D_WGS_1984"),
                    new NamedIdentifier(Citations.EPSG,
                            "World Geodetic System 1984") };
            return identifiers;
        }
        // TODO: Handle mores
        return null;
    }

    private CRSUtilities() {

    }

    /**
     * Build a {@link DefaultGeodeticDatum} given a set of parameters.
     * 
     * @param name
     *                the datum name
     * @param equatorialRadius
     *                the equatorial radius parameter
     * @param inverseFlattening
     *                the inverse flattening parameter
     * @param unit
     *                the UoM
     * @return a properly built Datum.
     */
    public static DefaultGeodeticDatum getDefaultGeodeticDatum(
            final String name, final double equatorialRadius,
            final double inverseFlattening, Unit unit) {

        DefaultEllipsoid ellipsoid = DefaultEllipsoid.createFlattenedSphere(name, equatorialRadius, inverseFlattening, unit);
        final ReferenceIdentifier[] identifiers = CRSUtilities.getIdentifiers(name);
        // TODO: Should I change this behavior?
        if (identifiers == null)
            throw new IllegalArgumentException( "Reference Identifier not available");
        final Map<String, Object> properties = new HashMap<String, Object>(4);
        properties.put(DefaultGeodeticDatum.NAME_KEY, identifiers[0]);
        properties.put(DefaultGeodeticDatum.ALIAS_KEY, identifiers);
        DefaultGeodeticDatum datum = new DefaultGeodeticDatum(properties,ellipsoid, DefaultPrimeMeridian.GREENWICH);
        return datum;
    }

    /**
     * Simple utility method which allows to build a Mercator2SP Projected CRS
     * given the set of required parameters. It will be used by several Terascan
     * products.
     */
    @SuppressWarnings("deprecation")
	public static synchronized CoordinateReferenceSystem getMercator2SPProjectedCRS(
            final double standardParallel, final double centralMeridian,
            final double natOriginLat, GeographicCRS sourceCRS, Hints hints){
        CoordinateReferenceSystem projectedCRS = null;

        // //
        //
        // Creating a proper projected CRS
        //
        // //
        final ReferencingFactoryContainer fg = ReferencingFactoryContainer.instance(hints);
        ParameterValueGroup params;
        try {
            params = mtFactory.getDefaultParameters("Mercator_2SP");

            params.parameter("standard_parallel_1").setValue(standardParallel);
            params.parameter("central_meridian").setValue(centralMeridian);
            params.parameter("false_northing").setValue(0);
            params.parameter("false_easting").setValue(0);
            params.parameter("latitude_of_origin").setValue(natOriginLat);

            // //
            //
            // Setting the CRS
            //
            // //	
            final Map<String, String> props = new HashMap<String, String>();
            props.put("name", "Mercator CRS");
            projectedCRS = fg.createProjectedCRS(props, sourceCRS, null, params, DefaultCartesianCS.PROJECTED);
        } catch (FactoryException e) {
            if (LOGGER.isLoggable(Level.FINE))
            	LOGGER.log(Level.FINE,e.getLocalizedMessage());
        }
        return projectedCRS;
    }
    
    /**
     * Simple utility method which allows to build a Mercator1SP Projected CRS
     * given the set of required parameters. It will be used by several Terascan
     * products.
     */
    @SuppressWarnings("deprecation")
    public static synchronized CoordinateReferenceSystem getMercator1SPProjectedCRS(
            final double centralMeridian, final double latitudeOfOrigin,
                        final double falseEasting, final double falseNorthing,
                        final double scaleFactor, GeographicCRS sourceCRS, Hints hints){
        CoordinateReferenceSystem projectedCRS = null;

        // //
        //
        // Creating a proper projected CRS
        //
        // //
        final ReferencingFactoryContainer fg = ReferencingFactoryContainer.instance(hints);
        ParameterValueGroup params;
        try {
            params = mtFactory.getDefaultParameters("Mercator_1SP");

            params.parameter("central_meridian").setValue(centralMeridian);
            params.parameter("latitude_of_origin").setValue(latitudeOfOrigin);
            params.parameter("false_northing").setValue(falseNorthing);
            params.parameter("false_easting").setValue(falseEasting);
            params.parameter("scale_factor").setValue(scaleFactor);

            // //
            //
            // Setting the CRS
            //
            // //
            final Map<String, String> props = new HashMap<String, String>();
            props.put("name", "Mercator CRS");
            projectedCRS = fg.createProjectedCRS(props, sourceCRS, null, params, DefaultCartesianCS.PROJECTED);
        } catch (FactoryException e) {
        	 if (LOGGER.isLoggable(Level.FINE))
             	LOGGER.log(Level.FINE,e.getLocalizedMessage());
        }
        return projectedCRS;
    }
    
    
    public static GeographicCRS getBaseCRS(final double firstParameter,
            final double secondParameter, final boolean isSecondParameterInverseFlattening){
        final DefaultGeodeticDatum datum;
        datum = CRSUtilities.getDefaultGeodeticDatum("WGS84", firstParameter, secondParameter, SI.METER, isSecondParameterInverseFlattening);
        final GeographicCRS sourceCRS = new DefaultGeographicCRS("WGS-84", datum, DefaultGeographicCRS.WGS84.getCoordinateSystem());
        return sourceCRS;
    }
    
    public static AffineTransform createAffineTransform(final String projToImageTransformation){
    	AffineTransform at = null;
    	if (Utilities.ensureValidString(projToImageTransformation)) {
			final String[] coefficients = projToImageTransformation.split(",");
			if (coefficients.length == 6) {
				final double[] geotCoeff = new double[6];
				for (int i = 0; i < 6; i++) {
					geotCoeff[i] = Double.parseDouble(coefficients[i]);
				}
				final GeneralMatrix gm = new GeneralMatrix(3);

				// //
				//
				// The proj_to_image_affine is defined as follows:
				//
				// Image row = affine[0]*y + affine[2]*x + affine[4]
				// Image col = affine[1]+y + affine[3]*x + affine[5]
				//
				// As stated in the earth_location_notes, Y and X
				// are kilometers north and east of projection
				// origin
				//
				// //
				gm.setElement(0, 0, geotCoeff[3] / 1000);
				gm.setElement(1, 1, geotCoeff[0] / 1000);
				gm.setElement(0, 1, geotCoeff[2] / 1000);
				gm.setElement(1, 0, geotCoeff[1] / 1000);
				gm.setElement(0, 2, geotCoeff[5]);
				gm.setElement(1, 2, geotCoeff[4]);

				try {
					final MathTransform inverseTransformation = ProjectiveTransform.create(gm).inverse();
					final AffineTransform tempTransform = new AffineTransform((AffineTransform) inverseTransformation);
					at = tempTransform;
				} catch (NoninvertibleTransformException e1) {
					if (LOGGER.isLoggable(Level.WARNING))
						LOGGER.log(Level.WARNING,"unable to invert the projection to image affine transformation");
				}
			}
    	}
    	return at;
    }
    
    public static GeneralEnvelope buildEnvelope(final AffineTransform tempTransform, final Rectangle gridRange){
		GeneralEnvelope envelope = null;
		try {
//			final AffineTransform tempTransform = createAffineTransform(projToImageTransformation);
			final Envelope gridEnvelope = new GeneralEnvelope(gridRange);
			envelope = CRS.transform(ProjectiveTransform.create(tempTransform), gridEnvelope);
//			final double tr = -PixelTranslation.getPixelTranslation(PixelInCell.CELL_CORNER);
//			tempTransform.translate(tr, tr);

		} catch (NoninvertibleTransformException e1) {
			if (LOGGER.isLoggable(Level.WARNING))
				LOGGER.log(Level.WARNING,"unable to invert the projection to image affine transformation");
		} catch (TransformException e) {
			if (LOGGER.isLoggable(Level.WARNING))
				LOGGER.log(Level.WARNING,"unable to set the envelope using the projection to image affine transformation");
		}
		return envelope;
	}
    
    public static GeneralEnvelope buildEnvelope(CoordinateReferenceSystem sourceCRS, CoordinateReferenceSystem destCRS,
    		final String lowerLeftCorner, final String upperRightCorner){
    	GeneralEnvelope envelope = null;
    	try{ 
    		final MathTransform WGS84toPROJCRS = CRS.findMathTransform(sourceCRS, destCRS, true);
	    	 final String latlon1[] = lowerLeftCorner.split(",");
	    	 final String latlon2[] = upperRightCorner.split(",");
	
			 double lat1 = Double.parseDouble(latlon1[0]);
			 double lon1 = Double.parseDouble(latlon1[1]);
			 double lat2 = Double.parseDouble(latlon2[0]);
			 double lon2 = Double.parseDouble(latlon2[1]);
		
			 if (lon1 > lon2) {
			     double temp = lon2;
			     lon2 = lon1;
			     lon1 = temp;
			 }
		
			 if (lat1 > lat2) {
			     double temp = lat2;
			     lat2 = lat1;
			     lat1 = temp;
			 }
			 GeneralEnvelope tempEnvelope = new GeneralEnvelope(new double[] { lon1, lat1 }, new double[] { lon2, lat2 });
			 tempEnvelope.setCoordinateReferenceSystem(sourceCRS);
			 envelope = CRS.transform(WGS84toPROJCRS,tempEnvelope);
			 envelope.setCoordinateReferenceSystem(destCRS);
		} catch (TransformException e) {
			if (LOGGER.isLoggable(Level.WARNING))
				LOGGER.log(Level.WARNING,"unable to set the envelope using the projection to image affine transformation");
		} catch (FactoryException e) {
			if (LOGGER.isLoggable(Level.WARNING))
				LOGGER.log(Level.WARNING,"unable to set the envelope using the projection to image affine transformation");
		}
		return envelope;
    }
    
    public static DefaultGeodeticDatum getDefaultGeodeticDatum(final String name, final double firstParameter, 
    		final double secondParameter, Unit<Length> unit, final boolean isSecondParameterInverseFlattening) {
        final DefaultEllipsoid ellipsoid; 
        if (isSecondParameterInverseFlattening){
            ellipsoid = DefaultEllipsoid.createFlattenedSphere(name, firstParameter, secondParameter, unit);
        }
        else 
            ellipsoid = DefaultEllipsoid.createEllipsoid(name, firstParameter, secondParameter, unit);
        final ReferenceIdentifier[] identifiers = CRSUtilities.getIdentifiers(name);
        if (identifiers == null)
            throw new IllegalArgumentException("Reference Identifier not available");
        final Map<String, Object> properties = new HashMap<String, Object>(4);
        properties.put(DefaultGeodeticDatum.NAME_KEY, identifiers[0]);
        properties.put(DefaultGeodeticDatum.ALIAS_KEY, identifiers);
        DefaultGeodeticDatum datum = new DefaultGeodeticDatum(properties, ellipsoid, DefaultPrimeMeridian.GREENWICH);
        return datum;
    }

}
