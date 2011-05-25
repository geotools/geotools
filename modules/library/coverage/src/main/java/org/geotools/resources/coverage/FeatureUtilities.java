/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.resources.coverage;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.FactoryRegistryException;
import org.geotools.factory.GeoTools;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.type.FeatureTypeFactoryImpl;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.util.Utilities;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureTypeFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;


/**
 * A set of utilities methods for interactions between {@link GridCoverage}
 * and {@link Feature}. Those methods are not really rigorous; must of them
 * should be seen as temporary implementations.
 *
 * @since 2.4
 *
 * @source $URL$
 * @version $Id$
 * @author Simone Giannecchini
 */
public final class FeatureUtilities {
    
    static FeatureTypeFactory typeFactory;
    static FeatureFactory featureFactory;
    public static final FilterFactory2 DEFAULT_FILTER_FACTORY = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
    
    /**
     * Do not allows instantiation of this class.
     */
    private FeatureUtilities() {
    }
    
    private static FeatureTypeFactory getTypeFactory() {
        if(typeFactory == null)
            typeFactory = new FeatureTypeFactoryImpl();  // CommonFactoryFinder.getFeatureTypeFactory(null);
        return typeFactory;
    }
    
    private static FeatureFactory getFeatureFactory() {
        if(featureFactory == null)
            featureFactory = CommonFactoryFinder.getFeatureFactory(null);
        return featureFactory;
    }

    /**
     * Returns the polygon surrounding the specified rectangle.
     * Code lifted from ArcGridDataSource (temporary).
     */
    private static Polygon getPolygon(final Rectangle2D rect) {
        return getPolygon(rect, 0);
    }
    
    /**
     * Returns the polygon surrounding the specified rectangle.
     * Code lifted from ArcGridDataSource (temporary).
     */
    public static Polygon getPolygon(final Rectangle2D rect, final int srid) {
        final PrecisionModel  pm = new PrecisionModel();
        final GeometryFactory gf = new GeometryFactory(pm, srid);
        final Coordinate[] coord = new Coordinate[] {
            new Coordinate(rect.getMinX(), rect.getMinY()),
            new Coordinate(rect.getMaxX(), rect.getMinY()),
            new Coordinate(rect.getMaxX(), rect.getMaxY()),
            new Coordinate(rect.getMinX(), rect.getMaxY()),
            new Coordinate(rect.getMinX(), rect.getMinY())
        };
        final LinearRing ring = gf.createLinearRing(coord);
        return new Polygon(ring, null, gf);
    }

    /**
     * Wraps a grid coverage into a Feature. Code lifted from ArcGridDataSource
     * (temporary).
     *
     * @param  coverage the grid coverage.
     * @return a feature with the grid coverage envelope as the geometry and the
     *         grid coverage itself in the "grid" attribute.
     */
    public static SimpleFeatureCollection wrapGridCoverage(final GridCoverage2D coverage)
            throws TransformException, SchemaException {
        final Polygon bounds = getPolygon(coverage.getEnvelope2D());
        final CoordinateReferenceSystem sourceCRS = coverage.getCoordinateReferenceSystem2D();

        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder(getTypeFactory());
        ftb.setName("GridCoverage");
        ftb.add("geom", Polygon.class, sourceCRS);
        ftb.add("grid", GridCoverage.class);
        SimpleFeatureType schema = ftb.buildFeatureType();

        // create the feature
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(schema, getFeatureFactory());
        fb.add(bounds);
        fb.add(coverage);
        SimpleFeature feature = fb.buildFeature(null);

        final SimpleFeatureCollection collection = FeatureCollections.newCollection();
        collection.add(feature);

        return collection;
    }
    
    /**
     * Checks if the feature type specified is a GridCoverage wrapper
     * @param featureType
     * @return
     */
    public static boolean isWrappedCoverage(SimpleFeatureType featureType) {
        if(!"GridCoverage".equals(featureType.getName().getLocalPart()))
            return false;
        
        if(featureType.getAttributeCount() != 2)
            return false;
        
        AttributeDescriptor polyDescriptor = featureType.getDescriptor("geom");
        if(polyDescriptor == null || !Polygon.class.equals(polyDescriptor.getType().getBinding()))
            return false;
        
        AttributeDescriptor gridDescriptor = featureType.getDescriptor("grid");
        if(gridDescriptor == null || !GridCoverage.class.equals(gridDescriptor.getType().getBinding()))
            return false;
        
        return true;
    }

    /**
     * Wraps a grid coverage into a Feature. Code lifted from ArcGridDataSource
     * (temporary).
     *
     * @param  reader the grid coverage reader.
     * @return a feature with the grid coverage envelope as the geometry and the
     *         grid coverage itself in the "grid" attribute.
     */
    public static SimpleFeatureCollection wrapGridCoverageReader(final AbstractGridCoverage2DReader gridCoverageReader,
			GeneralParameterValue[] params) throws TransformException,
			FactoryRegistryException, SchemaException {

		// create surrounding polygon
		final PrecisionModel pm = new PrecisionModel();
		final GeometryFactory gf = new GeometryFactory(pm, 0);
		final Rectangle2D rect = gridCoverageReader.getOriginalEnvelope()
				.toRectangle2D();
		final CoordinateReferenceSystem sourceCrs = CRS
			.getHorizontalCRS(gridCoverageReader.getCrs());
		if(sourceCrs==null)
			throw new UnsupportedOperationException(
					Errors.format(
		                    ErrorKeys.CANT_SEPARATE_CRS_$1,gridCoverageReader.getCrs()));


		final Coordinate[] coord = new Coordinate[5];
		coord[0] = new Coordinate(rect.getMinX(), rect.getMinY());
		coord[1] = new Coordinate(rect.getMaxX(), rect.getMinY());
		coord[2] = new Coordinate(rect.getMaxX(), rect.getMaxY());
		coord[3] = new Coordinate(rect.getMinX(), rect.getMaxY());
		coord[4] = new Coordinate(rect.getMinX(), rect.getMinY());

		// }
		final LinearRing ring = gf.createLinearRing(coord);
		final Polygon bounds = new Polygon(ring, null, gf);

		SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder(getTypeFactory());
        ftb.setName("GridCoverage");
        ftb.add("geom", Polygon.class, sourceCrs);
        ftb.add("grid", AbstractGridCoverage2DReader.class);
        ftb.add("params", GeneralParameterValue[].class);
        SimpleFeatureType schema = ftb.buildFeatureType();

        // create the feature
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(schema, getFeatureFactory());
        fb.add(bounds);
        fb.add(gridCoverageReader);
        fb.add(params);
        SimpleFeature feature = fb.buildFeature(null);


		final SimpleFeatureCollection collection = FeatureCollections.newCollection();
		collection.add(feature);

		return collection;
	}
    
    /**
     * Checks if the feature type specified is a AbstractGridCoverage2DReader wrapper
     * @param featureType
     * @return
     */
    public static boolean isWrappedCoverageReader(SimpleFeatureType featureType) {
        if(!"GridCoverage".equals(featureType.getName().getLocalPart()))
            return false;
        
        if(featureType.getAttributeCount() != 3)
            return false;
        
        AttributeDescriptor polyDescriptor = featureType.getDescriptor("geom");
        if(polyDescriptor == null || !Polygon.class.equals(polyDescriptor.getType().getBinding()))
            return false;
        
        AttributeDescriptor gridDescriptor = featureType.getDescriptor("grid");
        if(gridDescriptor == null || !AbstractGridCoverage2DReader.class.equals(gridDescriptor.getType().getBinding()))
            return false;
        
        AttributeDescriptor paramDescriptor = featureType.getDescriptor("params");
        if(paramDescriptor == null || !GeneralParameterValue[].class.equals(paramDescriptor.getType().getBinding()))
            return false;
        
        return true;
    }

	/**
	 * Converts a JTS {@link Polygon}, which represents a ROI, int an AWT
	 * {@link java.awt.Polygon} by means of the provided {@link MathTransform}.
	 *
	 * @param roiInput
	 *            the input ROI as a JTS {@link Polygon}.
	 * @param worldToGridTransform
	 *            the {@link MathTransform} to apply to the input ROI.
	 * @return an AWT {@link java.awt.Polygon}.
	 * @throws TransformException
	 *             in case the provided {@link MathTransform} chokes.
	 */
	public static java.awt.Polygon convertPolygon(final Polygon roiInput,
			MathTransform worldToGridTransform) throws TransformException {
		return convertPolygonToPointArray(roiInput, worldToGridTransform, null);
	}

	/**
	 * Converts a JTS {@link Polygon}, which represents a ROI, int an AWT
	 * {@link java.awt.Polygon} by means of the provided {@link MathTransform}.
	 *
	 * <p>
	 * It also stores the points for this polygon into the provided {@link List}.
	 *
	 * @param roiInput
	 *            the input ROI as a JTS {@link Polygon}.
	 * @param worldToGridTransform
	 *            the {@link MathTransform} to apply to the input ROI.
	 * @param points
	 *            a {@link List} that should hold the transformed points.
	 * @return an AWT {@link java.awt.Polygon}.
	 * @throws TransformException
	 *             in case the provided {@link MathTransform} chokes.
	 */
	public static java.awt.Polygon convertPolygonToPointArray(final Polygon roiInput,
			MathTransform worldToGridTransform, List<Point2D> points)
			throws TransformException {
		final boolean isIdentity = worldToGridTransform.isIdentity();
		final double coords[] = new double[2];
		final LineString exteriorRing = roiInput.getExteriorRing();
		final CoordinateSequence exteriorRingCS = exteriorRing
				.getCoordinateSequence();
		final int numCoords = exteriorRingCS.size();
		final java.awt.Polygon retValue = new java.awt.Polygon();
		for (int i = 0; i < numCoords; i++) {
			// get the actual coord
			coords[0] = exteriorRingCS.getX(i);
			coords[1] = exteriorRingCS.getY(i);

			// transform it
			if (!isIdentity)
				worldToGridTransform.transform(coords, 0, coords, 0, 1);

			// send it back to the returned polygon
			final int x = (int) (coords[0] + 0.5d);
			final int y = (int) (coords[1] + 0.5d);
			if (points != null)
				points.add(new Point2D.Double(coords[0],coords[1]));

			// send it back to the returned polygon
			retValue.addPoint(x, y);

		}

		// return the created polygon.
		return retValue;
	}

	/**
	 * Convert the crop envelope into a polygon and the use the
	 * world-to-grid transform to get a ROI for the source coverage.
	 */
	public static Polygon getPolygon(final GeneralEnvelope env, final GeometryFactory gf) throws IllegalStateException, MismatchedDimensionException {
	    final Rectangle2D rect = env.toRectangle2D();
	    final Coordinate[] coord = new Coordinate[]{
	        new Coordinate(rect.getMinX(), rect.getMinY()),
	        new Coordinate(rect.getMinX(), rect.getMaxY()),
	        new Coordinate(rect.getMaxX(), rect.getMaxY()),
	        new Coordinate(rect.getMaxX(), rect.getMinY()),
	        new Coordinate(rect.getMinX(), rect.getMinY())};
	    final LinearRing ring = gf.createLinearRing(coord);
	    final Polygon modelSpaceROI = new Polygon(ring, null, gf);
	    // check that we have the same thing here
	    assert modelSpaceROI.getEnvelopeInternal().equals(new ReferencedEnvelope(rect, env.getCoordinateReferenceSystem()));
	    return modelSpaceROI;
	}

	/**
	 * Function to calculate the area of a polygon, according to the algorithm
	 * defined at http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/
	 *
	 * @param polyPoints
	 *            array of points in the polygon
	 * @return area of the polygon defined by pgPoints
	 */
	public static double area(Point2D[] polyPoints) {
		Utilities.ensureNonNull("polyPoints", polyPoints);
		int i, j, n = polyPoints.length;
		double area = 0;
	
		for (i = 0; i < n; i++) {
			j = (i + 1) % n;
			area += polyPoints[i].getX() * polyPoints[j].getY();
			area -= polyPoints[j].getX() * polyPoints[i].getY();
		}
		area /= 2.0;
		return (area);
	}
}
