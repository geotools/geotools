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
package org.geotools.renderer.lite;

import java.awt.BasicStroke;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Icon;

import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.GeodeticCalculator;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.renderer.style.GraphicStyle2D;
import org.geotools.renderer.style.IconStyle2D;
import org.geotools.renderer.style.LineStyle2D;
import org.geotools.renderer.style.Style2D;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.EngineeringCRS;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Class for holding utility functions that are common tasks for people using
 * the "StreamingRenderer/Renderer".
 * 
 * 
 * @author dblasby
 * @author Simone Giannecchini
 * @source $URL$
 */
public final class RendererUtilities {

	private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(RendererUtilities.class.getName());

    /**
     * Helber class for building affine transforms. We use one instance per thread,
     * in order to avoid the need for {@code synchronized} statements.
     */
    private static final ThreadLocal<GridToEnvelopeMapper> gridToEnvelopeMappers =
            new ThreadLocal<GridToEnvelopeMapper>() {
                @Override
                protected GridToEnvelopeMapper initialValue() {
                    final GridToEnvelopeMapper mapper = new GridToEnvelopeMapper();
                    mapper.setGridType(PixelInCell.CELL_CORNER);
                    return mapper;
                }
    };

	/**
	 * Utilities classes should not be instantiated.
	 * 
	 */
	private RendererUtilities() {
	};

	/**
	 * Sets up the affine transform <p/> ((Taken from the old LiteRenderer))
	 * 
	 * @param mapExtent
	 *            the map extent
	 * @param paintArea
	 *            the size of the rendering output area
	 * @return a transform that maps from real world coordinates to the screen
	 * @deprecated Uses the alternative based on <code>ReferencedEnvelope</code>
	 *             that doe not assume anything on axes order.
	 * 
	 */
	public static AffineTransform worldToScreenTransform(Envelope mapExtent,
			Rectangle paintArea) {
		double scaleX = paintArea.getWidth() / mapExtent.getWidth();
		double scaleY = paintArea.getHeight() / mapExtent.getHeight();

		double tx = -mapExtent.getMinX() * scaleX;
		double ty = (mapExtent.getMinY() * scaleY) + paintArea.getHeight();

		AffineTransform at = new AffineTransform(scaleX, 0.0d, 0.0d, -scaleY,
				tx, ty);
		AffineTransform originTranslation = AffineTransform
				.getTranslateInstance(paintArea.x, paintArea.y);
		originTranslation.concatenate(at);

		return originTranslation != null ? originTranslation : at;
	}

	/**
	 * Sets up the affine transform <p/>
	 * 
	 * NOTE It is worth to note that here we do not take into account the half a
	 * pixel translation stated by ogc for coverages bounds. One reason is that
	 * WMS 1.1.1 does not follow it!!!
	 * 
	 * @param mapExtent
	 *            the map extent
	 * @param paintArea
	 *            the size of the rendering output area
	 * @return a transform that maps from real world coordinates to the screen
	 */
	public static AffineTransform worldToScreenTransform(
			ReferencedEnvelope mapExtent, Rectangle paintArea) {

		// //
		//
		// Convert the JTS envelope and get the transform
		//
		// //
		final Envelope2D genvelope = new Envelope2D(mapExtent);

		// //
		//
		// Get the transform
		//
		// //
            final GridToEnvelopeMapper m = (GridToEnvelopeMapper) gridToEnvelopeMappers.get();
		try {
            m.setGridRange(new GridEnvelope2D(paintArea));
            m.setEnvelope(genvelope);
            return m.createAffineTransform().createInverse();
		} catch (MismatchedDimensionException e) {
			LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
			return null;
		} catch (NoninvertibleTransformException e) {
			LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
			return null;
		}

	}


    /**
     * Creates the map's bounding box in real world coordinates.
     * 
     * @param worldToScreen
     *            a transform which converts World coordinates to screen pixel coordinates. No
     *            assumptions are done on axis order as this is assumed to be pre-calculated. The
     *            affine transform may specify an rotation, in case the envelope will encompass the
     *            complete (rotated) world polygon.
     * @param paintArea
     *            the size of the rendering output area
     * @return the envelope in world coordinates corresponding to the screen rectangle.
     */
    public static Envelope createMapEnvelope(Rectangle paintArea, AffineTransform worldToScreen)
            throws NoninvertibleTransformException {
        //
        // (X1,Y1) (X2,Y1)
        //
        // (X1,Y2) (X2,Y2)
        double[] pts = new double[8];
        pts[0] = paintArea.getMinX();
        pts[1] = paintArea.getMinY();
        pts[2] = paintArea.getMaxX();
        pts[3] = paintArea.getMinY();
        pts[4] = paintArea.getMaxX();
        pts[5] = paintArea.getMaxY();
        pts[6] = paintArea.getMinX();
        pts[7] = paintArea.getMaxY();
        worldToScreen.inverseTransform(pts, 0, pts, 0, 4);
        double xMin = Double.MAX_VALUE;
        double yMin = Double.MAX_VALUE;
        double xMax = -Double.MAX_VALUE;
        double yMax = -Double.MAX_VALUE;
        for (int i = 0; i < 4; i++) {
            xMin = Math.min(xMin, pts[2 * i]);
            yMin = Math.min(yMin, pts[2 * i + 1]);
            xMax = Math.max(xMax, pts[2 * i]);
            yMax = Math.max(yMax, pts[2 * i + 1]);
        }
        return new Envelope(xMin, xMax, yMin, yMax);
    }

    /**
     * Creates the map's bounding box in real world coordinates
     * <p/>
     * 
     * NOTE It is worth to note that here we do not take into account the half a pixel translation
     * stated by ogc for coverages bounds. One reason is that WMS 1.1.1 does not follow it!!!
     * 
     * @param worldToScreen
     *            a transform which converts World coordinates to screen pixel coordinates.
     * @param paintArea
     *            the size of the rendering output area
     */
    public static ReferencedEnvelope createMapEnvelope(Rectangle paintArea,
            AffineTransform worldToScreen, final CoordinateReferenceSystem crs)
            throws NoninvertibleTransformException {

        // //
        //
        // Make sure the CRS is 2d
        //
        // //
        final CoordinateReferenceSystem crs2d = CRS.getHorizontalCRS(crs);
        if (crs2d == null)
            throw new UnsupportedOperationException(Errors.format(
                    ErrorKeys.CANT_REDUCE_TO_TWO_DIMENSIONS_$1, crs));

        Envelope env = createMapEnvelope(paintArea, worldToScreen);
        return new ReferencedEnvelope(env, crs2d);
    }

	/**
	 * Find the scale denominator of the map. Method: 1. find the diagonal
	 * distance (meters) 2. find the diagonal distance (pixels) 3. find the
	 * diagonal distance (meters) -- use DPI 4. calculate scale (#1/#2)
	 * 
	 * NOTE: return the scale denominator not the actual scale (1/scale =
	 * denominator)
	 * 
	 * TODO: (SLD spec page 28): Since it is common to integrate the output of
	 * multiple servers into a single displayed result in the web-mapping
	 * environment, it is important that different map servers have consistent
	 * behaviour with respect to processing scales, so that all of the
	 * independent servers will select or deselect rules at the same scales. To
	 * insure consistent behaviour, scales relative to coordinate spaces must be
	 * handled consistently between map servers. For geographic coordinate
	 * systems, which use angular units, the angular coverage of a map should be
	 * converted to linear units for computation of scale by using the
	 * circumference of the Earth at the equator and by assuming perfectly
	 * square linear units. For linear coordinate systems, the size of the
	 * coordinate space should be used directly without compensating for
	 * distortions in it with respect to the shape of the real Earth.
	 * 
	 * NOTE: we are actually doing a a much more exact calculation, and
	 * accounting for non-square pixels (which are allowed in WMS) ADDITIONAL
	 * NOTE from simboss: I added soe minor fixes. See below.
	 * 
	 * @param envelope
	 * @param coordinateReferenceSystem
	 * @param imageWidth
	 * @param imageHeight
	 * @param DPI
	 *            screen dots per inch (OGC standard is 90)
	 * @throws TransformException
	 * @throws FactoryException
	 * 
	 * @deprecated
	 */
	public static double calculateScale(Envelope envelope,
			CoordinateReferenceSystem coordinateReferenceSystem,
			int imageWidth, int imageHeight, double DPI)
			throws TransformException, FactoryException {


		final CoordinateReferenceSystem tempCRS = CRS.getHorizontalCRS(coordinateReferenceSystem);
        if(tempCRS==null)
            throw new TransformException(Errors.format(
                      ErrorKeys.CANT_REDUCE_TO_TWO_DIMENSIONS_$1,
                      coordinateReferenceSystem));
		// final CoordinateSystem tempCS = tempCRS.getCoordinateSystem();
		// final MathTransform preTransform;
		// if (tempCS.getAxis(0).getDirection().absolute().equals(
		// AxisDirection.NORTH)) {
		// preTransform = ProjectiveTransform.create(new AffineTransform(0, 1,
		// 1, 0, 0, 0));
		//
		// } else
		// preTransform = ProjectiveTransform.create(AffineTransform
		// .getTranslateInstance(0, 0));

		// DJB: be much wiser if the requested image is larger than the world
		// (this happens VERY OFTEN)
		// we first convert to WSG84 and check to see if we're outside the
		// 'world'
		// bbox
		final double[] cs = new double[4];
		final double[] csLatLong = new double[4];
		final Coordinate p1 = new Coordinate(envelope.getMinX(), envelope
				.getMinY());
		final Coordinate p2 = new Coordinate(envelope.getMaxX(), envelope
				.getMaxY());
		cs[0] = p1.x;
		cs[1] = p1.y;
		cs[2] = p2.x;
		cs[3] = p2.y;

		// transform the provided crs to WGS84 lon,lat
		final MathTransform transform = CRS.findMathTransform(tempCRS,
				DefaultGeographicCRS.WGS84, true);
		transform.transform(cs, 0, csLatLong, 0, 2);

		// in long/lat format
		if ((csLatLong[0] < -180) || (csLatLong[0] > 180)
				|| (csLatLong[2] < -180) || (csLatLong[2] > 180)
				|| (csLatLong[1] < -90) || (csLatLong[1] > 90)
				|| (csLatLong[3] < -90) || (csLatLong[3] > 90)) {
			// we have a problem -- the bbox is outside the 'world' so distance
			// will fail we handle this by making a new measurement for a smaller portion
			// of the image - the portion thats inside the world.
			// If the request is outside the world then we need to throw an
			// error

			if ((csLatLong[0] > csLatLong[2]) || (csLatLong[1] > csLatLong[3]))
				throw new IllegalArgumentException("BBox is backwards");
			if (((csLatLong[0] < -180) || (csLatLong[0] > 180))
					&& ((csLatLong[2] < -180) || (csLatLong[2] > 180))
					&& ((csLatLong[1] < -90) || (csLatLong[1] > 90))
					&& ((csLatLong[3] < -90) || (csLatLong[3] > 90)))
				throw new IllegalArgumentException(
						"World isn't in the requested bbox");

			// okay, all good. We need to find the world bbox intersect the
			// requested bbox then we're going to convert that back to the
			// original coordinate reference system and from there we can find
			// the (x1,y2) and (x2,y2) of this new bbox.
			// At that point we can do simple math to find the distance.

			final double[] newCsLatLong = new double[4]; // intersected with
			// the world bbox

			newCsLatLong[0] = Math.min(Math.max(csLatLong[0], -180), 180);
			newCsLatLong[1] = Math.min(Math.max(csLatLong[1], -90), 90);
			newCsLatLong[2] = Math.min(Math.max(csLatLong[2], -180), 180);
			newCsLatLong[3] = Math.min(Math.max(csLatLong[3], -90), 90);

			double[] origProject = new double[4];
			transform.transform(newCsLatLong, 0, origProject, 0, 2);

			// have the truncated bbox in the original projection, so we can
			// find the image (x,y) for the two points.
			double image_min_x = (origProject[0] - envelope.getMinX())
					/ envelope.getWidth() * imageWidth;
			double image_max_x = (origProject[2] - envelope.getMinX())
					/ envelope.getWidth() * imageWidth;

			double image_min_y = (origProject[1] - envelope.getMinY())
					/ envelope.getHeight() * imageHeight;
			double image_max_y = (origProject[3] - envelope.getMinY())
					/ envelope.getHeight() * imageHeight;

			double distance_ground = JTS.orthodromicDistance(new Coordinate(
					newCsLatLong[0], newCsLatLong[1]), new Coordinate(
					newCsLatLong[2], newCsLatLong[3]),
					DefaultGeographicCRS.WGS84);
			double pixel_distance = Math.sqrt((image_max_x - image_min_x)
					* (image_max_x - image_min_x) + (image_max_y - image_min_y)
					* (image_max_y - image_min_y));
			double pixel_distance_m = pixel_distance / DPI * 2.54 / 100.0;
			return distance_ground / pixel_distance_m;
			// remember, this is the denominator, not the actual scale;
		}

		// simboss:
		// this way we never ran into problems with lat,lon lon,lat
		double diagonalGroundDistance = JTS.orthodromicDistance(new Coordinate(
				csLatLong[0], csLatLong[1]), new Coordinate(csLatLong[2],
				csLatLong[3]), DefaultGeographicCRS.WGS84);
		// pythagorus theorm
		double diagonalPixelDistancePixels = Math.sqrt(imageWidth * imageWidth
				+ imageHeight * imageHeight);
		double diagonalPixelDistanceMeters = diagonalPixelDistancePixels / DPI
				* 2.54 / 100; // 2.54 = cm/inch, 100= cm/m
		return diagonalGroundDistance / diagonalPixelDistanceMeters;
		// remember, this is the denominator, not the actual scale;
	}
    
    final static double OGC_DEGREE_TO_METERS = 6378137.0 * 2.0 * Math.PI / 360;
    
    
	/**
	 * Calculates the pixels per meter ratio based on a scale denominator.
	 * 
	 * @param scaleDenominator
	 *            The scale denominator value.
	 * @param hints
	 *            The hints used in calculation. if "dpi" key is present, it
	 *            uses it's Integer value as the dpi of the current device. if
	 *            not it uses 90 that is the OGC default value.
	 * @return The pixels per meter ratio for the given scale denominator.
	 */
	public static double calculatePixelsPerMeterRatio(double scaleDenominator, Map hints) {
		if (scaleDenominator <= 0.0)
			throw new IllegalArgumentException("The scale denominator must be positive.");
		double scale = 1.0 / scaleDenominator;
		return scale * (getDpi(hints) / 0.0254);
	}
    
    
    /**
     * This method performs the computation using the methods suggested by the OGC SLD specification, page 26.
     * @param envelope
     * @param imageWidth
     * @return
     */
    public static double calculateOGCScale(ReferencedEnvelope envelope, int imageWidth, Map hints) {
        // if it's geodetic, we're dealing with lat/lon unit measures
        if(envelope.getCoordinateReferenceSystem() instanceof GeographicCRS) {
            return (envelope.getWidth() * OGC_DEGREE_TO_METERS) / (imageWidth / getDpi(hints) * 0.0254);
        } else {
            return envelope.getWidth() / (imageWidth / getDpi(hints) * 0.0254);
        }
    }
    
    /**
     * This method performs the computation using the methods suggested by the OGC SLD specification, page 26.
     * @param CRS the coordinate reference system. Used to check if we are operating in degrees or
     * meters.
     * @param worldToScreen the transformation mapping world coordinates to screen coordinates. Might
     * specify a rotation in addition to translation and scaling.
     * @return
     */
    public static double calculateOGCScaleAffine(CoordinateReferenceSystem crs,AffineTransform worldToScreen, Map hints) {
        double scale = XAffineTransform.getScale(worldToScreen);
        // if it's geodetic, we're dealing with lat/lon unit measures
        if(crs instanceof GeographicCRS) {
            return (OGC_DEGREE_TO_METERS * getDpi(hints))/(scale * 0.0254);
        } else {            
            return (getDpi(hints))/(scale * 0.0254);
        }
    }

    /**
     * First searches the hints for the scale denominator hint otherwise calls 
     * {@link #calculateScale(Envelope, CoordinateReferenceSystem, int, int, double)}.  If
     * the hints contains a DPI then that DPI is used otherwise 90 is used (the OGS default).
     */
    public static double calculateScale(ReferencedEnvelope envelope, int imageWidth,int imageHeight,
            Map hints )
	throws TransformException, FactoryException
    {
        
        if( hints!=null && hints.containsKey("declaredScaleDenominator")){
            Double scale=(Double) hints.get("declaredScaleDenominator");
            if( scale.doubleValue()<=0 )
                throw new IllegalArgumentException("the declaredScaleDenominator must be greater than 0, was: "+scale.doubleValue());
            return scale.doubleValue();
        }
            
        return calculateScale(envelope, imageWidth, imageHeight, getDpi(hints));
    }

    /**
     * Either gets a DPI from the hints, or return the OGC standard, stating that a pixel is 0.28 mm
     * (the result is a non integer DPI...)
     * @param hints 
     * @return DPI as doubles, to avoid issues with integer trunking in scale computation expression
     */
    public static double getDpi(Map hints) {
        if( hints!=null && hints.containsKey("dpi") ){
            return ((Number)hints.get("dpi")).doubleValue();
        }else{
            return  25.4 / 0.28;   // 90 = OGC standard
        }
    }
	
	/**
	 * Find the scale denominator of the map. Method: 1. find the diagonal
	 * distance (meters) 2. find the diagonal distance (pixels) 3. find the
	 * diagonal distance (meters) -- use DPI 4. calculate scale (#1/#2)
	 * 
	 * NOTE: return the scale denominator not the actual scale (1/scale =
	 * denominator)
	 * 
	 * TODO: (SLD spec page 28): Since it is common to integrate the output of
	 * multiple servers into a single displayed result in the web-mapping
	 * environment, it is important that different map servers have consistent
	 * behaviour with respect to processing scales, so that all of the
	 * independent servers will select or deselect rules at the same scales. To
	 * insure consistent behaviour, scales relative to coordinate spaces must be
	 * handled consistently between map servers. For geographic coordinate
	 * systems, which use angular units, the angular coverage of a map should be
	 * converted to linear units for computation of scale by using the
	 * circumference of the Earth at the equator and by assuming perfectly
	 * square linear units. For linear coordinate systems, the size of the
	 * coordinate space should be used directly without compensating for
	 * distortions in it with respect to the shape of the real Earth.
	 * 
	 * NOTE: we are actually doing a a much more exact calculation, and
	 * accounting for non-square pixels (which are allowed in WMS) ADDITIONAL
	 * NOTE from simboss: I added soe minor fixes. See below.
	 * 
	 * @param envelope
	 * @param imageWidth
	 * @param imageHeight
	 * @param DPI
	 *            screen dots per inch (OGC standard is 90)
	 * 
	 * 
	 * TODO should I take into account also the destination CRS? Otherwise I am
	 * just assuming that the final crs is lon,lat that is it maps lon to x (n
	 * raster space) and lat to y (in raster space).
	 * @throws TransformException
	 * @throws FactoryException
	 * 
	 */
	public static double calculateScale(ReferencedEnvelope envelope,
			int imageWidth, int imageHeight, double DPI)
			throws TransformException, FactoryException {

		final double diagonalGroundDistance;
		if (!(envelope.getCoordinateReferenceSystem() instanceof EngineeringCRS)) { // geographic or cad?
			// //
			//
			// get CRS2D for this referenced envelope, check that its 2d
			//
			// //
			final CoordinateReferenceSystem tempCRS = CRS.getHorizontalCRS(envelope.getCoordinateReferenceSystem());
	        if(tempCRS==null)
	            throw new TransformException(Errors.format(
	                      ErrorKeys.CANT_REDUCE_TO_TWO_DIMENSIONS_$1,
	                      envelope.getCoordinateReferenceSystem()));
			// make sure the crs is 2d
			envelope = new ReferencedEnvelope((Envelope) envelope, tempCRS);
			final MathTransform toWGS84 = CRS.findMathTransform(tempCRS, DefaultGeographicCRS.WGS84, true);

			// //
			// Try to compute the source crs envelope, either by asking CRS or
			// by trying to project the WGS84 envelope (world) to the specified
			// CRS
			// //
			GeneralEnvelope sourceCRSEnvelope = (GeneralEnvelope) CRS
					.getEnvelope(tempCRS);
			if (sourceCRSEnvelope == null) {
				try {
					// try to compute the envelope by reprojecting the WGS84
					// envelope
					sourceCRSEnvelope = CRS.transform(toWGS84
							.inverse(), CRS.getEnvelope(DefaultGeographicCRS.WGS84));
				} catch (TransformException e) {
					// for some transformations this is normal, it's not possible
					// to project the whole WGS84 envelope in many transforms,
					// such as Mercator or Gauss (the transform does diverge)
				}catch ( AssertionError ae){
                                    // same reason as above basically.  For some 
                                    // projections the assertion will complain.  Nothing can be done about this
                                }
			}

			// //
			//
			// Make sure it intersect the world in the source projection by
			// intersecting the provided envelope with the envelope of its crs.
			//
			// This will also prevent us from having problems with requests for
			// images bigger than the world (thanks Dave!!!)
			//
			// It is important to note that I also have to update the image
			// width in case the provided envelope is bigger than the envelope of the
			// source crs.
			// //
			final GeneralEnvelope intersectedEnvelope = new GeneralEnvelope(
					envelope);
			if (sourceCRSEnvelope != null) {
				intersectedEnvelope.intersect(sourceCRSEnvelope);
				if (intersectedEnvelope.isEmpty())
					throw new IllegalArgumentException(
							"The provided envelope is outside the source CRS definition area");
				if (!intersectedEnvelope.equals(envelope)) {
					final double scale0 = intersectedEnvelope.getLength(0)
							/ envelope.getLength(0);
					final double scale1 = intersectedEnvelope.getLength(1)
							/ envelope.getLength(1);
					imageWidth *= scale0;
					imageHeight *= scale1;
				}
			}

			// //
			//
			// Go to WGS84 in order to see how things are there
			//
			// //
			final GeneralEnvelope geographicEnvelope = CRS.transform(
					toWGS84, intersectedEnvelope);
			geographicEnvelope.setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);

			// //
			//
			// Instantiate a geodetic calculator for GCS WGS84 and get the
			// orthodromic distance between LLC and UC of the geographic
			// envelope.
			//
			// //
			final GeodeticCalculator calculator = new GeodeticCalculator(
					DefaultGeographicCRS.WGS84);
			final DirectPosition lowerLeftCorner = geographicEnvelope
					.getLowerCorner();
			final DirectPosition upperRightCorner = geographicEnvelope
					.getUpperCorner();
			calculator.setStartingGeographicPoint(lowerLeftCorner
					.getOrdinate(0), lowerLeftCorner.getOrdinate(1));
			calculator.setDestinationGeographicPoint(upperRightCorner
					.getOrdinate(0), upperRightCorner.getOrdinate(1));
			diagonalGroundDistance = calculator
					.getOrthodromicDistance();
		} else {
			// if it's an engineering crs, compute only the graphical scale, assuming a CAD space
			diagonalGroundDistance = Math.sqrt(envelope.getWidth() * envelope.getWidth()
					+ envelope.getHeight() * envelope.getHeight());
		}

		// //
		//
		// Compute the distances on the requested image using the provided DPI.
		//
		// //
		// pythagorus theorm
		double diagonalPixelDistancePixels = Math.sqrt(imageWidth * imageWidth
				+ imageHeight * imageHeight);
		double diagonalPixelDistanceMeters = diagonalPixelDistancePixels / DPI
				* 2.54 / 100; // 2.54 = cm/inch, 100= cm/m
		return diagonalGroundDistance / diagonalPixelDistanceMeters;
		// remember, this is the denominator, not the actual scale;
	}

	
	/**
	 * This worldToScreenTransform method makes the assumption that the crs is
	 * in Lon,Lat or Lat,Lon. If the provided envelope does not carry along a
	 * crs the assumption that the map extent is in the classic Lon,Lat form. In
	 * case the provided envelope is of type.
	 * 
	 * Note that this method takes into account also the OGC standard with
	 * respect to the relation between pixels and sample.
	 * 
	 * @param mapExtent
	 *            The envelope of the map in lon,lat
	 * @param paintArea
	 *            The area to paint as a rectangle
	 * @param destinationCrs
	 * @throws TransformException 
	 * @todo add georeferenced envelope check when merge with trunk will
	 *         be performed
	 * 
	 */
	public static AffineTransform worldToScreenTransform(Envelope mapExtent,
			Rectangle paintArea, CoordinateReferenceSystem destinationCrs) throws TransformException {

			// is the crs also lon,lat?
		final CoordinateReferenceSystem crs2D= CRS.getHorizontalCRS(destinationCrs);
		if(crs2D==null)
			throw new TransformException(Errors.format(
                    ErrorKeys.CANT_REDUCE_TO_TWO_DIMENSIONS_$1,
                    destinationCrs));
		final boolean lonFirst = crs2D
				.getCoordinateSystem().getAxis(0).getDirection().absolute()
				.equals(AxisDirection.EAST);
		final GeneralEnvelope newEnvelope = lonFirst ? new GeneralEnvelope(
				new double[] { mapExtent.getMinX(), mapExtent.getMinY() },
				new double[] { mapExtent.getMaxX(), mapExtent.getMaxY() })
				: new GeneralEnvelope(new double[] { mapExtent.getMinY(),
						mapExtent.getMinX() }, new double[] {
						mapExtent.getMaxY(), mapExtent.getMaxX() });
		newEnvelope.setCoordinateReferenceSystem(destinationCrs);

		//			
		// with this method I can build a world to grid transform
		// without adding half of a pixel translations. The cost
        // is a hashtable lookup. The benefit is reusing the last
        // transform (instead of creating a new one) if the grid
        // and envelope are the same one than during last invocation.
        final GridToEnvelopeMapper m = (GridToEnvelopeMapper) gridToEnvelopeMappers.get();
        m.setGridRange(new GridEnvelope2D(paintArea));
        m.setEnvelope(newEnvelope);
        return (AffineTransform) (m.createTransform().inverse());


	}
	
	/**
     * Finds the centroid of the input geometry if input = point, line, polygon
     * --> return a point that represents the centroid of that geom if input =
     * geometry collection --> return a multipoint that represents the centoid
     * of each sub-geom
     * 
     * @param g
     */
    public static Geometry getCentroid(Geometry g) {
        if(g instanceof MultiPoint) {
            return g;
        } else if (g instanceof GeometryCollection) {
            final GeometryCollection gc = (GeometryCollection) g;
            final Coordinate[] pts = new Coordinate[gc.getNumGeometries()];
            final int length = gc.getNumGeometries();
            for (int t = 0; t < length; t++) {
                pts[t] = pointInGeometry(gc.getGeometryN(t)).getCoordinate();
            }
            return g.getFactory().createMultiPoint(pts);
        } else if (g != null) {
            return pointInGeometry(g);
        }
        return null;
    }
    
    private static Geometry pointInGeometry(Geometry g) {
        Point p = g.getCentroid();
        if(g instanceof Polygon) {
            // if the geometry is heavily generalized centroid computation may fail and return NaN
            if(Double.isNaN(p.getX()) || Double.isNaN(p.getY()))
                return g.getFactory().createPoint(g.getCoordinate());
            // otherwise let's check if the point is inside. Again, this check and "getInteriorPoint"
            // will work only if the geometry is valid
            if(g.isValid() && !g.contains(p)) {
                try {
                    p = g.getInteriorPoint();
                } catch(Exception e) {
                    // generalized geometries might make interior point go bye bye
                    return p;
                }
            } else {
                return p;
            }
        }
        return p;
    }
    
    public static double getStyle2DSize(Style2D style) {
        if(style instanceof GraphicStyle2D) {
           final BufferedImage image = ((GraphicStyle2D) style).getImage();
           return Math.max(image.getWidth(), image.getHeight());
        } else if(style instanceof IconStyle2D) {
           final Icon icon = ((IconStyle2D) style).getIcon();
           return Math.max(icon.getIconWidth(), icon.getIconHeight());
        } else if(style instanceof LineStyle2D) {
            LineStyle2D ls = ((LineStyle2D) style);
            double gsSize = getStyle2DSize(ls.getGraphicStroke());
            double strokeSize = 0;
            if(ls.getStroke() instanceof BasicStroke) {
                strokeSize = ((BasicStroke) ls.getStroke()).getLineWidth();
            }
            return Math.max(gsSize, strokeSize);
        } else {
            return 0;
        }
    }
}
