/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011-2015, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.coverage;

import javax.measure.Unit;
import org.geotools.api.util.InternationalString;

/**
 * Contains information for an individual sample dimension of {@linkplain Coverage coverage}. This
 * interface is applicable to any coverage type. For {@linkplain
 * org.geotools.api.coverage.grid.GridCoverage grid coverages}, the sample dimension refers to an
 * individual band.
 *
 * @version <A HREF="http://www.opengis.org/docs/01-004.pdf">Grid Coverage specification 1.0</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
public interface SampleDimension {
    /**
     * Sample dimension title or description. This string may be null or empty if no description is
     * present.
     *
     * @return A description for this sample dimension.
     */
    InternationalString getDescription();

    /**
     * A code value indicating grid value data type. This will also indicate the number of bits for
     * the data type.
     *
     * @return A code value indicating grid value data type.
     */
    SampleDimensionType getSampleDimensionType();

    /**
     * Sequence of category names for the values contained in a sample dimension. This allows for
     * names to be assigned to numerical values. The first entry in the sequence relates to a cell
     * value of zero. For grid coverages, category names are only valid for a classified grid data.
     *
     * <p>For example:<br>
     *
     * <UL>
     *   <li>0 Background
     *   <li>1 Water
     *   <li>2 Forest
     *   <li>3 Urban
     * </UL>
     *
     * Note: If no category names exist, an empty sequence is returned.
     *
     * @return The category names.
     */
    InternationalString[] getCategoryNames();

    /**
     * Values to indicate no data values for the sample dimension. For low precision sample
     * dimensions, this will often be no data values.
     *
     * @return The values to indicate no data values for the sample dimension.
     * @see #getMinimumValue
     * @see #getMaximumValue
     */
    double[] getNoDataValues();

    /**
     * The minimum value occurring in the sample dimension. If this value is not available, this
     * value can be determined from the {@link
     * org.geotools.api.coverage.processing.GridAnalysis#getMinValue} operation. This value can be
     * empty if this value is not provided by the implementation.
     *
     * @return The minimum value occurring in the sample dimension.
     * @see #getMaximumValue
     * @see #getNoDataValues
     */
    double getMinimumValue();

    /**
     * The maximum value occurring in the sample dimension. If this value is not available, this
     * value can be determined from the {@link
     * org.geotools.api.coverage.processing.GridAnalysis#getMaxValue} operation. This value can be
     * empty if this value is not provided by the implementation.
     *
     * @return The maximum value occurring in the sample dimension.
     * @see #getMinimumValue
     * @see #getNoDataValues
     */
    double getMaximumValue();

    /**
     * The unit information for this sample dimension. This interface typically is provided with
     * grid coverages which represent digital elevation data. This value will be {@code null} if no
     * unit information is available.
     *
     * @return The unit information for this sample dimension.
     */
    Unit<?> getUnits();

    /**
     * Offset is the value to add to grid values for this sample dimension. This attribute is
     * typically used when the sample dimension represents elevation data. The default for this
     * value is 0.
     *
     * @return The offset.
     * @see #getScale
     */
    double getOffset();

    /**
     * Scale is the value which is multiplied to grid values for this sample dimension. This
     * attribute is typically used when the sample dimension represents elevation data. The default
     * for this value is 1.
     *
     * @return The scale factor.
     * @see #getOffset
     */
    double getScale();
}
