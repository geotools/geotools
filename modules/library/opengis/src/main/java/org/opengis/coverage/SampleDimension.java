/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011-2015, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.coverage;

import static org.opengis.annotation.Obligation.MANDATORY;
import static org.opengis.annotation.Specification.OGC_01004;

import javax.measure.Unit;
import org.opengis.annotation.UML;
import org.opengis.util.InternationalString;

/**
 * Contains information for an individual sample dimension of {@linkplain Coverage coverage}. This
 * interface is applicable to any coverage type. For {@linkplain
 * org.opengis.coverage.grid.GridCoverage grid coverages}, the sample dimension refers to an
 * individual band.
 *
 * @source $URL$
 * @version <A HREF="http://www.opengis.org/docs/01-004.pdf">Grid Coverage specification 1.0</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
@UML(identifier = "CV_SampleDimension", specification = OGC_01004)
public interface SampleDimension {
    /**
     * Sample dimension title or description. This string may be null or empty if no description is
     * present.
     *
     * @return A description for this sample dimension.
     */
    @UML(identifier = "description", obligation = MANDATORY, specification = OGC_01004)
    InternationalString getDescription();

    /**
     * A code value indicating grid value data type. This will also indicate the number of bits for
     * the data type.
     *
     * @return A code value indicating grid value data type.
     */
    @UML(identifier = "sampleDimensionType", obligation = MANDATORY, specification = OGC_01004)
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
    @UML(identifier = "categoryNames", obligation = MANDATORY, specification = OGC_01004)
    InternationalString[] getCategoryNames();

    /**
     * Color interpretation of the sample dimension. A sample dimension can be an index into a color
     * palette or be a color model component. If the sample dimension is not assigned a color
     * interpretation the value is {@link ColorInterpretation#UNDEFINED UNDEFINED}.
     *
     * @return The color interpretation of the sample dimension.
     * @deprecated No replacement.
     */
    @UML(identifier = "colorInterpretation", obligation = MANDATORY, specification = OGC_01004)
    ColorInterpretation getColorInterpretation();

    /**
     * Indicates the type of color palette entry for sample dimensions which have a palette. If a
     * sample dimension has a palette, the color interpretation must be {@link
     * ColorInterpretation#GRAY_INDEX GRAY_INDEX} or {@link ColorInterpretation#PALETTE_INDEX
     * PALETTE_INDEX}. A palette entry type can be Gray, RGB, CMYK or HLS.
     *
     * @return The type of color palette entry for sample dimensions which have a palette.
     * @deprecated No replacement.
     */
    @UML(identifier = "paletteInterpretation", obligation = MANDATORY, specification = OGC_01004)
    PaletteInterpretation getPaletteInterpretation();

    /**
     * Color palette associated with the sample dimension. A color palette can have any number of
     * colors. See palette interpretation for meaning of the palette entries. If the grid coverage
     * has no color palette, {@code null} will be returned.
     *
     * @return The color palette associated with the sample dimension.
     * @see #getPaletteInterpretation
     * @see #getColorInterpretation
     * @see java.awt.image.IndexColorModel
     * @deprecated No replacement.
     */
    @UML(identifier = "palette", obligation = MANDATORY, specification = OGC_01004)
    int[][] getPalette();

    /**
     * Values to indicate no data values for the sample dimension. For low precision sample
     * dimensions, this will often be no data values.
     *
     * @return The values to indicate no data values for the sample dimension.
     * @see #getMinimumValue
     * @see #getMaximumValue
     */
    @UML(identifier = "noDataValue", obligation = MANDATORY, specification = OGC_01004)
    double[] getNoDataValues();

    /**
     * The minimum value occurring in the sample dimension. If this value is not available, this
     * value can be determined from the {@link
     * org.opengis.coverage.processing.GridAnalysis#getMinValue} operation. This value can be empty
     * if this value is not provided by the implementation.
     *
     * @return The minimum value occurring in the sample dimension.
     * @see #getMaximumValue
     * @see #getNoDataValues
     */
    @UML(identifier = "minimumValue", obligation = MANDATORY, specification = OGC_01004)
    double getMinimumValue();

    /**
     * The maximum value occurring in the sample dimension. If this value is not available, this
     * value can be determined from the {@link
     * org.opengis.coverage.processing.GridAnalysis#getMaxValue} operation. This value can be empty
     * if this value is not provided by the implementation.
     *
     * @return The maximum value occurring in the sample dimension.
     * @see #getMinimumValue
     * @see #getNoDataValues
     */
    @UML(identifier = "maximumValue", obligation = MANDATORY, specification = OGC_01004)
    double getMaximumValue();

    /**
     * The unit information for this sample dimension. This interface typically is provided with
     * grid coverages which represent digital elevation data. This value will be {@code null} if no
     * unit information is available.
     *
     * @return The unit information for this sample dimension.
     */
    @UML(identifier = "units", obligation = MANDATORY, specification = OGC_01004)
    Unit<?> getUnits();

    /**
     * Offset is the value to add to grid values for this sample dimension. This attribute is
     * typically used when the sample dimension represents elevation data. The default for this
     * value is 0.
     *
     * @return The offset.
     * @see #getScale
     */
    @UML(identifier = "offset", obligation = MANDATORY, specification = OGC_01004)
    double getOffset();

    /**
     * Scale is the value which is multiplied to grid values for this sample dimension. This
     * attribute is typically used when the sample dimension represents elevation data. The default
     * for this value is 1.
     *
     * @return The scale factor.
     * @see #getOffset
     */
    @UML(identifier = "scale", obligation = MANDATORY, specification = OGC_01004)
    double getScale();
}
