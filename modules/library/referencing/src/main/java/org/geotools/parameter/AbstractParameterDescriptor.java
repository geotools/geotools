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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.parameter;

import java.util.Map;

import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.GeneralParameterDescriptor;
import org.geotools.referencing.AbstractIdentifiedObject;
import org.geotools.referencing.wkt.Formatter;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;


/**
 * Abstract definition of a parameter or group of parameters used by an operation method.
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @see AbstractParameter
 */
public abstract class AbstractParameterDescriptor extends AbstractIdentifiedObject
           implements GeneralParameterDescriptor
{
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -2630644278783845276L;

    /**
     * The minimum number of times that values for this parameter group or
     * parameter are required.
     */
    private final int minimumOccurs;

    /**
     * Constructs a descriptor with the same values than the specified one. This copy constructor
     * may be used in order to wraps an arbitrary implementation into a Geotools one.
     *
     * @since 2.2
     */
    protected AbstractParameterDescriptor(final GeneralParameterDescriptor descriptor) {
        super(descriptor);
        minimumOccurs = descriptor.getMinimumOccurs();
    }

    /**
     * Constructs a parameter from a set of properties. The properties map is given unchanged to the
     * {@linkplain AbstractIdentifiedObject#AbstractIdentifiedObject(Map) super-class constructor}.
     *
     * @param properties Set of properties. Should contains at least {@code "name"}.
     * @param minimumOccurs The {@linkplain #getMinimumOccurs minimum number of times}
     *        that values for this parameter group or parameter are required.
     * @param maximumOccurs The {@linkplain #getMaximumOccurs maximum number of times} that values
     *        for this parameter group or parameter are required. This value is used in order to
     *        check the range. For {@link org.opengis.parameter.ParameterValue}, it should always
     *        be 1.
     */
    protected AbstractParameterDescriptor(final Map<String,?> properties,
                                          final int minimumOccurs,
                                          final int maximumOccurs)
    {
        super(properties);
        this.minimumOccurs = minimumOccurs;
        if (minimumOccurs < 0  ||  maximumOccurs < minimumOccurs) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.BAD_RANGE_$2,
                        minimumOccurs, maximumOccurs));
        }
    }

    /**
     * Creates a new instance of {@linkplain AbstractParameter parameter value or group} initialized
     * with the {@linkplain DefaultParameterDescriptor#getDefaultValue default value(s)}.
     * The {@linkplain AbstractParameter#getDescriptor parameter value descriptor} for the
     * created parameter value(s) will be {@code this} object.
     * <p>
     * Example implementation:
     * <pre>
     * <b>return</b> new {@linkplain Parameter}(this);
     * </pre>
     */
    public abstract GeneralParameterValue createValue();

    /**
     * The minimum number of times that values for this parameter group or
     * parameter are required. The default value is one. A value of 0 means
     * an optional parameter.
     *
     * @see #getMaximumOccurs
     */
    public int getMinimumOccurs() {
        return minimumOccurs;
    }

    /**
     * The maximum number of times that values for this parameter group or parameter
     * can be included. For a {@linkplain DefaultParameterDescriptor single parameter},
     * the value is always 1. For a {@linkplain DefaultParameterDescriptorGroup parameter group},
     * it may vary.
     *
     * @see #getMinimumOccurs
     */
    public abstract int getMaximumOccurs();

    /**
     * Compares the specified object with this parameter for equality.
     *
     * @param  object The object to compare to {@code this}.
     * @param  compareMetadata {@code true} for performing a strict comparaison, or
     *         {@code false} for comparing only properties relevant to transformations.
     * @return {@code true} if both objects are equal.
     */
    @Override
    public boolean equals(final AbstractIdentifiedObject object, final boolean compareMetadata) {
        if (super.equals(object, compareMetadata)) {
            final AbstractParameterDescriptor that = (AbstractParameterDescriptor) object;
            return this.minimumOccurs == that.minimumOccurs;
        }
        return false;
    }

    /**
     * Returns a hash value for this parameter.
     *
     * @return The hash code value. This value doesn't need to be the same
     *         in past or future versions of this class.
     */
    @Override
    public int hashCode() {
        return (int)serialVersionUID ^ minimumOccurs;
    }

    /**
     * Format the inner part of a
     * <A HREF="http://geoapi.sourceforge.net/snapshot/javadoc/org/opengis/referencing/doc-files/WKT.html"><cite>Well
     * Known Text</cite> (WKT)</A> element. Note that WKT is not yet defined for parameter descriptor.
     * Current implementation print only the name.
     *
     * @param  formatter The formatter to use.
     * @return The WKT element name, which is "PARAMETER"
     */
    @Override
    protected String formatWKT(final Formatter formatter) {
        formatter.setInvalidWKT(GeneralParameterDescriptor.class);
        return "PARAMETER";
    }
}
