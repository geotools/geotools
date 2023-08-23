/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.metadata.distribution;

import java.util.Collection;
import javax.measure.Unit;
import org.geotools.api.util.InternationalString;

/**
 * Information about the media on which the resource can be distributed.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
public interface Medium {
    /**
     * Name of the medium on which the resource can be received.
     *
     * @return Name of the medium, or {@code null}.
     */
    MediumName getName();

    /**
     * Density at which the data is recorded. The numbers should be greater than zero.
     *
     * @return Density at which the data is recorded, or {@code null}.
     */
    Collection<Double> getDensities();

    /**
     * Units of measure for the recording density.
     *
     * @return Units of measure for the recording density, or {@code null}.
     */
    Unit<?> getDensityUnits();

    /**
     * Number of items in the media identified. Returns {@code null} if unknown.
     *
     * @return Number of items in the media identified, or {@code null}.
     */
    Integer getVolumes();

    /**
     * Method used to write to the medium.
     *
     * @return Method used to write to the medium, or {@code null}.
     */
    Collection<MediumFormat> getMediumFormats();

    /**
     * Description of other limitations or requirements for using the medium.
     *
     * @return Description of other limitations for using the medium, or {@code null}.
     */
    InternationalString getMediumNote();
}
