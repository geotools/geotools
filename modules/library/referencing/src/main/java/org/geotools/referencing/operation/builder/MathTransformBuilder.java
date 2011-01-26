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
package org.geotools.referencing.operation.builder;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.io.Writer;
import java.io.IOException;
import java.io.StringWriter;
import java.text.NumberFormat;
import javax.vecmath.MismatchedSizeException;

import org.opengis.util.InternationalString;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.cs.CartesianCS;  // For javadoc only
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.crs.*;       // Includes imports used only for javadoc.
import org.opengis.referencing.operation.*; // Includes imports used only for javadoc.
import org.opengis.referencing.datum.DatumFactory;
import org.opengis.metadata.extent.GeographicExtent;
import org.opengis.metadata.extent.GeographicBoundingBox;
import org.opengis.metadata.quality.EvaluationMethodType;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.geometry.MismatchedReferenceSystemException;

import org.geotools.factory.Hints;
import org.geotools.io.TableWriter;
import org.geotools.math.Statistics;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.cs.DefaultCartesianCS;
import org.geotools.referencing.operation.DefaultOperationMethod;
import org.geotools.referencing.operation.DefaultTransformation;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.metadata.iso.extent.ExtentImpl;
import org.geotools.metadata.iso.extent.GeographicBoundingBoxImpl;
import org.geotools.metadata.iso.quality.PositionalAccuracyImpl;
import org.geotools.metadata.iso.quality.QuantitativeResultImpl;
import org.geotools.resources.i18n.VocabularyKeys;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.CRSUtilities;
import org.geotools.resources.Classes;


/**
 * Provides a basic implementation for {@linkplain MathTransform math transform}
 * builders.
 * 
 * Math transform builders create {@link MathTransform} objects for transforming 
 * coordinates from a source CRS 
 * ({@linkplain CoordinateReferenceSystem Coordinate Reference System}) to
 * a target CRS using empirical parameters. Usually, one of those CRS is a
 * {@linkplain GeographicCRS geographic} or {@linkplain ProjectedCRS projected}
 * one with a well known relationship to the earth. The other CRS is often an
 * {@linkplain EngineeringCRS engineering} or {@linkplain ImageCRS image} one
 * tied to some ship. For example a remote sensing image <em>before</em>
 * georectification may be referenced by an {@linkplain ImageCRS image CRS}.
 *
 * <blockquote><p><font size=-1><strong>Design note:</strong>
 * It is technically possible to reference such remote sensing images with a
 * {@linkplain DerivedCRS CRS derived} from the geographic or projected CRS,
 * where the {@linkplain DerivedCRS#getConversionFromBase conversion from base}
 * is the math transform {@linkplain #getMathTransform computed by this builder}.
 * Such approach is advantageous for {@linkplain CoordinateOperationFactory
 * coordinate operation factory} implementations, since they can determine the
 * operation just by inspection of the {@link DerivedCRS} instance. However this
 * is conceptually incorrect since {@link DerivedCRS} can be related to an other
 * CRS only through {@linkplain Conversion conversions}, which by definition are
 * accurate up to rounding errors. The operations created by math transform
 * builders are rather {@linkplain Transformation transformations}, which can't
 * be used for {@link DerivedCRS} creation.
 * </font></p></blockquote>
 *
 * The math transform from {@linkplain #getSourceCRS source CRS} to {@linkplain
 * #getTargetCRS target CRS} is calculated by {@code MathTransformBuilder} from
 * a set of {@linkplain #getMappedPositions mapped positions} in both CRS.
 * <p>
 * Subclasses must implement at least the {@link #getMinimumPointCount()} and
 * {@link #computeMathTransform()} methods.
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Jan Jezek
 * @author Martin Desruisseaux
 */
public abstract class MathTransformBuilder {
    /**
     * The list of mapped positions.
     */
    private final List<MappedPosition> positions = new ArrayList<MappedPosition>();

    /**
     * An unmodifiable view of mapped positions to be returned by {@link #getMappedPositions}.
     */
    private final List<MappedPosition> unmodifiablePositions =
            Collections.unmodifiableList(positions);

    /**
     * Coordinate Reference System of the source and target points,
     * or {@code null} if unknown.
     */
    private CoordinateReferenceSystem sourceCRS, targetCRS;

    /**
     * The math transform. Will be computed only when first needed.
     */
    private transient MathTransform transform;

    /**
     * The transformation. Will be computed only when first needed.
     */
    private transient Transformation transformation;

    /**
     * The factory to use for creating {@link MathTransform math transform} instances.
     */
    protected final MathTransformFactory mtFactory;

    /**
     * The CRS factory to use for creating {@link EngineeringCRS} instances.
     */
    private final CRSFactory crsFactory;

    /**
     * The datum factory to use for creating {@link EngineeringCRS} instances.
     */
    private final DatumFactory datumFactory;

    /**
     * Creates a builder with the default factories.
     */
    public MathTransformBuilder() {
        this(null);
    }

    /**
     * Creates a builder from the specified hints.
     */
    public MathTransformBuilder(final Hints hints) {
        mtFactory    = ReferencingFactoryFinder.getMathTransformFactory(hints);
        crsFactory   = ReferencingFactoryFinder.getCRSFactory          (hints);
        datumFactory = ReferencingFactoryFinder.getDatumFactory        (hints);
    }

    /**
     * Returns the name for the {@linkplain #getTransformation transformation} to
     * be created by this builder.
     */
    public String getName() {
        return Classes.getShortClassName(this) + " fit";
    }

    /**
     * Returns the minimum number of points required by this builder. This minimum depends on the
     * algorithm used. For example {@linkplain AffineTransformBuilder affine transform builders}
     * require at least 3 points, while {@linkplain SimilarTransformBuilder similar transform
     * builders} requires only 2 points.
     */
    public abstract int getMinimumPointCount();

    /**
     * Returns the dimension for {@linkplain #getSourceCRS source} and
     * {@link #getTargetCRS target} CRS. The default value is 2.
     */
    public int getDimension() {
        return 2;
    }

    /**
     * Returns the list of mapped positions.
     */
    public List <MappedPosition> getMappedPositions() {
        return unmodifiablePositions;
    }

    /**
     * Set the list of mapped positions.
     *
     * @throws MismatchedSizeException if the list doesn't have the expected number of points.
     * @throws MismatchedDimensionException if some points doesn't have the
     *         {@linkplain #getDimension expected number of dimensions}.
     * @throws MismatchedReferenceSystemException if CRS is not the same for all points.
     */
    public void setMappedPositions(final List<MappedPosition> positions)
            throws MismatchedSizeException, MismatchedDimensionException,
                   MismatchedReferenceSystemException
    {
        final CoordinateReferenceSystem source, target;
        source = ensureValid(getPoints(positions, false), "sourcePoints");
        target = ensureValid(getPoints(positions, true ), "targetPoints");
        /*
         * Now stores the informations. Note that we set the source and target CRS
         * only after 'ensureValid' succeed for both CRS.
         */
        this.positions.clear();
        this.positions.addAll(positions);
        this.sourceCRS = source;
        this.targetCRS = target;
        this.transform = null;
    }

    /**
     * Extracts the source or target points from the specified list.
     *
     * @param positions The array where to take points from.
     * @param target {@code false} for extracting source points,
     *        or {@code true} for extracting target points.
     */
    private static DirectPosition[] getPoints(List <MappedPosition> positions, boolean target) {
        final DirectPosition[] points = new DirectPosition[positions.size()];
        for (int i=0; i<points.length; i++) {
            final MappedPosition mp = (MappedPosition) positions.get(i);
            points[i] = target ? mp.getTarget() : mp.getSource();
        }
        return points;
    }

    /**
     * Set the source or target points. Note: {@link #sourceCRS} or {@link #targetCRS} must be
     * setup appropriately before this method is invoked.
     *
     * @param points The new points to use.
     * @param target {@code false} for setting the source points,
     *        or {@code true} for setting the target points.
     *
     * @throws MismatchedSizeException if the array doesn't have the expected number of points.
     */
    private void setPoints(final DirectPosition[] points, final boolean target)
            throws MismatchedSizeException
    {
        transform = null;
        final boolean add = positions.isEmpty();
        if (!add && points.length != positions.size()) {
            throw new MismatchedSizeException(Errors.format(ErrorKeys.MISMATCHED_ARRAY_LENGTH));
        }
        final int dimension = getDimension();
        for (int i=0; i<points.length; i++) {
            final MappedPosition mp;
            if (add) {
                mp = new MappedPosition(dimension);
                positions.add(mp);
            } else {
                mp = positions.get(i);
            }
            final DirectPosition point = points[i];
            if (target) {
                mp.setTarget(point);
            } else {
                mp.setSource(point);
            }
        }
    }

    /**
     * Returns the source points. This convenience method extracts those points from
     * the {@linkplain #getMappedPositions mapped positions}.
     */
    public DirectPosition[] getSourcePoints() {
        final DirectPosition[] points = getPoints(getMappedPositions(), false);
        assert ensureValid(points, "sourcePoints", sourceCRS);
        return points;
    }

    /**
     * Convenience method setting the {@linkplain MappedPosition#getSource source points}
     * in mapped positions.
     *
     * @param  points The source points.
     * @throws MismatchedSizeException if the list doesn't have the expected number of points.
     * @throws MismatchedDimensionException if some points doesn't have the
     *         {@linkplain #getDimension expected number of dimensions}.
     * @throws MismatchedReferenceSystemException if CRS is not the same for all points.
     */
    public void setSourcePoints(final DirectPosition[] points)
            throws MismatchedSizeException, MismatchedDimensionException,
                   MismatchedReferenceSystemException
    {
        // Set the points only after we checked them.
        sourceCRS = ensureValid(points, "sourcePoints");
        setPoints(points, false);
    }

    /**
     * Returns the target points. This convenience method extracts those points from
     * the {@linkplain #getMappedPositions mapped positions}.
     */
    public DirectPosition[] getTargetPoints() {
        final DirectPosition[] points = getPoints(getMappedPositions(), true);
        assert ensureValid(points, "targetPoints", targetCRS);
        return points;
    }

    /**
     * Convenience method setting the {@linkplain MappedPosition#getTarget target points}
     * in mapped positions.
     *
     * @param  points The target points.
     * @throws MismatchedSizeException if the list doesn't have the expected number of points.
     * @throws MismatchedDimensionException if some points doesn't have the
     *         {@linkplain #getDimension expected number of dimensions}.
     * @throws MismatchedReferenceSystemException if CRS is not the same for all points.
     */
    public void setTargetPoints(final DirectPosition[] points)
            throws MismatchedSizeException, MismatchedDimensionException,
                   MismatchedReferenceSystemException
    {
        // Set the points only after we checked them.
        targetCRS = ensureValid(points, "targetPoints");
        setPoints(points, true);
    }

    /**
     * Prints a table of all source and target points stored in this builder.
     *
     * @param  out The output device where to print all points.
     * @param  locale The locale, or {@code null} for the default.
     * @throws IOException if an error occured while printing.
     *
     * @todo Insert a double-line column separator between the source and target points.
     */
    public void printPoints(final Writer out, Locale locale) throws IOException {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        final NumberFormat source = getNumberFormat(locale, false);
        final NumberFormat target = getNumberFormat(locale, true);
        final TableWriter  table  = new TableWriter(out, TableWriter.SINGLE_VERTICAL_LINE);
        table.setAlignment(TableWriter.ALIGN_CENTER);
        table.writeHorizontalSeparator();
        try {
            final CoordinateSystem sourceCS = getSourceCRS().getCoordinateSystem();
            final CoordinateSystem targetCS = getTargetCRS().getCoordinateSystem();
            int dimension = sourceCS.getDimension();
            for (int i=0; i<dimension; i++) {
                table.write(sourceCS.getAxis(i).getName().getCode());
                table.nextColumn();
            }
            dimension = targetCS.getDimension();
            for (int i=0; i<dimension; i++) {
                table.write(targetCS.getAxis(i).getName().getCode());
                table.nextColumn();
            }
            table.writeHorizontalSeparator();
        } catch (FactoryException e) {
            /*
             * Ignore. The only consequences is that the table will not
             * contains a title line.
             */
        }
        table.setAlignment(TableWriter.ALIGN_RIGHT);
        for (final Iterator <MappedPosition> it=getMappedPositions().iterator(); it.hasNext();) {
            final MappedPosition mp = (MappedPosition) it.next();
            DirectPosition point = mp.getSource();
            int dimension = point.getDimension();
            for (int i=0; i<dimension; i++) {
                table.write(source.format(point.getOrdinate(i)));
                table.nextColumn();
            }
            point = mp.getTarget();
            dimension = point.getDimension();
            for (int i=0; i<dimension; i++) {
                table.write(target.format(point.getOrdinate(i)));
                table.nextColumn();
            }
            table.nextLine();
        }
        table.writeHorizontalSeparator();
        table.flush();
    }

    /**
     * Returns the coordinate reference system for the {@link #getSourcePoints source points}.
     * This method determines the CRS as below:
     * <p>
     * <ul>
     *   <li>If at least one source points has a CRS, then this CRS is selected
     *       as the source one and returned.</li>
     *   <li>If no source point has a CRS, then this method creates an
     *       {@linkplain EngineeringCRS engineering CRS} using the same
     *       {@linkplain CoordinateSystem coordinate system} than the one used
     *       by the {@linkplain #getTargetCRS target CRS}.</li>
     * </ul>
     *
     * @throws FactoryException if the CRS can't be created.
     */
    public CoordinateReferenceSystem getSourceCRS() throws FactoryException {
        if (sourceCRS == null) {
            sourceCRS = createEngineeringCRS(false);
        }
        assert sourceCRS.getCoordinateSystem().getDimension() == getDimension();
        return sourceCRS;
    }

    /**
     * Returns the coordinate reference system for the {@link #getTargetPoints target points}.
     * This method determines the CRS as below:
     * <p>
     * <ul>
     *   <li>If at least one target points has a CRS, then this CRS is selected
     *       as the target one and returned.</li>
     *   <li>If no target point has a CRS, then this method creates an
     *       {@linkplain EngineeringCRS engineering CRS} using the same
     *       {@linkplain CoordinateSystem coordinate system} than the one used
     *       by the {@linkplain #getSourceCRS source CRS}.</li>
     * </ul>
     *
     * @throws FactoryException if the CRS can't be created.
     */
    public CoordinateReferenceSystem getTargetCRS() throws FactoryException {
        if (targetCRS == null) {
            targetCRS = createEngineeringCRS(true);
        }
        assert targetCRS.getCoordinateSystem().getDimension() == getDimension();
        return targetCRS;
    }

    /**
     * Creates an engineering CRS using the same {@linkplain CoordinateSystem
     * coordinate system} than the existing CRS, and an area of validity
     * determined from the specified points. This method is used for creating
     * a {@linkplain #getTargetCRS target CRS} from the
     * {@linkplain #getSourceCRS source CRS}, or conversely.
     *
     * @param target {@code false} for creating the source CRS, or
     *        or {@code true} for creating the target CRS.
     * @throws FactoryException if the CRS can't be created.
     */
    private EngineeringCRS createEngineeringCRS(final boolean target) throws FactoryException {
        final Map<String,Object> properties = new HashMap<String,Object>(4);
        properties.put(CoordinateReferenceSystem.NAME_KEY, Vocabulary.format(VocabularyKeys.UNKNOW));
        final GeographicExtent validArea = getValidArea(target);
        if (validArea != null) {
            final ExtentImpl extent = new ExtentImpl();
            extent.getGeographicElements().add(validArea);
            properties.put(CoordinateReferenceSystem.DOMAIN_OF_VALIDITY_KEY, extent.unmodifiable());
        }
        final CoordinateReferenceSystem oppositeCRS = target ? sourceCRS : targetCRS;
        final CoordinateSystem cs;
        if (oppositeCRS != null) {
            cs = oppositeCRS.getCoordinateSystem();
        } else {
            switch (getDimension()) {
                case 2: cs = DefaultCartesianCS.GENERIC_2D; break;
                case 3: cs = DefaultCartesianCS.GENERIC_3D; break;
                default: throw new FactoryException(Errors.format(ErrorKeys.UNSPECIFIED_CRS));
            }
        }
        return crsFactory.createEngineeringCRS(properties,
                datumFactory.createEngineeringDatum(properties), cs);
    }

    /**
     * Returns a default format for source or target points.
     * The precision is computed from the envelope.
     */
    private NumberFormat getNumberFormat(final Locale locale, final boolean target) {
        final NumberFormat format = NumberFormat.getNumberInstance(locale);
        final GeneralEnvelope envelope = getEnvelope(target);
        double length = 0;
        for (int i=envelope.getDimension(); --i>=0;) {
            final double candidate = envelope.getLength(i);
            if (candidate > length) {
                length = candidate;
            }
        }
        if (length > 0) {
            final int digits = Math.max(0, 3 - (int) Math.ceil(Math.log10(length)));
            if (digits < 16) {
                format.setMinimumFractionDigits(digits);
                format.setMaximumFractionDigits(digits);
            }
        }
        return format;
    }

    /**
     * Returns an envelope that contains fully all the specified points.
     * If the envelope can't be calculated, then this method returns {@code null}.
     *
     * @param target {@code false} for the envelope of source points,
     *        or {@code true} for the envelope of target points.
     */
    private GeneralEnvelope getEnvelope(final boolean target) {
        GeneralEnvelope envelope = null;
        CoordinateReferenceSystem crs = null;
        for (final Iterator <MappedPosition> it=getMappedPositions().iterator(); it.hasNext();) {
            final MappedPosition mp = (MappedPosition) it.next();
            final DirectPosition point = target ? mp.getTarget() : mp.getSource();
            if (point != null) {
                if (envelope == null) {
                    final double[] coordinates = point.getCoordinates();
                    envelope = new GeneralEnvelope(coordinates, coordinates);
                } else {
                    envelope.add(point);
                }
                crs = getCoordinateReferenceSystem(point, crs);
            }
        }
        if (envelope != null) {
            envelope.setCoordinateReferenceSystem(crs);
        }
        return envelope;
    }

    /**
     * Returns a geographic extent that contains fully all the specified points.
     * If the envelope can't be calculated, then this method returns {@code null}.
     *
     * @param target {@code false} for the valid area of source points,
     *        or {@code true} for the valid area of target points.
     */
    private GeographicBoundingBox getValidArea(final boolean target) {
        GeneralEnvelope envelope = getEnvelope(target);
        if (envelope != null) try {
            return new GeographicBoundingBoxImpl(envelope);
        } catch (TransformException exception) {
            /*
             * Can't transform the envelope. Do not rethrown this exception. We don't
             * log it neither (at least not at the warning level) because this method
             * is optional.
             */
        }
        return null;
    }

    /**
     * Returns the CRS of the specified point. If the CRS of the previous point is known,
     * it can be specified. This method will then ensure that the two CRS are compatibles.
     */
    private static CoordinateReferenceSystem getCoordinateReferenceSystem(
            final DirectPosition point, CoordinateReferenceSystem previousCRS)
            throws MismatchedReferenceSystemException
    {
        final CoordinateReferenceSystem candidate = point.getCoordinateReferenceSystem();
        if (candidate != null) {
            if (previousCRS == null) {
                return candidate;
            }
            /*
             * We use strict 'equals' instead of 'equalsIgnoreCase' because if the metadata
             * are not identical, we have no easy way to choose which CRS is the "main" one.
             */
            if (!previousCRS.equals(candidate)) {
                throw new MismatchedReferenceSystemException(
                        Errors.format(ErrorKeys.MISMATCHED_COORDINATE_REFERENCE_SYSTEM));
            }
        }
        return previousCRS;
    }

    /**
     * Returns the required coordinate system type. The default implementation returns
     * {@code CoordinateSystem.class}, which means that every kind of coordinate system
     * is legal. Some subclasses will restrict to {@linkplain CartesianCS cartesian CS}.
     */
    public Class<? extends CoordinateSystem> getCoordinateSystemType() {
        return CoordinateSystem.class;
    }

    /**
     * Ensures that the specified list of points is valid, and returns their CRS.
     *
     * @param points The points to check.
     * @param label  The argument name, used for formatting error message only.
     *
     * @throws MismatchedSizeException if the list doesn't have the expected number of points.
     * @throws MismatchedDimensionException if some points doesn't have the
     *         {@linkplain #getDimension expected number of dimensions}.
     * @throws MismatchedReferenceSystemException if CRS is not the same for all points.
     * @return The CRS used for the specified points, or {@code null} if unknown.
     */
    private CoordinateReferenceSystem ensureValid(final DirectPosition[] points,
                                                  final String label)
            throws MismatchedSizeException, MismatchedDimensionException,
                   MismatchedReferenceSystemException
    {
        final int necessaryNumber = getMinimumPointCount();
        if (points.length < necessaryNumber) {
            throw new MismatchedSizeException(Errors.format(ErrorKeys.INSUFFICIENT_POINTS_$2,
                        points.length, necessaryNumber));
        }
        CoordinateReferenceSystem crs = null;
        final int dimension = getDimension();
        for (int i=0; i<points.length; i++) {
            final DirectPosition point = points[i];
            final int pointDim = point.getDimension();
            if (pointDim != dimension) {
                throw new MismatchedDimensionException(Errors.format(
                        ErrorKeys.MISMATCHED_DIMENSION_$3, label + '[' + i + ']', pointDim, dimension));
            }
            crs = getCoordinateReferenceSystem(point, crs);
        }
        if (crs != null) {
            final CoordinateSystem cs = crs.getCoordinateSystem();
            if (!getCoordinateSystemType().isAssignableFrom(cs.getClass())) {
                throw new MismatchedReferenceSystemException(Errors.format(
                        ErrorKeys.UNSUPPORTED_COORDINATE_SYSTEM_$1, cs.getName()));
            }
        }
        return crs;
    }

    /**
     * Used for assertions only.
     */
    private boolean ensureValid(final DirectPosition[] points, final String label,
                                final CoordinateReferenceSystem expected)
    {
        final CoordinateReferenceSystem actual = ensureValid(points, label);
        return actual == null || actual == expected;
    }

    /**
     * Returns statistics about the errors. The errors are computed as the distance between
     * {@linkplain #getSourcePoints source points} transformed by the math transform computed
     * by this {@code MathTransformBuilder}, and the {@linkplain #getTargetPoints target points}.
     * Use {@link Statistics#rms} for the <cite>Root Mean Squared error</cite>.
     *
     * @throws FactoryException If the math transform can't be created or used.
     */
    public Statistics getErrorStatistics() throws FactoryException {
        final MathTransform  mt     = getMathTransform();
        final Statistics     stats  = new Statistics();
        final DirectPosition buffer = new GeneralDirectPosition(getDimension());
        for (final Iterator <MappedPosition> it=getMappedPositions().iterator(); it.hasNext();) {
            final MappedPosition mp = (MappedPosition) it.next();
            /*
             * Transforms the source point using the math transform calculated by this class.
             * If the transform can't be applied, then we consider this failure as if it was
             * a factory error rather than a transformation error. This simplify the exception
             * declaration, but also has some sense on a conceptual point of view. We are
             * transforming the exact same points than the one used for creating the math
             * transform. If one of those points can't be transformed, then there is probably
             * something wrong with the transform we just created.
             */
            final double error;
            try {
                error = mp.getError(mt, buffer);
            } catch (TransformException e) {
                throw new FactoryException(Errors.format(ErrorKeys.CANT_TRANSFORM_VALID_POINTS), e);
            }
            stats.add(error);
        }
        return stats;
    }

    /**
     * Calculates the math transform immediately.
     *
     * @return Math transform from {@link #setMappedPositions MappedPosition}.
     * @throws FactoryException if the math transform can't be created.
     */
    protected abstract MathTransform computeMathTransform() throws FactoryException;

    /**
     * Returns the calculated math transform. This method {@linkplain #computeMathTransform the math
     * transform} the first time it is requested.
     *
     * @return Math transform from {@link #setMappedPositions MappedPosition}.
     * @throws FactoryException if the math transform can't be created.
     */
    public final MathTransform getMathTransform() throws FactoryException {
        if (transform == null) {
            transform = computeMathTransform();
        }
        return transform;
    }

    /**
     * Returns the coordinate operation wrapping the {@linkplain #getMathTransform() calculated
     * math transform}. The {@linkplain Transformation#getPositionalAccuracy positional
     * accuracy} will be set to the Root Mean Square (RMS) of the differences between the
     * source points transformed to the target CRS, and the expected target points.
     */
    public Transformation getTransformation() throws FactoryException {
        if (transformation == null) {
            final Map<String,Object> properties = new HashMap<String,Object>();
            properties.put(Transformation.NAME_KEY, getName());
            /*
             * Set the valid area as the intersection of source CRS and target CRS valid area.
             */
            final CoordinateReferenceSystem sourceCRS = getSourceCRS();
            final CoordinateReferenceSystem targetCRS = getTargetCRS();
            final GeographicBoundingBox sourceBox = CRS.getGeographicBoundingBox(sourceCRS);
            final GeographicBoundingBox targetBox = CRS.getGeographicBoundingBox(targetCRS);
            final GeographicBoundingBox validArea;
            if (sourceBox == null) {
                validArea = targetBox;
            } else if (targetBox == null) {
                validArea = sourceBox;
            } else {
                final GeneralEnvelope area = new GeneralEnvelope(sourceBox);
                area.intersect(new GeneralEnvelope(sourceBox));
                try {
                    validArea = new GeographicBoundingBoxImpl(area);
                } catch (TransformException e) {
                    // Should never happen, because we know that 'area' CRS is WGS84.
                    throw new AssertionError(e);
                }
            }
            if (validArea != null) {
                final ExtentImpl extent = new ExtentImpl();
                extent.getGeographicElements().add(validArea);
                properties.put(Transformation.DOMAIN_OF_VALIDITY_KEY, extent.unmodifiable());
            }
            /*
             * Computes the positional accuracy as the RMS value of differences
             * between the computed target points and the supplied target points.
             */
            final double error = getErrorStatistics().rms();
            if (!Double.isNaN(error)) {
                final InternationalString description =
                        Vocabulary.formatInternational(VocabularyKeys.ROOT_MEAN_SQUARED_ERROR);
                final QuantitativeResultImpl result = new QuantitativeResultImpl();
                result.setValues(new double[] {error});
                //result.setValueType(Double.TYPE);
                result.setValueUnit(CRSUtilities.getUnit(targetCRS.getCoordinateSystem()));
                result.setErrorStatistic(description);
                final PositionalAccuracyImpl accuracy = new PositionalAccuracyImpl(result);
                accuracy.setEvaluationMethodType(EvaluationMethodType.DIRECT_INTERNAL);
                accuracy.setEvaluationMethodDescription(description);
                properties.put(Transformation.COORDINATE_OPERATION_ACCURACY_KEY, accuracy.unmodifiable());
            }
            /*
             * Now creates the transformation.
             */
            final MathTransform transform = getMathTransform();
            transformation = new DefaultTransformation(properties, sourceCRS, targetCRS, transform,
                             new DefaultOperationMethod(transform));
        }
        return transformation;
    }

    /**
     * Returns a string representation of this builder. The default implementation
     * returns a table containing all source and target points.
     */
    @Override
    public String toString() {
        final StringWriter out = new StringWriter();
        try {
            printPoints(out, null);
        } catch (IOException e) {
            // Should never happen, since we are printing to a StringWriter.
            throw new AssertionError(e);
        }
        return out.toString();
    }
}
