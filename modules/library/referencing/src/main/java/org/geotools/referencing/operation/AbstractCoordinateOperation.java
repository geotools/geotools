/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.referencing.operation;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import org.opengis.metadata.extent.Extent;
import org.opengis.metadata.quality.Result;
import org.opengis.metadata.quality.QuantitativeResult;
import org.opengis.metadata.quality.PositionalAccuracy;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.ConcatenatedOperation;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.Transformation;
import org.opengis.referencing.operation.Operation;
import org.opengis.referencing.operation.Conversion;
import org.opengis.referencing.operation.Projection;
import org.opengis.referencing.operation.PlanarProjection;
import org.opengis.referencing.operation.CylindricalProjection;
import org.opengis.referencing.operation.ConicProjection;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.util.InternationalString;
import org.opengis.util.Record;

import org.geotools.metadata.iso.quality.PositionalAccuracyImpl;
import org.geotools.referencing.AbstractIdentifiedObject;
import org.geotools.referencing.crs.AbstractDerivedCRS;
import org.geotools.referencing.wkt.Formatter;
import org.geotools.util.Utilities;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;


/**
 * Establishes an association between a source and a target coordinate reference system,
 * and provides a {@linkplain MathTransform transform} for transforming coordinates in
 * the source CRS to coordinates in the target CRS. Many but not all coordinate operations (from
 * {@linkplain CoordinateReferenceSystem coordinate reference system} <VAR>A</VAR> to
 * {@linkplain CoordinateReferenceSystem coordinate reference system} <VAR>B</VAR>)
 * also uniquely define the inverse operation (from
 * {@linkplain CoordinateReferenceSystem coordinate reference system} <VAR>B</VAR> to
 * {@linkplain CoordinateReferenceSystem coordinate reference system} <VAR>A</VAR>).
 * In some cases, the operation method algorithm for the inverse operation is the same
 * as for the forward algorithm, but the signs of some operation parameter values must
 * be reversed. In other cases, different algorithms are required for the forward and
 * inverse operations, but the same operation parameter values are used. If (some)
 * entirely different parameter values are needed, a different coordinate operation
 * shall be defined.
 * <p>
 * This class is conceptually <cite>abstract</cite>, even if it is technically possible to
 * instantiate it. Typical applications should create instances of the most specific subclass with
 * {@code Default} prefix instead. An exception to this rule may occurs when it is not possible to
 * identify the exact type.
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class AbstractCoordinateOperation extends AbstractIdentifiedObject
        implements CoordinateOperation
{
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 1237358357729193885L;

    /**
     * An empty array of positional accuracy. This is usefull for fetching accuracies as an array,
     * using the following idiom:
     * <blockquote><pre>
     * {@linkplain #getPositionalAccuracy()}.toArray(EMPTY_ACCURACY_ARRAY);
     * </pre></blockquote>
     */
    public static final PositionalAccuracy[] EMPTY_ACCURACY_ARRAY = new PositionalAccuracy[0];

    /**
     * List of localizable properties. To be given to {@link AbstractIdentifiedObject} constructor.
     */
    private static final String[] LOCALIZABLES = {SCOPE_KEY};

    /**
     * The source CRS, or {@code null} if not available.
     */
    protected final CoordinateReferenceSystem sourceCRS;

    /**
     * The target CRS, or {@code null} if not available.
     */
    protected final CoordinateReferenceSystem targetCRS;

    /**
     * Version of the coordinate transformation
     * (i.e., instantiation due to the stochastic nature of the parameters).
     */
    final String operationVersion;

    /**
     * Estimate(s) of the impact of this operation on point accuracy, or {@code null}
     * if none.
     */
    private final Collection<PositionalAccuracy> coordinateOperationAccuracy;

    /**
     * Area in which this operation is valid, or {@code null} if not available.
     */
    protected final Extent domainOfValidity;

    /**
     * Description of domain of usage, or limitations of usage, for which this operation is valid.
     */
    private final InternationalString scope;

    /**
     * Transform from positions in the {@linkplain #getSourceCRS source coordinate reference system}
     * to positions in the {@linkplain #getTargetCRS target coordinate reference system}.
     */
    protected final MathTransform transform;

    /**
     * Constructs a new coordinate operation with the same values than the specified
     * defining conversion, together with the specified source and target CRS. This
     * constructor is used by {@link DefaultConversion} only.
     */
    AbstractCoordinateOperation(final Conversion               definition,
                                final CoordinateReferenceSystem sourceCRS,
                                final CoordinateReferenceSystem targetCRS,
                                final MathTransform             transform)
    {
        super(definition);
        this.sourceCRS                   = sourceCRS;
        this.targetCRS                   = targetCRS;
        this.operationVersion            = definition.getOperationVersion();
        this.coordinateOperationAccuracy = definition.getCoordinateOperationAccuracy();
        this.domainOfValidity            = definition.getDomainOfValidity();
        this.scope                       = definition.getScope();
        this.transform                   = transform;
    }

    /**
     * Constructs a coordinate operation from a set of properties.
     * The properties given in argument follow the same rules than for the
     * {@linkplain AbstractIdentifiedObject#AbstractIdentifiedObject(Map) super-class constructor}.
     * Additionally, the following properties are understood by this construtor:
     * <p>
     * <table border='1'>
     *   <tr bgcolor="#CCCCFF" class="TableHeadingColor">
     *     <th nowrap>Property name</th>
     *     <th nowrap>Value type</th>
     *     <th nowrap>Value given to</th>
     *   </tr>
     *   <tr>
     *     <td nowrap>&nbsp;{@value org.opengis.referencing.operation.CoordinateOperation#OPERATION_VERSION_KEY}&nbsp;</td>
     *     <td nowrap>&nbsp;{@link String}&nbsp;</td>
     *     <td nowrap>&nbsp;{@link #getOperationVersion}</td>
     *   </tr>
     *   <tr>
     *     <td nowrap>&nbsp;{@value org.opengis.referencing.operation.CoordinateOperation#COORDINATE_OPERATION_ACCURACY_KEY}&nbsp;</td>
     *     <td nowrap>&nbsp;<code>{@linkplain PositionalAccuracy}[]</code>&nbsp;</td>
     *     <td nowrap>&nbsp;{@link #getCoordinateOperationAccuracy}</td>
     *   </tr>
     *   <tr>
     *     <td nowrap>&nbsp;{@value org.opengis.referencing.operation.CoordinateOperation#DOMAIN_OF_VALIDITY_KEY}&nbsp;</td>
     *     <td nowrap>&nbsp;{@link Extent}&nbsp;</td>
     *     <td nowrap>&nbsp;{@link #getDomainOfValidity}</td>
     *   </tr>
     *   <tr>
     *     <td nowrap>&nbsp;{@value org.opengis.referencing.operation.CoordinateOperation#SCOPE_KEY}&nbsp;</td>
     *     <td nowrap>&nbsp;{@link String} or {@link InternationalString}&nbsp;</td>
     *     <td nowrap>&nbsp;{@link #getScope}</td>
     *   </tr>
     * </table>
     *
     * @param properties Set of properties. Should contains at least {@code "name"}.
     * @param sourceCRS The source CRS.
     * @param targetCRS The target CRS.
     * @param transform Transform from positions in the {@linkplain #getSourceCRS source CRS}
     *                  to positions in the {@linkplain #getTargetCRS target CRS}.
     */
    public AbstractCoordinateOperation(final Map<String,?>            properties,
                                       final CoordinateReferenceSystem sourceCRS,
                                       final CoordinateReferenceSystem targetCRS,
                                       final MathTransform             transform)
    {
        this(properties, new HashMap<String,Object>(), sourceCRS, targetCRS, transform);
    }

    /**
     * Work around for RFE #4093999 in Sun's bug database
     * ("Relax constraint on placement of this()/super() call in constructors").
     */
    private AbstractCoordinateOperation(final Map<String,?>            properties,
                                        final Map<String,Object>    subProperties,
                                        final CoordinateReferenceSystem sourceCRS,
                                        final CoordinateReferenceSystem targetCRS,
                                        final MathTransform             transform)
    {
        super(properties, subProperties, LOCALIZABLES);
        PositionalAccuracy[] positionalAccuracy;
        domainOfValidity   = (Extent)               subProperties.get(DOMAIN_OF_VALIDITY_KEY);
        scope              = (InternationalString)  subProperties.get(SCOPE_KEY);
        operationVersion   = (String)               subProperties.get(OPERATION_VERSION_KEY);
        positionalAccuracy = (PositionalAccuracy[]) subProperties.get(COORDINATE_OPERATION_ACCURACY_KEY);
        if (positionalAccuracy==null || positionalAccuracy.length==0) {
            positionalAccuracy = null;
        } else {
            positionalAccuracy = positionalAccuracy.clone();
            for (int i=0; i<positionalAccuracy.length; i++) {
                ensureNonNull(COORDINATE_OPERATION_ACCURACY_KEY, positionalAccuracy, i);
            }
        }
        this.coordinateOperationAccuracy = asSet(positionalAccuracy);
        this.sourceCRS = sourceCRS;
        this.targetCRS = targetCRS;
        this.transform = transform;
        validate();
    }

    /**
     * Checks the validity of this operation. This method is invoked by the constructor after
     * every fields have been assigned. It can be overriden by subclasses if different rules
     * should be applied.
     * <p>
     * {@link DefaultConversion} overrides this method in order to allow null values, providing
     * that all of {@code transform}, {@code sourceCRS} and {@code targetCRS} are null together.
     * Note that null values are not allowed for transformations, so {@link DefaultTransformation}
     * does not override this method.
     *
     * @throws IllegalArgumentException if at least one of {@code transform}, {@code sourceCRS}
     *         or {@code targetCRS} is invalid. We throw this kind of exception rather than
     *         {@link IllegalStateException} because this method is invoked by the constructor
     *         for checking argument validity.
     */
    void validate() throws IllegalArgumentException {
        ensureNonNull ("sourceCRS", transform);
        ensureNonNull ("targetCRS", transform);
        ensureNonNull ("transform", transform);
        checkDimension("sourceCRS", sourceCRS, transform.getSourceDimensions());
        checkDimension("targetCRS", targetCRS, transform.getTargetDimensions());
    }

    /**
     * Checks if a reference coordinate system has the expected number of dimensions.
     *
     * @param name     The argument name.
     * @param crs      The coordinate reference system to check.
     * @param expected The expected number of dimensions.
     */
    private static void checkDimension(final String name,
                                       final CoordinateReferenceSystem crs,
                                       final int expected)
    {
        final int actual = crs.getCoordinateSystem().getDimension();
        if (actual != expected) {
            throw new IllegalArgumentException(Errors.format(
                    ErrorKeys.MISMATCHED_DIMENSION_$3, name, actual, expected));
        }
    }

    /**
     * Returns the source CRS.
     */
    public CoordinateReferenceSystem getSourceCRS() {
        return sourceCRS;
    }

    /**
     * Returns the target CRS.
     */
    public CoordinateReferenceSystem getTargetCRS() {
        return targetCRS;
    }

    /**
     * Version of the coordinate transformation (i.e., instantiation due to the stochastic
     * nature of the parameters). Mandatory when describing a transformation, and should not
     * be supplied for a conversion.
     *
     * @return The coordinate operation version, or {@code null} in none.
     */
    public String getOperationVersion() {
        return operationVersion;
    }

    /**
     * Estimate(s) of the impact of this operation on point accuracy. Gives
     * position error estimates for target coordinates of this coordinate
     * operation, assuming no errors in source coordinates.
     *
     * @return The position error estimates, or an empty collection if not available.
     *
     * @see #getAccuracy()
     *
     * @since 2.4
     */
    public Collection<PositionalAccuracy> getCoordinateOperationAccuracy() {
        if (coordinateOperationAccuracy == null) {
            return Collections.emptySet();
        }
        return coordinateOperationAccuracy;
    }

    /**
     * Estimate(s) of the impact of this operation on point accuracy. Gives
     * position error estimates for target coordinates of this coordinate
     * operation, assuming no errors in source coordinates.
     *
     * @return The position error estimates, or an empty collection if not available.
     *
     * @see #getAccuracy()
     *
     * @deprecated Renamed as {@link #getCoordinateOperationAccuracy}.
     */
    public Collection<PositionalAccuracy> getPositionalAccuracy() {
        if (coordinateOperationAccuracy == null) {
            return Collections.emptySet();
        }
        return coordinateOperationAccuracy;
    }

    /**
     * Convenience method returning the accuracy in meters. The default implementation delegates
     * to <code>{@linkplain #getAccuracy(CoordinateOperation) getAccuracy}(this)</code>. Subclasses
     * should override this method if they can provide a more accurate algorithm.
     *
     * @return The accuracy in meters, or NaN if unknown.
     *
     * @since 2.2
     */
    public double getAccuracy() {
        return getAccuracy0(this);
    }

    /**
     * Convenience method returning the accuracy in meters for the specified operation. This method
     * try each of the following procedures and returns the first successful one:
     *
     * <ul>
     *   <li>If a {@linkplain QuantitativeResult quantitative} positional accuracy is found with a
     *       linear unit, then this accuracy estimate is converted to {@linkplain SI#METER meters}
     *       and returned.</li>
     *
     *   <li>Otherwise, if the operation is a {@linkplain Conversion conversion}, then returns
     *       0 since a conversion is by definition accurates up to rounding errors.</li>
     *
     *   <li>Otherwise, if the operation is a {@linkplain Transformation transformation}, then
     *       checks if the datum shift were applied with the help of Bursa-Wolf parameters.
     *       This procedure looks for Geotools-specific
     *       {@link PositionalAccuracyImpl#DATUM_SHIFT_APPLIED DATUM_SHIFT_APPLIED} and
     *       {@link PositionalAccuracyImpl#DATUM_SHIFT_OMITTED DATUM_SHIFT_OMITTED} metadata.
     *       If a datum shift has been applied, returns 25 meters. If a datum shift should have
     *       been applied but has been omitted, returns 1000 meters. The 1000 meters value is
     *       higher than the highest value (999 meters) found in the EPSG database version 6.7.
     *       The 25 meters value is the next highest value found in the EPSG database for a
     *       significant number of transformations.
     *
     *   <li>Otherwise, if the operation is a {@linkplain ConcatenatedOperation concatenated one},
     *       returns the sum of the accuracy of all components.</li>
     * </ul>
     *
     * @param  operation The operation to inspect for accuracy.
     * @return The accuracy estimate (always in meters), or NaN if unknow.
     *
     * @since 2.2
     */
    public static double getAccuracy(final CoordinateOperation operation) {
        if (operation instanceof AbstractCoordinateOperation) {
            // Maybe the user overridden this method...
            return ((AbstractCoordinateOperation) operation).getAccuracy();
        }
        return getAccuracy0(operation);
    }

    /**
     * Implementation of {@code getAccuracy} methods, both the ordinary and the
     * static member variants. The {@link #getAccuracy()} method can't invoke
     * {@link #getAccuracy(CoordinateOperation)} directly since it would cause
     * never-ending recursive calls.
     */
    private static double getAccuracy0(final CoordinateOperation operation) {
        final Collection<PositionalAccuracy> accuracies = operation.getCoordinateOperationAccuracy();
        if (accuracies != null) for (final PositionalAccuracy accuracy : accuracies) {
            if (accuracy != null) for (final Result result : accuracy.getResults()) {
                if (result instanceof QuantitativeResult) {
                    final QuantitativeResult quantity = (QuantitativeResult) result;
                    final Collection<? extends Record> records = quantity.getValues();
                    if (records != null) {
                        final Unit<?> unit = quantity.getValueUnit();
                        if (unit!=null && SI.METER.isCompatible(unit)) {
                            for (final Record record : records) {
                                for (final Object value : record.getAttributes().values()) {
                                    if (value instanceof Number) {
                                        double v = ((Number) value).doubleValue();
                                        v = unit.getConverterTo(SI.METER).convert(v);
                                        return v;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        /*
         * No quantitative, linear accuracy were found. If the coordinate operation is actually
         * a conversion, the accuracy is up to rounding error (i.e. conceptually 0) by definition.
         */
        if (operation instanceof Conversion) {
            return 0;
        }
        /*
         * If the coordinate operation is actually a transformation, checks if Bursa-Wolf
         * parameters were available for the datum shift. This is Geotools-specific.
         * See javadoc for a rational about the return values choosen.
         */
        if (operation instanceof Transformation) {
            if (!accuracies.contains(PositionalAccuracyImpl.DATUM_SHIFT_OMITTED)) {
                if (accuracies.contains(PositionalAccuracyImpl.DATUM_SHIFT_APPLIED)) {
                    return 25;
                }
            }
            return 1000;
        }
        /*
         * If the coordinate operation is a compound of other coordinate operations, returns
         * the sum of their accuracy, skipping unknow ones.
         */
        double accuracy = Double.NaN;
        if (operation instanceof ConcatenatedOperation) {
            final Collection components = ((ConcatenatedOperation) operation).getOperations();
            for (final Iterator it=components.iterator(); it.hasNext();) {
                final double candidate = Math.abs(getAccuracy((CoordinateOperation) it.next()));
                if (!Double.isNaN(candidate)) {
                    if (Double.isNaN(accuracy)) {
                        accuracy = candidate;
                    } else {
                        accuracy += candidate;
                    }
                }
            }
        }
        return accuracy;
    }

    /**
     * Area or region or timeframe in which this coordinate operation is valid.
     * Returns {@code null} if not available.
     *
     * @since 2.4
     */
    public Extent getDomainOfValidity() {
        return domainOfValidity;
    }

    /**
     * Area in which this operation is valid.
     *
     * @return Coordinate operation valid area, or {@code null} if not available.
     *
     * @deprecated Renamed {@link #getDomainOfValidity}.
     */
    public Extent getValidArea() {
        return domainOfValidity;
    }

    /**
     * Description of domain of usage, or limitations of usage, for which this operation is valid.
     */
    public InternationalString getScope() {
        return scope;
    }

    /**
     * Gets the math transform. The math transform will transform positions in the
     * {@linkplain #getSourceCRS source coordinate reference system} into positions
     * in the {@linkplain #getTargetCRS target coordinate reference system}.
     */
    public MathTransform getMathTransform() {
        return transform;
    }

    /**
     * Returns the most specific GeoAPI interface implemented by the specified operation.
     *
     * @param  object A coordinate operation.
     * @return The most specific GeoAPI interface
     *         (e.g. <code>{@linkplain Transformation}.class</code>).
     */
    public static Class<? extends CoordinateOperation> getType(final CoordinateOperation object) {
        if (object instanceof        Transformation) return        Transformation.class;
        if (object instanceof       ConicProjection) return       ConicProjection.class;
        if (object instanceof CylindricalProjection) return CylindricalProjection.class;
        if (object instanceof      PlanarProjection) return      PlanarProjection.class;
        if (object instanceof            Projection) return            Projection.class;
        if (object instanceof            Conversion) return            Conversion.class;
        if (object instanceof             Operation) return             Operation.class;
        return CoordinateOperation.class;
    }

    /**
     * Compares this coordinate operation with the specified object for equality.
     * If {@code compareMetadata} is {@code true}, then all available properties are
     * compared including {@linkplain #getDomainOfValidity domain of validity} and
     * {@linkplain #getScope scope}.
     *
     * @param  object The object to compare to {@code this}.
     * @param  compareMetadata {@code true} for performing a strict comparaison, or
     *         {@code false} for comparing only properties relevant to transformations.
     * @return {@code true} if both objects are equal.
     */
    @Override
    public boolean equals(final AbstractIdentifiedObject object, final boolean compareMetadata) {
        if (object == this) {
            return true; // Slight optimization.
        }
        if (super.equals(object, compareMetadata)) {
            final AbstractCoordinateOperation that = (AbstractCoordinateOperation) object;
            if (equals(this.sourceCRS, that.sourceCRS, compareMetadata) &&
                Utilities.equals(this.transform, that.transform))
                // See comment in DefaultOperation.equals(...) about why we compare MathTransform.
            {
                if (compareMetadata) {
                    if (!Utilities.equals(this.domainOfValidity, that.domainOfValidity) ||
                        !Utilities.equals(this.scope, that.scope) ||
                        !Utilities.equals(this.coordinateOperationAccuracy, that.coordinateOperationAccuracy))
                    {
                        return false;
                    }
                }
                /*
                 * Avoid never-ending recursivity: AbstractDerivedCRS has a 'conversionFromBase'
                 * field that is set to this AbstractCoordinateOperation.
                 */
                final Boolean comparing = AbstractDerivedCRS._COMPARING.get();
                if (comparing!=null && comparing.booleanValue()) {
                    return true;
                }
                try {
                    AbstractDerivedCRS._COMPARING.set(Boolean.TRUE);
                    return equals(this.targetCRS, that.targetCRS, compareMetadata);
                } finally {
                    AbstractDerivedCRS._COMPARING.set(Boolean.FALSE);
                }
            }
        }
        return false;
    }

    /**
     * Returns a hash code value for this coordinate operation.
     */
    @Override
    public int hashCode() {
        int code = (int)serialVersionUID;
        if (sourceCRS != null) code ^= sourceCRS.hashCode();
        if (targetCRS != null) code ^= targetCRS.hashCode();
        if (transform != null) code ^= transform.hashCode();
        return code;
    }

    /**
     * Format this operation as a pseudo-WKT format. No WKT format were defined for coordinate
     * operation at the time this method was written. This method may change in any future version
     * until a standard format is found.
     *
     * @param  formatter The formatter to use.
     * @return The WKT element name.
     */
    @Override
    protected String formatWKT(final Formatter formatter) {
        append(formatter, sourceCRS, "SOURCE");
        append(formatter, targetCRS, "TARGET");
        return super.formatWKT(formatter);
    }

    /**
     * Append the identifier for the specified object name (possibly {@code null}) to the specified
     * formatter.
     *
     * @param formatter The formatter where to append the object name.
     * @param object    The object to append, or {@code null} if none.
     * @param type      The label to put in front of the object name.
     */
    @SuppressWarnings("serial")
    static void append(final Formatter formatter, final IdentifiedObject object, final String type) {
        if (object != null) {
            final Map<String,Object> properties = new HashMap<String,Object>(4);
            properties.put(IdentifiedObject.NAME_KEY,        formatter.getName(object));
            properties.put(IdentifiedObject.IDENTIFIERS_KEY, formatter.getIdentifier(object));
            formatter.append((IdentifiedObject) new AbstractIdentifiedObject(properties) {
                @Override
                protected String formatWKT(final Formatter formatter) {
                    /*
                     * Do not invoke super.formatWKT(formatter), since it doesn't do anything
                     * more than invoking 'formatter.setInvalidWKT(...)' (we ignore the value
                     * returned). This method will rather be invoked by the enclosing class.
                     */
                    return type;
                }
            });
        }
    }
}
