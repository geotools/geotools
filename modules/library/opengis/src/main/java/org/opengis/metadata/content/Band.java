/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.metadata.content;

import javax.measure.quantity.Length;
import javax.measure.unit.Unit;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Range of wavelengths in the electromagnetic spectrum.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @author  Cory Horner (Refractions Research)
 * @since   GeoAPI 2.0
 */
@UML(identifier="MD_Band", specification=ISO_19115)
public interface Band extends RangeDimension {
    /**
     * Longest wavelength that the sensor is capable of collecting within a designated band.
     * Returns {@code null} if unspecified.
     *
     * @return Longest wavelength that the sensor is capable of collecting within a designated band,
     *         or {@code null}.
     */
    @UML(identifier="maxValue", obligation=OPTIONAL, specification=ISO_19115)
    Double getMaxValue();

    /**
     * Shortest wavelength that the sensor is capable of collecting within a designated band.
     * Returns {@code null} if unspecified.
     *
     * @return Shortest wavelength that the sensor is capable of collecting within a designated band,
     *         or {@code null}.
     */
    @UML(identifier="minValue", obligation=OPTIONAL, specification=ISO_19115)
    Double getMinValue();

    /**
     * Units in which sensor wavelengths are expressed. Should be non-null if
     * {@linkplain #getMinValue min value} or {@linkplain #getMaxValue max value}
     * are provided.
     *
     * @return Units in which sensor wavelengths are expressed, or {@code null}.
     */
    @UML(identifier="units", obligation=CONDITIONAL, specification=ISO_19115)
    Unit<Length> getUnits();

    /**
     * Wavelength at which the response is the highest.
     * Returns {@code null} if unspecified.
     *
     * @return Wavelength at which the response is the highest, or {@code null}.
     */
    @UML(identifier="peakResponse", obligation=OPTIONAL, specification=ISO_19115)
    Double getPeakResponse();

    /**
     * Maximum number of significant bits in the uncompressed representation for the value
     * in each band of each pixel.
     * Returns {@code null} if unspecified.
     *
     * @return Maximum number of significant bits in the uncompressed representation, or {@code null}.
     */
    @UML(identifier="bitsPerValue", obligation=OPTIONAL, specification=ISO_19115)
    Integer getBitsPerValue();

    /**
     * Number of discrete numerical values in the grid data.
     * Returns {@code null} if unspecified.
     *
     * @return Number of discrete numerical values in the grid data, or {@code null}.
     */
    @UML(identifier="toneGradation", obligation=OPTIONAL, specification=ISO_19115)
    Integer getToneGradation();

    /**
     * Scale factor which has been applied to the cell value.
     * Returns {@code null} if unspecified.
     *
     * @return Scale factor which has been applied to the cell value, or {@code null}.
     */
    @UML(identifier="scaleFactor", obligation=OPTIONAL, specification=ISO_19115)
    Double getScaleFactor();

    /**
     * The physical value corresponding to a cell value of zero.
     * Returns {@code null} if unspecified.
     *
     * @return The physical value corresponding to a cell value of zero, or {@code null}.
     */
    @UML(identifier="offset", obligation=OPTIONAL, specification=ISO_19115)
    Double getOffset();
}
