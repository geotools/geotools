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

import static org.geotools.api.annotation.Obligation.OPTIONAL;
import static org.geotools.api.annotation.Specification.ISO_19115;

import java.util.Collection;
import org.geotools.api.annotation.UML;
import org.geotools.api.util.InternationalString;

/**
 * Description of the computer language construct that specifies the representation of data objects
 * in a record, file, message, storage device or transmission channel.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
public interface Format {
    /**
     * Name of the data transfer format(s).
     *
     * @return Name of the data transfer format(s).
     */
    InternationalString getName();

    /**
     * Version of the format (date, number, <cite>etc.</cite>).
     *
     * @return Version of the format.
     */
    InternationalString getVersion();

    /**
     * Amendment number of the format version.
     *
     * @return Amendment number of the format version, or {@code null}.
     */
    InternationalString getAmendmentNumber();

    /**
     * Name of a subset, profile, or product specification of the format.
     *
     * @return Name of a subset, profile, or product specification of the format, or {@code null}.
     */
    InternationalString getSpecification();

    /**
     * Recommendations of algorithms or processes that can be applied to read or expand resources to
     * which compression techniques have been applied.
     *
     * @return Processes that can be applied to read resources to which compression techniques have
     *     been applied, or {@code null}.
     */
    @UML(
            identifier = "fileDecompressionTechnique",
            obligation = OPTIONAL,
            specification = ISO_19115)
    InternationalString getFileDecompressionTechnique();

    /**
     * Provides information about the distributor's format.
     *
     * @return Information about the distributor's format.
     */
    Collection<? extends Distributor> getFormatDistributors();
}
