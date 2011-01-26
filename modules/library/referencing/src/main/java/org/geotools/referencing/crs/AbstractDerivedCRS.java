/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.crs;

import java.util.Map;

import org.opengis.referencing.crs.SingleCRS;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.crs.GeneralDerivedCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.datum.Datum;
import org.opengis.referencing.operation.Conversion;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.opengis.referencing.operation.OperationMethod;
import org.opengis.referencing.operation.Projection;
import org.opengis.geometry.MismatchedDimensionException;

import org.geotools.referencing.AbstractIdentifiedObject;
import org.geotools.referencing.operation.DefaultOperation;
import org.geotools.referencing.operation.DefaultConversion;
import org.geotools.referencing.operation.DefiningConversion;  // For javadoc
import org.geotools.referencing.operation.DefaultOperationMethod;
import org.geotools.referencing.wkt.Formatter;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;


/**
 * A coordinate reference system that is defined by its coordinate
 * {@linkplain Conversion conversion} from another coordinate reference system
 * (not by a {@linkplain Datum datum}).
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
public class AbstractDerivedCRS extends AbstractSingleCRS implements GeneralDerivedCRS {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -175151161496419854L;

    /**
     * Key for the <code>{@value}</code> property to be given to the constructor. The value should
     * be one of <code>{@linkplain org.opengis.referencing.operation.PlanarProjection}.class</code>,
     * <code>{@linkplain org.opengis.referencing.operation.CylindricalProjection}.class</code> or
     * <code>{@linkplain org.opengis.referencing.operation.ConicProjection}.class</code>.
     * <p>
     * This is a Geotools specific property used as a hint for creating a {@linkplain Projection
     * projection} of proper type from a {@linkplain DefiningConversion defining conversion}. In
     * many cases, this hint is not needed since Geotools is often capable to infer it. This hint is
     * used mostly by advanced factories like the {@linkplain org.geotools.referencing.factory.epsg
     * EPSG backed} one.
     *
     * @see DefaultConversion#create
     *
     * @since 2.4
     */
    public static final String CONVERSION_TYPE_KEY = "conversionType";

    /**
     * A lock for avoiding never-ending recursivity in the {@code equals} method. This field
     * contains a {@code boolean} flag set to {@code true} when a comparaison is in progress.
     * This lock is necessary because {@code AbstractDerivedCRS} objects contain a
     * {@link #conversionFromBase} field, which contains a {@link DefaultConversion#targetCRS}
     * field set to this {@code AbstractDerivedCRS} object.
     * <P>
     * <STRONG>DO NOT USE THIS FIELD. It is strictly for internal use by {@link #equals} and
     * {@link org.geotools.referencing.operation.AbstractCoordinateOperation#equals} methods.</STRONG>
     *
     * @todo Hide this field from the javadoc. It is not possible to make it package-privated
     *       because {@link org.geotools.referencing.operation.AbstractCoordinateOperation}
     *       lives in a different package.
     */
    public static final ThreadLocal<Boolean> _COMPARING = new ThreadLocal<Boolean>();

    /**
     * The base coordinate reference system.
     */
    protected final CoordinateReferenceSystem baseCRS;

    /**
     * The conversion from the {@linkplain #getBaseCRS base CRS} to this CRS.
     */
    protected final Conversion conversionFromBase;

    /**
     * Constructs a new derived CRS with the same values than the specified one.
     * This copy constructor provides a way to wrap an arbitrary implementation into a
     * Geotools one or a user-defined one (as a subclass), usually in order to leverage
     * some implementation-specific API. This constructor performs a shallow copy,
     * i.e. the properties are not cloned.
     *
     * @param crs The coordinate reference system to copy.
     *
     * @since 2.2
     */
    protected AbstractDerivedCRS(final GeneralDerivedCRS crs) {
        super(crs);
        baseCRS            = crs.getBaseCRS();
        conversionFromBase = crs.getConversionFromBase();
    }

    /**
     * Constructs a derived CRS from a {@linkplain DefiningConversion defining conversion}.
     * The properties are given unchanged to the
     * {@linkplain org.geotools.referencing.AbstractReferenceSystem#AbstractReferenceSystem(Map)
     * super-class constructor}.
     *
     * @param  properties Name and other properties to give to the new derived CRS object.
     * @param  conversionFromBase The {@linkplain DefiningConversion defining conversion}.
     * @param  base Coordinate reference system to base the derived CRS on.
     * @param  baseToDerived The transform from the base CRS to returned CRS.
     * @param  derivedCS The coordinate system for the derived CRS. The number
     *         of axes must match the target dimension of the transform
     *         {@code baseToDerived}.
     * @throws MismatchedDimensionException if the source and target dimension of
     *         {@code baseToDerived} don't match the dimension of {@code base}
     *         and {@code derivedCS} respectively.
     */
    protected AbstractDerivedCRS(final Map<String,?>       properties,
                                 final Conversion  conversionFromBase,
                                 final CoordinateReferenceSystem base,
                                 final MathTransform    baseToDerived,
                                 final CoordinateSystem     derivedCS)
            throws MismatchedDimensionException
    {
        super(properties, getDatum(base), derivedCS);
        ensureNonNull("conversionFromBase", conversionFromBase);
        ensureNonNull("baseToDerived",      baseToDerived);
        this.baseCRS = base;
        checkDimensions(base, baseToDerived, derivedCS);
        DefaultOperationMethod.checkDimensions(conversionFromBase.getMethod(), baseToDerived);
        final Class<?> c = (Class<?>) properties.get(CONVERSION_TYPE_KEY);
        Class<? extends Conversion> typeHint = getConversionType();
        if (c != null) {
            typeHint = c.asSubclass(typeHint);
        }
        this.conversionFromBase = DefaultConversion.create(
            /* definition */ conversionFromBase,
            /* sourceCRS  */ base,
            /* targetCRS  */ this,
            /* transform  */ baseToDerived,
            /* typeHints  */ typeHint);
    }

    /**
     * Constructs a derived CRS from a set of properties. A {@linkplain DefaultOperationMethod
     * default operation method} is inferred from the {@linkplain MathTransform math transform}.
     * This is a convenience constructor that is not garanteed to work reliably for non-GeoTools
     * implementations. Use the constructor expecting a {@linkplain DefiningConversion defining
     * conversion} for more determinist result.
     * <p>
     * The properties are given unchanged to the
     * {@linkplain org.geotools.referencing.AbstractReferenceSystem#AbstractReferenceSystem(Map)
     * super-class constructor}. The following optional properties are also understood:
     * <p>
     * <table border='1'>
     *   <tr bgcolor="#CCCCFF" class="TableHeadingColor">
     *     <th nowrap>Property name</th>
     *     <th nowrap>Value type</th>
     *     <th nowrap>Value given to</th>
     *   </tr>
     *   <tr>
     *     <td nowrap>&nbsp;<code>"conversion.name"</code>&nbsp;</td>
     *     <td nowrap>&nbsp;{@link String}&nbsp;</td>
     *     <td nowrap>&nbsp;<code>{@linkplain #getConversionFromBase}.getName()</code></td>
     *   </tr>
     * </table>
     * <p>
     * Additional properties for the {@link DefaultConversion} object to be created can be
     * specified with the <code>"conversion."</code> prefix added in front of property names
     * (example: <code>"conversion.remarks"</code>). The same applies for operation method,
     * using the <code>"method."</code> prefix.
     *
     * @param  properties Name and other properties to give to the new derived CRS object and to
     *         the underlying {@linkplain DefaultConversion conversion}.
     * @param  base Coordinate reference system to base the derived CRS on.
     * @param  baseToDerived The transform from the base CRS to returned CRS.
     * @param  derivedCS The coordinate system for the derived CRS. The number
     *         of axes must match the target dimension of the transform
     *         {@code baseToDerived}.
     * @throws MismatchedDimensionException if the source and target dimension of
     *         {@code baseToDerived} don't match the dimension of {@code base}
     *         and {@code derivedCS} respectively.
     *
     * @since 2.5
     */
    protected AbstractDerivedCRS(final Map<String,?>       properties,
                                 final CoordinateReferenceSystem base,
                                 final MathTransform    baseToDerived,
                                 final CoordinateSystem     derivedCS)
            throws MismatchedDimensionException
    {
        this(properties, new DefaultOperationMethod(baseToDerived), base, baseToDerived, derivedCS);
    }

    /**
     * @deprecated Create explicitly a {@link DefiningConversion} instead.
     *
     * @todo Move the implementation in the previous constructor after we removed the deprecated
     *       signature.
     */
    AbstractDerivedCRS(final Map<String,?>       properties,
                       final OperationMethod         method,
                       final CoordinateReferenceSystem base,
                       final MathTransform    baseToDerived,
                       final CoordinateSystem     derivedCS)
            throws MismatchedDimensionException
    {
        super(properties, getDatum(base), derivedCS);
        ensureNonNull("method",        method);
        ensureNonNull("baseToDerived", baseToDerived);
        this.baseCRS = base;
        /*
         * A method was explicitly specified. Make sure that the source and target
         * dimensions match. We do not check parameters in current version of this
         * implementation (we may add this check in a future version), since the
         * descriptors provided in this user-supplied OperationMethod may be more
         * accurate than the one inferred from the MathTransform.
         */
        checkDimensions(base, baseToDerived, derivedCS);
        DefaultOperationMethod.checkDimensions(method, baseToDerived);
        this.conversionFromBase = (Conversion) DefaultOperation.create(
            /* properties */ new UnprefixedMap(properties, "conversion."),
            /* sourceCRS  */ base,
            /* targetCRS  */ this,
            /* transform  */ baseToDerived,
            /* method     */ method,
            /* type       */ (this instanceof ProjectedCRS) ? Projection.class : Conversion.class);
    }

    /**
     * Work around for RFE #4093999 in Sun's bug database
     * ("Relax constraint on placement of this()/super() call in constructors").
     *
     * @todo What to do if {@code base} is not an instance of {@link SingleCRS}?
     */
    private static Datum getDatum(final CoordinateReferenceSystem base) {
        ensureNonNull("base",  base);
        return (base instanceof SingleCRS) ? ((SingleCRS) base).getDatum() : null;
    }

    /**
     * Checks consistency between the base CRS and the "base to derived" transform.
     */
    private static void checkDimensions(final CoordinateReferenceSystem base,
                                        final MathTransform    baseToDerived,
                                        final CoordinateSystem     derivedCS)
            throws MismatchedDimensionException
    {
        final int dimSource = baseToDerived.getSourceDimensions();
        final int dimTarget = baseToDerived.getTargetDimensions();
        int dim1, dim2;
        if ((dim1=dimSource) != (dim2=base.getCoordinateSystem().getDimension()) ||
            (dim1=dimTarget) != (dim2=derivedCS.getDimension()))
        {
            throw new MismatchedDimensionException(Errors.format(
                    ErrorKeys.MISMATCHED_DIMENSION_$2, dim1, dim2));
        }
    }

    /**
     * Returns the base coordinate reference system.
     *
     * @return The base coordinate reference system.
     */
    public CoordinateReferenceSystem getBaseCRS() {
        return baseCRS;
    }

    /**
     * Returns the conversion from the {@linkplain #getBaseCRS base CRS} to this CRS.
     *
     * @return The conversion to this CRS.
     */
    public Conversion getConversionFromBase() {
        return conversionFromBase;
    }

    /**
     * Returns the expected type of conversion.
     * {@link DefaultProjectedCRS} will override this type with {@link Projection}.
     */
    Class<? extends Conversion> getConversionType() {
        return Conversion.class;
    }

    /**
     * Compare this coordinate reference system with the specified object for equality.
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
            final AbstractDerivedCRS that = (AbstractDerivedCRS) object;
            if (equals(this.baseCRS, that.baseCRS, compareMetadata)) {
                /*
                 * Avoid never-ending recursivity: Conversion has a 'targetCRS' field (inherited from
                 * the AbstractCoordinateOperation super-class) that is set to this AbstractDerivedCRS.
                 */
                final Boolean comparing = _COMPARING.get();
                if (comparing!=null && comparing.booleanValue()) {
                    return true;
                }
                try {
                    _COMPARING.set(Boolean.TRUE);
                    return equals(this.conversionFromBase,
                                  that.conversionFromBase,
                                  compareMetadata);
                } finally {
                    _COMPARING.remove();
                }
            }
        }
        return false;
    }

    /**
     * Returns a hash value for this derived CRS.
     *
     * @return The hash code value. This value doesn't need to be the same
     *         in past or future versions of this class.
     */
    @Override
    public int hashCode() {
        /*
         * Do not invoke 'conversionFromBase.hashCode()' in order to avoid a never-ending loop.
         * This is because Conversion has a 'sourceCRS' field (in the AbstractCoordinateOperation
         * super-class), which is set to this AbstractDerivedCRS. Checking the identifier should
         * be enough.
         */
        return (int)serialVersionUID ^ baseCRS.hashCode() ^ conversionFromBase.getName().hashCode();
    }

    /**
     * Format the inner part of a
     * <A HREF="http://geoapi.sourceforge.net/snapshot/javadoc/org/opengis/referencing/doc-files/WKT.html"><cite>Well
     * Known Text</cite> (WKT)</A> element.
     *
     * @param  formatter The formatter to use.
     * @return The name of the WKT element type, which is {@code "FITTED_CS"}.
     */
    @Override
    protected String formatWKT(final Formatter formatter) {
        MathTransform inverse = conversionFromBase.getMathTransform();
        try {
            inverse = inverse.inverse();
        } catch (NoninvertibleTransformException exception) {
            // TODO: provide a more accurate error message.
            throw new IllegalStateException(exception.getLocalizedMessage(), exception);
        }
        formatter.append(inverse);
        formatter.append(baseCRS);
        return "FITTED_CS";
    }
}
