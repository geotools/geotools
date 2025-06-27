/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2015, Open Source Geospatial Foundation (OSGeo)
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
import java.text.MessageFormat;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.measure.Unit;
import javax.measure.quantity.Length;
import javax.swing.Icon;
import org.geotools.api.geometry.Bounds;
import org.geotools.api.geometry.MismatchedDimensionException;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.crs.EngineeringCRS;
import org.geotools.api.referencing.crs.GeographicCRS;
import org.geotools.api.referencing.cs.AxisDirection;
import org.geotools.api.referencing.datum.PixelInCell;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.data.crs.ForceCoordinateSystemFeatureResults;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.GeneralBounds;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.referencing.CRS;
import org.geotools.referencing.GeodeticCalculator;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.renderer.style.GraphicStyle2D;
import org.geotools.renderer.style.IconStyle2D;
import org.geotools.renderer.style.LineStyle2D;
import org.geotools.renderer.style.MarkStyle2D;
import org.geotools.renderer.style.Style2D;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.locationtech.jts.geom.prep.PreparedGeometryFactory;
import si.uom.SI;

/**
 * Class for holding utility functions that are common tasks for people using the "StreamingRenderer/Renderer".
 *
 * @author dblasby
 * @author Simone Giannecchini
 */
public final class RendererUtilities {

    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(RendererUtilities.class);

    /**
     * Enable unit correction in {@link #toMeters(double, CoordinateReferenceSystem)} calculation.
     *
     * <p>Toggle for a bug fix that will invalidate a good number of SLDs out there (and thus, we allow people to turn
     * off the fix).
     */
    static boolean SCALE_UNIT_COMPENSATION =
            Boolean.parseBoolean(System.getProperty("org.geotoools.render.lite.scale.unitCompensation", "true"));

    /**
     * Helber class for building affine transforms. We use one instance per thread, in order to avoid the need for
     * {@code synchronized} statements.
     */
    private static final ThreadLocal<GridToEnvelopeMapper> gridToEnvelopeMappers = new ThreadLocal<>() {
        @Override
        protected GridToEnvelopeMapper initialValue() {
            final GridToEnvelopeMapper mapper = new GridToEnvelopeMapper();
            mapper.setPixelAnchor(PixelInCell.CELL_CORNER);
            return mapper;
        }
    };

    /** Utilities classes should not be instantiated. */
    private RendererUtilities() {}
    /**
     * Sets up the affine transform
     *
     * <p>NOTE It is worth to note that here we do not take into account the half a pixel translation stated by ogc for
     * coverages bounds. One reason is that WMS 1.1.1 does not follow it!!!
     *
     * @param mapExtent the map extent
     * @param paintArea the size of the rendering output area
     * @return a transform that maps from real world coordinates to the screen
     */
    public static AffineTransform worldToScreenTransform(ReferencedEnvelope mapExtent, Rectangle paintArea) {

        // //
        //
        // Convert the JTS envelope and get the transform
        //
        // //
        final Bounds genvelope = new GeneralBounds(mapExtent);

        // //
        //
        // Get the transform
        //
        // //
        final GridToEnvelopeMapper m = gridToEnvelopeMappers.get();
        try {
            m.setGridRange(new GridEnvelope2D(paintArea));
            m.setEnvelope(genvelope);
            return m.createAffineTransform().createInverse();
        } catch (MismatchedDimensionException | NoninvertibleTransformException e) {
            LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
            return null;
        }
    }

    /**
     * Creates the map's bounding box in real world coordinates.
     *
     * @param worldToScreen a transform which converts World coordinates to screen pixel coordinates. No assumptions are
     *     done on axis order as this is assumed to be pre-calculated. The affine transform may specify an rotation, in
     *     case the envelope will encompass the complete (rotated) world polygon.
     * @param paintArea the size of the rendering output area
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
     *
     * <p>NOTE It is worth to note that here we do not take into account the half a pixel translation stated by ogc for
     * coverages bounds. One reason is that WMS 1.1.1 does not follow it!!!
     *
     * @param worldToScreen a transform which converts World coordinates to screen pixel coordinates.
     * @param paintArea the size of the rendering output area
     */
    public static ReferencedEnvelope createMapEnvelope(
            Rectangle paintArea, AffineTransform worldToScreen, final CoordinateReferenceSystem crs)
            throws NoninvertibleTransformException {

        // //
        //
        // Make sure the CRS is 2d
        //
        // //
        final CoordinateReferenceSystem crs2d = CRS.getHorizontalCRS(crs);
        if (crs2d == null)
            throw new UnsupportedOperationException(
                    MessageFormat.format(ErrorKeys.CANT_REDUCE_TO_TWO_DIMENSIONS_$1, crs));

        Envelope env = createMapEnvelope(paintArea, worldToScreen);
        return new ReferencedEnvelope(env, crs2d);
    }

    static final double OGC_DEGREE_TO_METERS = 6378137.0 * 2.0 * Math.PI / 360;

    /**
     * Calculates the pixels per meter ratio based on a scale denominator.
     *
     * @param scaleDenominator The scale denominator value.
     * @param hints The hints used in calculation. if "dpi" key is present, it uses it's Integer value as the dpi of the
     *     current device. if not it uses 90 that is the OGC default value.
     * @return The pixels per meter ratio for the given scale denominator.
     */
    public static double calculatePixelsPerMeterRatio(double scaleDenominator, Map hints) {
        if (scaleDenominator <= 0.0) throw new IllegalArgumentException("The scale denominator must be positive.");
        double scale = 1.0 / scaleDenominator;
        return scale * (getDpi(hints) / 0.0254);
    }

    /**
     * This method performs the computation using the methods suggested by the OGC SLD specification, page 26.
     *
     * <p>In GeoTools 12 this method started to take into account units of measure, if this is not desirable in your
     * application you can set the system variable "org.geotoools.render.lite.scale.unitCompensation" to false.
     */
    public static double calculateOGCScale(ReferencedEnvelope envelope, int imageWidth, Map hints) {
        // if it's geodetic, we're dealing with lat/lon unit measures
        CoordinateReferenceSystem crs = envelope.getCoordinateReferenceSystem();
        double width = envelope.getWidth();
        double widthMeters = toMeters(width, crs);

        return widthMeters / (imageWidth / getDpi(hints) * 0.0254);
    }

    /**
     * Method used by the OGC scale calculation to turn a given length in the specified CRS towards meters.
     *
     * <p>GeographicCRS uses {@link #OGC_DEGREE_TO_METERS} for conversion of lat/lon measures
     *
     * <p>Otherwise the horizontal component of the CRS is assumed to have a uniform axis unit of measure providing the
     * Unit used for conversion. To ignore unit disable {@link #SCALE_UNIT_COMPENSATION} to for the unaltered size.
     *
     * @return size adjusted for GeographicCRS or CRS units
     */
    public static double toMeters(double size, CoordinateReferenceSystem crs) {
        if (crs == null) {
            LOGGER.finer("toMeters: assuming the original size is in meters already, as crs is null");
            return size;
        }
        if (crs instanceof GeographicCRS) {
            return size * OGC_DEGREE_TO_METERS;
        }
        if (!SCALE_UNIT_COMPENSATION) {
            return size;
        }
        CoordinateReferenceSystem horizontal = CRS.getHorizontalCRS(crs);
        if (horizontal != null) {
            crs = horizontal;
        }
        @SuppressWarnings("unchecked")
        Unit<Length> unit = (Unit<Length>) crs.getCoordinateSystem().getAxis(0).getUnit();
        if (unit == null) {
            LOGGER.finer(
                    "toMeters: assuming the original size is in meters already, as the first crs axis unit is null. CRS is "
                            + crs);
            return size;
        }
        if (!unit.isCompatible(SI.METRE)) {
            LOGGER.warning("toMeters: could not convert unit " + unit + " to meters");
            return size;
        }
        return unit.getConverterTo(SI.METRE).convert(size);
    }

    /**
     * This method performs the computation using the methods suggested by the OGC SLD specification, page 26.
     *
     * @param crs the coordinate reference system. Used to check if we are operating in degrees or meters.
     * @param worldToScreen the transformation mapping world coordinates to screen coordinates. Might specify a rotation
     *     in addition to translation and scaling.
     */
    public static double calculateOGCScaleAffine(
            CoordinateReferenceSystem crs, AffineTransform worldToScreen, Map hints) {
        double scale = XAffineTransform.getScale(worldToScreen);
        // if it's geodetic, we're dealing with lat/lon unit measures
        if (crs instanceof GeographicCRS) {
            return OGC_DEGREE_TO_METERS * getDpi(hints) / (scale * 0.0254);
        } else {
            return getDpi(hints) / (scale * 0.0254);
        }
    }

    /**
     * First searches the hints for the scale denominator hint otherwise calls
     * {@link #calculateScale(org.geotools.util.SoftValueHashMap.Reference, int, int, double)}. If the hints contains a
     * DPI then that DPI is used otherwise 90 is used (the OGS default).
     */
    public static double calculateScale(ReferencedEnvelope envelope, int imageWidth, int imageHeight, Map<?, ?> hints)
            throws TransformException, FactoryException {

        if (hints != null && hints.containsKey("declaredScaleDenominator")) {
            Double scale = (Double) hints.get("declaredScaleDenominator");
            if (scale.doubleValue() <= 0)
                throw new IllegalArgumentException(
                        "the declaredScaleDenominator must be greater than 0, was: " + scale.doubleValue());
            return scale.doubleValue();
        }

        return calculateScale(envelope, imageWidth, imageHeight, getDpi(hints));
    }

    /**
     * Either gets a DPI from the hints, or return the OGC standard, stating that a pixel is 0.28 mm (the result is a
     * non integer DPI...)
     *
     * @return DPI as doubles, to avoid issues with integer trunking in scale computation expression
     */
    public static double getDpi(Map hints) {
        if (hints != null && hints.containsKey("dpi")) {
            return ((Number) hints.get("dpi")).doubleValue();
        } else {
            return 25.4 / 0.28; // 90 = OGC standard
        }
    }

    /**
     * Find the scale denominator of the map. Method: 1. find the diagonal distance (meters) 2. find the diagonal
     * distance (pixels) 3. find the diagonal distance (meters) -- use DPI 4. calculate scale (#1/#2)
     *
     * <p>NOTE: return the scale denominator not the actual scale (1/scale = denominator)
     *
     * <p>TODO: (SLD spec page 28): Since it is common to integrate the output of multiple servers into a single
     * displayed result in the web-mapping environment, it is important that different map servers have consistent
     * behaviour with respect to processing scales, so that all of the independent servers will select or deselect rules
     * at the same scales. To insure consistent behaviour, scales relative to coordinate spaces must be handled
     * consistently between map servers. For geographic coordinate systems, which use angular units, the angular
     * coverage of a map should be converted to linear units for computation of scale by using the circumference of the
     * Earth at the equator and by assuming perfectly square linear units. For linear coordinate systems, the size of
     * the coordinate space should be used directly without compensating for distortions in it with respect to the shape
     * of the real Earth.
     *
     * <p>NOTE: we are actually doing a a much more exact calculation, and accounting for non-square pixels (which are
     * allowed in WMS) ADDITIONAL NOTE from simboss: I added soe minor fixes. See below.
     *
     * @param DPI screen dots per inch (OGC standard is 90)
     *     <p>TODO should I take into account also the destination CRS? Otherwise I am just assuming that the final crs
     *     is lon,lat that is it maps lon to x (n raster space) and lat to y (in raster space).
     */
    public static double calculateScale(ReferencedEnvelope envelope, int imageWidth, int imageHeight, double DPI)
            throws TransformException, FactoryException {

        final double diagonalGroundDistance;
        if (!(envelope.getCoordinateReferenceSystem() instanceof EngineeringCRS)) {
            // //
            //
            // get CRS2D for this referenced envelope, check that its 2d
            //
            // //
            final CoordinateReferenceSystem tempCRS = CRS.getHorizontalCRS(envelope.getCoordinateReferenceSystem());
            if (tempCRS == null) {
                final Object arg0 = envelope.getCoordinateReferenceSystem();
                throw new TransformException(MessageFormat.format(ErrorKeys.CANT_REDUCE_TO_TWO_DIMENSIONS_$1, arg0));
            }
            ReferencedEnvelope envelopeWGS84 = envelope.transform(DefaultGeographicCRS.WGS84, true);
            diagonalGroundDistance = geodeticDiagonalDistance(envelopeWGS84);
        } else {
            // if it's an engineering crs, compute only the graphical scale, assuming a CAD space
            diagonalGroundDistance =
                    Math.sqrt(envelope.getWidth() * envelope.getWidth() + envelope.getHeight() * envelope.getHeight());
        }

        // //
        //
        // Compute the distances on the requested image using the provided DPI.
        //
        // //
        // pythagoras theorem
        double diagonalPixelDistancePixels = Math.sqrt(imageWidth * imageWidth + imageHeight * imageHeight);
        double diagonalPixelDistanceMeters =
                diagonalPixelDistancePixels / DPI * 2.54 / 100; // 2.54 = cm/inch, 100= cm/m
        return diagonalGroundDistance / diagonalPixelDistanceMeters;
        // remember, this is the denominator, not the actual scale;
    }

    private static double geodeticDiagonalDistance(Envelope env) {
        if (env.getWidth() < 180 && env.getHeight() < 180) {
            return getGeodeticSegmentLength(env.getMinX(), env.getMinY(), env.getMaxX(), env.getMaxY());
        } else {
            // we cannot compute geodetic distance for distances longer than a hemisphere,
            // we have to build a set of lines connecting the two points that are smaller to
            // get a value that makes any sense rendering wise by crossing the original line with
            // a set of quadrants that are 180x180
            double distance = 0;
            GeometryFactory gf = new GeometryFactory();
            LineString ls = gf.createLineString(new Coordinate[] {
                new Coordinate(env.getMinX(), env.getMinY()), new Coordinate(env.getMaxX(), env.getMaxY())
            });
            int qMinX = -1;
            int qMaxX = 1;
            int qMinY = -1;
            int qMaxY = 1;
            // we must consider at least a pair of quadrants in each direction other wise lines
            // which don't cross both the equator and prime meridian are
            // measured as 0 length. But for some cases we need to consider still more hemispheres.
            qMinX = Math.min(qMinX, (int) (Math.signum(env.getMinX()) * Math.ceil(Math.abs(env.getMinX() / 180.0))));
            qMaxX = Math.max(qMaxX, (int) (Math.signum(env.getMaxX()) * Math.ceil(Math.abs(env.getMaxX() / 180.0))));
            qMinY = Math.min(
                    qMinY, (int) (Math.signum(env.getMinY()) * Math.ceil(Math.abs((env.getMinY() + 90) / 180.0))));
            qMaxY = Math.max(
                    qMaxY, (int) (Math.signum(env.getMaxY()) * Math.ceil(Math.abs((env.getMaxY() + 90) / 180.0))));
            for (int i = qMinX; i < qMaxX; i++) {
                for (int j = qMinY; j < qMaxY; j++) {
                    double minX = i * 180.0;
                    double minY = j * 180.0 - 90;
                    double maxX = minX + 180;
                    double maxY = minY + 180;
                    LinearRing ring = gf.createLinearRing(new Coordinate[] {
                        new Coordinate(minX, minY),
                        new Coordinate(minX, maxY),
                        new Coordinate(maxX, maxY),
                        new Coordinate(maxX, minY),
                        new Coordinate(minX, minY)
                    });
                    Polygon p = gf.createPolygon(ring, null);
                    Geometry intersection = p.intersection(ls);
                    if (!intersection.isEmpty()) {
                        if (intersection instanceof LineString) {
                            LineString ils = (LineString) intersection;
                            double d = getGeodeticSegmentLength(ils);
                            distance += d;
                        } else if (intersection instanceof GeometryCollection) {
                            GeometryCollection igc = (GeometryCollection) intersection;
                            for (int k = 0; k < igc.getNumGeometries(); k++) {
                                Geometry child = igc.getGeometryN(k);
                                if (child instanceof LineString) {
                                    double d = getGeodeticSegmentLength((LineString) child);
                                    distance += d;
                                }
                            }
                        }
                    }
                }
            }

            return distance;
        }
    }

    private static double getGeodeticSegmentLength(LineString ls) {
        Coordinate start = ls.getCoordinateN(0);
        Coordinate end = ls.getCoordinateN(1);
        return getGeodeticSegmentLength(start.x, start.y, end.x, end.y);
    }

    private static double getGeodeticSegmentLength(double minx, double miny, double maxx, double maxy) {
        final GeodeticCalculator calculator = new GeodeticCalculator(DefaultGeographicCRS.WGS84);
        double rminx = rollLongitude(minx);
        double rminy = rollLatitude(miny);
        double rmaxx = rollLongitude(maxx);
        double rmaxy = rollLatitude(maxy);
        calculator.setStartingGeographicPoint(rminx, rminy);
        calculator.setDestinationGeographicPoint(rmaxx, rmaxy);
        return calculator.getOrthodromicDistance();
    }

    protected static double rollLongitude(final double x) {
        double rolled = x - ((int) (x + Math.signum(x) * 180) / 360) * 360.0;
        return rolled;
    }

    protected static double rollLatitude(final double x) {
        double rolled = x - ((int) (x + Math.signum(x) * 90) / 180) * 180.0;
        return rolled;
    }

    /**
     * This worldToScreenTransform method makes the assumption that the crs is in Lon,Lat or Lat,Lon. If the provided
     * envelope does not carry along a crs the assumption that the map extent is in the classic Lon,Lat form. In case
     * the provided envelope is of type.
     *
     * <p>Note that this method takes into account also the OGC standard with respect to the relation between pixels and
     * sample.
     *
     * @param mapExtent The envelope of the map in lon,lat
     * @param paintArea The area to paint as a rectangle
     * @todo add georeferenced envelope check when merge with trunk will be performed
     */
    public static AffineTransform worldToScreenTransform(
            Envelope mapExtent, Rectangle paintArea, CoordinateReferenceSystem destinationCrs)
            throws TransformException {

        // is the crs also lon,lat?
        final CoordinateReferenceSystem crs2D = CRS.getHorizontalCRS(destinationCrs);
        if (crs2D == null)
            throw new TransformException(
                    MessageFormat.format(ErrorKeys.CANT_REDUCE_TO_TWO_DIMENSIONS_$1, destinationCrs));
        final boolean lonFirst =
                crs2D.getCoordinateSystem().getAxis(0).getDirection().absolute().equals(AxisDirection.EAST);
        final GeneralBounds newEnvelope = lonFirst
                ? new GeneralBounds(
                        new double[] {mapExtent.getMinX(), mapExtent.getMinY()},
                        new double[] {mapExtent.getMaxX(), mapExtent.getMaxY()})
                : new GeneralBounds(
                        new double[] {mapExtent.getMinY(), mapExtent.getMinX()},
                        new double[] {mapExtent.getMaxY(), mapExtent.getMaxX()});
        newEnvelope.setCoordinateReferenceSystem(destinationCrs);

        //
        // with this method I can build a world to grid transform
        // without adding half of a pixel translations. The cost
        // is a hashtable lookup. The benefit is reusing the last
        // transform (instead of creating a new one) if the grid
        // and envelope are the same one than during last invocation.
        final GridToEnvelopeMapper m = gridToEnvelopeMappers.get();
        m.setGridRange(new GridEnvelope2D(paintArea));
        m.setEnvelope(newEnvelope);
        return (AffineTransform) m.createTransform().inverse();
    }

    /**
     * Finds the centroid of the input geometry if input = point, line, polygon --> return a point that represents the
     * centroid of that geom if input = geometry collection --> return a multipoint that represents the centoid of each
     * sub-geom
     */
    public static Geometry getCentroid(Geometry g) {
        if (g instanceof Point || g instanceof MultiPoint) {
            return g;
        } else if (g instanceof GeometryCollection) {
            final GeometryCollection gc = (GeometryCollection) g;
            final Coordinate[] pts = new Coordinate[gc.getNumGeometries()];
            final int length = gc.getNumGeometries();
            for (int t = 0; t < length; t++) {
                pts[t] = pointInGeometry(gc.getGeometryN(t)).getCoordinate();
            }
            return g.getFactory().createMultiPoint(new CoordinateArraySequence(pts));
        } else if (g != null) {
            return pointInGeometry(g);
        }
        return null;
    }

    private static Geometry pointInGeometry(Geometry g) {
        Point p = g.getCentroid();
        if (g instanceof Polygon) {
            // if the geometry is heavily generalized centroid computation may fail and return NaN
            if (Double.isNaN(p.getX()) || Double.isNaN(p.getY()))
                return g.getFactory().createPoint(g.getCoordinate());
            // otherwise let's check if the point is inside. Again, this check and
            // "getInteriorPoint"
            // will work only if the geometry is valid
            if (g.isValid() && !g.contains(p)) {
                try {
                    p = g.getInteriorPoint();
                } catch (Exception e) {
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
        if (style instanceof GraphicStyle2D) {
            final BufferedImage image = ((GraphicStyle2D) style).getImage();
            return maxSize(image.getWidth(), image.getHeight());
        } else if (style instanceof IconStyle2D) {
            final Icon icon = ((IconStyle2D) style).getIcon();
            return maxSize(icon.getIconWidth(), icon.getIconHeight());
        } else if (style instanceof LineStyle2D) {
            LineStyle2D ls = (LineStyle2D) style;
            double gsSize = getStyle2DSize(ls.getGraphicStroke());
            double strokeSize = 0;
            if (ls.getStroke() instanceof BasicStroke) {
                strokeSize = ((BasicStroke) ls.getStroke()).getLineWidth();
            }
            double offset = ls.getPerpendicularOffset();
            double lineSize = maxSize(maxSize(gsSize, strokeSize), offset);
            // a MarkStyle2D is also a LineStyle2D, but we have to account for the symbol size
            if (style instanceof MarkStyle2D) {
                MarkStyle2D mark = (MarkStyle2D) style;
                return mark.getSize() + lineSize;
            } else {
                return lineSize;
            }
        } else {
            return 0;
        }
    }

    private static double maxSize(double d1, double d2) {
        if (Double.isNaN(d1)) {
            d1 = 0;
        }
        if (Double.isNaN(d2)) {
            d2 = 0;
        }
        return Math.max(d1, d2);
    }

    /**
     * Makes sure the feature collection generates the desired sourceCrs, this is mostly a workaround against feature
     * sources generating feature collections without a CRS (which is fatal to the reprojection handling later in the
     * code)
     *
     * @return FeatureCollection<SimpleFeatureType, SimpleFeature> that produces results with the correct CRS
     */
    static FeatureCollection fixFeatureCollectionReferencing(
            FeatureCollection features, CoordinateReferenceSystem sourceCrs) {
        // this is the reader's CRS
        CoordinateReferenceSystem rCS = null;
        try {
            rCS = features.getSchema().getGeometryDescriptor().getType().getCoordinateReferenceSystem();
        } catch (NullPointerException e) {
            // life sucks sometimes
        }

        if (rCS != sourceCrs && sourceCrs != null) {
            // if the datastore is producing null CRS, we recode.
            // if the datastore's CRS != real CRS, then we recode
            if (rCS == null || !CRS.equalsIgnoreMetadata(rCS, sourceCrs)) {
                // need to retag the features
                try {
                    return new ForceCoordinateSystemFeatureResults((SimpleFeatureCollection) features, sourceCrs);
                } catch (Exception ee) {
                    LOGGER.log(Level.WARNING, ee.getLocalizedMessage(), ee);
                }
            }
        }
        return features;
    }

    /**
     * Finds a centroid for a polygon catching any exceptions resulting from generalization or other polygon
     * irregularities.
     *
     * @param geom The polygon.
     * @return The polygon centroid, or null if it can't be found.
     */
    public static Point getPolygonCentroid(Polygon geom) {
        Point centroid;
        try {
            centroid = geom.getCentroid();
        } catch (Exception e) {
            // generalized polygons causes problems - this
            // tries to hide them.
            try {
                centroid = geom.getExteriorRing().getCentroid();
            } catch (Exception ee) {
                try {
                    centroid = geom.getFactory().createPoint(geom.getCoordinate());
                } catch (Exception eee) {
                    return null; // we're hooped
                }
            }
        }
        return centroid;
    }

    /**
     * Uses a sampling technique to obtain a central point that lies inside the specified polygon.
     *
     * <p>Sampling occurs horizontally along the middle of the polygon obtained from the y coordinate of the polygon
     * centroid.
     *
     * @param geom The polygon.
     * @param centroid The centroid of the polygon, can be null in which case it will be computed from
     *     {@link #getPolygonCentroid(Polygon)}.
     * @param pg The prepared version of geom, can be null in which case it will be computed on demand.
     * @param gf The geometry factory, can be null in which case the polygons factory will be used.
     * @return A central point that lies inside of the polygon, or null if one could not be found.
     */
    public static Point sampleForInternalPoint(
            Polygon geom, Point centroid, PreparedGeometry pg, GeometryFactory gf, double stepSize, int numSamples) {

        if (centroid == null) {
            centroid = getPolygonCentroid(geom);
        }
        if (pg == null) {
            pg = PreparedGeometryFactory.prepare(geom);
        }
        if (gf == null) {
            gf = geom.getFactory();
        }

        if (pg.contains(centroid)) {
            return centroid;
        }

        Envelope env = geom.getEnvelopeInternal();
        if (stepSize > 0) {
            numSamples = (int) Math.round(env.getWidth() / stepSize);
        } else if (numSamples > 0) {
            stepSize = env.getWidth() / numSamples;
        } else {
            throw new IllegalArgumentException("One of stepSize or numSamples must be greater than zero");
        }

        Coordinate c = new Coordinate();
        Point pp = gf.createPoint(c);
        c.y = centroid.getY();
        int max = -1;
        int maxIdx = -1;
        int containCounter = -1;
        for (int i = 0; i < numSamples; i++) {
            c.x = env.getMinX() + stepSize * i;
            pp.geometryChanged();
            if (!pg.contains(pp)) {
                containCounter = 0;
            } else if (i == 0) {
                containCounter = 1;
            } else {
                containCounter++;
                if (containCounter > max) {
                    max = containCounter;
                    maxIdx = i;
                }
            }
        }

        if (maxIdx != -1) {
            int midIdx = max > 1 ? maxIdx - max / 2 : maxIdx;
            c.x = env.getMinX() + stepSize * midIdx;
            pp.geometryChanged();
            return pp;
        } else {
            return null;
        }
    }
}
