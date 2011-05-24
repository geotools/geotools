/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.metadata.identification;

import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.MANDATORY;
import static org.opengis.annotation.Specification.ISO_19115;


/**
 * A scale defined as the inverse of a denominator. This is derived from ISO 19103 {@code Scale}
 * where {@linkplain #getDenominator denominator} = 1 / <var>scale</var>.
 * <p>
 * Implementations are encouraged to extend {@link Number} in a manner equivalent to:
 *
 * <blockquote><pre>
 *  class MyRepresentedFraction extends Number implements RepresentedFraction {
 *      ...
 *      public double doubleValue() {
 *          return 1.0 / (double) denominator;
 *      }
 *      public float floatValue() {
 *          return 1.0f / (float) denominator;
 *      }
 *      public long longValue() {
 *          return 1 / denominator; // Result is zero except for denominator=[0,1].
 *      }
 *      ...
 *  }
 * </pre></blockquote>
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/metadata/identification/RepresentativeFraction.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Ely Conn (Leica Geosystems Geospatial Imaging, LLC)
 * @since   GeoAPI 2.1
 */
@UML(identifier="MD_RepresentativeFraction", specification=ISO_19115)
public interface RepresentativeFraction {
    /**
     * @deprecated Replaced by {@link #doubleValue}, which is both consistent with
     *  {@link java.lang.Number} naming and avoid the idea that a representative
     *  fraction is only for scales - it could be used for any quantity conveniently
     *  represented as a ratio.
     */
    @Deprecated
    double toScale();

    /**
     * Returns the scale value in a form usable for computation.
     *
     * @return {@code 1.0 / (double) {@linkplain #getDenominator()}.
     *
     * @since GeoAPI 2.2
     */
    double doubleValue();

    /**
     * The number below the line in a vulgar fraction.
     *
     * @return The denominator.
     */
    @UML(identifier = "denominator", obligation = MANDATORY, specification = ISO_19115)
    long getDenominator();

    /**
     * Compares this representative fraction with the specified object for equality.
     * {@code RepresentativeFraction} is a data object - {@code equals} is defined
     * acoording to {@link #getDenominator};
     * <p>
     * Implementations should match the following:
     *
     * <blockquote><pre>
     * public boolean equals(Object object) {
     *     if (object instanceof RepresentativeFraction) {
     *         final RepresentativeFraction that = (RepresentativeFraction) object;
     *         return getDenominator() == that.getDenominator();
     *     }
     *     return false;
     * }
     * </pre></blockquote>
     *
     * @param other The object to compare with.
     * @return {@code true} if {@code other} is a {@code RepresentedFraction} with the same
     *         {@linkplain #getDenominator denominator} value.
     */
    ///@Override
    boolean equals(Object other);

    /**
     * Returns a hash value for this representative fraction.
     * {@code RepresentativeFraction} is a data object - {@code hashcode} is defined
     * according to {@link #getDenominator}.
     * <p>
     * Implementations should match the following:
     *
     * <blockquote><pre>
     * public int hashCode() {
     *     return (int) getDenominator();
     * }
     * </pre></blockquote>
     *
     * @return A hash code value for this representative fraction.
     */
    ///@Override
    int hashCode();
}
